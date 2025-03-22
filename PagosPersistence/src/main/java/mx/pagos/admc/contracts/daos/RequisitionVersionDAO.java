package mx.pagos.admc.contracts.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.RequisitionVersionable;
import mx.pagos.admc.contracts.structures.ApprovalArea;
import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionVersion;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.document.version.daos.constants.TableVersionConstants;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.structures.User;

/**
 * 
 * @author Mizraim
 *
 */
@Repository
public class RequisitionVersionDAO implements RequisitionVersionable {
    private static final String WHERE_ID_VERSION_EQUALS_ID_VERSION =
            "WHERE IdRequisitionVersion = :IdRequisitionVersion";
    private static final String WHERE_ID_EQUALS_ID = "WHERE IdRequisition = :IdRequisition ";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    private static final Logger LOG = Logger.getLogger(RequisitionVersionDAO.class);

    @Override
    public Integer saveRequisitionVersion(final Integer idRequisition) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindRequisitionByIdNamedParameters(idRequisition);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            String query = this.buildSaveRequisitionVersionQuery();
            LOG.info("QUERY saveRequisitionVersion : \n"+query);
            this.namedjdbcTemplate.update(query, namedParameters, keyHolder, 
                    new String[]{"IdRequisitionVersion"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequisitionRequisitionVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionRequisitionVersionQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequisitionApprovalAreasVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionApprovalAreasVersionQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequisitionAttatchmentsVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionAttatchmentsVersionQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequisitionAuthorizationDgasVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createVersionRequisitionNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionAuthorizationDgasVersionQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequisitionFinantialEntitiesVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createVersionRequisitionNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionFinantialEntitiesVersionQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequisitionFinantialEntityWitnessesVersion(
            final Integer idRequisition, final Integer idRequisitionVersion)
                    throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createVersionRequisitionNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionFinantialEntityWitnessesVersionQuery(),
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequisitionLegalRepresentativesVersion(
            final Integer idRequisition, final Integer idRequisitionVersion)
                    throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createVersionRequisitionNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionLegalRepresentativesVersionQuery(),
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequisitionUsersVoboVersion(final Integer idRequisition,
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createVersionRequisitionNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionUsersVoboVersionQuery(),
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveScalingVersion(final Integer idRequisition, final Integer idRequisitionVersion) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createVersionRequisitionNamedParameters(idRequisition, idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildsaveScalingVersionQuery(),
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<VersionDTO> findRequisitionVersions(
            final Integer idRequisition) throws DatabaseException {
//        try {
//            final MapSqlParameterSource namedParameters = this.createFindRequisitionByIdNamedParameters(idRequisition);
//            return this.namedjdbcTemplate.query(this.buildFindRequisitionVersionsQuery(), namedParameters,
//                    new BeanPropertyRowMapper<RequisitionVersion>(RequisitionVersion.class));
//        } 
    	  try {
         	 final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
              namedParameters.addValue(TableVersionConstants.ID_REQUISITION, idRequisition);
              return this.namedjdbcTemplate.query(this.buildFindRequisitionVersionsQuery(), namedParameters,
                      new BeanPropertyRowMapper<VersionDTO>(VersionDTO.class));
         }catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Requisition findById(final Integer idRequisitionVersion)
            throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<Requisition>(Requisition.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Integer> findApprovalAreas(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            return this.namedjdbcTemplate.queryForList(this.buiFldindApprovalAreasQuery(), namedParameters,
                    Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<String> findApprovalAreasActive(Integer idRequisitionVersion) throws DatabaseException {
        final String sql = "Select name From "
                + "RequisitionApprovalArea_V raa inner join Area "
                + "on raa.idArea = Area.idArea "
                + "where raa.idRequisitionVersion = :IdRequisitionVersion ";
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_REQUISITION_VERSION, idRequisitionVersion);
            List<String> list = namedjdbcTemplate.queryForList(sql, namedParameters, String.class);
            return list;
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }	


    @Override
    public List<Version> findAttatchments(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            return this.namedjdbcTemplate.query(this.buildFindAttatchmentsQuery(), namedParameters,
                    new BeanPropertyRowMapper<Version>(Version.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Dga> findAuthorizationDgas(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            return this.namedjdbcTemplate.query(this.buildFindAuthorizationDgasQuery(), namedParameters,
                    new BeanPropertyRowMapper<Dga>(Dga.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<FinancialEntity> findFinantialEntities(
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            return this.namedjdbcTemplate.query(this.buildFindFinantialEntitiesQuery(), namedParameters,
                    new BeanPropertyRowMapper<FinancialEntity>(FinancialEntity.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<String> findFinantialEntitiesWitnesses(
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            return this.namedjdbcTemplate.queryForList(this.buildFindFinantialEntitiesWitnessesQuery(),
                    namedParameters, String.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<LegalRepresentative> findLegalRepresentatives(
            final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            return this.namedjdbcTemplate.query(this.buildFindLegalRepresentativesQuery(), namedParameters,
                    new BeanPropertyRowMapper<LegalRepresentative>(LegalRepresentative.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<User> findUsersVobo(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            return this.namedjdbcTemplate.query(this.buildFindUsersVoboQuery(), namedParameters,
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<ApprovalArea> findApprovalAreasVoBo(final Integer idRequisitionVersion)
            throws DatabaseException {
        try {
            final MapSqlParameterSource nameDParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            return this.namedjdbcTemplate.query(this.buildFindApprovalAreasVoBoQuery(), nameDParameters,
                    new BeanPropertyRowMapper<ApprovalArea>(ApprovalArea.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteById(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteRequisitionRequisitionVersions(final Integer idRequisition) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindRequisitionByIdNamedParameters(idRequisition);
            this.namedjdbcTemplate.update(this.buildDeleteRequisitionRequisitionVersionsQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteApprovalAreas(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildDeleteApprovalAreasQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteAttatchments(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildDeleteAttatchmentsQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteAuthorizationDgas(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildDeleteAuthorizationDgasQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteFinancialEntities(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildDeleteFinancialEntitiesQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteFinantialEntityWitnesses(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildDeleteFinantialEntityWitnessesQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteLegalRepresentatives(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildDeleteLegalRepresentativesQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteUsersVoBo(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionVersion);
            this.namedjdbcTemplate.update(this.buildDeleteUsersVoBoQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildFindByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private MapSqlParameterSource createFindByIdNamedParameters(
            final Integer idRequisitionVersion) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION_VERSION, idRequisitionVersion);
        return namedParameters;
    }

    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT IdRequisitionVersion AS IdRequisition, ");
        query.append("SUPPLIER.CompanyName AS SupplierName, SUPPLIER.Rfc AS RFC, ");
        query.append("SUPPLIER.NonFiscalAddress AS SupplierAddress, SUPPLIER.FiscalAddress, ");
        query.append("SUPPLIER.SupplierCompanyPurpose AS SupplierCompanyPurpose, ");
        query.append("SUPPLIER.CompanyType AS SupplierCompanyType, DOCUMENTTYPE.Name AS DocumentTypeName, ");
        query.append("SCREEN.Name As FlowStatus, DOCUMENTTYPE.DocumentTypeEnum AS EnumDocumentType, ");
        query.append("AREA.Name AS AreaTender, ");
        this.buildAllNonPrimaryFields(query);
        this.buildRequisitionJoins(query);
    }
    
    private void buildRequisitionJoins(final StringBuilder query) {
        query.append("FROM REQUISITION_V AS REQUISITION LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
        query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
        query.append("LEFT JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
        query.append("LEFT JOIN AREA ON REQUISITION.IdAreaTender = AREA.IdArea ");
    }

    private void buildAllNonPrimaryFields(final StringBuilder query) {
        query.append("IdFlow, IdApplicant, REQUISITION.IdSupplier, IdLawyer, IdEvaluator, IdAreaTender, ApplicationDate, ");
        query.append("REQUISITION.IdDocumentType, TemplateIdDocument, TemplateCurrentVersion, AuthorizationDocumentName, AuthorizationDocumentIdDoc, ");
        query.append("ServiceDescription, TechnicalDetails, ");
        this.buildAttatchmentsFields(query);
        query.append("Validity, AutomaticRenewal, RenewalPeriods, ValidityStartDate, ValidityEndDate, ValidityClause,");
        this.buildInstrumentCharacteristicsFields(query);
        this.buildClausulesSectionFields(query);
        this.buildDeclarationsSectionFields(query);
        this.buildContractDraftFields(query);
        query.append("ProfessionalLicense, ExpeditionDateProfLicense, Specialty, SpecialtyCeduleNumber, ");
        query.append("ExpeditionDateSpecialtyLicense, SanitaryLicense, SocialReasonOfTheContract, ");
        query.append("SupplierSignSendDate, WitnessesSignSendDate, SupplierSignReturnDate, WitnessesSignReturnDate, ");
        query.append("IsOrigSignCntrDelvrdSupplier, IsCpySignCntrDelvrdSupplier, SupplierAtention, SupplierPhone, SupplierAccountNumber, ");
        query.append("SignedContractSendDateSupplier, IsOrigSignCntrDelvrdWitnesses, UpdateRequisitionBy, ");
        query.append("IsCpySignCntrDelvrdWitnesses, SigndContractSendDateWitnesses, UpdateRequisitionDate, ");
        query.append("IsOrigSignCntrDelvrdRegistry, IsCpySignCntrDelvrdRegistry, ");
        query.append("SignedContractSendDateRegistry, SupplierApprovalIdDocument, REQUISITION.Status ");
        query.append(buildNotIncludedRequisitionFields());
    }

    private void buildAllNonPrimaryFieldsFromRequisition(final StringBuilder query) {
        query.append("IdFlow, IdApplicant, IdSupplier, IdLawyer, IdEvaluator, IdAreaTender, ApplicationDate, ");
        query.append("IdDocumentType, TemplateIdDocument, ");
        this.buildSubQueryTempleteCurrentVersion(query);
        query.append("AuthorizationDocumentName, AuthorizationDocumentIdDoc, ");
        query.append("ServiceDescription, TechnicalDetails, ");
        this.buildAttatchmentsFields(query);
        query.append("Validity, AutomaticRenewal, RenewalPeriods, ValidityStartDate, ValidityEndDate, ValidityClause,");
        this.buildInstrumentCharacteristicsFields(query);
        this.buildClausulesSectionFields(query);
        this.buildDeclarationsSectionFields(query);
        this.buildContractDraftFields(query);
        query.append("ProfessionalLicense, ExpeditionDateProfLicense, Specialty, SpecialtyCeduleNumber, ");
        query.append("ExpeditionDateSpecialtyLicense, SanitaryLicense, SocialReasonOfTheContract, ");
        query.append("SupplierSignSendDate, WitnessesSignSendDate, SupplierSignReturnDate, WitnessesSignReturnDate, ");
        query.append("IsOrigSignCntrDelvrdSupplier, IsCpySignCntrDelvrdSupplier, SupplierAtention, SupplierPhone, SupplierAccountNumber, ");
        query.append("SignedContractSendDateSupplier, IsOrigSignCntrDelvrdWitnesses, UpdateRequisitionBy, ");
        query.append("IsCpySignCntrDelvrdWitnesses, SigndContractSendDateWitnesses, UpdateRequisitionDate, ");
        query.append("IsOrigSignCntrDelvrdRegistry, IsCpySignCntrDelvrdRegistry, ");
        query.append("SignedContractSendDateRegistry, SupplierApprovalIdDocument, Status ");
        query.append(buildNotIncludedRequisitionFields());
    }

    private String buildNotIncludedRequisitionFields() {
        String aFields[] = {"AaExtensionForcedYears", "AaExtensionVoluntaryYears", "AccountNumberFinancial", "ActiveActor", "AmountOfInsuranceForDamageOrLoss", 
                "AnticipatedEndDate", "AppraisersPfName", "BankingInstitution", "BillingCycle", "BookNumber", 
                "BusinessDaysAcceptRejectPurchaseOrder", "BusinessDaysToModifyCancelOrders", "CalendarDayOfDeliveryDate", "CalendarDaysToWithdrawContract", "CancellationDate", 
                "CityJurisdiction", "CivilLawState", "CivilResponsabilityBailAmount", "ClauseToModifyContent", "CompensationMonthsRent", 
                "CondosNumber", "ConsiderationAmount", "ConstructionStagesEndDate", "ConstructionStagesStartDate", "ContingencyBailAmount", 
                "ContractEndClause", "ContractExtendClause", "ContractObject", "ContractObjetToModify", "ContractToModify", 
                "ContractualPenaltyMonths", "ContractValidityMonths", "ConventionalPenaltyAmount", "ConventionalPenaltyPercentage", "CurrencyCountry", 
                "CurrencyType", "CustomsAgentPatentNumber", "CustomsState", "DateContractToModify", "DatePropertyDeed", 
                "DaysDeadlineForPayment", "DaysForAnomalyDeliveryReport", "DaysNoticeForEarlyTermination", "DeedOrCommercialRegister", "DeliveryCalendarDays", 
                "DeliveryCost", "DeliveryMonthNominativeCheck", "DepositAmount", "DepositsRealizedMonthsNumber", "Developer", 
                "Digitalization", "DigitalizationCost", "DiscountAgreedService", "EmployeesNumber", "EndDateClause", 
                "EndDateContractToEnd", "EndDateDeclaration", "EquivalentDepositsMonthsNumber", "ExtensionAmount", "ExtensionForcedYearsLetter", 
                "ExtensionNumber", "FiberCopy", "FiberCopyCost", "FidelityBailAmount", "FinancialEntityAddress", 
                "FinancialEntityAtention", "FinancialEntityBranchAddress", "FinancialEntityDirection", "FinancialEntityLegalRepresentativePosition", "FolioDeedOrCommercialRegister", 
                "ForcedYears", "FractionationName", "FrameContractNumberTelecomServices", "GracePeriodMonths", "HealthLicenseGrantsAuthority", 
                "HiddenVicesAmount", "HiddenVicesBailIdDoc", "InitialAdvanceAmount", 
                "InscriptionCommercialFolioState", "IsCpySignCntrDelvrdLegal", "IsExpiredAttended", "IsHiddenVices", "IsOrigSignCntrDelvrdLegal", 
                "JurisdictionState", "MegacableObligationsPaymentPercentageExchange", "MegacableServiceSupplierProvided", "MonthlyMaintenanceAmount", "MonthlyRentAmount", 
                "NetworkCopy", "NetworkCopyCost", "NetworkGPON", "NetworkGPONCost", "NominativeCheckDeliveryDay", 
                "NotaryDeed", "NumberNotaryDeed", "OcupationDate", "OfficialId", "OfficialIdNumber", 
                "OutsourcedAddress", "OutsourcedAtention", "OutsourcedMail", "OutsourcedPhoneNumber", "PaExtensionForcedYears", 
                "PaExtensionVoluntaryYears", "PassiveActor", "PaymentCycle", "PaymentMethodSubscribers", "PersonMailSendDailySalesReports", 
                "PersonNameSendDailySalesReports", "PremisesInTheSquareNumber", "ProemName", "Property", "PropertyAddress", 
                "PropertyBusinessLine", "PropertyDateVacated", "PropertyDeed", "PropertyDeliveryDate", "PropertyDeliveryPeriod", 
                "ProviderServiceMegacableProvided", "Proyect", "ProyectAddress", "PublicDeedNumberCopy", "PublicNotaryDeed", 
                "RegistrationDateFolio", "ReimbursementTerminationCalendarDays", "RenovationRequisitionParent", "RentClause", "RentEquivalent", 
                "RepresentativeSocietyName", "RetroactiveDate", "ScheduleFrom", "ScheduleService", "ScheduleTimeFrom", 
                "ScheduleTimeFromEnd", "ScheduleTimeTo", "ScheduleTimeToEnd", "ScheduleTo", "SellerObligationsPaymentPercentageExchange", 
                "ServiceLocationState", "ServiceSchedule", "ServiceStartDate", "SettlementObligations", "SignedContractSendDateLegal", 
                "SignState", "SpaceServiceGranted", "SquareName", "Stage", "StageStartDate", 
                "StandardizedKeyBankingFinancialEntity", "StateCivilLaw", "StateNotaryDeed", "StepsBuildNumber", "StrokeStreet", 
                "StrokeStreetCost", "Subsidiaries", "SupplierDeedComercialFolio", "SupplierDeedConstitutionDate", "SupplierDeedInscrptDateFolio", 
                "SupplierDeedNotary", "SupplierDeedNumber", "SupplierIMMS", "SupplierLegalRepresentativePosition", "SupplierNationality", 
                "SupplierNotaryNumber", "SupplierNotaryState", "SupplierObligations", "SurfaceLetter", "SurfaceStoreMerchandise", 
                "Surveying", "SurveyingCost", "TotalAmountForServicesRendered", "TotalNetAmountOfWorkDone", "TotalPaymentPercentajeAmountTotal", 
                "TowerNameProperty", "TransferDay", "TransferMonth", "Volume", "VoluntaryYears", 
                "WorksEndDate", "WorksStartDate", "NegotiatorRepresentativeName", "MetrocarrierSquareAddress", "FrameworkContractSingDate", 
                "BillingContactName", "BillingPosition", "BillingEmail", "BillingPhone", "BillingExtension", "BillingFax", 
                "TechnitianContactName", "TechnitianPosition", "TechnitianEmail", "TechnitianPhone", "TechnitianExtension", 
                "TechnitianFax", "ServiceSellerName", "PromissoryNoteAmount", 
                "Facturation", "ActReceptionService", "EthernetPrivateLine", "Vpn",
                "DedicatedInternet", "DigitalPrivateLine", "Infrastructure", "VideoPrivateLine", 
                "Trunk", "OtherServices", "CableTv", "BusinessInternet",
                "BusinessTelephony", "Intranet", "TecnologySolutions"," GoogleServiceCloud",
                "REQUISITION.IdArea", "REQUISITION.IdUnit","REQUISITION.Contract", "REQUISITION.ContractApplicant", 
                "REQUISITION.idCompany", "REQUISITION.IdDocument", "REQUISITION.ContractRisk", "REQUISITION.VoBocontractRisk"};

        StringBuilder sb = new StringBuilder();
        int n = 0;
        for(String fld: aFields) {
            sb.append(", "); 
            sb.append(fld);
        }
        sb.append(" ");
        return sb.toString();    	
    }

    private void buildAttatchmentsFields(final StringBuilder query) {
        query.append("AttatchmentDeliverables, AttchmtServiceLevelsMeasuring, AttatchmentPenalty, ");
        query.append("AttatchmentScalingMatrix, AttatchmentCompensation, AttchmtBusinessMonitoringPlan, ");
        query.append("BusinessReasonMonitoringPlan, AttchmtImssInfoDeliveryReqrmts, ");
        query.append("AttatchmentInformationSecurity, AttatchmentOthers, AttatchmentOthersName, ");
    }

    private void buildInstrumentCharacteristicsFields(final StringBuilder query) {
        query.append("IsRequiredHumanResources, IsImssCeduleGiven, IsSupplierSingleRegistered, IsSupplierPenalties, ");
        query.append("SupplierPenaltiesText, IsAdvanceBailNeeded, AdvanceBailAmount, IsFulfillmentBailNeeded, ");
        query.append("FulfillmentBailAmount, IsFidelityBailNeeded, IsContingencyBailNeeded, ");
        query.append("IsCivilRespInsuranceBailNeeded, IsContractTermAuthorized, ");
        query.append("IsSpecialProvisionNegotiated, IsWarrantyDeposit, SpecialProvisionNegotiated, ");
        query.append("IsStaffUnderGroupDirecction, ImssCeduleNotGivenIdDocument, AdvanceBailNotNeededIdDocument, ");
        query.append("FulfillmentBailNeedNoIdDoc, FidelityBailNeedNoIdDoc, ");
        query.append("ContingencyBailNeedNoIdDoc, CivilRespInsurBailNeedNoIdDoc, ");
    }

    private void buildClausulesSectionFields(final StringBuilder query) {
        query.append("ContractObjectClause, ConsiderationClause, Clabe, ContractDurationDate, ");
        query.append("ConsiderationInitialReport, ConsiderationMonthlyReport, ConsiderExtraordinaryReport, ");
        query.append("InitialPaymentPercentage, InitialPaymentPeriod, MonthlyPaymentPercentage, ");
        query.append("MonthlyPaymentPeriod, ExtraordinaryPaymentPercentage, ExtraordinaryPaymentPeriod, ");
    }

    private void buildDeclarationsSectionFields(final StringBuilder query) {
        query.append("DomainName, BrandName, SubcontractorLegalRepName, ExtensionsNumber, ExtensionYears, ");
        query.append("ExtensionPeriod, ExtensionValidity, ExtensionForcedYears, ExtensionVoluntaryYears, ");
        query.append("RentInitialQuantity, ExtensionFirstYearRent, MaintenanceInitialQuantity, ");
        query.append("StartExtensionDate, EndExtensionDate, StartFirstExtensionDate, EndFirstExtensionDate, ");
        query.append("MonthlyRentCurrency, ");
    }

    private void buildContractDraftFields(final StringBuilder query) {
        query.append("NatPersonTenantDeclarations, MoralOrNatPersonDeclarations, ContractDate, Surface, ");
        query.append("PropertyDeedTitleNumber, PropertyDeedTitleDate, PropertyDeedTitleNotary, ");
        query.append("PropertyDeedTitleNotaryNumber, PropertyDeedTitleNotaryState, PropDeedTitleCommercialFolio, ");
        query.append("PropDeedTitleRegistrationDate, SubcontactedPersonality, DirectSupplierPersonality, ");
        query.append("ContractType, EventName, EventDatetime, ClausulesToModify, ContractValidity, ");
        query.append("MaintenanceClause, SignDate, ");
    }
    
    private void buildSubQueryTempleteCurrentVersion(final StringBuilder query) {
        query.append("(SELECT CurrentVersion AS TemplateCurrentVersion ");
        query.append("FROM DOCUMENT WHERE DOCUMENT.IdDocument = REQUISITION.TemplateIdDocument) , ");
    }

    private String buildSaveRequisitionVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO  REQUISITION_V (");
        this.buildAllNonPrimaryFields(query);
        query.append(") SELECT ");
        this.buildAllNonPrimaryFieldsFromRequisition(query);
        query.append("FROM REQUISITION ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private MapSqlParameterSource createFindRequisitionByIdNamedParameters(final Integer idRequisition) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
        return namedParameters;
    }

    private String buildSaveRequisitionRequisitionVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONREQUISITION_V (IdRequisition, IdRequisitionVersion,VersionNumber) ");
        query.append("VALUES (:IdRequisition, :IdRequisitionVersion, (");
        query.append("SELECT COALESCE(MAX(VersionNumber) + 1, 1) FROM REQUISITIONREQUISITION_V ");
        query.append(WHERE_ID_EQUALS_ID);
        query.append("))");
        return query.toString();
    }

    private MapSqlParameterSource createVersionRequisitionNamedParameters(
            final Integer idRequisition, final Integer idRequisitionVersion) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
        namedParameters.addValue(TableConstants.ID_REQUISITION_VERSION, idRequisitionVersion);
        return namedParameters;
    }

    private String buildSaveRequisitionApprovalAreasVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONAPPROVALAREA_V (IdArea, IdRequisitionVersion, VoboIdDocument) ");
        query.append("SELECT IdArea, :IdRequisitionVersion, VoboIdDocument FROM REQUISITIONAPPROVALAREA ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildSaveRequisitionAttatchmentsVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONATTACHMENT_V (IdDocument, IdRequisitionVersion) ");
        query.append("SELECT IdDocument, :IdRequisitionVersion FROM REQUISITIONATTACHMENT ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildSaveRequisitionAuthorizationDgasVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONAUTHORIZATIONDGA_V (IdDga, IdRequisitionVersion) ");
        query.append("SELECT IdDga, :IdRequisitionVersion FROM REQUISITIONAUTHORIZATIONDGA ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildSaveRequisitionFinantialEntitiesVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONFINANCIALENTITY_V (IdFinancialEntity, IdRequisitionVersion,  ");
        query.append("Phone, Email, Attention, Rfc)  ");
        query.append("SELECT IdFinancialEntity, :IdRequisitionVersion, Phone, Email, Attention, Rfc ");
        query.append("FROM REQUISITIONFINANCIALENTITY ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildSaveRequisitionFinantialEntityWitnessesVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONFINANCIALENTWIT_V (Name, IdRequisitionVersion) ");
        query.append("SELECT Name, :IdRequisitionVersion FROM REQUISITIONFINANCIALENTWIT ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildSaveRequisitionLegalRepresentativesVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQLEGALREPRESENTATIVE_V ");
        query.append("(IdLegalRepresentative, IdRequisitionVersion, SignSendDate, SignReturnDate, ");
        query.append("IsOriginalSignedContDelivered, IsCopySignedContractDelivered, SignedContractSendDate) ");
        query.append("SELECT IdLegalRepresentative, :IdRequisitionVersion , SignSendDate, SignReturnDate, ");
        query.append("IsOriginalSignedContDelivered, IsCopySignedContractDelivered, SignedContractSendDate ");
        query.append("FROM REQLEGALREPRESENTATIVE ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildSaveRequisitionUsersVoboVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONUSERSVOBO_V (IdUser, IdRequisitionVersion) ");
        query.append("SELECT IdUser, :IdRequisitionVersion FROM REQUISITIONUSERSVOBO ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildsaveScalingVersionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO SCALING_V (IdScaling, Name, Position, Area, Phone, Mail, ");
        query.append("ScalingLevel, idRequisitionVersion, ScalingType ) ");
        query.append("SELECT IdScaling, Name, Position, Area, Phone, Mail, ScalingLevel, ");
        query.append(":IdRequisitionVersion, ScalingType FROM SCALING ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildFindRequisitionVersionsQuery() {
        final StringBuilder query = new StringBuilder();
//        query.append("SELECT IdRequisition, IdRequisitionVersion, VersionNumber FROM REQUISITIONREQUISITION_V ");
//        query.append(WHERE_ID_EQUALS_ID);
//        query.append("ORDER BY VersionNumber DESC ");
        query.append("SELECT top 1 v.IdVersion, v.IdDocument, v.DocumentPath, v.VersionNumber, v.IdUser, v.CreateDate,");
        query.append("(CONVERT(VARCHAR(10), v.CreateDate, 103) + ' ' + convert(VARCHAR(8), v.CreateDate, 14)) AS CreateDateString,");
        query.append("(COALESCE(u.Name,'') + COALESCE(' ' + u.FirstLastName,'') +COALESCE(' ' + u.SecondLastName,'')) as UserName ");
        query.append("FROM REQUISITIONCONTRACTHISTORY AS h ");
        query.append("INNER JOIN VERSION AS v ON v.IdDocument = h.IdDocument ");
        query.append("LEFT JOIN USERS AS u ON u.IdUser = v.IdUser ");
        query.append("WHERE h.IdRequisition = :IdRequisition ");
        query.append("GROUP BY v.IdVersion, v.IdDocument, v.DocumentPath, v.VersionNumber, v.IdUser, v.CreateDate, ");
        query.append("u.Name, u.FirstLastName, u.SecondLastName ");
        query.append("ORDER BY v.IdDocument DESC, v.versionNumber DESC ");
        
        return query.toString();
    }

    private String buiFldindApprovalAreasQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdArea FROM REQUISITIONAPPROVALAREA_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String findApprovalAreasNameQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT AREA.Name FROM AREA ");
        builder.append("INNER JOIN REQUISITIONAPPROVALAREA_V ");
        builder.append("ON AREA.IdArea = REQUISITIONAPPROVALAREA_V.IdArea ");
        builder.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return builder.toString();
    }

    private String buildFindAttatchmentsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT VERSION.IdDocument, DocumentPath, VersionNumber ");
        query.append("FROM REQUISITIONATTACHMENT_V INNER JOIN ");
        query.append("DOCUMENT ON REQUISITIONATTACHMENT_V.IdDocument = DOCUMENT.IdDocument INNER JOIN ");
        query.append("VERSION ON VERSION.IdDocument = DOCUMENT.IdDocument ");
        query.append("AND VERSION.VersionNumber = DOCUMENT.CurrentVersion ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildFindAuthorizationDgasQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT DGA.IdDga, Name, Status FROM REQUISITIONAUTHORIZATIONDGA_V INNER JOIN ");
        query.append("DGA ON DGA.IdDga = REQUISITIONAUTHORIZATIONDGA_V.IdDga ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildFindFinantialEntitiesQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT FE.IdFinancialEntity, FE.Name, FER.Phone, FER.Email, ");
        query.append("FER.Attention, FER.Rfc, FE.AccountNumber, FE.BankBranch, FE.BankingInstitution ");
        query.append("FROM REQUISITIONFINANCIALENTITY_V AS FER INNER JOIN FINANCIALENTITY AS FE ON ");
        query.append("FE.IdFinancialEntity = FER.IdFinancialEntity ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildFindFinantialEntitiesWitnessesQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT Name FROM REQUISITIONFINANCIALENTWIT_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildFindLegalRepresentativesQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT LEGALREPRESENTATIVE.IdLegalRepresentative, Name, Status, SignSendDate, SignReturnDate, ");
        query.append("IsOriginalSignedContDelivered, IsCopySignedContractDelivered, SignedContractSendDate  ");
        query.append("FROM REQLEGALREPRESENTATIVE_V INNER JOIN LEGALREPRESENTATIVE ");
        query.append("ON LEGALREPRESENTATIVE.IdLegalRepresentative = ");
        query.append("REQLEGALREPRESENTATIVE_V.IdLegalRepresentative ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildFindUsersVoboQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT USERS.IdUser, USERS.Name, USERS.FirstLastName, USERS.SecondLastName, ");
        query.append("AREA.Name AS AreaName, POSITION.Name AS PositionName ");
        query.append("FROM REQUISITIONUSERSVOBO_V INNER JOIN ");
        query.append("USERS ON USERS.IdUser = REQUISITIONUSERSVOBO_V.IdUser LEFT JOIN ");
        query.append("AREA ON AREA.IdArea = USERS.IdArea LEFT JOIN ");
        query.append("POSITION ON POSITION.IdPosition = USERS.IdPosition ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITION_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildDeleteRequisitionRequisitionVersionsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONREQUISITION_V ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildDeleteApprovalAreasQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONAPPROVALAREA_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildDeleteAttatchmentsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONATTACHMENT_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildDeleteAuthorizationDgasQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONAUTHORIZATIONDGA_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildDeleteFinancialEntitiesQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONFINANCIALENTITY_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildDeleteFinantialEntityWitnessesQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONFINANCIALENTWIT_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildDeleteLegalRepresentativesQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQLEGALREPRESENTATIVE_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildDeleteUsersVoBoQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONUSERSVOBO_V ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    private String buildFindApprovalAreasVoBoQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT AREA.IdArea, AREA.Name, VoboIdDocument, VERSION.DocumentPath ");
        query.append("FROM REQUISITIONAPPROVALAREA_V INNER JOIN ");
        query.append("AREA ON AREA.IdArea = REQUISITIONAPPROVALAREA_V.IdArea LEFT JOIN ");
        query.append("DOCUMENT ON DOCUMENT.IdDocument = REQUISITIONAPPROVALAREA_V.VoboIdDocument LEFT JOIN ");
        query.append("VERSION ON DOCUMENT.IdDocument = VERSION.IdDocument ");
        query.append("AND DOCUMENT.CurrentVersion = VERSION.VersionNumber ");
        query.append(WHERE_ID_VERSION_EQUALS_ID_VERSION);
        return query.toString();
    }

    @Override
    public List<String> findApprovalAreasName(final Integer idRequisitionVersion) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.ID_REQUISITION_VERSION, idRequisitionVersion);
            return this.namedjdbcTemplate.queryForList(this.findApprovalAreasNameQuery(), 
                    parameterSource, String.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }


    // DEBUGGING CODE PLEASE DO NOT REMOVE - LuisV
    @Override
    public String debug(String task) throws DatabaseException {
        final String focusTable = "REQUISITION_V"; // "REQUISITION_TEST";
        ArrayList<String> fieldsReqV = null;
        String s = "";
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        String sql = "";
        switch(task) {

        // getAllQueries - sends the actual text of the queries sent to the database
        // 				   this is useful when debugging the queries and we want to
        // 				   make sure all parts are in.
        case "getAllQueries":
            RequisitionDAO reqDao = new RequisitionDAO();
            //			s = s + "buildFindByIdQuery() = ";
            //			s = s + this.buildFindByIdQuery();
            //	        s = s + (char) 10 + (char) 10;
            //			s = s + "buildFindByIdQuery() = ";
            //			s = s + reqDao.buildFindByIdQuery();
            //	        s = s + (char) 10 + (char) 10;
            s = s + "buildSaveRequisitionVersionQuery() = ";
            s = s + buildSaveRequisitionVersionQuery();
            s = s + (char) 10 + (char) 10;
            //	        s = s + "buildSaveRequisitionRequisitionVersionQuery() = ";
            //	        s = s + buildSaveRequisitionRequisitionVersionQuery();
            //	        s = s + (char) 10 + (char) 10;
            //	        s = s + "buildSaveRequisitionApprovalAreasVersionQuery() = ";
            //	        s = s + buildSaveRequisitionApprovalAreasVersionQuery();
            //	        s = s + (char) 10 + (char) 10;
            //	        s = s + "buildSaveRequisitionAttatchmentsVersionQuery() = ";
            //	        s = s + buildSaveRequisitionAttatchmentsVersionQuery();
            //	        s = s + (char) 10 + (char) 10;
            //	        s = s + "buildSaveRequisitionAuthorizationDgasVersionQuery() = ";
            //	        s = s + buildSaveRequisitionAuthorizationDgasVersionQuery();
            //	        s = s + (char) 10 + (char) 10;
            //	        s = s + "buildSaveRequisitionFinantialEntitiesVersionQuery() = ";
            //	        s = s + buildSaveRequisitionFinantialEntitiesVersionQuery();
            //	        s = s + (char) 10 + (char) 10;
            //	        s = s + "buildSaveRequisitionFinantialEntityWitnessesVersionQuery() = ";
            //	        s = s + buildSaveRequisitionFinantialEntityWitnessesVersionQuery();
            //	        s = s + (char) 10 + (char) 10;
            //	        s = s + "buildSaveRequisitionLegalRepresentativesVersionQuery() = ";
            //	        s = s + buildSaveRequisitionLegalRepresentativesVersionQuery();
            //	        s = s + (char) 10 + (char) 10;
            //	        s = s + "buildSaveRequisitionUsersVoboVersionQuery() = ";
            //	        s = s + buildSaveRequisitionUsersVoboVersionQuery();
            //	        s = s + (char) 10 + (char) 10;
            //	        s = s + "buildsaveScalingVersionQuery() = ";
            //	        s = s + buildsaveScalingVersionQuery();
            //	        s = s + (char) 10 + (char) 10;
            s = s + "fin";
            break;

            // getMissingFields - compares fields in Requisition vs Requisition_V (or whatever the focusTable is)
            // 					  the comparison is sent to the frontend for a LOG to be printed

        case "getMissingFields":

            try {
                sql = "select column_name from information_schema.columns where table_name = '" + focusTable + "'";
                fieldsReqV = (ArrayList<String>) this.namedjdbcTemplate.queryForList(sql, parameterSource, String.class);
                sql = "select column_name from information_schema.columns where table_name = 'REQUISITION'";
                ArrayList<String> fieldsReq = null;
                fieldsReq = (ArrayList<String>) this.namedjdbcTemplate.queryForList(sql, parameterSource, String.class);

                StringBuilder builder = new StringBuilder();
                for(String sfld: fieldsReq) {
                    if(! fieldsReqV.contains(sfld))
                        builder.append(sfld + "\n");
                }
                if(builder.length()==0) {
                    s = "there is no missing fields";
                } else {
                    s = "Requisition table fields missing at " + focusTable + "\n" + builder.toString();
                }	        		
            } catch (DataAccessException dataAccessException) {
                throw new DatabaseException(dataAccessException);	
            }
            break;

            // addMissingFieldsToTable
            // 					Compares tables recovering all the missing fields. When it finds a missing field
            // 				 	it assembles the ALTER SQL statement and sends it to the database to be processed
            // 					and the field to be added.

        case "addMissingFieldsToTable":
            try {
                sql = "select column_name from information_schema.columns where table_name = '" + focusTable + "'";
                fieldsReqV = (ArrayList<String>) this.namedjdbcTemplate.queryForList(sql, parameterSource, String.class);

                sql = "select column_name, column_default, is_nullable, data_type, character_maximum_length, "
                        + "numeric_precision, numeric_scale, datetime_precision "
                        + "from information_schema.columns where table_name = 'REQUISITION' "
                        + "order by ORDINAL_POSITION";
                ArrayList<ColumnAttributes> fieldsColAtt = null;
                fieldsColAtt = (ArrayList<ColumnAttributes>) this.namedjdbcTemplate.query(sql, parameterSource, 
                        new ColumnAttributesRowMapper());

                StringBuilder builder = new StringBuilder();
                for(ColumnAttributes ca: fieldsColAtt) {
                    boolean b = false;
                    for(String sReqV: fieldsReqV)
                        if(sReqV.equalsIgnoreCase(ca.colName)) {
                            b = true;
                            break;
                        }

                    if(!b && !ca.colName.toLowerCase().equals("idrequisition")) {
                        String type = "";
                        switch(ca.colDataType.toLowerCase()) {
                        case "varchar":
                            if(ca.colMaxLength == -1) type = "varchar(max)";
                            else type = "varchar(" + ca.colMaxLength + ")";
                            break;
                        case "numeric":
                            type = "numeric(" + ca.colNumPrecision + ", " + ca.colNumScale + ")";
                            break;
                        case "int":
                        case "bigint":
                        case "smallint":
                        case "bit":
                        case "date":
                        case "datetime":
                            type = ca.colDataType;
                            break;
                        }

                        if(ca.colIsNullable.toLowerCase().equals("no")) {
                            type = type + " not null";
                        }

                        sql = "alter table " + focusTable + " add " + ca.colName + " " + type;
                        this.namedjdbcTemplate.update(sql, parameterSource);

                        builder.append(sql + "\n");	        			
                    }
                }
                s = builder.toString();
            } catch (DataAccessException dataAccessException) {
                throw new DatabaseException(dataAccessException);	
            }
            break;        	
        }

        return s;
    }

    // the means for Spring to bring field related information from the database. 
    public class ColumnAttributesRowMapper implements RowMapper<ColumnAttributes> {
        @Override
        public ColumnAttributes mapRow(ResultSet rs, int rowNum) throws SQLException {
            ColumnAttributes colAttr = new ColumnAttributes();
            colAttr.setColName(rs.getString("column_name"));
            colAttr.setColDefault(rs.getString("column_default"));
            colAttr.setColIsNullable(rs.getString("is_nullable"));
            colAttr.setColDataType(rs.getString("data_type"));
            colAttr.setColMaxLength(rs.getInt("character_maximum_length"));
            colAttr.setColNumPrecision(rs.getInt("numeric_precision"));
            colAttr.setColNumScale(rs.getInt("numeric_scale"));
            colAttr.setColDatePrecision(rs.getInt("datetime_precision"));
            return colAttr;
        }
    }

    // ColumnAttributes: a placeholder for the Field Attribute information
    // 					 to be kept.

    public class ColumnAttributes {
        private String  colName;
        private String  colDefault;
        private String  colIsNullable;
        private String  colDataType;
        private Integer colMaxLength;
        private Integer colNumPrecision;
        private Integer colNumScale;
        private Integer colDatePrecision;
        public String getColName() {return colName;}
        public void setColName(String colName) {this.colName = colName;}
        public String getColDefault() {return colDefault;}
        public void setColDefault(String colDefault) {this.colDefault = colDefault;}
        public String getColIsNullable() {return colIsNullable;}
        public void setColIsNullable(String colIsNullable) {this.colIsNullable = colIsNullable;}
        public String getColDataType() {return colDataType;}
        public void setColDataType(String colDataType) {this.colDataType = colDataType;}
        public Integer getColMaxLength() {return colMaxLength;}
        public void setColMaxLength(Integer colMaxLength) {this.colMaxLength = colMaxLength;}
        public Integer getColNumPrecision() {return colNumPrecision;}
        public void setColNumPrecision(Integer colNumPrecision) {this.colNumPrecision = colNumPrecision;}
        public Integer getColNumScale() {return colNumScale;}
        public void setColNumScale(Integer colNumScale) {this.colNumScale = colNumScale;}
        public Integer getColDatePrecision() {return colDatePrecision;}
        public void setColDatePrecision(Integer colDatePrecision) {this.colDatePrecision = colDatePrecision;}
    }
    // END OF DEBUGGING CODE

    @Override
    public RequisitionVersion findIdRequisitionVersion(final Integer idRequisition) throws DatabaseException {
        try {
            return this.namedjdbcTemplate.queryForObject(this.findIdRequisitionVersionQuery(), 
                    new MapSqlParameterSource(TableConstants.ID_REQUISITION, idRequisition), 
                    new BeanPropertyRowMapper<RequisitionVersion>(RequisitionVersion.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findIdRequisitionVersionQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdRequisitionVersion, VersionNumber FROM REQUISITIONREQUISITION_V ");
        builder.append("WHERE IdRequisition = :IdRequisition ");
        builder.append("AND VersionNumber = (SELECT MAX(VersionNumber) FROM REQUISITIONREQUISITION_V ");
        builder.append("WHERE IdRequisition = :IdRequisition) ");
        return builder.toString();
    }
}
