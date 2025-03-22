package mx.pagos.admc.contracts.daos;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.engineer.utils.database.JdbcTemplateUtils;
import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.Requisitable;
import mx.pagos.admc.contracts.structures.ApprovalArea;
import mx.pagos.admc.contracts.structures.Attachment;
import mx.pagos.admc.contracts.structures.Clause;
import mx.pagos.admc.contracts.structures.ContractCancellationComment;
import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.FinantialEntityWitness;
import mx.pagos.admc.contracts.structures.Instrument;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.Obligation;
import mx.pagos.admc.contracts.structures.RequiredDocumentBySupplier;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionAngular;
import mx.pagos.admc.contracts.structures.RequisitionAttachment;
import mx.pagos.admc.contracts.structures.RequisitionDocuSign;
import mx.pagos.admc.contracts.structures.RequisitionDraftPart2;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.contracts.structures.RequisitionsPartFour;
import mx.pagos.admc.contracts.structures.RequisitionsPartOneAndTwo;
import mx.pagos.admc.contracts.structures.RequisitionsPartThree;
import mx.pagos.admc.contracts.structures.Scaling;
import mx.pagos.admc.contracts.structures.SupplierPersonByRequisition;
import mx.pagos.admc.contracts.structures.TrayFilter;
import mx.pagos.admc.contracts.structures.TrayRequisition;
import mx.pagos.admc.contracts.structures.UserInProgressRequisition;
import mx.pagos.admc.contracts.structures.UserInProgressRequisitionFilter;
import mx.pagos.admc.contracts.structures.dtos.RequisitionDTO;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.ScalingTypeEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.document.version.daos.VersionDAO;
import mx.pagos.document.versioning.structures.DocumentBySection;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.constants.TableConstantsSecurity;
import mx.pagos.security.structures.User;

/**
 * 
 * @author Mizraim
 * 
 *
 */
@Repository
public class RequisitionDAO implements Requisitable {
	
	private static final Logger Log = Logger.getLogger(RequisitionDAO.class);

	private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";
	private static final String TURN_DATE_PLUS_TURN_ATTENTION_DAYS = "(Turn.TurnDate + TURN.AttentionDays)";
	private static final String SELECT_COUNT_ID_REQUISITION = "SELECT COUNT(IdRequisition) ";
	private static final String WHERE_ID_FLOW_EQUALS_ID_FLOW = "WHERE IdFlow = :IdFlow ";
	private static final String WHERE_REQUISITION_ID_REQUISITION_EQUALS_ID_REQUISITION = "WHERE REQUISITION.IdRequisition = :IdRequisition";
	private static final String SINGLE_QUOTE = "'";
	private static final String WHEN = "WHEN ";
	private static final String WHERE_ID_REQUISITION_AND_ID_USER = "WHERE IdRequisition = :IdRequisition AND IdUser = :IdUser";
	private static final String STATUS = "Status";
	private static final String SELECT = "SELECT ";
	private static final String RIGHT_BRACES = ")";
	private static final String DELETE_FROM_REQUISITIONDIGITALIZATION = "DELETE FROM REQUISITIONDIGITALIZATION ";
	private static final String DELETE_FROM_REQUISITIONATTACHMENT = "DELETE FROM REQUISITIONATTACHMENT ";
	private static final String WHERE_ID_REQUISITION_EQUALS_AND_ID_LEGAL_REPRESENTATIVE = "WHERE IdRequisition = :IdRequisition AND IdLegalRepresentative = :IdLegalRepresentative";
	private static final String UPDATE_REQUISITIONLEGALREPRESENTATIVE_SET = "UPDATE REQLEGALREPRESENTATIVE SET ";
	private static final String UPDATE_REQUISITION_SET = "UPDATE REQUISITION SET ";
	private static final String COMMA = ", ";
	private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE REQUISITION.Status = :Status ";
	private static final String FROM_REQUISITION = "FROM REQUISITION ";
	private static final String FROM_USERS="FROM USERS ";
	private static final String WHERE_ID_EQUALS_ID = "WHERE IdRequisition = :IdRequisition ";
	private static final String WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT = "WHERE idDocument = :IdDocument";
	private static final String NULL = "Null";
	private static final String LIKE = "%";
	private static final String ORDER_BY_ID = " ORDER BY IdRequisition DESC ";
	private static final String WHERE_ID_SUPPLIER_PERSON_EQUALS_ID_SUPPLIER_PERSON = "WHERE Id= :Id ";
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Autowired
	private DatabaseUtils databaseUtils;

	public void setDatabaseUtils(final DatabaseUtils databaseUtilsParameter) {
		this.databaseUtils = databaseUtilsParameter;
	}

	@Override
	public Integer saveOrUpdate(final Requisition requisition) throws DatabaseException {
		return requisition.getIdRequisition() == null ? this.insertRequisition(requisition)
				: this.updateRequisition(requisition);
	}

	@Override
	public Integer saveOrUpdate(final RequisitionsPartOneAndTwo requisition) throws DatabaseException {
		if (requisition.getIdRequisition() == null) {
			requisition.setIdRequisition(0);
			requisition.setIdDocumentType(Constants.CERO);
			return this.insertRequisition(requisition);
		} else if (requisition.getIdRequisition() == Constants.CERO) {
			requisition.setIdRequisition(0);
			requisition.setIdDocumentType(Constants.CERO);
			return this.insertRequisition(requisition);
		} else if (requisition.getIdRequisition() <= 0) {
			return this.insertRequisition(requisition);
		} else {
			return this.updateRequisition(requisition);
		}
	}

