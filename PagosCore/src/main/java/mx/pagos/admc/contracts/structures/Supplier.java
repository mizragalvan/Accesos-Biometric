package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.pagos.admc.enums.RecordStatusEnum;

/**
 * 
 * @author Mizraim
 * 
 *
 */
public class Supplier {
	private Integer idSupplier;
	private String commercialName;
	private String companyName;
	private String rfc;
	private String accountNumber;
	private String imss;
	private Integer idPersonality;
	private String personalityName;
	private String companyType;
	private String supplierCompanyPurpose;
	private String nonFiscalAddress;
	private String fiscalAddress;
	private String phoneNumber;
	private String email;
	private String atention;
	private String publicDeedPropertyNotary;
	private String publicDeedNotaryNumber;
	private String publicDeedNotaryState;
	private String commercialFolio;
	private String publicDeedNumber;
	private String publicDeedTitleDate;
	private String inscriptionFolioDate;
	private String inscriptionFolioState;
	private String bankBranch;
	private String supplierPaymentFinInstitution;
	private Integer numberPage;
	private Integer totalRows;
	private SupplierPerson supplierPerson;
	private String commercialOrPropertyRegister;
	private String street;
	private String exteriorNumber;
	private String interiorNumber;
	private String suburb;
	private String city;
	private String township;
	private String state;
	private String postalCode;

	private String streetMail;
	private String exteriorNumberMail;
	private String interiorNumberMail;
	private String suburbMail;
	private String cityMail;
	private String townshipMail;
	private String stateMail;
	private String postalCodeMail;
	private String nacionality;
	private String businessReferences;
	private RecordStatusEnum status;
	private List<RequiredDocument> requiredDocumentList = new ArrayList<>();
	private List<SupplierPerson> supplierPersonList = new ArrayList<>();
	private List<String> supplierWitnessList = new ArrayList<>();
	private Map<Integer, FileUploadInfo> supplierRequiredDocument = new HashMap<>();
	private List<SupplierPerson> legalRepresentativesList = new ArrayList<>();
	private List<SupplierPerson> witnessesList = new ArrayList<>();
	private List<FileUploadInfo> supplierRequiredDocumentList = new ArrayList<>();

	// MetroCarrier Fields
	private String position;

	public final Integer getIdSupplier() {
		return this.idSupplier;
	}

	public final void setIdSupplier(final Integer idSupplierParameter) {
		this.idSupplier = idSupplierParameter;
	}

	public final String getCommercialName() {
		return this.commercialName;
	}

	public final String getCommercialNameUpper() {
		if (this.commercialName != null)
			return this.commercialName.toUpperCase();
		return this.commercialName;
	}

	public final void setCommercialName(final String commercialNameParameter) {
		this.commercialName = commercialNameParameter;
	}

	public final String getCompanyName() {
		return this.companyName;
	}

	public final void setCompanyName(final String companyNameParameter) {
		this.companyName = companyNameParameter;
	}

	public final String getAccountNumber() {
		return this.accountNumber;
	}

	public final void setAccountNumber(final String accountNumberParameter) {
		this.accountNumber = accountNumberParameter;
	}

	public final String getRfc() {
		return this.rfc;
	}

	public final void setRfc(final String rfcParameter) {
		this.rfc = rfcParameter;
	}

	public final String getEmail() {
		return this.email;
	}

	public final void setEmail(final String emailParameter) {
		this.email = emailParameter;
	}

	public final String getPhoneNumber() {
		return this.phoneNumber;
	}

	public final void setPhoneNumber(final String phoneNumberParameter) {
		this.phoneNumber = phoneNumberParameter;
	}

	public final String getAtention() {
		return this.atention;
	}

	public final void setAtention(final String atentionParameter) {
		this.atention = atentionParameter;
	}

	public final Integer getIdPersonality() {
		return this.idPersonality;
	}

	public final void setIdPersonality(final Integer idPersonalityParameter) {
		this.idPersonality = idPersonalityParameter;
	}

	public final String getPersonalityName() {
		return this.personalityName;
	}

	public final void setPersonalityName(final String personalityNameParameter) {
		this.personalityName = personalityNameParameter;
	}

