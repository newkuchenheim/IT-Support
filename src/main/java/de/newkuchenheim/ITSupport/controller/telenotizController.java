package de.newkuchenheim.ITSupport.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
public class telenotizController {
	//private static List<Ticket> tickets = new ArrayList();
	private boolean _sended = false;
	private final String _URL_CSVS  = System.getenv("USERPROFILE") + "\\IT-SupportContent\\Formulare\\General\\";//%USERPROFILE%/it-supportcontent/formulare/general
	private final String _URL_CSVS_LINUX  = System.getProperty("user.home") + "/IT-SupportContent/Formulare/General/";//home/itsupport/itsupport/it-supportcontent/formulare/general
	
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
		String _sys_path = null;
		
		if(System.getProperty("os.name").equals("Linux")) {
			_sys_path = _URL_CSVS_LINUX;
		} else if (System.getProperty("os.name").equals("Windows 10")) {
			_sys_path = _URL_CSVS;
		}
		ArrayList<HashMap<String, String>> telelist_kall = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> telelist_khm = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> telelist_uelp = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> telelist_zhm = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> telelist_zvw = new ArrayList<HashMap<String, String>>();
		if(_sys_path != null && !_sys_path.isBlank()) {
			// Load Kall csv
			String _csv_file = _sys_path + "kall_telelist.csv";
			telelist_kall = createArrayListFromCSV(_csv_file);
			// Load Kuchenheim csv
			_csv_file = _sys_path + "khm_telelist.csv";
			telelist_khm = createArrayListFromCSV(_csv_file);
			// Load Uelpenich csv
			_csv_file = _sys_path + "uelp_telelist.csv";
			telelist_uelp = createArrayListFromCSV(_csv_file);
			// Load Zingsheim csv
			_csv_file = _sys_path + "zhm_telelist.csv";
			telelist_zhm = createArrayListFromCSV(_csv_file);
			// Load Zentrale csv
			_csv_file = _sys_path + "zvw_telelist.csv";
			telelist_zvw = createArrayListFromCSV(_csv_file);
		}
		model.addAttribute("telelist_kall", telelist_kall);
		model.addAttribute("telelist_khm", telelist_khm);
		model.addAttribute("telelist_uelp", telelist_uelp);
		model.addAttribute("telelist_zhm", telelist_zhm);
		model.addAttribute("telelist_zvw", telelist_zvw);
		//tracking
		System.out.println("call a form telenotiz " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a telenotiz form");
		
		return "formulare/telenotiz/form";
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
			String email = "";
			String name;
			String prename;
			HashMap<String, String> person;
			while((currLine = reader.readLine()) != null) {
				person = new HashMap<String, String>();
				values = currLine.split(",");
				prename = values[1];
				name = values[0];
				if (values.length > 8) {
					name = values[1].replaceAll("\"", "") + " " + values[0].replaceAll("\"", "");
					name = name.trim();
					prename = values[2];
					email = values[8];
				} else if (values.length > 7) {
					if (values[7].contains("@")) {
						email = values[7];
					} else {
						name = values[1].replaceAll("\"", "") + " " + values[0].replaceAll("\"", "");
						name = name.trim();
						prename = values[2];
					}
				} else if (values[1].toLowerCase().contains("von")) {
					name = values[1].replaceAll("\"", "") + " " + values[0].replaceAll("\"", "");
					name = name.trim();
					prename = values[2];
				}
				person.put("Name", name);
				person.put("Vorname", prename);
				person.put("E-Mail", email);
				telelist.add(person);
				email = "";
			}
		}
		return telelist;
	}
}
