package mx.pagos.admc.contracts.business.general;

import java.util.Date;
import java.util.List;

import mx.engineer.utils.date.DateUtils;
import mx.pagos.admc.contracts.business.HolidayBusiness;
import mx.pagos.general.exceptions.BusinessException;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AttentionDaysBusiness {
    
    @Autowired
    private HolidayBusiness holidayBusiness;

    public final Integer calculateWorkingDays(final Date startDateParameter, final Date endDateParameter)
            throws BusinessException {
        final LocalDate startDate = new LocalDate(startDateParameter);
        final LocalDate endDate = new LocalDate(endDateParameter);
        final List<Date> holidayDatesList = this.holidayBusiness.findAllDates();
        final Integer weekendDays = DateUtils.weekendDaysBetweenDates(startDate.toDate(), endDate.toDate());
        final Integer holidayDays =
                DateUtils.daysBetweenDatesFromList(startDate.toDate(), endDate.toDate(), holidayDatesList);
        final Integer naturalDays = Days.daysBetween(startDate, endDate).getDays();
        return naturalDays - weekendDays - holidayDays;
    }
}
