package de.newkuchenheim.ITSupport.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.TicketCategory;
import de.newkuchenheim.ITSupport.bdo.tLog;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 01.03.2024
 * 
 */

@Controller
@RequestMapping("formulare/aendmitteilungzuw")
public class aendmitteilungzuwController extends abstractFormulareController {
	@GetMapping({"", "/"})
	public String displayAllEvents(Model model) {
		model.addAttribute("page", "aend_home");
		model.addAttribute("sended", _sended);
		_sended = false;
		return "formulare/aendmitteilungzuw/home";
	}
	
	@GetMapping({"form", "form/"})
	public String renderCreateForm(Model model) throws IOException {
		model.addAttribute("page", "aendmitteilung");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get the option lists
		List<TicketCategory> _optvouchers = new ArrayList<TicketCategory>();
		List<TicketCategory> _optreasons = new ArrayList<TicketCategory>();
		List<TicketCategory> _optjubilar = new ArrayList<TicketCategory>();
		String _options_path = getFormPath("Aendmitteilungzuw") + "options.json";
		if(_options_path != null && !_options_path.isBlank()) {
			Path _checkPath = Paths.get(_options_path);
			if(Files.exists(_checkPath)) {
				InputStream is = new FileInputStream(new File(_options_path));
				
		        JSONTokener tokener = new JSONTokener(is);
		        JSONObject _options_json = new JSONObject(tokener);
		        JSONArray _optvouchers_json = _options_json.getJSONArray("optvouchers");
		        _optvouchers_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_optvouchers.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
		        JSONArray _optreasons_json = _options_json.getJSONArray("optreasons");
		        _optreasons_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_optreasons.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
		        JSONArray _optjubilar_json = _options_json.getJSONArray("optjubilar");
		        _optjubilar_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_optjubilar.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
			}
		}
		model.addAttribute("optvouchers", _optvouchers);
		model.addAttribute("optreasons", _optreasons);
		model.addAttribute("optjubilar", _optjubilar);
		
		//tracking
		System.out.println("call a form aendmitteilungzuw " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungzuw form");
		
		return "formulare/aendmitteilungzuw/form";
	}
}
