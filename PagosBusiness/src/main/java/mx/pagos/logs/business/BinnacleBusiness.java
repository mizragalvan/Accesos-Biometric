package mx.pagos.logs.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.logs.interfaces.Binnacleable;
import mx.pagos.logs.structures.Binnacle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mizraim
 */

@Service("BinnacleBusiness")
public class BinnacleBusiness extends AbstractExportable {
    
    private static final String MESSAGE_BINNACLE_NOT_EXISTS_ERROR = "La bitácora ha dejado de existir";
    private static final String MESSAGE_RETIEVING_BINNACLES_BY_ID_ERROR =
            "Hubo un error al recuperar la bitácora por id";
    private static final String MESSAGE_RETIEVING_BINNACLES_BY_DATE_ERROR =
            "Hubo un error al recuperar las bitácoras por fecha";
    private static final String MESSAGE_RETIEVING_BINNACLES_BY_USER_ERROR =
            "Hubo un error al recuperar la bitácora  por usuario";
    private static final String MESSAGE_RETIEVING_ALL_BINNACLES_ERROR = "Hubo un error al recuperar la bitácora";
    private static final String MESSAGE_SAVING_BINNACLE_ERROR = "Hubo un error al guardar la bitácora";
    private static final String MESSAGE_RETIEVING_BINNACLES_BY_FLOW_ERROR =
            "Hubo un error al recuperar la bitácora  por flujo";
    private static final String MESSAGE_RETIEVING_BINNACLES_BY_CATEGORY_ERROR =
            "Hubo un error al recuperar la bitácora por categoría";
    private static final String MESSAGE_DELETE_BINNACLES_BY_DATES_RANGE_ERROR =
            "Hubo un error al eliminar las bitácoras por rango de fecha";
    private static final String MESSAGE_FIND_BINNACLES_BY_CATEGORIES_ERROR =
            "Hubo un error al buscar las bitácoras por categoría";
    private static final String MESSAGE_EXPORTING_BINNACLE_ERROR =
            "Hubo un problema al exportar el catálogo bitacoras";
    private static final String MESSAGE_COUNT_TOTAL_PAGES_ERROR = 
            "Hubo un problema al contar el número de páginas a mostrar";
    
    private static final Logger LOG = Logger.getLogger(BinnacleBusiness.class);
	
    @Autowired
    private Binnacleable binnacleble;
    
    @Autowired
    private ConfigurationsBusiness configuration;
	
	public final Integer save(final Binnacle binnacleParameter) throws BusinessException {
		try {
			return this.binnacleble.save(binnacleParameter);
		} catch (DatabaseException databaseException) {
		    LOG.error(MESSAGE_SAVING_BINNACLE_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_BINNACLE_ERROR, databaseException);
		}
	}
	
	public final List<Binnacle> findAll() throws BusinessException {
		try {
			return this.binnacleble.findAll();
		} catch (DatabaseException databaseException) {
		    LOG.error(MESSAGE_RETIEVING_ALL_BINNACLES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETIEVING_ALL_BINNACLES_ERROR, databaseException);
		}	
	}
	
	public final List<Binnacle> findByIdUser(final Integer idUser) throws BusinessException {
		try {
			return this.binnacleble.findByIdUser(idUser);
		} catch (DatabaseException databaseException) {
		    LOG.error(MESSAGE_RETIEVING_BINNACLES_BY_USER_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETIEVING_BINNACLES_BY_USER_ERROR, databaseException);
		}
	}
	
	public final List<Binnacle> findByDate(final String startDate, final String endDate) throws BusinessException {
		try {
			return this.binnacleble.findByDate(startDate, endDate);
		} catch (DatabaseException databaseException) {
		    LOG.error(MESSAGE_RETIEVING_BINNACLES_BY_DATE_ERROR, databaseException);
		    throw new BusinessException(MESSAGE_RETIEVING_BINNACLES_BY_DATE_ERROR, databaseException);
		}
	}
	
