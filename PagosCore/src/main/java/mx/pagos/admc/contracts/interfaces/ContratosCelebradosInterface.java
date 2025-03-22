package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.FiltrosGrafica;
import mx.pagos.general.exceptions.DatabaseException;

public interface ContratosCelebradosInterface {

	<T> List<T> obtnerListaContratosCelebrados(FiltrosGrafica params) throws DatabaseException ;
	<T> List<T> obtnerListaTotalContratosCelebrados(FiltrosGrafica params) throws DatabaseException ;
	int selectCountPagination(FiltrosGrafica params) throws DatabaseException;
}
