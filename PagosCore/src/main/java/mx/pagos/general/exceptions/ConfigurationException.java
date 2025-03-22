package mx.pagos.general.exceptions;

public class ConfigurationException extends Exception {
    private static final long serialVersionUID = 1291571619454857568L;

    public ConfigurationException(final String message) {
        super(message);
    }
    
    public ConfigurationException(final Throwable throwable) {
        super(throwable);
    }
    
    public ConfigurationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
