package mx.pagos.admc.service.digitalsignature;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.constants.DsMessagesConstants;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Documentable;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.InfoDocument;
import mx.pagos.admc.contracts.structures.digitalsignature.Recipient;
import mx.pagos.admc.core.business.digitalsignature.DocuSignBusiness;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
@Controller
public class DocuSignService {
	@Autowired
	DocuSignBusiness docuSignBusiness;

	@RequestMapping(value = UrlConstants.DS_SEND_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public DocumentDS sendDocument(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {

			return docuSignBusiness.sendDocument(documentRequest);

		} catch (BusinessException businessException) {
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	
	@RequestMapping(value = UrlConstants.DS_STATUS_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public InfoDocument statusDocument(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {

			return docuSignBusiness.statusDocuSignDocument(documentRequest);

		} catch (Exception businessException) {
			return new InfoDocument();
		}
	}
	
	@RequestMapping(value = UrlConstants.DS_STATUS_DIGITAL_SIGNATURE_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public void statusDigitalSignatureDocument(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {

			docuSignBusiness.updateStatusDocument(documentRequest.getIdRequisition(),documentRequest);

		} catch (Exception businessException) {
//			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	
	@RequestMapping(value = UrlConstants.DS_STATUS_RECIPIENT_SIGNED_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public void statusRecipienSignedDocument(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {

			docuSignBusiness.updateStatusRecipienSignedDocument(documentRequest);


		} catch (Exception businessException) {
//			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	@RequestMapping(value = UrlConstants.DS_DELETE_SIGNED_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public void deleteSignedDocument(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {

			docuSignBusiness.deleteRecipienSignedDocument(documentRequest);


		} catch (Exception businessException) {
//			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	

	@RequestMapping(value = UrlConstants.DS_POSITION_SIGNED_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public final String positionSignedDocument(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception{
		String reportName = docuSignBusiness.posicionFirma(documentRequest.getIdRequisition());
		return reportName != null ? "\"" +  reportName + "\"" : null;
	}
	
	
	@RequestMapping(value = UrlConstants.DS_REENVIAR_SIGNED_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public void reenvioEnvelope(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {

			docuSignBusiness.reenviarSignedDocument(documentRequest);


		} catch (Exception businessException) {
//			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	@RequestMapping(value = UrlConstants.DS_CORRECT_SIGNED_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public final DocumentDS correccionEnvelope(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {

		return	docuSignBusiness.correctSignedDocument(documentRequest);
		


		} catch (Exception businessException) {
//			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
		return null;
	}
	@RequestMapping(value = UrlConstants.DS_VIEW_SIGNED_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public final DocumentDS viewEnvelopeSigned(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {

		return	docuSignBusiness.viewSignedDocument(documentRequest);
		


		} catch (Exception businessException) {
//			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
		return null;
	}
}
