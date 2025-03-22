package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.FlowTypeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;

public class Flow {
	private Integer idFlow;
	private String name;
	private FlowTypeEnum flowType;
 	private RecordStatusEnum status;
 	
 	public Flow() {

	}
 	
	public Flow(final Integer idFlowParameter) {
		this.idFlow = idFlowParameter;
	}

	public final Integer getIdFlow() {
		return this.idFlow;
	}
	
	public final void setIdFlow(final Integer idFlowParameter) {
		this.idFlow = idFlowParameter;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}
	
	public final FlowTypeEnum getFlowType() {
		return this.flowType;
	}
	
	public final void setFlowType(final FlowTypeEnum flowTypeParameter) {
		this.flowType = flowTypeParameter;
	}
	
	public final RecordStatusEnum getStatus() {
		return this.status;
	}
	
	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	} 	
}
