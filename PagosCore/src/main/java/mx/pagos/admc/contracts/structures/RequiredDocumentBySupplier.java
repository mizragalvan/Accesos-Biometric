package mx.pagos.admc.contracts.structures;

public class RequiredDocumentBySupplier {
    private Integer idSupplier;
    private Integer idPersonality;
    private Integer idRequiredDocument;
	private String found; 
    private String  name;
    
	public Integer getIdSupplier() {
		return idSupplier;
	}
	public void setIdSupplier(Integer idSupplier) {
		this.idSupplier = idSupplier;
	}
	public Integer getIdPersonality() {
		return idPersonality;
	}
	public void setIdPersonality(Integer idPersonality) {
		this.idPersonality = idPersonality;
	}
    public Integer getIdRequiredDocument() {
		return idRequiredDocument;
	}
	public void setIdRequiredDocument(Integer idRequiredDocument) {
		this.idRequiredDocument = idRequiredDocument;
	}
	public String getFound() {
		return found;
	}
	public void setFound(String found) {
		this.found = found;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
