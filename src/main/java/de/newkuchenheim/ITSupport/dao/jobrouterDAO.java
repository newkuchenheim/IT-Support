package de.newkuchenheim.ITSupport.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.ArchiveJobrouterConfig;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.DataJobrouterConfig;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.FileUploadJobrouterConfig;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 14.02.2024
 * 
 */
public abstract class jobrouterDAO {
	public final String _URL = "http://192.168.0.236/jobrouter/api/rest/v2";
	public final String _DOMAIN = "192.168.0.236";
	public final String _COOKIE_PATH = "/jobrouter/api/rest/v2";
	public final String _USER = "Form_remote";
	public final String _PWD = "B|ThAaP08/`+l|D@^Qo+";
	
	/**
	 * create a Request to login in Jobrouter
	 * @param method String - sessions or tokens
	 * @return HttpHeaders Header with token or session information
	 */
	protected HttpHeaders sendLoginRequest(String method) {
		List<String> result = new ArrayList<String>();
		String loginBody = String.format("{\"username\": \"%s\",\"password\": \"%s\"", _USER, _PWD);
		RestTemplate restTmpl = new RestTemplate();
		String fullURL = _URL;
		int resultIndex = 0;
		// build Request
		// add headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (method == "tokens") {
			fullURL += "/application/tokens";
			loginBody += ",\"lifetime\": 1800}";
		} else if (method == "sessions") {
			fullURL += "/application/sessions";
			loginBody += "}";
		}
		// response after push request
		HttpEntity<String> entity = new HttpEntity<String>(loginBody, headers);
		String response = restTmpl.postForObject(fullURL, entity, String.class);
		System.out.println(response);
		
		// get token or sessionID String
		try {
			// reset headers
			headers.clear();
			JSONObject responseObject = new JSONObject(response);
			JSONArray resultArray = responseObject.getJSONArray(method);
			if (method == "sessions") {
				headers.add(HttpHeaders.COOKIE, ((JSONObject) resultArray.get(0)).getString("sessionName")
						+ "=" + ((JSONObject) resultArray.get(0)).getString("sessionId"));
				return headers;
			} else {
				headers.add("X-Jobrouter-Authorization", "Bearer " + resultArray.getString(resultIndex));
				return headers;
			}
		} catch (JSONException err) {
			System.out.println(err);
			return null;
		}
	}
	
	public Object sendArchiveRequest(ArchiveJobrouterConfig config) {
		// first login
		HttpHeaders loginHeaders = sendLoginRequest("sessions");
		if (loginHeaders != null) {
			String fullURL = _URL + config.buildRequestRoute();
			Object requestData = config.buildRequestBody();
			RestTemplate restTmpl = new RestTemplate();
			String response = "";
			if (requestData != null) {
				loginHeaders.setContentType(config.getContentType());
				if (requestData instanceof String) {
					response = restTmpl.exchange(fullURL, config.getRequestMethod(), new HttpEntity<String>((String)requestData, loginHeaders), String.class).getBody();
				} else {
					response = restTmpl.exchange(fullURL, config.getRequestMethod(), 
							new HttpEntity<MultiValueMap<String, Object>>((MultiValueMap<String, Object>) requestData, loginHeaders), String.class).getBody();
				}
			} else {
				response = restTmpl.exchange(fullURL, config.getRequestMethod(), new HttpEntity<String>(loginHeaders), String.class).getBody();
			}
			System.out.println(response);
			
			try {
				JSONObject responseObject = new JSONObject(response);
				return responseObject;
			} catch (JSONException err) {
				System.out.println(err);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public JSONObject sendDataRequest(DataJobrouterConfig config) {
		// first login
		HttpHeaders loginHeaders = sendLoginRequest("sessions");
		if (loginHeaders != null) {
			String fullURL = _URL + config.buildRequestRoute();
			String requestData = config.buildRequestBody();
			if (requestData != null) {
				loginHeaders.setContentType(MediaType.APPLICATION_JSON);
			}
			RestTemplate restTmpl = new RestTemplate();
			String response = restTmpl.exchange(fullURL, config.getRequestMethod(), new HttpEntity<String>(requestData, loginHeaders), String.class).getBody();
			System.out.println(response);
			
			try {
				JSONObject responseObject = new JSONObject(response);
				return responseObject;
			} catch (JSONException err) {
				System.out.println(err);
				return null;
			}
		} else {
			return null;
		}
	}
}
