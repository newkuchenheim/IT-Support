/**
 * 
 */
package de.newkuchenheim.ITSupport.dao.kanboard;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;

import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.bdo.itsupport.Ticket;
import de.newkuchenheim.ITSupport.bdo.itsupport.awNote;
import de.newkuchenheim.ITSupport.bdo.kanboardConfig.TaskFileKanboardConfigutration;
import de.newkuchenheim.ITSupport.bdo.kanboardConfig.TaskKanboardConfiguration;
import de.newkuchenheim.ITSupport.bdo.response.ErrorKanboard;
import de.newkuchenheim.ITSupport.bdo.response.ResponseKanboard;
import de.newkuchenheim.ITSupport.bdo.response.ResultKanboard;
import org.json.*;

/**
 * @author Minh Tam Truong
 * 
 * @createOn 28.09.2022
 * 
 */
public abstract class kanboardDAO {

	public final String _URL = "http://192.168.0.216/kanboard/jsonrpc.php";
	public final String _USER = "jsonrpc";
	public final String _API_TOKEN = "201bc0696f069fce5734bd955951011904ba7576a4014d888ab5c42acd22";
	public final String _TAB_UL_1 = "- ", _NEWLINE = " \\r\\n", _BOLD_OPEN = " **", _BOLD_CLOSE = "** ",
			_HEADING_1_OPEN = "# ", _HEADING_1_CLOSE = " # \\r\\n", _HEADING_2_OPEN = "## ",
			_HEADING_2_CLOSE = " ## \\r\\n", _HEADING_3_OPEN = "### ", _HEADING_3_CLOSE = " ### \\r\\n";
	
	
	/**
	 * create a Request to send a File to Kanboard
	 * @param config TaskFileConfiguration
	 * @return object can be instance of integer, string or json object
	 * @throws UnsupportedEncodingException
	 */
	public Object sendFileRequest(TaskFileKanboardConfigutration config) throws UnsupportedEncodingException {
		
		RestTemplate restTemplate = new RestTemplate();

		// encode api Token
		byte[] xAPIAuthTokenBytes = String.join(":", _USER, _API_TOKEN).getBytes("utf-8");
		//String xAPIAuthToken = Base64.getEncoder().encodeToString(xAPIAuthTokenBytes);

		byte[] xLogin = String.join(":", "system_user", "gh5KuH4eLPYv4eA").getBytes("utf-8");
		String xLoginToken = Base64.getEncoder().encodeToString(xLogin);

		// build Request
		HttpHeaders headers = new HttpHeaders();
		// add header parameter
		// headers.add("X-API-Auth", xAPIAuthToken);
		headers.add("Authorization", "Basic " + xLoginToken);
		
		headers.setContentType(MediaType.APPLICATION_JSON);

		// response after push request
		HttpEntity<String> entity = new HttpEntity<String>(config.buildRequest(), headers);
		String response = restTemplate.postForObject(_URL, entity, String.class);
		System.out.println(response);

		// convert answer to object
		// then read Information of response

		try {
			JSONObject responseObject = new JSONObject(response);
			tLog.getInstance().log(null, "info", responseObject.get("result").toString());
			
			return responseObject.get("result");
		} catch (JSONException err) {
			System.out.println(err);
			return err.toString();
		}
	}
	
