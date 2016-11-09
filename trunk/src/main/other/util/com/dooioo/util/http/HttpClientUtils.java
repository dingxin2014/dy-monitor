package com.dooioo.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gelif on 2015/3/25.
 */
public class HttpClientUtils {
	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	public static CommonModel<String> httpPost(String url, HttpEntity entity) {
		CommonModel<String> result;
		CloseableHttpResponse httpResponse = null;
		CloseableHttpClient httpClient = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setPath(url);

			URI uri = uriBuilder.build();
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(entity);
			// httpPost.setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			httpClient = HttpClients.custom().build();
			httpResponse = httpClient.execute(httpPost);
			if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream inputStream = httpEntity.getContent();
				String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8.displayName());
				result = CommonModel.buildSuccess(content);
			}
			else {
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream inputStream = httpEntity.getContent();
				String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8.displayName());
				result = CommonModel.buildFail("", content);
			}
			return result;
		}
		catch (IOException e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail("", e.getMessage());
		}
		catch (URISyntaxException e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail("", e.getMessage());
		}
		finally {
			try {
				if (httpResponse != null) {
					httpResponse.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			}
			catch (IOException e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

	public static CommonModel<String> httpGet(String url, Map<String, Object> params) {
		return httpGet(url, params, null);
	}

	public static CommonModel<String> httpGet(String url, Map<String, Object> params, HttpServletRequest request) {
		CommonModel<String> result;
		try {
			HttpResponse httpResponse = getByObjectParams(url, params, request);
			if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream inputStream = httpEntity.getContent();
				String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8.displayName());
				result = CommonModel.buildSuccess(content);
			}
			else {
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream inputStream = httpEntity.getContent();
				String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8.displayName());
				result = CommonModel.buildFail("", content);
			}
			return result;
		}
		catch (IOException e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail("", e.getMessage());
		}
		catch (URISyntaxException e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail("", e.getMessage());
		}
		catch (NoSuchAlgorithmException e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail("", e.getMessage());
		}
		catch (KeyStoreException e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail("", e.getMessage());
		}
		catch (KeyManagementException e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail("", e.getMessage());
		}
		catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail("", e.getMessage());
		}
	}

	public static HttpResponse getByURI(URI uri, HttpServletRequest request, int timeout) throws URISyntaxException,
			IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		HttpGet httpGet = new HttpGet(uri);
		RequestConfig requestConfig =
				RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();// 设置请求和传输超时时间
		httpGet.setConfig(requestConfig);
		if (request != null) {
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				httpGet.addHeader(headerName, request.getHeader(headerName));
			}
		}
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		}).build();
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		return httpResponse;
	}

	public static HttpResponse getByObjectParams(String url, Map<String, Object> params, HttpServletRequest request)
			throws URISyntaxException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		return getByObjectParams(url, params, request, 3000);
	}

	public static HttpResponse getByObjectParams(String url, Map<String, Object> params, HttpServletRequest request,
			int timeout) throws URISyntaxException, IOException, KeyStoreException, NoSuchAlgorithmException,
			KeyManagementException {
		URIBuilder uriBuilder = new URIBuilder().setPath(url);
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, Object> param : params.entrySet()) {
				uriBuilder.addParameter(param.getKey(),
						param.getValue() == null ? null : String.valueOf((param.getValue())));
			}
		}
		URI uri = uriBuilder.build();
		return getByURI(uri, request, timeout);
	}

	public static HttpResponse get(String url, Map<String, String[]> params, HttpServletRequest request)
			throws URISyntaxException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		return get(url, params, request, 3000);
	}

	public static HttpResponse get(String url, Map<String, String[]> params, HttpServletRequest request, int timeout)
			throws URISyntaxException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		URIBuilder uriBuilder = new URIBuilder().setPath(url);
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String[]> param : params.entrySet()) {
				uriBuilder.addParameter(param.getKey(), StringUtils.join(param.getValue(), ","));
			}
		}
		URI uri = uriBuilder.build();
		return getByURI(uri, request, timeout);
	}

	public static HttpResponse post(String url, HttpEntity entity, HttpServletRequest request)
			throws URISyntaxException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		URIBuilder uriBuilder = new URIBuilder().setPath(url);

		URI uri = uriBuilder.build();
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setEntity(entity);
		// httpPost.setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		}).build();
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
		return httpResponse;

	}

	// public static void main(String[] args) {
	// String url = "http://fs.dooioo.org/fetch/vp/caigou/other/20150611/537f9dd1-f6cc-4842-a046-e6de372e706e.xls";
	// CommonModel<byte[]> file = download(url);
	// logger.debug(file.status);
	// }

	public static CommonModel<byte[]> download(String url) {
		CloseableHttpResponse httpResponse = null;
		CloseableHttpClient httpClient = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setPath(url);
			URI uri = uriBuilder.build();
			HttpGet httpGet = new HttpGet(uri);

			httpClient = HttpClients.custom().build();
			httpResponse = httpClient.execute(httpGet);
			if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream inputStream = httpEntity.getContent();
				byte[] bytes = IOUtils.toByteArray(inputStream);
				return CommonModel.buildSuccess(bytes);
			}
			else {
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream inputStream = httpEntity.getContent();
				String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8.displayName());
				return CommonModel.buildFail(null, content);
			}

		}
		catch (IOException e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail(null, e.getMessage());
		}
		catch (URISyntaxException e) {
			logger.warn(e.getMessage(), e);
			return CommonModel.buildFail(null, e.getMessage());
		}
		finally {
			try {
				if (httpResponse != null) {
					httpResponse.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			}
			catch (IOException e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}
}
