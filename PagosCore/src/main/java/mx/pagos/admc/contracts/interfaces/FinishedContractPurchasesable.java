package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.FinishedContractPurchases;
import mx.pagos.general.exceptions.DatabaseException;

public interface FinishedContractPurchasesable {

    List<FinishedContractPurchases> findFinishedContractsPurchases(
    		FinishedContractPurchases vo) throws DatabaseException;
}
