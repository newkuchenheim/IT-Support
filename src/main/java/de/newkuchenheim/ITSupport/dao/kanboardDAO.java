/**
 * 
 */
package de.newkuchenheim.ITSupport.dao;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.tLog;
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
public class kanboardDAO {
	
	private static kanboardDAO instance;
	private final String _URL = "http://192.168.0.216/kanboard/jsonrpc.php";
	private final String _USER = "jsonrpc";
	private final String _API_TOKEN = "201bc0696f069fce5734bd955951011904ba7576a4014d888ab5c42acd22";
	private final String _TAB_UL_1 = "- ", 
			_NEWLINE = " \\r\\n", 
			_BOLD_OPEN = " **",
			_BOLD_CLOSE = "** ",
			_HEADING_1_OPEN = "# ",
			_HEADING_1_CLOSE = " # \\r\\n",
			_HEADING_2_OPEN = "## ",
			_HEADING_2_CLOSE = " ## \\r\\n",
			_HEADING_3_OPEN = "### ",
			_HEADING_3_CLOSE = " ### \\r\\n";;
	
	public static kanboardDAO getInstance() {
		if(instance == null) {
			return new kanboardDAO();
		}
		return instance;
	}
	
	public String sendTicket(Ticket ticket) throws UnsupportedEncodingException {
		if(ticket != null) {
			String title = "[" + ticket.getBuilding() + "]-[" 
							+ ticket.getCategory() + "]-[" 
							+ ticket.getLastname() + "]";
			
			String descript = _HEADING_1_OPEN + ticket.getCategory() + _HEADING_1_CLOSE 
								+ _HEADING_3_OPEN + "Kontakt" + _HEADING_3_CLOSE
								+ _TAB_UL_1 + _BOLD_OPEN + "Name:" + _BOLD_CLOSE + ticket.getFirstname() + " " + ticket.getLastname() + _NEWLINE
								+ _TAB_UL_1 + _BOLD_OPEN + "Tel: " + _BOLD_CLOSE + ticket.getTelefon() + _NEWLINE
								+ _TAB_UL_1 + _BOLD_OPEN + "Werkstatt: " + _BOLD_CLOSE + ticket.getBuilding() + _NEWLINE
								+ _TAB_UL_1 + _BOLD_OPEN + "Abteilung: " + _BOLD_CLOSE + ticket.getBranch() + _NEWLINE
								+ _NEWLINE
								+ _HEADING_3_OPEN + "TICKET" + _HEADING_3_CLOSE
								+ _TAB_UL_1 + _BOLD_OPEN + "Grund: " + _BOLD_CLOSE + ticket.getCategory() + _NEWLINE
								+ _TAB_UL_1 + _BOLD_OPEN + "Beschreibung: " + _BOLD_CLOSE + ticket.getDescription();
			RestTemplate restTemplate = new RestTemplate();
			
			//encode api Token
			byte[] xAPIAuthTokenBytes = String.join(":", _USER, _API_TOKEN).getBytes("utf-8");
			String xAPIAuthToken = Base64.getEncoder().encodeToString(xAPIAuthTokenBytes);
			
			byte[] xLogin = String.join(":", "system_user", "gh5KuH4eLPYv4eA").getBytes("utf-8");
			String xLoginToken = Base64.getEncoder().encodeToString(xLogin);
			
			//build Request
			HttpHeaders headers = new HttpHeaders();
			//add header parameter
			//headers.add("X-API-Auth", xAPIAuthToken);
			headers.add("Authorization", "Basic " + xLoginToken);;
			String requestJson = "{"
					+ "\"jsonrpc\": \"2.0\","
					+ "\"method\": \"createTask\","
					+ "\"id\": 1176509098,"
					+ "\"params\": {"
//							+ "\"owner_id\": 1," //test that a task without reference person
							+ "\"creator_id\": 18,"
							+ "\"description\": \"" + descript + "\","
							+ "\"title\": \"" + title +"\","
							+ "\"project_id\": 1,"
							+ "\"swimmland_id\": 57,"
							+ "\"color_id\": \"cyan\","
							+ "\"column_id\": 1"
						+ "}"
					+ "}";
			headers.setContentType(MediaType.APPLICATION_JSON);
			//response after push request
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
			String answer = restTemplate.postForObject(_URL, entity, String.class);
			System.out.println(answer);
			
			//convert answer to object
			//then read Information of response
			try {
				JSONObject responseObject = new JSONObject(answer);
				
				//set value to response object
				ResponseKanboard<Integer> resp = new ResponseKanboard();
				if(responseObject.get("id") instanceof Long) {
					resp.setMethodID(responseObject.getInt("id"));
					if(responseObject.get("result") instanceof Integer) {
						ResultKanboard<Integer> result = new ResultKanboard();
						resp.setResults(result);
						tLog.getInstance().log(null, "info", "Result on success! " + answer);
					}
				} else {
					resp.setMethodID(0);
					
					//create error obj
					ErrorKanboard err = new ErrorKanboard();
					if(responseObject.getJSONObject("error")!=null) {
						err.setErrorCode(responseObject.getJSONObject("error").getInt("code"));
						err.setErrorMessage(responseObject.getJSONObject("error").getString("message"));
						resp.setError(err);
						tLog.getInstance().log(null, "severe", "Result on failure! " + answer);
					}
				}
				
				return resp.getAnswerFromKanboard();
			} catch (JSONException err){
				System.out.println(err);
			}
			
		}
		return "Result on failure";
	}
}
