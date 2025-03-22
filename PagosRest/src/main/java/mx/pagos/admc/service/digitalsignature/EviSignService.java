package mx.pagos.admc.service.digitalsignature;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.digitalsignature.EviSignBusiness;
import mx.pagos.admc.contracts.constants.DsMessagesConstants;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.util.shared.UrlConstants;

@Controller
public class EviSignService {
	
	@Autowired
	EviSignBusiness eviSignBus;

	@RequestMapping(value = UrlConstants.EVI_SEND_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public DocumentDS sendDocument(@RequestBody DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {
			
			return eviSignBus.eviSignSubmit(documentRequest);

		} catch (Exception Exception) {
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}

}
