package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.ScreensBusiness;
import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ScreenService {
    @Autowired
    private ScreensBusiness screensBusiness;
    
    @RequestMapping (value = UrlConstants.FIND_SCREEN_BY_STATUS , method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Screen> findByRecordStatus(@RequestBody final String status,
            final HttpServletResponse response) {
        try {
            final ConsultaList<Screen> listReturn = new ConsultaList<Screen>();
            listReturn.setList(this.screensBusiness.findByRecordStatus(RecordStatusEnum.valueOf(status)));
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Screen>();
    }

}
