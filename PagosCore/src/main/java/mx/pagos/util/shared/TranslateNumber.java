package mx.pagos.util.shared;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * @author Mizraim
 *
 */
public class TranslateNumber {

	private final static String[] UNITS = {"primera", "segunda", "tercera", "cuarta", "quinta", 
			                        "sexta", "séptima", "octava", "novena" };
	private final static String[] TENS = { "décima", "vigésimo", "trigésima", "cuadragésima", "quincuagésima",
			                               "sexagésima", "septuagésima", "octogésima", "nonagésima" };
	private final static String CENTENA = "Centésima ";;
	private final static String blank = "";
	
	public static String getString (int number) {
		
		if(number <= 0) {
			return blank;
		}
		
		if(number > 100) {
			return blank;
		}
		
		if(number == 100) {
			return CENTENA.trim();
		}
		
		return getOrdinalNumber(number);
	}
	
	private static String getOrdinalNumber(int number) {
		
		if(number%10==0) {
			int ten = (number/10);
			return getFirstLetter(TENS[ten-1].toString().trim());
		}
		
		if(number < 10) {
			return getFirstLetter(UNITS[number-1].toString().trim());
		}
		
		int tens = (number/10);
		int units = (number%10);
		
		return  getFirstLetter(TENS[tens-1].toString().trim()) + " " + UNITS[units-1].toString().trim();
	}
	
	private static String getFirstLetter(String first) {
		return first.substring(0,1).toUpperCase() + first.substring(1,first.length());
	}
	
	
	/*public static void main (String[] args) {
		int uno = 1; int dos = 42; int tres = 3; int cuatro = 49;
		int cinco = 55; int seis = 36; int siete = 50; int ocho = 100;
		
	}*/


}
