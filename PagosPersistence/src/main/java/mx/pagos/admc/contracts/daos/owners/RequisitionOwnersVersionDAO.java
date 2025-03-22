package mx.pagos.admc.contracts.daos.owners;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.owners.RequisitionOwnersVersionable;
import mx.pagos.admc.contracts.structures.owners.CategoryCheckDocumentation;
import mx.pagos.admc.contracts.structures.owners.RequisitionOwners;
import mx.pagos.admc.enums.SectionTypeEnum;
import mx.pagos.document.versioning.structures.DocumentBySection;
import mx.pagos.general.exceptions.DatabaseException;

/**
 * 
 * @author Mizraim
 * 
 */

@Repository
public class RequisitionOwnersVersionDAO implements RequisitionOwnersVersionable {

    private static final String WHERE_ID_REQUISITION_EQUALS = "WHERE IdRequisitionOwners =:IdRequisitionOwners ";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;


    @Override
    public Integer saveRequisitionOwnersVersion(final Integer idRequisition) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindRequisitionByIdNamedParameters(idRequisition);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildsaveRequisitionOwnersVersionQuery(), namedParameters, keyHolder, 
                    new String[]{"IdRequisitionOwnersVersion"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequisitionRequisitionOwnersVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionOwnersNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionOwnersVersionVersionNumberQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildSaveRequisitionOwnersVersionVersionNumberQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERS_V_V_NUMBER (IdRequisitionOwners, ");
        query.append("IdRequisitionOwnersVersion,VersionNumber) VALUES (:IdRequisitionOwners, ");
        query.append(":IdRequisitionOwnersVersion, ( SELECT COALESCE(MAX(VersionNumber) + 1, 1) FROM ");
        query.append("REQUISITIONOWNERS_V_V_NUMBER WHERE IdRequisitionOwners = :IdRequisitionOwners )) ");
        return query.toString();
    }

    private MapSqlParameterSource createVersionRequisitionOwnersNamedParameters(
            final Integer idRequisition, final Integer idRequisitionVersion) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisition);
        namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS_VERSION, idRequisitionVersion);
        return namedParameters;
    }

    private MapSqlParameterSource createFindRequisitionByIdNamedParameters(final Integer idRequisition) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisition);
        return namedParameters;
    }

    private String buildsaveRequisitionOwnersVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERS_V ( ");
        this.buildFieldsRequisitionOwners(query);
        query.append(") SELECT ");
        this.buildFieldsRequisitionOwners(query);
        query.append("FROM REQUISITIONOWNERS ");
        query.append(WHERE_ID_REQUISITION_EQUALS);
        return query.toString();
    }

    private void buildFieldsRequisitionOwners(final StringBuilder queryParameter) {
        queryParameter.append("IdLawyer,IdCurrency,IdBusinessman,IdCategory,CustomerCompanyName,CustomerRFC,");
        queryParameter.append("IdDictaminationTemplate,IdDeciderLawyer,Jurisdiction,IdOrganizationEntity, ");
        queryParameter.append("IsPublicNotaryDelivered,IsVerifiedReceivedContract,CreditResolutionNumber, ");
        queryParameter.append("CreditTypeProduct,Amount,DocumentType,IdPublicBroker,IdPublicNotary,DeedNumber, ");
        queryParameter.append("StartDate,EndDate,IsPrivateFormalization,IsPublicNotaryRatified, ");
        queryParameter.append("IsPublicRedordEnmrolled,IdLawyerVobo,ApplicationDate,IdUserDictamenVoBo, ");
        queryParameter.append("IdUserProjectReviewVoBo,IdUserSignVoBo,IdFlow,IsStandarized,DateFirstProject ");
    }

    @Override
    public void saveRequisitionOwnersDigitalizationsVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionOwnersNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionOwnersDigitalizationsQuery(), 
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildSaveRequisitionOwnersDigitalizationsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERDIGITAL_V (idRequisitionOwnersVersion, IdDocument, ");
        query.append("SectionType,DocumentName) SELECT :IdRequisitionOwnersVersion, IdDocument, SectionType, ");
        query.append("DocumentName FROM REQUISITIONOWNERDIGITAL ");
        query.append(WHERE_ID_REQUISITION_EQUALS);
        return query.toString();
    }

    @Override
    public void saveRequisitionOwnersAttachmentVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionOwnersNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionOwnersAttachmentVersionQuery(), 
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildSaveRequisitionOwnersAttachmentVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERSATTACHMENT_V (idRequisitionOwnersVersion, IdDocument, ");
        query.append("SectionType, DocumentName) SELECT :IdRequisitionOwnersVersion, IdDocument, SectionType, ");
        query.append("DocumentName FROM REQUISITIONOWNERSATTACHMENT ");
        query.append(WHERE_ID_REQUISITION_EQUALS);
        return query.toString();
    }

    @Override
    public void saveRequisitionOwnersCheckDocumentationVersion(final Integer idRequisitionVersion,
            final Integer idCategory, final Integer idCheckDocumentation) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("idRequisitionOwnersVersion", idRequisitionVersion);
            source.addValue(TableConstants.ID_CATEGORY, idCategory);
            source.addValue(TableConstants.ID_CHECK_DOCUMENTATION, idCheckDocumentation);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionOwnersCheckDocumentationVersionQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildSaveRequisitionOwnersCheckDocumentationVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERSCHECKDOC_V (idRequisitionOwnersVersion, ");
        query.append("IdCategory, IdCheckDocumentation) ");
        query.append("VALUES(:idRequisitionOwnersVersion, :IdCategory, :IdCheckDocumentation) ");
        return query.toString();
    }

    @Override
    public void saveRequisitionOwnersGuaranteeCheckDocumentVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionOwnersNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionOwnersGuaranteeCheckDocumentVersionQuery(), 
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildSaveRequisitionOwnersGuaranteeCheckDocumentVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQOWNERSGUARANTEECHECKDOC_V (idRequisitionOwnersVersion, ");
        query.append("IdCheckDocument) SELECT :IdRequisitionOwnersVersion, IdCheckDocument ");
        query.append("FROM REQOWNERSGUARANTEECHECKDOC ");
        query.append(WHERE_ID_REQUISITION_EQUALS);
        return query.toString();
    }

    @Override
    public void saveRequisitionOwnersStatusVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionOwnersNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionOwnersStatusVersionQuery(), 
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildSaveRequisitionOwnersStatusVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERSSTATUS_V (idRequisitionOwnersVersion, Status, ");
        query.append("HoldForBranch) SELECT :IdRequisitionOwnersVersion, Status, HoldForBranch ");
        query.append("FROM REQUISITIONOWNERSSTATUS ");
        query.append(WHERE_ID_REQUISITION_EQUALS);
        return query.toString();
    }

    @Override
    public void saveRequisitionOwnersStatusTurnVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionOwnersNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionOwnersStatusTurnVersionQuery(), 
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildSaveRequisitionOwnersStatusTurnVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERSSTATUSTURN_V (idRequisitionOwnersVersion, Status, ");
        query.append("Turn, TurnDate, AttentionDays) SELECT :IdRequisitionOwnersVersion, Status, Turn, ");
        query.append("TurnDate, AttentionDays FROM REQUISITIONOWNERSSTATUSTURN ");
        query.append(WHERE_ID_REQUISITION_EQUALS);
        return query.toString();
    }

    @Override
    public RequisitionOwners findRequisitionOwnerVersionByIdVersion(final Integer idRequisitionOwnerVersion, 
            final Integer versionNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS_VERSION, idRequisitionOwnerVersion);
            source.addValue(TableConstants.VERSION_NUMBER, versionNumber);
            return this.namedjdbcTemplate.queryForObject(this.findRequisitionOwnerVersionByIdVersionQuery(), source,
                    new BeanPropertyRowMapper<RequisitionOwners>(RequisitionOwners.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findRequisitionOwnerVersionByIdVersionQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT ROVVN.IdRequisitionOwners, IdBusinessman, CustomerCompanyName, CustomerRfc, ");
        builder.append("ROV.IdRequisitionOwnersVersion, ");
        builder.append("CURRENCY.Name AS CurrencyName, CATEGORY.Name AS CategoryName, ROV.IdCategory, ");
        builder.append("ORGANIZATIONENTITY.Name AS OrganizationEntityName, ");
        builder.append("CONCAT(USERS.Name,' ',USERS.FirstLastName,' ',USERS.SecondLastName) AS LawyerAssigmentName ");
        builder.append("FROM REQUISITIONOWNERS_V AS ROV ");
        builder.append("INNER JOIN REQUISITIONOWNERS_VERSION_VERSION_NUMBER AS ROVVN ");
        builder.append("ON ROVVN.IdRequisitionOwnersVersion = ROV.IdRequisitionOwnersVersion ");
        builder.append("INNER JOIN CURRENCY ON ROV.IdCurrency = CURRENCY.IdCurrency ");
        builder.append("INNER JOIN ORGANIZATIONENTITY ");
        builder.append("ON ROV.IdOrganizationEntity = ORGANIZATIONENTITY.IdOrganizationEntity ");
        builder.append("INNER JOIN CATEGORY ON ROV.IdCategory = CATEGORY.IdCategory ");
        builder.append("LEFT JOIN USERS ON ROV.IdLawyer = USERS.IdUser ");
        builder.append("WHERE ROVVN.IdRequisitionOwnersVersion = :IdRequisitionOwnersVersion ");
        builder.append("AND ROVVN.VersionNumber = :VersionNumber ");
        return builder.toString();
    }

    @Override
    public List<DocumentBySection> findAttachmentByVersion(final Integer idRequisitionOwnerVersion, 
            final SectionTypeEnum sectionType) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS_VERSION, idRequisitionOwnerVersion);
            source.addValue(TableConstants.SECTION_TYPE, sectionType.toString());
            return this.namedjdbcTemplate.query(this.findAttachmentByVersionQuery(), source, 
                    new BeanPropertyRowMapper<DocumentBySection>(DocumentBySection.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findAttachmentByVersionQuery() { 
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT idRequisitionOwnersVersion, IdDocument, SectionType, DocumentName ");
        builder.append("FROM REQUISITIONOWNERSATTACHMENT_V "); 
        builder.append("WHERE idRequisitionOwnersVersion = :IdRequisitionOwnersVersion ");
        builder.append("AND SectionType = :SectionType ");
        return builder.toString();
    }

    @Override
    public List<Integer> findCheckDocumentationByVersion(final Integer idRequisitionOwnerVersion) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS_VERSION, idRequisitionOwnerVersion);
            return this.namedjdbcTemplate.queryForList(this.findCheckDocumentationByVersionQuery(), 
                    source, Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findCheckDocumentationByVersionQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdCategoryCheckDocumentation ");
        builder.append("FROM REQUISITIONOWNERSCHECKDOC_V ");
        builder.append("WHERE idRequisitionOwnersVersion = :IdRequisitionOwnersVersion ");
        return builder.toString();
    }

    @Override
    public List<CategoryCheckDocumentation> findCategoryCheckDocumentationForVersion(
            final Integer idRequisitionOwners) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwners);
            return this.namedjdbcTemplate.query(this.findCategoryCheckDocumentationForVersionQuery(), source, 
                    new BeanPropertyRowMapper<CategoryCheckDocumentation>(CategoryCheckDocumentation.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findCategoryCheckDocumentationForVersionQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT DISTINCT CCD.IdCategory, CCD.IdCheckDocumentation ");
        builder.append("FROM CATEGOYCHECKDOCUMENTATION AS CCD ");
        builder.append("INNER JOIN CATEGORY ON CCD.IdCategory = CATEGORY.IdCategory ");
        builder.append("INNER JOIN CHECKDOCUMENTATION ");
        builder.append("ON CCD.IdCheckDocumentation = CHECKDOCUMENTATION.IdCheckDocumentation ");
        builder.append("WHERE IdCategoryCheckDocumentation IN ");
        builder.append("(SELECT IdCategoryCheckDocumentation FROM REQUISITIONOWNERSCHECKDOC ");
        builder.append("WHERE IdRequisitionOwners = :IdRequisitionOwners) ");
        return builder.toString();
    }
}
