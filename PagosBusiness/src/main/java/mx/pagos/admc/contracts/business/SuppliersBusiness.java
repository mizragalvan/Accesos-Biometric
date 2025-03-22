package mx.pagos.admc.contracts.business;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.interfaces.SupplierPersonable;
import mx.pagos.admc.contracts.interfaces.Supplierable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Personality;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.contracts.structures.Supplier;
import mx.pagos.admc.contracts.structures.SupplierPerson;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.SupplierPersonTypeEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

/**
 * 
 * Clase que contiene las reglas de negocio para el manejo de proveedores.
 * Permite guardar, recuperar y cambiar estatus de los mismos;
 * 
 * @author Mizraim
 * 
 * @see Supplier
 * @See SupplierPerson
 * @see Supplierable
 * @See SupplierPersonable
 * @see BusinessException
 *
 */
@Service("SuppliersBusiness")
public class SuppliersBusiness extends AbstractExportable {
	private static final String MESSAGE_FIND_PERSONALITY_ERROR = "Hubo un problema al determinar la personalidad del proveedor";
	private static final String SUPPLIER_LAWYERS = "representantes legales del proveedor";
	private static final String LOS_TESTIGOS_DEL_PROVEEDOR = "los testigos del proveedor.";
	private static final String MESSAGE_SAVE_SUPPLIER_ERROR = "Ocurrió un problema al guardar los datos de Proveedor";
	private static final String MESSAGE_FIND_BY_STATUS_ERROR = "Ocurrió un problema al obtener los proveedores por estatus";
	private static final String MESSAGE_FIND_SUPPLIER_LEGAL_REPRESENTATIVES_ERROR = "Ocurrió un problema al obtener representantes legales del proveedores";
	private static final String MESSAGE_FIND_BY_RFC_ERROR = "Ocurrió un problema al obtener el proveedor por RFC";

	private static final Logger LOG = Logger.getLogger(SuppliersBusiness.class);

	private static final String MESSAGE_DELETING_SUPPLIER_PERSON_ERROR = "Ocurrió un problema al eliminar las personas del proveedor";
	private static final String MESSAGE_RETRIEVING_SUPPLIER_PERSON_ERROR = "Ocurrió un problema al obtener los testigos y/o representantes legales del proveedor";
	private static final String MESSAGE_SAVING_SUPPLIER_PERSON_ERROR = "Ocurrió un problema al guardar ";
	private static final String MESSAGE_SUPPLIER_NOT_FOUND_BY_RFC = "El RFC/ID TAX no ha sido ingresado anteriormente. Se creará un nuevo proveedor.";
	private static final String MESSAGE_SUPPLIER_EROOR_BY_RFC = "El RFC/ID TAX es incorrecto.";
	private static final String MESSAGE_SUPPLIER_NOT_FOUND = "No se encontró el proveedor";
	private static final String MESSAGE_UPDATE_SUPPLIER_FIELDS = "No se pudo actualizar los datos del proveedor";
	private static final String MESSAGE_IS_COMPANY_NAME_EXIST_ERROR = "No se pudo validar si la denominación social existe";
	private static final String MESSAGE_EXPORTING_SUPPLIER_ERROR = "Hubo un problema al exportar el catálogo de proveedores";
	private static final String MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR = "Hubo un problema al recuperar los representantes legales del proveedor";
	private static final String MESSAGE_FIND_ALL_SUPPLIER_CATALOG_PAGED_ERROR = "Hubo un problema al buscar proveedores paginados";
	private static final String MESSAGE_FIND_TOTAL_PAGES_SUPPLIER_ERROR = "Hubo un problema al buscar número de proveedores";
	private static final String MESSAGE_SAVE_SUPPLIER_REQUIRED_DOCUMENT_ERROR = "Hubo un problema al guardar documentos requeridos del proveedor";
	private static final String MESSAGE_RETRIEVING_WITNESSES_ERROR = "Hubo un problema al recuperar los testigos del proveedor";

	@Autowired
	private Supplierable supplierable;

	@Autowired
	private SupplierPersonable supplierPersonable;

