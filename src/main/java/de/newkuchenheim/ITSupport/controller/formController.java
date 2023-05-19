/**
 * 
 */
package de.newkuchenheim.ITSupport.controller;

import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
 * @author Minh Tam Truong
 * 
 * @createOn 22.09.2022
 * 
 */

@Controller
@RequestMapping("itsupport/ticket")
public class formController {

	private static List<Ticket> tickets = new ArrayList();

	@ModelAttribute("page")
    String page() {
        return "ticket";
    }
	
	@GetMapping
	public String displayAllEvents(Model model) {
		
		model.addAttribute("tickets", tickets);
		
		//send a request with ticket
		try {
			int answer = kanboardDAO.getInstance().sendTicket(tickets.get(0));
			model.addAttribute("result", answer);
						
			int TicketID = kanboardDAO.getInstance().sendTicket(tickets.get(0));
			model.addAttribute("result", TicketID);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			tLog.getInstance().log(null, "severe", e.getMessage());
		}
		
		return "itsupport/ticket/home";
	}
	
	@GetMapping("form")
	public String renderCreateForm(Model model) {
		
		tickets.clear();

		//tracking
		System.out.println("call a form ticket" + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a form");
		
		model.addAttribute("ticket", new Ticket());
		return "itsupport/ticket/form";
	}
	
	@PostMapping("form")
	public String sendForm(@ModelAttribute Ticket ticket, Model model) {
		model.addAttribute("ticket", ticket);
		
		System.out.println(ticket.getFirstname() + " " + ticket.getLastname());
		System.out.println("awNote wurde gesendet am " + LocalDateTime.now());
		tLog.getInstance().log(null, "Info", "Trying to create a awNote");
		
		tickets.add(ticket);
//		return "create/home";
		return "redirect:";
	}
}
