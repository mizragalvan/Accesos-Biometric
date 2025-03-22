package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.Noticeable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Notice;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("NoticesBusiness")
public class NoticesBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(NoticesBusiness.class);
    private static final String ERRORAVISO = "Error al obtener los avisos";
    private static final String ERROR_MESSAGE_DELETE = "Error al eliminar registro por id";
    private static final String MESSAGE_EXPORTING_NOTICE_ERROR =
            "Hubo un problema al exportar el catálogo Noticias";
    private static final String MESSAGE_FIND_ALL_AVAILABLE_NOTICES_ERROR =
            "Hubo un problema al buscar todas las noticias disponibles";
    private static final String MESSAGE_FIND_ALL_AVAILABLE_NOTICES_PAGES_ERROR =
            "Hubo un problema al buscar el número de pagínas de noticias disponibles";
    
    @Autowired
    private Noticeable noticeable;
    
    @Autowired
    private ConfigurationsBusiness configuration;

	public final Integer saveOrUpdate(final Notice notice) throws BusinessException {
		try {
			return this.noticeable.saveOrUpdate(notice);
		} catch (DatabaseException databaseException) {
		    LOG.error("Error al guardar los datos del Aviso", databaseException);
			throw new BusinessException("Error al guardar el Aviso", databaseException);
		}
	}

	public final List<Notice> findByAvailable() throws BusinessException {
		try {
			return this.noticeable.findByAvailable();
		} catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Avisos vigentes", databaseException);
		    throw new BusinessException(ERRORAVISO, databaseException);
		}
	}
	
	public final List<Notice> findAll() throws BusinessException {
        try {
            return this.noticeable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener todos los Avisos", databaseException);
            throw new BusinessException(ERRORAVISO, databaseException);
        }
    }
	
	 public final Notice findByNoticeId(final Integer idNotice) throws BusinessException {
         try {
             return this.noticeable.findByNoticeId(idNotice);
         } catch (DatabaseException databaseException) {
             LOG.error("Error al obtener Aviso por Id", databaseException);
             throw new BusinessException("Error al obtener la Noticia por id", databaseException);
         }
     }
	 
	 public final void deleteNoticeByIdNotice(final Integer idNotice) throws BusinessException {
	     try {
            this.noticeable.deleteById(idNotice);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_DELETE, databaseException);
            throw new BusinessException(ERROR_MESSAGE_DELETE, databaseException);
        }
	 }
	 
	 public final List<Notice> findAllAvailableNoticesPaged(final Integer pagesNumber) throws BusinessException {
	     try {
	         return this.noticeable.findAllNoticesAvailablePaged(pagesNumber, Integer.parseInt(
	                 this.configuration.findByName(ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_AVAILABLE_NOTICES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_AVAILABLE_NOTICES_ERROR, databaseException);
        }
	 }
	 
	 public final Notice returnTotalPagesToShowOfAvailablesNotices() throws BusinessException {
	     try {
	         final Long totalRecords = this.noticeable.countAllNoticesAvailablesRecords();
	         final Notice notice = new Notice();
	         notice.setNumberPage(this.configuration.totalPages(totalRecords));
	         notice.setTotalRows(totalRecords.intValue());
	         return notice;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_AVAILABLE_NOTICES_PAGES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_AVAILABLE_NOTICES_PAGES_ERROR, databaseException);
        }
	 }

	 @Override
	    public final String[][] getCatalogAsMatrix() throws BusinessException {
	        try {
	            final List<Notice> noticeList = this.noticeable.findAll();
	            return this.getExportNoticeMatrix(noticeList);
	        } catch (DatabaseException dataBaseException) {
	          LOG.error(MESSAGE_EXPORTING_NOTICE_ERROR, dataBaseException);
	          throw new BusinessException(MESSAGE_EXPORTING_NOTICE_ERROR, dataBaseException);
	        }
	    }
	    
	    private String[][] getExportNoticeMatrix(final List<Notice> noticeList) {
	        final Integer columnsNumber = 4;
	        final String[][] dataMatrix = new String[noticeList.size() + 1][columnsNumber];
	        dataMatrix[0][0] = "IdNotice";
	        dataMatrix[0][1] = "NoticeType";
	        dataMatrix[0][2] = "CreationDate";
	        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "DueDate";
	        Integer index = 1;
	        for (Notice notice : noticeList) {
	            dataMatrix[index][0] = notice.getIdNotice().toString();
	            dataMatrix[index][1] = notice.getNoticeType();
	            dataMatrix[index][2] = notice.getCreationDate();
	            dataMatrix[index][NumbersEnum.THREE.getNumber()] = notice.getDueDate();
	            index++;
	        }
	        return dataMatrix;
	    }
}
