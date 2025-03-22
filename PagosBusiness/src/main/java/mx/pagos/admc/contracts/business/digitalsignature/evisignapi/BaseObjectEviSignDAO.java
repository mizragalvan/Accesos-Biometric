package mx.pagos.admc.contracts.business.digitalsignature.evisignapi;

import mx.pagos.admc.contracts.structures.digitalsignature.DocumentEviSign;
import mx.pagos.admc.contracts.structures.digitalsignature.EviSignQuery;
import mx.pagos.admc.contracts.structures.digitalsignature.ResultRequestEviSign;

public abstract class BaseObjectEviSignDAO<T> {

	protected EviSignApiClient eviSignApiClient;

	protected BaseObjectEviSignDAO(final EviSignApiClient eviSignApiClient) {
		this.eviSignApiClient = eviSignApiClient;
	}

	public EviSignApiClient getEviSignApiClient() {
		return eviSignApiClient;
	}

	public void setEviSignApiClient(final EviSignApiClient eviSignApiClient) {
		this.eviSignApiClient = eviSignApiClient;
	}

	public abstract DocumentEviSign save(DocumentEviSign newDocumentEviSign) throws Exception;
	
	public abstract ResultRequestEviSign find(EviSignQuery eviSignQuery) throws Exception;
	
	public abstract DocumentEviSign cancel(DocumentEviSign eviSignQuery) throws Exception;

}