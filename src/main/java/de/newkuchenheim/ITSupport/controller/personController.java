package de.newkuchenheim.ITSupport.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.Person;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.dao.implement.personJobrouterDAO;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 06.05.2026
 * 
 */

@Controller
@RequestMapping("digidaten/personen")
public class personController {
	private static List<Person> Persons = new ArrayList<Person>();
	
	@GetMapping({"form", "form/"})
	public String renderCreateForm(@ModelAttribute Person Person, Model model) {
		Persons = personJobrouterDAO.getInstance().getDataSets();
		
		//tracking
		System.out.println("call a form personen " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a personen form");
		
		model.addAttribute("Person", new Person());
		model.addAttribute("Persons", Persons);
		model.addAttribute("page", "pl");
		return "digidaten/personen/form";
	}
}