	public final RecordStatusEnum getStatus() {
		return this.status;
	}

	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}

	public final List<RequiredDocument> getRequiredDocumentList() {
		return this.requiredDocumentList;
	}

	public final void setRequiredDocumentList(final List<RequiredDocument> requiredDocumentListParameter) {
		this.requiredDocumentList = requiredDocumentListParameter;
	}

	public final List<SupplierPerson> getSupplierPersonList() {
		return this.supplierPersonList;
	}

	public final void setSupplierPersonList(final List<SupplierPerson> supplierPersonListParameter) {
		this.supplierPersonList = supplierPersonListParameter;
	}

	public final List<String> getSupplierWitnessList() {
		return this.supplierWitnessList;
	}

	public final void setSupplierWitnessList(final List<String> supplierWitnessListParameter) {
		this.supplierWitnessList = supplierWitnessListParameter;
	}

	public final Map<Integer, FileUploadInfo> getSupplierRequiredDocument() {
		return this.supplierRequiredDocument;
	}

	public final void setSupplierRequiredDocument(
			final Map<Integer, FileUploadInfo> supplierRequiredDocumentParameter) {
		this.supplierRequiredDocument = supplierRequiredDocumentParameter;
	}

	public final String getCompanyType() {
		return this.companyType;
	}

	public final void setCompanyType(final String companyTypeParameter) {
		this.companyType = companyTypeParameter;
	}

	public final String getSupplierCompanyPurpose() {
		return this.supplierCompanyPurpose;
	}

	public final void setSupplierCompanyPurpose(final String supplierCompanyPurposeParameter) {
		this.supplierCompanyPurpose = supplierCompanyPurposeParameter;
	}

	public final String getNonFiscalAddress() {
		return this.nonFiscalAddress;
	}

	public final void setNonFiscalAddress(final String nonFiscalAddressParameter) {
		this.nonFiscalAddress = nonFiscalAddressParameter;
	}

	public final String getFiscalAddress() {
		return this.fiscalAddress;
	}

	public final void setFiscalAddress(final String fiscalAddressParameter) {
		this.fiscalAddress = fiscalAddressParameter;
	}

	public final String getPublicDeedPropertyNotary() {
		return this.publicDeedPropertyNotary;
	}

	public final void setPublicDeedPropertyNotary(final String publicDeedPropertyNotaryParameter) {
		this.publicDeedPropertyNotary = publicDeedPropertyNotaryParameter;
	}

	public final String getPublicDeedNotaryNumber() {
		return this.publicDeedNotaryNumber;
	}

	public final void setPublicDeedNotaryNumber(final String publicDeedNotaryNumberParameter) {
		this.publicDeedNotaryNumber = publicDeedNotaryNumberParameter;
	}

	public final String getPublicDeedNotaryState() {
		return this.publicDeedNotaryState;
	}

	public final void setPublicDeedNotaryState(final String publicDeedNotaryStateParameter) {
		this.publicDeedNotaryState = publicDeedNotaryStateParameter;
	}

	public final String getCommercialFolio() {
		return this.commercialFolio;
	}

	public final void setCommercialFolio(final String commercialFolioParameter) {
		this.commercialFolio = commercialFolioParameter;
	}

	public final String getPublicDeedNumber() {
		return this.publicDeedNumber;
	}

	public final void setPublicDeedNumber(final String publicDeedNumberParameter) {
		this.publicDeedNumber = publicDeedNumberParameter;
	}

	public final String getPublicDeedTitleDate() {
		return this.publicDeedTitleDate;
	}

	public final void setPublicDeedTitleDate(final String publicDeedTitleDateParameter) {
		this.publicDeedTitleDate = publicDeedTitleDateParameter;
	}

	public final String getInscriptionFolioDate() {
		return this.inscriptionFolioDate;
	}

	public final void setInscriptionFolioDate(final String inscriptionFolioDateParameter) {
		this.inscriptionFolioDate = inscriptionFolioDateParameter;
	}

	public final String getBankBranch() {
		return this.bankBranch;
	}

	public final void setBankBranch(final String bankBranchParameter) {
		this.bankBranch = bankBranchParameter;
	}

	public final String getSupplierPaymentFinInstitution() {
		return this.supplierPaymentFinInstitution;
	}

	public final void setSupplierPaymentFinInstitution(final String supplierPaymentFinInstitutionParameter) {
		this.supplierPaymentFinInstitution = supplierPaymentFinInstitutionParameter;
	}

	public final Integer getNumberPage() {
		return this.numberPage;
	}

	public final void setNumberPage(final Integer numberPageParameter) {
		this.numberPage = numberPageParameter;
	}

	public final Integer getTotalRows() {
		return this.totalRows;
	}

	public final void setTotalRows(final Integer totalRowsParameter) {
		this.totalRows = totalRowsParameter;
	}

	public final SupplierPerson getSupplierPerson() {
		return this.supplierPerson;
	}

	public final void setSupplierPerson(final SupplierPerson supplierPersonParameter) {
		this.supplierPerson = supplierPersonParameter;
	}

	public final String getCommercialOrPropertyRegister() {
		return this.commercialOrPropertyRegister;
	}

	public final void setCommercialOrPropertyRegister(final String commercialOrPropertyRegisterParameter) {
		this.commercialOrPropertyRegister = commercialOrPropertyRegisterParameter;
	}

	public final String getStreet() {
		return this.street;
	}

	public final void setStreet(final String streetParameter) {
		this.street = streetParameter;
	}

	public final String getExteriorNumber() {
		return this.exteriorNumber;
	}

	public final void setExteriorNumber(final String exteriorNumberParameter) {
		this.exteriorNumber = exteriorNumberParameter;
	}

	public final String getInteriorNumber() {
		return this.interiorNumber;
	}

	public final void setInteriorNumber(final String interiorNumberParameter) {
		this.interiorNumber = interiorNumberParameter;
	}

	public final String getSuburb() {
		return this.suburb;
	}

	public final void setSuburb(final String suburbParameter) {
		this.suburb = suburbParameter;
	}

	public final String getCity() {
		return this.city;
	}

	public final void setCity(final String cityParameter) {
		this.city = cityParameter;
	}

	public final String getTownship() {
		return this.township;
	}

	public final void setTownship(final String townshipParameter) {
		this.township = townshipParameter;
	}

	public final String getState() {
		return this.state;
	}

	public final void setState(final String stateParameter) {
		this.state = stateParameter;
	}

	public final String getPostalCode() {
		return this.postalCode;
	}

	public final void setPostalCode(final String postalCodeParameter) {
		this.postalCode = postalCodeParameter;
	}

	public final String getStreetMail() {
		return this.streetMail;
	}

	public final void setStreetMail(final String streetMailParameter) {
		this.streetMail = streetMailParameter;
	}

	public final String getExteriorNumberMail() {
		return this.exteriorNumberMail;
	}

	public final void setExteriorNumberMail(final String exteriorNumberMailParameter) {
		this.exteriorNumberMail = exteriorNumberMailParameter;
	}

	public final String getInteriorNumberMail() {
		return this.interiorNumberMail;
	}

	public final void setInteriorNumberMail(final String interiorNumberMailParameter) {
		this.interiorNumberMail = interiorNumberMailParameter;
	}

	public final String getSuburbMail() {
		return this.suburbMail;
	}

	public final void setSuburbMail(final String suburbMailParameter) {
		this.suburbMail = suburbMailParameter;
	}

	public final String getCityMail() {
		return this.cityMail;
	}

	public final void setCityMail(final String cityMailParameter) {
		this.cityMail = cityMailParameter;
	}

	public final String getTownshipMail() {
		return this.townshipMail;
	}

	public final void setTownshipMail(final String townshipMailParameter) {
		this.townshipMail = townshipMailParameter;
	}

	public final String getStateMail() {
		return this.stateMail;
	}

	public final void setStateMail(final String stateMailParameter) {
		this.stateMail = stateMailParameter;
	}

	public final String getPostalCodeMail() {
		return this.postalCodeMail;
	}

	public final void setPostalCodeMail(final String postalCodeMailParameter) {
		this.postalCodeMail = postalCodeMailParameter;
	}

	public final String getImss() {
		return this.imss;
	}

	public final void setImss(final String imssParameter) {
		this.imss = imssParameter;
	}

	public final String getNacionality() {
		return this.nacionality;
	}

	public final void setNacionality(final String nacionalityParameter) {
		this.nacionality = nacionalityParameter;
	}

	public final String getInscriptionFolioState() {
		return this.inscriptionFolioState;
	}

	public final void setInscriptionFolioState(final String inscriptionFolioStateParameter) {
		this.inscriptionFolioState = inscriptionFolioStateParameter;
	}

	public final List<SupplierPerson> getLegalRepresentativesList() {
		return this.legalRepresentativesList;
	}

	public final void setLegalRepresentativesList(final List<SupplierPerson> legalRepresentativesListParameter) {
		this.legalRepresentativesList = legalRepresentativesListParameter;
	}

	public final List<SupplierPerson> getWitnessesList() {
		return this.witnessesList;
	}

	public final void setWitnessesList(final List<SupplierPerson> witnessesListParameter) {
		this.witnessesList = witnessesListParameter;
	}

	public final String getPosition() {
		return this.position;
	}

	public final void setPosition(final String positionParameter) {
		this.position = positionParameter;
	}

	public List<FileUploadInfo> getSupplierRequiredDocumentList() {
		return supplierRequiredDocumentList;
	}

	public void setSupplierRequiredDocumentList(List<FileUploadInfo> supplierRequiredDocumentList) {
		this.supplierRequiredDocumentList = supplierRequiredDocumentList;
		if (supplierRequiredDocumentList!=null&& !supplierRequiredDocumentList.isEmpty()) {
			this.supplierRequiredDocument=this.mapeaLista(supplierRequiredDocumentList,this.requiredDocumentList);
		}
	}

	private Map<Integer, FileUploadInfo> mapeaLista(List<FileUploadInfo> supplierRequiredDocumentList2,
			List<RequiredDocument> requiredDocumentList2) {
		Map<Integer, FileUploadInfo> lista = new HashMap<>();
		if (null != supplierRequiredDocumentList2 && !supplierRequiredDocumentList2.isEmpty()) {
			for (int i = 0; i < requiredDocumentList2.size(); i++) {
				lista.put(requiredDocumentList2.get(i).getIdRequiredDocument(), supplierRequiredDocumentList2.get(i));
			}
		}
		return lista;
	}

	public String getBusinessReferences() {
		return businessReferences;
	}

	public void setBusinessReferences(String businessReferences) {
		this.businessReferences = businessReferences;
	}


}