	@Autowired
	private RequisitionBusiness requisitionBusiness;

	@Autowired
	private ConfigurationsBusiness configuration;

	public Integer saveOrUpdate(final Supplier supplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveOrUpdate");
			final Integer idSupplier = this.supplierable.saveOrUpdate(supplier);
			this.saveSupplierPersonsList(idSupplier, supplier.getSupplierPersonList());
			this.saveSupplierPersonsList(idSupplier, supplier.getSupplierWitnessList(), SupplierPersonTypeEnum.WITNESS);
			final List<RequiredDocument> list = this.requisitionBusiness
					.supplierRequiredDocument(supplier.getSupplierRequiredDocument(), idSupplier);
			supplier.setRequiredDocumentList(list);
			supplier.setIdSupplier(idSupplier);
			for (RequiredDocument getRequiredDocument : supplier.getRequiredDocumentList())
				this.saveSupplierRequiredDocument(idSupplier, getRequiredDocument);
			return idSupplier;
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_SAVE_SUPPLIER_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_SAVE_SUPPLIER_ERROR, dataBaseException);
		}
	}

	private void saveSupplierRequiredDocument(final Integer idSupplier, final RequiredDocument getRequiredDocument)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveSupplierRequiredDocument  DOC ::"+getRequiredDocument.getName());
			this.supplierable.deleteSupplierRequiredDocument(idSupplier, getRequiredDocument.getIdRequiredDocument());
			this.supplierable.saveRequiredDocument(idSupplier, getRequiredDocument.getIdRequiredDocument(),
					getRequiredDocument.getIdDocument());
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVE_SUPPLIER_REQUIRED_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVE_SUPPLIER_REQUIRED_DOCUMENT_ERROR, databaseException);
		}
	}

	public void updateDraftSupplierFields(final Supplier supplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> updateDraftSupplierFields");
			if (supplier.getSupplierPersonList() != null && supplier.getSupplierPersonList().size() > 0) {
				this.saveSupplierPersonsList(supplier.getIdSupplier(), supplier.getSupplierPersonList());
			}
			this.supplierable.updateDraftSupplierFields(supplier);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_UPDATE_SUPPLIER_FIELDS, databaseException);
			throw new BusinessException(MESSAGE_UPDATE_SUPPLIER_FIELDS, databaseException);
		}
	}

	public void changeSupplierStatus(final Integer idSupplier, final RecordStatusEnum status) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> changeSupplierStatus");
			if (status == RecordStatusEnum.ACTIVE)
				this.supplierable.changeSupplierStatus(idSupplier, RecordStatusEnum.INACTIVE);
			else
				this.supplierable.changeSupplierStatus(idSupplier, RecordStatusEnum.ACTIVE);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al cambiar estatus de Proveedor", databaseException);
			throw new BusinessException("Error al cambiar el estatus del Proveedor", databaseException);
		}
	}

	public List<Supplier> findAll() throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findAll");
			return this.supplierable.findAll();
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener Proveedores", databaseException);
			throw new BusinessException("Error al obtener datos del Proveedor", databaseException);
		}
	}

	public List<Supplier> findByRecordStatus(final RecordStatusEnum recordStatusEnum) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findByRecordStatus");
			return this.supplierable.findByRecordStatus(recordStatusEnum);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_FIND_BY_STATUS_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_FIND_BY_STATUS_ERROR, dataBaseException);
		}
	}

	public List<SupplierPerson> findSupplierLegalRepresentativesPower(final Integer idSupplier)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findSupplierLegalRepresentativesPower");
			return this.supplierPersonable.findSupplierLegalRepresentativesPower(idSupplier);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_FIND_SUPPLIER_LEGAL_REPRESENTATIVES_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_FIND_SUPPLIER_LEGAL_REPRESENTATIVES_ERROR, dataBaseException);
		}
	}

	public List<Supplier> findByNameAndRfc(final String name, final String rfc) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findByNameAndRfc");
			return this.supplierable.findByNameAndRfc(name, rfc);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener Proveedores por nombre y RFC", databaseException);
			throw new BusinessException("Error al obtener Proveedores por Nombre y RFC", databaseException);
		}
	}

	public Supplier findByRfc(final String rfc, final Integer personalidad) throws BusinessException, PatternSyntaxException {
		try {
			
			 if(!this.validaRfc(rfc, personalidad)) {	
				 LOG.error(MESSAGE_SUPPLIER_EROOR_BY_RFC);
				 throw new PatternSyntaxException(MESSAGE_SUPPLIER_EROOR_BY_RFC, rfc, personalidad);
			 }
			
			LOG.info("SuppliersBusiness -> findByRfc");
			return this.supplierable.findByRfc(rfc);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_BY_RFC_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_BY_RFC_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_SUPPLIER_NOT_FOUND_BY_RFC, emptyResultException);
			throw new BusinessException(MESSAGE_SUPPLIER_NOT_FOUND_BY_RFC, emptyResultException);
		}
	}
	
	public static boolean validaRfc(final String rfc, final Integer personalidad){
		String regex = "([a-zñA-ZÑ]{3,4}([0-9]{6})([a-zñA-ZÑ|0-9]{3}))";
		 if(personalidad != 3 && personalidad != 4) { // no extrajera
			  Pattern p = Pattern.compile(regex);
			  Matcher m = p.matcher(rfc);
			  LOG.info(m.matches());
			  return m.matches();
		 }
		 return true;
        
    }

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

	public List<RequiredDocument> findRequiredDocumentsByIdSupplier(final Integer idSupplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findRequiredDocumentsByIdSupplier");
			return this.supplierable.findRequiredDocumentsByIdSupplier(idSupplier);
		} catch (DatabaseException databaseException) {
			throw new BusinessException("Error al obtener documentos requeridos", databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveSupplierPersonsList(final Integer idSupplier, final List<SupplierPerson> supplierPersonsList)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveSupplierPersonsList LEGAL");
			for (SupplierPerson supplierPerson : supplierPersonsList) {
				if (supplierPerson.getIdSupplierPerson() != null && supplierPerson.getIdSupplierPerson() > 0) {
					this.updateSupplier(supplierPerson);
				} else {
					if (supplierPerson.getName() != null) {
						supplierPerson.setIdSupplier(idSupplier);
						supplierPerson.setActive(Boolean.TRUE);
						LOG.info("supplierPerson.getIdSupplier()" + supplierPerson.getIdSupplier());
						LOG.info("supplierPerson.getName()" + supplierPerson.getName().trim());
						LOG.info("supplierPerson.getSupplierPersonType()" + supplierPerson.getSupplierPersonType());
						LOG.info("supplierPerson.getIdSupplier()" + supplierPerson.getIdSupplier());
						LOG.info("supplierPerson.getCommercialFolio()" + supplierPerson.getCommercialFolio());
						LOG.info("supplierPerson.getPower()" + supplierPerson.getPower());
						this.supplierPersonable.save(supplierPerson);
					}
				}
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(SUPPLIER_LAWYERS), databaseException);
			throw new BusinessException(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(SUPPLIER_LAWYERS),
					databaseException);
		}
	}

	private void updateSupplier(SupplierPerson person) throws DatabaseException {
		this.supplierPersonable.updateSupplier(person);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveSupplierPersonsListAngular(final Integer idSupplier, final List<SupplierPerson> supplierPersonsList)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveSupplierPersonsList LEGAL");
			for (SupplierPerson supplierPerson : supplierPersonsList) {
				if (supplierPerson.getIdSupplierPerson() != null && supplierPerson.getIdSupplierPerson() > 0) {
					this.updateSupplier(supplierPerson);
				} else {
					if (supplierPerson.getName() != null) {
						supplierPerson.setIdSupplier(idSupplier);
						supplierPerson.setActive(Boolean.TRUE);
						LOG.info("supplierPerson.getIdSupplier()" + supplierPerson.getIdSupplier());
						LOG.info("supplierPerson.getName()" + supplierPerson.getName().trim());
						LOG.info("supplierPerson.getSupplierPersonType()" + supplierPerson.getSupplierPersonType());
						LOG.info("supplierPerson.getIdSupplier()" + supplierPerson.getIdSupplier());
						LOG.info("supplierPerson.getCommercialFolio()" + supplierPerson.getCommercialFolio());
						LOG.info("supplierPerson.getPower()" + supplierPerson.getPower());
						this.supplierPersonable.save(supplierPerson);
					}
				}
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(SUPPLIER_LAWYERS), databaseException);
			throw new BusinessException(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(SUPPLIER_LAWYERS),
					databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveSupplierPersonsList(final Integer idSupplier, final List<String> supplierPersonsList,
			final SupplierPersonTypeEnum supplierPersontype) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveSupplierPersonsList SAVE");
			List<SupplierPerson> people = this.findWitnessesByIdSupplier(idSupplier);

			if (people == null || people.isEmpty()) {
				for (String supplierPersonName : supplierPersonsList) {
					final SupplierPerson supplierPerson = createWitness(idSupplier, supplierPersonName, supplierPersontype);
					this.supplierPersonable.save(supplierPerson);
				}
			} else {

				if (supplierPersonsList != null && !supplierPersonsList.isEmpty()) {
					supplierPersonsList.stream().forEach(sup -> {
						if(!getDuplicate(people, sup)){
							final SupplierPerson supplierPerson = createWitness(idSupplier, sup, supplierPersontype);
							try {
								this.supplierPersonable.save(supplierPerson);
							} catch (DatabaseException e) {
								LOG.error(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR), e);
							}
						}
					});
				}
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR), databaseException);
			throw new BusinessException(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR),
					databaseException);
		}
	}

	private boolean getDuplicate(List<SupplierPerson> people, String name) {
		List<SupplierPerson> list = people.stream().filter(p-> p.getName().trim().equals(name.trim())).collect(Collectors.toList());
		return list!= null && list.size() > 0 ? true : false;
	}

	private SupplierPerson createWitness(final Integer idSupplier, String name, SupplierPersonTypeEnum type) {
		SupplierPerson supplierPerson = new SupplierPerson();
		supplierPerson.setIdSupplier(idSupplier);
		supplierPerson.setName(name != null ? name.trim() : null);
		supplierPerson.setSupplierPersonType(type);
		supplierPerson.setActive(Boolean.TRUE);
		LOG.info("supplierPerson.getIdSupplier()" + supplierPerson.getIdSupplier());
		LOG.info("supplierPerson.getName()" + supplierPerson.getName());
		LOG.info("supplierPerson.getSupplierPersonType()" + supplierPerson.getSupplierPersonType());
		LOG.info("supplierPerson.getCommercialFolio()" + supplierPerson.getCommercialFolio());
		LOG.info("supplierPerson.getPower()" + supplierPerson.getPower());
		return supplierPerson;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public Integer saveSupplierPerson (final Integer idSupplier, final String name, final SupplierPersonTypeEnum type) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> saveSupplierPerson SAVE");
			List<SupplierPerson> people = this.supplierPersonable.findSupplierPersonByIdSupplierAndTypeAndName(idSupplier, type, name);

			if (people == null || people.isEmpty()) {
				SupplierPerson supplierPerson = createWitness(idSupplier, name, type);
				return this.supplierPersonable.save(supplierPerson);
			} else {
				if (type == SupplierPersonTypeEnum.WITNESS) {
					return people.get(0).getIdSupplierPerson();
				} else {
					SupplierPerson supplierPerson = createWitness(idSupplier, name, type);
					return this.supplierPersonable.save(supplierPerson);
				}
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR), databaseException);
			throw new BusinessException(MESSAGE_SAVING_SUPPLIER_PERSON_ERROR.concat(LOS_TESTIGOS_DEL_PROVEEDOR),
					databaseException);
		}
	}

	public void deleteSupplierPersonByIdSupplierAndType(final Integer idSupplier,
			final SupplierPersonTypeEnum supplierPersontype) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> deleteSupplierPersonByIdSupplierAndType");
			this.supplierPersonable.deleteSupplierPersonByIdSupplierAndType(idSupplier, supplierPersontype);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETING_SUPPLIER_PERSON_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETING_SUPPLIER_PERSON_ERROR, databaseException);
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

	public List<SupplierPerson> findSupplierPersonsByIdSupplierAndType(final Integer idSupplier,
			final SupplierPersonTypeEnum supplierPersontype) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findSupplierPersonsByIdSupplierAndType");
			return this.supplierPersonable.findSupplierPersonsByIdSupplierAndType(idSupplier, supplierPersontype);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_SUPPLIER_PERSON_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_SUPPLIER_PERSON_ERROR, databaseException);
		}
	}

	public Boolean isCompanyNameExist(final String companyName) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> isCompanyNameExist");
			return this.supplierable.isCompanyNameExist(companyName);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_IS_COMPANY_NAME_EXIST_ERROR, databaseException);
			throw new BusinessException(MESSAGE_IS_COMPANY_NAME_EXIST_ERROR, databaseException);
		}
	}

	public Boolean existRFC(final String rfc) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> existRFC");
			return this.supplierable.existRFC(rfc);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_IS_COMPANY_NAME_EXIST_ERROR, databaseException);
			throw new BusinessException(MESSAGE_IS_COMPANY_NAME_EXIST_ERROR, databaseException);
		}
	}

	public List<Supplier> findSupplierCatalogPaged(final Supplier supplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findSupplierCatalogPaged");
			return this.supplierable.findAllSupplierCatalogPaged(supplier, supplier.getNumberPage(), Integer.parseInt(
					this.configuration.findByName(ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_ALL_SUPPLIER_CATALOG_PAGED_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_ALL_SUPPLIER_CATALOG_PAGED_ERROR, databaseException);
		}
	}

	public Supplier returnTotalPagesShowSupplier(final Supplier supplier)
			throws NumberFormatException, BusinessException {
		try {
			LOG.info("SuppliersBusiness -> returnTotalPagesShowSupplier");
			final Long totalPages = this.supplierable.countTotalItemsToShowOfSupplier(supplier);
			// GAO final Supplier supplier = new Supplier();
			supplier.setNumberPage(this.configuration.totalPages(totalPages));
			supplier.setTotalRows(totalPages.intValue());
			return supplier;
		} catch (DatabaseException | NumberFormatException databaseException) {
			LOG.error(MESSAGE_FIND_TOTAL_PAGES_SUPPLIER_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_SUPPLIER_ERROR, databaseException);
		}
	}

	public Personality findPersonality(final Integer idSupplier) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findPersonality");
			return this.supplierable.findPersonality(idSupplier);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_PERSONALITY_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_PERSONALITY_ERROR, databaseException);
		}
	}

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<Supplier> supplierList = this.supplierable.findAll();
			return this.getExportSupplierMatrix(supplierList);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_EXPORTING_SUPPLIER_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_EXPORTING_SUPPLIER_ERROR, dataBaseException);
		}
	}

	private String[][] getExportSupplierMatrix(final List<Supplier> supplierListParameter) {
		final Integer columnsNumber = 15;
		final String[][] dataMatrix = new String[supplierListParameter.size() + 1][columnsNumber];
		dataMatrix[0][0] = "IdSupplier";
		dataMatrix[0][1] = "IdPersonality";
		dataMatrix[0][2] = "CommercialName";
		dataMatrix[0][NumbersEnum.THREE.getNumber()] = "CompanyName";
		dataMatrix[0][NumbersEnum.FOUR.getNumber()] = "Rfc";
		dataMatrix[0][NumbersEnum.FIVE.getNumber()] = "AccountNumber";
		dataMatrix[0][NumbersEnum.SIX.getNumber()] = "SupplierCompanyPurpose";
		dataMatrix[0][NumbersEnum.SEVEN.getNumber()] = "CompanyType";
		dataMatrix[0][NumbersEnum.EIGTH.getNumber()] = "NonFiscalAddress";
		dataMatrix[0][NumbersEnum.NINE.getNumber()] = "FiscalAddress";
		dataMatrix[0][NumbersEnum.TEN.getNumber()] = "Status";
		dataMatrix[0][NumbersEnum.ELEVEN.getNumber()] = "PersonalityName";
		dataMatrix[0][NumbersEnum.TWELVE.getNumber()] = "BankBranch";
		dataMatrix[0][NumbersEnum.THIRTEEN.getNumber()] = "SupplierPaymentFinInstitution";
		dataMatrix[0][NumbersEnum.FOURTEEN.getNumber()] = "Imss";
		Integer index = 1;

		for (Supplier supplier : supplierListParameter) {
			dataMatrix[index][0] = supplier.getIdSupplier().toString();
			dataMatrix[index][1] = supplier.getIdPersonality().toString();
			dataMatrix[index][2] = StringUtils.getObjectStringValue(supplier.getCommercialName());
			dataMatrix[index][NumbersEnum.THREE.getNumber()] = supplier.getCompanyName();
			dataMatrix[index][NumbersEnum.FOUR.getNumber()] = supplier.getRfc();
			dataMatrix[index][NumbersEnum.FIVE.getNumber()] = StringUtils
					.getObjectStringValue(supplier.getAccountNumber());
			dataMatrix[index][NumbersEnum.SIX.getNumber()] = StringUtils
					.getObjectStringValue(supplier.getSupplierCompanyPurpose());
			dataMatrix[index][NumbersEnum.SEVEN.getNumber()] = StringUtils
					.getObjectStringValue(supplier.getCompanyType());
			dataMatrix[index][NumbersEnum.EIGTH.getNumber()] = StringUtils
					.getObjectStringValue(supplier.getNonFiscalAddress());
			dataMatrix[index][NumbersEnum.NINE.getNumber()] = StringUtils
					.getObjectStringValue(supplier.getFiscalAddress());
			dataMatrix[index][NumbersEnum.TEN.getNumber()] = StringUtils.getObjectStringValue(supplier.getStatus());
			dataMatrix[index][NumbersEnum.ELEVEN.getNumber()] = StringUtils
					.getObjectStringValue(supplier.getPersonalityName());
			dataMatrix[index][NumbersEnum.TWELVE.getNumber()] = StringUtils
					.getObjectStringValue(supplier.getBankBranch());
			dataMatrix[index][NumbersEnum.THIRTEEN.getNumber()] = StringUtils
					.getObjectStringValue(supplier.getSupplierPaymentFinInstitution());
			dataMatrix[index][NumbersEnum.FOURTEEN.getNumber()] = StringUtils.getObjectStringValue(supplier.getImss());

			index++;
		}

		return dataMatrix;
	}


	public void changeSupplierPersonStatus(final SupplierPerson supplierPerson) throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> changeSupplierPersonStatus: + " + supplierPerson.getIdSupplierPerson() + " -> " + supplierPerson.isActive());
			if (supplierPerson.isActive()) {
				supplierPerson.setActive(false);
				this.supplierPersonable.changePersonSupplierStatus(supplierPerson);
			} else {
				supplierPerson.setActive(true);

				this.supplierPersonable.changePersonSupplierStatus(supplierPerson);
			}
		} catch (DatabaseException databaseException) {
			LOG.error("Error al cambiar estatus de Proveedor", databaseException);
			throw new BusinessException("Error al cambiar el estatus del Proveedor", databaseException);
		}
	}

	public List<SupplierPerson> findLegalRepresentativesByIdRequisition(final Integer idRequisition, SupplierPersonTypeEnum type)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findLegalRepresentativesByIdRequisition");
			return this.supplierPersonable.findLegalRepresentativesByIdRequisition(idRequisition, type);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}

	public List<SupplierPerson> findLegalRepresentativesTestigosByIdSupplierActive(final Integer idSupplier, SupplierPersonTypeEnum type, boolean active)
			throws BusinessException {
		try {
			LOG.info("SuppliersBusiness -> findLegalRepresentativesByIdSupplierActive");
			return this.supplierPersonable.findSupplierPersonsByIdSupplierAndTypeAtive(idSupplier, type, active);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_ERROR, databaseException);
		}
	}
}