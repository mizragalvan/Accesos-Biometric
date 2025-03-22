package mx.pagos.security.exceptions;

public class InvalidCredentialsException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidCredentialsException(final String message) {
		super(message);
	}
}
