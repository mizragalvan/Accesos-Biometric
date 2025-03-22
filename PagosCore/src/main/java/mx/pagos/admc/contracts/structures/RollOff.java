package mx.pagos.admc.contracts.structures;

public class RollOff {
	private Integer idRollOff;
	private String brand;
	private String description;
	private String serialNumber;
	private String manufacturingDate;
	private String tareWeight;
	private String dimensions;
	private Integer idRequisition;
	
	public Integer getIdRollOff() {
		return idRollOff;
	}
	public void setIdRollOff(Integer idRollOff) {
		this.idRollOff = idRollOff;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getManufacturingDate() {
		return manufacturingDate;
	}
	public void setManufacturingDate(String manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}
	public String getTareWeight() {
		return tareWeight;
	}
	public void setTareWeight(String tareWeight) {
		this.tareWeight = tareWeight;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}
	public Integer getIdRequisition() {
		return idRequisition;
	}
	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}
	
}
