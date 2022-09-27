/**
 * Controller Class for view home.html
 */
package de.newkuchenheim.ITSupport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Minh Tam Truong
 *
 * @createdOn 15.09.2022
 * 
 */

@Controller
public class homeController {
	
	@GetMapping("/")
	String getHome() {
		return "home";
	}
	
}
