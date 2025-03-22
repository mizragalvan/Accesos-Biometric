package mx.pagos.admc.exceptions.back;

public class EncryptionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EncryptionException(Throwable t) {
		super(t);
	}
}
