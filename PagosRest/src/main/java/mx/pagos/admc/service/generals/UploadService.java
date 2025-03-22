package mx.pagos.admc.service.generals;

import gwtupload.server.UploadAction;
import gwtupload.server.UploadListener;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.engineer.utils.file.PrintDirectoryTree;
import mx.engineer.utils.general.SubparagraphUtils;
import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.structures.Clause;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.ConfigurationException;
import mx.pagos.security.structures.UserSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadService extends UploadAction {
	private static final String OPEN_FILE_ATTRIBUTE = "<file-";
	private static final String CLOSE_FILE_ATTRIBUTE = "</file-";
	private static final long serialVersionUID = 5939798441314844205L;
	private final Integer bytesFactor = 1024;
	private final Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	private final Hashtable<String, File> receivedFiles = new Hashtable<String, File>();
	private static final Logger LOG = Logger.getLogger(UploadService.class);

	@Autowired
	private UserSession session;

	@Autowired
	private ConfigurationsBusiness configuration;
	
	@Autowired
	private RequisitionBusiness requisitionBusiness;

	@PostMapping(UrlConstants.UPLOAD_SERVICE_ANGULAR)
	public void handleFileUpload(@RequestParam("file") MultipartFile[] files, final HttpServletResponse response)
			throws ConfigurationException {
		LOG.info("Se reciben archivos en BACK");
		try {
			LOG.info("la sesion es :::: "+this.session.getIdUsuarioSession());
			LOG.info("Lo que trae files UPLOADSERVICES:::: "+files);
			String rutaArchivos = rutaBase();
			for (MultipartFile file : files) {
				sendFiles(file, rutaArchivos);
			}
			response.setContentType("multipart/form-data");
			response.setContentType("text/plain");
			response.setContentType("multipart");
			response.setContentType("form-data");
			response.setContentType("multipart/mixed");
			response.getWriter().write("");
		} catch (BusinessException | IOException e) {
			e.printStackTrace();
		}
	}

	private String rutaBase() throws BusinessException, ConfigurationException {
		return this.configuration.getTemporalPath();
	}

	private void sendFiles(MultipartFile item, String path) {
		try {
//			final String saveName = StringUtils.limpiaCadena(item.getOriginalFilename());
			final String saveName = SubparagraphUtils.stripAccents(item.getOriginalFilename()).replaceAll("[\\\\/><\\|\\s\"'`´,^{}()\\[\\]:]+", "_");
			final String pathName = path + File.separator + this.session.getIdUsuarioSession() + File.separator;
			final File filePath = new File(pathName);
			if (!filePath.exists())
				filePath.mkdirs();
			LOG.info("Llega el arcivo: " + item.getOriginalFilename() + " - renombrado a: " + saveName);
			LOG.info("Enviado a: " + pathName);
			if (!filePath.exists())
				throw new BusinessException(
						"Hubo un problema al cargar el archivo debido a " + "que la carpeta temporal no existe");
			Path copyLocation = Paths.get(pathName + File.separator + saveName);
			Files.copy(item.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception ex) {
			LOG.error("Excepción durante la transferencia: ", ex);
		}
	}

	@Override
	@RequestMapping(value = UrlConstants.UPLOAD_SERVICE, method = RequestMethod.GET)
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException {
		try {
			LOG.info("CARGA DE ARCHIVO COMPLETA");
			super.doGet(request, response);
		} catch (Exception exception) {
			LOG.error("Hubor un problema al reponder la carga del archivo");
			LOG.error(exception);
		}
	}

	@Override
	@RequestMapping(value = UrlConstants.UPLOAD_SERVICE, method = RequestMethod.POST)
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException {
		try {
			LOG.info(
					"\n==================================== SERVICIO DE CARGA DE ARCHVOS. ====================================");
			super.doPost(request, response);

		} catch (Exception exception) {
			LOG.error("Hubor un problema al cargar el archivo");
			LOG.error(exception);
		}
	}

	@Override
	public String executeAction(final HttpServletRequest request, final List<FileItem> sessionFiles)
			throws UploadActionException {
		String response = "";
		Integer counter = 0;

		for (FileItem item : sessionFiles) {
			if (!item.isFormField()) {
				counter++;
				try {
					LOG.info("ARCHIVO RECIBIDO :: nombre (" + item.getFieldName() + ")");
					if (item.getSize() <= 0) {
						LOG.error("ERRO :: El archivo tiene un tamaño de :" + item.getSize());
						throw new UploadActionException("ERRO :: El archivo tiene un tamaño de :" + item.getSize());
					}
					this.receivedFiles.put(item.getFieldName(), this.validaCarga(item));
					this.receivedContentTypes.put(item.getFieldName(), item.getContentType());
					response = this.createResponse(counter, item);
				} catch (Exception exception) {
					LOG.error(exception);
					throw new UploadActionException(exception.getMessage());
				}
			}
		}
		removeSessionFileItems(request);
		LOG.info("<response>\n" + response + "</response>\n");
		return "<response>\n" + response + "</response>\n";
	}

	private File validaCarga(FileItem archivo) {
		LOG.error("VALIDA CARGA DE ARCHIVO");
		for (int x = 0; x < 4; x++) {
			LOG.error("PRUEBA " + x);
			try {
				File tempArchivo = this.createFileFromSessionFiles(archivo);
				LOG.error("CARGA EXITOSA");
				return tempArchivo;
			} catch (Exception exception) {
				LOG.error("ERROR AL CARGAR EL ARCHIVO");
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
				}
			}

		}
		LOG.error("NO SE PUDO CARGAR EL ARCHIVO");
		return null;
	}

	private String createResponse(final Integer counter, final FileItem item) {
		return OPEN_FILE_ATTRIBUTE + counter + "-field>" + item.getFieldName() + CLOSE_FILE_ATTRIBUTE + counter
				+ "-field>\n" + OPEN_FILE_ATTRIBUTE + counter + "-name>" + item.get() + CLOSE_FILE_ATTRIBUTE + counter
				+ "-name>\n" + OPEN_FILE_ATTRIBUTE + counter + "-size>" + item.getSize() + CLOSE_FILE_ATTRIBUTE + counter
				+ "-size>\n" + OPEN_FILE_ATTRIBUTE + counter + "-type>" + item.getContentType() + CLOSE_FILE_ATTRIBUTE + counter
				+ "type>\n";
	}

	private File createFileFromSessionFiles(final FileItem item) throws Exception {
		LOG.info("createFileFromSessionFiles: " + item.getFieldName());
		final String path = this.configuration.getTemporalPath();
		final String saveName = StringUtils.limpiaCadena(item.getName());
		// SubparagraphUtils.stripAccents(item.getName()).replaceAll("[\\\\/><\\|\\s\"'`´,^{}()\\[\\]:]+",
		// "_");
		final File filePath = new File(path + File.separator + this.session.getIdUsuarioSession() + File.separator);
		LOG.info("createFileFromSessionFiles: (User) " + this.session.getIdUsuarioSession());

		filePath.mkdir();
		LOG.info("VALIDA DIRECTORIO PREVIAMENTE: ");
		LOG.info(
				"===============================================================================================================================");
		LOG.info("DIRECORIO :: " + filePath.getPath());
		LOG.info("ARCHIVO :: [" + saveName + "]");
		LOG.info("CONTENIDO ::");

		this.validaDirectorio(filePath);
		final File file = new File(filePath + File.separator + saveName);
		if (!filePath.exists())
			throw new BusinessException(
					"Hubo un problema al cargar el archivo debido a " + "que la carpeta temporal no existe");
		item.write(file);

		LOG.info("VALIDA DIRECTORIO POSTERIORMENTE: ");
		this.validaDirectorio(filePath);
		LOG.info(
				"===============================================================================================================================");

		return file;
	}

	private void validaDirectorio(File filePath) {
		String[] archivos = filePath.list();
		if (archivos == null)
			LOG.info("Directirio esta vacio");
		else
			LOG.info("\n" + PrintDirectoryTree.printDirectoryTree(filePath));
	}

	@Override
	public final void getUploadedFile(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		try {
			LOG.info("Se procesará el archivo recibido");
			final String fieldName = request.getParameter(UConsts.PARAM_SHOW);
			final File file = this.receivedFiles.get(fieldName);
			if (file != null) {
				response.setContentType(this.receivedContentTypes.get(fieldName));
				final FileInputStream is = new FileInputStream(file);
				copyFromInputStreamToOutputStream(is, response.getOutputStream());
			} else
				super.getUploadedFile(request, response);
			LOG.info("Archivo procesado");
		} catch (Exception exception) {
			LOG.error(exception);
		}
	}

	@Override
	public final void removeItem(final HttpServletRequest request, final String fieldName) throws UploadActionException {
		try {
			final File file = this.receivedFiles.get(fieldName);
			this.receivedFiles.remove(fieldName);
			this.receivedContentTypes.remove(fieldName);
			if (file != null)
				file.delete();
		} catch (Exception exception) {
			LOG.error(exception);
		}
	}

	@Override
	public final void init() throws ServletException {
		super.init();
	}

	@Override
	public final void checkRequest(final HttpServletRequest request) {
		try {
			LOG.info("Se validará el tamaño del archivo");
			final Integer maxUploadFileSize = Integer
					.parseInt(this.configuration.findByName(ConfigurationEnum.MAX_FILEUPLOAD_SIZE.toString()));
			maxSize = maxUploadFileSize * this.bytesFactor * this.bytesFactor;
			maxFileSize = maxUploadFileSize * this.bytesFactor * this.bytesFactor;
			UploadListener.setNoDataTimeout(600000); // 10min
			super.checkRequest(request);
		} catch (NumberFormatException | BusinessException exception) {
			LOG.error(exception);
			throw new RuntimeException(exception);
		} catch (RuntimeException run) {
			LOG.error(
					"ERROR -  TIEMPO DE EXPERA PARA CARGA DE ARCHIVO TERMINADO. :: " + run.getMessage() + " - " + run.getCause(),
					run);
			throw new RuntimeException(run);
		} catch (Exception exception) {
			LOG.error(exception);
			throw new RuntimeException(exception);
		}
	}

	@RequestMapping(value = UrlConstants.DOCUMENT_VALIDATE_DATE, method = RequestMethod.POST)
	@ResponseBody
	public final Integer valdateDate(@RequestBody final Clause clause, final HttpServletResponse response) {
		return configuration.isValid(clause);
	}
}