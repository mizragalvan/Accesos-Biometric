package mx.pagos.admc.enums;

public enum BailsClauseEnum {
    ADVANCE("Anticipo"),
    FULFILLMENT("Cumplimiento"),
    FIDELITY("Fidelidad"),
    CONTINGENCY("Contingencias laborales, Obrero patronal"),
    CIVILRESPONSABILITY("Seguro de Responsabilidad Civil"),
    HIDDEN_VICES("Vicios Ocultos");

    private String descripcion = "";

    BailsClauseEnum(final String descripcionParameter) {
        this.descripcion = descripcionParameter;
    }

    public String getDescription() {
        return this.descripcion;
    }
}
