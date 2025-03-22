package mx.pagos.admc.contracts.business;

import java.util.Date;
import java.util.List;

import mx.pagos.admc.contracts.interfaces.Holidayable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Holiday;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("HolidayBusiness")
public class HolidayBusiness extends AbstractExportable {
    private static final String MESSAGE_SAVING_HOLIDAYS_ERROR = "Hubo un problema al guardar los días festivos";
    private static final String MESSAGE_RETRIVING_ALL_HOLIDAYS_ERROR =
            "Hubo un problema al recuparar los días festivos";

    private static final Logger LOG = Logger.getLogger(HolidayBusiness.class);
    private static final String MESSAGE_RETRIVING_ALL_HOLIDAYS_DATES_ERROR =
            "Hubo un problema al recuperar las fechas de los días festivos";
    private static final String MESSAGE_EXPORTING_HOLIDAY_ERROR =
            "Hubo un problema al exportar el catálogo Días Inhábiles";

    
    @Autowired
    private Holidayable holidayable;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
    public void saveHolidays(final List<Holiday> holidaysList) throws BusinessException {
        try {
            this.holidayable.deleteAll();
            for (Holiday holiday : holidaysList)
                this.holidayable.save(holiday);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_SAVING_HOLIDAYS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_SAVING_HOLIDAYS_ERROR, databaseException);
        }
    }
    
    public List<Holiday> findAll() throws BusinessException {
        try {
            return this.holidayable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_ALL_HOLIDAYS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_ALL_HOLIDAYS_ERROR, databaseException);
        }
    }
    
    public List<Date> findAllDates() throws BusinessException {
        try {
            return this.holidayable.findAllDates();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_ALL_HOLIDAYS_DATES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_ALL_HOLIDAYS_DATES_ERROR, databaseException);
        }
    }

    @Override
    public String[][] getCatalogAsMatrix() throws BusinessException {
        try {
            final List<Holiday> holidayList = this.holidayable.findAll();
            return this.getExportHolidayMatrix(holidayList);
        } catch (DatabaseException dataBaseException) {
          LOG.error(MESSAGE_EXPORTING_HOLIDAY_ERROR, dataBaseException);
          throw new BusinessException(MESSAGE_EXPORTING_HOLIDAY_ERROR, dataBaseException);
        }
    }
    
    private String[][] getExportHolidayMatrix(final List<Holiday> holidayList) {
        final Integer columnsNumber = 2;
        final String[][] dataMatrix = new String[holidayList.size() + 1][columnsNumber];
        dataMatrix[0][0] = "Date";
        dataMatrix[0][1] = "Description";
        Integer index = 1;
        for (Holiday holiday : holidayList) {
            dataMatrix[index][0] = holiday.getDate();
            dataMatrix[index][1] = holiday.getDescription();
            index++;
        }
        return dataMatrix;
    }
}
