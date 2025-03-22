package mx.pagos.admc.contracts.constants;

public class TableDSConstants {
	
	public static final String CREATED_AT = "createdAt";
	public static final String UPDATED_AT = "updatedAt";

	// Table DS_DOCUMENTS
	public static final String TABLE_DS_DOCUMENTS = "DS_DOCUMENTS ";
	public static final String ID_DOCUMENT = "idDocument";
	public static final String ID_USER = "idUser";
	public static final String ID_REQUISITION = "idRequisition";
	public static final String DOCUMENT_NAME = "documentName";
	public static final String DIGITAL_SIGNATURE_PROVIDER = "digitalSignatureProvider";
	public static final String PROVIDER_DOCUMENT_ID = "providerDocumentId";
	public static final String FILE_PATH = "filePath";
	public static final String ONLY_SIGNER = "onlySigner";
	public static final String EMAIL_SUBJECT = "emailSubject";
	public static final String EMAIL_MESSAGE = "emailMessage";
	public static final String STATUS_DIGITAL_SIGNATURE = "statusDigitalSignature";
	
	// Table DS_RECIPIENTS
	public static final String TABLE_DS_RECIPIENTS = "DS_RECIPIENTS ";
	public static final String ID_RECIPIENT = "idRecipient";
	public static final String SIGNING_ORDER = "signingOrder";
	public static final String RFC = "rfc";
	public static final String FULLNAME = "fullName";
	public static final String RECIPIENT_ACTION = "recipientAction";
	public static final String EMAIL = "email";
	public static final String SECRET_CODE = "secretCode";
	public static final String NOTE = "note";
	public static final String PROVIDER_RECIPIENT_ID = "providerRecipientId";
	public static final String WIDGET_ID = "widgetId";
	public static final String SIGNED = "signed";

	// Table DS_RECIPIENTS
	public static final String TABLE_DS_USER_INFORMATION = "DS_USER_INFORMATION ";

	// Table DS_CONTACTS
	public static final String TABLE_DS_CONTACTS = "DS_CONTACTS ";
	public static final String ID_CONTACT = "idContact";
	public static final String NOTES = "notes";
	

}