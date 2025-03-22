package mx.pagos.admc.contracts.daos;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.engineer.utils.database.JdbcTemplateUtils;
import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.Alertable;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.structures.Alert;
import mx.pagos.admc.contracts.structures.AlertConfigurationDay;
import mx.pagos.admc.contracts.structures.AlertDocumentType;
import mx.pagos.admc.contracts.structures.AlertFlowStep;
import mx.pagos.admc.contracts.structures.owners.RequisitionOwners;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Repository
public class AlertsDAO implements Alertable {
    private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TURN_DATE_PLUS_ALERTDAY_ALERT_FROM_DAY = "(Turn.TurnDate + ALERTDAY.AlertFromDay)";
    private static final String TURN_DATE_PLUS_TURN_ATTENTION_DAYS = "(Turn.TurnDate + TURN.AttentionDays)";
    private static final String TAG_BEFORE_DAYS_EXPIRATION_ALERT = ":BeforeDaysExpirationAlert";
    private static final String BETWEEN = " BETWEEN ";
    private static final String AND = " AND ";
    private static final String CAST_END_DATE_AS_DATE = "CAST(EndDate AS DATE)";
    private static final String SINGLE_QUOTE = "'";
    
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Autowired
    private DatabaseUtils databaseUtils;



	@Override
    public Integer saveOrUpdate(final Alert alert) throws DatabaseException {
	    return alert.getIdAlert() == null ? this.insertAlert(alert) : this.updateAlert(alert);
	}

