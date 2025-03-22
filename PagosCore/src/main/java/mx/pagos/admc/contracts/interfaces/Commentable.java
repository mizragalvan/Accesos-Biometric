package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Comment;
import mx.pagos.admc.contracts.structures.dtos.CommentDTO;
import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Commentable {
    Integer saveOrUpdate(Comment comment) throws DatabaseException;
    
    List<Comment> findByIdRequisitionFlowStatusAndCommentType(Integer idrequisition,
            FlowPurchasingEnum flowStatus, CommentType commentType) throws DatabaseException;
    
    Comment findById(Integer idComment) throws DatabaseException;
    
    List<Comment> findAllByRequisition(final Integer idRequisition) throws DatabaseException;
    
    List<CommentDTO> findAllCommentsByIdRequisition(final Integer idRequisition) throws DatabaseException;
    
    List<CommentDTO> findByIdRequisitionAndFlowStatusAndCommentType(final Integer idRequisition,
            final FlowPurchasingEnum flowStatus, final CommentType commentType) throws DatabaseException;
}