	@Override
	public Integer saveOrUpdate5(final Instrument requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
			StringBuilder str = new StringBuilder();
			str.append("  RenewalPeriods = :RenewalPeriods, ");
			str.append(" FinancialEntityAddress= :FinancialEntityAddress,   ");
			str.append(" TechnicalDetails= :TechnicalDetails, Validity= :Validity ");
			this.namedjdbcTemplate.update(this.buildUpdateQueryStepMayor(str.toString()), namedParameters);
			return requisition.getIdRequisition();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer saveOrUpdate6(final Attachment requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
			StringBuilder str = new StringBuilder();
			str.append(
					" AttatchmentDeliverables = :AttatchmentDeliverables, AttchmtServiceLevelsMeasuring = :AttchmtServiceLevelsMeasuring, AttatchmentPenalty = :AttatchmentPenalty, ");
			str.append(
					" AttatchmentScalingMatrix= :AttatchmentScalingMatrix, AttatchmentCompensation= :AttatchmentCompensation, AttchmtBusinessMonitoringPlan= :AttchmtBusinessMonitoringPlan, ");
			str.append(
					" AttchmtImssInfoDeliveryReqrmts= :AttchmtImssInfoDeliveryReqrmts, AttatchmentInformationSecurity= :AttatchmentInformationSecurity, AttatchmentOthers= :AttatchmentOthers, ");
			str.append(
					" BusinessReasonMonitoringPlan= :BusinessReasonMonitoringPlan, AttatchmentOthersName= :AttatchmentOthersName ");
			this.namedjdbcTemplate.update(this.buildUpdateQueryStepMayor(str.toString()), namedParameters);
			return requisition.getIdRequisition();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer saveOrUpdate7(final Clause requisition) throws DatabaseException {
		try {
					
			final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
			StringBuilder str = new StringBuilder();
			str.append(
					" ContractObject = :ContractObject, ConsiderationClause = :ConsiderationClause, ContractObjectClause = :ContractObjectClause, ValidityStartDate= :ValidityStartDate, ValidityEndDate= :ValidityEndDate, ClausulaFormaPago= :ClausulaFormaPago, ");
			Integer lawyer = findFirstLawyer();
			if (0 == lawyer) {
				str.append(" DepositAmount = :DepositAmount ");
			} else {
				str.append(" DepositAmount = :DepositAmount, ");
				str.append(" IdLawyer= :IdLawyer ");
				requisition.setIdLawyer(lawyer);
				Log.info("ID Assigned Lawyer : " + lawyer);
			}
			
			Log.info("FechaInicio :: ("+requisition.getValidityStartDate()+")\n"
					+ "FechaFin :: ("+requisition.getValidityEndDate()+")");
			
			this.namedjdbcTemplate.update(this.buildUpdateQueryStepMayor(str.toString()), namedParameters);
			if (0 != lawyer) {
				return requisition.getIdRequisition();
			}
			return -1;
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer saveOrUpdateRequisitionDraftPart2(RequisitionDraftPart2 requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this
					.createRequisitionNamedParametersDraftPart2(requisition);
			StringBuilder query = new StringBuilder();

			query.append(
					" idDocumentType = :idDocumentType, UpdateRequisitionBy = :UpdateRequisitionBy, ActiveActor= :ActorActivo, PassiveActor= :ActorPasivo ");
			this.namedjdbcTemplate.update(this.buildUpdateQueryStepMayor(query.toString()), namedParameters);
			return requisition.getIdRequisition();
		} catch (

				DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer saveOrUpdateRequisitionDraftProem(Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this
					.createRequisitionNamedParametersDraftProem(requisition);
			StringBuilder query = new StringBuilder();
			if (requisition.getActiveActor() != null && requisition.getPassiveActor() != null) {
				query.append(
						"  activeActor= :activeActor, passiveActor= :passiveActor, ");

			} 
			query.append(" serviceDescription = :serviceDescription, contractObjetToModify =:contractObjetToModify  ");

			this.namedjdbcTemplate.update(this.buildUpdateQueryStepMayor(query.toString()), namedParameters);
			return requisition.getIdRequisition();
		} catch (

				DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer saveOrUpdateRequisitionDraftClausules(Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this
					.createRequisitionNamedParametersDraftProem(requisition);
			StringBuilder query = new StringBuilder();
			query.append(
					" ContractToModify = :contractToModify,  NumeroConvenio = :numeroConvenio, DateContractToModify= :dateContractToModify, ");

			query.append(
					" paymentCycle = :paymentCycle, ConventionalPenaltyAmount = :conventionalPenaltyAmount, daysDeadlineForPayment= :daysDeadlineForPayment, gracePeriodMonths= :gracePeriodMonths, ");
			query.append(
					" monthlyRentAmount = :monthlyRentAmount, depositAmount= :depositAmount, rentEquivalent= :rentEquivalent, ");
			query.append(
					" compensationMonthsRent = :compensationMonthsRent, stateCivilLaw= :stateCivilLaw, propertyBusinessLine= :propertyBusinessLine, ");
			query.append(
					" contractValidity = :contractValidity, jurisdictionState= :jurisdictionState, signDate= :signDate, DescripcionClausulaModificada= :descripcionClausulaModificada, ");
			query.append(
					" SolicitudDescripcionNegociacion = :solicitudDescripcionNegociacion, SolicitudDescripcionLargaNegociacion= :solicitudDescripcionLargaNegociacion, NumeroAdendum= :numeroAdendum, ");
			query.append(
					" EndDateContractToEnd = :endDateContractToEnd, ContratoaTerminar= :contratoaTerminar, NombreClausulaAdicionar= :nombreClausulaAdicionar, NombreAnexoAdicionar= :nombreAnexoAdicionar, ");
			query.append(
					" signState = :signState, ContractObject= :contractObject, ConsiderationClause= :considerationClause, FechaTerminacionContratoaFinalizar= :fechaTerminacionContratoaFinalizar, ");
			query.append(
					" ConsiderationAmount = :ConsiderationAmount, ContractObjectClause= :contractObjectClause, validityEndDate= :validityEndDate, ");
			query.append(
					" PenalizacionRentaMensualInmueble = :penalizacionRentaMensualInmueble, NombreFiador= :nombreFiador, FiadorNumeroEscrituraPublica= :fiadorNumeroEscrituraPublica, ");
			query.append(
					" propertyDeliveryDate = :propertyDeliveryDate, estacionamientosInmueble= :estacionamientosInmueble, SettlementObligations= :settlementObligations, ");
			query.append(
					" PenalizacionComisionRentaMensual = :penalizacionComisionRentaMensual, DaysNoticeForEarlyTermination= :daysNoticeForEarlyTermination, ");
			query.append(
					" InitialAdvanceAmount = :initialAdvanceAmount, extraordinaryPaymentPercentage = :extraordinaryPaymentPercentage, ContactoMatenimiento = :contactoMatenimiento,");
			query.append(
					" fiadorFechaEscrituraPublica = :fiadorFechaEscrituraPublica, FiadorNumeroNotariaPublica= :fiadorNumeroNotariaPublica, PorcentajeIvaContraprestacion = :PorcentajeIvaContraprestacion, ");
			query.append(
					" validityStartDate = :validityStartDate, ClausulaFormaPago= :ClausulaFormaPago , conventionalPenaltyPercentage = :conventionalPenaltyPercentage, DescripcionEquipos = :descripcionEquipos, ");
			query.append(
					" CesionDerechos = :cesionDerechos, ObligacionesEspecificas= :obligacionesEspecificas , TechnicalDetails = :technicalDetails, ImportePolizaSeguro = :importePolizaSeguro  ");

			this.namedjdbcTemplate.update(this.buildUpdateQueryStepMayor(query.toString()), namedParameters);
			return requisition.getIdRequisition();
		} catch (

				DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer saveOrUpdateRequisitionDraftProperty(Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this
					.createRequisitionNamedParametersDraftProem(requisition);
			StringBuilder query = new StringBuilder();

			query.append(" property = :property, propertyAddress = :propertyAddress, surface= :surface ");
			this.namedjdbcTemplate.update(this.buildUpdateQueryStepMayor(query.toString()), namedParameters);
			return requisition.getIdRequisition();
		} catch (

				DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private Integer insertRequisition(final Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertRequisitionQuery(), namedParameters, keyHolder,
					new String[] { "IdRequisition" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private Integer insertRequisition(final RequisitionsPartOneAndTwo requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertRequisitionQuery2(), namedParameters, keyHolder,
					new String[] { "IdRequisition" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private BeanPropertySqlParameterSource createRequisitionNamedParameters(final Requisition requisition) {
		final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
		namedParameters.registerSqlType("Validity", Types.VARCHAR);
		namedParameters.registerSqlType("MonthlyRentCurrency", Types.VARCHAR);
		return namedParameters;
	}

	private BeanPropertySqlParameterSource createRequisitionNamedParameters(
			final RequisitionsPartOneAndTwo requisition) {
		final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
		namedParameters.registerSqlType("Validity", Types.VARCHAR);
		namedParameters.registerSqlType("MonthlyRentCurrency", Types.VARCHAR);
		return namedParameters;
	}

	private BeanPropertySqlParameterSource createRequisitionNamedParametersDraftPart2(
			final RequisitionDraftPart2 requisition) {
		final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
		namedParameters.registerSqlType(TableConstants.ID_REQUISITION, Types.INTEGER);
		return namedParameters;
	}

	private BeanPropertySqlParameterSource createRequisitionNamedParametersDraftProem(final Requisition requisition) {
		final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
		namedParameters.registerSqlType(TableConstants.ID_REQUISITION, Types.INTEGER);
		return namedParameters;
	}

	private BeanPropertySqlParameterSource createRequisitionNamedParameters(final Instrument requisition) {
		final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
		namedParameters.registerSqlType("Validity", Types.VARCHAR);
		namedParameters.registerSqlType("MonthlyRentCurrency", Types.VARCHAR);
		return namedParameters;
	}

	private BeanPropertySqlParameterSource createRequisitionNamedParameters(final Attachment requisition) {
		final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
		namedParameters.registerSqlType("Validity", Types.VARCHAR);
		namedParameters.registerSqlType("MonthlyRentCurrency", Types.VARCHAR);
		return namedParameters;
	}

	private BeanPropertySqlParameterSource createRequisitionNamedParameters(final Clause requisition) {
		final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
		namedParameters.registerSqlType("Validity", Types.VARCHAR);
		namedParameters.registerSqlType("MonthlyRentCurrency", Types.VARCHAR);
		return namedParameters;
	}

	private BeanPropertySqlParameterSource createRequisitionNamedParameters(final RequisitionsPartThree requisition) {
		final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
		namedParameters.registerSqlType("Validity", Types.VARCHAR);
		namedParameters.registerSqlType("MonthlyRentCurrency", Types.VARCHAR);
		return namedParameters;
	}

	private BeanPropertySqlParameterSource createRequisitionNamedParameters(final RequisitionsPartFour requisition) {
		final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
		namedParameters.registerSqlType("Validity", Types.VARCHAR);
		namedParameters.registerSqlType("MonthlyRentCurrency", Types.VARCHAR);
		return namedParameters;
	}

	private Integer updateRequisition(final Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
			this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
			return requisition.getIdRequisition();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private Integer updateRequisition(final RequisitionsPartOneAndTwo requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
			this.namedjdbcTemplate.update(this.buildUpdateQueryPartOneAndTwo(), namedParameters);
			return requisition.getIdRequisition();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private Integer updateRequisitionPartThree(final RequisitionsPartThree requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
			this.namedjdbcTemplate.update(this.buildUpdateQueryPartThree(), namedParameters);
			return requisition.getIdRequisition();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private Integer updateRequisitionPartFour(final RequisitionsPartFour requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = this.createRequisitionNamedParameters(requisition);
			this.namedjdbcTemplate.update(this.buildUpdateQueryPartFour(), namedParameters);
			return requisition.getIdRequisition();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void changeRequisitionStatus(final Integer idRequisition, final FlowPurchasingEnum status)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createRequisitionChangeStatusNamedParameters(idRequisition, status);
			this.namedjdbcTemplate.update(this.buildChangeStatusQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void changeRequisitionStatusToCancelled(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
			this.namedjdbcTemplate.update(this.buildChangeRequisitionStatusToCancelledQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Requisition findById(final Integer idRequisition) throws DatabaseException, EmptyResultException {
		try {

			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			String sql = this.buildFindByIdQuery();
			Requisition req = this.namedjdbcTemplate.queryForObject(sql, namedParameters,
					new BeanPropertyRowMapper<Requisition>(Requisition.class));
			return req;
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			throw new EmptyResultException(emptyResultDataAccessException);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	
	@Override
	public DocumentDS getUser(final Integer idRequisition) throws DatabaseException {
		Log.info("///////////Entro a GETUSER");
		try {
			Log.info("///////////Entro a TRY GETUSER QUERY");
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			String sql = this.buildGetUserQuery();
			DocumentDS req = this.namedjdbcTemplate.queryForObject(sql, namedParameters,
					new BeanPropertyRowMapper<DocumentDS>(DocumentDS.class));
			return req;
//		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
//			Log.error("///////////TRONO  TRY GETUSER QUERY");
//			throw new EmptyResultException(emptyResultDataAccessException);
		} catch (DataAccessException dataAccessException) {
			Log.info("///////////TRONO GETUSER QUERY");
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Requisition findByIdInProgress(final Integer idRequisition)
			throws DatabaseException, EmptyResultException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			String sql = this.buildFindByIdInProgressQuery();
			Requisition req = this.namedjdbcTemplate.queryForObject(sql, namedParameters,
					new BeanPropertyRowMapper<Requisition>(Requisition.class));
			return req;
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			throw new EmptyResultException(emptyResultDataAccessException);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Integer> findRequisitionsToCreateOneFromAnother(final String idRequisitionParameter,
			final String documentTypeParameter, final String supplierParameter) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindRequisitionsToCreateOneFromAnotherParameters(
					idRequisitionParameter, documentTypeParameter, supplierParameter);
			return this.namedjdbcTemplate.queryForList(this.buildFindRequisitionsToCreateOneFromAnotherQuery(),
					namedParameters, Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	@Override
	public List<RequisitionDTO> findAllRequisitions(RequisitionAngular requisition) throws DatabaseException {
//		try {
//			final MapSqlParameterSource namedParameters = this.createFindRequisitionsToCreateOneFromAnotherParameters(
//					idRequisitionParameter, documentTypeParameter, supplierParameter);
//			return this.namedjdbcTemplate.queryForList(this.buildFindAllRequisitionsQuery(),
////					namedParameters, UserInProgressRequisition.class);
//		Log.info("Hola soy el intento de idRequisitionParameter en el DAO " + idRequisitionParameter);
//		Log.info("Hola soy el intento de documentTypeParameter en el DAO  " + documentTypeParameter);
//		Log.info("Hola soy el intento de supplierParameter en el DAO " + supplierParameter);
		try {
            final MapSqlParameterSource namedParameters =this.createFindRequisitionByManyParametersprueba(requisition);
//            return this.namedjdbcTemplate.queryForList(this.buildFindAllRequisitionsQuery(),namedParameters, Integer.class);
            return this.namedjdbcTemplate.query(this.buildFindAllRequisitionsQuery(),
					namedParameters,new BeanPropertyRowMapper<>(RequisitionDTO.class));
		} catch (DataAccessException dataAccessException) {
			Log.error("error al intentar hacer el query", dataAccessException);
			throw new DatabaseException("error en el query",dataAccessException);
		}
	}
	@Override
	public List<Requisition> findByFlowPurchasingStatus(final FlowPurchasingEnum status, final Integer idFlow)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByFlowPurchasingStatusNamedParameters(status,
					idFlow);
			return this.namedjdbcTemplate.query(this.buildFindByFlowPurchasingStatusQuery(), namedParameters,
					new BeanPropertyRowMapper<Requisition>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionApprovalArea(final Integer idRequisition, final Integer idArea,
			final Integer voboIdDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createSaveRequisitionApprovalAreaNamedParameters(idRequisition, idArea, voboIdDocument);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionApprovalAreaQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionAuthorizationDga(final Integer idRequisition, final Integer idDga)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createSaveRequisitionAuthorizationDgaNamedParameters(idRequisition, idDga);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionAuthorizationDgaQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteRequisitionApprovalAreas(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			this.namedjdbcTemplate.update(this.buildDeleteRequisitionApprovalAreasByIdrequisitionQuery(),
					namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteRequisitionAuthorizationDgas(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			this.namedjdbcTemplate.update(this.buildDeleteRequisitionAuthorizationDgaByIdRequisitionQuery(),
					namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Integer> findRequisitionApprovalAreas(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.queryForList(this.buildFindRequisitionApprovalAreasQuery(), namedParameters,
					Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<String> findRequisitionApprovalAreasActive(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.queryForList(this.buildFindRequisitionApprovalAreasActiveQuery(),
					namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Integer> findRequisitionAuthorizationDgas(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.queryForList(this.buildFindRequisitionAuthorizationDgasQuery(),
					namedParameters, Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<String> findRequisitionAuthorizationDgasActive(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.queryForList(this.buildFindRequisitionAuthorizationDgasActiveQuery(),
					namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Dga> findAutorizationDga(Integer idRequisition) throws DatabaseException {
		return null;
	}

	@Override
	public void saveRequisitionLegalRepresentative(final Integer idRequisition,
			final Integer idLegalRepresentative) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createSaveRequisitionLegalRepresentativeNamedParameters(idRequisition, idLegalRepresentative);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionLegalRepresentativeQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<LegalRepresentative> findRequisitionLegalRepresentatives(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindRequisitionLegalRepresentativeQuery(), namedParameters,
					new BeanPropertyRowMapper<LegalRepresentative>(LegalRepresentative.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<String> findRequisitionLegalRepresentativesActive(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.queryForList(this.buildFindRequisitionLegalRepresentativeActiveQuery(),
					namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteRequisitionLegalRepresentatives(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			this.namedjdbcTemplate.update(this.buildDeleteRequisitionLegalRepresentativesQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Integer> findRequisitionFinancialEntityByIdRequisition(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
			return this.namedjdbcTemplate.queryForList(this.buildFindRequisitionFinancialQuery(), namedParameters,
					Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<String> findRequisitionFinancialEntityActiveByIdRequisition(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
			return this.namedjdbcTemplate.queryForList(this.buildFindRequisitionFinancialActiveQuery(), namedParameters,
					String.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<FinancialEntity> findActiveFinancialEntitiesByIdRequisition(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindActiveFinancialEntitiesByIdRequisitionQuery(),
					namedParameters, new BeanPropertyRowMapper<>(FinancialEntity.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<LegalRepresentative> findActiveLegalRepByRequisitionAndFinancialEnt(final Integer idRequisition,
			final Integer idFinancialEntity) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
			namedParameters.addValue(TableConstants.ID_FINANCIALENTITY, idFinancialEntity);
			return this.namedjdbcTemplate.query(this.buildFindActiveLegalRepByRequisitionAndFinancialEntQuery(),
					namedParameters, new BeanPropertyRowMapper<>(LegalRepresentative.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	public List<FinancialEntity> findFinancialEntityByRepLegalAndRequisition(Integer idRepLegal,
			Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_LEGALREPRESENTATIVE, idRepLegal);
			namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
			return this.namedjdbcTemplate.query(this.buildFinancialEntityByRepLegalAndRequisition(), namedParameters,
					new BeanPropertyRowMapper<>(FinancialEntity.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildFinancialEntityByRepLegalAndRequisition() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT FE.* FROM FINANCIALENTITY FE ").append(
				"INNER JOIN dbo.REQUISITIONFINANCIALENTITY RQF ON FE.IdFinancialEntity = RQF.IdFinancialEntity ")
		.append("INNER JOIN dbo.LEGALREPEFINANCIALENTITIES LRFE ON FE.IdFinancialEntity = LRFE.IdFinancialEntity ")
		.append("WHERE FE.STATUS = 'ACTIVE' ").append("AND RQF.IdRequisition = :IdRequisition ")
		.append("AND LRFE.IdLegalRepresentative = :IdLegalRepresentative");
		return query.toString();
	}

	private String buildFindActiveLegalRepByRequisitionAndFinancialEntQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT LR.* FROM REQUISITIONFINANCIALENTITY REQFE INNER JOIN ");
		query.append("FINANCIALENTITY FE ON REQFE.IdFinancialEntity = FE.IdFinancialEntity INNER JOIN ");
		query.append("REQLEGALREPRESENTATIVE REQLR ON REQFE.IdRequisition = REQLR.IdRequisition INNER JOIN ");
		query.append("LEGALREPEFINANCIALENTITIES LGLFE ON FE.IdFinancialEntity = LGLFE.IdFinancialEntity ");
		query.append("AND REQLR.IdLegalRepresentative = LGLFE.IdLegalRepresentative INNER JOIN ");
		query.append("LEGALREPRESENTATIVE LR ON LGLFE.IdLegalRepresentative = LR.IdLegalRepresentative ");
		query.append("WHERE FE.Status = 'ACTIVE' AND LR.Status = 'ACTIVE' ");
		query.append("AND REQFE.IdRequisition = :IdRequisition AND REQFE.IdFinancialEntity = :IdFinancialEntity ");
		return query.toString();
	}

	private String buildFindActiveFinancialEntitiesByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT FE.* FROM REQUISITIONFINANCIALENTITY REQFE ");
		query.append("INNER JOIN FINANCIALENTITY FE ON REQFE.IdFinancialEntity = FE.IdFinancialEntity ");
		query.append("WHERE FE.Status = 'ACTIVE' AND REQFE.IdRequisition = :IdRequisition ");
		return query.toString();
	}

	@Override
	public List<FinantialEntityWitness> findRequisitionFinancialEntityByIdRequisitionWitness(
			final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindRequisitionFinancialWitnessQuery(), namedParameters,
					new BeanPropertyRowMapper<FinantialEntityWitness>(FinantialEntityWitness.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionFinancialEntity(final FinancialEntity entity) throws DatabaseException {
		try {
			this.namedjdbcTemplate.update(this.buildSaveRequisitionFinancialEntityQuery(),
					new BeanPropertySqlParameterSource(entity));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteRequisitionFinancialEntity(final Integer idRequisition) throws DatabaseException {
		try {
			final StringBuilder queryDelete = new StringBuilder();
			queryDelete.append("DELETE FROM REQUISITIONFINANCIALENTITY WHERE IdRequisition = :IdRequisition");
			final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
			this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveUserToVoBo(final Integer idRequisition, final Integer idUser) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createSaveUserToVoBoNamedParameters(idRequisition,
					idUser);
			this.namedjdbcTemplate.update(this.buildSaveUserToVoBoQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<User> findUsersToVoBo(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.query(this.findUsersToVoBoQuery(), namedParameters,
					new BeanPropertyRowMapper<User>(User.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteUsersToVoBo(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			this.namedjdbcTemplate.update(this.buildDeleteUsersToVoBoQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionFinantialEntityWitness(final Integer idRequisition, final String witnessName)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createSaveRequisitionFinantialEntityWitnessNamedParameters(idRequisition, witnessName);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionFinantialEntityWitnessQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<String> findRequisitionFinantialEntityWitnesses(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.queryForList(this.buildFindRequisitionFinantialEntityWitnessesQuery(),
					namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteRequisitionFinantialEntityWitnesses(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			this.namedjdbcTemplate.update(this.buildDeleteRequisitionFinantialEntityWitnessesQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionLawyer(final Integer idRequisition, final Integer idLawyer)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createUpdateRequisitionLawyerNamedParameters(idRequisition, idLawyer);
			this.namedjdbcTemplate.update(this.buildUpdateRequisitionLawyerQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionAttatchmentsFields(final Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionAttatchmentsFieldsQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionEvaluator(final Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionEvaluatorQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveProviderAndWitnessesSignDates(final Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
			this.namedjdbcTemplate.update(this.buildSaveProviderAndWitnessesSignDatesQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionLegalRepresentativeSignDate(final Integer idRequisition,
			final LegalRepresentative legalRepresentative) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createSaveRequisitionLegalRepresentativeSignNamedParameters(idRequisition, legalRepresentative);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionLegalRepresentativeSignDateQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionSignedContractData(final Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionSignedContractDataQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionLegalRepresentativeSignedContractData(final Integer idRequisition,
			final LegalRepresentative legalRepresentative) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createSaveRequisitionLegalRepresentativeSignedContractDataNamedParameters(idRequisition,
							legalRepresentative);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionLegalRepresentativeSignedContractDataQuery(),
					namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer findHistoryDocumentsVersions(final Integer idSupplier, final Integer idRequiredDocument)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
			namedParameters.addValue(TableConstants.ID_REQUIRED_DOCUMENT, idRequiredDocument);
			return this.namedjdbcTemplate.queryForObject(this.buildFindHistoryDocumentsVersionsQuery(), namedParameters,
					Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer saveRequisitionAttatchment(final RequisitionAttachment requisitionAttatchment)
			throws DatabaseException {
		return this.insertRequisitionAttachment(requisitionAttatchment);
	}

	@Override
	public void deleteRequisitionAttatchmentByIdRequisition(final Integer idRequisition)
			throws DatabaseException {
		try {
			final StringBuilder queryDelete = new StringBuilder();
			queryDelete.append(DELETE_FROM_REQUISITIONATTACHMENT);
			queryDelete.append(WHERE_ID_EQUALS_ID);
			final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
			this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteRequisitionAttatchmentByIdDocument(final Integer idDocument) throws DatabaseException {
		try {
			final StringBuilder queryDelete = new StringBuilder();
			queryDelete.append(DELETE_FROM_REQUISITIONATTACHMENT);
			queryDelete.append(WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT);
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_DOCUMENT, idDocument);
			this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Integer> findRequisitionAttachmentByIdRequisition(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
			return this.namedjdbcTemplate.queryForList(this.findRequisitionAttachmentByIdRequisitionQuery(),
					namedParameters, Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveDigitalizationIdDocument(final Integer idRequisition, final Integer digitalizationIdDocument)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createSaveDigitalizationIdDocumentNamedParameters(idRequisition, digitalizationIdDocument);
			this.namedjdbcTemplate.update(this.buildSaveDigitalizationIdDocumentQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Version> findDigitalizationDocuments(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindDigitalizationDocumentsQuery(), namedParameters,
					new BeanPropertyRowMapper<Version>(Version.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteDigitalizationDocuments(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			this.namedjdbcTemplate.update(this.buildDeleteDigitalizationDocumentsQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<ApprovalArea> findRequisitionApprovalAreasVoBo(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindRequisitionApprovalAreasVoBoQuery(), namedParameters,
					new BeanPropertyRowMapper<ApprovalArea>(ApprovalArea.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveSupplierApprovalIdDocument(final Integer idRequisition,
			final Integer supplierApprovalIdDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
			namedParameters.addValue(TableConstants.SUPPLIER_APPROVAL_ID_DOCUMENT, supplierApprovalIdDocument);
			this.namedjdbcTemplate.update(this.buildSaveSupplierApprovalIdDocumentQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveTemplateIdDocument(final Integer idRequisition, final Integer templateIdDocument)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createSaveTemplateIdDocumentNamedParameters(idRequisition, templateIdDocument);
			this.namedjdbcTemplate.update(this.buildsaveTemplateIdDocumentQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	@Override
	public void saveSupplierApprovalDocument(final Integer idRequisition, final Integer supplierApprovalDocument)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createSaveSupplierApprovalDocumentNamedParameters(idRequisition, supplierApprovalDocument);
			this.namedjdbcTemplate.update(this.buildsaveSupplierApprovalDocumentQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteDigitalizationByIdDocument(final Integer idDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createDeleteDigitalizationByIdDocumentNamedParameters(idDocument);
			this.namedjdbcTemplate.update(this.deleteDigitalizationByIdDocumentQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer saveObligation(final Obligation obligation) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obligation);
			namedParameters.registerSqlType(STATUS, Types.VARCHAR);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.createInsertObligationQuery(), namedParameters, keyHolder,
					new String[] { "IdObligation" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Obligation> findObligationsByIdRequisition(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindObligationsByIdRequisitionQuery(), namedParameters,
					new BeanPropertyRowMapper<Obligation>(Obligation.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteObligationsByIdRequisition(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			this.namedjdbcTemplate.update(this.buildDeleteObligationsByIdRequisitionQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<TrayRequisition> findPaginatedTrayRequisitions(final TrayFilter trayFilter,
			final Integer pageNumber, final Integer itemsNumber, final String search) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createfindTrayRequisitionsNamedParameters(trayFilter);
			final String paginatedQuery = this.databaseUtils.buildPaginatedQuerySearch(TableConstants.ID_REQUISITION,
					this.buildFindRequisitionsForTrayQuery(), pageNumber, itemsNumber, search);
			return this.namedjdbcTemplate.query(paginatedQuery, namedParameters,
					new BeanPropertyRowMapper<>(TrayRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Requisition> findInprogressRequisitions(final TrayFilter trayFilter) throws DatabaseException {
		try {
			trayFilter.setStatus(FlowPurchasingEnum.IN_PROGRESS);
			final MapSqlParameterSource namedParameters = this.createfindTrayRequisitionsNamedParameters(trayFilter);
			return this.namedjdbcTemplate.query(this.buildFindInprogressRequisitionsQuery(), namedParameters,
					new BeanPropertyRowMapper<>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildFindInprogressRequisitionsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT SUPPLIER.CompanyName AS SupplierName, DOCUMENTTYPE.Name AS DocumentTypeName, ");
		query.append("REQUISITION.IdRequisition, REQUISITION.ApplicationDate, REQUISITION.Status ");
		query.append(FROM_REQUISITION);
		query.append("LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier  ");
		query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("LEFT JOIN REQUISITION APPLICANT ON APPLICANT.IdRequisition = REQUISITION.IdRequisition WHERE ");
		this.buildInboxFilter(query);
		query.append("REQUISITION.IdApplicant = :IdUser AND REQUISITION.IdFlow = :IdFlow ");
		query.append("AND REQUISITION.Status = 'IN_PROGRESS'");
		return query.toString();
	}

	@Override
	public Long countTotalRowsForTray(final TrayFilter trayFilter, String search) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.createfindTrayRequisitionsNamedParameters(trayFilter);
			final String countRows = this.databaseUtils.countTotalRowsSearch(this.buildFindRequisitionsForTrayQuery(), search);
			return this.namedjdbcTemplate.queryForObject(countRows, parameterSource, Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveUserVobo(final Integer idRequisition, final Integer idUser) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createSaveUserVoboNamedParameters(idRequisition, idUser);
			this.namedjdbcTemplate.update(this.buildSaveUserVoboQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Boolean findIsAllUsersVobo(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.buildFindIsAllUsersVoboQuery(), namedParameters,
					Boolean.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequisitionStatusTurn(final Integer idRequisition, final FlowPurchasingEnum status)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createRequisitionChangeStatusNamedParameters(idRequisition, status);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionStatusTurnQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer findCurrentTurnByIdRequisition(final Integer idRequisition) throws DatabaseException {// ***************
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.buildFindCurrentTurnByIdRequisitionQuery(),
					namedParameters, Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	public void deleteRequisitionStatusTurns(final Integer idRequisition) {
		final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
		this.namedjdbcTemplate.update(this.buildDeleteRequisitionStatusTurnsQuery(), namedParameters);
	}

	public Boolean isUserVoboGiven(final Integer idRequisition, final Integer idUser) {
		final MapSqlParameterSource namedParameters = this.createSaveUserVoboNamedParameters(idRequisition, idUser);
		return this.namedjdbcTemplate.queryForObject(this.buildIsUserVoboGivenQuery(), namedParameters, Boolean.class);
	}

	@Override
	public List<Requisition> findRequisitionByFlow(final Integer idFlow) throws DatabaseException {
		try {
			final MapSqlParameterSource namSource = new MapSqlParameterSource();
			namSource.addValue(TableConstants.ID_FLOW, idFlow);
			return this.namedjdbcTemplate.query(this.buildSelectAllByFlowQuery(), namSource,
					new BeanPropertyRowMapper<Requisition>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveApplicant(final Integer idRequisition, final Integer idApplicant) throws DatabaseException {
		try {
			final MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue(TableConstants.ID_APPLICANT, idApplicant);
			source.addValue(TableConstants.ID_REQUISITION, idRequisition);
			this.namedjdbcTemplate.update(this.buildUpdateApplicantQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveLawyer(final Integer idRequisition, final Integer idLawyer) throws DatabaseException {
		try {
			final MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue(TableConstants.ID_LAWYER, idLawyer);
			source.addValue(TableConstants.ID_REQUISITION, idRequisition);
			this.namedjdbcTemplate.update(this.buildUpdateLawyerQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Requisition> findRequisitionByManyParameters(final Requisition requisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.createFindRequisitionByManyParameters(requisition);
			return this.namedjdbcTemplate.query(this.buildFindRequisitionByParamenterQuery(), parameterSource,
					new BeanPropertyRowMapper<Requisition>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Requisition> findRequisitionClosed(final Requisition requisition,
			final Integer beforeExpirationDays, final Integer afterExpirationDate) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.createRequisitionFiltersAndExpirationNamedParameters(
					requisition, beforeExpirationDays, afterExpirationDate);
			return this.namedjdbcTemplate.query(this.buildFindRequisitionClosedQuery(), parameterSource,
					new BeanPropertyRowMapper<Requisition>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource createRequisitionFiltersAndExpirationNamedParameters(final Requisition requisition,
			final Integer beforeExpirationDays, final Integer afterExpirationDate) {
		final MapSqlParameterSource parameterSource = this.createFindRequisitionByManyParameters(requisition);
		parameterSource.addValue("BeforeExpirationDate", beforeExpirationDays);
		parameterSource.addValue("AfterExpirationDate", afterExpirationDate);
		return parameterSource;
	}

	@Override
	public List<Requisition> findPaginatedRequisitionsClosed(final Requisition requisition,
			final Integer beforeExpirationDays, final Integer afterExpirationDate, final Integer pageNumber,
			final Integer itemsNumber) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.createRequisitionFiltersAndExpirationNamedParameters(
					requisition, beforeExpirationDays, afterExpirationDate);
			final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUISITION,
					this.buildFindRequisitionClosedQuery(), pageNumber, itemsNumber);
			return this.namedjdbcTemplate.query(paginatedQuery, parameterSource,
					new BeanPropertyRowMapper<>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Requisition> findPaginatedContracts(final Requisition requisition, final Integer pageNumber,
			final Integer itemsNumber) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.createFindRequisitionByManyParameters(requisition);
			final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUISITION,
					this.buildFindContractsQuery(), pageNumber, itemsNumber);
			return this.namedjdbcTemplate.query(paginatedQuery, parameterSource,
					new BeanPropertyRowMapper<>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Long countTotalRowsRequisitionsClosed(final Requisition requisition,
			final Integer beforeExpirationDays, final Integer afterExpirationDate) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.createRequisitionFiltersAndExpirationNamedParameters(
					requisition, beforeExpirationDays, afterExpirationDate);
			final String countRows = this.databaseUtils.countTotalRows(this.buildFindRequisitionClosedQuery());
			return this.namedjdbcTemplate.queryForObject(countRows, parameterSource, Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void changeAttendStatus(final Integer idRequisition, final Boolean isExpiredAttended)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
			namedParameters.addValue(TableConstants.IS_EXPIRED_ATTENDED, isExpiredAttended);
			this.namedjdbcTemplate.update(this.changeAttendStatusQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al cambiar el estatus de atendido", dataAccessException);
		}
	}

	@Override
	public void saveRequisitionStatusTurnAttentionDaysAndStage(final RequisitionStatusTurn requisitionStatusTurn)
			throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
					requisitionStatusTurn);
			namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
			this.namedjdbcTemplate.update(this.buildSaveRequisitionStatusTurnAttentionDaysAndStageQuery(),
					namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<RequisitionStatusTurn> findRequisitionStatusTurnsByIdRequisition(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindRequisitionStatusTurnsByIdRequisitionQuery(),
					namedParameters, new BeanPropertyRowMapper<>(RequisitionStatusTurn.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Requisition> findClosedRequisitionUnattended(final Integer beforeExpirationDate,
			final Integer afteExpirationDate) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue("BeforeExpirationDate", beforeExpirationDate);
			parameterSource.addValue("AfterExpirationDate", afteExpirationDate);
			return this.namedjdbcTemplate.query(this.buildFindCloseRequisitionQuery(), parameterSource,
					new BeanPropertyRowMapper<Requisition>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Requisition> findPaginatedRequisitionsManagement(final Requisition requisition,
			final Integer pageNumber, final Integer itemsNumber) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.createFindRequisitionByManyParameters(requisition);
			System.out.println("Query admin folios: " + this.buildFindRequisitionByParamenterQuery2());
			final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUISITION,
					this.buildFindRequisitionByParamenterQuery2(), pageNumber, itemsNumber);
			return this.namedjdbcTemplate.query(paginatedQuery, parameterSource,
					new BeanPropertyRowMapper<>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Long countTotalRowsRequisitionsManagement(final Requisition requisition) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.createFindRequisitionByManyParameters(requisition);
			final String countRows = this.databaseUtils.countTotalRows(this.buildFindRequisitionByParamenterQuery());
			return this.namedjdbcTemplate.queryForObject(countRows, parameterSource, Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Long countTotalRowsOfContracts(final Requisition requisition) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.createFindRequisitionByManyParameters(requisition);
			final String countRows = this.databaseUtils.countTotalRows(this.buildFindContractsQuery());
			return this.namedjdbcTemplate.queryForObject(countRows, parameterSource, Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String changeAttendStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("IsExpiredAttended = :IsExpiredAttended ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildFindCloseRequisitionQuery() {
		final StringBuilder builder = new StringBuilder();
		final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
		final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
		builder.append("SELECT IdRequisition,DOCUMENTTYPE.Name AS DocumentTypeName,USERS.Email AS EmailApplicant, ");
		builder.append("CONCAT(USERS.Name,' ',USERS.FirstLastName,' ',USERS.SecondLastName) AS FullNameApplicant, ");
		builder.append("ValidityEndDate, CASE WHEN ").append(formatedTodayDate).append(" BETWEEN ");
		builder.append(this.databaseUtils.buildDateAddDays(":BeforeExpirationDate", TableConstants.VALIDITY_END_DATE));
		builder.append(" AND ValidityEndDate THEN 'before' ELSE 'after' END AS DateValue ");
		builder.append("FROM REQUISITION INNER JOIN USERS ON REQUISITION.IdApplicant = USERS.IdUser ");
		builder.append("INNER JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		builder.append("WHERE REQUISITION.Status = 'REQUISITION_CLOSE' AND REQUISITION.ValidityEndDate IS NOT NULL ");
		builder.append("AND REQUISITION.IsExpiredAttended != 1 ");
		builder.append("AND ").append(formatedTodayDate).append(" BETWEEN ");
		builder.append(this.databaseUtils.buildDateAddDays(":BeforeExpirationDate", TableConstants.VALIDITY_END_DATE));
		builder.append(" AND ")
		.append(this.databaseUtils.buildDateAddDays(":AfterExpirationDate", TableConstants.VALIDITY_END_DATE))
		.append(ORDER_BY_ID);
		return builder.toString();
	}

	private String buildFindRequisitionsToCreateOneFromAnotherQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT R.IdRequisition FROM REQUISITION R ");
		query.append("INNER JOIN DOCUMENTTYPE DT ON R.IdDocumentType = DT.IdDocumentType ");
		query.append("INNER JOIN SUPPLIER S ON R.IdSupplier = S.IdSupplier ");
		query.append("WHERE ((:IdRequisitionNull IS NULL)  OR (R.IdRequisition LIKE :IdRequisition)) AND ");
		query.append("((:DocumentTypeNull IS NULL )  OR (DT.Name LIKE :DocumentType)) AND ");
		/** GAO se modifica por problema con el filtro */
		// query.append("((:SupplierNameNull IS NULL) OR (S.CommercialName LIKE
		// :SupplierName) ");
		query.append("((:SupplierNameNull IS NULL) OR (S.CompanyName LIKE :SupplierName) ");
		query.append("OR (S.CompanyName LIKE :SupplierName)) ");
		return query.toString();
	}
	
	private String buildFindAllRequisitionsQuery() {
		final StringBuilder query = new StringBuilder();
		    query.append("SELECT R.IdRequisition,R.ApplicationDate,DT.Name, ");
			query.append(" FINANCIALENTITY.Name AS supplierCompanyName, SCREEN.Name As FlowStatus,SUPPLIER.CompanyName AS SupplierName, ");
			query.append("(SELECT DISTINCT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = R.IdRequisition order by TurnDate DESC) as LastDateModify ,");
//			query.append("CONCAT (USERS.Name, ' ' , USERS.FirstLastName, ' ', USERS.SecondLastName) As LawyerName,");
			query.append("CONCAT (USERS.Name, ' ' , USERS.FirstLastName, ' ', USERS.SecondLastName) As fullName,");
			query.append("CASE ");
			query.append("WHEN SUPPLIER.IdPersonality =1 THEN SUPPLIER.CompanyName " );
			query.append("WHEN SUPPLIER.IdPersonality =2 THEN SUPPLIER.CommercialName " );
			query.append("WHEN SUPPLIER.IdPersonality =3 THEN SUPPLIER.CompanyName " );
			query.append("WHEN SUPPLIER.IdPersonality =4 THEN SUPPLIER.CommercialName " );
			query.append("ELSE SUPPLIER.CompanyName ");
			query.append("END as supplierName, " );
			query.append("SUPPLIER.Rfc AS RFC, a.Name as areaName ,DT.Name AS DocumentTypeName FROM REQUISITION R ");
//	        query.append("INNER JOIN DOCUMENTTYPE DT ON R.IdDocumentType = DT.IdDocumentType ");
//	        query.append("INNER JOIN SUPPLIER S ON R.IdSupplier = S.IdSupplier ");
//	        query.append("INNER JOIN USERS ON R.IdLawyer = USERS.IdUser ");
			query.append(
					" LEFT JOIN REQUISITIONFINANCIALENTITY ON REQUISITIONFINANCIALENTITY.IdRequisition =  R.IdRequisition ");
			query.append(
					" LEFT JOIN FINANCIALENTITY ON FINANCIALENTITY.IdFinancialEntity = REQUISITIONFINANCIALENTITY.IdFinancialEntity ");
	        query.append("LEFT JOIN DOCUMENTTYPE DT ON R.IdDocumentType = DT.IdDocumentType ");
	        query.append("LEFT JOIN SUPPLIER S ON R.IdSupplier = S.IdSupplier ");
//	        query.append("LEFT JOIN USERS ON R.IdLawyer = USERS.IdUser ");
	        query.append("LEFT JOIN USERS ON R.IdApplicant = USERS.IdUser ");
	        query.append(" LEFT JOIN SUPPLIER ON R.IdSupplier = SUPPLIER.IdSupplier ");
	        query.append("LEFT JOIN SCREEN ON R.Status = SCREEN.FlowStatus ");
	        query.append("LEFT JOIN AREA a on a.IdArea = R.IdAreaTender ");
	        query.append("WHERE ((:IdRequisitionNull IS NULL)  OR (R.IdRequisition LIKE :IdRequisition)) ");
//	        query.append("WHERE ((:IdRequisitionNull IS NULL)  OR (R.IdRequisition LIKE :IdRequisition)) AND ");
//	        query.append("((:DocumentTypeNull IS NULL )  OR (DT.Name LIKE :DocumentType)) AND ");
//	        query.append("((:SupplierNameNull IS NULL) OR (S.CompanyName LIKE :SupplierName)) ");
//	        query.append("OR (S.CompanyName LIKE :SupplierName)) ");
//	        query.append("WHERE (R.IdRequisition LIKE :IdRequisition) OR ");
//	        query.append("(DT.Name LIKE :DocumentType) OR ");
//	        query.append("(S.CompanyName LIKE :SupplierName) ");
//	        query.append(" LIMIT :regLimit OFFSET :rowInicio");
//	        query.append("ORDER BY R.IdRequisition DESC ");
	        query.append("ORDER BY R.IdRequisition ASC ");
		return query.toString();
	}
	private String buildFindRequisitionByParamenterQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append("WHERE ((:IdRequisition IS NULL)  OR (REQUISITION.IdRequisition = :IdRequisition)) AND ");
		query.append("((:DocumentTypeNameNull IS NULL )  OR ");
		query.append("(DOCUMENTTYPE.Name LIKE :DocumentTypeName)) AND ");
		query.append("((:RfcNull IS NULL) OR (Rfc LIKE :Rfc)) AND ");
		/** GAO se modifica por problema con el filtro */
		// query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CommercialName LIKE
		// :SupplierName)) AND ");
		query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CompanyName LIKE :SupplierName)) AND ");
		query.append("((:FlowStatusNull IS NULL) OR (SCREEN.Name LIKE :FlowStatus)) AND ");
		query.append("((:StartDate IS NULL OR :EndDate IS NULL) ");
		query.append("OR CAST(ApplicationDate AS DATE) BETWEEN :StartDate AND :EndDate) AND ");
		query.append("IdFlow = :IdFlow AND REQUISITION.Status NOT IN('REQUISITION_CLOSE','RENEWED') ");
		query.append("ORDER BY REQUISITION.IdRequisition DESC ");
		return query.toString();
	}

	private String buildFindRequisitionByParamenterQuery2() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery2(query);
		query.append("WHERE ((:IdRequisition IS NULL)  OR (REQUISITION.IdRequisition = :IdRequisition)) AND ");
		query.append("((:DocumentTypeNameNull IS NULL )  OR ");
		query.append("(DOCUMENTTYPE.Name LIKE :DocumentTypeName)) AND ");
		query.append(
				"((:LawyerNameNull IS NULL) OR ((CONCAT (USERS.Name, ' ' , USERS.FirstLastName, ' ', USERS.SecondLastName)) LIKE :LawyerName)) AND ");
		query.append("((:RfcNull IS NULL) OR (Rfc LIKE :Rfc)) AND ");
		/** GAO se modifica por problema con el filtro */
		// query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CommercialName LIKE
		// :SupplierName)) AND ");
		query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CompanyName LIKE :SupplierName)) AND ");
		query.append("((:FlowStatusNull IS NULL) OR (SCREEN.Name LIKE :FlowStatus)) AND ");
		query.append("((:StartDate IS NULL OR :EndDate IS NULL) ");
		query.append("OR CAST(ApplicationDate AS DATE) BETWEEN :StartDate AND :EndDate) AND ");
		query.append("IdFlow = :IdFlow AND REQUISITION.Status NOT IN('REQUISITION_CLOSE','RENEWED') ");
		query.append("ORDER BY REQUISITION.IdRequisition DESC ");
		return query.toString();
	}

	private String buildFindRequisitionClosedQuery() {
		final StringBuilder query = new StringBuilder();
		final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
		final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
		query.append("SELECT SUPPLIER.CompanyName AS SupplierName, SUPPLIER.Rfc AS RFC, DOCUMENTTYPE.Name ");
		query.append("AS DocumentTypeName, REQUISITION.IdRequisition, ApplicationDate, IsExpiredAttended, ");
		query.append("ValidityEndDate, RST.TurnDate AS ClosedDate ");
		query.append("FROM REQUISITION INNER JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
		query.append("INNER JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("INNER JOIN REQUISITIONSTATUSTURN RST ON REQUISITION.IdRequisition = RST.IdRequisition ");
		query.append("AND REQUISITION.Status = RST.Status ");
		query.append("WHERE ((:IdRequisition IS NULL)  OR (REQUISITION.IdRequisition = :IdRequisition)) AND ");
		query.append("((:DocumentTypeNameNull IS NULL )  OR ");
		query.append("(DOCUMENTTYPE.Name LIKE :DocumentTypeName)) AND ");
		query.append("((:RfcNull IS NULL) OR (Rfc LIKE :Rfc)) AND ");
		/** GAO Se cambio por broncas en el filtro */
		// query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CommercialName LIKE
		// :SupplierName)) AND ");
		query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CompanyName LIKE :SupplierName)) AND ");
		query.append("((:StartDate IS NULL OR :EndDate IS NULL) ");
		query.append("OR CAST(ApplicationDate AS DATE) BETWEEN :StartDate AND :EndDate ");
		query.append("OR ValidityEndDate BETWEEN :StartDate AND :EndDate ");
		query.append("OR CAST(RST.TurnDate AS DATE) BETWEEN :StartDate AND :EndDate) AND ");
		query.append("IdFlow = :IdFlow AND REQUISITION.Status = 'REQUISITION_CLOSE' AND NOT ValidityEndDate IS null ");
		query.append("AND ").append(formatedTodayDate).append(" BETWEEN ");
		query.append(this.databaseUtils.buildDateAddDays(":BeforeExpirationDate", TableConstants.VALIDITY_END_DATE));
		query.append(" AND ")
		.append(this.databaseUtils.buildDateAddDays(":AfterExpirationDate", TableConstants.VALIDITY_END_DATE))
		.append("ORDER BY REQUISITION.IdRequisition DESC ");
		return query.toString();
	}

	private String buildFindContractsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT SUPPLIER.CompanyName AS SupplierName, SUPPLIER.Rfc AS RFC, DOCUMENTTYPE.Name ");
		query.append("AS DocumentTypeName, IdRequisition, ValidityStartDate, ValidityEndDate, REQUISITION.Status ");
		query.append("AS Status FROM REQUISITION INNER JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
		query.append("INNER JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("WHERE ((:IdRequisition IS NULL) OR (IdRequisition = :IdRequisition)) AND ");
		query.append("((:DocumentTypeNameNull IS NULL ) OR (DOCUMENTTYPE.Name LIKE :DocumentTypeName)) AND ");
		query.append("((:RfcNull IS NULL) OR (Rfc LIKE :Rfc)) AND ");
		/** GAO se quita por broncas con el filtro */
		// query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CommercialName LIKE
		// :SupplierName)) AND ");
		query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CompanyName LIKE :SupplierName)) AND ");
		query.append("((:StartDate IS NULL OR :EndDate IS NULL) ");
		query.append("OR CAST(ApplicationDate AS DATE) BETWEEN :StartDate AND :EndDate) AND ");
		query.append("IdFlow = :IdFlow AND REQUISITION.Status IN ('REQUISITION_CLOSE','CANCELED_CONTRACT') ");
		query.append(ORDER_BY_ID);
		return query.toString();
	}

	private MapSqlParameterSource createFindRequisitionByManyParameters(final Requisition requisition) {
		final MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue(TableConstants.ID_REQUISITION, requisition.getIdRequisition());
		source.addValue(TableConstants.DOCUMENT_TYPE_NAME, LIKE + requisition.getDocumentTypeName() + LIKE);
		source.addValue(TableConstants.DOCUMENT_TYPE_NAME + NULL, requisition.getDocumentTypeName());
		source.addValue(TableConstants.LAWYER_NAME, LIKE + requisition.getLawyerName() + LIKE);
		source.addValue(TableConstants.LAWYER_NAME + NULL, requisition.getLawyerName());
		source.addValue(TableConstants.RFC, LIKE + requisition.getRfc() + LIKE);
		source.addValue(TableConstants.RFC + NULL, requisition.getRfc());
		source.addValue(TableConstants.SUPPLIER_NAME, LIKE + requisition.getSupplierName() + LIKE);
		source.addValue(TableConstants.SUPPLIER_NAME + NULL, requisition.getSupplierName());
		source.addValue(TableConstants.FLOW_STATUS, requisition.getFlowStatus());
		source.addValue(TableConstants.FLOW_STATUS + NULL, requisition.getFlowStatus());
		source.addValue(TableConstants.START_DATE, requisition.getStarDate());
		source.registerSqlType(TableConstants.START_DATE, Types.VARCHAR);
		source.addValue(TableConstants.END_DATE, requisition.getEndDate());
		source.registerSqlType(TableConstants.END_DATE, Types.VARCHAR);
		source.addValue(TableConstants.ID_FLOW, requisition.getIdFlow());

		System.out.println("PARAMETROS DocumentTypeName: " + requisition.getDocumentTypeName());
		System.out.println("PARAMETROS LAWYER NAME: " + requisition.getLawyerName());
		System.out.println("PARAMETROS RFC: " + requisition.getRfc());
		System.out.println("PARAMETROS SupplierName: " + requisition.getSupplierName());
        System.out.println("PARAMETROS: " + source.toString());
        System.out.println(" -- PARAMETROS: ID FLOW:  " + requisition.getIdFlow());
		return source;
	}

	private String buildUpdateApplicantQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("IdApplicant = :IdApplicant ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildUpdateLawyerQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("IdLawyer = :IdLawyer ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildSelectAllByFlowQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append(WHERE_ID_FLOW_EQUALS_ID_FLOW);
		query.append(ORDER_BY_ID);
		return query.toString();

	}

	private MapSqlParameterSource createSaveDigitalizationIdDocumentNamedParameters(final Integer idRequisition,
			final Integer digitalizationIdDocument) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.DIGITALIZATION_ID_DOCUMENT, digitalizationIdDocument);
		return namedParameters;
	}

	private String buildSaveDigitalizationIdDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITIONDIGITALIZATION (IdRequisition, DigitalizationIdDocument) ");
		query.append("VALUES (:IdRequisition, :DigitalizationIdDocument)");
		return query.toString();
	}

	private String buildFindHistoryDocumentsVersionsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdDocument FROM SUPPLIERREQUIREDDOCUMENT ");
		query.append("WHERE IdSupplier =:IdSupplier ");
		query.append("AND IdRequiredDocument =:IdRequiredDocument");
		return query.toString();
	}

	private String buildSaveRequisitionLegalRepresentativeSignedContractDataQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITIONLEGALREPRESENTATIVE_SET);
		query.append("IsOriginalSignedContDelivered = :IsOriginalSignedContDelivered, ");
		query.append("IsCopySignedContractDelivered = :IsCopySignedContractDelivered, ");
		query.append("SignedContractSendDate = :SignedContractSendDate ");
		query.append(WHERE_ID_REQUISITION_EQUALS_AND_ID_LEGAL_REPRESENTATIVE);
		return query.toString();
	}

	private MapSqlParameterSource createSaveRequisitionLegalRepresentativeSignedContractDataNamedParameters(
			final Integer idRequisition, final LegalRepresentative legalRepresentative) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.ID_LEGALREPRESENTATIVE, legalRepresentative.getIdLegalRepresentative());
		namedParameters.addValue(TableConstants.IS_ORIGINAL_SIGNED_CONTRACT_DELIVERED,
				legalRepresentative.getIsOriginalSignedContDelivered());
		namedParameters.addValue(TableConstants.IS_COPY_SIGNED_CONTRACT_DELIVERED,
				legalRepresentative.getIsCopySignedContractDelivered());
		namedParameters.addValue(TableConstants.SIGNED_CONTRACT_SEND_DATE,
				legalRepresentative.getSignedContractSendDate());
		return namedParameters;
	}

	private String buildSaveRequisitionSignedContractDataQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("IsOrigSignCntrDelvrdSupplier = :IsOrigSignCntrDelvrdSupplier, ");
		query.append("IsCpySignCntrDelvrdSupplier = :IsCpySignCntrDelvrdSupplier, ");
		query.append("SignedContractSendDateSupplier = :SignedContractSendDateSupplier, ");
		query.append("IsOrigSignCntrDelvrdWitnesses = :IsOrigSignCntrDelvrdWitnesses, ");
		query.append("IsCpySignCntrDelvrdWitnesses = :IsCpySignCntrDelvrdWitnesses, ");
		query.append("SigndContractSendDateWitnesses = :SigndContractSendDateWitnesses, ");
		query.append("IsOrigSignCntrDelvrdRegistry = :IsOrigSignCntrDelvrdRegistry, ");
		query.append("IsCpySignCntrDelvrdRegistry = :IsCpySignCntrDelvrdRegistry, ");
		query.append("SignedContractSendDateRegistry = :SignedContractSendDateRegistry, ");
		query.append("IsOrigSignCntrDelvrdLegal = :IsOrigSignCntrDelvrdLegal, ");
		query.append("IsCpySignCntrDelvrdLegal = :IsCpySignCntrDelvrdLegal, ");
		query.append("SignedContractSendDateLegal = :SignedContractSendDateLegal ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildSaveProviderAndWitnessesSignDatesQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("SupplierSignSendDate = :SupplierSignSendDate, WitnessesSignSendDate = :WitnessesSignSendDate, ");
		query.append("SupplierSignReturnDate = :SupplierSignReturnDate, ");
		query.append("WitnessesSignReturnDate = :WitnessesSignReturnDate ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildSaveRequisitionEvaluatorQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("IdEvaluator = :IdEvaluator ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	public void saveContractDraftFields(final Requisition requisition) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(requisition);
			this.namedjdbcTemplate.update(this.buildSaveContractDraftFieldsQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildSaveRequisitionAttatchmentsFieldsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append(JdbcTemplateUtils.buildUpdateFields(this.buildAttatchmentsFields(), COMMA));
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	public Integer countByIdRequisition(final Integer idRequisition) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		return this.namedjdbcTemplate.queryForObject(this.buildCountByIdRequisition(), namedParameters, Integer.class);
	}

	private String buildCountByIdRequisition() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(IdRequisition) FROM REQUISITIONFINANCIALENTITY ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildInsertRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITION (");
		this.buildAllNonPrimaryOrDefaultFields(query);
		query.append(") VALUES (:IdSupplier, :IdDocumentType, :IdFlow,  GETDATE(), :UpdateRequisitionBy, ");
		query.append(JdbcTemplateUtils.tagFields(this.buildNonTableSpecifiedPrimaryKeyFields(), COMMA));
		query.append(RIGHT_BRACES);
		return query.toString();
	}

	private String buildInsertRequisitionQuery2() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITION (");
		this.buildAllNonPrimaryOrDefaultFields1(query);
		query.append(") VALUES ( :IdApplicant, :IdDocument, :IdFlow, GETDATE(), :UpdateRequisitionBy, ");
		query.append(" :IdArea, :IdUnit,  :Contract, :ContractType, :IdDocumentType ");
		// query.append(JdbcTemplateUtils.tagFields(this.buildNonTableSpecifiedPrimaryKeyFields(),
		// COMMA));
		query.append(RIGHT_BRACES);
		return query.toString();
	}

	private void buildAllNonPrimaryOrDefaultFields(final StringBuilder query) {
		query.append(
				"REQUISITION.IdSupplier, REQUISITION.IdDocument, IdFlow, UpdateRequisitionDate, UpdateRequisitionBy, ");
		query.append(this.buildNonTableSpecifiedPrimaryKeyFields());
	}

	private void buildAllNonPrimaryOrDefaultFields1(final StringBuilder query) {
		query.append(
				"REQUISITION.IdApplicant, REQUISITION.IdDocument, IdFlow, UpdateRequisitionDate, UpdateRequisitionBy, ");
		query.append(
				" REQUISITION.IdAreaTender, REQUISITION.IdUnit,  REQUISITION.Contract, REQUISITION.ContractType, REQUISITION.IdDocumentType ");

		// query.append(this.buildNonTableSpecifiedPrimaryKeyFields());
	}

	  private String buildNonTableSpecifiedPrimaryKeyFields() {
	        final StringBuilder query = new StringBuilder();
	        query.append("IdApplicant, IdLawyer, IdEvaluator,REQUISITION.IdArea, ");
	        query.append("IdAreaTender, AuthorizationDocumentName, AuthorizationDocumentIdDoc, ");
	        query.append(this.buildAttatchmentsFields());
	        query.append("TechnicalDetails, Validity, ServiceDescription, TemplateIdDocument, ");
	        query.append("AutomaticRenewal, RenewalPeriods, ValidityStartDate, ValidityEndDate, ValidityClause, ");
	        this.buildClausulesSectionFields(query);
	        this.buildDeclarationsSectionFields(query);
	        query.append("SupplierSignSendDate, WitnessesSignSendDate, SupplierSignReturnDate, WitnessesSignReturnDate, ");
	        query.append("IsOrigSignCntrDelvrdSupplier, IsCpySignCntrDelvrdSupplier, ");
	        query.append("SignedContractSendDateSupplier, IsOrigSignCntrDelvrdWitnesses, ");
	        query.append("IsCpySignCntrDelvrdWitnesses, SigndContractSendDateWitnesses, ");
	        query.append("IsOrigSignCntrDelvrdRegistry, IsCpySignCntrDelvrdRegistry, ");
	        query.append("IsOrigSignCntrDelvrdLegal, IsCpySignCntrDelvrdLegal, SignedContractSendDateLegal, ");
	        query.append("SignedContractSendDateRegistry, SupplierApprovalIdDocument, activeActor, passiveActor, ");
	        query.append(
				"CancellationDate, InitialAdvanceAmount,CesionDerechos,PrcentajePenaConvencional,ImportePolizaSeguro,ObligacionesEspecificas, ")
				.append(this.setRequisitionFields());
	        return query.toString();
	    }

	private String setRequisitionFields() {
		final StringBuilder query = new StringBuilder();
		query.append("CustomsAgentPatentNumber, CustomsState, FinancialEntityBranchAddress, ");
		query.append(
				"ContractValidityMonths, ConventionalPenaltyPercentage, DepositsRealizedMonthsNumber, SignState, ");
		query.append("CondosNumber, ConsiderationAmount, ProemName, SignDate, ");
		query.append("TotalAmountForServicesRendered, BillingCycle, PaymentCycle, ");
		query.append("StandardizedKeyBankingFinancialEntity, TransferDay, DaysNoticeForEarlyTermination, ");
		query.append("NominativeCheckDeliveryDay, BusinessDaysAcceptRejectPurchaseOrder, CalendarDayOfDeliveryDate, ");
		query.append(
				"BusinessDaysToModifyCancelOrders, CalendarDaysToWithdrawContract, DaysForAnomalyDeliveryReport, ");
		query.append(
				"DaysDeadlineForPayment, FinancialEntityDirection, FinancialEntityAddress, ServiceLocationState, ");
		query.append("ConstructionStagesEndDate, WorksEndDate, ConstructionStagesStartDate, WorksStartDate, ");
		query.append("PaymentMethodSubscribers, AmountOfInsuranceForDamageOrLoss, TotalNetAmountOfWorkDone, ");
		query.append("SupplierIMMS, BankingInstitution, DeliveryMonthNominativeCheck, TransferMonth, ");
		query.append("GracePeriodMonths, SupplierNationality, SquareName, PersonNameSendDailySalesReports, ");
		query.append(
				"PersonMailSendDailySalesReports, FractionationName, EmployeesNumber, SupplierLegalRepresentativePosition, ");
		query.append(
				"FrameContractNumberTelecomServices, AccountNumberFinancial, StepsBuildNumber, PremisesInTheSquareNumber, ");
		query.append("EquivalentDepositsMonthsNumber, CurrencyCountry, PropertyDeliveryPeriod, RentEquivalent, ");
		query.append("TotalPaymentPercentajeAmountTotal, Surface, CurrencyType, ContractValidity, ");
		query.append("MegacableServiceSupplierProvided, ProviderServiceMegacableProvided, SurfaceStoreMerchandise, ");
		query.append("MegacableObligationsPaymentPercentageExchange, SellerObligationsPaymentPercentageExchange, ");
		query.append("Surveying, StrokeStreet, Digitalization, NetworkCopy, NetworkGPON, FiberCopy, SurveyingCost, ");
		query.append("StrokeStreetCost, DigitalizationCost, NetworkCopyCost, NetworkGPONCost, FiberCopyCost, ");
		query.append("DeliveryCalendarDays, ReimbursementTerminationCalendarDays, PropertyBusinessLine, ");
		query.append("FinancialEntityAtention, RepresentativeSocietyName, ServiceSchedule, ");
		query.append("SupplierAtention, SupplierPhone, SupplierAccountNumber, DepositAmount, ");
		query.append("Facturation, ActReceptionService, EthernetPrivateLine, Vpn, ");
		query.append("DedicatedInternet, DigitalPrivateLine, Infrastructure, VideoPrivateLine, ");
		query.append("Trunk, OtherServices, CableTv, BusinessInternet, ");
		query.append("BusinessTelephony, Intranet, TecnologySolutions, GoogleServiceCloud, ");
		query.append("NegotiatorRepresentativeName, MetrocarrierSquareAddress, FrameworkContractSingDate, ");
		query.append("BillingContactName, BillingPosition, BillingEmail, BillingPhone, BillingExtension, BillingFax, ");
		query.append("TechnitianContactName, TechnitianPosition, TechnitianEmail, TechnitianPhone, ");
		query.append("TechnitianExtension, TechnitianFax, ServiceSellerName, SpaceServiceGranted ");
		return query.toString();
	}

	private String setDraftFields() {
		final StringBuilder query = new StringBuilder();
		query.append("CustomsAgentPatentNumber, CustomsState, FinancialEntityBranchAddress, ProemName, ");
		query.append("CondosNumber, Volume, BookNumber, TowerNameProperty, PropertyAddress, Property, SignDate, ");
		query.append("TotalAmountForServicesRendered, BillingCycle, PaymentCycle, ConsiderationAmount, ");
		query.append("StandardizedKeyBankingFinancialEntity, TransferDay, DaysNoticeForEarlyTermination, ");
		query.append("NominativeCheckDeliveryDay, BusinessDaysAcceptRejectPurchaseOrder, CalendarDayOfDeliveryDate, ");
		query.append("BusinessDaysToModifyCancelOrders, CalendarDaysToWithdrawContract, DaysForAnomalyDeliveryReport,");
		query.append(
				"DaysDeadlineForPayment, FinancialEntityDirection, FinancialEntityAddress, ServiceLocationState, ");
		query.append("ConstructionStagesEndDate, WorksEndDate, ConstructionStagesStartDate, WorksStartDate, ");
		query.append("PaymentMethodSubscribers, AmountOfInsuranceForDamageOrLoss, TotalNetAmountOfWorkDone, ");
		query.append("SupplierIMMS, BankingInstitution, DeliveryMonthNominativeCheck, TransferMonth, ");
		query.append("GracePeriodMonths, SupplierNationality, SquareName, PersonNameSendDailySalesReports, ");
		query.append("PersonMailSendDailySalesReports, FractionationName, EmployeesNumber, PropertyBusinessLine, ");
		query.append(
				"FrameContractNumberTelecomServices, AccountNumberFinancial, StepsBuildNumber, PremisesInTheSquareNumber, ");
		query.append(
				"EquivalentDepositsMonthsNumber, CurrencyCountry, PropertyDeliveryPeriod, SupplierLegalRepresentativePosition, ");
		query.append("TotalPaymentPercentajeAmountTotal, Surface, CurrencyType, ContractValidity, RentEquivalent, ");
		query.append("MegacableServiceSupplierProvided, ProviderServiceMegacableProvided, SurfaceStoreMerchandise, ");
		query.append("MegacableObligationsPaymentPercentageExchange, SellerObligationsPaymentPercentageExchange, ");
		query.append("Surveying, StrokeStreet, Digitalization, NetworkCopy, NetworkGPON, FiberCopy, SurveyingCost, ");
		query.append("StrokeStreetCost, DigitalizationCost, NetworkCopyCost, NetworkGPONCost, FiberCopyCost, ");
		query.append("FinancialEntityAtention, RepresentativeSocietyName, ServiceSchedule, ");
		query.append("SupplierAtention, SupplierPhone, SupplierAccountNumber, DepositAmount, ");
		query.append("Facturation, ActReceptionService, EthernetPrivateLine, Vpn, ");
		query.append("DedicatedInternet, DigitalPrivateLine, Infrastructure, VideoPrivateLine, ");
		query.append("Trunk, OtherServices, CableTv, BusinessInternet, ");
		query.append("BusinessTelephony, Intranet, TecnologySolutions, GoogleServiceCloud, ");
		query.append("NegotiatorRepresentativeName, MetrocarrierSquareAddress, FrameworkContractSingDate, ");
		query.append("BillingContactName, BillingPosition, BillingEmail, BillingPhone, BillingExtension, BillingFax, ");
		query.append("TechnitianContactName, TechnitianPosition, TechnitianEmail, TechnitianPhone, ");
		query.append("TechnitianExtension, TechnitianFax, ServiceSellerName, SpaceServiceGranted ");
		return query.toString();
	}

	private String buildAttatchmentsFields() {
		final StringBuilder query = new StringBuilder();
		query.append("AttatchmentDeliverables, AttchmtServiceLevelsMeasuring, AttatchmentPenalty, ");
		query.append("AttatchmentScalingMatrix, AttatchmentCompensation, AttchmtBusinessMonitoringPlan, ");
		query.append("BusinessReasonMonitoringPlan, AttchmtImssInfoDeliveryReqrmts, ");
		query.append("AttatchmentInformationSecurity, AttatchmentOthers, AttatchmentOthersName, ");
		return query.toString();
	}

    private void buildClausulesSectionFields(final StringBuilder query) {
        query.append(" ContractObjectClause, ConsiderationClause, Clabe, ContractDurationDate, ");
        query.append("ConsiderationInitialReport, ConsiderationMonthlyReport, ConsiderExtraordinaryReport, ");
        query.append("InitialPaymentPercentage, InitialPaymentPeriod, MonthlyPaymentPercentage, fechaTerminacionContratoaFinalizar,");
        query.append("MonthlyPaymentPeriod, ExtraordinaryPaymentPercentage, ExtraordinaryPaymentPeriod, ");
        query.append("EndDateClause, ConventionalPenaltyAmount, ServiceStartDate, ");
        query.append("ScheduleService, SettlementObligations, ClausulaFormaPago,");
        query.append(
		   "PropertyDateVacated, Volume, BookNumber, TowerNameProperty, CityJurisdiction,ContactoMatenimiento,NumeroConvenio, ");
		query.append("ContratoaTerminar, NombreClausulaAdicionar, NombreAnexoAdicionar, ");
		query.append("SolicitudDescripcionNegociacion, SolicitudDescripcionLargaNegociacion, NumeroAdendum, DescripcionClausulaModificada, ");
		query.append(
				"nombreFiador, FiadorNumeroEscrituraPublica, FiadorFechaEscrituraPublica, FiadorNumeroNotariaPublica, PenalizacionRentaMensualInmueble, ");
		query.append("PenalizacionComisionRentaMensual, EstacionamientosInmueble, PorcentajeIvaContraprestacion, DescripcionEquipos, ");
    }

	private void buildDeclarationsSectionFields(final StringBuilder query) {
		query.append("DomainName, BrandName, SubcontractorLegalRepName, ExtensionsNumber, ExtensionYears, ");
		query.append("ExtensionPeriod, ExtensionValidity, ExtensionForcedYears, ExtensionVoluntaryYears, ");
		query.append("RentInitialQuantity, ");
		query.append("ExtensionFirstYearRent, MaintenanceInitialQuantity, EndDateDeclaration, ");
		query.append("OcupationDate, ExtensionAmount, Proyect, ProyectAddress, Developer, Property, ");
		query.append("OfficialId, OfficialIdNumber, PropertyAddress, PropertyDeliveryDate, MonthlyRentAmount, ");
		query.append("MonthlyMaintenanceAmount, PaExtensionForcedYears, PaExtensionVoluntaryYears, ");
		query.append("AaExtensionForcedYears, AaExtensionVoluntaryYears, ");
		query.append("StartExtensionDate, EndExtensionDate, StartFirstExtensionDate, EndFirstExtensionDate, ");
		query.append("MonthlyRentCurrency, ");
	}

	private void buildDeclarationsDraftBySupplierQuery(final StringBuilder query) {
		query.append("SupplierDeedNumber, SupplierDeedConstitutionDate, SupplierDeedNotary, SupplierNotaryNumber, ");
		query.append("SupplierNotaryState, SupplierDeedComercialFolio, SupplierDeedInscrptDateFolio ");
	}

	private String buildContractDraftFields() {
		final StringBuilder query = new StringBuilder();
		query.append("NatPersonTenantDeclarations, MoralOrNatPersonDeclarations, ContractDate, ");
		query.append("PropertyDeedTitleNumber, PropertyDeedTitleDate, ");
		query.append("PropertyDeedTitleNotary, PropertyDeedTitleNotaryNumber, PropertyDeedTitleNotaryState, ");
		query.append("DirectSupplierPersonality, PropDeedTitleCommercialFolio, ");
		query.append("PropDeedTitleRegistrationDate, SubcontactedPersonality, ");
		query.append("ContractType, EventName, EventDatetime, ClausulesToModify, ");
		query.append("MaintenanceClause, ProfessionalLicense, ");
		query.append("ExpeditionDateProfLicense, Specialty, SpecialtyCeduleNumber, ");
		query.append("ExpeditionDateSpecialtyLicense, SanitaryLicense, SocialReasonOfTheContract, ");
		query.append("ContractObject, ContractExtendClause, ContractEndClause, ForcedYears, VoluntaryYears, ");
		query.append("PublicNotaryDeed, DeedOrCommercialRegister, PropertyDeed, DatePropertyDeed, ");
		query.append("NotaryDeed, NumberNotaryDeed, StateNotaryDeed, FolioDeedOrCommercialRegister, ");
		query.append("RegistrationDateFolio, RentClause, ContractualPenaltyMonths, ");
		query.append("PublicDeedNumberCopy, ExtensionNumber, ContractToModify, DateContractToModify, ");
		query.append("ContractObjetToModify, RetroactiveDate, ClauseToModifyContent, EndDateContractToEnd, ");
		query.append("OutsourcedMail, OutsourcedPhoneNumber, OutsourcedAtention, AnticipatedEndDate, ");
		query.append("JurisdictionState, OutsourcedAddress, HealthLicenseGrantsAuthority, DiscountAgreedService, ");
		query.append("AppraisersPfName, ");
		query.append("FinancialEntityLegalRepresentativePosition, ");
		query.append("InscriptionCommercialFolioState, StateCivilLaw, Subsidiaries, ");
		query.append("DeliveryCost, CompensationMonthsRent, ");
		query.append("SupplierObligations ");
		this.buildDeclarationsDraftBySupplierQuery(query);
		return query.toString();
	}

	private String buildUpdateQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append(
				"IdSupplier = :IdSupplier, IdDocumentType = :IdDocumentType, UpdateRequisitionDate = GETDATE(), UpdateRequisitionBy = :UpdateRequisitionBy, ");
		query.append(JdbcTemplateUtils.buildUpdateFields(this.buildNonTableSpecifiedPrimaryKeyFields(), COMMA));
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildUpdateQueryPartOneAndTwo() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append(
				" IdDocument = :IdDocument, UpdateRequisitionDate = GETDATE(), UpdateRequisitionBy = :UpdateRequisitionBy, ");
		query.append(" IdAreaTender= :IdArea, IdUnit= :IdUnit, Contract= :Contract, ContractType= :ContractType ");
		// query.append(JdbcTemplateUtils.buildUpdateFields(this.buildNonTableSpecifiedPrimaryKeyFields(),
		// COMMA));
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildUpdateQueryPartThree() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("IdSupplier = :IdSupplier ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildUpdateQueryPartFour() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append(
				"SupplierAtention = :SupplierAtention,SupplierPhone = :SupplierPhone, SupplierAccountNumber= :SupplierAccountNumber ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildUpdateQueryStepMayor(String queryStep) {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append(queryStep);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildFindByIdQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT SUPPLIER.CompanyName AS SupplierName, SUPPLIER.Rfc AS RFC, ");
		query.append("SUPPLIER.NonFiscalAddress AS SupplierAddress, SUPPLIER.FiscalAddress, ");
		query.append("SUPPLIER.SupplierCompanyPurpose AS SupplierCompanyPurpose, ContractType, ");
		query.append("SUPPLIER.CompanyType AS SupplierCompanyType, DOCUMENTTYPE.Name AS DocumentTypeName, ");
		query.append(
				"CATALOGDOCTYPE.Name AS DocumentName,CATALOGDOCTYPE.IdDocument AS idDocument, REQUISITION.idDocumentType,  ");
		query.append("SCREEN.Name As FlowStatus, DOCUMENTTYPE.DocumentTypeEnum AS EnumDocumentType, ");
		query.append("AREA.Name AS AreaTender, RST.TurnDate, ");
		this.buildAllFields(query);
		query.append(
				", ContractObject, ConsiderationClause, ConsiderationAmount, ContractObjectClause, IdAreaTender, IdUnit, ");
		query.append(
				"Contract, ContractApplicant, ValidityStartDate, ValidityEndDate, ContractType, ContractRisk, VoBocontractRisk ");
		query.append(FROM_REQUISITION)
		.append(" LEFT JOIN REQUISITIONSTATUSTURN AS RST ON REQUISITION.Status = RST.Status ");
		query.append("LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
		query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("LEFT JOIN CATALOGDOCTYPE ON REQUISITION.IdDocument = CATALOGDOCTYPE.IdDocument ");
		query.append("LEFT JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
		query.append("LEFT JOIN AREA ON REQUISITION.IdAreaTender = AREA.IdArea ");
		query.append("WHERE REQUISITION.IdRequisition = :IdRequisition ");
		query.append(
				"AND RST.TurnDate = (SELECT MAX(TurnDate) FROM REQUISITIONSTATUSTURN WHERE IdRequisition = :IdRequisition) ");
		return query.toString();
	}
	private String buildGetUserQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT USERS.IdUser, USERS.IdPosition, USERS.IdArea, USERS.Email as emailSubject, USERS.Status as status,USERS.StatusLogin, CONCAT(USERS.Name,' ', USERS.FirstLastName,' ',  USERS.SecondLastName) as fullName, REQUISITION.IdRequisition, REQUISITION.IdDocumentType, REQUISITION.TemplateIdDocument, REQUISITION.ApplicationDate  ");
		query.append(FROM_USERS );
		query.append("INNER JOIN REQUISITION on USERS.IdUser=REQUISITION.IdApplicant ");
		query.append("WHERE REQUISITION.IdRequisition = :IdRequisition ");
		return query.toString();
	}

	private String buildFindByIdInProgressQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT SUPPLIER.CompanyName AS SupplierName, SUPPLIER.Rfc AS RFC, ");
		query.append("SUPPLIER.NonFiscalAddress AS SupplierAddress, SUPPLIER.FiscalAddress, ");
		query.append("SUPPLIER.SupplierCompanyPurpose AS SupplierCompanyPurpose, ");
		query.append("SUPPLIER.CompanyType AS SupplierCompanyType, DOCUMENTTYPE.Name AS DocumentTypeName, ");
		query.append("SCREEN.Name As FlowStatus, DOCUMENTTYPE.DocumentTypeEnum AS EnumDocumentType, ");
		query.append("AREA.Name AS AreaTender, ");
		this.buildAllFields(query);
		query.append(
				", ContractObject, ConsiderationClause, ConsiderationAmount, ContractObjectClause, IdAreaTender, IdUnit, Contract, ContractApplicant, ValidityStartDate, ValidityEndDate, ContractType, ClausulaFormaPago ");
		query.append(FROM_REQUISITION).append(" LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
		query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("LEFT JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
		query.append("LEFT JOIN AREA ON REQUISITION.IdAreaTender = AREA.IdArea ");
		query.append("WHERE REQUISITION.IdRequisition = :IdRequisition ");
		return query.toString();
	}

	private void buildSelectAllQuery(final StringBuilder query) {
		query.append("SELECT SUPPLIER.CompanyName AS SupplierName, SUPPLIER.Rfc AS RFC, ");
		query.append("SUPPLIER.NonFiscalAddress AS SupplierAddress, SUPPLIER.FiscalAddress, ");
		query.append("SUPPLIER.SupplierCompanyPurpose AS SupplierCompanyPurpose, ");
		query.append("SUPPLIER.CompanyType AS SupplierCompanyType, DOCUMENTTYPE.Name AS DocumentTypeName, ");
		query.append("SCREEN.Name As FlowStatus, DOCUMENTTYPE.DocumentTypeEnum AS EnumDocumentType, ");
		query.append("AREA.Name AS AreaTender, ");
		query.append(" CONCAT (USERS.Name, ' ' , USERS.FirstLastName, ' ', USERS.SecondLastName) As LawyerName, ");
		this.buildAllFields(query);
		this.buildRequisitionJoins2(query);
	}

	private void buildSelectAllQuery2(final StringBuilder query) {
		query.append("SELECT SUPPLIER.CompanyName AS SupplierName, SUPPLIER.Rfc AS RFC, ");
		query.append("SUPPLIER.NonFiscalAddress AS SupplierAddress, SUPPLIER.FiscalAddress, ");
		query.append("SUPPLIER.SupplierCompanyPurpose AS SupplierCompanyPurpose, ");
		query.append("SUPPLIER.CompanyType AS SupplierCompanyType, DOCUMENTTYPE.Name AS DocumentTypeName, ");
		query.append("SCREEN.Name As FlowStatus, DOCUMENTTYPE.DocumentTypeEnum AS EnumDocumentType, ");
		query.append("AREA.Name AS AreaTender, ");
		query.append(" CONCAT (USERS.Name, ' ' , USERS.FirstLastName, ' ', USERS.SecondLastName) As LawyerName, ");
		this.buildAllFields(query);
		this.buildRequisitionJoins2(query);
	}

	private void buildSelectAllDistinctForTrayQuery(final StringBuilder query) {
		query.append("SELECT DISTINCT SUPPLIER.CompanyName AS SupplierName, DOCUMENTTYPE.Name AS DocumentTypeName,");
		query.append("REQUISITION.IdRequisition, REQUISITION.ApplicationDate, REQUISITION.Status, ");
		query.append(
				"CONCAT (USERLAWYER.Name, ' ' , USERLAWYER.FirstLastName, ' ', USERLAWYER.SecondLastName) As LawyerName, ");
		query.append("TURN.Turn AS Retry, COMMENTS.CommentText AS Comment, TURN.TurnDate, ");
		query.append("REQUISITION.ContractType as contractType, ");
		query.append("AREA.Name as area, ");
		query.append("unit.name as unidad, "); // FIXME ENTITY

		this.buildSemaphoreFields(query);
		this.buildRequisitionJoins(query);
	}

	private void buildSemaphoreFields(final StringBuilder query) {
		final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
		final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
		query.append("CASE ");
		query.append(WHEN).append(
				this.databaseUtils.buildDateDiffSSqlFunction(formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
		query.append(" < REDSEMAPHORE.RedSemaphore");
		query.append(" * TURN.AttentionDays THEN 'RED' ");
		query.append(WHEN).append(
				this.databaseUtils.buildDateDiffSSqlFunction(formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
		query.append(" < YELLOWSEMAPHORE.YellowSemaphore");
		query.append(" * TURN.AttentionDays THEN 'YELLOW' ");
		query.append("ELSE 'GREEN' END AS Semaphore, FINANCIALENTITY.Name AS Empresa, ");
		query.append("TURN.Turn AS Turn, TURN.AttentionDays AS AttentionDays ");
	}

	private void buildRequisitionJoins(final StringBuilder query) {
		query.append(FROM_REQUISITION);
		query.append(
				" LEFT JOIN REQUISITIONFINANCIALENTITY ON REQUISITIONFINANCIALENTITY.IdRequisition =  REQUISITION.IdRequisition ");
		query.append(
				" LEFT JOIN FINANCIALENTITY ON FINANCIALENTITY.IdFinancialEntity = REQUISITIONFINANCIALENTITY.IdFinancialEntity ");
		query.append(" LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
		query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("LEFT JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
		query.append("LEFT JOIN AREA ON REQUISITION.IdAreaTender = AREA.IdArea ");
		query.append("LEFT JOIN unit ON REQUISITION.IdUnit = unit.idUnit ");

	}

	private void buildRequisitionJoins2(final StringBuilder query) {
		query.append(FROM_REQUISITION).append(" LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
		query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("LEFT JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
		query.append("LEFT JOIN AREA ON REQUISITION.IdAreaTender = AREA.IdArea ");
		query.append("LEFT JOIN USERS ON REQUISITION.IdLawyer = USERS.IdUser ");
	}

	private void buildAllFields(final StringBuilder query) {
		query.append("REQUISITION.IdRequisition, ApplicationDate, ");
		this.buildAllNonPrimaryOrDefaultFields(query);
		query.append(COMMA).append(this.buildContractDraftFields());
		query.append(", REQUISITION.Status ");
	}

	private String buildChangeStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("Status = :Status ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildChangeRequisitionStatusToCancelledQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("Status = 'CANCELLED', CancellationDate = GETDATE()");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private MapSqlParameterSource createRequisitionChangeStatusNamedParameters(final Integer idRequisition,
			final FlowPurchasingEnum status) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.STATUS, status.toString());
		return namedParameters;
	}

	public void deleteById(final Integer idRequisition) {
		final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
		this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
	}

	private String buildDeleteByIdQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM REQUISITION ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private MapSqlParameterSource createFindByIdNamedParameters(final Integer idRequisition) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		return namedParameters;
	}

	private String buildSaveRequisitionApprovalAreaQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITIONAPPROVALAREA (IdArea, IdRequisition, VoboIdDocument) ");
		query.append("VALUES (:IdArea, :IdRequisition, :VoboIdDocument)");
		return query.toString();
	}

	private MapSqlParameterSource createSaveRequisitionApprovalAreaNamedParameters(final Integer idRequisition,
			final Integer idArea, final Integer voboIdDocument) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.ID_AREA, idArea);
		namedParameters.addValue(TableConstants.VOBO_ID_DOCUMENT, voboIdDocument);
		return namedParameters;
	}

	private String buildSaveRequisitionAuthorizationDgaQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITIONAUTHORIZATIONDGA (IdRequisition, IdDga) VALUES (:IdRequisition, :IdDga)");
		return query.toString();
	}

	private MapSqlParameterSource createSaveRequisitionAuthorizationDgaNamedParameters(final Integer idRequisition,
			final Integer idDga) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.ID_DGA, idDga);
		return namedParameters;
	}

	private String buildDeleteRequisitionAuthorizationDgaByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM REQUISITIONAUTHORIZATIONDGA ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildDeleteRequisitionApprovalAreasByIdrequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM REQUISITIONAPPROVALAREA ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildFindRequisitionAuthorizationDgasQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdDga FROM REQUISITIONAUTHORIZATIONDGA ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildFindRequisitionAuthorizationDgasActiveQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT DGA.Name FROM REQUISITIONAUTHORIZATIONDGA INNER JOIN DGA "
				+ "ON DGA.IdDga = REQUISITIONAUTHORIZATIONDGA.IdDga ");
		query.append(WHERE_ID_EQUALS_ID);
		query.append(" AND DGA.Status = 'ACTIVE'");
		return query.toString();
	}

	private String buildFindRequisitionApprovalAreasQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdArea FROM REQUISITIONAPPROVALAREA ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildFindRequisitionApprovalAreasActiveQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT AREA.Name ");
		query.append("FROM AREA INNER JOIN REQUISITIONAPPROVALAREA ");
		query.append("ON REQUISITIONAPPROVALAREA.IdArea = AREA.IdArea ");
		query.append("WHERE REQUISITIONAPPROVALAREA.IdRequisition = :IdRequisition");
		return query.toString();
	}

	private String buildSaveRequisitionLegalRepresentativeQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQLEGALREPRESENTATIVE (IdRequisition, IdLegalRepresentative) ");
		query.append("VALUES (:IdRequisition, :IdLegalRepresentative)");
		return query.toString();
	}

	private MapSqlParameterSource createSaveRequisitionLegalRepresentativeNamedParameters(final Integer idRequisition,
			final Integer idLegalRepresentative) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.ID_LEGALREPRESENTATIVE, idLegalRepresentative);
		return namedParameters;
	}

	private String buildFindRequisitionLegalRepresentativeQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT LEGALREPRESENTATIVE.IdLegalRepresentative, LEGALREPRESENTATIVE.Name, ");
		query.append("REQLEGALREPRESENTATIVE.SignSendDate,  REQLEGALREPRESENTATIVE.SignReturnDate, ");
		query.append("IsOriginalSignedContDelivered, IsCopySignedContractDelivered, SignedContractSendDate ");
		query.append("FROM REQLEGALREPRESENTATIVE INNER JOIN LEGALREPRESENTATIVE ");
		query.append("ON LEGALREPRESENTATIVE.IdLegalRepresentative = ");
		query.append("REQLEGALREPRESENTATIVE.IdLegalRepresentative ");
		query.append(WHERE_ID_EQUALS_ID);
		query.append(" AND LEGALREPRESENTATIVE.Status = 'ACTIVE' ");
		return query.toString();
	}

	private String buildFindRequisitionLegalRepresentativeActiveQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT LEGALREPRESENTATIVE.Name FROM REQLEGALREPRESENTATIVE INNER JOIN ");
		query.append("LEGALREPRESENTATIVE ON LEGALREPRESENTATIVE.IdLegalRepresentative ");
		query.append("= REQLEGALREPRESENTATIVE.IdLegalRepresentative ");
		query.append(WHERE_ID_EQUALS_ID);
		query.append(" AND LEGALREPRESENTATIVE.Status = 'ACTIVE'");
		return query.toString();
	}

	private String buildDeleteRequisitionLegalRepresentativesQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM REQLEGALREPRESENTATIVE ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildFindByFlowPurchasingStatusQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append(WHERE_STATUS_EQUALS_STATUS);
		query.append("AND IdFlow = :IdFlow ORDER BY ApplicationDate DESC");
		return query.toString();
	}

	private MapSqlParameterSource createFindRequisitionsToCreateOneFromAnotherParameters(
			final String idRequisitionParameter, final String documentTypeParameter, final String supplierParameter) {
		final MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue(TableConstants.ID_REQUISITION, LIKE + idRequisitionParameter + LIKE);
		source.addValue(TableConstants.ID_REQUISITION + NULL, idRequisitionParameter);
		source.addValue(TableConstants.DOCUMENT_TYPE, LIKE + documentTypeParameter + LIKE);
		source.addValue(TableConstants.DOCUMENT_TYPE + NULL, documentTypeParameter);
		source.addValue(TableConstants.SUPPLIER_NAME, LIKE + supplierParameter + LIKE);
		source.addValue(TableConstants.SUPPLIER_NAME + NULL, supplierParameter);
		return source;
	}
	
	
//	  private MapSqlParameterSource createFindRequisitionByManyParametersprueba( final RequisitionDTO requisition ) {
	  private MapSqlParameterSource createFindRequisitionByManyParametersprueba( RequisitionAngular requisition ) {
			final MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue(TableConstants.ID_REQUISITION, LIKE + requisition.getIdRequisition() + LIKE);
			source.addValue(TableConstants.ID_REQUISITION + NULL, requisition.getIdRequisition());
//			source.addValue(TableConstants.ID_REQUISITION, LIKE + requisition.getIdRequisition() + LIKE);
//			source.addValue(TableConstants.ID_REQUISITION + NULL, requisition.getIdRequisition());
//			source.addValue(TableConstants.DOCUMENT_TYPE, LIKE + requisition.getDocumentType() + LIKE);
//			source.addValue(TableConstants.DOCUMENT_TYPE + NULL, requisition.getDocumentType());
//			source.addValue(TableConstants.LAWYER_NAME, LIKE + requisition.getLawyer() + LIKE);
//			source.addValue(TableConstants.LAWYER_NAME + NULL, requisition.getLawyer());
//			source.addValue(TableConstants.RFC, LIKE + requisition.getRfc() + LIKE);
//			source.addValue(TableConstants.RFC + NULL, requisition.getRfc());
//			source.addValue(TableConstants.SUPPLIER_NAME, LIKE + requisition.getSupplierName() + LIKE);
//			source.addValue(TableConstants.SUPPLIER_NAME + NULL, requisition.getSupplierName());
//			source.addValue(TableConstants.FLOW_STATUS, requisition.getFlowStatus());
//			source.addValue(TableConstants.FLOW_STATUS + NULL, requisition.getFlowStatus());
//			source.addValue(TableConstants.START_DATE, requisition.getStarDate());
//			source.registerSqlType(TableConstants.START_DATE, Types.VARCHAR);
//			source.addValue(TableConstants.END_DATE, requisition.getEndDate());
//			source.registerSqlType(TableConstants.END_DATE, Types.VARCHAR);
//			source.addValue(TableConstants.ID_FLOW, requisition.getIdFlow());
//			source.addValue(TableConstants.COMPANY_NAME, requisition.getCompanyName());
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("PARAMETROS IdRequisition: " + requisition);
//			System.out.println("PARAMETROS DocumentType: " + requisition.getDocumentType());
//			System.out.println("PARAMETROS LAWYER NAME: " + requisition.getLawyer());
////			System.out.println("PARAMETROS RFC: " + requisition.getRfc());
//			System.out.println("PARAMETROS SupplierName: " + requisition.getSupplierName());
//	        System.out.println("PARAMETROS: " + source.toString());
//	        System.out.println(" -- PARAMETROS: ID FLOW:  " + requisition.getIdFlow());
//	        System.out.println("------------------------------------------------------------------------------------");
//	        System.out.println("------------------------------------------------------------------------------------");
//	        System.out.println("------------------------------------------------------------------------------------");
//		        final MapSqlParameterSource source = new MapSqlParameterSource();
//		        source.addValue(TableConstants.ID_REQUISITION, LIKE + requisition.getIdRequisition() + LIKE);
//		        source.addValue(TableConstants.ID_REQUISITION + NULL, requisition.getIdRequisition());
//		        source.addValue(TableConstants.DOCUMENT_TYPE, LIKE + requisition.getDocumentTypeName() + LIKE);
//		        source.addValue(TableConstants.DOCUMENT_TYPE + NULL, requisition.getDocumentTypeName());
//		        source.addValue(TableConstants.SUPPLIER_NAME, LIKE + requisition.getSupplierName() + LIKE);
//		        source.addValue(TableConstants.SUPPLIER_NAME + NULL, requisition.getSupplierName());
		       Log.info("QUERY -> createFindRequisitionsToCreateOneFromAnotherParameters -PARAMETROS LIKE ");
			return source;
	    }

	
	
	
	
	

	private MapSqlParameterSource createFindByFlowPurchasingStatusNamedParameters(final FlowPurchasingEnum status,
			final Integer idFlow) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.STATUS, status.toString());
		namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
		return namedParameters;
	}

	private String buildFindRequisitionFinancialQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT FINANCIALENTITY.IdFinancialEntity ");
		this.buildFromActiveRequisitionFinantialEntityQuery(query);
		return query.toString();
	}

	private String buildFindRequisitionFinancialActiveQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT FINANCIALENTITY.Name ");
		this.buildFromActiveRequisitionFinantialEntityQuery(query);
		return query.toString();
	}

	private void buildFromActiveRequisitionFinantialEntityQuery(final StringBuilder query) {
		query.append("FROM REQUISITIONFINANCIALENTITY INNER JOIN FINANCIALENTITY ");
		query.append("ON FINANCIALENTITY.IdFinancialEntity = REQUISITIONFINANCIALENTITY.IdFinancialEntity ");
		query.append("WHERE IdRequisition = :IdRequisition AND FINANCIALENTITY.Status = 'ACTIVE'");
	}

	private String buildFindRequisitionFinancialWitnessQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdRequisitonFinEntityWitness, IdRequisition, Name ");
		query.append("FROM REQUISITIONFINANCIALENTWIT ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildSaveRequisitionFinancialEntityQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITIONFINANCIALENTITY(IdFinancialEntity, IdRequisition, Phone, Email,  ");
		query.append("Attention, Rfc) ");
		query.append("VALUES(:IdFinancialEntity, :IdRequisition, :Phone, :Email, :Attention, :Rfc) ");
		return query.toString();
	}

	private String buildSaveUserToVoBoQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITIONUSERSVOBO (IdRequisition, IdUser) ");
		query.append("VALUES (:IdRequisition, :IdUser)");
		return query.toString();
	}

	private MapSqlParameterSource createSaveUserToVoBoNamedParameters(final Integer idRequisition,
			final Integer idUser) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstantsSecurity.ID_USER, idUser);
		return namedParameters;
	}

	private String findUsersToVoBoQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT USERS.IdUser, USERS.Name, USERS.FirstLastName, USERS.SecondLastName, IsVoBoGiven, ");
		query.append("POSITION.Name AS positionName, AREA.Name AS AreaName ");
		query.append("FROM REQUISITION INNER JOIN REQUISITIONUSERSVOBO ON ");
		query.append("REQUISITION.IdRequisition = REQUISITIONUSERSVOBO.IdRequisition INNER JOIN ");
		query.append("USERS ON REQUISITIONUSERSVOBO.IdUser = USERS.IdUser LEFT JOIN ");
		query.append("AREA ON USERS.IdArea = AREA.IdArea LEFT JOIN ");
		query.append("POSITION ON USERS.IdPosition = POSITION.IdPosition ");
		query.append(WHERE_REQUISITION_ID_REQUISITION_EQUALS_ID_REQUISITION);
		return query.toString();
	}

	private String buildDeleteUsersToVoBoQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM REQUISITIONUSERSVOBO ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildSaveRequisitionFinantialEntityWitnessQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITIONFINANCIALENTWIT (IdRequisition, Name) ");
		query.append("VALUES (:IdRequisition, :Name)");
		return query.toString();
	}

	private MapSqlParameterSource createSaveRequisitionFinantialEntityWitnessNamedParameters(
			final Integer idRequisition, final String witnessName) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.NAME, witnessName);
		return namedParameters;
	}

	private String buildFindRequisitionFinantialEntityWitnessesQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT Name FROM REQUISITIONFINANCIALENTWIT ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildDeleteRequisitionFinantialEntityWitnessesQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM REQUISITIONFINANCIALENTWIT ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildUpdateRequisitionLawyerQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITION SET IdLawyer = :IdLawyer ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private MapSqlParameterSource createUpdateRequisitionLawyerNamedParameters(final Integer idRequisition,
			final Integer idLawyer) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.ID_LAWYER, idLawyer);
		return namedParameters;
	}

	private String buildSaveContractDraftFieldsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append(JdbcTemplateUtils.buildUpdateFields(this.buildContractDraftFields(), COMMA));
		query.append(", ValidityStartDate = :ValidityStartDate, ValidityEndDate = :ValidityEndDate, ");
		query.append("ActiveActor = :ActiveActor, PassiveActor = :PassiveActor, ");
		query.append(
				"MonthlyRentAmount = :MonthlyRentAmount, UpdateRequisitionDate = GETDATE(), UpdateRequisitionBy = :UpdateRequisitionBy, ");
		query.append(JdbcTemplateUtils.buildUpdateFields(this.setDraftFields(), COMMA));
		query.append(COMMA);
		query.append(JdbcTemplateUtils.buildUpdateFields(this.buildDraftClausesFields(), COMMA)).append(" ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildDraftClausesFields() {
		final StringBuilder query = new StringBuilder();
		query.append("ConsiderationClause, ContractValidityMonths, ConventionalPenaltyPercentage, ");
		query.append("DepositsRealizedMonthsNumber, SignState, ServiceDescription, DeliveryCalendarDays, ");
		query.append("ReimbursementTerminationCalendarDays, InitialPaymentPercentage, InitialAdvanceAmount ");
		return query.toString();
	}

	private String buildSaveRequisitionLegalRepresentativeSignDateQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITIONLEGALREPRESENTATIVE_SET);
		query.append("SignSendDate = :SignSendDate, SignReturnDate = :SignReturnDate ");
		query.append(WHERE_ID_REQUISITION_EQUALS_AND_ID_LEGAL_REPRESENTATIVE);
		return query.toString();
	}

	private MapSqlParameterSource createSaveRequisitionLegalRepresentativeSignNamedParameters(
			final Integer idRequisition, final LegalRepresentative legalRepresentative) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.ID_LEGALREPRESENTATIVE, legalRepresentative.getIdLegalRepresentative());
		namedParameters.registerSqlType(TableConstants.SIGN_SEND_DATE, Types.VARCHAR);
		namedParameters.addValue(TableConstants.SIGN_SEND_DATE, legalRepresentative.getSignSendDate());
		namedParameters.registerSqlType(TableConstants.SIGN_RETURN_DATE, Types.VARCHAR);
		namedParameters.addValue(TableConstants.SIGN_RETURN_DATE, legalRepresentative.getSignReturnDate());
		namedParameters.registerSqlType(TableConstants.SIGN_SEND_DATE, Types.VARCHAR);
		namedParameters.registerSqlType(TableConstants.SIGN_RETURN_DATE, Types.VARCHAR);
		return namedParameters;
	}

	private Integer insertRequisitionAttachment(final RequisitionAttachment requisitionAttatchment)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createRequisitionAttachmentNamedParameters(requisitionAttatchment);
			this.namedjdbcTemplate.update(this.buildInsertRequisitionAttachmentQuery(), namedParameters);
			return requisitionAttatchment.getIdRequisition();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildInsertRequisitionAttachmentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITIONATTACHMENT VALUES (:IdRequisition,:IdDocument)");
		return query.toString();
	}

	private MapSqlParameterSource createRequisitionAttachmentNamedParameters(
			final RequisitionAttachment requisitionAttatchment) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, requisitionAttatchment.getIdRequisition());
		namedParameters.addValue(TableConstants.ID_DOCUMENT, requisitionAttatchment.getIdDocument());
		return namedParameters;
	}

	private String findRequisitionAttachmentByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdDocument ");
		query.append("FROM REQUISITIONATTACHMENT ");
		query.append("WHERE IdRequisition =:IdRequisition ");
		return query.toString();
	}

	private String buildFindRequisitionApprovalAreasVoBoQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT AREA.IdArea, AREA.Name, VoboIdDocument, VERSION.DocumentPath ");
		query.append("FROM REQUISITIONAPPROVALAREA INNER JOIN ");
		query.append("AREA ON AREA.IdArea = REQUISITIONAPPROVALAREA.IdArea LEFT JOIN ");
		query.append("DOCUMENT ON DOCUMENT.IdDocument = REQUISITIONAPPROVALAREA.VoboIdDocument LEFT JOIN ");
		this.buildCurentVersionQuery(query);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildSaveSupplierApprovalIdDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITION SET SupplierApprovalIdDocument = :SupplierApprovalIdDocument ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildFindDigitalizationDocumentsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT DOCUMENT.IdDocument, VERSION.DocumentPath ");
		query.append("FROM REQUISITIONDIGITALIZATION INNER JOIN ");
		query.append("DOCUMENT ON DigitalizationIdDocument = DOCUMENT.IdDocument INNER JOIN ");
		this.buildCurentVersionQuery(query);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private void buildCurentVersionQuery(final StringBuilder query) {
		query.append("VERSION ON DOCUMENT.IdDocument = VERSION.IdDocument ");
		query.append("AND DOCUMENT.CurrentVersion = VERSION.VersionNumber ");
	}

	private String buildDeleteDigitalizationDocumentsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(DELETE_FROM_REQUISITIONDIGITALIZATION);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildsaveTemplateIdDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("TemplateIdDocument = :TemplateIdDocument ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}
	private String buildsaveSupplierApprovalDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("supplierApprovalIdDocument = :supplierApprovalDocument ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private MapSqlParameterSource createSaveTemplateIdDocumentNamedParameters(final Integer idRequisition,
			final Integer templateIdDocument) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.TEMPLATE_ID_DOCUMENT, templateIdDocument);
		return namedParameters;
	}
	private MapSqlParameterSource createSaveSupplierApprovalDocumentNamedParameters(final Integer idRequisition,
			final Integer supplierApprovalDocument) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.Supplier_Approval_Document, supplierApprovalDocument);
		return namedParameters;
	}

	private String deleteDigitalizationByIdDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(DELETE_FROM_REQUISITIONDIGITALIZATION);
		query.append("WHERE DigitalizationIdDocument = :DigitalizationIdDocument");
		return query.toString();
	}

	private MapSqlParameterSource createDeleteDigitalizationByIdDocumentNamedParameters(final Integer idDocument) {
		final MapSqlParameterSource namedParemeters = new MapSqlParameterSource();
		namedParemeters.addValue(TableConstants.DIGITALIZATION_ID_DOCUMENT, idDocument);
		return namedParemeters;
	}

	private String createInsertObligationQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO OBLIGATION (").append(this.buildNonPrimaryKeyObligtionFields()).append(") ");
		query.append("VALUES (");
		query.append(JdbcTemplateUtils.tagFields(this.buildNonPrimaryKeyObligtionFields(), COMMA));
		query.append(RIGHT_BRACES);
		return query.toString();
	}

	private String buildNonPrimaryKeyObligtionFields() {
		final StringBuilder query = new StringBuilder();
		query.append("IdRequisition, StartDate, EndDate, ObligationText, Amount, Status ");
		return query.toString();
	}

	private String buildAllObligationFields() {
		final StringBuilder query = new StringBuilder();
		query.append("IdObligation, ").append(this.buildNonPrimaryKeyObligtionFields());
		return query.toString();
	}

	private void buildSelecAllObligationFields(final StringBuilder query) {
		query.append(SELECT).append(this.buildAllObligationFields()).append("FROM OBLIGATION ");
	}

	private String buildFindObligationsByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelecAllObligationFields(query);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildDeleteObligationsByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM OBLIGATION ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildFindRequisitionsForTrayQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllDistinctForTrayQuery(query);
		this.buildFindRequisitionsForTrayJoinsQuery(query);
		this.buildFindRequisitionsForTrayFiltersQuery(query);
		query.append("ORDER BY TURN.TurnDate DESC");
//		query.append("ORDER BY REQUISITION.ApplicationDate DESC");
		return query.toString();
	}
	private String buildFindRequisitionsPorFechasForTrayQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllDistinctForTrayQuery2(query);
		this.buildFindRequisitionsForTrayJoinsQuery2(query);
		this.buildFindRequisitionsForTrayFiltersQueryFechas(query);			
		query.append("ORDER BY TURN.TurnDate DESC");
//		query.append("ORDER BY REQUISITION.ApplicationDate DESC");
		return query.toString();
	}
	
	private String buildFindContractsInRevision() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT r.IdRequisition, r.StageStartDate, r.Status, c.Email\r\n"
				+ "FROM REQUISITION r\r\n"
				+ "JOIN USERS c ON r.IdApplicant = c.IdUser \r\n"
				+ "WHERE r.Status = 'NEGOTIATOR_CONTRACT_REVIEW'");
		return query.toString();
	}
	
	private String buildFindContractsInSignatures() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT r.IdRequisition, r.StageStartDate, r.Status, c.Email\r\n"
				+ "FROM REQUISITION r\r\n"
				+ "JOIN USERS c ON r.IdLawyer = c.IdUser \r\n"
				+ "WHERE r.Status = 'SACC_SIGN_CONTRACT'");
		return query.toString();
	}

	private void buildFindRequisitionsForTrayJoinsQuery(final StringBuilder query) {
		query.append(
				"INNER JOIN REQUISITIONSTATUSTURN TURN ON REQUISITION.IdRequisition = TURN.IdRequisition AND REQUISITION.Status = TURN.Status ");
		query.append("LEFT JOIN REQUISITION LAWYERSUBDIRECTOR ");
		query.append("ON LAWYERSUBDIRECTOR.IdRequisition = REQUISITION.IdRequisition ");
		query.append("LEFT JOIN USERS ON USERS.IdUser = REQUISITION.IdApplicant ");
		query.append("LEFT JOIN USERS USERLAWYER ON USERLAWYER.IdUser = REQUISITION.IdLawyer ");
		query.append("LEFT JOIN REQUISITIONUSERSVOBO USERSVOBO ");
		query.append("ON USERSVOBO.IdRequisition = REQUISITION.IdRequisition ");
		query.append("LEFT JOIN REQUISITION USERSVOBOREQUISITION ");
		query.append("ON USERSVOBOREQUISITION.IdRequisition = USERSVOBO.IdRequisition ");
		query.append("LEFT JOIN COMMENTS ON COMMENTS.IdRequisition = REQUISITION.IdRequisition ");
		query.append("AND COMMENTS.FlowStatus = :Status ");
		query.append("LEFT JOIN (SELECT IdRequisition, MAX(CreationDate) AS CreationDate FROM COMMENTS WHERE ");
		query.append("FlowStatus = :Status GROUP BY ");
		query.append("IdRequisition) RECENT_COMMENT ON COMMENTS.IdRequisition = RECENT_COMMENT.IdRequisition,");
		query.append(
				"(SELECT CAST(Value AS DECIMAL(3,2)) AS RedSemaphore FROM CONFIGURATION WHERE Name = 'RED_SEMAPHORE_PERCENTAGE') REDSEMAPHORE,");
		query.append(
				"(SELECT CAST(Value AS DECIMAL(3,2)) AS YellowSemaphore FROM CONFIGURATION WHERE Name = 'YELLOW_SEMAPHORE_PERCENTAGE') YELLOWSEMAPHORE ");
	}

	private void buildFindRequisitionsForTrayFiltersQuery(final StringBuilder query) {
		query.append("WHERE REQUISITION.IdFlow = :IdFlow AND REQUISITION.Status = :Status ");
		query.append("AND (COMMENTS.CreationDate = RECENT_COMMENT.CreationDate OR COMMENTS.CreationDate IS NULL) AND ");
		this.buildInboxFilter2(query);
		query.append(
				"TURN.TurnDate = (SELECT MAX(TurnDate) FROM REQUISITIONSTATUSTURN TURNDATE WHERE TURNDATE.IdRequisition = REQUISITION.IdRequisition) AND (");
		query.append(
				"((REQUISITION.IdApplicant = :IdUser OR :isUserFiltered = 0) AND :IdFlow = REQUISITION.IdFlow AND :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANT_BUSY_STATUS'"));
		query.append(
				")) OR ((USERS.IdUnderdirector = :IdUser OR :isUserFiltered = 0) AND :IdFlow = REQUISITION.IdFlow AND :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANTSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR (:IdFlow = LAWYERSUBDIRECTOR.IdFlow AND :Status = LAWYERSUBDIRECTOR.Status AND LAWYERSUBDIRECTOR.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYERSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR ((REQUISITION.IdLawyer = :IdUser OR :isUserFiltered = 0) AND :IdFlow = REQUISITION.IdFlow AND :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils
				.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYER_BUSY_STATUS'"));
		query.append(
				")) OR ((USERSVOBO.IdUser = :IdUser OR :isUserFiltered = 0) AND USERSVOBO.IsVoBoGiven = 0 AND USERSVOBOREQUISITION.IdFlow = :IdFlow AND :Status = USERSVOBOREQUISITION.Status AND USERSVOBOREQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'USERSVOBOREQUISITION_BUSY_STATUS'"));
		query.append(
				")) OR ((REQUISITION.IdEvaluator = :IdUser OR :isUserFiltered = 0) AND :IdFlow = REQUISITION.IdFlow AND :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'EVALUATOR_BUSY_STATUS'"));
		query.append("))) ");
	}

	private void buildInboxFilter(final StringBuilder query) {
		query.append("( ");
		query.append("((:IdRequisitionNull IS NULL) OR (REQUISITION.IdRequisition LIKE :IdRequisition)) AND ");
		query.append("((:DocumentTypeNull IS NULL) OR (DOCUMENTTYPE.Name LIKE :DocumentType)) AND ");
		query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CompanyName LIKE :SupplierName)) ");
		query.append(") AND ");
	}

	private void buildInboxFilter2(final StringBuilder query) {
		query.append("( ");
		query.append("((:IdRequisitionNull IS NULL) OR (REQUISITION.IdRequisition LIKE :IdRequisition)) AND ");
		query.append("((:DocumentTypeNull IS NULL) OR (DOCUMENTTYPE.Name LIKE :DocumentType)) AND ");
		query.append("((:SupplierNameNull IS NULL) OR (SUPPLIER.CompanyName LIKE :SupplierName)) AND ");
		query.append(
				"((:LawyerNameNull IS NULL) OR ((CONCAT (USERLAWYER.Name, ' ' , USERLAWYER.FirstLastName, ' ', USERLAWYER.SecondLastName)) LIKE :LawyerName)) ");
		query.append(") AND ");
	}

	private String buildSaveUserVoboQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITIONUSERSVOBO SET IsVoBoGiven = 1 ");
		query.append(WHERE_ID_REQUISITION_AND_ID_USER);
		return query.toString();
	}

	private MapSqlParameterSource createfindTrayRequisitionsNamedParameters(final TrayFilter trayFilter) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_FLOW, trayFilter.getIdFlow());
		namedParameters.addValue(TableConstants.START_DATE, trayFilter.getFechaInicio());
		namedParameters.addValue(TableConstants.END_DATE, trayFilter.getFechaFin());
		namedParameters.addValue(TableConstants.ID_USER, trayFilter.getIdUser());
		namedParameters.addValue(TableConstants.IS_USER_FILTERED, trayFilter.getIsUserFiltered());
		namedParameters.registerSqlType(STATUS, Types.VARCHAR);
		namedParameters.addValue(TableConstants.STATUS, trayFilter.getStatus().toString());
		namedParameters.addValue(TableConstants.ID_REQUISITION + NULL, trayFilter.getIdRequisition());
		namedParameters.addValue(TableConstants.ID_REQUISITION,
				QueryConstants.ANY_CHARACTER + trayFilter.getIdRequisition() + QueryConstants.ANY_CHARACTER);
		namedParameters.addValue(TableConstants.DOCUMENT_TYPE + NULL, trayFilter.getDocumentType());
		namedParameters.addValue(TableConstants.DOCUMENT_TYPE,
				QueryConstants.ANY_CHARACTER + trayFilter.getDocumentType() + QueryConstants.ANY_CHARACTER);
		namedParameters.addValue(TableConstants.SUPPLIER_NAME + NULL, trayFilter.getSupplierName());
		namedParameters.addValue(TableConstants.SUPPLIER_NAME,
				QueryConstants.ANY_CHARACTER + trayFilter.getSupplierName() + QueryConstants.ANY_CHARACTER);
		namedParameters.addValue(TableConstants.LAWYER_NAME + NULL, trayFilter.getLawyerName());
		namedParameters.addValue(TableConstants.LAWYER_NAME,
				QueryConstants.ANY_CHARACTER + trayFilter.getLawyerName() + QueryConstants.ANY_CHARACTER);

		return namedParameters;
	}

	private MapSqlParameterSource createSaveUserVoboNamedParameters(final Integer idRequisition, final Integer idUser) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.ID_USER, idUser);
		return namedParameters;
	}

	private String buildIsUserVoboGivenQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IsVoBoGiven FROM REQUISITIONUSERSVOBO ");
		query.append(WHERE_ID_REQUISITION_AND_ID_USER);
		return query.toString();
	}

	private String buildFindIsAllUsersVoboQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT 1 - CASE WHEN COUNT(USERS.IdUser) - COUNT(USERSVOBO.IdUser) > 0 THEN 1 ELSE 0 END ");
		query.append("AS IsAllUsersVobo FROM REQUISITIONUSERSVOBO USERS LEFT JOIN REQUISITIONUSERSVOBO USERSVOBO ");
		query.append("ON USERSVOBO.IdRequisition = USERS.IdRequisition AND USERSVOBO.IdUser = USERS.IdUser ");
		query.append("AND USERSVOBO.IsVoBoGiven = 1 ");
		query.append("WHERE USERS.IdRequisition = :IdRequisition");
		return query.toString();
	}

	private String buildSaveRequisitionStatusTurnQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUISITIONSTATUSTURN (IdRequisition, Status, Turn) ");
		query.append("SELECT :IdRequisition, :Status, COALESCE(MAX(Turn), -1) + 1 AS Turn ");
		query.append("FROM REQUISITIONSTATUSTURN ");
		query.append("WHERE IdRequisition = :IdRequisition AND Status = :Status");
		return query.toString();
	}

	private String buildDeleteRequisitionStatusTurnsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM REQUISITIONSTATUSTURN ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	public Integer countRecordsByFlowQuery(final Integer idFlow) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
		return this.namedjdbcTemplate.queryForObject(this.countRecordsByFlow(), namedParameters, Integer.class);
	}

	private String countRecordsByFlow() {
		final StringBuilder query = new StringBuilder();
		query.append(SELECT_COUNT_ID_REQUISITION);
		query.append(FROM_REQUISITION);
		query.append(WHERE_ID_FLOW_EQUALS_ID_FLOW);
		return query.toString();
	}

	public Integer countRecordsByIdRequisitionQuery(final Integer idRequisition) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		return this.namedjdbcTemplate.queryForObject(this.countRecordsByIdRequisition(), namedParameters,
				Integer.class);
	}

	private String countRecordsByIdRequisition() {
		final StringBuilder query = new StringBuilder();
		query.append(SELECT_COUNT_ID_REQUISITION);
		query.append(FROM_REQUISITION);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private String buildSaveRequisitionStatusTurnAttentionDaysAndStageQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITIONSTATUSTURN SET AttentionDays = :AttentionDays, Stage = :Stage ");
		query.append("WHERE IdRequisition = :IdRequisition AND Status = :Status AND Turn = :Turn");
		return query.toString();
	}

	private String buildFindRequisitionStatusTurnsByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdRequisition, Status, Turn, TurnDate, AttentionDays, Stage ");
		query.append("FROM REQUISITIONSTATUSTURN WHERE IdRequisition = :IdRequisition");
		return query.toString();
	}

	private String buildFindCurrentTurnByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT MAX(Turn) AS Turn ");
		query.append("FROM REQUISITION INNER JOIN REQUISITIONSTATUSTURN TURN ");
		query.append("ON REQUISITION.IdRequisition = TURN.IdRequisition AND REQUISITION.Status = TURN.Status ");
		query.append(WHERE_REQUISITION_ID_REQUISITION_EQUALS_ID_REQUISITION);
		return query.toString();
	}

	@Override
	public void saveScalingMatrix(final Scaling scaling) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(scaling);
			parameterSource.registerSqlType(TableConstants.SCALING_TYPE, Types.VARCHAR);
			this.namedjdbcTemplate.update(this.buildSaveScalingMatrixQuery(), parameterSource);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildSaveScalingMatrixQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO SCALING (Name, Position, Area, Phone, Mail, ScalingLevel, IdRequisition, ");
		query.append("ScalingType) ");
		query.append("VALUES (:Name, :Position, :Area, :Phone, :Mail, :ScalingLevel, :IdRequisition, :ScalingType)");
		return query.toString();
	}

	@Override
	public void deleteScalingMatrixByIdRequisition(final Integer idRequisition, final ScalingTypeEnum scalingType)
			throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.idRequisitionParameterSource(idRequisition);
			parameterSource.addValue(TableConstants.SCALING_TYPE, scalingType.toString());
			parameterSource.registerSqlType(TableConstants.SCALING_TYPE, Types.VARCHAR);
			this.namedjdbcTemplate.update(this.buildDeleteScalingMatrixByIdRequisitionxQuery(), parameterSource);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildDeleteScalingMatrixByIdRequisitionxQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM SCALING ");
		query.append(WHERE_ID_EQUALS_ID);
		query.append("AND ScalingType =:ScalingType ");
		return query.toString();
	}

	@Override
	public List<Scaling> findScalingMatrixVersionByIdRequisitionVersion(final Integer idRequisitionVersion,
			final ScalingTypeEnum scalingType) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue(TableConstants.ID_REQUISITION_VERSION, idRequisitionVersion);
			parameterSource.addValue(TableConstants.SCALING_TYPE, scalingType.toString());
			parameterSource.registerSqlType(TableConstants.SCALING_TYPE, Types.VARCHAR);
			return this.namedjdbcTemplate.query(this.buildfindScalingMatrixVersionByIdRequisitionVersionQuery(),
					parameterSource, new BeanPropertyRowMapper<Scaling>(Scaling.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Scaling> findScalingMatrixByIdRequisition(final Integer idRequisition,
			final ScalingTypeEnum scalingType) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.idRequisitionParameterSource(idRequisition);
			parameterSource.addValue(TableConstants.SCALING_TYPE, scalingType.toString());
			parameterSource.registerSqlType(TableConstants.SCALING_TYPE, Types.VARCHAR);
			return this.namedjdbcTemplate.query(this.buildFindScalingMatrixByIdRequisitionQuery(), parameterSource,
					new BeanPropertyRowMapper<Scaling>(Scaling.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildfindScalingMatrixVersionByIdRequisitionVersionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT Name, Position, Area, Phone, Mail, Level, IdRequisitionVersion AS IdRequisition, ");
		query.append("scalingType ");
		query.append("FROM SCALING_V ");
		query.append("WHERE IdRequisitionVersion =:IdRequisitionVersion ");
		query.append("AND ScalingType =:ScalingType ");
		return query.toString();
	}

	private String buildFindScalingMatrixByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT Name, Position, Area, Phone, Mail, ScalingLevel, IdRequisition, ScalingType ");
		query.append("FROM SCALING ");
		query.append(WHERE_ID_EQUALS_ID);
		query.append("AND ScalingType =:ScalingType ");
		return query.toString();
	}

	private MapSqlParameterSource idRequisitionParameterSource(final Integer idRequisition) {
		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue(TableConstants.ID_REQUISITION, idRequisition);
		return parameterSource;
	}

	@Override
	public List<String> findApprovalAreas(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue(TableConstants.ID_REQUISITION, idRequisition);
			return this.namedjdbcTemplate.queryForList(this.findApprovalAreasNameQuery(), parameterSource,
					String.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findApprovalAreasNameQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT AREA.Name FROM AREA INNER JOIN REQUISITIONAPPROVALAREA ");
		builder.append("ON AREA.IdArea = REQUISITIONAPPROVALAREA.IdArea ");
		builder.append(WHERE_ID_EQUALS_ID);
		return builder.toString();
	}

	@Override
	public void cleanUsersVobo(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisition);
			this.namedjdbcTemplate.update(this.buildCleanUsersVoboQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildCleanUsersVoboQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITIONUSERSVOBO SET IsVoBoGiven = 0 ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	@Override
	public void deleteRequisitionAttachmentVersion(final Integer idDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue(TableConstants.ID_DOCUMENT, idDocument);
			this.namedjdbcTemplate.update(this.deleteRequisitionAttachamentVersionQuery(), parameterSource);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String deleteRequisitionAttachamentVersionQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM REQUISITIONATTACHMENT_V ");
		builder.append("WHERE IdDocument = :IdDocument ");
		return builder.toString();
	}

	@Override
	public Integer saveComment(final ContractCancellationComment contractCancellationComment)
			throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(
					contractCancellationComment);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.saveCommentQuery(), parameterSource, keyHolder,
					new String[] { "IdCancellationComment" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String saveCommentQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO CONTRACTCANCELLATIONCOMMENT(Comment, IdRequisition) ");
		builder.append("VALUES(:Comment, :IdRequisition) ");
		return builder.toString();
	}

	@Override
	public void saveContractCancellationCommentDocument(final Integer idCancellationComment,
			final Integer idDocument, final String documentName) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue(TableConstants.ID_DOCUMENT, idDocument);
			parameterSource.addValue(TableConstants.ID_CANCELLATION_COMMENT, idCancellationComment);
			parameterSource.addValue(TableConstants.DOCUMENT_NAME, documentName);
			this.namedjdbcTemplate.update(this.saveContractCancellationCommentDocumentQuery(), parameterSource);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String saveContractCancellationCommentDocumentQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO CONTRACTCANCELCOMMENTDOCUMENT(IdCancellationComent, IdDocument, ");
		builder.append("DocumentName) VALUES(:IdCancellationComent, :IdDocument, :DocumentName) ");
		return builder.toString();
	}

	@Override
	public ContractCancellationComment findContractCancellationComment(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.idRequisitionParameterSource(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.findContractCancellationCommentQuery(), source,
					new BeanPropertyRowMapper<ContractCancellationComment>(ContractCancellationComment.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findContractCancellationCommentQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT IdCancellationComent, Comment, CreationDate ");
		builder.append("FROM CONTRACTCANCELLATIONCOMMENT ");
		builder.append(WHERE_ID_EQUALS_ID);
		return builder.toString();
	}

	@Override
	public List<DocumentBySection> findContractCancelationCommentDocument(final Integer idRequisition)
			throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.idRequisitionParameterSource(idRequisition);
			return this.namedjdbcTemplate.query(this.findContractCancelationCommentDocumentQuery(), parameterSource,
					new BeanPropertyRowMapper<DocumentBySection>(DocumentBySection.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findContractCancelationCommentDocumentQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT IdDocument, DocumentName FROM CONTRACTCANCELCOMMENTDOCUMENT ");
		builder.append("INNER JOIN CONTRACTCANCELLATIONCOMMENT ON ");
		builder.append("CONTRACTCANCELCOMMENTDOCUMENT.IdCancellationComent = ");
		builder.append("CONTRACTCANCELLATIONCOMMENT.IdCancellationComent ");
		builder.append(WHERE_ID_EQUALS_ID);
		return builder.toString();
	}

	@Override
	public void saveFreezeInformationOfContractDetail(final Integer idRequisition,
			final String contractDetailJson) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.saveFreezeInformationOfContractDetailNamedParameters(idRequisition, contractDetailJson);
			this.namedjdbcTemplate.update(this.buildSaveFreezeInformationOfContractDetailQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource saveFreezeInformationOfContractDetailNamedParameters(
			final Integer idRequisitionParameter, final String contractDetailJsonParameter) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisitionParameter);
		namedParameters.addValue(TableConstants.CONTRACT_DETAIL_JSON_VALUE, contractDetailJsonParameter);
		return namedParameters;
	}

	private String buildSaveFreezeInformationOfContractDetailQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO CONTRACTDETAIL(IdRequisition, ContractDetailJsonValue) ");
		query.append("VALUES(:IdRequisition, :ContractDetailJsonValue) ");
		return query.toString();
	}

	@Override
	public String findContractDetailByIdRequisition(final Integer idRequisitionParameter)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisitionParameter);
			return this.namedjdbcTemplate.queryForObject(this.buildFindContractDetailByIdRequisitionQuery(),
					namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildFindContractDetailByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT ContractDetailJsonValue FROM CONTRACTDETAIL WHERE IdRequisition =:IdRequisition ");
		return query.toString();
	}

	@Override
	public Boolean isRequisitionCancelled(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource parameterSource = this.idRequisitionParameterSource(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.builFindIsRequisitonCancelledQuery(), parameterSource,
					Boolean.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String builFindIsRequisitonCancelledQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(1) AS IsCancelled FROM REQUISITION WHERE IdRequisition = :IdRequisition ");
		query.append("AND status = 'CANCELLED'");
		return query.toString();
	}

	@Override
	public void deleteAuthorizationDocument(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.idRequisitionParameterSource(idRequisition);
			this.namedjdbcTemplate.update(this.deleteAuthorizationDocumentQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String deleteAuthorizationDocumentQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append(UPDATE_REQUISITION_SET);
		builder.append("AuthorizationDocumentIdDoc = NULL ");
		builder.append(WHERE_REQUISITION_ID_REQUISITION_EQUALS_ID_REQUISITION);
		return builder.toString();
	}

	@Override
	public String findRequisitionStage(final Integer idRequisition)
			throws DatabaseException, EmptyResultException {
		try {
			return this.namedjdbcTemplate.queryForObject(this.buildFindRequisitionStageQuery(),
					this.createFindByIdNamedParameters(idRequisition), String.class);
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			throw new EmptyResultException(emptyResultDataAccessException);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildFindRequisitionStageQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT Stage ");
		query.append(FROM_REQUISITION);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	@Override
	public void saveRequisitionStage(final Integer idRequisition, final String stage) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createSaveRequisitionStageNamedParameters(idRequisition,
					stage);
			this.namedjdbcTemplate.update(this.saveRequisitionStageQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource createSaveRequisitionStageNamedParameters(final Integer idRequsition,
			final String stage) {
		final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
		final String stageStartDate = toDateTimeFormat.format(new Date());
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequsition);
		namedParameters.addValue(TableConstants.STAGE, stage);
		namedParameters.addValue(TableConstants.STAGE_START_DATE, stageStartDate);
		return namedParameters;
	}

	private String saveRequisitionStageQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITION SET Stage = :Stage, StageStartDate = :StageStartDate ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	@Override
	public void deleteImssCeduleFile(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.idRequisitionParameterSource(idRequisition);
			this.namedjdbcTemplate.update(this.deleteImssCeduleFileQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String deleteImssCeduleFileQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append(UPDATE_REQUISITION_SET);
		builder.append("ImssCeduleNotGivenIdDocument = NULL, IsImssCeduleGiven = 0 ");
		builder.append(WHERE_REQUISITION_ID_REQUISITION_EQUALS_ID_REQUISITION);
		return builder.toString();
	}

	@Override
	public Requisition findRequisitionBailsByIdRequisition(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.idRequisitionParameterSource(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.findRequisitionBailsByIdRequisitionQuery(), source,
					new BeanPropertyRowMapper<Requisition>(Requisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findRequisitionBailsByIdRequisitionQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT IsAdvanceBailNeeded, IsFulfillmentBailNeeded, IsFidelityBailNeeded, ");
		builder.append("IsContingencyBailNeeded, IsCivilRespInsuranceBailNeeded ");
		builder.append(FROM_REQUISITION).append(WHERE_ID_EQUALS_ID);
		return builder.toString();
	}

	@Override
	public void deleteSupplierApprovalDocument(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource source = new MapSqlParameterSource(TableConstants.ID_REQUISITION,
					idRequisition);
			this.namedjdbcTemplate.update(this.deleteSupplierApprovalDocumentQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String deleteSupplierApprovalDocumentQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append(UPDATE_REQUISITION_SET);
		builder.append("SupplierApprovalIdDocument = NULL ");
		builder.append(WHERE_ID_EQUALS_ID);
		return builder.toString();
	}

	@Override
	public void saveFinancialEntityByRequisition(final Integer idRequisition,
			final FinancialEntity financialEntity) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.saveFinancialEntityByRequisitionParameters(idRequisition,
					financialEntity);
			this.namedjdbcTemplate.update(this.buildSaveFinancialEntityByRequisitionQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al insertar la Entidad", dataAccessException);
		}
	}

	@Override
	public void updateFinancialEntityRequisitionDraftFields(final Integer idRequisition,
			final FinancialEntity financialEntity) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.saveFinancialEntityByRequisitionParameters(idRequisition,
					financialEntity);
			this.namedjdbcTemplate.update(this.buildUpdateFinancialEntityRequisitionFieldsQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al insertar la Entidad", dataAccessException);
		}
	}

	private String buildUpdateFinancialEntityRequisitionFieldsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITIONFINANCIALENTITY SET Email = :Correo, Rfc = :Rfc ");
		query.append("WHERE IdRequisition = :IdRequisition AND IdFinancialEntity = :IdFinancialEntity");
		return query.toString();
	}

	private MapSqlParameterSource saveFinancialEntityByRequisitionParameters(final Integer idRequisition,
			final FinancialEntity financialEntity) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_FINANCIALENTITY, financialEntity.getIdFinancialEntity());
		namedParameters.addValue(TableConstants.TELEPHONE, financialEntity.getTelefono());
		namedParameters.addValue(TableConstants.CORREO, financialEntity.getCorreo());
		namedParameters.addValue(TableConstants.ATENCION, financialEntity.getAtencion());
		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
		namedParameters.addValue(TableConstants.RFC, financialEntity.getRfc());
		return namedParameters;
	}

	private String buildSaveFinancialEntityByRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO FINANCIALENTITYREQUISITION ");
		query.append("VALUES(:IdFinancialEntity, :Telefono, :Correo, :Atencion, :IdRequisition, :Rfc) ");
		return query.toString();
	}

	@Override
	public void deleteFinancialEntityByRequisition(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource source = new MapSqlParameterSource(TableConstants.ID_REQUISITION,
					idRequisition);
			this.namedjdbcTemplate.update(this.buildDeleteFinancialEntityByRequisitionQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildDeleteFinancialEntityByRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FINANCIALENTITYREQUISITION ");
		query.append("WHERE IdRequisition = :IdRequisition");
		return query.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.contratos.admc.contracts.interfaces.Requisitable#deletePendingRequisitions
	 * (java.util.List)
	 * 
	 * deletePendingRequisitions es el algoritmo para borrar de la base de datos las
	 * Solicitudes Pendientes que se hayan marcado para ser eliminadas. Parámetros:
	 * list : trae las id de solicitud (idRequisition) que se seleccionaron en la
	 * pantalla de Solicitudes Pendientes.
	 *
	 * Luis Virueña: 17 jun 2016
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { DatabaseException.class })
	public void deletePendingRequisitions(List<Integer> list) throws DatabaseException {
		// obtiene todas las tablas que tienen el campo idRequisitionVersion
		String sqlRVTables = "select table_name from information_schema.columns where "
				+ "column_name = 'idRequisitionVersion' and " + "table_name <> 'RequisitionRequisition_V'";
		MapSqlParameterSource paramRVTables = new MapSqlParameterSource();
		List<Map<String, Object>> lstRVTables = namedjdbcTemplate.queryForList(sqlRVTables, paramRVTables);

		// sql para eliminar los registros con idRequisitionVersion que corresponden al
		// idRequisition
		// que queremos eliminar.
		// Notar que se borran los registros de todas las tablas que contienen el campo
		// idRequisitionVersion con
		// excepción de la tabla que da la relación: RequisitionRequisition_v
		String sqlIdReqV = "delete from #TargetTbl# where idRequisitionVersion in "
				+ "(select idRequisitionVersion from RequisitionRequisition_v where idRequisition = :idRequisitionParam) ";

		for (Map<String, Object> m : lstRVTables) {
			String targetTable = m.get("table_name").toString();
			String sql = sqlIdReqV.replaceFirst("#TargetTbl#", targetTable);
			for (Integer idReq : list) {
				MapSqlParameterSource paramIdReq = new MapSqlParameterSource();
				paramIdReq.addValue("idRequisitionParam", idReq);

				namedjdbcTemplate.update(sql, paramIdReq);

			}
		}

		// obtiene todas las tablas que tienen el campo idRequisition
		String sqlRTables = "select table_name from information_schema.columns where "
				+ "column_name = 'idRequisition'";
		// " and table_name <> 'RequisitionRequisition_V'";
		MapSqlParameterSource paramRTables = new MapSqlParameterSource();
		List<Map<String, Object>> lstRTables = namedjdbcTemplate.queryForList(sqlRTables, paramRTables);

		// sql para eliminar los registros con idRequisition que deseamos eliminar.
		// Notar que se borran registros de todas las tablas que contienen el campo
		// idRequisition.
		String sqlIdReq = "delete from #TargetTbl# where idRequisition = :idRequisitionParam ";

		for (Map<String, Object> m : lstRTables) {
			String targetTable = m.get("table_name").toString();
			String sql = sqlIdReq.replaceFirst("#TargetTbl#", targetTable);
			for (Integer idReq : list) {
				MapSqlParameterSource paramIdReq = new MapSqlParameterSource();
				paramIdReq.addValue("idRequisitionParam", idReq);

				namedjdbcTemplate.update(sql, paramIdReq);
			}
		}
	}

	@Override
	public List<RequiredDocumentBySupplier> findRequiredDocumentsBySupplier(Integer idSupplier)
			throws DatabaseException {
		try {
			String sqlA = "select sup.idSupplier, sup.idPersonality, pr.idRequiredDocument, '-' Found, rd.Name "
					+ "from Supplier sup inner join PersonalityRequiredDocument pr "
					+ "on sup.idPersonality = pr.idPersonality " + "inner join RequiredDocument rd "
					+ "on rd.idRequiredDocument = pr.idRequiredDocument " + "where sup.idSupplier = :IdSupplier ";

			String sqlB = "select idRequiredDocument " + "from SupplierRequiredDocument "
					+ "where idSupplier = :IdSupplier ";

			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
			List<RequiredDocumentBySupplier> listRA = namedjdbcTemplate.query(sqlA, namedParameters,
					new BeanPropertyRowMapper<>(RequiredDocumentBySupplier.class));
			List<Integer> listRB = namedjdbcTemplate.queryForList(sqlB, namedParameters, Integer.class);

			for (RequiredDocumentBySupplier reqDoc : listRA)
				for (Integer rb : listRB)
					if (reqDoc.getIdRequiredDocument().equals(rb))
						reqDoc.setFound("found");

			return listRA;
		} catch (DataAccessException dataAccessException) {
			DatabaseException dbex = new DatabaseException(dataAccessException);
			throw dbex;
		}
	}

	@Override
	public List<UserInProgressRequisition> findApplicantInProgressRequisitions(
			final UserInProgressRequisitionFilter filter, final Integer pageNumber, final Integer itemsNumber)
					throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createFindUserInprogressRequisitionsNamedParameters(TableConstants.ID_APPLICANT, filter);
			return this.namedjdbcTemplate.query(
					this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUISITION,
							this.buildFindUserInProgressRequisitionsQuery(
									this.buildApplicantInprogressRequisitionFilterQuery()),
							pageNumber, itemsNumber),
					namedParameters, new BeanPropertyRowMapper<>(UserInProgressRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource createFindUserInprogressRequisitionsNamedParameters(final String idUserColumnName,
			final UserInProgressRequisitionFilter filter) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource(idUserColumnName, filter.getIdUser());
		 namedParameters.addValue(TableConstants.ID_REQUISITION, filter.getIdRequisition());
	        namedParameters.addValue(TableConstants.COMPANY_NAME, filter.getCompanyName());
	        namedParameters.addValue(TableConstants.NAME, filter.getName());
	        namedParameters.addValue(TableConstants.DOCUMENT_NAME, filter.getDocumentTypeName());
	        namedParameters.addValue(TableConstants.STATUS, filter.getStatusName());
	        namedParameters.addValue(TableConstants.START_DATE, filter.getStartDate(), Types.VARCHAR);
	        namedParameters.addValue(TableConstants.END_DATE, filter.getEndDate(), Types.VARCHAR);
		return namedParameters;
	}

	private String buildApplicantInprogressRequisitionFilterQuery() {
		return "IdApplicant = :IdApplicant AND REQ.Status NOT IN ('REQUISITION','REQUISITION_CLOSE',"
				+ "'CANCELLED', 'RENEWED', 'CANCELED_CONTRACT') ";
	}

	@Override
	public Long countTotalRowsApplicantInProgressRequisitions(final UserInProgressRequisitionFilter filter)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createFindUserInprogressRequisitionsNamedParameters(TableConstants.ID_APPLICANT, filter);
			final String countRows = this.databaseUtils.countTotalRows(this
					.buildFindUserInProgressRequisitionsQuery(this.buildApplicantInprogressRequisitionFilterQuery()));
			return this.namedjdbcTemplate.queryForObject(countRows, namedParameters, Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildFindUserInProgressRequisitionsQuery(final String userFilter) {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT REQ.IdRequisition, USERNAME.Name, ");
		query.append("SUP.CompanyName, DT.Name AS DocumentTypeName, SC.Name AS StatusName, ");
		query.append("CAST(RST.TurnDate AS DATE) AS TurnDate, REQ.TemplateIdDocument as IdTemplateDocument, ");
		query.append(this.databaseUtils.buildDateAddDays("RST.AttentionDays", "CAST(RST.TurnDate AS DATE)"));
		query.append(" AS ExpectedAttentionDate ");
		query.append("FROM REQUISITION REQ INNER JOIN SUPPLIER SUP ON REQ.IdSupplier = SUP.IdSupplier ");
		query.append("INNER JOIN DOCUMENTTYPE DT ON REQ.IdDocumentType = DT.IdDocumentType ");
		query.append("INNER JOIN SCREEN SC ON REQ.Status = SC.FlowStatus ");
		query.append("INNER JOIN REQUISITIONSTATUSTURN RST ON REQ.IdRequisition = RST.IdRequisition ");
		query.append("INNER JOIN (SELECT IdUser, ")
		.append(this.databaseUtils.buildConcat("USERS.Name", "' '", "USERS.FirstLastName ", "' '",
				"USERS.SecondLastName"))
		.append(" AS Name FROM USERS) AS USERNAME ON REQ.IdLawyer = USERNAME.IdUser ");
		query.append("AND REQ.Status = RST.Status ");
		query.append("WHERE ").append(userFilter);
		query.append("AND RST.TurnDate = (SELECT MAX(TurnDate) FROM REQUISITIONSTATUSTURN TURNDATE ");
		query.append("WHERE TURNDATE.IdRequisition = REQ.IdRequisition) ");
		query.append("AND (:Name IS NULL OR USERNAME.Name LIKE ")
		.append(this.databaseUtils.buildConcat("'%'", ":Name", "'%'")).append(")");
		query.append(" AND (:IdRequisition IS NULL OR REQ.IdRequisition = :IdRequisition) ");
		query.append("AND (:CompanyName IS NULL OR SUP.CompanyName LIKE CONCAT('%', :CompanyName, '%')) ");
		query.append("AND (:DocumentName IS NULL OR DT.Name LIKE CONCAT('%', :DocumentName, '%')) ");
		query.append("AND (:Status IS NULL OR SC.Name LIKE CONCAT('%', :Status, '%')) ");
		query.append("AND (:StartDate IS NULL OR :EndDate IS NULL OR CAST(RST.TurnDate AS DATE) BETWEEN ");
		query.append(":StartDate AND :EndDate OR DATEADD(DAY, RST.AttentionDays, CAST(RST.TurnDate AS DATE)) ");
		query.append("BETWEEN :StartDate AND :EndDate) ");
		query.append("ORDER BY REQ.IdRequisition DESC ");

		return query.toString();
	}

	@Override
	public List<UserInProgressRequisition> findLawyerInProgressRequisitions(
			final UserInProgressRequisitionFilter filter, final Integer pageNumber, final Integer itemsNumber)
					throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createFindUserInprogressRequisitionsNamedParameters(TableConstants.ID_LAWYER, filter);
			return this.namedjdbcTemplate.query(
					this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUISITION,
							this.buildFindUserInProgressRequisitionsQuery(
									this.buildLawyerInprogressRequisitionFilterQuery()),
							pageNumber, itemsNumber),
					namedParameters, new BeanPropertyRowMapper<>(UserInProgressRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildLawyerInprogressRequisitionFilterQuery() {
		return " REQ.Status IN ('NEGOTIATOR_CONTRACT_REVIEW','USER_CONTRACT_REVIEW', "
				+ "'LOAD_SUPPLIER_AREAS_APPROVAL', 'APPROVAL_CONTRACT', 'NEGOTIATOR_SIGN_CONTRACT', "
				+ "'SACC_SIGN_CONTRACT', 'SACC_SCAN_CONTRACT') ";
	}

	@Override
	public Long countTotalRowsLawyerInProgressRequisitions(final UserInProgressRequisitionFilter filter)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createFindUserInprogressRequisitionsNamedParameters(TableConstants.ID_LAWYER, filter);
			final String countRows = this.databaseUtils.countTotalRows(
					this.buildFindUserInProgressRequisitionsQuery(this.buildLawyerInprogressRequisitionFilterQuery()));
			return this.namedjdbcTemplate.queryForObject(countRows, namedParameters, Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<TrayRequisition> obtenerSolicitudesPendientes(final TrayFilter trayFilter)
			throws DatabaseException {
		try {
			trayFilter.setStatus(FlowPurchasingEnum.IN_PROGRESS);
			final MapSqlParameterSource namedParameters = this.createfindTrayRequisitionsNamedParameters(trayFilter);
			return this.namedjdbcTemplate.query(this.obtenerSolicitudesEnProcesoPorUsuario(), namedParameters,
					new BeanPropertyRowMapper<>(TrayRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String obtenerSolicitudesEnProcesoPorUsuario() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT SUPPLIER.CompanyName AS SupplierName, DOCUMENTTYPE.Name AS DocumentTypeName, ");
//		query.append(
//				"REQUISITION.IdRequisition, convert(varchar, REQUISITION.ApplicationDate, 103) AS ApplicationDate, REQUISITION.Status, ");
		query.append("REQUISITION.IdRequisition,REQUISITION.ApplicationDate AS ApplicationDate, REQUISITION.Status, ");
		query.append("REQUISITION.ContractType as contractType, ");
		query.append("AREA.Name as area, ");
		query.append("unit.name as unidad, FINANCIALENTITY.Name AS Empresa "); // FIXME Mayus
		query.append(FROM_REQUISITION);
		query.append("LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier  ");
		query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("LEFT JOIN AREA ON REQUISITION.IdAreaTender = AREA.IdArea ");
		query.append("LEFT JOIN unit ON REQUISITION.IdUnit = unit.idUnit ");
		query.append("LEFT JOIN FLOW ON REQUISITION.IdFlow = FLOW.IdFlow ");
		query.append(
				"LEFT JOIN REQUISITIONFINANCIALENTITY ON REQUISITIONFINANCIALENTITY.IdRequisition =  REQUISITION.IdRequisition ");
		query.append(
				"LEFT JOIN FINANCIALENTITY ON FINANCIALENTITY.IdFinancialEntity = REQUISITIONFINANCIALENTITY.IdFinancialEntity ");
		query.append("LEFT JOIN REQUISITION APPLICANT ON APPLICANT.IdRequisition = REQUISITION.IdRequisition WHERE ");
		this.buildInboxFilter(query);
		query.append("(REQUISITION.IdApplicant = :IdUser OR :isUserFiltered = 0) AND REQUISITION.IdFlow = :IdFlow ");
		query.append("AND REQUISITION.Status = 'IN_PROGRESS'");
		query.append(" ORDER BY REQUISITION.IdRequisition DESC ");
		return query.toString();
	}

	@Override
	public Integer saveOrUpdatePartThree(RequisitionsPartThree requisition) throws DatabaseException {
		this.updateRequisitionPartThree(requisition);
		return requisition.getIdRequisition();
	}

	@Override
	public Integer saveOrUpdatePartFour(RequisitionsPartFour requisition) throws DatabaseException {
		this.updateRequisitionPartFour(requisition);
		return requisition.getIdRequisition();
	}

	public Integer findFirstLawyer() throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			StringBuilder st = new StringBuilder();
			st.append("SELECT TOP 1 USERS.idUser ");
			st.append("FROM USERS, PROFILE, PROFILEUSER ");
			st.append("WHERE USERS.IsLawyer = 1 AND USERS.Status = 'ACTIVE' ");
			st.append("AND PROFILEUSER.IdUser = USERS.IdUser AND PROFILEUSER.IdProfile = PROFILE.IdProfile ");
			st.append("AND PROFILE.Name = 'ABOGADO' AND PROFILE.Status = 'ACTIVE' ");
			st.append("ORDER BY USERS.idUser ASC");
			return this.namedjdbcTemplate.queryForObject(st.toString(), namedParameters, Integer.class);
		} catch (DataAccessException dataAccessException) {
			return 0;
			// throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void actualizarEvaluadorVoBoJuridico(Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.crearParametrosActualizacionEvaluador(idRequisition);
			this.namedjdbcTemplate.update(this.construirQueryActualizacionEvaluador(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource crearParametrosActualizacionEvaluador(final Integer idRequisition) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		return namedParameters;
	}
	
	private MapSqlParameterSource crearParametrosRecuperaTipoDocumentofinal(final Integer idRequisition) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		return namedParameters;
	}

	private String construirQueryActualizacionEvaluador() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_REQUISITION_SET);
		query.append("IdEvaluator = IdApplicant ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	@Override
	public Integer getIdLawyerByIdRequisition(Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.crearParametrosActualizacionEvaluador(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.buildQueryFindIdLawyerByIdRequisition(), namedParameters,
					Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildQueryFindIdLawyerByIdRequisition() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdLawyer FROM REQUISITION ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	@Override
	public Integer getIdApplicantByIdRequisition(Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.crearParametrosActualizacionEvaluador(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.buildQueryFindIdApplicantByIdRequisition(),
					namedParameters, Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildQueryFindIdApplicantByIdRequisition() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdApplicant FROM REQUISITION ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	@Override
	public List<TrayRequisition> findPaginatedTrayRequisitions(final TrayFilter trayFilter)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createfindTrayRequisitionsNamedParameters(trayFilter);
			return this.namedjdbcTemplate.query(this.buildFindRequisitionsForTrayQuery(), namedParameters,
					new BeanPropertyRowMapper<>(TrayRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	@Override
	public List<TrayRequisition> findPaginatedTrayRequisitionsPorFechas(final TrayFilter trayFilter)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createfindTrayRequisitionsNamedParameters(trayFilter);
			return this.namedjdbcTemplate.query(this.buildFindRequisitionsPorFechasForTrayQuery(), namedParameters,
					new BeanPropertyRowMapper<>(TrayRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	
	@Override
	public List<TrayRequisition> findContractsInRevision() throws DatabaseException {
		try {
			return this.namedjdbcTemplate.query(this.buildFindContractsInRevision(),
					new BeanPropertyRowMapper<>(TrayRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	
	@Override
	public List<TrayRequisition> findContractsInSignatures() throws DatabaseException {
		try {
			return this.namedjdbcTemplate.query(this.buildFindContractsInSignatures(),
					new BeanPropertyRowMapper<>(TrayRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void updateContractRiskByIdRequisition(final Integer idRequisition, final boolean contractRisk)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.crearParametrosActualizacionEvaluador(idRequisition);
			namedParameters.addValue(TableConstants.CONTRACT_RISK, contractRisk);
			this.namedjdbcTemplate.update(this.buildUpdateContractRiskQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildUpdateContractRiskQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITION SET ContractRisk = :ContractRisk ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	@Override
	public void updateVoBoContractRiskByIdRequisition(final Integer idRequisition, final boolean voboContractRisk)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.crearParametrosActualizacionEvaluador(idRequisition);
			namedParameters.addValue(TableConstants.VOBO_CONTRACT_RISK, voboContractRisk);
			this.namedjdbcTemplate.update(this.buildUpdateVoBoContractRiskQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildUpdateVoBoContractRiskQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITION SET VoBocontractRisk = :VoBocontractRisk ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	@Override
	public FlowPurchasingEnum getStatusByIdRequisition(Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.crearParametrosActualizacionEvaluador(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.buildQueryFindStatustByIdRequisition(), namedParameters,
					FlowPurchasingEnum.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildQueryFindStatustByIdRequisition() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT Status FROM REQUISITION ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	public Integer findFirstJuristic() throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			StringBuilder st = new StringBuilder();
			st.append("SELECT TOP 1 USERS.idUser ");
			st.append("FROM USERS, PROFILE, PROFILEUSER ");
			st.append("WHERE USERS.Status = 'ACTIVE' ");
			st.append("AND PROFILEUSER.IdUser = USERS.IdUser AND PROFILEUSER.IdProfile = PROFILE.IdProfile ");
			st.append("AND PROFILE.Name = 'JURIDICO' AND PROFILE.Status = 'ACTIVE' ");
			st.append("ORDER BY USERS.idUser ASC");
			return this.namedjdbcTemplate.queryForObject(st.toString(), namedParameters, Integer.class);
		} catch (DataAccessException dataAccessException) {
			return 0;
		}
	}

	@Override
	public Integer findTemplateIdDocumentByIdRequisition (final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.crearParametrosActualizacionEvaluador(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.buildQueryFindIdDocumentByIdRequisition(),
					namedParameters, Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	@Override
	public Integer findTemplateIdSupplierDocumentByIdRequisition (final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.crearParametrosActualizacionEvaluador(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.buildQueryFindIdSupplierDocumentByIdRequisition(),
					namedParameters, Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildQueryFindIdDocumentByIdRequisition() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT TemplateIdDocument FROM REQUISITION ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}
	
	private String buildQueryFindIdSupplierDocumentByIdRequisition() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT supplierApprovalIdDocument FROM REQUISITION ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}
	
	@Override
	public String findTemplateNameDocumentByIdRequisition (final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.crearParametrosRecuperaTipoDocumentofinal(idRequisition);
			return this.namedjdbcTemplate.queryForObject(this.buildQueryFindNameDocumentByIdRequisition(),
					namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	
	private String buildQueryFindNameDocumentByIdRequisition() {
		final StringBuilder query = new StringBuilder();	
		
		query.append("SELECT DocumentTypeEnum ");
		query.append("FROM DOCUMENTTYPE d ");
		query.append("INNER JOIN REQUISITION r ON r.IdDocumentType = d.IdDocumentType AND r.IdRequisition = :IdRequisition ");
		return query.toString();
	}

	@Override
	public List<SupplierPersonByRequisition> getIdsSupplierPersonByIdRequisition (final Integer idRequisition, final Integer idSupplier) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createGetIdsSupplierPerson(idRequisition, idSupplier);
			return this.namedjdbcTemplate.query(this.buildGetIdsSupplierPersonByIdRequisition(), namedParameters,
					new BeanPropertyRowMapper<SupplierPersonByRequisition>(SupplierPersonByRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource createGetIdsSupplierPerson(final Integer idRequisition, final Integer idSupplier) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
		namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
		return namedParameters;
	}

	private String buildGetIdsSupplierPersonByIdRequisition() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT SPR.Id AS Id, SPR.IdRequisition AS IdRequisition, SPR.IdSupplier AS IdSupplier, SPR.IdSupplierPerson AS IdSupplierPerson ");
		query.append("FROM SUPPLIERPERSONBYREQUISITION AS SPR ");
		query.append("WHERE SPR.IdRequisition = :IdRequisition and SPR.IdSupplier = :IdSupplier ");
		return query.toString();
	}


	@Override
	public List<SupplierPersonByRequisition> getIdsSupplierPersonByIdRequisitionDTO (final Integer idRequisition, final Integer idSupplier) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createGetIdsSupplierPerson(idRequisition, idSupplier);
			return this.namedjdbcTemplate.query(this.buildGetIdsSupplierPersonByIdRequisitionDTO(), namedParameters,
					new BeanPropertyRowMapper<SupplierPersonByRequisition>(SupplierPersonByRequisition.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	private String buildGetIdsSupplierPersonByIdRequisitionDTO() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT SPR.Id AS Id, SPR.IdRequisition AS IdRequisition, SPR.IdSupplier AS IdSupplier, ");
		query.append("SPR.IdSupplierPerson AS IdSupplierPerson, SP.Name AS Name, SP.SupplierPersonType AS type ");
		query.append("FROM SUPPLIERPERSONBYREQUISITION AS SPR ");
		query.append("INNER JOIN SUPPLIERPERSON AS SP ON SP.IdSupplierPerson = SPR.IdSupplierPerson ");
		query.append("WHERE SPR.IdRequisition = :IdRequisition and SPR.IdSupplier = :IdSupplier ");
		return query.toString();
	}

	@Override
	public Integer saveSupplierPersonByIdRequisition(SupplierPersonByRequisition person) throws DatabaseException {
		try {
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			final MapSqlParameterSource namedParameters = this.createSaveSupplierPersonNamedParameters(person.getIdRequisition(), person.getIdSupplier(), person.getIdSupplierPerson());
			this.namedjdbcTemplate.update(this.buildSaveSaveSupplierPersonByRequisitionQuery(), namedParameters, keyHolder, new String[]{"Id"});
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource createSaveSupplierPersonNamedParameters(final Integer idRequisition, final Integer idSupplier, 
			final Integer idPerson) {
		final MapSqlParameterSource namedParameters = this.idRequisitionParameterSource(idRequisition);
		namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
		namedParameters.addValue(TableConstants.ID_SUPPLIER_PERSON, idPerson);
		return namedParameters;
	}

	private String buildSaveSaveSupplierPersonByRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO SUPPLIERPERSONBYREQUISITION (IdRequisition, IdSupplier, IdSupplierPerson) ");
		query.append("VALUES (:IdRequisition, :IdSupplier, :IdSupplierPerson)");
		return query.toString();
	}

	@Override
	public void deleteSupplierPersonByIdSupplierPerson(Integer id) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("Id", id);
			this.namedjdbcTemplate.update(this.buildDeleteSupplierPersonByIdSupplierPersonQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildDeleteSupplierPersonByIdSupplierPersonQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM SUPPLIERPERSONBYREQUISITION ");
		query.append(WHERE_ID_SUPPLIER_PERSON_EQUALS_ID_SUPPLIER_PERSON);
		return query.toString();
	}
	private void buildSelectAllDistinctForTrayQuery2(final StringBuilder query) {
//		query.append("SELECT COUNT(*)  ");
//		query.append("FROM ( ");
		query.append("SELECT DISTINCT SUPPLIER.CompanyName AS SupplierName, DOCUMENTTYPE.Name AS DocumentTypeName,");
		query.append("REQUISITION.IdRequisition, REQUISITION.ApplicationDate, REQUISITION.Status, ");
		query.append(
				"CONCAT (USERLAWYER.Name, ' ' , USERLAWYER.FirstLastName, ' ', USERLAWYER.SecondLastName) As LawyerName, ");
		query.append("TURN.Turn AS Retry, COMMENTS.CommentText AS Comment, TURN.TurnDate, ");
		query.append("REQUISITION.ContractType as contractType, ");
		query.append("AREA.Name as area, ");
		query.append("unit.name as unidad, "); // FIXME ENTITY

		this.buildSemaphoreFields2(query);
		this.buildRequisitionJoins22(query);
	}
	private void buildSemaphoreFields2(final StringBuilder query) {
		final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
		final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
		query.append("CASE ");
		query.append(WHEN).append(
				this.databaseUtils.buildDateDiffSSqlFunction(formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
		query.append(" < REDSEMAPHORE.RedSemaphore");
		query.append(" * TURN.AttentionDays THEN 'RED' ");
		query.append(WHEN).append(
				this.databaseUtils.buildDateDiffSSqlFunction(formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
		query.append(" < YELLOWSEMAPHORE.YellowSemaphore");
		query.append(" * TURN.AttentionDays THEN 'YELLOW' ");
		query.append("ELSE 'GREEN' END AS Semaphore, FINANCIALENTITY.Name AS Empresa, ");
		query.append("TURN.Turn AS Turn, TURN.AttentionDays AS AttentionDays ");
	}
	private void buildRequisitionJoins22(final StringBuilder query) {
		query.append("FROM REQUISITION ");
		query.append(
				" LEFT JOIN REQUISITIONFINANCIALENTITY ON REQUISITIONFINANCIALENTITY.IdRequisition =  REQUISITION.IdRequisition ");
		query.append(
				" LEFT JOIN FINANCIALENTITY ON FINANCIALENTITY.IdFinancialEntity = REQUISITIONFINANCIALENTITY.IdFinancialEntity ");
		query.append(" LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
		query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("LEFT JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
		query.append("LEFT JOIN AREA ON REQUISITION.IdAreaTender = AREA.IdArea ");
		query.append("LEFT JOIN unit ON REQUISITION.IdUnit = unit.idUnit ");

	}
	private void buildFindRequisitionsForTrayJoinsQuery2(final StringBuilder query) {
		query.append(
				"INNER JOIN REQUISITIONSTATUSTURN TURN ON REQUISITION.IdRequisition = TURN.IdRequisition AND REQUISITION.Status = TURN.Status ");
		query.append("LEFT JOIN REQUISITION LAWYERSUBDIRECTOR ");
		query.append("ON LAWYERSUBDIRECTOR.IdRequisition = REQUISITION.IdRequisition ");
		query.append("LEFT JOIN USERS ON USERS.IdUser = REQUISITION.IdApplicant ");
		query.append("LEFT JOIN USERS USERLAWYER ON USERLAWYER.IdUser = REQUISITION.IdLawyer ");
		query.append("LEFT JOIN REQUISITIONUSERSVOBO USERSVOBO ");
		query.append("ON USERSVOBO.IdRequisition = REQUISITION.IdRequisition ");
		query.append("LEFT JOIN REQUISITION USERSVOBOREQUISITION ");
		query.append("ON USERSVOBOREQUISITION.IdRequisition = USERSVOBO.IdRequisition ");
		query.append("LEFT JOIN COMMENTS ON COMMENTS.IdRequisition = REQUISITION.IdRequisition ");
		query.append("AND COMMENTS.FlowStatus = :Status ");
		query.append("LEFT JOIN (SELECT IdRequisition, MAX(CreationDate) AS CreationDate FROM COMMENTS WHERE ");
		query.append("FlowStatus = :Status GROUP BY ");
		query.append("IdRequisition) RECENT_COMMENT ON COMMENTS.IdRequisition = RECENT_COMMENT.IdRequisition,");
		query.append(
				"(SELECT CAST(Value AS DECIMAL(3,2)) AS RedSemaphore FROM CONFIGURATION WHERE Name = 'RED_SEMAPHORE_PERCENTAGE') REDSEMAPHORE,");
		query.append(
				"(SELECT CAST(Value AS DECIMAL(3,2)) AS YellowSemaphore FROM CONFIGURATION WHERE Name = 'YELLOW_SEMAPHORE_PERCENTAGE') YELLOWSEMAPHORE ");
	}
	private void buildFindRequisitionsForTrayFiltersQueryFechas(final StringBuilder query) {
		query.append("WHERE REQUISITION.IdFlow = :IdFlow AND REQUISITION.Status = :Status ");
		query.append("AND (COMMENTS.CreationDate = RECENT_COMMENT.CreationDate OR COMMENTS.CreationDate IS NULL) AND ");
		this.buildInboxFilter2(query);
		query.append(
				"TURN.TurnDate = (SELECT MAX(TurnDate) FROM REQUISITIONSTATUSTURN TURNDATE WHERE TURNDATE.IdRequisition = REQUISITION.IdRequisition) AND (");
		query.append(
				"((REQUISITION.IdApplicant = :IdUser OR :isUserFiltered = 0) AND :IdFlow = REQUISITION.IdFlow AND :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANT_BUSY_STATUS'"));
		query.append(
				")) OR ((USERS.IdUnderdirector = :IdUser OR :isUserFiltered = 0) AND :IdFlow = REQUISITION.IdFlow AND :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANTSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR (:IdFlow = LAWYERSUBDIRECTOR.IdFlow AND :Status = LAWYERSUBDIRECTOR.Status AND LAWYERSUBDIRECTOR.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYERSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR ((REQUISITION.IdLawyer = :IdUser OR :isUserFiltered = 0) AND :IdFlow = REQUISITION.IdFlow AND :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils
				.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYER_BUSY_STATUS'"));
		query.append(
				")) OR ((USERSVOBO.IdUser = :IdUser OR :isUserFiltered = 0) AND USERSVOBO.IsVoBoGiven = 0 AND USERSVOBOREQUISITION.IdFlow = :IdFlow AND :Status = USERSVOBOREQUISITION.Status AND USERSVOBOREQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'USERSVOBOREQUISITION_BUSY_STATUS'"));
		query.append(
				")) OR ((REQUISITION.IdEvaluator = :IdUser OR :isUserFiltered = 0) AND :IdFlow = REQUISITION.IdFlow AND :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'EVALUATOR_BUSY_STATUS'"));
		query.append("))) ");
//		query.append("AND REQUISITION.StageStartDate BETWEEN :StartDate and :EndDate ");
		query.append("AND TURN.TurnDate BETWEEN :StartDate and :EndDate ");
	}


}
