/**
 * 
 */
package de.newkuchenheim.ITSupport.controller.it;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.Email;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.bdo.itsupport.Ticket;
import de.newkuchenheim.ITSupport.bdo.itsupport.TicketCategory;
import de.newkuchenheim.ITSupport.bdo.mailConfig.emailConfiguration;
import de.newkuchenheim.ITSupport.bdo.mailConfig.emailUtil;
import de.newkuchenheim.ITSupport.dao.implement.it.ticketKanboardDAO;
import de.newkuchenheim.ITSupport.dao.kanboard.kanboardDAO;
import jakarta.mail.Session;
import javassist.compiler.ast.Symbol;

/**
 * @author Minh Tam Truong, Sebastian Hansen
 * 
 * @createOn 22.09.2022
 * 
 */

@Controller
@RequestMapping("itsupport/ticket")
public class ticketController extends itsupportController {

	private static List<Ticket> tickets = new ArrayList();
	//private String current_mapping;
	private final String _URL_TICKETCATS  = System.getenv("USERPROFILE") + "\\IT-SupportContent\\Ticket\\ticketcats.json";//"%USERPROFILE%/it-supportcontent/ticket/ticketcats.json";
	private final String _URL_TICKETCATS_LINUX  = System.getProperty("user.home") + "/IT-SupportContent/Ticket/ticketcats.json";//"/home/itsupport/itsupport/it-supportcontent/ticket/ticketcats.json";
	
	//Mail Configuration
	private emailConfiguration emailConfig = new emailConfiguration();
	private emailUtil emailUtilitiy = emailUtil.getInstance();
		
		
//	@ModelAttribute("page")
//    String page() {
//		
//		return "ticket";
//    }
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of ticket_tracking
* Post & GetMapping via form
* Response via home
*/
//////////////////////////////////////////////////////////////////////////////////////////////////

