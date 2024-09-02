package de.newkuchenheim.ITSupport.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.bdo.daten.CostCentre;
import de.newkuchenheim.ITSupport.dao.implement.data.kostenstelleJobrouterDAO;

@Controller
@RequestMapping("formulare/bewirtungsantrag")
public class bewirtungsantragController extends abstractFormulareController {
	@GetMapping({"", "/"})
	public String displayAllEvents(Model model) {
		model.addAttribute("page", "bewirtung_home");
		model.addAttribute("sended", _sended);
		_sended = false;
		return "formulare/bewirtungsantrag/home";
	}
	
	@GetMapping({"form", "form/"})
	public String renderCreateForm(Model model) throws IOException {
		// get cost centres from jobrouter
		List<CostCentre> CostCentres = kostenstelleJobrouterDAO.getInstance().getDataSets("33937D15-AC9A-A7CE-9B2D-0DC182D13FEB");
		model.addAttribute("page", "bewirtungsantrag");
		model.addAttribute("CostCentres", CostCentres);
		//tracking
		System.out.println("call a form bewirtungsantrag " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a bewirtungsantrag form");
		
		return "formulare/bewirtungsantrag/form";
	}
}
