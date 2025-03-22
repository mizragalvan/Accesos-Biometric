package mx.pagos.admc.contracts.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tr;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import mx.engineer.utils.date.DateUtils;
import mx.engineer.utils.excel.ExcelUtils;
import mx.engineer.utils.file.PrintDirectoryTree;
import mx.engineer.utils.general.CurrencyUtils;
import mx.engineer.utils.general.NumberToEnglishLetters;
import mx.engineer.utils.general.NumbersToLetters;
import mx.engineer.utils.general.OrdinalNumberUtils;
import mx.engineer.utils.general.PercentageToStringWords;
import mx.engineer.utils.general.ReflectionUtils;
import mx.engineer.utils.general.SubparagraphUtils;
import mx.engineer.utils.general.ValidatePathSistem;
import mx.engineer.utils.general.exceptions.InvalidFieldException;
import mx.engineer.utils.mail.structures.EmailContent;
import mx.engineer.utils.secutiry.SecurityEncrypt;
import mx.engineer.utils.string.StringUtils;
import mx.engineer.utils.word.AddFooterQr;
import mx.engineer.utils.word.CreaTablaFirmas;
import mx.engineer.utils.word.DocXUtil;
import mx.engineer.utils.word.VariablePrepare;
import mx.engineer.utils.word.WordComparationException;
import mx.engineer.utils.word.WordComparationUtils;
import mx.pagos.admc.contracts.interfaces.FlowScreenActionable;
import mx.pagos.admc.contracts.interfaces.FlowStepable;
import mx.pagos.admc.contracts.interfaces.Qr;
import mx.pagos.admc.contracts.interfaces.Requisitable;
import mx.pagos.admc.contracts.interfaces.SupplierPersonable;
import mx.pagos.admc.contracts.interfaces.Supplierable;
import mx.pagos.admc.contracts.interfaces.owners.RequisitionOwnersable;
import mx.pagos.admc.contracts.structures.AlertFlowStep;
import mx.pagos.admc.contracts.structures.ApprovalArea;
import mx.pagos.admc.contracts.structures.Attachment;
import mx.pagos.admc.contracts.structures.Clause;
import mx.pagos.admc.contracts.structures.Comment;
import mx.pagos.admc.contracts.structures.ComparationWord;
import mx.pagos.admc.contracts.structures.ContractCancellationComment;
import mx.pagos.admc.contracts.structures.ContractDetail;
import mx.pagos.admc.contracts.structures.Customs;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.DocumentType;
import mx.pagos.admc.contracts.structures.FileUploadInfo;
import mx.pagos.admc.contracts.structures.FinancialEntitieCombination;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.FinantialEntityWitness;
import mx.pagos.admc.contracts.structures.FlowScreenAction;
import mx.pagos.admc.contracts.structures.Instrument;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.ModifiedClausules;
import mx.pagos.admc.contracts.structures.Obligation;
import mx.pagos.admc.contracts.structures.Personality;
import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.contracts.structures.QuickResponse;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.contracts.structures.RequiredDocumentBySupplier;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionAngular;
import mx.pagos.admc.contracts.structures.RequisitionAttachment;
import mx.pagos.admc.contracts.structures.RequisitionComplete;
import mx.pagos.admc.contracts.structures.RequisitionDraftPart2;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.contracts.structures.RequisitionsPartFour;
import mx.pagos.admc.contracts.structures.RequisitionsPartOneAndTwo;
import mx.pagos.admc.contracts.structures.RequisitionsPartThree;
import mx.pagos.admc.contracts.structures.RollOff;
import mx.pagos.admc.contracts.structures.Scaling;
import mx.pagos.admc.contracts.structures.Supplier;
import mx.pagos.admc.contracts.structures.SupplierPerson;
import mx.pagos.admc.contracts.structures.SupplierPersonByRequisition;
import mx.pagos.admc.contracts.structures.TagField;
import mx.pagos.admc.contracts.structures.Tracto;
import mx.pagos.admc.contracts.structures.TrayFilter;
import mx.pagos.admc.contracts.structures.TrayRequisition;
import mx.pagos.admc.contracts.structures.UserInProgressRequisition;
import mx.pagos.admc.contracts.structures.UserInProgressRequisitionFilter;
import mx.pagos.admc.contracts.structures.VoBoDocumentFile;
import mx.pagos.admc.contracts.structures.dtos.RequisitionDTO;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.business.EmailsBusiness;
import mx.pagos.admc.core.utils.DirectoryUtils;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.DigitalSignatureProviderEnum;
import mx.pagos.admc.enums.DigitalSignatureStatusEnum;
import mx.pagos.admc.enums.DocumentTypeEnum;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.PersonalityEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.ScalingTypeEnum;
import mx.pagos.admc.enums.SupplierPersonTypeEnum;
import mx.pagos.admc.enums.TagFieldEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.document.version.business.DocumentVersionBusiness;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.ConfigurationException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.business.UsersBusiness;
import mx.pagos.security.structures.User;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.shared.TranslateNumber;

@Service
public class RequisitionBusiness {
	private static final String MESSAGE_DELETING_PENDING_REQUISITIONS_ERROR = "Hubo un problema al eliminar las solicitudes pendientes";
	private static final String MESSAGE_RETRIEVIG_POWER_ERROR = "Hubo un problema al recuperar los datos del poder del representante legal para la entidad seleccionada";
	private static final String MESSAGE_NO_FINANCIAL_ENTITY_ERROR = "La solicitud no tiene una entidad relacionada o esta está inactiva";
	private static final String MESSAGE_RETRIVING_FINANCIAL_ENTITY_ERROR = "Hubo un problema al recuperar la entidad seleccionada";
	private static final String NOT_CAPTURED_FIELD = "CAMPO NO CAPTURADO";
	private static final String Y = " y ";
	private static final String SAY = "DICE: ";
	private static final String SHOULD_SAY = "DEBE DECIR: ";
	private static final String UPPER_AND = " Y ";
	private static final String COLON_SPACE = ": ";

	private static final String MESSAGE_ADDING_ATTATCHMENTS_ERROR = "Hubo un problema al agregar los archivos anexos";
	private static final String MESSAGE_CREATING_REPLACES_MAP_ERROR = "Hubo un problema al generar el mapa de reemplazos";
	private static final String BR = "\n";
	private static final String QUOTES = "\"";
	private static final String BOLD_OPEN = "<b>";
	private static final String BOLD_CLOSE = "</b>";
	private static final String WHITE_SPACE = " ";
	private static final String MESSAGE_RETRIVING_NEXT_STAGE_ERROR = "Hubo un problema al guardar la etapa del siguiente estatus de la solicitud";
	private static final Logger LOG = Logger.getLogger(RequisitionBusiness.class);
	private static final String ES = "es";
	private static final String SET_DATE_WITH_LONG_FORMAT = "dd 'de' MMMM 'de' yyyy";
	private static final String SET_DATE_SIGN = "dd 'de' MMMM 'de' yyyy";
	private static final String MESSAGE_CANCELL_CHECK_QUERY_ERROR = "Hubo un problema al determinar si la solicitud ha sido cancelada previamente";
	private static final String DOUBLE_TABULATOR = "\t\t";
	private static final String COMMA = ", ";
	private static final String UNDERSCORE = "_";
	private static final String INVALID_CHARACTERS = "[\\\\/><\\|\\s\"'`´,^{}()\\[\\]:]+";
	private static final String MESSAGE_MOVING_DOCUMENT_ERROR = "Hubo un error al guardar el documento";
	private static final String MESSAGE_SAVING_REQUISITION_ERROR = "Hubo un problema al guardar los datos de la solicitud";
	private static final String MESSAGE_SAVING_REQUISITION_FINANTIAL_ENTITIES_ERROR = "Hubo un problema al guardar entidades de la solicitud";
	private static final String MESSAGE_SAVING_REQUISITION_DATA_FINANTIAL_ENTITIES_ERROR = "Hubo un problema al guardar los datos de entidades de la solicitud";
	private static final String MESSAGE_SAVE_REQUISITION_STATUS_ERROR = "Hubo un problema al guardar el estatus de la solicitud";
	private static final String MESSAGE_SAVE_REQUISITION_STATUS_CANCELLED_ERROR = "Hubo un problema al guardar la cancelación de la solicitud";
	private static final String MESSAGE_RETRIEVING_REQUISITION_BY_ID_ERROR = "Hubo un problema al recuperar la solicitud por Id";
	private static final String MESSAGE_REQUISITION_NO_LONGER_EXISTS = "La solicitud ha dejado de existir";
	private static final String MESSAGE_RETRIEVINGREQUISITION_BY_STATUS_ERROR = "Hubo un problema al obtener solicitudes por estatus";
	private static final String MESSAGE_CREATING_WORD_ERROR = "Hubo un problema al generar el archivo";
	private static final String MESSAGE_RETRIEVING_FINATIAL_ENTITIES_ERROR = "Error al obtener Entidades por Id de Solicitud";
	private static final String MESSAGE_RETRIEVING_FINATIAL_ENTITIES_WITNESS_ERROR = "Error al obtener Testigos Entidades";
	private static final String MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR = "Hubo un problema al recuperar los representantes legales de la solicitud";
	private static final String MESSAGE_SAVING_LEGAL_REPRESENTATIVES_ERROR = "Hubo un problema al guardar los representantes legales de la solicitud";
	private static final String MESSAGE_SAVING_APPROVAL_AREAS_ERROR = "Hubo un problema al guardar las áreas que darán el VoBo";
	private static final String MESSAGE_SAVING_AUTHORIZATION_DGAS_ERROR = "Hubo un problema al guardar las DGA's de la solicitud";
	private static final String MESSAGE_RETRIVING_APPROVAL_AREAS_ERROR = "Hubo un problema al recuperar las Areas";
	private static final String MESSAGE_RETRIVING_AUTHORIZATION_DGAS_ERROR = "Hubo un problema al recuperar las DGA's";
	private static final String MESSAGE_RETRIVING_APPROVAL_AREAS_NAME = "Hubo un problema al recuperar los nombres de las áreas";
	private static final String MESSAGE_SAVING_USERS_TO_VOBO_ERROR = "Hubo un problema al guardar los usuarios para VoBo de la solicitud";
	private static final String MESSAGE_RETRIEVING_USERS_TO_VOBO_ERROR = "Hubo un problema al recuperar los usuarios para VoBo de la solicitud";
	private static final String MESSAGE_SAVING_FINANTIAL_ENTITY_WITNESSES_ERROR = "Hubo un problema al guardar los testigos de las entidades de la solicitud";
	private static final String MESSAGE_RETRIEVING_USERS_ENTITY_WITNESSES_ERROR = "Hubo un problema al recuperar las entidades de la solicitud";
	private static final String MESSAGE_RETRIVING_NEXT_STATUS_ERROR = "Hubo  un problema al recuperar el siguiente estatus del flujo";
	private static final String MESSAGE_UPDATING_LAWYER_ERROR = "Hubo un problema al actualizar el abogado asignado";
	private static final String MESSAGE_UPDATING_ATTATCHMENTS_ERROR = "Hubo un problema al actualizar los anexos seleccionados de la solicitud";
	private static final String MESSAGE_SAVING_EVALUATOR_ERROR = "Hubo un problema al actualizar el evaluador de la solicitud";
	private static final String MESSAGE_SAVING_PROVIDER_WINTNESS_SIGN_DATES_ERROR = "Hubo un problema al actualizar "
			+ "las fechas de entrega/recibo del documento para firmas del proveedor y/o testigos";
	private static final String MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGN_DATES_ERROR = "Hubo un problema al guardar "
			+ "las fechas de entrega/recibo del documento para firmas de los representantes legales";
	private static final String MESSAGE_SEND_CHANGE_DRAFT_ERROR = "Hubo un problema al guardar los datos del borrador";
	private static final String MESSAGE_SAVING_SIGNED_CONTRACT_DATA_ERROR = "Hubo un problema al guardar la información de la entrega de documentos firmados";
	private static final String MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGNED_CONTRACT_DATA_ERROR = "Hubo un problema al "
			+ "guardar la información de la entrega de documentos firmados de los representantes legales";
	private static final String MESSAGE_FIND_FLOW_STEP_ERROR = "Hubo un problema al obtener la lista de la rutas de imagenes";
	private static final String MESSAGE_DOCUMENTS_ATTATCHMENTS_ERROR = "Hubo un error al guardar los documentos anexos";
	private static final String MESSAGE_DOCUMENTS_HISTORY_ERROR = "Hubo un problema al recuperar el historial de versiones";
	private static final String MESSAGE_SAVING_DIGITALIZATION_ID_DOCUMENT_ERROR = "Hubo un problema al guardar el documento digitalizado en la solicitud";
	private static final String MESSAGE_SAVING_APPROVAL_AREAS_VOBO_ERROR = "Hubo un problema al guardar los documentos de VoBo de las áreas de aprobación";
	private static final String MESSAGE_RETRIEVING_APPROVAL_AREAS_VOBO_ERROR = "Hubo un problema al recuperar los documentos de VoBo de las áreas de aprobación";
	private static final String MESSAGE_SAVING_SUPPLIER_APROVAL_ID_DOCUMENT_ERROR = "Hubo un problema al guardar el documento de VoBo del proveedor";
	private static final String MESSAGE_RETRIEVING_DIGITALIZATION_DOCUMENTS_ERROR = "Hubo un problema al recuperar los documentos digitalizados";
	private static final String MESSAGE_DELETE_ATTATCHMENTS_ERROR = "Hubo un error al eliminar los documentos anexos";
	private static final String MESSAGE_NO_NEXT_STATUS_ERROR = "No existe siguiente estatus en el flujo";
	private static final String MESSAGE_SAVING_TEMPLATE_ID_DOCUMENT_ERROR = "Hubo un problema al guardar la plantilla";
	private static final String MESSAGE_DELETING_DIGITALIZATION_BY_ID_DOCUMENT_ERROR = "Hubo un error al eliminar la digitalización solicitada";
	private static final String MESSAGE_SAVING_OBLIGATIONS_ERROR = "Hubo un problema al guardar las obligaciones";
	private static final String MESSAGE_RETRIVING_OBLIGATIONS_BY_REQUISITION_ERROR = "Hubo un problema al recuperar las obligaciones";
	private static final String MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR = "Hubo un problema al recuperar las solicitudes de la bandeja";
	private static final String MESSAGE_SAVING_USER_VOBO_ERROR = "Hubo un problema al guardar el visto bueno del usuario";
	private static final String MESSAGE_RETRIEVING_IS_ALL_USERS_VOBO_ERROR = "Hubo un error al validar si todos los usuarios indicados en la solicitud han dado su VoBo";
	private static final String MESSAGE_FIND_REQUISITION_BY_FLOW_ERROR = "Hubo un problema al obtener las solicitudes por flujo";
	private static final String MESSAGE_SAVE_LAWYER_ERROR = "Error al intentar actualizar el negociador";
	private static final String MESSAGE_SAVE_APPLICANT_ERROR = "Error al intentar guardar el abogado";
	private static final String MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR = "Hubo un problema al recuperar solicitudes por los parametros proporcionados";
	private static final String MESSAGE_COUNT_TOTAL_PAGES_ERROR = "Hubo un problema al contar el número de páginas a mostrar";
	private static final String MESSAGE_SAVING_TURN_ERROR = "Hubo un problema al registrar el numero de reproceso de la solicitud";
	private static final String MESSAGE_SAVING_REQUISITION_STATUS_TURN_ATTENTION_DAYS_ERROR = "Hubo un problema al guardar los días de atención de la solicitud";
	private static final String MESSAGE_RETRIVING_REQUISITION_STATUS_TURNS_ERROR = "Hubo un problema al recuperar la bitácora de movimientos de la solicitud";
	private static final String MESSAGE_RETRIVING_CURRENT_REQUISITION_TURN_ERROR = "Hubo un problema al recuperar el número de movimiento del estatus actual de la solicitud";
	private static final String MESSAGE_SAVE_SCALING_MATRIX_ERROR = "Hubo un problema al guardar la matris de escalabilidad";
	private static final String MESSAGE_DELETE_SCALING_MATRIX_BY_ID_REQUISITION_ERROR = "Hubo un problema al borrar la matris de escalabilidad por id de la solicitud";
	private static final String MESSAGE_FIND_SCALING_MATRIX_BY_ID_REQUISITION_ERROR = "Hubo un problema al obtener la matriz de escalabilidad por id de la solicitud";
	private static final String MESSAGE_FIND_SCALING_MATRIX_VERSION_BY_ID_REQUISITION_VERSION_ERROR = "Hubo un problema al obtener la matriz de escalabilidad de la versión por id de la solicitud";
	private static final String MESSAGE_CREATING_EXCEL_ERROR = "Hubo un problema al generar el archivo de Excel";
	private static final String MESSAGE_CLEAN_USERS_VOBO_ERROR = "Hubo un problema al reiniciar los Vobos de los usuarios";
	private static final String MESSAGE_SAVE_CONTRACT_CANCELLATION_COMMENT = "Hubo un problema al guardar los comentarios del contrato a cancelar";
	private static final String MESSAGE_SAVE_CONTRACT_CANCELLATION_DOCUMENT = "Hubo un problema al guardar los documentos del contrato cancelado";
	private static final String MESSAGE_FIND_CONTRACT_CANCELLATION_COMMENT = "Hubo un problema al buscar los comentarios y documentos del contrato cancelado";
	private static final String MESSAGE_DELETE_TEMPLATE_ID_DOCUMENT_ERROR = "Hubo un problema al borrar la plantilla";
	private static final String MESSAGE_ERROR_DOWNLOAD_PREVIEW = "Hubo un problema al descargar documento con vista previa.";
	private static final String MESSAGE_COUNT_TOTAL_PAGES_FOR_TRAY = "Hubo un problema al consultar el número de pagínas a mostrar en la bandeja";
	private static final String MESSAGE_SAVING_FREEZE_CONTRACT_DETAIL_ERROR = "Hubo un problema al guardar la información congelada del detalle de contrato";
	private static final String MESSAGE_FIND_CONTRACT_DETAIL_ERROR = "Hubo un problema al buscar la información del detalle de contrato";
	private static final String MESSAGE_DELETE_AUTHORIZATION_DOCUMENT_ERROR = "Hubo un problema al eliminar el documento de authorización";
	private static final String MESSAGE_FILLING_TEMPLATE_ERROR = "Hubo un problema al llenar la plantilla";
	private static final String MESSAGE_RETRIVING_IN_PROGRESS_REQUISITIONS_ERROR = "Hubo un problema al recuperar las solicitudes en progreso";
	private static final String MESSAGE_DELETE_SUPPLIER_VOBO_ERROR = "Hubo un problema al eliminar el visto bueno del proveedor";
	private static final String MESSAGE_FIND_SUPPLIER_REQUIRED_DOCUMENT_ERROR = "Hubo un problema al buscar los documentos requeridos del proveedor";
	private static final String MAIL_MATRIX_FE = "[&MatrizEF_CorreoElectronico_";
	private static final String PHONE_MATRIX_FE = "[&MatrizEF_Telefono_";
	private static final String AREA_MATRIX_FE = "[&MatrizEF_Area_";
	private static final String NAME_MATRIX_FE = "[&MatrizEF_Nombre_";
	private static final String LEVEL_MATRIX_FE = "[&MatrizEF_Nivel_";
	private static final String MAIL_MATRIX_S = "[&MatrizP_CorreoElectronico_";
	private static final String PHONE_MATRIX_S = "[&MatrizP_Telefono_";
	private static final String AREA_MATRIX_S = "[&MatrizP_Area_";
	private static final String NAME_MATRIX_S = "[&MatrizP_Nombre_";
	private static final String LEVEL_MATRIX_S = "[&MatrizP_Nivel_";
	private static final String AMPERSAND_SQUARE_BRACKET = "&]";
	private static final String MESSAGE_NO_POWER_ERROR = "El representante legal seleccionado no tiene un poder relacionado para la entidad seleccionada";
	private static final String MESSAGE_RETRIVING_USER_IN_PROGRESS_REQUISITIONS_ERROR = "Hubo un problema al recuperar las solicitudes en progreso del usuario firmado";
	private static final String MESSAGE_UPDATE_VOBO = "Hubo un problema al actualizar el VoBo Jurídico";
	private static final String MESSAGE_ERROR_SuPPLIER_PERSON = "Hubo un problema al obtener los representantes legales";
	private static final String MESSAGE_RETRIEVING_SUPPLIER_PERSON_ERROR = "Ocurrió un problema al obtener los testigos y/o representantes legales del proveedor";
	private static final String MESSAGE_SAVING_SUPPLIER_PERSON_ERROR = "Ocurrió un problema al guardar ";
	private static final String MESSAGE_SUPPLIER_NOT_FOUND = "No se encontró el proveedor";
	private static final String MESSAGE_SAVE_SUPPLIER_REQUIRED_DOCUMENT_ERROR = "Hubo un problema al guardar documentos requeridos del proveedor";
	private static final String MESSAGE_RETRIEVING_WITNESSES_ERROR = "Hubo un problema al recuperar los testigos del proveedor";
	private static final String MESSAGE_FIND_PERSONALITY_ERROR = "Hubo un problema al determinar la personalidad del proveedor";
	private static final String SUPPLIER_LAWYERS = "representantes legales del proveedor";
	private static final String LOS_TESTIGOS_DEL_PROVEEDOR = "los testigos del proveedor.";
	private static final String MESSAGE_SAVE_SUPPLIER_ERROR = "Ocurrió un problema al guardar los datos de Proveedor";
	private static final int FOLIO_LENGHT = 8;
	private static final int RFC_LENGHT = 13;
	private static final int DATE_LENGHT = 19;
	private static final int ID_LENGHT = 4;
	private static final int QR_SIZE = 150;
	private static final String QR_MASC = "*";
	private static final String IMAGE_TYPE_FILE = "PNG";
	private static final String QUICK_RESPONSE = "QRC";
	private static final String EXTENSION_FILE = ".png";
	private static final String EXTENSION_WORD_FILE = ".docx";
	private static final String EXTENSION_PDF_FILE = ".pdf";
	private static final String SEPARATOR_POINT = ".";
	private static final int CERO = 0;

	@Autowired
	private Requisitable requisitable;
	
	@Autowired
	private Qr qrDao;

	@Autowired
	private RequisitionVersionBusiness requisitionVersionBusiness;

	@Autowired
	private DocumentTypeBusiness documentTypeBusiness;

	@Autowired
	private FlowScreenActionable flowScreenActionable;

	@Autowired
	private TagFieldBusiness tagFieldBusiness;

	@Autowired
	private FlowStepable flowStepable;
	@Autowired
	private UnitBusiness unitBusiness;
	@Autowired
	private DocumentVersionBusiness documentVersionBusiness;

	@Autowired
	private DirectoryUtils directoryUtils;

	@Autowired
	private ConfigurationsBusiness configuration;

	@Autowired
	private UserSession session;

	@Autowired
	private UsersContractBusiness usersContractBusiness;

	@Autowired
	private EmailsBusiness emailsBusiness;

	@Autowired
	private AlertsBusiness alertsBusiness;

	@Autowired
	private ScreensBusiness screensBusiness;

	@Autowired
	private HolidayBusiness holidayBusiness;

	@Autowired
	private FinancialEntityBusiness financialEntityBusiness;

	@Autowired
	private RequisitionOwnersable requisitionOwnersable;

	@Autowired
	private LegalRepresentativeBusiness legalRepresentativeBusiness;

	@Autowired
	private FlowsBusiness flowBusiness;

	@Autowired
	private UsersBusiness usersBusiness;

	@Autowired
	private AreasBusiness areasBusiness;

	@Autowired
	private PersonalitiesBusiness personalitiesBusiness;

	@Autowired
	private CommentsBusiness commentsBusiness;
	
	@Autowired
	private CustomsBusiness customsBusiness;
	@Autowired
	private ModifiedClausulesBusiness clausulesBusiness;

	@Autowired
	private TractoBusiness tractoBusiness;

	@Autowired
	private RollOffBusiness rollOffBusiness;
	
	@Autowired
	private AnexoBusiness anexoBusiness;
	
	@Autowired
	private Supplierable supplierable;
	
