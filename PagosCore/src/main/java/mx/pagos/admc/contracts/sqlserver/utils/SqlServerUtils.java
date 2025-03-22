package mx.pagos.admc.contracts.sqlserver.utils;

import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.interfaces.DatabaseUtils;

@Repository
public class SqlServerUtils implements DatabaseUtils {
    private static final String ORDER_BY = "ORDER BY";
    private static final String RIGHT_BRACES = ")";
    private static final String COMMA = ", ";

    @Override
    public final String buildDateDiffSSqlFunction(final String baseDate, final String comparationDate) {
        return "DATEDIFF(DAY, " + baseDate + COMMA + comparationDate + RIGHT_BRACES;
    }

    @Override
    public final String buildDateAddYears(final String years, final String dateField) {
        return "DATEADD(YEAR, " + years + COMMA + dateField + RIGHT_BRACES;
    }
    
    @Override
    public final String buildDateAddMonths(final String months, final String dateField) {
        return "DATEADD(MONTH, " + months + COMMA + dateField + RIGHT_BRACES;
    }

    @Override
    public final String buildDateAddDays(final String days, final String dateField) {
        return "DATEADD(DAY, " + days + COMMA + dateField + RIGHT_BRACES;
    }
    
    @Override
    public final String buildPaginatedQuery(final String rowNumberedField, final String query,
            final Integer pageNumber, final Integer itemsNumber) {
        final Integer start = ((pageNumber - 1) * itemsNumber) + 1;
        final String[] sectionedQuery = query.split(ORDER_BY);
        final Boolean isQueryOrdered = sectionedQuery.length > 1;
        final String orderlessQuery = sectionedQuery[0];
        String[] orderByField = new String[]{};
        if (isQueryOrdered)
            orderByField = sectionedQuery[1].split("\\.");
        final StringBuilder paginatedQuery = new StringBuilder();
        paginatedQuery.append("SELECT TOP ").append(itemsNumber.toString()).append(" * ");
        paginatedQuery.append("FROM (SELECT ROW_NUMBER() OVER (ORDER BY ");
        paginatedQuery.append(isQueryOrdered ? orderByField[orderByField.length - 1] : rowNumberedField);
        paginatedQuery.append(") AS RowNumber, DataTable.* ");
        paginatedQuery.append("FROM (").append(orderlessQuery).append(") AS DataTable").append(") AS PaginatedData ");
        paginatedQuery.append("WHERE RowNumber >= ").append(start);
        
        return paginatedQuery.toString();
    }
    
    @Override
	public String buildPaginatedQuerySearch(String rowNumberedField, String query, Integer pageNumber,
			Integer itemsNumber, String search) {
    	 final Integer start = ((pageNumber - 1) * itemsNumber) + 1;
         final String[] sectionedQuery = query.split(ORDER_BY);
         final Boolean isQueryOrdered = sectionedQuery.length > 1;
         final String orderlessQuery = sectionedQuery[0];
         String[] orderByField = new String[]{};
         if (isQueryOrdered)
             orderByField = sectionedQuery[1].split("\\.");
         final StringBuilder paginatedQuery = new StringBuilder();
         paginatedQuery.append("SELECT TOP ").append(itemsNumber.toString()).append(" * ");
         paginatedQuery.append("FROM (SELECT ROW_NUMBER() OVER (ORDER BY ");
         paginatedQuery.append(isQueryOrdered ? orderByField[orderByField.length - 1] : rowNumberedField);
         paginatedQuery.append(") AS RowNumber, DataTable.* ");
         paginatedQuery.append("FROM (").append(orderlessQuery).append(") AS DataTable").append(") AS PaginatedData ");
         paginatedQuery.append("WHERE RowNumber >= ").append(start);
         if (search != null && search != "") {
           paginatedQuery.append(" AND IdRequisition LIKE '%").append(search).append("%'");
         }
         return paginatedQuery.toString();
	}
    
    @Override
    public final String countTotalRows(final String query) {
        final String orderlessQuery = query.split(ORDER_BY)[0];
        final StringBuilder countRowsQuery = new StringBuilder();
        countRowsQuery.append("SELECT COUNT(1) FROM (").append(orderlessQuery).append(") AS ResultTable");
        return countRowsQuery.toString();
    }
    
    @Override
	public String countTotalRowsSearch(String query, String search) {
      final String orderlessQuery = query.split(ORDER_BY)[0];
      final StringBuilder countRowsQuery = new StringBuilder();
      countRowsQuery.append("SELECT COUNT(1) FROM (").append(orderlessQuery).append(") AS ResultTable");
      if (search != null && search != "") {
       countRowsQuery.append(" WHERE IdRequisition LIKE '%").append(search).append("%'");
      }
      return countRowsQuery.toString();
	}
    
    @Override
    public final String buildConcat(final String... strings) {
        final StringBuilder concatQuery = new StringBuilder();
        concatQuery.append("CONCAT(");
        for (String field : strings)
            concatQuery.append(field).append(COMMA);
        concatQuery.delete(concatQuery.lastIndexOf(COMMA), concatQuery.length());
        concatQuery.append(RIGHT_BRACES);
        return concatQuery.toString();
    }

    @Override
    public final String arrayToTableFunc(final String array) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT StrVal FROM ArrayToTable((").append(array).append("))");
        return query.toString();
    }

    @Override
    public final String converToShortDate(final String column) {
        final StringBuilder builder = new StringBuilder();
        builder.append("CAST(").append(column).append(" AS DATE) ");
        return builder.toString();
    }

    @Override
    public final String getCurrentDate() {
        final StringBuilder builder = new StringBuilder();
        builder.append("GETDATE()");
        return builder.toString();
    }
}
