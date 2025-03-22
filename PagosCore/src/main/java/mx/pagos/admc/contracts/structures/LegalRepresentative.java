package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.RecordStatusEnum;

public class LegalRepresentative {
    private Integer idLegalRepresentative;
    private Integer idFinancialEntity;
    private String financialEntityName;
    private Integer idDga;
    private String dgaName;
    private String name;
    private List<Power> powerList = new ArrayList<>();
    private String signSendDate;
    private String signReturnDate;
    private Boolean isOriginalSignedContDelivered;
    private Boolean isCopySignedContractDelivered;
    private String signedContractSendDate;
    private RecordStatusEnum status;
    private List<FinancialEntity> financialEntitiesList = new ArrayList<>();
    private Integer numberPage;
    private Integer totalRows;

    public final Integer getIdLegalRepresentative() {
        return this.idLegalRepresentative;
    }

    public final void setIdLegalRepresentative(final Integer idLegalRepresentativeParameter) {
        this.idLegalRepresentative = idLegalRepresentativeParameter;
    }

    public final Integer getIdFinancialEntity() {
        return this.idFinancialEntity;
    }

    public final void setIdFinancialEntity(final Integer idFinancialEntityParameter) {
        this.idFinancialEntity = idFinancialEntityParameter;
    }

    public final Integer getIdDga() {
        return this.idDga;
    }

    public final void setIdDga(final Integer idDgaParameter) {
        this.idDga = idDgaParameter;
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

    public final List<Power> getPowerList() {
        return this.powerList;
    }

    public final void setPowerList(final List<Power> listPowerParameter) {
        this.powerList = listPowerParameter;
    }

    public final String getSignSendDate() {
        return this.signSendDate;
    }

    public final void setSignSendDate(final String signSendDateParameter) {
        this.signSendDate = signSendDateParameter;
    }

    public final String getSignReturnDate() {
        return this.signReturnDate;
    }

    public final void setSignReturnDate(final String signReturnDateParameter) {
        this.signReturnDate = signReturnDateParameter;
    }

    public final Boolean getIsOriginalSignedContDelivered() {
        return this.isOriginalSignedContDelivered;
    }

    public final void setIsOriginalSignedContDelivered(
            final Boolean isOriginalSignedContractDeliveredParameter) {
        this.isOriginalSignedContDelivered = isOriginalSignedContractDeliveredParameter;
    }

    public final Boolean getIsCopySignedContractDelivered() {
        return this.isCopySignedContractDelivered;
    }

    public final void setIsCopySignedContractDelivered(
            final Boolean isCopySignedContractDeliveredParameter) {
        this.isCopySignedContractDelivered = isCopySignedContractDeliveredParameter;
    }

    public final String getSignedContractSendDate() {
        return this.signedContractSendDate;
    }

    public final void setSignedContractSendDate(final String signedContractSendDateParameter) {
        this.signedContractSendDate = signedContractSendDateParameter;
    }

	public final String getFinancialEntityName() {
		return this.financialEntityName;
	}

	public final void setFinancialEntityName(final String financialEntityNameParameter) {
		this.financialEntityName = financialEntityNameParameter;
	}

	public final String getDgaName() {
		return this.dgaName;
	}

	public final void setDgaName(final String dgaNameParameter) {
		this.dgaName = dgaNameParameter;
	}

    public final List<FinancialEntity> getFinancialEntitiesList() {
        return this.financialEntitiesList;
    }

    public final void setFinancialEntitiesList(final List<FinancialEntity> financialEntitiesListParameter) {
        this.financialEntitiesList = financialEntitiesListParameter;
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
