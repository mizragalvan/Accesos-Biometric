package mx.pagos.admc.contracts.structures;

public class LogFile {
    private String fileName;
    private String path;
    
    public final String getFileName() {
        return this.fileName;
    }
    
    public final void setFileName(final String fileNameParameter) {
        this.fileName = fileNameParameter;
    }
    
    public final String getPath() {
        return this.path;
    }
    
    public final void setPath(final String pathParameter) {
        this.path = pathParameter;
    }
}
