/**
 * 
 */
package de.newkuchenheim.ITSupport.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
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
@RequestMapping("itsupport/wikifeedback")
public class wikifeedbackController {

	private static List<Ticket> tickets = new ArrayList();
	
	@ModelAttribute("page")
    String page() {
        return "wikifeedback";
    }
	
	@GetMapping
	public String displayAllEvents(Model model) {

		model.addAttribute("tickets", tickets);

		// send a request with ticket
		try {
			String answer = kanboardDAO.getInstance().sendWKFeedback(tickets.get(0));
			model.addAttribute("result", answer);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			tLog.getInstance().log(null, "severe", e.getMessage());
		}

		return "itsupport/wikifeedback/home";
	}

	@GetMapping("form")
	public String renderCreateForm(Model model) {

		tickets.clear();

		// tracking
		System.out.println("call a form wikifeedback" + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a form");

		model.addAttribute("ticket", new Ticket());
		return "itsupport/wikifeedback/form";
	}

	@PostMapping("form")
	public String sendForm(@ModelAttribute Ticket ticket, Model model) {
		model.addAttribute("ticket", ticket);

		System.out.println(ticket.getFirstname() + " " + ticket.getLastname());
		System.out.println("wikifeedback wurde gesendet am " + LocalDateTime.now());
		tLog.getInstance().log(null, "Info", "Trying to create a wikifeedback at " + LocalDateTime.now().toString());

		tickets.add(ticket);
//		return "create/home";
		return "redirect:";
	}
}
