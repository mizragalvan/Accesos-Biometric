package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Flowable {

	Integer saveOrUpdate(final Flow flow) throws DatabaseException;

	void changeFlowStatus(final Integer idFlow, final RecordStatusEnum status) 
			throws DatabaseException;
	
	List<Flow> findAll() throws DatabaseException;
	
	Flow findByFlowName(final String flowName) throws DatabaseException;
	
	List<Flow> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;
	
	Flow findById(final Integer idFlow) throws DatabaseException;
	
	List<String> findStepListByIdFlow(final Integer idFlow) throws DatabaseException;

    Boolean isManagerialFlow(Integer idFlow) throws DatabaseException;
    
    List<Flow> findPurchasingFlows() throws DatabaseException;
}
