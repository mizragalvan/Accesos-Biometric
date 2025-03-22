package mx.pagos.admc.contracts.interfaces;

import java.util.Date;
import java.util.List;

import mx.pagos.admc.contracts.structures.Holiday;
import mx.pagos.general.exceptions.DatabaseException;

public interface Holidayable {
    void save(Holiday holiday) throws DatabaseException;
    
    void deleteAll() throws DatabaseException;
    
    List<Holiday> findAll() throws DatabaseException;

    List<Date> findAllDates() throws DatabaseException;
}
