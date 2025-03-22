package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.apache.log4j.helpers.LogLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.Commentable;
import mx.pagos.admc.contracts.structures.Comment;
import mx.pagos.admc.contracts.structures.dtos.CommentDTO;
import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class CommentsDAO implements Commentable {

	private static final String WHERE_ID_EQUALS_ID = QueryConstants.WHERE + TableConstants.ID_COMMENT
			+ QueryConstants.EQUAL_TAG + TableConstants.ID_COMMENT;
	private static final String FROM_COMMENT = QueryConstants.FROM + TableConstants.TABLE_COMMENTS;
	@Autowired
	private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Override
	public Integer saveOrUpdate(final Comment comment) throws DatabaseException {
		return comment.getIdComment() == null ? this.insertComment(comment) : this.updateComment(comment);
	}

	private Integer insertComment(final Comment comment) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createInsertCommentNamedParameters(comment);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertCommentQuery(), namedParameters, keyHolder,
					new String[] { "IdComment" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al insertar el comentario", dataAccessException);
		}
	}

	private Integer updateComment(final Comment comment) {
		return comment.getIdComment();
	}

	@Override
	public List<Comment> findByIdRequisitionFlowStatusAndCommentType(final Integer idRequisition,
			final FlowPurchasingEnum flowStatus, final CommentType commentType) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createFindByIdRequisitionAndFlowStatusNamedParameters(idRequisition, flowStatus, commentType);
			return this.namedjdbcTemplate.query(this.buildFindByIdRequisitionAndFlowStatusQuery(), namedParameters,
					new BeanPropertyRowMapper<Comment>(Comment.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener los comentarios", dataAccessException);
		}
	}

	@Override
	public Comment findById(Integer idComment) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idComment);
			return this.namedjdbcTemplate.queryForObject(this.buildFindById(), namedParameters,
					new BeanPropertyRowMapper<Comment>(Comment.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener el comentario", dataAccessException);
		}
	}

	private String buildFindByIdRequisitionAndFlowStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT);
		this.buildAllFieldsQuery(query);
		query.append(FROM_COMMENT + "com ");
		query.append("inner join USERS us on com.IdUser=us.IdUser ");
		query.append("WHERE IdRequisition = :IdRequisition ");
		query.append(" ORDER BY CreationDate DESC");
		LogLog.error("----------------------------" + query.toString());
		return query.toString();
	}

	private String buildFindById() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT);
		this.buildAllFieldsQuery(query);
		query.append(FROM_COMMENT);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private void buildWhereIdAndFlowQuery(final StringBuilder query) {
		query.append(QueryConstants.WHERE);
		query.append(TableConstants.ID_REQUISITION);
		query.append(QueryConstants.EQUAL_TAG + TableConstants.ID_REQUISITION);
		query.append(QueryConstants.AND);
		query.append(TableConstants.FLOW_STATUS + QueryConstants.EQUAL_TAG + TableConstants.FLOW_STATUS);
		query.append(QueryConstants.AND);
		query.append(TableConstants.COMMENT_TYPE + QueryConstants.EQUAL_TAG + TableConstants.COMMENT_TYPE);
	}

	private void buildAllFieldsQuery(final StringBuilder query) {
		query.append("com." + TableConstants.ID_COMMENT + QueryConstants.COMMA);
		this.buildInsertFieldsQuery(query);
		query.append(QueryConstants.COMMA + TableConstants.CREATION_DATE + QueryConstants.SPACE);
	}

	private MapSqlParameterSource createFindByIdRequisitionAndFlowStatusNamedParameters(final Integer idRequisition,
			final FlowPurchasingEnum flowStatus, final CommentType commentType) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
		namedParameters.addValue(TableConstants.FLOW_STATUS, (flowStatus != null ? flowStatus.toString() : null));
		namedParameters.addValue(TableConstants.COMMENT_TYPE, commentType.toString());
		return namedParameters;
	}

	private String buildInsertCommentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.INSERT_INTO + TableConstants.TABLE_COMMENTS + QueryConstants.LEFT_BRACES);
		this.buildInsertFieldsQuery2(query);
		query.append(QueryConstants.RIGHT_BRACES + QueryConstants.VALUES_TAG);
		query.append(TableConstants.ID_REQUISITION + QueryConstants.COMMA_TAG);
		query.append(TableConstants.COMMENT_TEXT + QueryConstants.COMMA_TAG);
		query.append(TableConstants.FLOW_STATUS + QueryConstants.COMMA_TAG);
		query.append(TableConstants.COMMENT_TYPE).append(QueryConstants.COMMA_TAG);
		query.append(TableConstants.ID_USER).append(QueryConstants.RIGHT_BRACES);
		return query.toString();
	}

	private void buildInsertFieldsQuery2(final StringBuilder query) {
		query.append(TableConstants.ID_REQUISITION + QueryConstants.COMMA);
		query.append(TableConstants.COMMENT_TEXT + QueryConstants.COMMA);
		query.append(TableConstants.FLOW_STATUS + QueryConstants.COMMA);
		query.append(TableConstants.COMMENT_TYPE).append(QueryConstants.COMMA);
		query.append(TableConstants.ID_USER).append(QueryConstants.SPACE);
	}

	private void buildInsertFieldsQuery(final StringBuilder query) {
		query.append("com." + TableConstants.ID_REQUISITION + QueryConstants.COMMA);
		query.append("com." + TableConstants.COMMENT_TEXT + QueryConstants.COMMA);
		query.append("com." + TableConstants.FLOW_STATUS + QueryConstants.COMMA);
		query.append("com." + TableConstants.COMMENT_TYPE).append(QueryConstants.COMMA);
		query.append("com." + TableConstants.ID_USER).append(QueryConstants.COMMA);
		query.append("CONCAT(us.Name,' ',us.FirstLastName) userName").append(QueryConstants.SPACE);
	}

	private MapSqlParameterSource createInsertCommentNamedParameters(final Comment comment) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, comment.getIdRequisition());
		namedParameters.addValue(TableConstants.COMMENT_TEXT, comment.getCommentText());
		namedParameters.addValue(TableConstants.FLOW_STATUS, comment.getFlowStatus().toString());
		namedParameters.addValue(TableConstants.COMMENT_TYPE, comment.getCommentType().toString());
		namedParameters.addValue(TableConstants.ID_USER, comment.getIdUser());
		return namedParameters;
	}

	public void deleteById(final Integer idComment) {
		final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idComment);
		this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
	}

	private String buildDeleteByIdQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.DELETE_FROM + TableConstants.TABLE_COMMENTS);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private MapSqlParameterSource createFindByIdNamedParameters(final Integer idComment) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_COMMENT, idComment);
		return namedParameters;
	}

	public Integer countByIdAndFlowStatus(final Integer idRequisition, final FlowPurchasingEnum flowStatus,
			final CommentType commentType) {
		final MapSqlParameterSource namedParameters = this
				.createFindByIdRequisitionAndFlowStatusNamedParameters(idRequisition, flowStatus, commentType);
		return this.namedjdbcTemplate.queryForObject(this.buildCountByIdAndFlowStatusQuery(), namedParameters,
				Integer.class);
	}

	private String buildCountByIdAndFlowStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT_COUNT + TableConstants.ID_COMMENT + QueryConstants.RIGHT_BRACES);
		query.append(FROM_COMMENT);
		this.buildWhereIdAndFlowQuery(query);
		return query.toString();
	}

	@Override
	public List<Comment> findAllByRequisition(final Integer idRequisition) throws DatabaseException {
		return this.namedjdbcTemplate.query(this.findAllByIdQuery(),
				new MapSqlParameterSource("IdRequisition", idRequisition), new BeanPropertyRowMapper<Comment>(Comment.class));
	}

	private String findAllByIdQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT);
		this.buildAllFieldsQuery(query);
		query.append(FROM_COMMENT);
		query.append("WHERE IdRequisition = :IdRequisition ");
		query.append(" ORDER BY CreationDate DESC");
		return query.toString();
	}

	@Override
	public List<CommentDTO> findAllCommentsByIdRequisition(final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindAllCommentsByIdRequisitionQuery(), namedParameters,
					new BeanPropertyRowMapper<CommentDTO>(CommentDTO.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener los comentarios", dataAccessException);
		}
	}

	private String buildFindAllCommentsByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT c.IdComment, c.IdRequisition, u.IdUser, ");
		query.append(" (u.Name + ' ' + u.FirstLastName + COALESCE(' ' + u.SecondLastName ,'')) as userName, ");
		query.append(" c.FlowStatus, CommentType, CommentText, CreationDate ");
		query.append(" FROM COMMENTS as c INNER JOIN USERS as u on u.IdUser = c.IdUser ");
		query.append(" WHERE c.IdRequisition = :IdRequisition ");
		query.append(" ORDER BY c.CreationDate DESC, c.IdComment DESC");
		return query.toString();
	}

	@Override
	public List<CommentDTO> findByIdRequisitionAndFlowStatusAndCommentType(final Integer idRequisition,
			final FlowPurchasingEnum flowStatus, final CommentType commentType) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this
					.createFindByIdRequisitionAndFlowStatusNamedParameters(idRequisition, flowStatus, commentType);
			return this.namedjdbcTemplate.query(this.buildFindByIdRequisitionAndFlowStatusAndCommentTypeQuery(),
					namedParameters, new BeanPropertyRowMapper<CommentDTO>(CommentDTO.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener los comentarios", dataAccessException);
		}
	}

	private String buildFindByIdRequisitionAndFlowStatusAndCommentTypeQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT c.IdComment, c.IdRequisition, u.IdUser, ");
		query.append(" (u.Name + ' ' + u.FirstLastName + COALESCE(' ' + u.SecondLastName ,'')) as userName, ");
		query.append(" c.FlowStatus, CommentType, CommentText, CreationDate ");
		query.append(" FROM COMMENTS as c INNER JOIN USERS as u on u.IdUser = c.IdUser ");
		query.append(" WHERE c.IdRequisition = :IdRequisition ");
		query.append(" AND (FlowStatus = :FlowStatus OR :FlowStatus is null) ");
		query.append(" AND CommentType = :CommentType ");
		query.append(" ORDER BY CreationDate DESC");
		return query.toString();
	}

}
