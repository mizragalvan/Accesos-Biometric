package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.RecordStatusEnum;

public class FinancialEntity {
	private Integer idFinancialEntity;
	private String name;
	private RecordStatusEnum status;
	private String longName;
	private String domicile;
	private Integer idArticleOfLaw;
	private Integer idConfidentialityLaw;
	private List<Power> powersList = new ArrayList<>();
	private String rfc;
	private String telefono;
	private String correo;
	private String atencion;
	private String constitutive;
	private String entidadFinanceiraRegistro;
	private String entidadFinanceiraRegistroIngles;
	private String constitutiveEnglish;
	private String treatment;
	private String accountNumber;
	private String bankBranch;
	private String bankingInstitution;
	private Integer numberPage;
	private Integer totalRows;
	private Power power;
	private Integer idLegalRepresentative;
	private String legalRepresentativeName;
	private Integer idRequisition;
	private String phone;
	private String email;
	private String attention;
	private String constitutiveRegistred;
	private String constitutiveRegistredIngles;

	/**
	 * @return the constitutiveRegistred
	 */
	public String getConstitutiveRegistred() {
		return constitutiveRegistred;
	}

	/**
	 * @param constitutiveRegistred the constitutiveRegistred to set
	 */
	public void setConstitutiveRegistred(String constitutiveRegistred) {
		this.constitutiveRegistred = constitutiveRegistred;
	}

	public final Integer getIdFinancialEntity() {
		return this.idFinancialEntity;
	}

	public final void setIdFinancialEntity(final Integer idFinancialEntityParameter) {
		this.idFinancialEntity = idFinancialEntityParameter;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}

	public final RecordStatusEnum getStatus() {
		return this.status;
	}

	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}

	public final String getLongName() {
		return this.longName;
	}

	public final void setLongName(final String longNameParameter) {
		this.longName = longNameParameter;
	}

	public final String getDomicile() {
		return this.domicile;
	}

	public final void setDomicile(final String domicileParameter) {
		this.domicile = domicileParameter;
	}

	public final List<Power> getPowersList() {
		return this.powersList;
	}

	public final void setPowersList(final List<Power> powersListParameter) {
		this.powersList = powersListParameter;
	}

	public final Integer getIdArticleOfLaw() {
		return this.idArticleOfLaw;
	}

	public final void setIdArticleOfLaw(final Integer idArticleOfLawParameter) {
		this.idArticleOfLaw = idArticleOfLawParameter;
	}

	public final Integer getIdConfidentialityLaw() {
		return this.idConfidentialityLaw;
	}

	public final void setIdConfidentialityLaw(final Integer idConfidentialityLawParameter) {
		this.idConfidentialityLaw = idConfidentialityLawParameter;
	}

	public final String getRfc() {
		return this.rfc;
	}

	public final void setRfc(final String rfcParameter) {
		this.rfc = rfcParameter;
	}

	public final String getTelefono() {
		return this.telefono;
	}

	public final void setTelefono(final String telefonoParameter) {
		this.telefono = telefonoParameter;
	}

	public final String getCorreo() {
		return this.correo;
	}

	public final void setCorreo(final String correoParameter) {
		this.correo = correoParameter;
	}

	public final String getAtencion() {
		return this.atencion;
	}

	public final void setAtencion(final String atencionParameter) {
		this.atencion = atencionParameter;
	}

	public final String getConstitutive() {
		return this.constitutive;
	}

	public final void setConstitutive(final String constitutiveParameter) {
		this.constitutive = constitutiveParameter;
	}

	public final String getConstitutiveEnglish() {
		return this.constitutiveEnglish;
	}

	public final void setConstitutiveEnglish(final String constitutiveEnglishParameter) {
		this.constitutiveEnglish = constitutiveEnglishParameter;
	}

	public final String getTreatment() {
		return this.treatment;
	}

	public final void setTreatment(final String treatmentParameter) {
		this.treatment = treatmentParameter;
	}

	public final String getAccountNumber() {
		return this.accountNumber;
	}

	public final void setAccountNumber(final String accountNumberParameter) {
		this.accountNumber = accountNumberParameter;
	}

	public final String getBankBranch() {
		return this.bankBranch;
	}

	public final void setBankBranch(final String bankBranchParameter) {
		this.bankBranch = bankBranchParameter;
	}

	public final String getBankingInstitution() {
		return this.bankingInstitution;
	}

	public final void setBankingInstitution(final String bankingInstitutionParameter) {
		this.bankingInstitution = bankingInstitutionParameter;
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

	public final Power getPower() {
		return this.power;
	}

	public final void setPower(final Power powerParameter) {
		this.power = powerParameter;
	}

	public final Integer getIdLegalRepresentative() {
		return this.idLegalRepresentative;
	}

	public final void setIdLegalRepresentative(final Integer idLegalRepresentativeParameter) {
		this.idLegalRepresentative = idLegalRepresentativeParameter;
	}

	public final String getLegalRepresentativeName() {
		return this.legalRepresentativeName;
	}

	public final void setLegalRepresentativeName(final String legalRepresentativeNameParameter) {
		this.legalRepresentativeName = legalRepresentativeNameParameter;
	}

	public final Integer getIdRequisition() {
		return this.idRequisition;
	}

	public final void setIdRequisition(final Integer idRequisitionParameter) {
		this.idRequisition = idRequisitionParameter;
	}

	public final String getPhone() {
		return this.phone;
	}

	public final void setPhone(final String phoneParameter) {
		this.phone = phoneParameter;
	}

	public final String getEmail() {
		return this.email;
	}

	public final void setEmail(final String mailParameter) {
		this.email = mailParameter;
	}

	public final String getAttention() {
		return this.attention;
	}

	public final void setAttention(final String attentionParameter) {
		this.attention = attentionParameter;
	}

	/**
	 * @return the constitutiveRegistredIngles
	 */
	public String getConstitutiveRegistredIngles() {
		return constitutiveRegistredIngles;
	}

	/**
	 * @param constitutiveRegistredIngles the constitutiveRegistredIngles to set
	 */
	public void setConstitutiveRegistredIngles(String constitutiveRegistredIngles) {
		this.constitutiveRegistredIngles = constitutiveRegistredIngles;
	}

	public String getEntidadFinanceiraRegistro() {
		return entidadFinanceiraRegistro;
	}

	public void setEntidadFinanceiraRegistro(String entidadFinanceiraRegistro) {
		this.entidadFinanceiraRegistro = entidadFinanceiraRegistro;
	}

	public String getEntidadFinanceiraRegistroIngles() {
		return entidadFinanceiraRegistroIngles;
	}

	public void setEntidadFinanceiraRegistroIngles(String entidadFinanceiraRegistroIngles) {
		this.entidadFinanceiraRegistroIngles = entidadFinanceiraRegistroIngles;
	}
}

