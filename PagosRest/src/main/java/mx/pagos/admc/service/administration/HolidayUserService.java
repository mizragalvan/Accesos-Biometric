package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.HolidayUserBusiness;
import mx.pagos.admc.contracts.structures.HolidayUser;
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
public class HolidayUserService {

    @Autowired
    private HolidayUserBusiness holidayUserBusiness;

    @RequestMapping(value = UrlConstants.SAVE_HOLIDAYS_USER, method = RequestMethod.POST)
    @ResponseBody
    public final void saveHolidaysUser(@RequestBody final ConsultaList<HolidayUser> holidayUserList, 
            final HttpServletResponse response) {
        try {
            this.holidayUserBusiness.saveHolidaysUser(holidayUserList.getList(), holidayUserList.getParam4());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }
    
    @RequestMapping(value = UrlConstants.FIND_HOLIDAYS_BY_USER, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<HolidayUser> findHolidaysByUser(@RequestBody final String idUser, 
            final HttpServletResponse response) {
        try {
            final ConsultaList<HolidayUser> holidaysUserList = new ConsultaList<HolidayUser>();
            holidaysUserList.setList(this.holidayUserBusiness.findByIdUser(Integer.valueOf(idUser)));
            return holidaysUserList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new ConsultaList<HolidayUser>();
    }
}
