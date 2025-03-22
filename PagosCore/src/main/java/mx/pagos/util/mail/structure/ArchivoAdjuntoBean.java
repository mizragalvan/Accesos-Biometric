package mx.pagos.util.mail.structure;

import java.io.ByteArrayInputStream;

public class ArchivoAdjuntoBean {
	private String name;
	private String mimeType;
	private ByteArrayInputStream content;
	
	public ArchivoAdjuntoBean(final String nameParameter, final String mimeTypeParameter,
	        final ByteArrayInputStream contentParameter) {
		this.name = nameParameter;
		this.mimeType = mimeTypeParameter;
		this.content = contentParameter;
	}
	
	public final String getNombre() {
		return this.name;
	}
	
	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}
	
	public final ByteArrayInputStream getContent() {
		return this.content;
	}
	
	public final void setContent(final ByteArrayInputStream contentParameter) {
		this.content = contentParameter;
	}
	
	public final String getMimeType() {
		return this.mimeType;
	}
	
	public final void setMimeType(final String mimeTypeParameter) {
		this.mimeType = mimeTypeParameter;
	}
}
