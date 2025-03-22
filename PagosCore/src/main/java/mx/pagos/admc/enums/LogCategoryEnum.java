package mx.pagos.admc.enums;

public enum LogCategoryEnum {
	INSERT("INGRESA INFORMACIÓN"),
	QUERY("CONSULTA"),
	UPDATE("ACTUALIZA"),
	DELETE("ELIMINA"),
	SAVE("GUARDA INFORMACIÓN"),
	LOGIN("ACCESA SISTEMA"),
	LOGOUT("SALE SISTEMA"),
	DOWNLOAD("DESCARGA ARCHIVOS"),
	BLOCK("BLOQUEADO"), 
	TRY("INTENTO"),
	EXPORT("EXPORTACIÓN");
	
	private String logDescription;
	
	LogCategoryEnum(final String binnacleName) {
	    this.logDescription = binnacleName;
	}

    public String getLogDescription() {
        return this.logDescription;
    }
}
