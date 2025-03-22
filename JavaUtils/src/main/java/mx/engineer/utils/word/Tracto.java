package mx.engineer.utils.word;

public class Tracto {

private Integer idTracto;
private String brand;
private String model;
private String federalPlates;
private String gpsProvider;
private String driver;
private String tractoInsurancePolicyNumber;
private Integer idRequisition;

public String getDriver() {
	return driver;
}
public void setDriver(String driver) {
	this.driver = driver;
}
public String getTractoInsurancePolicyNumber() {
	return tractoInsurancePolicyNumber;
}
public void setTractoInsurancePolicyNumber(String tractoInsurancePolicyNumber) {
	this.tractoInsurancePolicyNumber = tractoInsurancePolicyNumber;
}

public Integer getIdTracto() {
	return idTracto;
}
public void setIdTracto(Integer idTracto) {
	this.idTracto = idTracto;
}
public String getBrand() {
	return brand;
}
public void setBrand(String brand) {
	this.brand = brand;
}
public String getModel() {
	return model;
}
public void setModel(String model) {
	this.model = model;
}
public String getFederalPlates() {
	return federalPlates;
}
public void setFederalPlates(String federalPlates) {
	this.federalPlates = federalPlates;
}
public String getGpsProvider() {
	return gpsProvider;
}
public void setGpsProvider(String gpsProvider) {
	this.gpsProvider = gpsProvider;
}
public Integer getIdRequisition() {
	return idRequisition;
}
public void setIdRequisition(Integer idRequisition) {
	this.idRequisition = idRequisition;
} 
}
