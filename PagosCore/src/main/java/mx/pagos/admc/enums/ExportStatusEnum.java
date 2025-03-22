package mx.pagos.admc.enums;

public enum ExportStatusEnum {
    IN_PROGRESS("Progreso"),
    ERROR("Error"),
    COMPLETED("Completo"),
    STOP("Detenido");
    
    private String description; 

    ExportStatusEnum(final String descriptionParameter) {
        this.description = descriptionParameter;
    }

    public final String getDescription() {
        return this.description;
    }
}
