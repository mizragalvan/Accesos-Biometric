package mx.pagos.admc.service.generals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.FlowsBusiness;
import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FlowService {
    private static final Logger LOG = Logger.getLogger(FlowService.class);
    
    @Autowired
    private FlowsBusiness flowBuss;

    @RequestMapping (value = UrlConstants.SEARCH_FLOWS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Flow> searchFlows(final HttpServletRequest request, final HttpServletResponse  response) {

        LOG.info("FlowService :: searchFlows");
        try {
            final ConsultaList<Flow> listReturn = new ConsultaList<Flow>();
            listReturn.setList(this.flowBuss.findAll());
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
            LOG.info(" BusinessException :: FlowService :: searchFlows");
            LOG.info(businessException.getMessage(), businessException);
        }
        return new ConsultaList<Flow>();
    }
    
    @RequestMapping (value = UrlConstants.FIND_FLOW_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Flow> findByStatus(
            final HttpServletRequest request, final HttpServletResponse  response) {
        try {
            final ConsultaList<Flow> listReturn = new ConsultaList<Flow>();
            listReturn.setList(this.flowBuss.findByRecordStatus(RecordStatusEnum.ACTIVE));
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new ConsultaList<Flow>();
    }
    
    @RequestMapping (value = UrlConstants.IS_MANAGERIAL_FLOW, method = RequestMethod.POST)
    @ResponseBody
    public final Boolean isManagerialFlow(@RequestBody final Integer idFlow, final HttpServletResponse  response) {
        try {
            return this.flowBuss.isManagerialFlow(idFlow);
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return false;
    }
    
    @RequestMapping (value = UrlConstants.FIND_PURCHASING_FLOWS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Flow> findPurchasingFlows(
            final HttpServletRequest request, final HttpServletResponse  response) {
        try {
            final ConsultaList<Flow> listReturn = new ConsultaList<Flow>();
            listReturn.setList(this.flowBuss.findPurchasingFlows());
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new ConsultaList<Flow>();
    }
}
