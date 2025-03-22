package mx.pagos.admc.contracts.daos.owners;

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
import mx.pagos.admc.contracts.interfaces.owners.CommentOwnersable;
import mx.pagos.admc.contracts.structures.owners.CommentOwner;
import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.document.versioning.structures.DocumentBySection;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class CommentOwnerDAO implements CommentOwnersable {
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Override
    public Integer saveComment(final CommentOwner commentOwnerParameter) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = this.saveCommentParameters(commentOwnerParameter); 
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.saveCommentQuery(), parameterSource, keyHolder, 
                    new String[]{"IdCommentOwner"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException  dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private MapSqlParameterSource saveCommentParameters(final CommentOwner commentOwnerParameter) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.ID_REQUISITION_OWNERS, commentOwnerParameter.getIdRequisitionOwners());
        source.addValue(TableConstants.COMMENT_TEXT, commentOwnerParameter.getCommentText());
        source.addValue(TableConstants.FLOW_STATUS, commentOwnerParameter.getFlowStatus().toString());
        source.addValue(TableConstants.COMMENT_TYPE, commentOwnerParameter.getCommentType().toString());
        return source;
    }
    
    private String saveCommentQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO COMMENTOWNER(IdRequisitionOwners, CommentText, FlowStatus, CommentType) ");
        builder.append("VALUES(:IdRequisitionOwners, :CommentText, :FlowStatus, :CommentType) ");
        return builder.toString();
    }

    @Override
    public List<CommentOwner> findCommentOwnerByIdRequisitionOwnerFlowStatusAndCommentType(
            final Integer idRequisitionOwner, final FlowPurchasingEnum flowStatus,
            final CommentType commentType) throws DatabaseException {
        try {
            final MapSqlParameterSource source = 
                    this.findCommenstParameters(idRequisitionOwner, flowStatus, commentType);
            return this.namedjdbcTemplate.query(
                    this.findCommentOwnerByIdRequisitionOwnerFlowStatusAndCommentTypeQuery(), source, 
                    new BeanPropertyRowMapper<CommentOwner>(CommentOwner.class));
        } catch (DataAccessException  dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private MapSqlParameterSource findCommenstParameters(final Integer idRequisitionOwner, 
            final FlowPurchasingEnum flowStatus, final CommentType commentType) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
        source.addValue(TableConstants.FLOW_STATUS, flowStatus.toString());
        source.addValue(TableConstants.COMMENT_TYPE, commentType.toString());
        return source;
    }
    
    private String findCommentOwnerByIdRequisitionOwnerFlowStatusAndCommentTypeQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT COMMENTOWNER.IdCommentOwner, COMMENTOWNER.IdRequisitionOwners, ");
        builder.append("COMMENTOWNER.CreateDate, COMMENTOWNER.CommentText, COMMENTOWNER.FlowStatus, ");
        builder.append("COMMENTOWNER.CommentType FROM COMMENTOWNER ");
        builder.append("WHERE COMMENTOWNER.IdRequisitionOwners = :IdRequisitionOwners AND ");
        builder.append("COMMENTOWNER.FlowStatus = :FlowStatus AND COMMENTOWNER.CommentType = :CommentType ");
        builder.append("ORDER BY COMMENTOWNER.IdCommentOwner DESC ");
        return builder.toString();
    }

    @Override
    public void saveCommentOwnerDocuments(final Integer idCommentOwner,
            final Integer idDocument, final String documentName) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_COMMENT_OWNER, idCommentOwner);
            source.addValue(TableConstants.ID_DOCUMENT, idDocument);
            source.addValue(TableConstants.DOCUMENT_NAME, documentName);
            this.namedjdbcTemplate.update(this.saveCommentOwnerDocumentsQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String saveCommentOwnerDocumentsQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO COMMENTOWNERDOCUMENT(IdCommentOwner, IdDocument, DocumentName) ");
        builder.append("VALUES(:IdCommentOwner, :IdDocument, :DocumentName) ");
        return builder.toString();
    }

    @Override
    public List<DocumentBySection> findCommentDocument(final Integer idCommentOwner)
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_COMMENT_OWNER, idCommentOwner);
            return this.namedjdbcTemplate.query(this.findCommentDocumentQuery(), source,
                    new BeanPropertyRowMapper<DocumentBySection>(DocumentBySection.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findCommentDocumentQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdDocument, DocumentName ");
        builder.append("FROM COMMENTOWNERDOCUMENT ");
        builder.append("WHERE IdCommentOwner = :IdCommentOwner ");
        return builder.toString();
    }
}
