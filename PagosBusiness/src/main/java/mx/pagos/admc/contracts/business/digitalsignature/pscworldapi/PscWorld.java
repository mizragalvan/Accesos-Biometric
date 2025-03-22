package mx.pagos.admc.contracts.business.digitalsignature.pscworldapi;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import mx.pagos.admc.contracts.business.digitalsignature.ApiRestUtils;
import mx.pagos.admc.contracts.structures.digitalsignature.DocumentPscWorld;

public class PscWorld extends BaseObjectPscWorldDAO<DocumentPscWorld>{

	public static final String ACTION_SUBMIT = "/Genera";
	public static final String ACTION_GET = "/Recupera";
	public static final String DOCUMENT_CANONICAL_NAME = DocumentPscWorld.class.getCanonicalName();
	
	public PscWorld(final PscWorldApiClient apiClient) {
		super(apiClient);
	}
	
	
	@Override
	public String save(DocumentPscWorld documentPscWorld) throws Exception {
		try {
			
			final String json = ApiRestUtils.convertObjectToJson(documentPscWorld);
			
			StringEntity httpContent = new StringEntity(json, "UTF-8");
			HttpEntity entityResponse = pscWorldApiClient.post(ACTION_SUBMIT, httpContent);
			
			return ApiRestUtils.entityToString(entityResponse).replace("\"", "");
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	
	
	@Override
	public String find(DocumentPscWorld documentPscWorld) throws Exception {
		try {

			String query = ApiRestUtils.createRequestMethodGetPscWorld(documentPscWorld);

			final HttpEntity entityResponse = pscWorldApiClient.get(ACTION_GET + query);
			final String response = ApiRestUtils.entityToString(entityResponse);
			
			DocumentPscWorld result = (DocumentPscWorld) 
					ApiRestUtils.convertJsonToObject(response, DOCUMENT_CANONICAL_NAME);
			
			return result.getTimeStamp().replace("\"", "");

		} catch (Exception exception) {
			throw exception;
		}
	}
	
	
}
