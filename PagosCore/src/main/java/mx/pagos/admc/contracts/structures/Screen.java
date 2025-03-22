package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.RecordStatusEnum;

public class Screen {
	private String name;
	private String factoryName;
	private String factoryNameTray;
	private FlowPurchasingEnum flowStatus;
	private RecordStatusEnum status;
	
	public final String getName() {
	    return this.name;
	}
	
	public final void setName(final String nameParameter) {
	    this.name = nameParameter;
	}	
	
	public final String getFactoryName() {
		return this.factoryName;
	}
	
	public final void setFactoryName(final String factoryNameParameter) {
		this.factoryName = factoryNameParameter;
	}
	
	public final String getFactoryNameTray() {
	    return this.factoryNameTray;
	}
	
	public final void setFactoryNameTray(final String factoryNameTrayParameter) {
	    this.factoryNameTray = factoryNameTrayParameter;
	}
	
	public final FlowPurchasingEnum getFlowStatus() {
		return this.flowStatus;
	}
	
	public final void setFlowStatus(final FlowPurchasingEnum flowStatusParameter) {
		this.flowStatus = flowStatusParameter;
	}
	
	public final RecordStatusEnum getStatus() {
		return this.status;
	}
	
	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}
}
