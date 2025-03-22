package mx.pagos.security.exceptions;

public class BlockedUserException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public BlockedUserException(final String message) {
        super(message);
    }
    
    public BlockedUserException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
