package mx.pagos.admc.contracts.daos.owners;

import java.sql.Types;
import java.text.SimpleDateFormat;
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
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.owners.RequisitionOwnersable;
import mx.pagos.admc.contracts.structures.ContractCancellationComment;
import mx.pagos.admc.contracts.structures.TrayFilter;
import mx.pagos.admc.contracts.structures.TrayRequisition;
import mx.pagos.admc.contracts.structures.owners.OwnersVersion;
import mx.pagos.admc.contracts.structures.owners.RequisitionOwners;
import mx.pagos.admc.enums.SectionTypeEnum;
import mx.pagos.admc.util.shared.ParametersHolder;
import mx.pagos.admc.util.shared.ParametersHolderConstants;
import mx.pagos.document.versioning.structures.DocumentBySection;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Repository
public class RequisitionOwnersDAO implements RequisitionOwnersable {
    private static final String COMMA = ", ";
    private static final String SPACE_STRING = "' '";
    private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String ROST_DATE_PLUS_ROST_ATTENTION_DAYS = "(ROST.TurnDate + ROST.AttentionDays)";
    private static final String AND = " AND ";
    private static final String CAST_END_DATE_AS_DATE = "CAST(EndDate AS DATE)";
    private static final String END_DATE_REQUISITION = "EndDateRequisition";
    private static final String START_DATE_REQUISITION = "StartDateRequisition";
    private static final String END_DATE_CONTRACT = "EndDateContract";
    private static final String START_DATE_CONTRACT = "StartDateContract";
    private static final String WHEN = "WHEN ";
    private static final String SINGLE_QUOTE = "'";
    private static final String FROM_REQUISITIONOWNERDIGITALIZATIONS = "FROM REQUISITIONOWNERDIGITAL ";
    private static final String AND_SECTION_TYPE_EQUALS = "AND SectionType = :SectionType ";
    private static final String FROM_REQUISITIONOWNERSATTACHMENT = "FROM REQUISITIONOWNERSATTACHMENT ";
    private static final String UPDATE_REQUISITIONOWNERS_SET = "UPDATE REQUISITIONOWNERS SET ";
    private static final String WHERE_ID_EQUALS_ID = "WHERE IdRequisitionOwners = :IdRequisitionOwners ";
    private static final String DELETE_FROM_REQUISITIONOWNERSATTACHMENT = "DELETE FROM REQUISITIONOWNERSATTACHMENT ";
    private static final String DELETE_FROM_REQUISITIONOWNERSCHECKDOCUMENTATION = 
            "DELETE FROM REQUISITIONOWNERSCHECKDOC ";
    private static final String INSERT_INTO_REQUISITIONOWNERSCHECKDOCUMENTATION = 
            "INSERT INTO REQUISITIONOWNERSCHECKDOC (IdRequisitionOwners,IdCategoryCheckDocumentation) ";
    private static final String NULL = "Null";
    private static final String LIKE = "%";

    @Autowired
    private DatabaseUtils databaseUtils;

    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    public void setDatabaseUtils(final DatabaseUtils databaseUtilsParameter) {
        this.databaseUtils = databaseUtilsParameter;
    }

    @Override
    public Integer saveOrUpdate(final RequisitionOwners requisition) throws DatabaseException {
        return requisition.getIdRequisitionOwners() == null ? this.insertRequisition(requisition) : 
            this.updateRequisition(requisition);
    }