	/**
	 * Send a request to Kanboard
	 * 
	 * Please show Kanboard API https://docs.kanboard.org/v1/api/task_procedures/
	 * 
	 * @param config TaskKanboardConfiguration a defined task
	 * @return Object result can be any Type, such as integer, JSONObject, String ...    
	 * @throws UnsupportedEncodingException
	 */
	public Object sendTaskRequest(TaskKanboardConfiguration config) throws UnsupportedEncodingException {
		
		RestTemplate restTemplate = new RestTemplate();

		// encode api Token
		byte[] xAPIAuthTokenBytes = String.join(":", _USER, _API_TOKEN).getBytes("utf-8");
		//String xAPIAuthToken = Base64.getEncoder().encodeToString(xAPIAuthTokenBytes);

		byte[] xLogin = String.join(":", "system_user", "gh5KuH4eLPYv4eA").getBytes("utf-8");
		String xLoginToken = Base64.getEncoder().encodeToString(xLogin);

		// build Request
		HttpHeaders headers = new HttpHeaders();
		// add header parameter
		// headers.add("X-API-Auth", xAPIAuthToken);
		headers.add("Authorization", "Basic " + xLoginToken);
		
		headers.setContentType(MediaType.APPLICATION_JSON);

		// response after push request
		HttpEntity<String> entity = new HttpEntity<String>(config.buildRequest(), headers);
		String response = restTemplate.postForObject(_URL, entity, String.class);
		//System.out.println(response);

		// convert answer to object
		// then read Information of response

		try {
			JSONObject responseObject = new JSONObject(response);
			tLog.getInstance().log(null, "info", responseObject.get("result").toString());
			
			return responseObject.get("result");
		} catch (JSONException err) {
			System.out.println(err);
			return err.toString();
		}
	}
	
	/**
	 * Build a request connection to Kanboard with kriterien
	 * 
	 * @param titel      title of ticket
	 * @param descript   description of ticket
	 * @param color      color of ticket, cyan for a new ticket input
	 * @param projectid  in which Project should be a ticket ordered
	 * @param swimlaneid ordered Swimlane
	 * @param columnid   ordered Column
	 * @param start_date Start-Date
	 * @param due_date   End-Date
	 * @return int id task id if request is success, else -1.
	 * @throws UnsupportedEncodingException
	 */
	public int buildTicket(String titel, String descript, String color, int projectid, int swimlaneid,
			int columnid, LocalDate start_date, LocalDate due_date) throws UnsupportedEncodingException {
		int intKanboardID = 0;
		RestTemplate restTemplate = new RestTemplate();

		// encode api Token
		byte[] xAPIAuthTokenBytes = String.join(":", _USER, _API_TOKEN).getBytes("utf-8");
		String xAPIAuthToken = Base64.getEncoder().encodeToString(xAPIAuthTokenBytes);

		byte[] xLogin = String.join(":", "system_user", "gh5KuH4eLPYv4eA").getBytes("utf-8");
		String xLoginToken = Base64.getEncoder().encodeToString(xLogin);

		// build Request
		HttpHeaders headers = new HttpHeaders();
		// add header parameter
		// headers.add("X-API-Auth", xAPIAuthToken);
		headers.add("Authorization", "Basic " + xLoginToken);
		
		String requestJson = "{" + "\"jsonrpc\": \"2.0\"," + "\"method\": \"createTask\"," + "\"id\": 1176509098,"
				+ "\"params\": {"
//						+ "\"owner_id\": 1," //test that a task without reference person
				+ "\"creator_id\": 18," + "\"description\": \"" + descript + "\"," + "\"title\": \"" + titel + "\","
				+ "\"project_id\": \"" + projectid + "\",";
		if (due_date != null) {
			requestJson += "\"date_started\": \"" + start_date.toString() + "\"," + "\"date_due\": \""
					+ due_date.toString() + "\"," + "\"swimmland_id\": \"" + swimlaneid + "\"," + "\"color_id\": \""
					+ color + "\"," + "\"column_id\": \"" + columnid + "\"" + "}" + "}";
		} else {
			requestJson += "\"date_started\": \"" + start_date.toString() + "\"," + "\"swimmland_id\": \"" + swimlaneid
					+ "\"," + "\"color_id\": \"" + color + "\"," + "\"column_id\": \"" + columnid + "\"" + "}" + "}";
		}

		headers.setContentType(MediaType.APPLICATION_JSON);

		// response after push request
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		String answer = restTemplate.postForObject(_URL, entity, String.class);
		System.out.println(answer);

		// convert answer to object
		// then read Information of response

		try {
			JSONObject responseObject = new JSONObject(answer);
			// get value of key "result"
			intKanboardID = responseObject.getInt("result");

			tLog.getInstance().log(null, "info", answer);
		} catch (JSONException err) {
			System.out.println(err);
		}

		if (intKanboardID > 0) {
			return intKanboardID;
		} else {
			return -1;
		}
	}
	
}
