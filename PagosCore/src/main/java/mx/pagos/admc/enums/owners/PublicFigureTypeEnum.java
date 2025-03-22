package mx.pagos.admc.enums.owners;

public enum PublicFigureTypeEnum {
    BROKER("Corredor"),
    NOTARY("Notario");
    
    private String description;
    
    PublicFigureTypeEnum(final String descriptionParameter) {
        this.description = descriptionParameter;
    }
    
    public String getDescription() {
        return this.description;
    }
}
