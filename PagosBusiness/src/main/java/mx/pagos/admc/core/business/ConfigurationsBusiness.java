package mx.pagos.admc.core.business;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.pagos.admc.contracts.structures.Clause;
import mx.pagos.admc.core.enums.Category;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.core.structures.Configuration;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ParametersHolder;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.ConfigurationException;
import mx.pagos.general.exceptions.DatabaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mizraim 
 */
@Service
public class ConfigurationsBusiness {

	private static final Logger log = LoggerFactory.getLogger(ConfigurationsBusiness.class);
	private static final String claseName = "ConfigurationsBusiness: ";

	private static final String MESSAGE_GENERAL_REPORT_YELLOW_SEMAPHORE_BAD_CONFIG_ERROR = "La configuración del porcentaje del semáforo amarillo del reporte general es incorrecta";
	private static final String MESSAGE_RETRIVING_GENERAL_REPORT_YELLOW_SEMAPHORE_ERROR = "Hubo un problema al recuperar el porcentaje para el semáforo amarillo del reporte general";
	private static final String MESSAGE_GENERAL_REPORT_RED_SEMAPHORE_BAD_CONFIG_ERROR = "La configuración del porcentaje del semáforo rojo del reporte general es incorrecta";
	private static final String MESSAGE_RETRIVING_GENERAL_REPORT_RED_SEMAPHORE_ERROR = "Hubo un problema al recuperar el porcentaje para el semáforo rojo del reporte general";
	private static final String MESSAGE_SEND_MEAILS_CONFIG_ERROR = "La configuración de activación/desactivación de envío de correctos es incorrecta";
	private static final String MESSAGE_RETRIEVING_ROOT_PATH_ERROR = "Hubo un problema al recuperar el path raíz";
	private static final String MESSAGE_RETRIEVING_TEMP_PATH_ERROR = "Hubo un problema al recuperar el path de los archivos temporales";
	private static final String MESSAGE_RETRIEVING_LOG_PATH_ERROR = "Hubo un problema al recuperar el path de los archivos de log";
	private static final String ERRORCONFIG = "Hubo un problema al obtener las configuraciones";
	private static final String MESSAGE_SAVING_CONFIGURATION_ERROR = "Hubo un problema al guardar la configuración";
	private static final String MESSAGE_FIND_CONFIGURATION_BY_CATEGORY_ERROR = "Hubo un problema al buscar las configuraciones por categoría";
	private static final String MESSAGE_PARAMETER_PAGINATION_ITEMS_NUMBER_NOT_A_NUMBER_ERROR = "La configuración del sistema del parámetro PAGINATION_ITEMS_NUMBER no es un número entero";
	private static final String MESSAGE_RETRIEVING_EXPORTED_CATALOGS_PATH_ERROR = "Hubo un problema al recuperar el path de los catálogos exportados";
	private static final String MESSAGE_RETRIVING_GENERAL_REPORT_PATH_ERROR = "Hubo un problema la recuperar la ruta del reporte general";
	private static final String MESSAGE_RETRIVING_GENERAL_REPORT_LATESTS_MONTHS_ERROR = "Hubo un problema al recuperar la configuración del número de meses para el reporte general de compras";
	private static final String MESSAGE_RETRIVING_GENERAL_REPORT_LATESTS_MONTHS_CONFIG_ERROR = "La configuración del número de meses del reporte general de compras es incorrecta";

	private static final String MESSAGE_RETRIVING_GENERAL_REPORT_EMAILS_ERROR = "Hubo un problema al recuperar los correos a donde se enviarán las notificaciones"
			+ "de la creación del reporte general";
	private static final String MESSAGE_RETRIVING_AUTOSAVE_TIME_SECONDS_ERROR = "Hubo un problema al recuperar el tiempo de autoguardado";
	private static final String MESSAGE_RETRIVING_AUTOSAVE_TIME_SECONDS_CONFIG_ERROR = "La configuración del tiempo de autoguardado no es un número de segundos válido";

	@Autowired
	private Configurable configurable;

