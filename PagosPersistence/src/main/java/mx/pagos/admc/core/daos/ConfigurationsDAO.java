package mx.pagos.admc.core.daos;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.core.enums.Category;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.core.structures.Configuration;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class ConfigurationsDAO implements Configurable {
	private static final String claseName = "ConfigurationsDAO: ";

	private static final Logger log = LoggerFactory.getLogger(ConfigurationsDAO.class);

	private static final String CATEGORY = "Category";
	private static final String NAMEFIELD = "Name";

	@Autowired
	private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Override
	public void update(final Configuration configuration) throws DatabaseException {
		try {
			log.info(claseName + "Se inicia cambio de Configuración " + configuration.getName());
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(configuration);
			namedParameters.registerSqlType(CATEGORY, Types.VARCHAR);
			namedParameters.registerSqlType("ValueType", Types.VARCHAR);
			this.namedjdbcTemplate.update(this.updateConfigurationQuery(), namedParameters);
		} catch (DataAccessException e) {
			log.error(claseName + "Error al cambiar la configuración: " + e.getMessage());
			e.printStackTrace();
			throw new DatabaseException(e);
		}
	}

	@Override
	public void updateValue(final Configuration configuration) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(configuration);
			namedParameters.registerSqlType(CATEGORY, Types.VARCHAR);
			this.namedjdbcTemplate.update(this.updateValueConfigurationQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public String findByName(final String name) throws DatabaseException {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(NAMEFIELD, name);
		String result = this.namedjdbcTemplate.queryForObject(this.findByNameQuery(), namedParameters, String.class);
		try {
			switch (name) {
			case "ROOT_PATH":
				if (!System.getProperty("os.name").toLowerCase().contains("windows"))
					result = findByName(ConfigurationEnum.ROOT_PATH_2.toString());
				break;
			case "EMAIL_TEMPLATES_PATH":
				if (!System.getProperty("os.name").toLowerCase().contains("windows"))
					result = findByName(ConfigurationEnum.EMAIL_TEMPLATES_PATH_2.toString());
				break;
			default:
			}
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
		return result;
	}

	@Override
	public List<Configuration> findByNameList(final List<String> nameList) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByNameListParameters(nameList);
			return this.namedjdbcTemplate.query(this.findByNameListQuery(), namedParameters,
					new BeanPropertyRowMapper<Configuration>(Configuration.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Configuration> findAll() throws DatabaseException {
		try {
			return this.namedjdbcTemplate.query(this.buildFindAllQuery(), new MapSqlParameterSource(),
					new BeanPropertyRowMapper<Configuration>(Configuration.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildFindAllQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		return query.toString();
	}

	public void saveConfiguration(final Configuration configuration) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(configuration);
			namedParameters.registerSqlType(CATEGORY, Types.VARCHAR);
			this.namedjdbcTemplate.update(this.buildSaveConfigurationQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	public void deleteConfiguration(final String name) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(NAMEFIELD, name);
		this.namedjdbcTemplate.update(this.deleteConfigurationQuery(), namedParameters);
	}

	public Integer countAll() {
		return this.namedjdbcTemplate.queryForObject(this.buildCountAll(), new MapSqlParameterSource(), Integer.class);
	}

	private String buildCountAll() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(Name) FROM CONFIGURATION");
		return query.toString();
	}

	private String deleteConfigurationQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM CONFIGURATION WHERE Name = :Name");
		return query.toString();
	}

	private String buildSaveConfigurationQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO CONFIGURATION (Name, Value, Description, Category) ");
		query.append("VALUES (:Name, :Value, :Description, :Category)");
		return query.toString();
	}

	private String updateConfigurationQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE CONFIGURATION SET Value = :Value, Description = :Description, ");
		query.append("ValueType = :ValueType WHERE Name = :Name ");
		return query.toString();
	}

	private String updateValueConfigurationQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE CONFIGURATION SET Value = :Value WHERE Name = :Name");
		return query.toString();
	}

	private void buildSelectAllQuery(final StringBuilder query) {
		query.append("SELECT Name, Value, Description, Category FROM CONFIGURATION ");
	}

	private String findByNameQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT Value FROM CONFIGURATION WHERE Name = :Name");
		return query.toString();
	}

	private MapSqlParameterSource createFindByNameListParameters(final List<String> nameList) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		final Map<String, List<String>> nameMap = new HashMap<String, List<String>>();
		nameMap.put(NAMEFIELD, nameList);
		namedParameters.addValues(nameMap);
		return namedParameters;
	}

	private String findByNameListQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append("WHERE Name IN (:Name)");
		return query.toString();
	}

	@Override
	public List<Configuration> findConfigurationByCategory(final Category categoryType) throws DatabaseException {
		try {
			final MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue(TableConstants.CATEGORY_TYPE, categoryType.toString());
			return this.namedjdbcTemplate.query(this.findConfigurationByCategoryquery(), source,
					new BeanPropertyRowMapper<Configuration>(Configuration.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findConfigurationByCategoryquery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT Name, Value, Description, RiskLevel, ValueType ");
		builder.append("FROM CONFIGURATION ");
		builder.append("WHERE Category = :Category ");
		builder.append("ORDER BY Name ");
		return builder.toString();
	}

}
