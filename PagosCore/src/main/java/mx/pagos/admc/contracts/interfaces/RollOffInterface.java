package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.RollOff;
import mx.pagos.general.exceptions.DatabaseException;

public interface RollOffInterface {
	Integer save(final RollOff rollOff) throws DatabaseException;
	Integer saveOrUpdate(final RollOff rollOff) throws DatabaseException;
	List<RollOff>  getRollOffByIdRequisition(final Integer idRequisition) throws DatabaseException;
	RollOff getRollOffById(final Integer idRollOff) throws DatabaseException;
	List<RollOff> getRollOffAll() throws DatabaseException;
	Integer deleteRollOffById(final Integer idRollOff) throws DatabaseException;
	Integer deleteRollOffByIdRequisition(final Integer idRequisition) throws DatabaseException;
}