    private Integer insertRequisition(final RequisitionOwners requisition) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
            final KeyHolder keyHolder = new GeneratedKeyHolder(); 
            this.namedjdbcTemplate.update(this.buildInsertRequisitionQuery(), namedParameters,  keyHolder, 
                    new String[]{"IdRequisitionOwners"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private Integer updateRequisition(final RequisitionOwners requisition) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
            this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
            return requisition.getIdRequisitionOwners();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

	@Override
	public void saveLoadingProject(final RequisitionOwners requisitionParameter) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.buildSaveLoadingProjectPrameters(requisitionParameter);
            this.namedjdbcTemplate.update(this.buildSaveLoadingProjectQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
    @Override
    public void insertCheckList(final Integer idRequisition, final Integer idParameter)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.insertCheckListParameterSource(idRequisition, idParameter);
            final StringBuilder queryDelete = new StringBuilder();
            queryDelete.append(INSERT_INTO_REQUISITIONOWNERSCHECKDOCUMENTATION);
            queryDelete.append("VALUES (:IdRequisitionOwners,:IdCategoryCheckDocumentation) ");
            this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public RequisitionOwners findRequisitionOwnersById(final Integer idRequisitionOwners) 
            throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createFindRequisitionOwnersByIdNamedParameters(idRequisitionOwners);
            return this.namedjdbcTemplate.queryForObject(this.buildFindRequisitionOwnersByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<RequisitionOwners>(RequisitionOwners.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Integer> findCheckDocumentationByIdRequisitionOwner(final Integer idRequisitionOwner) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            return this.namedjdbcTemplate.queryForList(this.findCheckDocumentationByIdRequisitionOwnerQuery(), 
                    parameterSource, Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveDeciderLawyer(final Integer idRequisitionOwner, final Integer idDeciderLawyer) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            parameterSource.addValue(TableConstants.ID_DECIDER_LAWYER, idDeciderLawyer);
            this.namedjdbcTemplate.update(this.saveDeciderLawyerQuery(), parameterSource);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveLawyer(final Integer idRequisitionOwner, final Integer idLawyer) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            parameterSource.addValue(TableConstants.ID_LAWYER, idLawyer);
            this.namedjdbcTemplate.update(this.buildSaveLawyerQuery(), parameterSource);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }        
    }

    @Override
    public List<Integer> findRequisitionOwnersAttachmentByIdRequisitionOwners(final Integer idRequisitionOwner, 
            final SectionTypeEnum sectionType) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = this.attachmentsParameters(idRequisitionOwner, sectionType);
            return this.namedjdbcTemplate.queryForList(this.findRequisitionOwnersAttachmentByIdRequisitionOwnersQuery(),
                    parameterSource, Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<DocumentBySection> findDocumentsAttachmentBySectionType(final Integer idRequisitionOwner, 
            final SectionTypeEnum sectionType) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = this.attachmentsParameters(idRequisitionOwner, sectionType);
            return this.namedjdbcTemplate.query(this.findDocumentsAttachmentBySectionTypeQuery(), parameterSource,
                    new BeanPropertyRowMapper<DocumentBySection>(DocumentBySection.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource attachmentsParameters(final Integer idRequisitionOwner, 
            final SectionTypeEnum sectionType) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
        parameterSource.addValue(TableConstants.SECTION_TYPE, sectionType.toString());
        return parameterSource;
    }

    @Override
    public Integer saveRequisitionAttatchmentOwners(final Integer idRequisitionOwner, final Integer idDocument,
            final String sectionType, final String documentName) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createRequisitionAttachmentOwnersNamedParameters(idRequisitionOwner, idDocument, 
                            sectionType, documentName);
            this.namedjdbcTemplate.update(this.buildsaveRequisitionAttatchmentOwnersQuery(), namedParameters);
            return idRequisitionOwner;
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void deleteCheckListDocumentsByIdRequisition(final Integer idRequisition) 
            throws DatabaseException {
        try {
            final StringBuilder queryDelete = new StringBuilder();
            queryDelete.append(DELETE_FROM_REQUISITIONOWNERSCHECKDOCUMENTATION);
            queryDelete.append(WHERE_ID_EQUALS_ID);
            final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
            this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }       
    }

    @Override
    public void deleteRequisitionAttatchmentOwnersByIdRequisitionAndSectionType(final Integer idRequisition, 
            final String sectionType) throws DatabaseException {
        try {
            final StringBuilder queryDelete = new StringBuilder();
            queryDelete.append(DELETE_FROM_REQUISITIONOWNERSATTACHMENT);
            queryDelete.append(WHERE_ID_EQUALS_ID);
            queryDelete.append("AND SectionType =:SectionType ");
            final MapSqlParameterSource namedParameters = this.deleteParameterSource(idRequisition, sectionType);
            this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }       
    }

    @Override
    public void updateJurisdictionByIdRequisition(final Integer idRequisition, final String jurisdiction)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.saveOrUpdateJurisdictionByIdRequisitionParameters(idRequisition, jurisdiction);
            this.namedjdbcTemplate.update(this.buildUpdateJurisdictionByIdRequisitionQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<TrayRequisition> findRequisitionsForTray(final TrayFilter trayFilter, final Integer pageNumber, 
            final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createfindTrayRequisitionsNamedParameters(trayFilter);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUISITION,
                    this.buildFindRequisitionsForTrayQuery(), pageNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, namedParameters,
                    new BeanPropertyRowMapper<>(TrayRequisition.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Long countTotalRowsForTray(final TrayFilter trayFilter) throws DatabaseException {
    	try {
            final MapSqlParameterSource parameterSource = this.createfindTrayRequisitionsNamedParameters(trayFilter);
            final String countRows = this.databaseUtils.countTotalRows(this.buildFindRequisitionsForTrayQuery());
            return this.namedjdbcTemplate.queryForObject(countRows, parameterSource, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
	private MapSqlParameterSource buildSaveLoadingProjectPrameters(final RequisitionOwners requisitionParameter) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, 
        		requisitionParameter.getIdRequisitionOwners());
        namedParameters.addValue(TableConstants.IS_STANDARIZED, 
        		requisitionParameter.getIsStandarized());
        return namedParameters;
	}
	
	private String buildSaveLoadingProjectQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE REQUISITIONOWNERS SET IsStandarized =:IsStandarized, ");
        query.append("DateFirstProject = GETDATE() ");
        query.append("WHERE IdRequisitionOwners =:IdRequisitionOwners ");
        return query.toString();
	}
    
    private String buildFindRequisitionsForTrayQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT REQUISITION.IdRequisitionOwners AS IdRequisition, CustomerCompanyName AS SupplierName, ");
        query.append("CATEGORY.Name AS DocumentTypeName, REQUISITION.ApplicationDate, REQSTATUS.Status, ");
        query.append("ROST.Turn AS Retry, COMMENTOWNER.CommentText AS Comment, ");
        this.buildSemaphoreFields(query);
        query.append("FROM REQUISITIONOWNERS REQUISITION INNER JOIN ");
        query.append("(SELECT REQUISITIONOWNERSSTATUSTURN.IdRequisitionOwners, MAXTURN.Turn AS Turn, ");
        query.append("REQUISITIONOWNERSSTATUSTURN.AttentionDays, REQUISITIONOWNERSSTATUSTURN.TurnDate, REQUISITIONOWNERSSTATUSTURN.Status ");
        query.append("FROM REQUISITIONOWNERSSTATUSTURN INNER JOIN ");
        query.append("(SELECT IdRequisitionOwners, MAX(Turn) AS Turn ");
        query.append("FROM REQUISITIONOWNERSSTATUSTURN WHERE Status = :Status ");
        query.append("GROUP BY IdRequisitionOwners) MAXTURN ON REQUISITIONOWNERSSTATUSTURN.IdRequisitionOwners = MAXTURN.IdRequisitionOwners AND REQUISITIONOWNERSSTATUSTURN.Turn = MAXTURN.Turn ");
        query.append("WHERE REQUISITIONOWNERSSTATUSTURN.Status = :Status) ROST ON REQUISITION.IdRequisitionOwners = ROST.IdRequisitionOwners ");
        query.append("INNER JOIN REQUISITIONOWNERSSTATUS REQSTATUS ON REQSTATUS.IdRequisitionOwners = ");
        query.append("ROST.IdRequisitionOwners AND REQSTATUS.Status = ROST.Status AND REQSTATUS.HoldForBranch IS NULL ");
        query.append("LEFT JOIN ALERTCONFIG ALERT ON REQUISITION.IdFlow = ALERT.IdFlow AND REQSTATUS.Status = ALERT.Status AND (ALERT.Turn = -1 OR ALERT.Turn = ROST.Turn) ");
        query.append("LEFT JOIN CATEGORY ON REQUISITION.IdCategory = CATEGORY.IdCategory ");
        query.append("LEFT JOIN COMMENTOWNER ON COMMENTOWNER.IdRequisitionOwners = REQUISITION.IdRequisitionOwners ");
        query.append("AND COMMENTOWNER.FlowStatus = :Status ");
        query.append("LEFT JOIN (SELECT IdRequisitionOwners, MAX(CreateDate) AS CreateDate FROM COMMENTOWNER ");
        query.append("WHERE FlowStatus = :Status GROUP BY IdRequisitionOwners) RECENT_COMMENT ON ");
        query.append("COMMENTOWNER.IdRequisitionOwners = RECENT_COMMENT.IdRequisitionOwners, ");
        query.append("(SELECT CAST(Value AS DECIMAL(3,2)) AS RedSemaphore FROM CONFIGURATION WHERE Name = 'RED_SEMAPHORE_PERCENTAGE') REDSEMAPHORE,");
        query.append("(SELECT CAST(Value AS DECIMAL(3,2)) AS YellowSemaphore FROM CONFIGURATION WHERE Name = 'YELLOW_SEMAPHORE_PERCENTAGE') YELLOWSEMAPHORE ");
        query.append("WHERE ROST.Status = :Status AND (COMMENTOWNER.CreateDate = RECENT_COMMENT.CreateDate ");
        query.append("OR COMMENTOWNER.CreateDate IS NULL) AND  ");
        query.append("((:IdRequisitionNull IS NULL OR REQUISITION.IdRequisitionOwners = :IdRequisition) AND ");
        query.append("(:DocumentTypeNull IS NULL OR CATEGORY.Name LIKE :DocumentType) AND ");
        query.append("(:SupplierNameNull IS NULL OR CustomerCompanyName LIKE :SupplierName)) AND (");
        query.append("((REQUISITION.IdBusinessman = :IdUser OR :isUserFiltered = 0) AND REQSTATUS.Status IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_BUSINESSMAN_BUSY_STATUS'")).append("");
        query.append(")) OR ((REQUISITION.IdLawyer = :IdUser OR :isUserFiltered = 0) AND REQSTATUS.Status IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_LAWYER_BUSY_STATUS'"));
        query.append(")) OR ((REQUISITION.IdDeciderLawyer = :IdUser OR :isUserFiltered = 0) AND REQSTATUS.Status IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_DECIDER_LAWYER_BUSY_STATUS'"));
        query.append(")) OR (REQSTATUS.Status IN (");
        query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'MANAGERIAL_NOT_ASIGNED_BUSY_STATUS'"));
        query.append("))) ORDER BY IdRequisition DESC ");
        return query.toString();
    }
    
    private void buildSemaphoreFields(final StringBuilder query) {
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
        query.append("CASE ").append(WHEN);
        query.append(this.databaseUtils.buildDateDiffSSqlFunction(
                formatedTodayDate, ROST_DATE_PLUS_ROST_ATTENTION_DAYS));
        query.append(" < REDSEMAPHORE.RedSemaphore");
        query.append(" * ROST.AttentionDays THEN 'RED' ");
        query.append(WHEN).append(this.databaseUtils.buildDateDiffSSqlFunction(
                formatedTodayDate, ROST_DATE_PLUS_ROST_ATTENTION_DAYS));
        query.append(" < YELLOWSEMAPHORE.YellowSemaphore");
        query.append(" * ROST.AttentionDays THEN 'YELLOW' ");
        query.append("ELSE 'GREEN' END AS Semaphore ");
    }

    private MapSqlParameterSource createfindTrayRequisitionsNamedParameters(final TrayFilter trayFilter) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_USER, trayFilter.getIdUser());
        namedParameters.addValue(TableConstants.IS_USER_FILTERED, trayFilter.getIsUserFiltered());
        namedParameters.addValue(TableConstants.STATUS, trayFilter.getStatus() != null ? trayFilter.getStatus().toString() : trayFilter.getStatus());
        namedParameters.addValue(TableConstants.ID_REQUISITION + NULL, trayFilter.getIdRequisition());
        namedParameters.addValue(TableConstants.ID_REQUISITION, trayFilter.getIdRequisition());
        namedParameters.addValue(TableConstants.DOCUMENT_TYPE + NULL, trayFilter.getDocumentType());
        namedParameters.addValue(TableConstants.DOCUMENT_TYPE, QueryConstants.ANY_CHARACTER +
                trayFilter.getDocumentType() + QueryConstants.ANY_CHARACTER);
        namedParameters.addValue(TableConstants.SUPPLIER_NAME + NULL, trayFilter.getSupplierName());
        namedParameters.addValue(TableConstants.SUPPLIER_NAME, QueryConstants.ANY_CHARACTER +
                trayFilter.getSupplierName() + QueryConstants.ANY_CHARACTER);
        return namedParameters;
    }

    public void deleteById(final Integer idRequisitionOwners) {
        final MapSqlParameterSource namedParameters =
                this.createFindRequisitionOwnersByIdNamedParameters(idRequisitionOwners);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
    }

    public void deleteTestDependencies() {
        this.namedjdbcTemplate.update(this.buildDeleteTestDependenciesQuery(), new MapSqlParameterSource());
    }

    private String buildInsertRequisitionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERS (IdLawyer, IdCurrency, IdBusinessman, IdCategory, ");
        query.append("CustomerCompanyName, CustomerRFC, IdOrganizationEntity, IdFlow) ");
        query.append("VALUES(:IdLawyer, :IdCurrency, :IdBusinessman,:IdCategory,  ");
        query.append(":CustomerCompanyName, :CustomerRFC, :IdOrganizationEntity, :IdFlow) ");
        return query.toString();
    }

    private String buildUpdateQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE REQUISITIONOWNERS SET IdLawyer =:IdLawyer, ");
        query.append("IdCurrency =:IdCurrency, IdBusinessman =:IdBusinessman, IdCategory =:IdCategory, ");
        query.append("IdOrganizationEntity = :IdOrganizationEntity, ");
        query.append("CustomerCompanyName = :CustomerCompanyName, CustomerRFC = :CustomerRFC ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private BeanPropertySqlParameterSource createRequisitionNamedParameters(
            final RequisitionOwners requisition) {
        final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
        namedParameters.registerSqlType("Validity", Types.VARCHAR);
        return namedParameters;
    }

    private MapSqlParameterSource createFindRequisitionOwnersByIdNamedParameters(final Integer idRequisitionOwners) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwners);
        return namedParameters;
    }

    private String buildFindRequisitionOwnersByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT RO.IdRequisitionOwners, RO.IdLawyer, RO.IdCurrency, RO.IdCategory, ");
        query.append("RO.IdBusinessman, RO.IdDeciderLawyer, RO.Jurisdiction, RO.IdOrganizationEntity, RO.IdFlow, ");
        query.append("CustomerCompanyName, CustomerRfc, CURRENCY.Name AS CurrencyName, ");
        query.append("CATEGORY.Name AS CategoryName, ORGANIZATIONENTITY.Name AS OrganizationEntityName, ");
        query.append("PUBLICFIGURE.Name AS PublicBroker, NOTARY.Name AS PublicNotary, ");
        query.append(this.databaseUtils.buildConcat("LAWYER.Name", SPACE_STRING,
                "LAWYER.FirstLastName", SPACE_STRING, "LAWYER.SecondLastName"));
        query.append(" AS LawyerAssigmentName, ").append(this.buildSignNotificationFields());
        query.append(COMMA).append(this.databaseUtils.buildConcat("USERS.Name", SPACE_STRING,
                "USERS.FirstLastName", SPACE_STRING, "USERS.SecondLastName")).append("");
        query.append(" AS UserProjectReviewName, RO.isStandarized ");
        query.append("FROM REQUISITIONOWNERS RO INNER JOIN CURRENCY ON RO.IdCurrency = CURRENCY.IdCurrency ");
        query.append("INNER JOIN ORGANIZATIONENTITY ");
        query.append("ON RO.IdOrganizationEntity = ORGANIZATIONENTITY.IdOrganizationEntity ");
        query.append("LEFT JOIN PUBLICFIGURE ON RO.IdPublicBroker = PUBLICFIGURE.IdPublicFigure ");
        query.append("LEFT JOIN PUBLICFIGURE NOTARY ON RO.IdPublicNotary = NOTARY.IdPublicFigure ");
        query.append("LEFT JOIN CATEGORY ON RO.IdCategory = CATEGORY.IdCategory ");
        query.append("LEFT JOIN USERS ON RO.IdUserProjectReviewVoBo = USERS.IdUser ");
        query.append("LEFT JOIN USERS LAWYER ON RO.IdLawyer = LAWYER.IdUser ");
    }

