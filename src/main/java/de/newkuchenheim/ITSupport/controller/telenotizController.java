package de.newkuchenheim.ITSupport.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.tLog;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 19.09.2023
 * 
 */

@Controller
@RequestMapping("formulare/telenotiz")
public class telenotizController {
	//private static List<Ticket> tickets = new ArrayList();
	@ModelAttribute("page")
    String page() {
        return "telenotiz";
    }
	
	@GetMapping
	public String displayAllEvents(Model model) {
		return "formulare/telenotiz/form";
	}
	
	@GetMapping("form")
	public String renderCreateForm(Model model) {
		//tracking
		System.out.println("call a form telenotiz " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a telenotiz form");
		
		return "formulare/telenotiz/form";
	}
	
	@PostMapping("form")
	public void sendForm() {
	}
}
