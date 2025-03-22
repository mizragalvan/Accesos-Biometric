package mx.pagos.admc.service.generals;

import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.CommentsBusiness;
import mx.pagos.admc.contracts.structures.Comment;
import mx.pagos.admc.contracts.structures.dtos.CommentDTO;
import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentsService {
	private static final Logger LOG = Logger.getLogger(CommentsService.class);

	@Autowired
	private CommentsBusiness commentsBusiness;

	@RequestMapping(value = UrlConstants.COMMENT_SAVE_OR_UPDATE, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdate(@RequestBody final Comment comment, final HttpServletResponse response) {
		Integer idComment = 0;
		try {
			idComment = this.commentsBusiness.saveOrUpdate(comment);
			LOG.debug("Guardado de comenario exitoso. Se regreso el id " + idComment);
			return idComment;
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
		return idComment;
	}

	@RequestMapping(value = UrlConstants.FIND_BY_IDREQ_AND_FLOWSTATUS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Comment> findByIdRequisitionAndFlowStatus(@RequestBody final ConsultaList<Comment> comment,
			final HttpServletResponse response) {
		LOG.info("Se hará un filtro por idSolicitud " + comment.getParam1() + " y Estatus " + comment.getParam2());
		try {
			final ConsultaList<Comment> listResponse = new ConsultaList<Comment>();
			listResponse.setList(
					this.commentsBusiness.findByIdRequisitionFlowStatusAndCommentType(Integer.valueOf(comment.getParam1()),
							FlowPurchasingEnum.valueOf(comment.getParam2()), CommentType.valueOf(comment.getParam3())));
			return listResponse;
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
			return null;
		}

	}

	@RequestMapping(value = UrlConstants.FIND_BY_ID_COMMENT, method = RequestMethod.POST)
	@ResponseBody
	public final Comment findByIdComment(@RequestBody final Comment comment, final HttpServletResponse response) {
		try {
			return this.commentsBusiness.findByIdComment(comment.getIdComment());
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
			return null;
		}

	}

	@RequestMapping(value = UrlConstants.FIND_ALL_COMMENTS_BY_IDREQUISITION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<CommentDTO> findAllCommentsByIdRequisition(@RequestBody final Integer idRequisition,
			final HttpServletResponse response) {
		LOG.info("Se hará un filtro por idSolicitud " + idRequisition);
		try {
			final ConsultaList<CommentDTO> listResponse = new ConsultaList<CommentDTO>();
			listResponse.setList(this.commentsBusiness.findAllCommentsByIdRequisition(idRequisition));
			return listResponse;
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
			return null;
		}
	}

	@RequestMapping(value = UrlConstants.FIND_COMMENTS_BY_IDREQUISITION_FLOWSTATUS_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<CommentDTO> findByIdRequisitionAndFlowStatusAndCommentType(
			@RequestBody final ConsultaList<CommentDTO> comment, final HttpServletResponse response) {
		LOG.info("Se hará un filtro por idSolicitud " + comment.getParam1() + " y Estatus " + comment.getParam2());
		try {
			final ConsultaList<CommentDTO> listResponse = new ConsultaList<CommentDTO>();
			FlowPurchasingEnum status = comment.getParam2() != null ? FlowPurchasingEnum.valueOf(comment.getParam2()) : null;
			listResponse.setList(this.commentsBusiness.findByIdRequisitionAndFlowStatusAndCommentType(
					Integer.valueOf(comment.getParam1()), status, CommentType.valueOf(comment.getParam3())));
			return listResponse;
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
			return null;
		}
	}

}
