package mx.pagos.admc.contracts.business;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.Guaranteesable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.CheckDocument;
import mx.pagos.admc.contracts.structures.Guarantees;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.UserSession;

@Service("GuaranteesBusiness")
public class GuaranteesBusiness extends AbstractExportable {
    private static final String DOT = ".";
    private static final String WHITE_SPACE = " ";
    private static final String UNDERLINE = "_";
    private static final Logger LOG = Logger.getLogger(GuaranteesBusiness.class);
    private static final String INVALID_CHARACTERS = "[\\\\/><\\|\\s\"'{}()\\[\\]:]+";
    private static final String MESSAGE_FIND_CHECK_DOCUMENT_ERROR = 
            "Hubo un problema al recuperar los checklist pertenecientes a la garantía";
    private static final String MESSAGE_EXPORTING_GUARANTEES_ERROR =
            "Hubo un problema al exportar el catálogo de entidades de garantías";
    private static final String MESSAGE_FIND_ALL_GUARANTEES_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar garantías paginadas";
    private static final String MESSAGE_FIND_TOTAL_PAGES_GUARANTEES_ERROR = 
            "Hubo un problema al buscar número de pagínas de garantías";
    
    @Autowired
    private Guaranteesable guaranteesable;

    @Autowired
    private ConfigurationsBusiness configuration;

    @Autowired
    private UserSession session;

    @Autowired
    private CheckDocumentBusiness checkDocumentBusiness;

    public final Integer saveOrUpdate(final Guarantees guaranteesParameter) throws BusinessException {
        try {
            this.saveFile(guaranteesParameter);
            final Integer idGuarantee = this.guaranteesable.saveOrUpdate(guaranteesParameter);
            for (CheckDocument checkDocument : guaranteesParameter.getCheckDocumentList()) {
                checkDocument.setIdGuarantee(idGuarantee);
                this.checkDocumentBusiness.saveOrUpdate(checkDocument);
            }
            return idGuarantee;
        } catch (DatabaseException | IOException databaseException) {
            LOG.error("Hubo un error al guardar los datos de Garantía", databaseException);
            throw new BusinessException("Error al guardar los datos de Garantía", databaseException);
        }
    }

    private void saveFile(final Guarantees guarantees) throws BusinessException, IOException {
        Path userTemporalPath = null;
        Path targetDocumentPath = null;
        if (guarantees.getIsNewFile())
            guarantees.setPath(guarantees.getPath().replaceAll(INVALID_CHARACTERS, UNDERLINE));
        if (guarantees.getIdGuarantee() == null)
            userTemporalPath = this.createFilePath(new File(this.createUserTemporalPath()),
                    guarantees.getPath());
        else
            userTemporalPath = this.isSamePath(guarantees);
        targetDocumentPath = this.createFilePath(new File(this.createTargetDocumentPath()),
                guarantees.getName().replace(WHITE_SPACE, UNDERLINE) + 
                DOT + FilenameUtils.getExtension(guarantees.getPath()));
        Files.copy(userTemporalPath, targetDocumentPath, StandardCopyOption.ATOMIC_MOVE);
        guarantees.setPath(this.createFileTargetDocumentPath(guarantees.getPath(),
                guarantees.getName().replace(WHITE_SPACE, UNDERLINE)));
    }

    private Path isSamePath(final Guarantees guarantees) throws BusinessException {
        Path userTemporalPath;
        if (this.findById(guarantees.getIdGuarantee()).getPath().equals(guarantees.getPath()))
            userTemporalPath = this.convertToPath(guarantees.getPath());
        else
            userTemporalPath = this.createFilePath(new File(this.createUserTemporalPath()), guarantees.getPath());
        return userTemporalPath;
    }

    private String createUserTemporalPath() throws BusinessException {
        return this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_TMP
                + File.separator + this.session.getIdUsuarioSession();
    }

    private String createTargetDocumentPath() throws BusinessException {
        return this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_GUARANTEE;
    }

    private Path convertToPath(final String path) {
        return FileSystems.getDefault().getPath(path);
    }

    private Path createFilePath(final File userTemporalFilesPath, final String fileName) {
        return FileSystems.getDefault().getPath(userTemporalFilesPath.getAbsolutePath() + File.separator + fileName);
    }

    private String createFileTargetDocumentPath(final String fileName, final String newFileName)
            throws BusinessException {
        return this.createTargetDocumentPath() + File.separator + newFileName + DOT
                + FilenameUtils.getExtension(fileName);
    }

