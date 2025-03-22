package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.Currenciable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Currency;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Service("CurrencyBusiness")
public class CurrencyBusiness extends AbstractExportable {
    private static final String MESSAGE_SAVING_CURRENCY_ERROR = "Hubo un problema al guardar el tipo de moneda";
    private static final String MESSAGE_RETRIEVING_CURRENCY_BY_ID_ERROR =
            "Hubo un problema al recuperar el tipo de moneda";
    private static final String MESSAGE_CURRENCY_NOT_EXISTS_ERROR =
            "El tipo de moneda ha dejado de existir";
    private static final String MESSAGE_RETRIVING_CURRENCIES_BY_STATUS_ERROR =
            "Hubo un problema al recuperar los tipos de moneda por estatus";
    private static final String MESSAGE_RETRIVING_ALL_CURRENCIES_ERROR =
            "Hubo un problema al recuperar todos los tipos de moneda";
    private static final String MESSAGE_CHANGING_CURRENCY_STATUS_ERROR =
            "Hubo un problema al cambiar el estatus del tipo de moneda";
    private static final String MESSAGE_EXPORTING_CURRENCY_ERROR =
            "Hubo un problema al exportar el catálogo de entidades de moneda";
    private static final String MESSAGE_FIND_ALL_CURRENCY_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar monedas paginados";
    private static final String MESSAGE_FIND_TOTAL_PAGES_CURRENCY_ERROR = 
            "Hubo un problema al buscar número de pagínas de monedas";

    private static final Logger LOG = Logger.getLogger(CurrencyBusiness.class);

    @Autowired
    private Currenciable currenciable;
    
    @Autowired
    private ConfigurationsBusiness configuration;
    
    public final Integer saveOrUpdate(final Currency currency) throws BusinessException {
        try {
            return this.currenciable.saveOrUpdate(currency);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_SAVING_CURRENCY_ERROR, databaseException);
            throw new BusinessException(MESSAGE_SAVING_CURRENCY_ERROR, databaseException);
        }
    }
    
    public final Currency findById(final Integer idCurrency) throws BusinessException {
        try {
            return this.currenciable.findById(idCurrency);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_CURRENCY_BY_ID_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_CURRENCY_BY_ID_ERROR, databaseException);
        } catch (EmptyResultException emptyResultException) {
            LOG.error(MESSAGE_CURRENCY_NOT_EXISTS_ERROR, emptyResultException);
            throw new BusinessException(MESSAGE_CURRENCY_NOT_EXISTS_ERROR, emptyResultException);
        }
    }
    
    public final List<Currency> findByStatus(final RecordStatusEnum status) throws BusinessException {
        try {
            return this.currenciable.findByStatus(status);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_CURRENCIES_BY_STATUS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_CURRENCIES_BY_STATUS_ERROR, databaseException);
        }
    }
    
    public final List<Currency> findAll() throws BusinessException {
        try {
            return this.currenciable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_ALL_CURRENCIES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_ALL_CURRENCIES_ERROR, databaseException);
        }
    }
    
    public final void changeStatus(final Integer idCurrency, final RecordStatusEnum status) throws BusinessException {
        try {
            if (RecordStatusEnum.ACTIVE == status)
                this.currenciable.changeStatus(idCurrency, RecordStatusEnum.INACTIVE);
            else
                this.currenciable.changeStatus(idCurrency, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_CHANGING_CURRENCY_STATUS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_CHANGING_CURRENCY_STATUS_ERROR, databaseException);
        }
    }
    
    public final List<Currency> findCurrencyCatalogPaged(final Currency currency) throws BusinessException {
        try {
            return this.currenciable.findAllCurrencyCatalogPaged(currency.getStatus(), currency.getNumberPage(), 
                    Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_CURRENCY_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_CURRENCY_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final Currency returnTotalPagesShowCurrency(final RecordStatusEnum status)
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.currenciable.countTotalItemsToShowOfPower(status);
            final Currency currency = new Currency();
            currency.setNumberPage(this.configuration.totalPages(totalPages));
            currency.setTotalRows(totalPages.intValue());
            return currency;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_CURRENCY_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_CURRENCY_ERROR, databaseException);
        }
    }

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<Currency> currencyList = this.currenciable.findAll();
	        return this.getExportCurrencyMatrix(currencyList);
	    } catch (DatabaseException dataBaseException) {
	      throw new BusinessException(MESSAGE_EXPORTING_CURRENCY_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportCurrencyMatrix(final List<Currency> currencyListParameter) {
        final Integer columnsNumber = 3;
        final String[][] dataMatrix = new String[currencyListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdCurrency";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Status";
        Integer index = 1;
        
        for (Currency currency : currencyListParameter) {
            dataMatrix[index][0] = currency.getIdCurrency().toString();
            dataMatrix[index][1] = currency.getName();
            dataMatrix[index][2] = currency.getStatus().toString();
            index++;
        }
        
        return dataMatrix;
	}
}
