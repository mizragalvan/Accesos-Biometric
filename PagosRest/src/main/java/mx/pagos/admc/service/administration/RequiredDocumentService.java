package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.RequiredDocumentBusiness;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

@Controller
public class RequiredDocumentService {

	@Autowired
	private RequiredDocumentBusiness requiredDocumentBusiness;

	@Autowired
	private UserSession session;

	@Autowired
	private BinnacleBusiness binnacleBusiness;

	@RequestMapping(value = UrlConstants.REQUIRED_DOCUMENT_SAVE_OR_UPDATE, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdate(@RequestBody final RequiredDocument requiredDocument, 
			final HttpServletResponse response) {
		Integer idRequiredDocument = 0;
		try {
			idRequiredDocument = this.requiredDocumentBusiness.saveOrUpdate(requiredDocument);
			if (requiredDocument.getIdRequiredDocument() != null)
				this.requiredDocumentBusiness.deletePersonalityRequiredDocument(
						requiredDocument.getIdRequiredDocument());
			this.requiredDocumentBusiness.savePersonalityRequiredDocument(
					requiredDocument.getIdPersonality(), idRequiredDocument);
			return idRequiredDocument;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}   
		return idRequiredDocument;
	}

	@RequestMapping(value = UrlConstants.REQUIRED_DOCUMENT_CHANGE_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final void changeCategoryStatus(@RequestBody final RequiredDocument requiredDocument, 
			final HttpServletResponse response) {
		try {
			this.requiredDocumentBusiness.changeRequiredDocumentStatus(
					requiredDocument.getIdRequiredDocument(), requiredDocument.getStatus());        
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
		}
	}

	@RequestMapping (value = UrlConstants.REQUIRED_DOCUMENT_FIND_ALL, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<RequiredDocument> findAll(final HttpServletResponse response) {
		final ConsultaList<RequiredDocument> requiredDocumentList = new ConsultaList<RequiredDocument>();
		try {
			requiredDocumentList.setList(this.requiredDocumentBusiness.findAll());
			return requiredDocumentList;            
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return requiredDocumentList;
	}

	@RequestMapping (value = UrlConstants.REQUIRED_DOCUMENT_FIND_BY_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<RequiredDocument> findByRecordStatus(@RequestBody final String status, 
			final HttpServletResponse response) {
		final ConsultaList<RequiredDocument> requiredDocumentList = new ConsultaList<RequiredDocument>();
		try {
			requiredDocumentList.setList(this.requiredDocumentBusiness.findByStatus(RecordStatusEnum.valueOf(status)));
			return requiredDocumentList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return requiredDocumentList;
	}

	@RequestMapping (value = UrlConstants.REQUIRED_DOCUMENT_FIND_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final RequiredDocument findById(@RequestBody final RequiredDocument requiredDocument, 
			final HttpServletResponse response) {
		try {
			return this.requiredDocumentBusiness.findByIdRequiredDocument(requiredDocument.getIdRequiredDocument());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return new RequiredDocument();
	}

	@RequestMapping (value = UrlConstants.REQUIRED_DOCUMENT_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<RequiredDocument> findAllRequiredDocumentCatalogPaged(
			@RequestBody final RequiredDocument requiredDocument, final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Consulta de todos los documentos requeridos", this.session, LogCategoryEnum.QUERY));
			final ConsultaList<RequiredDocument> requiredDocumentList = new ConsultaList<>();
			requiredDocumentList.setList(
					this.requiredDocumentBusiness.findRequiredDocumentCatalogPaged(requiredDocument));
			return requiredDocumentList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return new ConsultaList<RequiredDocument>();
	}

	@RequestMapping (value = UrlConstants.REQUIRED_DOCUMENT_TOTAL_PAGES, method = RequestMethod.POST)
	@ResponseBody
	public final RequiredDocument returnTotalRowsOfRequiredDocument(
			@RequestBody final RequiredDocument requiredDocument, final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Consulta del número de paginas de catálogo de documentos requeridos", 
					this.session, LogCategoryEnum.QUERY));
			return this.requiredDocumentBusiness.returnTotalPagesShowRequiredDocument(requiredDocument);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return new RequiredDocument();
	}
}