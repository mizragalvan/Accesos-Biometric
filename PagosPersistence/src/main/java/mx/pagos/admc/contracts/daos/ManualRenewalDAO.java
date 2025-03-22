package mx.pagos.admc.contracts.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.ManualRenewable;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;

public class ManualRenewalDAO implements ManualRenewable {
    private static final String RENEWAL_PERIODS = "RenewalPeriods";
    
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Autowired
    private DatabaseUtils databaseUtils;

    public void setDatabaseUtils(final DatabaseUtils databaseUtilsParameter) {
        this.databaseUtils = databaseUtilsParameter;
    }
    
    @Override
    public Integer renewContract(final Integer idRequisition, final FlowPurchasingEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createRenewContractNamedParameters(idRequisition, status);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildRenewContractQuery(), namedParameters, keyHolder, 
                    new String[]{"IdRequisition"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public FlowPurchasingEnum getRenewalStatus(final Integer idFlow) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdFlowNamedParameters(idFlow);
            return this.namedjdbcTemplate.queryForObject(this.buildGetRenewalStatusQuery(),
                    namedParameters, FlowPurchasingEnum.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource createRenewContractNamedParameters(final Integer idRequisition,
            final FlowPurchasingEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }

    private String buildRenewContractQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITION (");
        this.buildCommonFieldsQuery(query);
        query.append("RenovationRequisitionParent, Status, ValidityStartDate, ValidityEndDate) ");
        query.append("SELECT ");
        this.buildCommonFieldsQuery(query);
        query.append("IdRequisition, :Status, ");
        query.append(this.databaseUtils.buildDateAddDays("1", TableConstants.VALIDITY_END_DATE)).append(", ");
        query.append(this.databaseUtils.buildDateAddYears(RENEWAL_PERIODS,
                TableConstants.VALIDITY_END_DATE)).append(" ");
        query.append("FROM REQUISITION WHERE IdRequisition = :IdRequisition");
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
        query.append("ContractObject, ContractExtendClause,");
        query.append("ContractEndClause,ForcedYears,VoluntaryYears,RentClause,Property,ContingencyBailAmount,Proyect,");
        query.append("ProyectAddress,Developer,EndDateClause,ExtensionAmount,EndDateDeclaration,");
        query.append("OcupationDate,");
    }
    
    private String buildGetRenewalStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT Status FROM RENEWALFLOWSTATUS WHERE IdFlow = :IdFLow");
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdFlowNamedParameters(final Integer idFlow) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
        return namedParameters;
    }
}
