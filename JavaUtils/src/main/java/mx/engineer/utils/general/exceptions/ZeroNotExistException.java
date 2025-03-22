package mx.engineer.utils.general.exceptions;

public class ZeroNotExistException extends RomanNumeralCastException {

    private static final long serialVersionUID = 820865509766960211L;

    public ZeroNotExistException() {
        super("El Número Cero No Existe");
    }
}