	public final void update(final Configuration configuration) throws BusinessException {
		try {
			log.info(claseName + "Se procesa cambio de Configuración");
			this.configurable.update(configuration);
		} catch (DatabaseException e) {
			log.error(ERRORCONFIG, e);
			throw new BusinessException(MESSAGE_SAVING_CONFIGURATION_ERROR, e);
		}
	}

	public final void update(final String name, final String value) throws BusinessException {
		try {
			final Configuration configuration = new Configuration(name, value);
			this.configurable.updateValue(configuration);
		} catch (DatabaseException databaseException) {
			log.error(ERRORCONFIG, databaseException);
			throw new BusinessException(MESSAGE_SAVING_CONFIGURATION_ERROR, databaseException);
		}
	}

	public final String findByName(final String name) throws BusinessException {
		try {
			return this.configurable.findByName(name);
		} catch (DatabaseException databaseException) {
			log.error(ERRORCONFIG, databaseException);
			throw new BusinessException(ERRORCONFIG, databaseException);
		}
	}

	public final List<Configuration> findByNamesList(final List<String> configurationList) throws BusinessException {
		final List<Configuration> configurations = new ArrayList<Configuration>();
		try {
			Configuration configuration = new Configuration();
			for (String configurationName : configurationList) {
				configuration = new Configuration();
				configuration.setName(configurationName);
				configuration.setValue(this.configurable.findByName(configurationName));
				configurations.add(configuration);
			}
			return configurations;
		} catch (DatabaseException databaseException) {
			log.error(ERRORCONFIG, databaseException);
			throw new BusinessException(ERRORCONFIG, databaseException);
		}
	}

	public final List<Configuration> findAll() throws BusinessException {
		try {
			return this.configurable.findAll();
		} catch (DatabaseException databaseException) {
			log.error(ERRORCONFIG, databaseException);
			throw new BusinessException(ERRORCONFIG, databaseException);
		}
	}

