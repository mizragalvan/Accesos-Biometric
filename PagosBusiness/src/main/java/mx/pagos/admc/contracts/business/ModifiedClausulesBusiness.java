package mx.pagos.admc.contracts.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.ModifiedClausuleable;
import mx.pagos.admc.contracts.structures.ModifiedClausules;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class ModifiedClausulesBusiness {

	@Autowired
	private ModifiedClausuleable clausuleable;

	public final Integer saveOrUpdate(ModifiedClausules clausules) throws BusinessException {
		try {
			return clausuleable.saveOrUpdate(clausules);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al guardar datos de las clausulas", e);
		}
	}

	public final ModifiedClausules getModifiedClausulesById(final Integer idModifiedClause) throws BusinessException {
		try {
			return clausuleable.getClausulesById(idModifiedClause);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener datos de las clausulas", e);
		}
	}

	public final List<ModifiedClausules> getAll() throws BusinessException {
		try {
			return clausuleable.getAll();
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener datos de las clausulas", e);
		}

	}

	public final List<ModifiedClausules> getModifiedClausulesByIdRequisition(final Integer idRequisition)
			throws BusinessException {
		try {
			return clausuleable.getClausulesByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener datos de las clausulas", e);
		}
	}
	public final Integer deleteModifiedClausulesById(final Integer idModifiedClause) throws BusinessException {
		try {
			return clausuleable.deleteClausulesById(idModifiedClause);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al borrar datos de las clausulas", e);
		}
	}
	
	public final Integer deleteModifiedClausulesByIdRequisition(final Integer idRequisition) throws BusinessException {
		try {
			return clausuleable.deleteClausulesByIdRequisition(idRequisition);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al borrar datos de las clausulas", e);
		}
	}
	public final Integer save(ModifiedClausules clausules)throws BusinessException {
		try {
			return clausuleable.save(clausules);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al guardar datos de las clausulas", e);
		}
	}
	
	
}
