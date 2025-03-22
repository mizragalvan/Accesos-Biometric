/**
 * 
 */
package mx.pagos.admc.contracts.structures;

import java.io.Serializable;

/**
 * @author Mizraim
 *
 */
public class QuickResponse implements Serializable {

	/**
	 * Selialization ID generated
	 */
	private static final long serialVersionUID = -8794569451487732627L;

	private String name;
	private String nameAux;
	private String folio;
	private String RFC;
	private Integer idApplicant;
	private String requistionDate;
	private Integer idCompany;
	private Integer idUnit;
	private Integer idArea;
	private String voBoDate; // turn date
	private Boolean risk;
	private Integer idRequisitionQr;
	private Integer idRequisition;
	private String qr;
	private String salt ;
	private String dateCreation;
	    
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the folio
	 */
	public String getFolio() {
		return folio;
	}

	/**
	 * @param folio the folio to set
	 */
	public void setFolio(String folio) {
		this.folio = folio;
	}

	/**
	 * @return the rFC
	 */
	public String getRFC() {
		return RFC;
	}

	/**
	 * @param rFC the rFC to set
	 */
	public void setRFC(String rFC) {
		RFC = rFC;
	}

	/**
	 * @return the idApplicant
	 */
	public Integer getIdApplicant() {
		return idApplicant;
	}

	/**
	 * @param idApplicant the idApplicant to set
	 */
	public void setIdApplicant(Integer idApplicant) {
		this.idApplicant = idApplicant;
	}

	/**
	 * @return the requistionDate
	 */
	public String getRequistionDate() {
		return requistionDate;
	}

	/**
	 * @param requistionDate the requistionDate to set
	 */
	public void setRequistionDate(String requistionDate) {
		this.requistionDate = requistionDate;
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
	 * @return the voBodate
	 */
	public String getVoBodate() {
		return voBoDate;
	}

	/**
	 * @param voBodate the voBodate to set
	 */
	public void setVoBodate(String voBodate) {
		this.voBoDate = voBodate;
	}

	/**
	 * @return the risk
	 */
	public Boolean getRisk() {
		return risk;
	}

	/**
	 * @param risk the risk to set
	 */
	public void setRisk(Boolean risk) {
		this.risk = risk;
	}

	/**
	 * @return the nameAux
	 */
	public String getNameAux() {
		return nameAux;
	}

	/**
	 * @param nameAux the nameAux to set
	 */
	public void setNameAux(String nameAux) {
		this.nameAux = nameAux;
	}

	public Integer getIdRequisitionQr() {
		return idRequisitionQr;
	}

	public void setIdRequisitionQr(Integer idRequisitionQr) {
		this.idRequisitionQr = idRequisitionQr;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