	@Autowired
	private SupplierPersonable supplierPersonable;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveOrUpdate(final Requisition requisition) throws BusinessException {
		try {

			if (requisition.getSupplier().getRfc() != null)
				requisition.setIdSupplier(this.saveSupplier(requisition.getSupplier()));
			this.setDocumentTypeActors(requisition);
			final Integer idRequisition = this.requisitable.saveOrUpdate(requisition);
			requisition.setIdRequisition(idRequisition);
			this.saveRequisitionDocuments(requisition);
			this.requisitable.saveOrUpdate(requisition);
			this.saveRequisitionLists(requisition, idRequisition);
			this.saveRequisitionAttatchmentsFields(requisition);
			this.saveScalingMatrix(idRequisition, requisition.getScalingListSupplier());
			this.saveScalingMatrix(idRequisition, requisition.getScalingListFinancialEntity());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveOrUpdatePart1And2(final RequisitionsPartOneAndTwo requisition) throws BusinessException {
		try {
			final Integer idRequisition = this.requisitable.saveOrUpdate(requisition);
			requisition.setIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveOrUpdateDartPart2(final RequisitionDraftPart2 requisition) throws BusinessException {
		try {

			if (null != requisition.getIdDocumentType()) {
				this.setDocumentTypeActors(requisition);
			}
			final Integer idRequisition = this.requisitable.saveOrUpdateRequisitionDraftPart2(requisition);
			requisition.setIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveOrUpdateDartProem(final Requisition requisition) throws BusinessException {
		try {

			final Integer idRequisition = this.requisitable.saveOrUpdateRequisitionDraftProem(requisition);
			requisition.setIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveOrUpdateDartClausules(final Requisition requisition) throws BusinessException {
		try {

			Integer idRequisition = this.requisitable.saveOrUpdateRequisitionDraftClausules(requisition);
			this.customsBusiness.deleteCustomByIdRequisition(idRequisition);
			if (!requisition.getCustomsList().isEmpty()) {

				for (Customs customs : requisition.getCustomsList()) {
					this.customsBusiness.save(customs);
				}
			}
			this.clausulesBusiness.deleteModifiedClausulesByIdRequisition(idRequisition);
			if (!requisition.getModifiedClausulesList().isEmpty()) {

				for (int i = 0; i < requisition.getModifiedClausulesList().size(); i++) {
					requisition.getModifiedClausulesList().get(i).setIdRequisition(idRequisition);
					requisition.getModifiedClausulesList().get(i)
					.setNumberClause(OrdinalNumberUtils.getOrdinalNumber(i + 1));
					this.clausulesBusiness.save(requisition.getModifiedClausulesList().get(i));
				}
			}
			this.tractoBusiness.deleteTractoByIdRequisition(idRequisition);
			if (!requisition.getTractoList().isEmpty()) {
				for (Tracto tracto : requisition.getTractoList()) {
					tracto.setIdRequisition(idRequisition);
					tractoBusiness.save(tracto);
				}
			}
			this.rollOffBusiness.deleteRollOffByIdRequisition(idRequisition);
			if (!requisition.getRollOffList().isEmpty()) {
				for (RollOff rollOff : requisition.getRollOffList()) {
					rollOffBusiness.save(rollOff);
				}
			}

			requisition.setIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveOrUpdateDartProperty(final Requisition requisition) throws BusinessException {
		try {

			final Integer idRequisition = this.requisitable.saveOrUpdateRequisitionDraftProperty(requisition);
			requisition.setIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveOrUpdatePartThree(final RequisitionsPartThree requisition) throws BusinessException {
		try {
			if (requisition.getSupplier().getRfc() != null || requisition.getSupplier().getIdSupplier() == null) {
				requisition.getSupplier().setIdSupplier(this.saveSupplier(requisition.getSupplier()));
			}
			requisition.setIdSupplier(requisition.getSupplier().getIdSupplier());
			final Integer idRequisition = this.requisitable.saveOrUpdatePartThree(requisition);
			requisition.setIdRequisition(idRequisition);
			//  Se guardan los representantes y testigos para la solicitud
			if(requisition.getSupplierPersoList() !=null && requisition.getSupplierPersoList().size() > 0) {
				for(SupplierPersonByRequisition person: requisition.getSupplierPersoList()) {
					if(person.getId() == null || person.getId() <0) {
						if(person.getIdSupplierPerson() == null || person.getIdSupplierPerson() <0) {							
							Integer newId = this.saveSupplierPerson(requisition.getSupplier().getIdSupplier(), person.getName(), person.getType());
							
							LOG.info("\n==========================================\nGUARDANDO NUEVO REPRESENTANTE O TESTIGO\n"
									+ "Nombre :: "+person.getName()+"\n"
									+ "Tipo :: "+person.getType()+"\n"
									+ "IdSuplier :: "+requisition.getSupplier().getIdSupplier()+"\n"
									+ "IdRegistro :: "+newId);
							
							person.setIdSupplierPerson(newId);
						}
						
						
						person.setIdSupplier(requisition.getSupplier().getIdSupplier());
						person.setIdRequisition(idRequisition);
						person.setId(this.requisitable.saveSupplierPersonByIdRequisition(person));
						
						LOG.info("\n==========================================\nASIGNANDO REPRESENTANTE O TESTIGO\n"
								+ "Nombre :: "+person.getName()+"\n"
								+ "Tipo :: "+person.getType()+"\n"
								+ "IdSuplierPerson :: "+person.getIdSupplierPerson()+"\n"								
								+ "IdSuplier :: "+person.getIdSupplier()+"\n"
								+ "Id :: "+person.getId());
												
					}
				}
			}

			this.deleteListSupplierPersonByRequisition(idRequisition, requisition.getSupplier().getIdSupplier(), requisition.getSupplierPersoList());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public Integer saveSupplierPerson (final Integer idSupplier, final String name, final SupplierPersonTypeEnum type) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveSupplierPerson SAVE");
			List<SupplierPerson> people = this.supplierPersonable.findSupplierPersonByIdSupplierAndTypeAndName(idSupplier, type, name);

			if (people == null || people.isEmpty()) {
				SupplierPerson supplierPerson = createWitness(idSupplier, name, type);
				return this.supplierPersonable.save(supplierPerson);
			} else {
				if (type == SupplierPersonTypeEnum.WITNESS) {
					return people.get(0).getIdSupplierPerson();
				} else {
					SupplierPerson supplierPerson = createWitness(idSupplier, name, type);
					return this.supplierPersonable.save(supplierPerson);
				}
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR), databaseException);
			throw new BusinessException(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR),
					databaseException);
		}
	}
	
	private SupplierPerson createWitness(final Integer idSupplier, String name, SupplierPersonTypeEnum type) {
		SupplierPerson supplierPerson = new SupplierPerson();
		supplierPerson.setIdSupplier(idSupplier);
		supplierPerson.setName(name != null ? name.trim() : null);
		supplierPerson.setSupplierPersonType(type);
		supplierPerson.setActive(Boolean.TRUE);
		return supplierPerson;
	}

	private void deleteListSupplierPersonByRequisition(Integer idRequisition, Integer idSupplier, List<SupplierPersonByRequisition> list) throws DatabaseException {
		
		List<SupplierPersonByRequisition> baseList = this.requisitable.getIdsSupplierPersonByIdRequisitionDTO (idRequisition, idSupplier);
		
		HashMap<Integer, SupplierPersonByRequisition> listaFront = new HashMap<Integer, SupplierPersonByRequisition>();
		HashMap<Integer, SupplierPersonByRequisition> listaBorrar = new HashMap<Integer, SupplierPersonByRequisition>();
		
		// Lleno la lista
		for (SupplierPersonByRequisition beanFront : list) {
			listaFront.put(beanFront.getId(), beanFront);
		}
		// Elimino elementos
		for (SupplierPersonByRequisition beanBack : baseList) {
			if(!listaFront.containsKey(beanBack.getId())) {
				listaBorrar.put(beanBack.getId(), beanBack);
			}
		}		
		
		if(baseList !=null && !baseList.isEmpty()) {
			if(list == null || list.isEmpty()) {
				deleteSupplierPersonByRequisition(baseList);
				LOG.info("ELIMINA A TODOS LOS REPRESENTATES Y TESTIGOS");
			} else {
								
				 for (Map.Entry<Integer, SupplierPersonByRequisition> bean : listaBorrar.entrySet()) {
					 this.requisitable.deleteSupplierPersonByIdSupplierPerson(bean.getKey());
						LOG.info("ELIMINA ::"+bean.getValue().getName());
				 }		
			}
		}
	}

	private void deleteSupplierPersonByRequisition (List<SupplierPersonByRequisition> list) throws DatabaseException {
		for(SupplierPersonByRequisition row: list) {
			this.requisitable.deleteSupplierPersonByIdSupplierPerson(row.getId());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveOrUpdatePartFour(final RequisitionsPartFour requisition) throws BusinessException {
		try {

			final Integer idRequisition = this.requisitable.saveOrUpdatePartFour(requisition);
			requisition.setIdRequisition(idRequisition);

		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	private void setDocumentTypeActors(final Requisition requisition) throws BusinessException {
		final DocumentType documentType = this.documentTypeBusiness.findById(requisition.getIdDocumentType());
		if (requisition.getActiveActor() == null)
			requisition.setActiveActor(documentType.getActorActivo());
		if (requisition.getPassiveActor() == null)
			requisition.setPassiveActor(documentType.getActorPasivo());
	}

	private void setDocumentTypeActors(final RequisitionDraftPart2 requisition) throws BusinessException {
		final DocumentType documentType = this.documentTypeBusiness.findById(requisition.getIdDocumentType());
		if (documentType.getActorActivo() != null)
			requisition.setActorActivo(documentType.getActorActivo());
		if (documentType.getActorPasivo() != null)
			requisition.setActorPasivo(documentType.getActorPasivo());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInProgress(final Requisition requisition) throws BusinessException {
		final Boolean isNewRequisition = requisition.getIdRequisition() == null;
		this.saveOrUpdate(requisition);
		if (isNewRequisition)
			this.changeRequisitionStatus(requisition.getIdRequisition(), FlowPurchasingEnum.IN_PROGRESS);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInProgressPart1And2(final RequisitionsPartOneAndTwo requisition)
			throws BusinessException {
		Boolean isNewRequisition = false;
		if (requisition.getIdRequisition() == null) {
			requisition.setIdDocument(Constants.CERO);
			isNewRequisition = true;
		} else if (0 <= requisition.getIdRequisition()) {
			isNewRequisition = true;
		}
		this.saveOrUpdatePart1And2(requisition);
		this.saveRequisitionFinancialEntity(requisition.getIdRequisition(), requisition.getDataFinancialEntityList());
		if (isNewRequisition)
			this.changeRequisitionStatus(requisition.getIdRequisition(), FlowPurchasingEnum.IN_PROGRESS);
		    try {
                this.requisitable.saveRequisitionStage(requisition.getIdRequisition(),FlowPurchasingEnum.IN_PROGRESS.toString());
            } catch (DatabaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInDraftPart2(final RequisitionDraftPart2 requisition) throws BusinessException {
		this.saveOrUpdateDartPart2(requisition);

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInProgressPart3(final RequisitionsPartThree requisition) throws BusinessException {
		final Boolean isNewRequisition = requisition.getIdRequisition() == null;
		LOG.info("ID SUPLIER: " + requisition.getSupplier().getIdSupplier());
		requisition.setIdSupplier(requisition.getSupplier().getIdSupplier());
		this.saveOrUpdatePartThree(requisition);
		if (isNewRequisition)
			this.changeRequisitionStatus(requisition.getIdRequisition(), FlowPurchasingEnum.IN_PROGRESS);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInProgressPart4(final RequisitionsPartFour requisition) throws BusinessException {
		final Boolean isNewRequisition = requisition.getIdRequisition() == null;

		this.saveOrUpdatePartFour(requisition);
		if (isNewRequisition)
			this.changeRequisitionStatus(requisition.getIdRequisition(), FlowPurchasingEnum.IN_PROGRESS);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInProgressPart5(final Instrument requisition) throws BusinessException {
		try {
			this.requisitable.saveOrUpdate5(requisition);
			this.saveRequisitionListsInstrument(requisition);

		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInProgressPart6(final Attachment requisition) throws BusinessException {
		try {
			this.requisitable.saveOrUpdate6(requisition);
			Requisition req = new Requisition();
			if (null == requisition.getDocumentsAttachment()) {
				return;
			} else {
				req.setDocumentsAttachment(requisition.getDocumentsAttachment());
				req.setDocumentsAttachmentList(requisition.getDocumentsAttachment());
				req.setIdRequisition(requisition.getIdRequisition());
				this.saveDocumentsAttachment(req);
			}

		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInProgressPart7(final Clause requisition) throws BusinessException {
		try {
			this.requisitable.saveOrUpdate7(requisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public RequisitionComplete obtenerDetalleSolicitud(final Integer idRequisition) throws BusinessException {
		try {
			RequisitionComplete requisitionComplete = new RequisitionComplete();
			Requisition requisition = findWholeRequisitionById(idRequisition);
			requisitionComplete.setIdRequisition(idRequisition);
			requisitionComplete = mapearRequisition(requisition);
			return requisitionComplete;
		} catch (BusinessException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	/**
	 * Metodo para mapear la solicitud.
	 * 
	 * @param requisition requisition anterior
	 * @return requisition Angular
	 */
	public RequisitionComplete mapearRequisition(Requisition requisition) {
		RequisitionComplete requisitionComplete = new RequisitionComplete();
		RequisitionsPartOneAndTwo requisitionsPartOneAndTwo = new RequisitionsPartOneAndTwo();
		requisitionsPartOneAndTwo.setIdRequisition(requisition.getIdRequisition());
		requisitionsPartOneAndTwo.setIdApplicant(checkInteger(requisition.getIdApplicant()));
		/*
		 * if (null != requisition.getIdDocumentType()) {
		 * requisitionsPartOneAndTwo.setDocumentType(new DocumentType());
		 * requisitionsPartOneAndTwo.getDocumentType().setIdDocumentType(checkInteger(
		 * requisition.getDocumentType().getIdDocumentType())); } else {
		 * requisitionsPartOneAndTwo.setDocumentType(new DocumentType());
		 * requisitionsPartOneAndTwo.getDocumentType().setIdDocumentType(requisition.
		 * getIdDocumentType() != null ? checkInteger(requisition.getIdDocumentType()) :
		 * null); }
		 */
		// requisitionsPartOneAndTwo.getDocumentType().setActorActivo(checkString(requisition.getActiveActor()));
		// requisitionsPartOneAndTwo.getDocumentType().setActorPasivo(checkString(requisition.getPassiveActor()));
		// requisitionsPartOneAndTwo.setDocumentType(new CatDocumentType());
		requisitionsPartOneAndTwo.setIdDocument(checkInteger(requisition.getIdDocument()));
		requisitionsPartOneAndTwo.setIdFlow(checkInteger(requisition.getIdFlow()));
		requisitionsPartOneAndTwo.setUpdateRequisitionBy(checkInteger(requisition.getUpdateRequisitionBy()));
		requisitionsPartOneAndTwo.setIdArea(checkInteger(requisition.getIdAreaTender()));
		requisitionsPartOneAndTwo.setIdUnit(checkInteger(requisition.getIdUnit()));
		requisitionsPartOneAndTwo.setContract(checkString(requisition.getContract()));
		requisitionsPartOneAndTwo.setContractApplicant(checkString(requisition.getContractApplicant()));
		requisitionsPartOneAndTwo.setContractType(requisition.getContractType());
		if (null == requisition.getDataFinancialEntityList()) {
			LOG.info("Lista DataFinancialEntityList vacia, req: " + requisition.getIdRequisition());
			requisitionsPartOneAndTwo.setDataFinancialEntityList(new ArrayList<FinancialEntity>());
		} else {
			requisitionsPartOneAndTwo.setDataFinancialEntityList(requisition.getDataFinancialEntityList());
			if (!requisitionsPartOneAndTwo.getDataFinancialEntityList().isEmpty()) {
				requisitionsPartOneAndTwo
				.setIdCompany(requisition.getDataFinancialEntityList().get(0).getIdFinancialEntity());
			}
		}
		requisitionsPartOneAndTwo.setApplicationDate(checkString(requisition.getApplicationDate()));
		requisitionComplete.setRequisitionsPartOneAndTwo(requisitionsPartOneAndTwo);
		RequisitionsPartThree requisitionsPartThree = new RequisitionsPartThree();
		requisitionsPartThree.setIdRequisition(requisition.getIdRequisition());
		requisitionsPartThree.setSupplier(requisition.getSupplier());
		requisitionsPartThree.setIdSupplier(checkInteger(requisition.getIdSupplier()));
		if (null == requisition.getIdSupplier()) {
			LOG.info("REVISA getIdSupplier, req: " + requisition.getIdRequisition());
		}
		if(requisition.getSupplierLegaList()!=null && !requisition.getSupplierLegaList().isEmpty()) {
			requisitionsPartThree.setSupplierPersoList(requisition.getSupplierLegaList());
		} else {
			requisitionsPartThree.setSupplierPersoList(null);
		}
		requisitionComplete.setRequisitionsPartThree(requisitionsPartThree);

		RequisitionsPartFour requisitionsPartFour = new RequisitionsPartFour();
		requisitionsPartFour.setIdRequisition(requisition.getIdRequisition());
		requisitionsPartFour.setSupplierAtention(checkString(requisition.getSupplierAtention()));
		requisitionsPartFour.setSupplierPhone(checkString(requisition.getSupplierPhone()));
		requisitionsPartFour.setSupplierAccountNumber(checkString(requisition.getSupplierAccountNumber()));
		requisitionComplete.setRequisitionsPartFour(requisitionsPartFour);

		Instrument instrument = new Instrument();
		instrument.setIdRequisition(requisition.getIdRequisition());
		instrument.setValidity(requisition.getValidity());
		instrument.setAutomaticRenewal(requisition.getAutomaticRenewal());
		instrument.setRenewalPeriods(checkInteger(requisition.getRenewalPeriods()));
		instrument.setValidityStartDate(checkString(requisition.getValidityStartDate()));
		instrument.setValidityEndDate(checkString(requisition.getValidityEndDate()));
		instrument.setFinancialEntityAddress(checkString(requisition.getFinancialEntityAddress()));
		if (null == requisition.getDataFinancialEntityList()) {
			LOG.info("Lista DataFinancialEntityList vacia, req: " + requisition.getIdRequisition());
			instrument.setDataFinancialEntityList(new ArrayList<FinancialEntity>());
		} else {
			instrument.setDataFinancialEntityList(requisition.getDataFinancialEntityList());
		}
		if (null == requisition.getLegalRepresentativesList()) {
			LOG.info("Lista LegalRepresentativesList vacia, req: " + requisition.getIdRequisition());
			instrument.setLegalRepresentativesList(new ArrayList<LegalRepresentative>());
		} else {
			instrument.setLegalRepresentativesList(requisition.getLegalRepresentativesList());
		}
		instrument.setServiceDescription(checkString(requisition.getServiceDescription()));
		instrument.setTechnicalDetails(checkString(requisition.getTechnicalDetails()));
		requisitionComplete.setInstrument(instrument);

		Attachment attachment = new Attachment();
		attachment.setIdRequisition(requisition.getIdRequisition());
		attachment.setBusinessReasonMonitoringPlan(checkString(requisition.getBusinessReasonMonitoringPlan()));
		attachment.setAttatchmentOthersName(checkString(requisition.getAttatchmentOthersName()));
		attachment.setAttatchmentDeliverables(requisition.getAttatchmentDeliverables());
		attachment.setAttchmtServiceLevelsMeasuring(requisition.getAttchmtServiceLevelsMeasuring());
		attachment.setAttatchmentPenalty(requisition.getAttatchmentPenalty());
		attachment.setAttatchmentScalingMatrix(requisition.getAttatchmentScalingMatrix());
		attachment.setAttatchmentCompensation(requisition.getAttatchmentCompensation());
		attachment.setAttchmtBusinessMonitoringPlan(requisition.getAttchmtBusinessMonitoringPlan());
		attachment.setAttchmtImssInfoDeliveryReqrmts(requisition.getAttchmtImssInfoDeliveryReqrmts());
		attachment.setAttatchmentInformationSecurity(requisition.getAttatchmentInformationSecurity());
		if (null == requisition.getAttachmentListDocument()) {
			LOG.info("Lista AttachmentListDocument vacia, req: " + requisition.getIdRequisition());
			attachment.setDocumentsAttachment(new ArrayList<FileUploadInfo>());
		} else {
			if (!requisition.getAttachmentListDocument().isEmpty()) {
				attachment.setDocumentsAttachment(mapAttachment(requisition.getAttachmentListDocument()));
			} else {
				LOG.info("Lista AttachmentListDocument vacia, req: " + requisition.getIdRequisition());
				attachment.setDocumentsAttachment(new ArrayList<FileUploadInfo>());
			}
		}
		requisitionComplete.setAttachment(attachment);
		Clause clause = new Clause();
		clause.setIdRequisition(requisition.getIdRequisition());
		// clause.setContractValidity(checkString(requisition.getContractValidity()));
		clause.setValidityStartDate(checkString(requisition.getValidityStartDate()));
		clause.setValidityEndDate(checkString(requisition.getValidityEndDate()));
		clause.setContractObject(checkString(requisition.getContractObject()));
		clause.setConsiderationClause(checkString(requisition.getConsiderationClause()));
		clause.setConsiderationAmount(checkString(requisition.getConsiderationAmount()));
		clause.setContractObjectClause(checkString(requisition.getContractObjectClause()));
		clause.setClausulaFormaPago(checkString(requisition.getClausulaFormaPago()));
		clause.setDepositAmount(checkString(requisition.getDepositAmount()));
		// clause.setContractType(requisition.getContractType());
		requisitionComplete.setClause(clause);

		// Pasar datos del usuario
		if (requisition.getApplicant() != null && requisition.getApplicant().getIdUser() != null
				&& requisition.getApplicant().getIdUser() > 0) {
			requisitionComplete.setApplicant(requisition.getApplicant());
		} else {
			requisitionComplete.setApplicant(null);
		}
		return requisitionComplete;
	}

	private List<FileUploadInfo> mapAttachment(List<Version> attachmentListDocument) {
		List<FileUploadInfo> list = new ArrayList<FileUploadInfo>();
		for (Version att : attachmentListDocument) {
			FileUploadInfo bean = new FileUploadInfo();
			bean.setDocumentName(att.getFileName());
			bean.setFilePath(att.getDocumentPath());
			bean.setFileNew(false);
			bean.setIdFile(att.getIdDocument());
			list.add(bean);
		}
		return list;
	}

	private static Integer checkInteger(Integer inte) {
		if (null == inte) {
			return null;
		}
		return inte;
	}

	private static String checkString(String str) {
		if (null == str) {
			return "";
		}
		return str;
	}

	private void saveRequisitionListsInstrument(final Instrument requisition) throws BusinessException {
		this.saveRequisitionFinancialEntity(requisition.getIdRequisition(), requisition.getDataFinancialEntityList());
		List<Integer> legalReprentativeIdList = new ArrayList<Integer>();
		if (null == requisition.getLegalRepresentativesList()) {
			return;
		} else {
			for (LegalRepresentative legal : requisition.getLegalRepresentativesList()) {
				legalReprentativeIdList.add(legal.getIdLegalRepresentative());
			}
			this.saveRequisitionLegalRepresentative(requisition.getIdRequisition(), legalReprentativeIdList);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendRequisition(final Requisition requisition) throws BusinessException {
		this.saveOrUpdate(requisition);
		this.saveChangeRequisitionStatus(requisition.getIdRequisition(), requisition.getFlowScreenActionParams());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendRequisition(final RequisitionAngular requisition) throws BusinessException {
		// this.saveOrUpdate(requisition);
		this.saveChangeRequisitionStatus(requisition.getIdRequisition(), requisition.getFlow());
	}

	private void saveRequisitionDocuments(final Requisition requisition) throws BusinessException {
		if (requisition.getAuthorizationDocument() != null)
			requisition.setAuthorizationDocumentIdDoc(
					this.saveDocument(requisition.getAuthorizationDocument(), requisition.getIdRequisition()));
		requisition.setImssCeduleNotGivenIdDocument(
				this.saveDocument(requisition.getImssCeduleNotGivenDocument(), requisition.getIdRequisition()));
		requisition.setAdvanceBailNotNeededIdDocument(
				this.saveDocument(requisition.getAdvanceBailNotNeededDocument(), requisition.getIdRequisition()));
		requisition.setFulfillmentBailNeedNoIdDoc(
				this.saveDocument(requisition.getFulfillmentBailNotNeededDocument(), requisition.getIdRequisition()));
		requisition.setFidelityBailNeedNoIdDoc(
				this.saveDocument(requisition.getFidelityBailNotNeededDocument(), requisition.getIdRequisition()));
		requisition.setContingencyBailNeedNoIdDoc(
				this.saveDocument(requisition.getContingencyBailNotNeededDocument(), requisition.getIdRequisition()));
		requisition.setCivilRespInsurBailNeedNoIdDoc(this.saveDocument(
				requisition.getCivilResponsabilityInsuranceBailNotNeededDocument(), requisition.getIdRequisition()));
		requisition.setHiddenVicesBailIdDoc(
				this.saveDocument(requisition.getHiddenVicesBailDocument(), requisition.getIdRequisition()));
	}

	private Integer saveDocument(final FileUploadInfo file, final Integer idRequisition) throws BusinessException {
		return file != null ? this.versionRequisitionDocument(idRequisition, file) : null;
	}

	private void saveRequisitionLists(final Requisition requisition, final Integer idRequisition)
			throws BusinessException {
		this.saveRequisitionApprovalAreas(idRequisition, requisition.getApprovalAreasList());
		this.saveRequisitionAuthorizationDgas(idRequisition, requisition.getAuthorizationDgasList());
		this.saveRequisitionFinancialEntity(idRequisition, requisition.getDataFinancialEntityList());
		// this.saveDataFinancialEntity(idRequisition,
		// requisition.getDataFinancialEntityList());
		this.saveRequisitionLegalRepresentative(idRequisition, requisition.getLegalReprentativeIdList());
		this.saveUsersToVoBo(idRequisition, requisition.getUsersToVoboList());
		this.saveRequisitionFinantialEntityWitness(idRequisition, requisition.getFinancialEntityWitnessesList());
	}

	private String getTrayNameByFlowStatus(final FlowPurchasingEnum flowStatus) throws BusinessException {
		return this.screensBusiness.findNameByFlowStatus(flowStatus);
	}

	public void saveRequisitionEditedData(final Requisition requisition) throws BusinessException {
		this.saveApplicant(requisition.getIdRequisition(), requisition.getIdApplicant());
		this.saveRequisitionAuthorizationDgas(requisition.getIdRequisition(), requisition.getAuthorizationDgasList());
		this.saveRequisitionFinancialEntity(requisition.getIdRequisition(), requisition.getDataFinancialEntityList());
		// this.saveDataFinancialEntity(requisition.getIdRequisition(),
		// requisition.getDataFinancialEntityList());
		this.saveRequisitionLegalRepresentative(requisition.getIdRequisition(),
				requisition.getLegalReprentativeIdList());
		this.saveUsersToVoBo(requisition.getIdRequisition(), requisition.getUsersToVoboList());
		if (requisition.getIdLawyer() != null)
			this.saveLawyer(requisition.getIdRequisition(), requisition.getIdLawyer());
		if (requisition.getIdEvaluator() != null)
			this.saveRequisitionEvaluator(requisition);
	}

	public List<Requisition> findRequisitionByManyParameters(final Requisition requisition) throws BusinessException {
		try {
			return this.requisitable.findRequisitionByManyParameters(requisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
		}
	}

	public List<Requisition> findRequisitionClosed(final Requisition requisitionParameter) throws BusinessException {
		try {
			return this.requisitable.findRequisitionClosed(requisitionParameter,
					-Integer.valueOf(this.configuration.findByName("BEFORE_DAYS_EXPIRATION_ALERT")),
					Integer.valueOf(this.configuration.findByName("AFTER_DAYS_EXPIRATION_ALERT")));
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
		}
	}

	public List<Requisition> paginatedFindRequisitionsClosed(final Requisition requisitionParameter,
			final Integer pageNumber) throws BusinessException {
		try {
			final Integer itemsNumber = this.configuration.getPaginationItemsNumberParameter();
			return this.requisitable.findPaginatedRequisitionsClosed(requisitionParameter,
					-Integer.valueOf(this.configuration.findByName("BEFORE_DAYS_EXPIRATION_ALERT")),
					Integer.valueOf(this.configuration.findByName("AFTER_DAYS_EXPIRATION_ALERT")), pageNumber,
					itemsNumber);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
		}
	}

	public List<Requisition> findPaginatedContracts(final Requisition requisitionParameter, final Integer pageNumber)
			throws BusinessException {
		try {
			final Integer itemsNumber = this.configuration.getPaginationItemsNumberParameter();
			return this.requisitable.findPaginatedContracts(requisitionParameter, pageNumber, itemsNumber);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
		}
	}

	public Integer returnTotalPagesShowRequisitionsClosed(final Requisition requisition)
			throws NumberFormatException, BusinessException {
		try {
			return this.configuration.totalPages(this.requisitable.countTotalRowsRequisitionsClosed(requisition,
					-Integer.valueOf(this.configuration.findByName("BEFORE_DAYS_EXPIRATION_ALERT")),
					Integer.valueOf(this.configuration.findByName("AFTER_DAYS_EXPIRATION_ALERT"))));
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
		}
	}

	public void changeAttendStatus(final Integer idRequisition, final Boolean isExpiredAttended)
			throws BusinessException {
		try {
			if (isExpiredAttended)
				this.requisitable.changeAttendStatus(idRequisition, false);
			else
				this.requisitable.changeAttendStatus(idRequisition, true);
			LOG.info("Cambio el estatus de la solicitud " + idRequisition + " como "
					+ (isExpiredAttended ? "Atendido" : "No Atendido") + " por el usuario: "
					+ this.session.getUsuario().getFullName());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_LAWYER_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVE_LAWYER_ERROR, databaseException);
		}
	}

	public void saveLawyer(final Integer idRequisition, final Integer idLawyer) throws BusinessException {
		try {
			this.requisitable.saveLawyer(idRequisition, idLawyer);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_LAWYER_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVE_LAWYER_ERROR, databaseException);
		}
	}

	public void saveApplicant(final Integer idRequisition, final Integer idApplicant) throws BusinessException {
		try {
			this.requisitable.saveApplicant(idRequisition, idApplicant);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_APPLICANT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVE_APPLICANT_ERROR, databaseException);
		}
	}

	private Integer saveSupplier(final Supplier supplierParameter) throws BusinessException {
		LOG.info("supplierParameter.getRfc(): " + supplierParameter.getRfc());
		final Integer idSupplier = this.saveOrUpdate(supplierParameter);
		return idSupplier;
	}
	
	public Integer saveOrUpdate(final Supplier supplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveOrUpdate");
			final Integer idSupplier = this.supplierable.saveOrUpdate(supplier);
			this.saveSupplierPersonsList(idSupplier, supplier.getSupplierPersonList());
			this.saveSupplierPersonsList(idSupplier, supplier.getSupplierWitnessList(), SupplierPersonTypeEnum.WITNESS);
			final List<RequiredDocument> list = this.supplierRequiredDocument(supplier.getSupplierRequiredDocument(), idSupplier);
			supplier.setRequiredDocumentList(list);
			supplier.setIdSupplier(idSupplier);
			for (RequiredDocument getRequiredDocument : supplier.getRequiredDocumentList())
				this.saveSupplierRequiredDocument(idSupplier, getRequiredDocument);
			return idSupplier;
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_SAVE_SUPPLIER_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_SAVE_SUPPLIER_ERROR, dataBaseException);
		}
	}
	

	public List<SupplierPerson> findWitnessesByIdSupplier(final Integer idSupplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findWitnessesByIdSupplier");
			return this.supplierPersonable.findSupplierPersonsByIdSupplierAndType(idSupplier,
					SupplierPersonTypeEnum.WITNESS);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_WITNESSES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_WITNESSES_ERROR, databaseException);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveSupplierPersonsList(final Integer idSupplier, final List<String> supplierPersonsList,
			final SupplierPersonTypeEnum supplierPersontype) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveSupplierPersonsList SAVE");
			List<SupplierPerson> people = this.findWitnessesByIdSupplier(idSupplier);

			if (people == null || people.isEmpty()) {
				for (String supplierPersonName : supplierPersonsList) {
					final SupplierPerson supplierPerson = createWitness(idSupplier, supplierPersonName, supplierPersontype);
					this.supplierPersonable.save(supplierPerson);
				}
			} else {

				if (supplierPersonsList != null && !supplierPersonsList.isEmpty()) {
					supplierPersonsList.stream().forEach(sup -> {
						if(!getDuplicate(people, sup)){
							final SupplierPerson supplierPerson = createWitness(idSupplier, sup, supplierPersontype);
							try {
								this.supplierPersonable.save(supplierPerson);
							} catch (DatabaseException e) {
								LOG.error(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR), e);
							}
						}
					});
				}
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR), databaseException);
			throw new BusinessException(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR),
					databaseException);
		}
	}
	
	private boolean getDuplicate(List<SupplierPerson> people, String name) {
		List<SupplierPerson> list = people.stream().filter(p-> p.getName().trim().equals(name.trim())).collect(Collectors.toList());
		return list!= null && list.size() > 0 ? true : false;
	}
	
	private void updateSupplier(SupplierPerson person) throws DatabaseException {
		this.supplierPersonable.updateSupplier(person);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveSupplierPersonsList(final Integer idSupplier, final List<SupplierPerson> supplierPersonsList)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveSupplierPersonsList LEGAL");
			for (SupplierPerson supplierPerson : supplierPersonsList) {
				if (supplierPerson.getIdSupplierPerson() != null && supplierPerson.getIdSupplierPerson() > 0) {
					this.updateSupplier(supplierPerson);
				} else {
					if (supplierPerson.getName() != null) {
						supplierPerson.setIdSupplier(idSupplier);
						supplierPerson.setActive(Boolean.TRUE);
						this.supplierPersonable.save(supplierPerson);
					}
				}
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(SUPPLIER_LAWYERS), databaseException);
			throw new BusinessException(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(SUPPLIER_LAWYERS),
					databaseException);
		}
	}
	
	private void saveSupplierRequiredDocument(final Integer idSupplier, final RequiredDocument getRequiredDocument)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveSupplierRequiredDocument  DOC ::"+getRequiredDocument.getName());
			this.supplierable.deleteSupplierRequiredDocument(idSupplier, getRequiredDocument.getIdRequiredDocument());
			this.supplierable.saveRequiredDocument(idSupplier, getRequiredDocument.getIdRequiredDocument(),
					getRequiredDocument.getIdDocument());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_SUPPLIER_REQUIRED_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVE_SUPPLIER_REQUIRED_DOCUMENT_ERROR, databaseException);
		}
	}

	public void saveChangeRequisitionStatus(final Integer idRequisitionParam, final FlowScreenAction flowScreenActionParameter) throws BusinessException {
		this.isRequisitonCancelled(idRequisitionParam);
		final RequisitionStatusTurn requisitionStatusTurn = new RequisitionStatusTurn();
		requisitionStatusTurn.setIdRequisition(idRequisitionParam);
		requisitionStatusTurn.setStatus(FlowPurchasingEnum.valueOf(this.findNextStatus(flowScreenActionParameter)));
		this.changeRequisitionStatus(idRequisitionParam, requisitionStatusTurn.getStatus());
		this.saveRequisitionStatusTurn(idRequisitionParam, requisitionStatusTurn.getStatus());
		requisitionStatusTurn.setTurn(this.findCurrentTurnByIdRequisition(idRequisitionParam));
		requisitionStatusTurn.setAttentionDays(this.getRequisitionTurnAttentiondays(idRequisitionParam,
				requisitionStatusTurn.getStatus(), requisitionStatusTurn.getTurn()));
		requisitionStatusTurn.setStage(this.saveStage(idRequisitionParam, requisitionStatusTurn));
		this.saveRequisitionStatusTurnAttentionDaysAndStage(requisitionStatusTurn);
		this.sendMails(idRequisitionParam, requisitionStatusTurn);
		LOG.info("IdSolicitud: " + idRequisitionParam + " Estatus: " + requisitionStatusTurn.getStatus());
	}

	private String saveStage(final Integer idRequisitionParam, final RequisitionStatusTurn requisitionStatusTurn)
			throws BusinessException {
		try {
			final String nextStage = this.screensBusiness.findStageByStatusAndTurn(requisitionStatusTurn.getStatus(),
					requisitionStatusTurn.getTurn());
			final String currentStage = this.requisitable.findRequisitionStage(idRequisitionParam);
			return this.saveStageWhenDifferent(idRequisitionParam, nextStage, currentStage);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_NEXT_STAGE_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_NEXT_STAGE_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_REQUISITION_NO_LONGER_EXISTS, emptyResultException);
			throw new BusinessException(MESSAGE_REQUISITION_NO_LONGER_EXISTS, emptyResultException);
		}
	}

	private String saveStageWhenDifferent(final Integer idRequisitionParam, final String nextStage,
			final String currentStage) throws DatabaseException {
		if (nextStage != currentStage) {
			this.requisitable.saveRequisitionStage(idRequisitionParam, nextStage);
			return nextStage;
		}
		return currentStage;
	}

	private void sendMails(final Integer idRequisitionParam, final RequisitionStatusTurn requisitionStatusTurn) {
		try {
			if (this.configuration.getIsEmailsEndActive())
				this.processSendEmail(idRequisitionParam, requisitionStatusTurn.getStatus());
		} catch (ConfigurationException configurationException) {
			LOG.error("Hubo un problema al determinar si está activo/inactivo el envío de correos",
					configurationException);
		}
	}

	public Integer findCurrentTurnByIdRequisition(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findCurrentTurnByIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_CURRENT_REQUISITION_TURN_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_CURRENT_REQUISITION_TURN_ERROR, databaseException);
		}
	}

	public Integer getRequisitionTurnAttentiondays(final Integer idRequisition, final FlowPurchasingEnum status,
			final Integer turn) throws BusinessException {
		final Integer validityDays = this.alertsBusiness.findValidityDaysByRequisitionFlowStatusTurn(idRequisition,
				status, turn);
		if (validityDays != null) {
			final LocalDate startDate = new LocalDate();
			final LocalDate endDate = new LocalDate().plusDays(validityDays);
			return validityDays
					+ this.calculateNonWorkingDays(startDate.plusDays(1), endDate, this.holidayBusiness.findAllDates());
		}
		return validityDays;
	}

	private Integer calculateNonWorkingDays(final LocalDate startDateParameter, final LocalDate endDateParameter,
			final List<Date> holidayDates) {
		final Date startDate = startDateParameter.toDate();
		final Date endDate = endDateParameter.toDate();
		final Integer weekendDays = DateUtils.weekendDaysBetweenDates(startDate, endDate);
		final Integer holidayDays = DateUtils.daysBetweenDatesFromList(startDate, endDate, holidayDates);
		Integer nonWorkingdaysTotal = weekendDays + holidayDays;
		if (nonWorkingdaysTotal > 0)
			nonWorkingdaysTotal += this.calculateNonWorkingDays(endDateParameter.plusDays(1),
					endDateParameter.plusDays(nonWorkingdaysTotal), holidayDates);
		return nonWorkingdaysTotal;
	}

	private void processSendEmail(final Integer idRequisition, final FlowPurchasingEnum flowStatus) {
		try {
			final List<AlertFlowStep> emailsToAlert = this.alertsBusiness.getEmailsToAlertsByStep(idRequisition,
					flowStatus);
			this.sendMailsWhenTherAreReceptors(flowStatus, emailsToAlert);
		} catch (BusinessException businessException) {
			LOG.error("Hubo un problema al enviar los correos", businessException);
		}
	}

	private void sendMailsWhenTherAreReceptors(final FlowPurchasingEnum flowStatus,
			final List<AlertFlowStep> emailsToAlert) throws BusinessException {
		if (emailsToAlert != null && emailsToAlert.size() > 0)
			this.emailsBusiness.sendEmail(this.createEmailContent(flowStatus, emailsToAlert.get(0)),
					this.getEmailsTo(emailsToAlert));
	}

	private String[] getEmailsTo(final List<AlertFlowStep> emailsToAlert) throws BusinessException {
		final String[] emails = new String[emailsToAlert.size()];
		Integer i = 0;
		for (AlertFlowStep alertFlowStep : emailsToAlert)
			emails[i++] = alertFlowStep.getEmail();
		return emails;
	}

	private EmailContent createEmailContent(final FlowPurchasingEnum flowStatus, final AlertFlowStep alertFlowStep)
			throws BusinessException {
		final EmailContent emailContent = new EmailContent();
		emailContent.setSubject(this.configuration.findByName("CHANGE_STATUS_SUBJECTEMAIL"));
		emailContent.setFieldOne("Solicitud:");
		emailContent.setFieldOneDescription(alertFlowStep.getIdRequisition().toString());
		emailContent.setFieldTwo("Tipo de Contrato:");
		emailContent.setFieldTwoDescription(
				alertFlowStep.getDocumentTypeName() == null ? "" : alertFlowStep.getDocumentTypeName());
		emailContent.setFieldThree("Proveedor:");
		emailContent.setFieldThreeDescription(
				alertFlowStep.getCommercialName() == null ? "" : alertFlowStep.getCommercialName());
		emailContent.setContent(this.configuration.findByName("CHANGE_STATUS_CONTENTEMAIL").replace("#",
				this.getTrayNameByFlowStatus(flowStatus)));
		emailContent.setBrand(this.configuration.findByName("EMAIL_NOTIFICATION_BRAND"));
		return emailContent;
	}

	public void saveRequisitionFinancialEntity(final Integer idRequisition, final List<FinancialEntity> entities)
			throws BusinessException {
		try {
			this.requisitable.deleteRequisitionFinancialEntity(idRequisition);
			for (FinancialEntity getFinancialEntity : entities) {
				getFinancialEntity.setIdRequisition(idRequisition);
				this.requisitable.saveRequisitionFinancialEntity(getFinancialEntity);
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_FINANTIAL_ENTITIES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_FINANTIAL_ENTITIES_ERROR, databaseException);
		}
	}

	public void saveDataFinancialEntity(final Integer idRequisition, final List<FinancialEntity> financialEntityList)
			throws BusinessException {
		try {
			this.requisitable.deleteFinancialEntityByRequisition(idRequisition);
			for (FinancialEntity financialentity : financialEntityList)
				this.requisitable.saveFinancialEntityByRequisition(idRequisition, financialentity);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_DATA_FINANTIAL_ENTITIES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_DATA_FINANTIAL_ENTITIES_ERROR, databaseException);
		}
	}

	public void changeRequisitionStatus(final Integer idRequisition, final FlowPurchasingEnum status)
			throws BusinessException {
		try {
			this.requisitable.changeRequisitionStatus(idRequisition, status);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_REQUISITION_STATUS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVE_REQUISITION_STATUS_ERROR, databaseException);
		}
	}

	public void changeRequisitionStatusToCancelled(final Integer idRequisition) throws BusinessException {
		try {
			this.requisitable.changeRequisitionStatusToCancelled(idRequisition);
		//	this.requisitable.saveRequisitionStage(idRequisition,FlowPurchasingEnum.CANCELLED.toString());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_REQUISITION_STATUS_CANCELLED_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVE_REQUISITION_STATUS_CANCELLED_ERROR, databaseException);
		}
	}

	public void isRequisitonCancelled(final Integer idRequisition) throws BusinessException {
		try {
			if (requisitable.isRequisitionCancelled(idRequisition))
				throw new BusinessException("La solicitud ha sido cancelada previamente");
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_CANCELL_CHECK_QUERY_ERROR, databaseException);
			throw new BusinessException(MESSAGE_CANCELL_CHECK_QUERY_ERROR, databaseException);
		}
	}

	public Requisition findById(final Integer idRequisition) throws BusinessException {
		try {
			LOG.info(this.getClass().getSimpleName() + " -> findById : " + idRequisition);
			final Requisition requisitonResponse = this.requisitable.findById(idRequisition);
			this.addRequisitionSupplier(requisitonResponse);
			this.addRequisitionLawyer(requisitonResponse);
			this.addDocumentCat(requisitonResponse);
			this.addDocumentType(requisitonResponse);
			requisitonResponse.setLegalRepresentativesList(this.findRequisitionLegalRepresentatives(idRequisition));
			final List<Customs> customsList = this.customsBusiness.getCustomsByIdRequisition(idRequisition);
			requisitonResponse.setCustomsList(customsList);
			final List<ModifiedClausules> modifiedClausulesList = clausulesBusiness
					.getModifiedClausulesByIdRequisition(idRequisition);
			requisitonResponse.setModifiedClausulesList(modifiedClausulesList);
			final List<FinancialEntity> financialEntitiesList = this.financialEntityBusiness
					.findDataFinantialEntityRequisition(idRequisition);
			requisitonResponse.setDataFinancialEntityList(financialEntitiesList);
			final List<Tracto> tractoList = tractoBusiness.getTractoByIdRequisition(idRequisition);
			requisitonResponse.setTractoList(tractoList);
			final List<RollOff> rollOffList = rollOffBusiness.getRollOffByIdRequisition(idRequisition);
			requisitonResponse.setRollOffList(rollOffList);
			// this.addDocumentType(requisitonResponse);
			if (requisitonResponse.getIdDocumentType() != null) {
				requisitonResponse.setFinancialEntitiesSelectionLimit(
						this.documentTypeBusiness.findById(requisitonResponse.getIdDocumentType()).getSelectionLimit());
			}
			requisitonResponse.setApplicant(this.usersBusiness.findByUserId(requisitonResponse.getIdApplicant()));
			return requisitonResponse;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_REQUISITION_BY_ID_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_REQUISITION_BY_ID_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_REQUISITION_NO_LONGER_EXISTS, emptyResultException);
			throw new BusinessException(MESSAGE_REQUISITION_NO_LONGER_EXISTS, emptyResultException);
		}
	}
	public DocumentDS getUser(final Integer idRequisition) throws BusinessException {
		try {
			LOG.info(this.getClass().getSimpleName() + " -> getUser() : " + idRequisition);
			final DocumentDS requisitonResponse = this.requisitable.getUser(idRequisition);
			
			
			return requisitonResponse;
		} catch (DatabaseException e) {
			LOG.error("Error al obtener datos para generar el Excel de Solicitudes",e);
			throw new BusinessException("Error al obtener datos para generar el Excel de Solicitudes", e);
		}
//		} catch (DatabaseException databaseException) {
//			LOG.error("///////////TRONO GETUSER CATCH 1");
//			LOG.error(MESSAGE_RETRIEVING_REQUISITION_BY_ID_ERROR, databaseException);
//			throw new BusinessException(MESSAGE_RETRIEVING_REQUISITION_BY_ID_ERROR, databaseException);
//		} catch (EmptyResultException emptyResultException) {
//			LOG.error("///////////TRONO GETUSER CATCH 2");
//			LOG.error(MESSAGE_REQUISITION_NO_LONGER_EXISTS, emptyResultException);
//			throw new BusinessException(MESSAGE_REQUISITION_NO_LONGER_EXISTS, emptyResultException);
//		}
	}

	public Requisition findByIdInProgress(final Integer idRequisition) throws BusinessException {
		try {
			LOG.info(this.getClass().getSimpleName() + " -> findByIdInProgress : " + idRequisition);
			final Requisition requisitonResponse = this.requisitable.findByIdInProgress(idRequisition);
			this.addRequisitionSupplier(requisitonResponse);
			this.addRequisitionLawyer(requisitonResponse);
			/// this.addDocumentCat(requisitonResponse);
			// requisitonResponse.setFinancialEntitiesSelectionLimit(
			// this.documentTypeBusiness.findById(requisitonResponse.getIdDocumentType()).getSelectionLimit());
			return requisitonResponse;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_REQUISITION_BY_ID_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_REQUISITION_BY_ID_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_REQUISITION_NO_LONGER_EXISTS, emptyResultException);
			throw new BusinessException(MESSAGE_REQUISITION_NO_LONGER_EXISTS, emptyResultException);
		}
	}

	public Requisition findWholeRequisitionById(final Integer idRequisition) throws BusinessException {
		LOG.info(this.getClass().getSimpleName() + " -> findWholeRequisitionById : " + idRequisition);
		final Requisition requisition = this.findByIdInProgress(idRequisition);
		requisition.setSupplierLegalRepresentativesList(
				this.findLegalRepresentativesByIdSupplier(requisition.getIdSupplier()));
		if (requisition.getUpdateRequisitionBy() != null)
			requisition.setUpdateByUser(this.usersBusiness.findByUserId(requisition.getUpdateRequisitionBy()));
		requisition.setVersionNumber("Versión actual");
		requisition.setApplicant(this.usersBusiness.findByUserId(requisition.getIdApplicant()));
		requisition.setApprovalAreasActiveList(this.findApprovalAreas(idRequisition));
		requisition.setUsersToVoboUserList(this.findUsersToVoBo(idRequisition));
		final List<FinancialEntity> financialEntitiesList = this.financialEntityBusiness
				.findDataFinantialEntityRequisition(idRequisition);
		requisition.setDataFinancialEntityList(financialEntitiesList);
		if (financialEntitiesList.size() > 0)
			requisition.setFinancialEntity(financialEntitiesList.get(0));
		requisition.setRequiredDocumentBySupplier(this.findSupplierRequiredDocument(requisition.getIdSupplier()));
		requisition.setLegalRepresentativesList(this.findRequisitionLegalRepresentatives(requisition.getIdRequisition()));
		requisition.setAttachmentListDocument(this.findDocumentsAttachment(idRequisition));
		requisition.setFinancialEntityWitnessesList(this.findRequisitionFinantialEntityWitnesses(idRequisition));
		// Se obtienen los Representantes legales y testigos
		requisition.setSupplierLegaList(this.getIdsSupplierPersonByIdRequisitionDTO(idRequisition, requisition.getIdSupplier()));
		return requisition;
	}
	

	public List<SupplierPerson> findLegalRepresentativesByIdSupplier(final Integer idSupplier)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findLegalRepresentativesByIdSupplier");
			return this.supplierPersonable.findSupplierPersonsByIdSupplierAndType(idSupplier,
					SupplierPersonTypeEnum.LEGALREPRESENTATIVE);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	public List<RequiredDocumentBySupplier> findSupplierRequiredDocument(final Integer idSupplier)
			throws BusinessException {
		LOG.info(this.getClass().getSimpleName() + " -> findSupplierRequiredDocument : " + idSupplier);
		try {
			return this.requisitable.findRequiredDocumentsBySupplier(idSupplier);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_SUPPLIER_REQUIRED_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_SUPPLIER_REQUIRED_DOCUMENT_ERROR, databaseException);
		}
	}

	private void addRequisitionSupplier(final Requisition requisitonResponse) throws BusinessException {
		if (requisitonResponse.getIdSupplier() != null) {
			requisitonResponse.setSupplier(this.findByIdSupplier(requisitonResponse.getIdSupplier()));
		}
	}

	private void addRequisitionLawyer(final Requisition requisitonResponse) throws BusinessException {
		if (requisitonResponse.getIdLawyer() != null) {
			requisitonResponse.setLawyer(this.usersBusiness.findByUserId(requisitonResponse.getIdLawyer()));
		}
	}
	
	public Supplier findByIdSupplier(final Integer idSupplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findById");
			final Supplier supplier = this.supplierable.findById(idSupplier);
			supplier.setWitnessesList(this.findWitnessesByIdSupplier(idSupplier));
			supplier.setLegalRepresentativesList(this.findLegalRepresentativesByIdSupplier(idSupplier));
			supplier.setSupplierPerson(this.findLegalRepresentativesByIdSupplier(idSupplier).size() > 0
					? this.findLegalRepresentativesByIdSupplier(idSupplier).get(0)
							: new SupplierPerson());
			return supplier;
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener Proveedor por Id", databaseException);
			throw new BusinessException("Error al obtener Proveedores por id", databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_SUPPLIER_NOT_FOUND, emptyResultException);
			throw new BusinessException(MESSAGE_SUPPLIER_NOT_FOUND, emptyResultException);
		}
	}
	

	private void addDocumentType(final Requisition requisitonResponse) throws BusinessException {
		if (requisitonResponse.getIdDocumentType() != null) {
			requisitonResponse
			.setDocumentType(this.documentTypeBusiness.findById(requisitonResponse.getIdDocumentType()));
		}
	}

	private void addDocumentCat(final Requisition requisitonResponse) throws BusinessException {
		if (requisitonResponse.getIdDocument() != null) {
			requisitonResponse.setCatDocumentType(
					this.documentTypeBusiness.findByIdDocumentCat(requisitonResponse.getIdDocument()));
		}
	}

	public List<Integer> findRequisitionsToCreateOneFromAnother(final String idRequisitionParameter,
			final String documentTypeParameter, final String supplierParameter) throws BusinessException {
		try {
			final List<Integer> listResponse = this.requisitable.findRequisitionsToCreateOneFromAnother(
					idRequisitionParameter, documentTypeParameter, supplierParameter);
			return listResponse;
		} catch (DatabaseException databaseException) {
			LOG.error("", databaseException);
			throw new BusinessException("", databaseException);
		}
	}
	public List<RequisitionDTO> findAllRequisitions(RequisitionAngular requisition) throws BusinessException {
		try {
//            final List<Requisition> listResponse = this.requisitable.findAllRequisitions(requisition);
//            return listResponse;
			return this.requisitable.findAllRequisitions(requisition);
        } catch (DatabaseException databaseException) {
            LOG.error("", databaseException);
            throw new BusinessException("", databaseException);
        }
	}

	public List<Requisition> findByFlowPurchasingStatus(final FlowPurchasingEnum status, final Integer idFlow)
			throws BusinessException {
		try {
			final List<Requisition> listRequisition = this.requisitable.findByFlowPurchasingStatus(status, idFlow);
			this.addCommentsToRequisitions(listRequisition);
			return listRequisition;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVINGREQUISITION_BY_STATUS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVINGREQUISITION_BY_STATUS_ERROR, databaseException);
		}
	}

	private void addCommentsToRequisitions(final List<Requisition> listRequisition) throws BusinessException {
		for (int counterList = 0; counterList < listRequisition.size(); counterList++) {
			final List<Comment> commentList = this.commentsBusiness
					.findAllByRequisition(listRequisition.get(counterList).getIdRequisition());
			if (commentList.size() > 0)
				listRequisition.get(counterList).setComment(commentList.get(commentList.size() - 1));
			else {
				final Comment comment = new Comment();
				comment.setCommentText("");
				listRequisition.get(counterList).setComment(comment);
			}
		}
	}

	private void saveRequisitionApprovalAreas(final Integer idRequisition, final List<Integer> approvalAreasList)
			throws BusinessException {
		try {
			this.requisitable.deleteRequisitionApprovalAreas(idRequisition);
			for (Integer idApprovalArea : approvalAreasList)
				this.requisitable.saveRequisitionApprovalArea(idRequisition, idApprovalArea, null);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_APPROVAL_AREAS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_APPROVAL_AREAS_ERROR, databaseException);
		}
	}

	private void saveRequisitionAuthorizationDgas(final Integer idRequisition,
			final List<Integer> authorizationDgasList) throws BusinessException {
		try {
			this.requisitable.deleteRequisitionAuthorizationDgas(idRequisition);
			for (Integer idAuthorizationDga : authorizationDgasList)
				this.requisitable.saveRequisitionAuthorizationDga(idRequisition, idAuthorizationDga);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_AUTHORIZATION_DGAS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_AUTHORIZATION_DGAS_ERROR, databaseException);
		}
	}

	public List<String> findFlowStep(final Integer idRequisition, final Integer idFlow) throws BusinessException {

		LOG.debug(this.getClass().getName() + " > findFlowStep [ idRequisition :: " + idRequisition + ", + idFlow :: "
				+ idFlow + "]");
		try {
			if (!this.flowBusiness.isManagerialFlow(idFlow)) {
				return this.getPurchasingStepsWhenNotInProgress(idRequisition, idFlow);
			} else {
				final Integer stepOwner = this.flowScreenActionable.findRequisitionOwnerStep(idRequisition);
				return this.flowStepable.flowStep(stepOwner, idFlow);
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_FLOW_STEP_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_FLOW_STEP_ERROR, databaseException);
		}
	}

	private List<String> getPurchasingStepsWhenNotInProgress(final Integer idRequisition, final Integer idFlow)
			throws BusinessException, DatabaseException {
		if (!this.findByIdInProgress(idRequisition).getStatus().equals(FlowPurchasingEnum.IN_PROGRESS)) {
			final Integer step = this.flowScreenActionable.findRequisitionStep(idRequisition);
			return this.flowStepable.flowStep(step, idFlow);
		}
		return new ArrayList<>();
	}

	public List<Integer> findRequisitionApprovalAreas(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findRequisitionApprovalAreas(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_APPROVAL_AREAS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_APPROVAL_AREAS_ERROR, databaseException);
		}
	}

	public List<String> findRequisitionApprovalAreasActive(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findRequisitionApprovalAreasActive(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_APPROVAL_AREAS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_APPROVAL_AREAS_ERROR, databaseException);
		}
	}

	public List<Integer> findRequisitionAuthorizationDgas(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findRequisitionAuthorizationDgas(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_AUTHORIZATION_DGAS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_AUTHORIZATION_DGAS_ERROR, databaseException);
		}
	}

	public List<String> findRequisitionAuthorizationDgasActive(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findRequisitionAuthorizationDgasActive(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_AUTHORIZATION_DGAS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_AUTHORIZATION_DGAS_ERROR, databaseException);
		}
	}

	public List<String> findApprovalAreas(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findApprovalAreas(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_APPROVAL_AREAS_NAME, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_APPROVAL_AREAS_NAME, databaseException);
		}
	}

	public void saveRequisitionLegalRepresentative(final Integer idRequisition,
			final List<Integer> legalRepresentativesIdList) throws BusinessException {
		try {
			this.requisitable.deleteRequisitionLegalRepresentatives(idRequisition);
			for (Integer idLegalRepresentative : legalRepresentativesIdList)
				this.requisitable.saveRequisitionLegalRepresentative(idRequisition, idLegalRepresentative);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	public List<LegalRepresentative> findRequisitionLegalRepresentatives(final Integer idRequisition)
			throws BusinessException {
		try {
			return this.requisitable.findRequisitionLegalRepresentatives(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	public List<Customs> findRequisitionCustoms(final Integer idRequisition) throws BusinessException {
		try {
			return this.customsBusiness.getCustomsByIdRequisition(idRequisition);
		} catch (BusinessException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	public List<String> findRequisitionLegalRepresentativesActive(final Integer idRequisition)
			throws BusinessException {
		try {
			return this.requisitable.findRequisitionLegalRepresentativesActive(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	public List<Integer> findRequisitionFinancialEntityByIdRequisition(final Integer idRequisition)
			throws BusinessException {
		try {
			return this.requisitable.findRequisitionFinancialEntityByIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_ERROR, databaseException);
		}
	}

	public List<String> findRequisitionFinancialEntityActiveByIdRequisition(final Integer idRequisition)
			throws BusinessException {
		try {
			return this.requisitable.findRequisitionFinancialEntityActiveByIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_ERROR, databaseException);
		}
	}

	public List<FinantialEntityWitness> findRequisitionFinancialEntityByIdRequisitionWitnes(final Integer idRequisition)
			throws BusinessException {
		try {
			return this.requisitable.findRequisitionFinancialEntityByIdRequisitionWitness(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_WITNESS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_WITNESS_ERROR, databaseException);
		}
	}

	public void saveUsersToVoBo(final Integer idRequisition, final List<Integer> usersToVoBolist)
			throws BusinessException {
		try {
			this.requisitable.deleteUsersToVoBo(idRequisition);
			for (Integer idUser : usersToVoBolist)
				this.requisitable.saveUserToVoBo(idRequisition, idUser);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_USERS_TO_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_USERS_TO_VOBO_ERROR, databaseException);
		}
	}

	public List<User> findUsersToVoBo(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findUsersToVoBo(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_USERS_TO_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_USERS_TO_VOBO_ERROR, databaseException);
		}
	}

	public void saveRequisitionFinantialEntityWitness(final Integer idRequisition,
			final List<String> finantialEntityWitnessesList) throws BusinessException {
		try {
			this.requisitable.deleteRequisitionFinantialEntityWitnesses(idRequisition);
			for (String witnessName : finantialEntityWitnessesList)
				this.requisitable.saveRequisitionFinantialEntityWitness(idRequisition, witnessName);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_FINANTIAL_ENTITY_WITNESSES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_FINANTIAL_ENTITY_WITNESSES_ERROR, databaseException);
		}
	}

	public List<String> findRequisitionFinantialEntityWitnesses(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findRequisitionFinantialEntityWitnesses(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_USERS_ENTITY_WITNESSES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_USERS_ENTITY_WITNESSES_ERROR, databaseException);
		}
	}

	public String findNextStatus(final FlowScreenAction flowScreenActionParam) throws BusinessException {
		try {
			return this.flowScreenActionable.findNextStatus(flowScreenActionParam);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_NEXT_STATUS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_NEXT_STATUS_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_NO_NEXT_STATUS_ERROR, emptyResultException);
			throw new BusinessException(MESSAGE_NO_NEXT_STATUS_ERROR, emptyResultException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionLawyer(final Requisition requisition) throws BusinessException {
		try {
			this.requisitable.saveRequisitionLawyer(requisition.getIdRequisition(), requisition.getIdLawyer());
			this.saveChangeRequisitionStatus(requisition.getIdRequisition(), requisition.getFlowScreenActionParams());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_UPDATING_LAWYER_ERROR, databaseException);
			throw new BusinessException(MESSAGE_UPDATING_LAWYER_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionDocumentReviewLawyer(final Requisition requisition) throws BusinessException {
		try {
			this.saveRequisitionAttatchmentsFields(requisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_UPDATING_ATTATCHMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_UPDATING_ATTATCHMENTS_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionAttatchments(final Requisition requisition) throws BusinessException {
		try {
			this.saveRequisitionAttatchmentsFields(requisition);
			this.saveChangeRequisitionStatus(requisition.getIdRequisition(), requisition.getFlowScreenActionParams());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_UPDATING_ATTATCHMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_UPDATING_ATTATCHMENTS_ERROR, databaseException);
		}
	}

	private void saveRequisitionAttatchmentsFields(final Requisition requisition)
			throws DatabaseException, BusinessException {
		this.requisitable.saveRequisitionAttatchmentsFields(requisition);
		this.saveDocumentsAttachment(requisition);
	}

	public void deleteRequisitionAttatchments(final Integer idRequisition, final Integer idDocument)
			throws BusinessException {
		try {
			this.requisitable.deleteRequisitionAttatchmentByIdDocument(idDocument);
			this.requisitable.deleteRequisitionAttachmentVersion(idDocument);
			this.documentVersionBusiness.deleteByIdDocument(idDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETE_ATTATCHMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETE_ATTATCHMENTS_ERROR, databaseException);
		}
	}

	public List<RequiredDocument> supplierRequiredDocument(final Map<Integer, FileUploadInfo> mapRequiredDocument,
			final Integer idSupplier) throws BusinessException {
		LOG.info("RequisitionBusiness -> supplierRequiredDocument : " + idSupplier);

		final List<RequiredDocument> supplierRequiredDocumentList = new ArrayList<RequiredDocument>();
		for (Map.Entry<Integer, FileUploadInfo> fileUploadInfo : mapRequiredDocument.entrySet()) {
			final RequiredDocument requiredDocument = new RequiredDocument();
			Integer idDocument = fileUploadInfo.getValue().getIdFile();
			if (fileUploadInfo.getValue().isFileNew())
				idDocument = this.versionSupplierDocument(idSupplier, fileUploadInfo.getValue());
			requiredDocument.setIdDocument(idDocument);
			requiredDocument.setIdRequiredDocument(fileUploadInfo.getKey());
			supplierRequiredDocumentList.add(requiredDocument);
		}
		return supplierRequiredDocumentList;
	}

	public final void saveDocumentsAttachment(final Requisition requisition) throws BusinessException {
		try {
			final List<FileUploadInfo> documentsAttachmentList = requisition.getDocumentsAttachmentList();
			this.requisitable.deleteRequisitionAttatchmentByIdRequisition(requisition.getIdRequisition());
			for (FileUploadInfo fileUploadInfo : documentsAttachmentList) {
				Integer idDocument = fileUploadInfo.getIdFile();
				idDocument = this.versionDocumentWhenNew(requisition, fileUploadInfo, idDocument);
				final RequisitionAttachment requisitionAttachment = new RequisitionAttachment(
						requisition.getIdRequisition(), idDocument);
				this.requisitable.saveRequisitionAttatchment(requisitionAttachment);
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DOCUMENTS_ATTATCHMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DOCUMENTS_ATTATCHMENTS_ERROR, databaseException);
		}
	}

	private Integer versionDocumentWhenNew(final Requisition requisition, final FileUploadInfo fileUploadInfo,
			final Integer idDocument) throws BusinessException {
		Integer idVersionedDocument = idDocument;
		if (fileUploadInfo.isFileNew())
			idVersionedDocument = this.versionRequisitionDocument(requisition.getIdRequisition(), fileUploadInfo);
		return idVersionedDocument;
	}

	private Integer versionRequisitionDocument(final Integer idRequisition, final FileUploadInfo fileUploadInfo)
			throws BusinessException {
		final File requisitionDocumentPath = this.getRequisitionPath(idRequisition);
//		return this.versionAndMoveDocument(requisitionDocumentPath, fileUploadInfo);
		return this.versionAndMoveDocumentSinRenombre(requisitionDocumentPath, fileUploadInfo);
	}
	private Integer versionRequisitionDocumentFinal(final Integer idRequisition, final FileUploadInfo fileUploadInfo)
			throws BusinessException {
		final File requisitionDocumentPath = this.getRequisitionPath(idRequisition);
//		return this.versionAndMoveDocument(requisitionDocumentPath, fileUploadInfo);
		return this.versionAndMoveDocumentFinal(requisitionDocumentPath, fileUploadInfo);
	}
	private Integer versionSupplierDocument(final Integer idSupplier, final FileUploadInfo fileUploadInfo)
			throws BusinessException {
		final File supplierDocumentPath = this.getSupplierPath(idSupplier);
		return this.versionAndMoveDocument(supplierDocumentPath, fileUploadInfo);
	}
	private Integer versionSupplierDocumentFinal(final Integer idSupplier, final FileUploadInfo fileUploadInfo)
			throws BusinessException {
		final File supplierDocumentPath = this.getSupplierPathFinal(idSupplier);
		return this.versionAndMoveDocument(supplierDocumentPath, fileUploadInfo);
	}

	private Integer versionAndMoveDocument(final File targetDocumentPath, final FileUploadInfo fileUploadInfo)
			throws BusinessException {
		
		LOG.info("\n==================================== MOVIENDO DOCUMENTO TEMPORAL. ====================================");
		
		
		Integer idDocument = fileUploadInfo.getIdFile();
		if (fileUploadInfo.isFileNew()) {
			this.cleanNameSpaces(fileUploadInfo);
			final File userTempFilesPath = new File(this.createUserTemporalPath());
			final String saveName = this.createSaveName(fileUploadInfo, idDocument);
			final File finalFullFile = new File(targetDocumentPath.getAbsolutePath() + File.separator + saveName);
			
			LOG.info("ORIGEN ::"+userTempFilesPath.getAbsolutePath());
			LOG.info("DESTINO ::"+finalFullFile.getAbsolutePath());
			
			LOG.info("DIRECTORIO ORIGEN PREVIAMENTE  #######################################################################");
			LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(new File(userTempFilesPath.getAbsolutePath())));
			
			LOG.info("DIRECTORIO DESTINO PREVIAMENTE #######################################################################");
			LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(targetDocumentPath));			
			
			idDocument = this.documentVersionBusiness.save(fileUploadInfo.getIdFile(), finalFullFile);
			if (this.fileExist(fileUploadInfo, userTempFilesPath)) {
				targetDocumentPath.mkdir();
				try {
					Files.move(this.getOriginFilePath(fileUploadInfo, userTempFilesPath),
							this.createFinalFilePath(saveName, targetDocumentPath), StandardCopyOption.ATOMIC_MOVE);
					
					LOG.info("DIRECTORIO ORIGEN POSTERIORMENTE #######################################################################");
					LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(new File(userTempFilesPath.getAbsolutePath())));
					
					LOG.info("DIRECTORIO DESTINO POSTERIORMENTE #######################################################################");
					LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(targetDocumentPath));			
					
				} catch (IOException ioException) {
					LOG.error(MESSAGE_MOVING_DOCUMENT_ERROR, ioException);
					throw new BusinessException(MESSAGE_MOVING_DOCUMENT_ERROR, ioException);
				}
				
				
			}
		}
		return idDocument;
	}
	private Integer versionAndMoveDocumentFinal(final File targetDocumentPath, final FileUploadInfo fileUploadInfo)
			throws BusinessException {
		
		LOG.info("\n==================================== MOVIENDO DOCUMENTO TEMPORAL. ====================================");
		
		
		Integer idDocument = fileUploadInfo.getIdFile();
		if (fileUploadInfo.isFileNew()) {
			this.cleanNameSpaces(fileUploadInfo);
			final File userTempFilesPath = new File(this.createUserTemporalPath());
			final String saveName = this.createSaveName(fileUploadInfo, idDocument);
			final File finalFullFile = new File(targetDocumentPath.getAbsolutePath() + File.separator + saveName);
			
			LOG.info("ORIGEN ::"+userTempFilesPath.getAbsolutePath());
			LOG.info("DESTINO ::"+finalFullFile.getAbsolutePath());
			
			LOG.info("DIRECTORIO ORIGEN PREVIAMENTE  #######################################################################");
			LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(new File(userTempFilesPath.getAbsolutePath())));
			
			LOG.info("DIRECTORIO DESTINO PREVIAMENTE #######################################################################");
			LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(targetDocumentPath));			
			
			idDocument = this.documentVersionBusiness.save(fileUploadInfo.getIdFile(), finalFullFile);
			if (this.fileExist(fileUploadInfo, userTempFilesPath)) {
				targetDocumentPath.mkdir();
				try {
					Files.move(this.getOriginFilePath(fileUploadInfo, userTempFilesPath),
							this.createFinalFilePath(saveName, targetDocumentPath), StandardCopyOption.ATOMIC_MOVE);
					
					LOG.info("DIRECTORIO ORIGEN POSTERIORMENTE #######################################################################");
					LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(new File(userTempFilesPath.getAbsolutePath())));
					
					LOG.info("DIRECTORIO DESTINO POSTERIORMENTE #######################################################################");
					LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(targetDocumentPath));			
					
				} catch (IOException ioException) {
					LOG.error(MESSAGE_MOVING_DOCUMENT_ERROR, ioException);
					throw new BusinessException(MESSAGE_MOVING_DOCUMENT_ERROR, ioException);
				}
				
				
			}
		}
		return idDocument;
	}

	private Integer versionAndMoveDocumentSinRenombre(final File targetDocumentPath,
			final FileUploadInfo fileUploadInfo) throws BusinessException {
		
		LOG.info("\n==================================== VERSIÓN Y MOVIMIENTO DE ARCHIVOS SIN RENOMBRE. ====================================");
		
				
		
		Integer idDocument = fileUploadInfo.getIdFile();
		if (fileUploadInfo.isFileNew()) {
			this.cleanNameSpaces(fileUploadInfo);
			final File userTempFilesPath = new File(this.createUserTemporalPath());
			final File finalFullFile = new File(
					targetDocumentPath.getAbsolutePath() + File.separator + fileUploadInfo.getName());
			
			
			LOG.info("ORIGEN ::"+userTempFilesPath.getAbsolutePath());
			LOG.info("DESTINO ::"+finalFullFile.getAbsolutePath());
			
			LOG.info("DIRECTORIO ORIGEN PREVIAMENTE  #######################################################################");
			LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(new File(userTempFilesPath.getAbsolutePath())));
			
			LOG.info("DIRECTORIO DESTINO PREVIAMENTE #######################################################################");
			LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(targetDocumentPath));	
			
			LOG.info("############################\n"+fileUploadInfo.isDocumentofina());
			idDocument = this.documentVersionBusiness.save(fileUploadInfo.getIdFile(), finalFullFile);
			if (this.fileExist(fileUploadInfo, userTempFilesPath)) {
				targetDocumentPath.mkdir();
				try {
					Files.move(this.getOriginFilePath(fileUploadInfo, userTempFilesPath),
							this.createFinalFilePath(fileUploadInfo.getName(), targetDocumentPath),
							StandardCopyOption.ATOMIC_MOVE);
					
					LOG.info("DIRECTORIO ORIGEN POSTERIORMENTE #######################################################################");
					LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(new File(userTempFilesPath.getAbsolutePath())));
					
					LOG.info("DIRECTORIO DESTINO POSTERIORMENTE #######################################################################");
					LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(targetDocumentPath));	
					
				} catch (IOException ioException) {
					LOG.error(MESSAGE_MOVING_DOCUMENT_ERROR, ioException);
					throw new BusinessException(MESSAGE_MOVING_DOCUMENT_ERROR, ioException);
				}
			}
		}
		return idDocument;
	}

