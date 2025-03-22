package mx.pagos.admc.service.administration;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.NoticesBusiness;
import mx.pagos.admc.contracts.structures.Notice;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

@Controller
public class NoticeService {
    private static final Logger LOG = Logger.getLogger(NoticeService.class);
    
    @Autowired
    private SimpMessagingTemplate template;
    
    @Autowired
    private SimpUserRegistry simpUserRegistry;
    
    @Autowired
    private NoticesBusiness noticesBusiness;
    
    
    @RequestMapping (value = UrlConstants.NOTICE_FIND_AVAILABLE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Notice> findNoticeByAvailables(final HttpServletResponse  response) {

        LOG.info("NoticeService :: findNoticeByAvailables");
        try {
            final ConsultaList<Notice> noticeList = new ConsultaList<Notice>();
            noticeList.setList(this.noticesBusiness.findByAvailable());
            return noticeList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
            LOG.info(" BusinessException :: PositionService :: searchPositions");
            LOG.info(businessException.getMessage(), businessException);
        }
        return new ConsultaList<Notice>();
    }

    @RequestMapping(value = UrlConstants.NOTICE_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final Notice notice, final HttpServletResponse response) throws Exception {
        Integer idNotice = 0;
        try {
            idNotice = this.noticesBusiness.saveOrUpdate(notice);
            LOG.debug("Guardado de Noticia exitoso " + idNotice);
            // sendNotice(notice);
            this.template.convertAndSend("/topic/resp/notice", notice);   
            return idNotice;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idNotice;
    }
    
    @SendTo("/topic/resp/notice")
    public Notice sendNotice(Notice idNotice ) throws Exception {				
        return idNotice;
    }


    @RequestMapping (value = UrlConstants.NOTICE_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Notice findByNoticeId(@RequestBody final Notice notice, final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener Noticia por id" + notice.getIdNotice());
            return this.noticesBusiness.findByNoticeId(notice.getIdNotice());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Notice();
    }
    
    @RequestMapping (value = UrlConstants.NOTICE_DELETE_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final void deleteNoticeById(@RequestBody final Integer idNotice, final HttpServletResponse response) {
        try {
            LOG.debug("Se va a eliminar Noticia por id" + idNotice);
            this.noticesBusiness.deleteNoticeByIdNotice(idNotice);
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
    }
    
    @RequestMapping (value = UrlConstants.NOTICE_FIND_ALL_AVAILABLE_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Notice> findAllAvailableNoticesPaged(@RequestBody final Integer numberPage,
            final HttpServletResponse response) {
        try {
            final ConsultaList<Notice> noticeList = new ConsultaList<>();
            noticeList.setList(this.noticesBusiness.findAllAvailableNoticesPaged(numberPage));
            return noticeList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Notice>();
    }
    
    @RequestMapping (value = UrlConstants.NOTICE_RETURN_TOTAL_PAGES_TO_SHOW, method = RequestMethod.POST)
    @ResponseBody
    public final Notice returnTotalPagesToShowNotices(final HttpServletResponse response) {
        try {
            return this.noticesBusiness.returnTotalPagesToShowOfAvailablesNotices();
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Notice();
    }
}
