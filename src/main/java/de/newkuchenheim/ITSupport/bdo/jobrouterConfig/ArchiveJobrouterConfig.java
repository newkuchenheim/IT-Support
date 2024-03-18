package de.newkuchenheim.ITSupport.bdo.jobrouterConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public enum ArchiveJobrouterConfig {
	
	GET_ARCHIVES(HttpMethod.GET, "archives", new HashMap<String, Object>()),
	POST_ARCHIVES(HttpMethod.POST, "archives_post", new HashMap<String, Object>(), MediaType.APPLICATION_JSON),
	GET_DOCUMENTS(HttpMethod.GET, "documents", new HashMap<String, Object>()),
	GET_DOCUMENTREVISIONS_FILE(HttpMethod.GET, "documentrevisions_file", new HashMap<String, Object>()),
	GET_DOCUMENTREVISIONS_FILES(HttpMethod.GET, "documentrevisions_files", new HashMap<String, Object>()),
	GET_DOCUMENTREVISIONS_CLIPPEDFILES(HttpMethod.GET, "documentrevisions_clippedfiles", new HashMap<String, Object>()),
	GET_DOCUMENTREVISIONS_CLIPPEDFILESID(HttpMethod.GET, "documentrevisions_clippedfilesid", new HashMap<String, Object>()),
	GET_DOCUMENTREVISIONS_PDF(HttpMethod.GET, "documentrevisions_pdf", new HashMap<String, Object>()),
	POST_DOCUMENTREVISIONS_FILE(HttpMethod.POST, "documentrevisions_file_post", new HashMap<String, Object>(), MediaType.MULTIPART_FORM_DATA),
	POST_DOCUMENTREVISIONS_CLIPPEDFILES(HttpMethod.POST, "documentrevisions_clippedfiles_post", new HashMap<String, Object>(), MediaType.MULTIPART_FORM_DATA),
	POST_DOCUMENTREVISIONS_FILE_REVID(HttpMethod.POST, "documentrevisions_file_revid_post", new HashMap<String, Object>(), MediaType.MULTIPART_FORM_DATA),
	DELETE_DOCUMENTREVISION(HttpMethod.DELETE, "documentrevisions_delete", new HashMap<String, Object>()),
	GET_INDICIES(HttpMethod.GET, "indicies", new HashMap<String, Object>()),
	GET_INDICIES_LISTOPTIONS(HttpMethod.GET, "indicies_listoptions", new HashMap<String, Object>()),
	POST_INDICIES(HttpMethod.POST, "indicies_post", new HashMap<String, Object>(), MediaType.APPLICATION_JSON),
	POST_INDICIES_FIELD(HttpMethod.POST, "indicies_field_post", new HashMap<String, Object>(), MediaType.APPLICATION_JSON),
	GET_INDEXDIALOGS(HttpMethod.GET, "indexdialogs", new HashMap<String, Object>()),
	GET_INDEXDIALOGS_NAME(HttpMethod.GET, "indexdialogs_name", new HashMap<String, Object>()),
	POST_VIEWS(HttpMethod.POST, "views", new HashMap<String, Object>(), MediaType.APPLICATION_JSON),
	POST_VIEWINDICIES(HttpMethod.POST, "viewindicies", new HashMap<String, Object>(), MediaType.APPLICATION_JSON);
	
	private HttpMethod requestMethod;
	private MediaType contentType;
	private String method_name;
	private Map<String, Object> params = new HashMap<String, Object>();
	private JSONObject post_params = new JSONObject();
	private String requestRoute = "/application/jobarchive/archives/:archive";
	private boolean binaryResponse = false;
	
	ArchiveJobrouterConfig(HttpMethod requestMethod, String method_name, Map<String, Object> map, MediaType contentType) {
		this.requestMethod = requestMethod;
		this.method_name = method_name;
		this.contentType = contentType;
		this.params = map;
		configParamsMap();
	}
	
	ArchiveJobrouterConfig(HttpMethod requestMethod, String method_name, Map<String, Object> map) {
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
		case "archives":
			params.put(":archive", "");
			requestRoute += "/index";
			break;
		case "archives_post":
			params.put(":archive", "");
			requestRoute += "/fulltext";
			JSONObject revData = new JSONObject();
			revData.put("revisionId", -1);
			revData.put("files", new JSONArray());
			post_params.put("revision", revData);
			break;
		case "documents":
			params.put(":archive", "");
			requestRoute += "/documents";
			break;
		case "documentrevisions_file":
			params.put(":archive", "");
			params.put(":revisionId", -1);
			requestRoute += "/documents/:revisionId/file";
			binaryResponse = true;
			break;
		case "documentrevisions_files":
			params.put(":archive", "");
			params.put(":revisionId", -1);
			requestRoute += "/documents/:revisionId/files";
			binaryResponse = true;
			break;
		case "documentrevisions_clippedfiles":
			params.put(":archive", "");
			params.put(":revisionId", -1);
			requestRoute += "/documents/:revisionId/clippedfiles";
			binaryResponse = true;
			break;
		case "documentrevisions_clippedfilesid":
			params.put(":archive", "");
			params.put(":revisionId", -1);
			params.put(":clippedFileId", -1);
			requestRoute += "/documents/:revisionId/clippedfiles/:clippedFileId";
			binaryResponse = true;
			break;
		case "documentrevisions_pdf":
			params.put(":archive", "");
			params.put(":revisionId", -1);
			requestRoute += "/documents/:revisionId/mergedpdf";
			binaryResponse = true;
			break;
		case "documentrevisions_file_post":
			params.put(":archive", "");
			requestRoute += "/documents";
			post_params.put("files", new JSONArray());
			break;
		case "documentrevisions_clippedfiles_post":
			params.put(":archive", "");
			params.put(":revisionId", -1);
			requestRoute += "/documents/:revisionId/clippedfiles";
			post_params.put("files", new JSONArray());
			break;
		case "documentrevisions_file_revid_post":
			params.put(":archive", "");
			params.put(":revisionId", -1);
			requestRoute += "/documents/:revisionId";
			post_params.put("files", new JSONArray());
			break;
		case "documentrevisions_delete":
			params.put(":archive", "");
			params.put(":revisionId", -1);
			requestRoute += "/documents/:revisionId";
			break;
		case "indicies":
			params.put(":archive", "");
			params.put(":indexfield", "");
			requestRoute += "/indicies/:indexfield";
			break;
		case "indicies_listoptions":
			params.put(":archive", "");
			params.put(":indexfield", "");
			requestRoute += "/indicies/:indexfield/listoptions";
			break;
		case "indicies_field_post":
			params.put(":archive", "");
			params.put(":indexfield", "");
			post_params.put("fields", new JSONObject());
			requestRoute += "/indicies/:indexfield/listoptions";
			break;
		case "indicies_post":
			params.put(":archive", "");
			post_params.put("fields", new JSONObject());
			requestRoute += "/indicies/listoptions";
			break;
		case "indexdialogs":
			params.put(":archive", "");
			requestRoute += "/indexdialogs";
			break;
		case "indexdialogs_name":
			params.put(":archive", "");
			params.put(":indexDialogName", "");
			requestRoute += "/indexdialogs/:indexDialogName";
			break;
		case "views":
			params.put(":archive", "");
			params.put(":viewId", 0);
			post_params.put("revisiondIds", new JSONArray());
			requestRoute += "/views/:viewId/mergedpdf";
			break;
		case "viewindices":
			params.put(":archive", "");
			params.put(":viewId", 0);
			post_params.put("fields", new JSONObject());
			requestRoute += "/views/:viewId/indicies/listoptions";
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
	
	/**
	 * @return the binaryResponse
	 */
	public boolean isBinaryResponse() {
		return binaryResponse;
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
			} else if (contentType == MediaType.MULTIPART_FORM_DATA) {
				if (!post_params.getJSONArray("files").isEmpty()) {
					MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
					// TODO correct multipart Body with files
					return null;
				}
				return null;
			}
		}
		return null;
	}
}
