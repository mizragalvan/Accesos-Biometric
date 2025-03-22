package mx.pagos.admc.contracts.structures;

import java.io.Serializable;

import mx.pagos.admc.enums.SupplierPersonTypeEnum;

/**
 * @author Mizraim
 */
public class SupplierPersonByRequisition implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer idRequisition;
	private Integer idSupplier;
	private Integer idSupplierPerson;
	
	// transient
	private String name;
	// transient
    private SupplierPersonTypeEnum type;
	
	public SupplierPersonByRequisition () {}

	public SupplierPersonByRequisition(Integer idRequisition, Integer idSupplier, Integer idSupplierPerson) {
		this.idRequisition = idRequisition;
		this.idSupplier = idSupplier;
		this.idSupplierPerson = idSupplierPerson;
	}
	
	public SupplierPersonByRequisition(Integer idRequisition, Integer idSupplier, Integer idSupplierPerson, String name,
			SupplierPersonTypeEnum type) {
		this.idRequisition = idRequisition;
		this.idSupplier = idSupplier;
		this.idSupplierPerson = idSupplierPerson;
		this.name = name;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdRequisition() {
		return idRequisition;
	}

	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}

	public Integer getIdSupplier() {
		return idSupplier;
	}

	public void setIdSupplier(Integer idSupplier) {
		this.idSupplier = idSupplier;
	}

	public Integer getIdSupplierPerson() {
		return idSupplierPerson;
	}

	public void setIdSupplierPerson(Integer idSupplierPerson) {
		this.idSupplierPerson = idSupplierPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SupplierPersonTypeEnum getType() {
		return type;
	}

	public void setType(SupplierPersonTypeEnum type) {
		this.type = type;
	}
	
}
