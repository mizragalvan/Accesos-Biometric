package mx.pagos.admc.contracts.structures;

public class ReporteExcel {
	private int idRequisition;
	private String nombreSolicitante;
	private String tipoDocumento;
	private String nombreProveedor;
	private String rfc;
	private String area;
	private String status;
	private String fechaCreacion;
	private String fechaUltimaModificacion;
	private String nombreSolicitud;
	private String totaldias;
	private Integer turn;
	private Integer attentiondays;
	private String fechaInicioContrato;
	private String fechaFinContrato;
	
	
	
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

	

	public Integer getTurn() {
		return turn;
	}

	public void setTurn(Integer turn) {
		this.turn = turn;
	}

	public Integer getAttentiondays() {
		return attentiondays;
	}

	public void setAttentiondays(Integer attentiondays) {
		this.attentiondays = attentiondays;
	}

	public String getTotaldias() {
		return totaldias;
	}

	public void setTotaldias(String totaldias) {
		this.totaldias = totaldias;
	}

	public String getNombreSolicitud() {
		return nombreSolicitud;
	}

	public void setNombreSolicitud(String nombreSolicitud) {
		this.nombreSolicitud = nombreSolicitud;
	}

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

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getFechaUltimaModificacion() {
		return fechaUltimaModificacion;
	}

	public void setFechaUltimaModificacion(String fechaUltimaModificacion) {
		this.fechaUltimaModificacion = fechaUltimaModificacion;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

}
