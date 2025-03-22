package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.RedFlag;
import mx.pagos.general.exceptions.DatabaseException;

public interface RedFlagable {
	
	Integer save(RedFlag redFlag) throws DatabaseException;
    
    List<RedFlag> findByIdRequisition(Integer idRequisition) throws DatabaseException;

}
