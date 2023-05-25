/**
 * 
 */
package de.newkuchenheim.ITSupport.dao;

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

import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.awNote;
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
	private final String _TAB_UL_1 = "- ", _NEWLINE = " \\r\\n", _BOLD_OPEN = " **", _BOLD_CLOSE = "** ",
			_HEADING_1_OPEN = "# ", _HEADING_1_CLOSE = " # \\r\\n", _HEADING_2_OPEN = "## ",
			_HEADING_2_CLOSE = " ## \\r\\n", _HEADING_3_OPEN = "### ", _HEADING_3_CLOSE = " ### \\r\\n";
	
	/**
	 * Get Instance kanboardDAO
	 * @return kanboardDAO
	 */
	public static kanboardDAO getInstance() {
		if (instance == null) {
			return new kanboardDAO();
		}
		return instance;
	}
	
	/**
	 * send a request to create a Ticket in IT-Support Board
	 * 
	 * @param ticket
	 * 
	 * @return int id if request was sent success. Otherwise -1 when failure.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public int sendTicket(Ticket ticket) throws UnsupportedEncodingException {
		int intKanboardID = 0;
		
		if (ticket != null) {
			String title = "[" + ticket.getBuilding() + "]-[" + ticket.getCategory() + "]-[" + ticket.getLastname()
					+ "]";
			
			//CR or LN check
			//bearbeiten description if line Seper. contain
			String description = ticket.getDescription();
			String new_desc = "";
			if (description != null && !description.isBlank() && description.contains(System.lineSeparator())) {
				//String[] _str_split = description.split(System.lineSeparator());
				String[] _str_split = description.split("\\R");
				for (String s : _str_split) {
					new_desc = new_desc + s + _NEWLINE;
				}
				description = new_desc;
			}

			String descript = _HEADING_1_OPEN + ticket.getCategory() + _HEADING_1_CLOSE + _HEADING_3_OPEN + "Kontakt"
					+ _HEADING_3_CLOSE + _TAB_UL_1 + _BOLD_OPEN + "Name:" + _BOLD_CLOSE + ticket.getFirstname() + " "
					+ ticket.getLastname() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Tel: " + _BOLD_CLOSE
					+ ticket.getTelefon() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Werkstatt: " + _BOLD_CLOSE
					+ ticket.getBuilding() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Abteilung: " + _BOLD_CLOSE
					+ ticket.getBranch() + _NEWLINE + _NEWLINE + _HEADING_3_OPEN + "TICKET" + _HEADING_3_CLOSE
					+ _TAB_UL_1 + _BOLD_OPEN + "Grund: " + _BOLD_CLOSE + ticket.getCategory() + _NEWLINE + _TAB_UL_1
					+ _BOLD_OPEN + "Beschreibung: " + _NEWLINE + _BOLD_CLOSE + description;
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
			;
			String requestJson = "{" + "\"jsonrpc\": \"2.0\"," + "\"method\": \"createTask\"," + "\"id\": 1176509098,"
					+ "\"params\": {"
//							+ "\"owner_id\": 1," //test that a task without reference person
					+ "\"creator_id\": 18," + "\"description\": \"" + descript + "\"," + "\"title\": \"" + title + "\","
					+ "\"project_id\": 1," + "\"swimmland_id\": 57," + "\"color_id\": \"cyan\"," + "\"column_id\": 1"
					+ "}" + "}";
			headers.setContentType(MediaType.APPLICATION_JSON);
			// response after push request
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
			String answer = restTemplate.postForObject(_URL, entity, String.class);
			
			// convert answer to object
			// then read Information of response
			try {
				JSONObject responseObject = new JSONObject(answer);
				//get value of key "result"
				intKanboardID = responseObject.getInt("result");
				
				tLog.getInstance().log(null, "info", answer);
			} catch (JSONException err) {
				System.out.println(err);
			}

		}
		if (intKanboardID > 0) {
			return intKanboardID;
		} else {
			return -1;
		}
	}

	/**
	 * Send Abwesenheitsnotiz
	 * 
	 * @param awNote Object of Abwesenheitsnotiz
	 * @return result
	 * @throws UnsupportedEncodingException
	 */
	public String sendAWNote(awNote ticket) throws UnsupportedEncodingException {
		if (ticket != null) {
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String fromDate = ticket.getAwvonDate().format(formatter),
					toDate = "";
			
			/////////////////////////////////////////////////////////////////////////////////
			//wenn bisDate null ist -> unbestimmte Enddatum = "auf weiteres"
			if(ticket.getAwtoDate() != null) {
				toDate = ticket.getAwtoDate().format(formatter);
			} else {
				toDate = "auf Weiteres";
			}
			////////////////////////////////////////////////////////////////////////////////
			
			//fester Mustertext
			String title = "[" + ticket.getMname() + "] [" + fromDate + " - " + toDate
					+ "]";
			
			String body = "Sehr geehrte Damen und Herren, "
					+ _NEWLINE + _NEWLINE
					+ "ich bin vom " + fromDate
					+ " bis " + toDate
					+ " nicht im Hause." + _NEWLINE
					+ "Bitte beachten Sie, dass Ihre Mail nicht automatisch weitergeleitet wird." + _NEWLINE;
			
			//Problem mit dem Binding von Object mit form über Typ booleen -> check, ob Kontakt 1 und 2 leer sind? 
			boolean isVertreter1 = ticket.getKontakt1()!=null && !ticket.getKontakt1().isBlank();
			boolean isVertreter2 = ticket.getKontakt2()!=null && !ticket.getKontakt2().isBlank();
			boolean isVertreter = isVertreter1 || isVertreter2;
			
			//falls kein vertreter 
			if(!isVertreter) {
				//set German Date format
				
				body += _NEWLINE + "Mit freundlichen Grüßen," + _NEWLINE
						+ ticket.getMname();
				
				// if fromDate im Intevall +1 oder -1 liegt, veschiebe ticket direkt in Bereit-Spalte (77)
				// ansonsten in Eingang (76)
				LocalDate _heute = LocalDate.now();
				if(_heute.isEqual(ticket.getAwvonDate()) 
						|| _heute.isEqual(ticket.getAwvonDate().minusDays(1)) 
						|| _heute.isEqual(ticket.getAwvonDate().plusDays(1))) {
					return buildCreateTicket(title, body, "green",18, 61, 77, ticket.getAwvonDate(), ticket.getAwtoDate());
				} 
				else return buildCreateTicket(title, body, "cyan",18, 61, 76, ticket.getAwvonDate(), ticket.getAwtoDate());
			} 
			else { //sonst mit Vertreter 1 oder 2 oder beiden
				String vertreter = "";
				
				if(ticket.getKontakt1() != null && !ticket.getKontakt1().isBlank()) {
					vertreter = _TAB_UL_1 + ticket.getKontakt1(); //neue Zeile mit list *
					
					String _tel = ticket.getTelnr1(), _mail = ticket.getMail1();
					if(_tel !=null && !_tel.isBlank()) { //überprüfen zuerst Kontak 1	
						vertreter += " telefonisch unter " + _tel;
					}
					if(_mail != null && !_mail.isBlank()) {
						if(_tel != null && !_tel.isBlank() && _mail !=null && !_mail.isBlank()) {
							vertreter += " oder unter " + _mail;
						} else 
							vertreter += " unter " + _mail;
					}
					
					if(ticket.getKontakt2() != null && !ticket.getKontakt2().isBlank()) { //dann überprüfen Kontakt 2, falls nicht leer, dann fügen daten hinzu
						vertreter += _NEWLINE + _TAB_UL_1 + ticket.getKontakt2();
						String _mail2 = ticket.getMail2(), _tel2 = ticket.getTelnr2();
						if(_tel2 !=null && !_tel2.isBlank()) {	
							vertreter += " telefonisch unter " + _tel2;
						}
						if(_mail2 != null && !_mail2.isBlank()) {
							_mail2 = ticket.getMail2();
							if(_tel2 != null && !_tel2.isBlank() && _mail2 !=null && !_mail2.isBlank()) {
								vertreter += " oder unter " + _mail2;
							} else 
								vertreter += " unter " + _mail2;
						}
					} 
				} else {
					vertreter = _TAB_UL_1 + ticket.getKontakt2(); // neue Zeile nut für kontakt 2 mit List *, wenn kontakt 1 nicht ausgefüllt wird
					
					String _mail2 = ticket.getMail2(), _tel2 = ticket.getTelnr2();
					if(_tel2 !=null && !_tel2.isBlank()) {	
						vertreter += " telefonisch unter " + _tel2;
					}
					if(_mail2 != null && !_mail2.isBlank()) {
						_mail2 = ticket.getMail2();
						if(_tel2 != null && !_tel2.isBlank() && _mail2 !=null && !_mail2.isBlank()) {
							vertreter += " oder unter " + _mail2;
						} else 
							vertreter += " unter " + _mail2;
					}
				}
				
				//fügen Kontakte in Message Body
				body += "In dringenden Fällen wenden Sie sich daher bitte an " + _NEWLINE
						+ vertreter + _NEWLINE + _NEWLINE
						+ "Mit freundlichen Grüßen," + _NEWLINE
						+ ticket.getMname();				
				/////////////////////////////////////////////////////////////////////////////////////
				
				// if fromDate im Intevall +1 oder -1 liegt, veschiebe ticket direkt in Bereit-Spalte (77)
				// ansonsten in Eingang (76)
				LocalDate _heute = LocalDate.now();
				if(_heute.isEqual(ticket.getAwvonDate()) 
						|| _heute.isEqual(ticket.getAwvonDate().minusDays(1)) 
						|| _heute.isEqual(ticket.getAwvonDate().plusDays(1))) {
					return buildCreateTicket(title, body, "green",18, 61, 77, ticket.getAwvonDate(), ticket.getAwtoDate());
				} 
				else return buildCreateTicket(title, body, "cyan",18, 61, 76, ticket.getAwvonDate(), ticket.getAwtoDate());
			}
						
		}
		return "Result on failure";
	}

	/**
	 * Build a request connection to Kanboard with kriterien
	 * 
	 * @param titel 	title of ticket
	 * @param descript 	descriptiom of ticket
	 * @param color		color of ticket, cyan for a new ticket input
	 * @param projectid in which Project should be a ticket ordered
	 * @param swimlaneid ordered Swimlane
	 * @param columnid  ordered Column
	 * @param start_date Start-Date 
	 * @param due_date  End-Date
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String buildCreateTicket(String titel, String descript, String color,  int projectid, int swimlaneid, int columnid, LocalDate start_date, LocalDate due_date)
			throws UnsupportedEncodingException {
		try {
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
			;
			
			
			String requestJson = "{" + "\"jsonrpc\": \"2.0\"," + "\"method\": \"createTask\"," + "\"id\": 1176509098,"
					+ "\"params\": {"
//						+ "\"owner_id\": 1," //test that a task without reference person
					+ "\"creator_id\": 18," 
					+ "\"description\": \"" + descript + "\"," 
					+ "\"title\": \"" + titel + "\","
					+ "\"project_id\": \"" + projectid + "\",";
			if(due_date!=null) {
				requestJson += "\"date_started\": \"" + start_date.toString() + "\","
						+ "\"date_due\": \"" + due_date.toString() + "\","
						+ "\"swimmland_id\": \"" +swimlaneid+"\"," 
						+ "\"color_id\": \"" + color +"\","
						+ "\"column_id\": \"" + columnid + "\"" + "}" + "}";
			} else {
				requestJson += "\"date_started\": \"" + start_date.toString() + "\","
						+ "\"swimmland_id\": \"" +swimlaneid+"\"," 
						+ "\"color_id\": \"" + color + "\","
						+ "\"column_id\": \"" + columnid + "\"" + "}" + "}";
			}
					
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			// response after push request
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
			String answer = restTemplate.postForObject(_URL, entity, String.class);
			System.out.println(answer);

			// convert answer to object
			// then read Information of response

			JSONObject responseObject = new JSONObject(answer);

			// set value to response object
			ResponseKanboard<Integer> resp = new ResponseKanboard();
			if (responseObject.get("id") instanceof Long) {
				resp.setMethodID(responseObject.getInt("id"));
				if (responseObject.get("result") instanceof Integer) {
					ResultKanboard<Integer> result = new ResultKanboard();
					resp.setResults(result);
					tLog.getInstance().log(null, "info", answer);
				}
			} else {
				resp.setMethodID(0);

				// create error obj
				ErrorKanboard err = new ErrorKanboard();
				if (responseObject.getJSONObject("error") != null) {
					err.setErrorCode(responseObject.getJSONObject("error").getInt("code"));
					err.setErrorMessage(responseObject.getJSONObject("error").getString("message"));
					resp.setError(err);
					tLog.getInstance().log(null, "severe", "Result on failure! " + answer);
				}
			}

			return resp.getAnswerFromKanboard();
		} catch (JSONException err) {
			System.out.println(err);
		}
		return "Result on failure";
	}

	/**
	 * Send a feedback to KanBoard Wiki Project
	 * 
	 * @param ticket feedback
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String sendWKFeedback(Ticket ticket) throws UnsupportedEncodingException {
		if (ticket != null) {
			String title = "[" + ticket.getBuilding() + "]-[" + ticket.getCategory() + "]-[" + ticket.getLastname()
					+ "]";
			
			//CR or LN check
			//bearbeiten description if line Seper. contain
			String description = ticket.getDescription();
			String new_desc = "";
			if (description != null && !description.isBlank() && description.contains(System.lineSeparator())) {
				//String[] _str_split = description.split(System.lineSeparator());
				String[] _str_split = description.split("\\R");
				for (String s : _str_split) {
					new_desc = new_desc + s + _NEWLINE;
				}
				description = new_desc;
			}

			String descript = _HEADING_1_OPEN + ticket.getCategory() + _HEADING_1_CLOSE + _HEADING_3_OPEN + "Kontakt"
					+ _HEADING_3_CLOSE + _TAB_UL_1 + _BOLD_OPEN + "Name:" + _BOLD_CLOSE + ticket.getFirstname() + " "
					+ ticket.getLastname() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Werkstatt: " + _BOLD_CLOSE
					+ ticket.getBuilding() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Abteilung: " + _BOLD_CLOSE
					+ ticket.getBranch() + _NEWLINE + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Grund: " + _BOLD_CLOSE + ticket.getCategory() + _NEWLINE + _TAB_UL_1
					+ _BOLD_OPEN + "Beschreibung: " + _NEWLINE + _BOLD_CLOSE + description;
			
			//send request to Kanboard
			return buildCreateTicket(title, descript, "cyan", 16, 143, 66, LocalDate.now(), LocalDate.now());

		}
		return "Result on failure";
	}
}
