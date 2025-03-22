package mx.pagos.admc.contracts.constants;

public class DsMessagesConstants {
	
	public static final String ERROR_CODE = "ERROR";
	public static final String ERROR_MESSAGE = "Ha ocurrido un error en el servidor";
	
	public static final String ERROR_USER_INFORMATION = "Error al consultar/actualizar la información del usuario";
	public static final String ERROR_SAVE_UPDATE_CONTACT = "Error al consultar/actualizar el contacto del usuario";
	public static final String ERROR_DELETE_CONTACT = "Error al eliminar el contacto del usuario";
	public static final String ERROR_GET_CONTACT = "Error al consultar los contactos del usuario";
	public static final String ERROR_API_MIFIEL = "Error al consumir la API de MiFiel";
	public static final String ERROR_DATA_SIGNER_MIFIEL = "No se ha encontrado registro del firmante en MiFiel";
	public static final String ERROR_INSERT_DB = "Error al insertar en la Base de datos";
	public static final String ERROR_DELETE_DB = "Error al eliminar en la Base de datos";
	public static final String ERROR_SAVE_FILE = "Ha ocurrido un error al guardar el documento";
	public static final String ERROR_FILE_PATH = "Ha ocurrido un error al obtener la ruta para el guardado del documento";
	public static final String ERROR_DS_DOCUMENT_NOT_FOUND = "ERROR_DS_DOCUMENT_NOT_FOUND";
	public static final String ERROR_DS_DOCUMENT_WITHOUT_RECIPIENTS = "ERROR_DS_DOCUMENT_WITHOUT_RECIPIENTS";

	public static final String ERROR_ONLY_SIGNER = "El usuario no tiene datos para usar en la firma";
	
	public static final String SUCCESS_CODE = "SUCCESS";
	public static final String SUCCESS_MESSAGE = "Operación realizada exitosamente";
	public static final String NO_NEED_AUTHORIZATION_CODE = "NO_NEED_AUTHORIZATION";
	public static final String NEED_AUTHORIZATION_CODE = "NEED_AUTHORIZATION";
	public static final String UNAUTHORIZED_CODE = "UNAUTHORIZED_CODE";
	public static final String UNAUTHORIZED_MESSAGE = "Código de autorización no válido ";
	public static final String NEED_AUTHORIZATION_MESSAGE = "Se necesita autorización para acceder a este documento";
	public static final String EMPTY_STRING = "";
	public static final String EQUAL = "=";
	public static final String QUESTION = "?";
	public static final String AMPERSAND = "&";
	public static final String MSG_CANCELED_BY_USER_REQUEST = "Cancelado por solicitud del usuario";

	public static final String RECIPIENT_TYPE_FINANCIAL_ENTITY = "Acreditante";
	public static final String RECIPIENT_TYPE_SUPPLIER = "Acreditado";
	public static final String RECIPIENT_TYPE_LEGAL_REPRESENTATIVE = "Acreditado";

	public static final String DOCUMENT = "document";
	public static final String USER = "user";
	public static final String IS_VIEWER = "isViewer";
	
	public static final String EMAIL_LINE_BREAK = "<br>";
	public static final String EMAIL_BOLD_START = "<b>";
	public static final String EMAIL_BOLD_END = "</b>";

	public static final String EMAIL_DESCRIPTION_ONE = "Estimado/a ";
	
	public static final String EMAIL_DESCRIPTION_TWO = "Se solicita su valiosa participación para firmar un documento "
			+ "usando su e.firma (FIEL) correspondiente al RFC: ";
	public static final String EMAIL_DESCRIPTION_TWO_VIEWER = "Ha sido considerado para consultar un documento "
			+ "que será firmado.";

	public static final String EMAIL_CONTENT_START = "<div "
			+ "style=\"display: flex; justify-content: center; align-items: center;\">"
			+ "<a href=\"";
	public static final String EMAIL_CONTENT_END = "\""
			+ "style=\"background-color:#ac0006; border-radius:28px; "
			+ "cursor:pointer; color:#ffffff; font-size:18px; padding:10px 40px; text-decoration:none;\">"
			+ "Firmar documento"
			+ "</a>"
			+ "</div>";
	public static final String EMAIL_CONTENT_END_VIEWER = "\""
			+ "style=\"background-color:#ac0006; border-radius:28px; "
			+ "cursor:pointer; color:#ffffff; font-size:18px; padding:10px 40px; text-decoration:none;\">"
			+ "Consultar documento"
			+ "</a>"
			+ "</div>";
	
	public static final String EMAIL_SECRET_CODE = "Para ingresar necesitará el siguiente código: ";
	
	
	
}