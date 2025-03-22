package mx.pagos.admc.contracts.business.digitalsignature.evisignapi;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import mx.pagos.admc.contracts.business.digitalsignature.ApiRestUtils;
import mx.pagos.admc.contracts.structures.digitalsignature.DocumentEviSign;
import mx.pagos.admc.contracts.structures.digitalsignature.EviSignQuery;
import mx.pagos.admc.contracts.structures.digitalsignature.ResultRequestEviSign;

public class EviSign extends BaseObjectEviSignDAO<DocumentEviSign> {

	public static final String ACTION_SUBMIT = "/Submit";
	public static final String ACTION_QUERY = "/Query";
	public static final String ACTION_CANCEL = "/Cancel";
	public static final String DOCUMENT_CANONICAL_NAME = DocumentEviSign.class.getCanonicalName();
	public static final String RESULT_REQUEST_CANONICAL_NAME = ResultRequestEviSign.class.getCanonicalName();

	public EviSign(final EviSignApiClient apiClient) {
		super(apiClient);
	}

	@Override
	public DocumentEviSign save(DocumentEviSign documentEviSign) throws Exception {
		try {

			final String json = ApiRestUtils.convertObjectToJson(documentEviSign);

			StringEntity httpContent = new StringEntity(json, "UTF-8");
			HttpEntity entityResponse = eviSignApiClient.post(ACTION_SUBMIT, httpContent);

			final String response = ApiRestUtils.entityToString(entityResponse);

			return (DocumentEviSign) ApiRestUtils.convertJsonToObject(response, DOCUMENT_CANONICAL_NAME);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	@Override
	public ResultRequestEviSign find(EviSignQuery eviSignQuery) throws Exception {
		try {

			String query = ApiRestUtils.createRequestMethodGetEviSign(eviSignQuery);

			final HttpEntity entityResponse = eviSignApiClient.get(ACTION_QUERY + query);
			final String response = ApiRestUtils.entityToString(entityResponse);
			return (ResultRequestEviSign) ApiRestUtils.convertJsonToObject(response, RESULT_REQUEST_CANONICAL_NAME);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	
	@Override
	public DocumentEviSign cancel(DocumentEviSign documentEviSign) throws Exception {
		try {

			final String json = ApiRestUtils.convertObjectToJson(documentEviSign);

			StringEntity httpContent = new StringEntity(json, "UTF-8");
			HttpEntity entityResponse = eviSignApiClient.post(ACTION_CANCEL, httpContent);

			final String response = ApiRestUtils.entityToString(entityResponse);

			return (DocumentEviSign) ApiRestUtils.convertJsonToObject(response, DOCUMENT_CANONICAL_NAME);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

}