	public final List<Configuration> findConfigurationsByCategory(final Category category) throws BusinessException {
		try {
			return this.configurable.findConfigurationByCategory(category);
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_FIND_CONFIGURATION_BY_CATEGORY_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_CONFIGURATION_BY_CATEGORY_ERROR, databaseException);
		}
	}

	public final Integer getPaginationItemsNumberParameter() throws BusinessException {
		try {
			return Integer.parseInt(this.findByName(ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString()));
		} catch (NumberFormatException numberFormatException) {
			log.error(MESSAGE_PARAMETER_PAGINATION_ITEMS_NUMBER_NOT_A_NUMBER_ERROR, numberFormatException);
			throw new BusinessException(MESSAGE_PARAMETER_PAGINATION_ITEMS_NUMBER_NOT_A_NUMBER_ERROR, numberFormatException);
		}
	}

	public final Integer totalPages(final Long rowsNum) throws BusinessException {
		return (int) Math.ceil(rowsNum / Double.valueOf(this.getPaginationItemsNumberParameter()));
	}

	public final String getRootPath() throws ConfigurationException {
		try {
			return this.configurable.findByName(ConfigurationEnum.ROOT_PATH.toString());
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_RETRIEVING_ROOT_PATH_ERROR, databaseException);
			throw new ConfigurationException(MESSAGE_RETRIEVING_ROOT_PATH_ERROR, databaseException);
		}
	}

	public final String getTemporalPath() throws ConfigurationException {
		try {
			return this.configurable.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_TMP;
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_RETRIEVING_TEMP_PATH_ERROR, databaseException);
			throw new ConfigurationException(MESSAGE_RETRIEVING_TEMP_PATH_ERROR, databaseException);
		}
	}

	public final String getLogPath() throws ConfigurationException {
		try {
			return this.configurable.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.LOG + File.separator;
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_RETRIEVING_LOG_PATH_ERROR, databaseException);
			throw new ConfigurationException(MESSAGE_RETRIEVING_LOG_PATH_ERROR, databaseException);
		}
	}

	public final String getExportedCatalogsPath() throws ConfigurationException {
		try {
			return this.configurable.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_EXPORTED_CATALOGS
					+ File.separator;
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_RETRIEVING_EXPORTED_CATALOGS_PATH_ERROR, databaseException);
			throw new ConfigurationException(MESSAGE_RETRIEVING_EXPORTED_CATALOGS_PATH_ERROR, databaseException);
		}
	}

	public final Boolean getIsEmailsEndActive() throws ConfigurationException {
		try {
			return Boolean.valueOf(this.findByName(ConfigurationEnum.SEND_MAILS.toString()));
		} catch (BusinessException exception) {
			log.error(MESSAGE_SEND_MEAILS_CONFIG_ERROR, exception);
			throw new ConfigurationException(MESSAGE_SEND_MEAILS_CONFIG_ERROR, exception);
		}
	}

	public final String getRequisitionGeneralReportPath() throws ConfigurationException {
		try {
			return this.configurable.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.GENERAL_REPORT_PATH;
		} catch (DatabaseException exception) {
			log.error(MESSAGE_RETRIVING_GENERAL_REPORT_PATH_ERROR, exception);
			throw new ConfigurationException(MESSAGE_RETRIVING_GENERAL_REPORT_PATH_ERROR, exception);
		}
	}

	public final Double getReqGeneralReportRedSemaphorePercentange() throws ConfigurationException {
		try {
			return Double.valueOf(this.configurable.findByName(ConfigurationEnum.GENERAL_REPORT_RED_SEMAPHORE.toString()));
		} catch (DatabaseException exception) {
			log.error(MESSAGE_RETRIVING_GENERAL_REPORT_RED_SEMAPHORE_ERROR, exception);
			throw new ConfigurationException(MESSAGE_RETRIVING_GENERAL_REPORT_RED_SEMAPHORE_ERROR, exception);
		} catch (NumberFormatException numberFormatException) {
			log.error(MESSAGE_GENERAL_REPORT_RED_SEMAPHORE_BAD_CONFIG_ERROR, numberFormatException);
			throw new ConfigurationException(MESSAGE_GENERAL_REPORT_RED_SEMAPHORE_BAD_CONFIG_ERROR, numberFormatException);
		}
	}

	public final Double getReqGeneralReportYellowSemaphorePercentange() throws ConfigurationException {
		try {
			return Double.valueOf(this.configurable.findByName(ConfigurationEnum.GENERAL_REPORT_YELLOW_SEMAPHORE.toString()));
		} catch (DatabaseException exception) {
			log.error(MESSAGE_RETRIVING_GENERAL_REPORT_YELLOW_SEMAPHORE_ERROR, exception);
			throw new ConfigurationException(MESSAGE_RETRIVING_GENERAL_REPORT_YELLOW_SEMAPHORE_ERROR, exception);
		} catch (NumberFormatException numberFormatException) {
			log.error(MESSAGE_GENERAL_REPORT_YELLOW_SEMAPHORE_BAD_CONFIG_ERROR, numberFormatException);
			throw new ConfigurationException(MESSAGE_GENERAL_REPORT_YELLOW_SEMAPHORE_BAD_CONFIG_ERROR, numberFormatException);
		}
	}

	public final Integer getPurReqGeneralReportLastestMonthsNumber() throws ConfigurationException {
		try {
			return Integer.valueOf(
					this.configurable.findByName(ConfigurationEnum.PURCHASING_GENERAL_REPORT_LATEST_MONTHS_NUMBER.toString()));
		} catch (DatabaseException exception) {
			log.error(MESSAGE_RETRIVING_GENERAL_REPORT_LATESTS_MONTHS_ERROR, exception);
			throw new ConfigurationException(MESSAGE_RETRIVING_GENERAL_REPORT_LATESTS_MONTHS_ERROR, exception);
		} catch (NumberFormatException numberFormatException) {
			log.error(MESSAGE_RETRIVING_GENERAL_REPORT_LATESTS_MONTHS_CONFIG_ERROR, numberFormatException);
			throw new ConfigurationException(MESSAGE_RETRIVING_GENERAL_REPORT_LATESTS_MONTHS_CONFIG_ERROR,
					numberFormatException);
		}
	}

	public final String getRequisitionGeneralReportEmails() throws ConfigurationException {
		try {
			return this.configurable.findByName(ConfigurationEnum.PURCHASING_GENERAL_REPORT_EMAILS.toString());
		} catch (DatabaseException exception) {
			log.error(MESSAGE_RETRIVING_GENERAL_REPORT_EMAILS_ERROR, exception);
			throw new ConfigurationException(MESSAGE_RETRIVING_GENERAL_REPORT_EMAILS_ERROR, exception);
		}
	}

	public final void updatePurReqGeneralReportConfigurations(final ParametersHolder parameters)
			throws BusinessException {
		this.update(ConfigurationEnum.PURCHASING_GENERAL_REPORT_EMAILS.toString(),
				parameters.getParameterValue(ConfigurationEnum.PURCHASING_GENERAL_REPORT_EMAILS.toString()).toString());
		this.update(ConfigurationEnum.PURCHASING_GENERAL_REPORT_LATEST_MONTHS_NUMBER.toString(), parameters
				.getParameterValue(ConfigurationEnum.PURCHASING_GENERAL_REPORT_LATEST_MONTHS_NUMBER.toString()).toString());
	}

	public final Integer getAutoSaveTimeSeconds() throws ConfigurationException {
		try {
			final Integer autoSaveTimeSeconds = Integer
					.valueOf(this.configurable.findByName(ConfigurationEnum.AUTOSAVE_TIME_SECONDS.toString()));
			return this.validatePositiveTime(autoSaveTimeSeconds);
		} catch (DatabaseException exception) {
			log.error(MESSAGE_RETRIVING_AUTOSAVE_TIME_SECONDS_ERROR, exception);
			throw new ConfigurationException(MESSAGE_RETRIVING_AUTOSAVE_TIME_SECONDS_ERROR, exception);
		} catch (NumberFormatException numberFormatException) {
			log.error(MESSAGE_RETRIVING_AUTOSAVE_TIME_SECONDS_CONFIG_ERROR, numberFormatException);
			throw new ConfigurationException(MESSAGE_RETRIVING_AUTOSAVE_TIME_SECONDS_CONFIG_ERROR, numberFormatException);
		}
	}

	private Integer validatePositiveTime(final Integer autoSaveTimeSeconds) {
		if (autoSaveTimeSeconds > 0)
			return autoSaveTimeSeconds;
		else
			throw new NumberFormatException("El número de segundos debe ser positivo");
	}

	public Integer isValid(Clause clause) {
		Integer response = new Integer(1);
		DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
		if (null != clause.getValidityStartDate()) {
			Date fechaInicio = null;
			Calendar calFechaActual = Calendar.getInstance();
			Calendar calFechaInicio = Calendar.getInstance();
			try {
				fechaInicio = format.parse(clause.getValidityStartDate());
				calFechaInicio.setTime(fechaInicio);
				log.info("actual  " + calFechaActual);
				Integer anioActual = calFechaActual.get(Calendar.YEAR);
				int mesActual = calFechaActual.get(Calendar.MONTH) + 1;
				int diaActual = calFechaActual.get(Calendar.DAY_OF_MONTH);
				Integer anioInicio = calFechaInicio.get(Calendar.YEAR);
				int mesInicio = calFechaInicio.get(Calendar.MONTH) + 1;
				int diaInicio = calFechaInicio.get(Calendar.DAY_OF_MONTH);
				if (anioInicio < anioActual) {
					response = 0;
					log.info("fecha inicio menor anio:: " + calFechaInicio);
				} else if (anioInicio.compareTo(anioActual) == 0 && mesInicio < mesActual) {
					response = 0;
					log.info("fecha inicio menor mes:: " + calFechaInicio);
				} else if (anioInicio.compareTo(anioActual) == 0 && mesInicio == mesActual && diaInicio < diaActual) {
					response = 0;
					log.info("fecha inicio menor dia:: " + calFechaInicio);
				} else {
					log.info("fecha inicio correcta:: " + anioInicio + "/" + mesInicio + "/" + diaInicio);
				}
			} catch (ParseException e) {
				return 0;
			}
		}
		return response;
	}
}
