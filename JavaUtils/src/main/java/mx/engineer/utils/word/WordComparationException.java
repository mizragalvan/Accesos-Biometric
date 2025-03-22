package mx.engineer.utils.word;

public class WordComparationException extends Exception {
    private static final long serialVersionUID = -499382271468973208L;
    
    public WordComparationException(final Throwable throwable) {
        super(throwable);
    }
    
    public WordComparationException(final String cause) {
        super(cause);
    }
}
