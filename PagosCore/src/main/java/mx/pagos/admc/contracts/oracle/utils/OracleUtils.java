package mx.pagos.admc.contracts.oracle.utils;
//package mx.contratos.admc.contracts.oracle.utils;
//
//import mx.contratos.admc.contracts.interfaces.DatabaseUtils;
//
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class OracleUtils implements DatabaseUtils {
//    private static final String COMMA_YYYY_MM_DD = ", 'yyyy-mm-dd')";
//    private static final String TO_DATE = "TO_DATE(";
//    private static final String COMMA = ", ";
//    private static final String ADD_MONTHS = "ADD_MONTHS(";
//    private static final String RIGHT_BRACES = ")";
//    private static final String CONCAT_OPERATOR = " || ";
//    private static final String ORDER_BY = " ORDER BY ";
//    private static final String AND = " AND ";
//
//    @Override
//    public final String buildDateDiffSSqlFunction(final String baseDate, final String comparationDate) {
//        return TO_DATE + comparationDate + ", 'yyyy-mm-dd') - TO_DATE(" + baseDate + COMMA_YYYY_MM_DD;
//    }
//
//    @Override
//    public final String buildDateAddYears(final String years, final String dateField) {
//        return ADD_MONTHS + dateField + COMMA + years + " * 12)";
//    }
//    
//    @Override
//    public final String buildDateAddMonths(final String months, final String dateField) {
//        return ADD_MONTHS + dateField + COMMA + months + RIGHT_BRACES;
//    }
//
//    @Override
//    public final String buildDateAddDays(final String days, final String dateField) {
//        return "(CAST(" + dateField + " AS DATE) + " + days + RIGHT_BRACES;
//    }
//
//    @Override
//    public final String buildPaginatedQuery(final String rowNumberedField, final String query,
//            final Integer pageNumber, final Integer itemsNumber) {
//        final Integer start = ((pageNumber - 1) * itemsNumber) + 1;
//        final Integer end = start + itemsNumber;
//        final StringBuilder paginatedQuery = new StringBuilder();
//        final String[] sectionedQuery = query.split(ORDER_BY);
//        final Boolean isQueryOrdered = sectionedQuery.length > 1;
//        final String orderlessQuery = sectionedQuery[0];
//        paginatedQuery.append("SELECT * FROM (SELECT CountedData.*, ROWNUM AS IND FROM (").append(orderlessQuery);
//        paginatedQuery.append(") CountedData) PaginatedData ");
//        paginatedQuery.append("WHERE IND >= ").append(start).append(AND).append("IND < ").append(end);
//        if (isQueryOrdered)
//            paginatedQuery.append(ORDER_BY).append(this.getOrderBYWithNoTable(sectionedQuery[1]));
//        return paginatedQuery.toString();
//    }
//    
//    private String getOrderBYWithNoTable(final String orderBySection) {
//        final String[] splitedOrderBy = orderBySection.split("\\.");
//        return splitedOrderBy[splitedOrderBy.length - 1];
//    }
//
//    @Override
//    public final String countTotalRows(final String query) {
//        final String orderlessQuery = query.split(ORDER_BY)[0];
//        final StringBuilder countRowsQuery = new StringBuilder();
//        countRowsQuery.append("SELECT COUNT(1) FROM (").append(orderlessQuery).append(") ResultTable");
//        return countRowsQuery.toString();
//    }
//
//    @Override
//    public final String buildConcat(final String... strings) {
//        final StringBuilder concatQuery = new StringBuilder();
//        for (String field : strings)
//            concatQuery.append(field).append(CONCAT_OPERATOR);
//        concatQuery.delete(concatQuery.lastIndexOf(CONCAT_OPERATOR), concatQuery.length());
//        return concatQuery.toString();
//    }
//
//    @Override
//    public final String arrayToTableFunc(final String array) {
//        final StringBuilder query = new StringBuilder();
//        query.append("SELECT * FROM (SELECT COLUMN_VALUE AS StrVal ");
//        query.append("FROM TABLE (ARRAYTOTABLE ((").append(array).append(")))) TabledArray");
//        return query.toString();
//    }
//
//    @Override
//    public final String converToShortDate(final String column) {
//        final StringBuilder builder = new StringBuilder();
//        builder.append(TO_DATE).append(column).append(COMMA_YYYY_MM_DD);
//        return builder.toString();
//    }
//
//    @Override
//    public final String getCurrentDate() {
//        final StringBuilder builder = new StringBuilder();
//        builder.append("SYSDATE");
//        return builder.toString();
//    }
//}
