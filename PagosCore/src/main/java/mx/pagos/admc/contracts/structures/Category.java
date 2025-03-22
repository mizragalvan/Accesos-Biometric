package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.RecordStatusEnum;

public class Category {
	private Integer idCategory;
	private String name;
	private RecordStatusEnum status;
	private Integer idCheckDocumentation;
	private List<Integer> idCheckDocumentationList = new ArrayList<>();
	private List<CheckDocument> categoryCheckDocumentList = new ArrayList<>();
	private Integer numberPage;
    private Integer totalRows;
	
	public final Integer getIdCategory() {
		return this.idCategory;
	}
	
	public final void setIdCategory(final Integer idCategoryParameter) {
		this.idCategory = idCategoryParameter;
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

    public final List<Integer> getIdCheckDocumentationList() {
        return this.idCheckDocumentationList;
    }

    public final void setIdCheckDocumentationList(final List<Integer> idCheckDocumentationListParameter) {
        this.idCheckDocumentationList = idCheckDocumentationListParameter;
    }

    public final List<CheckDocument> getCategoryCheckDocumentList() {
        return this.categoryCheckDocumentList;
    }

    public final void setCategoryCheckDocumentList(final List<CheckDocument> categoryCheckDocumentListParameter) {
        this.categoryCheckDocumentList = categoryCheckDocumentListParameter;
    }

    public final Integer getIdCheckDocumentation() {
        return this.idCheckDocumentation;
    }

    public final void setIdCheckDocumentation(final Integer idCheckDocumentationParameter) {
        this.idCheckDocumentation = idCheckDocumentationParameter;
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
