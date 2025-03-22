package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.CatalogDocTypeBusiness;
import mx.pagos.admc.contracts.structures.CatDocumentType;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

@Controller
public class CatalogDocTypeService {

	
	@Autowired
	private CatalogDocTypeBusiness documentBusiness;
	
	@PostMapping(value = UrlConstants.DOCUMENT_TYPE_FIND_ALL)
	@ResponseBody
	public final ConsultaList<CatDocumentType> findAll(final HttpServletResponse response) {
		try {
			// LOG.debug("Se va a obtener la lista de áreas");
			final ConsultaList<CatDocumentType> listReturn = new ConsultaList<CatDocumentType>();
			listReturn.setList(this.documentBusiness.findAll());
			return listReturn;
		} catch (BusinessException businessException) {
			// LOG.error(businessException.getMessage(), businessException);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<CatDocumentType>();
	}

}
