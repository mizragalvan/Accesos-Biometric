package mx.pagos.admc.contracts.structures;

public class ComparationWord {
    private Boolean isWordResponse;
    private String fileName;
    
    public final Boolean getIsWordResponse() {
        return this.isWordResponse;
    }
    
    public final void setIsWordResponse(final Boolean isWordResponseParameter) {
        this.isWordResponse = isWordResponseParameter;
    }
    
    public final String getFileName() {
        return this.fileName;
    }
    
    public final void setFileName(final String fileNameParameter) {
        this.fileName = fileNameParameter;
    }
}