	@GetMapping({"", "/"})
	public String displayAllEvents(Model model) {
		//configurate Navigation
		initNavigation(model, "ticket");
		
		model.addAttribute("tickets", tickets);
		
		//send a request with ticket
		try {
			for(Ticket newTicket:tickets){
				//Ticket newTicket = tickets.get(0);
				//System.out.println(newTicket.getId());
				
				// get a available ticket with ticket ID
				if(newTicket.getId() > 0) {
					Ticket result = ticketKanboardDAO.getInstance().getTicketByName(newTicket);
					
					//System.out.println(result);
					
					if(result.getTitle() != null && !result.getTitle().isBlank()) {
						//System.out.println(result.getTitle() + ": " + result.getDescription() + ", color: " + result.getColor_id());
						model.addAttribute("event_response", "tracking");
						model.addAttribute("color", result.getColor_id());
						
						//title
						model.addAttribute("title", result.getTitle());
						
						//Problem
						model.addAttribute("categorie", result.getCategory());
						//IT-Log
						model.addAttribute("state", "Vorbereitungsphase: " + result.getState());
						model.addAttribute("contact_person", "Ansprechpartner: " + (result.getContactperson()==null ? "Keine Angabe" : result.getContactperson()));
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);
						model.addAttribute("beginn_at", "** Vorraussichtlich gestartet am: " + (result.getBeginn_at()==null ? "Keine Angabe" : result.getBeginn_at().format(formatter)));
						model.addAttribute("ended_at", "** Vorraussichtlich abgeschlossen am: " + (result.getEnded_am()==null ? "Keine Angabe" : result.getEnded_am().format(formatter)));
						model.addAttribute("changed_at", (result.getEnded_am()==null ? "Bisher ist es keine Änderung gefunden." : ("Ticket wurde am " + result.getChanged_at().format(formatter) + " aktualisiert.")));
						
						
						List<String> desc_list = new ArrayList<>();//Arrays.asList(result.getDescription().split("\r\n"));
						if(result.getDescription() != null && !result.getDescription().isBlank()) {
							desc_list = Arrays.asList(result.getDescription().split("\r\n"));
						} else {
							desc_list.add("keine Angabe");
						}
						
						model.addAttribute("desc_list", desc_list);
						
					}
				} else { // send a new ticket
					model.addAttribute("event_response", "ticket");
					int TicketID = ticketKanboardDAO.getInstance().sendTicket(newTicket);
					model.addAttribute("result", TicketID);
					// add file to new ticket
					if(TicketID > -1) {
						//send mail
						if(newTicket.getEmail() != null && !newTicket.getEmail().isBlank()) {
							tLog.getInstance().log(null, "info", "sending mail to #"+TicketID);
							Session session = Session.getDefaultInstance(this.emailConfig.getProperties(), this.emailConfig.getAuthenticator());
							
							Email mail = new Email();
							mail.setSubject("Support-Ticket #" + TicketID + " wurde gesendet.");
							mail.setMsgBody("Vielen Dank für Ihre Nutzung unseres Ticketsystems! \nWir werden schnellstmöglich Ihr Ticket bearbeiten. "
									+ "Sie können den Bearbeitungstand des Tickets verfolgen, indem Sie das Ticket-Tracking anwenden. Dabei geben Sie bitte Ihre Ticket-ID und Ihren Nachnamen ein.\n"
									+ "\t* Ticket-Tracking: 192.168.0.224:8080/itsupport/Ticket-Tracking \n"
									+ "\t* Ticket-ID: " + TicketID + "\n"
									+ "\t* Gesendet am: " + LocalDateTime.now().toString());
							mail.setRecipient(newTicket.getEmail());
							
							this.emailUtilitiy.sendSimpleMail(session, mail, this.emailConfig.getFromMail());
							tLog.getInstance().log(null, "info", "#"+TicketID + ": mail have been to send");
							
						}
						
						//send file
						if (!newTicket.getFileContent().isBlank()) {
							newTicket.setId(TicketID);
							//send file
							ticketKanboardDAO.getInstance().sendFile(newTicket);
							
						}
					} 
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			tLog.getInstance().log(null, "severe", e.getMessage());
		}
		
		return "itsupport/ticket/home";
	}
	
	@GetMapping({"form", "form/"})
	public String renderCreateForm(Model model) throws FileNotFoundException {
		//configurate Navigation
		initNavigation(model, "ticket");
				
		tickets.clear();
		
		//tracking
		System.out.println("call a form ticket " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a ticket form");
		
		model.addAttribute("ticket", new Ticket());
		
		//============== add new List for Ticket,MICOS and User Categories ===============
		List<TicketCategory> _micoscats = new ArrayList<TicketCategory>();
		List<TicketCategory> _categories = new ArrayList<TicketCategory>();
		List<TicketCategory> _usercats = new ArrayList<TicketCategory>();
		String _sys_path = null;
		
		if(System.getProperty("os.name").equals("Linux")) {
			_sys_path = _URL_TICKETCATS_LINUX;
		} else if (System.getProperty("os.name").equals("Windows 10")) {
			_sys_path = _URL_TICKETCATS;
		}
		
		if(_sys_path != null && !_sys_path.isBlank()) {
			Path _checkPath = Paths.get(_sys_path);
			if(Files.exists(_checkPath)) {
				InputStream is = new FileInputStream(new File(_sys_path));
				
		        JSONTokener tokener = new JSONTokener(is);
		        JSONObject _ticketcats_json = new JSONObject(tokener);
		        JSONArray _micoscats_json = _ticketcats_json.getJSONArray("micoscats");
		        _micoscats_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_micoscats.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
		        JSONArray _categories_json = _ticketcats_json.getJSONArray("categories");
		        _categories_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_categories.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
		        JSONArray _usercats_json = _ticketcats_json.getJSONArray("usercats");
		        _usercats_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_usercats.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
			}
		}
		model.addAttribute("micoscats", _micoscats);
		model.addAttribute("categories", _categories);
		model.addAttribute("usercats", _usercats);
		//==========================================================================
		
		return "itsupport/ticket/form";
	}
	
	@PostMapping("form")
	public String sendForm(@ModelAttribute Ticket ticket, Model model) {
		
		if (ticket.getFile() != null) {
			try {
				ticket.setFileContent(Base64.getEncoder().encodeToString(ticket.getFile().getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
				tLog.getInstance().log(null, "severe", e.getMessage());
			}
		}
		model.addAttribute("ticket", ticket);
		tickets.add(ticket);
//		return "create/home";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Controller of ticket_tracking
 * Post & GetMapping
 */
//////////////////////////////////////////////////////////////////////////////////////////////////

	@GetMapping({"ticket_tracking", "ticket_tracking/"})
	public String renderTicketTracking(Model model) {
		
		//configurate Navigation
		initNavigation(model, "ticket_tracking");
				
		tickets.clear();
		//tracking
		System.out.println("call form ticket-status " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call form ticket-status");
		
		model.addAttribute("ticket", new Ticket());
		
		return "itsupport/ticket/ticket_tracking";
	}
	
	@PostMapping("ticket_tracking")
	public String sendTracking(@ModelAttribute Ticket ticket, Model model) {		
		
		// Get file content as Base64 String, uploaded temp file will be deleted after this
		model.addAttribute("ticket", ticket);
		tickets.add(ticket);
//		return "create/home";
		return "redirect:";
	}
}
