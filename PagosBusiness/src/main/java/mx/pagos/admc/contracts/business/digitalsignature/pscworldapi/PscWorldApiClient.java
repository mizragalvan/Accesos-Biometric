package mx.pagos.admc.contracts.business.digitalsignature.pscworldapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;

import mx.pagos.admc.contracts.business.digitalsignature.ApiRestUtils;
import mx.pagos.admc.util.shared.Constants;

public class PscWorldApiClient {

	private String bearerToken;
	private String url;
	
	public PscWorldApiClient(String bearerToken) {
		super();
		this.bearerToken = bearerToken;
	}

	public void setBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(final String url) throws Exception {
		if (!ApiRestUtils.isStringEmpty(url)) {
			this.url = url.replaceAll("/$", "");
		} else {
			throw new Exception("Invalid URL format");
		}
	}
	
	public HttpEntity get(final String action) throws Exception {
		return sendRequest(HttpMethod.GET, action, MultipartEntityBuilder.create().build());
	}

	public HttpEntity post(final String action, final HttpEntity content) throws Exception {
		return sendRequest(HttpMethod.POST, action, content);
	}

	public HttpEntity delete(final String action) throws Exception {
		return sendRequest(HttpMethod.DELETE, action, MultipartEntityBuilder.create().build());
	}

	public HttpEntity put(final String action, final HttpEntity content) throws Exception {
		return sendRequest(HttpMethod.PUT, action, content);
	}

	public HttpEntity patch(final String action, final HttpEntity content) throws Exception {
		return sendRequest(HttpMethod.PATCH, action, content);
	}

	private HttpEntity sendRequest(final HttpMethod httpMethod, final String action, final HttpEntity body)
			throws Exception {

		final String requestUrl = url + action;
		HttpRequestBase request = null;
		HttpResponse response = null;
		HttpEntity entityResponse = null;

		switch (httpMethod) {
		case POST:
			request = new HttpPost(requestUrl);
			((HttpPost) request).setEntity(body);
			break;
		case GET:
			request = new HttpGet(requestUrl);
			break;
		case DELETE:
			request = new HttpDelete(requestUrl);
			break;
		case PUT:
			request = new HttpPut(requestUrl);
			((HttpPut) request).setEntity(body);
			break;
		case PATCH:
			request = new HttpPatch(requestUrl);
			((HttpPatch) request).setEntity(body);
			break;
		default:
			throw new Exception("Unsupported HttpMethod: " + httpMethod);
		}

		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			body.writeTo(out);
		} catch (Exception e) {
			throw new Exception("Error on body request", e);
		}

		setAuthentication(request);

		try {
			response = HttpClientBuilder.create().build().execute(request);
		} catch (final IOException e) {
			throw new Exception("Error sending Http request", e);
		}

		final int responseStatusCode = response.getStatusLine().getStatusCode();

		entityResponse = response.getEntity();
		if (entityResponse == null) {
			try {
				entityResponse = new StringEntity("");
			} catch (UnsupportedEncodingException e) {
				throw new Exception("Error creating an empty Entity", e);
			}
		}

		if (isSuccessfulHttpCode(responseStatusCode)) {
			return entityResponse;
		}

		String responseString = EntityUtils.toString(entityResponse);
		throw new Exception(responseString);
		
	}

	private void setAuthentication(final HttpRequestBase request) throws Exception {

		request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, this.bearerToken);
		request.addHeader(Constants.HEADER_CONTET, Constants.CONTENT_TYPE);
		request.setHeader(Constants.CONFIRM_ACCEPT, Constants.CONTENT_TYPE);
	}

	private boolean isSuccessfulHttpCode(final int httpStatusCode) {
		return httpStatusCode >= Constants.TWO_HUNDRED && httpStatusCode < Constants.THREE_HUNDRED;
	}
	
}
