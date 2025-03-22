package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;

public class Power {
	private Integer idPower;
	private Integer idFinancialEntity;
	private String financialName;
	private Integer idLegalRepresentative;
	private Integer idSupplierPerson;
	private Integer numberPage;
	private Integer totalRows;
	private String alternativePower;
	private RecordStatusEnum status;
	private String publicDeedNumber;
	private String publicNotaryName;
	private String publicNotaryNumber;
	private String publicNotaryState;
	private String publicCommercialFolio;
	private String publicCommercialFolioInscriptionDate;
	private String publicCommercialFolioInscriptionState;
	private String publicDeedDate;
	private String name;
	
	public final Integer getIdFinancialEntity() {
        return this.idFinancialEntity;
    }

    public final void setIdFinancialEntity(final Integer idFinancialEntityParameter) {
        this.idFinancialEntity = idFinancialEntityParameter;
    }

	public final Integer getIdPower() {
		return this.idPower;
	}

	public final void setIdPower(final Integer idPowerParameter) {
		this.idPower = idPowerParameter;
	}

	public final RecordStatusEnum getStatus() {
		return this.status;
	}

	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}

    public final String getFinancialName() {
        return this.financialName;
    }

    public final void setFinancialName(final String financialNameParameter) {
        this.financialName = financialNameParameter;
    }

    public final Integer getIdLegalRepresentative() {
        return this.idLegalRepresentative;
    }

    public final void setIdLegalRepresentative(final Integer idLegalRepresentativeParameter) {
        this.idLegalRepresentative = idLegalRepresentativeParameter;
    }

    public final Integer getIdSupplierPerson() {
        return this.idSupplierPerson;
    }

    public final void setIdSupplierPerson(final Integer idSupplierPersonParameter) {
        this.idSupplierPerson = idSupplierPersonParameter;
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

    public final String getAlternativePower() {
        return this.alternativePower;
    }

    public final void setAlternativePower(final String alternativePowerParameter) {
        this.alternativePower = alternativePowerParameter;
    }

    public final String getPublicDeedNumber() {
        return this.publicDeedNumber;
    }

    public final void setPublicDeedNumber(final String publicDeedNumberParameter) {
        this.publicDeedNumber = publicDeedNumberParameter;
    }

    public final String getPublicNotaryName() {
        return this.publicNotaryName;
    }

    public final void setPublicNotaryName(final String publicNotaryNameParameter) {
        this.publicNotaryName = publicNotaryNameParameter;
    }

    public final String getPublicNotaryNumber() {
        return this.publicNotaryNumber;
    }

    public final void setPublicNotaryNumber(final String publicNotaryNumberParameter) {
        this.publicNotaryNumber = publicNotaryNumberParameter;
    }

    public final String getPublicNotaryState() {
        return this.publicNotaryState;
    }

    public final void setPublicNotaryState(final String publicNotaryStateParameter) {
        this.publicNotaryState = publicNotaryStateParameter;
    }

    public final String getPublicCommercialFolio() {
        return this.publicCommercialFolio;
    }

    public final void setPublicCommercialFolio(final String publicCommercialFolioParameter) {
        this.publicCommercialFolio = publicCommercialFolioParameter;
    }

    public final String getPublicCommercialFolioInscriptionDate() {
        return this.publicCommercialFolioInscriptionDate;
    }

    public final void setPublicCommercialFolioInscriptionDate(
            final String publicCommercialFolioInscriptionDateParameter) {
        this.publicCommercialFolioInscriptionDate = publicCommercialFolioInscriptionDateParameter;
    }

    public final String getPublicCommercialFolioInscriptionState() {
        return this.publicCommercialFolioInscriptionState;
    }

    public final void setPublicCommercialFolioInscriptionState(
            final String publicCommercialFolioInscriptionStateParameter) {
        this.publicCommercialFolioInscriptionState = publicCommercialFolioInscriptionStateParameter;
    }

    public final String getPublicDeedDate() {
        return this.publicDeedDate;
    }

    public final void setPublicDeedDate(final String publicDeedDateParameter) {
        this.publicDeedDate = publicDeedDateParameter;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }
}
