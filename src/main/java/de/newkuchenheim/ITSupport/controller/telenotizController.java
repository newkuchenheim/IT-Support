package de.newkuchenheim.ITSupport.controller;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.dao.kanboardDAO;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 19.04.2023
 * 
 */

@Controller
@RequestMapping("itsupport/telenotiz")
public class telenotizController {
	//private static List<Ticket> tickets = new ArrayList();
	@ModelAttribute("page")
    String page() {
        return "telenotiz";
    }
	
	@ModelAttribute("noteID")
    String noteID() {
		// create Random Note id
		Random rand = new Random();
		return "" + rand.nextInt(999999);
    }
	
	@GetMapping
	public String displayAllEvents(Model model) {
		
		//model.addAttribute("tickets", tickets);
		
		//send a request with ticket
//		try {
//			String answer = kanboardDAO.getInstance().sendTicket(tickets.get(0));
//			model.addAttribute("result", answer);
//						
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			tLog.getInstance().log(null, "severe", e.getMessage());
//		}
		
		return "itsupport/telenotiz/home";
	}
	
	@GetMapping("form")
	public String renderCreateForm(Model model) {
		
		//tickets.clear();

		//tracking
		System.out.println("call a form ticket" + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a form");
		
		// model.addAttribute("ticket", new Ticket());
		return "itsupport/telenotiz/form";
	}
	
	@PostMapping("form")
	public void sendForm() {
//	public String sendForm(@ModelAttribute Ticket ticket, Model model) {
//		model.addAttribute("ticket", ticket);
//		
//		System.out.println(ticket.getFirstname() + " " + ticket.getLastname());
//		System.out.println("awNote wurde gesendet am " + LocalDateTime.now());
//		tLog.getInstance().log(null, "Info", "Trying to create a awNote");
//		
//		tickets.add(ticket);
////		return "create/home";
//		return "redirect:";
	}
}
