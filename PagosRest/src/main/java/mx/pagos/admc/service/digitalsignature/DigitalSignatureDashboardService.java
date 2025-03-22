package mx.pagos.admc.service.digitalsignature;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.constants.DsMessagesConstants;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.core.business.digitalsignature.DigitalSignatureDashboardBusiness;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Controller
public class DigitalSignatureDashboardService {

	@Autowired
	DigitalSignatureDashboardBusiness digitalSignatureDashboardBusiness;
	
	
	@RequestMapping(value = UrlConstants.DS_VALIDATE_DRAFT_DOCUSIGN, method = RequestMethod.POST)
	@ResponseBody
	public DocumentDS validateDraftDocusign(@RequestBody DocumentDS documentRequest,
			final HttpServletResponse response) throws Exception {
		try {
			
			return digitalSignatureDashboardBusiness.validateDraftDocusign(documentRequest);
			
		} catch (DatabaseException databaseException) {
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	
	
	@RequestMapping(value = UrlConstants.DS_SEND_DOCUMENT_TO_PROVIDER, method = RequestMethod.POST)
	@ResponseBody
	public DocumentDS sendDocumentToProvider(@RequestBody DocumentDS documentRequest,
			final HttpServletResponse response) throws Exception {
		try {
			
			return digitalSignatureDashboardBusiness.sendDocumentToProvider(documentRequest);
			
		} catch (DatabaseException databaseException) {
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	
	
	@RequestMapping(value = UrlConstants.DS_VIEW_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public void viewDocument(@RequestBody DocumentDS documentDS, final HttpServletResponse response)
			throws Exception {
		try {
			
			digitalSignatureDashboardBusiness.viewDocument(documentDS, response);
			
		} catch (Exception businessException) {
			businessException.printStackTrace();
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}	
	
	
	@RequestMapping(value = UrlConstants.DS_GET_DOCUMENT_BY_ID_REQUISITION, method = RequestMethod.POST)
	@ResponseBody
	public DocumentDS getDocumentByIdRequisition(@RequestBody DocumentDS documentRequest,
			final HttpServletResponse response) throws Exception {
		try {
			
			return digitalSignatureDashboardBusiness.getDocumentByIdRequisition(documentRequest);
			
		} catch (DatabaseException databaseException) {
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	
	
	@RequestMapping(value = UrlConstants.DS_DELETE_DOCUMENT_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public DocumentDS deleteByIdDocument(@RequestBody DocumentDS documentRequest,
			final HttpServletResponse response) throws Exception {
		try {
			
			return digitalSignatureDashboardBusiness.deleteByIdDocument(documentRequest);
			
		} catch (DatabaseException databaseException) {
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	
	
	@RequestMapping(value = UrlConstants.DS_RESEND_INVITATION_EMAIL, method = RequestMethod.POST)
	@ResponseBody
	public DocumentDS resendInvitationEmail(@RequestBody DocumentDS documentDS, final HttpServletResponse response)
			throws Exception {
		try {
			
			return digitalSignatureDashboardBusiness.resendInvitationEmail(documentDS);
			
		} catch (Exception businessException) {
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	
	
	@RequestMapping(value = UrlConstants.DS_DOWNLOAD_SENT_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public void downloadSentDocument(@RequestBody DocumentDS documentDS, final HttpServletResponse response)
			throws Exception {
		try {
			
			digitalSignatureDashboardBusiness.downloadSentDocument(documentDS, response);
			
		} catch (Exception exception) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, exception.getMessage());
		}
	}	
	

	@RequestMapping(value = UrlConstants.DS_DOWNLOAD_SIGNED_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public void downloadSignedDocument(@RequestBody DocumentDS documentDS, final HttpServletResponse response)
			throws Exception {
		try {
			
			digitalSignatureDashboardBusiness.downloadSignedDocument(documentDS, response);
			
		} catch (Exception exception) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, exception.getMessage());
		}
	}	
	
    @RequestMapping(value = UrlConstants.DS_SAVE_SIGNED_DOCUMENT, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> saveSignedDocument(@RequestBody DocumentDS documentDS, final HttpServletResponse response) {
        try {
            digitalSignatureDashboardBusiness.saveSignedDocument(documentDS, response);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
	
	
}
