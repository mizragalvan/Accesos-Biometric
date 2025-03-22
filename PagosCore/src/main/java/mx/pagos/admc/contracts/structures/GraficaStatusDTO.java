package mx.pagos.admc.contracts.structures;

import java.util.Date;

public class GraficaStatusDTO {
	
	private int tipo;
	private int mes;
	private int anio;
	private int idRequisition;
	private int idArea;
	private String areaName;
	private Date fechaInicio;
	private Date fechaFin;
	  
	
	public int getIdRequisition() {
		return idRequisition;
	}
	public void setIdRequisition(int idRequisition) {
		this.idRequisition = idRequisition;
	}
	public int getIdArea() {
		return idArea;
	}
	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	@Override
	public String toString() {
		return "GraficaStatusDTO [tipo=" + tipo + ", mes=" + mes + ", anio=" + anio + ", fechaInicio=" + fechaInicio
				+ ", fechaFin=" + fechaFin + "]";
	}
	
}
