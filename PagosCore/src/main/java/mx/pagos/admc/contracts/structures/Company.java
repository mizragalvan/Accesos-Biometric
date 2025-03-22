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
public class Company implements Serializable {

	/**
	 * Default
	 */
	private static final long serialVersionUID = 4974452304341006553L;

	private Integer idCompany;
	private String name;
	private RecordStatusEnum status;

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
}
