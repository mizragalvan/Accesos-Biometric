package mx.pagos.admc.contracts.daos;

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

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.Categoriable;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.structures.Category;
import mx.pagos.admc.contracts.structures.owners.CheckDocumentation;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class CategoriesDAO implements Categoriable {
    private static final String WHERE_ID_CATEGORY_ID_CATEGORY = "WHERE IdCategory = :IdCategory ";
    private static final String UPDATE_CATEGORY = QueryConstants.UPDATE + TableConstants.TABLE_CATEGORY;
    private static final String FROM_CATEGORY = QueryConstants.FROM + TableConstants.TABLE_CATEGORY;
    private static final String WHERE_ID_EQUALS_ID =
            QueryConstants.WHERE + TableConstants.ID_CATEGORY + QueryConstants.EQUAL_TAG + TableConstants.ID_CATEGORY;
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Autowired
    private DatabaseUtils databaseUtils;
    

    @Override
    public Integer saveOrUpdate(final Category category) throws DatabaseException {
        return category.getIdCategory() == null ? this.insertCategory(category) : this.updateCategory(category);
    }

    private Integer insertCategory(final Category category) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createInsertNamedParameters(category);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertCategoryQuery(), namedParameters, keyHolder, 
                    new String[]{"IdCategory"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al guardar la categoría", dataAccessException);
        }
    }
    
    private Integer updateCategory(final Category category) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createUpdateNamedParameters(category);
            this.namedjdbcTemplate.update(this.buildUpdateCategoryQuery(), namedParameters);
            return category.getIdCategory();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar la categoría", dataAccessException);
        }
    }

    @Override
    public void changeCategoryStatus(final Integer idCategory,
            final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createChangeStatusNamedParaemters(idCategory, status);
            this.namedjdbcTemplate.update(this.buildChangeStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al cambiar el estatus de la categoría", dataAccessException);
        }
    }

    @Override
    public List<Category> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildSelectAllQuery(),
                    new BeanPropertyRowMapper<Category>(Category.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener todas las categorías", dataAccessException);
        }
    }

    @Override
    public Category findById(final Integer idCategory) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindById(idCategory);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<Category>(Category.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al obtener la categoría", exception);
        }
    }

    @Override
    public List<Category> findByRecordStatus(final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
            return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<Category>(Category.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener las categorías por estatus", dataAccessException);
        }
    }
    
    private String buildChangeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_CATEGORY + QueryConstants.SET);
        query.append(TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS + QueryConstants.SPACE);
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private MapSqlParameterSource createChangeStatusNamedParaemters(final Integer idCategory,
            final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_CATEGORY, idCategory);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String buildFindByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildSelectAllQuery());
        query.append(QueryConstants.WHERE);
        query.append(TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS);
        return query.toString();
    }

    private MapSqlParameterSource createFindByStatusNamedParameters(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }

    public void deleteById(final Integer idCategory) {
        final MapSqlParameterSource namedParameters = this.createFindById(idCategory);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
    }
    
    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.DELETE_FROM + TableConstants.TABLE_CATEGORY);
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private String buildFindByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildSelectAllQuery());
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildSelectAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAll(query);
        return query.toString();
    }

    private void buildSelectAll(final StringBuilder query) {
        query.append(QueryConstants.SELECT);
        query.append(TableConstants.ID_CATEGORY + QueryConstants.COMMA);
        query.append(TableConstants.NAME + QueryConstants.COMMA);
        query.append(TableConstants.STATUS + QueryConstants.SPACE);
        query.append(FROM_CATEGORY);
    }

    private MapSqlParameterSource createFindById(final Integer idCategory) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_CATEGORY, idCategory);
        return namedParameters;
    }

    private String buildInsertCategoryQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.INSERT_INTO + TableConstants.TABLE_CATEGORY + QueryConstants.LEFT_BRACES);
        query.append(TableConstants.NAME);
        query.append(QueryConstants.RIGHT_BRACES);
        query.append(QueryConstants.VALUES + QueryConstants.TAG);
        query.append(TableConstants.NAME);
        query.append(QueryConstants.RIGHT_BRACES);
        return query.toString();
    }
    
    private MapSqlParameterSource createInsertNamedParameters(final Category category) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.NAME, category.getName());
        return namedParameters;
    }
    
    private String buildUpdateCategoryQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_CATEGORY + QueryConstants.SET);
        query.append(TableConstants.NAME + QueryConstants.EQUAL_TAG + TableConstants.NAME + QueryConstants.SPACE);
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private MapSqlParameterSource createUpdateNamedParameters(final Category category) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_CATEGORY, category.getIdCategory());
        namedParameters.addValue(TableConstants.NAME, category.getName());
        return namedParameters;
    }

    @Override
    public Integer saveCategoryCheckDocumentation(final Integer idCheckDocumentation,
            final Integer idCategory) throws DatabaseException {
        try {
            final MapSqlParameterSource  source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_CHECK_DOCUMENTATION, idCheckDocumentation);
            source.addValue(TableConstants.ID_CATEGORY, idCategory);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.saveCategoryCheckDocumentationQuery(), source, keyHolder, 
                    new String[]{"IdCategoryCheckDocumentation"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String saveCategoryCheckDocumentationQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO CATEGOYCHECKDOCUMENTATION(IdCheckDocumentation, IdCategory) ");
        builder.append("VALUES(:IdCheckDocumentation, :IdCategory) ");
        return builder.toString();
    }

    @Override
    public void deleteCategoryCheckDocumentation(final Integer idCategory) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_CATEGORY, idCategory);
            this.namedjdbcTemplate.update(this.deleteCategoryCheckDocumentationQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String deleteCategoryCheckDocumentationQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM CATEGOYCHECKDOCUMENTATION ");
        builder.append(WHERE_ID_CATEGORY_ID_CATEGORY);
        return builder.toString();
    }

    @Override
    public List<Integer> findCheckDocumentationIdsByCategory(final Integer idCategory) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_CATEGORY, idCategory);
            return this.namedjdbcTemplate.queryForList(this.findCheckDocumentationIdsByCategoryQuery(), 
                    source, Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findCheckDocumentationIdsByCategoryQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT CHECKDOCUMENTATION.IdCheckDocumentation FROM CATEGOYCHECKDOCUMENTATION ");
        builder.append("INNER JOIN CHECKDOCUMENTATION ON ");
        builder.append("CATEGOYCHECKDOCUMENTATION.IdCheckDocumentation = CHECKDOCUMENTATION.IdCheckDocumentation ");
        builder.append("WHERE CATEGOYCHECKDOCUMENTATION.IdCategory = :IdCategory ");
        builder.append("AND CHECKDOCUMENTATION.STATUS = 'ACTIVE' ");
        return builder.toString();
    }

    @Override
    public List<CheckDocumentation> findOwnerCheckDocumentationByCategory(final Integer idCategory) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_CATEGORY, idCategory);
            return this.namedjdbcTemplate.query(this.findOwnerCheckDocumentationByCategory(), 
                    source, new BeanPropertyRowMapper<CheckDocumentation>(CheckDocumentation.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findOwnerCheckDocumentationByCategory() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT REQUISITIONOWNERSCHECKDOC.IdRequisitionOwners, IdCheckDocumentation, ");
        builder.append("IdCategory  FROM CATEGOYCHECKDOCUMENTATION ");
        builder.append("INNER JOIN REQUISITIONOWNERSCHECKDOC ON ");
        builder.append("CATEGOYCHECKDOCUMENTATION.IdCategoryCheckDocumentation = ");
        builder.append("REQUISITIONOWNERSCHECKDOC.IdCategoryCheckDocumentation ");
        builder.append(WHERE_ID_CATEGORY_ID_CATEGORY);
        return builder.toString();
    }

    @Override
    public void deleteRequisitionOwnerCheckDocumentation(final Integer idCheckDocumentation,
            final Integer idCategory) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_CATEGORY, idCategory);
            source.addValue(TableConstants.ID_CHECK_DOCUMENTATION, idCheckDocumentation);
            this.namedjdbcTemplate.update(this.deleteRequisitionOwnerCheckDocumentationQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String deleteRequisitionOwnerCheckDocumentationQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM REQUISITIONOWNERSCHECKDOC ");
        builder.append("WHERE IdCategoryCheckDocumentation = ");
        builder.append("(SELECT IdCategoryCheckDocumentation FROM CATEGOYCHECKDOCUMENTATION ");
        builder.append("WHERE IdCheckDocumentation = :IdCheckDocumentation AND IdCategory = :IdCategory) ");
        return builder.toString();
    }

    @Override
    public List<Category> findAllCategoriesWithCheckDocumentation() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAllCategoriesWithCheckDocumentationQuery(), 
                    new BeanPropertyRowMapper<Category>(Category.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllCategoriesWithCheckDocumentationQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT CATEGORY.IdCategory, CATEGORY.Name, CATEGORY.Status, ");
        builder.append("CATEGOYCHECKDOCUMENTATION.IdCheckDocumentation ");
        builder.append("FROM CATEGORY LEFT JOIN CATEGOYCHECKDOCUMENTATION ");
        builder.append("ON CATEGORY.IdCategory = CATEGOYCHECKDOCUMENTATION.IdCategory ");
        return builder.toString();
    }

    @Override
    public List<Category> findAllCategoryCatalogPaged(final RecordStatusEnum status, 
            final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
                    this.findAllCategoryCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<Category>(Category.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllCategoryCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.buildSelectAll(builder);
        builder.append("WHERE :Status IS NULL OR Status = :Status ");
        builder.append("ORDER BY Name ASC ");
        return builder.toString();
    }

    private MapSqlParameterSource statusParameter(final RecordStatusEnum status) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.STATUS, status == null ? null : status.toString());
        return source;
    }

    @Override
    public Long countTotalItemsToShowOfCategory(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = this.databaseUtils.countTotalRows(this.findAllCategoryCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
