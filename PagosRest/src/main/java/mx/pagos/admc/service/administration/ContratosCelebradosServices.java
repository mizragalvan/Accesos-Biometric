package mx.pagos.admc.service.administration;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.ContratosCelebradosBusiness;
import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.structures.ContratosCelebrados;
import mx.pagos.admc.contracts.structures.FiltrosGrafica;
import mx.pagos.admc.service.generals.DocumentService;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.BusinessException;



@Controller
public class ContratosCelebradosServices {
	
	@Autowired
	private ContratosCelebradosBusiness celebradosBusiness;
	@Autowired
	private RequisitionBusiness requisitionBusiness;
	
	private static final Logger LOG = Logger.getLogger(DocumentService.class);
	
	@PostMapping(value =UrlConstants.GET_LIST_DEAL_END)
	@ResponseBody
	public final ConsultaList<ContratosCelebrados> obtnerListaContratosCelebrados(@RequestBody FiltrosGrafica params) throws BusinessException{
		return celebradosBusiness.obtnerListaContratosCelebrados(params);
	}
	@PostMapping(value =UrlConstants.GET_LIST_ALL_DEAL_END)
	@ResponseBody
	public final ConsultaList<ContratosCelebrados> obtenerTotalContratosCelebrados(@RequestBody FiltrosGrafica params) throws BusinessException{
		return celebradosBusiness.obtenerTotalContratosCelebrados(params);
	}

	@CrossOrigin(exposedHeaders = "Content-Disposition")
	 @RequestMapping(value=UrlConstants.DOWNLOAD_CONTRACT_ZIP, produces="application/zip")
	 public void zipFile(@RequestBody String path, HttpServletResponse response)throws BusinessException {

	     response.addHeader("Content-Disposition", "attachment; filename=\"Contrato_"+ (path !=null ? path : "") + ".zip\"");
	     List<Version> documents =requisitionBusiness.findDigitalizationDocuments(Integer.parseInt(path));

	     
	     ZipOutputStream zipOutputStream;
		try {
			zipOutputStream = new ZipOutputStream(response.getOutputStream());
			LOG.info(path);
	     ArrayList<File> files = new ArrayList<>(1);
	     documents.forEach(i -> files.add(new File(i.getDocumentPath())));

	     for (File file : files) {
	         zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
	         FileInputStream fileInputStream = new FileInputStream(file);
	         IOUtils.copy(fileInputStream, zipOutputStream);
	         fileInputStream.close();
	         zipOutputStream.closeEntry();
	     }   
	     
	     response.setStatus(HttpServletResponse.SC_OK);
	     zipOutputStream.close();
	     
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, "Archivo no encontrado");
		}
	 }
	 
	 
}
