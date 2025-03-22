package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.FlowScreenAction;
import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.contracts.structures.StatusBranch;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

import org.springframework.dao.EmptyResultDataAccessException;

/**
 * 
 * @author Mizraim
 *
 * @see DatabaseException
 * @see EmptyResultException
 *
 */

public interface FlowScreenActionable {
    String findNextStatus(FlowScreenAction flowScreenAction) throws DatabaseException, EmptyResultException;
	
	Integer saveFlowScreenAction(FlowScreenAction flowScreenAction) throws DatabaseException;
	
	void deleteFlowScreenAction(Integer idFlow) throws DatabaseException;

	List<FlowScreenAction> findFlowScreenActionByFlow(FlowScreenAction flowScreenAction)
	        throws EmptyResultDataAccessException, DatabaseException;

	List<FlowScreenAction> findStatusByFlow(Integer idFlow) throws DatabaseException;

	Integer findRequisitionStep(Integer idRequisition) throws DatabaseException;
	
	Integer findRequisitionOwnerStep(Integer idRequisitionOwner) throws DatabaseException;

	List<Screen> findStatusNameByFlow(Integer idFlow) throws DatabaseException;

    List<StatusBranch> findNextMultipleStatus(FlowScreenAction flowScreenAction) throws DatabaseException;
}