	private void cleanNameSpaces(final FileUploadInfo fileUploadInfo) throws BusinessException {
		if (fileUploadInfo.getName() != null) {
			fileUploadInfo.setName(SubparagraphUtils.stripAccents(fileUploadInfo.getName())
//			fileUploadInfo.setName(StringUtils.limpiaCadena(fileUploadInfo.getName())
					.replaceAll(INVALID_CHARACTERS, UNDERSCORE));
		}
	}

	private String createUserTemporalPath() throws BusinessException {
		return this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_TMP
				+ File.separator + this.session.getIdUsuarioSession();
	}

	private String createSaveName(final FileUploadInfo fileUploadInfo, final Integer idDocument)
			throws BusinessException {
//		String version = "1";
//		if (idDocument != null) {
//			final Version currentVersion = this.documentVersionBusiness.findCurrentVersion(idDocument);
//			final Integer nextVersion = currentVersion.getVersionNumber() + (Integer) 1;
//			version = nextVersion.toString();
//		}
//		final String nombre=StringUtils.limpiaCadena(fileUploadInfo.getName());
//		final String fileNameWithoutExtension = FilenameUtils.removeExtension(nombre);
//		final String extension = FilenameUtils.getExtension(nombre);
//		return fileNameWithoutExtension + UNDERSCORE + version + "." + extension;
//	}
	    String version = "1";
        if (idDocument != null) {
            final Version currentVersion = this.documentVersionBusiness.findCurrentVersion(idDocument);
            final Integer nextVersion = currentVersion.getVersionNumber() + (Integer) 1;
            version = nextVersion.toString();
        }
        final String fileNameWithoutExtension = FilenameUtils.removeExtension(fileUploadInfo.getName());
        final String extension = FilenameUtils.getExtension(fileUploadInfo.getName());
        return fileNameWithoutExtension + UNDERSCORE + version + "." + extension;
    }

	private File getRequisitionPath(final Integer idRequisition) throws BusinessException {
		return this.directoryUtils.getRequisitionPath(idRequisition);
	}

	private File getSupplierPath(final Integer idSupplier) throws BusinessException {
		return this.directoryUtils.getSupplierPath(idSupplier);
	}
	private File getSupplierPathFinal(final Integer idSupplier) throws BusinessException {
		return this.directoryUtils.getSupplierPath(idSupplier);
	}
	private boolean fileExist(final FileUploadInfo fileUploadInfo, final File userTempFilesPath) {
		final File originFile = new File(userTempFilesPath + File.separator + fileUploadInfo.getName());
		return originFile.exists();
	}

	private Path getOriginFilePath(final FileUploadInfo fileUploadInfo, final File userTempFilesPath) {
		return this.createFilePath(userTempFilesPath, fileUploadInfo.getName());
	}

	private Path createFinalFilePath(final String saveName, final File targetDocumentPath) {
		return this.createFilePath(targetDocumentPath, saveName);
	}
	private Path createFinalFilePathFinal(final String saveName, final File targetDocumentPath) {
		return this.createFilePathFinal(targetDocumentPath, saveName);
	}

	private Path createFilePath(final File userTempFilesPath, final String fileName) {
		return FileSystems.getDefault().getPath(userTempFilesPath.getAbsolutePath() + File.separator + fileName);
	}
	private Path createFilePathFinal(final File userTempFilesPath, final String fileName) {
		String nombrefinal=fileName.split(".")[0];
		return FileSystems.getDefault().getPath(userTempFilesPath.getAbsolutePath() + File.separator + nombrefinal+"_vf.docx");
	}

