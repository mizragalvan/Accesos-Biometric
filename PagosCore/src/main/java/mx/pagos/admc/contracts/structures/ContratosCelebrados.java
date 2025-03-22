package mx.pagos.admc.contracts.structures;

public class ContratosCelebrados {

	private int idRequisition;
	private String nombreSolicitante;
	private String nombreProveedor;
	private String rfc;
	private String status;
	private String fechaInicioContrato;
	private String fechaFinContrato;
	private boolean expiredAttended;
	
	public int getIdRequisition() {
		return idRequisition;
	}
	public void setIdRequisition(int idRequisition) {
		this.idRequisition = idRequisition;
	}
	public String getNombreSolicitante() {
		return nombreSolicitante;
	}
	public void setNombreSolicitante(String nombreSolicitante) {
		this.nombreSolicitante = nombreSolicitante;
	}
	
	public String getNombreProveedor() {
		return nombreProveedor;
	}
	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFechaInicioContrato() {
		return fechaInicioContrato;
	}
	public void setFechaInicioContrato(String fechaInicioContrato) {
		this.fechaInicioContrato = fechaInicioContrato;
	}
	public String getFechaFinContrato() {
		return fechaFinContrato;
	}
	public void setFechaFinContrato(String fechaFinContrato) {
		this.fechaFinContrato = fechaFinContrato;
	}
	public boolean isExpiredAttended() {
		return expiredAttended;
	}
	public void setExpiredAttended(boolean expiredAttended) {
		this.expiredAttended = expiredAttended;
	}
	
	
	
}
