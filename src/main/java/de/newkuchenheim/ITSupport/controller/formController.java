/**
 * 
 */
package de.newkuchenheim.ITSupport.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.newkuchenheim.ITSupport.bdo.Ticket;

/**
 * @author Minh Tam Truong
 * 
 * @createOn 22.09.2022
 * 
 */

@Controller
@RequestMapping("create")
public class formController {

	private static List<Ticket> events = new ArrayList();
	
	@GetMapping
	public String displayAllEvents(Model model) {
		model.addAttribute("tickets", events);
		return "create/home";
	}
	
	@GetMapping("form")
	public String renderCreateForm(Model model) {
		model.addAttribute("ticket", new Ticket());
		return "create/form";
	}
	
//	@PostMapping("form")
//	public String createEvent(@RequestParam String vname,
//			@RequestParam String nname,
//			@RequestParam String werkstatt,
//			@RequestParam String abteilung,
//			@RequestParam String email,
//			@RequestParam String telfon,
//			@RequestParam String causetitle,
//			@RequestParam String causedescript) {
	@PostMapping("form")
	public String sendForm(@ModelAttribute Ticket ticket, Model model) {
		model.addAttribute("ticket", ticket);
		events.add(ticket);
		return "create/home";
	}
}
