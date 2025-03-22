package mx.pagos.general.exceptions;

public class EmptyResultException extends Exception {
    private static final long serialVersionUID = 6352026677448613441L;
    
    public EmptyResultException(final String message) {
        super(message);
    }    
    
    public EmptyResultException(final Throwable throwable) {
        super(throwable);
    }
}
