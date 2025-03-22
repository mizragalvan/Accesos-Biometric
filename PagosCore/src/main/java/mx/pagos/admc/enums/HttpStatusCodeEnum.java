package mx.pagos.admc.enums;

public enum HttpStatusCodeEnum {
	
	CONTINUE(100, "Continue", "Continuar"),
    SWITCHING_PROTOCOL(101, "Switching Protocols", "Protocolos de conmutación"),
    PROCESSING(102, "Processing", "Tratamiento"),
    OK(200, "OK", "Satisfactorio"),
    CREATED(201, "Created", "Creado"),
    ACCEPTED(202, "Accepted", "Aceptado"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information", "Información no autorizada"),
    NO_CONTENT(204,  "No Content", "Sin contenido"),
    RESET_CONTENT(205, "Reset Content", "Contenido de reposición"),
    PARTIAL_CONTENT(206, "Partial Content", "Contenido parcial"),
    MULTI_STATUS(207, "Multi-Status (WebDAV; RFC 4918", "Multi-Estado (WebDAV; RFC 4918)"),
    ALREADY_REPORTED(208, "Already Reported (WebDAV; RFC 5842)", "Ya informado (WebDAV; RFC 5842)"),
    IM_USED(226, "IM Used (RFC 3229)", "En uso (RFC 3229)"),
    MULTIPLE_CHOICES(300, "Multiple Choices", "Múltiples opciones"),
    MOVED_PERMANENTLY(301, "Moved Permanently", "Movido permanentemente"),
    FOUND(302, "Found", "Recurso localizado"),
    SEE_OTHER(303, "See Other (since HTTP/1.1)", "Ver otros (desde HTTP / 1.1)"),
    NOT_MODIFIED(304, "Not Modified", "No modificado"),
    USE_PROXY(305, "Use Proxy (since HTTP/1.1)", "Uso de proxy (desde HTTP / 1.1)"),
    SWITCH_PROXY(306, "Switch Proxy", "Conmutador de autorización"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect (since HTTP/1.1)", "Redirección temporal (desde HTTP / 1.1)"),
    PERMANENT_REDIRECT(308, "Permanent Redirect (approved as experimental RFC)[12]", 
    		"Redirección permanente (aprobado como la RFC experimental) [12]"),
    BAD_REQUEST(400, "Bad Request", "Solicitud incorrecta"),
    UNAUTHORIZED(401, "Unauthorized", "No autorizado"),
    PAYMENT_REQUIRED(402, "Payment Required", "Pago requerido"),
    FORBIDDEN(403, "Forbidden", "Prohibido"),
    NOT_FOUND(404, "Not Found", "Recurso no localizado"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "Método no permitido"),
    NOT_ACCEPTABLE(406, "Not Acceptable", "Inaceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required", "Se requiere autenticación proxy"),
    REQUEST_TIMEOUT(408, "Request Timeout", "Tiempo de espera agotado"),
    CONFLICT(409, "Conflict", "Conflicto"),
    GONE(410, "Gone", "Pasado"),
    LENGTH_REQUIRED(411, "Length Required", "Longitud requerida"),
    PRECONDITION_FAILED(412, "Precondition Failed", "Condición previa falló"),
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large", "Entidad de solicitud demasiado grande"),
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long", "URI de solicitud demasiado larga"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type", "Tipo de archivo no soportado"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable", "Rango solicitado no satisfiable"),
    EXPECTATION_FAILED(417, "Expectation Failed", "Error de expectativa"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "Error interno del servidor"),
    NOT_IMPLEMENTED(501, "Not Implemented", "No se ha implementado"),
    BAD_GATEWAY(502, "Bad Gateway", "Puerta de enlace incorrecta"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", "Servicio no disponible"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout", "Tiempo de espera de puerta de enlace"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported", "Versión de HTTP no compatible"),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates (RFC 2295)", "Variante también negocia (RFC 2295)"),
    INSUFFICIENT_STORAGE(507, "Insufficient Storage (WebDAV; RFC 4918)", 
    		"Almacenamiento insuficiente (WebDAV; RFC 4918)"),
    LOOP_DETECTED(508, "Loop Detected (WebDAV; RFC 5842)", "Bucle detectado (WebDAV; RFC 5842)"),
    BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded (Apache bw/limited extension)", 
    		"Límite de ancho de banda excedido (Apache de peso corporal / extensión limitada)"),
    NOT_EXTEND(510, "Not Extended (RFC 2774)", "No extendida (RFC 2774)"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required (RFC 6585)", 
    		"La red requiere autenticación (RFC 6585)"),
    CONNECTION_TIMED_OUT(522, "Connection timed out", "Tiempo de conexión agotado"),
    PROXY_DECLINED_REQUEST(523, "Proxy Declined Request", "Proxy rechaza la solicitud"),
    TIMEOUT_OCCURRED(524, "A timeout occurred", "Se ha producido un tiempo de espera"),
    UNKNOW_STATUS_CODE(null, "Unknow Status Code", "Falló la conexión con el servidor");

    private Integer code;
    private String englishDescription;
    private String spanishDescription;

    HttpStatusCodeEnum(final Integer codeParameter, final String englishDescriptionParameter, 
    		final String spanishDescriptionParameter) {
        this.code = codeParameter;
        this.englishDescription = englishDescriptionParameter;
        this.spanishDescription = spanishDescriptionParameter;
    }

    public final Integer getCode() {
        return this.code;
    }

    public final String getEnglishDescription() {
        return this.englishDescription;
    }
    
    public final String getSpanishDescription() {
        return this.spanishDescription;
    }
}
