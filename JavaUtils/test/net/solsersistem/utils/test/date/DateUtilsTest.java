package mx.solsersistem.utils.test.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import mx.solsersistem.utils.date.DateUtils;

public class DateUtilsTest {
    private static final String DATE_FORMAT = "yyy-MM-dd";

    @Test
    public final void whenWeekendDaysBetweenDates1TheReturnCorrectNumberOfWeekDays() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final Date startDate = dateFormat.parse("2015-08-18");
        final Date endDate = dateFormat.parse("2015-08-25");
        Assert.assertEquals("Número incorrecto de fines días de fin de semana para 2015-08-18 a 2015-08-25",
                new Integer(2), DateUtils.weekendDaysBetweenDates(startDate, endDate));
    }
    
    @Test
    public final void whenWeekendDaysBetweenDates2TheReturnCorrectNumberOfWeekDays() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final Date startDate = dateFormat.parse("2015-08-17");
        final Date endDate = dateFormat.parse("2015-08-23");
        Assert.assertEquals("Número incorrecto de fines días de fin de semana para 2015-08-18 a 2015-08-23",
                new Integer(2), DateUtils.weekendDaysBetweenDates(startDate, endDate));
    }

    @Test
    public final void whenWeekendDaysBetweenDates3TheReturnCorrectNumberOfWeekDays() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final Date startDate = dateFormat.parse("2015-08-08");
        final Date endDate = dateFormat.parse("2015-08-09");
        Assert.assertEquals("Número incorrecto de fines días de fin de semana para 2015-08-08 a 2015-08-09",
                new Integer(2), DateUtils.weekendDaysBetweenDates(startDate, endDate));
    }
    
    @Test
    public final void whenWeekendDaysBetweenDates4TheReturnCorrectNumberOfWeekDays() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final Date startDate = dateFormat.parse("2015-07-27");
        final Date endDate = dateFormat.parse("2015-09-05");
        final Integer weekendDays = 11;
        Assert.assertEquals("Número incorrecto de fines días de fin de semana para 2015-07-27 a 2015-09-05",
                weekendDays, DateUtils.weekendDaysBetweenDates(startDate, endDate));
    }
    
    @Test
    public final void whenWeekendDaysBetweenDatesSameDayTheReturnCorrectNumberOfWeekDays() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final String date = "2015-08-01";
        final Date startDate = dateFormat.parse(date);
        final Date endDate = dateFormat.parse(date);
        Assert.assertEquals("Número incorrecto de fines días de fin de semana para 2015-08-01 a 2015-08-01",
                new Integer(1), DateUtils.weekendDaysBetweenDates(startDate, endDate));
    }
    
    @Test
    public final void whenWeekendDaysBetweenDatesOlderDayTheReturnCorrectNumberOfWeekDays() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final Date startDate = dateFormat.parse("2015-09-12");
        final Date endDate = dateFormat.parse("2015-09-07");
        Assert.assertEquals("Número incorrecto de fines días de fin de semana para 2015-08-15 a 2015-08-09",
                new Integer(0), DateUtils.weekendDaysBetweenDates(startDate, endDate));
    }
    
    @Test
    public final void whenDaysBetweenDatesFromListThenReturnCorectNumberOfsDays() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final Date startDate = dateFormat.parse("2015-09-20");
        final Date endDate = dateFormat.parse("2015-09-27");
        Assert.assertEquals("Conteo incorrecto de días de una lista entre dos fechas", new Integer(2),
                DateUtils.daysBetweenDatesFromList(startDate, endDate, this.createDatesList()));
    }
    
    private List<Date> createDatesList() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final List<Date> datesList = new ArrayList<>();
        datesList.add(dateFormat.parse("2015-09-14"));
        datesList.add(dateFormat.parse("2015-09-22"));
        datesList.add(dateFormat.parse("2015-09-23"));
        datesList.add(dateFormat.parse("2015-09-30"));
        return datesList;
    }
}
