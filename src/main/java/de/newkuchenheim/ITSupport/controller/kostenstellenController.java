package de.newkuchenheim.ITSupport.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.CostCentre;
import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.TicketCategory;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.dao.implement.kostenstelleJobrouterDAO;

@Controller
@RequestMapping("digidaten/kostenstellen")
public class kostenstellenController {
	private static List<CostCentre> CostCentres = new ArrayList<CostCentre>();
	private final String _URL_LOCATIONS  = System.getenv("USERPROFILE") + "\\IT-SupportContent\\Digidaten\\Kostenstellen\\locations.json";//"%USERPROFILE%/it-supportcontent/digidaten/kostenstellen/locations.json";
	private final String _URL_LOCATIONS_LINUX  = System.getProperty("user.home") + "/IT-SupportContent/Digidaten/Kostenstellen/locations.json";//"/home/itsupport/itsupport/it-supportcontent/digidaten/kostenstellen/locations.json";
	
	// home not needed
	/*@GetMapping({"", "/"})
	public String displayAllEvents(Model model) {
		model.addAttribute("page", "ks_home");
		model.addAttribute("CostCentres", CostCentres);
		return "/newdaten/kostenstellen/home";
	}*/
	
	@GetMapping({"form", "form/"})
	public String renderCreateForm(@ModelAttribute CostCentre CostCentre, Model model) {
		getAllCostCentres();
			
		//tracking
		System.out.println("call a form kostenstellen " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a kostenstellen form");
		//============== add new List for Location Select ===============
		List<TicketCategory> _locations = new ArrayList<TicketCategory>();
		String _sys_path = null;
		
		if(System.getProperty("os.name").equals("Linux")) {
			_sys_path = _URL_LOCATIONS_LINUX;
		} else if (System.getProperty("os.name").equals("Windows 10")) {
			_sys_path = _URL_LOCATIONS;
		}
		
		if(_sys_path != null && !_sys_path.isBlank()) {
			Path _checkPath = Paths.get(_sys_path);
			if(Files.exists(_checkPath)) {
				try {
					InputStream is = new FileInputStream(new File(_sys_path));
					
			        JSONTokener tokener = new JSONTokener(is);
			        JSONObject _ticketcats_json = new JSONObject(tokener);
			        JSONArray _micoscats_json = _ticketcats_json.getJSONArray("locations");
			        _micoscats_json.forEach(item -> {
			        	if(item instanceof JSONObject) {
			        		_locations.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
			        	}
			        });
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					tLog.getInstance().log(null, "severe", e.getMessage());
				}
			}
		}
		//===============================================================
		model.addAttribute("locations", _locations);
		model.addAttribute("CostCentre", new CostCentre());
		model.addAttribute("CostCentres", CostCentres);
		model.addAttribute("page", "ks");
		return "digidaten/kostenstellen/form";
	}
	
	private void getAllCostCentres() {
		// get datasets from table neuTestKostenstelle
		CostCentres = kostenstelleJobrouterDAO.getInstance().getDataSets("33937D15-AC9A-A7CE-9B2D-0DC182D13FEB");
	}
}
