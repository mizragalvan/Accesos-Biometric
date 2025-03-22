package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Tracto;
import mx.pagos.general.exceptions.DatabaseException;

public interface TractoInterface {

	Integer save(final Tracto tracto) throws DatabaseException;
	Integer saveOrUpdate(final Tracto tracto) throws DatabaseException;
	List<Tracto>  getTractoByIdRequisition(final Integer idRequisition) throws DatabaseException;
	Tracto getTractoById(final Integer idTracto) throws DatabaseException;
	List<Tracto> getTractoAll() throws DatabaseException;
	Integer deleteTractoById(final Integer idTracto) throws DatabaseException;
	Integer deleteTractoByIdRequisition(final Integer idRequisition) throws DatabaseException;
}
