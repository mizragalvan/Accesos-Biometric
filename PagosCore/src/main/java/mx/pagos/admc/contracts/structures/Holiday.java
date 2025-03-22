package mx.pagos.admc.contracts.structures;

public class Holiday {
    private String date;
    private String description;
    
    public final String getDate() {
        return this.date;
    }
    
    public final void setDate(final String dateParameter) {
        this.date = dateParameter;
    }

    public final String getDescription() {
        return this.description;
    }
    
    public final void setDescription(final String descriptionParameter) {
        this.description = descriptionParameter;
    }
}
