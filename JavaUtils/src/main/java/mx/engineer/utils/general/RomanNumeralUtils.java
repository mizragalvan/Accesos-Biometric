package mx.engineer.utils.general;

import java.util.LinkedList;

import mx.engineer.utils.general.exceptions.NonRepresentableNumberException;
import mx.engineer.utils.general.exceptions.RomanNumeralCastException;
import mx.engineer.utils.general.exceptions.ZeroNotExistException;

public final class RomanNumeralUtils {
    private static final Integer GREATEST_ROMAN_NUMBER = 3000;

    private RomanNumeralUtils() { }

    public static String arabicToRoman(final int arabicNumber) throws RomanNumeralCastException {
        try {
            validateCastableArabicNumber(arabicNumber);
            return castArabicToRoman(arabicNumber);
        } catch (ZeroNotExistException | NonRepresentableNumberException exception) {
            throw new RomanNumeralCastException(exception);
        }
    }

    private static String castArabicToRoman(final int arabicNumber) {
        final LinkedList<RomanNumeralRepresentation> numeralsRepresentationList =
                RomanNumeralRepresentation.getRepresentationsList();
        RomanNumeralRepresentation currentNumeralRepresentation = numeralsRepresentationList.removeFirst();
        int convertionArabicNumber = arabicNumber;
        final StringBuilder romanNumeral = new StringBuilder();
        while (convertionArabicNumber > 0) {
            currentNumeralRepresentation = getNexUsableRepresentation(numeralsRepresentationList,
                    currentNumeralRepresentation, convertionArabicNumber);
            romanNumeral.append(currentNumeralRepresentation.getRomanNumeral());
            convertionArabicNumber -= currentNumeralRepresentation.getArabicNumber();
        }
        return romanNumeral.toString();
    }

    private static RomanNumeralRepresentation getNexUsableRepresentation(
            final LinkedList<RomanNumeralRepresentation> numeralsRepresentation,
            final RomanNumeralRepresentation currentNumeralRepresentation, final int convertionArabicNumber) {
        RomanNumeralRepresentation nextNumeralRepresentation = currentNumeralRepresentation;
        while (convertionArabicNumber < nextNumeralRepresentation.getArabicNumber()) 
            nextNumeralRepresentation = numeralsRepresentation.removeFirst();
        return nextNumeralRepresentation;
    }

    private static void validateCastableArabicNumber(final int arabicNumber) 
            throws ZeroNotExistException, NonRepresentableNumberException {
        if (arabicNumber == 0)
            throw new ZeroNotExistException();
        if (arabicNumber > GREATEST_ROMAN_NUMBER)
            throw new NonRepresentableNumberException();
    }
    
    private static class RomanNumeralRepresentation {
        private static LinkedList<RomanNumeralRepresentation> romanNumeralsRepresentationList =
                createRepresentationsList();
        private Integer arabicNumber;
        private String romanNumeral;
        
        RomanNumeralRepresentation(final Integer arabicNumberParameter, final String romanNumeralParameter) {
            this.arabicNumber = arabicNumberParameter;
            this.romanNumeral = romanNumeralParameter;
        }

        public Integer getArabicNumber() {
            return this.arabicNumber;
        }

        public String getRomanNumeral() {
            return this.romanNumeral;
        }
        
        @SuppressWarnings("unchecked")
        public static LinkedList<RomanNumeralRepresentation> getRepresentationsList() {
            return (LinkedList<RomanNumeralRepresentation>) romanNumeralsRepresentationList.clone();
        }
        
        private static LinkedList<RomanNumeralRepresentation> createRepresentationsList() {
            romanNumeralsRepresentationList = new LinkedList<>();
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(1000, "M"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(900, "CM"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(500, "D"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(400, "CD"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(100, "C"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(90, "XC"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(50, "L"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(40, "XL"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(10, "X"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(9, "IX"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(5, "V"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(4, "IV"));
            romanNumeralsRepresentationList.add(new RomanNumeralRepresentation(1, "I"));
            return romanNumeralsRepresentationList;
        }
    }
    
    public static int romanToArabic(final String roman) {
        return (int) evaluateNextRomanNumeral(roman, roman.length() - 1, 0);
    }

    private static double evaluateNextRomanNumeral(final String roman, final int pos, final double rightNumeral) {
        if (pos < 0) return 0;
        final char ch = roman.charAt(pos);
        final double value =
                Math.floor(Math.pow(10, "IXCM".indexOf(ch))) + 5 * Math.floor(Math.pow(10, "VLD".indexOf(ch)));
        return value * Math.signum(value + 0.5 - rightNumeral) + evaluateNextRomanNumeral(roman, pos - 1, value);
    }
}
