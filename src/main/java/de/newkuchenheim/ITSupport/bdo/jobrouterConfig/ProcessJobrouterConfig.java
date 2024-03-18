package de.newkuchenheim.ITSupport.bdo.jobrouterConfig;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public enum ProcessJobrouterConfig {
	GET_ATTACHMENTS(HttpMethod.GET, "attachments", new HashMap<String, Object>());
	
	private HttpMethod requestMethod;
	private String method_name;
	private MediaType contentType;
	private Map<String, Object> params = new HashMap<String, Object>();
	private JSONObject post_params = new JSONObject();
	private String requestRoute = "/application";
	
	ProcessJobrouterConfig(HttpMethod requestMethod, String method_name, Map<String, Object> map, MediaType contentType) {
		this.requestMethod = requestMethod;
		this.method_name = method_name;
		this.params = map;
		configParamsMap();
	}
	
	ProcessJobrouterConfig(HttpMethod requestMethod, String method_name, Map<String, Object> map) {
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
		case "attachments":
			params.put(":workflowId", "");
			params.put(":id", "");
			requestRoute += "/attachments/:workflowId/:id";
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
		}
		return null;
	}
	
	public Object buildRequestBody() {
		if (post_params != null && !post_params.isEmpty() && contentType != null) {
			if (contentType == MediaType.APPLICATION_JSON) {
				return post_params.toString();
			}
		}
		return null;
	}
}
