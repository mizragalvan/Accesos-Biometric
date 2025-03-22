package mx.pagos.admc.contracts.structures;

public class AlertDocumentType {
    private Integer idDocumentType;
    private String name;
    private Integer turn;
    
    public final Integer getIdDocumentType() {
        return this.idDocumentType;
    }
    
    public final void setIdDocumentType(final Integer idDocumentTypeParameter) {
        this.idDocumentType = idDocumentTypeParameter;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
    
    public final Integer getTurn() {
        return this.turn;
    }
    
    public final void setTurn(final Integer turnParameter) {
        this.turn = turnParameter;
    }
}
