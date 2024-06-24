package de.newkuchenheim.ITSupport.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.newkuchenheim.ITSupport.bdo.CostCentre;
import de.newkuchenheim.ITSupport.bdo.FormData;
import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.dao.implement.aendmittJobrouterDAO;
import de.newkuchenheim.ITSupport.dao.implement.aendmittKanboardDAO;
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

	// add global model attribute
	@ModelAttribute("optlocations")
	public List<FormData> getLocations() {
		return aendmittJobrouterDAO.getInstance().getFormData("sto", true);
	}
	
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
		List<FormData> opthousingtypes = aendmittJobrouterDAO.getInstance().getFormData("adr", false);
		model.addAttribute("opthousingtypes", opthousingtypes);
		
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
		
		// get working hours reason and lunchmodel
		List<FormData> _optreasons = aendmittJobrouterDAO.getInstance().getFormData("az", false);
		List<FormData> _optlunchmodels = aendmittJobrouterDAO.getInstance().getFormData("zm", false);
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
		List<FormData> optlunchmodels = aendmittJobrouterDAO.getInstance().getFormData("zm", false);
		model.addAttribute("optlunchmodels", optlunchmodels);
		
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
* Controller of fahrtendienst
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"fahrtendienst", "fahrtendienst/"})
	public String renderCreateShuttletForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittshuttle");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get all data from jobrouter for shuttle form
		List<FormData> _optshuttledata = aendmittJobrouterDAO.getInstance().getFormData("fd", false);
		// get only driver types
		List<FormData> _optdrivertypes = new ArrayList<>(_optshuttledata);
		_optdrivertypes.removeIf(s -> !s.getModule().equals("Selbstfahrer"));
		// get only reasons
		List<FormData> _optwithdrawdriverreasons = new ArrayList<>(_optshuttledata);
		_optwithdrawdriverreasons.removeIf(s -> !s.getModule().equals("Grund"));
		// get only buslines
		List<FormData> _optbuslines = new ArrayList<>(_optshuttledata);
		_optbuslines.removeIf(s -> !s.getModule().equals("Linie"));
		model.addAttribute("optbuslines", _optbuslines);
		model.addAttribute("optwithdrawdriverreasons", _optwithdrawdriverreasons);
		model.addAttribute("optdrivertypes", _optdrivertypes);
			
		
		//tracking
		System.out.println("call a form aendmitteilungshuttle " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungshuttle form");
		
		return "formulare/aendmitteilung/fahrtendienst";
	}
	
	@PostMapping("fahrtendienst")
	public String sendShuttleForm(Model model) {
		_sended = true;
		_formPage = "shuttle";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of krankheit
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"krankheit", "krankheit/"})
	public String renderIllnessForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittillness");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get lunch model from jobrouter
		List<FormData> _optlunchmodels = aendmittJobrouterDAO.getInstance().getFormData("zm", false);
		model.addAttribute("optlunchmodels", _optlunchmodels);
		
		//tracking
		System.out.println("call a form aendmitteilungillness " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungillness form");
		
		return "formulare/aendmitteilung/krankheit";
	}
	
	@PostMapping("krankheit")
	public String sendIllnessForm(Model model) {
		_sended = true;
		_formPage = "illness";
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
		
		// get case groups from jobrouter
		List<FormData> _optcasegroups = aendmittJobrouterDAO.getInstance().getFormData("mbf", false);
		model.addAttribute("optcasegroups", _optcasegroups);
		
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
		List<FormData> _optlunchmodels = aendmittJobrouterDAO.getInstance().getFormData("zm", false);
		model.addAttribute("optlunchmodels", _optlunchmodels);
		
		//tracking
		System.out.println("call a form aendmitteilunglunch " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilunglunch form");
		
		return "formulare/aendmitteilung/mittagessen";
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
		List<FormData> _optlunchmodels = aendmittJobrouterDAO.getInstance().getFormData("zm", false);
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
		model.addAttribute("page", "aendmittchginworkgrp");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		// get cost unit
		List<FormData> _optcostunits = aendmittJobrouterDAO.getInstance().getFormData("wab", false);
		// get cost centres from jobrouter
		List<CostCentre> CostCentres = kostenstelleJobrouterDAO.getInstance().getDataSets("33937D15-AC9A-A7CE-9B2D-0DC182D13FEB");
		// get lunch model from jobrouter
		List<FormData> _optlunchmodels = aendmittJobrouterDAO.getInstance().getFormData("zm", false);
		// Remove text only values
		_optlunchmodels.removeIf(s -> (!Character.isDigit(s.getText().charAt(0))));
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
		model.addAttribute("page", "aendmittchgcostcentre");
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
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of wechsel_zweigstelle
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"wechsel_zweigstelle", "wechsel_zweigstelle/"})
	public String renderCreateChgLocationForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittchglocation");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		List<CostCentre> CostCentres = kostenstelleJobrouterDAO.getInstance().getDataSets("33937D15-AC9A-A7CE-9B2D-0DC182D13FEB");
		model.addAttribute("CostCentres", CostCentres);
		// get lunch model from jobrouter
		List<FormData> _optlunchmodels = aendmittJobrouterDAO.getInstance().getFormData("zm", false);
		model.addAttribute("optlunchmodels", _optlunchmodels);
		
		//tracking
		System.out.println("call a form aendmitteilungchglocation " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungchglocation form");
		
		return "formulare/aendmitteilung/wechsel_zweigstelle";
	}
	
	@PostMapping("wechsel_zweigstelle")
	public String sendChgLocationForm(Model model) {
		_sended = true;
		_formPage = "chglocation";
		return "redirect:";
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
		
		// get all data from jobrouter for voucher form
		List<FormData> _optdonationdata = aendmittJobrouterDAO.getInstance().getFormData("zw", false);
		// get only jubilar
		List<FormData> _optjubilar = new ArrayList<>(_optdonationdata);
		_optjubilar.removeIf(s -> !s.getModule().equals("Jubiläum"));
		// get only reasons
		List<FormData> _optreasons = new ArrayList<>(_optdonationdata);
		_optreasons.removeIf(s -> !s.getModule().equals("Grund"));
		// get only vouchers
		List<FormData> _optvouchers = new ArrayList<>(_optdonationdata);
		_optvouchers.removeIf(s -> !s.getModule().equals("Gutschein"));
		model.addAttribute("optvouchers", _optvouchers);
		model.addAttribute("optreasons", _optreasons);
		model.addAttribute("optjubilar", _optjubilar);
		
		//tracking
		System.out.println("call a form aendmitteilungdonation " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungdonation form");
		
		return "formulare/aendmitteilung/zuwendung";
	}
	
	@PostMapping("zuwendung")
	public String sendDonationForm(Model model) {
		_sended = true;
		_formPage = "donation";
		return "redirect:";
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
* Controller of feedback
* Post & GetMapping
*/
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"feedback", "feedback/"})
	public String renderCreateFeedbackForm(Model model) throws IOException {
		model.addAttribute("page", "aendmittfeedback");
		// get telelist for each location
		addArrayListsFromCSV(model);
		
		model.addAttribute("ticket", new Ticket());
		
		//tracking
		System.out.println("call a form aendmitteilungfeedback " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a aendmitteilungfeedback form");
		
		return "formulare/aendmitteilung/feedback";
	}
	
	@PostMapping("feedback")
	public String sendFeedbackForm(@ModelAttribute Ticket ticket, Model model) throws UnsupportedEncodingException {
		_formPage = "feedback";
		_sended = aendmittKanboardDAO.getInstance().updateFeedbackTicket(ticket);
		model.addAttribute("ticket", ticket);
		return "redirect:";
	}
}
