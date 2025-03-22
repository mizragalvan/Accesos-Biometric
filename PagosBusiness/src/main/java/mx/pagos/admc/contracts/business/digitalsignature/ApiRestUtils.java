package mx.pagos.admc.contracts.business.digitalsignature;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import mx.pagos.admc.contracts.structures.digitalsignature.DocumentPscWorld;
import mx.pagos.admc.contracts.structures.digitalsignature.EviSignQuery;
import mx.pagos.admc.util.shared.Constants;

public class ApiRestUtils {

	private static final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private ApiRestUtils() {
	}

	public static String entityToString(final HttpEntity entity) throws Exception {
		try {
			return EntityUtils.toString(entity);
		} catch (final Exception e) {
			throw new Exception("Error converting Entity to String", e);
		}
	}

	public static String convertObjectToJson(final Object object) throws Exception {
		try {
			return objectWriter.writeValueAsString(object);
		} catch (final Exception e) {
			throw new Exception("Error converting object to JSON", e);
		}
	}

	public static Object convertJsonToObject(final String json, final String className) throws Exception {
		try {
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return objectMapper.readValue(json, Class.forName(className));
		} catch (final Exception e) {
			throw new Exception("Error converting JSON to Object", e);
		}
	}

	public static List<Object> convertJsonToObjects(final String json, final String className) throws Exception {
		try {
			return objectMapper.readValue(json,
					objectMapper.getTypeFactory().constructCollectionType(List.class, Class.forName(className)));
		} catch (final Exception e) {
			throw new Exception("Error converting JSON to List<Object>", e);
		}
	}

	public static boolean isStringEmpty(String string) {
		return string == null || string.length() == 0;
	}

	public static String createRequestMethodGetEviSign(EviSignQuery eviSignQuery) throws Exception {
		try {

			URIBuilder uriBuilder = new URIBuilder(Constants.BLANK);
			Class<?> queryClass = EviSignQuery.class;

			Field[] fields = queryClass.getDeclaredFields();

			Arrays.stream(fields).forEach(field -> {
				try {
					field.setAccessible(true);
					Object value = field.get(eviSignQuery);
					if (value != null) {
						uriBuilder.setParameter(field.getName(), value.toString());
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			});

			URI requestUri = uriBuilder.build();
			return requestUri.toString();

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	

	public static String createRequestMethodGetPscWorld(DocumentPscWorld docPscWorldQuery) throws Exception {
		try {

			URIBuilder uriBuilder = new URIBuilder(Constants.BLANK);
			Class<?> queryClass = DocumentPscWorld.class;

			Field[] fields = queryClass.getDeclaredFields();

			Arrays.stream(fields).forEach(field -> {
				try {
					field.setAccessible(true);
					Object value = field.get(docPscWorldQuery);
					if (value != null) {
						uriBuilder.setParameter(field.getName(), value.toString());
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			});

			URI requestUri = uriBuilder.build();
			return requestUri.toString();

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	
	public static File createPdfFileFromBase64(String name, String base64String) {
		byte[] fileBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64String);
		
		try (ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes)) {
			File tempFile = File.createTempFile(name + Constants.UNDERSCORE_WITH_SPACES, Constants.PDF_FILE);
			
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = bais.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
			}
			
			return tempFile;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static File createAsn1FileFromBase64(String name, String base64String) {
		byte[] fileBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64String);

		try (ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes)) {
			File tempFile = File.createTempFile(name + Constants.UNDERSCORE_WITH_SPACES, Constants.ASN1_FILE);

			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = bais.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
			}

			return tempFile;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static File createZipArchive(String nameZipFile, File[] files) {
		File zipFile = new File(nameZipFile);

		try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
			for (File file : files) {
				addFileToZip(file, zipOutputStream, Constants.BLANK);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return zipFile;
	}

	private static void addFileToZip(File file, ZipOutputStream zipOutputStream, String pathInZip) throws IOException {

		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			ZipEntry zipEntry = new ZipEntry(pathInZip + file.getName());
			zipOutputStream.putNextEntry(zipEntry);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = fileInputStream.read(buffer)) > 0) {
				zipOutputStream.write(buffer, 0, length);
			}

			zipOutputStream.closeEntry();
		}
	}

}
