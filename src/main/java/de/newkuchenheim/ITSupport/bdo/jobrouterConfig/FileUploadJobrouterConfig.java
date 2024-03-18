package de.newkuchenheim.ITSupport.bdo.jobrouterConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public enum FileUploadJobrouterConfig {
	GET_FILE(HttpMethod.GET, "getfile", new HashMap<String, Object>()),
	POST_FILE(HttpMethod.POST, "sendfile", new HashMap<String, Object>(), MediaType.MULTIPART_FORM_DATA),
	DELETE_FILE(HttpMethod.DELETE, "deletefile", new HashMap<String, Object>());
	
	private HttpMethod requestMethod;
	private String method_name;
	private MediaType contentType;
	private Map<String, Object> params = new HashMap<String, Object>();
	private JSONObject post_params = new JSONObject();
	private String requestRoute = "/application/fileuploads";
	private String dirSeperator = "/";
	
	FileUploadJobrouterConfig(HttpMethod requestMethod, String method_name, Map<String, Object> map, MediaType contentType) {
		this.requestMethod = requestMethod;
		this.method_name = method_name;
		this.params = map;
		this.contentType = contentType;
		if(System.getProperty("os.name").equals("Linux")) {
			dirSeperator = "/";
		} else {
			dirSeperator = "\\";
		}
		configParamsMap();
	}
	
	FileUploadJobrouterConfig(HttpMethod requestMethod, String method_name, Map<String, Object> map) {
		this(requestMethod, method_name, map, null);
	}
	
	/**
	 * Get request method e.g GET
	 * @return requestMethod
	 */
	public HttpMethod getRequestMethod() {
		return requestMethod;
	}
	
	/**
	 * Configure a default params-map Value of with the defined Keys can be computed
	 * later
	 * 
	 * @param
	 * @return
	 */
	private void configParamsMap() {
		params.clear();
		post_params.clear();
		switch (this.method_name) {
		case "getfile":
			params.put(":fileId", "");
			requestRoute += "/:fileId";
			break;
		case "sendfile":
			post_params.put("files", new JSONArray());
			break;
		case "deletefile":
			params.put(":fileId", "");
			requestRoute += "/:fileId";
			break;
		}
	}
	
	public Map<String, Object> getParams() {
		return params;
	}
	
	public JSONObject getPostParams() {
		return post_params;
	}
	
	public boolean setParameterValue(String param, Object value) {
		if (param != null && !param.isBlank()) {
			params.put(param, value);
			return true;
		} 
		return false;
	}
	
	public MediaType getContentType() {
		return contentType;
	}
	
	public boolean setPostParamsValue(String postParam, Object value) {
		if (postParam != null && !postParam.isBlank()) {
			this.post_params.put(postParam, value);
			return true;
		} 
		return false;
	}
	
	public String buildRequestRoute() {
		if (!params.isEmpty()) {
			if (requestRoute.contains(":")) {
				// replace each param placeholder with the value
				params.entrySet().forEach(e -> {
					requestRoute = requestRoute.replaceAll(e.getKey(), e.getValue().toString());
				});
			}
			return requestRoute;
		} else if (method_name.equals("sendfile")) {
			return requestRoute;
		}
		return null;
	}
	
	public Object buildRequestBody() throws IOException {
		if (post_params != null && !post_params.isEmpty() && contentType != null) {
			if (contentType == MediaType.APPLICATION_JSON) {
				return post_params.toString();
			} else if (contentType == MediaType.MULTIPART_FORM_DATA) {
				// TODO set correct files body
				if (!post_params.getJSONArray("files").isEmpty()) {
				 return null;
				}
				return null;
			}
		}
		return null;
	}
}
