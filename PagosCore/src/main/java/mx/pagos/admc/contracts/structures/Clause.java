/**
 * 
 */
package mx.pagos.admc.contracts.structures;

import java.io.Serializable;

import mx.pagos.admc.core.enums.ContratistaEnum;

/**
 * @author Mizraim
 *
 */
public class Clause implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3357643387068804209L;
	// private String contractValidity;
	private String validityStartDate;
	private String validityEndDate;
	private Integer idRequisition;
	private String contractObject;
	private String considerationClause;
	private String considerationAmount;
	private String contractObjectClause;
	private Integer IdLawyer;
	private String depositAmount;
	private String ClausulaFormaPago;

	/**
	 * @return the contractValidity
	 */
	/*
	 * public String getContractValidity() { return contractValidity; }
	 */

	/**
	 * @param contractValidity the contractValidity to set
	 */
	/*
	 * public void setContractValidity(String contractValidity) {
	 * this.contractValidity = contractValidity; }
	 */

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

	/**
	 * @return the idLawyer
	 */
	public Integer getIdLawyer() {
		return IdLawyer;
	}

	/**
	 * @param idLawyer the idLawyer to set
	 */
	public void setIdLawyer(Integer idLawyer) {
		IdLawyer = idLawyer;
	}

	/**
	 * @return the depositAmount
	 */
	public String getDepositAmount() {
		return depositAmount;
	}

	/**
	 * @param depositAmount the depositAmount to set
	 */
	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
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
	 * @return the contractObject
	 */
	public String getContractObject() {
		return contractObject;
	}

	/**
	 * @param contractObject the contractObject to set
	 */
	public void setContractObject(String contractObject) {
		this.contractObject = contractObject;
	}

	/**
	 * @return the considerationClause
	 */
	public String getConsiderationClause() {
		return considerationClause;
	}

	/**
	 * @param considerationClause the considerationClause to set
	 */
	public void setConsiderationClause(String considerationClause) {
		this.considerationClause = considerationClause;
	}

	/**
	 * @return the considerationAmount
	 */
	public String getConsiderationAmount() {
		return considerationAmount;
	}

	/**
	 * @param considerationAmount the considerationAmount to set
	 */
	public void setConsiderationAmount(String considerationAmount) {
		this.considerationAmount = considerationAmount;
	}

	/**
	 * @return the contractObjectClause
	 */
	public String getContractObjectClause() {
		return contractObjectClause;
	}

	/**
	 * @param contractObjectClause the contractObjectClause to set
	 */
	public void setContractObjectClause(String contractObjectClause) {
		this.contractObjectClause = contractObjectClause;
	}

	/**
	 * @return the clausulaFormaPago
	 */
	public String getClausulaFormaPago() {
		return ClausulaFormaPago;
	}

	/**
	 * @param clausulaFormaPago the clausulaFormaPago to set
	 */
	public void setClausulaFormaPago(String clausulaFormaPago) {
		ClausulaFormaPago = clausulaFormaPago;
	}
}
