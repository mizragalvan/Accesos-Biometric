package mx.pagos.admc.enums;

public enum ObligationStatusEnum {
    ACTIVE("Activa"),
    CANCELLED("Cancelada"),
    EXPIRED("Vencida"),
    CONCLUDED("Concluida");
    
    private String name;
    
    ObligationStatusEnum(final String nameParameter) {
        this.name = nameParameter;
    }
    
    public String getName() {
        return this.name;
    }
}
