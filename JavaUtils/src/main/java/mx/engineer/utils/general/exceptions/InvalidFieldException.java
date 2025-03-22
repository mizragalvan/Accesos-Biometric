package mx.engineer.utils.general.exceptions;

public class InvalidFieldException extends Exception {
    private static final long serialVersionUID = -7649776881217511851L;
    
    public InvalidFieldException(final Throwable throwable) {
        super(throwable);
    }
}
