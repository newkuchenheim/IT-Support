package de.newkuchenheim.ITSupport.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import de.newkuchenheim.ITSupport.bdo.JRUploadFile;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.dao.implement.kostenstelleJobRouterDAO;

@Controller
@RequestMapping("/newdaten/fileupload")
public class fileuploadController {
	private static List<JRUploadFile> jrFiles = new ArrayList<JRUploadFile>();
	
	@GetMapping({"", "/"})
	public String displayAllEvents(Model model) {
		String fileId = "-1";
		if (jrFiles.size() > 0) {
			fileId = jrFiles.get(0).getId();
		}
		model.addAttribute("fileId", fileId);
		model.addAttribute("page", "fileHome");
		return "/newdaten/fileupload/home";
	}
	
	@GetMapping({"form", "form/"})
	public String renderCreateForm(Model model) {
		jrFiles.clear();
		model.addAttribute("page", "fileupload");
		model.addAttribute("uploadedFile", new JRUploadFile());
		//tracking
		System.out.println("call a form fileupload " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a fileupload form");
		
		return "/newdaten/fileupload/form";
	}
	
	@PostMapping("form")
	public String sendForm(@ModelAttribute JRUploadFile uploadedFile, Model model) {
		//if (uploadedFile != null && !uploadedFile.getSrcFile().isEmpty()) {
			try {
				Map<String, String> fileData = kostenstelleJobRouterDAO.getInstance().sendFile(uploadedFile.getSrcFile());
				if (fileData != null && fileData.size() > 0) {
					JRUploadFile jrFile = new JRUploadFile(fileData.get("id"), fileData.get("viewerURL"),
					fileData.get("location"), uploadedFile.getSrcFile().getBytes());
					jrFiles.add(jrFile);
				}
			} catch(IOException ex) {
				ex.printStackTrace();
				tLog.getInstance().log(ex, "serve", ex.getMessage());
				return null;
			}
		//}
		return "redirect:";
	}
}
