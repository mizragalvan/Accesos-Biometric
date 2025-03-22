package mx.pagos.admc.contracts.interfaces.digitalsignature;

import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.enums.DigitalSignatureStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Documentable {

	Integer save(DocumentDS documentDS) throws DatabaseException;

	DocumentDS findById(Integer idDocument) throws DatabaseException;
	
	DocumentDS findDocumentByIdRequisition(Integer idRequisition) throws DatabaseException;
	
	void updateStatusById(Integer idDocument,
			DigitalSignatureStatusEnum digitalSignatureStatus) throws DatabaseException;
	
	void deleteByIdDocument(Integer idDocument) throws DatabaseException;
	
}
