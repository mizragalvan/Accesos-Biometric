package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.interfaces.CatalogDocumentType;
import mx.pagos.admc.contracts.structures.CatDocumentType;
import mx.pagos.general.exceptions.DatabaseException;

@Repository("CatalogDocumentTypeDAO")
public class CatalogDocumentTypeDAO  implements CatalogDocumentType{

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	
	@Override
	public List<CatDocumentType> findAll() throws DatabaseException {
		try {
            return this.namedjdbcTemplate.query("SELECT * FROM CATALOGDOCTYPE", new MapSqlParameterSource(),
                    new BeanPropertyRowMapper<CatDocumentType>(CatDocumentType.class));   
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar áreas", dataAccessException);
        }
	}

}
