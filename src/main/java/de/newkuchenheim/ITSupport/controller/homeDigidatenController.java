package de.newkuchenheim.ITSupport.controller;

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

import de.newkuchenheim.ITSupport.bdo.MainContent;
import de.newkuchenheim.ITSupport.bdo.tLog;

@Controller
public class homeDigidatenController {
	private final String _URL_MESSAGES  = System.getenv("USERPROFILE") + "\\IT-SupportContent\\Digidaten\\messages.json";//"%USERPROFILE%/it-supportcontent/digidaten/messages.json";
	private final String _URL_MESSAGES_LINUX  = System.getProperty("user.home") + "/IT-SupportContent/Digidaten/messages.json";//"/home/itsupport/itsupport/it-supportcontent/digidaten/messages.json";
	
	private static List<MainContent> contents = new ArrayList<MainContent>();
	
	@ModelAttribute("page")
    String page() {
        return "digidaten";
    }
	
	@GetMapping({"/digidaten", "/digidaten/"})
	public String getHome(Model model) {
		contents.clear();
		
		String _sys_path = null;
		
		if(System.getProperty("os.name").equals("Linux")) {
			_sys_path = _URL_MESSAGES_LINUX;
		} else if (System.getProperty("os.name").equals("Windows 10")) {
			_sys_path = _URL_MESSAGES;
		}
		
		if(_sys_path != null && !_sys_path.isBlank()) {
			Path _checkPath = Paths.get(_sys_path);
			if(Files.exists(_checkPath)) {
				try {
				InputStream is = new FileInputStream(new File(_sys_path));
				
		        JSONTokener tokener = new JSONTokener(is);
		        JSONObject _content_json = new JSONObject(tokener);
		        JSONArray _arrays_json = _content_json.getJSONArray("contents");
		        _arrays_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		MainContent cont = new MainContent();
		        		cont.setType(((JSONObject) item).getString("type").toLowerCase());
		        		cont.setTitle(((JSONObject) item).getString("title"));
		        		cont.setDescription(((JSONObject) item).getString("description"));
		        		cont.setWrittenBy(((JSONObject) item).getString("writtenBy"));
		        		// set day for message. if day time not exists then will be set with LocalDateTime.now()
		        		if(((JSONObject) item).has("writtenOn")){
		        			cont.setWrittenOn(LocalDateTime.parse(((JSONObject) item).getString("writtenOn")));
		        		} else {
		        			cont.setWrittenOn(LocalDateTime.now());
		        		}
		        		
		        		contents.add(cont);
		        		
		        		//sort message with time
		        		contents = contents.stream()
		        				.sorted(Comparator.comparingLong(o -> ((MainContent) o).getWrittenOn()
		        														.toEpochSecond(ZoneOffset.UTC))
		        														.reversed())
		        				.collect(Collectors.toList());
		        	}
		        });
		        tLog.getInstance().log(null, "Info", "Start Site - Read main contents. Counts: " + _arrays_json.length());
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
					tLog.getInstance().log(ex, "severe", ex.getMessage());
				}
			} else {
				MainContent cont = new MainContent();
	    		cont.setType("info");
	    		cont.setTitle("Willkommen zu Digi-Daten");
	    		cont.setDescription("Derzeits gibt es keine neue Informationen!");
	    		cont.setWrittenBy("System");
	    		cont.setWrittenOn(LocalDateTime.now());
	    		
				contents.add(cont);
				
				tLog.getInstance().log(null, "Info", "Start Site - Content not found!");
			}
		} else {
			MainContent cont = new MainContent();
    		cont.setType("info");
    		cont.setTitle("Willkommen zu Digi-Daten");
    		cont.setDescription("Derzeit gibt es keine neue Informationen!");
    		cont.setWrittenBy("System");
    		cont.setWrittenOn(LocalDateTime.now());
    		
			contents.add(cont);
			
			tLog.getInstance().log(null, "Info", "Start Site - Content not found!");
		}
		
		model.addAttribute("messages", contents);
		return "/digidaten/home";
	}
}
