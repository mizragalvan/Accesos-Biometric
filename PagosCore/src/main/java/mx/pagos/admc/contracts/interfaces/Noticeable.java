package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Notice;
import mx.pagos.general.exceptions.DatabaseException;

public interface Noticeable {
	Integer saveOrUpdate(Notice notice) throws DatabaseException;
	
	List<Notice> findByAvailable() throws DatabaseException;
	
	List<Notice> findAll() throws DatabaseException;

    Notice findByNoticeId(Integer idNotice) throws DatabaseException;
    
    void deleteById(Integer idNotice) throws DatabaseException;
    
    List<Notice> findAllNoticesAvailablePaged(Integer pagesNumber, Integer itemsNumber) throws DatabaseException;
    
    Long countAllNoticesAvailablesRecords() throws DatabaseException;
}
