package mx.engineer.utils.general;

import java.text.DecimalFormat;

public final class NumberToEnglishLetters {
	private static final String TEN = " ten";

	private static final String MILLION = " million ";

	private static final String BILLION = " billion ";
	
	private static final String PER_CENTAGE_TEXT = " percent ";

	private static final String[] TENS_NAMES = { "", TEN, " twenty", " thirty", " forty", " fifty", " sixty",
			" seventy", " eighty", " ninety", };

	private static final String[] NUMBER_NAMES = { "", " one", " two", " three", " four", " five", " six", " seven",
			" eight", " nine", TEN, " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen",
			" seventeen", " eighteen", " nineteen", };

	private NumberToEnglishLetters() {
	}

	private static String convertLessThanOneThousand(final int numberParameter) {
		String soFar;
		int number = numberParameter;
		final int hundred = 100;
		final int twenty = 20;
		if (number % hundred < twenty) {
			soFar = NUMBER_NAMES[number % hundred];
			number /= hundred;
		} else {
			final int ten = 10;
			soFar = NUMBER_NAMES[number % ten];
			number /= ten;

			soFar = TENS_NAMES[number % ten] + soFar;
			number /= ten;
		}
		return getNumberValue(number, soFar);
	}

	private static String getNumberValue(final int number, final String soFar) {
		if (number == 0)
			return soFar;
		return NUMBER_NAMES[number] + " hundred" + soFar;
	}

	public static String convert(final long number) {
		if (number == 0) {
			return "zero";
		}
		String snumber = Long.toString(number);

		final String mask = "000000000000";
		final DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);

		final int billions = Integer.parseInt(snumber.substring(0, 3));
		final int millions = Integer.parseInt(snumber.substring(3, 6));
		final int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
		final int thousands = Integer.parseInt(snumber.substring(9, 12));

		final String result = getThousand(thousands,
				getHundredThousands(hundredThousands, getMillions(millions, getBillions(billions))));
		return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ").trim();
	}
	
	public static String convertNumberToWord(final Long number) {
		if (number != null)
			return NumberToEnglishLetters.convert(number);
		return "";
	}
	
	public static String convertNumberToWord(final Double number) {
		
		String doubleToLetter = "";
		if (number != null) {
			final Long longPart = number.longValue();
			doubleToLetter = NumberToEnglishLetters.convert(longPart);
			doubleToLetter = createDecimalPartText(number, doubleToLetter);			
		}		
		return doubleToLetter;
	}
	
	private static String createDecimalPartText(final Double percentageToConvert, String doubleToLetter) {
		final int decimalPart = Integer.parseInt(percentageToConvert.toString().split("\\.")[1]);
		if (decimalPart > 0) {			
			doubleToLetter += " point " + NumberToEnglishLetters.convert(decimalPart);
		}
		return doubleToLetter;
	}
	
	public static String convertNumberToWord(final String number) {		
		try {			
			if(number.contains(".")) {
				return NumberToEnglishLetters.convertNumberToWord(Double.parseDouble(number));
			}else {
				return NumberToEnglishLetters.convertNumberToWord(Long.parseLong(number));				
			}
			
		}catch (NumberFormatException e) {
			return "";
		}
	}
	
	public static String convertPercentageToWord(final Double percentageToConvert) {
		String percentageToLetter = "";
		if (percentageToConvert != null) {
			final int intPart = percentageToConvert.intValue();
			percentageToLetter = NumberToEnglishLetters.convert(intPart);
			percentageToLetter = createDecimalPartText(percentageToConvert, percentageToLetter);
			percentageToLetter = percentageToLetter.concat(PER_CENTAGE_TEXT);
		}
		return percentageToLetter;
	}
	
	
	
	

	private static String getThousand(final int thousands, final String result) {
		final String tradThousand = convertLessThanOneThousand(thousands);
		return result + tradThousand;
	}

	private static String getHundredThousands(final int hundredThousands, final String result) {
		final String tradHundredThousands;
		switch (hundredThousands) {
		case 0:
			tradHundredThousands = "";
			break;
		case 1:
			tradHundredThousands = "one thousand ";
			break;
		default:
			tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " thousand ";
		}
		return result + tradHundredThousands;
	}

	private static String getMillions(final int millions, final String result) {
		final String tradMillions;
		switch (millions) {
		case 0:
			tradMillions = "";
			break;
		case 1:
			tradMillions = convertLessThanOneThousand(millions) + MILLION;
			break;
		default:
			tradMillions = convertLessThanOneThousand(millions) + MILLION;
		}
		return result + tradMillions;
	}

	private static String getBillions(final int billions) {
		final String tradBillions;
		switch (billions) {
		case 0:
			tradBillions = "";
			break;
		case 1:
			tradBillions = convertLessThanOneThousand(billions) + BILLION;
			break;
		default:
			tradBillions = convertLessThanOneThousand(billions) + BILLION;
		}
		return tradBillions;
	}

	public static void main(String args[]) {
		
		System.out.println(NumberToEnglishLetters.convertNumberToWord((long) 46900));
		System.out.println(NumberToEnglishLetters.convertNumberToWord(46900.15));
		System.out.println(NumberToEnglishLetters.convertNumberToWord("46900.15"));
		
		System.out.println(NumberToEnglishLetters.convertPercentageToWord(46900.15));
		 
	}
}