	public final Binnacle findByIdBinnacle(final Integer idBinnacle) throws BusinessException {
		try {
			return this.binnacleble.findByIdBinnacle(idBinnacle);
		} catch (DatabaseException databaseException) {
		    LOG.error(MESSAGE_RETIEVING_BINNACLES_BY_ID_ERROR, databaseException);
		    throw new BusinessException(MESSAGE_RETIEVING_BINNACLES_BY_ID_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
		    LOG.error(MESSAGE_BINNACLE_NOT_EXISTS_ERROR, emptyResultException);
		    throw new BusinessException(MESSAGE_BINNACLE_NOT_EXISTS_ERROR, emptyResultException);
        }
	}
	
	public final List<Binnacle> findByIdFlow(final Integer idFlow) throws BusinessException {
	    try {
	        return this.binnacleble.findByIdFlow(idFlow);
	    } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETIEVING_BINNACLES_BY_FLOW_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETIEVING_BINNACLES_BY_FLOW_ERROR, databaseException);
        }
	}
	
	public final List<Binnacle> findByLogCategory(final LogCategoryEnum category) throws BusinessException {
	    try {
	        return this.binnacleble.findByLogCategory(category);
	    } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETIEVING_BINNACLES_BY_CATEGORY_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETIEVING_BINNACLES_BY_CATEGORY_ERROR, databaseException);
        }
	}
	
	public final void deleteByDatesRange(final String dateFrom, final String dateTo, final List<String> listLogs) 
	        throws BusinessException {
	    try {
            this.binnacleble.deleteByRangeDates(dateFrom, dateTo, listLogs);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_DELETE_BINNACLES_BY_DATES_RANGE_ERROR, databaseException);
            throw new BusinessException(MESSAGE_DELETE_BINNACLES_BY_DATES_RANGE_ERROR, databaseException);
        }
	}

   public final List<Binnacle> findByLogCategoryListDatesAndUser(final Binnacle binnaclePar) throws BusinessException {
	    try {
	        return this.binnacleble.findByLogCategoryListDatesAndUser(binnaclePar);
	    } catch (DatabaseException databaseException) {
	        LOG.error(MESSAGE_FIND_BINNACLES_BY_CATEGORIES_ERROR, databaseException);
	        throw new BusinessException(MESSAGE_FIND_BINNACLES_BY_CATEGORIES_ERROR, databaseException);
	    }
	}
   
   public List<Binnacle> findByLogCategoryTypesPaginated(final Binnacle binnacleParamter, 
		   final Integer pagesNumberParameter) throws BusinessException {
       try {
           final Integer itemsNumber = this.configuration.getPaginationItemsNumberParameter();
           return this.binnacleble.findByLogCategoryTypesPaginated(binnacleParamter, pagesNumberParameter, itemsNumber);
       } catch (DatabaseException databaseException) {
           LOG.error(MESSAGE_FIND_BINNACLES_BY_CATEGORIES_ERROR, databaseException);
           throw new BusinessException(MESSAGE_FIND_BINNACLES_BY_CATEGORIES_ERROR, databaseException);
       }
	}
   
   @Override
   public final String[][] getCatalogAsMatrix() throws BusinessException {
       try {
           final List<Binnacle> binnacleList = this.binnacleble.findAll();
           return this.getExportBinnacleMatrix(binnacleList);
       } catch (DatabaseException dataBaseException) {
         LOG.error(MESSAGE_EXPORTING_BINNACLE_ERROR, dataBaseException);
         throw new BusinessException(MESSAGE_EXPORTING_BINNACLE_ERROR, dataBaseException);
       }
   }
   
   private String[][] getExportBinnacleMatrix(final List<Binnacle> binnacleList) {
       final Integer columnsNumber = 8;
       final String[][] dataMatrix = new String[binnacleList.size() + 1][columnsNumber];
       dataMatrix[0][0] = "IdBinnacle";
       dataMatrix[0][1] = "IdUser";
       dataMatrix[0][2] = "UserFullName";
       dataMatrix[0][NumbersEnum.THREE.getNumber()] = "IdFlow";
       dataMatrix[0][NumbersEnum.FOUR.getNumber()] = "RegisterDate";
       dataMatrix[0][NumbersEnum.FIVE.getNumber()] = "Action";
       dataMatrix[0][NumbersEnum.SIX.getNumber()] = "LogCategory";
       dataMatrix[0][NumbersEnum.SEVEN.getNumber()] = "FlowName";
       Integer index = 1;
       for (final Binnacle binnacle : binnacleList) {
           dataMatrix[index][0] = binnacle.getIdBinnacle().toString();
           dataMatrix[index][1] = binnacle.getIdUser() == null ? "" : binnacle.getIdUser().toString();
           dataMatrix[index][2] = binnacle.getUserFullName() ==  null ? "" : binnacle.getUserFullName();
           dataMatrix[index][NumbersEnum.THREE.getNumber()] = binnacle.getIdFlow() == null ? 
        		   "" : binnacle.getIdFlow().toString();
           dataMatrix[index][NumbersEnum.FOUR.getNumber()] = binnacle.getRegisterDate();
           dataMatrix[index][NumbersEnum.FIVE.getNumber()] = binnacle.getAction();
           dataMatrix[index][NumbersEnum.SIX.getNumber()] = binnacle.getLogCategory() == null ? 
        		   "" : binnacle.getLogCategory().toString();
           dataMatrix[index][NumbersEnum.SEVEN.getNumber()] = binnacle.getFlowName() == null ? 
        		   "" : binnacle.getFlowName().toString();
           index++;
       }
       return dataMatrix;
   }
   
   public Integer findByLogCategoryTypesPaginatedTotalPages(final Binnacle binnacle)
           throws NumberFormatException, BusinessException {
       try {
           return this.configuration.totalPages(this.binnacleble.findByLogCategoryTypesPaginatedTotalPages(binnacle));
       } catch (DatabaseException databaseException) {
           LOG.error(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
           throw new BusinessException(MESSAGE_COUNT_TOTAL_PAGES_ERROR, databaseException);
       }
   }
}
