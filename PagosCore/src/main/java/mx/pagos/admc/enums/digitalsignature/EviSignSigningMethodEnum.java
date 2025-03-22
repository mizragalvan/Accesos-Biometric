package mx.pagos.admc.enums.digitalsignature;

public enum EviSignSigningMethodEnum {

	EMAIL_PIN("EmailPin"),
	WEB_CLICK("WebClick"),
	MOBILE_PIN("MobilePin"),
	CHALLENGE("Challenge ");

    private String value;

    EviSignSigningMethodEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
	
}