	public List<Version> findDocumentsAttachment(final Integer idRequisition) throws BusinessException {
		try {
			List<Integer> idDocumentList = new ArrayList<Integer>();
			idDocumentList = this.requisitable.findRequisitionAttachmentByIdRequisition(idRequisition);
			final List<Version> versionList = new ArrayList<Version>();
			for (Integer idDocument : idDocumentList) {
				Version bean = new Version();
				bean = this.documentVersionBusiness.findCurrentVersion(idDocument);
				bean.setFileName(FilenameUtils.getName(bean.getDocumentPath()));
				versionList.add(bean);
			}
			return versionList;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGNED_CONTRACT_DATA_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGNED_CONTRACT_DATA_ERROR,
					databaseException);
		}
	}

	public List<Version> findHistoryDocumentsVersions(final Integer idSupplier, final Integer idRequiredDocument)
			throws BusinessException {
		try {
			final Integer idDocument = this.requisitable.findHistoryDocumentsVersions(idSupplier, idRequiredDocument);
			List<Version> versionList = new ArrayList<Version>();
			versionList = this.documentVersionBusiness.findVersionByIdDocument(idDocument);
			for (Version bean : versionList) {
				bean.setFileName(FilenameUtils.getName(bean.getDocumentPath()));
			}
			return versionList;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DOCUMENTS_HISTORY_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DOCUMENTS_HISTORY_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void rejectRequisitionDocumentReviewLawyer(final Requisition requisition) throws BusinessException {
		this.requisitionVersionBusiness.saveRequisitionVersion(requisition.getIdRequisition());
		this.saveChangeRequisitionStatus(requisition.getIdRequisition(), requisition.getFlowScreenActionParams());
	}

	public File downloadDraftRequisition(final Integer idRequisition, final Boolean setColorMark)
			throws BusinessException {
		return this.fillWord(idRequisition, setColorMark);
	}

	private File fillWord(final Integer idRequisition, final Boolean setColorMark) throws BusinessException {
		LOG.info("Comienza la generación del borrador/contrato de la solicitud: " + idRequisition);
		final List<Object> objectsList = new ArrayList<>();
		final Requisition requisition = this.setRequisitionObjectsData(idRequisition, objectsList);

		final Map<String, String> mapValues = this.fillReplacesMap(objectsList, requisition);
		this.setLegalRepresentativePower(idRequisition, objectsList);

		final List<String> requisitionLegalRepresentativesNames = this.getRequisitionLegalRepresentatives(idRequisition);
		final List<String> financialEntitiesWitnessesBoldNames = this.getFinancialEntitiesWitnessesForSingsTable(idRequisition);

		final List<String> supplierLegalRepresentativesNames = this.getSupplierLegalRepresentatives(requisition.getIdRequisition());
		final List<String> supplierWitnessesNames = this.getSupplierWitnessNames(requisition.getIdRequisition());

		LOG.info("Mensaje de CONTRAPRESTACION: " + requisition.getConsiderationAmount());

		if(validaEnteros(requisition.getConsiderationAmount())) {
			String textContra = "$ ";
			// $[&ImporteContraprestacion&] ([&SolicitudImporteContraprestacionLetra&] pesos [&SolicitudImporteContraprestacionCentavos&]/100 M.N.), 
			textContra += CurrencyUtils.convertNumberToCurrencyFormat(requisition.getConsiderationAmount());
			textContra += " ";
			textContra += this.convertNumberToString(requisition.getConsiderationAmount().toString());
			textContra += " pesos ";
			textContra += this.findDecimals(requisition.getConsiderationAmount().toString());
			textContra += "/100 M.N.),";
			mapValues.put(TagFieldEnum.CONSIDERATION_AMOUNT.getDescription(), textContra);
		} else {
			mapValues.put(TagFieldEnum.CONSIDERATION_AMOUNT.getDescription(),
					requisition.getConsiderationAmount());
		}

		mapValues.put(TagFieldEnum.DEPOSIT_AMOUNT.getDescription(),
				requisition.getDepositAmount() == null || requisition.getDepositAmount().trim().isEmpty() ? "0.00"
						: CurrencyUtils.convertNumberToCurrencyFormat(requisition.getDepositAmount()));


		/*
		mapValues.put(TagFieldEnum.LEGAL_REPRESENTATIVE_NAMES.getDescription(),
				requisition.getSupplier().getLegalRepresentativesList() == null
						|| requisition.getSupplier().getLegalRepresentativesList().size() == 0 ? NOT_CAPTURED_FIELD
								: requisition.getSupplier().getLegalRepresentativesList().get(0).getNameUpper());

		mapValues.put(TagFieldEnum.WITTNESS_NAMES.getDescription(),
				requisition.getSupplier().getWitnessesList() == null
						|| requisition.getSupplier().getWitnessesList().size() == 0 ? NOT_CAPTURED_FIELD
								: requisition.getSupplier().getWitnessesList().get(0).getNameUpper());
		 */

		mapValues.put(TagFieldEnum.LEGAL_REPRESENTATIVE_NAMES.getDescription(),
				requisition.getSupplier().getLegalRepresentativesList() == null
				|| requisition.getSupplier().getLegalRepresentativesList().size() == 0 ? NOT_CAPTURED_FIELD
						: (supplierLegalRepresentativesNames.isEmpty() ? "" : supplierLegalRepresentativesNames.get(0)));

		mapValues.put(TagFieldEnum.WITTNESS_NAMES.getDescription(),
				requisition.getSupplier().getWitnessesList() == null
				|| requisition.getSupplier().getWitnessesList().size() == 0 ? NOT_CAPTURED_FIELD
						: (supplierWitnessesNames.isEmpty() ? "" : supplierWitnessesNames.get(0)));


		mapValues.put(TagFieldEnum.AREA_AUTHORIZES_PAYMENT.getDescription(),
				requisition.getArea().getName() == null || requisition.getArea().getName().isEmpty()
				? NOT_CAPTURED_FIELD
						: requisition.getArea().getName());
		mapValues.put(TagFieldEnum.UNIT_NAME.getDescription(),
				requisition.getUnit().getName() == null || requisition.getUnit().getName().isEmpty()
				? NOT_CAPTURED_FIELD
						: requisition.getUnit().getName());

		this.upperMapValues(mapValues);
		return this.replaceTemplateValues(setColorMark, requisition, mapValues,
				this.createSupplierLegalRepAndWitnessesNamesMap(
						this.mergeStringLists(requisitionLegalRepresentativesNames, supplierLegalRepresentativesNames),
						this.mergeStringLists(financialEntitiesWitnessesBoldNames, supplierWitnessesNames)));
	}

	private void upperMapValues(final Map<String, String> mapValues) {
		final Map<String, String> upperMapValues = new HashMap<>();
		for (Entry<String, String> entry : mapValues.entrySet()) {
			if (entry.getValue() != null && !entry.getValue().isEmpty()) {
				upperMapValues.put(this.createUpperTag(entry.getKey()), entry.getValue().toUpperCase());
			} else
				mapValues.put(entry.getKey(), NOT_CAPTURED_FIELD);
		}
		mapValues.putAll(upperMapValues);
	}

	private String createUpperTag(final String key) {
		return key.substring(0, key.indexOf("&]")) + "Mayusculas&]";
	}

	private void setLegalRepresentativePower(final Integer idRequisition, final List<Object> list)
			throws BusinessException {
		final List<FinancialEntity> entities = this.financialEntityBusiness
				.findFinancialEntityByIdRequisition(idRequisition);
		if (entities.size() > 1)
			for (FinancialEntity entity : entities)
				entity.setPower(this.legalRepresentativeBusiness.findPowersByIdLegalRepresentativeAndIdFinancialEntity(
						entity.getIdLegalRepresentative(), entity.getIdFinancialEntity()));
		else {
			try {
				entities.get(0).setPower(
						this.legalRepresentativeBusiness.findPowersByIdLegalRepresentativeAndIdFinancialEntity(
								entities.get(0).getIdLegalRepresentative(), entities.get(0).getIdFinancialEntity()));
				list.add(entities.get(0).getPower());
			} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
				LOG.error(MESSAGE_NO_FINANCIAL_ENTITY_ERROR, indexOutOfBoundsException);
				throw new BusinessException(MESSAGE_NO_FINANCIAL_ENTITY_ERROR, indexOutOfBoundsException);
			}
		}
	}

	private Map<String, String> fillReplacesMap(final List<Object> objectsList, final Requisition requisition)
			throws BusinessException {
		final Map<String, String> mapValues = this.setRequisitionObjectsMapValues(objectsList, requisition);
		this.setRequisitionLegalRepresentatives(requisition.getIdRequisition(), mapValues);
		this.setRequisitionCustomsName(requisition.getIdRequisition(), mapValues);
		this.setRequisitionModifiedClausules(requisition.getIdRequisition(), mapValues);
		this.setPowerDates(requisition, mapValues);
		this.setAdicionalRequistionData(requisition, mapValues);
		return mapValues;
	}

	public Map<String, String> createReplacesMap(final Integer idRequisition) throws BusinessException {
		try {
			final List<Object> objectsList = new ArrayList<>();
			final Requisition requisition = this.setRequisitionObjectsData(idRequisition, objectsList);
			final Map<String, String> mapValues = this.fillReplacesMap(objectsList, requisition);
			mapValues.put(TagFieldEnum.ACTORS_DOMICILES.getDescription(),
					this.cleanupText(mapValues.get(TagFieldEnum.ACTORS_DOMICILES.getDescription())));
			mapValues.put(TagFieldEnum.CLAUSE_TO_MODIFY_CONTENT.getDescription(),
					this.cleanupText(mapValues.get(TagFieldEnum.CLAUSE_TO_MODIFY_CONTENT.getDescription())));
			mapValues.remove(TagFieldEnum.POWERS_BY_LEGAL_REPRESENTATIVES.getDescription());
			return mapValues;
		} catch (BusinessException businessException) {
			LOG.error(MESSAGE_CREATING_REPLACES_MAP_ERROR, businessException);
			throw new BusinessException(MESSAGE_CREATING_REPLACES_MAP_ERROR, businessException);
		}
	}

	private Map<String, String> setRequisitionObjectsMapValues(final List<Object> objectsList,
			final Requisition requisition) throws BusinessException {
		final Map<String, String> mapValues = new HashMap<String, String>();
		LOG.info("Se cargarán al mapa de reemplazos los valores de números a letras");
		this.setNumberInPercentaje(requisition, mapValues);
		// this.tuUpperCase(requisition, mapValues);
		this.setNumbersToCurrencyFormat(requisition, mapValues);
		this.setNumbersInLetter(requisition, mapValues);
		this.setNumbersInEnglish(requisition, mapValues);
		this.setCentsInRequisition(requisition, mapValues);
		this.replaceFieldsByObject(objectsList, mapValues);

		this.setRetroactiveDate(requisition.getRetroactiveDate(), mapValues);
		return mapValues;
	}

	private void setRetroactiveDate(final String retroactiveDate, final Map<String, String> mapValues) {
		if (retroactiveDate != null)
			mapValues
			.put(TagFieldEnum.RETROACTIVE_DATE.getDescription(),
					"[con efectos retroactivos al día ".concat(this.getStringValue(retroactiveDate, false)))
			.concat(" ]");
		else
			mapValues.put(TagFieldEnum.RETROACTIVE_DATE.getDescription(), "");
	}

	private String cleanupText(final String text) {
		return text.replace(BR, "\\r\\n").replace(DOUBLE_TABULATOR, "        ");
	}

	private Requisition setRequisitionObjectsData(final Integer idRequisition, final List<Object> objectsList)
			throws BusinessException {
		final Requisition requisition = this.addRequisitionData(idRequisition, objectsList);
		LOG.info("Datos de la solicitud cargados");
		this.addSupplierData(objectsList, requisition);
		LOG.info("Datos del proveedor cargados");
		this.addApplicantData(objectsList, requisition);
		LOG.info("Datos del solicitante cargados");
		final Power power = this.addFinancialEntityData(objectsList, idRequisition);
		requisition.setPublicDeedDate(power.getPublicDeedDate());
		requisition.setRegisterCommercialFolioDate(power.getPublicCommercialFolioInscriptionDate());
		LOG.info("Datos de la entidad cargados");
		return requisition;
	}

	private Power addFinancialEntityData(final List<Object> objectsList, final Integer idRequisition)
			throws BusinessException {
		try {
			final List<FinancialEntity> financialEntities = this.requisitable
					.findActiveFinancialEntitiesByIdRequisition(idRequisition);
			if (financialEntities.size() > 0) {
				objectsList.add(financialEntities.get(0));
			}
			LOG.info("Datos del poder del representante legal de la entidad cargados");
			return this.addFinancialEntityLegalRepresentativePowerData(objectsList, idRequisition,
					financialEntities.get(0).getIdFinancialEntity());
		} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
			LOG.error(MESSAGE_NO_FINANCIAL_ENTITY_ERROR, indexOutOfBoundsException);
			throw new BusinessException(MESSAGE_NO_FINANCIAL_ENTITY_ERROR, indexOutOfBoundsException);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_FINANCIAL_ENTITY_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_FINANCIAL_ENTITY_ERROR, databaseException);
		}
	}

	private Power addFinancialEntityLegalRepresentativePowerData(final List<Object> objectsList,
			final Integer idRequisition, final Integer idFinancialEntity) throws BusinessException {
		final Power power = this.findFinancialeEntityPowerByRequisitionAndFinancialEntity(idRequisition,
				idFinancialEntity);
		objectsList.add(power);
		return power;
	}

	private Power findFinancialeEntityPowerByRequisitionAndFinancialEntity(final Integer idRequisition,
			final Integer idFinancialEntity) throws BusinessException {
		try {
			final List<LegalRepresentative> legalRepresentatives = this.requisitable
					.findActiveLegalRepByRequisitionAndFinancialEnt(idRequisition, idFinancialEntity);
			final Power power = this.legalRepresentativeBusiness.findPowersByIdLegalRepresentativeAndIdFinancialEntity(
					legalRepresentatives.get(0).getIdLegalRepresentative(), idFinancialEntity);
			return power;
		} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
			LOG.error(MESSAGE_NO_POWER_ERROR, indexOutOfBoundsException);
			throw new BusinessException(MESSAGE_NO_POWER_ERROR, indexOutOfBoundsException);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVIG_POWER_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVIG_POWER_ERROR, databaseException);
		}
	}

	private List<String> getFinancialEntitiesWitnessesForSingsTable(final Integer idRequisition)
			throws BusinessException {
		final List<String> financialEntitiesWitnesses = this.findRequisitionFinantialEntityWitnesses(idRequisition);
		final List<String> financialEntitiesWitnessesBoldNames = new ArrayList<>();
		for (String financialEntityWitnessName : financialEntitiesWitnesses) {
			financialEntitiesWitnessesBoldNames.add(BOLD_OPEN + financialEntityWitnessName.toUpperCase() + BOLD_CLOSE);
		}
		return financialEntitiesWitnessesBoldNames;
	}

	private File replaceTemplateValues(final Boolean setColorMark, final Requisition requisition,
			final Map<String, String> mapValues,
			final LinkedHashMap<String, List<String>> legalRepesentativesAndWitnessesMap) throws BusinessException {
		final File template = new File(this.findTemplate(requisition.getIdRequisition()));
		
		LOG.info("replaceTemplateValues :: template ("+template.getAbsolutePath()+")");
		
		try {
			final WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(template);
			VariablePrepare.prepare(wordMLPackage);
			LOG.info("Se reemplazaran los tags en la plantilla");
			this.replaceTexts(mapValues, wordMLPackage, setColorMark);

			LOG.info("Se reemplazaran los tags de firmas en la plantilla");
			if (requisition.getActiveActor() != null)
				requisition.setActiveActor(requisition.getActiveActor().replace(
						TagFieldEnum.FINANCIAL_ENTITIES_SHORT_NAMES.getDescription(),
						mapValues.get(TagFieldEnum.FINANCIAL_ENTITIES_SHORT_NAMES.getDescription())));
			LOG.info("replaceSignsTable...");
			this.replaceSignsTable(wordMLPackage, requisition, legalRepesentativesAndWitnessesMap, setColorMark);
			LOG.info("replaceSignsTable... OK");
			LOG.info("replaceTractoTable...");
			this.replaceTractoTable(wordMLPackage, requisition);
			LOG.info("replaceTractoTable... OK");
			LOG.info("replaceRollOffTable...");
			this.replaceRollOffTable(wordMLPackage, requisition);
			LOG.info("replaceRollOffTable... OK");
			
			LOG.info("Se guardará el documento");
			final File outputFile = this.saveOutputFile(template, wordMLPackage);
			LOG.info("Se guardará el documento :: outputFile("+outputFile.getAbsolutePath()+")");
			
			// this.addAttatchmentsToOutputFile(requisition.getIdRequisition(), outputFile);
			// LOG.info(FileUtils.sizeOf(outputFile) + " ::Tamaño del archivo");
			LOG.info("El documento generado se enviará al navegador");
			return outputFile;
		} catch (Exception exception) {
			LOG.error(MESSAGE_CREATING_WORD_ERROR, exception);
			throw new BusinessException(MESSAGE_CREATING_WORD_ERROR + COLON_SPACE + exception.getMessage(), exception);
		}
				
	}

	private void replaceTractoTable(WordprocessingMLPackage wordMLPackage, Requisition requisition) {
		try {
			requisition.setTractoList(tractoBusiness.getTractoByIdRequisition(requisition.getIdRequisition()));
		} catch (BusinessException e) {

			e.printStackTrace();
		}
		ObjectFactory factory = Context.getWmlObjectFactory();
		List<String> list = this.createHeaderTracto();
		Tbl tractoTable = DocXUtil.createTractoTable(wordMLPackage, list, factory);
		this.createHeaderTable(wordMLPackage, tractoTable, list, factory);
		this.createContentTractoTable(wordMLPackage, tractoTable, requisition.getTractoList(), factory, list.size());
		DocXUtil.replacePlaceholderTabla(wordMLPackage, tractoTable, TagFieldEnum.TRACTO_TABLE.getDescription());

	}

	private void createContentTractoTable(WordprocessingMLPackage wordMLPackage, Tbl tabla, List<Tracto> tractoList,
			ObjectFactory factory, int columnNumber) {
		String backgroundColor = "white";
		String textColor = "black";
		String fontFamily = "Calibri";
		String fontSize = "20";
		Boolean isBold=Boolean.FALSE;
		Boolean isItalic=Boolean.FALSE;
		Boolean isUnderline=Boolean.FALSE;
		for (int i = 0; i < tractoList.size(); i++) {
			Tr content = factory.createTr();
			for (int j = 0; j < columnNumber; j++) {
				switch (j) {
				case 0:
					DocXUtil.addTC(content, factory, tractoList.get(i).getBrand(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 1:
					DocXUtil.addTC(content, factory, tractoList.get(i).getModel(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 2:
					DocXUtil.addTC(content, factory, tractoList.get(i).getFederalPlates(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 3:
					DocXUtil.addTC(content, factory, tractoList.get(i).getDriver(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 4:
					DocXUtil.addTC(content, factory, tractoList.get(i).getGpsProvider(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 5:
					DocXUtil.addTC(content, factory, tractoList.get(i).getTractoInsurancePolicyNumber(),
							backgroundColor, textColor, fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				default:
					break;
				}
			}
			tabla.getContent().add(content);
		}

	}

	private void replaceRollOffTable(WordprocessingMLPackage wordMLPackage, Requisition requisition) {
		try {
			requisition.setRollOffList(rollOffBusiness.getRollOffByIdRequisition(requisition.getIdRequisition()));
		} catch (BusinessException e) {

			e.printStackTrace();
		}
		ObjectFactory factory = Context.getWmlObjectFactory();
		List<String> list = this.createHeaderRollOff();
		Tbl tractoTable = DocXUtil.createTractoTable(wordMLPackage, list, factory);
		this.createHeaderTable(wordMLPackage, tractoTable, list, factory);
		this.createContentRollOffTable(wordMLPackage, tractoTable, requisition.getRollOffList(), factory, list.size());
		DocXUtil.replacePlaceholderTabla(wordMLPackage, tractoTable, TagFieldEnum.ROLLOFF_TABLE.getDescription());

	}
	private void createContentRollOffTable(WordprocessingMLPackage wordMLPackage, Tbl tabla, List<RollOff> tractoList,
			ObjectFactory factory, int columnNumber) {
		String backgroundColor = "white";
		String textColor = "black";
		String fontFamily = "Calibri";
		String fontSize = "20";
		Boolean isBold=Boolean.FALSE;
		Boolean isItalic=Boolean.FALSE;
		Boolean isUnderline=Boolean.FALSE;
		for (int i = 0; i < tractoList.size(); i++) {
			Tr content = factory.createTr();
			for (int j = 0; j < columnNumber; j++) {
				switch (j) {
				case 0:
					DocXUtil.addTC(content, factory, tractoList.get(i).getBrand(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 1:
					DocXUtil.addTC(content, factory, tractoList.get(i).getDescription(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 2:
					DocXUtil.addTC(content, factory, tractoList.get(i).getSerialNumber(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 3:
					DocXUtil.addTC(content, factory, tractoList.get(i).getManufacturingDate(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 4:
					DocXUtil.addTC(content, factory, tractoList.get(i).getTareWeight(), backgroundColor, textColor,
							fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				case 5:
					DocXUtil.addTC(content, factory, tractoList.get(i).getDimensions(),
							backgroundColor, textColor, fontFamily, fontSize, wordMLPackage,isBold,isItalic,isUnderline);
					break;
				default:
					break;
				}
			}
			tabla.getContent().add(content);
		}

	}
	private void createHeaderTable(WordprocessingMLPackage wordMLPackage, Tbl tabla, List<String> header,
			ObjectFactory factory) {
		Tr thead = factory.createTr();
		String backgroundColor = "012061";
		String textColor = "white";
		String fontFamily = "Calibri";
		String fontSize = "20";

		Boolean isBold=Boolean.FALSE;
		Boolean isItalic=Boolean.FALSE;
		Boolean isUnderline=Boolean.FALSE;
		for (int i = 0; i < header.size(); i++) {
			DocXUtil.addTC(thead, factory, header.get(i), backgroundColor, textColor, fontFamily, fontSize,
					wordMLPackage,isBold,isItalic,isUnderline);
		}
		tabla.getContent().add(thead);
	}

	private List<String> createHeaderTracto() {
		List<String> head = new ArrayList<String>();
		head.add("Marca");
		head.add("Modelo");
		head.add("Placas Federales");
		head.add("Chofer");
		head.add("Prov. GPS");
		head.add("No. Póliza Seguro Tracto");
		return head;
	}
	private List<String> createHeaderRollOff() {
		List<String> head = new ArrayList<String>();
		head.add("Marca");
		head.add("Descripción");
		head.add("No. Serie");
		head.add("Fecha de Fabricación");
		head.add("Peso Tara (kg)");
		head.add("Dimensiones (mm)");
		return head;
	}

	private void replaceTexts(final Map<String, String> mapValues, final WordprocessingMLPackage wordMLPackage,
			final Boolean setColorMark) {
		if (setColorMark)
			DocXUtil.replacePlaceholderTextBorrador(wordMLPackage, mapValues);
		else
			DocXUtil.replacePlaceholderText(wordMLPackage, mapValues);
	}

	private File saveOutputFile(final File template, final WordprocessingMLPackage wordMLPackage)
			throws Docx4JException, BusinessException {
		final File outputFile = this.builtOutputPathFile(template, this.getDocumentTypeTemporalPath(),
				FilenameUtils.getExtension(template.getName()));
		wordMLPackage.save(outputFile);
		return outputFile;
	}

	private File builtOutputPathFile(final File baseTemplate, final String outputPathFile, final String fileType) {
		return new File(outputPathFile + File.separator + FilenameUtils.removeExtension(baseTemplate.getName()) + "-"
				+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + "." + fileType);
	}

	private void replaceSignsTable(final WordprocessingMLPackage wordMLPackage, final Requisition requisition,
			final LinkedHashMap<String, List<String>> supplierLegalRepAndWitnessesNames, final Boolean setColorMark)
					throws BusinessException {
		try {
			final List<FinancialEntity> financialEntitiesList = this
					.findActiveFinancialEntitiesByIdRequisition(requisition.getIdRequisition());
			final List<String[]> financialEntitiesActorSigns = this.createFinancialEntitiesActorSigns(requisition,
					financialEntitiesList, requisition.getActiveActor());
			final List<String[]> financialEntitiesSigns = this.createFinancialEntitiesSigns(requisition,
					financialEntitiesList);
			final List<String[]> supplierSignsData = this.createSupplierSigns(requisition,
					requisition.getPassiveActor());
			this.createFinancialEntitiesActorSigns(requisition, financialEntitiesList, requisition.getPassiveActor());
			
			final List<String[]> supplierSignsDataInvertedActors = this.createSupplierSigns(requisition,
					requisition.getActiveActor());
			if (requisition.getAppraisersPfName() != null) {
				final String[] appraiserName = { "<b>El \"Perito\"</b>",
						"<b>".concat(requisition.getAppraisersPfName()).concat("</b>") };
				supplierSignsData.add(appraiserName);
				this.reeplaceSignsTable(wordMLPackage, supplierLegalRepAndWitnessesNames, setColorMark,
						financialEntitiesActorSigns, supplierSignsData,
						TagFieldEnum.APRAISER_SIGNS_ACTORS_TABLE.getDescription());
			}
			this.reeplaceSignsTable(wordMLPackage, supplierLegalRepAndWitnessesNames, setColorMark,
					financialEntitiesActorSigns, supplierSignsData, TagFieldEnum.SIGNS_ACTORS_TABLE.getDescription());
			final LinkedHashMap<String, List<String>> emptyWitnessesSigns = new LinkedHashMap<>();
			emptyWitnessesSigns.put(CreaTablaFirmas.TITLETESTIGOS, new ArrayList<String>());
			emptyWitnessesSigns.put(CreaTablaFirmas.TESTIGOS, new ArrayList<String>());

			this.reeplaceSignsTable(wordMLPackage, emptyWitnessesSigns, setColorMark, financialEntitiesActorSigns,
					supplierSignsData, TagFieldEnum.SIGNS_REPRESENTATIVES_TABLE.getDescription());
			this.reeplaceSignsTable(wordMLPackage, supplierLegalRepAndWitnessesNames, setColorMark,
					financialEntitiesSigns, supplierSignsData, TagFieldEnum.SIGNS_SHORT_NAMES_TABLE.getDescription());
			this.reeplaceSignsTable(wordMLPackage, this.invertWitnesses(supplierLegalRepAndWitnessesNames),
					setColorMark, supplierSignsData, financialEntitiesActorSigns,
					TagFieldEnum.SIGNS_TABLE_ACTORS_INVERTED.getDescription());
			this.reeplaceSignsTable(wordMLPackage, this.invertWitnesses(supplierLegalRepAndWitnessesNames),
					setColorMark, supplierSignsDataInvertedActors, financialEntitiesSigns,
					TagFieldEnum.SIGNS_TABLE_SHORT_NAMES_INVERTED.getDescription());
			// this.reeplaceFinancialEntitiesSigns(wordMLPackage, setColorMark,
			// requisition.getIdRequisition(), financialEntitiesList);
		} catch (Exception docx4jException) {
			throw new BusinessException(MESSAGE_FILLING_TEMPLATE_ERROR, docx4jException);
		}
	}


	private LinkedHashMap<String, List<String>> invertWitnesses(
			final LinkedHashMap<String, List<String>> supplierLegalRepAndWitnessesNames) {
		final List<String> witnesses = supplierLegalRepAndWitnessesNames.get(CreaTablaFirmas.TESTIGOS);
		int index = 0;
		for (int cicle = 0; cicle < (witnesses.size() / 2); cicle++) {
			final String temp = witnesses.get(index);
			witnesses.set(index, witnesses.get(index + 1));
			witnesses.set(index + 1, temp);
			index = index + 2;
		}
		return supplierLegalRepAndWitnessesNames;
	}

	private void reeplaceSignsTable(final WordprocessingMLPackage wordMLPackage,
			final LinkedHashMap<String, List<String>> supplierLegalRepAndWitnessesNames, final Boolean setColorMark,
			final List<String[]> activeActorSigns, final List<String[]> passiveActorSigns, final String tagToReplace) {
		final CreaTablaFirmas signsTable = this.createTablaFirmasWithFormat(supplierLegalRepAndWitnessesNames);
		signsTable.setBanco(activeActorSigns);
		signsTable.setProveedor(passiveActorSigns);
		signsTable.setTagToReplace(tagToReplace);
		this.replaceSignsTable(wordMLPackage, setColorMark, signsTable);
	}

	private CreaTablaFirmas createTablaFirmasWithFormat(
			final LinkedHashMap<String, List<String>> supplierLegalRepAndWitnessesNames) {
		final CreaTablaFirmas signsTable = new CreaTablaFirmas(supplierLegalRepAndWitnessesNames);
		signsTable.setFormat(true);
		return signsTable;
	}

	private void replaceSignsTable(final WordprocessingMLPackage wordMLPackage, final Boolean setColorMark,
			final CreaTablaFirmas signsTable) {
		if (setColorMark)
			DocXUtil.replacePlaceholderTablaCBorrador(wordMLPackage, signsTable, signsTable.getTagToReplace());
		else
			DocXUtil.replacePlaceholderTablaC(wordMLPackage, signsTable, signsTable.getTagToReplace());
	}
	
	public Personality findPersonality(final Integer idSupplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findPersonality");
			return this.supplierable.findPersonality(idSupplier);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_PERSONALITY_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_PERSONALITY_ERROR, databaseException);
		}
	}
	
	public List<SupplierPerson> findLegalRepresentativesByIdRequisition(final Integer idRequisition, SupplierPersonTypeEnum type)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findLegalRepresentativesByIdRequisition");
			return this.supplierPersonable.findLegalRepresentativesByIdRequisition(idRequisition, type);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	private List<String[]> createSupplierSigns(final Requisition requisition, final String actor)
			throws BusinessException {
		final List<String[]> supplierSignsData = new ArrayList<>();
		final Supplier supplier = this.findByIdSupplier(requisition.getIdSupplier());
		final PersonalityEnum supplierPersonality = this.findPersonality(requisition.getIdSupplier())
				.getPersonalityEnum();

		// final List<SupplierPerson> suppliersList = this.supplierBusiness.findLegalRepresentativesByIdSupplier(requisition.getIdSupplier());
		final List<SupplierPerson> suppliersList = this.findLegalRepresentativesByIdRequisition(requisition.getIdRequisition(), 
				SupplierPersonTypeEnum.LEGALREPRESENTATIVE);

		this.validateSupplierNaturalPerson(supplier, supplierPersonality, suppliersList);
		for (int index = 0; index < suppliersList.size(); index++) {
			final String[] supplierSign = new String[2];
			supplierSign[0] = this.getSignTitle(0, actor,
					this.isNaturalPerson(supplierPersonality) ? "" : supplier.getCompanyName());
			supplierSign[1] = BOLD_OPEN + StringUtils.getStringNotNull(suppliersList.get(index).getName().toUpperCase())
			+ BOLD_CLOSE;
			supplierSignsData.add(supplierSign);
		}
		return supplierSignsData;
	}

	private void validateSupplierNaturalPerson(final Supplier supplier, final PersonalityEnum supplierPersonality,
			final List<SupplierPerson> suppliersList) {
		if (this.isNaturalPerson(supplierPersonality) && suppliersList.size() == 0) {
			suppliersList.clear();
			final SupplierPerson supplierNaturalPerson = new SupplierPerson();
			supplierNaturalPerson.setName(supplier.getCompanyName());
			suppliersList.add(supplierNaturalPerson);
		}
	}

	private boolean isNaturalPerson(final PersonalityEnum supplierPersonality) {
		return supplierPersonality == PersonalityEnum.NATURALPERSON
				|| supplierPersonality == PersonalityEnum.FOREIGNNATURALPERSON;
	}

	private void setCentsInRequisition(final Requisition requisition, final Map<String, String> mapValues) {
		mapValues.put(TagFieldEnum.AMOUNT_SERVICES_RENDERED_CENTS.getDescription(),
				this.findDecimals(requisition.getTotalAmountForServicesRendered()));
		mapValues.put(TagFieldEnum.RENT_INITIAL_AMOUNT_CENTS.getDescription(),
				this.findDecimals(requisition.getRentInitialQuantity()));
		mapValues.put(TagFieldEnum.INITIAL_ADVANCE_CENTS.getDescription(),
				this.findDecimals(requisition.getInitialAdvanceAmount()));
		mapValues.put(TagFieldEnum.TOTAL_NET_WORK_DONE_CENTS.getDescription(),
				this.findDecimals(requisition.getTotalNetAmountOfWorkDone()));
		mapValues.put(TagFieldEnum.DEPOSIT_AMOUNT_CENTS.getDescription(),
				this.findDecimals(requisition.getDepositAmount()));
		mapValues.put(TagFieldEnum.EXTENSION_MAINTENANCE_AMOUNT_CENTS.getDescription(),
				requisition.getExtensionAmount() != null
				? this.findDecimals(requisition.getExtensionAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.AMOUNT_POLICY_INSURANCE_CENT.getDescription(),
				requisition.getImportePolizaSeguro() != null
				? this.findDecimals(requisition.getImportePolizaSeguro().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.MONTHLY_RENT_AMOUNT_CENTS.getDescription(),
				this.findDecimals(requisition.getMonthlyRentAmount()));
		mapValues.put(TagFieldEnum.MONTHLY_RENT_AMOUNT_PENALTY_CENTS.getDescription(),
				this.findDecimals(requisition.getPenalizacionRentaMensualInmueble()));
		mapValues.put(TagFieldEnum.MONTHLY_MAINTENANCE_AMOUNT_CENTS.getDescription(),
				this.findDecimals(requisition.getMonthlyMaintenanceAmount()));
		mapValues.put(TagFieldEnum.FIRST_RENT_EXTENSION_INITIAL_AMOUNT_CENTS.getDescription(),
				this.findDecimals(requisition.getExtensionFirstYearRent()));
		mapValues.put(TagFieldEnum.EXTENSION_MAINTENANCE_AMOUNT_LETTER.getDescription(),
				requisition.getExtensionAmount() != null
				? this.convertNumberToString(requisition.getExtensionAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.MAINTENANCE_INITIAL_AMOUNT_CENTS.getDescription(),
				this.findDecimals(requisition.getMaintenanceInitialQuantity()));
		mapValues.put(TagFieldEnum.FULLFILMENT_BAIL_AMOUNT_CENTS.getDescription(),
				requisition.getFulfillmentBailAmount() != null
				? this.findDecimals(requisition.getFulfillmentBailAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.CONVENTIONAL_PENALTY_AMOUNT_CENTS.getDescription(),
				requisition.getConventionalPenaltyAmount() != null
				? this.findDecimals(requisition.getConventionalPenaltyAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.CONTINGENCY_BAIL_AMOUNT_CENTS.getDescription(),
				requisition.getContingencyBailAmount() != null
				? this.findDecimals(requisition.getContingencyBailAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.HIDDEN_VICES_AMOUNT_CENTS.getDescription(),
				requisition.getHiddenVicesAmount() != null
				? this.findDecimals(requisition.getHiddenVicesAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.ADVANCE_BAIL_AMOUNT_CENTS.getDescription(),
				requisition.getAdvanceBailAmount() != null
				? this.findDecimals(requisition.getAdvanceBailAmount().toString())
						: NOT_CAPTURED_FIELD);
		if(validaEnteros(requisition.getConsiderationAmount()))
			mapValues.put(TagFieldEnum.CONSIDERATION_AMOUNT_CENTS.getDescription(),
					requisition.getConsiderationAmount() != null
					? this.findDecimals(requisition.getConsiderationAmount().toString())
							: NOT_CAPTURED_FIELD);
	}

	private String findDecimals(final String value) {
		if (value != null) {
			final String returnString = value.contains(".") ? value.substring(value.indexOf(".") + 1, value.length())
					: "00";
			if (returnString.length() == 1)
				return returnString.concat("0");
			return returnString;
		}
		return NOT_CAPTURED_FIELD;
	}

	private void setAdicionalRequistionData(final Requisition requisition, final Map<String, String> mapValues)
			throws BusinessException {
		final List<FinancialEntity> financialEntitiesList = this
				.setLongNamesAndDomicilesAndCombinationNameOfFinancialEntities(requisition.getIdRequisition(),
						mapValues);
		LOG.info("Se cargarán los domicilios de los actores al mapa");
		this.setActorsDomiciles(requisition, financialEntitiesList, mapValues);
		LOG.info("Se cargarán los representantes legales del proveedor al mapa");
		this.setAllSupplierLegalRepresentatives(requisition.getIdRequisition(), mapValues); //this.setAllSupplierLegalRepresentatives(requisition.getIdSupplier(), mapValues);
		LOG.info("Se cargarán los testigos de las entidades al mapa");
		this.setRequisitionFinantialEntityWitness(requisition.getIdRequisition(), mapValues);
		LOG.info("Se cargarán la matriz de escalamiento del proveedor al mapa");
		this.setScalingMatrixSupplier(requisition.getIdRequisition(), mapValues);
		LOG.info("Se cargarán la matriz de escalamiento de las entidades al mapa");
		this.setScalingMatrixFinancialEntity(requisition.getIdRequisition(), mapValues);
		LOG.info("Se cargarán los nombres del proemio de las entidades al mapa");
		this.setProemFinancialEntitiesNames(requisition.getIdRequisition(), mapValues);
		LOG.info("Se cargarán los poderes de los representantes legales de las entidades al mapa");
		this.setPowersByLegalRepresentatives(requisition.getIdRequisition(), mapValues);
		LOG.info("Se cargarán los nombres de las entidades para el proemio de subcontratación al mapa");
		this.setSubcontractorsFinancialEntitiesNames(requisition.getIdRequisition(), mapValues);
		LOG.info("Se cargará la fecha actual al mapa");
		mapValues.put(TagFieldEnum.POWERS_BY_LEGAL_REPRESENTATIVES.getDescription(),
				"NO SE SELECIONARON REPRESENTANTES LEGALES PARA ESTA ENTIDAD FINANCIERA O ESTOS NO TIENEN UN PODER "
						+ "ASIGNADO PARA LA MISMA");
		this.setTodaydates(mapValues);
	}

	private List<String[]> createFinancialEntitiesActorSigns(final Requisition requisition,
			final List<FinancialEntity> financialEntitiesList, final String actor) throws BusinessException {
		final List<String[]> financialEntitiesSigns = new ArrayList<>();
		final List<LegalRepresentative> legalRepresentativesList = this
				.findRepLegalRepresentativeByRequisition(requisition.getIdRequisition());

		for (LegalRepresentative legalRepresentative : legalRepresentativesList) {
			final String[] legalRepresentativeSign = new String[2];
			legalRepresentativeSign[0] = this.getSignTitle(actor);

			for (FinancialEntity financialEntity : this.findFinancialEntitiesByRepLegalAndIdRequisition(
					legalRepresentative.getIdLegalRepresentative(), requisition.getIdRequisition()))
				legalRepresentativeSign[0] += BOLD_OPEN.concat(financialEntity.getLongName()).concat(BOLD_CLOSE)
				.concat(BR);

			legalRepresentativeSign[1] = BOLD_OPEN.concat(StringUtils.getStringNotNull(legalRepresentative.getName()))
					.concat(BOLD_CLOSE);

			financialEntitiesSigns.add(legalRepresentativeSign);
		}

		return financialEntitiesSigns;
	}

	private List<String[]> createFinancialEntitiesSigns(final Requisition requisition,
			final List<FinancialEntity> financialEntitiesList) throws BusinessException {
		final List<String[]> financialEntitiesSigns = new ArrayList<>();
		for (FinancialEntity financialEntity : financialEntitiesList) {
			this.createFiancialEntitySign(requisition.getIdRequisition(), financialEntitiesSigns, financialEntity);
		}
		return financialEntitiesSigns;
	}

	private void createFiancialEntitySign(final Integer idRequisition, final List<String[]> financialEntitiesSigns,
			FinancialEntity financialEntity) throws BusinessException {
		final List<LegalRepresentative> legalRepresentativesList = this
				.findActiveLegalRepByRequisitionAndFinancialEnt(idRequisition, financialEntity.getIdFinancialEntity());
		for (int index = 0; index < legalRepresentativesList.size(); index++) {
			final String[] legalRepresentativeSign = new String[2];
			legalRepresentativeSign[0] = this.getSignTitle(index, financialEntity.getName(),
					financialEntity.getLongName());
			legalRepresentativeSign[1] = BOLD_OPEN
					+ StringUtils.getStringNotNull(legalRepresentativesList.get(index).getName()) + BOLD_CLOSE;
			financialEntitiesSigns.add(legalRepresentativeSign);
		}
	}

	private void setPowersByLegalRepresentatives(final Integer idRequisition, final Map<String, String> mapValues)
			throws BusinessException {
		final StringBuilder builder = new StringBuilder();
		final StringBuilder alternativePower = new StringBuilder();
		final List<LegalRepresentative> legalRepresentativeList = this
				.findRequisitionLegalRepresentatives(idRequisition);
		for (LegalRepresentative bean : legalRepresentativeList) {
			bean.setFinancialEntitiesList(
					this.legalRepresentativeBusiness.findRequisitionFinantialEntitiesByIdLegalRepresentative(
							idRequisition, bean.getIdLegalRepresentative()));
		}
		this.buildPowersByLegalRepresentativesText(builder, alternativePower, legalRepresentativeList);
		mapValues.put(TagFieldEnum.POWERS_BY_LEGAL_REPRESENTATIVES_PROEM.getDescription(), builder.toString());
		mapValues.put(TagFieldEnum.ALTERNATIVE_POWERS_BY_LEGAL_REPRESENTATIVES.getDescription(),
				alternativePower.toString());
	}

	private void buildPowersByLegalRepresentativesText(final StringBuilder builder,
			final StringBuilder alternativePower, final List<LegalRepresentative> legalRepresentativeList) {
		for (LegalRepresentative legalRepresentative : legalRepresentativeList) {
			builder.append(legalRepresentative.getName()).append(COMMA);
			alternativePower.append(legalRepresentative.getName()).append(COMMA);
			this.buildPowersByFinancialEntity(builder, legalRepresentative);
			this.buildAlternativePowersByFinancialEntity(alternativePower, legalRepresentative);
		}
	}

	private void buildAlternativePowersByFinancialEntity(final StringBuilder alternativePower,
			final LegalRepresentative legalRepresentative) {
		for (FinancialEntity financialEntity : legalRepresentative.getFinancialEntitiesList())
			for (Power power : financialEntity.getPowersList())
				alternativePower.append(power.getAlternativePower());
	}

	private void buildPowersByFinancialEntity(final StringBuilder builder,
			final LegalRepresentative legalRepresentative) {
		for (FinancialEntity financialEntity : legalRepresentative.getFinancialEntitiesList()) {
			for (Power power : financialEntity.getPowersList()) {
				builder.append(power.getName());
			}
		}
	}

	private String getSignTitle(final Integer index, final String actor, final String name) {
		return index > 0 ? ""
				: BOLD_OPEN + QUOTES + actor + QUOTES + BOLD_CLOSE + BR + BOLD_OPEN + StringUtils.getStringNotNull(name)
				+ BOLD_CLOSE;
	}

	private String getSignTitle(final String actor) {
		return BOLD_OPEN + QUOTES + actor + QUOTES + BOLD_CLOSE + BR;
	}

	private LinkedHashMap<String, List<String>> createSupplierLegalRepAndWitnessesNamesMap(
			final List<String> legalRepresentativesNames, final List<String> witnessesNames) {
		final LinkedHashMap<String, List<String>> supplierLegalRepAndWitnessesNames = new LinkedHashMap<>();
		this.addWitnessData(supplierLegalRepAndWitnessesNames, witnessesNames);
		return supplierLegalRepAndWitnessesNames;
	}

	private List<String> mergeStringLists(final List<String> listOne, final List<String> listTwo) {
		final Integer biggerTableSize = listOne.size() > listTwo.size() ? listOne.size() : listTwo.size();
		final List<String> mergedList = new ArrayList<>();
		for (int index = 0; index < biggerTableSize; index++) {
			mergedList.add(this.getListElement(listOne, index));
			mergedList.add(this.getListElement(listTwo, index));
		}
		return mergedList;
	}

	private String getListElement(final List<String> list, final int index) {
		if (list.size() > index)
			return list.get(index);
		return "";
	}

	private void addApplicantData(final List<Object> objectsList, final Requisition requisition)
			throws BusinessException {
		final User applicant = this.usersBusiness.findByUserId(requisition.getIdApplicant());
		objectsList.add(applicant);
	}

	private void addSupplierData(final List<Object> objectsList, final Requisition requisition)
			throws BusinessException {
		final Supplier supplier = this.findByIdSupplier(requisition.getIdSupplier());
		final List<SupplierPerson> persons = this.findLegalRepresentativesByIdRequisition(requisition.getIdRequisition(), 
				SupplierPersonTypeEnum.LEGALREPRESENTATIVE);

		if (persons != null && persons.size() > 0) {
			objectsList.add(persons.get(0));
			supplier.setSupplierPerson(persons.get(0));

		}

		final List<SupplierPerson> witness = this.findLegalRepresentativesByIdRequisition(requisition.getIdRequisition(), 
				SupplierPersonTypeEnum.WITNESS);

		if (witness != null && witness.size() > 0) {
			supplier.setWitnessesList(witness);
		}
		objectsList.add(supplier);
		requisition.setSupplier(supplier);
	}

	private Requisition addRequisitionData(final Integer idRequisition, final List<Object> objectsList)
			throws BusinessException {
		final Requisition requisition = this.findById(idRequisition);
		requisition.setUnit(unitBusiness.getUnitById(requisition.getIdUnit()));
		requisition.setArea(areasBusiness.findById(requisition.getIdArea()));
		objectsList.add(requisition);
		return requisition;
	}

	private void setNumberInPercentaje(final Requisition requisition, final Map<String, String> mapValues) {
		mapValues.put(TagFieldEnum.DISCOUNT_AGREED_SERVICE_LETTER.getDescription(),
				PercentageToStringWords.convertPercentageToWord(requisition.getDiscountAgreedService()));
		mapValues.put(TagFieldEnum.CONVENTIONAL_PENALTY_PERCENTAGE_LETTER.getDescription(),
				PercentageToStringWords.convertPercentageToWord(requisition.getConventionalPenaltyPercentage()));
		mapValues.put(TagFieldEnum.INITIAL_PAYMENT_PERCENTAJE_LETTER.getDescription(),
				PercentageToStringWords.convertPercentageToWord(requisition.getInitialPaymentPercentage()));
	}

	private void setNumbersToCurrencyFormat(final Requisition requisition, final Map<String, String> mapValues) {
		mapValues.put(TagFieldEnum.TOTAL_AMOUNT_FOR_SERVICES_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getTotalAmountForServicesRendered()));
		mapValues.put(TagFieldEnum.AMOUNT_OF_INSURANCE_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getAmountOfInsuranceForDamageOrLoss()));
		mapValues.put(TagFieldEnum.EXTENSION_MAINTENANCE_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getExtensionAmount()));
		mapValues.put(TagFieldEnum.INITIAL_ADVANCE_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getInitialAdvanceAmount()));
		mapValues.put(TagFieldEnum.TOTAL_NET_WORK_DONE_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getTotalNetAmountOfWorkDone()));
		if(validaEnteros(requisition.getConsiderationAmount()))
			mapValues.put(TagFieldEnum.CONSIDERATION_AMOUNT_CURRENCY.getDescription(),
					CurrencyUtils.convertNumberToCurrencyFormat(requisition.getConsiderationAmount()));
		mapValues.put(TagFieldEnum.MONTHLY_MAINTENANCE_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getMonthlyMaintenanceAmount()));
		mapValues.put(TagFieldEnum.CONVENTIONAL_PENALTY_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getConventionalPenaltyAmount()));
		mapValues.put(TagFieldEnum.INITIAL_MAINTENANCE_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getMaintenanceInitialQuantity()));
		mapValues.put(TagFieldEnum.INITIAL_RENT_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getRentInitialQuantity()));
		mapValues.put(TagFieldEnum.CONTINGENCY_BAIL_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getContingencyBailAmount()));
		mapValues.put(TagFieldEnum.FULLFILMENT_BAIL_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getFulfillmentBailAmount()));
		mapValues.put(TagFieldEnum.MONTHLY_RENT_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getMonthlyRentAmount()));
		mapValues.put(TagFieldEnum.HIDDEN_VICES_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getHiddenVicesAmount()));
		mapValues.put(TagFieldEnum.ADVANCE_BAIL_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getAdvanceBailAmount()));
		mapValues.put(TagFieldEnum.FIRST_YEAR_RENT_AMOUNT_CURRENCY.getDescription(),
				CurrencyUtils.convertNumberToCurrencyFormat(requisition.getExtensionFirstYearRent()));
		if(validaEnteros(requisition.getConsiderationAmount()))
			mapValues.put(TagFieldEnum.CONSIDERATION_AMOUNT_CURRENCY.getDescription(),
					CurrencyUtils.convertNumberToCurrencyFormat(requisition.getConsiderationAmount()));
		mapValues.put(TagFieldEnum.DEPOSIT_AMOUNT.getDescription(),
				(requisition.getDepositAmount() == null || requisition.getDepositAmount().trim().isEmpty() ? "0.00"
						: CurrencyUtils.convertNumberToCurrencyFormat(requisition.getDepositAmount())));
	}

	private void setPowerDates(final Requisition requisition, final Map<String, String> mapValues) {
		mapValues.put(TagFieldEnum.PUBLIC_DEED_DATE_ENGLISH_LETTER.getDescription(),
				this.getDateValueInEnglish(requisition.getPublicDeedDate()));
		mapValues.put(TagFieldEnum.PUBLIC_COMMERCIAL_FOLIO_DATE_ENGLISH_LETTER.getDescription(),
				this.getDateValueInEnglish(requisition.getRegisterCommercialFolioDate()));
		mapValues.put(TagFieldEnum.PUBLIC_DEED_TITLE_DATE_ENGLISH_LETTER.getDescription(),
				this.getDateValueInEnglish(requisition.getSupplier().getPublicDeedTitleDate()));
		mapValues.put(TagFieldEnum.DEED_DATE_ENGLISH_LETTER.getDescription(),
				this.getDateValueInEnglish(requisition.getSupplier().getSupplierPerson() != null
				? requisition.getSupplier().getSupplierPerson().getDeedDate()
						: ""));
	}

	private void setNumbersInEnglish(final Requisition requisition, final Map<String, String> mapValues) {
		mapValues.put(TagFieldEnum.CALENDAR_DAY_DELIVERY_ENGLISH_LETTER.getDescription(),
				this.convertStringNumberToLong(requisition.getCalendarDayOfDeliveryDate()));
		mapValues.put(TagFieldEnum.BUSINESS_DAY_ACCEPTREJECT_ENGLISH_LETTER.getDescription(),
				this.convertStringNumberToLong(requisition.getBusinessDaysAcceptRejectPurchaseOrder()));
		// mapValues.put(TagFieldEnum.BUSINESS_DAY_ACCEPTREJECT_LETTER.getDescription(),
		// this.convertStringNumberToLong(requisition.getBusinessDaysAcceptRejectPurchaseOrder()));
		mapValues.put(TagFieldEnum.DAYS_FOR_ANOMALY_REPORT_ENGLISH_LETTER.getDescription(),
				this.convertStringNumberToLong(requisition.getDaysForAnomalyDeliveryReport()));
		mapValues.put(TagFieldEnum.DAYS_DEADLINE_FOR_PAYMENT_ENGLISH_LETTER.getDescription(),
				this.convertStringNumberToLong(requisition.getDaysDeadlineForPayment()));

		mapValues.put(TagFieldEnum.PERCENTAGE_SUFFERS_CONVENTIONAL_ENGLISH_LETTER.getDescription(),
				NumberToEnglishLetters.convertPercentageToWord(requisition.getConventionalPenaltyPercentage() == null ? 0
						: requisition.getConventionalPenaltyPercentage()));
		if(validaEnteros(requisition.getConsiderationAmount()))
			mapValues.put(TagFieldEnum.CONSIDERATION_AMOUNT_ENGLISH_LETTER.getDescription(),
					this.convertStringNumberToLong(requisition.getConsiderationAmount() == null ? "0"
							: requisition.getConsiderationAmount()));

		mapValues.put(TagFieldEnum.AMOUNT_POLICY_INSURANCE_ENGLISH_LETTER.getDescription(),
				this.convertStringNumberToLong(requisition.getImportePolizaSeguro() == null ? "0"
						: requisition.getImportePolizaSeguro().contains(".")
						? requisition.getImportePolizaSeguro().split("\\.")[0]
								: requisition.getImportePolizaSeguro()));

		mapValues.put(TagFieldEnum.BUSINESS_DAY_TO_MODIFY_ORDERS_ENGLISH_LETTER.getDescription(),
				this.convertStringNumberToLong(requisition.getBusinessDaysToModifyCancelOrders()));
		mapValues.put(TagFieldEnum.CALENDAR_DAY_WITHDRAW_CONTRACT_ENGLISH_LETTER.getDescription(),
				this.convertStringNumberToLong(requisition.getCalendarDaysToWithdrawContract()));
	}

	private void setNumbersInLetter(final Requisition requisition, final Map<String, String> mapValues) {
		mapValues.put(TagFieldEnum.AMOUNT_SERVICES_RENDERED_LETTER.getDescription(),
				this.convertNumberToString(requisition.getTotalAmountForServicesRendered()));
		mapValues.put(TagFieldEnum.INGLISH_SIGN_DATE.getDescription(),
				this.getDateValueInEnglish(requisition.getSignDate()));
		mapValues.put(TagFieldEnum.CALENDAR_DAY_DELIVERY_LETTER.getDescription(),
				this.convertNumberToString(requisition.getCalendarDayOfDeliveryDate()));
		mapValues.put(TagFieldEnum.BUSINESS_DAY_ACCEPTREJECT_LETTER.getDescription(),
				this.convertNumberToString(requisition.getBusinessDaysAcceptRejectPurchaseOrder()));
		mapValues.put(TagFieldEnum.BUSINESS_DAY_CANCEL_LETTER.getDescription(),
				this.convertNumberToString(requisition.getBusinessDaysToModifyCancelOrders()));
		mapValues.put(TagFieldEnum.CALENDAR_DAY_WITHDRAW_CONTRACT_LETTER.getDescription(),
				this.convertNumberToString(requisition.getCalendarDaysToWithdrawContract()));
		mapValues.put(TagFieldEnum.INITIAL_ADVANCE_LETTER.getDescription(),
				this.convertNumberToString(requisition.getInitialAdvanceAmount()));
		mapValues.put(TagFieldEnum.TOTAL_NET_WORK_DONE_LETTER.getDescription(),
				this.convertNumberToString(requisition.getTotalNetAmountOfWorkDone()));
		mapValues.put(TagFieldEnum.STEP_BUILD_LETTER.getDescription(),
				this.convertNumberToString(requisition.getStepsBuildNumber()));
		mapValues.put(TagFieldEnum.EXTENSION_FORCED_YEARS_LETTER.getDescription(),
				this.convertNumberToString(requisition.getExtensionForcedYears()));
		mapValues.put(TagFieldEnum.DEPOSIT_AMOUNT_LETTER.getDescription(),
				(requisition.getDepositAmount() == null || requisition.getDepositAmount().trim().isEmpty() ? "0.00"
						: this.convertNumberToString(requisition.getDepositAmount())));
		mapValues.put(TagFieldEnum.RENT_EQUIVALENT_LETTER.getDescription(),
				this.convertNumberToString(requisition.getRentEquivalent()));
		mapValues.put(TagFieldEnum.FORCED_YEARS_LETTER.getDescription(),
				this.convertNumberToString(requisition.getForcedYears()));
		mapValues.put(TagFieldEnum.SURFACE_LETTER.getDescription(),
				this.convertNumberToString(this.cleanNumber(requisition)));
		mapValues.put(TagFieldEnum.RENT_INITIAL_AMOUNT_LETTER.getDescription(),
				this.convertNumberToString(requisition.getRentInitialQuantity()));
		mapValues.put(TagFieldEnum.MAINTENANCE_INITIAL_AMOUNT_LETTER.getDescription(),
				this.convertNumberToString(requisition.getMaintenanceInitialQuantity()));
		mapValues.put(TagFieldEnum.EXTENSION_MAINTENANCE_AMOUNT_LETTER.getDescription(),
				requisition.getExtensionAmount() != null
				? this.convertNumberToString(requisition.getExtensionAmount().toString())
						: "");
		mapValues.put(TagFieldEnum.MONTHLY_RENT_AMOUNT_LETTER.getDescription(),
				this.convertNumberToString(requisition.getMonthlyRentAmount()));
		mapValues.put(TagFieldEnum.MONTHLY_MAINTENANCE_AMOUNT_LETTER.getDescription(),
				this.convertNumberToString(requisition.getMonthlyMaintenanceAmount()));
		mapValues.put(TagFieldEnum.ACTIVE_ACTOR_EXTENSION_FORCED_YEARS_LETTER.getDescription(),
				this.convertNumberToString(requisition.getAaExtensionForcedYears()));
		mapValues.put(TagFieldEnum.PASIVE_ACTOR_EXTENSION_FORCED_YEARS_LETTER.getDescription(),
				this.convertNumberToString(requisition.getPaExtensionForcedYears()));
		mapValues.put(TagFieldEnum.FIRST_RENT_EXTENSION_INITIAL_AMOUNT_LETTER.getDescription(),
				this.convertNumberToString(requisition.getExtensionFirstYearRent()));
		mapValues.put(TagFieldEnum.FULLFILMENT_BAIL_AMOUNT_LETTER.getDescription(),
				requisition.getFulfillmentBailAmount() != null
				? this.convertNumberToString(requisition.getFulfillmentBailAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.ADVANCE_BAIL_AMOUNT_LETTER.getDescription(),
				requisition.getAdvanceBailAmount() != null
				? this.convertNumberToString(requisition.getAdvanceBailAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.HIDDEN_VICES_AMOUNT_LETTER.getDescription(),
				this.convertNumberToString(requisition.getHiddenVicesAmount()));
		mapValues.put(TagFieldEnum.CONVENTIONAL_PENALTY_AMOUNT_LETTER.getDescription(),
				(!isNull(requisition.getConventionalPenaltyAmount()))
				? this.convertNumberToString(requisition.getConventionalPenaltyAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.CONTINGENCY_BAIL_AMOUNT_LETTER.getDescription(),
				(!isNull(requisition.getContingencyBailAmount()))
				? this.convertNumberToString(requisition.getContingencyBailAmount().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.EXTENSION_NUMBER_LETTER.getDescription(),
				(!isNull(requisition.getExtensionsNumber()))
				? this.convertNumberToString(requisition.getExtensionsNumber().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.EXTENSION_YEARS_LETTER.getDescription(),
				(!isNull(requisition.getExtensionYears()))
				? this.convertNumberToString(requisition.getExtensionYears().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.ACTIVE_ACTOR_EXTENSION_VOLUNTARY_YEARS_LETTER.getDescription(),
				(!isNull(requisition.getAaExtensionVoluntaryYears()))
				? this.convertNumberToString(requisition.getAaExtensionVoluntaryYears().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.PASIVE_ACTOR_EXTENSION_VOLUNTARY_YEARS_LETTER.getDescription(),
				(!isNull(requisition.getPaExtensionVoluntaryYears()))
				? this.convertNumberToString(requisition.getPaExtensionVoluntaryYears().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.DEPOSITS_REALIZED_MONTHS_NUMBER_LETTER.getDescription(),
				(!isNull(requisition.getDepositsRealizedMonthsNumber()))
				? this.convertNumberToString(requisition.getDepositsRealizedMonthsNumber().toString()).trim()
						: NOT_CAPTURED_FIELD);
		if(validaEnteros(requisition.getConsiderationAmount()))
			mapValues.put(TagFieldEnum.CONSIDERATION_AMOUNT_LETTER.getDescription(),
					requisition.getConsiderationAmount() != null
					? this.convertNumberToString(requisition.getConsiderationAmount().toString())
							: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.DAYS_DEADLINE_FOR_PAYMENT_LETTER.getDescription(),
				(!isNull(requisition.getDaysDeadlineForPayment()))
				? this.convertNumberToString(requisition.getDaysDeadlineForPayment().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.MONTHLY_RENT_PROPERTY_PENALTY_LETTER.getDescription(),
				(!isNull(requisition.getPenalizacionRentaMensualInmueble()))
				? this.convertNumberToString(requisition.getPenalizacionRentaMensualInmueble().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.AMOUNT_POLICY_INSURANCE_LETTER.getDescription(),
				(!isNull(requisition.getImportePolizaSeguro()))
				? this.convertNumberToString(requisition.getImportePolizaSeguro().toString())
						: NOT_CAPTURED_FIELD);
		mapValues.put(TagFieldEnum.PERCENTAGE_SUFFERS_CONVENTIONAL_LETTER.getDescription(),
				(!isNull(requisition.getConventionalPenaltyPercentage()))
				? PercentageToStringWords.convertPercentageToWord(requisition.getConventionalPenaltyPercentage())
						: NOT_CAPTURED_FIELD);

	}

	private String cleanNumber(final Requisition requisition) {
		return requisition.getSurface() != null ? requisition.getSurface().replaceAll("[\\D.]", "") : null;
	}

	private String convertNumberToString(final String value) {
		try {
			final Double doubleValue = Double.parseDouble(value);
			final String letter = NumbersToLetters.letterConvert(doubleValue.longValue());
			if (letter.equals(""))
				return "Cero";
			else
				return letter.substring(0, 1).toUpperCase().concat(letter.substring(1, letter.length()));
		
		}catch (NullPointerException n) {
			LOG.error("ERRROR :: convertNumberToString()-> "+value, n );
			return NOT_CAPTURED_FIELD;
		}
	}

	private String convertStringNumberToLong(final String value) {
		try {			
			final String letter = NumberToEnglishLetters.convertNumberToWord(value);
			if (letter.equals(""))
				return "zero";
			else
				return letter.substring(0, 1).toUpperCase().concat(letter.substring(1, letter.length()));
		}catch (NullPointerException n) {
			LOG.error("ERRROR :: convertNumberToString()-> "+value, n);
			return NOT_CAPTURED_FIELD;
		}
	}

	private void addWitnessData(final LinkedHashMap<String, List<String>> data,
			final List<String> supplierWitnessesNames) {
		final List<String> witnessTitle = new ArrayList<>();
		witnessTitle.add(BR + "<b>TESTIGOS:</b>");
		data.put(CreaTablaFirmas.TITLETESTIGOS, witnessTitle);
		data.put(CreaTablaFirmas.TESTIGOS, supplierWitnessesNames);
	}

	private void setTodaydates(final Map<String, String> mapValues) {
		final Date today = new Date();
		final SimpleDateFormat dateFormat = new SimpleDateFormat(SET_DATE_WITH_LONG_FORMAT, new Locale(ES));
		final SimpleDateFormat timeFormat24Hours = new SimpleDateFormat("HH:mm");
		final SimpleDateFormat timeFormat12Hours = new SimpleDateFormat("hh:mm aa");
		mapValues.put(TagFieldEnum.TODAY_DATE.getDescription(), dateFormat.format(today));
		mapValues.put(TagFieldEnum.NOW_HOUR_DATE_12.getDescription(), timeFormat12Hours.format(today));
		mapValues.put(TagFieldEnum.NOW_HOUR_DATE_24.getDescription(), timeFormat24Hours.format(today));
	}

	private void setProemFinancialEntitiesNames(final Integer idRequisition, final Map<String, String> mapValues)
			throws BusinessException {
		mapValues.put(TagFieldEnum.PROEM_FINANCIAL_ENT_REPRESENTATIVES_NAMES.getDescription(),
				this.buildProemFinanciaEntitiesNames(idRequisition,
						this.findActiveFinancialEntitiesByIdRequisition(idRequisition)));
	}

	private void setSubcontractorsFinancialEntitiesNames(final Integer idRequisition,
			final Map<String, String> mapValues) throws BusinessException {
		mapValues.put(TagFieldEnum.SUBCONTRACTOR_FINANCIAL_ENT_REPRESENTATIVES_NAMES.getDescription(),
				this.buildSubcontractorsFinancialEntitiesNames(idRequisition,
						this.findActiveFinancialEntitiesByIdRequisition(idRequisition),
						mapValues.get(TagFieldEnum.COMBINATION_NAME_FINANCIAL_ENTITIES.getDescription())));
	}

	public List<FinancialEntity> findActiveFinancialEntitiesByIdRequisition(final Integer idRequisition)
			throws BusinessException {
		try {
			return this.requisitable.findActiveFinancialEntitiesByIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_ERROR, databaseException);
		}
	}

	public List<FinancialEntity> findFinancialEntitiesByRepLegalAndIdRequisition(final Integer idRepLegal,
			final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findFinancialEntityByRepLegalAndRequisition(idRepLegal, idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FINATIAL_ENTITIES_ERROR, databaseException);
		}
	}

	public List<LegalRepresentative> findActiveLegalRepByRequisitionAndFinancialEnt(final Integer idRequisition,
			final Integer idFinancialEntity) throws BusinessException {
		try {
			return this.requisitable.findActiveLegalRepByRequisitionAndFinancialEnt(idRequisition, idFinancialEntity);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	public List<LegalRepresentative> findRepLegalRepresentativeByRequisition(final Integer idRequisition)
			throws BusinessException {
		try {
			return this.requisitable.findRequisitionLegalRepresentatives(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	private String buildProemFinanciaEntitiesNames(final Integer idRequisition,
			final List<FinancialEntity> financialEntitiesList) throws BusinessException {
		final StringBuilder proemNames = new StringBuilder();
		final StringBuilder financialEntitiesDesignation = new StringBuilder();
		for (FinancialEntity financialEntity : financialEntitiesList) {
			proemNames.append(BOLD_OPEN).append(this.getFinancialEntityLongName(financialEntity));
			proemNames.append(COMMA).append(BOLD_CLOSE);
			final List<LegalRepresentative> legalRepresentativesList = this
					.findActiveLegalRepByRequisitionAndFinancialEnt(idRequisition,
							financialEntity.getIdFinancialEntity());
			this.addRepresentatedBy(legalRepresentativesList, proemNames);
		}
		this.buildFinancialEntitiesProemNames(financialEntitiesDesignation, financialEntitiesList);
		return this.replaceYIWithEI(proemNames);
	}

	private String buildSubcontractorsFinancialEntitiesNames(final Integer idRequisition,
			final List<FinancialEntity> financialEntitiesList, final String combinationName) throws BusinessException {
		final StringBuilder subcontractNames = new StringBuilder();
		for (int index = 0; index < financialEntitiesList.size(); index++) {
			final FinancialEntity financialEntity = financialEntitiesList.get(index);
			subcontractNames.append(this.getFinancialEntityLongName(financialEntity)).append(COMMA);
			subcontractNames.append("(en lo sucesivo ");
			subcontractNames.append(StringUtils.getStringNotNull(financialEntity.getTreatment())).append(WHITE_SPACE);
			subcontractNames.append(QUOTES).append(financialEntity.getName()).append(QUOTES).append(")");
			subcontractNames.append(this.getTextSeparator(index, financialEntitiesList.size()).toLowerCase());
		}
		if (financialEntitiesList.size() > 1)
			subcontractNames.append(" y en forma conjunta como las ").append(combinationName).append(WHITE_SPACE);
		return this.replaceYIWithEI(subcontractNames);
	}

	private String replaceYIWithEI(final StringBuilder builderToReplace) {
		return builderToReplace.toString().replace(" Y I", " E I").replace(" Y <b>I", " E <b>I").replace(" y i", "e i")
				.replace(" y <b>y", " e <b>i");
	}

	private void buildFinancialEntitiesProemNames(final StringBuilder financialEntitiesDesignation,
			final List<FinancialEntity> financialEntities) {
		for (int financialIndex = 0; financialIndex < financialEntities.size(); financialIndex++) {
			final FinancialEntity financialEntity = financialEntities.get(financialIndex);
			financialEntitiesDesignation
			.append(StringUtils.getStringNotNull(financialEntity.getTreatment()).toUpperCase())
			.append(" <b>\"");
			financialEntitiesDesignation.append(financialEntity.getName().toUpperCase()).append("\"</b>");
			financialEntitiesDesignation.append(this.getTextSeparator(financialIndex, financialEntities.size()));
		}
	}

	private String getTextSeparator(final Integer index, final Integer size) {
		if (!this.isPenultimateElement(index, size))
			return COMMA;
		return " Y ";
	}

	private boolean isPenultimateElement(final int financialIndex, final int size) {
		return financialIndex == size - 2;
	}

	private void addRepresentatedBy(final List<LegalRepresentative> legalRepresentativeList,
			final StringBuilder proemNames) {
		proemNames.append("REPRESENTADA EN ESTE ACTO POR ");
		for (int index = 0; index < legalRepresentativeList.size(); index++) {
			proemNames.append(BOLD_OPEN);
			proemNames.append(legalRepresentativeList.get(index).getName().toUpperCase());
			proemNames.append(BOLD_CLOSE);
			proemNames.append(this.getTextSeparator(index, legalRepresentativeList.size()));
		}
		if (legalRepresentativeList.size() == 0) {
			proemNames.append(COMMA);
		}
	}

	private String getFinancialEntityLongName(final FinancialEntity financialEntity) {
		return financialEntity.getLongName() == null ? "NO TIENE NOMBRE LARGO" : financialEntity.getLongName();
	}

	private void setScalingMatrixSupplier(final Integer idRequisition, final Map<String, String> mapValues)
			throws BusinessException {
		final List<Scaling> scalingList = this.findScalingMatrixByIdRequisition(idRequisition,
				ScalingTypeEnum.SUPPLIER);

		for (int i = 0; i < scalingList.size(); i++) {
			mapValues.put(LEVEL_MATRIX_S + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getScalingLevel() == null ? Constants.BLANK
							: scalingList.get(i).getScalingLevel().toString());
			mapValues.put(NAME_MATRIX_S + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getName() == null ? Constants.BLANK : scalingList.get(i).getName());
			mapValues.put(AREA_MATRIX_S + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getArea() == null ? Constants.BLANK : scalingList.get(i).getArea());
			mapValues.put(PHONE_MATRIX_S + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getPhone() == null ? Constants.BLANK : scalingList.get(i).getPhone());
			mapValues.put(MAIL_MATRIX_S + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getMail() == null ? Constants.BLANK : scalingList.get(i).getMail());
		}

		if (scalingList.size() < NumbersEnum.FIVE.getNumber()) {
			this.completeScalingMatrixSupplier(mapValues, NumbersEnum.FIVE.getNumber() - scalingList.size(),
					scalingList.size());
		}
	}

	private void setScalingMatrixFinancialEntity(final Integer idRequisition, final Map<String, String> mapValues)
			throws BusinessException {
		final List<Scaling> scalingList = this.findScalingMatrixByIdRequisition(idRequisition,
				ScalingTypeEnum.FINANCIAL_ENTITY);

		for (int i = 0; i < scalingList.size(); i++) {
			mapValues.put(LEVEL_MATRIX_FE + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getScalingLevel() == null ? Constants.BLANK
							: scalingList.get(i).getScalingLevel().toString());
			mapValues.put(NAME_MATRIX_FE + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getName() == null ? Constants.BLANK : scalingList.get(i).getName());
			mapValues.put(AREA_MATRIX_FE + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getArea() == null ? Constants.BLANK : scalingList.get(i).getArea());
			mapValues.put(PHONE_MATRIX_FE + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getPhone() == null ? Constants.BLANK : scalingList.get(i).getPhone());
			mapValues.put(MAIL_MATRIX_FE + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					scalingList.get(i).getMail() == null ? Constants.BLANK : scalingList.get(i).getMail());
		}

		if (scalingList.size() < NumbersEnum.FIVE.getNumber()) {
			this.completeScalingMatrixFinancialEntity(mapValues, NumbersEnum.FIVE.getNumber() - scalingList.size(),
					scalingList.size());
		}
	}

	private void completeScalingMatrixFinancialEntity(final Map<String, String> mapValues, final Integer leftTagsCount,
			Integer currentIndex) {
		for (int i = 0; i < leftTagsCount; i++) {
			mapValues.put(LEVEL_MATRIX_FE + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			mapValues.put(NAME_MATRIX_FE + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			mapValues.put(AREA_MATRIX_FE + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			mapValues.put(PHONE_MATRIX_FE + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			mapValues.put(MAIL_MATRIX_FE + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			currentIndex++;
		}
	}

	private void completeScalingMatrixSupplier(final Map<String, String> mapValues, final Integer leftTagsCount,
			Integer currentIndex) {
		for (int i = 0; i < leftTagsCount; i++) {
			mapValues.put(LEVEL_MATRIX_S + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			mapValues.put(NAME_MATRIX_S + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			mapValues.put(AREA_MATRIX_S + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			mapValues.put(PHONE_MATRIX_S + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			mapValues.put(MAIL_MATRIX_S + (currentIndex + 1) + AMPERSAND_SQUARE_BRACKET, Constants.BLANK);
			currentIndex++;
		}
	}

	private void setRequisitionFinantialEntityWitness(final Integer idRequisition, final Map<String, String> mapValues)
			throws BusinessException {
		final List<FinantialEntityWitness> finantialWitness = this.findRequisitionFinancialEntityByIdRequisitionWitnes(idRequisition);
		this.buildStringBuilderFinantialWitness(finantialWitness, mapValues);
	}

	private void buildStringBuilderFinantialWitness(final List<FinantialEntityWitness> finantialWitnessList,
			final Map<String, String> mapValues) {
		for (Integer i = 0; i < finantialWitnessList.size(); i++) {
			mapValues.put("[&TestigoEntidadFinanciera" + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					finantialWitnessList.get(i).getName());
		}
	}

	private void setRequisitionLegalRepresentatives(final Integer idRequisition, final Map<String, String> mapValues)
			throws BusinessException {
		final StringBuilder builder = new StringBuilder();
		final List<LegalRepresentative> legalRepresentativeList = this.findRequisitionLegalRepresentatives(idRequisition);
		if (legalRepresentativeList.size() > 0) {
			this.buildStringBuilderRequisitionLegalRepresentatives(builder, legalRepresentativeList);
			mapValues.put(TagFieldEnum.REQUISITION_LEGAL_REPRESENTATIVES.getDescription(),
					builder.substring(0, builder.length() - 2));
			mapValues.put(TagFieldEnum.REQUISITION_LEGAL_REPRESENTATIVES_UPPER.getDescription(),
					builder.substring(0, builder.length() - 2).toUpperCase());

		}
		for (Integer i = 0; i < legalRepresentativeList.size(); i++) {
			mapValues.put("[&RepresentanteLegalSolicitud" + (i + 1) + AMPERSAND_SQUARE_BRACKET,
					legalRepresentativeList.get(i).getName());
		}
	}

	private void setRequisitionCustomsName(final Integer idRequisition, final Map<String, String> mapValues)
			throws BusinessException {
		final StringBuilder builder = new StringBuilder();
		final List<Customs> customsNameList = this.customsBusiness.getCustomsByIdRequisition(idRequisition);
		if (customsNameList.size() > 0) {
			this.buildStringBuilderRequisitionCustomsName(builder, customsNameList);
			mapValues.put(TagFieldEnum.CUSTOMS_NAME.getDescription(), builder.toString());
		}

	}

	private void setRequisitionModifiedClausules(final Integer idRequisition, final Map<String, String> mapValues)
			throws BusinessException {
		final StringBuilder builder = new StringBuilder();
		final List<ModifiedClausules> modifiedClausulesList = this.clausulesBusiness
				.getModifiedClausulesByIdRequisition(idRequisition);
		if (modifiedClausulesList.size() > 0) {
			this.buildStringBuilderRequisitionModifiedClausules(builder, modifiedClausulesList);
			;
			mapValues.put(TagFieldEnum.MODIFIED_CLAUSULES.getDescription(), builder.toString());
		}

	}

	private List<String> getRequisitionLegalRepresentatives(final Integer idRequisition) throws BusinessException {
		final List<String> legalRepresentativesNamesList = new ArrayList<>();
		for (LegalRepresentative legalRepresentative : this.findRequisitionLegalRepresentatives(idRequisition))
			legalRepresentativesNamesList.add(BOLD_OPEN + legalRepresentative.getName() + BOLD_CLOSE);
		return legalRepresentativesNamesList;
	}

	private void buildStringBuilderRequisitionLegalRepresentatives(final StringBuilder legalRepresentativeBuilder,
			final List<LegalRepresentative> legalRepresentativeList) {
		for (final LegalRepresentative legalRepresentative : legalRepresentativeList)
			if (legalRepresentative.getName() != null)
				legalRepresentativeBuilder.append(legalRepresentative.getName()).append(COMMA);
	}

	private void buildStringBuilderRequisitionCustomsName(final StringBuilder customNameBuilder,
			final List<Customs> customsList) {
		for (final Customs customs : customsList)
			if (customs.getName() != null)
				customNameBuilder.append(customs.getName()).append(BR);
	}

	private void buildStringBuilderRequisitionModifiedClausules(final StringBuilder builder,
			final List<ModifiedClausules> modifiedClausulesList) {
		for (final ModifiedClausules clausules : modifiedClausulesList) {
			builder.append(SAY);
			builder.append(BR);
			builder.append(clausules.getNumberClause() + COLON_SPACE + clausules.getNameClause());
			builder.append(BR);
			builder.append(BR);
			builder.append(DOUBLE_TABULATOR + clausules.getPreviousClause());
			builder.append(BR);
			builder.append(BR);
			builder.append(SHOULD_SAY);
			builder.append(BR);
			builder.append(clausules.getNumberClause() + COLON_SPACE + clausules.getNameClause());
			builder.append(BR);
			builder.append(BR);
			builder.append(DOUBLE_TABULATOR + clausules.getNewClause());
			builder.append(BR);
			builder.append(BR);
		}

	}

	private void setAllSupplierLegalRepresentatives(final Integer idRequisition, final Map<String, String> mapValues)
			throws BusinessException {

		final List<SupplierPerson> supplierPersonList = this.findLegalRepresentativesByIdRequisition(idRequisition, SupplierPersonTypeEnum.LEGALREPRESENTATIVE);

		if (supplierPersonList.size() > 0) {
			final StringBuilder builder = new StringBuilder();
			int cont = 0;
			for (final SupplierPerson legalRepresentative : supplierPersonList) {
				cont++;
				mapValues.put("[&NombreRepLegal" + cont + "&]", legalRepresentative.getName());
				mapValues.put("[&PropiedadOComercioRepLegal" + cont + "&]",
						legalRepresentative.getCommercialOrPropertyRegister());
				mapValues.put("[&EstadoIncripcionFolioMercantilRepLegal" + cont + "&]",
						legalRepresentative.getCommercialFolioInscriptionState());
				mapValues.put("[&EstadoNotariaPublicaRepLegal" + cont + "&]",
						legalRepresentative.getPublicNotaryState());
				mapValues.put("[&FechaEscrituraPublicaRepLegal" + cont + "&]",
						this.getStringValue(legalRepresentative.getDeedDate(), false));
				mapValues.put("[&FechaInscripcionFolioComercialRepLegal" + cont + "&]",
						this.getStringValue(legalRepresentative.getCommercialFolioInscriptionDate(), false));
				mapValues.put("[&FolioComercialRepLegal" + cont + "&]", legalRepresentative.getCommercialFolio());
				mapValues.put("[&NotarioEscrituraPublicaRepLegal" + cont + "&]",
						legalRepresentative.getPublicNotaryName());
				mapValues.put("[&NumeroEscrituraPublicaRepLegal" + cont + "&]", legalRepresentative.getDeedNumber());
				mapValues.put("[&NumeroNotariaPublicaRepLegal" + cont + "&]",
						legalRepresentative.getDeedNotaryNumber() != null
						? legalRepresentative.getDeedNotaryNumber().toString()
								: NOT_CAPTURED_FIELD);
				builder.append(legalRepresentative.getName()).append(COMMA);
			}
			String LegalRepresentativeUpper = builder.toString().substring(0, builder.length() - 2);
			mapValues.put(TagFieldEnum.SUPPLIER_LEGAL_REPRESENTATIVES.getDescription(),
					builder.toString().substring(0, builder.length() - 2));
			mapValues.put(TagFieldEnum.SUPPLIER_LEGAL_REPRESENTATIVES_UPPER.getDescription(),
					LegalRepresentativeUpper.toUpperCase());
		}
	}

	private List<String> getSupplierLegalRepresentatives(final Integer idRequisition) throws BusinessException {
		// final List<SupplierPerson> supplierPersonList = this.supplierBusiness.findSupplierPersonsByIdSupplierAndType(idSupplier, SupplierPersonTypeEnum.LEGALREPRESENTATIVE);
		final List<SupplierPerson> supplierPersonList = this.findLegalRepresentativesByIdRequisition(idRequisition, SupplierPersonTypeEnum.LEGALREPRESENTATIVE);
		final List<String> legalRepresentativeNames = new ArrayList<>();
		for (SupplierPerson legalRepresentative : supplierPersonList) {
			legalRepresentativeNames.add(BOLD_OPEN + legalRepresentative.getName() + BOLD_CLOSE);
		}
		return legalRepresentativeNames;
	}

	private List<String> getSupplierWitnessNames(final Integer idRequisition) throws BusinessException {
		// final List<SupplierPerson> supplierWitnessList = this.supplierBusiness.findSupplierPersonsByIdSupplierAndType(idSupplier, SupplierPersonTypeEnum.WITNESS);
		final List<SupplierPerson> supplierWitnessList = this.findLegalRepresentativesByIdRequisition(idRequisition, SupplierPersonTypeEnum.WITNESS);
		final List<String> witnessesNames = new ArrayList<>();
		for (SupplierPerson witness : supplierWitnessList) {
			witnessesNames.add(BOLD_OPEN + witness.getName().toUpperCase() + BOLD_CLOSE);
		}
		return witnessesNames;
	}

	private List<FinancialEntity> setLongNamesAndDomicilesAndCombinationNameOfFinancialEntities(
			final Integer idRequisition, final Map<String, String> mapValues) throws BusinessException {
		final StringBuilder financialEntities = new StringBuilder();
		final StringBuilder financialEntitiesDomiciles = new StringBuilder();
		final List<FinancialEntity> financialEntitiesList = this.financialEntityBusiness
				.findFinancialEntityByIdRequisition(idRequisition);
		this.setAccountNumberBanBranchAndBankingInstitution(mapValues, financialEntitiesList);
		this.setFinancialEntitiesShortNames(mapValues, financialEntitiesList);
		this.addFinancialEntitiesValues(mapValues, financialEntities, financialEntitiesDomiciles,
				financialEntitiesList);
		this.addFinancialEntitiesRepresentativesValues(idRequisition, financialEntitiesList, mapValues);
		mapValues.put(TagFieldEnum.LONG_NAME_FINANCIAL_ENTITIES.getDescription(), financialEntities.toString());
		mapValues.put(TagFieldEnum.LONG_NAME_FINANCIAL_ENTITIES_UPPER.getDescription(), financialEntities.toString().toUpperCase());
		mapValues.put(TagFieldEnum.DOMICILES_FINANCIAL_ENTITIES.getDescription(),
				financialEntitiesDomiciles.toString());
		final List<FinancialEntitieCombination> combinationsList = this.financialEntityBusiness
				.findFinancialEntityCombinationDistinctByCombinationName();
		this.setCombinationName(idRequisition, mapValues, combinationsList);
		return financialEntitiesList;
	}

	private void addFinancialEntitiesValues(final Map<String, String> mapValues, final StringBuilder financialEntities,
			final StringBuilder financialEntitiesDomiciles, final List<FinancialEntity> financialEntitiesList) {
		int index = 0;
		for (FinancialEntity financial : financialEntitiesList) {
			if (financialEntitiesList.size() > 1
					&& financial.equals(financialEntitiesList.get(financialEntitiesList.size() - 1)))
				financialEntities.append(UPPER_AND).append("\"" + financial.getLongName() + "\"").append(COMMA);
			else
				financialEntities.append("\"" + financial.getLongName() + "\"").append(COMMA);

			if (financial.getDomicile() != null) {
				if (financialEntitiesDomiciles.length() > 0)
					financialEntitiesDomiciles.append(COMMA);
				financialEntitiesDomiciles.append(financial.getDomicile());
			}
			mapValues.put("[&NombreLargoEntidadFinanciera" + (index + 1) + AMPERSAND_SQUARE_BRACKET,
					StringUtils.getStringNotNull(financial.getLongName()));
			mapValues.put("[&NombreCortoEntidadFinanciera" + (index + 1) + AMPERSAND_SQUARE_BRACKET,
					StringUtils.getStringNotNull(financial.getName()));
			mapValues.put("[&DomicilioDeEntidadFinanciera" + (index + 1) + AMPERSAND_SQUARE_BRACKET,
					StringUtils.getStringNotNull(financial.getDomicile()));
			mapValues.put("[&RfcEntidadFinanciera" + (index + 1) + AMPERSAND_SQUARE_BRACKET,
					StringUtils.getStringNotNull(financial.getRfc()));
			mapValues.put("[&TelefonoEntidadFinanciera" + (index + 1) + AMPERSAND_SQUARE_BRACKET,
					StringUtils.getStringNotNull(financial.getTelefono()));
			mapValues.put("[&CorreoEntidadFinanciera" + (index + 1) + AMPERSAND_SQUARE_BRACKET,
					StringUtils.getStringNotNull(financial.getCorreo()));
			mapValues.put("[&AtencionEntidadFinanciera" + (index + 1) + AMPERSAND_SQUARE_BRACKET,
					StringUtils.getStringNotNull(financial.getAtencion()));
			mapValues.put("[&ConstitutivaEntidadFinanciera" + (index + 1) + AMPERSAND_SQUARE_BRACKET,
					this.getConstitutiva(financial.getConstitutive()));
			index++;
		}
	}

	private void addFinancialEntitiesRepresentativesValues(final Integer idRequisition,
			final List<FinancialEntity> financialEntitiesList, final Map<String, String> mapValues)
					throws BusinessException {
		for (int index = 0; index < financialEntitiesList.size(); index++) {
			final Power power = this.findFinancialeEntityPowerByRequisitionAndFinancialEntity(idRequisition,
					financialEntitiesList.get(index).getIdFinancialEntity());
			mapValues.put("[&EntidadFinancieraRepresentantePoderNumeroEscrituraPublica" + (index + 1)
					+ AMPERSAND_SQUARE_BRACKET, StringUtils.getStringNotNull(power.getPublicDeedNumber()));
			mapValues.put("[&EntidadFinancieraRepresentantePoderFechaEscrituraPublica" + (index + 1)
					+ AMPERSAND_SQUARE_BRACKET, this.getStringValue(power.getPublicDeedDate(), false));
			mapValues.put("[&EntidadFinancieraRepresentantePoderNombreNotarioPublico" + (index + 1)
					+ AMPERSAND_SQUARE_BRACKET, StringUtils.getStringNotNull(power.getPublicNotaryName()));
			mapValues.put("[&EntidadFinancieraRepresentantePoderNumeroNotariaPublica" + (index + 1)
					+ AMPERSAND_SQUARE_BRACKET, StringUtils.getStringNotNull(power.getPublicNotaryNumber()));
			mapValues.put("[&EntidadFinancieraRepresentantePoderEstadoNotariaPublica" + (index + 1)
					+ AMPERSAND_SQUARE_BRACKET, StringUtils.getStringNotNull(power.getPublicNotaryState()));
			mapValues.put(
					"[&EntidadFinancieraRepresentantePoderFolioMercantil" + (index + 1) + AMPERSAND_SQUARE_BRACKET,
					StringUtils.getStringNotNull(power.getPublicCommercialFolio()));
			mapValues.put(
					"[&EntidadFinancieraRepresentantePoderFechaRegistroFolioMercantil" + (index + 1)
					+ AMPERSAND_SQUARE_BRACKET,
					this.getStringValue(power.getPublicCommercialFolioInscriptionDate(), false));
			mapValues.put(
					"[&EntidadFinancieraRepresentantePoderEstadoRegistroFolioMercantil" + (index + 1)
					+ AMPERSAND_SQUARE_BRACKET,
					StringUtils.getStringNotNull(power.getPublicCommercialFolioInscriptionState()));
		}
	}

	private void setActorsDomiciles(final Requisition requisition, final List<FinancialEntity> financialEntitiesList,
			final Map<String, String> mapValues) throws BusinessException {
		final StringBuilder actorsDomiciles = new StringBuilder();
		for (FinancialEntity beanF : financialEntitiesList) {
			this.buildFinancialEntityDomicile(actorsDomiciles, beanF, beanF.getName());
		}
		this.buildSupplierDomicile(actorsDomiciles, requisition, mapValues);
		mapValues.put(TagFieldEnum.ACTORS_DOMICILES.getDescription(), actorsDomiciles.toString());
	}

	private void buildFinancialEntityDomicile(final StringBuilder actorsDomiciles,
			final FinancialEntity financialEntity, final String activeActor) {
		actorsDomiciles.append(" \"").append(activeActor).append("\" ").append(COLON_SPACE).append(BR);
		actorsDomiciles.append(DOUBLE_TABULATOR).append(StringUtils.getStringNotNull(financialEntity.getDomicile()))
		.append(BR);
		this.addDomicileData(actorsDomiciles, "Tel.: ", StringUtils.getStringNotNull(financialEntity.getTelefono()));
		this.addDomicileData(actorsDomiciles, "Correo electrónico: ",
				StringUtils.getStringNotNull(financialEntity.getCorreo()));
		this.addDomicileData(actorsDomiciles, "Atención: ",
				StringUtils.getStringNotNull(financialEntity.getAtencion()));
		actorsDomiciles.append(BR);
	}

	private void fillValidFieldValuesByObject(final Map<String, String> mapValues, final Object object)
			throws BusinessException {
		final String tableName = object.getClass().getSimpleName().toUpperCase();
		LOG.info("Se cargaran a la tabla los datos del objeto: " + tableName);
		for (TagField tagField : this.tagFieldBusiness.findByTable(tableName)) {
			try {
				if (tagField.getTag().equals("[&EntidadFinacieraConstitutiva&]"))
					mapValues.put(tagField.getTag(), this.getConstitutiva(this.getStringValue(object, tagField)));
				else
					mapValues.put(tagField.getTag(), this.getStringValue(object, tagField));
			} catch (InvalidFieldException invalidFieldException) {
				final StringBuilder warningMessage = new StringBuilder();
				warningMessage.append("El campo ").append(tagField.getField()).append(" de etiqueta ");
				warningMessage.append(tagField.getTag()).append(" NO existe en la tabla ");
				warningMessage.append(tagField.getTableName());
				LOG.warn(warningMessage.toString(), invalidFieldException);
			}
		}
		LOG.info("Datos cargados a la tabla de reemplazos del objeto: " + tableName);
	}

	private void addDomicileData(final StringBuilder financialEntDomiciles, final String fieldName,
			final String domicileData) {
		if (domicileData != null)
			financialEntDomiciles.append(DOUBLE_TABULATOR).append(fieldName).append(domicileData).append(BR);
	}

	private void buildSupplierDomicile(final StringBuilder actorsDomiciles, final Requisition requisition,
			final Map<String, String> mapValues) throws BusinessException {
		final Supplier supplier = this.findByIdSupplier(requisition.getIdSupplier());
		this.buildSupplierConventionalAddress(supplier, mapValues);
		this.builSupplierFiscalAddress(supplier, mapValues);
		actorsDomiciles.append(" \"").append(requisition.getPassiveActor()).append("\" ").append(COLON_SPACE)
		.append(BR);
		actorsDomiciles.append(DOUBLE_TABULATOR);
		this.buildSupplierAddress(actorsDomiciles, supplier);
		this.addDomicileData(actorsDomiciles, "Tel.: ", supplier.getPhoneNumber());
		this.addDomicileData(actorsDomiciles, "Correo electrónico: ", supplier.getEmail());
		this.addDomicileData(actorsDomiciles, "Atención: ", supplier.getAtention());
	}

	private void buildSupplierConventionalAddress(final Supplier supplier, final Map<String, String> mapValues) {
		final StringBuilder supplierConventionalAddress = new StringBuilder();
		// supplierConventionalAddress.append(supplier.getStreetMail()).append(COMMA);
		supplierConventionalAddress.append(supplier.getStreetMail() == null ? "" : supplier.getStreetMail() + COMMA);
		if (supplier.getInteriorNumberMail() != null && supplier.getInteriorNumberMail().trim().length() > 0) {
			// supplierConventionalAddress.append(" No. Ext.
			// ").append(supplier.getExteriorNumberMail()).append(", ");
			supplierConventionalAddress.append(supplier.getExteriorNumberMail() == null ? ""
					: " No. Ext. " + supplier.getExteriorNumberMail() + ", ");
			// supplierConventionalAddress.append(" No. Int.
			// ").append(supplier.getInteriorNumberMail());
			supplierConventionalAddress.append(
					supplier.getInteriorNumberMail() == null ? "" : " No. Int. " + supplier.getInteriorNumberMail());
		} else {
			// supplierConventionalAddress.append(" No.
			// ").append(supplier.getExteriorNumberMail());
			supplierConventionalAddress
			.append(supplier.getExteriorNumberMail() == null ? "" : " No. " + supplier.getExteriorNumberMail());
		}
		this.addStringPart(supplierConventionalAddress, "en la Colonia ", supplier.getSuburbMail());
		this.addStringPart(supplierConventionalAddress, "C.P. ",
				StringUtils.getObjectStringValue(supplier.getPostalCodeMail()));
		this.addStringPart(supplierConventionalAddress, "en ",
				StringUtils.getObjectStringValue(supplier.getTownshipMail()));
		this.addStringPart(supplierConventionalAddress, "", supplier.getStateMail());
		mapValues.put(TagFieldEnum.SUPPLIER_CONVENTIONAL_ADDRESS.getDescription(),
				supplierConventionalAddress.toString());
	}

	private void builSupplierFiscalAddress(final Supplier supplier, final Map<String, String> mapValues) {
		final StringBuilder supplierFiscalAddress = new StringBuilder();
		supplierFiscalAddress.append(supplier.getStreet()).append(COMMA);
		if (supplier.getInteriorNumber() != null && !supplier.getInteriorNumber().isEmpty())
			supplierFiscalAddress.append("No. Ext. ").append(supplier.getExteriorNumber()).append(COMMA)
			.append("No. Int. ").append(supplier.getInteriorNumber());
		else
			supplierFiscalAddress.append("No. ").append(supplier.getExteriorNumber());
		this.addStringPart(supplierFiscalAddress, "en la Colonia", supplier.getSuburb());
		this.addStringPart(supplierFiscalAddress, "C.P. ", StringUtils.getObjectStringValue(supplier.getPostalCode()));
		this.addStringPart(supplierFiscalAddress, "en ", StringUtils.getObjectStringValue(supplier.getTownship()));
		this.addStringPart(supplierFiscalAddress, "", supplier.getState());
		mapValues.put(TagFieldEnum.SUPPLIER_FISCAL_ADDRESS.getDescription(), supplierFiscalAddress.toString());

	}

	private void addStringPart(final StringBuilder builder, final String stringPartPrefix, final String stringPart) {
		if (stringPart != null) {
			builder.append(COMMA).append(stringPartPrefix).append(stringPart);
		}
	}

	private void buildSupplierAddress(StringBuilder actorsDomiciles, Supplier supplier) {
		actorsDomiciles.append(supplier.getNonFiscalAddress()).append(COMMA);
		actorsDomiciles.delete(actorsDomiciles.length() - 2, actorsDomiciles.length());
		actorsDomiciles.append(BR);
	}

	private void setFinancialEntitiesShortNames(final Map<String, String> map, final List<FinancialEntity> entities) {
		if (entities.size() > 0) {
			final StringBuilder builder = new StringBuilder("");
			final StringBuilder finaEntityBuilder = new StringBuilder("");
			final StringBuilder financialEntiesProem = new StringBuilder("");
			for (FinancialEntity entity : entities) {
				this.addFinantialEntityInfo(builder, financialEntiesProem, entity);
				this.concatFinantialEntity(finaEntityBuilder, entity);
			}
			builder.replace(builder.lastIndexOf(COMMA), builder.length(), "");
			finaEntityBuilder.replace(finaEntityBuilder.lastIndexOf(COMMA), finaEntityBuilder.length(), "");
			financialEntiesProem.replace(financialEntiesProem.lastIndexOf(COMMA), financialEntiesProem.length(), "");
			map.put(TagFieldEnum.FINANCIAL_ENTITIES_SHORT_NAMES.getDescription(), builder.toString());
			map.put(TagFieldEnum.FINANCIAL_ENTITIES_SHORT_NAMES_WITHOUT_ARTICLES.getDescription(),
					finaEntityBuilder.toString());
			map.put(TagFieldEnum.FINANCIAL_ENTITIES_SHORT_NAMES_AND.getDescription(),
					this.addAndSeparator(map.get(TagFieldEnum.FINANCIAL_ENTITIES_SHORT_NAMES.getDescription())));
			map.put(TagFieldEnum.FINANCIAL_ENTITIES_SHORT_NAMES_PROEM.getDescription(),
					this.addAndSeparator(financialEntiesProem.toString()).toUpperCase().replace("<B>", "<b>")
					.replace("</B>", "</b>"));
			map.put(TagFieldEnum.FINANCIAL_ENTITIES_SHORT_NAMES_OR.getDescription(),
					this.addOrSeparator(map.get(TagFieldEnum.FINANCIAL_ENTITIES_SHORT_NAMES.getDescription())));
		}
	}

	private void addFinantialEntityInfo(final StringBuilder builder, final StringBuilder financialEntiesProem,
			final FinancialEntity entity) {
		if (entity.getName() != null) {
			this.addFinancialEntityTreatment(builder, financialEntiesProem, entity);
			builder.append(entity.getName()).append(COMMA);
			financialEntiesProem.append("<b>\"").append(entity.getName()).append("\"</b>").append(COMMA);
		}
	}

	private void concatFinantialEntity(final StringBuilder builder, final FinancialEntity entity) {
		if (entity.getName() != null)
			builder.append(entity.getName()).append(COMMA);
	}

	private void addFinancialEntityTreatment(final StringBuilder builder, final StringBuilder financialEntiesProem,
			final FinancialEntity entity) {
		if (entity.getTreatment() != null) {
			builder.append(StringUtils.getObjectStringValueNotNull(entity.getTreatment().toLowerCase())).append(" ");
			financialEntiesProem.append(StringUtils.getObjectStringValueNotNull(entity.getTreatment().toLowerCase()))
			.append(" ");
		}
	}

	private String addAndSeparator(final String str) {
		final StringBuilder text = new StringBuilder(str);
		if (text.lastIndexOf(COMMA) >= 0)
			text.replace(text.lastIndexOf(COMMA), text.lastIndexOf(COMMA) + 2, Y);
		return text.toString();
	}

	private String addOrSeparator(final String str) {
		final StringBuilder text = new StringBuilder(str);
		if (text.lastIndexOf(COMMA) >= 0)
			text.replace(text.lastIndexOf(COMMA), text.lastIndexOf(COMMA) + 2, " o ");
		return text.toString();
	}

	private void setAccountNumberBanBranchAndBankingInstitution(final Map<String, String> map,
			final List<FinancialEntity> financialList) {
		if (financialList.size() > 0) {
			map.put("[&NumeroCuentaEntidad&]", this.validNullValue(financialList.get(0).getAccountNumber()));
			map.put("[&SucursalEntidad&]", this.validNullValue(financialList.get(0).getBankBranch()));
			map.put("[&InstitucionBancariaEntidad&]",
					this.validNullValue(financialList.get(0).getBankingInstitution()));
		}
	}

	private String validNullValue(final String value) {
		return value == null ? "" : value;
	}

	private void setCombinationName(final Integer idRequisition, final Map<String, String> mapValues,
			final List<FinancialEntitieCombination> combinationsList) throws BusinessException {
		boolean isCombination = false;
		String combinationName = "";
		for (FinancialEntitieCombination combination : combinationsList) {
			isCombination = this.financialEntityBusiness.findIsCombination(idRequisition,
					combination.getCombinationName());
			if (isCombination) {
				combinationName = combination.getCombinationName();
				break;
			}
		}
		if (isCombination)
			mapValues.put(TagFieldEnum.COMBINATION_NAME_FINANCIAL_ENTITIES.getDescription(),
					combinationName.toUpperCase());
		else
			mapValues.put(TagFieldEnum.COMBINATION_NAME_FINANCIAL_ENTITIES.getDescription(),
					this.financialEntityBusiness.findDefaultCombinationName().toUpperCase() + WHITE_SPACE);
	}

	private void replaceFieldsByObject(final List<Object> objectsList, final Map<String, String> mapValues)
			throws BusinessException {
		for (Object object : objectsList)
			this.fillValidFieldValuesByObject(mapValues, object);
	}

	private String getStringValue(final Object object, final TagField tagField) throws InvalidFieldException {
		Object value = ReflectionUtils.getValue(object, tagField.getField());
		return this.getStringValue(value, tagField.getField().equals("signDate"));
	}

	private String getStringValue(Object value, boolean isSolicitudFechaFirma) {
		if (value == null)
			value = "";
		else {
			final Pattern simpleDatePattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}");
			Matcher matcher = simpleDatePattern.matcher(value.toString());
			try {
				if (matcher.matches() && isSolicitudFechaFirma)
					value = this.dateValueFormat("yyyy-MM-dd", SET_DATE_SIGN, value.toString());
				else if (matcher.matches())
					value = this.dateValueFormat("yyyy-MM-dd", SET_DATE_WITH_LONG_FORMAT, value.toString());
				else {
					final Pattern dateTimePattern = Pattern
							.compile("^\\d{4}-\\d{2}-\\d{2}\\p{Space}\\d{2}:\\d{2}:\\d{2}.\\d*");
					matcher = dateTimePattern.matcher(value.toString());
					if (matcher.matches())
						value = this.dateValueFormat("yyyy-MM-dd HH:mm:ss.S", "d 'de' MMMM 'de' yyyy 'a las' HH:mm",
								value.toString());
				}
			} catch (ParseException parseException) {
				LOG.error("Error al dar formato a fecha: " + value.toString(), parseException);
			}
		}
		return value.toString();
	}

	private String getDateValueInEnglish(Object value) {
		if (value == null)
			value = "";
		else {
			final Pattern simpleDatePattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}");
			Matcher matcher = simpleDatePattern.matcher(value.toString());
			try {
				if (matcher.matches())
					value = this.dateValueEnglishFormat("yyyy-MM-dd", "MMMM d',' yyyy", value.toString());
				else {
					final Pattern dateTimePattern = Pattern
							.compile("^\\d{4}-\\d{2}-\\d{2}\\p{Space}\\d{2}:\\d{2}:\\d{2}.\\d*");
					matcher = dateTimePattern.matcher(value.toString());
					if (matcher.matches())
						value = this.dateValueFormat("yyyy-MM-dd HH:mm:ss.S", "d 'of' MMMM 'of' yyyy 'to' HH:mm",
								value.toString());
				}
			} catch (ParseException parseException) {
				LOG.error("Error al dar formato a fecha: " + value.toString(), parseException);
			}
		}
		return value.toString();
	}

	public String dateValueFormat(final String fromDateFormat, final String toDateFormat, final String dateValue)
			throws ParseException {
		String formatedDate = null;
		if (dateValue != null) {
			final SimpleDateFormat fromDateTimeFormat = new SimpleDateFormat(fromDateFormat, new Locale(ES));
			final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(toDateFormat, new Locale(ES));
			final Date parsedDate = fromDateTimeFormat.parse(dateValue);
			formatedDate = toDateTimeFormat.format(parsedDate);
		}
		return formatedDate;
	}

	public String dateValueEnglishFormat(final String fromDateFormat, final String toDateFormat, final String dateValue)
			throws ParseException {
		String formatedDate = null;
		if (dateValue != null) {
			final SimpleDateFormat fromDateTimeFormat = new SimpleDateFormat(fromDateFormat, new Locale("us"));
			final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(toDateFormat, new Locale("us"));
			final Date parsedDate = fromDateTimeFormat.parse(dateValue);
			formatedDate = toDateTimeFormat.format(parsedDate);
		}
		return formatedDate;
	}

	private String getDocumentTypeTemporalPath() throws BusinessException {
		String tempPath = "";
		try {
			tempPath = this.configuration.getTemporalPath() + File.separator;
		} catch (ConfigurationException configurationException) {
			throw new BusinessException(configurationException);
		}
		if (!new File(tempPath).exists())
			throw new BusinessException("La carpeta de archivos temporales no existe");
		return tempPath;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendRequisitionToLawyer(final Requisition requisitionParam) throws BusinessException {
		final Integer idLawyer = this.usersContractBusiness
				.findAviableLawyer(requisitionParam.getFlowScreenActionParams().getIdFlow()).getIdUser();
		requisitionParam.setIdLawyer(idLawyer);
		this.saveRequisitionLawyer(requisitionParam);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void rejectRequisitionToLawyer(final Requisition requisitionParam) throws BusinessException {
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
		this.requisitionVersionBusiness.saveRequisitionVersion(requisitionParam.getIdRequisition());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveDraftInfo(final Requisition requisitionParam) throws BusinessException {
		try {

			LOG.error("IMSS 2: " + requisitionParam.getSupplierIMMS());
			this.requisitable.saveContractDraftFields(requisitionParam);
			this.saveRequisitionFinantialEntityWitness(requisitionParam.getIdRequisition(),
					requisitionParam.getFinancialEntityWitnessesList());
			this.requisitable.updateFinancialEntityRequisitionDraftFields(requisitionParam.getIdRequisition(),
					requisitionParam.getFinancialEntity());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SEND_CHANGE_DRAFT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SEND_CHANGE_DRAFT_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendReviewDraft(final Requisition requisitionParam) throws BusinessException {
		this.saveDraftInfo(requisitionParam);
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void changeReviewDraft(final Requisition requisitionParam) throws BusinessException {
		this.requisitionVersionBusiness.saveRequisitionVersion(requisitionParam.getIdRequisition());
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendDraftContractNegotiator(final Requisition requisitionParam) throws BusinessException {
		LOG.info("Se dió VoBo por el negociador");
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
		LOG.info("Se cambió el estatus de la solicitud");
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void rejectDraftContractNegotiator(final Requisition requisitionParam) throws BusinessException {
		this.requisitionVersionBusiness.saveRequisitionVersion(requisitionParam.getIdRequisition());
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void requestModificationDraft(final Requisition requisitionParam) throws BusinessException {
		this.requisitionVersionBusiness.saveRequisitionVersion(requisitionParam.getIdRequisition());
		if (requisitionParam.getTemplateUploadInfo() != null) {
			this.saveTemplateIdDocument(requisitionParam.getTemplateUploadInfo());
		}
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveDraftUserVobo(final Requisition requisitionParam, final Integer idUser) throws BusinessException {
		this.saveUserVobo(requisitionParam.getIdRequisition(), idUser);
		LOG.info("Se validará si todos los usuarios dieron VoBo");
		if (this.findIsAllUsersVobo(requisitionParam.getIdRequisition())) {
			LOG.info("Todos los usuarios dieron VoBO");
			LOG.info("Se cambiará el estatus de la solicitud");
			this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
					requisitionParam.getFlowScreenActionParams());
			LOG.info("Se limpiarán los VoBos");
			this.cleanUsersVobo(requisitionParam.getIdRequisition());
			LOG.info("Se limpiaron los VoBos correctamente");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void rejectDraftContractUser(final Requisition requisitionParam) throws BusinessException {
		this.requisitionVersionBusiness.saveRequisitionVersion(requisitionParam.getIdRequisition());
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
		this.cleanUsersVobo(requisitionParam.getIdRequisition());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public Integer saveVoBo(final Requisition requisitionParam) throws BusinessException {
		this.saveRequisitionApprovalAreasVoBo(requisitionParam.getIdRequisition(),
				requisitionParam.getDocumentsAreasVoBoList());
		return this.saveSupplierApprovalIdDocument(requisitionParam.getIdRequisition(),
				requisitionParam.getSupplierApprovalDocument());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void deleteSupplierApprovalDocument(final Integer idRequisition, final Integer idDocument)
			throws BusinessException {
		try {
			this.documentVersionBusiness.deleteByIdDocument(idDocument);
			this.requisitable.deleteSupplierApprovalDocument(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETE_SUPPLIER_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETE_SUPPLIER_VOBO_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendToVoBo(final Requisition requisitionParam) throws BusinessException {
		/*
		 * this.saveRequisitionApprovalAreasVoBo(requisitionParam.getIdRequisition(),
		 * requisitionParam.getDocumentsAreasVoBoList());
		 * this.saveSupplierApprovalIdDocument(requisitionParam.getIdRequisition(),
		 * requisitionParam.getSupplierApprovalDocument());
		 */
//		final FileUploadInfo approvalSupplierDocument=requisitionParam.getSupplierApprovalDocument();
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
		try {
			this.requisitable.updateContractRiskByIdRequisition(requisitionParam.getIdRequisition(),
					(requisitionParam.isContractRisk()));
			//servicios para subir archivo final
//			if(approvalSupplierDocument.getDocumentName()!=null) {
//			approvalSupplierDocument.setIdFile(this.versionRequisitionDocument(requisitionParam.getIdRequisition(), approvalSupplierDocument));
//			 this.requisitable.saveSupplierApprovalIdDocument(requisitionParam.getIdRequisition(), approvalSupplierDocument.getIdFile());
//			 this.saveTemplateIdDocument(approvalSupplierDocument);
//			}else {
//				System.out.println("no se cargo el archivo final");
//			}
		} catch (DatabaseException databaseException) {
			LOG.error("Error al actualizar el contrato: ContractRisk", databaseException);
			throw new BusinessException("Error al actualizar el contrato: ContractRisk", databaseException);
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendToDocumentFinal(final Requisition requisitionParam) throws BusinessException {
		final FileUploadInfo approvalSupplierDocument=requisitionParam.getSupplierApprovalDocument();
//		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
//				requisitionParam.getFlowScreenActionParams());
		try {
			//servicios para subir archivo final
			if(approvalSupplierDocument.getDocumentName()!=null) {
			approvalSupplierDocument.setIdFile(this.versionRequisitionDocumentFinal(requisitionParam.getIdRequisition(), approvalSupplierDocument));
			LOG.info("##################################################");
			LOG.info("EL ID DE DOCUMENT ES :::: "+approvalSupplierDocument.getIdFile());
			LOG.info("##################################################");
			List<VersionDTO> dtos = this.documentVersionBusiness.findContractVersionDTO(approvalSupplierDocument.getIdRequisition());
			this.validDocumentVersion(approvalSupplierDocument, dtos);
//			final Integer idVersionedDocument = this.versionSupplierDocument(approvalSupplierDocument.getIdRequisition(), approvalSupplierDocument);
			LOG.info("##################################################");
			LOG.info("EL List<VersionDTO> dtos ES :::: "+dtos);
			LOG.info("##################################################");
			LOG.info("##################################################");
			LOG.info("EL idVersionedDocument ES :::: "+approvalSupplierDocument.getIdFile());
			LOG.info("##################################################");
			
			this.requisitable.saveSupplierApprovalDocument(approvalSupplierDocument.getIdRequisition(), approvalSupplierDocument.getIdFile());
			this.saveContractVersion(approvalSupplierDocument.getIdRequisition(), dtos, approvalSupplierDocument.getIdFile());
//			this.versionSupplierDocumentFinal(getIdJuridico(), approvalSupplierDocument);
//			 this.requisitable.saveSupplierApprovalIdDocument(requisitionParam.getIdRequisition(), approvalSupplierDocument.getIdFile());
//			 this.SupplierApprovalDocument(approvalSupplierDocument);
			}else {
				System.out.println("no se cargo el archivo final");
			}
//	} catch (DatabaseException databaseException) {
//		LOG.error("Error al actualizar el contrato: ContractRisk", databaseException);
//		throw new BusinessException("Error al actualizar el contrato: ContractRisk", databaseException);
//	}
//}
		} catch (Exception databaseException) {
			LOG.error("Error al actualizar el contrato: ContractRisk", databaseException);
			throw new BusinessException("Error al actualizar el contrato: ContractRisk", databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void rejectToVoBo(final Requisition requisitionParam) throws BusinessException {
		this.requisitionVersionBusiness.saveRequisitionVersion(requisitionParam.getIdRequisition());
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendApprovedContract(final Requisition requisitionParam) throws BusinessException {
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendSigningContract(final Requisition requisitionParam) throws BusinessException {
		this.saveRequisitionEvaluator(requisitionParam);
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveSignContract(final Requisition requisitionParam) throws BusinessException {
		this.saveProviderAndWitnessesSignDates(requisitionParam);
		this.saveRequisitionLegalRepresentative(requisitionParam.getIdRequisition(),
				requisitionParam.getLegalReprentativeIdList());
		this.saveRequisitionLegalRepresentativesSignDates(requisitionParam.getIdRequisition(),
				requisitionParam.getLegalRepresentativesList());
		this.saveObligations(requisitionParam.getIdRequisition(), requisitionParam.getObligationsList());
		if (requisitionParam.getSupplierSignSendDate() != null && requisitionParam.getSupplierSignReturnDate() != null
				&& requisitionParam.getWitnessesSignSendDate() != null
				&& requisitionParam.getWitnessesSignReturnDate() != null
				&& requisitionParam.getLegalRepresentativesList() != null
				&& this.validateSignsLegalRepresentatives(requisitionParam))
			this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
					requisitionParam.getFlowScreenActionParams());
	}

	private Boolean validateSignsLegalRepresentatives(Requisition requisitionParameter) {
		Boolean valid = true;
		for (LegalRepresentative legalRepresentative : requisitionParameter.getLegalRepresentativesList()) {
			if (legalRepresentative.getSignSendDate() == null || legalRepresentative.getSignReturnDate() == null)
				valid = false;
		}
		return valid;
	}

	private void freezeInformationToContractDetail(final Integer idRequisitionParameter) throws BusinessException {
		final ContractDetail contractDetailBean = this.createContractDetail(idRequisitionParameter);
//		final String contractDetailJSON = this.contractDetailToJson(contractDetailBean);
//		this.saveFreezeInformationOfContractDetail(idRequisitionParameter, contractDetailJSON);
	}

	private void saveFreezeInformationOfContractDetail(final Integer idRequisitionParameter,
			final String contractDetailJsonParameter) throws BusinessException {
		try {
			this.requisitable.saveFreezeInformationOfContractDetail(idRequisitionParameter,
					contractDetailJsonParameter);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_FREEZE_CONTRACT_DETAIL_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_FREEZE_CONTRACT_DETAIL_ERROR, databaseException);
		}
	}

	private String contractDetailToJson(final ContractDetail contractDetailBeanParameter) {
		return new Gson().toJson(contractDetailBeanParameter);
	}

	private ContractDetail createContractDetail(final Integer idRequisition) throws BusinessException {
		final Requisition requisitionParameter = this.findWholeRequisitionById(idRequisition);
		final ContractDetail contracDetail = new ContractDetail();
		LOG.info("## CONSULTA A EL SOLICITANTE");
		this.setUserInformationToContractDetail(contracDetail,
				this.usersBusiness.findByUserId(requisitionParameter.getIdApplicant()));
		this.setSupplierDataToContractDetail(requisitionParameter, contracDetail,
				this.findByIdSupplier(requisitionParameter.getIdSupplier()));
		this.setAuthorizationLevelToContractDetail(requisitionParameter, contracDetail);
		this.setInstrumentDataToContractDetail(requisitionParameter, contracDetail);
		this.setScalingMatrixToContractDetail(requisitionParameter, contracDetail);
		LOG.info("## CONSULTA A EL ABOGADO");
		this.setAssignedLawyerToContractDetail(contracDetail,
				this.usersBusiness.findByUserId(requisitionParameter.getIdLawyer()),
				requisitionParameter.getIdSupplier());
		this.setApprovalAreasVoBoToContractDetail(requisitionParameter, contracDetail);
		LOG.info("## CONSULTA AL EVALUADOR");
		if (requisitionParameter.getIdEvaluator() != null)
			this.setEvaluatorInformationToContractDetail(contracDetail,
					this.usersBusiness.findByUserId(requisitionParameter.getIdEvaluator()));
		this.setFirmsToContractDetail(requisitionParameter, contracDetail);
		contracDetail.setSupplierLegalRepresentativeList(
				findLegalRepresentativesByIdSupplier(requisitionParameter.getIdSupplier()));
		contracDetail.setRequisition(requisitionParameter);
		return contracDetail;
	}

	private void setFirmsToContractDetail(final Requisition requisitionParameter,
			final ContractDetail contractDetailParameter) throws BusinessException {
		requisitionParameter.setLegalRepresentativesList(
				this.findRequisitionLegalRepresentatives(requisitionParameter.getIdRequisition()));
		contractDetailParameter
		.setObligationsList(this.findObligationsByIdRequisition(requisitionParameter.getIdRequisition()));
	}

	private void setEvaluatorInformationToContractDetail(final ContractDetail contracDetailParameter,
			final User userParameter) {
		contracDetailParameter.setEvaluatorName(userParameter.getFullName());
	}

	private void setApprovalAreasVoBoToContractDetail(final Requisition requisitionParameter,
			final ContractDetail contracDetail) throws BusinessException {
		contracDetail.setAviableAreasList(this.areasBusiness.findByRecordStatus(RecordStatusEnum.ACTIVE));
		contracDetail.setApprovalAreasVoBoList(
				this.findRequisitionApprovalAreasVoBo(requisitionParameter.getIdRequisition()));
	}

	private void setAssignedLawyerToContractDetail(final ContractDetail contracDetailParameter,
			final User userParameter, final Integer idSupplierParameter) throws BusinessException {
		contracDetailParameter.setAssignedLawyerIdLawyer(userParameter.getIdUser());
		contracDetailParameter.setAssignedLawyerName(userParameter.getFullName());
	}

	private void setScalingMatrixToContractDetail(final Requisition requisitionParameter,
			final ContractDetail contracDetail) throws BusinessException {
		contracDetail.setScalingMatrixSupplierList(this
				.findScalingMatrixByIdRequisition(requisitionParameter.getIdRequisition(), ScalingTypeEnum.SUPPLIER));
		contracDetail.setScalingMatrixFinancialEntity(this.findScalingMatrixByIdRequisition(
				requisitionParameter.getIdRequisition(), ScalingTypeEnum.FINANCIAL_ENTITY));
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveContractToDigitalize(final Requisition requisitionParam) throws BusinessException {
		this.saveRequisitionSignedContractData(requisitionParam);
		this.saveDigitalizationDocuments(requisitionParam.getIdRequisition(),
				requisitionParam.getDigitalizationDocument());
	}

	private void setInstrumentDataToContractDetail(final Requisition requisitionParameter,
			final ContractDetail contracDetail) throws BusinessException {
		contracDetail.setInstrumentDataFinantialEntityWitnessList(
				this.findRequisitionFinancialEntityByIdRequisitionWitnes(requisitionParameter.getIdRequisition()));
		contracDetail.setInstrumentDataFinancialEntityList(
				this.findRequisitionFinancialEntityActiveByIdRequisition(requisitionParameter.getIdRequisition()));
		requisitionParameter.setDataFinancialEntityList(this.financialEntityBusiness
				.findDataFinantialEntityRequisition(requisitionParameter.getIdRequisition()));
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveAndSendContractToDigitalize(final Requisition requisitionParam) throws BusinessException {
		this.saveRequisitionSignedContractData(requisitionParam);
		this.saveDigitalizationDocuments(requisitionParam.getIdRequisition(),
				requisitionParam.getDigitalizationDocument());
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
		this.freezeInformationToContractDetail(requisitionParam.getIdRequisition());
	}

	private void setAuthorizationLevelToContractDetail(final Requisition requisitionParameter,
			final ContractDetail contracDetailParameter) throws BusinessException {
		contracDetailParameter.setAuthorizationLevelAuthorizationDgasList(
				this.findRequisitionAuthorizationDgasActive(requisitionParameter.getIdRequisition()));
		contracDetailParameter.setAuthorizationLevelAprovalAreasList(
				this.findRequisitionApprovalAreasActive(requisitionParameter.getIdRequisition()));
		contracDetailParameter.setAuthorizationLevelAddedUsersToVoBoList(
				this.findUsersToVoBo(requisitionParameter.getIdRequisition()));
	}

	private void setLegalRepresentativesAndWitnessToContractDetail(final ContractDetail contracDetailParameter,
			final Integer idSupplierParameter) throws BusinessException {
		contracDetailParameter.setSupplierLegalRepresentativeList(
				this.findLegalRepresentativesByIdSupplier(idSupplierParameter));
		contracDetailParameter.setSupplierWitnessList(this.findSupplierPersonsByIdSupplierAndType(idSupplierParameter, SupplierPersonTypeEnum.WITNESS));
	}
	
	public List<SupplierPerson> findSupplierPersonsByIdSupplierAndType(final Integer idSupplier,
			final SupplierPersonTypeEnum supplierPersontype) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findSupplierPersonsByIdSupplierAndType");
			return this.supplierPersonable.findSupplierPersonsByIdSupplierAndType(idSupplier, supplierPersontype);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_SUPPLIER_PERSON_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_SUPPLIER_PERSON_ERROR, databaseException);
		}
	}

	private void setSupplierDataToContractDetail(final Requisition requisitionParameter,
			final ContractDetail contracDetailParameter, final Supplier supplierParameter) throws BusinessException {
		requisitionParameter.setSupplier(supplierParameter);
		this.setLegalRepresentativesAndWitnessToContractDetail(contracDetailParameter,
				supplierParameter.getIdSupplier());
	}

	public final void setUserInformationToContractDetail(final ContractDetail contractDetailParameter,
			final User userParameter) throws BusinessException {
		contractDetailParameter.setUserApplicant(userParameter);
		contractDetailParameter.setPersonalityList(this.personalitiesBusiness.findByStatus(RecordStatusEnum.ACTIVE));
	}

	public ContractDetail findContractDetailByIdRequisition(final Integer idRequisition) throws BusinessException {
		try {
			final String contractDetailJson = this.requisitable.findContractDetailByIdRequisition(idRequisition);
			return this.createContractDetailFromJson(contractDetailJson);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_CONTRACT_DETAIL_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_CONTRACT_DETAIL_ERROR, databaseException);
		}
	}

	private ContractDetail createContractDetailFromJson(final String contractDetailJsonParameter) {
		final Gson gson = new Gson();
		final ContractDetail contractDetail = gson.fromJson(contractDetailJsonParameter, ContractDetail.class);
		return contractDetail;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveDocumentationMissing(final Requisition requisitionParam) throws BusinessException {
		try {
			saveRequisitionAttatchmentsFields(requisitionParam);
			supplierRequiredDocument(requisitionParam.getSupplier().getSupplierRequiredDocument(),
					requisitionParam.getIdSupplier());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_UPDATING_ATTATCHMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_UPDATING_ATTATCHMENTS_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendDocumentationMissing(final Requisition requisitionParam) throws BusinessException {
		try {
			saveRequisitionAttatchmentsFields(requisitionParam);
			this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
					requisitionParam.getFlowScreenActionParams());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_UPDATING_ATTATCHMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_UPDATING_ATTATCHMENTS_ERROR, databaseException);
		}
	}

	public void saveRequisitionEvaluator(final Requisition requisition) throws BusinessException {
		try {
			this.requisitable.saveRequisitionEvaluator(requisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_EVALUATOR_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_EVALUATOR_ERROR, databaseException);
		}
	}

	public void saveProviderAndWitnessesSignDates(final Requisition requisition) throws BusinessException {
		try {
			this.requisitable.saveProviderAndWitnessesSignDates(requisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_PROVIDER_WINTNESS_SIGN_DATES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_PROVIDER_WINTNESS_SIGN_DATES_ERROR, databaseException);
		}
	}

	public void saveRequisitionLegalRepresentativesSignDates(final Integer idRequisition,
			final List<LegalRepresentative> legalRepresentativesList) throws BusinessException {
		try {
			for (LegalRepresentative legalRepresentative : legalRepresentativesList)
				this.requisitable.saveRequisitionLegalRepresentativeSignDate(idRequisition, legalRepresentative);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGN_DATES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGN_DATES_ERROR, databaseException);
		}
	}

	public void saveRequisitionSignedContractData(final Requisition requisition) throws BusinessException {
		try {
			this.requisitable.saveRequisitionSignedContractData(requisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_SIGNED_CONTRACT_DATA_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_SIGNED_CONTRACT_DATA_ERROR, databaseException);
		}
	}

	// public void saveRequisitionLegalRepresentativesSignedContractData(final
	// Integer idRequisition,
	// final List<LegalRepresentative> legalRepresentativesList) throws
	// BusinessException {
	// try {
	// for (LegalRepresentative legalRepresentative : legalRepresentativesList)
	// this.requisitable.saveRequisitionLegalRepresentativeSignedContractData(idRequisition,
	// legalRepresentative);
	// } catch (DatabaseException databaseException) {
	// LOG.error(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGNED_CONTRACT_DATA_ERROR,
	// databaseException);
	// throw new
	// BusinessException(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGNED_CONTRACT_DATA_ERROR,
	// databaseException);
	// }
	// }

	public List<ApprovalArea> findRequisitionApprovalAreasVoBo(final Integer idRequisition) throws BusinessException {
		try {
			final List<ApprovalArea> approvalAreasList = this.requisitable
					.findRequisitionApprovalAreasVoBo(idRequisition);
			for (ApprovalArea approvalArea : approvalAreasList)
				approvalArea.setDocumentPath(FilenameUtils.getName(approvalArea.getDocumentPath()));
			return approvalAreasList;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_APPROVAL_AREAS_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_APPROVAL_AREAS_VOBO_ERROR, databaseException);
		}
	}

	public List<Version> findDigitalizationDocuments(final Integer idRequisition) throws BusinessException {
		try {
			final List<Version> versionNameList = this.requisitable.findDigitalizationDocuments(idRequisition);
			for (Version version : versionNameList)
				version.setFileName(FilenameUtils.getName(version.getDocumentPath()));
			return versionNameList;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_DIGITALIZATION_DOCUMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_DIGITALIZATION_DOCUMENTS_ERROR, databaseException);
		}
	}

	private void saveDigitalizationDocuments(final Integer idRequisition,
			final List<FileUploadInfo> digitalizationDocumentsList) throws BusinessException {
		try {
			this.requisitable.deleteDigitalizationDocuments(idRequisition);
			for (FileUploadInfo digitalizationFile : digitalizationDocumentsList)
				this.requisitable.saveDigitalizationIdDocument(idRequisition,
						this.versionDocument(idRequisition, digitalizationFile));
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_DIGITALIZATION_ID_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_DIGITALIZATION_ID_DOCUMENT_ERROR, databaseException);
		}
	}

	private Integer versionDocument(final Integer idRequisition, final FileUploadInfo fileUploadInfo)
			throws BusinessException {
		Integer idDocument = fileUploadInfo.getIdFile();
		if (fileUploadInfo.isFileNew())
			idDocument = this.versionRequisitionDocument(idRequisition, fileUploadInfo);
		return idDocument;
	}

	private void saveRequisitionApprovalAreasVoBo(final Integer idRequisition,
			final List<VoBoDocumentFile> approvalAreas) throws BusinessException {
		try {
			this.requisitable.deleteRequisitionApprovalAreas(idRequisition);
			for (VoBoDocumentFile bean : approvalAreas) {
				if (bean.getDocumment().isFileNew()) {
					bean.getDocumment().setIdFile(this.versionRequisitionDocument(idRequisition, bean.getDocumment()));
				}
				this.requisitable.saveRequisitionApprovalArea(idRequisition, bean.getIdArea(),
						bean.getDocumment().getIdFile());
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_APPROVAL_AREAS_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_APPROVAL_AREAS_VOBO_ERROR, databaseException);
		}
	}

	private Integer saveSupplierApprovalIdDocument(final Integer idRequisition,
			final FileUploadInfo approvalSupplierDocument) throws BusinessException {
		try {
			approvalSupplierDocument
			.setIdFile(this.versionRequisitionDocument(idRequisition, approvalSupplierDocument));
			this.requisitable.saveSupplierApprovalIdDocument(idRequisition, approvalSupplierDocument.getIdFile());
			return approvalSupplierDocument.getIdFile();
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_SUPPLIER_APROVAL_ID_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_SUPPLIER_APROVAL_ID_DOCUMENT_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveTemplateIdDocument(final FileUploadInfo template) throws BusinessException {
		try {
			List<VersionDTO> dtos = this.documentVersionBusiness.findContractVersionDTO(template.getIdRequisition());
			this.validDocumentVersion(template, dtos);
			final Integer idVersionedDocument = this.versionRequisitionDocument(template.getIdRequisition(), template);
			this.requisitable.saveTemplateIdDocument(template.getIdRequisition(), idVersionedDocument);
			this.saveContractVersion(template.getIdRequisition(), dtos, idVersionedDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_TEMPLATE_ID_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_TEMPLATE_ID_DOCUMENT_ERROR, databaseException);
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void SupplierApprovalDocument(final FileUploadInfo template) throws BusinessException {
		try {
			List<VersionDTO> dtos = this.documentVersionBusiness.findContractVersionDTO(template.getIdRequisition());
			this.validDocumentVersion(template, dtos);
			final Integer idVersionedDocument = this.versionSupplierDocument(template.getIdRequisition(), template);
			LOG.info("##################################################");
			LOG.info("EL List<VersionDTO> dtos ES :::: "+dtos);
			LOG.info("##################################################");
			LOG.info("##################################################");
			LOG.info("EL idVersionedDocument ES :::: "+idVersionedDocument);
			LOG.info("##################################################");
			
			this.requisitable.saveSupplierApprovalDocument(template.getIdRequisition(), idVersionedDocument);
			this.saveContractVersion(template.getIdRequisition(), dtos, idVersionedDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_TEMPLATE_ID_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_TEMPLATE_ID_DOCUMENT_ERROR, databaseException);
		}
	}
//		} catch (Exception databaseException) {
//			LOG.error(MESSAGE_SAVING_TEMPLATE_ID_DOCUMENT_ERROR, databaseException);
//			throw new BusinessException(MESSAGE_SAVING_TEMPLATE_ID_DOCUMENT_ERROR, databaseException);
//		}
//	}
	public void deleteRequisitionTemplate(final Requisition requisition) throws BusinessException {
		try {
			this.requisitable.saveTemplateIdDocument(requisition.getIdRequisition(), null);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETE_TEMPLATE_ID_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETE_TEMPLATE_ID_DOCUMENT_ERROR, databaseException);
		}
	}

	public String returnDraftContractFileName(final Integer idRequisition) throws BusinessException {
		final File draftContract = !existTemplate(idRequisition) ? this.downloadDraftRequisition(idRequisition, true)
				: new File(this.findTemplate(idRequisition));
		return FilenameUtils.getName(draftContract.getPath());
	}

	public String returnContractFileName(final Integer idRequisition) throws BusinessException {
		final File draftContract = !existTemplate(idRequisition) ? this.downloadDraftRequisition(idRequisition, false)
				: new File(this.findTemplate(idRequisition));
		return FilenameUtils.getName(draftContract.getPath());
	}

	public boolean existTemplate(final Integer idRequisition) throws BusinessException {
		final Requisition requisition = this.findById(idRequisition);
		return (requisition != null && requisition.getTemplateIdDocument() != null);
	}

	public String draftContractPath(final String fileName) throws BusinessException, UnsupportedEncodingException {
		return this.getDocumentTypeTemporalPath().concat(SubparagraphUtils.convertToUTF8(fileName));
	}
	
	public String findTemplateByTypeDoc(final Integer idRequisition) throws BusinessException {
		final Requisition requisition = this.findById(idRequisition);
		final DocumentType documentType = this.documentTypeBusiness.findById(requisition.getIdDocumentType());
		String path = "";
		String templatePath = documentType.getTemplatePath();
		File filePath = this.getRequisitionPath(idRequisition);
		
		try {			
			if (!filePath.isDirectory()) {
				throw new BusinessException("No existe el directorio folio: " + idRequisition);
			}
			
			final File fileOut = new File(templatePath);
			String nameFile = fileOut.getName().split(".docx")[0];
			final File fileIs = new File(filePath+"\\" + nameFile + "_VP.docx");
			final FileOutputStream streamIs = new FileOutputStream(fileIs);
			final FileInputStream streamInput = new FileInputStream(fileOut);
			IOUtils.copy(streamInput, streamIs);
			
		    path = filePath+"\\" + nameFile + "_VP.docx";
		
			anexoBusiness.addAnexoToContract(idRequisition, filePath+"\\" + nameFile + "_VP.docx", filePath);
		} catch (BusinessException | IOException e) {
			throw new BusinessException(MESSAGE_ERROR_DOWNLOAD_PREVIEW, e);
		}
		return path;
	}

	public String findTemplate(final Integer idRequisition) throws BusinessException {
		final Requisition requisition = this.findById(idRequisition);
		String templatePath = "";
		if (requisition.getTemplateIdDocument() != null) {
			templatePath = this.documentVersionBusiness.findCurrentVersion(requisition.getTemplateIdDocument())
					.getDocumentPath();
		} else {
			templatePath = this.findTemplateByPersonality(requisition.getIdDocumentType(), requisition.getIdSupplier());
		}
		return ValidatePathSistem.getUrlSistem(templatePath);
	}

	private String findTemplateByPersonality(final Integer idDocumentType, final Integer idSupplier)
			throws BusinessException {
		final DocumentType documentType = this.documentTypeBusiness.findById(idDocumentType);
		final Personality personality = this.findPersonality(idSupplier);
		if (documentType.getIsDiferentTemplateForPersonality() && this.isNaturalPerson(personality)) {
			return documentType.getTemplatePathNaturalPerson();
		}
		return documentType.getTemplatePath();
	}

	private boolean isNaturalPerson(final Personality personality) {
		return personality.getPersonalityEnum() == PersonalityEnum.NATURALPERSON
				|| personality.getPersonalityEnum() == PersonalityEnum.FOREIGNNATURALPERSON;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void deleteDigitalizationByIdDocument(final Integer idDocument) throws BusinessException {
		try {
			this.requisitable.deleteDigitalizationByIdDocument(idDocument);
			this.documentVersionBusiness.deleteByIdDocument(idDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETING_DIGITALIZATION_BY_ID_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETING_DIGITALIZATION_BY_ID_DOCUMENT_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveObligations(final Integer idRequisition, final List<Obligation> obligationsList)
			throws BusinessException {
		try {
			this.requisitable.deleteObligationsByIdRequisition(idRequisition);
			for (Obligation obligation : obligationsList) {
				obligation.setIdRequisition(idRequisition);
				this.requisitable.saveObligation(obligation);
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_OBLIGATIONS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_OBLIGATIONS_ERROR, databaseException);
		}
	}

	public List<Obligation> findObligationsByIdRequisition(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findObligationsByIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_OBLIGATIONS_BY_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_OBLIGATIONS_BY_REQUISITION_ERROR, databaseException);
		}
	}

	public List<TrayRequisition> findPaginatedTrayRequisitions(final TrayFilter trayFilter, final Integer numberPage, final String search)
			throws BusinessException {
		try {
			trayFilter.setIsUserFiltered(this.usersContractBusiness.findIsTrayUserFiltered(trayFilter.getIdUser(),
					trayFilter.getStatus(), trayFilter.getIdFlow()));
			final List<TrayRequisition> listRequisition = this.getTrayRequisitions(trayFilter, numberPage, search);
			return listRequisition;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, databaseException);
		}
	}

	public List<Requisition> findInProgressRequisitions(final TrayFilter trayFilter) throws BusinessException {
		try {
			if (!this.flowBusiness.isManagerialFlow(trayFilter.getIdFlow()))
				return this.requisitable.findInprogressRequisitions(trayFilter);
			return new ArrayList<>();
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_IN_PROGRESS_REQUISITIONS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_IN_PROGRESS_REQUISITIONS_ERROR, databaseException);
		}
	}

	private List<TrayRequisition> getTrayRequisitions(final TrayFilter trayFilter, final Integer numberPage, final String search)
			throws DatabaseException, BusinessException {

		List<TrayRequisition> listRequisition = new ArrayList<>();
		final Integer itemsPerPage = Integer.parseInt(this.configuration.findByName("ITEMS_NUMBER_FOR_PAGE_TRAY"));
		if (!this.flowBusiness.isManagerialFlow(trayFilter.getIdFlow())) {
			listRequisition = this.requisitable.findPaginatedTrayRequisitions(trayFilter, numberPage, itemsPerPage, search);
		} else {
			listRequisition = this.requisitionOwnersable.findRequisitionsForTray(trayFilter, numberPage, itemsPerPage);
		}
		return listRequisition;
	}

	public Integer totalPagesForTray(final TrayFilter trayFilter, String search) throws BusinessException {
		Integer result = 0;
		try {
			trayFilter.setIsUserFiltered(this.usersContractBusiness.findIsTrayUserFiltered(trayFilter.getIdUser(),
					trayFilter.getStatus(), trayFilter.getIdFlow()));
			if (!this.flowBusiness.isManagerialFlow(trayFilter.getIdFlow())) {
				//result = (int) Math.ceil(this.requisitable.countTotalRowsForTray(trayFilter)
					//	/ Double.valueOf(this.configuration.findByName("ITEMS_NUMBER_FOR_PAGE_TRAY")));
				result = (int) Math.ceil(this.requisitable.countTotalRowsForTray(trayFilter, search));
			} else {
				result = (int) Math.ceil(this.requisitionOwnersable.countTotalRowsForTray(trayFilter));
						/// Double.valueOf(this.configuration.findByName("ITEMS_NUMBER_FOR_PAGE_TRAY")));
			}
			return result;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_COUNT_TOTAL_PAGES_FOR_TRAY, databaseException);
			throw new BusinessException(MESSAGE_COUNT_TOTAL_PAGES_FOR_TRAY, databaseException);
		}
	}

	private void saveUserVobo(final Integer idRequisition, final Integer idUser) throws BusinessException {
		try {
			this.requisitable.saveUserVobo(idRequisition, idUser);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_USER_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_USER_VOBO_ERROR, databaseException);
		}
	}

	private Boolean findIsAllUsersVobo(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findIsAllUsersVobo(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_IS_ALL_USERS_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_IS_ALL_USERS_VOBO_ERROR, databaseException);
		}
	}

	public List<Requisition> findRequisitionByFlow(final Integer idFlow) throws BusinessException {
		try {
			
			return this.requisitable.findRequisitionByFlow(idFlow);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_REQUISITION_BY_FLOW_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_REQUISITION_BY_FLOW_ERROR, databaseException);
		}
	}

	private void saveRequisitionStatusTurn(final Integer idRequisition, final FlowPurchasingEnum status)
			throws BusinessException {
		try {
			this.requisitable.saveRequisitionStatusTurn(idRequisition, status);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_TURN_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_TURN_ERROR, databaseException);
		}
	}

	private void saveRequisitionStatusTurnAttentionDaysAndStage(final RequisitionStatusTurn requisitionStatusTurn)
			throws BusinessException {
		try {
			this.requisitable.saveRequisitionStatusTurnAttentionDaysAndStage(requisitionStatusTurn);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_STATUS_TURN_ATTENTION_DAYS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_STATUS_TURN_ATTENTION_DAYS_ERROR, databaseException);
		}
	}

	public List<RequisitionStatusTurn> findRequisitionStatusTurnsByIdRequisition(final Integer idRequisition)
			throws BusinessException {
		try {
			LOG.info("RequisitionBusiness -> findRequisitionStatusTurnsByIdRequisition");
			return this.requisitable.findRequisitionStatusTurnsByIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_REQUISITION_STATUS_TURNS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_REQUISITION_STATUS_TURNS_ERROR, databaseException);
		}
	}

	public void saveScalingMatrix(final Integer idRequisition, final List<Scaling> scalingList)
			throws BusinessException {
		try {
			if (scalingList.size() > 0)
				this.deleteScalingMatrixByIdRequisition(idRequisition, scalingList.get(0).getScalingType());
			for (final Scaling scaling : scalingList) {
				scaling.setIdRequisition(idRequisition);
				this.requisitable.saveScalingMatrix(scaling);
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_SCALING_MATRIX_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVE_SCALING_MATRIX_ERROR, databaseException);
		}
	}

	public void deleteScalingMatrixByIdRequisition(final Integer idRequisition, final ScalingTypeEnum scalingType)
			throws BusinessException {
		try {
			this.requisitable.deleteScalingMatrixByIdRequisition(idRequisition, scalingType);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETE_SCALING_MATRIX_BY_ID_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETE_SCALING_MATRIX_BY_ID_REQUISITION_ERROR, databaseException);
		}
	}

	public List<Scaling> findScalingMatrixVersionByIdRequisitionVersion(final Integer idRequisitionVersion,
			final ScalingTypeEnum scalingType) throws BusinessException {
		try {
			return this.requisitable.findScalingMatrixVersionByIdRequisitionVersion(idRequisitionVersion, scalingType);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_SCALING_MATRIX_VERSION_BY_ID_REQUISITION_VERSION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_SCALING_MATRIX_VERSION_BY_ID_REQUISITION_VERSION_ERROR,
					databaseException);
		}
	}

	public List<Scaling> findScalingMatrixByIdRequisition(final Integer idRequisition,
			final ScalingTypeEnum scalingType) throws BusinessException {
		try {
			return this.requisitable.findScalingMatrixByIdRequisition(idRequisition, scalingType);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_SCALING_MATRIX_BY_ID_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_SCALING_MATRIX_BY_ID_REQUISITION_ERROR, databaseException);
		}
	}

	public File createExcelReport(final String reportName, final List<List<String>> matrix) throws BusinessException {
		try {
			final String temporalPath = this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString())
					+ Constants.PATH_TMP;
			final String[][] dataMatrix = new String[matrix.size()][matrix.get(0).size()];
			Integer row = 0;
			for (final List<String> stringList : matrix) {
				Integer column = 0;
				for (final String text : stringList) {
					dataMatrix[row][column] = text;
					column++;
				}
				row++;
			}
			return ExcelUtils.createXlsxFromArray(temporalPath, reportName, dataMatrix);
		} catch (IOException exception) {
			LOG.error(MESSAGE_CREATING_EXCEL_ERROR, exception);
			throw new BusinessException(MESSAGE_CREATING_EXCEL_ERROR, exception);
		}
	}

	public List<Requisition> paginatedFindRequisitionsManagement(final Requisition requisition,
			final Integer pageNumber) throws BusinessException {
		try {
			LOG.info(" RequisitionBusiness -> paginatedFindRequisitionsManagement Aqui");
			final Integer itemsNumber = this.configuration.getPaginationItemsNumberParameter();
			return this.requisitable.findPaginatedRequisitionsManagement(requisition, pageNumber, itemsNumber);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_REQUISITION_LIST_BY_PARAMETERS_ERROR, databaseException);
		}
	}

	public Integer returnTotalPagesShowRequisitionsManagement(final Requisition requisition)
			throws NumberFormatException, BusinessException {
		try {
			LOG.info(" RequisitionBusiness -> returnTotalPagesShowRequisitionsManagement");
			return this.configuration.totalPages(this.requisitable.countTotalRowsRequisitionsManagement(requisition));
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
		}
	}
	
	public Integer returnTotalDataShowRequisitionsManagement(final Requisition requisition)
            throws NumberFormatException, BusinessException {
        try {
            LOG.info(" RequisitionBusiness -> returnTotalPagesShowRequisitionsManagement");
            Integer valor = this.requisitable.countTotalRowsRequisitionsManagement(requisition).intValue();
            return valor;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
        }
    }

	public Integer returnTotalPagesShowOfContracts(final Requisition requisition)
			throws NumberFormatException, BusinessException {
		try {
			return this.configuration.totalPages(this.requisitable.countTotalRowsOfContracts(requisition));
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
		}
	}

	public void cleanUsersVobo(final Integer idRequisition) throws BusinessException {
		try {
			this.requisitable.cleanUsersVobo(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_CLEAN_USERS_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_CLEAN_USERS_VOBO_ERROR, databaseException);
		}
	}

	public ComparationWord wordComparate(final String fileName1, final String fileName2, final Integer idFlow)
			throws BusinessException, WordComparationException {
		final ComparationWord word = new ComparationWord();
		final File baseFile = new File(this.fileNameWithExtension(this.cleanFileNames(fileName1), idFlow));
		final File comparedFile = new File(this.fileNameWithExtension(this.cleanFileNames(fileName2), idFlow));
		try {
			final File wordComparationFile = WordComparationUtils.compare(baseFile, comparedFile,
					this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_TMP
					+ File.separator);
			if (wordComparationFile != null) {
				word.setIsWordResponse(true);
				word.setFileName(FilenameUtils.getName(wordComparationFile.getPath()));
			}
		} catch (Exception e) {
			word.setIsWordResponse(false);
		}
		return word;
	}

	private String fileNameWithExtension(final String fileName, final Integer idUsuario) throws BusinessException {
		final String fileNameWithoutExtension = FilenameUtils.removeExtension(fileName);
		final String extension = FilenameUtils.getExtension(fileName);
		return this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_TMP
				+ File.separator + idUsuario + File.separator + fileNameWithoutExtension + "." + extension;
	}

	private String cleanFileNames(final String fileName) {
		return fileName.replaceAll(INVALID_CHARACTERS, UNDERSCORE);
	}

	private String getConstitutiva(final String constitutiva) {
		if (constitutiva != null && !constitutiva.isEmpty() && !constitutiva.endsWith("."))
			return constitutiva.concat(".");
		return constitutiva;
	}

	public void saveCommentWithDocuments(final ContractCancellationComment contractCancellationComment)
			throws BusinessException {
		try {
			final Integer idContractCancellationComment = this.requisitable.saveComment(contractCancellationComment);
			this.saveContractCancellationDocument(contractCancellationComment.getIdRequisition(),
					idContractCancellationComment, contractCancellationComment.getFileInfoList());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_CONTRACT_CANCELLATION_COMMENT, databaseException);
			throw new BusinessException(MESSAGE_SAVE_CONTRACT_CANCELLATION_COMMENT, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveContractCancellationComment(final ContractCancellationComment contractCancellationComment)
			throws BusinessException {
		this.changeRequisitionStatus(contractCancellationComment.getIdRequisition(),
				contractCancellationComment.getRequisitionCancelStatus());
		this.saveCommentWithDocuments(contractCancellationComment);
	}

	private void saveContractCancellationDocument(final Integer idRequisition,
			final Integer idContractCancellationComment, List<FileUploadInfo> uploadList) throws BusinessException {
		try {
			
			for (FileUploadInfo fileUploadInfo : uploadList)
				this.requisitable.saveContractCancellationCommentDocument(idContractCancellationComment,
						this.versionRequisitionDocument(idRequisition, fileUploadInfo),
						fileUploadInfo.getDocumentName());
			
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_CONTRACT_CANCELLATION_DOCUMENT, databaseException);
			throw new BusinessException(MESSAGE_SAVE_CONTRACT_CANCELLATION_DOCUMENT, databaseException);
		}
	}

	public ContractCancellationComment findContractCancellationComment(final Integer idRequisition)
			throws BusinessException {
		try {
			final ContractCancellationComment contractCancellationComment = this.requisitable
					.findContractCancellationComment(idRequisition);
			contractCancellationComment
			.setDocumentList(this.requisitable.findContractCancelationCommentDocument(idRequisition));
			return contractCancellationComment;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_CONTRACT_CANCELLATION_COMMENT, databaseException);
			throw new BusinessException(MESSAGE_FIND_CONTRACT_CANCELLATION_COMMENT, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void deleteAuthorizationDocument(final Integer idRequisition, final Integer idAuthorizationDocument)
			throws BusinessException {
		try {
			this.requisitable.deleteAuthorizationDocument(idRequisition);
			// this.documentVersionBusiness.deleteByIdDocument(idAuthorizationDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETE_AUTHORIZATION_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETE_AUTHORIZATION_DOCUMENT_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void deleteImssCeduleFile(final Integer idRequisition, final Integer idImssCedule) throws BusinessException {
		try {
			this.requisitable.deleteImssCeduleFile(idRequisition);
			this.documentVersionBusiness.deleteByIdDocument(idImssCedule);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETE_AUTHORIZATION_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETE_AUTHORIZATION_DOCUMENT_ERROR, databaseException);
		}
	}

	public void deletePendingRequisitions(ConsultaList<Integer> clist) throws BusinessException {
		try {
			this.requisitable.deletePendingRequisitions(clist.getList());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETING_PENDING_REQUISITIONS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETING_PENDING_REQUISITIONS_ERROR, databaseException);
		}
	}

	public List<UserInProgressRequisition> findApplicantInProgressRequisitions(
			final UserInProgressRequisitionFilter filter, final Integer pageNumber) throws BusinessException {
		try {
			return this.requisitable.findApplicantInProgressRequisitions(filter, pageNumber,
					this.configuration.getPaginationItemsNumberParameter());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_USER_IN_PROGRESS_REQUISITIONS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_USER_IN_PROGRESS_REQUISITIONS_ERROR, databaseException);
		}
	}

	public Integer countTotalPagesApplicantInProgressRequisitions(final UserInProgressRequisitionFilter filter)
			throws BusinessException {
		try {
			return this.configuration
					.totalPages(this.requisitable.countTotalRowsApplicantInProgressRequisitions(filter));
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
		}
	}

	public List<UserInProgressRequisition> findLawyerInProgressRequisitions(
			final UserInProgressRequisitionFilter filter, final Integer pageNumber) throws BusinessException {
		try {
			return this.requisitable.findLawyerInProgressRequisitions(filter, pageNumber,
					this.configuration.getPaginationItemsNumberParameter());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_USER_IN_PROGRESS_REQUISITIONS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_USER_IN_PROGRESS_REQUISITIONS_ERROR, databaseException);
		}
	}

	public Integer countTotalPagesLawyerInProgressRequisitions(final UserInProgressRequisitionFilter filter)
			throws BusinessException {
		try {
			return this.configuration.totalPages(this.requisitable.countTotalRowsLawyerInProgressRequisitions(filter));
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
		}
	}

	public List<TrayRequisition> obtenerSolicitudesPendientes(final TrayFilter trayFilter) throws BusinessException {
		try {
			if (!this.flowBusiness.isManagerialFlow(trayFilter.getIdFlow())) {
				trayFilter.setIsUserFiltered(this.usersContractBusiness.findIsTrayUserFiltered(trayFilter.getIdUser(),
						FlowPurchasingEnum.REQUISITION, trayFilter.getIdFlow()));
				return this.requisitable.obtenerSolicitudesPendientes(trayFilter);
			}
			return new ArrayList<>();
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIVING_IN_PROGRESS_REQUISITIONS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIVING_IN_PROGRESS_REQUISITIONS_ERROR, databaseException);
		}
	}	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void agregarAnexos(final Requisition requisitionParam) throws BusinessException {
		try {
			LOG.info("FUNCIONALIDAD ANEXAR ARCHIVOS.");
			File filePath = this.getRequisitionPath(requisitionParam.getIdRequisition());
			if (!filePath.isDirectory()) {
				throw new BusinessException("No existe el directorio folio: " + requisitionParam.getIdRequisition());
			}
			List<VersionDTO> dtos = this.documentVersionBusiness.findContractVersionDTO(requisitionParam.getIdRequisition());
			final Version currentVersion = this.documentVersionBusiness.findCurrentVersion(dtos.get(0).getIdDocument());
		    anexoBusiness.addAnexoToContract(requisitionParam.getIdRequisition(), currentVersion.getDocumentPath(), filePath);
		} catch (BusinessException | IOException e) {
			e.printStackTrace();
			throw new BusinessException(MESSAGE_UPDATE_VOBO, e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void guardarVoBoJuridico(final Requisition requisitionParam, final Integer idUser) throws BusinessException {
//	public void guardarVoBoJuridico(final Requisition requisitionParam, final Integer idUser,final FileUploadInfo approvalSupplierDocument) throws BusinessException {
		LOG.info("Se cambiará el estatus de la solicitud");
		try {

			// Se genera la clave
			File filePath = this.getRequisitionPath(requisitionParam.getIdRequisition());
			if (!filePath.isDirectory()) {
				throw new BusinessException("No existe el directorio folio: " + requisitionParam.getIdRequisition());
			}
			
			String status = findNextStatus(requisitionParam.getFlowScreenActionParams());
			requisitionParam.setStatus(FlowPurchasingEnum.valueOf(status));
			// aquí se genera el qr
			String finalPath = this.writeQR(requisitionParam, filePath);
			LOG.info("DATOS PARA GUARDAR VERSIÓN FINAL QR \n IdRequisition :: "+requisitionParam.getIdRequisition()+"\n finalPath :: "+finalPath);
		    // aquí se guarda el nuevo documento con el QR
			saveFinalVersionQR(requisitionParam.getIdRequisition(), finalPath);
			
			this.requisitable.updateVoBoContractRiskByIdRequisition(requisitionParam.getIdRequisition(),
					requisitionParam.isVoBocontractRisk());
			this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
					requisitionParam.getFlowScreenActionParams());
//			approvalSupplierDocument.setIdFile(this.versionRequisitionDocument(requisitionParam.getIdRequisition(), approvalSupplierDocument));
//			 this.requisitable.saveSupplierApprovalIdDocument(requisitionParam.getIdRequisition(), approvalSupplierDocument.getIdFile());
//			 this.saveTemplateIdDocument(approvalSupplierDocument);
		} catch (DatabaseException e) {
			LOG.error(MESSAGE_UPDATE_VOBO, e);
			throw new BusinessException(MESSAGE_UPDATE_VOBO, e);
		}
	}
	
	/**
	 * Metodo que genera el contrato con QR.
	 * 
	 * @param fileInput contrato anterior
	 * @param path      la raiz
	 * @param req       el objeto padre
	 * @return requisition id
	 * @throws BusinessException la exepcion contralada
	 */
	public String writeQR(Requisition req, File filePath) throws BusinessException {
		 String nameT = req.getSupplierApprovalDocument().getDocumentName();
		Integer idRequisition = req.getIdRequisition();
		Integer templateIdDocument = this.findTemplateIdSupplierIdDocumentFinal(idRequisition);
		if(templateIdDocument == null && nameT.endsWith(".pdf")) {
			 templateIdDocument = this.findTemplateIdDocumentByIdRequisition(idRequisition);
		}
		String templateName = this.findTemplateNameDocumentByIdRequisition(idRequisition);
		
		LOG.info("writeQR :: Ruta ("+filePath.getPath()+") - idRequisition ("+idRequisition+")"+ "- Template ("+templateIdDocument+")" + "- NombreTemplate ("+templateName+")");
		
		File fileName = this.returnFinalContractFile(idRequisition, templateIdDocument, filePath.getPath());
		
		String complete = new String(fileName.getName());
		req.setDocumentName(complete);
		req.setIdApplicant(session.getIdUsuarioSession());		
		
		String path = fileName.getPath().replace(ValidatePathSistem.getSeparatorSistem()+complete,"");
		
		String finalQRPath = addQRFooter(fileName, path, req, templateName);
		return finalQRPath;
	}
	
	/**
	 * Metodo que realiza la generacion de QR.
	 * <p>
	 * Incluye el QR generado en el contrato.
	 * 
	 * @param requisition modelo padre
	 * @param path        la raiz
	 * @param qr          bean para generacion de codigo
	 * @throws BusinessException la exepcion controlada
	 */
	public String addQRFooter(File requisition, String path, Requisition req, String templateName) throws BusinessException {
		String nameQR = null;
		RequisitionComplete reqCom = this.obtenerDetalleSolicitud(req.getIdRequisition());
		QuickResponse qr = mapearQR(reqCom);
		qr.setName(req.getDocumentName());
		qr.setRisk(req.isVoBocontractRisk());
		qr.setIdApplicant(req.getIdApplicant());
		qr.setIdRequisition(req.getIdRequisition());
		List<RequisitionStatusTurn> list = this.findRequisitionStatusTurnsByIdRequisition(req.getIdRequisition());
		list.forEach(r -> {
			if (r.getStatus() == req.getStatus()) {
				qr.setVoBodate(r.getTurnDate());
			}
		});
		AddFooterQr afqr = new AddFooterQr();
		int isj = qr.getName().lastIndexOf(SEPARATOR_POINT);
		String imageName = new String(qr.getName().substring(CERO, isj));
		String imageNameComplete = nameComplete(path, imageName, IMAGE_TYPE_FILE, EXTENSION_FILE);
		
		try {
			String salt = SecurityEncrypt.generateSalt();
			qr.setSalt(salt);

			Integer check = this.qrDao.saveQr(qr, quickResponseData(qr));
			if ((new Integer(CERO)).compareTo(check) == CERO) {
				new BusinessException(QR_MASC);
			} else {
				Boolean templateOthers = (null== templateName? false: templateName.equalsIgnoreCase(DocumentTypeEnum.OTHERS.name()));
				LOG.info("\nASIGANAR FOLIO A TEMPLATE \n templateName :: "+templateName+"\n AsignaFolio? :: "+templateOthers);
				nameQR = nameComplete(path, imageName, QUICK_RESPONSE, EXTENSION_WORD_FILE);
				afqr.generateSaveQr(salt, qr.getRisk(), imageNameComplete, QR_SIZE, QR_SIZE);
				// verificar si es .pdf
				if (requisition.toString().endsWith(".pdf")) {
					System.out.println("El documento termina con '.pdf'.");
					afqr.addQRToExistingPDF(requisition.toString(), path.toString(), imageNameComplete, qr.getFolio(),
							true);
					nameQR = nameComplete(path, imageName, QUICK_RESPONSE, EXTENSION_PDF_FILE);
				} else {
					System.out.println("El documento no termina con '.pdf'.");
					afqr.addQRFooter(requisition, nameImage(path, imageName), imageNameComplete, nameQR, qr.getFolio(),
							templateOthers);
				}
				return nameQR;

			}
		} catch (Exception e) {
			new BusinessException(QR_MASC);
		}
		return nameQR;
	}
	
	/**
	 * Metodo para generar ruta de imagen.
	 * 
	 * @param path la ruta base
	 * @param name el nombre a asignar
	 * @return cadena completa
	 */
	private String nameImage(String path, String name) {
		StringBuilder sb0 = new StringBuilder();
		sb0.append(name);
		sb0.append(IMAGE_TYPE_FILE);
		sb0.append(EXTENSION_FILE);
		return sb0.toString();
	}
	
	/**
	 * Metodo generacion da cadena para codigo.
	 * 
	 * @param qr modelo para generar el QR
	 * @return la cadena final
	 */
	private static String quickResponseData(QuickResponse qr) {
		StringBuilder st = new StringBuilder();
		st.append(completWithSpaces(qr.getFolio(), FOLIO_LENGHT));
		st.append(completWithSpaces(qr.getRFC(), RFC_LENGHT));
		st.append(completWithSpaces(qr.getRequistionDate(), DATE_LENGHT));
		st.append(completWithSpaces(qr.getIdApplicant().toString(), FOLIO_LENGHT));
		st.append(completWithSpaces(qr.getIdCompany().toString(), ID_LENGHT));
		st.append(completWithSpaces(qr.getIdArea().toString(), ID_LENGHT));
		st.append(completWithSpaces(qr.getIdUnit().toString(), ID_LENGHT));
		st.append(completWithSpaces(qr.getVoBodate(), DATE_LENGHT));
		return st.toString();
	}

	/**
	 * Metodo auxiliar enmascarar datos incompletos.
	 * 
	 * @param in     cadena de entrada
	 * @param length longitud esperada
	 * @return cadena con el campo enmascarado
	 */
	private static String completWithSpaces(final String in, int length) {
		StringBuilder out = new StringBuilder();
		if (null != in) {
			int completa = length - in.length();
			while (completa > 0) {
				out.append(QR_MASC);
				completa--;
			}
			out.append(in);
		}
		return out.toString();
	}
	
	/**
	 * Metodo genera nombre completo.
	 * 
	 * @param path      ruta base
	 * @param name      nombre a asignar
	 * @param type      tipo de archivo
	 * @param extension estension del archivo
	 * @return
	 */
	private String nameComplete(String path, String name, String type, String extension) {
		StringBuilder sb0 = new StringBuilder();
		sb0.append(path);
		sb0.append(ValidatePathSistem.getSeparatorSistem());
		sb0.append(name);
		sb0.append(type);
		sb0.append(extension);
		return sb0.toString();
	}

	
	/**
	 * Metodo para mapear la cadena.
	 * 
	 * @param req modelo padre
	 * @return modelo de codigo
	 */
	public QuickResponse mapearQR(RequisitionComplete req) {
		QuickResponse qr = new QuickResponse();
		qr.setFolio(req.getRequisitionsPartOneAndTwo().getIdRequisition().toString());
		qr.setRFC(req.getRequisitionsPartThree().getSupplier().getRfc());
		qr.setRequistionDate(req.getRequisitionsPartOneAndTwo().getApplicationDate());
		qr.setIdCompany(req.getRequisitionsPartOneAndTwo().getIdCompany());
		qr.setIdUnit(req.getRequisitionsPartOneAndTwo().getIdUnit());
		qr.setIdArea(req.getRequisitionsPartOneAndTwo().getIdArea());
		// qr.setVoBodate("2019-07-17 11:37:45");
		return qr;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void rechazarVoBoJuridico(final Requisition requisitionParam) throws BusinessException {
		this.requisitionVersionBusiness.saveRequisitionVersion(requisitionParam.getIdRequisition());
		this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void actualizaEvaluadorVoBoJuridico(final Integer idRequisition) throws BusinessException {
		LOG.info("Se actualiza el evaluador en la solicitud");
		try {
			this.requisitable.actualizarEvaluadorVoBoJuridico(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_ERROR, databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public Integer getIdLawyerByIdRequisition(final Integer idRequisition) throws BusinessException {
		LOG.info("obtiene el id del abogado");
		try {
			return this.requisitable.getIdLawyerByIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener el abogado", databaseException);
			throw new BusinessException("Error al obtener el abogado", databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public Integer getIdApplicantByIdRequisition(final Integer idRequisition) throws BusinessException {
		LOG.info("Se obtiene el id del solicitante");
		try {
			return this.requisitable.getIdApplicantByIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener el abogado", databaseException);
			throw new BusinessException("Error al obtener el abogado", databaseException);
		}
	}

	public List<VersionDTO> findDocumentsAttachmentDTO(final Integer idRequisition, final Integer idUser)
			throws BusinessException {
		try {
			List<Integer> idDocumentList = new ArrayList<Integer>();
			idDocumentList = this.requisitable.findRequisitionAttachmentByIdRequisition(idRequisition);
			final List<VersionDTO> versionList = new ArrayList<VersionDTO>();
			for (Integer idDocument : idDocumentList) {
				VersionDTO bean = new VersionDTO();
				bean = this.documentVersionBusiness.findCurrentVersionDTO(idDocument);
				bean.setFileName(FilenameUtils.getName(bean.getDocumentPath()));
				versionList.add(bean);
			}
			return versionList;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGNED_CONTRACT_DATA_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_LEGAL_REPRESENTATIVES_SIGNED_CONTRACT_DATA_ERROR,
					databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public List<VersionDTO> saveNewDocumentsAttachment(List<FileUploadInfo> filesUploadInfo, final Integer idUser)
			throws BusinessException {
		try {
			Requisition requisition = new Requisition();
			for (FileUploadInfo fileUploadInfo : filesUploadInfo) {
				Integer idDocument = fileUploadInfo.getIdFile();
				requisition.setIdRequisition(fileUploadInfo.getIdRequisition());
				idDocument = this.versionDocumentWhenNew(requisition, fileUploadInfo, idDocument);
				final RequisitionAttachment requisitionAttachment = new RequisitionAttachment(
						requisition.getIdRequisition(), idDocument);
				this.requisitable.saveRequisitionAttatchment(requisitionAttachment);
			}
			return this.findDocumentsAttachmentDTO(requisition.getIdRequisition(), idUser);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DOCUMENTS_ATTATCHMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DOCUMENTS_ATTATCHMENTS_ERROR, databaseException);
		}
	}

	private List<TrayRequisition> getAllTrayRequisitions(final TrayFilter trayFilter)
			throws DatabaseException, BusinessException {		
		List<TrayRequisition> listRequisition = new ArrayList<>();
		if (!this.flowBusiness.isManagerialFlow(trayFilter.getIdFlow())) {
			listRequisition = this.requisitable.findPaginatedTrayRequisitions(trayFilter);
		} else {
			listRequisition = this.requisitionOwnersable.findAllRequisitionsForTray(trayFilter);
		}
		return listRequisition;
	}
	private List<TrayRequisition> getAllTrayRequisitionsPorFechas(final TrayFilter trayFilter)
			throws DatabaseException, BusinessException {		
		List<TrayRequisition> listRequisition = new ArrayList<>();
		if (!this.flowBusiness.isManagerialFlow(trayFilter.getIdFlow())) {
			listRequisition = this.requisitable.findPaginatedTrayRequisitionsPorFechas(trayFilter);
		} else {
			listRequisition = this.requisitionOwnersable.findAllRequisitionsForTray(trayFilter);
		}
		return listRequisition;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveContractCancellationCommentAngular(final ContractCancellationComment contractCancellationComment)
			throws BusinessException {
		try {
			final RequisitionStatusTurn requisitionStatusTurn = new RequisitionStatusTurn();
			requisitionStatusTurn.setIdRequisition(contractCancellationComment.getIdRequisition());
			requisitionStatusTurn.setStatus(FlowPurchasingEnum.CANCELED_CONTRACT);
			this.saveRequisitionStatusTurn(contractCancellationComment.getIdRequisition(),
					requisitionStatusTurn.getStatus());

			this.changeRequisitionStatus(contractCancellationComment.getIdRequisition(),
					contractCancellationComment.getRequisitionCancelStatus());
			this.requisitable.saveComment(contractCancellationComment);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_CONTRACT_CANCELLATION_COMMENT, databaseException);
			throw new BusinessException(MESSAGE_SAVE_CONTRACT_CANCELLATION_COMMENT, databaseException);
		}
	}

	public List<TrayRequisition> findAllTrayRequisitionsAngular(final TrayFilter trayFilter) throws BusinessException {
		try {

			trayFilter.setIsUserFiltered(this.usersContractBusiness.findIsTrayUserFiltered(trayFilter.getIdUser(),
					trayFilter.getStatus(), trayFilter.getIdFlow()));

			List<TrayRequisition> requisitions = this.getAllTrayRequisitions(trayFilter);

			if (requisitions != null && !requisitions.isEmpty()) {
				DateTime today = new DateTime();
				SimpleDateFormat before = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat after = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				List<Date> holidays = this.findAllHolidays();

				for (TrayRequisition item : requisitions) {
					Date turnDate = before.parse(item.getTurnDate());
					item.setTurnDate(after.format(before.parse(item.getTurnDate())));
					item.setTurnString(item.getTurn() != null ? TranslateNumber.getString(item.getTurn() + 1) : "");
					int days = getAttentiondaysByIdRequisition(item.getIdRequisition(), item.getStatus(),
							(item.getTurn() != null ? item.getTurn() : 0),
							(turnDate != null ? new DateTime(turnDate) : new DateTime()), today,
							(item.getAttentiondays() != null ? item.getAttentiondays() : 0), holidays);
					item.setAttentiondays(days);
				}
				;
				return requisitions;
			} else {
				return new ArrayList<TrayRequisition>();
			}
		} catch (Exception e) {
			LOG.error(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, e);
			throw new BusinessException(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, e);
		}
	}

	
	public List<TrayRequisition> findAllTrayRequisitionsAngularPorFechas(final TrayFilter trayFilter) throws BusinessException {
		try {

			trayFilter.setIsUserFiltered(this.usersContractBusiness.findIsTrayUserFiltered(trayFilter.getIdUser(),
					trayFilter.getStatus(), trayFilter.getIdFlow()));

			List<TrayRequisition> requisitions = this.getAllTrayRequisitionsPorFechas(trayFilter);

			if (requisitions != null && !requisitions.isEmpty()) {
				DateTime today = new DateTime();
				SimpleDateFormat before = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat after = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				List<Date> holidays = this.findAllHolidays();

				for (TrayRequisition item : requisitions) {
					Date turnDate = before.parse(item.getTurnDate());
					item.setTurnDate(after.format(before.parse(item.getTurnDate())));
					item.setTurnString(item.getTurn() != null ? TranslateNumber.getString(item.getTurn() + 1) : "");
					int days = getAttentiondaysByIdRequisition(item.getIdRequisition(), item.getStatus(),
							(item.getTurn() != null ? item.getTurn() : 0),
							(turnDate != null ? new DateTime(turnDate) : new DateTime()), today,
							(item.getAttentiondays() != null ? item.getAttentiondays() : 0), holidays);
					item.setAttentiondays(days);
				}
				;
				return requisitions;
			} else {
				return new ArrayList<TrayRequisition>();
			}
		} catch (Exception e) {
			LOG.error(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, e);
			throw new BusinessException(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, e);
		}
	}
	public List<TrayRequisition> findAllContractsInRevision() throws BusinessException {
		try {
			List<TrayRequisition> requisitions = this.requisitable.findContractsInRevision();
			return requisitions;
		} catch (Exception e) {
			LOG.error(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, e);
			throw new BusinessException(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, e);
		}
	}
	
	public List<TrayRequisition> findAllContractsInSignatures() throws BusinessException {
		try {
			List<TrayRequisition> requisitions = this.requisitable.findContractsInSignatures();
			return requisitions;
		} catch (Exception e) {
			LOG.error(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, e);
			throw new BusinessException(MESSAGE_RETRIVING_REQUISITIONS_FOR_TRAY_ERROR, e);
		}
	}

	public int getAttentiondaysByIdRequisition(final Integer idRequisition, final FlowPurchasingEnum status,
			final Integer turn, DateTime turnDay, DateTime today, int naturales, List<Date> holidays) {
		try {
			DateTime fin = turnDay.plusDays(naturales);
			Integer validityDays = this.alertsBusiness.findValidityDaysByRequisitionFlowStatusTurn(idRequisition,
					status, turn);

			if (validityDays == null) {
				return 0;
			}

			if (validInitDay(today, turnDay)) {
				return validityDays;
			}

			if (validEndDay(today, fin)) {
				return 0;
			}

			int diasDisponibles = validityDays;
			DateTime date = turnDay;

			for (int i = 0; i < naturales; i++) {

				if (isWeekendDay(date.getDayOfWeek())) {
					continue;
				}

				if (validHolidays(holidays, date)) {
					continue;
				}

				if (date.equals(today) || date.isAfter(today)) {
					diasDisponibles--;
					break;
				}

				date = date.plusDays(1);
				if (i > 0) {
					diasDisponibles--;
				}
			}

			return diasDisponibles;
		} catch (Exception e) {
			return 0;
		}
	}

	private boolean validHolidays(List<Date> holidays, DateTime date) {
		DateTime value = new DateTime(date.getYear() + "-" + date.getMonthOfYear() + "-" + date.getDayOfMonth());
		if (holidays.contains(value)) {
			return true;
		}
		return false;
	}

	private boolean validInitDay(DateTime hoy, DateTime inicio) {
		DateTime init = new DateTime(inicio.getYear() + "-" + inicio.getMonthOfYear() + "-" + inicio.getDayOfMonth());
		DateTime end = new DateTime(hoy.getYear() + "-" + hoy.getMonthOfYear() + "-" + hoy.getDayOfMonth());
		if (init.equals(end)) {
			return true;
		}
		return false;
	}

	private boolean validEndDay(DateTime hoy, DateTime fin) {
		if (hoy.equals(fin) || hoy.isAfter(fin)) {
			return true;
		}
		return false;
	}

	private boolean isWeekendDay(int dayOfTheweek) {
		return dayOfTheweek == DateTimeConstants.SATURDAY || dayOfTheweek == DateTimeConstants.SUNDAY;
	}

	public List<Date> findAllHolidays() throws BusinessException {
		try {
			return holidayBusiness.findAllDates();
		} catch (Exception exception) {
			LOG.error("Error al obtener los dias festivos", exception);
			throw new BusinessException("Error al obtener los dias festivos", exception);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void savePrintContract(final Requisition requisitionParam, final Integer idUser) throws BusinessException {
		try {
			LOG.info("Se cambiará el estatus de la solicitud");
			this.saveChangeRequisitionStatus(requisitionParam.getIdRequisition(),
				requisitionParam.getFlowScreenActionParams());
		
			// saveDigitalSignatureData(requisitionParam); // TODO Integrate flow
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	private void saveDigitalSignatureData(Requisition requisitionParam) throws Exception {
		try {
			
			Integer idRequisition = requisitionParam.getIdRequisition();

			if (requisitionParam.getDocumentDS() == null 
					|| requisitionParam.getDocumentDS().getDigitalSignatureProvider() == null) {
				LOG.info("No se eligió un proveedor de firma digital para la solicitud" + idRequisition);
				return;
			}
			
			LOG.info("Creando registro de firma digital para la solicitud " + idRequisition);
			
//			boolean existDS = dsRequisitable.existsByIdRequisition(idRequisition); // TODO

			if (false /*existDS*/) {
				throw new BusinessException("Ya existe una firma digital para este registro de solicitud");
			}
			
			DigitalSignatureProviderEnum digitalSignatureProvider = 
					requisitionParam.getDocumentDS().getDigitalSignatureProvider();
			
			DigitalSignatureStatusEnum digitalSignatureStatus = 
					requisitionParam.getDocumentDS().getStatusDigitalSignature();
			
			DocumentDS newDocumentDS = new DocumentDS();
			newDocumentDS.setIdRequisition(idRequisition);
			newDocumentDS.setDigitalSignatureProvider(digitalSignatureProvider);
			newDocumentDS.setStatusDigitalSignature(digitalSignatureStatus);
			newDocumentDS.setCreatedAt(new Date());
			newDocumentDS.setUpdatedAt(new Date());
			
//			dsRequisitable.save(newDocumentDS); // TODO
			LOG.info("El registro de firma digital para la solicitud " + idRequisition + " ah sido creado");
			LOG.info("Proveedor de firma elegido " + newDocumentDS.getDigitalSignatureProvider());

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}

	}

	private FileUploadInfo validDocumentVersion(FileUploadInfo template, List<VersionDTO> dtos)
			throws BusinessException {
		if (dtos != null && !dtos.isEmpty()) {
			if (template.getIdFile() == null || template.getIdFile() <= 0) {
				template.setIdFile(dtos.get(0).getIdDocument());
			}
		}
		return template;
	}

	private void saveContractVersion(Integer idRequisition, List<VersionDTO> dtos, Integer idVersionedDocument)
			throws BusinessException {
		if (dtos == null || dtos.isEmpty()) {
			LOG.info("###########################################");
			LOG.info("IS NULLLLLLL");
			LOG.info("###########################################");
			this.documentVersionBusiness.saveContractVersion(idRequisition, idVersionedDocument);
		} else {
			LOG.info("###########################################");
			LOG.info("Es DIFERENTE DE NULLLLLLL");
			LOG.info("###########################################");
			dtos = dtos.stream().filter(d -> d.getIdDocument().toString().equals(idVersionedDocument.toString()))
					.collect(Collectors.toList());
			if (dtos == null || dtos.isEmpty()) {
				LOG.info("###########################################");
				LOG.info("IS NULLLLLLL en else de !isEmpty");
				LOG.info("###########################################");
				this.documentVersionBusiness.saveContractVersion(idRequisition, idVersionedDocument);

			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInDraftProem(Requisition requisition) throws BusinessException {
		this.saveOrUpdateDartProem(requisition);

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInDraftClausules(Requisition requisition) throws BusinessException {
		this.saveOrUpdateDartClausules(requisition);

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveRequisitionInDraftProperty(Requisition requisition) throws BusinessException {
		this.saveOrUpdateDartProperty(requisition);
	}

	public boolean validateStatusByIdRequisition(final Integer idRequisition, FlowPurchasingEnum status)
			throws BusinessException {
		try {
			FlowPurchasingEnum statusBD = this.requisitable.getStatusByIdRequisition(idRequisition);
			if (statusBD == status) {
				return true;
			}
			return false;
		} catch (DatabaseException e) {
			LOG.error("Error al obtener el estatus de la solicitud", e);
			throw new BusinessException("Error al obtener el estatus de la solicitud", e);
		}
	}

	public Integer getIdJuridico() throws BusinessException {
		LOG.info("Se obtiene el id del Jurídico");
		try {
			return this.requisitable.findFirstJuristic();
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener el abogado", databaseException);
			throw new BusinessException("Error al obtener el abogado", databaseException);
		}
	}

	public Integer findTemplateIdDocumentByIdRequisition(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findTemplateIdDocumentByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			LOG.error("Error al obtener el TemplateIdDocument ", e);
			throw new BusinessException("Error al obtener el TemplateIdDocument", e);
		}
	}
	public Integer findTemplateIdSupplierIdDocumentFinal(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findTemplateIdSupplierDocumentByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			LOG.error("Error al obtener el TemplateIdDocument ", e);
			throw new BusinessException("Error al obtener el TemplateIdDocument", e);
		}
	}
	
	public String findTemplateNameDocumentByIdRequisition(final Integer idRequisition) throws BusinessException {
		try {
			return this.requisitable.findTemplateNameDocumentByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			LOG.error("Error al obtener el TemplateIdDocument ", e);
			throw new BusinessException("Error al obtener el TemplateIdDocument", e);
		}
	}

	public File returnFinalContractFile(final Integer idRequisition, final Integer templateIdDocument,
			String pathRequisition) throws BusinessException {
		Integer idDocument = null;
		if (templateIdDocument == null || templateIdDocument <= 0) {

			File draftRequisition = this.downloadDraftRequisition(idRequisition, false);
			String pathTemp = ValidatePathSistem.getUrlSistem(pathRequisition + "\\" + draftRequisition.getName());
			if (draftRequisition.renameTo(new File(pathTemp))) {
				draftRequisition = new File(pathTemp);
				return draftRequisition;
			} else {
				throw new BusinessException("Error al generar el QR");
			}

		} else {
			String templatePath = "";
			templatePath = this.documentVersionBusiness.findCurrentVersion(templateIdDocument).getDocumentPath();
			return new File(templatePath);
		}
	}

	public File returnContractFileNameQR(final Integer idRequisition) throws BusinessException {
		Integer templateIdDocument = findTemplateIdDocumentByIdRequisition(idRequisition);
		if (templateIdDocument == null) {
			throw new BusinessException("Error al generar el QR");
		}

		String templatePath = this.documentVersionBusiness.findCurrentVersion(templateIdDocument).getDocumentPath();
		File file = new File(templatePath);
		return file;
	}

	private void saveFinalVersionQR(final Integer idRequisition, String finalPath) throws BusinessException {
		Integer idDocument = null;
		File draftRequisition = new File(finalPath);
		List<VersionDTO> dtos = this.documentVersionBusiness.findContractVersionDTO(idRequisition);

		LOG.info("\n==================================== GUARDANDO VERSIÓN FINAL QR. ====================================");
		
		LOG.info("DESTINO ::"+finalPath);
		LOG.info("DIRECTORIO DESTINO PREVIAMENTE #######################################################################");
		LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(draftRequisition.getParentFile()));	
		
		if (dtos != null && !dtos.isEmpty()) {
			idDocument = dtos.get(0).getIdDocument();
		} else {
			idDocument = this.documentVersionBusiness.save(idDocument, draftRequisition);
		}
		LOG.info("idDocument :: "+idDocument);

		final Integer idVersionedDocument = this.documentVersionBusiness.save(idDocument, draftRequisition);
		LOG.info("idVersionedDocument :: "+idVersionedDocument);

		try {
			this.requisitable.saveTemplateIdDocument(idRequisition, idVersionedDocument);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al generar el QR");
		}
		this.saveContractVersion(idRequisition, dtos, idVersionedDocument);
		
		LOG.info("DIRECTORIO DESTINO POSTERIORMENTE #######################################################################");
		LOG.info("\n"+PrintDirectoryTree.printDirectoryTree(draftRequisition.getParentFile()));
	}
	
	public List<SupplierPersonByRequisition> getIdsSupplierPersonByIdRequisitionDTO(final Integer idRequisition, final Integer idSupplier)
			throws BusinessException {
		LOG.info(this.getClass().getSimpleName() + " -> getIdsSupplierPersonByIdRequisition : " + idRequisition);
		try {
			if(idSupplier !=null) {
				return this.requisitable.getIdsSupplierPersonByIdRequisitionDTO(idRequisition, idSupplier);
			} 
			return null;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_ERROR_SuPPLIER_PERSON, databaseException);
			throw new BusinessException(MESSAGE_ERROR_SuPPLIER_PERSON, databaseException);
		}
	}

	private Boolean validaEnteros(String contrapresta) {
		try {
			Double cantidad = Double.parseDouble(contrapresta);
			LOG.info("CONTRAPRESTACION CANTIDAD: " + cantidad);
			return true;
		} catch (NumberFormatException e) {
			LOG.info("CONTRAPRESTACION texto: " + contrapresta);
			return false;
		}catch (NullPointerException n) {
			return false;
		}
	}
	
	private Boolean isNull(Object valor) {
		try {
		  if (valor instanceof Integer) {
			  if(null != valor) {
					return false;
				}
		    } else if(valor instanceof String) {
		    	if(!valor.toString().trim().isEmpty()) {
					return false;
				}		    	
		    } else if(valor instanceof Double) {
		    	if(null != valor) {
					return false;
				}		    	
		    }
		  return true;
		} catch (NullPointerException e) {			
			return true;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void requestUpdateDraft(final Requisition requisitionParam) throws BusinessException {
		this.requisitionVersionBusiness.saveRequisitionVersion(requisitionParam.getIdRequisition());
		if (requisitionParam.getTemplateUploadInfo() != null) {
			this.saveTemplateIdDocument(requisitionParam.getTemplateUploadInfo());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void sendToDocumentFinalDS(final Requisition requisitionParam) throws BusinessException {
		final FileUploadInfo approvalSupplierDocument = requisitionParam.getSupplierApprovalDocument();
		try {
			// servicios para subir archivo final
			if (approvalSupplierDocument.getDocumentName() != null) {
				approvalSupplierDocument.setIdFile(this.versionRequisitionDocumentFinalDS(
						requisitionParam.getIdRequisition(), approvalSupplierDocument));
				LOG.info("##################################################");
				LOG.info("EL ID DE DOCUMENT ES :::: " + approvalSupplierDocument.getIdFile());
				LOG.info("##################################################");
				List<VersionDTO> dtos = this.documentVersionBusiness
						.findContractVersionDTO(approvalSupplierDocument.getIdRequisition());
				this.validDocumentVersion(approvalSupplierDocument, dtos);
				// final Integer idVersionedDocument =
				// this.versionSupplierDocument(approvalSupplierDocument.getIdRequisition(),
				// approvalSupplierDocument);
				LOG.info("##################################################");
				LOG.info("EL List<VersionDTO> dtos ES :::: " + dtos);
				LOG.info("##################################################");
				LOG.info("##################################################");
				LOG.info("EL idVersionedDocument ES :::: " + approvalSupplierDocument.getIdFile());
				LOG.info("##################################################");

				this.requisitable.saveSupplierApprovalDocument(approvalSupplierDocument.getIdRequisition(),
						approvalSupplierDocument.getIdFile());
				this.saveContractVersion(approvalSupplierDocument.getIdRequisition(), dtos,
						approvalSupplierDocument.getIdFile());
			} else {
				System.out.println("no se cargo el archivo final");
			}
		} catch (Exception databaseException) {
			LOG.error("Error al actualizar el contrato: ContractRisk", databaseException);
			throw new BusinessException("Error al actualizar el contrato: ContractRisk", databaseException);
		}
	}

		
	private String createSaveNameDS(final FileUploadInfo fileUploadInfo, final Integer idDocument)
			throws BusinessException {
		String version = "1";
		if (idDocument != null) {
			final Version currentVersion = this.documentVersionBusiness.findCurrentVersion(idDocument);
			final Integer nextVersion = currentVersion.getVersionNumber() + (Integer) 1;
			version = nextVersion.toString();
		}
		final String fileNameWithoutExtension = FilenameUtils.removeExtension(fileUploadInfo.getName());
		final String extension = FilenameUtils.getExtension(fileUploadInfo.getName());
		return fileUploadInfo.getName();
	}

	private Integer versionAndMoveDocumentFinalDS(final File targetDocumentPath, final FileUploadInfo fileUploadInfo)
			throws BusinessException {

		LOG.info(
				"\n==================================== MOVIENDO DOCUMENTO TEMPORAL. ====================================");

		Integer idDocument = fileUploadInfo.getIdFile();
		this.cleanNameSpaces(fileUploadInfo);
		final File userTempFilesPath = new File(this.createUserTemporalPath());
		final String saveName = this.createSaveNameDS(fileUploadInfo, idDocument);
		final File finalFullFile = new File(targetDocumentPath.getAbsolutePath() + File.separator + saveName);

		LOG.info("ORIGEN ::" + userTempFilesPath.getAbsolutePath());
		LOG.info("DESTINO ::" + finalFullFile.getAbsolutePath());

		LOG.info(
				"DIRECTORIO ORIGEN PREVIAMENTE  #######################################################################");
		LOG.info("\n" + PrintDirectoryTree.printDirectoryTree(new File(userTempFilesPath.getAbsolutePath())));

		LOG.info(
				"DIRECTORIO DESTINO PREVIAMENTE #######################################################################");
		LOG.info("\n" + PrintDirectoryTree.printDirectoryTree(targetDocumentPath));

		idDocument = this.documentVersionBusiness.save(fileUploadInfo.getIdFile(), finalFullFile);
		if (this.fileExist(fileUploadInfo, userTempFilesPath)) {
			targetDocumentPath.mkdir();
			try {
				Files.move(this.getOriginFilePath(fileUploadInfo, userTempFilesPath),
						this.createFinalFilePath(saveName, targetDocumentPath), StandardCopyOption.ATOMIC_MOVE);

				LOG.info(
						"DIRECTORIO ORIGEN POSTERIORMENTE #######################################################################");
				LOG.info("\n" + PrintDirectoryTree.printDirectoryTree(new File(userTempFilesPath.getAbsolutePath())));

				LOG.info(
						"DIRECTORIO DESTINO POSTERIORMENTE #######################################################################");
				LOG.info("\n" + PrintDirectoryTree.printDirectoryTree(targetDocumentPath));

			} catch (IOException ioException) {
				LOG.error(MESSAGE_MOVING_DOCUMENT_ERROR, ioException);
				throw new BusinessException(MESSAGE_MOVING_DOCUMENT_ERROR, ioException);
			}

		}
		return idDocument;
	}

	private Integer versionRequisitionDocumentFinalDS(final Integer idRequisition, final FileUploadInfo fileUploadInfo)
			throws BusinessException {
		final File requisitionDocumentPath = this.getRequisitionPath(idRequisition);
		return this.versionAndMoveDocumentFinalDS(requisitionDocumentPath, fileUploadInfo);
	}

}