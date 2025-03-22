package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;
/**
 * 
 * @author Mizraim
 *
 */
public class RequiredDocument {
    private Integer idRequiredDocument;
    private String name;
    private RecordStatusEnum status;
    private Integer idDocument;
    private Integer idPersonality;
    private String personalityName;
    private Integer numberPage;
    private Boolean isRequired;
    private Integer totalRows;

    public final Integer getIdRequiredDocument() {
        return this.idRequiredDocument;
    }
    
    public final void setIdRequiredDocument(final Integer idDocumentParameter) {
        this.idRequiredDocument = idDocumentParameter;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
    
    public final RecordStatusEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final RecordStatusEnum statusParameter) {
        this.status = statusParameter;
    }

    public final Integer getIdDocument() {
        return this.idDocument;
    }

    public final void setIdDocument(final Integer idDocumentParameter) {
        this.idDocument = idDocumentParameter;
    }

    public final Integer getIdPersonality() {
        return this.idPersonality;
    }

    public final void setIdPersonality(final Integer idPersonalityParameter) {
        this.idPersonality = idPersonalityParameter;
    }

    public final String getPersonalityName() {
        return this.personalityName;
    }

    public final void setPersonalityName(final String personalityNameParameter) {
        this.personalityName = personalityNameParameter;
    }
    
    public final Integer getNumberPage() {
        return this.numberPage;
    }

    public final void setNumberPage(final Integer numberPageParameter) {
        this.numberPage = numberPageParameter;
    }

    public final Integer getTotalRows() {
        return this.totalRows;
    }

    public final void setTotalRows(final Integer numberRowsParameter) {
        this.totalRows = numberRowsParameter;
    }

	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}
}