	@Override
    public Integer insertAlert(final Alert alert) throws DatabaseException {
	    try {
	        final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(alert);
	        final KeyHolder keyHolder = new GeneratedKeyHolder();
	        this.namedjdbcTemplate.update(this.buildInsertAlertQuery(), namedParameters, keyHolder, 
	                new String[]{"IdAlert"});
	        return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}

	@Override
    public Integer updateAlert(final Alert alert) throws DatabaseException {
	    try {
	        final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(alert);
	        this.namedjdbcTemplate.update(this.buildUpdateAlertQuery(), namedParameters);
            return alert.getIdAlert();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
    @Override
    public List<Alert> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildSelectAllQuery(), new BeanPropertyRowMapper<>(Alert.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Alert findById(final Integer idAlert) throws DatabaseException, EmptyResultException {
        try {
        	final BeanPropertySqlParameterSource namedParameters = this.createFindByIdAlertNamedParameters(idAlert);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(Alert.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
	@Override
    public List<Alert> findbyFlowStatus(final Alert alert) throws DatabaseException {
	    try {
	    	final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(alert);
            return this.namedjdbcTemplate.query(this.buildFindByFlowStatus(), namedParameters,
                    new BeanPropertyRowMapper<>(Alert.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
	@Override
    public Integer findValidityDaysByRequisitionFlowStatusTurn(final Integer idRequisition,
            final FlowPurchasingEnum status, final Integer turn) throws DatabaseException {
	    try {
	        final MapSqlParameterSource namedParameters =
	                this.createRequisitionFlowStatusTurnNamedParameters(idRequisition, status, turn);
	        return this.namedjdbcTemplate.queryForObject(this.buildFindByFlowStatusTurnQuery(),
	                namedParameters, Integer.class);
	    } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
	@Override
	public Integer findValidityDaysByFlowStatusTurn(final Integer idFlow,
            final FlowPurchasingEnum status, final Integer turn) throws DatabaseException {
	    try {
	        final MapSqlParameterSource namedParameters =
                    this.createFlowStatusTurnNamedParameters(idFlow, status, turn);
	        return this.namedjdbcTemplate.queryForObject(
                    this.buildFindValidityDaysByRequisitionOwnersFlowStatusTurnQuery(), namedParameters, Integer.class);
	    } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}

    @Override
    public void deleteAlert(final Alert alert) throws DatabaseException {
	    try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(alert);
            this.namedjdbcTemplate.update(this.buildDeleteAlertQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
	@Override
	public Integer saveAlertconfigurationDay(final AlertConfigurationDay alertConfigurationDay)
	        throws DatabaseException {
	    try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(alertConfigurationDay);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildSaveAlertconfigurationDayQuery(), namedParameters, keyHolder, 
                    new String[]{"IdAlertConfigDay"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
    
    @Override
    public List<AlertConfigurationDay> findAlertConfigurationDaysByIdAlert(final Integer idAlert)
            throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = this.createFindByIdAlertNamedParameters(idAlert);
            return this.namedjdbcTemplate.query(this.buildFindAlertConfigurationDaysByIdAlertQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(AlertConfigurationDay.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void deleteAlertConfigurationDaysByIdAlert(final Integer idAlert) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = this.createFindByIdAlertNamedParameters(idAlert);
            this.namedjdbcTemplate.update(this.buildDeleteAlertConfigurationDaysByIdAlertQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<AlertFlowStep> getEmailsToAlertsByStep(final Integer idRequisition,
            final FlowPurchasingEnum flowStatus) throws DatabaseException {
        final MapSqlParameterSource namedParameters = this.createEmailAlertsParameters(idRequisition, flowStatus);
        return this.namedjdbcTemplate.query(this.buildGetEmailsToAlertByStepQuery(), namedParameters,
                new BeanPropertyRowMapper<>(AlertFlowStep.class));
    }
    
    @Override
    public void saveDocumentType(final Integer idAlert, final Integer idDocumentType) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createSaveDocumentTypeNamedParameters(idAlert, idDocumentType);
            this.namedjdbcTemplate.update(this.buildSaveDocumentTypeQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void deleteDocumentTypesByIdAlert(final Integer idAlert) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = this.createFindByIdAlertNamedParameters(idAlert);
            this.namedjdbcTemplate.update(this.buildDeleteDocumentTypesByIdAlertQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<Integer> findActiveDocumentTypesByIdAlert(final Integer idAlert) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = this.createFindByIdAlertNamedParameters(idAlert);
            return this.namedjdbcTemplate.queryForList(this.buildFindActiveDocumentTypesByIdAlertQuery(),
                    namedParameters, Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<AlertDocumentType> findAlertConflicts(final Alert alert) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindAlertConflictsNamedParameters(alert);
            return this.namedjdbcTemplate.query(this.buildFindAlertConflictsQuery(),
                    namedParameters, new BeanPropertyRowMapper<>(AlertDocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildFindAlertConflictsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT DOCUMENTTYPE.IdDocumentType, DOCUMENTTYPE.Name, Turn ");
        query.append("FROM ALERTCONFIG INNER JOIN ALERTCONFIGDOCUMENTTYPE ON ");
        query.append("ALERTCONFIG.IdAlert = ALERTCONFIGDOCUMENTTYPE.IdAlert INNER JOIN DOCUMENTTYPE ON ");
        query.append("ALERTCONFIGDOCUMENTTYPE.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
        query.append("WHERE IdFlow = :IdFlow AND ALERTCONFIG.Status = :Status AND ");
        query.append("(Turn = :Turn) AND DOCUMENTTYPE.IdDocumentType IN (:IdDocumentType) ");
        return query.toString();
    }

    private MapSqlParameterSource createFindAlertConflictsNamedParameters(
            final Alert alert) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_FLOW, alert.getIdFlow());
        namedParameters.addValue(TableConstants.STATUS, alert.getIdStatus());
        namedParameters.addValue(TableConstants.TURN, alert.getTurn());
        namedParameters.addValues(
                Collections.singletonMap(TableConstants.ID_DOCUMENT_TYPE, alert.getDocumentTypesList()));
        return namedParameters;
    }
    
    private MapSqlParameterSource createEmailAlertsParameters(final Integer idRequisition,
            final FlowPurchasingEnum flowStatus) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("IdRequisition", idRequisition);
        namedParameters.addValue("FlowStatus", flowStatus.toString());
        return namedParameters;
    }
    
    private String buildGetEmailsToAlertByStepQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT REQUISITION.IdRequisition, DOCUMENTTYPE.Name AS DocumentTypeName, SUPPLIER.CommercialName, USERS.Email ");
        query.append("FROM USERS INNER JOIN ");
        query.append("POSITION ON USERS.IdPosition = POSITION.IdPosition INNER JOIN ");
        query.append("PROFILEUSER ON USERS.IdUser = PROFILEUSER.IdUser INNER JOIN ");
        query.append("PROFILESCREENFLOW ON PROFILEUSER.IdProfile = PROFILESCREENFLOW.IdProfile INNER JOIN ");
        query.append("SCREEN ON PROFILESCREENFLOW.FactoryName = SCREEN.FactoryName AND SCREEN.FlowStatus = :FlowStatus, ");
        query.append("REQUISITION INNER JOIN ");
        query.append("DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType INNER JOIN ");
        query.append("SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier LEFT JOIN ");
        query.append("REQUISITION APPLICANTSUBDIRECTOR on APPLICANTSUBDIRECTOR.IdRequisition = REQUISITION.IdRequisition LEFT JOIN ");
        query.append("USERS APPLICANTBOSS ON APPLICANTBOSS.IdUser = REQUISITION.IdApplicant LEFT JOIN ");
        query.append("REQUISITIONUSERSVOBO USERSVOBO ON USERSVOBO.IdRequisition = REQUISITION.IdRequisition LEFT JOIN ");
        query.append("REQUISITION EVALUATOR ON EVALUATOR.IdRequisition = REQUISITION.IdRequisition ");
        query.append("WHERE USERS.Status = 'ACTIVE' AND APPLICANTBOSS.Status = 'ACTIVE'"); 
        query.append("AND REQUISITION.IdRequisition = :IdRequisition ");
        query.append("AND ( ");
        query.append("(REQUISITION.IdApplicant = USERS.IdUser AND :FlowStatus IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANT_BUSY_STATUS'"));
        query.append(")) OR (REQUISITION.IdApplicant IN (SELECT EMPLOYEE.IdUser FROM USERS AS EMPLOYER INNER JOIN USERS AS EMPLOYEE ON EMPLOYEE.IdUnderdirector = EMPLOYER.IdUser WHERE EMPLOYER.IdUser = USERS.IdUser) AND :FlowStatus IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANTSUBDIRECTOR_BUSY_STATUS'"));
        query.append(")) OR (PROFILESCREENFLOW.IdFlow = REQUISITION.IdFlow AND :FlowStatus IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYERSUBDIRECTOR_BUSY_STATUS'"));
        query.append(")) OR (REQUISITION.IdLawyer = USERS.IdUser AND :FlowStatus IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYER_BUSY_STATUS'))"));
        query.append(" OR (USERSVOBO.IdUser = USERS.IdUser AND :FlowStatus IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'USERSVOBOREQUISITION_BUSY_STATUS'"));
        query.append(")) OR (REQUISITION.IdEvaluator = USERS.IdUser AND :FlowStatus IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'EVALUATOR_BUSY_STATUS'"));
        query.append(")) ) ");
        query.append("ORDER BY REQUISITION.IdRequisition;");

        return query.toString();
    }
    
    private String buildInsertAlertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ALERTCONFIG(IdFlow, Status, Name, Turn, Time, ExpirationMailText) ");
        query.append("VALUES(:IdFlow, :IdStatus, :Name, :Turn, :Time, :ExpirationMailText)");
        return query.toString();
    }

    private String buildUpdateAlertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE ALERTCONFIG SET ");
        query.append("Name = :Name, ");
        query.append("ExpirationMailText = :ExpirationMailText, ");
        query.append("Time = :Time ");
        query.append("WHERE IdAlert = :IdAlert ");
        return query.toString();
    }
    
    private String buildDeleteAlertQuery() {
    	final StringBuilder query = new StringBuilder();
    	query.append("DELETE FROM ALERTCONFIG WHERE IdAlert = :IdAlert");
    	return query.toString();
    }
    
    private String buildSelectAllQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ALERTCONFIG");
        return query.toString();
    }

    private String buildFindByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildSelectAllQuery());
        query.append(" WHERE IdAlert  = :IdAlert");
        return query.toString();
    }
    
    private String buildFindByFlowStatus() {
    	final StringBuilder query = new StringBuilder();
    	query.append(this.buildSelectAllQuery());
    	query.append(" WHERE IdFlow = :IdFlow AND Status = :Status ORDER BY IdAlert DESC");
    	return query.toString();
    }

    private String buildSaveAlertconfigurationDayQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ALERTCONFIGDAY (");
        query.append(this.buildAlertConfigurationDayNonPrimaryKeyFields());
        query.append(") VALUES (");
        query.append(JdbcTemplateUtils.tagFields(this.buildAlertConfigurationDayNonPrimaryKeyFields(), ", "));
        query.append(")");
        return query.toString();
    }

    private String buildAlertConfigurationDayNonPrimaryKeyFields() {
        final StringBuilder query = new StringBuilder();
        query.append("IdAlert, AlertFromDay, EmailsList, IsMailSendDaily, IsApplicantEmailSend, ");
        query.append("IsUserSubdirectorEmailSend, MailContent ");
        return query.toString();
    }

    private String buildFindAlertConfigurationDaysByIdAlertQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllAlertConfigurationDayQuery(query);
        query.append("WHERE IdAlert = :IdAlert");
        return query.toString();
    }

    private void buildSelectAllAlertConfigurationDayQuery(
            final StringBuilder query) {
        query.append("SELECT IdAlertConfigDay, ");
        query.append(this.buildAlertConfigurationDayNonPrimaryKeyFields());
        query.append("FROM ALERTCONFIGDAY ");
    }
    

    private BeanPropertySqlParameterSource createFindByIdAlertNamedParameters(final Integer idAlert) {
        final Alert alert = new Alert(idAlert);
        final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(alert);
        return namedParameters;
    }

    private String buildDeleteAlertConfigurationDaysByIdAlertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ALERTCONFIGDAY WHERE IdAlert = :IdAlert");
        return query.toString();
    }
    
    private String buildFindByFlowStatusTurnQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT MIN(Time) AS ValidityDays ");
        query.append("FROM ALERTCONFIG INNER JOIN REQUISITION ON ALERTCONFIG.IdFlow = REQUISITION.IdFlow INNER JOIN ");
        query.append("ALERTCONFIGDOCUMENTTYPE ON REQUISITION.IdDocumentType = ALERTCONFIGDOCUMENTTYPE.IdDocumentType ");
        query.append("AND ALERTCONFIG.IdAlert = ALERTCONFIGDOCUMENTTYPE.IdAlert ");
        query.append("WHERE IdRequisition = :IdRequisition AND ");
        query.append("ALERTCONFIG.Status = :Status AND (Turn = :Turn OR Turn = -1)");
        return query.toString();
    }

    private MapSqlParameterSource createRequisitionFlowStatusTurnNamedParameters(
            final Integer idRequisition, final FlowPurchasingEnum status,
            final Integer turn) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        namedParameters.addValue(TableConstants.TURN, turn);
        return namedParameters;
    }
    
    private String buildFindValidityDaysByRequisitionOwnersFlowStatusTurnQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT MIN(Time) AS ValidityDays FROM ALERTCONFIG ");
        query.append("WHERE IdFlow = :IdFlow AND Status = :Status AND (Turn = :Turn OR Turn = -1)");
        return query.toString();
    }

    private MapSqlParameterSource createFlowStatusTurnNamedParameters(
            final Integer idFlow, final FlowPurchasingEnum status, final Integer turn) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        namedParameters.addValue(TableConstants.TURN, turn);
        return namedParameters;
    }

    @Override
    public List<Alert> findAlerts() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAlertsQuery(), new BeanPropertyRowMapper<>(Alert.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findAlertsQuery() {
        final StringBuilder builder = new StringBuilder();
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
        builder.append("SELECT REQUISITION.IdRequisition, REQUISITION.ServiceDescription, SCREEN.Name AS Status, ");
        builder.append("SUPPLIER.CommercialName, ALERTDAY.EmailsList, ALERTDAY.MailContent, ");
        builder.append("CASE ALERTDAY.IsApplicantEmailSend WHEN 1 THEN APPLICANT.Email ELSE NULL ");
        builder.append("END AS ApplicantMail, ALERTDAY.IsUserSubdirectorEmailSend ");
        builder.append("FROM REQUISITION INNER JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
        builder.append("INNER JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
        builder.append("INNER JOIN USERS APPLICANT ON REQUISITION.IdApplicant = APPLICANT.IdUser LEFT JOIN ");
        builder.append("USERS APPLICANTUNDERDIRECTOR ON APPLICANT.IdUnderdirector = APPLICANTUNDERDIRECTOR.IdUser ");
        builder.append("INNER JOIN REQUISITIONSTATUSTURN TURN ON REQUISITION.IdRequisition = TURN.IdRequisition ");
        builder.append("AND REQUISITION.Status = TURN.Status LEFT JOIN ");
        builder.append("(SELECT ALERT.IdAlert, IdFlow, Turn, Status, IdDocumentType FROM ALERTCONFIG ALERT ");
        builder.append("INNER JOIN ALERTCONFIGDOCUMENTTYPE ON ALERT.IdAlert = ALERTCONFIGDOCUMENTTYPE.IdAlert) ");
        builder.append("ALERT ON REQUISITION.IdFlow = ALERT.IdFlow AND REQUISITION.Status = ALERT.Status ");
        builder.append("AND REQUISITION.IdDocumentType = ALERT.IdDocumentType ");
        builder.append("LEFT JOIN ALERTCONFIGDAY AS ALERTDAY ON ALERT.IdAlert = ALERTDAY.IdAlert ");
        builder.append("WHERE TURN.TurnDate = (SELECT MAX(TurnDate) FROM REQUISITIONSTATUSTURN TURNDATE ");
        builder.append("WHERE TURNDATE.IdRequisition = REQUISITION.IdRequisition) AND ((");
        builder.append(this.databaseUtils.buildDateDiffSSqlFunction(
                formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
        builder.append(" <= 0 AND (ALERT.Turn = TURN.Turn OR ALERT.Turn = -1)) ");
        builder.append("OR ((ALERT.Turn = -1 OR ALERT.Turn = TURN.Turn) AND (");
        builder.append(this.databaseUtils.buildDateDiffSSqlFunction(
                formatedTodayDate, TURN_DATE_PLUS_ALERTDAY_ALERT_FROM_DAY));
        builder.append(" = 0 OR (ALERTDAY.IsMailSendDaily = 1 AND ");
        builder.append(this.databaseUtils.buildDateDiffSSqlFunction(
                formatedTodayDate, TURN_DATE_PLUS_ALERTDAY_ALERT_FROM_DAY));
        builder.append(" <= 0 )))) ");
        builder.append("ORDER BY REQUISITION.IdRequisition ");
        return builder.toString();
    }
    
    @Override
    public List<Alert> findUnderDirectorMailSendAlert(final List<Integer> idRequisitionList) 
            throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findUnderDirectorMailQuery(),
                    Collections.singletonMap(TableConstants.ID_REQUISITION, idRequisitionList), 
                    new BeanPropertyRowMapper<>(Alert.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findUnderDirectorMailQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT DISTINCT REQUISITION.IdRequisition,USERS.IdUser, UNDERDIRECTOR.IdUser ");
        builder.append("AS IdUnderDirector, UNDERDIRECTOR.Email AS UnderDirectorEmail FROM REQUISITION ");
        builder.append("LEFT JOIN REQUISITION APPLICANT on APPLICANT.IdRequisition = REQUISITION.IdRequisition ");
        builder.append("LEFT JOIN REQUISITION APPLICANTSUBDIRECTOR ON ");
        builder.append("APPLICANTSUBDIRECTOR.IdRequisition = REQUISITION.IdRequisition ");
        builder.append("LEFT JOIN REQUISITION LAWYER on LAWYER.IdRequisition = REQUISITION.IdRequisition ");
        builder.append("LEFT JOIN USERS APPLICANTUNDERDIRECTOR ON ");
        builder.append("APPLICANTUNDERDIRECTOR.IdUser = REQUISITION.IdApplicant ");
        builder.append("LEFT JOIN REQUISITIONUSERSVOBO USERSVOBO ON ");
        builder.append("USERSVOBO.IdRequisition = REQUISITION.IdRequisition ");
        builder.append("LEFT JOIN REQUISITION USERSVOBOREQUISITION ON ");
        builder.append("USERSVOBOREQUISITION.IdRequisition = USERSVOBO.IdRequisition ");
        builder.append("LEFT JOIN REQUISITION EVALUATOR ON ");
        builder.append("EVALUATOR.IdRequisition = REQUISITION.IdRequisition, USERS ");
        builder.append("INNER JOIN USERS UNDERDIRECTOR ON USERS.IdUnderdirector = UNDERDIRECTOR.IdUser ");
        builder.append("WHERE REQUISITION.Status NOT IN ('REQUISITION_CLOSED', 'CANCELLED', 'RENEWED') ");
        builder.append("AND REQUISITION.IdRequisition IN (:IdRequisition) ");
        builder.append("AND ((REQUISITION.Status = APPLICANT.Status AND APPLICANT.IdApplicant = USERS.IdUser ");
        builder.append("AND APPLICANT.Status IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANT_BUSY_STATUS'"));
        builder.append(")) OR (REQUISITION.Status = APPLICANTSUBDIRECTOR.Status AND REQUISITION.IdApplicant IN ");
        builder.append("(SELECT EMPLOYEE.IdUser FROM USERS EMPLOYER INNER JOIN USERS EMPLOYEE ON ");
        builder.append("EMPLOYEE.IdUnderdirector = EMPLOYER.IdUser WHERE EMPLOYER.IdUser = USERS.IdUser) ");
        builder.append("AND REQUISITION.Status IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANTSUBDIRECTOR_BUSY_STATUS'"));
        builder.append(")) OR (REQUISITION.Status = LAWYER.Status AND LAWYER.IdLawyer = USERS.IdUser ");
        builder.append("AND LAWYER.Status IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYER_BUSY_STATUS'"));
        builder.append(")) OR (REQUISITION.Status = USERSVOBOREQUISITION.Status AND USERSVOBO.IdUser = USERS.IdUser ");
        builder.append("AND USERSVOBOREQUISITION.Status IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'USERSVOBOREQUISITION_BUSY_STATUS'"));
        builder.append(")) OR (REQUISITION.Status = EVALUATOR.Status AND EVALUATOR.IdEvaluator = USERS.IdUser ");
        builder.append("AND EVALUATOR.Status IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'EVALUATOR_BUSY_STATUS'"));
        builder.append("))) ORDER BY REQUISITION.IdRequisition, USERS.IdUser ");
        return builder.toString();
    }
    
    private MapSqlParameterSource createSaveDocumentTypeNamedParameters(
            final Integer idAlert, final Integer idDocumentType) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_ALERT, idAlert);
        namedParameters.addValue(TableConstants.ID_DOCUMENT_TYPE, idDocumentType);
        return namedParameters;
    }
    
    private String buildSaveDocumentTypeQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ALERTCONFIGDOCUMENTTYPE(IdAlert, IdDocumentType) VALUES (:IdAlert, :IdDocumentType)");
        return query.toString();
    }
    
