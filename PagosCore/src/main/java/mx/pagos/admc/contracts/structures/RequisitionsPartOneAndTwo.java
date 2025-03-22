package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.core.enums.ContratistaEnum;

public class RequisitionsPartOneAndTwo {
	private Integer idRequisition;
	private Integer idApplicant;
	private Integer idFlow;
	private Integer idCompany;
	private Integer updateRequisitionBy;
	private Integer idArea;
	private Integer idUnit;
	private String contractApplicant;
	private String contract;
	private CatDocumentType documentType;
	private Integer idDocumentType;
	private List<FinancialEntity> dataFinancialEntityList = new ArrayList<FinancialEntity>();
	private Integer idDocument;
	private String applicationDate;
	
	private ContratistaEnum contractType;
	public Integer getIdRequisition() {
		return idRequisition;
	}

	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}

	public Integer getIdApplicant() {
		return idApplicant;
	}

	public void setIdApplicant(Integer idApplicant) {
		this.idApplicant = idApplicant;
	}

	public Integer getIdFlow() {
		return idFlow;
	}

	public void setIdFlow(Integer idFlow) {
		this.idFlow = idFlow;
	}

	public Integer getUpdateRequisitionBy() {
		return updateRequisitionBy;
	}

	public void setUpdateRequisitionBy(Integer updateRequisitionBy) {
		this.updateRequisitionBy = updateRequisitionBy;
	}

	/**
	 * @return the idArea
	 */
	public Integer getIdArea() {
		return idArea;
	}

	/**
	 * @param idArea the idArea to set
	 */
	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}

	/**
	 * @return the idUnit
	 */
	public Integer getIdUnit() {
		return idUnit;
	}

	/**
	 * @param idUnit the idUnit to set
	 */
	public void setIdUnit(Integer idUnit) {
		this.idUnit = idUnit;
	}

	public String getContractApplicant() {
		return contractApplicant.toString();
	}

	/**
	 * @return the contractor
	 */
	public String getContract() {
		return contract;
	}

	/**
	 * @param contractor the contractor to set
	 */
	public void setContract(String contract) {
		this.contract = contract;
	}

	/**
	 * @return the documentType
	 */
	public Integer getIdDocument() {
		return idDocument;
	}

	/**
	 * @param documentType the documentType to set
	 */
	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}

	/**
	 * @return the idCompany
	 */
	public Integer getIdCompany() {
		return idCompany;
	}

	/**
	 * @param idCompany the idCompany to set
	 */
	public void setIdCompany(Integer idCompany) {
		this.idCompany = idCompany;
	}

	/**
	 * @param contractApplicant the contractApplicant to set
	 */
	public void setContractApplicant(String contractApplicant) {
		this.contractApplicant = contractApplicant;
	}

	/**
	 * @return the dataFinancialEntityList
	 */
	public List<FinancialEntity> getDataFinancialEntityList() {
		return dataFinancialEntityList;
	}

	/**
	 * @param dataFinancialEntityList the dataFinancialEntityList to set
	 */
	public void setDataFinancialEntityList(List<FinancialEntity> dataFinancialEntityList) {
		this.dataFinancialEntityList = dataFinancialEntityList;
	}
	
	/**
	 * @return the contratista
	 */
	public ContratistaEnum getContractType1() {
		return contractType;
	}

	/**
	 * @return the contratista
	 */
	public String getContractType() {
		if (contractType == null) {
			return null;
		} else {
			return contractType.toString();
		}
	}

	/**
	 * @param contratista the contratista to set
	 */
	public void setContractType(ContratistaEnum contractType) {
		this.contractType = contractType;
	}

	public void setContractType(String contractType) {
		if (null == contractType) {
			this.contractType = null;
		} else {
			this.contractType = ContratistaEnum.valueOf(contractType);
		}
	}

	/**
	 * @return the applicationDate
	 */
	public String getApplicationDate() {
		return applicationDate;
	}

	/**
	 * @param applicationDate the applicationDate to set
	 */
	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	/**
	 * @return the idDocumentType
	 */
	public Integer getIdDocumentType() {
		return idDocumentType;
	}

	/**
	 * @param idDocumentType the idDocumentType to set
	 */
	public void setIdDocumentType(Integer idDocumentType) {
		this.idDocumentType = idDocumentType;
	}

}
