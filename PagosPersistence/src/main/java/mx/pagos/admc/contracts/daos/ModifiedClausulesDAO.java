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

import mx.pagos.admc.contracts.interfaces.ModifiedClausuleable;
import mx.pagos.admc.contracts.structures.ModifiedClausules;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class ModifiedClausulesDAO implements ModifiedClausuleable {
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	@Override
	public Integer save(ModifiedClausules clausules) throws DatabaseException {
		 return this.insertClausules(clausules) ;
	}

	private Integer insertClausules(ModifiedClausules clausules) throws DatabaseException {
		try { 
        	String SQL ="INSERT INTO MODIFIEDCLAUSES " + 
        			"(PreviousClause, NewClause,NameClause, NumberClause, IdRequisition) " + 
        			"VALUES(:previousClause, :newClause, :nameClause, :numberClause,:idRequisition)";
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("previousClause",clausules.getPreviousClause());
            params.addValue("newClause", clausules.getNewClause());
            params.addValue("numberClause", clausules.getNumberClause());
            params.addValue("nameClause", clausules.getNameClause());
            params.addValue("idRequisition", clausules.getIdRequisition());
            
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(SQL, params, keyHolder, 
                    new String[]{"IdModifiedClause"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar la clausula", dataAccessException);
        }
	}
	private Integer updateClausules(ModifiedClausules clausules) throws DatabaseException {
		try { 
        	String SQL ="UPDATE MODIFIEDCLAUSES " + 
        			"SET PreviousClause = :previousClause, NewClause = :newClause, NameClause= :nameClause, NumberClause = :numberClause, IdRequisition = :idRequisition "+ 
        			"WHERE IdModifiedClause = :idModifiedClause";
        	final MapSqlParameterSource params = new MapSqlParameterSource();
        	 params.addValue("previousClause",clausules.getPreviousClause());
             params.addValue("newClause", clausules.getNewClause());
             params.addValue("numberClause", clausules.getNumberClause());
             params.addValue("nameClause", clausules.getNameClause());
             params.addValue("idRequisition", clausules.getIdRequisition());
             params.addValue("idModifiedClause", clausules.getIdModifiedClause());
            
            this.namedjdbcTemplate.update(SQL, params);
            return clausules.getIdModifiedClause();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar la clausula", dataAccessException);
        }
	}
	@Override
	public Integer saveOrUpdate(ModifiedClausules clausules) throws DatabaseException {
		return clausules.getIdModifiedClause()== null ? this.insertClausules(clausules) : this.updateClausules(clausules);
	}

	@Override
	public ModifiedClausules getClausulesById(Integer idModifiedClause) throws DatabaseException {
		try { 
        	String SQL ="SELECT IdModifiedClause, PreviousClause, NewClause, NumberClause, IdRequisition FROM MODIFIEDCLAUSES" + 
        			"WHERE IdModifiedClause = :idModifiedClause";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("idModifiedClause", idModifiedClause);
            return namedjdbcTemplate.queryForObject(SQL, params, new BeanPropertyRowMapper<>(ModifiedClausules.class));
           
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener la clausula", dataAccessException);
        }
	}

	@Override
	public List<ModifiedClausules> getClausulesByIdRequisition(Integer idRequisition) throws DatabaseException {
		try { 
        	String SQL ="SELECT IdModifiedClause, PreviousClause, NewClause, NameClause, NumberClause, IdRequisition FROM MODIFIEDCLAUSES " + 
        			"WHERE IdRequisition = :idRequisition";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("idRequisition", idRequisition);
            return namedjdbcTemplate.query(SQL, params, new BeanPropertyRowMapper<>(ModifiedClausules.class));
           
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener la clausula", dataAccessException);
        }
	}

	@Override
	public List<ModifiedClausules> getAll() throws DatabaseException {
		try { 
        	String SQL ="SELECT IdModifiedClause, PreviousClause, NameClause,,NewClause, NumberClause, IdRequisition"
        			+ " FROM MODIFIEDCLAUSES";            
            return namedjdbcTemplate.query(SQL,  new BeanPropertyRowMapper<>(ModifiedClausules.class));
           
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener la clausula", dataAccessException);
        }
	}

	@Override
	public Integer deleteClausulesById(Integer idModifiedClause) throws DatabaseException {
		try { 
        	String SQL ="DELETE FROM MODIFIEDCLAUSES " + 
        			"WHERE IdModifiedClause = :idModifiedClause";
        	MapSqlParameterSource params = new MapSqlParameterSource();
        	params.addValue("IdModifiedClause", idModifiedClause);
            this.namedjdbcTemplate.update(SQL, params);
            return idModifiedClause;
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al eliminar la clausula", dataAccessException);
        }
	}
	

	@Override
	public Integer deleteClausulesByIdRequisition(Integer idRequisition) throws DatabaseException {
		try { 
        	String SQL ="DELETE FROM MODIFIEDCLAUSES " + 
        			"WHERE IdRequisition = :idRequisition";
        	MapSqlParameterSource params = new MapSqlParameterSource();
        	params.addValue("idRequisition", idRequisition);
            this.namedjdbcTemplate.update(SQL, params);
            return idRequisition;
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al eliminar la clausula", dataAccessException);
        }
	}

}
