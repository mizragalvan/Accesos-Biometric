package mx.pagos.admc.service.generals;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.SuppliersBusiness;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.contracts.structures.Supplier;
import mx.pagos.admc.contracts.structures.SupplierPerson;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.SupplierPersonTypeEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

/**
 * @author Mizraim
 */

@Controller
public class SupplierService {

	private static final Logger LOG = Logger.getLogger(SupplierService.class);

	@Autowired
	private SuppliersBusiness supplierableBusiness;

	@Autowired
	private UserSession session;

	@Autowired
	private BinnacleBusiness binnacleBusiness;

	@RequestMapping (value = UrlConstants.SAVE_OR_UPDATE_SUPPLIER, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdate(@RequestBody final Supplier supplier, final HttpServletResponse  response) {
		final Integer idSupplier = null;
		try {
			return this.supplierableBusiness.saveOrUpdate(supplier);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return idSupplier;
	}

	@RequestMapping (value = UrlConstants.SUPPLIER_CHANGE_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final void changeStatus(@RequestBody final Supplier supplier, final HttpServletResponse  response) {
		LOG.info("SupplierService :: changeStatus");
		try {
			this.supplierableBusiness.changeSupplierStatus(supplier.getIdSupplier(), supplier.getStatus());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping (value = UrlConstants.SUPPLIER_FIND_ALL, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Supplier> findAll(final HttpServletResponse  response) {
		final ConsultaList<Supplier> supplierList = new ConsultaList<>();
		LOG.info("SupplierService :: findAll");
		try {
			supplierList.setList(this.supplierableBusiness.findAll());
			return supplierList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return supplierList;
	}

	@RequestMapping (value = UrlConstants.SUPPLIER_FIND_BY_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Supplier> findByStatus(@RequestBody final String status, 
			final HttpServletResponse  response) {
		final ConsultaList<Supplier> supplierList = new ConsultaList<>();
		LOG.info("SupplierService :: findByStatus");
		try {
			supplierList.setList(this.supplierableBusiness.findByRecordStatus(RecordStatusEnum.valueOf(status)));
			return supplierList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return supplierList;
	}

	@RequestMapping (value = UrlConstants.UPDATE_SUPPLIER_DRAFT_FIELDS, method = RequestMethod.POST)
	@ResponseBody
	public final void updateDraftSupplierFields(@RequestBody final Supplier supplier, 
			final HttpServletResponse  response) {
		
		LOG.info("\n========================================================\n ACTULIZACIÓN/GUARDADO DE LA BANDEJA SOLICITUD DE CONTRATO \n"
				+ "Paso:: Declaraciones del proveedor/cliente/intercompany \n"
				+ "Metodo:: saveOrUpdateRequisitionPart5()\n"
				+ "Proveedor :: "+supplier.getSupplierPerson().getName());
		try {
			this.supplierableBusiness.updateDraftSupplierFields(supplier);
			LOG.info("GUARDADO/ACTULIZACIÓN DE LA BANDEJA SOLICITUD DE CONTRATO  EXITOSO\n"
					+ "Paso:: Declaraciones del proveedor/cliente/intercompany \n"
					+ "Proveedor :: "+supplier.getSupplierPerson().getName());
			
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	@RequestMapping (value = UrlConstants.SUPPLIER_UPDATE_REP_LEGAL, method = RequestMethod.POST)
	@ResponseBody
	public final void updateDraftSupplierRepLegal(@RequestBody final Supplier supplier, 
			final HttpServletResponse  response) {
		
		LOG.info("\n========================================================\n ACTULIZACIÓN/GUARDADO DE LA BANDEJA SOLICITUD DE CONTRATO \n"
				+ "Paso:: Declaraciones del representante legal del proveedor/cliente/intercompany \n"
				+ "Proveedor :: "+supplier.getPersonalityName());
		
		try {
			this.supplierableBusiness.saveSupplierPersonsList(supplier.getIdSupplier(), supplier.getSupplierPersonList());
			LOG.info("GUARDADO/ACTULIZACIÓN EXITOSO - Declaraciones del representante legal del proveedor/cliente/intercompany");
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	@RequestMapping (value = UrlConstants.SUPPLIER_UPDATE_REP_LEGAL_ANGULAR, method = RequestMethod.POST)
	@ResponseBody
	public final void updateDraftSupplierRepLegalAngular(@RequestBody final Supplier supplier, 
			final HttpServletResponse  response) {
		LOG.info("\n========================================================\n ACTULIZACIÓN/GUARDADO DE LA BANDEJA SOLICITUD DE CONTRATO \n"
				+ "Paso:: Declaraciones del representante legal del proveedor/cliente/intercompany \n"
				+ "Proveedor :: "+supplier.getPersonalityName());		
		try {
			this.supplierableBusiness.saveSupplierPersonsListAngular(supplier.getIdSupplier(), supplier.getSupplierPersonList());
			LOG.info("\n=====================================\nACTULIZACIÓN/GUARDADO DE LA BANDEJA SOLICITUD DE CONTRATO EXITOSO!!\n"
					+ "Paso:: Declaraciones del representante legal del proveedor/cliente/intercompany \n");
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	@RequestMapping (value = UrlConstants.SEARCH_PROVIDERS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Supplier> searchProviders(@RequestBody final ConsultaList<Supplier> parameters, 
			final HttpServletRequest request, final HttpServletResponse  response) {
		LOG.info("================= searchProviders() :: BUSQUEDA DE PROVEEDORES");
		try {
			final ConsultaList<Supplier> listReturn = new ConsultaList<Supplier>();
			listReturn.setList(this.supplierableBusiness.findByNameAndRfc(parameters.getParam1(),
					parameters.getParam2()));
			return listReturn;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info(" BusinessException :: SupplierService :: searchProviders");
			LOG.info(businessException.getMessage(), businessException);
			return null;
		}
	}

	@RequestMapping (value = UrlConstants.SAVE_SUPPLIER_PERSONS, method = RequestMethod.POST)
	@ResponseBody
	public final void saveSupplierPersons(@RequestBody final ConsultaList<String> parameters, 
			final HttpServletRequest request, final HttpServletResponse  response) {
		LOG.info("SupplierService :: saveSupplierPersons");
		try {
			this.supplierableBusiness.saveSupplierPersonsList(Integer.valueOf(parameters.getParam1()),
					parameters.getList(), SupplierPersonTypeEnum.valueOf(parameters.getParam2()));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info(" BusinessException :: SupplierService :: saveSupplierPersons");
			LOG.info(businessException.getMessage(), businessException);
		}
	}

	@RequestMapping (value = UrlConstants.SEARCH_SUPPLIER_PERSONS_BY_IDSUPPLIER_AND_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<SupplierPerson> findSupplierPersonsByIdSupplierAndType(
			@RequestBody final ConsultaList<SupplierPerson> parameters,
			final HttpServletRequest request, final HttpServletResponse  response) {
		LOG.info("SupplierService :: findSupplierPersonsByIdSupplierAndType");
		try {
			final ConsultaList<SupplierPerson> result = new ConsultaList<SupplierPerson>();
			result.setList(this.supplierableBusiness.findSupplierPersonsByIdSupplierAndType(
					Integer.valueOf(parameters.getParam1()), SupplierPersonTypeEnum.valueOf(parameters.getParam2())));
			return result;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info(" BusinessException :: SupplierService :: findSupplierPersonsByIdSupplierAndType");
			LOG.info(businessException.getMessage(), businessException);
		}
		return new ConsultaList<SupplierPerson>();
	}

	@RequestMapping (value = UrlConstants.FIND_SUPPLIER_LAWYERS_BY_SUPPLIER, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<SupplierPerson> findLegalRepresentativesByIdSupplier(
			@RequestBody final Integer idSupplier, final HttpServletResponse  response) {
		final ConsultaList<SupplierPerson> legalList = new ConsultaList<>();
		LOG.info("SupplierService :: findLegalRepresentativesByIdSupplier");
		try {
			legalList.setList(this.supplierableBusiness.findLegalRepresentativesByIdSupplier(idSupplier));
			return legalList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return legalList;
	}

	@RequestMapping (value = UrlConstants.FIND_SUPPLIER_WITNESSES_BY_SUPPLIER, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<SupplierPerson> findWitnessesByIdSupplier(
			@RequestBody final Integer idSupplier, final HttpServletResponse  response) {
		final ConsultaList<SupplierPerson> witnessesList = new ConsultaList<>();
		LOG.info("SupplierService :: findWitnessesByIdSupplier");
		try {
			witnessesList.setList(this.supplierableBusiness.findWitnessesByIdSupplier(idSupplier));
			return witnessesList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return witnessesList;
	}

	@RequestMapping (value = UrlConstants.SEARCH_PROVIDERS_BY_RFC, method = RequestMethod.POST)
	@ResponseBody
	public final Supplier searchProvidersByRfc(@RequestBody final Supplier supplierRfc, 
			final HttpServletRequest request, final HttpServletResponse  response) {
		LOG.info("SupplierService :: searchProvidersByRfc");
		try {
			return this.supplierableBusiness.findByRfc(supplierRfc.getRfc(), supplierRfc.getIdPersonality());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info(" BusinessException :: SupplierService :: searchProvidersByRfc");
			LOG.info(businessException.getMessage(), businessException);
		} catch (PatternSyntaxException regex) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, regex.getMessage());
			LOG.info(" PatternSyntaxException :: RFC INVALIDO");
			Supplier sup =new Supplier();
			sup.setIdPersonality(-1);
			return sup;
	    }
		return new Supplier();
	}

	@RequestMapping (value = UrlConstants.FIND_REQUIRED_DOCUMENT, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<RequiredDocument> findRequiredDocumentsByIdSupplier(
			@RequestBody final ConsultaList<RequiredDocument> requiredParemeter, 
			final HttpServletRequest request, final HttpServletResponse  response) {
		try {
			LOG.info("SupplierService :: findRequiredDocumentsByIdSupplier");
			final ConsultaList<RequiredDocument> listResponse = new ConsultaList<RequiredDocument>();
			LOG.info(">>> PARAMETRO 4: " + requiredParemeter.getParam4() );

			listResponse.setList(this.supplierableBusiness.findRequiredDocumentsByIdSupplier(requiredParemeter.getParam4()));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info(" SupplierService :: findRequiredDocumentsByIdSupplier");
			LOG.info(businessException.getMessage(), businessException);
		}
		return new ConsultaList<RequiredDocument>();
	}

	@RequestMapping (value = UrlConstants.SEARCH_PROVIDERS_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final Supplier searchProvidersById(@RequestBody final Supplier supplierBean, 
			final HttpServletRequest request, final HttpServletResponse  response) {
		LOG.info("SupplierService :: searchProvidersById");
		try {
			return this.supplierableBusiness.findById(supplierBean.getIdSupplier());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info(" BusinessException :: SupplierService :: searchProvidersById");
			LOG.info(businessException.getMessage(), businessException);
		}
		return null;
	}

	@RequestMapping (value = UrlConstants.IS_COMPANY_NAME_EXIST, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean isCompanyNameExist(@RequestBody final String companyName, 
			final HttpServletResponse  response) {
		LOG.info("SupplierService :: isCompanyNameExist");
		try {
			return this.supplierableBusiness.isCompanyNameExist(companyName);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return false;
	}

	@RequestMapping (value = UrlConstants.SUPPLIER_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Supplier> findAllSupplierCatalogPaged(@RequestBody final Supplier supplier, 
			final HttpServletResponse response) {
		LOG.info("SupplierService :: findAllSupplierCatalogPaged");
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Consulta de todos los proveedores", this.session, LogCategoryEnum.QUERY));
			final ConsultaList<Supplier> supplierList = new ConsultaList<>();
			supplierList.setList(this.supplierableBusiness.findSupplierCatalogPaged(supplier));
			return supplierList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return new ConsultaList<Supplier>();
	}

	@RequestMapping (value = UrlConstants.SUPPLIER_FIND_TOTAL_PAGES, method = RequestMethod.POST)
	@ResponseBody
	public final Supplier returnTotalRowsOfSupplier(
			@RequestBody final Supplier supplier, final HttpServletResponse response) {
		LOG.info("SupplierService :: returnTotalRowsOfSupplier");
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Consulta del número de paginas de catálogo de proveedores", 
					this.session, LogCategoryEnum.QUERY));
			return this.supplierableBusiness.returnTotalPagesShowSupplier(supplier);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return new Supplier();
	}

	@RequestMapping (value = UrlConstants.EXIST_RFC, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean existRFC(@RequestBody final String rfc, 
			final HttpServletResponse  response) {
		LOG.info("SupplierService :: existRFC");
		try {
			return this.supplierableBusiness.existRFC(rfc);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return false;
	}

	@RequestMapping (value = UrlConstants.SUPPLIER_PERSON_CHANGE_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final void changeStatusPerson(@RequestBody final SupplierPerson supplierPerson, final HttpServletResponse  response) {
		LOG.info("SupplierService :: changeStatus");
		try {
			LOG.info("DATOS: " + supplierPerson.getIdSupplierPerson() + " --> " + supplierPerson.isActive());

			this.supplierableBusiness.changeSupplierPersonStatus(supplierPerson);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping (value = UrlConstants.FIND_SUPPLIER_PERSON_BY_REQUISITION, method = RequestMethod.POST)
	@ResponseBody
	public final List<SupplierPerson> findLegalRepresentativesByIdRequisition(@RequestBody final Integer idRequisition, final HttpServletResponse  response) {
		final ConsultaList<SupplierPerson> legalList = new ConsultaList<>();
		LOG.info("SupplierService :: findLegalRepresentativesByIdRequisition");
		try {
			return this.supplierableBusiness.findLegalRepresentativesByIdRequisition(idRequisition, SupplierPersonTypeEnum.LEGALREPRESENTATIVE);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return null;
		}
	}

	@RequestMapping (value = UrlConstants.FIND_SUPPLIER_LAWYERS_BY_SUPPLIER_ACTIVE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<SupplierPerson> findLegalRepresentativesByIdSupplierActive(
			@RequestBody final Integer idSupplier, final HttpServletResponse  response) {
		final ConsultaList<SupplierPerson> legalList = new ConsultaList<>();
		LOG.info("SupplierService :: findLegalRepresentativesByIdSupplier");
		try {
			legalList.setList(this.supplierableBusiness.findLegalRepresentativesTestigosByIdSupplierActive(idSupplier, 
					SupplierPersonTypeEnum.LEGALREPRESENTATIVE, Boolean.TRUE));
			return legalList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return legalList;
	}

	@RequestMapping (value = UrlConstants.FIND_SUPPLIER_WITNESSES_BY_SUPPLIER_ACTIVE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<SupplierPerson> findWitnessesByIdSupplierActive(
			@RequestBody final Integer idSupplier, final HttpServletResponse  response) {
		final ConsultaList<SupplierPerson> witnessesList = new ConsultaList<>();
		LOG.info("SupplierService :: findWitnessesByIdSupplier");
		try {
			witnessesList.setList(this.supplierableBusiness.findLegalRepresentativesTestigosByIdSupplierActive(idSupplier, 
					SupplierPersonTypeEnum.WITNESS, Boolean.TRUE));
			return witnessesList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return witnessesList;
	}
}