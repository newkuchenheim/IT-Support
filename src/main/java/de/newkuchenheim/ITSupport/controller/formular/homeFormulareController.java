/**
 * Controller Class for view home.html
 */
package de.newkuchenheim.ITSupport.controller.formular;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import de.newkuchenheim.ITSupport.bdo.FormChgLog;
import de.newkuchenheim.ITSupport.bdo.MainContent;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.dao.implement.formJobrouterDAO;


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
		List<FormChgLog> FormChgLogs = formJobrouterDAO.getInstance().getChangeLogs();
		model.addAttribute("contents", FormChgLogs);
		return "formulare/home";
	}
	
}
