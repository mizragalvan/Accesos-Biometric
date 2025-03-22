package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.security.structures.User;

public class ContractDetail {

	private Requisition requisition;
	private User userApplicant;
	private List<SupplierPerson> supplierLegalRepresentativeList = new ArrayList<SupplierPerson>();
	private List<SupplierPerson> supplierWitnessList = new ArrayList<SupplierPerson>();
	private List<String> authorizationLevelAuthorizationDgasList = new ArrayList<String>();
	private List<String> authorizationLevelAprovalAreasList = new ArrayList<String>();
	private List<User> authorizationLevelAddedUsersToVoBoList = new ArrayList<User>();
	private List<FinantialEntityWitness> instrumentDataFinantialEntityWitnessList = 
			new ArrayList<FinantialEntityWitness>();
	private List<Scaling> scalingMatrixSupplierList = new ArrayList<Scaling>();
	private List<Scaling> scalingMatrixFinancialEntity = new ArrayList<Scaling>();
	private Integer assignedLawyerIdLawyer;
	private String assignedLawyerName;
	private List<ApprovalArea> approvalAreasVoBoList = new ArrayList<ApprovalArea>();
	private List<Area> aviableAreasList = new ArrayList<Area>();
	private String evaluatorName;
	private List<Obligation> obligationsList = new ArrayList<Obligation>();
	private List<Personality> personalityList = new ArrayList<Personality>();
	private List<String> instrumentDataFinancialEntityList = new ArrayList<String>();
	
	public final Requisition getRequisition() {
		return this.requisition;
	}

	public final void setRequisition(final Requisition requisitionParameter) {
		this.requisition = requisitionParameter;
	}

	public final List<SupplierPerson> getSupplierLegalRepresentativeList() {
        return this.supplierLegalRepresentativeList;
    }

    public final void setSupplierLegalRepresentativeList(
            final List<SupplierPerson> supplierLegalRepresentativeListParameter) {
        this.supplierLegalRepresentativeList = supplierLegalRepresentativeListParameter;
    }

	public final List<SupplierPerson> getSupplierWitnessList() {
		return this.supplierWitnessList;
	}

	public final void setSupplierWitnessList(final List<SupplierPerson> supplierWitnessListParameter) {
		this.supplierWitnessList = supplierWitnessListParameter;
	}

	public final List<String> getAuthorizationLevelAuthorizationDgasList() {
		return this.authorizationLevelAuthorizationDgasList;
	}

	public final void setAuthorizationLevelAuthorizationDgasList(
			final List<String> authorizationLevelAuthorizationDgasListParameter) {
		this.authorizationLevelAuthorizationDgasList = authorizationLevelAuthorizationDgasListParameter;
	}

	public final List<String> getAuthorizationLevelAprovalAreasList() {
		return this.authorizationLevelAprovalAreasList;
	}

	public final void setAuthorizationLevelAprovalAreasList(
			final List<String> authorizationLevelAprovalAreasListParameter) {
		this.authorizationLevelAprovalAreasList = authorizationLevelAprovalAreasListParameter;
	}

	public final List<User> getAuthorizationLevelAddedUsersToVoBoList() {
		return this.authorizationLevelAddedUsersToVoBoList;
	}

	public final void setAuthorizationLevelAddedUsersToVoBoList(
			final List<User> authorizationLevelAddedUsersToVoBoListParameter) {
		this.authorizationLevelAddedUsersToVoBoList = authorizationLevelAddedUsersToVoBoListParameter;
	}


	public final List<FinantialEntityWitness> getInstrumentDataFinantialEntityWitnessList() {
		return this.instrumentDataFinantialEntityWitnessList;
	}

	public final void setInstrumentDataFinantialEntityWitnessList(
			final List<FinantialEntityWitness> instrumentDataFinantialEntityWitnessListParameter) {
		this.instrumentDataFinantialEntityWitnessList = instrumentDataFinantialEntityWitnessListParameter;
	}

	public final List<Scaling> getScalingMatrixSupplierList() {
		return this.scalingMatrixSupplierList;
	}

	public final void setScalingMatrixSupplierList(final List<Scaling> scalingMatrixSupplierListParameter) {
		this.scalingMatrixSupplierList = scalingMatrixSupplierListParameter;
	}

	public final Integer getAssignedLawyerIdLawyer() {
		return this.assignedLawyerIdLawyer;
	}

	public final void setAssignedLawyerIdLawyer(final Integer assignedLawyerIdLawyerParameter) {
		this.assignedLawyerIdLawyer = assignedLawyerIdLawyerParameter;
	}

	public final String getAssignedLawyerName() {
		return this.assignedLawyerName;
	}

	public final void setAssignedLawyerName(final String assignedLawyerNameParameter) {
		this.assignedLawyerName = assignedLawyerNameParameter;
	}

	public final List<ApprovalArea> getApprovalAreasVoBoList() {
		return this.approvalAreasVoBoList;
	}

	public final void setApprovalAreasVoBoList(final List<ApprovalArea> approvalAreasVoBoListParameter) {
		this.approvalAreasVoBoList = approvalAreasVoBoListParameter;
	}

	public final List<Area> getAviableAreasList() {
		return this.aviableAreasList;
	}

	public final void setAviableAreasList(final List<Area> aviableAreasListParameter) {
		this.aviableAreasList = aviableAreasListParameter;
	}

	public final String getEvaluatorName() {
		return this.evaluatorName;
	}

	public final void setEvaluatorName(final String evaluatorNameParameter) {
		this.evaluatorName = evaluatorNameParameter;
	}

	public final List<Obligation> getObligationsList() {
		return this.obligationsList;
	}

	public final void setObligationsList(final List<Obligation> obligationsListParameter) {
		this.obligationsList = obligationsListParameter;
	}

	public final User getUserApplicant() {
		return this.userApplicant;
	}

	public final void setUserApplicant(final User userApplicantParameter) {
		this.userApplicant = userApplicantParameter;
	}

	public final List<Personality> getPersonalityList() {
		return this.personalityList;
	}

	public final void setPersonalityList(final List<Personality> personalityListParameter) {
		this.personalityList = personalityListParameter;
	}

	public final List<String> getInstrumentDataFinancialEntityList() {
		return this.instrumentDataFinancialEntityList;
	}

	public final void setInstrumentDataFinancialEntityList(
			final List<String> instrumentDataFinancialEntityListParameter) {
		this.instrumentDataFinancialEntityList = instrumentDataFinancialEntityListParameter;
	}

	public final List<Scaling> getScalingMatrixFinancialEntity() {
		return this.scalingMatrixFinancialEntity;
	}

	public final void setScalingMatrixFinancialEntity(final List<Scaling> scalingMatrixFinancialEntityParameter) {
		this.scalingMatrixFinancialEntity = scalingMatrixFinancialEntityParameter;
	}
}
