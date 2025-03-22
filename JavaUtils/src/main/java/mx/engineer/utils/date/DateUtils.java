package mx.engineer.utils.date;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;


import static java.time.temporal.ChronoUnit.DAYS;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class DateUtils {
    public static Integer weekendDaysBetweenDates(final Date startDateParameter, final Date endDateParameter) {
        LocalDate weekday = new LocalDate(startDateParameter.getTime());
        final LocalDate endDate = new LocalDate(endDateParameter.getTime());
        Integer weekendDays = 0;
        while (isDateBeforeOrEqual(weekday, endDate)) {
            final int dayOfTheweek = weekday.getDayOfWeek();
            if (isWeekendDay(dayOfTheweek))
                weekendDays++;
            weekday = weekday.plusDays(1);
        }
        return weekendDays;
    }
    
    public static Integer daysBetweenDatesFromList(final Date startDateParameter, final Date endDateParameter,
            final List<Date> datesList) {
        final LocalDate startDate = new LocalDate(startDateParameter.getTime());
        final LocalDate endDate = new LocalDate(endDateParameter.getTime());
        Integer days = 0;
        for (Date dateItem : datesList) {
            final LocalDate date = new LocalDate(dateItem.getTime());
            if (isDateBetween(date, startDate, endDate))
                days++;
        }
        return days;
    }
    
    private static boolean isDateBetween(final LocalDate day, final LocalDate startDate, final LocalDate endDate) {
        return isDateAfterOrEqual(day, startDate) && isDateBeforeOrEqual(day, endDate);
    }

    private static boolean isDateBeforeOrEqual(final LocalDate day, final LocalDate endDate) {
        return day.isBefore(endDate) || day.isEqual(endDate);
    }
    
    private static boolean isDateAfterOrEqual(final LocalDate day, final LocalDate startDate) {
        return day.isAfter(startDate) || day.isEqual(startDate);
    }

    private static boolean isWeekendDay(final int dayOfTheweek) {
        return dayOfTheweek == DateTimeConstants.SATURDAY || dayOfTheweek == DateTimeConstants.SUNDAY;
    }
    
	/**
	 * Obtiene la diferencia de días entre 2 fechas
	 * 
	 * @param inicio Fecha inicial
	 * @param fin    Fecha fin
	 * @return Diferencia en días
	 */
	public static int daysBetweenTwoDates(Date inicio, Date fin) {
		Calendar in = Calendar.getInstance();
		in.setTime(inicio);
		Calendar fi = Calendar.getInstance();
		fi.setTime(fin);
		java.time.LocalDate i = java.time.LocalDate.of(in.get(Calendar.YEAR), in.get(Calendar.MONTH) + 1, in.get(Calendar.DAY_OF_MONTH));
		java.time.LocalDate f = java.time.LocalDate.of(fi.get(Calendar.YEAR), fi.get(Calendar.MONTH) + 1, fi.get(Calendar.DAY_OF_MONTH));
		long dias = DAYS.between(i, f);
		return (int) dias;
	}
	
	public static String dateToString(String stringFormat, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(stringFormat);
		String format = formatter.format(date);
		return format;
	}
	
	public static Date stringToDate(String stringFormat, String dateInString) {
		SimpleDateFormat formatter = new SimpleDateFormat(stringFormat);
		Date date = null;
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return date;
		
	}
	
	
	
	
}
