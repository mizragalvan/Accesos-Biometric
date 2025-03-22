package mx.pagos.admc.enums;

public enum EviSignStatusEnum {

	STATE_SENT("Sent"),
	STATE_CLOSED("Closed"),
	STATE_PROCESSED("Processed"),
	STATE_SUBMITTED("Submitted"),
	STATE_UNKNOWN("Unknown"),
	
	OUTCOME_NONE("None"),
	OUTCOME_CANCELLED("Cancelled"),
	OUTCOME_SIGNED("Signed");

    private String value;

    EviSignStatusEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
    
    public static EviSignStatusEnum findByValue(String value) {
        for (EviSignStatusEnum status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No se encontró ninguna constante con valor: " + value);
    }
	
}
