package de.newkuchenheim.ITSupport.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 08.12.2023
 * 
 */

public abstract class generalFormulareController {
	protected boolean _sended = false;
	protected final String _URL_FORM_CONTENT  = System.getenv("USERPROFILE") + "\\IT-SupportContent\\Formulare\\";//%USERPROFILE%/it-supportcontent/formulare/
	protected final String _URL_FORM_CONTENT_LINUX = System.getProperty("user.home") + "/IT-SupportContent/Formulare/";//home/itsupport/itsupport/it-supportcontent/formulare/
	protected final String[] _CSV_FILES = { "kall_telelist.csv", "khm_telelist.csv", "uelp_telelist.csv", "zhm_telelist.csv", "zvw_telelist.csv"};
	abstract public String displayAllEvents(Model model);
	abstract public String renderCreateForm(Model model) throws IOException;
	
	@PostMapping("form")
	public String sendForm(Model model) {
		_sended = true;
		return "redirect:";
	}
	/**
	 * create arraylist for each location and add them to given model
	 * @param model Model
	 * @return 
	 * @throws IOException
	 */
	protected void addArrayListsFromCSV(Model model) throws IOException {
		// Get General path
		String _general_path = getFormPath("General");
		ArrayList<HashMap<String, String>> telelist = null;
		// create telelist for each location
		for (int i = 0; i < _CSV_FILES.length; i++) {
			telelist = new ArrayList<HashMap<String, String>>();
			if(_general_path != null && !_general_path.isBlank()) {
				Path _checkPath = Paths.get(_general_path + _CSV_FILES[i]);
				if(Files.exists(_checkPath)) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(_general_path + _CSV_FILES[i]))));;
					String currLine = reader.readLine(); // header;
					String[] header = currLine.split(",");
					String[] values;
					HashMap<String, String> person;
					while((currLine = reader.readLine()) != null) {
						person = new HashMap<String, String>();
						values = currLine.split(",", -1);
						for (int j = 0; j < header.length; j++) {
							person.put(header[j], values[j]);
						}
						telelist.add(person);
						
					}
				}
			}
			model.addAttribute(_CSV_FILES[i].substring(0, _CSV_FILES[i].indexOf('.')), telelist);
		}
	}
	/**
	 * create path to formulare content and given subdirectory
	 * @param subdir String
	 * @return formulare/subdir path String
	 * @throws 
	 */
	protected String getFormPath(String subdir) {
		String _form_path = null;
		if(System.getProperty("os.name").equals("Linux")) {
			_form_path = _URL_FORM_CONTENT_LINUX + "/" + subdir + "/";
		} else if(System.getProperty("os.name").equals("Windows 10")) {
			_form_path = _URL_FORM_CONTENT + "\\" + subdir + "\\";
		}
		return _form_path;
	}
}
