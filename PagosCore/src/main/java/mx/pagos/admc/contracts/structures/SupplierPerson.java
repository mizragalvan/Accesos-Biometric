package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.SupplierPersonTypeEnum;

/**
 * 
 * @author Mizraim
 * 
 * Estructura que tiene los campos necesarios para almacenar una persona relacionada a un proveedor
 *
 */
public class SupplierPerson {
	private Integer idSupplierPerson;
	private Integer idSupplier;
	private String name;
	private SupplierPersonTypeEnum type;
	private String power;
	private String deedNumber;
	private String deedDate;
	private String publicNotaryName;
	private String deedNotaryNumber;
	private String publicNotaryState;
	private String commercialFolio;
	private String commercialFolioInscriptionDate;
	private String commercialFolioInscriptionState;
	private String commercialOrPropertyRegister;
	private boolean active;
	private String rfc;
    private String correoElectronico;

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public SupplierPerson() { }

	public SupplierPerson(final String nameParameter, final SupplierPersonTypeEnum typeParameter) {
		this.name = nameParameter;
		this.type = typeParameter;
	}

	public final Integer getIdSupplierPerson() {
		return this.idSupplierPerson;
	}

	public final void setIdSupplierPerson(final Integer idSupplierPersonParameter) {
		this.idSupplierPerson = idSupplierPersonParameter;
	}

	public final Integer getIdSupplier() {
		return this.idSupplier;
	}

	public final void setIdSupplier(final Integer idSupplierParameter) {
		this.idSupplier = idSupplierParameter;
	}

	public final String getName() {
		return this.name;
	}

	public final String getNameUpper() {
		if (this.name != null)
			return this.name.toUpperCase();
		return this.name;
	}

	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}

	public final SupplierPersonTypeEnum getSupplierPersonType() {
		return this.type;
	}

	public final void setSupplierPersonType(final SupplierPersonTypeEnum typeParameter) {
		this.type = typeParameter;
	}

	public final String getPower() {
		return this.power;
	}

	public final void setPower(final String powerParameter) {
		this.power = powerParameter;
	}

	public final SupplierPersonTypeEnum getType() {
		return type;
	}

	public final void setType(final SupplierPersonTypeEnum type) {
		this.type = type;
	}

	public final String getDeedNumber() {
		return this.deedNumber;
	}

	public final void setDeedNumber(final String deedNumberParameter) {
		this.deedNumber = deedNumberParameter;
	}

	public final String getDeedDate() {
		return this.deedDate;
	}

	public final void setDeedDate(final String deedDateParameter) {
		this.deedDate = deedDateParameter;
	}

	public final String getPublicNotaryName() {
		return this.publicNotaryName;
	}

	public final void setPublicNotaryName(final String publicNotaryNameParameter) {
		this.publicNotaryName = publicNotaryNameParameter;
	}

	public final String getDeedNotaryNumber() {
		return this.deedNotaryNumber;
	}

	public final void setDeedNotaryNumber(final String deedNotaryNumberParameter) {
		this.deedNotaryNumber = deedNotaryNumberParameter;
	}

	public final String getPublicNotaryState() {
		return this.publicNotaryState;
	}

	public final void setPublicNotaryState(final String publicNotaryStateParameter) {
		this.publicNotaryState = publicNotaryStateParameter;
	}

	public final String getCommercialFolio() {
		return this.commercialFolio;
	}

	public final void setCommercialFolio(final String commercialFolioParameter) {
		this.commercialFolio = commercialFolioParameter;
	}

	public final String getCommercialFolioInscriptionDate() {
		return this.commercialFolioInscriptionDate;
	}

	public final void setCommercialFolioInscriptionDate(final String commercialFolioInscriptionDateParameter) {
		this.commercialFolioInscriptionDate = commercialFolioInscriptionDateParameter;
	}

	public final String getCommercialOrPropertyRegister() {
		return this.commercialOrPropertyRegister;
	}

	public final void setCommercialOrPropertyRegister(final String commercialOrPropertyRegisterParameter) {
		this.commercialOrPropertyRegister = commercialOrPropertyRegisterParameter;
	}

	public final String getCommercialFolioInscriptionState() {
		return this.commercialFolioInscriptionState;
	}

	public final void setCommercialFolioInscriptionState(final String commercialFolioInscriptionStateParameter) {
		this.commercialFolioInscriptionState = commercialFolioInscriptionStateParameter;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}