package mx.pagos.admc.service.generals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.engineer.utils.general.SubparagraphUtils;
import mx.engineer.utils.general.ValidatePathSistem;
import mx.engineer.utils.pdf.PDFUtils;
import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.business.qr.QrBusiness;
import mx.pagos.admc.contracts.structures.FileUploadInfo;
import mx.pagos.admc.contracts.structures.QrData;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.utils.DirectoryUtils;
import mx.pagos.admc.core.utils.RemoteFileUtilsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.document.version.business.DocumentVersionBusiness;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

/**
 * @author Mizraim
 */

@Controller
public class DocumentService {

	private static final String UNDERSCORE = "_";
	private static final String SCAPED_QUOTES = "\"";
	private static final String INVALID_CHARACTERS = "[\\\\/><\\|\\s\"'{}()\\[\\]:]+";
	private static final String EXTENSION_FILE = ".docx";
	private static final String FILE_ERROR = "El archivo solicitado ya no existe";
	private static final String QUICK_RESPONSE = "QRC";
	private static final String SEPARATOR_POINT = ".";
	private static final int CERO = 0;
	private static final String EXTENSION_WORD_FILE = ".docx";

	@Autowired
	private UserSession session;

	@Autowired
	private ConfigurationsBusiness configuration;

	@Autowired
	private RemoteFileUtilsBusiness remoteFileUtils;

	@Autowired
	private DirectoryUtils directoryUtils;

	@Autowired
	private RequisitionBusiness requisitionBusiness;

	@Autowired
	private DocumentVersionBusiness documentVersionBusiness;

	@Autowired
	private QrBusiness qrBusiness;

	@Autowired
	private BinnacleBusiness binnacleBusiness;
	
	private static final Logger LOG = Logger.getLogger(DocumentService.class);

