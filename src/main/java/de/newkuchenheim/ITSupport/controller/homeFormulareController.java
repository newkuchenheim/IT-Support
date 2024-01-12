/**
 * Controller Class for view home.html
 */
package de.newkuchenheim.ITSupport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


/**
 * @author Sebastian Hansen
 *
 * @createdOn 19.09.2023
 * 
 */

@Controller
public class homeFormulareController {
	@ModelAttribute("page")
    String page() {
        return "formulare";
    }
	
	@GetMapping({"/formulare", "/formulare/"})
	String getHome(Model model) {
		return "formulare/home";
	}
	
}
