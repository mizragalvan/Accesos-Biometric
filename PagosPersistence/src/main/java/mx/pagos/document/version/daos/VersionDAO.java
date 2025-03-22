package mx.pagos.document.version.daos;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.document.version.daos.constants.TableVersionConstants;
import mx.pagos.document.versioning.interfaces.Versionable;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.daos.ProfilesDAO;

@Repository
public class VersionDAO implements Versionable {
    private static final String WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT = "WHERE IdDocument = :IdDocument ";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	private static final Logger Log = Logger.getLogger(VersionDAO.class);

    @Override
    public Integer save(final Integer idDocument, final File path, final Integer idUser) throws DatabaseException {
        try {
        	
        	Log.info("\n***************************************\n SAVE DOCUMENT \n idDocument ("+idDocument+") \n path ("+path+") \n idUser ("+idUser+")");
        	
            final MapSqlParameterSource namedParameters = this.createSaveNamedParameters(idDocument, path, idUser);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertQuery(), namedParameters, keyHolder, 
                    new String[]{"IdVersion"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Version> findByIdDocument(final Integer idDocument) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdDocumentNamedParameters(idDocument);
            return this.namedjdbcTemplate.query(this.findByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<Version>(Version.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Version findDocumentVersion(final Integer idDoc, final Integer versionNumber) 
            throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.namedDocumentVersionParameter(idDoc, versionNumber);
            return this.namedjdbcTemplate.queryForObject(this.findByIdAndVersionQuery(), namedParameters,
                    new BeanPropertyRowMapper<Version>(Version.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void deleteByIdDocument(final Integer idDocument) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdDocumentNamedParameters(idDocument);
            this.namedjdbcTemplate.update(this.buildDeleteByIdDocumentQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String buildDeleteByIdDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM VERSION ");
        query.append(WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT);
        return query.toString();
    }

    private MapSqlParameterSource createSaveNamedParameters(final Integer idDocument, final File path, final Integer idUser) {
        final MapSqlParameterSource namedParameters = this.createFindByIdDocumentNamedParameters(idDocument);
        namedParameters.addValue(TableVersionConstants.DOCUMENT_PATH, path.getAbsolutePath());
        namedParameters.addValue(TableVersionConstants.ID_USER, idUser);
        return namedParameters;
    }
    
    private String buildInsertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO VERSION(IdUser, IdDocument, DocumentPath, VersionNumber) ");
        query.append("SELECT :IdUser, :IdDocument, :DocumentPath, COALESCE(MAX(VersionNumber), 0) + 1 FROM VERSION ");
        query.append(WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT);
        return query.toString();
    }
    
    private String findByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT DocumentPath, VersionNumber, VERSION.IdDocument, VERSION.IdVersion FROM VERSION ");
        query.append(WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT);
        query.append("ORDER BY VersionNumber DESC");
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdDocumentNamedParameters(
            final Integer idDocument) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableVersionConstants.ID_DOCUMENT, idDocument);
        return namedParameters;
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT IdVersion, IdDocument, DocumentPath, VersionNumber, IdUser, CreateDate ");
    }
    
    public void deleteById(final Integer idVersion) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableVersionConstants.ID_VERSION, idVersion);
        this.namedjdbcTemplate.update(this.buildDeteleByIdQuery(), namedParameters);
    }
    
    private String buildDeteleByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM VERSION WHERE IdVersion = :IdVersion");
        return query.toString();
    }

    private MapSqlParameterSource namedDocumentVersionParameter(final Integer idDoc, final Integer versionNumber) {
        final MapSqlParameterSource namedParameters = this.createFindByIdDocumentNamedParameters(
                idDoc);
        namedParameters.addValue(TableVersionConstants.VERSION_NUMBER, versionNumber);
        return namedParameters;
    }

    private String findByIdAndVersionQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("FROM VERSION WHERE IdDocument = :IdDocument AND VersionNumber = :VersionNumber");
        return query.toString();
    }
    
    public Integer countRecordsByIdDocumentQuery(final Integer idDocument) {
        final MapSqlParameterSource namedParameters = this.createFindByIdDocumentNamedParameters(
                idDocument);
        return this.namedjdbcTemplate.queryForObject(this.buildCountQuery(), namedParameters, Integer.class);
    }
    
