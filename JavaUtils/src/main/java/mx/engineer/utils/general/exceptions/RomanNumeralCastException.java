package mx.engineer.utils.general.exceptions;

public class RomanNumeralCastException extends Exception {
    private static final long serialVersionUID = 3632837673411089851L;

    public RomanNumeralCastException(final String message) {
        super(message);
    }
    
    public RomanNumeralCastException(final Throwable throwable) {
        super(throwable);
    }
    
}
