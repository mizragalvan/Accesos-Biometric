package mx.pagos.admc.contracts.structures;

public class Log {
    private String className;
    private Exception exception;
    
    public final String getClassName() {
        return this.className;
    }
    
    public final void setClassName(final String classNameParameter) {
        this.className = classNameParameter;
    }

    public final Exception getException() {
        return this.exception;
    }

    public final void setException(final Exception exceptionParameter) {
        this.exception = exceptionParameter;
    }
}
