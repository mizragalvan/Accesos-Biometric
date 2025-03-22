package mx.pagos.admc.contracts.business.digitalsignature.evisignapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

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

public class EviSignApiClient {

	private String username;
	private String password;
	private String url;

	public EviSignApiClient(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setPassword(final String password) {
		this.password = password;
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

		try {
			throw new Exception("Status code error: " + responseStatusCode + " | Response: "
					+ EntityUtils.toString(entityResponse));
		} catch (final Exception e) {
			throw new Exception("Status code error: " + responseStatusCode, e);
		}
	}

	private void setAuthentication(final HttpRequestBase request) throws Exception {

		String authString = this.username + Constants.TWO_POINTS + this.password;
		String authHeaderValue = Constants.BASIC_PREFIX + Base64.getEncoder().encodeToString(authString.getBytes());

		request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, authHeaderValue);
		request.addHeader(Constants.HEADER_CONTET, Constants.CONTENT_TYPE);
		request.setHeader(Constants.CONFIRM_ACCEPT, Constants.CONTENT_TYPE);
	}

	private boolean isSuccessfulHttpCode(final int httpStatusCode) {
		return httpStatusCode >= Constants.TWO_HUNDRED && httpStatusCode < Constants.THREE_HUNDRED;
	}

}
