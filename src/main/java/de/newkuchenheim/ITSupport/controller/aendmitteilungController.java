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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.CostCentre;
import de.newkuchenheim.ITSupport.bdo.TicketCategory;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.dao.implement.aendmitteilungJobrouterDAO;
import de.newkuchenheim.ITSupport.dao.implement.kostenstelleJobrouterDAO;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 01.03.2024
 * 
 */

@Controller
@RequestMapping("formulare/aendmitteilung")
public class aendmitteilungController extends abstractFormulareController {
	private String _formPage = "";
	@GetMapping({"", "/"})
	public String displayAllEvents(Model model) {
		model.addAttribute("formpage", _formPage);
		model.addAttribute("page", "aend_home");
		model.addAttribute("sended", _sended);
		_sended = false;
		_formPage = "";
		return "formulare/aendmitteilung/home";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of zuwendung
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"zuwendung", "zuwendung/"})
	public String renderCreateForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittdonation");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get the option lists
		List<TicketCategory> _optvouchers = new ArrayList<TicketCategory>();
		//List<TicketCategory> _optreasons = new ArrayList<TicketCategory>();
		List<TicketCategory> _optjubilar = new ArrayList<TicketCategory>();
		String _options_path = getFormPath("Aendmitteilung") + "options.json";
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
		        /*JSONArray _optreasons_json = _options_json.getJSONArray("optreasons");
		        _optreasons_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_optreasons.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });*/
		        JSONArray _optjubilar_json = _options_json.getJSONArray("optjubilar");
		        _optjubilar_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_optjubilar.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
			}
		}
		// get donation reason from jobrouter
		List<String> _optreasons = aendmitteilungJobrouterDAO.getInstance().getDataSets("1B0C492D-A103-3BA3-3A26-18413F6EA54B");
		model.addAttribute("optvouchers", _optvouchers);
		model.addAttribute("optreasons", _optreasons);
		model.addAttribute("optjubilar", _optjubilar);
		
		//tracking
		System.out.println("call a form aendmitteilungzuw " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungzuw form");
		
		return "formulare/aendmitteilung/zuwendung";
	}
	
	@PostMapping("zuwendung")
	public String sendAffectionForm(Model model) {
		_sended = true;
		_formPage = "donation";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of anschrift
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"anschrift", "anschrift/"})
	public String renderCreateAddressForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittaddr");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get housingtype from jobrouter
		/*JSONArray datasets = aendmitteilungJobrouterDAO.getInstance().getDataSets("C7AEDB5A-887D-DE03-6859-C826E296A41B");
		List<String> housingTypes = new ArrayList<>();
		if (datasets != null && !datasets.isEmpty()) {
			for (Object item : datasets) {
				housingTypes.add(((JSONObject)item).getString("Name"));
			}
		}
		model.addAttribute("housingTypes", housingTypes);*/
		List<TicketCategory> _opthousingtypes = new ArrayList<TicketCategory>();
		String _options_path = getFormPath("Aendmitteilung") + "options.json";
		if(_options_path != null && !_options_path.isBlank()) {
			Path _checkPath = Paths.get(_options_path);
			if(Files.exists(_checkPath)) {
				InputStream is = new FileInputStream(new File(_options_path));
				
		        JSONTokener tokener = new JSONTokener(is);
		        JSONObject _options_json = new JSONObject(tokener);
		        JSONArray _opthousingtypes_json = _options_json.getJSONArray("opthousingtypes");
		        _opthousingtypes_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_opthousingtypes.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
			}
		}
		model.addAttribute("opthousingtypes", _opthousingtypes);
		
		//tracking
		System.out.println("call a form aendmitteilungaddr " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungaddr form");
		
		return "formulare/aendmitteilung/anschrift";
	}
	
	@PostMapping("anschrift")
	public String sendAddressForm(Model model) {
		_sended = true;
		_formPage = "address";
		return "redirect:";
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of arbeitszeit
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"arbeitszeit", "arbeitszeit/"})
	public String renderCreateWorkingHoursForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittwork");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get working hours reason
		List<TicketCategory> _optreasons = new ArrayList<TicketCategory>();
		String _options_path = getFormPath("Aendmitteilung") + "options.json";
		if(_options_path != null && !_options_path.isBlank()) {
			Path _checkPath = Paths.get(_options_path);
			if(Files.exists(_checkPath)) {
				InputStream is = new FileInputStream(new File(_options_path));
				
		        JSONTokener tokener = new JSONTokener(is);
		        JSONObject _options_json = new JSONObject(tokener);
		        JSONArray _optreasons_json = _options_json.getJSONArray("optreasonswork");
		        _optreasons_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_optreasons.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
			}
		}
		
		// get lunch model from jobrouter
		List<String> _optlunchmodels = aendmitteilungJobrouterDAO.getInstance().getDataSets("FBFD504A-6BED-5E3B-DC4C-D6C4E3DFF7CC");
		model.addAttribute("optreasons", _optreasons);
		model.addAttribute("optlunchmodels", _optlunchmodels);
		
		//tracking
		System.out.println("call a form aendmitteilungwork " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungwork form");
		
		return "formulare/aendmitteilung/arbeitszeit";
	}
	
	@PostMapping("arbeitszeit")
	public String sendWorkingHoursForm(Model model) {
		_sended = true;
		_formPage = "WorkingHours";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of austritt
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////	
	
	@GetMapping({"austritt", "austritt/"})
	public String renderCreateWithdrawalForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittwithdrawal");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get lunch model from jobrouter
		List<String> _optlunchmodels = aendmitteilungJobrouterDAO.getInstance().getDataSets("FBFD504A-6BED-5E3B-DC4C-D6C4E3DFF7CC");
		model.addAttribute("optlunchmodels", _optlunchmodels);
		
		//tracking
		System.out.println("call a form aendmitteilungwithdrawal " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungwithdrawal form");
		
		return "formulare/aendmitteilung/austritt";
	}
	
	@PostMapping("austritt")
	public String sendWithdrawalForm(Model model) {
		_sended = true;
		_formPage = "withdrawal";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of bankverbindung
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////

	@GetMapping({"bankverbindung", "bankverbindung/"})
	public String renderCreateBankAccountForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittbankacc");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		//tracking
		System.out.println("call a form aendmitteilungbankacc " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungbankacc form");
		
		return "formulare/aendmitteilung/bankverbindung";
	}
	
	@PostMapping("bankverbindung")
	public String sendBankAccountForm(Model model) {
		_sended = true;
		_formPage = "BankAccount";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of lohn
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"lohn", "lohn/"})
	public String renderCreatePayForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittpay");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		//tracking
		System.out.println("call a form aendmitteilungpay " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungpay form");
		
		return "formulare/aendmitteilung/lohn";
	}
	
	@PostMapping("lohn")
	public String sendPayForm(Model model) {
		_sended = true;
		_formPage = "pay";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of mehrbedarf
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"mehrbedarf", "mehrbedarf/"})
	public String renderCreateAddRequireForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittaddrequire");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		//tracking
		System.out.println("call a form aendmitteilungaddrequire " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungaddrequire form");
		
		return "formulare/aendmitteilung/mehrbedarf";
	}
	
	@PostMapping("mehrbedarf")
	public String sendAddRequireForm(Model model) {
		_sended = true;
		_formPage = "addrequire";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of mittagessen
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////	
	
	@GetMapping({"mittagessen", "mittagessen/"})
	public String renderCreateLunchForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittlunch");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get lunch model from jobrouter
		List<String> _optlunchmodels = aendmitteilungJobrouterDAO.getInstance().getDataSets("FBFD504A-6BED-5E3B-DC4C-D6C4E3DFF7CC");
		model.addAttribute("optlunchmodels", _optlunchmodels);
		
		//tracking
		System.out.println("call a form aendmitteilunglunch " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilunglunch form");
		
		return "formulare/aendmitteilung/austritt";
	}
	
	@PostMapping("mittagessen")
	public String sendLunchForm(Model model) {
		_sended = true;
		_formPage = "lunch";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of sonstiges
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"sonstiges", "sonstiges/"})
	public String renderCreateOtherForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittother");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		//tracking
		System.out.println("call a form aendmitteilungother " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungother form");
		
		return "formulare/aendmitteilung/sonstiges";
	}
	
	@PostMapping("sonstiges")
	public String sendOtherForm(Model model) {
		_sended = true;
		_formPage = "other";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of fehlzeit_unbezahlt
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"fehlzeit_unbezahlt", "fehlzeit_unbezahlt/"})
	public String renderCreateUnpayTimeForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittunpaidtime");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		//tracking
		System.out.println("call a form aendmitteilungunpaidtime " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungunpaidtime form");
		
		return "formulare/aendmitteilung/fehlzeit_unbezahlt";
	}
	
	@PostMapping("fehlzeit_unbezahlt")
	public String sendUnpayTimeForm(Model model) {
		_sended = true;
		_formPage = "unpaidtime";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of urlaub_unbezahlt
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"urlaub_unbezahlt", "urlaub_unbezahlt/"})
	public String renderCreateUnpaidLeaveForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittunpaidleave");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get lunch model from jobrouter
		List<String> _optlunchmodels = aendmitteilungJobrouterDAO.getInstance().getDataSets("FBFD504A-6BED-5E3B-DC4C-D6C4E3DFF7CC");
		model.addAttribute("optlunchmodels", _optlunchmodels);
		
		//tracking
		System.out.println("call a form aendmitteilungunpaidleave " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungunpaidleave form");
		
		return "formulare/aendmitteilung/urlaub_unbezahlt";
	}
	
	@PostMapping("urlaub_unbezahlt")
	public String sendUnpaidLeaveForm(Model model) {
		_sended = true;
		_formPage = "unpaidleave";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of fehlen_unentschuldigt
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"fehlen_unentschuldigt", "fehlen_unentschuldigt/"})
	public String renderCreateTruantForm(Model model) throws IOException {
		model.addAttribute("page", "aendmitttruant");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		//tracking
		System.out.println("call a form aendmitteilungunpaytime " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungunpaytime form");
		
		return "formulare/aendmitteilung/fehlen_unentschuldigt";
	}
	
	@PostMapping("fehlen_unentschuldigt")
	public String sendTruantForm(Model model) {
		_sended = true;
		_formPage = "truant";
		return "redirect:";
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of wechsel_arbeitsbereich
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"wechsel_arbeitsbereich", "wechsel_arbeitsbereich/"})
	public String renderCreateChgInWorkGrpForm(Model model) throws IOException {
		model.addAttribute("page", "aendmitttchginworkgrp");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get cost unit
		List<TicketCategory> _optcostunits = new ArrayList<TicketCategory>();
		String _options_path = getFormPath("Aendmitteilung") + "options.json";
		if(_options_path != null && !_options_path.isBlank()) {
			Path _checkPath = Paths.get(_options_path);
			if(Files.exists(_checkPath)) {
				InputStream is = new FileInputStream(new File(_options_path));
				
		        JSONTokener tokener = new JSONTokener(is);
		        JSONObject _options_json = new JSONObject(tokener);
		        JSONArray _optcostunit_json = _options_json.getJSONArray("optcostunit");
		        _optcostunit_json.forEach(item -> {
		        	if(item instanceof JSONObject) {
		        		_optcostunits.add(new TicketCategory(((JSONObject) item).getString("value"), ((JSONObject) item).getString("text")));
		        	}
		        });
			}
		}
		// get cost centres from jobrouter
		List<CostCentre> CostCentres = kostenstelleJobrouterDAO.getInstance().getDataSets("33937D15-AC9A-A7CE-9B2D-0DC182D13FEB");
		// get lunch model from jobrouter
		List<String> _optlunchmodels = aendmitteilungJobrouterDAO.getInstance().getDataSets("FBFD504A-6BED-5E3B-DC4C-D6C4E3DFF7CC");
		model.addAttribute("optlunchmodels", _optlunchmodels);
		model.addAttribute("CostCentres", CostCentres);
		model.addAttribute("optcostunits", _optcostunits);
		
		//tracking
		System.out.println("call a form aendmitteilungchginworkgrp " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungchginworkgrp form");
		
		return "formulare/aendmitteilung/wechsel_arbeitsbereich";
	}
	
	@PostMapping("wechsel_arbeitsbereich")
	public String sendChgInWorkGrpForm(Model model) {
		_sended = true;
		_formPage = "chginworkgrp";
		return "redirect:";
	}
	

//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of wechsel_kostenstelle
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"wechsel_kostenstelle", "wechsel_kostenstelle/"})
	public String renderCreateChgCostCentreForm(Model model) throws IOException {
		model.addAttribute("page", "aendmitttchgcostcentre");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		List<CostCentre> CostCentres = kostenstelleJobrouterDAO.getInstance().getDataSets("33937D15-AC9A-A7CE-9B2D-0DC182D13FEB");
		model.addAttribute("CostCentres", CostCentres);
		
		//tracking
		System.out.println("call a form aendmitteilungchgcostcentre " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungchgcostcentre form");
		
		return "formulare/aendmitteilung/wechsel_kostenstelle";
	}
	
	@PostMapping("wechsel_kostenstelle")
	public String sendChgCostCentreForm(Model model) {
		_sended = true;
		_formPage = "chgcostcentre";
		return "redirect:";
	}
}
