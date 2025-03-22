package mx.pagos.admc.contracts.structures.owners;

import mx.pagos.admc.enums.RecordStatusEnum;

public class OrganizationEntity {
    private Integer idOrganizationEntity;
    private String name;
    private RecordStatusEnum status;
    private Integer numberPage;
    private Integer totalRows;
    
    public final Integer getIdOrganizationEntity() {
        return this.idOrganizationEntity;
    }
    
    public final void setIdOrganizationEntity(final Integer idOrganizationEntityParameter) {
        this.idOrganizationEntity = idOrganizationEntityParameter;
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
