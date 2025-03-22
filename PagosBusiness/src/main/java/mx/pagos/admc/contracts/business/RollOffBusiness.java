package mx.pagos.admc.contracts.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.RollOffInterface;
import mx.pagos.admc.contracts.structures.RollOff;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class RollOffBusiness {

	@Autowired
	private RollOffInterface rollOffInterface;

	public Integer save(RollOff rollOff) throws BusinessException {
		try {
			return rollOffInterface.save(rollOff);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al guardar datos del ROLLOFF", e);
		}
	}

	public Integer saveOrUpdate(RollOff rollOff) throws BusinessException {
		try {
			return rollOffInterface.saveOrUpdate(rollOff);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al guardar datos del ROLLOFF", e);
		}
	}

	public List<RollOff> getRollOffByIdRequisition(Integer idRequisition) throws BusinessException {
		try {
			return rollOffInterface.getRollOffByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al consultar datos del ROLLOFF", e);
		}
	}

	public RollOff getRollOffById(Integer idRollOff) throws BusinessException {
		try {
			return rollOffInterface.getRollOffById(idRollOff);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al consultar datos del ROLLOFF", e);
		}
	}

	public List<RollOff> getRollOffAll() throws BusinessException {
		try {
			return rollOffInterface.getRollOffAll();
		} catch (DatabaseException e) {
			throw new BusinessException("Error al consultar datos del ROLLOFF", e);
		}
	}

	public Integer deleteRollOffById(Integer idRollOff) throws BusinessException {
		try {
			return rollOffInterface.deleteRollOffById(idRollOff);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al borrar datos del ROLLOFF", e);
		}
	}

	public Integer deleteRollOffByIdRequisition(Integer idRequisition) throws BusinessException {
		try {
			return rollOffInterface.deleteRollOffByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al borrar datos del ROLLOFF", e);
		}
	}

}
