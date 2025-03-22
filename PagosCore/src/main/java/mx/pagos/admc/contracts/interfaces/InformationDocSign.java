package mx.pagos.admc.contracts.interfaces;

import com.docusign.esign.model.EnvelopeSummary;

import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.InfoDocument;
import mx.pagos.general.exceptions.DatabaseException;

public interface InformationDocSign {

//	InfoDocument saveInfoDocumentDocuSign(EnvelopeSummary results, DocumentDS documentDS, String docxBaseName)throws DatabaseException;
	void saveInfoDocumentDocuSign(EnvelopeSummary results, DocumentDS documentDS, String docxBaseName, String rutapdf)throws DatabaseException;
}
