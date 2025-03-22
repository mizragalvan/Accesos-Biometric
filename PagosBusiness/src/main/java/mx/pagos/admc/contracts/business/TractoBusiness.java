package mx.pagos.admc.contracts.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.TractoInterface;
import mx.pagos.admc.contracts.structures.Tracto;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class TractoBusiness {

	@Autowired
	private TractoInterface tractoInterface;

	public Integer save(Tracto tracto) throws BusinessException {
		try {
			return tractoInterface.save(tracto);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al guardar datos del tracto", e);
		}
	}

	public Integer saveOrUpdate(Tracto tracto) throws BusinessException {
		try {
			return tractoInterface.saveOrUpdate(tracto);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al guardar datos del tracto", e);
		}
	}

	public List<Tracto> getTractoByIdRequisition(Integer idRequisition) throws BusinessException {
		try {
			return tractoInterface.getTractoByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al consultar datos del tracto", e);
		}
	}

	public Tracto getTractoById(Integer idTracto) throws BusinessException {
		try {
			return tractoInterface.getTractoById(idTracto);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al consultar datos del tracto", e);
		}
	}

	public List<Tracto> getTractoAll() throws BusinessException {
		try {
			return tractoInterface.getTractoAll();
		} catch (DatabaseException e) {
			throw new BusinessException("Error al consultar datos del tracto", e);
		}
	}

	public Integer deleteTractoById(Integer idTracto) throws BusinessException {
		try {
			return tractoInterface.deleteTractoById(idTracto);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al borrar datos del tracto", e);
		}
	}

	public Integer deleteTractoByIdRequisition(Integer idRequisition) throws BusinessException {
		try {
			return tractoInterface.deleteTractoByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al borrar datos del tracto", e);
		}
	}

}
