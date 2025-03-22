package mx.pagos.admc.contracts.mysql.utils;
//package mx.contratos.admc.contracts.mysql.utils;
//
//import mx.contratos.admc.contracts.interfaces.DatabaseUtils;
//
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class MySqlUtils implements DatabaseUtils {
//    private static final String RIGHT_BRACES = ")";
//    private static final String INTERVAL = ", INTERVAL ";
//    private static final String DATE_ADD = "DATE_ADD(";
//    private static final String COMMA = ", ";
//
//    @Override
//    public final String buildDateDiffSSqlFunction(final String baseDate, final String comparationDate) {
//        return "DATEDIFF(" + baseDate + COMMA + comparationDate + RIGHT_BRACES;
//    }
//
//    @Override
//    public final String buildDateAddYears(final String years, final String dateField) {
//        return DATE_ADD + dateField + INTERVAL + years + " YEAR)";
//    }
//    
//    @Override
//    public final String buildDateAddMonths(final String months, final String dateField) {
//        return DATE_ADD + dateField + INTERVAL + months + " MONTH)";
//    }
//
//    @Override
//    public final String buildDateAddDays(final String days, final String dateField) {
//        return DATE_ADD + dateField + INTERVAL + days + " DAY)";
//    }
//
//    @Override
//    public final String buildPaginatedQuery(final String rowNumberedField, final String query,
//            final Integer pageNumber, final Integer itemsNumber) {
//        final Integer start = (pageNumber - 1) * itemsNumber;
//        final StringBuilder paginatedQuery = new StringBuilder();
//        paginatedQuery.append(query).append(" LIMIT ").append(start).append(COMMA);
//        paginatedQuery.append(itemsNumber.toString());
//        return pageNumber.toString();
//    }
//
//    @Override
//    public final String countTotalRows(final String query) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public final String buildConcat(final String... strings) {
//        final StringBuilder concatQuery = new StringBuilder();
//        concatQuery.append("CONCAT(");
//        for (String field : strings)
//            concatQuery.append(field).append(COMMA);
//        concatQuery.delete(concatQuery.lastIndexOf(COMMA), concatQuery.length());
//        concatQuery.append(RIGHT_BRACES);
//        return concatQuery.toString();
//    }
//
//    @Override
//    public final String arrayToTableFunc(final String array) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public final String converToShortDate(final String column) {
//        final StringBuilder builder = new StringBuilder();
//        builder.append("CAST(").append(column).append(" AS DATE) ");
//        return builder.toString();
//    }
//
//    @Override
//    public final String getCurrentDate() {
//        final StringBuilder builder = new StringBuilder();
//        builder.append("NOW()");
//        return builder.toString();
//    }
//}
