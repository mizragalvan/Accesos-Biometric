package mx.pagos.admc.contracts.daos;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.interfaces.Customsable;
import mx.pagos.admc.contracts.structures.Customs;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class CustomsDAO implements Customsable {
	
	@Autowired
	private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	
	@Override
	public Integer saveOrUpdate(Customs customs) throws DatabaseException {
			  return customs.getIdCustoms() == null ? this.insertCustoms(customs) : this.updateCustoms(customs);
	}

	private Integer insertCustoms(Customs customs)throws DatabaseException {
		try { 
        	String SQL ="INSERT INTO CUSTOMS " + 
        			"(name,  idRequisition) " + 
        			"VALUES(:name, :idRequisition)";
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", customs.getName());
            params.addValue("idRequisition", customs.getIdRequisition());
            
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(SQL, params, keyHolder, 
                    new String[]{"IdUnit"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar la Aduana", dataAccessException);
        }
	}
	private Integer updateCustoms(Customs customs) throws DatabaseException{
		try { 
        	String SQL ="UPDATE CUSTOMS " + 
        			"SET name=:name,  idRequisition=:idRequisition "+ 
        			"WHERE IdCustoms=:IdCustoms";
        	final MapSqlParameterSource params = new MapSqlParameterSource();
        	 params.addValue("name", customs.getName());
             params.addValue("idRequisition", customs.getIdRequisition());
            params.addValue("IdCustoms", customs.getIdCustoms());
            
            this.namedjdbcTemplate.update(SQL, params);
            return customs.getIdCustoms();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar la Aduana", dataAccessException);
        }
	}
	@Override
	public Customs getCustomsById(Integer idCusInteger) throws DatabaseException {
		try { 
        	String SQL ="SELECT IdCustoms, Name, idRequisition FROM CUSTOMS" + 
        			"WHERE IdCustoms =:idCustoms";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("idCustoms", idCusInteger);
            return namedjdbcTemplate.queryForObject(SQL, params, new BeanPropertyRowMapper<>(Customs.class));
           
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener la Aduana", dataAccessException);
        }
	}

	@Override
	public List<Customs> getAll() throws DatabaseException {
		try { 
        	String SQL ="SELECT IdCustoms, Name, idRequisition"
        			+ "FROM CUSTOMS;";
            return this.namedjdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Customs.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener las aduanas", dataAccessException);
        }
	}

	@Override
	public Integer deleteCustomsById(Integer idCustoms) throws DatabaseException {
		try { 
        	String SQL ="DELETE FROM CUSTOMS " + 
        			"WHERE IdCustoms =:idCustoms";
        	MapSqlParameterSource params = new MapSqlParameterSource();
        	params.addValue("idCustoms", idCustoms);
            this.namedjdbcTemplate.update(SQL, params);
            return idCustoms;
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al eliminar la aduana", dataAccessException);
        }
	}
	@Override
	public List<Customs> getCustomsByIdRequisition(Integer idRequisition) throws DatabaseException {
		try { 
        	String SQL ="SELECT IdCustoms, Name, idRequisition FROM CUSTOMS " + 
        			"WHERE idRequisition =:idRequisition";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("idRequisition", idRequisition);
            return this.namedjdbcTemplate.query(SQL,params, new BeanPropertyRowMapper<>(Customs.class));
           
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener la Aduana", dataAccessException);
        }
	}
	@Override
	public Integer deleteCustomsByIdRequisition(Integer idRequisition) throws DatabaseException {
		try { 
        	String SQL ="DELETE FROM CUSTOMS " + 
        			"WHERE idRequisition =:idRequisition";
        	MapSqlParameterSource params = new MapSqlParameterSource();
        	params.addValue("idRequisition", idRequisition);
            this.namedjdbcTemplate.update(SQL, params);
            return idRequisition;
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al eliminar la Aduana", dataAccessException);
        }
	}
	@Override
	public Integer save(Customs customs) throws DatabaseException {
		
		return this.insertCustoms(customs);
	}

}
