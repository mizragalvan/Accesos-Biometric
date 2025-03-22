package mx.engineer.utils.general.exceptions;

public class SubparagraphException extends Exception {
    private static final long serialVersionUID = -7649776881217511851L;
    
    public SubparagraphException(final String message) {
        super(message);
    }
    
    public SubparagraphException(final Throwable throwable) {
        super(throwable);
    }
    
    public SubparagraphException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
