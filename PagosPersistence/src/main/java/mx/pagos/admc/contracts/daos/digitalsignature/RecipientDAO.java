package mx.pagos.admc.contracts.daos.digitalsignature;

import java.util.List;

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
import mx.pagos.admc.contracts.interfaces.digitalsignature.Recipientable;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.digitalsignature.Recipient;
import mx.pagos.admc.enums.digitalsignature.RecipientActionEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class RecipientDAO implements Recipientable {

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	@Override
    public Integer save(final Recipient recipient) throws DatabaseException {
		try {
            final MapSqlParameterSource namedParameters = createNamedParameters(recipient);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.queryInsert(), namedParameters, keyHolder, new String[]{"idRecipient"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }		
	}
	
	private String queryInsert() {
	    final StringBuilder query = new StringBuilder();
	    query.append(QueryConstants.INSERT_INTO + TableDSConstants.TABLE_DS_RECIPIENTS 
	    		+ "(idDocument, recipientAction, providerRecipientId, signingOrder, rfc, "
	    		+ "fullName, email, secretCode, note, widgetId, signed, createdAt, updatedAt) "
	    		+ QueryConstants.VALUES
	    		+":idDocument, :recipientAction, :providerRecipientId, :signingOrder, :rfc, "
	    		+ ":fullName, :email, :secretCode, :note, :widgetId, :signed, :createdAt, :updatedAt)");
	    return query.toString();
	}
	
	
	private MapSqlParameterSource createNamedParameters(final Recipient recipient) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableDSConstants.ID_DOCUMENT, recipient.getIdDocument());
        namedParameters.addValue(TableDSConstants.RECIPIENT_ACTION, recipient.getRecipientAction().toString());
        namedParameters.addValue(TableDSConstants.PROVIDER_RECIPIENT_ID, recipient.getProviderRecipientId());
        namedParameters.addValue(TableDSConstants.SIGNING_ORDER, recipient.getSigningOrder());
        namedParameters.addValue(TableDSConstants.RFC, recipient.getRfc());
        namedParameters.addValue(TableDSConstants.FULLNAME, recipient.getFullName());
        namedParameters.addValue(TableDSConstants.EMAIL, recipient.getEmail());
        namedParameters.addValue(TableDSConstants.SECRET_CODE, recipient.getSecretCode());
        namedParameters.addValue(TableDSConstants.NOTE, recipient.getNote());
        namedParameters.addValue(TableDSConstants.WIDGET_ID, recipient.getWidgetId());
        namedParameters.addValue(TableDSConstants.SIGNED, recipient.getSigned());
        namedParameters.addValue(TableDSConstants.CREATED_AT, recipient.getCreatedAt());
        namedParameters.addValue(TableDSConstants.UPDATED_AT, recipient.getUpdatedAt());
        return namedParameters;
    }
	
	
	@Override
    public void updateStatusRecipientSigned(int idRequisition,final Recipient recipient) throws DatabaseException {
		try {
            final MapSqlParameterSource namedParameters = createNamedParametersUpdateStatusRS(idRequisition,recipient);
            String query = queryUpdateStatusSignedRecipients();
            
            this.namedjdbcTemplate.update(query, namedParameters);
            
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }		
	}
	@Override
    public void updateStatusDocumentSigned(int idRequisition, final DocumentDS recipient) throws DatabaseException {
		try {
            final MapSqlParameterSource namedParameters = createNamedParametersUpdateStatusDS(idRequisition,recipient);
            String query = queryUpdateStatusSignedDocuments();
            
            this.namedjdbcTemplate.update(query, namedParameters);
            
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }		
	}
	@Override
    public void updateRecipientSigned(final Recipient recipient) throws DatabaseException {
		try {
            final MapSqlParameterSource namedParameters = createNamedParametersUpdateRS(recipient);
            String query = queryUpdateRecipientSigned();
            
            this.namedjdbcTemplate.update(query, namedParameters);
            
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }		
	}
	
	private String queryUpdateRecipientSigned() {
	    final StringBuilder query = new StringBuilder();
	    query.append(QueryConstants.UPDATE + TableDSConstants.TABLE_DS_RECIPIENTS 
	    		+ QueryConstants.SET 
	    		+ TableDSConstants.SIGNED + QueryConstants.EQUAL_TAG + TableDSConstants.SIGNED
	    		+ QueryConstants.WHERE 
	    		+ TableDSConstants.ID_RECIPIENT 
	    		+ QueryConstants.EQUAL_TAG + TableDSConstants.ID_RECIPIENT);
	    return query.toString();
	}
	
	private String queryUpdateStatusSignedRecipients() {
	    final StringBuilder query = new StringBuilder();
	    query.append(QueryConstants.UPDATE + TableDSConstants.TABLE_DS_RECIPIENTS 
	    		+ QueryConstants.SET 
	    		+ TableDSConstants.SIGNED + QueryConstants.EQUAL_TAG + TableDSConstants.SIGNED
	    		+ QueryConstants.WHERE 
	    		+ TableDSConstants.ID_DOCUMENT 
	    		+ QueryConstants.EQUAL_TAG + TableDSConstants.ID_DOCUMENT);
//	    		+ QueryConstants.AND + TableDSConstants.RECIPIENT_ACTION
//	    		+ QueryConstants.EQUAL_TAG + TableConstants.STATUS_DIGITAL_SIGNATURE);
	    query.append(" AND recipientAction IN ('NEEDS_TO_VIEW','NEEDS_TO_SIGN')");
//	    query.append("UPDATE DS_RECIPIENTS SET signed=:signed WHERE idRecipient=:idRecipient");
	    return query.toString();
	}
	
	private String queryUpdateStatusSignedDocuments() {
	    final StringBuilder query = new StringBuilder();
	    query.append(QueryConstants.UPDATE + TableDSConstants.TABLE_DS_DOCUMENTS 
	    		+ QueryConstants.SET 
	    		+ TableDSConstants.STATUS_DIGITAL_SIGNATURE + QueryConstants.EQUAL_TAG + TableDSConstants.STATUS_DIGITAL_SIGNATURE
	    		+ QueryConstants.WHERE 
	    		+ TableDSConstants.ID_REQUISITION
	    		+ QueryConstants.EQUAL_TAG + TableDSConstants.ID_REQUISITION);
//	    query.append("UPDATE DS_DOCUMENTS SET signed=:signed WHERE idRecipient=:idRecipient");
	    return query.toString();
	}
	
	private MapSqlParameterSource createNamedParametersUpdateRS(final Recipient recipient) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableDSConstants.ID_RECIPIENT, recipient.getIdRecipient());
        namedParameters.addValue(TableDSConstants.SIGNED, recipient.getSigned());
        return namedParameters;
    }
	
	private MapSqlParameterSource createNamedParametersUpdateStatusRS(int idRequisition,final Recipient recipient) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableDSConstants.ID_DOCUMENT, idRequisition);
        namedParameters.addValue(TableDSConstants.STATUS_DIGITAL_SIGNATURE, RecipientActionEnum.NEEDS_TO_SIGN.toString());
        namedParameters.addValue(TableDSConstants.SIGNED, recipient.getSigned());
        return namedParameters;
    }
	private MapSqlParameterSource createNamedParametersUpdateStatusDS(int idRequisition,final DocumentDS document) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableDSConstants.ID_REQUISITION, idRequisition);
        namedParameters.addValue(TableDSConstants.STATUS_DIGITAL_SIGNATURE, document.getStatusDigitalSignature().toString());
        return namedParameters;
    }
	
	
	@Override
    public List<Recipient> findRecipientsByIdDocument(final Integer idDocument) throws DatabaseException {
        try {
        	final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableDSConstants.ID_DOCUMENT, idDocument);
            String query = this.queryFinByIdDocument();
            
            return this.namedjdbcTemplate.query(query, namedParameters,
                    new BeanPropertyRowMapper<Recipient>(Recipient.class));
            
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			return null;

        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
	
	private String queryFinByIdDocument() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.SELECT + QueryConstants.ASTERISK 
        		+ QueryConstants.FROM + TableDSConstants.TABLE_DS_RECIPIENTS
        		+ QueryConstants.WHERE + TableDSConstants.ID_DOCUMENT 
        		+ QueryConstants.EQUAL_TAG + TableDSConstants.ID_DOCUMENT);
        return query.toString();
    }
	
	
	@Override
	public Recipient findByWidgetId(final String widgetId) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableDSConstants.WIDGET_ID, widgetId);
			String query = this.querFindByWidgetId();
			
			return this.namedjdbcTemplate.queryForObject(query, namedParameters, 
					new BeanPropertyRowMapper<Recipient>(Recipient.class));
			
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			return null;

		} catch (DataAccessException dataAccessException) {
			final Throwable cause = dataAccessException.getMostSpecificCause();
			final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
					cause : dataAccessException;
			throw new DatabaseException("Error al buscar por widgetId", exception);
		}
	}
	
	private String querFindByWidgetId() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT + QueryConstants.ASTERISK 
				+ QueryConstants.FROM + TableDSConstants.TABLE_DS_RECIPIENTS
				+ QueryConstants.WHERE + TableDSConstants.WIDGET_ID 
				+ QueryConstants.EQUAL_TAG + TableDSConstants.WIDGET_ID);
		return query.toString();
	}
	
	
	@Override
	public Recipient findByProviderRecipientId(final String providerRecipientId) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableDSConstants.PROVIDER_RECIPIENT_ID, providerRecipientId);
			String query = this.querFindByProviderRecipientId();
		
			return this.namedjdbcTemplate.queryForObject(query, namedParameters, 
					new BeanPropertyRowMapper<Recipient>(Recipient.class));

		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			return null;

		} catch (DataAccessException dataAccessException) {
			final Throwable cause = dataAccessException.getMostSpecificCause();
			final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
					cause : dataAccessException;
			throw new DatabaseException("Error al buscar por providerRecipientId", exception);
		}
	}
	
	private String querFindByProviderRecipientId() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT + QueryConstants.ASTERISK 
				+ QueryConstants.FROM + TableDSConstants.TABLE_DS_RECIPIENTS
				+ QueryConstants.WHERE + TableDSConstants.PROVIDER_RECIPIENT_ID 
				+ QueryConstants.EQUAL_TAG + TableDSConstants.PROVIDER_RECIPIENT_ID);
		return query.toString();
	}

	
	@Override
    public Recipient findById(final Integer idRecipient) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableDSConstants.ID_RECIPIENT, idRecipient);
            String query = this.findByIdQuery();
            
            return this.namedjdbcTemplate.queryForObject(query, namedParameters, 
                    new BeanPropertyRowMapper<Recipient>(Recipient.class));
            
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			return null;

        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al buscar por idRecipient", exception);
        }
    }
	
	private String findByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.SELECT + QueryConstants.ASTERISK 
        		+ QueryConstants.FROM + TableDSConstants.TABLE_DS_RECIPIENTS
        		+ QueryConstants.WHERE + TableDSConstants.ID_RECIPIENT
        		+ QueryConstants.EQUAL_TAG + TableDSConstants.ID_RECIPIENT);
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
		query.append(QueryConstants.DELETE + QueryConstants.FROM + TableDSConstants.TABLE_DS_RECIPIENTS
				+ QueryConstants.WHERE + TableDSConstants.ID_DOCUMENT
        		+ QueryConstants.EQUAL_TAG + TableDSConstants.ID_DOCUMENT);
		return query.toString();
	}

}