    private String buildDeleteDocumentTypesByIdAlertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ALERTCONFIGDOCUMENTTYPE WHERE IdAlert = :IdAlert");
        return query.toString();
    }
    
    private String buildFindActiveDocumentTypesByIdAlertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT ALERTCONFIGDOCUMENTTYPE.IdDocumentType ");
        query.append("FROM ALERTCONFIGDOCUMENTTYPE INNER JOIN ");
        query.append("DOCUMENTTYPE ON ALERTCONFIGDOCUMENTTYPE.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
        query.append("WHERE IdAlert = :IdAlert AND Status = 'ACTIVE'");
        return query.toString();
    }

    @Override
    public List<AlertFlowStep> getOwnersEmailsToAlertByStep(final Integer idRequisitionOwner, 
            final FlowPurchasingEnum flowStatus) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            source.addValue(TableConstants.FLOW_STATUS, flowStatus.toString());
            return this.namedjdbcTemplate.query(this.getOwnersEmailsToAlertByStepQuery(), source, 
                    new BeanPropertyRowMapper<AlertFlowStep>(AlertFlowStep.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String getOwnersEmailsToAlertByStepQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT DISTINCT REQUISITIONOWNERS.IdRequisitionOwners,REQUISITIONOWNERS.CustomerCompanyName, ");
        builder.append("USERS.Email, CATEGORY.Name AS CategoryName FROM USERS ");
        builder.append("INNER JOIN POSITION ON USERS.IdPosition = POSITION.IdPosition ");
        builder.append("INNER JOIN PROFILEUSER ON USERS.IdUser = PROFILEUSER.IdUser ");
        builder.append("INNER JOIN PROFILESCREENFLOW ON PROFILEUSER.IdProfile = PROFILESCREENFLOW.IdProfile ");
        builder.append("INNER JOIN SCREEN ON PROFILESCREENFLOW.FactoryName = SCREEN.FactoryName ");
        builder.append("AND SCREEN.FlowStatus = :FlowStatus, ");
        builder.append("REQUISITIONOWNERS ");
        builder.append("INNER JOIN CATEGORY ON REQUISITIONOWNERS.IdCategory = CATEGORY.IdCategory ");
        builder.append("INNER JOIN REQUISITIONOWNERSSTATUS ON ");
        builder.append("REQUISITIONOWNERS.IdRequisitionOwners = REQUISITIONOWNERSSTATUS.IdRequisitionOwners ");
        builder.append("AND REQUISITIONOWNERSSTATUS.Status = :FlowStatus ");
        builder.append("LEFT JOIN REQUISITIONOWNERS BUSINESSMAN ON ");
        builder.append("BUSINESSMAN.IdRequisitionOwners = REQUISITIONOWNERS.IdRequisitionOwners ");
        builder.append("LEFT JOIN REQUISITIONOWNERS LAWYER ");
        builder.append("ON LAWYER.IdRequisitionOwners = REQUISITIONOWNERS.IdRequisitionOwners ");
        builder.append("LEFT JOIN REQUISITIONOWNERS DECIDERLAWYER ON ");
        builder.append("DECIDERLAWYER.IdRequisitionOwners = REQUISITIONOWNERS.IdRequisitionOwners ");
        builder.append("WHERE REQUISITIONOWNERSSTATUS.Status NOT IN ('ENTERPRISE_REQUISITION_CLOSE') ");
        builder.append("AND REQUISITIONOWNERS.IdRequisitionOwners = :IdRequisitionOwners ");
        builder.append("AND ( ");
        builder.append("(BUSINESSMAN.IdBusinessman = USERS.IdUser AND :FlowStatus IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_BUSINESSMAN_BUSY_STATUS'"));
        builder.append(")) OR (PROFILESCREENFLOW.IdFlow = REQUISITIONOWNERS.IdFlow AND :FlowStatus IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_NOT_ASIGNED_BUSY_STATUS'"));
        builder.append(")) OR (LAWYER.IdLawyer = USERS.IdUser AND :FlowStatus IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_LAWYER_BUSY_STATUS'"));
        builder.append(")) OR (DECIDERLAWYER.IdDeciderLawyer = USERS.IdUser AND :FlowStatus IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_DECIDER_LAWYER_BUSY_STATUS'"));
        builder.append(")) ) ORDER BY REQUISITIONOWNERS.IdRequisitionOwners ");
        return builder.toString();
    }

    @Override
    public List<Alert> findOwnersServiceLevelsAlerts() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findOwnersServiceLevelsAlertsQuery(), 
                    new BeanPropertyRowMapper<Alert>(Alert.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findOwnersServiceLevelsAlertsQuery() {
        final StringBuilder builder = new StringBuilder();
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
        builder.append("SELECT REQUISITIONOWNERS.IdRequisitionOwners, CustomerCompanyName, SCREEN.Name AS Status, ");
        builder.append("ALERTDAY.EmailsList, ALERTDAY.MailContent, ");
        builder.append("CASE ALERTDAY.IsApplicantEmailSend WHEN 1 THEN BUSSINESMAN.Email ELSE NULL ");
        builder.append("END AS BusinessManMail, ALERTDAY.IsUserSubdirectorEmailSend FROM REQUISITIONOWNERS ");
        builder.append("INNER JOIN REQUISITIONOWNERSSTATUS ");
        builder.append("ON REQUISITIONOWNERS.IdRequisitionOwners = REQUISITIONOWNERSSTATUS.IdRequisitionOwners ");
        builder.append("INNER JOIN SCREEN ON REQUISITIONOWNERSSTATUS.Status = SCREEN.FlowStatus ");
        builder.append("INNER JOIN USERS BUSSINESMAN ON REQUISITIONOWNERS.IdBusinessman = BUSSINESMAN.IdUser ");
        builder.append("LEFT JOIN USERS APPLICANTUNDERDIRECTOR ");
        builder.append("ON BUSSINESMAN.IdUnderdirector = APPLICANTUNDERDIRECTOR.IdUser ");
        builder.append("INNER JOIN REQUISITIONOWNERSSTATUSTURN TURN ");
        builder.append("ON REQUISITIONOWNERS.IdRequisitionOwners = TURN.IdRequisitionOwners ");
        builder.append("AND REQUISITIONOWNERSSTATUS.Status = TURN.Status ");
        builder.append("LEFT JOIN ALERTCONFIG ALERT ON REQUISITIONOWNERS.IdFlow = ALERT.IdFlow ");
        builder.append("AND REQUISITIONOWNERSSTATUS.Status = ALERT.Status ");
        builder.append("LEFT JOIN ALERTCONFIGDAY ALERTDAY ON ALERT.IdAlert = ALERTDAY.IdAlert ");
        builder.append("WHERE TURN.TurnDate = (");
        builder.append("SELECT MAX(TurnDate) FROM REQUISITIONOWNERSSTATUSTURN TURNDATE ");
        builder.append("WHERE TURNDATE.IdRequisitionOwners = REQUISITIONOWNERS.IdRequisitionOwners) AND ((");
        builder.append(this.databaseUtils.buildDateDiffSSqlFunction(
                formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
        builder.append(" <= 0 AND (ALERT.Turn = TURN.Turn OR ALERT.Turn = -1)) ");
        builder.append("OR ((ALERT.Turn = -1 OR ALERT.Turn = TURN.Turn) AND (");
        builder.append(this.databaseUtils.buildDateDiffSSqlFunction(
                formatedTodayDate, TURN_DATE_PLUS_ALERTDAY_ALERT_FROM_DAY)).append(" = 0 ");
        builder.append("OR (ALERTDAY.IsMailSendDaily = 1 AND ");
        builder.append(this.databaseUtils.buildDateDiffSSqlFunction(
                formatedTodayDate, TURN_DATE_PLUS_ALERTDAY_ALERT_FROM_DAY));
        builder.append(" <= 0 )))) ORDER BY REQUISITIONOWNERS.IdRequisitionOwners ");
        return builder.toString();
    }

    @Override
    public List<Alert> findRequisitionOwnersUnderDirectorMail(final List<Integer> idRequisitionOwnersList) 
            throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findRequisitionOwnersUnderDirectorMailQuery(),
                    Collections.singletonMap(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwnersList), 
                    new BeanPropertyRowMapper<>(Alert.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findRequisitionOwnersUnderDirectorMailQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT DISTINCT REQUISITIONOWNERS.IdRequisitionOwners AS IdRequisition, USERS.IdUser, ");
        builder.append("UNDERDIRECTOR.IdUser, UNDERDIRECTOR.Email FROM REQUISITIONOWNERS ");
        builder.append("INNER JOIN REQUISITIONOWNERSSTATUS ON ");
        builder.append("REQUISITIONOWNERS.IdRequisitionOwners = REQUISITIONOWNERSSTATUS.IdRequisitionOwners ");
        builder.append("LEFT JOIN REQUISITIONOWNERS BUSINESSMAN ON ");
        builder.append("BUSINESSMAN.IdRequisitionOwners = REQUISITIONOWNERS.IdRequisitionOwners ");
        builder.append("LEFT JOIN REQUISITIONOWNERS DECIDERLAWYER ON ");
        builder.append("DECIDERLAWYER.IdRequisitionOwners = REQUISITIONOWNERS.IdRequisitionOwners ");
        builder.append("LEFT JOIN REQUISITIONOWNERS LAWYER ON ");
        builder.append("LAWYER.IdRequisitionOwners = REQUISITIONOWNERS.IdRequisitionOwners, ");
        builder.append("USERS INNER JOIN USERS UNDERDIRECTOR ON USERS.IdUnderdirector = UNDERDIRECTOR.IdUser ");
        builder.append("WHERE REQUISITIONOWNERSSTATUS.Status NOT IN ('ENTERPRISE_REQUISITION_CLOSE') ");
        builder.append("AND REQUISITIONOWNERS.IdRequisitionOwners IN (:IdRequisitionOwners) ");
        builder.append("AND (");
        builder.append("(REQUISITIONOWNERSSTATUS.IdRequisitionOwners = BUSINESSMAN.IdRequisitionOwners AND BUSINESSMAN.IdBusinessman = USERS.IdUser AND REQUISITIONOWNERSSTATUS.Status IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_BUSINESSMAN_BUSY_STATUS'"));
        builder.append(")) OR (REQUISITIONOWNERSSTATUS.IdRequisitionOwners = LAWYER.IdRequisitionOwners AND LAWYER.IdLawyer = USERS.IdUser AND REQUISITIONOWNERSSTATUS.Status IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_LAWYER_BUSY_STATUS'"));
        builder.append(")) OR (REQUISITIONOWNERSSTATUS.IdRequisitionOwners = DECIDERLAWYER.IdRequisitionOwners AND DECIDERLAWYER.IdDeciderLawyer = USERS.IdUser AND REQUISITIONOWNERSSTATUS.Status IN (");
        builder.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_DECIDER_LAWYER_BUSY_STATUS'"));
        builder.append(")) ) ORDER BY REQUISITIONOWNERS.IdRequisitionOwners, USERS.IdUser");
        return builder.toString();
    }
    
    @Override
    public List<RequisitionOwners> findContractsToExpireForAlerts(final RequisitionOwners requisitionOwners)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindContractstoExpireForAlertsNamedParameters(requisitionOwners);
            return this.namedjdbcTemplate.query(this.buildFindContratcsToExpireForAlertsQuery(),
                    namedParameters, new BeanPropertyRowMapper<>(RequisitionOwners.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource createFindContractstoExpireForAlertsNamedParameters(
            final RequisitionOwners requisitionOwners) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        this.addExpirationsParameters(requisitionOwners, namedParameters);
        return namedParameters;
    }
    
    private void addExpirationsParameters(
            final RequisitionOwners requisitionOwners,
            final MapSqlParameterSource namedParameters) {
        namedParameters.addValue("BeforeDaysExpirationAlert", requisitionOwners.getBeforeDaysExpirationAlert());
        namedParameters.addValue("AfterDaysExpirationAlert", requisitionOwners.getAfterDaysExpirationAlert());
    }
    
    private String buildFindContratcsToExpireForAlertsQuery() {
        final StringBuilder query = new StringBuilder();
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
        query.append("SELECT DISTINCT REQUISITION.IdRequisitionOwners, CATEGORY.Name AS CategoryName, ");
        query.append("CustomerRFC, CustomerCompanyName, StartDate, EndDate, CONCAT(BUSINESSMAN.Name, ' ', ");
        query.append("BUSINESSMAN.FirstLastName, ' ',BUSINESSMAN.SecondLastName) AS BusinesmanName,BUSINESSMAN.Email ");
        query.append("AS BusinessManEMail, CASE WHEN ").append(formatedTodayDate).append(BETWEEN);
        query.append(this.databaseUtils.buildDateAddDays(TAG_BEFORE_DAYS_EXPIRATION_ALERT, CAST_END_DATE_AS_DATE));
        query.append(" AND EndDate THEN 'before' ELSE 'after' END AS DateValue ");
        query.append("FROM REQUISITIONOWNERS REQUISITION INNER JOIN USERS BUSINESSMAN ");
        query.append("ON REQUISITION.IdBusinessman = BUSINESSMAN.IdUser INNER JOIN ");
        query.append("CATEGORY ON REQUISITION.IdCategory = CATEGORY.IdCategory INNER JOIN REQUISITIONOWNERSSTATUS ");
        query.append("STATUS ON REQUISITION.IdRequisitionOwners = STATUS.IdRequisitionOwners ");
        query.append("WHERE Status.Status = 'ENTERPRISE_REQUISITION_CLOSE' AND IsExpiredAttended = 0 AND ");
        this.buildDateBetweenExpirationAlertDays(query, formatedTodayDate);
        return query.toString();
    }
    
    private void buildDateBetweenExpirationAlertDays(final StringBuilder query,
            final String formatedTodayDate) {
        query.append(formatedTodayDate).append(BETWEEN);
        query.append(this.databaseUtils.buildDateAddDays(TAG_BEFORE_DAYS_EXPIRATION_ALERT, CAST_END_DATE_AS_DATE));
        query.append(AND);
        query.append(this.databaseUtils.buildDateAddDays(":AfterDaysExpirationAlert", CAST_END_DATE_AS_DATE));
    }
}
