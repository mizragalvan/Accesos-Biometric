package mx.pagos.admc.util.shared;

public abstract class Constants {

	//AdmContract Constants
	public static final String LOGIN_ACTIVE_DIRECTORY = "LOGIN_ACTIVE_DIRECTORY";
	public static final String LOGIN_SYSTEM = "LOGIN_SYSTEM";
	public static final String CONFIRM_ACCEPT = "accept";
	public static final String CONFIRM_CANCEL = "cancel";
	public static final String WHITE_SPACE = " ";
	public static final String SELECT = "Seleccionar";
	public static final String REQUISITION_DETAIL_TITLE = "Detalle de la Solicitud";
	public static final String CONTRACT_DETAIL_PURCHASES_TITLE = "Detalle de contrato en compras";
	public static final String CONTRACT_DETAIL_OWNERS_TITLE = "Detalle de contrato en empresarial";
	public static final Integer REQUISITION_DETAIL_WIDTH = 850;
	public static final Integer REQUISITION_DETAIL_HEIGHT = 650;
	public static final String IS_REQUISITION_VERSION_DETAIL = "isRequisitionVersionDetail";
	public static final String SELECT_FILE_LABEL = "ELIGE UN ARCHIVO ...";
	public static final String THREE_POINTS = "...";
	public static final String REQUISITION_OWNERS_FOLDER_NAME = "SolicitudesEmpresariales";
	public static final String DICTUM_DOCUMENT_LABEL = "dictumDocument";
	public static final String NORMAL_DETAIL = "NORMAL_DETAIL";
	public static final String VERSION_DETAIL = "VERSION_DETAIL";
	public static final String CONTRACT_DETAIL = "CONTRACT_DETAIL";
	public static final String  BLANK = "";
	public static final String  PASSWORD = "******";
	public static final String ASTERISK = "*";
	public static final String HIGH = "HIGH";
	public static final String LOW = "LOW";
	public static final String GENERAL_REPORT_PATH = "ReporteGeneralSolicitud";

	//Header Types
	public static final String HEADER_STATUS = "COD_STATUS";
	public static final String HEADER_MESSAGE = "MESSAGE";
	public static final String HEADER_SESSION = "SESSION_PARAM";
	public static final String HEADER_CODE = "HEADER_CODE";
	public static final String HEADER_NAVEGADOR = "user-agent";
	public static final String HEADER_CONTET = "Content-Type";
	public static final String HEADER_ACCEPT = "Accept";
	public static final String HEADER_CONTET_DISPOSITION = "Content-Disposition";

	public static final String HEADER_RESQUESTWITH_KEY = "x-requested-with";
	public static final String HEADER_TOKEN_AUT = "X-Auth-Token";
	public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	public static final String HEADER_AUTHORIZACION_KEY = "Authorization";	
	public static final String TOKEN_BEARER_PREFIX = "Bearer ";
	public static final String BASIC_PREFIX = "Basic ";
	public static final String TWO_POINTS = ":";
	public static final String TIME_TO_LIVE = "timeToLive";
	public static final String SIGNATURE_REQUEST_INFO_TEXT = "SignatureRequestInfoText";
	public static final String LANGUAGE = "language";
	public static final String SPANISH = "es";
	public static final int TIME_TO_LIVE_VALUE = 120;
	public static final int TWO_HUNDRED = 200;
	public static final int THREE_HUNDRED = 300;
	public static final int TIME_TO_LIVE_ONE_WEEK =  10080;
	public static final int MINUTES_PER_DAY = 1440;
	public static final String TRUE_STRING = "true";
	
	//Header "COD_STATUS" Types 
	public static final String COD_STATUS_OK = "OK";
	public static final String COD_STATUS_ERROR = "ERROR";
	public static final String COD_ERROR_NOT_FOUND_PSC = "CONSTANCIA_NO_ENCONTRADA";

	//Content Type
	public static final String CONTENT_TYPE = "application/json";

	//Configuration Services
	public static final String  PATH_TMP = "ArchivosTemporales";
	public static final String  PATH_DOCTYPE = "CatalogoTipoDocumentos";
	public static final String  PATH_GUARANTEE = "CatalogoGarantias";
	public static final String LOG = "logs";
	public static final String PATH_EXPORTED_CATALOGS = "ExportacionCatalogos";
    public static final String COD_STATUS_NOT_LOGGED = "NOT_LOGGED";
    
    //Dates
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT_DATABASE = "yyyy-MM-dd";
    
