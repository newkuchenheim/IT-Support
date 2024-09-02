package de.newkuchenheim.ITSupport.controller.formular;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.bdo.itsupport.TicketCategory;
import de.newkuchenheim.ITSupport.controller.abstractFormulareController;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 04.12.2023
 * 
 */

@Controller
@RequestMapping("formulare/zeiterfassung")
public class zeiterfassungController extends abstractFormulareController {	
		@GetMapping({"", "/"})
		public String displayAllEvents(Model model) {
			model.addAttribute("page", "zeit_home");
			model.addAttribute("sended", _sended);
			_sended = false;
			return "formulare/zeiterfassung/home";
		}
		
		@GetMapping({"form", "form/"})
		public String renderCreateForm(Model model) throws IOException {
			model.addAttribute("page", "zeiterfassung");
			// get telelist for each location
			addArrayListsFromCSV(model);
			
			// get the option lists
			List<TicketCategory> _optrequests = new ArrayList<TicketCategory>();
			List<TicketCategory> _optreasons = new ArrayList<TicketCategory>();
			String _options_path = getFormPath("Zeiterfassung") + "options.json";
			if(_options_path != null && !_options_path.isBlank()) {
				Path _checkPath = Paths.get(_options_path);
				if(Files.exists(_checkPath)) {
					InputStream is = new FileInputStream(new File(_options_path));
					
			        JSONTokener tokener = new JSONTokener(is);
			        JSONObject _options_json = new JSONObject(tokener);
			        JSONArray _optrequests_json = _options_json.getJSONArray("optrequests");
			        _optrequests_json.forEach(item -> {
			        	if(item instanceof JSONObject) {
			        		_optrequests.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
			        	}
			        });
			        JSONArray _optreasons_json = _options_json.getJSONArray("optreasons");
			        _optreasons_json.forEach(item -> {
			        	if(item instanceof JSONObject) {
			        		_optreasons.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
			        	}
			        });
				}
			}
			model.addAttribute("optrequests", _optrequests);
			model.addAttribute("optreasons", _optreasons);
			
			//tracking
			System.out.println("call a form zeiterfassung " + LocalDateTime.now());
			tLog.getInstance().log(null, "info", "call a zeiterfassung form");
			
			return "formulare/zeiterfassung/form";
		}

		/**
		 * get mapping hilfstool 
		 */
		@GetMapping({"hilfstool", "hilfstool/"})
		public String renderCreateHilfstool(Model model) throws IOException {
			model.addAttribute("page", "hilfstool");
			//tracking
			System.out.println("call a zeiterfassung-hilfstool " + LocalDateTime.now());
			tLog.getInstance().log(null, "info", "call a zeiterfassung-hilfstool");
			
			return "formulare/zeiterfassung/hilfstool";
		}
}
