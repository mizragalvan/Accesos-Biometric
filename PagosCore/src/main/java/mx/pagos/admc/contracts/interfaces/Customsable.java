package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Customs;
import mx.pagos.general.exceptions.DatabaseException;

public interface Customsable {
	Integer save(final Customs customs) throws DatabaseException;
	Integer saveOrUpdate(final Customs customs) throws DatabaseException;
	Customs getCustomsById(final Integer idCusInteger) throws DatabaseException;
	List<Customs> getCustomsByIdRequisition(final Integer idRequisition) throws DatabaseException;
	List<Customs> getAll() throws DatabaseException;
	Integer deleteCustomsById(final Integer idCustoms) throws DatabaseException;
	Integer deleteCustomsByIdRequisition(final Integer idRequisition) throws DatabaseException;
}
