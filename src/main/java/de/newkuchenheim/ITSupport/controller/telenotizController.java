package de.newkuchenheim.ITSupport.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
public class telenotizController extends abstractFormulareController {
	@GetMapping
	public String displayAllEvents(Model model) {
		model.addAttribute("page", "tele_home");
		model.addAttribute("sended", _sended);
		_sended = false;
		return "formulare/telenotiz/home";
	}
	
	@GetMapping("form")
	public String renderCreateForm(Model model) throws IOException {
		model.addAttribute("page", "telenotiz");
		// get telelist for each location
		addArrayListsFromCSV(model);
		//tracking
		System.out.println("call a form telenotiz " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a telenotiz form");
		
		return "formulare/telenotiz/form";
	}
}
