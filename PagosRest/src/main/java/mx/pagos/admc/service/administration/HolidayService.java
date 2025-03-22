package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.HolidayBusiness;
import mx.pagos.admc.contracts.structures.Holiday;
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
public class HolidayService {

    @Autowired
    private HolidayBusiness holidayBusiness;

    @RequestMapping(value = UrlConstants.SAVE_HOLIDAYS, method = RequestMethod.POST)
    @ResponseBody
    public final void saveHolidays(@RequestBody final ConsultaList<Holiday> holidayList, 
            final HttpServletResponse response) {
        try {
            this.holidayBusiness.saveHolidays(holidayList.getList());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }

    @RequestMapping(value = UrlConstants.FIND_ALL_HOLIDAYS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Holiday> findAllHolidays(final HttpServletResponse response) {
        try {
            final ConsultaList<Holiday> holidaysList = new ConsultaList<Holiday>();
            holidaysList.setList(this.holidayBusiness.findAll());
            return holidaysList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new ConsultaList<Holiday>();
    }
}
