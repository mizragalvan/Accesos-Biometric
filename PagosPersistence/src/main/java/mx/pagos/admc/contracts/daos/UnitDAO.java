package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.interfaces.UnitInterface;
import mx.pagos.admc.contracts.structures.Unit;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class UnitDAO implements UnitInterface {

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	@Override
	public Integer saveOrUpdate(final Unit unit) throws DatabaseException {
		 return unit.getIdUnit() == null ? this.insertUnit(unit) : this.updateUnit(unit);
	}
	
	private Integer insertUnit(Unit unit) throws DatabaseException {     
        try { 
        	String SQL ="INSERT INTO unit " + 
        			"(name, status, idCompany) " + 
        			"VALUES(:name, :status, :idCompany)";
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", unit.getName());
            params.addValue("status", unit.getStatus().name());
            params.addValue("idCompany", unit.getIdCompany());
            
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(SQL, params, keyHolder, 
                    new String[]{"IdUnit"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar la Unidad", dataAccessException);
        }
	}
	
	
	private Integer updateUnit(Unit unit) throws DatabaseException {     
        try { 
        	String SQL ="UPDATE unit " + 
        			"SET name=:name, status=:status, idCompany=:idCompany "+ 
        			"WHERE idUnit=:idUnit";
        	final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", unit.getName());
            params.addValue("status", unit.getStatus().name());
            params.addValue("idCompany", unit.getIdCompany());
            params.addValue("idUnit", unit.getIdUnit());
            
            this.namedjdbcTemplate.update(SQL, params);
            return unit.getIdUnit();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar la Unidad", dataAccessException);
        }
	}

	@Override
	public Unit getUnitById(Integer idUnit) throws DatabaseException {
		try { 
        	String SQL ="SELECT * FROM unit " + 
        			"WHERE idUnit =:idUnit";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("idUnit", idUnit);
            return namedjdbcTemplate.queryForObject(SQL, params, new BeanPropertyRowMapper<>(Unit.class));
           
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener la unidad", dataAccessException);
        }
	}

	@Override
	public List<Unit> getAll() throws DatabaseException {
		try { 
        	String SQL ="SELECT * FROM unit";
            return this.namedjdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Unit.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener las unidades", dataAccessException);
        }
	}

	@Override
	public Integer deleteUnitById(Integer idUnit) throws DatabaseException {
		try { 
        	String SQL ="DELETE FROM unit " + 
        			"WHERE idUnit=:idUnit";
        	MapSqlParameterSource params = new MapSqlParameterSource();
        	params.addValue("idUnit", idUnit);
            this.namedjdbcTemplate.update(SQL, params);
            return idUnit;
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al eliminar la Unidad", dataAccessException);
        }
	}

	
}
