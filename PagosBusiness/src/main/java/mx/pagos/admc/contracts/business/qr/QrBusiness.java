package mx.pagos.admc.contracts.business.qr;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.engineer.utils.general.ValidatePathSistem;
import mx.engineer.utils.secutiry.SecurityEncrypt;
import mx.engineer.utils.word.AddFooterQr;
import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.interfaces.Qr;
import mx.pagos.admc.contracts.interfaces.owners.RequisitionOwnersable;
import mx.pagos.admc.contracts.structures.QrData;
import mx.pagos.admc.contracts.structures.QuickResponse;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionComplete;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.admc.core.utils.DirectoryUtils;
import mx.pagos.admc.enums.DocumentTypeEnum;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.document.version.business.DocumentVersionBusiness;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.business.UsersBusiness;
import mx.pagos.security.structures.UserSession;

@Service("QrBusiness")
public class QrBusiness {

	private static final Logger LOG = Logger.getLogger(UsersBusiness.class);
	private static final String MESSAGE_CONTRACT_ERROR = "Error al generar el contrato";
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
	private static final String SEPARATOR_POINT = ".";
	private static final int CERO = 0;

	@Autowired
	private Qr qrDao;

	@Autowired
	private RequisitionBusiness requisitionBusiness;

	@Autowired
	private UserSession session;
	
