package mx.pagos.admc.contracts.daos.digitalsignature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableDSConstants;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Documentable;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.enums.DigitalSignatureStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class DocumentDsDAO implements Documentable {

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	@Override
    public Integer save(final DocumentDS documentDS) throws DatabaseException {
		try {
            final MapSqlParameterSource namedParameters = createNamedParameters(documentDS);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            
            this.namedjdbcTemplate.update(this.buildInsertQuery(),
            		namedParameters, keyHolder, new String[]{"idDocument"});
            
            return keyHolder.getKey().intValue();
            
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }		
	}
	
	private String buildInsertQuery() {
	    final StringBuilder query = new StringBuilder();
	    query.append(QueryConstants.INSERT_INTO + TableDSConstants.TABLE_DS_DOCUMENTS 
	    		+ "(idUser, idRequisition, documentName, digitalSignatureProvider, "
	    		+ "providerDocumentId, filePath, onlySigner, emailSubject, emailMessage, "
	    		+ "statusDigitalSignature, createdAt, updatedAt) "
	    		+ QueryConstants.VALUES
	    		+":idUser, :idRequisition, :documentName, :digitalSignatureProvider, "
	    		+ ":providerDocumentId, :filePath, :onlySigner, :emailSubject, :emailMessage, "
	    		+ ":statusDigitalSignature, :createdAt, :updatedAt)");
	    return query.toString();
	}
	
	private MapSqlParameterSource createNamedParameters(final DocumentDS documentDS) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableDSConstants.ID_USER, documentDS.getIdUser());
        namedParameters.addValue(TableDSConstants.ID_REQUISITION, documentDS.getIdRequisition());
        namedParameters.addValue(TableDSConstants.DOCUMENT_NAME, documentDS.getDocumentName());
        namedParameters.addValue(TableDSConstants.DIGITAL_SIGNATURE_PROVIDER, documentDS.getDigitalSignatureProvider().toString());
        namedParameters.addValue(TableDSConstants.PROVIDER_DOCUMENT_ID, documentDS.getProviderDocumentId());
        namedParameters.addValue(TableDSConstants.FILE_PATH, documentDS.getFilePath());
        namedParameters.addValue(TableDSConstants.ONLY_SIGNER, documentDS.isOnlySigner());
        namedParameters.addValue(TableDSConstants.EMAIL_SUBJECT, documentDS.getEmailSubject());
        namedParameters.addValue(TableDSConstants.EMAIL_MESSAGE, documentDS.getEmailMessage());
        namedParameters.addValue(TableDSConstants.STATUS_DIGITAL_SIGNATURE, documentDS.getStatusDigitalSignature().toString());
        namedParameters.addValue(TableDSConstants.CREATED_AT, documentDS.getCreatedAt());
        namedParameters.addValue(TableDSConstants.UPDATED_AT, documentDS.getUpdatedAt());
        return namedParameters;
    }
	
	
	@Override
	public DocumentDS findById(final Integer idDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableDSConstants.ID_DOCUMENT, idDocument);
			String query = this.queryFindById();
			
			return this.namedjdbcTemplate.queryForObject(query, namedParameters, 
					new BeanPropertyRowMapper<DocumentDS>(DocumentDS.class));
			
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			return null;

		} catch (DataAccessException dataAccessException) {
			final Throwable cause = dataAccessException.getMostSpecificCause();
			final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
					cause : dataAccessException;
			throw new DatabaseException("Error al buscar por idDocument", exception);
		}
	}
	
	private String queryFindById() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.SELECT + QueryConstants.ASTERISK 
        		+ QueryConstants.FROM + TableDSConstants.TABLE_DS_DOCUMENTS
        		+ QueryConstants.WHERE + TableDSConstants.ID_DOCUMENT
        		+ QueryConstants.EQUAL_TAG + TableDSConstants.ID_DOCUMENT);
        return query.toString();
    }
	
	
	@Override
	public DocumentDS findDocumentByIdRequisition(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableDSConstants.ID_REQUISITION, idRequisition);
			String query = this.queryFindByIdRequisition();
			
			return this.namedjdbcTemplate.queryForObject(query, namedParameters, 
					new BeanPropertyRowMapper<DocumentDS>(DocumentDS.class));
			
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			return null;
			
		} catch (DataAccessException dataAccessException) {
			final Throwable cause = dataAccessException.getMostSpecificCause();
			final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
					cause : dataAccessException;
			throw new DatabaseException("Error al buscar por idDocument", exception);
		}
	}
	
	private String queryFindByIdRequisition() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.SELECT + QueryConstants.ASTERISK 
        		+ QueryConstants.FROM + TableDSConstants.TABLE_DS_DOCUMENTS
        		+ QueryConstants.WHERE + TableDSConstants.ID_REQUISITION
        		+ QueryConstants.EQUAL_TAG + TableDSConstants.ID_REQUISITION);
        return query.toString();
    }
	
	
	@Override
    public void updateStatusById(final Integer idDocument, final DigitalSignatureStatusEnum digitalSignatureStatus) 
    		throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableDSConstants.ID_DOCUMENT, idDocument);
			namedParameters.addValue(TableDSConstants.STATUS_DIGITAL_SIGNATURE, digitalSignatureStatus.toString());
            
	        this.namedjdbcTemplate.update(this.buildUpdateStatusQuery(), namedParameters);
	        
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }		
	}
	
	private String buildUpdateStatusQuery() {
	    final StringBuilder query = new StringBuilder();
	    query.append(QueryConstants.UPDATE + TableDSConstants.TABLE_DS_DOCUMENTS
	    		+ QueryConstants.SET 
	    		+ TableDSConstants.STATUS_DIGITAL_SIGNATURE 
	    		+ QueryConstants.EQUAL_TAG + TableDSConstants.STATUS_DIGITAL_SIGNATURE
	    		+ QueryConstants.WHERE 
	    		+ TableDSConstants.ID_DOCUMENT + QueryConstants.EQUAL_TAG + TableDSConstants.ID_DOCUMENT);
	    return query.toString();
	}
	
	
	@Override
	public void deleteByIdDocument(final Integer idDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableDSConstants.ID_DOCUMENT, idDocument);
            String query = this.queryDeleteByIdDocument();
            
			this.namedjdbcTemplate.update(query, namedParameters);
			
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	
	private String queryDeleteByIdDocument() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.DELETE + QueryConstants.FROM + TableDSConstants.TABLE_DS_DOCUMENTS
				+ QueryConstants.WHERE + TableDSConstants.ID_DOCUMENT
        		+ QueryConstants.EQUAL_TAG + TableDSConstants.ID_DOCUMENT);
		return query.toString();
	}

}
