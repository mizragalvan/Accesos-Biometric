package mx.pagos.admc.contracts.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import mx.pagos.admc.contracts.interfaces.UnitInterface;
import mx.pagos.admc.contracts.structures.Unit;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class UnitBusiness {

	@Autowired
	private UnitInterface unitRepository;
	
	public final Integer saveOrUpdate(Unit unit) throws BusinessException {
		try {
			return unitRepository.saveOrUpdate(unit);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al guardar datos de la Unidad", e);
		}
	}
	
	public final Unit getUnitById(final Integer idUnit)throws BusinessException {
		try {
			return unitRepository.getUnitById(idUnit);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener la unidad por ID", e);
		}
	}
	
	public final List<Unit> getAll() throws BusinessException{
		
		try {
			return unitRepository.getAll();
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener la lista de unidades", e);
		}
	}

	public final Integer deleteUnitById(final Integer idUnit) throws BusinessException{
		try {
			return unitRepository.deleteUnitById(idUnit);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al eliminar la unidad", e);
		}
	}
}