    private String buildCountQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(IdVersion) FROM VERSION ");
        query.append(WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT);
        return query.toString();
    }

    @Override
    public Version findByIdVersion(final Integer idVersion) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableVersionConstants.ID_VERSION, idVersion);
            return this.namedjdbcTemplate.queryForObject(this.findByIdVersionQuery(), namedParameters,
                    new BeanPropertyRowMapper<Version>(Version.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar por id de Versión", dataAccessException);
        }
    }
    
    private String findByIdVersionQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("FROM VERSION WHERE IdVersion = :IdVersion");
        return query.toString();
    }
    
    @Override
    public VersionDTO findDocumentVersionDTO(final Integer idDoc, final Integer versionNumber) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.namedDocumentVersionParameter(idDoc, versionNumber);
            return this.namedjdbcTemplate.queryForObject(this.findByIdAndVersionDTOQuery(), namedParameters,
                    new BeanPropertyRowMapper<VersionDTO>(VersionDTO.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findByIdAndVersionDTOQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT v.IdVersion, v.IdDocument, v.DocumentPath, v.VersionNumber, v.CreateDate, CONVERT(VARCHAR(10), v.CreateDate, 103) AS CreateDateString, "); 
        query.append("u.IdUser, (COALESCE(Name,'') + COALESCE(' ' + u.FirstLastName,'') +COALESCE(' ' + u.SecondLastName,'')) as UserName "); 
        query.append("FROM VERSION AS v LEFT JOIN USERS As u on v.IdUser = u.IdUser ");
        query.append("WHERE v.IdDocument = :IdDocument AND v.VersionNumber = :VersionNumber");
        return query.toString();
    }
    
    @Override
    public List<VersionDTO> findContractVersionDTO(final Integer idRequisition) throws DatabaseException, EmptyResultException {
        try {
        	 final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
             namedParameters.addValue(TableVersionConstants.ID_REQUISITION, idRequisition);
             return this.namedjdbcTemplate.query(this.findContractVersionByIdDocumentQuery(), namedParameters,
                     new BeanPropertyRowMapper<VersionDTO>(VersionDTO.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findContractVersionByIdDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT v.IdVersion, v.IdDocument, v.DocumentPath, v.VersionNumber, v.IdUser, v.CreateDate, ");
        query.append(" (CONVERT(VARCHAR(10), v.CreateDate, 103) + ' ' + convert(VARCHAR(8), v.CreateDate, 14)) AS CreateDateString, ");
        query.append(" (COALESCE(u.Name,'') + COALESCE(' ' + u.FirstLastName,'') +COALESCE(' ' + u.SecondLastName,'')) as UserName ");
        query.append(" FROM REQUISITIONCONTRACTHISTORY AS h "); 
        query.append(" INNER JOIN VERSION AS v ON v.IdDocument = h.IdDocument ");
        query.append(" LEFT JOIN USERS AS u ON u.IdUser = v.IdUser ");
        query.append(" WHERE h.IdRequisition = :IdRequisition  ");
        query.append(" GROUP BY v.IdVersion, v.IdDocument, v.DocumentPath, v.VersionNumber, v.IdUser, v.CreateDate, "); 
        query.append(" u.Name, u.FirstLastName, u.SecondLastName ");
        query.append(" ORDER BY v.IdDocument DESC, v.versionNumber DESC "); 
        return query.toString();
    }
    
   
    @Override
    public void saveContractVersion(final Integer idRequisition, final Integer idDocument) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createContractParameters(idRequisition, idDocument);
            this.namedjdbcTemplate.update(this.buildInsertContractQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private MapSqlParameterSource createContractParameters(final Integer idRequisition, final Integer idDocument) {
    	final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableVersionConstants.ID_REQUISITION, idRequisition);
        namedParameters.addValue(TableVersionConstants.ID_DOCUMENT, idDocument);
        return namedParameters;
    }
    
    private String buildInsertContractQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONCONTRACTHISTORY (IdRequisition, IdDocument) ");
        query.append("VALUES (:IdRequisition, :IdDocument)");
        Log.info("###########################################");
        Log.info("IS NULLLLLLL"+query.toString());
        Log.info("###########################################");
        return query.toString();
    }
    
}
