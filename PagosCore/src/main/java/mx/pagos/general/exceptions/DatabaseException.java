package mx.pagos.general.exceptions;

public class DatabaseException extends Exception {
	private static final long serialVersionUID = -836111939815749258L;

	public DatabaseException(final String message) {
		super(message);
	}
	
	public DatabaseException(final Throwable throwable) {
		super(throwable);
	}
	
	public DatabaseException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
}
