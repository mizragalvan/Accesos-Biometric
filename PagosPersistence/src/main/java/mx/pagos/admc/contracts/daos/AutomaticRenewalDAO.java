package mx.pagos.admc.contracts.daos;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.AutomaticRenewable;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class AutomaticRenewalDAO implements AutomaticRenewable {
    private static final String WHERE_RENOVATION_REQUISITION_PARENT_IS_NOT_NULL_AND_STATUS_REQUISITION_CLOSE =
            "WHERE RenovationRequisitionParent IS NOT NULL AND Status = 'REQUISITION_CLOSE' ";
    private static final String RENEWAL_PERIODS = "RenewalPeriods";
    private static final String SINGLE_QUOTE = "'";

    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Autowired
    private DatabaseUtils databaseUtils;

    public void setDatabaseUtils(final DatabaseUtils databaseUtilsParameter) {
        this.databaseUtils = databaseUtilsParameter;
    }
    
    @Override
    public void automaticRenewContracts() throws DatabaseException {
        try {
            this.namedjdbcTemplate.update(this.buildRenewContractsQuery(), new MapSqlParameterSource());
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Integer> findRenewableRequisitions() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.queryForList(this.buildFindRenewableRequisitionsQuery(),
                    new MapSqlParameterSource(), Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void updateToRenewedStatus(final List<Integer> idRequisitionsList) throws DatabaseException {
        try {
            this.namedjdbcTemplate.update(this.buildUpdateToRenewedStatusQuery(),
                    Collections.singletonMap(TableConstants.ID_REQUISITION, idRequisitionsList));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void automaticUpdateToRenewedStatus() throws DatabaseException {
        try {
            this.namedjdbcTemplate.update(this.buildAutomaticUpdateToRenewedStatusQuery(), new MapSqlParameterSource());
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<Requisition> findRenewedRequisitions() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindRenewedRequisitionsQuery(),
                    new BeanPropertyRowMapper<Requisition>(Requisition.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteRenovatedByIdRequisitionParentsList(final List<Integer> idRequisitionParentsList)
            throws DatabaseException {
        try {
            this.namedjdbcTemplate.update(this.buildDeleteRenovatedQuery(),
                    Collections.singletonMap(TableConstants.RENOVATION_REQUISITION_PARENT, idRequisitionParentsList));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    public Integer countRenovatedByIdRequisitionParentsList(final List<Integer> idRequisitionParentsList)
            throws DatabaseException {
        try {
            return this.namedjdbcTemplate.queryForObject(this.buildCountRenovatedByIdRequisitionParentsListQuery(),
                    Collections.singletonMap(TableConstants.RENOVATION_REQUISITION_PARENT, idRequisitionParentsList),
                    Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    public Integer countRenewedRequisitions() throws DatabaseException {
        return this.namedjdbcTemplate.queryForObject(this.buildCountRenewedRequisitionsQuery(),
                new MapSqlParameterSource(), Integer.class);
    }

    private String buildRenewContractsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITION (");
        this.buildCommonFieldsQuery(query);
        query.append("RenovationRequisitionParent,Status,ValidityStartDate,ValidityEndDate");
        query.append(") ");
        query.append("SELECT ");
        this.buildCommonFieldsQuery(query);
        query.append("IdRequisition,'REQUISITION_CLOSE',");
        query.append(this.databaseUtils.buildDateAddDays("1", TableConstants.VALIDITY_END_DATE)).append(",");
        query.append(this.databaseUtils.buildDateAddYears(RENEWAL_PERIODS,
                TableConstants.VALIDITY_END_DATE)).append(" ");
        this.buildSelectAutomaticRenewalCandidatesBodyQuery(query);
        return query.toString();
    }
    
    private void buildCommonFieldsQuery(final StringBuilder query) {
        query.append("IdFlow,IdApplicant,IdSupplier,IdLawyer,IdEvaluator,IdAreaTender,");
        query.append("IdDocumentType,TemplateIdDocument,AuthorizationDocumentName,");
        query.append("AuthorizationDocumentIdDoc,ServiceDescription,TechnicalDetails,");
        query.append("AttatchmentDeliverables,AttchmtServiceLevelsMeasuring,AttatchmentPenalty,");
        query.append("AttatchmentScalingMatrix,AttatchmentCompensation,AttchmtBusinessMonitoringPlan,");
        query.append("BusinessReasonMonitoringPlan,SpecialProvisionNegotiated,");
        query.append("AttchmtImssInfoDeliveryReqrmts,AttatchmentInformationSecurity,");
        query.append("AttatchmentOthers,AttatchmentOthersName,Validity,AutomaticRenewal,RenewalPeriods,");
        query.append("ValidityClause,IsRequiredHumanResources,IsImssCeduleGiven,");
        query.append("ImssCeduleNotGivenIdDocument,IsSupplierSingleRegistered,IsSupplierPenalties,");
        query.append("SupplierPenaltiesText,IsAdvanceBailNeeded,AdvanceBailAmount,AdvanceBailNotNeededIdDocument,");
        query.append("IsFulfillmentBailNeeded,FulfillmentBailAmount,FulfillmentBailNeedNoIdDoc,");
        query.append("IsFidelityBailNeeded,FidelityBailNeedNoIdDoc,IsContingencyBailNeeded,");
        query.append("ContingencyBailNeedNoIdDoc,IsCivilRespInsuranceBailNeeded,");
        query.append("CivilRespInsurBailNeedNoIdDoc,IsContractTermAuthorized,");
        query.append("IsSpecialProvisionNegotiated,IsWarrantyDeposit,IsStaffUnderGroupDirecction,");
        query.append("ContractObjectClause,ConsiderationClause,Clabe,ContractDurationDate,ConsiderationInitialReport,");
        query.append("ConsiderationMonthlyReport,ConsiderExtraordinaryReport,InitialPaymentPercentage,");
        query.append("InitialPaymentPeriod,MonthlyPaymentPercentage,MonthlyPaymentPeriod,");
        query.append("ExtraordinaryPaymentPercentage,ExtraordinaryPaymentPeriod,DomainName,BrandName,");
        query.append("SubcontractorLegalRepName,ExtensionsNumber,ExtensionYears,ExtensionPeriod,");
        query.append("ExtensionValidity,ExtensionForcedYears,ExtensionVoluntaryYears,RentInitialQuantity,");
        query.append("ExtensionFirstYearRent,MaintenanceInitialQuantity,NatPersonTenantDeclarations,");
        query.append("MoralOrNatPersonDeclarations,ContractDate,Surface,PropertyDeedTitleNumber,");
        query.append("PropertyDeedTitleDate,PropertyDeedTitleNotary,PropertyDeedTitleNotaryNumber,");
        query.append("PropertyDeedTitleNotaryState,PropDeedTitleCommercialFolio,");
        query.append("PropDeedTitleRegistrationDate,SubcontactedPersonality,DirectSupplierPersonality,");
        query.append("ContractType,EventName,EventDatetime,ClausulesToModify,ContractValidity,MaintenanceClause,");
        query.append("SignDate,ProfessionalLicense,");
        query.append("ExpeditionDateProfLicense,Specialty,SpecialtyCeduleNumber,");
        query.append("ExpeditionDateSpecialtyLicense,SanitaryLicense,SocialReasonOfTheContract,SupplierSignSendDate,");
        query.append("WitnessesSignSendDate,SupplierSignReturnDate,WitnessesSignReturnDate,");
        query.append("IsOrigSignCntrDelvrdSupplier,IsCpySignCntrDelvrdSupplier,");
        query.append("SignedContractSendDateSupplier,IsOrigSignCntrDelvrdWitnesses,");
        query.append("IsCpySignCntrDelvrdWitnesses,SigndContractSendDateWitnesses,");
        query.append("IsOrigSignCntrDelvrdRegistry,IsCpySignCntrDelvrdRegistry,");
        query.append("SignedContractSendDateRegistry,SupplierApprovalIdDocument,IsExpiredAttended,ProemName,");
        query.append("ContractObject,ContractExtendClause,");
        query.append("ContractEndClause,ForcedYears,VoluntaryYears,RentClause,Property,ContingencyBailAmount,Proyect,");
        query.append("ProyectAddress,Developer,EndDateClause,ExtensionAmount,EndDateDeclaration,");
        query.append("OcupationDate,");
    }
    
    private String buildFindRenewableRequisitionsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdRequisition ");
        this.buildSelectAutomaticRenewalCandidatesBodyQuery(query);
        return query.toString();
    }

    private void buildSelectAutomaticRenewalCandidatesBodyQuery(final StringBuilder query) {
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
        query.append("FROM REQUISITION ");
        query.append("WHERE Validity = 'DEFINED' AND AutomaticRenewal = 1 ");
        query.append("AND ValidityEndDate < ").append(formatedTodayDate).append(" AND Status = 'REQUISITION_CLOSE'");
    }
    
    private String buildUpdateToRenewedStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE REQUISITION SET Status = 'RENEWED' WHERE IdRequisition IN (:IdRequisition)");
        return query.toString();
    }
    
    private String buildDeleteRenovatedQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITION WHERE RenovationRequisitionParent IN (:RenovationRequisitionParent)");
        return query.toString();
    }
    
    private String buildCountRenovatedByIdRequisitionParentsListQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(1) FROM REQUISITION ");
        query.append("WHERE RenovationRequisitionParent IN (:RenovationRequisitionParent)");
        return query.toString();
    }
    
    private String buildFindRenewedRequisitionsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT REQUISITION.IdRequisition, REQUISITION.ApplicationDate, ");
        query.append("SUPPLIER.CommercialName AS SupplierName, DOCUMENTTYPE.Name AS DocumentTypeName ");
        query.append("FROM ( ");
        query.append("SELECT REQUISITION.IdRequisition ");
        query.append("FROM REQUISITION LEFT JOIN REQUISITIONDIGITALIZATION ON ");
        query.append("REQUISITION.IdRequisition = REQUISITIONDIGITALIZATION.IdRequisition ");
        query.append(WHERE_RENOVATION_REQUISITION_PARENT_IS_NOT_NULL_AND_STATUS_REQUISITION_CLOSE);
        query.append("GROUP BY REQUISITION.IdRequisition ");
        query.append("HAVING COUNT(REQUISITIONDIGITALIZATION.IdRequisition) = 0 ");
        query.append(") RenewedRequisitions INNER JOIN ");
        query.append("REQUISITION ON RenewedRequisitions.IdRequisition = REQUISITION.IdRequisition ");
        query.append("INNER JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
        query.append("INNER JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
        return query.toString();
    }

    private String buildAutomaticUpdateToRenewedStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE REQUISITION SET Status = 'RENEWED' WHERE IdRequisition IN (");
        query.append("SELECT REQUISITION.RenovationRequisitionParent ");
        query.append("FROM REQUISITION LEFT JOIN REQUISITIONDIGITALIZATION ");
        query.append("ON REQUISITION.IdRequisition = REQUISITIONDIGITALIZATION.IdRequisition ");
        query.append(WHERE_RENOVATION_REQUISITION_PARENT_IS_NOT_NULL_AND_STATUS_REQUISITION_CLOSE);
        query.append("GROUP BY REQUISITION.RenovationRequisitionParent ");
        query.append("HAVING COUNT(REQUISITIONDIGITALIZATION.IdRequisition)= 0 ");
        query.append(")");
        return query.toString();
    }

    private String buildCountRenewedRequisitionsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(1) FROM ( ");
        query.append("SELECT 1 AS Requisition ");
        query.append("FROM REQUISITION LEFT JOIN REQUISITIONDIGITALIZATION ON  ");
        query.append("REQUISITION.IdRequisition = REQUISITIONDIGITALIZATION.IdRequisition  ");
        query.append(WHERE_RENOVATION_REQUISITION_PARENT_IS_NOT_NULL_AND_STATUS_REQUISITION_CLOSE);
        query.append("GROUP BY REQUISITION.IdRequisition  ");
        query.append("HAVING COUNT(REQUISITIONDIGITALIZATION.IdRequisition) = 0  ");
        query.append(") RenewedRequisitions ");
        return query.toString();
    }
}
