package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.enums.Category;
import mx.pagos.admc.core.structures.Configuration;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.ParametersHolder;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.ConfigurationException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

@Controller
public class ConfigurationService {
	private static final String claseName = "ConfigurationService: ";

	private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

	@Autowired
	private ConfigurationsBusiness configurationsBusiness;

	@Autowired
	private BinnacleBusiness binnacleBusiness;

	@Autowired
	private UserSession session;

	@RequestMapping(value = UrlConstants.CONFIGURATION_FIND_ALL, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Configuration> findAll(final HttpServletResponse response) {
		try {
			final ConsultaList<Configuration> configurationList = new ConsultaList<Configuration>();
			configurationList.setList(this.configurationsBusiness.findAll());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Busqueda de todas las configuraciones",
					this.session, LogCategoryEnum.QUERY));
			return configurationList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<Configuration>();
	}

	@RequestMapping(value = UrlConstants.UPDATE_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody
	public final void updateConfiguration(@RequestBody final Configuration configuration,
			final HttpServletResponse response) {
		try {
			log.info(claseName + "Se recibe cambio de Configuración de tipo: ", configuration.getName());
			this.configurationsBusiness.update(configuration);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Actualización de la configuración con el nombre: " + configuration.getName(), this.session,
					LogCategoryEnum.UPDATE));
		} catch (BusinessException e) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.UPDATE_PUR_REQ_GENERAL_REPORT_CONFIGURATIONS, method = RequestMethod.POST)
	@ResponseBody
	public final void updatePurReqGeneralReportConfigurations(@RequestBody final ParametersHolder parameters,
			final HttpServletResponse response) {
		try {
			this.configurationsBusiness.updatePurReqGeneralReportConfigurations(parameters);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Se han actualizado las configuraciones del reporte general de compras. Correos: "
							+ parameters.getParameterValue(ConfigurationEnum.PURCHASING_GENERAL_REPORT_EMAILS.toString())
							+ ". Meses de actividad: "
							+ parameters
									.getParameterValue(ConfigurationEnum.PURCHASING_GENERAL_REPORT_LATEST_MONTHS_NUMBER.toString()),
					this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.FIND_CONFIGURATION_BY_CATEGORY, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Configuration> findConfigurationsByCategory(@RequestBody final String category,
			final HttpServletResponse response) {
		try {
			final ConsultaList<Configuration> configurationList = new ConsultaList<Configuration>();
			configurationList.setList(this.configurationsBusiness.findConfigurationsByCategory(Category.valueOf(category)));
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Busqueda de configuraciones filtradas " + "por la categoría: " + category, this.session,
					LogCategoryEnum.QUERY));
			return configurationList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return new ConsultaList<Configuration>();
	}

	@RequestMapping(value = UrlConstants.FIND_PUR_GENERAL_REPORT_MAILS, method = RequestMethod.POST)
	@ResponseBody
	public final String findPurGeneralReportMails(final HttpServletResponse response) {
		try {
			return this.configurationsBusiness.getRequisitionGeneralReportEmails();
		} catch (ConfigurationException configurationException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, configurationException.getMessage());
		}
		return "";
	}

	@RequestMapping(value = UrlConstants.FIND_PUR_GENERAL_REPORT_LATEST_MONTHS, method = RequestMethod.POST)
	@ResponseBody
	public final Integer findPurGeneralLatestMonths(final HttpServletResponse response) {
		try {
			return this.configurationsBusiness.getPurReqGeneralReportLastestMonthsNumber();
		} catch (ConfigurationException configurationException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, configurationException.getMessage());
		}
		return 0;
	}

	@RequestMapping(value = UrlConstants.FIND_AUTOSAVE_TIME_SECONDS_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody
	public final Integer findAutoSaveTimeSeconds(final HttpServletResponse response) {
		try {
			return this.configurationsBusiness.getAutoSaveTimeSeconds();
		} catch (ConfigurationException configurationException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, configurationException.getMessage());
		}
		return 0;
	}
}
