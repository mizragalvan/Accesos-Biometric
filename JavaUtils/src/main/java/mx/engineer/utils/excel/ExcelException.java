package mx.engineer.utils.excel;

public class ExcelException extends Exception {
    private static final long serialVersionUID = -5916470119394829514L;
    
    public ExcelException(final String message) {
        super(message);
    }
    
    public ExcelException(final Throwable throwable) {
        super(throwable);
    }
    
    public ExcelException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
