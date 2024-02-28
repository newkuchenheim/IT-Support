package de.newkuchenheim.ITSupport.bdo.jobrouterConfig;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
	
	FileUploadJobrouterConfig(HttpMethod requestMethod, String method_name, Map<String, Object> map, MediaType contentType) {
		this.requestMethod = requestMethod;
		this.method_name = method_name;
		this.params = map;
		this.contentType = contentType;
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
	
	public boolean setPostParams(JSONObject postParams) {
		if (postParams != null && !postParams.isEmpty()) {
			this.post_params = postParams;
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
	
	public Object buildRequestBody() {
		//if (post_params != null && !post_params.isEmpty() && contentType != null) {
			if (contentType == MediaType.APPLICATION_JSON) {
				post_params.toString();
			} else if (contentType == MediaType.MULTIPART_FORM_DATA) {
				// TODO set correct files body
				MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<String, Object>();
				//requestBody.add("files", (byte[])((JSONObject)post_params.getJSONArray("files").get(0)).get("bytes"));
				requestBody.add("files", getTestFile());
				return requestBody;
			}
		//}
		return null;
	}
	
	private FileSystemResource getTestFile() {
		return new FileSystemResource("C:\\Users\\Hansen\\Documents\\Dummy.txt");
	}
}
