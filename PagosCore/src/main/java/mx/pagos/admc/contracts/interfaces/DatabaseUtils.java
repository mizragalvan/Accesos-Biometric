package mx.pagos.admc.contracts.interfaces;

public interface DatabaseUtils {
	String buildDateDiffSSqlFunction(String baseDate, String comparationDate);
    
    String buildDateAddYears(String years, String dateField);
    
    String buildDateAddMonths(String months, String dateField);
    
    String buildDateAddDays(String days, String dateField);

    String buildPaginatedQuery(String rowNumberedField, String query, Integer pageNumber, final Integer itemsNumber);
    
    String buildPaginatedQuerySearch(String rowNumberedField, String query, Integer pageNumber, final Integer itemsNumber, final String search);
    
    String countTotalRowsSearch(String query, final String search);
    
    String countTotalRows(String query);

    String buildConcat(String... strings);
    
    String arrayToTableFunc(String array);
    
    String converToShortDate(String column);
    
    String getCurrentDate();

}
