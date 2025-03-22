/**
 * 
 */
package mx.pagos.admc.contracts.structures;

import java.io.Serializable;

import mx.pagos.admc.enums.RecordStatusEnum;

/**
 * @author Mizraim
 *
 */
public class Unit implements Serializable {
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -3819478257746746609L;
	private Integer idUnit;
	private String name;
	private RecordStatusEnum status;
	private Integer idCompany;

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
	 * @return the status
	 */
	public RecordStatusEnum getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(RecordStatusEnum status) {
		this.status = status;
	}

	public Integer getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(Integer idCompany) {
		this.idCompany = idCompany;
	}
	
	

}
