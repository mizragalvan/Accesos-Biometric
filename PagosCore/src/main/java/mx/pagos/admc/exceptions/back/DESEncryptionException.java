package mx.pagos.admc.exceptions.back;

public class DESEncryptionException extends Exception {
    private static final long serialVersionUID = 7860552243422600993L;
    
    public DESEncryptionException(final String message){
        super(message);
    }
    
    public DESEncryptionException(final Throwable throwable){
        super(throwable);
    }
    
    public DESEncryptionException(final String message, final Throwable throwable){
        super(message, throwable);
    }
}
