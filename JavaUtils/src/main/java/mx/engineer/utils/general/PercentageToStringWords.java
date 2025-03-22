package mx.engineer.utils.general;

import org.apache.log4j.Logger;

import mx.engineer.utils.string.StringUtils;

public final class PercentageToStringWords {

	private static final Logger LOG = Logger.getLogger(PercentageToStringWords.class);
	private static final String PER_CENTAGE_TEXT = " por ciento";
	private static final String NOT_CAPTURED_FIELD = "CAMPO NO CAPTURADO";

	private PercentageToStringWords() {
	}

	public static String convertPercentageToWord(final Integer percentageToConvert) {
		try {
			return NumbersToLetters.letterConvert(percentageToConvert).concat(PER_CENTAGE_TEXT);
		}catch (NullPointerException n) {
			LOG.error("ERRROR :: convertPercentageToWord(Integer)-> "+percentageToConvert, n );
			return NOT_CAPTURED_FIELD;
		}
	}

	public static String convertPercentageToWord(final Double percentageToConvert) {
		String percentageToLetter = "";		
		try {
			final int intPart = percentageToConvert.intValue();
			percentageToLetter = NumbersToLetters.letterConvert(intPart);
			percentageToLetter = createDecimalPartText(percentageToConvert, percentageToLetter);
			percentageToLetter = percentageToLetter.concat(PER_CENTAGE_TEXT);
			return percentageToLetter;
		}catch (NullPointerException n) {
			LOG.error("ERRROR :: convertPercentageToWord(Double)-> "+percentageToConvert, n);
			return NOT_CAPTURED_FIELD;
		}
	}

	private static String createDecimalPartText(final Double percentageToConvert, String percentageToLetter) {
		final int decimalPart = Integer.parseInt(percentageToConvert.toString().split("\\.")[1]);
		if (decimalPart > 0) {
			percentageToLetter += " punto " + NumbersToLetters.letterConvert(decimalPart);
		}
		return percentageToLetter;
	}

	public static void main(String args[]) {

		System.out.println(PercentageToStringWords.convertPercentageToWord(15230));

	}
}