    public final void changeGuaranteesStatus(final Integer idGuasrantees,
            final RecordStatusEnum status) throws BusinessException {
        try {
            if (status == RecordStatusEnum.INACTIVE)
                this.guaranteesable.changeGuaranteesStatus(idGuasrantees, RecordStatusEnum.ACTIVE);
            else
                this.guaranteesable.changeGuaranteesStatus(idGuasrantees, RecordStatusEnum.INACTIVE);
        } catch (DatabaseException databaseException) {
            LOG.error("Hubo un error al cambiar el estatus de Garantía", databaseException);
            throw new BusinessException("Error al cambiar el estatus de Garantía", databaseException);
        }
    }

    public final List<Guarantees> findAll() throws BusinessException {
        try {
            return this.guaranteesable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error("Hubo un error al obtener Garantías", databaseException);
            throw  new BusinessException("Error al obtener Garantías", databaseException);
        }
    }

    public final List<Guarantees> findByStatus(final RecordStatusEnum statusEnum) throws BusinessException {
        try {
            return this.guaranteesable.findByStatus(statusEnum);
        } catch (DatabaseException databaseException) {
            LOG.error("Hubo un error al obtener Garantías por estatus", databaseException);
            throw new BusinessException("Error al obtener Garantías por estatus", databaseException);
        }
    }

    public final Guarantees findById(final Integer idGuasrantee) throws BusinessException {
        try {
            return this.guaranteesable.findById(idGuasrantee);
        } catch (DatabaseException databaseException) {
            LOG.error("Hubo un error al obtener Garantía por Id", databaseException);
            throw new BusinessException("Error al obtener Garantias por id", databaseException);
        }
    }

    public final List<CheckDocument> findCheckDocumentListByIdGuarantee(final Integer idGuarantee) 
            throws BusinessException {
        try {
            return this.guaranteesable.findCheckDocumentListByIdGuarantee(idGuarantee);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_CHECK_DOCUMENT_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_CHECK_DOCUMENT_ERROR, databaseException);
        }
    }
    
    public final List<Guarantees> findGuaranteeCheckDocuementation(final RecordStatusEnum status) 
            throws BusinessException {
        final List<Guarantees> guaranteeCheckDocumentList = new ArrayList<>();
        final List<Guarantees> guarantesList = this.findByStatus(status);
        for (final Guarantees guarantees : guarantesList) {
            guarantees.setCheckDocumentList(this.findCheckDocumentListByIdGuarantee(guarantees.getIdGuarantee()));
            if (guarantees.getCheckDocumentList() != null && guarantees.getCheckDocumentList().size() > 0) 
                guaranteeCheckDocumentList.add(guarantees);
        }
        return guaranteeCheckDocumentList;
    }
    
    public final List<Guarantees> findGuaranteesCatalogPaged(final Guarantees guarantees) 
            throws BusinessException {
        try {
            return this.guaranteesable.findAllGuaranteesCatalogPaged(guarantees.getStatus(), 
                    guarantees.getNumberPage(), Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_GUARANTEES_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_GUARANTEES_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final Guarantees returnTotalPagesShowGuarantees(final RecordStatusEnum status) 
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.guaranteesable.countTotalItemsToShowOfGuarantees(status);
            final Guarantees guarantees = new Guarantees();
            guarantees.setNumberPage(this.configuration.totalPages(totalPages));
            guarantees.setTotalRows(totalPages.intValue());
            return guarantees;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_GUARANTEES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_GUARANTEES_ERROR, databaseException);
        }
    }
    
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<Guarantees> guaranteesList = this.guaranteesable.findAll();
	        return this.getExportGuaranteesMatrix(guaranteesList);
	    } catch (DatabaseException dataBaseException) {
	      throw new BusinessException(MESSAGE_EXPORTING_GUARANTEES_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportGuaranteesMatrix(
			final List<Guarantees> guaranteesListParameter) {
        final Integer columnsNumber = 4;
        final String[][] dataMatrix = new String[guaranteesListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdGuarantee";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Path";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "Status";
        Integer index = 1;
        
        for (Guarantees guarantee : guaranteesListParameter) {
            dataMatrix[index][0] = guarantee.getIdGuarantee().toString();
            dataMatrix[index][1] = guarantee.getName();
            dataMatrix[index][2] = guarantee.getPath();
            dataMatrix[index][NumbersEnum.THREE.getNumber()] = guarantee.getStatus().toString();
            index++;
        }
        
        return dataMatrix;
	}
}