	public QrData findBySalt(final String codigo) throws BusinessException, ParseException {
		try {
			LOG.info("***************** findBySalt en QrBusiness ************** Codigo:" + codigo);
			DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
			final QuickResponse datosQr = this.qrDao.findBySalt(codigo);
			RequisitionComplete requisitionComplete = this.requisitionBusiness
					.obtenerDetalleSolicitud(datosQr.getIdRequisition());
			QuickResponse quick = mapearQR(requisitionComplete);
			QrData qrData = new QrData();
			qrData.setFolio(quick.getFolio());
			qrData.setRfc(quick.getRFC());
			qrData.setIdApplicant(requisitionComplete.getApplicant().getFullName());
			Date fecha = format.parse(requisitionComplete.getRequisitionsPartOneAndTwo().getApplicationDate());
			qrData.setRequistionDate(format.format(fecha));
			qrData.setIdCompany(quick.getIdCompany());
			qrData.setIdUnit(quick.getIdUnit());
			qrData.setIdArea(quick.getIdArea());
			List<RequisitionStatusTurn> list = this.requisitionBusiness.findRequisitionStatusTurnsByIdRequisition(
					requisitionComplete.getRequisitionsPartOneAndTwo().getIdRequisition());
			list.forEach(r -> {
				if (r.getStatus() == FlowPurchasingEnum.PRINT_CONTRACT) {
					Date fecha2 = null;
					try {
						fecha2 = format.parse(r.getTurnDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					qrData.setVoBoDate(format.format(fecha2));
				}
			});
			LOG.info("Date qr folio :: " + qrData.getFolio());
			return qrData;
		} catch (DatabaseException databaseException) {
			LOG.error("Hubo un error al obtener los datos del QR", databaseException);
			throw new BusinessException("Error al obtener el QR", databaseException);
		} catch (EmptyResultException emptyResultException) {
			throw new BusinessException("Tal código de Qr no existe.", emptyResultException);
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
		
		Integer idRequisition = req.getIdRequisition();
		Integer templateIdDocument = this.requisitionBusiness.findTemplateIdDocumentByIdRequisition(idRequisition);
		String templateName = this.requisitionBusiness.findTemplateNameDocumentByIdRequisition(idRequisition);
		
		LOG.info("writeQR :: Ruta ("+filePath.getPath()+") - idRequisition ("+idRequisition+")"+ "- Template ("+templateIdDocument+")" + "- NombreTemplate ("+templateName+")");
		
		File fileName = this.requisitionBusiness.returnFinalContractFile(idRequisition, templateIdDocument, filePath.getPath());
		
		String complete = new String(fileName.getName());
		req.setDocumentName(complete);
		req.setIdApplicant(session.getIdUsuarioSession());		
		
		String path = fileName.getPath().replace(ValidatePathSistem.getSeparatorSistem()+complete,"");
		
		String finalQRPath = addQRFooter(fileName, path, req, templateName);
//		if (fileName.exists()) {
//			fileName.delete();
//		}
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
		RequisitionComplete reqCom = this.requisitionBusiness.obtenerDetalleSolicitud(req.getIdRequisition());
		QuickResponse qr = mapearQR(reqCom);
		qr.setName(req.getDocumentName());
		qr.setRisk(req.isVoBocontractRisk());
		qr.setIdApplicant(req.getIdApplicant());
		qr.setIdRequisition(req.getIdRequisition());
		List<RequisitionStatusTurn> list = this.requisitionBusiness
				.findRequisitionStatusTurnsByIdRequisition(req.getIdRequisition());
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
				afqr.addQRFooter(requisition, nameImage(path, imageName), imageNameComplete,
						         nameQR, qr.getFolio(), templateOthers);
				return nameQR;

			}
		} catch (Exception e) {
			new BusinessException(QR_MASC);
		}
		return nameQR;
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
	
	
//	private String addQRFooterTest(File requisition, String path, Requisition req, String templateName, QuickResponse qr) throws BusinessException {
//		String nameQR = null;		
//		qr.setName(req.getDocumentName());
//		qr.setRisk(req.isVoBocontractRisk());
//		qr.setIdApplicant(req.getIdApplicant());
//		qr.setIdRequisition(req.getIdRequisition());
//		qr.setVoBodate("2020-09-03 18:39:03"); // turnDate
//		System.out.println("AddFooterQr");
//		AddFooterQr afqr = new AddFooterQr();
//		int isj = qr.getName().lastIndexOf(SEPARATOR_POINT);
//		String imageName = new String(qr.getName().substring(CERO, isj));
//		String imageNameComplete = nameComplete(path, imageName, IMAGE_TYPE_FILE, EXTENSION_FILE);
//		System.out.println("imageName "+imageName);
//		System.out.println("imageNameComplete "+imageNameComplete);
//		try {
//			String salt = SecurityEncrypt.generateSalt();
//			qr.setSalt(salt);
//
//			Integer check = 15; // ID QR
//			if ((new Integer(CERO)).compareTo(check) == CERO) {
//				new BusinessException(QR_MASC);
//			} else {
//				nameQR = nameComplete(path, imageName, QUICK_RESPONSE, EXTENSION_WORD_FILE);
//				System.out.println("name qr"+nameQR);
//				afqr.generateSaveQr(salt, qr.getRisk(), imageNameComplete, QR_SIZE, QR_SIZE);
////				afqr.addQRFooter(requisition, nameImage(path, imageName), imageNameComplete,
////						         nameQR, qr.getFolio(), templateName.equalsIgnoreCase(DocumentTypeEnum.OTHERS.name()));
//				return nameQR;
//
//			}
//		} catch (Exception e) {
//			new BusinessException(QR_MASC);
//		}
//		return nameQR;
//	}
//	
	
	public static void main(String args[]) {
		
		QrBusiness qr = new QrBusiness();
		
		/* -templateName-
		 * SELECT DocumentTypeEnum		FROM DOCUMENTTYPE d
			INNER JOIN REQUISITION r ON r.IdDocumentType = d.IdDocumentType AND r.IdRequisition = :IdRequisition
		 */
		
//		Integer idreq = 52;
//		
//		String templateName = DocumentTypeEnum.OTHERS.getName();
//		File requisition = new  File("/home/emunguia/Documentos/PROYECTOS/gerdau/INCIDENCIAS/GTL_SERV_ADTIVOS___CONTRATO_ARRENDAMIENTO___MARIO_MARTINS_2020-2021___VF.doc");
//		String path ="//home/emunguia/Documentos/PROYECTOS/gerdau/INCIDENCIAS/";
//		Requisition req = new Requisition();
//		req.setIdRequisition(idreq);
//		req.setDocumentName("GTL_SERV_ADTIVOS___CONTRATO_ARRENDAMIENTO___MARIO_MARTINS_2020-2021___VF.doc"); // ??
//		req.setVoBocontractRisk(Boolean.FALSE); // en BD es = ContractRisk
//		req.setIdApplicant(20);
//		req.setStatus(FlowPurchasingEnum.APROVED_BY_JURISTIC);
//		
//		
//		QuickResponse qrRes = new QuickResponse();
//		
//		qrRes.setFolio(req.getIdRequisition().toString());
//		qrRes.setRFC("RSI160310516");
//		qrRes.setRequistionDate("10/03/2016");
//		qrRes.setIdCompany(1);
//		qrRes.setIdUnit(23);
//		qrRes.setIdArea(1);
//		
//		try {
//			System.out.println(qr.addQRFooterTest(requisition, path, req, templateName, qrRes));
//		} catch (BusinessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
