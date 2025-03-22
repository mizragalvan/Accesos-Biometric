package mx.pagos.admc.enums;

public enum ManagerialDocumentTypeEnum {
     CONTRACT("Contrato"),
     GUARANTEE("Garantía"),
     DICTUM("Dictamen");
    
    private String description;
    
    ManagerialDocumentTypeEnum(final String descriptionParameter) {
        this.description = descriptionParameter;
    }
    
    public final String getDescription() {
       return this.description;
    }
}
