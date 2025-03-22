package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.ModifiedClausules;
import mx.pagos.general.exceptions.DatabaseException;

public interface ModifiedClausuleable {
	Integer save(final ModifiedClausules clausules) throws DatabaseException;
	Integer saveOrUpdate(final ModifiedClausules clausules) throws DatabaseException;
	ModifiedClausules getClausulesById(final Integer idModifiedClause) throws DatabaseException;
	List<ModifiedClausules> getClausulesByIdRequisition(final Integer idRequisition) throws DatabaseException;
	List<ModifiedClausules> getAll() throws DatabaseException;
	Integer deleteClausulesById(final Integer idModifiedClause) throws DatabaseException;
	Integer deleteClausulesByIdRequisition(final Integer idRequisition) throws DatabaseException;
}
