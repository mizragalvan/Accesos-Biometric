package mx.pagos.admc.contracts.business.owners;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.interfaces.owners.PublicFigurable;
import mx.pagos.admc.contracts.structures.owners.PublicFigure;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.owners.PublicFigureTypeEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Service("PublicFigureBusiness")
public class PublicFigureBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(PublicFigureBusiness.class);
    private static final String MESSAGE_SAVING_ERROR = "Hubo un problema al guardar la figura pública";
    private static final String MESSAGE_CHANGE_STATUS_ERROR = "Hubo un problema al cambiar el estatus";
    private static final String MESSAGE_FIND_BY_ID_ERROR = "Hubo un problema al recuperar la figura pública";
    private static final String MESSAGE_PUBLIC_FIGURE_NOT_EXISTS_ERROR = "La figura pública ha dejado de existir";
    private static final String MESSAGE_FIND_ALL_ERROR = "Hubo un problema al recuperar todas las figuras públicas";
    private static final String MESSAGE_FIND_BY_STATUS_ERROR =
            "Hubo un problema al recuperar las figuras públicas por estatus";
    private static final String MESSAGE_FIND_BY_TYPE_ERROR =
            "Hubo un problema al recuperar las figuras públicas por tipo";
    private static final String MESSAGE_EXPORTING_PUBLIC_FIGURE_ERROR =
            "Hubo un problema al exportar el catálogo de figura pública";
    private static final String MESSAGE_FIND_ALL_PUBLIC_FIGURE_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar figuras públicas paginados";
    private static final String MESSAGE_FIND_TOTAL_PAGES_PUBLIC_FIGURE_ERROR = 
            "Hubo un problema al buscar número de pagínas de figuras públicas";
    
    @Autowired
    private PublicFigurable publicFigurable;
    
    @Autowired
    private ConfigurationsBusiness configuration;
    
    public Integer saveOrUpdate(final PublicFigure publicFigure) throws BusinessException {
        try {
            return this.publicFigurable.saveOrUpdate(publicFigure);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_SAVING_ERROR, databaseException);
            throw new BusinessException(MESSAGE_SAVING_ERROR, databaseException);
        }
    }
    
    public void changeStatus(final Integer idPublicFigure, final RecordStatusEnum status) throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)  
                this.publicFigurable.changeStatus(idPublicFigure, RecordStatusEnum.INACTIVE);
            else
                this.publicFigurable.changeStatus(idPublicFigure, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_CHANGE_STATUS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_CHANGE_STATUS_ERROR, databaseException);
        }
    }
    
    public PublicFigure findById(final Integer idPublicFigure) throws BusinessException {
        try {
            return this.publicFigurable.findById(idPublicFigure);
        } catch (EmptyResultException emptyResultException) {
            LOG.error(MESSAGE_PUBLIC_FIGURE_NOT_EXISTS_ERROR, emptyResultException);
            throw new BusinessException(MESSAGE_PUBLIC_FIGURE_NOT_EXISTS_ERROR, emptyResultException);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_BY_ID_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_BY_ID_ERROR, databaseException);
        }
    }
    
    public List<PublicFigure> findAll() throws BusinessException {
        try {
            return this.publicFigurable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_ERROR, databaseException);
        }
    }
    
    public List<PublicFigure> findByStatus(final RecordStatusEnum status) throws BusinessException {
        try {
            return this.publicFigurable.findByStatus(status);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_BY_STATUS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_BY_STATUS_ERROR, databaseException);
        }
    }
    
    public List<PublicFigure> findByType(final PublicFigureTypeEnum type) throws BusinessException {
        try {
            return this.publicFigurable.findByType(type);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_BY_TYPE_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_BY_TYPE_ERROR, databaseException);
        }
    }
    
    public List<PublicFigure> findPublicFigureCatalogPaged(final PublicFigure publicFigure) 
            throws BusinessException {
        try {
            return this.publicFigurable.findAllPublicFigureCatalogPaged(publicFigure.getStatus(),
                    publicFigure.getType(), publicFigure.getNumberPage(), 
                    Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_PUBLIC_FIGURE_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_PUBLIC_FIGURE_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public PublicFigure returnTotalPagesShowPublicFigure(final RecordStatusEnum status, 
            final PublicFigureTypeEnum type) throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.publicFigurable.countTotalItemsToShowOfPublicFigure(status, type);
            final PublicFigure publicFigure = new PublicFigure();
            publicFigure.setNumberPage(this.configuration.totalPages(totalPages));
            publicFigure.setTotalRows(totalPages.intValue());
            return publicFigure;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_PUBLIC_FIGURE_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_PUBLIC_FIGURE_ERROR, databaseException);
        }
    }
    
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<PublicFigure> publicFigureList = this.publicFigurable.findAll();
	        return this.getExportPublicFigureMatrix(publicFigureList);
	    } catch (DatabaseException dataBaseException) {
	      throw new BusinessException(MESSAGE_EXPORTING_PUBLIC_FIGURE_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportPublicFigureMatrix(final List<PublicFigure> publicFigureList) {
        final Integer columnsNumber = 4;
        final String[][] dataMatrix = new String[publicFigureList.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdPublicFigure";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Type";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "Status";
        Integer index = 1;
        
        for (PublicFigure publicFigure : publicFigureList) {
            dataMatrix[index][0] = publicFigure.getIdPublicFigure().toString();
            dataMatrix[index][1] = publicFigure.getName();
            dataMatrix[index][2] = publicFigure.getType().toString();
            dataMatrix[index][NumbersEnum.THREE.getNumber()] = publicFigure.getStatus().toString();
            index++;
        }
        
        return dataMatrix;
	}
}
