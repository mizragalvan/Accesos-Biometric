package mx.pagos.admc.contracts.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.pagos.admc.contracts.interfaces.Requisitable;
import mx.pagos.admc.contracts.interfaces.RequisitionVersionable;
import mx.pagos.admc.contracts.interfaces.SupplierPersonable;
import mx.pagos.admc.contracts.interfaces.Supplierable;
import mx.pagos.admc.contracts.structures.ApprovalArea;
import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.RequiredDocumentBySupplier;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionVersion;
import mx.pagos.admc.contracts.structures.Supplier;
import mx.pagos.admc.contracts.structures.SupplierPerson;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.admc.enums.SupplierPersonTypeEnum;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.business.UsersBusiness;
import mx.pagos.security.structures.User;

@Service
public class RequisitionVersionBusiness {
	@Autowired
	private RequisitionVersionable requisitionVersion;

//	@Autowired
//	private RequisitionBusiness requisitionBusiness;


	@Autowired
	private Requisitable requisitable;
	
	@Autowired
    private UsersBusiness usersBusiness;
	
	private static final Logger LOG = Logger.getLogger(RequisitionVersionBusiness.class);

	private static final String MESSAGE_SAVING_REQUISITION_VERSION_ERROR = "Hubo un error al guardar la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_REQUISITION_VERSIONS_ERROR = "Hubo un problema al recuperar las versiones de la solicitud";
	private static final String MESSAGE_REQUISITION_VERSION_NOT_EXISTS_ERROR = "La versión de la solicitud ha dejado de existir";
	private static final String MESSAGE_RETRIEVING_REQUISITION_VERSION_ERROR = "Hubo un problema al recuerar la información de la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_APPROVAL_AREAS_ERROR = "Hubo un problema al recuperar las áreas de aprovación de la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_ATTATCHMENTS_ERROR = "Hubo un problema al recuperar los anexos de la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_AUTHORIZATION_DGAS_ERROR = "Hubo un problema al recuperar las DGA's";
	private static final String MESSAGE_RETRIEVING_FINANTIAL_ENTITIES_ERROR = "Hubo un problema al recuperara las entidades de la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_FINANTIAL_ENTITIES_WITNESSES_ERROR = "Hubo un problema al recupear los testigos de las entidades de la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR = "Hubo un problema al recuperar los representantes legales de la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_USERS_VOBO_ERROR = "Hubo un problema al recuperar los usuarios para VoBo de la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_APPROVAL_AREAS_VOBO_ERROR = "Hubo un error al recuperar los documentos de VoBo de las áreas "
			+ "de aprobación de la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_APPROVAL_AREAS_NAMES = "Hubo un problema al recuperar las Áreas de aprobación de la versión de la solicitud";
	private static final String MESSAGE_FIND_ID_REQUISITION_VERSION_ERROR = "Hubo un problema al recuperar el id de la versión de la solicitud";
	private static final String MESSAGE_RETRIEVING_WITNESSES_ERROR = "Hubo un problema al recuperar los testigos del proveedor";
	private static final String MESSAGE_SUPPLIER_NOT_FOUND = "No se encontró el proveedor";
	private static final String MESSAGE_FIND_SUPPLIER_REQUIRED_DOCUMENT_ERROR = "Hubo un problema al buscar los documentos requeridos del proveedor";
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public Integer saveRequisitionVersion(final Integer idRequisition) throws BusinessException {
		try {
			final Integer idRequisitionVersion = this.requisitionVersion.saveRequisitionVersion(idRequisition);
			this.requisitionVersion.saveRequisitionRequisitionVersion(idRequisition, idRequisitionVersion);
			this.requisitionVersion.saveRequisitionApprovalAreasVersion(idRequisition, idRequisitionVersion);
			this.requisitionVersion.saveRequisitionAttatchmentsVersion(idRequisition, idRequisitionVersion);
			this.requisitionVersion.saveRequisitionAuthorizationDgasVersion(idRequisition, idRequisitionVersion);
			this.requisitionVersion.saveRequisitionFinantialEntitiesVersion(idRequisition, idRequisitionVersion);
			this.requisitionVersion.saveRequisitionFinantialEntityWitnessesVersion(idRequisition, idRequisitionVersion);
			this.requisitionVersion.saveRequisitionLegalRepresentativesVersion(idRequisition, idRequisitionVersion);
			this.requisitionVersion.saveRequisitionUsersVoboVersion(idRequisition, idRequisitionVersion);
			this.requisitionVersion.saveScalingVersion(idRequisition, idRequisitionVersion);
			return idRequisitionVersion;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_REQUISITION_VERSION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_REQUISITION_VERSION_ERROR, databaseException);
		}
	}

	public List<VersionDTO> findRequisitionVersions(final Integer idRequisition) throws BusinessException {
//		try {
//			return this.requisitionVersion.findRequisitionVersions(idRequisition);
//		} catch (DatabaseException databaseException) {
//			LOG.error(MESSAGE_RETRIEVING_REQUISITION_VERSIONS_ERROR, databaseException);
//			throw new BusinessException(MESSAGE_RETRIEVING_REQUISITION_VERSIONS_ERROR, databaseException);
//		}
		try {
			List<VersionDTO> versions = this.requisitionVersion.findRequisitionVersions(idRequisition);
			for (VersionDTO versionDto : versions) {
				versionDto.setFileName(FilenameUtils.getName(versionDto.getDocumentPath()));
			}
			return versions;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_REQUISITION_VERSIONS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_REQUISITION_VERSIONS_ERROR, databaseException);
		}
	
	}

	public Requisition findRequisitionVersionById(final Integer idRequisitionVersion) throws BusinessException {
		try {
		    final Requisition requisition = this.requisitionVersion.findById(idRequisitionVersion);
		    requisition.setSupplier(this.findById(requisition.getIdSupplier()));
		    requisition.getSupplier().setSupplierPerson(this.findLegalRepresentativesByIdSupplier(
                    requisition.getIdSupplier()).size() > 0 ? this.findLegalRepresentativesByIdSupplier(
            requisition.getIdSupplier()).get(0) : new SupplierPerson());
			return requisition;
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_REQUISITION_VERSION_NOT_EXISTS_ERROR, emptyResultException);
			throw new BusinessException(MESSAGE_REQUISITION_VERSION_NOT_EXISTS_ERROR, emptyResultException);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_REQUISITION_VERSION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_REQUISITION_VERSION_ERROR, databaseException);
		}
	}
	
	@Autowired
	private Supplierable supplierable;
	
	@Autowired
	private SupplierPersonable supplierPersonable;
	
	public Supplier findById(final Integer idSupplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findById");
			final Supplier supplier = this.supplierable.findById(idSupplier);
			supplier.setWitnessesList(this.findWitnessesByIdSupplier(idSupplier));
			supplier.setLegalRepresentativesList(this.findLegalRepresentativesByIdSupplier(idSupplier));
			supplier.setSupplierPerson(this.findLegalRepresentativesByIdSupplier(idSupplier).size() > 0
					? this.findLegalRepresentativesByIdSupplier(idSupplier).get(0)
							: new SupplierPerson());
			return supplier;
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener Proveedor por Id", databaseException);
			throw new BusinessException("Error al obtener Proveedores por id", databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_SUPPLIER_NOT_FOUND, emptyResultException);
			throw new BusinessException(MESSAGE_SUPPLIER_NOT_FOUND, emptyResultException);
		}
	}
	public List<SupplierPerson> findWitnessesByIdSupplier(final Integer idSupplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findWitnessesByIdSupplier");
			return this.supplierPersonable.findSupplierPersonsByIdSupplierAndType(idSupplier,
					SupplierPersonTypeEnum.WITNESS);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_WITNESSES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_WITNESSES_ERROR, databaseException);
		}
	}
	
	public List<SupplierPerson> findLegalRepresentativesByIdSupplier(final Integer idSupplier)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findLegalRepresentativesByIdSupplier");
			return this.supplierPersonable.findSupplierPersonsByIdSupplierAndType(idSupplier,
					SupplierPersonTypeEnum.LEGALREPRESENTATIVE);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	public Requisition findWholeRequisitionVersionById(final RequisitionVersion version) throws BusinessException {
	    final Requisition requisition;
	    if (version.getIsCompareCurrentRequisition()) {
	        final RequisitionVersion requisitionVersion = this.findIdRequisitionVersion(version.getIdRequisition());
	        requisition = this.findRequisitionVersion(requisitionVersion.getIdRequisitionVersion());
	        requisition.setIdRequisition(version.getIdRequisition());
	        requisition.setVersionNumber(requisitionVersion.getVersionNumber() != null ?
	                requisitionVersion.getVersionNumber().toString() : null);
	        return requisition;
	    } else {
	        requisition = this.findRequisitionVersion(version.getIdRequisitionVersion());
	        requisition.setIdRequisition(version.getIdRequisition());
	        requisition.setVersionNumber(version.getVersionNumber() != null ?
	                version.getVersionNumber().toString() : null);
	        return requisition;
	    }
	}

	public Requisition findSecondWholeRequisitionVersionById(final RequisitionVersion version) 
	        throws BusinessException {
	    final Requisition requisition = this.findRequisitionVersion(version.getIdSecondRequisitionVersion());
	    requisition.setIdRequisition(version.getIdRequisition());
	    requisition.setVersionNumber(version.getSecondVersionNumber().toString());
	    return requisition;
	}
	
    private Requisition findRequisitionVersion(final Integer idRequisitionVersion) throws BusinessException {
        final Requisition reqver = this.findRequisitionVersionById(idRequisitionVersion);
        reqver.setApplicant(this.usersBusiness.findByUserId(reqver.getIdApplicant()));
        if (reqver.getUpdateRequisitionBy() != null)
            reqver.setUpdateByUser(this.usersBusiness.findByUserId(reqver.getUpdateRequisitionBy()));
		reqver.setSupplierLegalRepresentativesList(
		        this.findLegalRepresentativesByIdSupplier(reqver.getIdSupplier()));	
		reqver.setUsersToVoboUserList(this.findUsersVobo(idRequisitionVersion));
		if (reqver.getIdLawyer() != null)
			reqver.setLawyer(this.usersBusiness.findByUserId(reqver.getIdLawyer()));
		reqver.setApprovalAreasActiveList(this.findApprovalAreasName(idRequisitionVersion));
		
		List<FinancialEntity> financialEntities = this.findFinantialEntities(idRequisitionVersion);
		reqver.setFinancialEntity(financialEntities!=null && !financialEntities.isEmpty() ? financialEntities.get(0) : null);
		List<FinancialEntity> dataEntities = this.findFinantialEntities(idRequisitionVersion);
		reqver.setDataFinancialEntityList(dataEntities);
		
		reqver.setRequiredDocumentBySupplier( this.findSupplierRequiredDocument(reqver.getIdSupplier())); 	
		reqver.setLegalRepresentativesList(this.findLegalRepresentatives(idRequisitionVersion));
		reqver.setAttachmentListDocument(this.findAttatchments(idRequisitionVersion));
		reqver.setFinancialEntityWitnessesList(this.findFinantialEntitiesWitnesses(idRequisitionVersion));
        return reqver;
    }
    

	public List<RequiredDocumentBySupplier> findSupplierRequiredDocument(final Integer idSupplier)
			throws BusinessException {
		LOG.info(this.getClass().getSimpleName() + " -> findSupplierRequiredDocument : " + idSupplier);
		try {
			return this.requisitable.findRequiredDocumentsBySupplier(idSupplier);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_SUPPLIER_REQUIRED_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_SUPPLIER_REQUIRED_DOCUMENT_ERROR, databaseException);
		}
	}

    public RequisitionVersion findIdRequisitionVersion(final Integer idRequisition) throws BusinessException {
        try {
            return this.requisitionVersion.findIdRequisitionVersion(idRequisition);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ID_REQUISITION_VERSION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ID_REQUISITION_VERSION_ERROR, databaseException);
        }
    }
    
	public List<Integer> findApprovalAreas(final Integer idRequisitionVersion) throws BusinessException {
		try {
			return this.requisitionVersion.findApprovalAreas(idRequisitionVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_APPROVAL_AREAS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_APPROVAL_AREAS_ERROR, databaseException);
		}
	}

	public List<String> findApprovalAreasActive(final Integer idRequisitionVersion) throws BusinessException {
		try {
			return this.requisitionVersion.findApprovalAreasActive(idRequisitionVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_APPROVAL_AREAS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_APPROVAL_AREAS_ERROR, databaseException);
		}
	}
	
	public List<Version> findAttatchments(final Integer idRequisitionVersion) throws BusinessException {
		try {
			final List<Version> versionList = this.requisitionVersion.findAttatchments(idRequisitionVersion);
			final List<Version> versionReturn = new ArrayList<>();
			for (Version version : versionList) {
			    version.setFileName(FilenameUtils.getName(version.getDocumentPath()));
				versionReturn.add(version);
			}
			return versionReturn;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_ATTATCHMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_ATTATCHMENTS_ERROR, databaseException);
		}
	}

	public List<Dga> findAuthorizationDgas(final Integer idRequisitionVersion) throws BusinessException {
		try {
			return this.requisitionVersion.findAuthorizationDgas(idRequisitionVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_AUTHORIZATION_DGAS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_AUTHORIZATION_DGAS_ERROR, databaseException);
		}
	}

	public List<FinancialEntity> findFinantialEntities(final Integer idRequisitionVersion) throws BusinessException {
		try {
			return this.requisitionVersion.findFinantialEntities(idRequisitionVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_FINANTIAL_ENTITIES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FINANTIAL_ENTITIES_ERROR, databaseException);
		}
	}

	public List<String> findFinantialEntitiesWitnesses(final Integer idRequisitionVersion) throws BusinessException {
		try {
			return this.requisitionVersion.findFinantialEntitiesWitnesses(idRequisitionVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_FINANTIAL_ENTITIES_WITNESSES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FINANTIAL_ENTITIES_WITNESSES_ERROR, databaseException);
		}
	}

	public List<LegalRepresentative> findLegalRepresentatives(final Integer idRequisitionVersion)
			throws BusinessException {
		try {
			return this.requisitionVersion.findLegalRepresentatives(idRequisitionVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	public List<User> findUsersVobo(final Integer idRequisitionVersion) throws BusinessException {
		try {
			return this.requisitionVersion.findUsersVobo(idRequisitionVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_USERS_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_USERS_VOBO_ERROR, databaseException);
		}
	}

	public List<ApprovalArea> findApprovalAreasVoBo(final Integer idRequisitionVersion) throws BusinessException {
		try {
			return this.requisitionVersion.findApprovalAreasVoBo(idRequisitionVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_APPROVAL_AREAS_VOBO_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_APPROVAL_AREAS_VOBO_ERROR, databaseException);
		}
	}

	public List<String> findApprovalAreasName(final Integer idRequisitionVersion) throws BusinessException {
		try {
			return this.requisitionVersion.findApprovalAreasName(idRequisitionVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_APPROVAL_AREAS_NAMES, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_APPROVAL_AREAS_NAMES, databaseException);
		}
	}

	// DEBUGGING CODE PLEASE DO NOT REMOVE - LuisV
	public String debugMissingFields() {
		String s;
		try {
			s = requisitionVersion.debug("getAllQueries");
		} catch(Exception e) {
			s = e.getMessage() + " " + e.getStackTrace();
		}
		return s;
	}	
	
	public String debugGetAllQueries() {
		String s;
		try {			
			s = requisitionVersion.debug("getAllQueries");	
		} catch(Exception e) {
			s = e.getMessage() + " " + e.getStackTrace();
		}
		return s;
	}
	
	public String debugAddRequisitionFields() {
		String s;
		try {			
			s = requisitionVersion.debug("addMissingFieldsToTable");	
		} catch(Exception e) {
			s = e.getMessage() + " " + e.getStackTrace();
		}
		return s;
	}
	// END OF DEBUGGING CODE
}

