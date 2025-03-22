package mx.pagos.admc.contracts.structures;

public class Customs {
	private Integer idCustoms;
	private String Name;
	private Integer idRequisition;
	public Integer getIdCustoms() {
		return idCustoms;
	}
	public void setIdCustoms(Integer idCustoms) {
		this.idCustoms = idCustoms;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Integer getIdRequisition() {
		return idRequisition;
	}
	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}
	
}
