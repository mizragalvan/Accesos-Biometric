package mx.pagos.admc.enums;

public enum DocuSignStatusEnum {
	
	CREATED("created"),
	COMPLETED("completed"),
	SENT("sent");

    private String value;

    DocuSignStatusEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
    
    public static DocuSignStatusEnum findByValue(String value) {
        for (DocuSignStatusEnum status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No se encontró ninguna constante con valor: " + value);
    }
	
	
}
