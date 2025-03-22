package mx.engineer.utils.general.exceptions;

public class NonRepresentableNumberException extends RomanNumeralCastException {

    private static final long serialVersionUID = -4134291073954905250L;

    public NonRepresentableNumberException() {
        super("Número no representable");
    }
}
