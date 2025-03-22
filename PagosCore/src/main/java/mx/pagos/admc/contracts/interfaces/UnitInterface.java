package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Unit;
import mx.pagos.general.exceptions.DatabaseException;

public interface UnitInterface{
	
	Integer saveOrUpdate(final Unit unit) throws DatabaseException;
	Unit getUnitById(final Integer idUnit) throws DatabaseException;
	List<Unit> getAll() throws DatabaseException;
	Integer deleteUnitById(final Integer idUnit) throws DatabaseException;

}
