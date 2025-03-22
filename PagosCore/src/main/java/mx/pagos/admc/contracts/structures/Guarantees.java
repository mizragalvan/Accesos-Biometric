package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.RecordStatusEnum;

public class Guarantees {
	private Integer idGuarantee;
	private String name;
	private String path;
	private Boolean isNewFile;
	private RecordStatusEnum status;
	private Integer idRequisitionOwners;
	private List<CheckDocument> checkDocumentList = new ArrayList<>();
	private List<Integer> idCheckDocumentList = new ArrayList<>();
	private Integer numberPage;
    private Integer totalRows;
	
	public final Integer getIdGuarantee() {
		return this.idGuarantee;
	}
	
	public final void setIdGuarantee(final Integer idGuarantessParameter) {
		this.idGuarantee = idGuarantessParameter;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final void setName(final String nombreParameter) {
		this.name = nombreParameter;
	}
	
	public final String getPath() {
		return this.path;
	}
	
	public final void setPath(final String inPutPathParameter) {
		this.path = inPutPathParameter;
	}
	
	public final RecordStatusEnum getStatus() {
		return this.status;
	}
	
	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}

    public final Boolean getIsNewFile() {
        return this.isNewFile;
    }

    public final void setIsNewFile(final Boolean isNewFileParameter) {
        this.isNewFile = isNewFileParameter;
    }

    public final List<CheckDocument> getCheckDocumentList() {
        return this.checkDocumentList;
    }

    public final void setCheckDocumentList(final List<CheckDocument> checkDocumentListParameter) {
        this.checkDocumentList = checkDocumentListParameter;
    }

    public final List<Integer> getIdCheckDocumentList() {
        return this.idCheckDocumentList;
    }

    public final void setIdCheckDocumentList(final List<Integer> idCheckDocumentListParameter) {
        this.idCheckDocumentList = idCheckDocumentListParameter;
    }

    public final Integer getIdRequisitionOwners() {
        return this.idRequisitionOwners;
    }

    public final void setIdRequisitionOwners(final Integer idRequisitionOwnerParameter) {
        this.idRequisitionOwners = idRequisitionOwnerParameter;
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
}
