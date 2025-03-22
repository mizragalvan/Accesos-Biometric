package mx.pagos.admc.contracts.structures;

public class TagField {
    private String tag;
    private String field;
    private String tableName;
    
    public TagField() { }
    
    public TagField(final String tagParameter, final String fieldParameter) {
        this.tag = tagParameter;
        this.field = fieldParameter;
    }
    
    public final String getTag() {
        return this.tag;
    }
    
    public final void setTag(final String tagParameter) {
        this.tag = tagParameter;
    }
    
    public final String getField() {
        return this.field;
    }
    
    public final void setField(final String fieldParameter) {
        this.field = fieldParameter;
    }
    
    public final String getTableName() {
        return this.tableName;
    }
    
    public final void setTableName(final String tableNameParameter) {
        this.tableName = tableNameParameter;
    }
}
