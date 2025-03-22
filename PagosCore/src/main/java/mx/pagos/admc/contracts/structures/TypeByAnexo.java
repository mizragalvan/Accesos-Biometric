package mx.pagos.admc.contracts.structures;


/**
 * @author Mizraim
 */
public class TypeByAnexo {
	
	private int codigo;
	private String mensaje;
	
	private Integer idTypeByAnexo;
    private Integer idDocumentType;
    private boolean moral;
    private String name;
    private int order;
    //private int page;
    private int folio;
    private int idDocument;
    private String fileName;

    public TypeByAnexo () {}
    
	public TypeByAnexo(int codigo, String mensaje, Integer idTypeByAnexo) {
		super();
		this.codigo = codigo;
		this.mensaje = mensaje;
		this.idTypeByAnexo = idTypeByAnexo;
	}

	public Integer getIdTypeByAnexo() {
		return idTypeByAnexo;
	}

	public void setIdTypeByAnexo(Integer idTypeByAnexo) {
		this.idTypeByAnexo = idTypeByAnexo;
	}

	public Integer getIdDocumentType() {
		return idDocumentType;
	}

	public void setIdDocumentType(Integer idDocumentType) {
		this.idDocumentType = idDocumentType;
	}
	
	public boolean getMoral() {
		return moral;
	}

	public void setMoral(boolean moral) {
		this.moral = moral;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	/*public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}*/

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getFolio() {
		return folio;
	}

	public void setFolio(int folio) {
		this.folio = folio;
	}

	public int getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(int idDocument) {
		this.idDocument = idDocument;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
    
}
