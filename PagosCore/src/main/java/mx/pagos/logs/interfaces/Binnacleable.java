package mx.pagos.logs.interfaces;

import java.util.List;

import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.logs.structures.Binnacle;

public interface Binnacleable {
	
	Integer save(Binnacle binnacle) throws DatabaseException;
	
	List<Binnacle> findAll() throws DatabaseException;
	
	List<Binnacle> findByIdUser(Integer idUser) throws DatabaseException;
	
	List<Binnacle> findByDate(String starDate, String endDate) throws DatabaseException;
	
	Binnacle findByIdBinnacle(Integer idBinnacle) throws DatabaseException, EmptyResultException;

    List<Binnacle> findByIdFlow(Integer idFlow) throws DatabaseException;

    List<Binnacle> findByLogCategory(LogCategoryEnum logCategory) throws DatabaseException;
    
    void deleteByRangeDates(String dateFrom, String dateTo, List<String> list) throws DatabaseException;
    
    List<Binnacle> findByLogCategoryListDatesAndUser(Binnacle binnacle) throws DatabaseException;

	List<Binnacle> findByLogCategoryTypesPaginated(Binnacle binnacle, Integer pagesNumber,
			Integer itemsNumber) throws DatabaseException;

	Long findByLogCategoryTypesPaginatedTotalPages(Binnacle binnacle) throws DatabaseException;
}
