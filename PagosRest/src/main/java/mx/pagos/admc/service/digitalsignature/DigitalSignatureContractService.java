package mx.pagos.admc.service.digitalsignature;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.constants.DsMessagesConstants;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.admc.core.business.digitalsignature.DigitalSignatureContractBusiness;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.document.versioning.interfaces.Versionable;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Controller
public class DigitalSignatureContractService {
	@Autowired
	DigitalSignatureContractBusiness digitalSignatureContractBusiness;
	@Autowired
	private Versionable versionable;
	@Autowired
	private RequisitionBusiness requisitionBusiness;
	
	
//	@RequestMapping(value = UrlConstants.DS_GET_SIGNATURE_OPTION, method = RequestMethod.GET)
//	@ResponseBody
//	public BaseDS getSignatureOption(final HttpServletRequest request,
//			final HttpServletResponse response) throws BusinessException, DatabaseException {
//		try {
//			
//			return digitalSignatureContractBusiness.getSignatureOption();
//			
//		} catch (BusinessException businessException) {
//			businessException.printStackTrace();
//			return null;
//		}
//	}
	
	
//	@RequestMapping(value = UrlConstants.DS_GET_DOCUMENT_BY_ID_REQUISITION, method = RequestMethod.POST)
//	@ResponseBody
//	public DocumentDS getDocumentByIdRequisition(@RequestBody DocumentDS documentRequest,
//			final HttpServletResponse response)
//			throws DatabaseException, BusinessException {
//		try {
//
//			return digitalSignatureContractBusiness.getDocumentByIdRequisition(documentRequest);
//
//		} catch (DatabaseException databaseException) {
//			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
//		}
//	}
	

	@RequestMapping(value = UrlConstants.DS_VIEW_DOCUMENT_CONTRACT, method = RequestMethod.POST)
	@ResponseBody
	public void viewDocumentContract(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws BusinessException, InvalidFormatException {
		try {

			digitalSignatureContractBusiness.viewDocument(documentRequest, response);

//		} catch (BusinessException businessException) {
//			businessException.printStackTrace();
//			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
//			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
//		}
		} catch (Exception businessException) {
			businessException.printStackTrace();
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.DS_DOCUMENT_EXTENSION, method = RequestMethod.POST)
	@ResponseBody
	public DocumentDS documentDetailExtension(@RequestBody DocumentDS documentRequest,
			final HttpServletResponse response) throws Exception {
//		String rutaPath="";
//		List<VersionDTO> versions = this.versionable.findContractVersionDTO(documentRequest.getIdRequisition());
//		for (VersionDTO versionDto : versions) {
//			versionDto.setFileName(FilenameUtils.getName(versionDto.getDocumentPath()));
////				LOG.info("LA RUTA DE VERSION DEL DOCUMENTO ES " + versionDto.getFileName());
////				LOG.info("LA RUTA ES " + versionDto.getDocumentPath());
//			rutaPath=versionDto.getDocumentPath();
//		}

		File pdf=  new File(this.requisitionBusiness.findTemplate(documentRequest.getIdRequisition()));
		String docxBaseName = FilenameUtils.getBaseName(pdf.getName());
		
		String extension = pdf.getName().substring(pdf.getName().lastIndexOf(".docx") + 1);
		System.out.println("//////////////////////////////");
		System.out.println("--------------------------LA EXTENSION-----------------------------");
		System.out.println("LA EXTENSION ES :   "+extension);
		final DocumentDS listResponse = new DocumentDS();
		listResponse.setExtension(extension.toString());
		return listResponse;
	}
	
	
	
	@RequestMapping(value = UrlConstants.DS_GET_DOCUMENT_INFORMATION, method = RequestMethod.POST)
	@ResponseBody
	public DocumentDS getDocumentInformation(@RequestBody DocumentDS documentRequest,
			final HttpServletResponse response) throws Exception {
		try {

			return digitalSignatureContractBusiness.getDocumentInformation(documentRequest);

		} catch (BusinessException businessException) {
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}

}
