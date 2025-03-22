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

import mx.pagos.admc.contracts.interfaces.TractoInterface;
import mx.pagos.admc.contracts.structures.Tracto;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class TractoDAO implements TractoInterface {
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;


	@Override
	public Integer save(Tracto tracto) throws DatabaseException {
		return this.insertTracto(tracto);
	}

	@Override
	public Integer saveOrUpdate(Tracto tracto) throws DatabaseException {
		return tracto.getIdTracto() == null ? this.insertTracto(tracto) : this.updateTracto(tracto);
	}

	@Override
	public List<Tracto> getTractoByIdRequisition(Integer idRequisition) throws DatabaseException {
		try {
			String SQL = "SELECT IdTracto, Brand, Model, FederalPlates, GPSProvider, Driver, TractoInsurancePolicyNumber, IdRequisition"
					+ " FROM Tracto " + "WHERE IdRequisition = :idRequisition";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRequisition", idRequisition);
			return namedjdbcTemplate.query(SQL, params, new BeanPropertyRowMapper<>(Tracto.class));

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener el tracto", dataAccessException);
		}
	}

	@Override
	public Tracto getTractoById(Integer idTracto) throws DatabaseException {
		try {
			String SQL = "SELECT IdTracto, Brand, Model, FederalPlates, GPSProvider, Driver, TractoInsurancePolicyNumber, IdRequisition"
					+ " FROM Tracto " + "WHERE IdTracto = :idTracto";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idTracto", idTracto);
			return namedjdbcTemplate.queryForObject(SQL, params, new BeanPropertyRowMapper<>(Tracto.class));

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener el tracto", dataAccessException);
		}
	}

	@Override
	public List<Tracto> getTractoAll() throws DatabaseException {
		try {
			String SQL = "SELECT IdTracto, Brand, Model, FederalPlates, GPSProvider, Driver, TractoInsurancePolicyNumber, IdRequisition"
					+ " FROM Tracto";
			return namedjdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Tracto.class));

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener los datos del tracto", dataAccessException);
		}
	}

	@Override
	public Integer deleteTractoById(Integer idTracto) throws DatabaseException {
		try {
			String SQL = "DELETE FROM Tracto " + "WHERE IdTracto = :idTracto";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idTracto", idTracto);
			this.namedjdbcTemplate.update(SQL, params);
			return idTracto;
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al eliminar el tracto", dataAccessException);
		}
	}

	@Override
	public Integer deleteTractoByIdRequisition(Integer idRequisition) throws DatabaseException {
		try {
			String SQL = "DELETE FROM Tracto " + "WHERE IdRequisition = :idRequisition";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRequisition", idRequisition);
			this.namedjdbcTemplate.update(SQL, params);
			return idRequisition;
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al eliminar El tracto", dataAccessException);
		}
	}

	private Integer insertTracto(Tracto tracto) throws DatabaseException {
		try {
			String SQL = "INSERT INTO Tracto "
					+ "(Brand, Model, FederalPlates, GPSProvider, Driver, TractoInsurancePolicyNumber, IdRequisition) "
					+ "VALUES(:brand, :model, :federalPlates, :gpsProvider, :driver, :tractoInsurancePolicyNumber, :idRequisition)";
			final MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("brand", tracto.getBrand());
			params.addValue("model", tracto.getModel());
			params.addValue("federalPlates", tracto.getFederalPlates());
			params.addValue("gpsProvider", tracto.getGpsProvider());
			params.addValue("driver", tracto.getDriver());
			params.addValue("tractoInsurancePolicyNumber", tracto.getTractoInsurancePolicyNumber());
			params.addValue("idRequisition", tracto.getIdRequisition());

			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(SQL, params, keyHolder, new String[] { "IdTracto" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al insertar el tracto", dataAccessException);
		}
	}

	private Integer updateTracto(Tracto tracto) throws DatabaseException {
		try {
			String SQL = "UPDATE Tracto "
					+ "SET Brand= :brand, Model= :model, FederalPlates=:federalPlates, GPSProvider= :gpsProvider, Driver= :driver, TractoInsurancePolicyNumber= :tractoInsurancePolicyNumber, IdRequisition= :idRequisition"
					+ " WHERE IdTracto = :idTracto";
			final MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("brand", tracto.getBrand());
			params.addValue("model", tracto.getModel());
			params.addValue("federalPlates", tracto.getFederalPlates());
			params.addValue("gpsProvider", tracto.getGpsProvider());
			params.addValue("driver", tracto.getDriver());
			params.addValue("tractoInsurancePolicyNumber", tracto.getTractoInsurancePolicyNumber());
			params.addValue("idRequisition", tracto.getIdRequisition());
			this.namedjdbcTemplate.update(SQL, params);
			return tracto.getIdTracto();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al actualizar el tracto", dataAccessException);
		}
	}
}
