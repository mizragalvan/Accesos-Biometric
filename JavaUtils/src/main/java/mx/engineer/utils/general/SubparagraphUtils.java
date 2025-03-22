package mx.engineer.utils.general;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

import mx.engineer.utils.general.exceptions.SubparagraphException;

public final class SubparagraphUtils {
    private static final String INVALID_SUBPARAGRAPH = "Inciso no valido";
    private static final String PARENTHESIS = ") ";
    private static final int CHARACTERNUMBEROFALPHABET = 26;
    private static final int FOUR = 4;

    private SubparagraphUtils() {
    }

    private static void validNumberToConvert(final Integer numberToConvert) throws SubparagraphException {
        if (numberToConvert == null || numberToConvert <= 0)
            throw new SubparagraphException("Número no valido para un inciso");
    }
    
    public static String convertNumberToSubparagraph(final Integer numberToConvert) throws SubparagraphException  {
        try {
            validNumberToConvert(numberToConvert);
            Integer number = numberToConvert - 1;
            final StringBuilder subparagraph = new StringBuilder();
            subparagraph.append((char) ('a' + number % CHARACTERNUMBEROFALPHABET));
            while ((number /= CHARACTERNUMBEROFALPHABET) > 0)
                subparagraph.insert(0, (char) ('a' + --number % CHARACTERNUMBEROFALPHABET));
            return subparagraph.append(PARENTHESIS).toString();
        } catch (SubparagraphException subparagraphException) {
            throw new SubparagraphException(subparagraphException);
        }
    }
    
    public static String convertToUTF8(final String stringToConvert) throws UnsupportedEncodingException {
        return new String(stringToConvert.getBytes("ISO-8859-1"), "UTF-8");
    }
    
    public static int convertSubparagraphToInteger(final String subparagraphToConvert) throws SubparagraphException {
        int rtn = 0;
        for (final Character c : validSubparagraphToConvert(subparagraphToConvert).toCharArray()) {
            if (Character.isDigit(c))
                throw new SubparagraphException(INVALID_SUBPARAGRAPH);
            rtn = rtn * CHARACTERNUMBEROFALPHABET + (Character.toUpperCase(c) - '@');
        }
        return rtn;
    }
    
    private static String validSubparagraphToConvert(final String subparagraphToConvert) throws SubparagraphException {
        String stringToConvert = null;
        if (subparagraphToConvert.length() > FOUR)
            throw new SubparagraphException(INVALID_SUBPARAGRAPH);
        if (subparagraphToConvert.contains(PARENTHESIS))
            stringToConvert = subparagraphToConvert.replace(PARENTHESIS, "");
        else
            throw new SubparagraphException(INVALID_SUBPARAGRAPH);
        return stringToConvert;
    }
    
    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}
