package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.RecordStatusEnum;

public class Subparagraph {
	private Integer idDeclarSubparByRequisition;
	private Integer idDeclarationsByRequisition;
	private Integer idRequisition;
    private Integer idDeclarationSubparagraph;
    private Integer idDeclaration;
    private String description;
    private String replacedDescription;
    private List<Integer> idPersonalitiesList = new ArrayList<>();
    private List<Integer> idDocumentTypesList = new ArrayList<>();
    private List<Integer> idFinancialEntitiesList = new ArrayList<>();
    private RecordStatusEnum status;
    private Integer idDocumentType;
    private Integer idFinancialEntity;
    private Integer idPersonality;
    private Integer subparagraphOrder;
    private Boolean isEdited = false;
    
    public final Integer getIdDeclarationSubparagraph() {
        return this.idDeclarationSubparagraph;
    }
    
    public final void setIdDeclarationSubparagraph(final Integer idDeclarationSubparagraphParameter) {
        this.idDeclarationSubparagraph = idDeclarationSubparagraphParameter;
    }
    
    public final Integer getIdDeclaration() {
        return this.idDeclaration;
    }
    
    public final void setIdDeclaration(final Integer idDeclarationParameter) {
        this.idDeclaration = idDeclarationParameter;
    }
    
    public final String getDescription() {
        return this.description;
    }
    
    public final void setDescription(final String descriptionParameter) {
        this.description = descriptionParameter;
    }
    
    public final String getReplacedDescription() {
        return this.replacedDescription;
    }

    public final void setReplacedDescription(final String replacedDescriptionParameter) {
        this.replacedDescription = replacedDescriptionParameter;
    }

    public final Integer getSubparagraphOrder() {
        return this.subparagraphOrder;
    }

    public final void setSubparagraphOrder(final Integer subparagraphOrderParameter) {
        this.subparagraphOrder = subparagraphOrderParameter;
    }

    public final List<Integer> getIdPersonalitiesList() {
        return this.idPersonalitiesList;
    }

    public final void setIdPersonalitiesList(final List<Integer> idPersonalitiesListParameter) {
        this.idPersonalitiesList = idPersonalitiesListParameter;
    }

    public final List<Integer> getIdDocumentTypesList() {
        return this.idDocumentTypesList;
    }

    public final void setIdDocumentTypesList(final List<Integer> idDocumentTypesListParameter) {
        this.idDocumentTypesList = idDocumentTypesListParameter;
    }

    public final List<Integer> getIdFinancialEntitiesList() {
        return this.idFinancialEntitiesList;
    }

    public final void setIdFinancialEntitiesList(final List<Integer> idFinancialEntitiesListParameter) {
        this.idFinancialEntitiesList = idFinancialEntitiesListParameter;
    }

    public final RecordStatusEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final RecordStatusEnum statusParameter) {
        this.status = statusParameter;
    }

	public final Integer getIdDeclarSubparByRequisition() {
		return this.idDeclarSubparByRequisition;
	}

	public final void setIdDeclarSubparByRequisition(final Integer idDeclarSubparByRequisitionParameter) {
		this.idDeclarSubparByRequisition = idDeclarSubparByRequisitionParameter;
	}

	public final Integer getIdRequisition() {
		return this.idRequisition;
	}

	public final void setIdRequisition(final Integer idRequisitionParameter) {
		this.idRequisition = idRequisitionParameter;
	}

	public final Integer getIdDocumentType() {
		return this.idDocumentType;
	}

	public final void setIdDocumentType(final Integer idDocumentTypeParameter) {
		this.idDocumentType = idDocumentTypeParameter;
	}

	public final Integer getIdFinancialEntity() {
		return this.idFinancialEntity;
	}

	public final void setIdFinancialEntity(final Integer idFinancialEntityParameter) {
		this.idFinancialEntity = idFinancialEntityParameter;
	}

	public final Integer getIdPersonality() {
		return this.idPersonality;
	}

	public final void setIdPersonality(final Integer idPersonalityParameter) {
		this.idPersonality = idPersonalityParameter;
	}

	public final Integer getIdDeclarationsByRequisition() {
		return this.idDeclarationsByRequisition;
	}

	public final void setIdDeclarationsByRequisition(final Integer idDeclarationsByRequisitionParameter) {
		this.idDeclarationsByRequisition = idDeclarationsByRequisitionParameter;
	}
	
    public final Boolean getIsEdited() {
        return this.isEdited;
    }

    public final void setIsEdited(final Boolean isEditedParameter) {
        this.isEdited = isEditedParameter;
    }
}
