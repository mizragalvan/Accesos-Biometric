package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.HolidayUser;
import mx.pagos.general.exceptions.DatabaseException;

public interface HolidaysUserable {
	Integer save(HolidayUser holidayUser) throws DatabaseException;
	
	void deleteHolidayUser(final Integer idHolidayUser) throws DatabaseException;

    List<HolidayUser> findByIdUser(Integer idUser) throws DatabaseException;
    
    List<HolidayUser> findAll() throws DatabaseException;

    void deleteHolidaysUserByIdUser(Integer idUser) throws DatabaseException;
}
