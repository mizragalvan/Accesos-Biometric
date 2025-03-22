/**
 * 
 */
package mx.pagos.admc.contracts.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.ValidityEnum;

/**
 * @author Mizraim
 *
 */
public class Instrument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -154060034412805011L;

	private Integer idRequisition;
	private ValidityEnum validity;
	private Boolean automaticRenewal;
	private Integer renewalPeriods;
	private String validityStartDate;
	private String validityEndDate;
	private String financialEntityAddress;
	private List<FinancialEntity> dataFinancialEntityList = new ArrayList<FinancialEntity>();
	private List<LegalRepresentative> legalRepresentativesList = new ArrayList<LegalRepresentative>();
	private String serviceDescription;
	private String technicalDetails;

	/**
	 * @return the validity
	 */
	public ValidityEnum getValidity() {
		return validity;
	}

	/**
	 * @param validity the validity to set
	 */
	public void setValidity(ValidityEnum validity) {
		this.validity = validity;
	}

	/**
	 * @return the automaticRenewal
	 */
	public Boolean getAutomaticRenewal() {
		return automaticRenewal;
	}

	/**
	 * @param automaticRenewal the automaticRenewal to set
	 */
	public void setAutomaticRenewal(Boolean automaticRenewal) {
		this.automaticRenewal = automaticRenewal;
	}

	/**
	 * @return the renewalPeriods
	 */
	public Integer getRenewalPeriods() {
		return renewalPeriods;
	}

	/**
	 * @param renewalPeriods the renewalPeriods to set
	 */
	public void setRenewalPeriods(Integer renewalPeriods) {
		this.renewalPeriods = renewalPeriods;
	}

	/**
	 * @return the validityStartDate
	 */
	public String getValidityStartDate() {
		return validityStartDate;
	}

	/**
	 * @param validityStartDate the validityStartDate to set
	 */
	public void setValidityStartDate(String validityStartDate) {
		this.validityStartDate = validityStartDate;
	}

	/**
	 * @return the validityEndDate
	 */
	public String getValidityEndDate() {
		return validityEndDate;
	}

	/**
	 * @param validityEndDate the validityEndDate to set
	 */
	public void setValidityEndDate(String validityEndDate) {
		this.validityEndDate = validityEndDate;
	}

	/**
	 * @return the financialEntityAddress
	 */
	public String getFinancialEntityAddress() {
		return financialEntityAddress;
	}

	/**
	 * @param financialEntityAddress the financialEntityAddress to set
	 */
	public void setFinancialEntityAddress(String financialEntityAddress) {
		this.financialEntityAddress = financialEntityAddress;
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
	 * @return the legalRepresentativesList
	 */
	public List<LegalRepresentative> getLegalRepresentativesList() {
		return legalRepresentativesList;
	}

	/**
	 * @param legalRepresentativesList the legalRepresentativesList to set
	 */
	public void setLegalRepresentativesList(List<LegalRepresentative> legalRepresentativesList) {
		this.legalRepresentativesList = legalRepresentativesList;
	}

	/**
	 * @return the serviceDescription
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}

	/**
	 * @param serviceDescription the serviceDescription to set
	 */
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	/**
	 * @return the technicalDetails
	 */
	public String getTechnicalDetails() {
		return technicalDetails;
	}

	/**
	 * @param technicalDetails the technicalDetails to set
	 */
	public void setTechnicalDetails(String technicalDetails) {
		this.technicalDetails = technicalDetails;
	}

	/**
	 * @return the idRequisition
	 */
	public Integer getIdRequisition() {
		return idRequisition;
	}

	/**
	 * @param idRequisition the idRequisition to set
	 */
	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}

}
