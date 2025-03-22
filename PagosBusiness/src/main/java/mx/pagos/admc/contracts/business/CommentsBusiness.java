package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.Commentable;
import mx.pagos.admc.contracts.structures.Comment;
import mx.pagos.admc.contracts.structures.dtos.CommentDTO;
import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsBusiness {
	private static final Logger LOG = Logger.getLogger(CommentsBusiness.class);

	@Autowired
	private Commentable commentable;

	public final Integer saveOrUpdate(final Comment comment) throws BusinessException {
		try {
			LOG.debug("Se guardará un comentario");
			return this.commentable.saveOrUpdate(comment);
		} catch (DatabaseException databaseException) {
			LOG.error(databaseException.getMessage(), databaseException);
			throw new BusinessException("Error al guardar los datos del Comentario", databaseException);
		}
	}

	public final List<Comment> findByIdRequisitionFlowStatusAndCommentType(final Integer idrequisition,
			final FlowPurchasingEnum flowStatus, final CommentType commentType) throws BusinessException {
		try {
			LOG.debug("Se obtendrá la lista de comentarios por solicitud y estatus");
			return this.commentable.findByIdRequisitionFlowStatusAndCommentType(idrequisition, flowStatus, commentType);
		} catch (DatabaseException databaseException) {
			LOG.error(databaseException.getMessage(), databaseException);
			throw new BusinessException("Error al obtener los Comentarios", databaseException);
		}
	}

	// falta implementar el DAO
	public final Comment findLastCommentByIdRequisitionFlowStatusAndCommentType(final Integer idrequisition,
			final FlowPurchasingEnum flowStatus, final CommentType commentType) throws BusinessException {
		try {
			LOG.debug("Se obtendrá el ultimo comentario por solicitud y estatus");
			final List<Comment> commentList = this.commentable.findByIdRequisitionFlowStatusAndCommentType(idrequisition,
					flowStatus, commentType);
			return commentList.get(commentList.size() - 1);
		} catch (DatabaseException databaseException) {
			LOG.error(databaseException.getMessage(), databaseException);
			throw new BusinessException("Error al obtener el ultimo comentario", databaseException);
		}
	}

	public Comment findByIdComment(final Integer idComment) throws BusinessException {
		try {
			return this.commentable.findById(idComment);
		} catch (DatabaseException databaseException) {
			LOG.error(databaseException.getMessage(), databaseException);
			throw new BusinessException("Error al obtener el comentario", databaseException);
		}
	}

	public final List<Comment> findAllByRequisition(final Integer idRequisition) throws BusinessException {
		try {
			return this.commentable.findAllByRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(databaseException.getMessage(), databaseException);
			throw new BusinessException("Error al los comentarios por solicitud", databaseException);
		}
	}

	public final List<CommentDTO> findAllCommentsByIdRequisition(final Integer idRequisition) throws BusinessException {
		try {
			return this.commentable.findAllCommentsByIdRequisition(idRequisition);
		} catch (DatabaseException databaseException) {
			LOG.error(databaseException.getMessage(), databaseException);
			throw new BusinessException("Error al los comentarios por solicitud", databaseException);
		}
	}

	public final List<CommentDTO> findByIdRequisitionAndFlowStatusAndCommentType(final Integer idrequisition,
			final FlowPurchasingEnum flowStatus, final CommentType commentType) throws BusinessException {
		try {
			LOG.debug("Se obtendrá la lista de comentarios por solicitud y estatus");
			return this.commentable.findByIdRequisitionAndFlowStatusAndCommentType(idrequisition, flowStatus, commentType);
		} catch (DatabaseException databaseException) {
			LOG.error(databaseException.getMessage(), databaseException);
			throw new BusinessException("Error al obtener los Comentarios", databaseException);
		}
	}
}
