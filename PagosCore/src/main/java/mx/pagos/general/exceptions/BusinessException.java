package mx.pagos.general.exceptions;

public class BusinessException extends Exception {
	private static final long serialVersionUID = 9165742448208535818L;

	public BusinessException(final String message) {
		super(message);
	}
	
	public BusinessException(final Throwable throwable) {
		super(throwable);
	}
	
	public BusinessException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
}
