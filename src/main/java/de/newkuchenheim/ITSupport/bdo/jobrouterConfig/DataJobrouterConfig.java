package de.newkuchenheim.ITSupport.bdo.jobrouterConfig;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public enum DataJobrouterConfig {
	GET_DATASETS(HttpMethod.GET, "datasets", new HashMap<String, Object>()),
	GET_DATASETS_JRID(HttpMethod.GET, "datasets_jrid", new HashMap<String, Object>()),
	POST_DATASETS(HttpMethod.POST, "datasets_post", new HashMap<String, Object>(), MediaType.APPLICATION_JSON),
	POST_LISTOPTIONS(HttpMethod.POST, "listoptions_post", new HashMap<String, Object>(), MediaType.APPLICATION_JSON),
	DELETE_DATASETS(HttpMethod.DELETE, "datasets_delete", new HashMap<String, Object>(), MediaType.APPLICATION_JSON);
	
	private HttpMethod requestMethod;
	private String method_name;
	private MediaType contentType;
	private Map<String, Object> params = new HashMap<String, Object>();
	private JSONObject post_params = new JSONObject();
	private final String mainRoute = "/application/jobdata/tables/:guid";
	private String requestRoute;
	
	DataJobrouterConfig(HttpMethod requestMethod, String method_name, Map<String, Object> map, MediaType contentType) {
		this.requestMethod = requestMethod;
		this.method_name = method_name;
		this.params = map;
		this.contentType = contentType;
		this.requestRoute = this.mainRoute;
		configParamsMap();
	}
	
	DataJobrouterConfig(HttpMethod requestMethod, String method_name, Map<String, Object> map) {
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
		case "datasets":
			params.put(":guid", "");
			requestRoute += "/datasets";
			break;
		case "datasets_jrid":
			params.put(":guid", "");
			params.put(":jrid", -1);
			requestRoute += "/datasets/:jrid";
			break;
		case "datasets_post":
			params.put(":guid", "");
			requestRoute += "/datasets";
			post_params.put("dataset", new JSONObject());
			break;
		case "listoptions_post":
			params.put(":guid", "");
			post_params.put("dataset", new JSONObject());
			requestRoute += "/listoptions";
			break;
		case "datasets_delete":
			params.put(":guid", "");
			requestRoute += "/datasets";
			post_params.put("datasets", new JSONArray());
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
	
	public void resetRequestRoute() {
		// reset route for next call
		if (!requestRoute.contains(":")) {
			this.requestRoute = this.mainRoute;
			configParamsMap();
		}
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
		}
		return null;
	}
	
	public String buildRequestBody() {
		if (post_params != null && !post_params.isEmpty() && contentType != null) {
			if (contentType == MediaType.APPLICATION_JSON) {
				return post_params.toString();
			}
		}
		return null;
	}
}
