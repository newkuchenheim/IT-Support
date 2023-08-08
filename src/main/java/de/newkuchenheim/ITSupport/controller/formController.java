/**
 * 
 */
package de.newkuchenheim.ITSupport.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.TicketCategory;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.dao.kanboardDAO;
import de.newkuchenheim.ITSupport.dao.implement.ticketKanboardDAO;

/**
 * @author Minh Tam Truong, Sebastian Hansen
 * 
 * @createOn 22.09.2022
 * 
 */

@Controller
@RequestMapping("itsupport/ticket")
public class formController {

	private static List<Ticket> tickets = new ArrayList();

	private final String _URL_TICKETCATS  = System.getenv("USERPROFILE") + "\\TicketContent\\ticketcats.json";//"%USERPROFILE%/ticketcontent/ticketcats.json";
	private final String _URL_TICKETCATS_LINUX  = System.getProperty("user.home") + "/TicketContent/ticketcats.json";//"/home/itsupport/itsupport/ticketcats.json";
	
	@ModelAttribute("page")
    String page() {
        return "ticket";
    }
	
	@GetMapping
	public String displayAllEvents(Model model) {
		
		model.addAttribute("tickets", tickets);
		
		//send a request with ticket
		try {
//			int answer = kanboardDAO.getInstance().sendTicket(tickets.get(0));
//			model.addAttribute("result", answer);
						
			int TicketID = ticketKanboardDAO.getInstance().sendTicket(tickets.get(0));
			model.addAttribute("result", TicketID);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			tLog.getInstance().log(null, "severe", e.getMessage());
		}
		
		return "itsupport/ticket/home";
	}
	
	@GetMapping("form")
	public String renderCreateForm(Model model) throws FileNotFoundException {
		
		tickets.clear();

		//tracking
		System.out.println("call a form ticket" + LocalDateTime.now());
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
		model.addAttribute("ticket", ticket);
		
		tickets.add(ticket);
//		return "create/home";
		return "redirect:";
	}
}
