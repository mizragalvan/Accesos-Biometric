package mx.pagos.security.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.constants.TableConstantsSecurity;
import mx.pagos.security.interfaces.Menuable;
import mx.pagos.security.structures.Menu;

/**
 * Clase que contiene el manejo del menu, permite guardar las opciones de menú en la base de datos.
 * 
 * @see Menu
 */
@Repository
public class MenuDAO implements Menuable {
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Override
    public String save(final Menu menu) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(menu);
            this.namedjdbcTemplate.update(this.buildInsertMenuQuery(), namedParameters);
            return menu.getFactoryName();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar el menu", dataAccessException);
        }
    }

    private String buildInsertMenuQuery() {
        final StringBuilder queryInsert = new StringBuilder();
        queryInsert.append("INSERT INTO MENU(FactoryName,MenuLevel,FactoryNameParent)");
        queryInsert.append(" VALUES(:FactoryName,:MenuLevel,:FactoryNameParent)");
        return queryInsert.toString();
    }

    @Override
    public void deleteByFactoryName(final String factoryName) throws DatabaseException {
    	   try {
	            final MapSqlParameterSource namedParameters =
	                    new MapSqlParameterSource(TableConstantsSecurity.FACTORY_NAME, factoryName);
	            this.namedjdbcTemplate.update(this.buildDeleteByFactoryNameQuery(), namedParameters);
	        } catch (DataAccessException dataAccessException) {
	            throw new DatabaseException(dataAccessException);
	        }
    }

 
    private String buildDeleteByFactoryNameQuery() {
        final StringBuilder queryDelete = new StringBuilder();
        queryDelete.append("DELETE FROM MENU WHERE FactoryName = :FactoryName");
        return queryDelete.toString();
    }
    
	@Override
	public List<Menu> findAll() throws DatabaseException {
	        try {
	            return this.namedjdbcTemplate.query(this.buildSelectAllQuery(),
	                    new BeanPropertyRowMapper<Menu>(Menu.class));
	        } catch (DataAccessException dataAccessException) {
	            throw new DatabaseException("Error al obtener todas los Menus", dataAccessException);
	        }
	}
	
	private String buildSelectAllQuery() {
	    final StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM MENU");
        return query.toString();
	}
}
