package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.Dgable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("DgaBusiness")
public class DgaBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(DgaBusiness.class);
    private static final String MESSAGE_EXPORTING_DGA_ERROR =
            "Hubo un problema al exportar el catálogo de dga";
    private static final String MESSAGE_FIND_ALL_DGA_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar dgas paginados";
    private static final String MESSAGE_FIND_TOTAL_PAGES_DGA_ERROR = 
            "Hubo un problema al buscar número de pagínas de dgas";
    
    @Autowired
    private Dgable dgable;
    
    @Autowired
    private ConfigurationsBusiness configuration;
    
    public final Integer saveOrUpdate(final Dga dga) throws BusinessException {
        try {
        	if(this.dgable.ExistsDGAByName(dga.getName())){
        		throw new BusinessException("Existe un DGA con el mismo nombre");
        	}
            return this.dgable.saveOrUpdate(dga);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al guardar los datos pertenecientes al DGA", dataBaseException);
            throw new BusinessException("Error al guardar datos del Dga", dataBaseException);
        }
    }

    public final void changeDgaStatus(final Integer idDga, final RecordStatusEnum status)
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.dgable.changeDgaStatus(idDga, RecordStatusEnum.INACTIVE);	
            else
                this.dgable.changeDgaStatus(idDga, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al cambiar el estatus al DGA", dataBaseException);
            throw new BusinessException("Error al cambiar el estatus del Dga", dataBaseException);
        }
    }

    public final List<Dga> findAll() throws BusinessException {
        try {
            return this.dgable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener los DGA", dataBaseException);
            throw new BusinessException("Error al obtener datos del Dga", dataBaseException);
        }
    }

    public final Dga findById(final Integer idDga)
            throws BusinessException {
        try {
            return this.dgable.findById(idDga);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener DGA por Id", dataBaseException);
            throw new BusinessException("Error al obtener Dga", dataBaseException);
        }
    }

    public final List<Dga> findByStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            return this.dgable.findByStatus(recordStatusEnum);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener DGAs por estatus", dataBaseException);
            throw new BusinessException("Error al obtener estatus del Dga", dataBaseException);
        }
    }

    public final List<Dga> findDgaCatalogPaged(final Dga dga) throws BusinessException {
        try {
            return this.dgable.findAllDgaCatalogPaged(dga.getStatus(), dga.getNumberPage(), 
                    Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_DGA_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_DGA_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final Dga returnTotalPagesShowDga(final RecordStatusEnum status) 
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.dgable.countTotalItemsToShowOfDga(status);
            final Dga dga = new Dga();
            dga.setNumberPage(this.configuration.totalPages(totalPages));
            dga.setTotalRows(totalPages.intValue());
            return dga;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_DGA_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_DGA_ERROR, databaseException);
        }
    }
    
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<Dga> dgaList = this.dgable.findAll();
	        return this.getExportDgaMatrix(dgaList);
	    } catch (DatabaseException dataBaseException) {
	      LOG.error(MESSAGE_EXPORTING_DGA_ERROR, dataBaseException);
	      throw new BusinessException(MESSAGE_EXPORTING_DGA_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportDgaMatrix(final List<Dga> dgaListParameter) {
        final Integer columnsNumber = 3;
        final String[][] dataMatrix = new String[dgaListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdDga";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Status";
        Integer index = 1;
        
        for (Dga dga : dgaListParameter) {
            dataMatrix[index][0] = dga.getIdDga().toString();
            dataMatrix[index][1] = dga.getName();
            dataMatrix[index][2] = dga.getStatus().toString();
            index++;
        }
        
        return dataMatrix;
	}
}
