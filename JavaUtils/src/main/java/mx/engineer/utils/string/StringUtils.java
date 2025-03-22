package mx.engineer.utils.string;

import java.text.Normalizer;
import org.apache.log4j.Logger;

public final class StringUtils {

	private static final Logger LOG = Logger.getLogger(StringUtils.class);

	private StringUtils() {
	}

	public static String getObjectStringValue(final Object object) {
		return object != null ? object.toString() : null;
	}

	public static String getObjectStringValueNotNull(final Object object) {
		return object != null ? object.toString() : "";
	}

	public static String getStringNotNull(final String value) {
		return value != null ? value : "";
	}

	public static String limpiaCadena(String cadena) {

//    	byte[] bytes = cadena.getBytes(StandardCharsets.UTF_8);    	 
//    	String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);    	 
//    	assertEquals(cadena, utf8EncodedString);

		LOG.info("&&&&&&&&&&&&&&&& Limpia nombre archivo &&&&&&&&&&&&&&&&");
		LOG.info("Doc_Name :: " + cadena);

		cadena = Normalizer.normalize(cadena, Normalizer.Form.NFD);
		cadena = cadena.replaceAll("[^\\p{ASCII}]", "");
		cadena = cadena.replaceAll("\\p{M}", "");
		cadena = cadena.replaceAll("[_ ]+", "_");
		cadena = cadena.replaceAll("[..]+", ".");
		cadena = cadena.replaceAll("[?¿¡!\"$%&/()=\\*~+\\-´,;:#]", "");
		LOG.info("Doc_Name_Limpio ::" + cadena);

		return cadena;
	}

	public static void main(String args[]) {
		String cad = "GERDAU_CORSA____1243s6!#$%&/()=*¡?¡MONTACARGAS_TRUCK_\"_Cesión_de_derechos_y_obligaciones_ñando_cigüeña.docx";
		System.out.println(cad);
		System.out.println(StringUtils.limpiaCadena(cad));
	}
	public static boolean isEmptyString(String string) {
		if (string == null) {
			return Boolean.TRUE;
		}
		if (string.trim().isEmpty() || "".equals(string.trim())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
