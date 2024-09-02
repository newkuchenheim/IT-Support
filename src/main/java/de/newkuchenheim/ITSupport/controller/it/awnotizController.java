/**
 * 
 */
package de.newkuchenheim.ITSupport.controller.it;

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

import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.bdo.itsupport.awNote;
import de.newkuchenheim.ITSupport.dao.implement.it.awsnotizKanboardDAO;
import de.newkuchenheim.ITSupport.dao.kanboard.kanboardDAO;

/**
 * @author Minh Tam Truong
 * 
 * @createOn 22.09.2022
 * 
 */

@Controller
@RequestMapping("itsupport/awnotiz")
public class awnotizController extends itsupportController{

	private static List<awNote> aws = new ArrayList();

	@GetMapping({"", "/"})
	public String displayAllEvents(Model model) {
		//configurate Navigation
		initNavigation(model, "awnotiz");
				
		model.addAttribute("aws", aws);

		//send a request with ticket
		try {
			int answer = awsnotizKanboardDAO.getInstance().sendTicket(aws.get(0));
			model.addAttribute("result", answer);
						
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			tLog.getInstance().log(null, "severe", e.getMessage());
		}
		
		return "itsupport/awnotiz/home";
	}
	
	@GetMapping({"form", "form/"})
	public String renderCreateForm(Model model) {
		//configurate Navigation
		initNavigation(model, "awnotiz");
				
		aws.clear();

		//tracking
		System.out.println("call a form aws " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a form aws");
		
		model.addAttribute("awNote", new awNote());
		return "itsupport/awnotiz/form";
	}
	
	@PostMapping("form")
	public String sendForm(@ModelAttribute awNote awNote ,Model model) {
		
		model.addAttribute("awNote", awNote);
		
		aws.add(awNote);

		return "redirect:";
	}
}
