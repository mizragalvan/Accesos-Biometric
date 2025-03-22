package mx.pagos.general.constants;

public final class QueryConstants {

    public static final String TAG = ":";
    public static final String DOT = ".";
    public static final String SELECT = "SELECT ";
    public static final String SELECT_COUNT = "SELECT COUNT(";
    public static final String FROM = " FROM ";
    public static final String DELETE_FROM = " DELETE " + FROM;
    public static final String INSERT_INTO = " INSERT INTO ";
    public static final String VALUES = " VALUES (";
    public static final String VALUES_TAG = VALUES + TAG;
    public static final String WHERE = " WHERE ";
    public static final String SPACE = " ";
    public static final String LEFT_BRACES = " (";
    public static final String RIGHT_BRACES = ") ";
    public static final String COMMA = ", ";
    public static final String EQUAL_TAG = " = :";
    public static final String COMMA_TAG = ", :";
    public static final String UPDATE = " UPDATE ";
    public static final String SET = " SET ";
    public static final String LIKE = " LIKE ";
    public static final String LIKE_TAG = " LIKE :";
    public static final String OR = " OR ";
    public static final String ANY_CHARACTER = "%";
    public static final String UPPER = " UPPER(";
    public static final String UPPER_TAG = " UPPER(:";
    public static final String AND = " AND ";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String ON = " ON ";
    public static final String EQUAL = " = ";
    public static final String IS_NULL = " IS NULL ";
    public static final String SINGLE_QUOTE = "'";
    public static final String ASTERISK = " * ";
    public static final String GROUPBY = " GROUP BY ";
    public static final String DISTINCT = " DISTINCT ";
    public static final String EMPTY = "";
    public static final String DELETE = " DELETE ";
    public static final String ORDER_BY = " ORDER BY ";
    public static final String ASC = " ASC ";
    public static final String DESC = " DESC ";
    
    private QueryConstants() {
    }
}
