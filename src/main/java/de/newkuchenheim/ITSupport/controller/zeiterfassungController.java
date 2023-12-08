package de.newkuchenheim.ITSupport.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.bdo.TicketCategory;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 04.12.2023
 * 
 */

@Controller
@RequestMapping("formulare/zeiterfassung")
public class zeiterfassungController {
		private boolean _sended = false;
		private final String _URL_CSVS  = System.getenv("USERPROFILE") + "\\IT-SupportContent\\Formulare\\";//%USERPROFILE%/it-supportcontent/formulare/
		private final String _URL_CSVS_LINUX  = System.getProperty("user.home") + "/IT-SupportContent/Formulare/";//home/itsupport/itsupport/it-supportcontent/formulare/
		
		@GetMapping
		public String displayAllEvents(Model model) {
			model.addAttribute("page", "zeit_home");
			model.addAttribute("sended", _sended);
			_sended = false;
			return "formulare/zeiterfassung/home";
		}
		
		@GetMapping("form")
		public String renderCreateForm(Model model) throws IOException {
			model.addAttribute("page", "zeiterfassung");
			String _sys_path = null;
			String _path_sign = "\\";
			
			if(System.getProperty("os.name").equals("Linux")) {
				_sys_path = _URL_CSVS_LINUX;
				_path_sign = "/";
			} else if (System.getProperty("os.name").equals("Windows 10")) {
				_sys_path = _URL_CSVS;
			}
			// get telelist for each location
			ArrayList<HashMap<String, String>> telelist_kall = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> telelist_khm = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> telelist_uelp = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> telelist_zhm = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> telelist_zvw = new ArrayList<HashMap<String, String>>();
			if(_sys_path != null && !_sys_path.isBlank()) {
				// Load Kall csv
				String _csv_file = _sys_path + "General" + _path_sign + "kall_telelist.csv";
				telelist_kall = createArrayListFromCSV(_csv_file);
				// Load Kuchenheim csv
				_csv_file = _sys_path + "General" + _path_sign + "khm_telelist.csv";
				telelist_khm = createArrayListFromCSV(_csv_file);
				// Load Uelpenich csv
				_csv_file = _sys_path + "General" + _path_sign + "uelp_telelist.csv";
				telelist_uelp = createArrayListFromCSV(_csv_file);
				// Load Zingsheim csv
				_csv_file = _sys_path + "General" + _path_sign + "zhm_telelist.csv";
				telelist_zhm = createArrayListFromCSV(_csv_file);
				// Load Zentrale csv
				_csv_file = _sys_path + "General" + _path_sign + "zvw_telelist.csv";
				telelist_zvw = createArrayListFromCSV(_csv_file);
			}
			model.addAttribute("telelist_kall", telelist_kall);
			model.addAttribute("telelist_khm", telelist_khm);
			model.addAttribute("telelist_uelp", telelist_uelp);
			model.addAttribute("telelist_zhm", telelist_zhm);
			model.addAttribute("telelist_zvw", telelist_zvw);
			
			// get the option lists
			List<TicketCategory> _optrequests = new ArrayList<TicketCategory>();
			List<TicketCategory> _optreasons = new ArrayList<TicketCategory>();
			String _options_path = _sys_path + "Zeiterfassung" + _path_sign + "options.json";
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
		
		@PostMapping("form")
		public String sendForm(Model model) {
			_sended = true;
			return "redirect:";
		}
		
		private ArrayList<HashMap<String, String>> createArrayListFromCSV(String csv_file) throws IOException {
			ArrayList<HashMap<String, String>> telelist = new ArrayList<HashMap<String, String>>();
			Path _checkPath = Paths.get(csv_file);
			if(Files.exists(_checkPath)) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(csv_file))));;
				String currLine = reader.readLine(); // skip first line;
				String[] values;
				HashMap<String, String> person;
				while((currLine = reader.readLine()) != null) {
					person = new HashMap<String, String>();
					values = currLine.split(",", -1);
					person.put("Name", values[0]);
					person.put("Vorname", values[1]);
					person.put("Funktion", values[2]);
					person.put("E-Mail", values[7]);
					telelist.add(person);
				}
			}
			return telelist;
		}
}
