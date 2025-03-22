package mx.pagos.util.mail.structure;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class EmailBean implements Serializable {
	private String subject = "";
	private String from = "";
	private String to = "";
	private String emailTextContent = "";
	private List<ImagesBean> images;
	private List<ArchivoAdjuntoBean> attachments;
	
	public final void addImages(final String imageId, final String imagePath) {
		if (this.images == null) {
			this.images = new ArrayList<ImagesBean>();
		}
		final ImagesBean bean = new ImagesBean(imageId, imagePath);
		this.images.add(bean);
	}
	
    public final void addFiles(final String fileName, final String mimeType, final ByteArrayInputStream fileContent) {
		if (this.attachments == null) {
			this.attachments = new ArrayList<ArchivoAdjuntoBean>();
		}
		final ArchivoAdjuntoBean attachedFileBean = new ArchivoAdjuntoBean(fileName, mimeType, fileContent);
		this.attachments.add(attachedFileBean);
	}

	public final String getSubject() {
		return this.subject;
	}

	public final void setSubject(final String subjectParameter) {
		this.subject = subjectParameter;
	}

	public final String getEmailTextContent() {
		return this.emailTextContent;
	}

	public final void setEmailTextContent(final String emailTextContentParameter) {
		this.emailTextContent = emailTextContentParameter;
	}

	public final String getTo() {
		return this.to;
	}

	public final void setTo(final String toParameter) {
		this.to = toParameter;
	}

	public final String getFrom() {
		return this.from;
	}

	public final void setFrom(final String fromParameter) {
		this.from = fromParameter;
	}
	
	public final List<ImagesBean> getImages() {
		return this.images;
	}

	public final List<ArchivoAdjuntoBean> getAttachements() {
		return this.attachments;
	}

	
}
