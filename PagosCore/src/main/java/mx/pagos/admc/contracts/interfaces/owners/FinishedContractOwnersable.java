package mx.pagos.admc.contracts.interfaces.owners;

import java.util.List;

import mx.pagos.admc.contracts.structures.owners.FinishedContractOwners;
import mx.pagos.general.exceptions.DatabaseException;

public interface FinishedContractOwnersable {

    List<FinishedContractOwners> findFinishedContractsOwners(
    		FinishedContractOwners vo) throws DatabaseException;
}
