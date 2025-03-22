package mx.pagos.admc.contracts.structures;

import java.util.List;

import mx.pagos.admc.enums.DocumentTypeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;

public class DocumentType {
	private Integer idDocumentType;
	private String name;
	private String templatePath;
	private String templatePathNaturalPerson;
	private RecordStatusEnum status;
	private String actorActivo;
	private String actorPasivo;
	private List<Integer> idBaseClauseList;
	private DocumentTypeEnum documentTypeEnum;
	private String docType;
	private Boolean isNewTemplate;
	private Boolean isNewNaturalPersonTemplate;
	private Boolean isDiferentTemplateForPersonality;
	private Integer selectionLimit;
	private Boolean isHideClausesNumbers = false;
	private Integer numberPage;
	private Integer totalRows;

    public final Integer getIdDocumentType() {
		return this.idDocumentType;
	}

	public final void setIdDocumentType(final Integer idDocumentTypeParameter) {
		this.idDocumentType = idDocumentTypeParameter;
	}

    public final String getName() {
		return this.name;
	}

	public final void setName(final String nombreParameter) {
		this.name = nombreParameter;
	}

	public final String getTemplatePath() {
		return this.templatePath;
	}

	public final void setTemplatePath(final String templatePathParameter) {
		this.templatePath = templatePathParameter;
	}

	public final String getTemplatePathNaturalPerson() {
        return this.templatePathNaturalPerson;
    }

    public final void setTemplatePathNaturalPerson(final String templatePathNaturalPersonParameter) {
        this.templatePathNaturalPerson = templatePathNaturalPersonParameter;
    }

    public final RecordStatusEnum getStatus() {
		return this.status;
	}

	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}

    public final String getActorActivo() {
		return this.actorActivo;
	}

	public final void setActorActivo(final String actorActivoParameter) {
		this.actorActivo = actorActivoParameter;
	}

	public final String getActorPasivo() {
		return this.actorPasivo;
	}

	public final void setActorPasivo(final String actorPasivoParameter) {
		this.actorPasivo = actorPasivoParameter;
	}

	public final List<Integer> getIdBaseClauseList() {
        return this.idBaseClauseList;
    }

    public final void setIdBaseClauseList(final List<Integer> idBaseClauseListParameter) {
        this.idBaseClauseList = idBaseClauseListParameter;
    }

    public final DocumentTypeEnum getDocumentTypeEnum() {
        return this.documentTypeEnum;
    }

    public final void setDocumentTypeEnum(final DocumentTypeEnum documentTypeEnumParameter) {
        this.documentTypeEnum = documentTypeEnumParameter;
    }

    public final Boolean getIsNewTemplate() {
        return this.isNewTemplate;
    }

    public final void setIsNewTemplate(final Boolean isNewTemplateParameter) {
        this.isNewTemplate = isNewTemplateParameter;
    }
    
    public final Boolean getIsNewNaturalPersonTemplate() {
        return this.isNewNaturalPersonTemplate;
    }
    
    public final void setIsNewNaturalPersonTemplate(final boolean isNewNaturalPersonTemplateParameter) {
        this.isNewNaturalPersonTemplate = isNewNaturalPersonTemplateParameter;
    }

    public final Boolean getIsDiferentTemplateForPersonality() {
        return this.isDiferentTemplateForPersonality;
    }

    public final void setIsDiferentTemplateForPersonality(final Boolean isDiferentTemplateForPersonalityParamater) {
        this.isDiferentTemplateForPersonality = isDiferentTemplateForPersonalityParamater;
    }

    public final Integer getSelectionLimit() {
        return this.selectionLimit;
    }

    public final void setSelectionLimit(final Integer selectionLimitParameter) {
        this.selectionLimit = selectionLimitParameter;
    }

    public final Boolean getIsHideClausesNumbers() {
        return this.isHideClausesNumbers;
    }

    public final void setIsHideClausesNumbers(final Boolean isHideClausesNumbersParameter) {
        this.isHideClausesNumbers = isHideClausesNumbersParameter;
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

    public final void setTotalRows(final Integer totalRowsParameter) {
        this.totalRows = totalRowsParameter;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}
