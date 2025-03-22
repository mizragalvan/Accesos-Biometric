package mx.pagos.admc.contracts.structures;

public class VoBoDocumentFile {

	private Integer idArea;
	private FileUploadInfo documment;
	
	public final Integer getIdArea() {
		return this.idArea;
	}
	
	public final void setIdArea(final Integer idAreaParameter) {
		this.idArea = idAreaParameter;
	}
	
	public final FileUploadInfo getDocumment() {
		return this.documment;
	}
	
	public final void setDocumment(final FileUploadInfo docummentParameter) {
		this.documment = docummentParameter;
	}
}
