package mx.engineer.utils.secutiry;

public class InvalidPasswordException extends Exception {
    private static final long serialVersionUID = -9014769073186227579L;

    public InvalidPasswordException(final String message) {
        super(message);
    }
    
    public InvalidPasswordException(final Throwable throwable) {
        super(throwable);
    }
    
    public InvalidPasswordException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
