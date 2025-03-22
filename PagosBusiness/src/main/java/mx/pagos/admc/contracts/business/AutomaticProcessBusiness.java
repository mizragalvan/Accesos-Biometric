package mx.pagos.admc.contracts.business;

import java.text.ParseException;

import javax.annotation.PostConstruct;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mx.engineer.utils.file.FileUtils;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.ConfigurationException;

@Service
public class AutomaticProcessBusiness {
	private static final Logger LOG = Logger.getLogger(AutomaticProcessBusiness.class);

	@Autowired
	private AlertsBusiness alertsBusiness;

	@Autowired
	private ConfigurationsBusiness configuration;

	@Autowired
	private AutomaticRenewalBusiness automaticRenewalBusiness;

	/*
	 * @Autowired private CatalogExportBusiness catalogExportBusiness;
	 * 
	 * @Autowired private ReportsBusiness reportBusiness;
	 */
	// The Alert feature is launched using spring's Scheduled
	// annotation. with the cron feature. I found no clear
	// documentation about it, but about crontab (linux) which
	// is slightly different.
	// original setting run every day at 3:00 am any weekday with:
	// 										+------------------ seconds
	// 										| +---------------- minutes
	// 										| | +-------------- hours
	// 										| | | +------------ day of month
	// 										| | | | +---------- month
	// 										| | | | | +-------- day of week
	// 										| | | | | |
	// @Scheduled(cron = "0 0 3 * * ?")
	//
	// for testing I want to run it every 5 seconds with
	// @Scheduled(cron = "0/5 * * * * *")
	//
	@Scheduled(cron = "0 00 08 * * MON-FRI")
	public void executeAlerts() throws BusinessException, ParseException {
		LOG.info("Inicia la eliminación de archivos basura temporales");
		// FIXME SE COMENTA POR PERDIDA DE ARCHIVOS.
		// this.deleteAllFoldersOldFiles();
		// this.catalogExportBusiness.deleteCatalogExportByDate();
		LOG.info("Termina exitosamente la eliminación de archivos basura temporales");
		if (Boolean.parseBoolean(this.configuration.findByName("SEND_MAILS"))) {
			LOG.info("Inicia envío de alertas");
			this.alertsBusiness.sendcompletionAlert();
			this.alertsBusiness.sendAlerts();
			this.alertsBusiness.sendServiceLevelsAlerts();
			this.alertsBusiness.sendContractsToExpireForAlerts();
		}
	}

	// The Contract Renewal feature is launched at 3:00 am everyday
	// Whould this be a problem because it is running at the same time as the
	// Alerts?.
	@Scheduled(cron = "0 0 07 * * ?")
	public void generateAlertContractToExpire() throws BusinessException {
		LOG.info("Inicia envío de alertas de Contratos por vencer");
		this.alertsBusiness.sendContractsToExpireForAlerts();
		LOG.info("Finaliza envío de alertas de Contratos por vencer");
	}

	// The Contract Renewal feature is launched at 3:00 am everyday
	// Whould this be a problem because it is running at the same time as the
	// Alerts?.
	@Scheduled(cron = "0 0 07 * * ?")
	public void automaticContractsRenovation() throws BusinessException {
		LOG.info("Inicia envío de alertas de Renovacion de Contratos");
		this.automaticRenewalBusiness.automaticRenewRequisitions();
		LOG.info("Finaliza envío de alertas de Renovacion de Contratos");
	}

	private void deleteAllFoldersOldFiles() throws BusinessException {
		FileUtils.deleteAllFoldersOldFiles(this.getDocumentTempPath(),
				Integer.valueOf(this.configuration.findByName("TIMEFILE_TEMP_FILE")));
	}

	private String getDocumentTempPath() throws BusinessException {
		return this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_TMP;
	}

	@PostConstruct
	public void init() {
		// FIXME SE comenta codigo en lo que se llena la tabla cor
		try {
			final DailyRollingFileAppender fileAppender = new DailyRollingFileAppender();
			fileAppender.setName("file");
			fileAppender.setFile(this.configuration.getLogPath() + "admc.log");
			fileAppender.setDatePattern("'.'yyyy-MM-dd");
			final PatternLayout conversionPattern = new PatternLayout();
			conversionPattern.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p [:%C:%L:] - %m%n");
			fileAppender.setLayout(conversionPattern);
			fileAppender.setAppend(false);
			fileAppender.activateOptions();

			Logger.getRootLogger().setLevel(Level.INFO);
			Logger.getRootLogger().addAppender(fileAppender);
		} catch (ConfigurationException configurationException) {
			LOG.error("Hubo un problema al configurar la ruta del log", configurationException);
		}
	}
}