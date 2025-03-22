package mx.pagos.admc.contracts.structures.owners;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.security.structures.User;

public class ContractDetailOwners {

	private RequisitionOwners requisitionOwners;
	private User executiveUser;
	private List<CheckDocumentation> checkDocumentationList = new ArrayList<CheckDocumentation>();
	private List<Integer> idsCheckDocumentationList = new ArrayList<Integer>();
	private User decidedLawyerUser;

	public final RequisitionOwners getRequisitionOwners() {
		return this.requisitionOwners;
	}

	public final void setRequisitionOwners(final RequisitionOwners requisitionOwnersParameter) {
		this.requisitionOwners = requisitionOwnersParameter;
	}

	public final User getExecutiveUser() {
		return this.executiveUser;
	}

	public final void setExecutiveUser(final User executiveUserParameter) {
		this.executiveUser = executiveUserParameter;
	}

	public final List<CheckDocumentation> getCheckDocumentationList() {
		return this.checkDocumentationList;
	}

	public final void setCheckDocumentationList(final List<CheckDocumentation> checkDocumentationListParameter) {
		this.checkDocumentationList = checkDocumentationListParameter;
	}

	public final List<Integer> getIdsCheckDocumentationList() {
		return this.idsCheckDocumentationList;
	}

	public final void setIdsCheckDocumentationList(final List<Integer> idsCheckDocumentationListParameter) {
		this.idsCheckDocumentationList = idsCheckDocumentationListParameter;
	}

	public final User getDecidedLawyerUser() {
		return this.decidedLawyerUser;
	}

	public final void setDecidedLawyerUser(final User decidedLawyerUserParameter) {
		this.decidedLawyerUser = decidedLawyerUserParameter;
	}
}
