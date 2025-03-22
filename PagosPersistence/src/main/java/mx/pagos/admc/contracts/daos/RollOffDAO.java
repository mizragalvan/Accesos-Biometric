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

import mx.pagos.admc.contracts.interfaces.RollOffInterface;
import mx.pagos.admc.contracts.structures.RollOff;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class RollOffDAO implements RollOffInterface {

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Override
	public Integer save(RollOff rollOff) throws DatabaseException {
		return this.insertRollOff(rollOff);
	}

	@Override
	public Integer saveOrUpdate(RollOff rollOff) throws DatabaseException {
		return rollOff.getIdRollOff() == null ? this.insertRollOff(rollOff) : this.updateRollOff(rollOff);
	}

	@Override
	public List<RollOff> getRollOffByIdRequisition(Integer idRequisition) throws DatabaseException {
		try {
			String SQL = "SELECT IdRollOff, Brand, Description, SerialNumber, ManufacturingDate, TareWeight, Dimensions, IdRequisition"
					+ " FROM ROLLOFF" + " WHERE IdRequisition = :idRequisition";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRequisition", idRequisition);
			return namedjdbcTemplate.query(SQL, params, new BeanPropertyRowMapper<>(RollOff.class));

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener datos del ROLLOFF", dataAccessException);
		}
	}

	@Override
	public RollOff getRollOffById(Integer idRollOff) throws DatabaseException {
		try {
			String SQL = "SELECT IdRollOff, Brand, Description, SerialNumber, ManufacturingDate, TareWeight, Dimensions, IdRequisition"
					+ " FROM ROLLOFF" + " WHERE IdRollOff = :idRollOff";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRollOff", idRollOff);
			return namedjdbcTemplate.queryForObject(SQL, params, new BeanPropertyRowMapper<>(RollOff.class));

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener datos del ROLLOFF", dataAccessException);
		}
	}

	@Override
	public List<RollOff> getRollOffAll() throws DatabaseException {
		try {
			String SQL = "SELECT IdRollOff, Brand, Description, SerialNumber, ManufacturingDate, TareWeight, Dimensions, IdRequisition"
					+ " FROM ROLLOFF" ;			
			return namedjdbcTemplate.query(SQL,  new BeanPropertyRowMapper<>(RollOff.class));

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener datos del ROLLOFF", dataAccessException);
		}
	}

	@Override
	public Integer deleteRollOffById(Integer idRollOff) throws DatabaseException {
		try {
			String SQL = "DELETE FROM ROLLOFF " 
		+ "WHERE IdRollOff = :idRollOff";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRollOff", idRollOff);
			this.namedjdbcTemplate.update(SQL, params);
			return idRollOff;
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al eliminar datos del ROLLOFF", dataAccessException);
		}
	}

	@Override
	public Integer deleteRollOffByIdRequisition(Integer idRequisition) throws DatabaseException {
		try {
			String SQL = "DELETE FROM ROLLOFF " 
		+ "WHERE IdRequisition = :idRequisition";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRequisition", idRequisition);
			this.namedjdbcTemplate.update(SQL, params);
			return idRequisition;
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al eliminar datos del ROLLOFF", dataAccessException);
		}
	}

	private Integer insertRollOff(RollOff rollOff) throws DatabaseException {
		try {
			String SQL = "INSERT INTO ROLLOFF "
					+ "(Brand, Description, SerialNumber, ManufacturingDate, TareWeight, Dimensions, IdRequisition) "
					+ "VALUES(:brand, :description, :serialNumber, :manufacturingDate, :tareWeight, :dimensions, :idRequisition)";
			final MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("brand", rollOff.getBrand());
			params.addValue("description", rollOff.getDescription());
			params.addValue("serialNumber", rollOff.getDescription());
			params.addValue("manufacturingDate", rollOff.getManufacturingDate());
			params.addValue("tareWeight", rollOff.getTareWeight());
			params.addValue("dimensions", rollOff.getDimensions());
			params.addValue("idRequisition", rollOff.getIdRequisition());

			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(SQL, params, keyHolder, new String[] { "IdUnit" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al insertar el ROLLOFF", dataAccessException);
		}
	}

	private Integer updateRollOff(RollOff rollOff) throws DatabaseException {
		try {
			String SQL = "UPDATE ROLLOFF "
					+ "SET Brand = :brand, Description= :description, SerialNumber= :serialNumber, ManufacturingDate= :manufacturingDate, TareWeight= :tareWeight, Dimensions= :dimensions, IdRequisition= :idRequisition)"
					+ " WHERE IdRollOff = :idRollOff";
			final MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRollOff", rollOff.getIdRollOff());
			params.addValue("brand", rollOff.getBrand());
			params.addValue("description", rollOff.getDescription());
			params.addValue("serialNumber", rollOff.getDescription());
			params.addValue("manufacturingDate", rollOff.getManufacturingDate());
			params.addValue("tareWeight", rollOff.getTareWeight());
			params.addValue("dimensions", rollOff.getDimensions());
			params.addValue("idRequisition", rollOff.getIdRequisition());

			 this.namedjdbcTemplate.update(SQL, params);
			return rollOff.getIdRollOff();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al insertar el ROLLOFF", dataAccessException);
		}
	}

}
