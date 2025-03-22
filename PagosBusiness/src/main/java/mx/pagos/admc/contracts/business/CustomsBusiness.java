package mx.pagos.admc.contracts.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.Customsable;
import mx.pagos.admc.contracts.structures.Customs;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class CustomsBusiness {
	@Autowired
	private Customsable customsable;

	public final Integer saveOrUpdate(Customs customs) throws BusinessException {
		try {
			return customsable.saveOrUpdate(customs);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al guardar datos de la aduana", e);
		}
	}

	public final Customs getCustomById(final Integer idCustoms) throws BusinessException {
		try {
			return customsable.getCustomsById(idCustoms);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener la aduanas por ID", e);
		}
	}

	public final List<Customs> getAll() throws BusinessException {

		try {
			return customsable.getAll();
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener la lista de aduanas", e);
		}
	}

	public final List<Customs> getCustomsByIdRequisition(final Integer idRequisition) throws BusinessException {

		try {
			return customsable.getCustomsByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener la lista de aduanas", e);
		}
	}

	public final Integer deleteCustomById(final Integer idCustoms) throws BusinessException {
		try {
			return customsable.deleteCustomsById(idCustoms);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al eliminar la aduana", e);

		}
	}

	public final Integer deleteCustomByIdRequisition(final Integer idRequisition) throws BusinessException {
		try {
			return customsable.deleteCustomsByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al eliminar la aduana", e);
		}
	}

	public Integer save(Customs customs) throws BusinessException {
		try {
			return customsable.save(customs);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al eliminar la aduana", e);
		}
	}
}
