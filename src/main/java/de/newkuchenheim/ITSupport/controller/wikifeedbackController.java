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
import de.newkuchenheim.ITSupport.dao.implement.wikifbKanboardDAO;

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
	
	@GetMapping({"", "/"})
	public String displayAllEvents(Model model) {

		model.addAttribute("tickets", tickets);

		// send a request with ticket
		try {
			int answer = wikifbKanboardDAO.getInstance().sendTicket(tickets.get(0));
			model.addAttribute("result", answer);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			tLog.getInstance().log(null, "severe", e.getMessage());
		}

		return "itsupport/wikifeedback/home";
	}

	@GetMapping({"form", "form/"})
	public String renderCreateForm(Model model) {

		tickets.clear();

		// tracking
		System.out.println("call a form wikifeedback" + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a form feedback");

		model.addAttribute("ticket", new Ticket());
		return "itsupport/wikifeedback/form";
	}

	@PostMapping("form")
	public String sendForm(@ModelAttribute Ticket ticket, Model model) {
		model.addAttribute("ticket", ticket);

		tickets.add(ticket);
		
		return "redirect:";
	}
}
