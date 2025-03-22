package mx.engineer.utils.general;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyUtils {
    
    private CurrencyUtils() {
    }

    public static String convertNumberToCurrencyFormat(final Double numberToConvert) {
        if (numberToConvert != null) 
            return stringConvertNumber(numberToConvert);
        return "";
    }
    
    public static String convertNumberToCurrencyFormat(final String numberToConvert) {
        if (numberToConvert != null) 
            return stringConvertNumber(Double.parseDouble(numberToConvert));
        return "";
    }
    
    private static String stringConvertNumber(final Double numberToConvert) {
        final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US); 
        return currencyFormat.format(numberToConvert);
    }
}