    private String findCheckDocumentationByIdRequisitionOwnerQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdCategoryCheckDocumentation ");
        builder.append("FROM REQUISITIONOWNERSCHECKDOC ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }

    private String saveDeciderLawyerQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append(UPDATE_REQUISITIONOWNERS_SET);
        builder.append("IdDeciderLawyer = :IdDeciderLawyer ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }

    private String buildSaveLawyerQuery() throws DatabaseException {
        final StringBuilder builder = new StringBuilder();
        builder.append(UPDATE_REQUISITIONOWNERS_SET);
        builder.append("IdLawyer = :IdLawyer ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }

    private String findRequisitionOwnersAttachmentByIdRequisitionOwnersQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdDocument FROM REQUISITIONOWNERSATTACHMENT ");
        builder.append(WHERE_ID_EQUALS_ID);
        builder.append(AND_SECTION_TYPE_EQUALS);
        return builder.toString();
    }

    private String findDocumentsAttachmentBySectionTypeQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdRequisitionOwners, IdDocument, SectionType, DocumentName ");
        builder.append(FROM_REQUISITIONOWNERSATTACHMENT);
        builder.append(WHERE_ID_EQUALS_ID);
        builder.append(AND_SECTION_TYPE_EQUALS);
        return builder.toString();
    }

    private String buildsaveRequisitionAttatchmentOwnersQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERSATTACHMENT VALUES (:IdRequisitionOwners,:IdDocument,:SectionType, "
                + ":DocumentName)");
        return query.toString();
    }

    private MapSqlParameterSource createRequisitionAttachmentOwnersNamedParameters(final Integer idRequisitionOwner,
            final Integer idDocument, final String sectionType, final String documentName) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
        namedParameters.addValue(TableConstants.ID_DOCUMENT, idDocument);
        namedParameters.addValue(TableConstants.SECTION_TYPE, sectionType);
        namedParameters.addValue(TableConstants.DOCUMENT_NAME, documentName);
        return namedParameters;
    }