	@RequestMapping(value = UrlConstants.DELETE_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public final FileUploadInfo deleteService(@RequestBody final FileUploadInfo uploadLayoutInfo,
			final HttpServletRequest request, final HttpServletResponse response) {
		String path;
		int reg;
		File filePath;
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Eliminación del archivo " + "llamado " + uploadLayoutInfo.getName(), this.session,
					LogCategoryEnum.DELETE));
			final String saveName = uploadLayoutInfo.getName().replaceAll(INVALID_CHARACTERS, UNDERSCORE);
			final boolean fileNew = uploadLayoutInfo.isFileNew();
			if (fileNew) {
				path = this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString());
				filePath = new File(path + Constants.PATH_TMP + File.separator + this.session.getIdUsuarioSession()
						+ File.separator);
			} else {
				filePath = this.getRequisitionPath(uploadLayoutInfo.getIdRequisition());
				this.requisitionBusiness.deleteRequisitionAttatchments(uploadLayoutInfo.getIdRequisition(),
						uploadLayoutInfo.getIdFile());
			}
			final File file = new File(filePath + File.separator + saveName);
			reg = file.exists() ? 1 : 2;
			if (reg == 1)
				file.delete();
		} catch (Exception e) {
			reg = 0;
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
		uploadLayoutInfo.setReturnExist(reg);
		return uploadLayoutInfo;

	}

	@GetMapping(value = UrlConstants.DOWNLOAD_SERVICE)
	@ResponseBody
	public final void downloadService(final HttpServletRequest request, final HttpServletResponse response) {
		String path;
		File filePath;
		
		LOG.info(" **********   downloadService  **********");
		
		try {
//			final String saveName = StringUtils.limpiaCadena(request.getParameter("fileName")).replaceAll(INVALID_CHARACTERS, UNDERSCORE);
			final String saveName = SubparagraphUtils.stripAccents(request.getParameter("fileName")).replaceAll(INVALID_CHARACTERS, UNDERSCORE);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(	"Descarga del archivo " + "llamado " + saveName, this.session, LogCategoryEnum.DOWNLOAD));
			final boolean fileNew = Boolean.valueOf(request.getParameter("fileNew"));
			final String idRequisition = request.getParameter("idRequisition");
			if (fileNew) {
				path = this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString());
				filePath = new File(path + Constants.PATH_TMP + File.separator + this.session.getIdUsuarioSession()
						+ File.separator);
				LOG.info(" path :: "+path);
			} else {
				filePath = this.getRequisitionPath(Integer.parseInt(idRequisition));
			}
			
			LOG.info(" filePath :: "+filePath);

			final File file = new File(filePath + File.separator + saveName);
			LOG.info(" ARCHIVO :: "+filePath + File.separator + saveName);
			if (file.exists()) {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=" + saveName);
				final FileInputStream fileInputStream = new FileInputStream(file);
				final ServletOutputStream servletOutputStream = response.getOutputStream();
				IOUtils.copy(fileInputStream, servletOutputStream);
				servletOutputStream.flush();
				servletOutputStream.close();
				fileInputStream.close();
			} else {
				LOG.error("El archivo solicitado ya no existe");
				throw new Exception("El archivo solicitado ya no existe");
			}
		} catch (Exception e) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
	}

	private File getRequisitionPath(final Integer idRequisition) throws BusinessException {
		return this.directoryUtils.getRequisitionPath(idRequisition);
	}

	@RequestMapping(value = UrlConstants.VERIFY_CURRENT_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public final void verifyDocument(@RequestBody final Integer idDocument, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			final File document = new File(
					this.documentVersionBusiness.findCurrentVersion(idDocument).getDocumentPath());
			if (!document.exists())
				throw new BusinessException("El archivo solicitado ha dejado de existir");
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.VERIFY_DOCUMENT_PATH, method = RequestMethod.POST)
	@ResponseBody
	public final void verifyPathDocument(@RequestBody final String documentPath, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			
			LOG.info("verifyPathDocument :: documentPath ("+documentPath+")");
			
			final File document = new File(documentPath);
			if (!document.exists())
				throw new BusinessException("El archivo solicitado ha dejado de existir");
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.GENERAL_DOWNLOAD_SERVICE, method = RequestMethod.GET)
	public final void downloadDocumentRequired(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String idDocumentParameter = request.getParameter("idDocument");
			final Boolean isDeleteFile = Boolean.valueOf(request.getParameter("isDeleteFile"));
			Version bean = new Version();
			bean = this.documentVersionBusiness.findCurrentVersion(Integer.parseInt(idDocumentParameter));
			final String parameterDocumentPath = bean.getDocumentPath();
			this.sendFileToDownload(response, parameterDocumentPath, isDeleteFile);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Descarga del archivo requerido " + FilenameUtils.getName(request.getParameter("documentPath")),
					this.session, LogCategoryEnum.DOWNLOAD));
		} catch (IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.DOWNLOAD_REMOTE_FILE_SERVICE, method = RequestMethod.GET)
	public final void downloadRemoteFile(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String filePath = request.getParameter("filePath");
			final File file = this.remoteFileUtils.getRemoteFile(filePath,
					this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_TMP);
			this.sendFile(response, file);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Descarga del archivo remoto " + FilenameUtils.getName(request.getParameter("filePath")),
					this.session, LogCategoryEnum.DOWNLOAD));
		} catch (IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	private void sendFile(final HttpServletResponse response, final File file)
			throws FileNotFoundException, IOException {
		this.setReponseData(response, file.getName(), file);
		final FileInputStream fileInputStream = new FileInputStream(file);
		final ServletOutputStream servletOutputStream = response.getOutputStream();
		IOUtils.copy(fileInputStream, servletOutputStream);
		fileInputStream.close();
		servletOutputStream.flush();
		servletOutputStream.close();
		if (file.exists())
			file.delete();
	}

	private void sendFileToDownload(final HttpServletResponse response, final String parameterDocumentPath,
			final Boolean isDeleteFile) throws FileNotFoundException, IOException {
		final File file = new File(parameterDocumentPath);
		this.setReponseData(response, file.getName(), file);
		final FileInputStream fileInputStream = new FileInputStream(file);
		final ServletOutputStream servletOutputStream = response.getOutputStream();
		IOUtils.copy(fileInputStream, servletOutputStream);
		fileInputStream.close();
		servletOutputStream.flush();
		servletOutputStream.close();
		if (isDeleteFile)
			if (file.exists())
				file.delete();
	}

	private void setReponseData(final HttpServletResponse response, final String name, final File file) {
		final int buffer = 1024 * 100;
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment;filename=" + SCAPED_QUOTES + name + SCAPED_QUOTES);
		response.setContentLength(Long.valueOf(file.length()).intValue());
		response.setBufferSize(buffer);
	}

	@RequestMapping(value = UrlConstants.GENERATE_QR, method = RequestMethod.POST)
	@ResponseBody
	public final String generateQr(@RequestBody final Requisition req, final HttpServletResponse response) {
		LOG.info("-------------------------------------------------------- GENERANDO QR");
		String qrPath = null;
		try {
			File filePath = this.getRequisitionPath(req.getIdRequisition());
			qrPath = this.qrBusiness.writeQR(req, filePath);
			return qrPath;
		} catch (BusinessException e) {
			qrPath = null;
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
		return qrPath;
	}

	/**
	 * Metodo genera QR y lo agrega al contrato final.
	 * 
	 * @param req      Requisition con el detalle
	 * @param response solamente envia exepcion si ocurre error
	 * @throws Docx4JException 
	 * @throws BusinessException exepcion a manejar
	 */
	//Descarga el contrato de "Contratos para impresión"
	@RequestMapping(value = UrlConstants.DOWNLOAD_QR, method = RequestMethod.GET)
	@ResponseBody
	public final void downloadQr(final HttpServletRequest request, final HttpServletResponse response) throws Docx4JException {
		LOG.info("-------------------------------------------------------- DESCARGA DE ARCHIVO CON QR");
		try {			
			final String idRequisition = StringUtils.limpiaCadena(request.getParameter("idRequisition"));
//			final String idRequisition = SubparagraphUtils.stripAccents(request.getParameter("idRequisition")).replaceAll(INVALID_CHARACTERS, UNDERSCORE);
			File file = this.requisitionBusiness.returnContractFileNameQR(Integer.parseInt(idRequisition));
			
			file = new File(ValidatePathSistem.getUrlSistem(file.getPath()));
			
			LOG.info("idRequisition ::"+idRequisition+"");
			LOG.info("Archivo ::"+file+"");
			if (file.exists()) {
				String rutaPDF = file.getPath().replace(".docx", ".pdf");
				this.descargarPDFBorradorContrato(response, file, rutaPDF);
				File filePDF = new File(rutaPDF);	
				if (filePDF.exists() && !file.getPath().endsWith(".pdf")) {
					filePDF.delete();
				}
			} else {
				LOG.error("ERROR :: El archivo solicitado ya no existe");
				throw new BusinessException("El archivo solicitado ya no existe");
			}
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (BusinessException | IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, FILE_ERROR);
		}
	}
	
	/** Metodo genera QR y lo agrega al contrato final.
	 * 
	 * @param req      Requisition con el detalle
	 * @param response solamente envia exepcion si ocurre error
	 * @throws Docx4JException 
	 * @throws BusinessException exepcion a manejar
	 */
	@RequestMapping(value = UrlConstants.DOWNLOAD_DOC_QR, method = RequestMethod.GET)
	@ResponseBody
	public final void downloadQrDoc(final HttpServletRequest request, final HttpServletResponse response) throws Docx4JException {
		LOG.info("-------------------------------------------------------- DESCARGA DE ARCHIVO DOC CON QR");
		try {			
			final String idRequisition = StringUtils.limpiaCadena(request.getParameter("idRequisition"));
			File file = this.requisitionBusiness.returnContractFileNameQR(Integer.parseInt(idRequisition));
			
			file = new File(ValidatePathSistem.getUrlSistem(file.getPath()));
			
			LOG.info("idRequisition ::"+idRequisition+"");
			LOG.info("Archivo ::"+file+"");
			if (file.exists()) {				
				this.descargarDOCBorradorContrato(response, file);				
			} else {
				LOG.error("ERROR :: El archivo solicitado ya no existe");
				throw new BusinessException("El archivo solicitado ya no existe");
			}
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (BusinessException | IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, FILE_ERROR);
		}
	}

	/**
	 * Metodo para obtener los datos del contrato que se guardaron en QR.
	 * 
	 * @param codigo obtenido de el escaneo del QR.
	 * @return objeto con los datos gurdados en el QR
	 * @throws ParseException 
	 * @throws BusinessException exepcion a manejar
	 */
	@RequestMapping(value = UrlConstants.GET_QR, method = RequestMethod.POST)
	@ResponseBody
	public final QrData findUserById(@RequestBody final QrData codigo, final HttpServletResponse response) throws ParseException {
	        try {
	        	QrData qr = this.qrBusiness.findBySalt(codigo.getCodigo());
	        	return qr;
	        } catch (BusinessException businessException) {
	            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
	            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
	            return new QrData();
	        }
	}
	
	private void descargarPDFBorradorContrato (final HttpServletResponse response, File doc, String pdfPath) throws Docx4JException, IOException  {
		
		String docStr = doc.getPath();
		String pdfStr = FilenameUtils.removeExtension(doc.getPath())+".pdf";
		
		LOG.info("==================================================\n DESCARGA PDF BORRADOR CONTRATO\n"
				+ "RUTA-Documento WORD  :: "+docStr+"\n"
				+ "Documento PDF   		:: "+pdfStr);
		
		/* v1 word to pdf */
//		final File document = PDFUtils.convertDocxToPDF(doc);
		
		/* v2 word to pdf */	
		if(!doc.getPath().endsWith(".pdf")) {			
			PDFUtils.convertDocxToPDF_v2(docStr, pdfStr);
		}
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
	
	private void descargarDOCBorradorContrato (final HttpServletResponse response, File doc) throws Docx4JException, IOException  {
		
		String docStr = doc.getPath();
		
		LOG.info("==================================================\n DESCARGA DOC BORRADOR CONTRATO\n"
				+ "RUTA-Documento WORD  :: "+docStr);
		
		final File document = new File(docStr);
		
		
		this.setReponseData(response, document.getName(), document);
		final FileInputStream fileInputStream = new FileInputStream(document);
		final ServletOutputStream servletOutputStream = response.getOutputStream();
		IOUtils.copy(fileInputStream, servletOutputStream);
		fileInputStream.close();
		servletOutputStream.flush();
		servletOutputStream.close();
		
		LOG.info("=================================================================================");
	}

}
