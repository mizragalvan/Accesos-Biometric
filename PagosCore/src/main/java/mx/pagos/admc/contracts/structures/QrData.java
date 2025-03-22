package mx.pagos.admc.contracts.structures;

public class QrData {

	private String folio;
	private String rfc;
	private String idApplicant;
	private String requistionDate;
	private Integer idCompany;
	private Integer idUnit;
	private Integer idArea;
	private String voBoDate;
	private String codigo;
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getIdApplicant() {
		return idApplicant;
	}
	public void setIdApplicant(String idApplicant) {
		this.idApplicant = idApplicant;
	}
	public String getRequistionDate() {
		return requistionDate;
	}
	public void setRequistionDate(String requistionDate) {
		this.requistionDate = requistionDate;
	}

	public Integer getIdCompany() {
		return idCompany;
	}
	public void setIdCompany(Integer idCompany) {
		this.idCompany = idCompany;
	}
	public Integer getIdUnit() {
		return idUnit;
	}
	public void setIdUnit(Integer idUnit) {
		this.idUnit = idUnit;
	}
	public Integer getIdArea() {
		return idArea;
	}
	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}
	public String getVoBoDate() {
		return voBoDate;
	}
	public void setVoBoDate(String voBoDate) {
		this.voBoDate = voBoDate;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
