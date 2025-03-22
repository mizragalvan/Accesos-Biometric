package mx.pagos.admc.contracts.daos.owners;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.owners.RequisitionOwnersStatusable;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.contracts.structures.StatusBranch;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class RequisitionOwnersStatusDAO implements RequisitionOwnersStatusable {
    private static final String WHERE_ID_EQUALS_ID = "WHERE IdRequisitionOwners = :IdRequisitionOwners ";
    private static final String FROM_REQUISITIONOWNERSSTATUSTURN = "FROM REQUISITIONOWNERSSTATUSTURN ";
    private static final String WHERE_ID_EQUALS_ID_AND_STATUS_EQUALS_STATUS =
            "WHERE IdRequisitionOwners = :IdRequisitionOwners AND Status = :Status";
    private static final String FROM_REQUISITIONOWNERSSTATUS = "FROM REQUISITIONOWNERSSTATUS ";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;


    
    @Override
    public void insertStatus(final StatusBranch statusBranch) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(statusBranch);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionOwnerStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public Boolean statusExists(final StatusBranch statusBranch) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(statusBranch);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            return this.namedjdbcTemplate.queryForObject(this.buildRequisitionOwnerStatusExistsQuery(),
                    namedParameters, Boolean.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void deleteStatus(final StatusBranch statusBranch) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(statusBranch);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.buildDeleteRequisitionOwnerStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public Boolean isBranchToBeEnded(final StatusBranch statusBranch) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(statusBranch);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            return this.namedjdbcTemplate.queryForObject(this.buildIsBranchToBeEndedQuery(),
                    namedParameters, Boolean.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void deleteStatusByBranch(final StatusBranch statusBranch) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(statusBranch);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.buildDeleteRequisitionOwnerStatusByBranchQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<StatusBranch> findStatusById(final Integer idRequisitionOwners) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionOwners);
            return this.namedjdbcTemplate.query(this.buildFindStatusByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(StatusBranch.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public Boolean isStatusAsigned(final StatusBranch statusBranch) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(statusBranch);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            return this.namedjdbcTemplate.queryForObject(this.buildIsStatusAsignedQuery(),
                    namedParameters, Boolean.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void saveRequisitionStatusTurn(final Integer idRequisitionOwners, final FlowPurchasingEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createSaveRequisitionStatusTurnNamedParameters(idRequisitionOwners, status);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionStatusTurnQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public Integer findCurrentTurnByIdRequisitionAndStatus(final Integer idRequisitionOwners,
            final FlowPurchasingEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createSaveRequisitionStatusTurnNamedParameters(idRequisitionOwners, status);
            return this.namedjdbcTemplate.queryForObject(this.buildFindCurrentTurnByIdRequisitionAndStatusQuery(),
                    namedParameters, Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void saveRequisitionStatusTurnAttentionDays(final RequisitionStatusTurn requisitionStatusTurn)
            throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(requisitionStatusTurn);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.buildSaveRequisitionStatusTurnAttentionDaysQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void deleteAllStatusByIdRequisition(final Integer idRequisitionOwners) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idRequisitionOwners);
            this.namedjdbcTemplate.update(this.buildDeleteAllStatusByIdRequisitionQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildDeleteAllStatusByIdRequisitionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONOWNERSSTATUS ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }

    private String buildFindCurrentTurnByIdRequisitionAndStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT MAX(Turn) AS Turn ");
        query.append(FROM_REQUISITIONOWNERSSTATUSTURN);
        query.append(WHERE_ID_EQUALS_ID_AND_STATUS_EQUALS_STATUS);
        return query.toString();
    }

    private String buildDeleteRequisitionOwnerStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONOWNERSSTATUS ");
        query.append(WHERE_ID_EQUALS_ID_AND_STATUS_EQUALS_STATUS);
        return query.toString();
    }

    private String buildSaveRequisitionOwnerStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERSSTATUS (IdRequisitionOwners, Status, HoldForBranch) ");
        query.append("VALUES (:IdRequisitionOwners, :Status, :HoldForBranch)");
        return query.toString();
    }
    
    private String buildRequisitionOwnerStatusExistsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(1) AS requisitionOwnerStatusExists ");
        query.append(FROM_REQUISITIONOWNERSSTATUS);
        query.append("WHERE IdRequisitionOwners = 1 AND Status = ''");
        return query.toString();
    }
    
    private String buildIsBranchToBeEndedQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT CASE ");
        query.append("WHEN RequisitionBranchesCount + 1 = FlowBranchesCount THEN 1 ");
        query.append("ELSE 0 ");
        query.append("END AS isBranchToBeEnded ");
        query.append("FROM ( ");
        query.append("SELECT COUNT(1) AS RequisitionBranchesCount ");
        query.append(FROM_REQUISITIONOWNERSSTATUS);
        query.append("WHERE IdRequisitionOwners = :IdRequisitionOwners AND HoldForBranch = :HoldForBranch ");
        query.append(") AS RequisitionStatusBranch, (");
        query.append("SELECT COUNT(1) AS FlowBranchesCount ");
        query.append("FROM FLOWSCREENACTION ");
        query.append("WHERE HoldForBranch = :HoldForBranch ");
        query.append(") AS FlowBranch ");
        return query.toString();
    }
    
    private String buildDeleteRequisitionOwnerStatusByBranchQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM REQUISITIONOWNERSSTATUS WHERE HoldForBranch = :HoldForBranch");
        return query.toString();
    }
    
    private String buildFindStatusByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT Status, HoldForBranch ");
        query.append(FROM_REQUISITIONOWNERSSTATUS);
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdNamedParameters(final Integer idRequisitionOwners) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwners);
        return namedParameters;
    }
    
    private String buildIsStatusAsignedQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(1) AS IsStatusAsigned ");
        query.append(FROM_REQUISITIONOWNERSSTATUS);
        query.append(WHERE_ID_EQUALS_ID_AND_STATUS_EQUALS_STATUS);
        return query.toString();
    }
    
    private String buildSaveRequisitionStatusTurnQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO REQUISITIONOWNERSSTATUSTURN (IdRequisitionOwners, Status, Turn) ");
        query.append("SELECT :IdRequisitionOwners, :Status, COALESCE(MAX(Turn), -1) + 1 AS Turn ");
        query.append(FROM_REQUISITIONOWNERSSTATUSTURN);
        query.append(WHERE_ID_EQUALS_ID_AND_STATUS_EQUALS_STATUS);
        return query.toString();
    }

    private MapSqlParameterSource createSaveRequisitionStatusTurnNamedParameters(
            final Integer idRequisitionOwners, final FlowPurchasingEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwners);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String buildSaveRequisitionStatusTurnAttentionDaysQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE REQUISITIONOWNERSSTATUSTURN SET AttentionDays = :AttentionDays ");
        query.append("WHERE IdRequisitionOwners = :IdRequisition AND Status = :Status AND Turn = :Turn");
        return query.toString();
    }
}
