package mx.pagos.admc.enums;

public enum GuaranteeEnum {
    GENERAL ("Generales"),
    PRE_SIGNING_CONTRACT ("Previo a la Firma del Contrato"),
    TO_SIGNING_CONTRACT ("A la Firma del Contrato"),
    TO_FIRST_PROVISION ("A la Primera Disposición"),
    TO_FOLLOWING_PROVISIONS ("A las Siguientes Disposiciones"),
    PROSPERITY_TO_FIRM ("Con Prosperidad a la Firma");
    
    private String descripcion = "";

    GuaranteeEnum(final String descripcionParameter) {
        this.descripcion = descripcionParameter;
    }

    public String getDescription() {
        return this.descripcion;
    }
}
