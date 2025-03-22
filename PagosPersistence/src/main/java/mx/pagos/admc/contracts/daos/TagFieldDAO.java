package mx.pagos.admc.contracts.daos;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.TagFieldable;
import mx.pagos.admc.contracts.structures.TagField;
import mx.pagos.general.exceptions.DatabaseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TagFieldDAO implements TagFieldable {
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Override
    public List<TagField> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindAllQuery(), 
                    new BeanPropertyRowMapper<TagField>(TagField.class));   
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<TagField> findByTable(final String table) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByTableNamedParameters(table);
            return this.namedjdbcTemplate.query(this.buildfindByTableQuery(), namedParameters,
                    new BeanPropertyRowMapper<TagField>(TagField.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildfindByTableQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("WHERE TableName = :TableName");
        return query.toString();
    }

    private MapSqlParameterSource createFindByTableNamedParameters(final String tableName) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.TABLE_NAME, tableName);
        return namedParameters;
    }

    private String buildFindAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }

    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT Tag, Field, TableName ");
        query.append("FROM TAGFIELD ");
    }
}
