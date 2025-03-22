package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.Anexable;
import mx.pagos.admc.contracts.structures.FolioByPlantilla;
import mx.pagos.admc.contracts.structures.TagField;
import mx.pagos.admc.contracts.structures.TypeByAnexo;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class AnexoDAO implements Anexable {

	private static final String FROM_TYPEBYANEXO = QueryConstants.FROM + TableConstants.TABLE_TYPEBYANEXO;

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;



	@Override
	public Integer save(TypeByAnexo anexo) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createInsertCommentNamedParameters(anexo);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertCommentQuery(), namedParameters, keyHolder,
					new String[] { "IdTypeByAnexo" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al insertar redFlag", dataAccessException);
		}
	}

	@Override
	public List<TypeByAnexo> findTypesByAnexosAndPerson(Integer idDocumentType, boolean moral, int folio)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdDocumentType(idDocumentType, moral, folio);
			return this.namedjdbcTemplate.query(this.buildFindByIdDocumentTypenQuery(), namedParameters,
					new BeanPropertyRowMapper<TypeByAnexo>(TypeByAnexo.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener: redflats", dataAccessException);
		}
	}
	
	@Override
	public List<TypeByAnexo> findTypesByFolio(Integer folio)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.FOLIO, folio);
			return this.namedjdbcTemplate.query(this.buildFindByFolio(), namedParameters,
					new BeanPropertyRowMapper<TypeByAnexo>(TypeByAnexo.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener: redflats", dataAccessException);
		}
	}
	
	@Override
	public List<TagField> findTagsAnexos()
			throws DatabaseException {
		try {
            return this.namedjdbcTemplate.query(this.buildFindAllTags(), 
                    new BeanPropertyRowMapper<TagField>(TagField.class));   
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
	@Override
	public List<FolioByPlantilla> findFolioByDocumentType(Integer idDocumentType)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_DOCUMENT_TYPE, idDocumentType);
			return this.namedjdbcTemplate.query(this.buildFindFolioByIdDocumentType(), namedParameters,
					new BeanPropertyRowMapper<FolioByPlantilla>(FolioByPlantilla.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener: folios", dataAccessException);
		}
	}

	@Override
	public List<TypeByAnexo> findTypesByAnexosAndPersonOrderPage(TypeByAnexo anexo) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdDocumentType(anexo);
			return this.namedjdbcTemplate.query(this.buildFindByIdDocumentTypeOrderPageQuery(), namedParameters,
					new BeanPropertyRowMapper<TypeByAnexo>(TypeByAnexo.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener: redflats", dataAccessException);
		}
	}

	@Override
	public Integer UpdateTypesByAnexos(TypeByAnexo anexo) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createUpdateIdTypeByAnexo(anexo);
			this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
			return anexo.getIdTypeByAnexo();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener: redflats", dataAccessException);
		}
	}

	@Override
	public void deleteTypeByAnexo(final Integer idTypeByAnexo) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.deleteIdTypeByAnexo(idTypeByAnexo);
			this.namedjdbcTemplate.update(this.buildDeleteQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource createInsertCommentNamedParameters(final TypeByAnexo anexo) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_DOCUMENT_TYPE, anexo.getIdDocumentType());
		namedParameters.addValue(TableConstants.MORAL, anexo.getMoral());
		namedParameters.addValue(TableConstants.NAME, anexo.getName());
		//namedParameters.addValue(TableConstants.PAGE, anexo.getPage());
		namedParameters.addValue(TableConstants.ORDER, anexo.getOrder());
		namedParameters.addValue(TableConstants.FOLIO, anexo.getFolio());
		namedParameters.addValue(TableConstants.IDDOCUMENT, anexo.getIdDocument());
		namedParameters.addValue(TableConstants.FILENAME, anexo.getFileName());
		return namedParameters;
	}

	private String buildInsertCommentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.INSERT_INTO + TableConstants.TABLE_TYPEBYANEXO + QueryConstants.LEFT_BRACES);
		this.buildInsertFieldsQuery(query);
		query.append(QueryConstants.RIGHT_BRACES + QueryConstants.VALUES_TAG);
		query.append(TableConstants.ID_DOCUMENT_TYPE + QueryConstants.COMMA_TAG);
		query.append(TableConstants.MORAL + QueryConstants.COMMA_TAG);
		query.append(TableConstants.NAME + QueryConstants.COMMA_TAG);
		//query.append(TableConstants.PAGE + QueryConstants.COMMA_TAG);
		query.append(TableConstants.ORDER + QueryConstants.COMMA_TAG);
		query.append(TableConstants.FOLIO + QueryConstants.COMMA_TAG);
		query.append(TableConstants.IDDOCUMENT + QueryConstants.COMMA_TAG);
		query.append(TableConstants.FILENAME).append(QueryConstants.RIGHT_BRACES);
		return query.toString();
	}

	private void buildInsertFieldsQuery(final StringBuilder query) {
		query.append(TableConstants.ID_DOCUMENT_TYPE + QueryConstants.COMMA);
		query.append(TableConstants.MORAL + QueryConstants.COMMA);
		query.append(TableConstants.NAME + QueryConstants.COMMA);
		//query.append(TableConstants.PAGE + QueryConstants.COMMA);
		query.append(TableConstants.ORDER + QueryConstants.COMMA);
		query.append(TableConstants.FOLIO + QueryConstants.COMMA);
		query.append(TableConstants.IDDOCUMENT + QueryConstants.COMMA);
		query.append(TableConstants.FILENAME).append(QueryConstants.SPACE);
	}

	private MapSqlParameterSource createFindByIdDocumentType(final Integer idDocumentType, boolean moral, int folio) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_DOCUMENT_TYPE, idDocumentType);
		namedParameters.addValue(TableConstants.MORAL, moral);
		namedParameters.addValue(TableConstants.FOLIO, folio);
		return namedParameters;
	}

	private MapSqlParameterSource createFindByIdDocumentType(TypeByAnexo anexo) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_TYPEBYANEXO, anexo.getIdTypeByAnexo());
		namedParameters.addValue(TableConstants.ID_DOCUMENT_TYPE, anexo.getIdDocumentType());
		namedParameters.addValue(TableConstants.NAMEANEX, anexo.getName());
		namedParameters.addValue(TableConstants.MORAL, anexo.getMoral());
		namedParameters.addValue(TableConstants.ORDER, anexo.getOrder());
		//namedParameters.addValue(TableConstants.PAGE, anexo.getPage());
		namedParameters.addValue(TableConstants.FOLIO, anexo.getFolio());
		namedParameters.addValue(TableConstants.IDDOCUMENT, anexo.getIdDocument());
		namedParameters.addValue(TableConstants.FILENAME, anexo.getFileName());
		return namedParameters;
	}

	private String buildFindByIdDocumentTypenQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT);
		query.append(TableConstants.ID_TYPEBYANEXO + QueryConstants.COMMA);
		query.append(TableConstants.ID_DOCUMENT_TYPE + QueryConstants.COMMA);
		query.append(TableConstants.MORAL + QueryConstants.COMMA);
		query.append(TableConstants.NAME + QueryConstants.COMMA);
		query.append(TableConstants.ORDER + QueryConstants.COMMA);
		//query.append(TableConstants.PAGE + QueryConstants.COMMA);
		query.append(TableConstants.FOLIO + QueryConstants.COMMA);
		query.append(TableConstants.IDDOCUMENT + QueryConstants.COMMA);
		query.append(TableConstants.FILENAME + " ");
		query.append(FROM_TYPEBYANEXO);
		query.append("WHERE " + TableConstants.ID_DOCUMENT_TYPE + " = :IdDocumentType ");
		query.append("AND " + TableConstants.MORAL + " = :moral ");
		query.append("AND " + TableConstants.FOLIO + " = :[folio] ");
		query.append(" ORDER BY [name],[order] ASC ");
		return query.toString();
	}
	
	private String buildFindByFolio() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT);
		query.append(QueryConstants.ASTERISK);
		query.append(FROM_TYPEBYANEXO);
		query.append("WHERE " + TableConstants.FOLIO + " = :[folio] ");
		query.append(" ORDER BY [name],[order] ASC ");
		return query.toString();
	}
	
	private String buildFindAllTags() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT Tag, Field, TableName ");
        query.append("FROM TAGFIELDANEXOS ");
        return query.toString();
    }
	
	private String buildFindFolioByIdDocumentType() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT);
		query.append("R.IdRequisition, R.Status , P.Name, ");
		query.append("P.PersonalityEnum, DT.Name as NameContrato, SC.Name as Bandeja");
		query.append(QueryConstants.FROM);
		query.append(TableConstants.TABLE_REQUISITION + " R" + QueryConstants.INNER_JOIN); 
		query.append(TableConstants.TABLE_DOCUMENT_TYPE + " DT" + QueryConstants.ON);
		query.append("R."+ TableConstants.ID_DOCUMENT_TYPE + QueryConstants.EQUAL);
		query.append("DT." + TableConstants.ID_DOCUMENT_TYPE + " LEFT JOIN ");
		query.append(TableConstants.TABLE_SUPPLIER + " S" + QueryConstants.ON);
		query.append("R." + TableConstants.ID_SUPPLIER + QueryConstants.EQUAL);
		query.append("S." + TableConstants.ID_SUPPLIER + QueryConstants.INNER_JOIN);
		query.append(TableConstants.TABLE_PERSONALITY + " P" + QueryConstants.ON);
		query.append("S." + TableConstants.ID_PERSONALITY + QueryConstants.EQUAL);
		query.append("P." + TableConstants.ID_PERSONALITY + " LEFT JOIN ");
		query.append(TableConstants.TABLE_SCREEN + " SC" + QueryConstants.ON);
		query.append("R." + TableConstants.STATUS + QueryConstants.EQUAL);
		query.append("SC." + TableConstants.FLOW_STATUS);
		query.append(" WHERE R." + TableConstants.ID_DOCUMENT_TYPE + " = :IdDocumentType ");
		query.append(" AND R.Status IN ('DRAFT_GENERATION','NEGOTIATOR_CONTRACT_REVIEW',");
		query.append("'LOAD_SUPPLIER_AREAS_APPROVAL','APROVED_BY_JURISTIC')");
		return query.toString();
	}

	private String buildFindByIdDocumentTypeOrderPageQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT);
		query.append(QueryConstants.ASTERISK);
		query.append(FROM_TYPEBYANEXO);
		query.append("WHERE " + TableConstants.ID_DOCUMENT_TYPE + " = :IdDocumentType ");
		query.append("AND " + TableConstants.MORAL + " = :moral ");
		query.append("AND " + TableConstants.ID_TYPEBYANEXO + " <> :idTypeByAnexo ");
		query.append("AND (" + TableConstants.ORDER + " = :[order] ");
		query.append("OR " + TableConstants.IDDOCUMENT + " = :[idDocument] )");
		query.append("AND " + TableConstants.NAMEANEX + " = :[name] ");
		//query.append("AND " + TableConstants.PAGE + " = :[page] )");
		query.append("AND " + TableConstants.FOLIO + " = :[folio] ");
		query.append(" ORDER BY [order] ASC ");
		return query.toString();
	}

	private MapSqlParameterSource createUpdateIdTypeByAnexo(TypeByAnexo anexo) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_TYPEBYANEXO, anexo.getIdTypeByAnexo());
		namedParameters.addValue(TableConstants.NAME, anexo.getName());
		namedParameters.addValue(TableConstants.ORDER, anexo.getOrder());
		namedParameters.addValue(TableConstants.FILENAME, anexo.getFileName());
		namedParameters.addValue(TableConstants.IDDOCUMENT, anexo.getIdDocument());
		//namedParameters.addValue(TableConstants.PAGE, anexo.getPage());
		return namedParameters;
	}

	private String buildUpdateQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE TYPEBYANEXO SET ");
		query.append("Name = :Name, ");
		query.append("[order] = :[order], ");
		query.append("[fileName] = :[fileName], ");
		query.append("[idDocument] = :[idDocument] ");
		//query.append("[page] = :[page] ");
		query.append("WHERE idTypeByAnexo = :idTypeByAnexo ");
		return query.toString();
	}

	private MapSqlParameterSource deleteIdTypeByAnexo(Integer idTypeByAnexo) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_TYPEBYANEXO, idTypeByAnexo);
		return namedParameters;
	}

	private String buildDeleteQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM TYPEBYANEXO ");
		query.append("WHERE idTypeByAnexo = :idTypeByAnexo ");
		return query.toString();
	}
}
