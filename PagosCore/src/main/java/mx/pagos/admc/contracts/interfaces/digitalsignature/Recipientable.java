package mx.pagos.admc.contracts.interfaces.digitalsignature;

import java.util.List;

import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.digitalsignature.Recipient;
import mx.pagos.general.exceptions.DatabaseException;

public interface Recipientable {

	Integer save(Recipient recipient) throws DatabaseException;

	void updateRecipientSigned(Recipient recipient) throws DatabaseException;
	
	void updateStatusRecipientSigned(int idRecipient, Recipient recipient) throws DatabaseException;
	
	void updateStatusDocumentSigned(int idRequisition, DocumentDS documentDS) throws DatabaseException;

	List<Recipient> findRecipientsByIdDocument(Integer idDocument) throws DatabaseException;
	
	Recipient findByWidgetId(final String widgetId) throws DatabaseException;

	Recipient findByProviderRecipientId(final String providerRecipientId) throws DatabaseException;

	Recipient findById(Integer idRecipient) throws DatabaseException;
	
	void deleteByIdDocument(Integer idDocument) throws DatabaseException;
	
}
