package mx.pagos.util.mail.structure;

public class ImagesBean {
	private String name;
	private String path;
	
	public ImagesBean(final String nameParameter, final String pathParameter) {
		this.name = nameParameter;
		this.path = pathParameter;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}
	
	public final String getPath() {
		return this.path;
	}
	
	public final void setPath(final String pathParameter) {
		this.path = pathParameter;
	}
}
