package mx.pagos.admc.service.generals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.FinishedContractPurchasesBusiness;
import mx.pagos.admc.contracts.structures.FinishedContractPurchases;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FinishedContractPurchasesService {

    @Autowired
    private BinnacleBusiness binnacleBusiness;
    
    @Autowired
    private UserSession session;
    
    @Autowired
    private FinishedContractPurchasesBusiness finishedContractPurchasesBusiness;
	
	@RequestMapping (value = UrlConstants.FIND_FINISHED_CONTRACTS_PURCHASES_LIST, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<FinishedContractPurchases> findFinishedContractsPurchases(
			@RequestBody final FinishedContractPurchases vo, 
			final HttpServletRequest request, final HttpServletResponse  response) {
		final ConsultaList<FinishedContractPurchases> responseList = new ConsultaList<>();
		try {
			responseList.setList(this.finishedContractPurchasesBusiness.findFinishedContractsPurchases(vo));
	        this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
	           "Consulta de contratos finalizados en compras", this.session, LogCategoryEnum.QUERY));
			return responseList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
		    response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		    return null;
		}
	}
}
