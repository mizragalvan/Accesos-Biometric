package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.HolidaysUserable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.HolidayUser;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("HolidayUserBusiness")
public class HolidayUserBusiness extends AbstractExportable {
    private static final String COLON = ": ";

    private static final String MESSAGE_SAVING_HOLIDAYS_USER_ERROR =
            "Hubo un error al guardar las vacaciones del usuario";
    private static final String MESSAGE_RETRIEVING_HOLIDAYS_USER_ERROR =
            "Hubo un error al recuperar las vacaciones del usuario";
    private static final String MESSAGE_EXPORTING_HOLIDAY_USERS_ERROR =
            "Hubo un problema al exportar el catálogo Vacaciones";

    private static final Logger LOG = Logger.getLogger(HolidayUserBusiness.class);

    
    @Autowired
    private HolidaysUserable holidaysUserable;
    
    public void saveHolidaysUser(final List<HolidayUser> holidayUsersList, final Integer idUser)
            throws BusinessException {
        try {
            this.holidaysUserable.deleteHolidaysUserByIdUser(idUser);
            for (HolidayUser holidayUser : holidayUsersList) {
                holidayUser.setIdUser(idUser);
                this.holidaysUserable.save(holidayUser);
            }
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_SAVING_HOLIDAYS_USER_ERROR + COLON + idUser.toString(), databaseException);
            throw new BusinessException(MESSAGE_SAVING_HOLIDAYS_USER_ERROR, databaseException);
        }
    }
    
    public List<HolidayUser> findByIdUser(final Integer idUser) throws BusinessException {
        try {
            return this.holidaysUserable.findByIdUser(idUser);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_HOLIDAYS_USER_ERROR + COLON + idUser.toString(), databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_HOLIDAYS_USER_ERROR, databaseException);
        }
    }
    
    public List<HolidayUser> findAll() throws BusinessException {
        try {
            return this.holidaysUserable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_HOLIDAYS_USER_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_HOLIDAYS_USER_ERROR, databaseException);
        }
    }

    @Override
    public final String[][] getCatalogAsMatrix() throws BusinessException {
        try {
            final List<HolidayUser> holidayUserList = this.holidaysUserable.findAll();
            return this.getExportHolidayUserMatrix(holidayUserList);
        } catch (DatabaseException dataBaseException) {
          LOG.error(MESSAGE_EXPORTING_HOLIDAY_USERS_ERROR, dataBaseException);
          throw new BusinessException(MESSAGE_EXPORTING_HOLIDAY_USERS_ERROR, dataBaseException);
        }
    }
    
    private String[][] getExportHolidayUserMatrix(final List<HolidayUser> holidayUserList) {
        final Integer columnsNumber = 4;
        final String[][] dataMatrix = new String[holidayUserList.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdHolidayUser";
        dataMatrix[0][1] = "IdUser";
        dataMatrix[0][2] = "StartDate";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "EndDate";
        Integer index = 1;
        for (HolidayUser holidayUser : holidayUserList) {
            dataMatrix[index][0] = holidayUser.getIdHolidayUser().toString();
            dataMatrix[index][1] = holidayUser.getIdUser().toString();
            dataMatrix[index][2] = holidayUser.getStartDate();
            dataMatrix[index][NumbersEnum.THREE.getNumber()] = holidayUser.getEndDate();
            index++;
        }
        return dataMatrix;
    }
}
