package mx.pagos.admc.service.purchasingflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.engineer.utils.mail.structures.EmailContent;
import mx.engineer.utils.pdf.PDFUtils;
import mx.engineer.utils.word.WordComparationException;
import mx.pagos.admc.contracts.business.CommentsBusiness;
import mx.pagos.admc.contracts.business.NotificationBusiness;
import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.business.RequisitionVersionBusiness;
import mx.pagos.admc.contracts.structures.ApprovalArea;
import mx.pagos.admc.contracts.structures.Attachment;
import mx.pagos.admc.contracts.structures.Clause;
import mx.pagos.admc.contracts.structures.Comment;
import mx.pagos.admc.contracts.structures.ComparationWord;
import mx.pagos.admc.contracts.structures.ContractCancellationComment;
import mx.pagos.admc.contracts.structures.ContractDetail;
import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.FileUploadInfo;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.FinantialEntityWitness;
import mx.pagos.admc.contracts.structures.Instrument;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.Notification;
import mx.pagos.admc.contracts.structures.Obligation;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionAngular;
import mx.pagos.admc.contracts.structures.RequisitionComplete;
import mx.pagos.admc.contracts.structures.RequisitionDocuSign;
import mx.pagos.admc.contracts.structures.RequisitionDraftPart2;
import mx.pagos.admc.contracts.structures.RequisitionVersion;
import mx.pagos.admc.contracts.structures.RequisitionsPartFour;
import mx.pagos.admc.contracts.structures.RequisitionsPartOneAndTwo;
import mx.pagos.admc.contracts.structures.RequisitionsPartThree;
import mx.pagos.admc.contracts.structures.Scaling;
import mx.pagos.admc.contracts.structures.SupplierPersonByRequisition;
import mx.pagos.admc.contracts.structures.TrayFilter;
import mx.pagos.admc.contracts.structures.TrayRequisition;
import mx.pagos.admc.contracts.structures.UserInProgressRequisition;
import mx.pagos.admc.contracts.structures.UserInProgressRequisitionFilter;
import mx.pagos.admc.contracts.structures.dtos.RequisitionDTO;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.business.EmailsBusiness;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.NotificacionTypeEnum;
import mx.pagos.admc.enums.ScalingTypeEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.Holidays;
import mx.pagos.admc.util.shared.ParametersHolder;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.document.version.business.DocumentVersionBusiness;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.business.ProfilesBusiness;
import mx.pagos.security.business.UsersBusiness;
import mx.pagos.security.structures.User;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.DirUtil;
import mx.pagos.util.LoggingUtils;

@Controller
public class PurchasingRequisitionService {


	private static final String FILE_NO_LONGER_EXISTS_MESSAGE = "El archivo solicitado ha dejado de existir";

	private static final String THE_REQUISITION = "la Solicitud ";

	private static final Logger LOG = Logger.getLogger(PurchasingRequisitionService.class);

	private static final String REQUISITION_SENDED_MESSAGE = "Envío de la Solicitud ";
	private static final String DOCUMENT_PATH = "documentPath";
	private static final String ID_DOCUMENT = "idDocument";
	private static final String ID_REQUISITION = "idRequisition";
	private static final String SET_COLOR_MARK = "setColorMark";
	private static final String SCAPED_QUOTES = "\"";
	private static final String PDF = "pdf";
	private static final String MESSAGE_RETRIEVING_BASE_PATH_ERROR =
            "Hubo un problema al recuperar la ruta base para guardar los archivos";

	@Autowired
	private RequisitionBusiness requisitionBusiness;

	@Autowired
	private CommentsBusiness commentsBusiness;

	@Autowired
	private UserSession session;

	@Autowired
	private DocumentVersionBusiness documentVersionBusiness;

	@Autowired
	private RequisitionVersionBusiness requisitionVersionBusiness;

	@Autowired
	private BinnacleBusiness binnacleBusiness;

	@Autowired
	private ConfigurationsBusiness configuration;

	@Autowired
	private NotificationBusiness notificationBusiness;

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private ProfilesBusiness profilesBusiness;

	@Autowired
	private ConfigurationsBusiness configurationBusiness;

	@Autowired
	Configurable configurable;

	@Autowired
	private EmailsBusiness emailsBusiness;

	@Autowired
	private UsersBusiness usersBusiness;

