package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;

public class MenuGroup {
	
	private Integer idGroup;
	private String groupName;
	private RecordStatusEnum status;
	
	public final Integer getIdGroup() {
		return this.idGroup;
	}
	
	public final void setIdGroup(final Integer idGroupParameter) {
		this.idGroup = idGroupParameter;
	}
	
	public final String getGroupName() {
		return this.groupName;
	}
	
	public final void setGroupName(final String groupNameParameter) {
		this.groupName = groupNameParameter;
	}
	
	public final RecordStatusEnum getStatus() {
		return this.status;
	}
	
	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}
	
	

}
