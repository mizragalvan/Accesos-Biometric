package mx.pagos.admc.contracts.business.digitalsignature.pscworldapi;

import mx.pagos.admc.contracts.structures.digitalsignature.DocumentPscWorld;

public abstract class BaseObjectPscWorldDAO<T> {

	protected PscWorldApiClient pscWorldApiClient;
	
	public BaseObjectPscWorldDAO(PscWorldApiClient pscWorldApiClient) {
		this.pscWorldApiClient = pscWorldApiClient;
	}

	public PscWorldApiClient getPscWorldApiClient() {
		return pscWorldApiClient;
	}

	public void setPscWorldApiClient(PscWorldApiClient pscWorldApiClient) {
		this.pscWorldApiClient = pscWorldApiClient;
	}
	
	public abstract String save(DocumentPscWorld newDocumentEviSign) throws Exception;
	
	public abstract String find(DocumentPscWorld documentPscWorld) throws Exception;
	
}