    public static final String CONTRASENA_VALIDA = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)([A-Za-z\\d$@$!%*?&+\\s]|[^ ]){8,30}$";
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String LOCALE_ES = "es";
    public static final String LOCALE_MX = "MX";
    public static final int CERO = 0;    
    public static final int ONE = 1;    
    public static final int FIFTY_NINE = 59;    
    public static final String TIME_ZONE_ISO = "Etc/GMT0";    
    public static final String SIGNER = "signer";
    public static final String FALSE = "false";
    public static final String TRUE = "true";
    public static final String CREATED = "created";
    
    public static final String ONE_STRING = "1"; 
    public static final String THREE_STRING = "3"; 
    public static final String FIFTEEN_STRING = "15";
    public static final String THIRTY_STRING = "30";
    
    // Digital signature
    //nuevas constantes para firma docusign
    public static final String  PATH_DIGITAL_SIGNATURE = "DigitalSignature";
    public static final String ROOT_PATH = "ROOT_PATH";
	public static final String TIMESTAMP_CODE = "yyyy.MM.dd.HH.mm.ss";
	public static final String FILE_STR = "file";
	public static final String DATA_STR = "data";
	public static final String HYPHEN = "-";
	public static final String UNDERSCORE_WITH_SPACES = " _ ";
	public static final String ZIP_FILE = ".zip";
	public static final String PDF_FILE = ".pdf";
	public static final String ASN1_FILE = ".asn1";
	public static final String ALGORITHM_SHA_256 = "SHA-256";
	public static final String STRING_PDF = "pdf";
	public static final String TEMP_FILE_EXTENSION = ".tmp";
	public static final String TEMP_FILE_NAME = "tempFile";
	public static final String STRING_CERTIFICATE = "_certificado";
	
	public static final String CONFIG_DS_MIFIEL_APP_ID = "DS_MIFIEL_APP_ID";
	public static final String CONFIG_DS_MIFIEL_APP_SECRET = "DS_MIFIEL_APP_SECRET";
	public static final String CONFIG_DS_MIFIEL_URL_API = "DS_MIFIEL_URL_API";
	public static final String CONFIG_DS_URL_APP_SIGN = "DS_URL_APP_SIGN";
	public static final String CONFIG_DS_DOC_CONVERTER_OPTION = "DS_DOC_CONVERTER_OPTION";
	public static final String CONFIG_DS_LIBRE_OFFICE_PATH = "DS_LIBRE_OFFICE_PATH";
	public static final String CONFIG_DS_SIGNATURE_OPTION = "DS_SIGNATURE_OPTION";
		
	public static final String CONFIG_DS_DOC_CONVERTER_MS_OFFICE = "MS_OFFICE";
	public static final String CONFIG_DS_DOC_CONVERTER_LIBRE_OFFICE = "LIBRE_OFFICE";
	
	public static final String CONFIG_DS_EVISIGN_USERNAME = "DS_EVISIGN_USERNAME";
	public static final String CONFIG_DS_EVISIGN_PASSWORD = "DS_EVISIGN_PASSWORD";
	public static final String CONFIG_DS_EVISIGN_URL_API = "DS_EVISIGN_URL_API";
	
	public static final String CONFIG_DS_PSC_WORLD_USERNAME = "DS_PSC_WORLD_USERNAME";
	public static final String CONFIG_DS_PSC_WORLD_PASSWORD = "DS_PSC_WORLD_PASSWORD";
	public static final String CONFIG_DS_PSC_WORLD_BEARER_TOKEN = "DS_PSC_BEARER_TOKEN";
	public static final String CONFIG_DS_PSC_WORLD_URL_API = "DS_PSC_WORLD_URL_API";

	public static final String CONFIG_DS_DOCUSIGN_URL_API = "DS_DOCUSIGN_URL_API";
	public static final String CONFIG_DS_DOCUSIGN_PATH_FILE_CONFIG = "DS_DOCUSIGN_PATH_FILE_CONFIG";
	public static final String CONFIG_DS_DOCUSIGN_OAUTH_BASE = "DS_DOCUSIGN_OAUTH_BASE";
	public static final String CONFIG_DS_DOCUSIGN_URL_ADD_FIELDS = "DS_DOCUSIGN_URL_ADD_FIELDS";
	public static final String CONFIG_DS_DOCUSIGN_SCOPE_SIGNATURE = "signature";
	public static final String CONFIG_DS_DOCUSIGN_SCOPE_IMPERSONATION = "impersonation";
	public static final String CONFIG_DS_DOCUSIGN_RSA_KEY_FILE = "rsaKeyFile";
	public static final String CONFIG_DS_DOCUSIGN_STRING_CLIENT_ID = "clientId";
	public static final String CONFIG_DS_DOCUSIGN_STRING_USER_ID = "userId";
	public static final String CONFIG_DS_DOCUSIGN_DOWNLOAD_CERTIFICATE = "certificate";
	public static final String CONFIG_DS_DOCUSIGN_DOWNLOAD_COMBINED = "combined";
	public static final String STRING_ADD_FIELDS = "/add-fields";
	public static final String DOCUSIGN = "_DOCUSIGN.zip";
	public static final String EVISIGN = "_EVISIGN.zip";
	
	
}
