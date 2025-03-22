package mx.pagos.admc.service.generals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.LogBusiness;
import mx.pagos.admc.contracts.structures.Log;
import mx.pagos.admc.contracts.structures.LogFile;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

@Controller
public class LogService {
	private static final Logger LOG = Logger.getLogger(LogService.class);
	private static final String SCAPED_QUOTES = "\"";

	@Autowired
	private LogBusiness logBusiness;

	@RequestMapping(value = UrlConstants.LOG_SAVE_ERROR, method = RequestMethod.POST)
	public final void saveErrorLog(@RequestBody final Log logParameter, final HttpServletResponse response) {
		final Logger log = Logger.getLogger(logParameter.getClassName());
		log.error(logParameter.getException());
	}

	@RequestMapping (value = UrlConstants.GET_FOLDER_LOG_FILES, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<LogFile> getFolderLogFiles(final HttpServletResponse response) {
		try {
			LOG.debug("Se va a obtener la lista de archivos de log");
			final ConsultaList<LogFile> documentTypeList = new ConsultaList<>();
			documentTypeList.setList(this.logBusiness.getFolderLogFiles());
			return documentTypeList;            
		} catch (BusinessException businessException) {
			LOG.error(businessException.getMessage(), businessException);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return new ConsultaList<LogFile>();
	}

	@RequestMapping (value = UrlConstants.DELETE_OLDER_LOG_FILES_THAN_DATE, method = RequestMethod.POST)
	@ResponseBody
	public final void deleteOlderLogFilesThanDate(@RequestBody final String dateParam, 
			final HttpServletResponse response) {
		try {
			this.logBusiness.deleteOlderLogFilesThanDate(dateParam, "yyyy-MM-dd");
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
	}

	@RequestMapping (value = UrlConstants.DOWNLOAD_LOG_FILE, method = RequestMethod.GET)
	public final void downloadLogFile(final HttpServletRequest request,
			final HttpServletResponse response) {
		LOG.info("downloadLogFileOK");
		try {
			final String parameterDocumentPath = String.valueOf(request.getParameter("path"));
			LOG.info("downloadLogFileOK: " + parameterDocumentPath.replace("-", "\\"));
			this.sendFileToDownload(response, parameterDocumentPath.replace("-", "\\"));
		} catch (IOException businessException) {
			LOG.error(businessException.getMessage(), businessException);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
	}

	private void sendFileToDownload(final HttpServletResponse response, final String parameterDocumentPath)
			throws FileNotFoundException, IOException {
		LOG.info("sendFileToDownload: " + parameterDocumentPath);
		final File file = new File(parameterDocumentPath);
		this.setReponseData(response, file.getName(), file);
		final FileInputStream fileInputStream = new FileInputStream(file);
		final ServletOutputStream servletOutputStream = response.getOutputStream();
		IOUtils.copy(fileInputStream, servletOutputStream);      
		servletOutputStream.flush();
		servletOutputStream.close();
	}

	private void setReponseData(final HttpServletResponse response, final String name, final File file) {
		final int buffer = 1024 * 100;
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment;filename=" + SCAPED_QUOTES + name + SCAPED_QUOTES);
		response.setContentLength(Long.valueOf(file.length()).intValue());
		response.setBufferSize(buffer);
	}
}