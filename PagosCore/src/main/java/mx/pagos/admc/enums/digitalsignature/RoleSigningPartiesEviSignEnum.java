package mx.pagos.admc.enums.digitalsignature;

public enum RoleSigningPartiesEviSignEnum {
	
	SIGNER("Signer"),
	REVIEWER("Reviewer");

    private String value;

    RoleSigningPartiesEviSignEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
    
}