    private MapSqlParameterSource saveOrUpdateJurisdictionByIdRequisitionParameters(final Integer idRequisition, 
            final String jurisdiction) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisition);
        parameterSource.addValue(TableConstants.JURISDICTION, jurisdiction);
        return parameterSource;
    }

    private MapSqlParameterSource deleteParameterSource(final Integer idRequisition, final String sectionType) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisition);
        parameterSource.addValue(TableConstants.SECTION_TYPE, sectionType);
        return parameterSource;
    }

    private MapSqlParameterSource idRequisitionParameterSource(final Integer idRequisition) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisition);
        return parameterSource;
    }

    @Override
    public Boolean haveDictaminationDocumentsOfRequisitionOwner(final Integer idRequisitionOwner) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            return this.namedjdbcTemplate.queryForObject(this.haveDictaminationDocumentsQuery(), parameterSource, 
                    Boolean.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String haveDictaminationDocumentsQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(IdRequisitionOwners) ");
        builder.append(FROM_REQUISITIONOWNERSATTACHMENT);
        builder.append("WHERE IdRequisitionOwners = :IdRequisitionOwners AND SectionType = 'DICTAMINATION' ");
        return builder.toString();
    }

    private MapSqlParameterSource insertCheckListParameterSource(final Integer idRequisition, 
            final Integer idParameter) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisition);
        parameterSource.addValue(TableConstants.ID_CATEGORY_CHECK_DOCUMENTATION, idParameter);
        return parameterSource;
    }

    private String buildUpdateJurisdictionByIdRequisitionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_REQUISITIONOWNERS_SET);
        query.append("Jurisdiction =:Jurisdiction ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONOWNERS WHERE IdRequisitionOwners = :IdRequisitionOwners");
        return query.toString();
    }

    private String buildDeleteTestDependenciesQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildDeleteTestDependenciesDeclareSection(query);
        this.buildDeleteTestDependenciesSelectSection(query);
        this.buildDeleteTestDependenciesDeleteSection(query);
        return query.toString();
    }

    private void buildDeleteTestDependenciesDeclareSection(final StringBuilder query) {
        query.append("DECLARE @IdRequisitionOwners AS BIGINT ");
        query.append("DECLARE @IdUser AS BIGINT ");
        query.append("DECLARE @IdArea AS BIGINT ");
        query.append("DECLARE @IdDga AS BIGINT ");
        query.append("DECLARE @IdPosition AS BIGINT ");
        query.append("DECLARE @IdCurrencies TABLE (IdCurrency BIGINT) ");
        query.append("DECLARE @IdOrganizationEntity AS BIGINT ");
    }

    private void buildDeleteTestDependenciesSelectSection(final StringBuilder query) {
        query.append("SELECT @IdUser = IdUser, @IdArea = IdArea, @IdDga = IdDga, @IdPosition = IdPosition ");
        query.append("FROM USERS WHERE Username = 'REQUISITION OWNERS TEST USERNAME' ");
        query.append("SELECT @IdRequisitionOwners = IdRequisitionOwners,@IdOrganizationEntity = IdOrganizationEntity ");
        query.append("FROM REQUISITIONOWNERS WHERE IdBusinessman = @IdUser ");
        query.append("INSERT INTO @IdCurrencies (IdCurrency) ");
        query.append("SELECT IdCurrency FROM CURRENCY WHERE Name LIKE 'REQUISITION OWNERS TEST %' ");
    }

    private void buildDeleteTestDependenciesDeleteSection(final StringBuilder query) {
        query.append("DELETE FROM REQUISITIONOWNERS WHERE IdRequisitionOwners = @IdRequisitionOwners ");
        query.append("DELETE FROM USERS WHERE IdUser = @IdUser ");
        query.append("DELETE FROM AREA WHERE IdArea = @IdArea ");
        query.append("DELETE FROM DGA WHERE IdDga = @IdDga ");
        query.append("DELETE FROM POSITION WHERE IdPosition = @IdPosition ");
        query.append("DELETE FROM CURRENCY WHERE IdCurrency IN (SELECT IdCurrency FROM @IdCurrencies) ");
        query.append("DELETE FROM ORGANIZATIONENTITY WHERE IdOrganizationEntity = @IdOrganizationEntity ");
    }

    @Override
    public void saveIdDictaminationTemplate(final Integer idRequisitionOwner, 
            final Integer idDictaminationTemplate) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.ID_DICTAMIANTION_TEMPLATE, idDictaminationTemplate);
            parameterSource.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            this.namedjdbcTemplate.update(this.saveIdDictaminationTemplateQuery(), parameterSource);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String saveIdDictaminationTemplateQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append(UPDATE_REQUISITIONOWNERS_SET);
        builder.append("IdDictaminationTemplate = :IdDictaminationTemplate ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }

    @Override
    public void saveSignNotification(final RequisitionOwners requisitionOwners) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(requisitionOwners);
            this.namedjdbcTemplate.update(this.buildSaveSignNotificationQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildSignNotificationFields() {
        final StringBuilder query = new StringBuilder();
        query.append("IsPublicNotaryDelivered, IsVerifiedReceivedContract, CreditResolutionNumber, ");
        query.append("CreditTypeProduct, Amount, DocumentType, IdPublicBroker, IdPublicNotary, DeedNumber, ");
        query.append("StartDate, EndDate, IsPrivateFormalization, IsPublicNotaryRatified, IsPublicRedordEnmrolled, ");
        query.append("IdLawyerVobo ");
        return query.toString();
    }

    private String buildSaveSignNotificationQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_REQUISITIONOWNERS_SET);
        query.append(JdbcTemplateUtils.buildUpdateFields(this.buildSignNotificationFields(), COMMA));
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    @Override
    public void saveRequisitionOwnersDigitalizations(final Integer idRequisitionOwner, final Integer idDocument, 
            final SectionTypeEnum sectionType, final String documentName) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = this.createRequisitionAttachmentOwnersNamedParameters(
                    idRequisitionOwner, idDocument, sectionType.toString(), documentName);
            this.namedjdbcTemplate.update(this.saveRequisitionOwnersDigitalizationsQuery(), parameterSource);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String saveRequisitionOwnersDigitalizationsQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO REQUISITIONOWNERDIGITAL ");
        builder.append("(IdRequisitionOwners, IdDocument, SectionType, DocumentName) ");
        builder.append("VALUES(:IdRequisitionOwners, :IdDocument, :SectionType, :DocumentName) ");
        return builder.toString();
    }

    @Override
    public void deleteRequisitionOwnersDigitalizationsByIdRequisitionAndSectionType(
            final Integer idRequisitionOwner, final SectionTypeEnum sectionType) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = 
                    this.deleteParameterSource(idRequisitionOwner, sectionType.toString());
            this.namedjdbcTemplate.update(
                    this.deleteRequisitionOwnersDigitalizationsByIdRequisitionAndSectionTypeQuery(), parameterSource);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }        
    }

    private String deleteRequisitionOwnersDigitalizationsByIdRequisitionAndSectionTypeQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM REQUISITIONOWNERDIGITAL WHERE IdRequisitionOwners = :IdRequisitionOwners ");
        builder.append(AND_SECTION_TYPE_EQUALS);
        return builder.toString();
    }

    @Override
    public List<DocumentBySection> findDigitalizationBySectionType(final Integer idRequisitionOwner, 
            final SectionTypeEnum sectionType) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = this.attachmentsParameters(idRequisitionOwner, sectionType);
            return this.namedjdbcTemplate.query(this.findDigitalizationBySectionTypeQuery(), parameterSource,
                    new BeanPropertyRowMapper<DocumentBySection>(DocumentBySection.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findDigitalizationBySectionTypeQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdRequisitionOwners, IdDocument, SectionType, DocumentName ");
        builder.append(FROM_REQUISITIONOWNERDIGITALIZATIONS);
        builder.append(WHERE_ID_EQUALS_ID);
        builder.append(AND_SECTION_TYPE_EQUALS);
        return builder.toString();
    }

    @Override
    public List<Version> findDictaminationVersionsByIdRequisitionOwner(final Integer idRequisitionOwner) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = this.idRequisitionParameterSource(idRequisitionOwner);
            return this.namedjdbcTemplate.query(this.findDictaminationVersionsByIdRequisitionOwnerQuery(),
                    parameterSource, new BeanPropertyRowMapper<Version>(Version.class));
        } catch (DataAccessException dataAccessException) { 
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findDictaminationVersionsByIdRequisitionOwnerQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT VERSION.IdVersion, VERSION.IdDocument, VERSION.DocumentPath, VERSION.VersionNumber ");
        builder.append("FROM REQUISITIONOWNERS INNER JOIN VERSION ");
        builder.append("ON REQUISITIONOWNERS.IdDictaminationTemplate = VERSION.IdDocument ");
        builder.append(WHERE_ID_EQUALS_ID);
        builder.append("ORDER BY VERSION.IdVersion DESC ");
        return builder.toString();
    }

    @Override
    public List<RequisitionOwners> findRequisitionDocumentsContracsAndGuaranteesByParameters(
            final ParametersHolder parameters) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = 
                    this.requisitionOwnersContractsAndGuaranteesParameter(parameters);
            return this.namedjdbcTemplate.query(
                    this.findRequisitionDocumentsContracsAndGuaranteesByParametersQuery(
                            parameters.getParameterValue(ParametersHolderConstants.LESS_OR_MORE_CONTRACT).toString(),
                            parameters.getParameterValue(ParametersHolderConstants.LESS_OR_MORE_GUARANTEE).toString()), 
                    parameterSource, new BeanPropertyRowMapper<RequisitionOwners>(RequisitionOwners.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource requisitionOwnersContractsAndGuaranteesParameter(
            final ParametersHolder parameters) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.ID_REQUISITION_OWNERS, 
                LIKE + parameters.getParameterValue(TableConstants.ID_REQUISITION_OWNERS) + LIKE);
        source.addValue(TableConstants.ID_REQUISITION_OWNERS + NULL, 
                parameters.getParameterValue(TableConstants.ID_REQUISITION_OWNERS));
        source.addValue(TableConstants.NAME, LIKE + parameters.getParameterValue(TableConstants.NAME) + LIKE);
        source.addValue(TableConstants.NAME + NULL, parameters.getParameterValue(TableConstants.NAME));
        source.addValue(TableConstants.DOCUMENT_NAME, LIKE + 
                parameters.getParameterValue(TableConstants.DOCUMENT_NAME) + LIKE);
        source.addValue(TableConstants.DOCUMENT_NAME + NULL,  
                parameters.getParameterValue(TableConstants.DOCUMENT_NAME));
        return source;
    }

    private String findRequisitionDocumentsContracsAndGuaranteesByParametersQuery(final String lessOrMoreContract,
            final String lessOrMoreGuarantee) {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT REQUISITIONOWNERS.IdRequisitionOwners, CustomerCompanyName AS CompanyName, ");
        builder.append("CATEGORY.Name AS CategoryName, ");
        builder.append("COUNT(REQUISITIONOWNERSATTACHMENT.IdRequisitionOwners) AS TotalDocumentsProjects, ");
        builder.append("COUNT(GUARANTEES.IdRequisitionOwners) AS TotalDocumentsGuarantees ");
        builder.append("FROM REQUISITIONOWNERS ");
        builder.append("INNER JOIN CATEGORY ON REQUISITIONOWNERS.IdCategory = CATEGORY.IdCategory ");
        builder.append("LEFT JOIN REQUISITIONOWNERSATTACHMENT ON ");
        builder.append("REQUISITIONOWNERS.IdRequisitionOwners = REQUISITIONOWNERSATTACHMENT.IdRequisitionOwners ");
        builder.append("AND REQUISITIONOWNERSATTACHMENT.SectionType = 'LOADING_PROJECT' ");
        builder.append("LEFT JOIN REQUISITIONOWNERSATTACHMENT GUARANTEES ON ");
        builder.append("REQUISITIONOWNERS.IdRequisitionOwners = GUARANTEES.IdRequisitionOwners ");
        builder.append("AND GUARANTEES.SectionType = 'LOADING_GUARANTEES' ");
        builder.append("WHERE ((:IdRequisitionOwnersNull IS NULL) OR ");
        builder.append("(REQUISITIONOWNERS.IdRequisitionOwners LIKE :IdRequisitionOwners)) AND ");
        builder.append("((:NameNull IS NULL) OR (CustomerCompanyName LIKE :Name)) AND ");
        builder.append("((:DocumentNameNull IS NULL) OR (CATEGORY.Name LIKE :DocumentName)) ");
        builder.append("GROUP BY REQUISITIONOWNERS.IdRequisitionOwners, CustomerCompanyName, CATEGORY.Name, ");
        builder.append("REQUISITIONOWNERSATTACHMENT.IdRequisitionOwners ");
        builder.append("HAVING COUNT(REQUISITIONOWNERSATTACHMENT.IdRequisitionOwners) ").append(lessOrMoreContract);
        builder.append(" AND COUNT(GUARANTEES.IdRequisitionOwners) ").append(lessOrMoreGuarantee);
        return builder.toString();
    }

    @Override
    public void saveUserDitamenVoBo(final Integer idRequisitionOwner, final Integer idUserDictamenVobo) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            source.addValue(TableConstants.ID_USER_DICTAMEN_VOBO, idUserDictamenVobo);
            this.namedjdbcTemplate.update(this.saveUserDictamenVoBoQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String saveUserDictamenVoBoQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append(UPDATE_REQUISITIONOWNERS_SET);
        builder.append("IdUserDictamenVoBo = :IdUserDictamenVoBo ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }

    @Override
    public void saveUserProjectReviewVoBo(final Integer idRequisitionOwner,
            final Integer idUserProjectReviewVoBo) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            source.addValue(TableConstants.ID_USER_PROJECT_REVIEW_VOBO, idUserProjectReviewVoBo);
            this.namedjdbcTemplate.update(this.saveUserProjectReviewVoBoQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String saveUserProjectReviewVoBoQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append(UPDATE_REQUISITIONOWNERS_SET);
        builder.append("IdUserProjectReviewVoBo = :IdUserProjectReviewVoBo ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }

    @Override
    public void saveUserNotificationSigning(final Integer idRequisitionOwnwer, final Integer idUserSignVoBo) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwnwer);
            source.addValue(TableConstants.ID_USER_SIGN_VOBO, idUserSignVoBo);
            this.namedjdbcTemplate.update(this.saveUserNotificationSigningQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String saveUserNotificationSigningQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append(UPDATE_REQUISITIONOWNERS_SET);
        builder.append("IdUserSignVoBo = :IdUserSignVoBo ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }

    @Override
    public RequisitionOwners findRequisitionMultidocuments(final Integer idRequisitionOwner)
            throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource =
                    this.createFindRequisitionOwnersByIdNamedParameters(idRequisitionOwner);
            return this.namedjdbcTemplate.queryForObject(this.findRequisitionMultidocumentsQuery(), 
                    parameterSource, new BeanPropertyRowMapper<RequisitionOwners>(RequisitionOwners.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findRequisitionMultidocumentsQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT REQUISITIONOWNERS.IdRequisitionOwners, CustomerCompanyName, ");
        builder.append("CATEGORY.Name AS CategoryName, ");
        builder.append("COUNT(REQUISITIONOWNERSATTACHMENT.IdRequisitionOwners) AS TotalDocumentsProjects, ");
        builder.append("COUNT(GUARANTEES.IdRequisitionOwners) AS TotalDocumentsGuarantees ");
        builder.append("FROM REQUISITIONOWNERS ");
        builder.append("INNER JOIN CATEGORY ON REQUISITIONOWNERS.IdCategory = CATEGORY.IdCategory ");
        builder.append("LEFT JOIN REQUISITIONOWNERSATTACHMENT ON ");
        builder.append("REQUISITIONOWNERS.IdRequisitionOwners = REQUISITIONOWNERSATTACHMENT.IdRequisitionOwners ");
        builder.append("AND REQUISITIONOWNERSATTACHMENT.SectionType = 'LOADING_PROJECT' ");
        builder.append("LEFT JOIN REQUISITIONOWNERSATTACHMENT GUARANTEES ON ");
        builder.append("REQUISITIONOWNERS.IdRequisitionOwners = GUARANTEES.IdRequisitionOwners ");
        builder.append("AND GUARANTEES.SectionType = 'LOADING_GUARANTEES' ");
        builder.append("WHERE REQUISITIONOWNERS.IdRequisitionOwners = :IdRequisitionOwners ");
        builder.append("GROUP BY REQUISITIONOWNERS.IdRequisitionOwners, CustomerCompanyName, CATEGORY.Name, ");
        builder.append("REQUISITIONOWNERSATTACHMENT.IdRequisitionOwners ");
        return builder.toString();
    }

    @Override
    public void deleteRequisitionOwnerGuaranteeCheckDocument(final Integer idRequisitionOwner) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            this.namedjdbcTemplate.update(this.deleteRequisitionOwnerGuaranteeCheckDocumentQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String deleteRequisitionOwnerGuaranteeCheckDocumentQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM REQOWNERSGUARANTEECHECKDOC ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }

    @Override
    public void saveRequisitionOwnerGuaranteeCheckDocument(final Integer idRequisitionOwner, 
            final Integer idCheckDocument) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            source.addValue(TableConstants.ID_CHECK_DOCUMENT, idCheckDocument);
            this.namedjdbcTemplate.update(this.saveRequisitionOwnerGuaranteeCheckDocumentQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String saveRequisitionOwnerGuaranteeCheckDocumentQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO REQOWNERSGUARANTEECHECKDOC(IdRequisitionOwners, IdCheckDocument) ");
        builder.append("VALUES(:IdRequisitionOwners, :IdCheckDocument)");
        return builder.toString();
    }

    @Override
    public List<OwnersVersion> findOwnersVersionByIdRequisition(final Integer idRequisitionOwner) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.idRequisitionParameterSource(idRequisitionOwner);
            return this.namedjdbcTemplate.query(this.findOwnersVersionByIdRequisitionQuery(), 
                    source, new BeanPropertyRowMapper<OwnersVersion>(OwnersVersion.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findOwnersVersionByIdRequisitionQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT OWNERVERSION.IdRequisitionOwnersVersion, OWNERVERSION.VersionNumber ");
        builder.append("FROM REQUISITIONOWNERS_VERSION_VERSION_NUMBER AS OWNERVERSION ");
        builder.append("WHERE OWNERVERSION.IdRequisitionOwners = :IdRequisitionOwners ");
        builder.append("ORDER BY OWNERVERSION.VersionNumber DESC ");
        return builder.toString();
    }

    @Override
    public void updateIsExpiredAttended(final Integer idRequisitionOwners, final Boolean isExpiredAttended) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwners);
            source.addValue(TableConstants.IS_EXPIRED_ATTENDED, isExpiredAttended);
            this.namedjdbcTemplate.update(this.updateIsExpiredAttendedQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String updateIsExpiredAttendedQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UPDATE REQUISITIONOWNERS ");
        builder.append("SET IsExpiredAttended = :IsExpiredAttended ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }
    
    @Override
    public Integer saveContractCancellationComment(final ContractCancellationComment cancellationComment)
            throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(cancellationComment);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildSaveContractCancellationCommentQuery(), namedParameters, keyHolder, 
                    new String[]{"IdOwnersCancellationComment"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildSaveContractCancellationCommentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO OWNERSCONTRACTCANCELCOMMENT(CancellationComment, IdRequisitionOwners) ");
        query.append("VALUES(:CancellationComment, :IdRequisition) ");
        return query.toString();
    }
    
    @Override
    public ContractCancellationComment findContractCancellationComment(final Integer idRequisitionOwners)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindRequisitionOwnersByIdNamedParameters(idRequisitionOwners);
            return this.namedjdbcTemplate.queryForObject(this.buildFindContractCancellationCommentQuery(),
                    namedParameters, new BeanPropertyRowMapper<>(ContractCancellationComment.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildFindContractCancellationCommentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdOwnersCancellationComent AS IdCancellationComent, CancellationComment, CreationDate ");
        query.append("FROM OWNERSCONTRACTCANCELCOMMENT ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    public void deleteContractCancellationComment(final Integer idRequisitionOwners)
            throws DatabaseException {
        final MapSqlParameterSource namedParameters =
                this.createFindRequisitionOwnersByIdNamedParameters(idRequisitionOwners);
        this.namedjdbcTemplate.update(this.buildDeleteContractCancellationCommentQuery(), namedParameters);
    }

    private String buildDeleteContractCancellationCommentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM OWNERSCONTRACTCANCELCOMMENT WHERE IdRequisitionOwners = :IdRequisitionOwners");
        return query.toString();
    }
    
    @Override
    public void saveContractCancellationCommentDocument(final Integer idOwnersCancellationComment, 
            final Integer idDocument, final String documentName) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createSaveCancellationDocumentNamedParameters(
                    idOwnersCancellationComment, idDocument, documentName);
            this.namedjdbcTemplate.update(this.buildSaveContractCancellationCommentDocumentQuery(),
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource createSaveCancellationDocumentNamedParameters(
            final Integer idOwnersCancellationComment, final Integer idDocument,
            final String documentName) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_OWNERS_CANCELLATION_COMMENT, idOwnersCancellationComment);
        namedParameters.addValue(TableConstants.ID_DOCUMENT, idDocument);
        namedParameters.addValue(TableConstants.DOCUMENT_NAME, documentName);
        return namedParameters;
    }

    private String buildSaveContractCancellationCommentDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO OWNERSCONTRACTCANCELCOMMENTDOC(IdOwnersCancellationComent, IdDocument, ");
        query.append("DocumentName) VALUES(:IdOwnersCancellationComent, :IdDocument, :DocumentName) ");
        return query.toString();
    }
    
    @Override
    public List<DocumentBySection> findContractCancelationCommentDocument(final Integer idRequisitionOwners) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindRequisitionOwnersByIdNamedParameters(idRequisitionOwners);
            return this.namedjdbcTemplate.query(this.findContractCancelationCommentDocumentQuery(), 
                    namedParameters, new BeanPropertyRowMapper<DocumentBySection>(DocumentBySection.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findContractCancelationCommentDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdDocument, DocumentName FROM OWNERSCONTRACTCANCELCOMMENTDOC ");
        query.append("INNER JOIN OWNERSCONTRACTCANCELCOMMENT ON ");
        query.append("OWNERSCONTRACTCANCELCOMMENTDOC.IdOwnersCancellationComent = ");
        query.append("OWNERSCONTRACTCANCELCOMMENT.IdOwnersCancellationComent ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private void buildFindRequisitionsByFiltersForManagement(final StringBuilder query) {
        query.append("SELECT DISTINCT REQUISITION.IdRequisitionOwners, CATEGORY.Name AS CategoryName, CustomerRFC, ");
        query.append("CustomerCompanyName, StartDate, EndDate, ApplicationDate, ");
        query.append("IdBusinessman, IdLawyer, IdDeciderLawyer, IdCurrency, IdOrganizationEntity, Jurisdiction, ");
        query.append("IsExpiredAttended, CASE WHEN RequisitionCancelled.IdRequisitionOwners IS NULL ");
        query.append("THEN 0 ELSE 1 END AS IsRequisitionCancelled, ");
        query.append("CASE WHEN ContractCancelled.IdRequisitionOwners IS NULL ");
        query.append("THEN 0 ELSE 1 END AS IsContractCancelled ");
        query.append("FROM REQUISITIONOWNERS REQUISITION INNER JOIN CATEGORY ON REQUISITION.IdCategory = ");
        query.append("CATEGORY.IdCategory INNER JOIN REQUISITIONOWNERSSTATUS STATUS ON ");
        query.append("REQUISITION.IdRequisitionOwners = STATUS.IdRequisitionOwners LEFT JOIN ");
        query.append("(SELECT DISTINCT IdRequisitionOwners FROM REQUISITIONOWNERSSTATUS ");
        query.append("WHERE Status = 'ENTERPRISE_REQUISITION_CANCELED') RequisitionCancelled ");
        query.append("ON RequisitionCancelled.IdRequisitionOwners = REQUISITION.IdRequisitionOwners LEFT JOIN ");
        query.append("(SELECT DISTINCT IdRequisitionOwners FROM REQUISITIONOWNERSSTATUS WHERE ");
        query.append("Status = 'ENTERPRISE_CONTRACT_CANCELED' GROUP BY IdRequisitionOwners) ContractCancelled ");
        query.append("ON ContractCancelled.IdRequisitionOwners = REQUISITION.IdRequisitionOwners ");
        this.buildFiltersForManegementQuery(query);
    }

    private void buildFiltersForManegementQuery(final StringBuilder query) {
        query.append("WHERE IdFlow = :IdFlow ");
        query.append("AND (:IdRequisitionOwners IS NULL OR REQUISITION.IdRequisitionOwners = :IdRequisitionOwners) ");
        query.append("AND (:IdCategory IS NULL OR CATEGORY.IdCategory = :IdCategory) ");
        query.append("AND (:CustomerRFC IS NULL OR CustomerRFC LIKE CONCAT('%', :CustomerRFC, '%')) ");
        query.append("AND (:CustomerCompanyName IS NULL OR CustomerCompanyName LIKE ");
        query.append("CONCAT('%',:CustomerCompanyName,'%')) ");
        query.append("AND (:StartDateContract IS NULL OR :EndDateContract IS NULL ");
        query.append("OR StartDate BETWEEN :StartDateContract AND :EndDateContract ");
        query.append("OR EndDate BETWEEN :StartDateContract AND :EndDateContract ");
        query.append("OR :StartDateContract BETWEEN StartDate AND EndDate ");
        query.append("OR :EndDateContract BETWEEN StartDate AND EndDate) ");
        query.append("AND (:StartDateRequisition IS NULL OR :EndDateRequisition IS NULL ");
        query.append("OR CAST(ApplicationDate AS DATE) BETWEEN :StartDateRequisition AND :EndDateRequisition) ");
    }
    
    private MapSqlParameterSource createCommonRequisitionManagementNamedParameters(
            final RequisitionOwners requisitionOwners) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource(); 
        namedParameters.addValue(TableConstants.ID_FLOW, requisitionOwners.getIdFlow());
        namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, requisitionOwners.getIdRequisitionOwners());
        namedParameters.addValue(TableConstants.ID_CATEGORY, requisitionOwners.getIdCategory());
        namedParameters.addValue(TableConstants.CUSTOMER_RFC, requisitionOwners.getCustomerRFC());
        namedParameters.addValue(TableConstants.CUSTOMER_COMPANY_NAME, requisitionOwners.getCustomerCompanyName());
        return namedParameters;
    }
    
    @Override
    public List<RequisitionOwners> findPaginatedContractsForManagement(final RequisitionOwners requisitionOwners,
            final Integer pageNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindContractsForManagementNamedParameters(requisitionOwners);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUISITION_OWNERS,
                    this.buildFindContractsForManagementQuery(), pageNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, namedParameters,
                    new BeanPropertyRowMapper<>(RequisitionOwners.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private MapSqlParameterSource createFindContractsForManagementNamedParameters(
            final RequisitionOwners requisitionOwners) {
        final MapSqlParameterSource namedParameters =
                this.createCommonRequisitionManagementNamedParameters(requisitionOwners);
        namedParameters.addValue(START_DATE_CONTRACT, requisitionOwners.getStartDate(), Types.VARCHAR);
        namedParameters.addValue(END_DATE_CONTRACT, requisitionOwners.getEndDate(), Types.VARCHAR);
        namedParameters.addValue(START_DATE_REQUISITION, null, Types.VARCHAR);
        namedParameters.addValue(END_DATE_REQUISITION, null, Types.VARCHAR);
        return namedParameters;
    }

    private String buildFindContractsForManagementQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildFindRequisitionsByFiltersForManagement(query);
        query.append("AND Status.Status IN ('ENTERPRISE_REQUISITION_CLOSE', 'ENTERPRISE_CONTRACT_CANCELED') ");
        return query.toString();
    }
    
    @Override
    public Long countPaginatedContractsForManagement(final RequisitionOwners requisitionOwners)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindContractsForManagementNamedParameters(requisitionOwners);
            final String countRowsQuery =
                    this.databaseUtils.countTotalRows(this.buildFindContractsForManagementQuery());
            return this.namedjdbcTemplate.queryForObject(countRowsQuery, namedParameters, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<RequisitionOwners> findPaginatedRequisitionsForManagement(
            final RequisitionOwners requisitionOwners, final Integer pageNumber,
            final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindRequisitionsForManagementNamedParameters(requisitionOwners);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUISITION_OWNERS,
                    this.buildFindRequisitionsForManagementQuery(), pageNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, namedParameters,
                    new BeanPropertyRowMapper<>(RequisitionOwners.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private MapSqlParameterSource createFindRequisitionsForManagementNamedParameters(
            final RequisitionOwners requisitionOwners) {
        final MapSqlParameterSource namedParameters =
                this.createCommonRequisitionManagementNamedParameters(requisitionOwners);
        namedParameters.addValue(START_DATE_CONTRACT, null, Types.VARCHAR);
        namedParameters.addValue(END_DATE_CONTRACT, null, Types.VARCHAR);
        namedParameters.addValue(START_DATE_REQUISITION, requisitionOwners.getStartDate(), Types.VARCHAR);
        namedParameters.addValue(END_DATE_REQUISITION, requisitionOwners.getEndDate(), Types.VARCHAR);
        return namedParameters;
    }
    
    private String buildFindRequisitionsForManagementQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildFindRequisitionsByFiltersForManagement(query);
        query.append("AND Status.Status NOT IN ('ENTERPRISE_REQUISITION_CLOSE', 'ENTERPRISE_CONTRACT_CANCELED') ");
        return query.toString();
    }
    
    @Override
    public Long countPaginatedRequisitionsForManagement(final RequisitionOwners requisitionOwners)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindRequisitionsForManagementNamedParameters(requisitionOwners);
            final String countRowsQuery =
                    this.databaseUtils.countTotalRows(this.buildFindRequisitionsForManagementQuery());
            return this.namedjdbcTemplate.queryForObject(countRowsQuery, namedParameters, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void updateManagementInfo(final RequisitionOwners requisitionOwners) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(requisitionOwners);
            this.namedjdbcTemplate.update(this.buildUpdateManagementInfoQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildUpdateManagementInfoQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE REQUISITIONOWNERS  SET IdLawyer = :IdLawyer, IdDeciderLawyer = :IdDeciderLawyer, ");
        query.append("IdOrganizationEntity = :IdOrganizationEntity, IdCurrency = :IdCurrency, ");
        query.append("Jurisdiction = :Jurisdiction, CustomerRFC = :CustomerRFC, ");
        query.append("CustomerCompanyName = :CustomerCompanyName ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    @Override
    public List<RequisitionOwners> findPaginatedContractsToExpire(final RequisitionOwners requisitionOwners,
            final Integer pageNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindContractsToExpireNamedParameters(requisitionOwners);
            return this.namedjdbcTemplate.query(
                    this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUISITION_OWNERS,
                            this.buildFindContractsToExpireQuery(), pageNumber, itemsNumber),
                    namedParameters, new BeanPropertyRowMapper<>(RequisitionOwners.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String buildFindContractsToExpireQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildFindRequisitionsByFiltersForManagement(query);
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
        query.append("AND Status.Status = 'ENTERPRISE_REQUISITION_CLOSE' AND ");
        this.buildDateBetweenExpirationAlertDays(query, formatedTodayDate);
        return query.toString();
    }

    private void buildDateBetweenExpirationAlertDays(final StringBuilder query,
            final String formatedTodayDate) {
        query.append(formatedTodayDate).append(" BETWEEN ");
        query.append(this.databaseUtils.buildDateAddDays(":BeforeDaysExpirationAlert", CAST_END_DATE_AS_DATE));
        query.append(AND);
        query.append(this.databaseUtils.buildDateAddDays(":AfterDaysExpirationAlert", CAST_END_DATE_AS_DATE));
    }

    private MapSqlParameterSource createFindContractsToExpireNamedParameters(
            final RequisitionOwners requisitionOwners) {
        final MapSqlParameterSource namedParameters =
                this.createFindContractsForManagementNamedParameters(requisitionOwners);
        this.addExpirationsParameters(requisitionOwners, namedParameters);
        return namedParameters;
    }
    
    @Override
    public Long countContractsToExpire(final RequisitionOwners requisitionOwners) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindContractsToExpireNamedParameters(requisitionOwners);
            return this.namedjdbcTemplate.queryForObject(
                    this.databaseUtils.countTotalRows(this.buildFindContractsToExpireQuery()),
                    namedParameters, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private void addExpirationsParameters(
            final RequisitionOwners requisitionOwners,
            final MapSqlParameterSource namedParameters) {
        namedParameters.addValue("BeforeDaysExpirationAlert", requisitionOwners.getBeforeDaysExpirationAlert());
        namedParameters.addValue("AfterDaysExpirationAlert", requisitionOwners.getAfterDaysExpirationAlert());
    }

	@Override
	public void saveFreezeInformationOfContractDetailOwners(final Integer idRequisitionOwnersParameter,
			final String contractDetailOwnersJson) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.saveFreezeInformationOfContractDetailOwnersNamedParameters(
                    		idRequisitionOwnersParameter, contractDetailOwnersJson);
            this.namedjdbcTemplate.update(this.buildSaveFreezeInformationOfContractDetailOwnersQuery(), 
            		namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}

	private String buildSaveFreezeInformationOfContractDetailOwnersQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO CONTRACTDETAILOWNERS(IdRequisitionOwners, ContractDetailJsonValue) ");
		query.append("VALUES(:IdRequisitionOwners, :ContractDetailJsonValue) ");
		return query.toString();
	}

	private MapSqlParameterSource saveFreezeInformationOfContractDetailOwnersNamedParameters(
			final Integer idRequisitionOwnersParameter, final String contractDetailOwnersJson) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwnersParameter);
		namedParameters.addValue(TableConstants.CONTRACT_DETAIL_JSON_VALUE, contractDetailOwnersJson);
		return namedParameters;
	}

	@Override
	public String findOwnersContractDetail(final Integer idRequisitionOwnersParameter) 
			throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwnersParameter);
            return this.namedjdbcTemplate.queryForObject(this.buildFindOwnersContractDetailQuery(),
            		namedParameters, String.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}

	private String buildFindOwnersContractDetailQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT ContractDetailJsonValue FROM CONTRACTDETAILOWNERS ");
		query.append("WHERE IdRequisitionOwners =:IdRequisitionOwners ");
		return query.toString();
	}
	
	 @Override
	 public List<TrayRequisition> findAllRequisitionsForTray(final TrayFilter trayFilter) throws DatabaseException {
	        try {
	            final MapSqlParameterSource namedParameters = this.createfindTrayRequisitionsNamedParameters(trayFilter);
	            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
	            return this.namedjdbcTemplate.query( this.buildFindRequisitionsForTrayQuery(), namedParameters,
	                    new BeanPropertyRowMapper<>(TrayRequisition.class));
	        } catch (DataAccessException dataAccessException) {
	            throw new DatabaseException(dataAccessException);
	        }
	    }
}
