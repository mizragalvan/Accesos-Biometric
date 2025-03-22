package mx.pagos.admc.service.alerts;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.AlertsBusiness;
import mx.pagos.admc.contracts.business.FlowScreenActionBusiness;
import mx.pagos.admc.contracts.structures.Alert;
import mx.pagos.admc.contracts.structures.AlertDocumentType;
import mx.pagos.admc.contracts.structures.FlowScreenAction;
import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.filter.RequestSecurityFilter;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.security.structures.UserSession;

@Controller
public class AlertsService {

    @Autowired
    private FlowScreenActionBusiness flowScreenBuss;
    
    private static final Logger LOG = Logger.getLogger(RequestSecurityFilter.class);

    @Autowired
    private AlertsBusiness alertBuss;
    
    @Autowired
    private UserSession session;

    @GetMapping (value = "test.do")
    @ResponseBody
    public final Screen test() {
    	return new Screen();
    }
    
    
    @RequestMapping (value = UrlConstants.SEARCH_STATUSBYFLOW, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Screen> findStatusByFlow(@RequestBody final FlowScreenAction flujo,
            final HttpServletRequest request, final HttpServletResponse  response) {
        try {
            final ConsultaList<Screen> listReturn = new ConsultaList<Screen>();
            listReturn.setList(this.flowScreenBuss.findStatusNameByFlow(flujo.getIdFlow()));
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new ConsultaList<Screen>();
    }

    @RequestMapping (value = UrlConstants.FIND_ALERT_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Alert findById(@RequestBody final Integer idAlert, final HttpServletRequest request, 
            final HttpServletResponse  response) {
        try {
            return this.alertBuss.findById(idAlert);
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new Alert();
    }

    @RequestMapping (value = UrlConstants.SEARCH_ALERTSBYFLOWANDSTATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Alert> findAlertbyFlowStatus(@RequestBody final Alert alert,
            final HttpServletRequest request, final HttpServletResponse  response) {
        try {
            final ConsultaList<Alert> listReturn = new ConsultaList<Alert>();
            listReturn.setList(this.alertBuss.findbyFlowStatus(alert));
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new ConsultaList<Alert>();
    }

    @RequestMapping (value = UrlConstants.ALERTS_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Alert saveOrUpdate(@RequestBody final Alert alert, final HttpServletRequest request, 
            final HttpServletResponse  response) {
        try {
        	LOG.error("Se recibe guardado de alerta en - saveOrUpdate");
            alert.setIdAlert(this.alertBuss.saveOrUpdate(alert));
            return alert;
        } catch (BusinessException businessException) {
        	LOG.error("Falló guardado de alerta en - saveOrUpdate");
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new Alert();
    }

    @RequestMapping (value = UrlConstants.ALERTS_DELETE, method = RequestMethod.POST)
    @ResponseBody
    public final Alert deleteAlert(@RequestBody final Alert alert, final HttpServletRequest request, 
            final HttpServletResponse response) {
        try {
            this.alertBuss.deleteAlert(alert);
            return alert;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new Alert();
    }

    @RequestMapping (value = UrlConstants.FIND_ALERT_CONFLICTS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<AlertDocumentType> findAlertConflicts(@RequestBody final Alert alert, 
            final HttpServletResponse response) {
        final ConsultaList<AlertDocumentType> alertDocumentList = new ConsultaList<AlertDocumentType>();
        try {
            alertDocumentList.setList(this.alertBuss.findAlertConflicts(alert));
            return alertDocumentList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return alertDocumentList;
    }

    @RequestMapping (value = UrlConstants.SEND_NOTIFICATION_TO_MANAGER_SYSTEM, method = RequestMethod.POST)
    @ResponseBody
    public final void senNotificationToManagerSystem(@RequestBody final String notificationContent, 
            final HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            this.alertBuss.sendNotificationToManagerSystem(this.session.getUsuario().getIdUser(), notificationContent);
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }
    
//    @RequestMapping (value = "alerts/testSend.do", method = RequestMethod.GET)
//    public final void alertsTestSend() throws UnsupportedEncodingException {
//            try {
//                this.alertBuss.sendServiceLevelPurchasingAlerts();
//            } catch (BusinessException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//    }
}