	@Autowired
	    private UserSession userSession;

	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_IN_PROGRESS, method = RequestMethod.POST)
	@ResponseBody
	public final Requisition saveOrUpdateRequisition(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			LOG.info("GUARDA O ACTULIZA REQUISICIÓN  ID ::"+requisition.getIdRequisition());
			requisition.setIdFlow(this.session.getIdFlow());
			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			this.requisitionBusiness.saveRequisitionInProgress(requisition);
			LOG.info("### GUARDADO EXITOSO");
			return requisition;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		final Requisition returnRequisition = new Requisition();
		returnRequisition.setIdRequisition(0);
		return returnRequisition;
	}

	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_IN_PROGRESS_PART_1_AND_2, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionPart1And2(
			@RequestBody final RequisitionsPartOneAndTwo requisition, final HttpServletResponse response) {
		
		LOG.info(" GUARDANDO PASO 1 Y 2  -  Requisición :: "+requisition.getIdRequisition());
		
		try {			
			requisition.setIdFlow(this.session.getIdFlow());			
			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			requisition.setIdApplicant(this.session.getUsuario().getIdUser());
			this.requisitionBusiness.saveRequisitionInProgressPart1And2(requisition);

			LOG.info(" ### PASO 1 Y 2 GUARDADOS CON EXITO");
			return requisition.getIdRequisition();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		final RequisitionsPartOneAndTwo returnRequisition = new RequisitionsPartOneAndTwo();
		returnRequisition.setIdRequisition(0);
		return 0;
	}
	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_DRATF_PART2, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionDraftPart2(
			@RequestBody final RequisitionDraftPart2 requisition, final HttpServletResponse response) {
		
		LOG.info("\n=======================================================\nBANDEJA (Solicitud de contrato) - GUARDADO/ACTUALIZACIÓN PASO 1 Y 2 "
				+ "\n Requisición ("+requisition.getIdRequisition()+")");
		try {
			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			this.requisitionBusiness.saveRequisitionInDraftPart2(requisition);
			LOG.info("GUARDADO/ACTUALIZACIÓN");
			return requisition.getIdRequisition();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		final RequisitionsPartOneAndTwo returnRequisition = new RequisitionsPartOneAndTwo();
		returnRequisition.setIdRequisition(0);
		return 0;
	}
	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_DRATF_PROEM, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionDraftProem(
			@RequestBody final Requisition requisition, final HttpServletResponse response) {
		
		LOG.info("\n=======================================================\nBANDEJA (Solicitud de contrato) - GUARDADO/ACTUALIZACIÓN PASO (PROEMIO) "
				+ "\n Requisición ("+requisition.getIdRequisition()+")");
		try {
			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			this.requisitionBusiness.saveRequisitionInDraftProem(requisition);
			LOG.info("GUARADO/ACTULIZACIÓN DEL PROEMIO - BANDEJA (Solicitud de contrato) - EXITOSO!!"
					+ "\n Requisición ("+requisition.getIdRequisition()+")");
			return requisition.getIdRequisition();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		final RequisitionsPartOneAndTwo returnRequisition = new RequisitionsPartOneAndTwo();
		returnRequisition.setIdRequisition(0);
		return 0;
	}
	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_DRATF_CLAUSULES, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionDraftClausules(
			@RequestBody final Requisition requisition, final HttpServletResponse response) {
		
		LOG.info("\n========================================================\n ACTULIZACIÓN/GUARDADO DE LA BANDEJA SOLICITUD DE CONTRATO \n"
				+ "Paso:: Cláusulas \n"
				+ "Requisición:: "+requisition.getIdRequisition()
				+ "\nUsuario :: "+this.session.getUsuario().getIdUser());		
		
		try {
			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			this.requisitionBusiness.saveRequisitionInDraftClausules(requisition);
			LOG.info("\n=====================================\nACTULIZACIÓN/GUARDADO DE LA BANDEJA SOLICITUD DE CONTRATO EXITOSO!!\n"
					+ "Paso:: Cláusulas ");
			
			return requisition.getIdRequisition();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		final RequisitionsPartOneAndTwo returnRequisition = new RequisitionsPartOneAndTwo();
		returnRequisition.setIdRequisition(0);
		return 0;
	}
	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_DRATF_PROPERTY, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionDraftPoperty(
			@RequestBody final Requisition requisition, final HttpServletResponse response) {
		try {
			LOG.info("Se guardarán los datos de la plantilla de la solicitud en borrador");

			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());

			this.requisitionBusiness.saveRequisitionInDraftProperty(requisition);

			LOG.info("Datos guardados de la solicitud en progreso. Id: " + requisition.getIdRequisition());
			return requisition.getIdRequisition();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		final RequisitionsPartOneAndTwo returnRequisition = new RequisitionsPartOneAndTwo();
		returnRequisition.setIdRequisition(0);
		return 0;
	}
	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_IN_PROGRESS_PART_5, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionPart5(@RequestBody final Instrument requisition,
			final HttpServletResponse response) {
		
		LOG.info("\n========================================================\n ACTULIZACIÓN/GUARDADO DE LA BANDEJA SOLICITUD DE CONTRATO \n"
				+ "Paso:: Declaraciones de la entidad \n"
				+ "Requisición :: "+requisition.getIdRequisition());
		
		try {
			this.requisitionBusiness.saveRequisitionInProgressPart5(requisition);
			LOG.info("\n=====================================\nGUARDADO/ACTULIZACIÓN DE LA BANDEJA (Declaraciones de la entidad) EXITOSO!!\n"
					+ "Requisición :: "+requisition.getIdRequisition());
			return requisition.getIdRequisition();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_IN_PROGRESS_PART_6, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionPart6(@RequestBody final Attachment requisition,
			final HttpServletResponse response) {
		try {
			LOG.info("Se guardarán los datos (Attachment) de la solicitud en progreso  Paso 6");
			this.requisitionBusiness.saveRequisitionInProgressPart6(requisition);
			LOG.info("Datos guardados (Attachment) de la solicitud en progreso. Id: " + requisition.getIdRequisition());
			return requisition.getIdRequisition();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return 0;
	}	

	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_IN_PROGRESS_PART_7, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionPart6(@RequestBody final Clause requisition,
			final HttpServletResponse response) {
		
		LOG.info("\n*************************************** \n GUARDANDO PASO -CLAUSULAS- -  Requisición :: "+requisition.getIdRequisition());
		
		try {
			this.requisitionBusiness.saveRequisitionInProgressPart7(requisition);
			LOG.info("\n##### PASO (CLAUSULAS) GUARDADO CON EXITO Requisición("+requisition.getIdRequisition()+") \n ****************************************");
			return requisition.getIdRequisition();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.error("\n##### ERROR - PASO (CLAUSULAS) ERROR NO SE GUARDARON LOS DATOS- Requisición("+requisition.getIdRequisition()+") \n ****************************************");
		}
		return 0;
	}

	@RequestMapping(value = UrlConstants.REQUISITION_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public final RequisitionComplete obtenerDetalleSolicitud(@RequestBody final Integer requisition,
			final HttpServletResponse response) {
		
		LOG.info("\n\n========================================\n CONSULTA DE SOLITUD :: Solicitudes por enviar ("+requisition+") \n==========================================");
		
		RequisitionComplete requisitionComplete =  new RequisitionComplete();
		requisitionComplete.setIdRequisition(requisition);	   
		try {		
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de la Solicitud por Id " + requisitionComplete.getIdRequisition(), this.session,
					LogCategoryEnum.QUERY));
			requisitionComplete = this.requisitionBusiness.obtenerDetalleSolicitud(requisition);
			LOG.info("============ obtenerDetalleSolicitud Exitoso: " + requisition);
			return requisitionComplete;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return requisitionComplete;
	}	

	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_IN_PROGRESS_PART_3, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionPart3(@RequestBody final RequisitionsPartThree requisition, final HttpServletResponse response) {
		
		LOG.info("\n*******************************\nGUARDANDO PASO 3  -  Requisición :: "+requisition.getIdRequisition());
		
		try {
			this.requisitionBusiness.saveRequisitionInProgressPart3(requisition);
			LOG.info("### PASO 3 GUARDADO CON EXITO");
			return requisition.getIdSupplier();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_IN_PROGRESS_PART_4, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdateRequisitionPart4(@RequestBody final RequisitionsPartFour requisition, final HttpServletResponse response) {
		try {
			LOG.info("Se guardarán los datos de la solicitud en progreso Parte 4");			
			this.requisitionBusiness.saveRequisitionInProgressPart4(requisition);
			LOG.info("Datos guardados de la solicitud en progreso. Id: " + requisition.getIdRequisition());
			return requisition.getIdRequisition();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = UrlConstants.FIND_WHOLE_REQUISITION, method = RequestMethod.POST)
	@ResponseBody
	public final Requisition findWholeRequisition(@RequestBody final Integer idRequisition,
			final HttpServletResponse response) {
		try {
			return this.requisitionBusiness.findWholeRequisitionById(idRequisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		final Requisition returnRequisition = new Requisition();
		returnRequisition.setIdRequisition(0);
		return returnRequisition;
	}

	@RequestMapping(value = UrlConstants.SEND_REQUISITION, method = RequestMethod.POST)
	@ResponseBody
	public final Requisition sendRequisition(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			LOG.info("Se guardarán los datos de la solicitud -> Crea Solicitud");
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			requisition.setIdFlow(this.session.getIdFlow());
			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			this.requisitionBusiness.sendRequisition(requisition);
			if (requisition.getComment() != null)
				this.commentsBusiness.saveOrUpdate(this.createComment(requisition, CommentType.RESEND));
			LOG.info("Datos guardados de la solicitud. Id: " + requisition.getIdRequisition());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Generación de Solicitud de Contrato " + "número " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.SAVE));
			return requisition;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		final Requisition returnRequisition = new Requisition();
		returnRequisition.setIdRequisition(0);
		return returnRequisition;
	}

	@RequestMapping(value = UrlConstants.CHANGE_REQUISITION_ESTATUS, method = RequestMethod.POST)
	@ResponseBody
	public final void changeRequisitionStatus(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.changeRequisitionStatus(requisition.getIdRequisition(), requisition.getStatus());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Cambio de estatus de la Solicitud " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_REQUISITION_ANGULAR, method = RequestMethod.POST)
	@ResponseBody
	public final RequisitionAngular sendRequisitionAngular(@RequestBody final RequisitionAngular requisition,
			final HttpServletResponse response) {
		
		LOG.info("\n======================================================================= \n INICIA PROCESO DE ENVIO DE SOLICITUD");
		
		try {
			requisition.getFlow().setIdFlow(this.session.getIdFlow());
			requisition.setIdFlow(this.session.getIdFlow());
			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			
			LOG.info("USUARIO QUE ENVÍA SOLICITUD :: "+this.session.getUsuario().getName()+"-"+this.session.getUsuario().getIdUser());
			LOG.info("PASO :: "+requisition.getFlow().getFactoryName());
			
			this.requisitionBusiness.sendRequisition(requisition);
			if (requisition.getComment() != null) {
				this.commentsBusiness.saveOrUpdate(this.createComment(CommentType.RESEND, FlowPurchasingEnum.IN_PROGRESS,
						requisition.getIdRequisition(), 
						requisition.getComment()!= null ? requisition.getComment().getCommentText() : ""));
			}
			
			LOG.info("SE ENVÍO EXITOSAMENTE,  Solicitud ::"+requisition.getIdRequisition());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Generación de Solicitud de Contrato " + "número " + 
					requisition.getIdRequisition(), this.session, LogCategoryEnum.SAVE));

			LOG.info("\n======================================================================= \n TERMINA PROCESO DE ENVIO DE SOLICITUD"
			+"\n=======================================================================");
			return requisition;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			
			final RequisitionAngular returnRequisition = new RequisitionAngular();
			returnRequisition.setIdRequisition(0);
			return returnRequisition;
		}

	}
	
	@RequestMapping(value = UrlConstants.SEND_REQUISITION_ANGULAR_NOTIFICATION, method = RequestMethod.POST)
	@ResponseBody
	public final void notificationRequisitionAngular(@RequestBody final RequisitionAngular requisition,
			final HttpServletResponse response) {
		try {
			// Se crea la notificación para el usuario
			Notification notification = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.SEND_DRAFT_GENERATION, null, null);
			Notification notification2 = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.SEND_DRAFT_GENERATION_REMITE, null, this.session.getUsuario().getIdUser());

			if(notification!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
				this.emailsBusiness.sendEmailNotify(this.createEmailContent(requisition, NotificacionTypeEnum.CHANGE_STATUS_SUBJECTEMAIL, NotificacionTypeEnum.SEND_DRAFT_GENERATION_LWR), this.obtenerEmail(this.requisitionBusiness.getIdLawyerByIdRequisition(requisition.getIdRequisition())));
			}

			if(notification2!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification2);
				LOG.info("NOTIFICACIÓN 2  OK");
				this.emailsBusiness.sendEmailNotify(this.createEmailContent(requisition, NotificacionTypeEnum.CHANGE_STATUS_SUBJECTEMAIL, NotificacionTypeEnum.SEND_DRAFT_GENERATION_USR), this.obtenerEmail(this.session.getUsuario().getIdUser()));
			}

		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.CHANGE_REQUISITION_STATUS_TO_CANLCELLED, method = RequestMethod.POST)
	@ResponseBody
	public final void changeRequisitionStatusToCancelled(@RequestBody final Integer idRequisition,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.changeRequisitionStatusToCancelled(idRequisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Cancelación de la solicitud con el id: " + idRequisition, this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.REQUISITION_FIND_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final Requisition findById(@RequestBody final Requisition requisition, final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de la Solicitud por Id " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.QUERY));
			return this.requisitionBusiness.findById(requisition.getIdRequisition());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new Requisition();
	}
	@PostMapping(value = UrlConstants.DS_GET_USER)
	@ResponseBody
	public final DocumentDS getUser(@RequestBody final Requisition requisition)throws BusinessException {
		LOG.info("///////////Entro a DS_GET_USER METHOD");
		return this.requisitionBusiness.getUser(requisition.getIdRequisition());
//		Requisition requisitionComplete =  new Requisition();
//		try {
//		LOG.info("///////////Entro a DS_GET_USER TRY");
//			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
//					"Búsqueda de la Solicitud por Id " + requisition.getIdRequisition(), this.session,
//					LogCategoryEnum.QUERY));
//			requisitionComplete= this.requisitionBusiness.getUser(requisition.getIdRequisition());
//		} catch (BusinessException businessException) {
//			LOG.error("////////////// NO ENTRO A DS_GET_USER");
//			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
//			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
//		}
//		return requisitionComplete;
	}

	@RequestMapping(value = UrlConstants.REQUISITION_FIND_DRAFT_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final Requisition findDraftById(@RequestBody final Integer requisition, final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de la Solicitud por Id " + requisition, this.session,
					LogCategoryEnum.QUERY));
			return this.requisitionBusiness.findById(requisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new Requisition();
	}

	@RequestMapping(value = UrlConstants.REQUISITION_FIND_BY_ID_IN_PROGRESS, method = RequestMethod.POST)
	@ResponseBody
	public final Requisition findByIdInProgress(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de la Solicitud por Id " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.QUERY));
			return this.requisitionBusiness.findByIdInProgress(requisition.getIdRequisition());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new Requisition();
	}

	@RequestMapping(value = UrlConstants.REQUISITION_VERSION_FIND_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final Requisition findRequisitionVersionById(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de versión de Solicitud por Id " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.QUERY));
			return this.requisitionVersionBusiness.findRequisitionVersionById(requisition.getIdRequisition());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new Requisition();
	}

	@RequestMapping(value = UrlConstants.FIND_WHOLE_REQUISITION_VERSION_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final RequisitionComplete findWholeRequisitionVersionById(@RequestBody final RequisitionVersion version,
			final HttpServletResponse response) {
		RequisitionComplete requisitionComplete =  new RequisitionComplete();
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de toda la información de" + " versión de Solicitud por Id " + version.getIdRequisition(),
					this.session, LogCategoryEnum.QUERY));
			Requisition requisition = this.requisitionVersionBusiness.findWholeRequisitionVersionById(version);
			requisitionComplete = this.requisitionBusiness.mapearRequisition(requisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return requisitionComplete;
	}

	@RequestMapping(value = UrlConstants.FIND_SECOND_WHOLE_REQUISITION_VERSION_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final Requisition findSecondWholeRequisitionVersionById(@RequestBody final RequisitionVersion version,
			final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de toda la información de" + " versión de Solicitud por Id " + version.getIdRequisition(),
					this.session, LogCategoryEnum.QUERY));
			return this.requisitionVersionBusiness.findSecondWholeRequisitionVersionById(version);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new Requisition();
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITIONS_TO_CREATE_ONE_FROM_ANOTHER, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Integer> findRequisitionsToCreateOneFromAnother(
			@RequestBody final ConsultaList<Integer> vo, final HttpServletResponse response) {
		try {
			final ConsultaList<Integer> responseList = new ConsultaList<>();
			responseList.setList(this.requisitionBusiness.findRequisitionsToCreateOneFromAnother(vo.getParam1(),
					vo.getParam2(), vo.getParam3()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de solicitudes para la " + "creación de una a partir de otra", this.session,
					LogCategoryEnum.QUERY));
			return responseList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Integer> result = new ConsultaList<Integer>();
			result.setList(new ArrayList<Integer>());
			return result;
		}
	}
	@RequestMapping(value = UrlConstants.FIND_ALL_REQUISITIONS_GENERAL, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<RequisitionDTO> findAllRequisitions(
			@RequestBody RequisitionAngular vo, final HttpServletResponse response) {
		final ConsultaList<RequisitionDTO> responseList = new ConsultaList<>();
		 try {
			 if (this.session.getIdFlow() != null) {
//			 	final RequisitionDTO requisition = vo.getList().get(0);
//	            responseList.setList(this.requisitionBusiness.findAllRequisitions(requisition));
	            responseList.setList(this.requisitionBusiness.findAllRequisitions(vo));
//	            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Búsqueda de solicitudes para la "
//	                    + "creación de una a partir de otra", this.session, LogCategoryEnum.QUERY));
	            return responseList;
//	        } catch (BusinessException businessException) {
//	            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
//	            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
//	            final ConsultaList<RequisitionDTO> result = new ConsultaList<RequisitionDTO>();
//	            result.setList(new ArrayList<RequisitionDTO>());
//	            return result;
//	        }
			 }
		 } catch (Exception businessException) {
				response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
				response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
				final ConsultaList<RequisitionDTO> result = new ConsultaList<RequisitionDTO>();
	            result.setList(new ArrayList<RequisitionDTO>());
			}
		 return responseList;
	}

	@RequestMapping(value = UrlConstants.REQUISITION_FIND_BY_RECORDSTATUS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Requisition> findByRecordStatus(@RequestBody final ConsultaList<Requisition> requisition,
			final HttpServletResponse response) {
		try {
			if (this.session.getIdFlow() != null) {
				requisition.setList(this.requisitionBusiness.findByFlowPurchasingStatus(
						FlowPurchasingEnum.valueOf(requisition.getParam1()), this.session.getIdFlow()));
				this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
						"Búsqueda de la Solicitud por estatus" + requisition.getParam1(), this.session,
						LogCategoryEnum.QUERY));
			}
			return requisition;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Requisition> result = new ConsultaList<Requisition>();
			result.setList(new ArrayList<Requisition>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.VERIFY_CURRENT_DRAF, method = RequestMethod.POST)
	@ResponseBody
	public final void verifyDocument(@RequestBody final Integer idRequisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			final File document = new File(this.requisitionBusiness.findTemplate(idRequisition));
			if (!document.exists())
				throw new BusinessException(FILE_NO_LONGER_EXISTS_MESSAGE);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.VERIFY_DRAF_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public final String verifyDrafDocument(@RequestBody final Integer idRequisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			final File document = new File(this.requisitionBusiness.findTemplate(idRequisition));
			if (!document.exists())
				throw new BusinessException(FILE_NO_LONGER_EXISTS_MESSAGE);
			return "\""+  this.requisitionBusiness.returnDraftContractFileName(idRequisition)+"\"";
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}

	@RequestMapping(value = UrlConstants.VERIFY_CONTRACT_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public final String verifyContractDocument(@RequestBody final Integer idRequisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final File document = new File(this.requisitionBusiness.findTemplate(idRequisition));
			if (!document.exists()) {
				throw new BusinessException(FILE_NO_LONGER_EXISTS_MESSAGE);
			}
			String fileName = this.requisitionBusiness.returnContractFileName(idRequisition);
			return fileName!=null ?  "\""+ fileName+"\"" : null;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value = UrlConstants.DOWNLOAD_DRAFT_CONTRACT, method = RequestMethod.GET)
	public final void downloadDraftContract(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String draftPath = this.requisitionBusiness.draftContractPath(request.getParameter("draftName"));
			this.sendFileToDownloadAndDeleteFile(response, draftPath);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Descarga de borrador de la Solicitud",
					this.session, LogCategoryEnum.QUERY));
		} catch (BusinessException | IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.DOWNLOAD_DRAFT_REQUISITION, method = RequestMethod.GET)
	public final void downloadDraftRequisition(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final Integer idRequisition = Integer.valueOf(request.getParameter(ID_REQUISITION));
			final Boolean setColorMark = Boolean.valueOf(request.getParameter(SET_COLOR_MARK));
			final String draftPath = this.requisitionBusiness.downloadDraftRequisition(idRequisition, setColorMark)
					.getPath();
			this.sendFileToDownloadAndDeleteFile(response, draftPath);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Descaga de borrador de la Solicitud " + idRequisition, this.session, LogCategoryEnum.QUERY));
		} catch (BusinessException | IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.DOWNLOAD_CURRENT_VERSION, method = RequestMethod.GET)
	public final void downloadCurrentVersion(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final Integer templateIdDocument = Integer.valueOf(request.getParameter("templateIdDocument"));
			final String draftPath = this.documentVersionBusiness.findCurrentVersion(templateIdDocument)
					.getDocumentPath();
			this.sendFileToDownload(response, draftPath);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Descaga de borrador de la Version " + templateIdDocument, this.session, LogCategoryEnum.QUERY));
		} catch (BusinessException | IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	
	@RequestMapping(value = UrlConstants.DOWNLOAD_PREVIEW, method = RequestMethod.GET)
	public final void downloadPreview(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		try {			
			final File document = new File(this.requisitionBusiness.findTemplateByTypeDoc(Integer.parseInt(request.getParameter("idRequisition"))));
			// Validación de documentos.
			if (!document.exists()) {
				throw new BusinessException(FILE_NO_LONGER_EXISTS_MESSAGE);
			} 
			LOG.info("Path final " + document.getPath());
			// Descarga del archivo.
			this.sendFileToDownload(response, document.getPath());
			final File file = new File(document.getPath());
			if (file.exists())
			    file.delete();
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	private void setReponseData(final HttpServletResponse response, final String name, final File file) {
		final int buffer = 1024 * 100;
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment;filename=" + SCAPED_QUOTES + name + SCAPED_QUOTES);
		response.setContentLength(Long.valueOf(file.length()).intValue());
		response.setBufferSize(buffer);
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_FINANCIAL_ENTITY, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Integer> findRequisitionFinancialEntityByIdRequisition(
			@RequestBody final ConsultaList<Integer> parameters, final HttpServletResponse response) {
		try {
			final Integer idRequisition = parameters.getParam4();
			final ConsultaList<Integer> requisitionFinancialEntityList = new ConsultaList<>();
			requisitionFinancialEntityList
			.setList(this.requisitionBusiness.findRequisitionFinancialEntityByIdRequisition(idRequisition));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de Entidad de Solicitud " + idRequisition, this.session, LogCategoryEnum.QUERY));
			return requisitionFinancialEntityList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Integer> result = new ConsultaList<Integer>();
			result.setList(new ArrayList<Integer>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_FINANCIAL_ACTIVE_ENTITY, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<String> findRequisitionFinancialEntityActivateByIdRequisition(
			@RequestBody final ConsultaList<String> idRequisition, final HttpServletResponse response) {
		try {
			final ConsultaList<String> requisitionFinancialEntityList = new ConsultaList<>();
			requisitionFinancialEntityList.setList(this.requisitionBusiness
					.findRequisitionFinancialEntityActiveByIdRequisition(idRequisition.getParam4()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de Entidad Activa de" + " la Solicitud " + idRequisition.getParam4(), this.session,
					LogCategoryEnum.QUERY));
			return requisitionFinancialEntityList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<String> result = new ConsultaList<String>();
			result.setList(new ArrayList<String>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_FINANCIAL_ENTITY_WITNESS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<FinantialEntityWitness> findRequisitionFinancialEntityByIdRequisitionWitness(
			@RequestBody final ConsultaList<FinantialEntityWitness> idRequisition, final HttpServletResponse response) {
		try {
			final ConsultaList<FinantialEntityWitness> requisitionFinancialEntityList = new ConsultaList<>();
			requisitionFinancialEntityList.setList(this.requisitionBusiness
					.findRequisitionFinancialEntityByIdRequisitionWitnes(idRequisition.getParam4()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de Testigos de la Entidad" + " de la Solicitud " + idRequisition.getParam4(),
					this.session, LogCategoryEnum.QUERY));
			return requisitionFinancialEntityList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<FinantialEntityWitness> result = new ConsultaList<FinantialEntityWitness>();
			result.setList(new ArrayList<FinantialEntityWitness>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_AUTHORIZATION_DGAS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Integer> findRequisitionAuthorizationDgas(
			@RequestBody final ConsultaList<Integer> idRequisition, final HttpServletResponse response) {
		try {
			final ConsultaList<Integer> requisitionAuthorizationDgasList = new ConsultaList<>();
			requisitionAuthorizationDgasList
			.setList(this.requisitionBusiness.findRequisitionAuthorizationDgas(idRequisition.getParam4()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de DGA's de la Solicitud " + idRequisition.getParam4(), this.session,
					LogCategoryEnum.QUERY));
			return requisitionAuthorizationDgasList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Integer> result = new ConsultaList<Integer>();
			result.setList(new ArrayList<Integer>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_AUTHORIZATION_DGAS_ACTIVE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<String> findRequisitionAuthorizationDgasActive(
			@RequestBody final ConsultaList<Integer> idRequisition, final HttpServletResponse response) {
		try {
			final ConsultaList<String> requisitionAuthorizationDgasList = new ConsultaList<>();
			requisitionAuthorizationDgasList.setList(
					this.requisitionBusiness.findRequisitionAuthorizationDgasActive(idRequisition.getParam4()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de DGA's Activas de la Solicitud" + idRequisition.getParam4(), this.session,
					LogCategoryEnum.QUERY));
			return requisitionAuthorizationDgasList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<String> result = new ConsultaList<String>();
			result.setList(new ArrayList<String>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_APPROVAL_AREAS_NAME, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<String> findApprovalAreas(@RequestBody final Integer idRequisition,
			final HttpServletResponse response) {
		try {
			final ConsultaList<String> approvalAreasList = new ConsultaList<>();
			approvalAreasList.setList(this.requisitionBusiness.findApprovalAreas(idRequisition));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de Áreas aprobacion de la Solicitud" + idRequisition, this.session,
					LogCategoryEnum.QUERY));
			return approvalAreasList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<String> result = new ConsultaList<String>();
			result.setList(new ArrayList<String>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_LEGAL_REPRESENTATIVES, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<LegalRepresentative> findRequisitionLegalRepresentatives(
			@RequestBody final ConsultaList<LegalRepresentative> idRequisition, final HttpServletResponse response) {
		try {
			final ConsultaList<LegalRepresentative> requisitionLegalRepresentatives = new ConsultaList<LegalRepresentative>();
			requisitionLegalRepresentatives
			.setList(this.requisitionBusiness.findRequisitionLegalRepresentatives(idRequisition.getParam4()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de Representantes Legales de la Solicitud  " + idRequisition.getParam4(), this.session,
					LogCategoryEnum.QUERY));
			return requisitionLegalRepresentatives;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<LegalRepresentative> result = new ConsultaList<LegalRepresentative>();
			result.setList(new ArrayList<LegalRepresentative>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.REQUISITION_LEGAL_REPRESENTATIVES_ACTIVE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<String> findRequisitionLegalRepresentativesActive(
			@RequestBody final ConsultaList<String> idRequisition, final HttpServletResponse response) {
		try {
			final ConsultaList<String> requisitionLegalRepresentatives = new ConsultaList<String>();
			requisitionLegalRepresentatives.setList(
					this.requisitionBusiness.findRequisitionLegalRepresentativesActive(idRequisition.getParam4()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de Representantes Legales " + "Activos de la Solicitud " + idRequisition.getParam4(),
					this.session, LogCategoryEnum.QUERY));
			return requisitionLegalRepresentatives;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<String> result = new ConsultaList<String>();
			result.setList(new ArrayList<String>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_APPROVAL_AREAS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Integer> findRequisitionApprovalAreas(
			@RequestBody final ConsultaList<Integer> idRequisition, final HttpServletResponse response) {
		try {
			final ConsultaList<Integer> requisitionApprovalAreasList = new ConsultaList<>();
			requisitionApprovalAreasList
			.setList(this.requisitionBusiness.findRequisitionApprovalAreas(idRequisition.getParam4()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Busqueda de Áreas para Visto Bueno de la " + "Solicitud  " + idRequisition.getParam4(),
					this.session, LogCategoryEnum.QUERY));
			return requisitionApprovalAreasList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Integer> result = new ConsultaList<Integer>();
			result.setList(new ArrayList<Integer>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_APPROVAL_AREAS_ACTIVE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<String> findRequisitionApprovalAreasActive(
			@RequestBody final ConsultaList<Integer> idRequisition, final HttpServletResponse response) {
		try {
			final ConsultaList<String> requisitionApprovalAreasList = new ConsultaList<>();
			requisitionApprovalAreasList
			.setList(this.requisitionBusiness.findRequisitionApprovalAreasActive(idRequisition.getParam4()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Bsqueda de reas para Visto Bueno " + "Activas de la Solicitud " + idRequisition.getParam4(),
					this.session, LogCategoryEnum.QUERY));
			return requisitionApprovalAreasList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<String> result = new ConsultaList<String>();
			result.setList(new ArrayList<String>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.SEARCH_ADDED_USERS_TO_VOBO, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<User> searchAddedUsersToVoBo(@RequestBody final ConsultaList<User> parameters,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final ConsultaList<User> listResponse = new ConsultaList<User>();
			listResponse.setList(this.requisitionBusiness.findUsersToVoBo(parameters.getParam4()));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de Usuarios para Visto Bueno " + "de la Solicitud " + parameters.getParam4(),
					this.session, LogCategoryEnum.QUERY));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<User> result = new ConsultaList<User>();
			result.setList(new ArrayList<User>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.REJECT_TO_LAWYER, method = RequestMethod.POST)
	@ResponseBody
	public final void rejectRequisitionToLawyer(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(requisition, CommentType.REJECTION));
			this.requisitionBusiness.rejectRequisitionToLawyer(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Rechazo de la Solicitud en la Asignación" + " de abogado " + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_REQUISITION_TO_LAWYER, method = RequestMethod.POST)
	@ResponseBody
	public final void sendRequisitionToLawyer(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.requisitionBusiness.sendRequisitionToLawyer(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					REQUISITION_SENDED_MESSAGE + requisition.getIdRequisition()
					+ " al abogado y asignación automática en base carga",
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_REQUISITION_TO_LAWYER_ASSIGMENT, method = RequestMethod.POST)
	@ResponseBody
	public final void sendRequisitionToLawyerAssigment(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.requisitionBusiness.saveRequisitionLawyer(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					REQUISITION_SENDED_MESSAGE + requisition.getIdRequisition() + " y asignación manual del abogado ",
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_DOCUMENT_REVIEW_LAWYER, method = RequestMethod.POST)
	@ResponseBody
	public final void saveRequisitionDocumentReviewLawyer(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			this.requisitionBusiness.saveRequisitionDocumentReviewLawyer(requisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_REQUISITION_DOCUMENT_REVIEW_LAWYER, method = RequestMethod.POST)
	@ResponseBody
	public final void sendRequisitionDocumentReviewLawyer(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.requisitionBusiness.saveRequisitionAttatchments(requisition);
			this.binnacleBusiness
			.save(LoggingUtils.createBinnacleForLogging(
					"Envío de Solicitud " + requisition.getIdRequisition()
					+ " a revisión de documentos por el abogado ",
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.REJECT_REQUISITION_DOCUMENT_REVIEW_LAWYER, method = RequestMethod.POST)
	@ResponseBody
	public final void rejectRequisitionDocumentReviewLawyer(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(requisition, CommentType.REJECTION));
			this.requisitionBusiness.rejectRequisitionDocumentReviewLawyer(requisition);
			this.binnacleBusiness
			.save(LoggingUtils.createBinnacleForLogging(
					"Rechazo de Solicitud " + requisition.getIdRequisition()
					+ " al revisar los documentos por el abogado ",
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_TEMPLATE, method = RequestMethod.POST)
	@ResponseBody
	public final void saveRequisitionTemplate(@RequestBody final FileUploadInfo template,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			this.requisitionBusiness.saveTemplateIdDocument(template);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Guardado de plantilla de documento de" + THE_REQUISITION + template.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.DELETE_REQUISITION_TEMPLATE, method = RequestMethod.POST)
	@ResponseBody
	public final void deleteRequisitionTemplate(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			this.requisitionBusiness.deleteRequisitionTemplate(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Borrando de plantilla de documento de" + THE_REQUISITION + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SAVE_DRAFT_INFO, method = RequestMethod.POST)
	@ResponseBody
	public final void saveDraftInfo(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			LOG.info("Se guardarán los datos del borrador de la solicitud: " + requisition.getIdRequisition());
			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.requisitionBusiness.saveDraftInfo(requisition);
			LOG.info("Datos del borrador de la solicitud: " + requisition.getIdRequisition() + " guardados");
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Solicitud de cambio de borrador de la Solicitud " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_REVIEW_DRAFT, method = RequestMethod.POST)
	@ResponseBody
	public final void sendReviewDraft(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			LOG.info("Se envía a revisión el borrador de la solicitud: " + requisition.getIdRequisition());
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			if (requisition.getComment() != null) {
				this.commentsBusiness
				.saveOrUpdate(this.createComment(requisition, requisition.getComment().getCommentType()));
			}

			this.requisitionBusiness.sendReviewDraft(requisition);
			LOG.info("Revisión del borrador de la solicitud: " + requisition.getIdRequisition() + " enviada");
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Envío a Revisión de Borrador de la " + "Solicitud " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_REVIEW_DRAFT_ANGULAR, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean sendReviewDraftAngular(@RequestBody final RequisitionAngular requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			LOG.info("Se envía a revisión el borrador de la solicitud: " + requisition.getIdRequisition());
			requisition.getFlow().setIdFlow(this.session.getIdFlow());

			requisition.setUpdateRequisitionBy(this.session.getUsuario().getIdUser());
			if (requisition.getComment() != null) {
				this.commentsBusiness
				.saveOrUpdate(this.createComment(requisition.getComment().getCommentType(), FlowPurchasingEnum.DRAFT_GENERATION,
						requisition.getIdRequisition(), 
						requisition.getComment()!= null ? requisition.getComment().getCommentText() : ""));
			}

            Requisition requisitionAnexo = new Requisition();
            requisitionAnexo.setIdRequisition(requisition.getIdRequisition());
            LOG.info("INICIA FUNCIONALIDA DE ANEXOS.");
			this.requisitionBusiness.agregarAnexos(requisitionAnexo);
			this.requisitionBusiness.saveChangeRequisitionStatus(requisition.getIdRequisition(),requisition.getFlow());
			LOG.info("Revisión del borrador de la solicitud: " + requisition.getIdRequisition() + " enviada");

			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Envío a Revisión de Borrador de la " + "Solicitud " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
			return true;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		}
	}
	
	@RequestMapping(value = UrlConstants.SEND_REVIEW_DRAFT_ANGULAR_NOTIFICATION, method = RequestMethod.POST)
	@ResponseBody
	public final void sendReviewDraftAngularNotification(@RequestBody final RequisitionAngular requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			// Se crea la notificación para el usuario
			Notification notification = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.SEND_NEGOTIATOR_CONTRACT, null, null);
			if(notification!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
				 this.emailsBusiness.sendEmailNotifyUser(requisition, this.obtenerEmail(this.requisitionBusiness.getIdApplicantByIdRequisition(requisition.getIdRequisition())));
//				this.emailsBusiness.sendEmailNotify(this.createEmailContent(requisition, NotificacionTypeEnum.CHANGE_STATUS_SUBJECTEMAIL, NotificacionTypeEnum.SEND_NEGOTIATOR_CONTRACT_USR), 
//						this.obtenerEmail(this.requisitionBusiness.getIdApplicantByIdRequisition(requisition.getIdRequisition())));
			}
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.CHANGE_REVIEW_DRAFT, method = RequestMethod.POST)
	@ResponseBody
	public final void changeReviewDraft(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(requisition, CommentType.REJECTION));
			this.requisitionBusiness.changeReviewDraft(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Solicitud de cambio de Borrador de la" + " Solicitud " + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_DRAFT_CONTRACT_NEGOTIATOR, method = RequestMethod.POST)
	@ResponseBody
	public final void sendDraftContractNegotiator(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(CommentType.SEND_TO_REVIEW, FlowPurchasingEnum.NEGOTIATOR_CONTRACT_REVIEW,
					requisition.getIdRequisition(), 
					requisition.getComment()!= null ? requisition.getComment().getCommentText() : ""));
			this.requisitionBusiness.sendDraftContractNegotiator(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Envío de Borrador de Contrato al Negociador de la Solicitud " + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));

		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	
	@RequestMapping(value = UrlConstants.SEND_DRAFT_CONTRACT_NEGOTIATOR_NOTIFICATION, method = RequestMethod.POST)
	@ResponseBody
	public final void sendDraftContractNegotiatorNotification(@RequestBody final RequisitionAngular requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			
			// Se crea la notificación para el usuario
			Notification notification = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.SEND_LOAD_SUPPLIER_AREAS_APPROVAL, null, null);
			Notification notification2 = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.SEND_LOAD_SUPPLIER_AREAS_APPROVAL_REMITE, null, this.session.getUsuario().getIdUser());

			if(notification!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
				this.emailsBusiness.sendEmailNotifyUser(requisition, this.obtenerEmail(this.requisitionBusiness.getIdApplicantByIdRequisition(requisition.getIdRequisition())));
			}

			if(notification2!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification2);
				LOG.info("NOTIFICACIÓN 2 OK");
			}

		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}


	@RequestMapping(value = UrlConstants.REJECT_DRAFT_CONTRACT_NEGOTIATOR, method = RequestMethod.POST)
	@ResponseBody
	public final void rejectDraftContractNegotiator(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(requisition, CommentType.REJECTION));
			this.requisitionBusiness.rejectDraftContractNegotiator(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Rechazo del Borrador de Contrato por el"
					+ " Negociador de la Solicitud " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.REQUEST_MODIFICATION_DRAFT, method = RequestMethod.POST)
	@ResponseBody
	public final boolean requestModificationDraft(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(CommentType.REJECTION, FlowPurchasingEnum.NEGOTIATOR_CONTRACT_REVIEW,
					requisition.getIdRequisition(), 
					requisition.getComment() != null ? requisition.getComment().getCommentText() : ""));
			this.requisitionBusiness.requestModificationDraft(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Solicitud de Modificación de Borrador de la Solicitud " + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));			
			return true;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		}
	}
	
	@RequestMapping(value = UrlConstants.REQUEST_MODIFICATION_DRAFT_NOTIFICATION, method = RequestMethod.POST)
	@ResponseBody
	public final void requestModificationDraftNotification(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			// Se crea la notificación para el usuario
			Notification notification = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.MODIFY_CONTRATO, null, null);
			Notification notification2 = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.MODIFY_CONTRATO_REMITE, null, this.session.getUsuario().getIdUser());

			if(notification!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
			}			
			if(notification2!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification2);
				LOG.info("NOTIFICACIÓN 2 OK");
			}

		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_DRAFT_CONTRACT_USER, method = RequestMethod.POST)
	@ResponseBody
	public final void saveDraftUserVobo(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.requisitionBusiness.saveDraftUserVobo(requisition, this.session.getUsuario().getIdUser());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Envío de Borrador de Contrato al Usuario" + " de la Solicitud " + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.REJECT_DRAFT_CONTRACT_USER, method = RequestMethod.POST)
	@ResponseBody
	public final void rejectDraftContractUser(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(requisition, CommentType.REJECTION));
			this.requisitionBusiness.rejectDraftContractUser(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Rechazo de Borrador de Contrato por el "
					+ "Usuario de la Solicitud " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SAVE_SUPPLIER_VO_BO, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveVoBo(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			return this.requisitionBusiness.saveVoBo(requisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new Integer(null);
	}

	@RequestMapping(value = UrlConstants.DELETE_SUPPLIER_APPROVAL_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public final void deleteSupplierApprovalDocument(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.deleteSupplierApprovalDocument(requisition.getIdRequisition(),
					requisition.getSupplierApprovalIdDocument());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_TO_VO_BO, method = RequestMethod.POST)
	@ResponseBody
	public final boolean sendToVoBo(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(CommentType.SEND_TO_REVIEW, FlowPurchasingEnum.LOAD_SUPPLIER_AREAS_APPROVAL,
					requisition.getIdRequisition(), 
					requisition.getComment()!= null ? requisition.getComment().getCommentText() : ""));
			this.requisitionBusiness.sendToVoBo(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					REQUISITION_SENDED_MESSAGE + requisition.getIdRequisition() + " a VoBo", this.session,
					LogCategoryEnum.UPDATE));
			// Se crea la notificación para el usuario
			Notification notification = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.SEND_APROVED_BY_JURISTIC, null, null);

			if(notification!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
				RequisitionAngular reqAngular = new RequisitionAngular();
				reqAngular.setIdRequisition(requisition.getIdRequisition());
				this.emailsBusiness.sendEmailNotify(this.createEmailContent(reqAngular, NotificacionTypeEnum.CHANGE_STATUS_SUBJECTEMAIL, NotificacionTypeEnum.SEND_APROVED_BY_JURISTIC_JRD), 
						this.obtenerEmail(this.requisitionBusiness.getIdJuridico()));
			}

			return true;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		}
	}
	@RequestMapping(value = UrlConstants.SEND_TO_DOCUMENT_FINAL, method = RequestMethod.POST)
	@ResponseBody
	public final boolean sendToDocumentFinal(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.sendToDocumentFinal(requisition);
		return true;
	} catch (BusinessException businessException) {
		response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
		response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		return false;
	}
}

	@RequestMapping(value = UrlConstants.REJECT_TO_VO_BO, method = RequestMethod.POST)
	@ResponseBody
	public final void rejectToVoBo(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(requisition, CommentType.REJECTION));
			this.requisitionBusiness.rejectToVoBo(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Rechazo de VoBo de la Solicitud " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	private Comment createComment(final Requisition requisition, final CommentType commentType)
			throws BusinessException {
		final Comment comment = new Comment();
		comment.setIdRequisition(requisition.getIdRequisition());
		comment.setIdUser(this.session.getUsuario().getIdUser());
		comment.setCommentText(requisition.getComment().getCommentText());
		comment.setFlowStatus(FlowPurchasingEnum
				.valueOf(this.requisitionBusiness.findNextStatus(requisition.getFlowScreenActionParams())));
		comment.setCommentType(commentType);
		return comment;
	}

	/*private Comment createComment(final RequisitionAngular requisition, final CommentType commentType)
			throws BusinessException {
		final Comment comment = new Comment();
		comment.setIdRequisition(requisition.getIdRequisition());
		comment.setIdUser(this.session.getUsuario().getIdUser());
		comment.setCommentText(requisition.getComment().getCommentText());
		comment.setFlowStatus(FlowPurchasingEnum
				.valueOf(this.requisitionBusiness.findNextStatus(requisition.getFlow())));
		comment.setCommentType(commentType);
		return comment;
	}*/

	@RequestMapping(value = UrlConstants.SEND_APPROVED_CONTRACT, method = RequestMethod.POST)
	@ResponseBody
	public final void sendApprovedContract(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.requisitionBusiness.sendApprovedContract(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Envio a Contrato Aprovado de la Solicitud" + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_SIGNING_CONTRACT, method = RequestMethod.POST)
	@ResponseBody
	public final void sendSigningContract(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.requisitionBusiness.sendSigningContract(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Envío a Firma de Contrato de la Solicitud" + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SAVE_SIGN_CONTRACT, method = RequestMethod.POST)
	@ResponseBody
	public final void saveSignContract(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.requisitionBusiness.saveSignContract(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Guardado de Firma de Contrato de la" + " Solicitud" + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));

			this.requisitionBusiness.saveChangeRequisitionStatus(requisition.getIdRequisition(),requisition.getFlowScreenActionParams());

		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	
	@RequestMapping(value = UrlConstants.SAVE_SIGN_CONTRACT_NOTIFICATION, method = RequestMethod.POST)
	@ResponseBody
	public final void saveSignContractNotification(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			// Se crea la notificación para el usuario
			Notification notification2 = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.SEND_SACC_SCAN_CONTRACT, null, this.session.getUsuario().getIdUser());

			if(notification2!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification2);
				LOG.info("NOTIFICACIÓN 2  OK");
				RequisitionAngular reqAngular = new RequisitionAngular();
				reqAngular.setIdRequisition(requisition.getIdRequisition());
				this.emailsBusiness.sendEmailNotify(this.createEmailContent(reqAngular, NotificacionTypeEnum.CHANGE_STATUS_SUBJECTEMAIL, NotificacionTypeEnum.SEND_SACC_SCAN_CONTRACT_USR), this.obtenerEmail(this.requisitionBusiness.getIdApplicantByIdRequisition(requisition.getIdRequisition())));
			}

		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SAVE_CONTRACT_TO_DIGITIZE, method = RequestMethod.POST)
	@ResponseBody
	public final void saveContractToDigitize(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.requisitionBusiness.saveContractToDigitalize(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Guardado de Contrato para Digitalización" + " de la Solicitud" + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SAVE_AND_SEND_CONTRACT_TO_DIGITIZE, method = RequestMethod.POST)
	@ResponseBody
	//@RequestBody final Requisition requisition,
	public final void saveAndSendContractToDigitize(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(CommentType.CLOSE, FlowPurchasingEnum.REQUISITION_CLOSE,
					requisition.getIdRequisition(), 
					requisition.getComment()!= null ? requisition.getComment().getCommentText() : ""));
			this.requisitionBusiness.saveAndSendContractToDigitalize(requisition);
			// Se crea la notificación para el usuario
			Notification notification = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.FINISH_CONTRACT_PROCESS, null, null);
			if(notification!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
				RequisitionAngular reqAngular = new RequisitionAngular();
				reqAngular.setIdRequisition(requisition.getIdRequisition());

				this.emailsBusiness.sendEmailNotify(this.createEmailContent(reqAngular, NotificacionTypeEnum.CHANGE_STATUS_SUBJECTEMAIL, NotificacionTypeEnum.FINISH_CONTRACT_PROCESS_USR), this.obtenerEmail(this.requisitionBusiness.getIdApplicantByIdRequisition(requisition.getIdRequisition())));
			}

			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Guardado de Contrato para Digitalización" + " de la Solicitud" + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SAVE_DOCUMENTATION_MISSING, method = RequestMethod.POST)
	@ResponseBody
	public final void saveDocumentationMissing(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			this.requisitionBusiness.saveDocumentationMissing(requisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SEND_DOCUMENTATION_MISSING, method = RequestMethod.POST)
	@ResponseBody
	public final void sendDocumentationMissing(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			if (requisition.getComment() != null)
				this.commentsBusiness.saveOrUpdate(this.createComment(requisition, CommentType.RESEND));
			this.requisitionBusiness.sendDocumentationMissing(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Envío a Documentación Faltante de la " + "Solicitud" + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.FIND_FLOW_SCREEN, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<String> findFlowScreen(@RequestBody final Integer idRequisition,
			final HttpServletResponse response) {
		try {
			final ConsultaList<String> findFlowScreen = new ConsultaList<String>();
			findFlowScreen.setList(this.requisitionBusiness.findFlowStep(idRequisition, this.session.getIdFlow())); 
			return findFlowScreen;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<String> result = new ConsultaList<String>();
			result.setList(new ArrayList<String>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_DOCUMENTS_ATTACHMENT, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Version> findDocumentsAttachment(@RequestBody final ConsultaList<Version> parameters,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final ConsultaList<Version> listResponse = new ConsultaList<Version>();
			listResponse.setList(this.requisitionBusiness.findDocumentsAttachment(parameters.getParam4()));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Version> result = new ConsultaList<Version>();
			result.setList(new ArrayList<Version>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_DOCUMENTS_ATTACHMENT_VERSION_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Version> findDocumentsAttachmentVersionDetail(
			@RequestBody final ConsultaList<Version> parameters, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			final ConsultaList<Version> listResponse = new ConsultaList<Version>();
			listResponse.setList(this.requisitionVersionBusiness.findAttatchments(parameters.getParam4()));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Version> result = new ConsultaList<Version>();
			result.setList(new ArrayList<Version>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_HISTORY_DOCUMENTS_VERSIONS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Version> findHistoryDocumentsVersions(@RequestBody final ConsultaList<Version> parameters,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final ConsultaList<Version> listResponse = new ConsultaList<Version>();
			listResponse.setList(this.requisitionBusiness.findHistoryDocumentsVersions(
					Integer.parseInt(parameters.getParam1()), Integer.parseInt(parameters.getParam2())));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Version> result = new ConsultaList<Version>();
			result.setList(new ArrayList<Version>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.DOWNLOAD_DOCUMENT_FROM_HISTORY, method = RequestMethod.GET)
	public final void downloadDocumentFromHistory(final HttpServletRequest request,
			final HttpServletResponse response) {
		try {

			this.sendFileToDownload(response, request.getParameter(DOCUMENT_PATH));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Descarga del archivo histórico " + FilenameUtils.getName(request.getParameter(DOCUMENT_PATH)),
					this.session, LogCategoryEnum.DOWNLOAD));
		} catch (IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.DOWNLOAD_REQDOC_FROM_HISTORY, method = RequestMethod.GET)
	public final void downloadReqDocumentFromHistory(final HttpServletRequest request,
			final HttpServletResponse response) throws NumberFormatException, EmptyResultException {
		try {
			final String idDocumentParameter = request.getParameter(ID_DOCUMENT);
			final String versionNumber = request.getParameter("versionNumber");
			Version bean = new Version();
			bean = this.documentVersionBusiness.findDocumentVersion(Integer.parseInt(idDocumentParameter),
					Integer.parseInt(versionNumber));
			final String parameterDocumentPath = bean.getDocumentPath();
			this.sendFileToDownload(response, parameterDocumentPath);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Descarga del archivo histórico " + FilenameUtils.getName(request.getParameter(DOCUMENT_PATH)),
					this.session, LogCategoryEnum.DOWNLOAD));
		} catch (IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	
	//Descarga el archivo desde "VoBo jurídico"
	@CrossOrigin(exposedHeaders = "fileName")
	@RequestMapping(value = UrlConstants.DOWNLOAD_DOCUMENT_REQUIRED, method = RequestMethod.GET)
	public final void downloadDocumentRequired(final HttpServletRequest request, final HttpServletResponse response) throws Docx4JException {
				try {
			final String idDocumentParameter = request.getParameter(ID_DOCUMENT);
			final Boolean pdf = (request.getParameter(PDF)!=null && !request.getParameter(PDF).trim().isEmpty()) ? 
					Boolean.parseBoolean(request.getParameter(PDF)) : false;
					Version bean = new Version();
					bean = this.documentVersionBusiness.findCurrentVersion(Integer.parseInt(idDocumentParameter));
					final String parameterDocumentPath = bean.getDocumentPath();

					String fileName = FilenameUtils.getName(parameterDocumentPath);
					LOG.info("fileName: " + fileName);
					response.setHeader("fileName", fileName);

					if(!pdf) {
						this.sendFileToDownload(response, parameterDocumentPath);
					} else {
						String pathPDF = parameterDocumentPath.replace(".docx", ".pdf");
						descargarPDFBorradorContrato(response, parameterDocumentPath, pathPDF);
					}
					this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
							"Descarga del archivo requerido " + FilenameUtils.getName(request.getParameter(DOCUMENT_PATH)),
							this.session, LogCategoryEnum.DOWNLOAD));
					response.setStatus(HttpServletResponse.SC_OK);
		} catch (IOException businessException) {
			LOG.info("No se pudo descargar el documento. IOException - " + businessException.toString());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (BusinessException businessException) {
			LOG.info("No se pudo descargar el documento. BusinessException - " + businessException.toString());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	//Transforma el archivo desde "Impresion"
	@CrossOrigin(exposedHeaders = "fileName")
	@RequestMapping(value = UrlConstants.DOCUMENT_REQUIRED_PDF, method = RequestMethod.GET)
	public final void documentRequiredpdf(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		LOG.info("///////////////////////// ");
		LOG.info("LA RESPUESTA ES : " + response.toString());
		try {
			final String id= request.getParameter(ID_REQUISITION);
//			final int idRequisition=Integer.parseInt(id);
			LOG.info("IDREQUISITION ES : " + id);
			final String idDocumentParameter = request.getParameter(ID_DOCUMENT);
			final Boolean pdf = (request.getParameter(PDF)!=null && !request.getParameter(PDF).trim().isEmpty()) ? 
					Boolean.parseBoolean(request.getParameter(PDF)) : false;
					Version bean = new Version();
					bean = this.documentVersionBusiness.findCurrentVersion(Integer.parseInt(idDocumentParameter));
					final String parameterDocumentPath = bean.getDocumentPath();

					String fileName = FilenameUtils.getName(parameterDocumentPath);
					LOG.info("fileName: " + fileName);
					response.setHeader("fileName", fileName);

					if(!pdf) {
						this.sendFileToDownload(response, parameterDocumentPath);
					} else {
						String pathPDF = parameterDocumentPath.replace(".docx", ".pdf");
						transformarPDFContrato(response, parameterDocumentPath, pathPDF,id);
					}
//					this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
//							"Descarga del archivo requerido " + FilenameUtils.getName(request.getParameter(DOCUMENT_PATH)),
//							this.session, LogCategoryEnum.DOWNLOAD));
					response.setStatus(HttpServletResponse.SC_OK);
		} catch (IOException businessException) {
			LOG.info("No se pudo descargar el documento. IOException - " + businessException.toString());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (BusinessException businessException) {
			LOG.info("No se pudo descargar el documento. BusinessException - " + businessException.toString());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	
	@RequestMapping(value = UrlConstants.DOWNLOAD_DOC_REQUIRED, method = RequestMethod.GET)
    public  void downloadDocRequired(final HttpServletRequest request,
            final HttpServletResponse response) {
    	LOG.info("entro al DOWNLOAD_DOC_REQUIRED ::::: ");
        try {
            final String idDocumentParameter = request.getParameter(ID_DOCUMENT);
            Version bean = new Version();
            bean = this.documentVersionBusiness.findVersion(Integer.parseInt(idDocumentParameter));
            final String parameterDocumentPath = bean.getDocumentPath();
            this.sendFileToDownload(response, parameterDocumentPath);
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Descarga del archivo requerido "
                    + FilenameUtils.getName(request.getParameter(DOCUMENT_PATH)), this.session,
                    LogCategoryEnum.DOWNLOAD));
        } catch (IOException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }

	private void sendFileToDownload(final HttpServletResponse response, final String parameterDocumentPath)
			throws FileNotFoundException, IOException {
		final File file = new File(parameterDocumentPath);
		if(!file.exists()) {
			new FileNotFoundException();
		}
		this.setReponseData(response, file.getName(), file);
		final FileInputStream fileInputStream = new FileInputStream(file);
		final ServletOutputStream servletOutputStream = response.getOutputStream();
		IOUtils.copy(fileInputStream, servletOutputStream);
		fileInputStream.close();
		servletOutputStream.flush();
		servletOutputStream.close();
	}

	private void sendFileToDownloadAndDeleteFile(final HttpServletResponse response, final String filePath)
			throws FileNotFoundException, IOException {
		this.sendFileToDownload(response, filePath);
		final File file = new File(filePath);
		if (file.exists())
		    file.delete();
	}

	@RequestMapping(value = UrlConstants.FIND_DIGITALIZATION_DOCUMENT_VERSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Version> findDigitalizationDocumentVersion(
			@RequestBody final ConsultaList<Version> versionList, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			final ConsultaList<Version> listResponse = new ConsultaList<Version>();
			listResponse.setList(this.requisitionBusiness.findDigitalizationDocuments(versionList.getParam4()));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Version> result = new ConsultaList<Version>();
			result.setList(new ArrayList<Version>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_APPROVAL_AREAS_DOCUMENTS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<ApprovalArea> findApprovalAreasDocuments(
			@RequestBody final ConsultaList<ApprovalArea> parameters, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			final ConsultaList<ApprovalArea> listResponse = new ConsultaList<ApprovalArea>();
			listResponse.setList(this.requisitionBusiness.findRequisitionApprovalAreasVoBo(parameters.getParam4()));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<ApprovalArea> result = new ConsultaList<ApprovalArea>();
			result.setList(new ArrayList<ApprovalArea>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_VERSION_HISTORY, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<VersionDTO> findRequisitionVersionHistory(
			@RequestBody final Integer idRequisition, final HttpServletRequest request,
			final HttpServletResponse response) {
//		try {
//			final ConsultaList<RequisitionVersion> listResponse = new ConsultaList<RequisitionVersion>();
//			listResponse.setList(this.requisitionVersionBusiness.findRequisitionVersions(idRequisition));
//			return listResponse;
//		} catch (BusinessException businessException) {
//			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
//			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
//			final ConsultaList<RequisitionVersion> result = new ConsultaList<RequisitionVersion>();
//			result.setList(new ArrayList<RequisitionVersion>());
//			return result;
//		}
		try {
			final ConsultaList<VersionDTO> listResponse = new ConsultaList<VersionDTO>();
			listResponse.setList(this.requisitionVersionBusiness.findRequisitionVersions(idRequisition));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<VersionDTO> result = new ConsultaList<VersionDTO>();
			result.setList(new ArrayList<VersionDTO>());
			return result;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_TEMPLATE, method = RequestMethod.GET)
	public final void findTemplate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String templatePath = this.requisitionBusiness
					.findTemplate(Integer.valueOf(request.getParameter(ID_REQUISITION)));
			this.sendFileToDownload(response, templatePath);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.DELETE_DIGITALIZATION, method = RequestMethod.POST)
	@ResponseBody
	public final void deleteDigitalizationByIdDocument(@RequestBody final Integer idDocument,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			this.requisitionBusiness.deleteDigitalizationByIdDocument(idDocument);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Eliminación de documento digitalizado " + idDocument, this.session, LogCategoryEnum.DOWNLOAD));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.DOWNLOAD_REQUISITION_DOCUMENT, method = RequestMethod.GET)
	public final void downloadDocument(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final Integer idDocument = Integer.valueOf(request.getParameter(ID_DOCUMENT));
			final Version version = this.documentVersionBusiness.findCurrentVersion(idDocument);
			final String templatePath = version.getDocumentPath();
			this.sendFileToDownload(response, templatePath);
			this.binnacleBusiness.save(
					LoggingUtils.createBinnacleForLogging("Descarga de archivo " + FilenameUtils.getName(templatePath),
							this.session, LogCategoryEnum.DOWNLOAD));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_OBLIGATIONS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Obligation> findObligations(@RequestBody final Integer idRequisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final ConsultaList<Obligation> responseList = new ConsultaList<>();
			responseList.setList(this.requisitionBusiness.findObligationsByIdRequisition(idRequisition));
			return responseList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<Obligation>();
	}

	@RequestMapping(value = UrlConstants.FIND_LAST_VERSION_DOCUMENT_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final FileUploadInfo findLastVersionDocumentById(@RequestBody final FileUploadInfo fileUpload,
			final HttpServletRequest request, final HttpServletResponse response) {
		final FileUploadInfo fileUploadInfo = new FileUploadInfo();
		final Integer idDocument = fileUpload.getIdFile();
		try {
			final Version version = this.documentVersionBusiness.findCurrentVersion(idDocument);
			fileUploadInfo.setName(FilenameUtils.getName(version.getDocumentPath()));
			fileUploadInfo.setIdFile(idDocument);
			fileUploadInfo.setFilePath(version.getDocumentPath());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			fileUploadInfo.setIdFile(idDocument);
		}
		return fileUploadInfo;
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_BY_ID_FLOW, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Requisition> findRequisitionByFlow(@RequestBody final String idFlow,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final ConsultaList<Requisition> requisitionList = new ConsultaList<Requisition>();
			requisitionList.setList(this.requisitionBusiness.findRequisitionByFlow(Integer.valueOf(idFlow)));
			return requisitionList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<Requisition>();
	}

	@RequestMapping(value = UrlConstants.OBTENER_SOLICITUDES_PENDIENTES, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<TrayRequisition> obtenerSolicitudesPendientesPorEnviar(
			@RequestBody final TrayFilter trayFilterParameter, final HttpServletResponse response) {
		final ConsultaList<TrayRequisition> requisitionList = new ConsultaList<TrayRequisition>();
		try {
			if (this.session.getIdFlow() != null) {
				this.setInProgressRequisitionsFilter(trayFilterParameter);
				requisitionList.setList(this.requisitionBusiness.obtenerSolicitudesPendientes(trayFilterParameter));
			}
			return requisitionList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			requisitionList.setList(new ArrayList<TrayRequisition>());
			return requisitionList;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_IN_PROGRESS_REQUISITIONS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Requisition> findPaginatedInProgressRequisitions(
			@RequestBody final TrayFilter trayFilterParameter, final HttpServletResponse response) {
		try {
			final ConsultaList<Requisition> requisitionList = new ConsultaList<Requisition>();
			if (this.session.getIdFlow() != null) {
				this.setInProgressRequisitionsFilter(trayFilterParameter);
				requisitionList.setList(this.requisitionBusiness.findInProgressRequisitions(trayFilterParameter));
			}
			return requisitionList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<Requisition> result = new ConsultaList<Requisition>();
			result.setList(new ArrayList<Requisition>());
			return result;
		}
	}

	private void setInProgressRequisitionsFilter(final TrayFilter trayFilter) {
		trayFilter.setIdFlow(this.session.getIdFlow());
		trayFilter.setIdUser(this.session.getUsuario().getIdUser());
	}

	@RequestMapping(value = UrlConstants.FIND_PAGINATED_TRAY_REQUISITIONS_BY_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<TrayRequisition> findPaginatedTrayByRecordStatus(
			@RequestBody final ConsultaList<TrayFilter> parameters, final HttpServletResponse response) {
		final ConsultaList<TrayRequisition> requisitionList = new ConsultaList<TrayRequisition>();
		try {
			if (this.session.getIdFlow() != null) {

				final TrayFilter trayFilter = parameters.getList().get(0);
				trayFilter.setIdFlow(this.session.getIdFlow());
				trayFilter.setIdUser(this.session.getUsuario().getIdUser());
//				LOG.info("//////////////////////////////////////////////////////////////////");
//				LOG.info("GETIDFLOW "+this.session.getIdFlow().toString());
//				LOG.info("GETIDFLOW "+this.session.getUsuario().getIdUser().toString());
				List<TrayRequisition> requisitions = this.requisitionBusiness.findAllTrayRequisitionsAngular(trayFilter);
				//requisitionList.setList(this.requisitionBusiness.findPaginatedTrayRequisitions(trayFilter,parameters.getParam4(), parameters.getParam3()));
				//requisitionList.setParam4(this.requisitionBusiness.totalPagesForTray(trayFilter, parameters.getParam3()));
				requisitionList.setList(requisitions);
			} else {
				requisitionList.setList(new ArrayList<TrayRequisition>());
			}
		} catch (Exception businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			requisitionList.setList(new ArrayList<TrayRequisition>());
		}
		return requisitionList;
	}
	@RequestMapping(value = UrlConstants.FIND_PAGINATED_TRAY_REQUISITIONS_BY_STATUS_GRAFICA, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<TrayRequisition> findPaginatedTrayByRecordStatusGrafica(
			@RequestBody final ConsultaList<TrayFilter> parameters, final HttpServletResponse response) {
		final ConsultaList<TrayRequisition> requisitionList = new ConsultaList<TrayRequisition>();
		try {
			if (this.session.getIdFlow() != null) {
				Integer usuario=1;
				final TrayFilter trayFilter = parameters.getList().get(0);
				trayFilter.setIdFlow(this.session.getIdFlow());
				trayFilter.setIdUser(usuario);
				LOG.info("//////////////////////////////////////////////////////////////////");
				LOG.info("GETIDFLOW "+this.session.getIdFlow().toString());
				LOG.info("GETIDFLOW "+this.session.getUsuario().getIdUser().toString());
				List<TrayRequisition> requisitions = this.requisitionBusiness.findAllTrayRequisitionsAngular(trayFilter);
				//requisitionList.setList(this.requisitionBusiness.findPaginatedTrayRequisitions(trayFilter,parameters.getParam4(), parameters.getParam3()));
				//requisitionList.setParam4(this.requisitionBusiness.totalPagesForTray(trayFilter, parameters.getParam3()));
				requisitionList.setList(requisitions);
			} else {
				requisitionList.setList(new ArrayList<TrayRequisition>());
			}
		} catch (Exception businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			requisitionList.setList(new ArrayList<TrayRequisition>());
		}
		return requisitionList;
	}
	@RequestMapping(value = UrlConstants.FIND_PAGINATED_TRAY_REQUISITIONS_BY_DATE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<TrayRequisition> findPaginatedTrayPorFechas(
			@RequestBody final ConsultaList<TrayFilter> parameters, final HttpServletResponse response) {
		final ConsultaList<TrayRequisition> requisitionList = new ConsultaList<TrayRequisition>();
		try {
			if (this.session.getIdFlow() != null) {

				final TrayFilter trayFilter = parameters.getList().get(0);
				trayFilter.setIdFlow(this.session.getIdFlow());
				trayFilter.setIdUser(this.session.getUsuario().getIdUser());
				List<TrayRequisition> requisitions = this.requisitionBusiness.findAllTrayRequisitionsAngularPorFechas(trayFilter);
				requisitionList.setList(requisitions);
			} else {
				requisitionList.setList(new ArrayList<TrayRequisition>());
			}
		} catch (Exception businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			requisitionList.setList(new ArrayList<TrayRequisition>());
		}
		return requisitionList;
	}

	@RequestMapping(value = UrlConstants.TOTAL_PAGES_SHOW_TRAY, method = RequestMethod.POST)
	@ResponseBody
	public final Integer totalPagesForTray(@RequestBody final TrayFilter trayFilter,
			final HttpServletResponse response) {
		try {
			if (this.session.getIdFlow() != null) {
				trayFilter.setIdFlow(this.session.getIdFlow());
				trayFilter.setIdUser(this.session.getUsuario().getIdUser());
			}
			return this.requisitionBusiness.totalPagesForTray(trayFilter, null);
		} catch (BusinessException businessException) {
			LOG.error(businessException.getMessage());
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}

	@RequestMapping(value = UrlConstants.SAVE_REQUISITION_EDITED_DATA, method = RequestMethod.POST)
	@ResponseBody
	public final void saveRequisitionEditedData(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.saveRequisitionEditedData(requisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_BY_PARAMETERS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Requisition> findRequisitionByParameters(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			final ConsultaList<Requisition> requisitionList = new ConsultaList<Requisition>();
			requisitionList.setList(this.requisitionBusiness.findRequisitionByManyParameters(requisition));
			return requisitionList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<Requisition>();
	}

	@RequestMapping(value = UrlConstants.FIND_PAGINATED_REQUISITIONS_MANAGEMENT, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Requisition> paginatedFindRequisitionsManagement(
			@RequestBody final ConsultaList<Requisition> parameters, final HttpServletResponse response) {
		try {
			final Requisition requisition = parameters.getList().get(0);
			requisition.setIdFlow(this.session.getIdFlow());
			// requisition.setIdFlow(1);
			final Integer pageNumber = parameters.getParam4();
			final ConsultaList<Requisition> requisitionList = new ConsultaList<Requisition>();
			requisitionList
			.setList(this.requisitionBusiness.paginatedFindRequisitionsManagement(requisition, pageNumber));
			return requisitionList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<Requisition>();
	}

	@RequestMapping(value = UrlConstants.TOTAL_PAGES_SHOW_PAGINATED_REQUISITIONS_MANAGEMENT, method = RequestMethod.POST)
	@ResponseBody
	public final Integer returnTotalPagesShowRequisitionsManagement(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			requisition.setIdFlow(this.session.getIdFlow());
			// requisition.setIdFlow(1);
			return this.requisitionBusiness.returnTotalPagesShowRequisitionsManagement(requisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value = UrlConstants.TOTAL_DATA_SHOW_PAGINATED_REQUISITIONS_MANAGEMENT, method = RequestMethod.POST)
    @ResponseBody
    public final Integer returnTotalDataShowRequisitionsManagement(@RequestBody final Requisition requisition,
            final HttpServletResponse response) {
        try {
            requisition.setIdFlow(this.session.getIdFlow());
            return this.requisitionBusiness.returnTotalDataShowRequisitionsManagement(requisition);
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return null;
    }

	@RequestMapping(value = UrlConstants.FIND_REQUISITION_CLOSED, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Requisition> findRequisitionClosed(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			final ConsultaList<Requisition> requisitionList = new ConsultaList<Requisition>();
			requisitionList.setList(this.requisitionBusiness.findRequisitionClosed(requisition));
			return requisitionList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<Requisition>();
	}

	@RequestMapping(value = UrlConstants.FIND_PAGINATED_REQUISITIONS_CLOSED, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Requisition> paginatedFindRequisitionsClosed(
			@RequestBody final ConsultaList<Requisition> parameters, final HttpServletResponse response) {
		try {
			final Requisition requisition = parameters.getList().get(0);
			requisition.setIdFlow(this.session.getIdFlow());
			final Integer pageNumber = parameters.getParam4();
			final ConsultaList<Requisition> requisitionList = new ConsultaList<Requisition>();
			requisitionList.setList(this.requisitionBusiness.paginatedFindRequisitionsClosed(requisition, pageNumber));
			return requisitionList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<Requisition>();
	}

	@RequestMapping(value = UrlConstants.FIND_PAGINATED_CONTRACTS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Requisition> findPaginatedContracts(
			@RequestBody final ConsultaList<Requisition> parameters, final HttpServletResponse response) {
		try {
			final Requisition requisition = parameters.getList().get(0);
			requisition.setIdFlow(this.session.getIdFlow());
			final Integer pageNumber = parameters.getParam4();
			final ConsultaList<Requisition> requisitionList = new ConsultaList<Requisition>();
			requisitionList.setList(this.requisitionBusiness.findPaginatedContracts(requisition, pageNumber));
			return requisitionList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<Requisition>();
	}

	@RequestMapping(value = UrlConstants.TOTAL_PAGES_SHOW_PAGINATED_REQUISITIONS_CLOSED, method = RequestMethod.POST)
	@ResponseBody
	public final Integer returnTotalPagesShowRequisitionsClosed(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			requisition.setIdFlow(this.session.getIdFlow());
			return this.requisitionBusiness.returnTotalPagesShowRequisitionsClosed(requisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}

	@RequestMapping(value = UrlConstants.TOTAL_PAGES_SHOW_PAGINATED_OF_CONTRACTS, method = RequestMethod.POST)
	@ResponseBody
	public final Integer returnTotalPagesShowOfContracts(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			return this.requisitionBusiness.returnTotalPagesShowOfContracts(requisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}

	@RequestMapping(value = UrlConstants.CHANGE_ATTEND_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final void changeAttendStatus(@RequestBody final Requisition requisition,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.changeAttendStatus(requisition.getIdRequisition(),
					requisition.getIsExpiredAttended());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Cambio el estatus de la solicitud " + requisition.getIdRequisition() + " como "
							+ (!requisition.getIsExpiredAttended() ? "Atendido" : "No Atendido"),
							this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.FIND_SCALING_MATRIX_VERSION_BY_ID_REQUISITION_VERSION_AND_SCALING_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Scaling> findScalingMatrixVersionByIdRequisitionVersion(
			@RequestBody final ConsultaList<String> vo, final HttpServletResponse response) {
		final ConsultaList<Scaling> scalingList = new ConsultaList<>();
		try {
			scalingList.setList(this.requisitionBusiness.findScalingMatrixVersionByIdRequisitionVersion(vo.getParam4(),
					ScalingTypeEnum.valueOf(vo.getParam1())));
			return scalingList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return scalingList;
	}

	@RequestMapping(value = UrlConstants.FIND_SCALING_MATRIX_BY_ID_REQUISITION_AND_SCALING_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Scaling> findScalingMatrixByIdRequisition(@RequestBody final ConsultaList<String> vo,
			final HttpServletResponse response) {
		final ConsultaList<Scaling> scalingList = new ConsultaList<>();
		try {
			scalingList.setList(this.requisitionBusiness.findScalingMatrixByIdRequisition(vo.getParam4(),
					ScalingTypeEnum.valueOf(vo.getParam1())));
			return scalingList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return scalingList;
	}

	@RequestMapping(value = UrlConstants.GENERATE_EXCEL_REPORT, method = RequestMethod.GET)
	public final void createRequisitionsByFlowExcelReport(final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			final List<List<String>> matrix = new ArrayList<>();
			for (final String row : request.getParameter("matrix").split(";")) {
				this.getColumn(matrix, row);
			}
			this.sendFileToDownloadAndDeleteFile(response,
					this.requisitionBusiness.createExcelReport(request.getParameter("reportName"), matrix).getPath());
			this.binnacleBusiness.save(
					LoggingUtils.createBinnacleForLogging("Generacion de Excel", this.session, LogCategoryEnum.SAVE));
		} catch (BusinessException | IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	private void getColumn(final List<List<String>> matrix, final String row) {
		final List<String> stringList = new ArrayList<>();
		for (final String column : row.split(",")) {

			stringList.add(this.decodeAscii(column));
		}
		matrix.add(stringList);
	}

	private String decodeAscii(final String text) {
		if ("null".equals(text))
			return "";
		else {
			final StringBuilder stringBuilder = new StringBuilder();
			for (final String ascii : text.split("_")) {
				final int code = Integer.valueOf(ascii);
				stringBuilder.append((char) code);
			}
			return stringBuilder.toString();
		}
	}

	@RequestMapping(value = UrlConstants.FIND_APPROVAL_AREAS_VERSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<String> findApprovalAreasVersion(@RequestBody final Integer idRequisitionVersion,
			final HttpServletResponse response) {
		final ConsultaList<String> approvalAreasIdList = new ConsultaList<>();
		try {
			approvalAreasIdList.setList(this.requisitionVersionBusiness.findApprovalAreasName(idRequisitionVersion));
			return approvalAreasIdList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return approvalAreasIdList;
	}

	@RequestMapping(value = UrlConstants.FIND_ATTACHMENT_VERSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Version> findAttchmentsVersion(@RequestBody final Integer idRequisitionVersion,
			final HttpServletResponse response) {
		final ConsultaList<Version> attachmentList = new ConsultaList<>();
		try {
			attachmentList.setList(this.requisitionVersionBusiness.findAttatchments(idRequisitionVersion));
			return attachmentList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return attachmentList;
	}

	@RequestMapping(value = UrlConstants.FIND_AUTHORIZATION_DGA_VERSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Dga> findAuthorizationDgas(@RequestBody final Integer idRequisitionVersion,
			final HttpServletResponse response) {
		final ConsultaList<Dga> dgasList = new ConsultaList<>();
		try {
			dgasList.setList(this.requisitionVersionBusiness.findAuthorizationDgas(idRequisitionVersion));
			return dgasList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return dgasList;
	}

	@RequestMapping(value = UrlConstants.FIND_FINANTIAL_ENTITY_VERSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<FinancialEntity> findFinantialEntitiesVersion(
			@RequestBody final Integer idRequisitionVersion, final HttpServletResponse response) {
		final ConsultaList<FinancialEntity> financialEntityList = new ConsultaList<>();
		try {
			financialEntityList.setList(this.requisitionVersionBusiness.findFinantialEntities(idRequisitionVersion));
			return financialEntityList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return financialEntityList;
	}

	@RequestMapping(value = UrlConstants.FIND_FINANTIAL_ENTITY_WITNESS_VERSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<String> findFinantialEntitiesWitnessesVersion(
			@RequestBody final Integer idRequisitionVersion, final HttpServletResponse response) {
		final ConsultaList<String> financialEntityWitnessesList = new ConsultaList<>();
		try {
			financialEntityWitnessesList
			.setList(this.requisitionVersionBusiness.findFinantialEntitiesWitnesses(idRequisitionVersion));
			return financialEntityWitnessesList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return financialEntityWitnessesList;
	}

	@RequestMapping(value = UrlConstants.FIND_LEGAL_REPRESENTATIVE_VERSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<LegalRepresentative> findLegalRepresentativesVersion(
			@RequestBody final Integer idRequisitionVersion, final HttpServletResponse response) {
		final ConsultaList<LegalRepresentative> legalRepresentativeList = new ConsultaList<>();
		try {
			legalRepresentativeList
			.setList(this.requisitionVersionBusiness.findLegalRepresentatives(idRequisitionVersion));
			return legalRepresentativeList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return legalRepresentativeList;
	}

	@RequestMapping(value = UrlConstants.FIND_USER_VOBO_VERSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<User> findUsersVoboVersion(@RequestBody final Integer idRequisitionVersion,
			final HttpServletResponse response) {
		final ConsultaList<User> usersVoBoList = new ConsultaList<>();
		try {
			usersVoBoList.setList(this.requisitionVersionBusiness.findUsersVobo(idRequisitionVersion));
			return usersVoBoList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return usersVoBoList;
	}

	@RequestMapping(value = UrlConstants.FIND_APPROVAL_AREAS_VOBO_VERSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<ApprovalArea> findfindApprovalAreasVoBoVersion(
			@RequestBody final Integer idRequisitionVersion, final HttpServletResponse response) {
		final ConsultaList<ApprovalArea> approvalAreasList = new ConsultaList<>();
		try {
			approvalAreasList.setList(this.requisitionVersionBusiness.findApprovalAreasVoBo(idRequisitionVersion));
			return approvalAreasList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return approvalAreasList;
	}

	@RequestMapping(value = UrlConstants.COMPARE_WORD_FILES, method = RequestMethod.POST)
	@ResponseBody
	public final ComparationWord wordComparate(@RequestBody final ParametersHolder parameters,
			final HttpServletResponse response) throws FileNotFoundException, IOException, WordComparationException {
		try {
			return this.requisitionBusiness.wordComparate(parameters.getParameterValue("baseFileName").toString(),
					parameters.getParameterValue("compareFileName").toString(), this.session.getIdUsuarioSession());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ComparationWord();
	}

	@RequestMapping(value = UrlConstants.DOWNLOAD_WORD_COMPARATION_FILE, method = RequestMethod.GET)
	@ResponseBody
	public final void downloadWordComparationFile(final HttpServletResponse response, final HttpServletRequest request)
			throws FileNotFoundException, IOException, WordComparationException {
		try {
			final String wordFilePath = this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString())
					+ Constants.PATH_TMP + File.separator + request.getParameter("fileName");
			this.sendFileToDownloadAndDeleteFile(response, wordFilePath);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.SAVE_CONTRACT_CANCELLATION_COMMENT, method = RequestMethod.POST)
	@ResponseBody
	public final boolean saveContractCancellationComment(
			@RequestBody final ContractCancellationComment contractCancellationComment,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.saveContractCancellationCommentAngular(contractCancellationComment);
			// Se crea la notificación para el usuario
//			Notification notification = this.notificationBusiness.createNotificationByRequisition(contractCancellationComment.getIdRequisition(), 
//					NotificacionTypeEnum.CANCEL_CONTRACT_BY_APPLICANT, null, null);
//			Notification notification2 = this.notificationBusiness.createNotificationByRequisition(contractCancellationComment.getIdRequisition(), 
//					NotificacionTypeEnum.CANCEL_CONTRACT_BY_APPLICANT, null, this.session.getUsuario().getIdUser());
//			if(notification!=null) {
//				this.template.convertAndSend("/topic/resp/notifications", notification);
//				LOG.info("NOTIFICACIÓN  OK");
//			}
//			if(notification2!=null) {
//				this.template.convertAndSend("/topic/resp/notifications", notification2);
//				LOG.info("NOTIFICACIÓN 2 OK");
//			}

			return Boolean.TRUE;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return Boolean.FALSE;
		}
	}
	
	@RequestMapping(value = UrlConstants.SAVE_CONTRACT_CANCELLATION_COMMENT_NOTIFICATION, method = RequestMethod.POST)
	@ResponseBody
	public final void saveContractCancellationCommentNotification(
			@RequestBody final ContractCancellationComment contractCancellationComment,
			final HttpServletResponse response) {
		try {
			// Se crea la notificación para el usuario
			Notification notification = this.notificationBusiness.createNotificationByRequisition(contractCancellationComment.getIdRequisition(), 
					NotificacionTypeEnum.CANCEL_CONTRACT_BY_APPLICANT, null, null);
			Notification notification2 = this.notificationBusiness.createNotificationByRequisition(contractCancellationComment.getIdRequisition(), 
					NotificacionTypeEnum.CANCEL_CONTRACT_BY_APPLICANT, null, this.session.getUsuario().getIdUser());
			if(notification!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
			}
			if(notification2!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification2);
				LOG.info("NOTIFICACIÓN 2 OK");
			}
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.FIND_CONTRACT_CANCELLATION_COMMENT, method = RequestMethod.POST)
	@ResponseBody
	public final ContractCancellationComment findContractCancellationComment(@RequestBody final Integer idRequisition,
			final HttpServletResponse response) {
		try {
			return this.requisitionBusiness.findContractCancellationComment(idRequisition);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ContractCancellationComment();
	}

	@RequestMapping(value = UrlConstants.FIND_CONTRACT_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public final ContractDetail findContractDetailByIdRequisition(@RequestBody final Integer idRequisition,
			final HttpServletResponse response) {
		try {
			final ContractDetail contractDetailResponse = this.requisitionBusiness
					.findContractDetailByIdRequisition(idRequisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Búsqueda de detalle de contrato por Id " + idRequisition, this.session, LogCategoryEnum.QUERY));
			return contractDetailResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return new ContractDetail();
		}
	}

	@RequestMapping(value = UrlConstants.DELETE_AUTHORIZATION_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public final void deleteAuthorizationDocument(@RequestBody final Requisition requisitionParameters,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.deleteAuthorizationDocument(requisitionParameters.getIdRequisition(),
					requisitionParameters.getAuthorizationDocumentIdDoc());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Eliminar el documento de authorización " + requisitionParameters.getAuthorizationDocumentIdDoc(),
					this.session, LogCategoryEnum.DELETE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.DELETE_IMSS_CEDULE, method = RequestMethod.POST)
	@ResponseBody
	public final void deleteImssCeduleFile(@RequestBody final Requisition requisitionParameters,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.deleteImssCeduleFile(requisitionParameters.getIdRequisition(),
					requisitionParameters.getImssCeduleNotGivenIdDocument());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Eliminar el cedula del IMSS: " + requisitionParameters.getImssCeduleNotGivenIdDocument(),
					this.session, LogCategoryEnum.DELETE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.CREATE_REPLACES_MAP, method = RequestMethod.POST)
	@ResponseBody
	public final ParametersHolder createReplacesMap(@RequestBody final Integer idRequisition,
			final HttpServletResponse response) {
		try {
			final ParametersHolder replacesMapHolder = new ParametersHolder();
			replacesMapHolder.setStringsMap(this.requisitionBusiness.createReplacesMap(idRequisition));
			return replacesMapHolder;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ParametersHolder();
	}

	@RequestMapping(value = UrlConstants.DELETE_PENDING_REQUISITIONS, method = RequestMethod.POST)
	@ResponseBody
	public final void deletePendingRequisitions(@RequestBody final ConsultaList<Integer> clist,
			final HttpServletResponse response) {
		try {
			this.requisitionBusiness.deletePendingRequisitions(clist);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.FIND_APPLICANT_INPROGRESS_REQUISITIONS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<UserInProgressRequisition> findApplicantInProgressRequisitions(
			@RequestBody final ConsultaList<UserInProgressRequisitionFilter> parameters,
			final HttpServletResponse response) {
		  final ConsultaList<UserInProgressRequisition> applicantInProgressRequisitionList = new ConsultaList<>();
	        try {
	            final UserInProgressRequisitionFilter filter = parameters.getList().get(0);
	            filter.setIdUser(this.session.getIdUsuarioSession());
	            final Integer pageNumber = parameters.getParam4();
	            applicantInProgressRequisitionList.setList(
	                    this.requisitionBusiness.findApplicantInProgressRequisitions(filter, pageNumber));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return applicantInProgressRequisitionList;
	}

	@RequestMapping(value = UrlConstants.TOTAL_PAGES_APPLICANT_IN_PROGRESS_REQUISITIONS, method = RequestMethod.POST)
	@ResponseBody
	public final Integer countTotalPagesApplicantInProgressRequisitions(
			@RequestBody final ConsultaList<UserInProgressRequisitionFilter> parameters,
			final HttpServletResponse response) {
		try {
			final UserInProgressRequisitionFilter filter = parameters.getList().get(0);
			filter.setIdUser(this.session.getIdUsuarioSession());
			return this.requisitionBusiness.countTotalPagesApplicantInProgressRequisitions(filter);
		} catch (BusinessException businessException) {
			LOG.error(businessException.getMessage());
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}

	@RequestMapping(value = UrlConstants.FIND_LAWYER_INPROGRESS_REQUISITIONS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<UserInProgressRequisition> findLawyerInProgressRequisitions(
			@RequestBody final ConsultaList<UserInProgressRequisitionFilter> parameters,
			final HttpServletResponse response) {
		final ConsultaList<UserInProgressRequisition> lawyertInProgressRequisitionList = new ConsultaList<>();
		try {
			final UserInProgressRequisitionFilter filter = parameters.getList().get(0);
			final Integer pageNumber = parameters.getParam4();
			lawyertInProgressRequisitionList
			.setList(this.requisitionBusiness.findLawyerInProgressRequisitions(filter, pageNumber));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return lawyertInProgressRequisitionList;
	}

	@RequestMapping(value = UrlConstants.TOTAL_PAGES_LAWYER_IN_PROGRESS_REQUISITIONS, method = RequestMethod.POST)
	@ResponseBody
	public final Integer countTotalPagesLawyerInProgressRequisitions(
			@RequestBody final ConsultaList<UserInProgressRequisitionFilter> parameters,
			final HttpServletResponse response) {
		try {
			final UserInProgressRequisitionFilter filter = parameters.getList().get(0);
			return this.requisitionBusiness.countTotalPagesLawyerInProgressRequisitions(filter);
		} catch (BusinessException businessException) {
			LOG.error(businessException.getMessage());
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}

	@RequestMapping(value = UrlConstants.ENVIAR_VOBO_JURIDICO, method = RequestMethod.POST)
	@ResponseBody
	public final boolean enviarVoBoJuridico(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			if(validateProfileByToScreens(FlowPurchasingEnum.APROVED_BY_JURISTIC)) {
				if(this.requisitionBusiness.validateStatusByIdRequisition(requisition.getIdRequisition(), FlowPurchasingEnum.APROVED_BY_JURISTIC)) {
					this.commentsBusiness.saveOrUpdate(this.createComment(CommentType.SEND_TO_REVIEW, FlowPurchasingEnum.APROVED_BY_JURISTIC,
							requisition.getIdRequisition(), 
							requisition.getComment()!= null ? requisition.getComment().getCommentText() : ""));
					this.requisitionBusiness.actualizaEvaluadorVoBoJuridico(requisition.getIdRequisition());
					LOG.info("------------------------------  OK--------------------------------------");
					LOG.info("supplierApprovalDocument:   "+requisition.getSupplierApprovalDocument());
//					this.requisitionBusiness.guardarVoBoJuridico(requisition, this.session.getUsuario().getIdUser(),requisition.getSupplierApprovalDocument());
					// al ser enviado de VoBo Juridico en el metodo guardarVoBoJuridico se crea el QR
					this.requisitionBusiness.guardarVoBoJuridico(requisition, this.session.getUsuario().getIdUser());
					this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Envío del VoBo de Juridico, número de solicitud: " + requisition.getIdRequisition(),
							this.session, LogCategoryEnum.UPDATE));

					// Se crea la notificación para el usuario
					Notification notification = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
							NotificacionTypeEnum.SEND_PRINT_CONTRACT, null, null);
					if(notification!=null) {
						this.template.convertAndSend("/topic/resp/notifications", notification);
						LOG.info("NOTIFICACIÓN  OK");
					}
				}
			}
			return true;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		}
	}

	@RequestMapping(value = UrlConstants.RECHAZAR_VOBO_JURIDICO, method = RequestMethod.POST)
	@ResponseBody
	public final boolean rechazarVoBoJuridico(@RequestBody final Requisition requisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(CommentType.REJECTION, FlowPurchasingEnum.APROVED_BY_JURISTIC,
					requisition.getIdRequisition(), 
					requisition.getComment()!= null ? requisition.getComment().getCommentText() : ""));
			this.requisitionBusiness.rechazarVoBoJuridico(requisition);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Rechazo VoBo de Juridico"
					+ ", número de solicitud: " + requisition.getIdRequisition(), this.session,
					LogCategoryEnum.UPDATE));
			
			// Se crea la notificación para el usuario
			Notification notification = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.REJECTION_BY_JURISTIC, null, null);
			if(notification!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
			}
			return true;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		}
	}

	@RequestMapping(value = UrlConstants.DESCARGAR_BORRADOR_CONTRATO_PDF, method = RequestMethod.GET)
	public final void descargarBorradorContratoPDF(final HttpServletRequest request, final HttpServletResponse response) throws Docx4JException {
		try {
			final String draftPath = this.requisitionBusiness.draftContractPath(request.getParameter("draftName"));
			this.descargarEliminarArchivoPDF(response, draftPath);
		} catch (BusinessException | IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	private void descargarEliminarArchivoPDF(final HttpServletResponse response, final String filePath)
			throws FileNotFoundException, IOException, Docx4JException {
		String rutaPDF = filePath.replace(".docx", ".pdf");
		this.descargarPDFBorradorContrato(response, filePath, rutaPDF);
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}

		file = new File(rutaPDF);	
		if (file.exists()) {
			file.delete();
		}

	}

	private void descargarPDFBorradorContrato (final HttpServletResponse response, String wordPath, String pdfPath) throws FileNotFoundException, IOException, Docx4JException {
		File doc = new File(wordPath);
		if(!doc.exists()) {
			LOG.error("================== DESCARGA PDF BORRADOR CONTRATO  ==================\n "
					+ "ERROR: ARCHIVO ORIGEN NO ENCONTRADO"
					+ "Documento WORD  :: "+doc+"\n"
					+ "Documento PDF   		:: "+pdfPath);
			new FileNotFoundException();
		}
		String docStr = doc.getPath();
		String pdfStr = FilenameUtils.removeExtension(doc.getPath())+".pdf";		
		
		LOG.info("==================================================\n DESCARGA PDF BORRADOR CONTRATO\n"
				+ "Documento WORD  :: "+docStr+"\n"
				+ "Documento PDF   		:: "+pdfStr);
		
		/* v1 word to pdf */
//		final File document = PDFUtils.convertDocxToPDF(doc);
		
		/* v2 word to pdf */
		PDFUtils.convertDocxToPDF_v2(docStr, pdfStr);
		final File document = new File(pdfStr);
		
		
		this.setReponseData(response, document.getName(), document);
		final FileInputStream fileInputStream = new FileInputStream(document);
		final ServletOutputStream servletOutputStream = response.getOutputStream();
		IOUtils.copy(fileInputStream, servletOutputStream);
		fileInputStream.close();
		servletOutputStream.flush();
		servletOutputStream.close();
		LOG.info("=================================================================================");
	}
	private void transformarPDFContrato (final HttpServletResponse response, String wordPath, String pdfPath, String idRequest) throws Exception {
		File doc = new File(wordPath);
		if(!doc.exists()) {
			LOG.error("================== DESCARGA PDF BORRADOR CONTRATO  ==================\n "
					+ "ERROR: ARCHIVO ORIGEN NO ENCONTRADO"
					+ "Documento WORD  :: "+doc+"\n"
					+ "Documento PDF   		:: "+pdfPath);
			new FileNotFoundException();
		}
		String docStr = doc.getPath();
		String pdfStr = FilenameUtils.removeExtension(doc.getPath())+".pdf";		
		
		LOG.info("==================================================\n DESCARGA PDF BORRADOR CONTRATO\n"
				+ "Documento WORD  :: "+docStr+"\n"
				+ "Documento PDF   		:: "+pdfStr);
		
		/* v1 word to pdf */
//		final File document = PDFUtils.convertDocxToPDF(doc);
		
		/* v2 word to pdf */
//		PDFUtils.convertDocxToPDFDocuSign(docStr, pdfStr);
//		Integer idRequest=4028;
		File docx = new File(this.requisitionBusiness.findTemplate(Integer.parseInt(idRequest)));
		String docxBaseName = FilenameUtils.getBaseName(docx.getName());

		String pdfPathh = this.buildPath(Integer.parseInt(idRequest), "Solicitudes")  + File.separator + docxBaseName + Constants.PDF_FILE;
		File pdf = new File(pdfPathh);
		PDFUtils.convertDocxToPDFDocuSign(docx, pdf);
		final File document = new File(pdfStr);
		
		
		this.setReponseData(response, document.getName(), document);
		final FileInputStream fileInputStream = new FileInputStream(document);
		final ServletOutputStream servletOutputStream = response.getOutputStream();
		IOUtils.copy(fileInputStream, servletOutputStream);
		fileInputStream.close();
		servletOutputStream.flush();
		servletOutputStream.close();
		LOG.info("=================================================================================");
	}
	
	private StringBuilder buildPath(final Integer idObject, final String objectName)
            throws BusinessException {
        final StringBuilder pathBuilder = new StringBuilder(this.getBasePath());
        pathBuilder.append(File.separator).append(objectName).append(File.separator);
        pathBuilder.append(File.separator).append(this.userSession.getIdFlow()).append(File.separator);
        pathBuilder.append(DirUtil.obtenRutaSolicitud(idObject)).append(File.separator);
        pathBuilder.append(idObject.toString());
        this.validateDirectory(pathBuilder.toString());
        return pathBuilder;
    }
	 private String getBasePath() throws BusinessException {
	        try {
	            return this.configurable.findByName("ROOT_PATH");
	        } catch (DatabaseException databaseException) {
	            LOG.error(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
	            throw new BusinessException(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
	        }
	    }
	 private void validateDirectory(final String path) {
	        final File requisitionPath = new File(path);
	        if (!requisitionPath.exists())
	            requisitionPath.mkdirs();
	    }
	
	
	
	
 

	@RequestMapping(value = UrlConstants.FIND_DOCUMENTS_ATTACHMENT_DTO, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<VersionDTO> findDocumentsAttachmentDTO(@RequestBody final Integer idRequisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final ConsultaList<VersionDTO> listResponse = new ConsultaList<VersionDTO>();
			listResponse.setList(this.requisitionBusiness.findDocumentsAttachmentDTO(idRequisition, this.session.getIdUsuarioSession()));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<VersionDTO> result = new ConsultaList<VersionDTO>();
			result.setList(new ArrayList<VersionDTO>());
			return result;
		}
	}


	@RequestMapping(value = UrlConstants.SAVE_DOCUMENT_ATTACHMENT_DTO, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<VersionDTO> saveDocumentAttachmentDTO(@RequestBody final  ConsultaList<FileUploadInfo> archivos,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			if(archivos != null && archivos.getList() != null && !archivos.getList().isEmpty()) {
				final ConsultaList<VersionDTO> listResponse = new ConsultaList<VersionDTO>();
				listResponse.setList(this.requisitionBusiness.saveNewDocumentsAttachment(archivos.getList(), this.session.getIdUsuarioSession()));
				return listResponse;
			}
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return null;
		}
		return null;
	}

	@RequestMapping(value = UrlConstants.SEND_PRINT_CONTRACT, method = RequestMethod.POST)
	@ResponseBody
	public final boolean sendPrintContract(@RequestBody final Requisition requisition, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			this.commentsBusiness.saveOrUpdate(this.createComment(CommentType.SEND_TO_REVIEW, FlowPurchasingEnum.PRINT_CONTRACT,
					requisition.getIdRequisition(), 
					requisition.getComment()!= null ? requisition.getComment().getCommentText() : ""));
			this.requisitionBusiness.savePrintContract(requisition, this.session.getUsuario().getIdUser());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Se envía a firmas, número de solicitud: " + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));
			// Se crea la notificación para el usuario
			Notification notification = this.notificationBusiness.createNotificationByRequisition(requisition.getIdRequisition(), 
					NotificacionTypeEnum.SEND_SACC_SIGN_CONTRACT, null, null);
			if(notification!=null) {
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
				RequisitionAngular reqAngular = new RequisitionAngular();
				reqAngular.setIdRequisition(requisition.getIdRequisition());
				this.emailsBusiness.sendEmailNotify(this.createEmailContent(reqAngular, NotificacionTypeEnum.CHANGE_STATUS_SUBJECTEMAIL, NotificacionTypeEnum.SEND_SACC_SIGN_CONTRACT_USR), 
						this.obtenerEmail(this.requisitionBusiness.getIdApplicantByIdRequisition(requisition.getIdRequisition())));
			}

			return true;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_VERSIONS_CONTRACT_DTO, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<VersionDTO> findContractVersionDTO(@RequestBody final Integer idRequisition,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final ConsultaList<VersionDTO> listResponse = new ConsultaList<VersionDTO>();
			listResponse.setList(this.documentVersionBusiness.findContractVersionDTO(idRequisition));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			final ConsultaList<VersionDTO> result = new ConsultaList<VersionDTO>();
			result.setList(new ArrayList<VersionDTO>());
			return result;
		}
	}


	private boolean validateProfileByToScreens(FlowPurchasingEnum flowStatus) throws BusinessException {
		return this.profilesBusiness.validateProfileByToScreens(this.session.getUsuario().getProfileList(), 
				this.session.getIdFlow(), 
				flowStatus);
	}

	private Comment createComment(final CommentType commentType,  FlowPurchasingEnum status,
			Integer idRequisition, String comments)
					throws BusinessException {
		final Comment comment = new Comment();
		comment.setIdRequisition(idRequisition);
		comment.setIdUser(this.session.getUsuario().getIdUser());
		comment.setCommentText(comments);
		comment.setFlowStatus(status);
		comment.setCommentType(commentType);
		return comment;
	}

	@RequestMapping (value = UrlConstants.GET_IDS_SUPPLIERPERSON_BY_IDREQUISITION, method = RequestMethod.POST)
	@ResponseBody
	public final List<SupplierPersonByRequisition> getIdsSupplierPersonByIdRequisition(@RequestBody SupplierPersonByRequisition person, final HttpServletResponse  response) {
		LOG.info("PurchasingRequisitionService :: getIdsSupplierPersonByIdRequisition");
		try {
			return this.requisitionBusiness.getIdsSupplierPersonByIdRequisitionDTO(person.getIdRequisition(), person.getIdSupplier());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return null;
		}
	}

	private EmailContent createEmailContent(final RequisitionAngular requisition, NotificacionTypeEnum subject, NotificacionTypeEnum description) throws BusinessException {
		final EmailContent emailContent = new EmailContent();
		emailContent.setSubject(getMessageByType(subject));
		emailContent.setFieldOne("FOLIO:");
		emailContent.setFieldOneDescription(requisition.getIdRequisition().toString());
		emailContent.setSendDateDescription("FECHA:");
		emailContent.setSendDate(this.obtenerFecha());
		emailContent.setContent(replaceVariables(description, requisition.getIdRequisition().toString()));
		emailContent.setBrand(this.configurationBusiness.findByName("EMAIL_NOTIFICATION_BRAND"));
		return emailContent;
	}

	private LocalDate obtenerFecha() {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDate fechaEnvio = currentTime.toLocalDate();
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
		String textFechaEnvio = fechaEnvio.format(formatters);
		LocalDate parsedDate = LocalDate.parse(textFechaEnvio, formatters);

		return parsedDate;
	}

	private String replaceVariables(NotificacionTypeEnum type, String idRequisition) {
		String cadena = getMessageByType(type);
		cadena = cadena.replace("%folio%", idRequisition);
		return cadena;
	}

	private String getMessageByType (NotificacionTypeEnum type) {
		String name = type.toString();
		String message = null;
		try {
			message = this.configurable.findByName(name);
		} catch (Exception e) {
			message = null;
		}
		return message !=null ? message : type.getLabel();
	}

	private String obtenerEmail(Integer idUser) {

		String email = "";
		LOG.info("IDUSER EMAIL: " + idUser);
		try {
			User userBean = this.usersBusiness.findByUserId(idUser);
			email = userBean.getEmail(); 
		} catch (BusinessException e) {
			LOG.error("ERROR: al obtener correo del idUSer" + idUser + " MSJ: " + e);
		}
		LOG.info("EMAIL IDUSER: " + email + "->" + idUser);
		return email;
	}
	
	@RequestMapping(value = UrlConstants.REQUEST_ONLY_UPDATE_DRAFT, method = RequestMethod.POST)
	@ResponseBody
	public final boolean requestOnlyUpdateDraft(@RequestBody final Requisition requisition, final HttpServletRequest request, final HttpServletResponse response) {
		try {
			requisition.getFlowScreenActionParams().setIdFlow(this.session.getIdFlow());
			
			this.requisitionBusiness.requestUpdateDraft(requisition);
			
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Modificación del Borrador de la solicitud" + requisition.getIdRequisition(),
					this.session, LogCategoryEnum.UPDATE));			
			return true;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		}
	}
	
	/*
	 * Este método se ejecutará automáticamente al iniciar el sistema y además
	 * ejecutara una o más tareas en un intervalo de tiempo programado.
	 */
	@PostConstruct
	public void startExecutorService() {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
		Runnable task = () -> findContractsInRevision(); // Expresión lambda que llama a findContractsInRevision()
		long initialDelay = 0;
		long period = 24 * 60 * 60; // 24 horas en segundos
//		long period = 60; // 60 segundos
		executorService.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
		
	    Runnable task2 = () -> findContractsInSignatures(); //Expresión lambda que llama a findContractsInSignature()
	    long initialDelay2 = 0;
		long period2 = 24 * 60 * 60; // 24 horas en segundos
//	    long period2 = 60; // 60 segundos
	    executorService.scheduleAtFixedRate(task2, initialDelay2, period2, TimeUnit.SECONDS);
	}

	/* Consultará los contratos que estén en bandeja de revisión */
	public void findContractsInRevision() {
		try {
			List<TrayRequisition> requisitions = this.requisitionBusiness.findAllContractsInRevision();
			String bandeja = "Revisión contrato solicitado";
			this.findContractsOutOfTime(requisitions, bandeja);

			Map<String, List<String>> emailToIdsMap = new HashMap<>();
		} catch (Exception businessException) {
			System.out.println("Falló: findContractsOutOfTimeRevision");
		}
	}
	
	/* Consultará los contratos que estén en bandeja de firmas */
	public void findContractsInSignatures() {
		try {
			List<TrayRequisition> requisitions = this.requisitionBusiness.findAllContractsInSignatures();
			String bandeja = "Firmas de contrato final";
			this.findContractsOutOfTime(requisitions, bandeja);

			Map<String, List<String>> emailToIdsMap = new HashMap<>();
		} catch (Exception businessException) {
			System.out.println("Falló: findContractsOutOfTimeInSignature");
		}
	}
	
	/*
	 * Obtiene los folios de los contratos que hayan excedido el tiempo de
	 * resolución y le envía un correo con una alerta a quien corresponda
	 */
	public void findContractsOutOfTime(List<TrayRequisition> req, String bandeja) {
		try {
			Map<String, List<String>> emailToIdsMap = new HashMap<>();
			for (TrayRequisition requisition : req) {
				LocalDate startDate = requisition.getStageStartDate();
				LocalDate currentDate = LocalDate.now();
				List<LocalDate> holidays = Holidays.getHolidays();
				long businessDaysDifference = calculateDaysDifference(startDate, currentDate, holidays);
				if (businessDaysDifference > 300) {
					String email = requisition.getEmail();
					Integer idRequisition = requisition.getIdRequisition();
					List<String> idsList = emailToIdsMap.getOrDefault(email, new ArrayList<>());
					idsList.add(idRequisition.toString());
					emailToIdsMap.put(email, idsList);
//					LOG.info("Han pasado más de 5 días desde: " + startDate + " y " + currentDate + " folio: "
//							+ requisition.getIdRequisition());
				} else {
//					LOG.info("No han pasado más de 5 días desde: " + startDate + " y " + currentDate + " folio: "
//							+ requisition.getIdRequisition());
				}
			}
			for (Map.Entry<String, List<String>> entry : emailToIdsMap.entrySet()) {
				String email = entry.getKey();
				List<String> idsList = entry.getValue();
				StringBuilder idsStringBuilder = new StringBuilder();
				for (String id : idsList) {
					if (idsStringBuilder.length() > 0) {
						idsStringBuilder.append(", ");
					}
					idsStringBuilder.append(id);
				}
				String idsAsString = idsStringBuilder.toString();
				this.emailsBusiness.sendEmailAlert(email, idsStringBuilder, bandeja);
				System.out.println("Correo electrónico: " + email);
				System.out.println("idRequisition: " + idsAsString);
			}
		} catch (Exception businessException) {
			System.out.println("Falló: findContractsOutOfTime");
		}
	}
    
	/* Calcula los días festivos y fines de semana */
	public static long calculateDaysDifference(LocalDate startDate, LocalDate endDate, List<LocalDate> holidays) {
		long businessDays = 0;
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			// Excluye los fines de semana y los días festivos
			if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY
					&& !holidays.contains(currentDate)) {
				businessDays++;
			}
			currentDate = currentDate.plus(1, ChronoUnit.DAYS);
		}
		return businessDays;
	}
	
	
	
	
}