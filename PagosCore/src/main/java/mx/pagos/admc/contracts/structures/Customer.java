package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;

public class Customer {
    private Integer idCustomer;
    private String companyName;
    private String rfc;
    private RecordStatusEnum status;
    private Integer numberPage;
    private Integer totalRows;
    
    public final Integer getIdCustomer() {
        return this.idCustomer;
    }
    
    public final void setIdCustomer(final Integer idCustomerParameter) {
        this.idCustomer = idCustomerParameter;
    }
    
    public final String getCompanyName() {
        return this.companyName;
    }
    
    public final void setCompanyName(final String companyNameParameter) {
        this.companyName = companyNameParameter;
    }
    
    public final String getRfc() {
        return this.rfc;
    }
    
    public final void setRfc(final String rfcParameter) {
        this.rfc = rfcParameter;
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
