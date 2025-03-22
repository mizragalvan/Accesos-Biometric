package mx.engineer.utils.database;

import java.util.StringTokenizer;

public final class JdbcTemplateUtils {
    
    private static final String TAG = ":";

    private JdbcTemplateUtils() { }
    
     public static String buildUpdateFields(final String fieldsString, final String fieldsSeparator) {
         final StringBuilder updateAsignations = new StringBuilder();
         final StringTokenizer tokens = new StringTokenizer(fieldsString, fieldsSeparator);
         String currentField;
         while (tokens.hasMoreTokens()) {
             currentField = tokens.nextToken();
             updateAsignations.append(currentField).append(" = ").append(TAG).append(currentField);
             updateAsignations.append(addUpdateAsignationsSeparator(tokens));
         }
         return updateAsignations.toString();
     }
    
     private static String addUpdateAsignationsSeparator(final StringTokenizer tokens) {
         return tokens.hasMoreTokens() ? ", " : " ";
     }
     
     public static String tagFields(final String fieldsString, final String fieldsSeparator) {
         return TAG + fieldsString.replace(fieldsSeparator, fieldsSeparator + TAG);
     }
}
