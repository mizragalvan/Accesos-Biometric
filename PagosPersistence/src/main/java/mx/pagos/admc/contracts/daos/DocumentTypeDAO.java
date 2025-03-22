package mx.pagos.admc.contracts.daos;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.DocumentTypeable;
import mx.pagos.admc.contracts.structures.CatDocumentType;
import mx.pagos.admc.contracts.structures.DocumentType;
import mx.pagos.admc.enums.DocumentTypeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class DocumentTypeDAO implements DocumentTypeable {
    private static final String FROM_DOCUMENTTYPE = "FROM DOCUMENTTYPE ";
    private static final String FROM_DOCUMENT = "FROM CATALOGDOCTYPE ";
    private static final String UPDATE_DOCUMENT_TYPE =
            QueryConstants.UPDATE + TableConstants.TABLE_DOCUMENT_TYPE + QueryConstants.SET;
    private static final String WHERE_ID_EQUALS_ID =
            QueryConstants.WHERE + TableConstants.ID_DOCUMENT_TYPE +
            QueryConstants.EQUAL_TAG + TableConstants.ID_DOCUMENT_TYPE;
    
    private static final String WHERE_ID_EQUALS_CATDOC_ID =
            QueryConstants.WHERE + TableConstants.ID_DOCUMENT +
            QueryConstants.EQUAL_TAG + TableConstants.ID_DOCUMENT;
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    private static final String NULL = "Null";
    private static final String LIKE = "%";
    
    @Autowired
    private DatabaseUtils databaseUtils;

    @Override
    public Integer saveOrUpdate(final DocumentType documentType) throws DatabaseException {
        return documentType.getIdDocumentType() == null ?
                this.insertDocumentType(documentType) : this.updateDocumentType(documentType);
    }

    private Integer insertDocumentType(final DocumentType documentType) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(documentType);
            namedParameters.registerSqlType(TableConstants.DOCUMENT_TYPE_ENUM, Types.VARCHAR);
            final KeyHolder keyholder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertDocumentTypeQuery(), namedParameters, keyholder, 
                    new String[]{"IdDocumentType"});
            return keyholder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar el tipo de documento", dataAccessException);
        }
    }
    
    private Integer updateDocumentType(final DocumentType documentType) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(documentType);
            namedParameters.registerSqlType(TableConstants.DOCUMENT_TYPE_ENUM, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.builUpdateDocumentTypeQuery(), namedParameters);
            return documentType.getIdDocumentType();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar el tipo de documento", dataAccessException);
        }
    }
    
    @Override
    public DocumentType findById(final Integer idDocumentType)
            throws DatabaseException {
        try {
            System.out.println("DocumentTypeDAO -> findById ");
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParaemters(idDocumentType);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<DocumentType>(DocumentType.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al recuperar el tipo de documento", exception);
        }
    }

    @Override
    public void changeDocumentTypeStatus(final Integer idDocumentType,
            final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createChangeDocumentTypeStatusnamedParameters(idDocumentType, status);
            this.namedjdbcTemplate.update(this.buildChangeDocumentTypeStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException)  {
            throw new DatabaseException("Error al cambiar el estatus del tipo de documento", dataAccessException);
        }
    }

    @Override
    public List<DocumentType> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildSelectAll(),
                    new BeanPropertyRowMapper<DocumentType>(DocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener todas las categorías", dataAccessException);
        }
    }

    @Override
    public List<DocumentType> findByRecordStatus(final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
            return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<DocumentType>(DocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener las categorías por estatus", dataAccessException);
        }
    }
    
    @Override
    public Boolean nameExists(final String name) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createNameExistsNamedParameters(name);
            return this.namedjdbcTemplate.queryForObject(this.buildNameExistsQuery(), namedParameters, Boolean.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildFindByStatusQuery() {
        final StringBuilder query = this.selectAllQuery();
        query.append("WHERE DOCUMENTTYPE.Status = :Status");
        return query.toString();
    }

    private MapSqlParameterSource createFindByStatusNamedParameters(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }

    private String buildChangeDocumentTypeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_DOCUMENT_TYPE);
        query.append(TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS + QueryConstants.SPACE);
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private MapSqlParameterSource createChangeDocumentTypeStatusnamedParameters(final Integer idDocumentType,
            final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_DOCUMENT_TYPE, idDocumentType);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String buildInsertDocumentTypeQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO DOCUMENTTYPE (");
        this.buildNonPrimaryKeyFields(query);
        query.append(") VALUES (:Name, :TemplatePath, :TemplatePathNaturalPerson, :IsDiferentTemplateForPersonality, ");
        query.append(":DocumentTypeEnum, :ActorActivo, :ActorPasivo, :SelectionLimit, :IsHideClausesNumbers)");
        return query.toString();
    }

    private void buildNonPrimaryKeyFields(final StringBuilder query) {
        query.append("Name, TemplatePath, TemplatePathNaturalPerson, IsDiferentTemplateForPersonality, ");
        query.append("DocumentTypeEnum, ActorActivo, ActorPasivo, SelectionLimit, IsHideClausesNumbers ");
    }
    
    private String buildSelectAll() {
        return this.selectAllQuery().toString();
    }

    private StringBuilder selectAllQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdDocumentType, Name, TemplatePath, TemplatePathNaturalPerson, ");
        query.append("IsDiferentTemplateForPersonality, Status, DocumentTypeEnum AS DocType, ActorActivo, ActorPasivo, ");
        query.append("SelectionLimit, IsHideClausesNumbers ");
        query.append(FROM_DOCUMENTTYPE);
        return query;
    }
    private StringBuilder selectAllDocCatQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdDocument, Name, Status ");
        query.append(FROM_DOCUMENT);
        query.append(WHERE_ID_EQUALS_CATDOC_ID);
        return query;
    }
    private String builUpdateDocumentTypeQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_DOCUMENT_TYPE);
        query.append(TableConstants.NAME + QueryConstants.EQUAL_TAG + TableConstants.NAME + QueryConstants.COMMA);
        query.append(TableConstants.TEMPLATE_PATH + QueryConstants.EQUAL_TAG);
        query.append(TableConstants.TEMPLATE_PATH + QueryConstants.COMMA);
        query.append("TemplatePathNaturalPerson = :TemplatePathNaturalPerson, ");
        query.append("IsDiferentTemplateForPersonality = :IsDiferentTemplateForPersonality, ");
        query.append(TableConstants.DOCUMENT_TYPE_ENUM + QueryConstants.EQUAL_TAG + TableConstants.DOCUMENT_TYPE_ENUM);
        query.append(", ActorActivo = :ActorActivo, ActorPasivo = :ActorPasivo, SelectionLimit = :SelectionLimit, ");
        query.append("IsHideClausesNumbers = :IsHideClausesNumbers ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    public void deleteById(final Integer idDocumentType) {
        final MapSqlParameterSource namedParameters = this.createFindByIdNamedParaemters(idDocumentType);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
    }

    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.DELETE_FROM + TableConstants.TABLE_DOCUMENT_TYPE);
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private String buildFindByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildSelectAll());
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private MapSqlParameterSource createFindByIdNamedParaemters(final Integer idDocumentType) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_DOCUMENT_TYPE, idDocumentType);
        return namedParameters;
    }
    
    private MapSqlParameterSource createFindByIdDocCatNamedParaemters(final Integer idDocument) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_DOCUMENT, idDocument);
        return namedParameters;
    }
    
    private String buildNameExistsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(1) AS nameExists ");
        query.append(FROM_DOCUMENTTYPE);
        query.append("WHERE Name = :Name ");
        return query.toString();
    }
    
    private MapSqlParameterSource createNameExistsNamedParameters(final String name) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.NAME, name);
        return namedParameters;
    }

    @Override
    public List<DocumentType> findDocumentTypeStatusAndDocumentType(
            final DocumentType documentType, final DocumentTypeEnum documentTypeEnum) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.documentTypeParameters(documentType, documentTypeEnum);
            return this.namedjdbcTemplate.query(this.findDocumentTypeByAreaStatusAndDocumentTypeQuery(), source,
                    new BeanPropertyRowMapper<DocumentType>(DocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private MapSqlParameterSource documentTypeParameters(
            final DocumentType documentType, final DocumentTypeEnum documentTypeEnum) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.STATUS, documentType.getStatus() == null ? null : documentType.getStatus().toString());
        source.addValue(TableConstants.DOCUMENT_TYPE_ENUM, documentTypeEnum == null ? null : documentTypeEnum.toString());
        source.addValue(TableConstants.NAME, LIKE + documentType.getName() + LIKE);
        source.addValue(TableConstants.NAME + NULL, documentType.getName());
        		
        return source;
    }
    
    private String findDocumentTypeByAreaStatusAndDocumentTypeQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdDocumentType, Name, IsDiferentTemplateForPersonality, Status, DocumentTypeEnum, ");
        builder.append("ActorActivo, ActorPasivo, SelectionLimit, IsHideClausesNumbers ");
        builder.append(FROM_DOCUMENTTYPE);
        builder.append("WHERE (:Status IS NULL OR DOCUMENTTYPE.Status = :Status) ");
        builder.append("AND (:DocumentTypeEnum IS NULL OR DocumentTypeEnum = :DocumentTypeEnum) ");
        builder.append("AND (:NameNull IS NULL OR DOCUMENTTYPE.Name LIKE :Name) ");        
        builder.append("ORDER BY Name ASC ");
        return builder.toString();
    }

    @Override
    public List<DocumentType> findAllDocumentTypeCatalogPaged(final DocumentType documentType, 
            final DocumentTypeEnum documentTypeEnum, final Integer pagesNumber, final Integer itemsNumber)
                    throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.documentTypeParameters(documentType, documentTypeEnum);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
                    this.findDocumentTypeByAreaStatusAndDocumentTypeQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<DocumentType>(DocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Long countTotalItemsToShowOfDocumentType(
            final DocumentType documentType, final DocumentTypeEnum documentTypeEnum) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.documentTypeParameters(documentType, documentTypeEnum);
            final String countItems = 
                    this.databaseUtils.countTotalRows(this.findDocumentTypeByAreaStatusAndDocumentTypeQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

	@Override
	public CatDocumentType findByIdCatDocument(Integer idDocumentType) throws DatabaseException {
		// TODO Auto-generated method stub
		try {
            final MapSqlParameterSource namedParameters = this.createFindByIdDocCatNamedParaemters(idDocumentType);
            return this.namedjdbcTemplate.queryForObject(this.selectAllDocCatQuery().toString(), namedParameters,
                    new BeanPropertyRowMapper<CatDocumentType>(CatDocumentType.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al recuperar el tipo de documento", exception);
        }
	}
}
