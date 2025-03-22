package mx.pagos.admc.service.owners.ownersflow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.owners.FinishedContractOwnersBusiness;
import mx.pagos.admc.contracts.structures.owners.FinishedContractOwners;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

@Controller
public class FinishedContractOwnersService {

	@Autowired
    private BinnacleBusiness binnacleBusiness;
    
    @Autowired
    private UserSession session;
    
    @Autowired
    private FinishedContractOwnersBusiness finishedContractOwnersBusiness;
	
	@RequestMapping (value = UrlConstants.FIND_FINISHED_CONTRACTS_OWNERS_LIST, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<FinishedContractOwners> findFinishedContractsOwners(
			@RequestBody final FinishedContractOwners vo, 
			final HttpServletRequest request, final HttpServletResponse  response) {
		final ConsultaList<FinishedContractOwners> responseList = new ConsultaList<>();
		try {
			responseList.setList(this.finishedContractOwnersBusiness.findFinishedContractsOwners(vo));
	        this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
	           "Consulta de contratos finalizados en empresarial", this.session, LogCategoryEnum.QUERY));
			return responseList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
		    response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		    return null;
		}
	}
}
