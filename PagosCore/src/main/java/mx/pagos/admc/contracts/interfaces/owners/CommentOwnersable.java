package mx.pagos.admc.contracts.interfaces.owners;

import java.util.List;

import mx.pagos.admc.contracts.structures.owners.CommentOwner;
import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.document.versioning.structures.DocumentBySection;
import mx.pagos.general.exceptions.DatabaseException;

public interface CommentOwnersable {

    Integer saveComment(CommentOwner commentOwnerParameter) throws DatabaseException;
    
    List<CommentOwner> findCommentOwnerByIdRequisitionOwnerFlowStatusAndCommentType(Integer idRequisitionOwner,
            FlowPurchasingEnum flowStatus, CommentType commentType) throws DatabaseException;
    
    List<DocumentBySection> findCommentDocument(Integer idCommentOwner) throws DatabaseException;
    
    void saveCommentOwnerDocuments(Integer idCommentOwner, Integer idDocument, String documentName) 
            throws DatabaseException;
}
