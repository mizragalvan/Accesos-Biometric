package mx.pagos.admc.contracts.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.docusign.esign.model.EnvelopeSummary;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.InformationDocSign;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class InformationDocuSignDAO implements InformationDocSign {

	 @Autowired
	    private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	@Override
	public void saveInfoDocumentDocuSign(EnvelopeSummary results, DocumentDS documentDS, String docxBaseName, String rutapdf)
			throws DatabaseException {
		 try {
	          final MapSqlParameterSource namedParameters = this.createInsertDsDocumentParameters(results,documentDS,docxBaseName, rutapdf);

	          this.namedjdbcTemplate.update(this.buildInsertDsDocumentQuery(), namedParameters);
	        } catch (DataAccessException dataAccessException) {
	            throw new DatabaseException(dataAccessException);
	        }   
    }
	
	private MapSqlParameterSource createInsertDsDocumentParameters(final EnvelopeSummary results,DocumentDS documentDS, String docxBaseName, String rutapdf) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        int resultado;

        if (documentDS.isOnlySigner()) {
            resultado = 1;
        } else {
            resultado = 0;
        }
        
        namedParameters.addValue(TableConstants.ID_DOCUMENT, results.getEnvelopeId());
        namedParameters.addValue(TableConstants.ID_USER, documentDS.getIdUser());
        namedParameters.addValue(TableConstants.ID_REQUISITION, documentDS.getIdRequisition());
        namedParameters.addValue(TableConstants.DOCUMENT_NAME, docxBaseName);
        namedParameters.addValue(TableConstants.DIGITAL_SIGNATURE_PROVIDER, results);
        
        namedParameters.addValue(TableConstants.PROVIDER_DOCUMENT_ID, results.getEnvelopeId());
        namedParameters.addValue(TableConstants.FILE_PATH, rutapdf);
        namedParameters.addValue(TableConstants.ONLY_SIGNER, resultado);
        namedParameters.addValue(TableConstants.EMAIL_SUBJECT, documentDS.getEmailSubject());
        namedParameters.addValue(TableConstants.EMAIL_MESSAGE, documentDS.getEmailMessage());
        namedParameters.addValue(TableConstants.STATUS_DIGITAL_SIGNATURE, results.getStatus());
        namedParameters.addValue(TableConstants.CREATED_AT, results.getStatusDateTime() );
//        namedParameters.addValue(TableConstants.UPDATED_AT, personality.getName());
        

        return namedParameters;
    }
	
	private String buildInsertDsDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append("DS_DOCUMENTS (idDocument, idUser,idRequisition, documentName,digitalSignatureProvider, providerDocumentId,filePath, onlySigner,emailSubject, emailMessage,statusDigitalSignature, createdAt, updatedAt) ");
        query.append("VALUES (:IdDocument, :IdUser,:IdRequisition, :DocumentName,:digitalSignatureProvider, :providerDocumentId,:IdDocument, :filePath, :onlySigner, :emailSubject, :emailMessage, :statusDigitalSignature, :createdAt, GETDATE())");
        return query.toString();
    }
	
	


}
