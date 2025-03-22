package mx.pagos.admc.contracts.structures;

public class DocumentPersonalityFields {
    private Integer idDocumentType;
    private String fieldName;
    private Integer idPersonality;
    private Boolean isShowField;
    private String sectionName;
    
    public final Integer getIdDocumentType() {
        return this.idDocumentType;
    }
    
    public final void setIdDocumentType(final Integer idDocumentTypeParameter) {
        this.idDocumentType = idDocumentTypeParameter;
    }
    
    public final String getFieldName() {
        return this.fieldName;
    }
    
    public final void setFieldName(final String fieldNameParameter) {
        this.fieldName = fieldNameParameter;
    }
    
    public final Integer getIdPersonality() {
        return this.idPersonality;
    }
    
    public final void setIdPersonality(final Integer idPersonalityParameter) {
        this.idPersonality = idPersonalityParameter;
    }

    public final Boolean getIsShowField() {
        return this.isShowField;
    }

    public final void setIsShowField(final Boolean isShowFieldParameter) {
        this.isShowField = isShowFieldParameter;
    }

    public final String getSectionName() {
        return this.sectionName;
    }

    public final void setSectionName(final String sectionNameParameter) {
        this.sectionName = sectionNameParameter;
    }
}
