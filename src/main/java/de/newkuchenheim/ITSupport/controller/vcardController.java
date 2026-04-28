package de.newkuchenheim.ITSupport.controller;

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
import de.newkuchenheim.ITSupport.bdo.Email;
import de.newkuchenheim.ITSupport.bdo.FormData;
import de.newkuchenheim.ITSupport.bdo.VCard;
import de.newkuchenheim.ITSupport.bdo.tLog;
import de.newkuchenheim.ITSupport.dao.implement.formDataJobrouterDAO;
import de.newkuchenheim.ITSupport.dao.implement.kostenstelleJobrouterDAO;
import de.newkuchenheim.ITSupport.mailConfig.emailConfiguration;
import de.newkuchenheim.ITSupport.mailConfig.emailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 27.04.2026
 * 
 */

@Controller
@RequestMapping("formulare/vcard")
public class vcardController {
	private static List<VCard> vcards = new ArrayList<>();
	protected boolean _sended = false;

	@GetMapping({ "", "/" })
	public String displayAllEvents(Model model) throws MessagingException {
		model.addAttribute("vcards", vcards);
		model.addAttribute("page", "vcard_home");
		model.addAttribute("sended", _sended);
		_sended = false;
		// Send VCard Data as Mail
		String body;
		VCard lastVCard = vcards.get(vcards.size() - 1);
		body = "Sehr geehrte Damen und Herren,\n\nAnbei die Informationen für die Vistenkarte/n:\n\n\t• Vorname / Name: %s %s\n\t• Abteilung: %s\n\t• Adresse: %s\n\t• Telefon: %s,"
				+ " Durchwahl: %s\n\t• Diensthandy: %s\n\t• Fax: %s\n\t• E-Mail: %s\n\t• Bestellmenge: %,d, Kostenstelle: %s\n\nMit freundlichen Grüßen,\n\n%s %s";
		body = String.format(body, lastVCard.getFirstname(), lastVCard.getLastname(), lastVCard.getBranch(),
				lastVCard.getAddress(), lastVCard.getPhone(), lastVCard.getDirect_dial(), lastVCard.getMobile(),
				lastVCard.getFax(), lastVCard.getEmail(), lastVCard.getCount(), lastVCard.getCostcentre(),
				lastVCard.getFirstname(), lastVCard.getLastname());
		Email mail = new Email();
		mail.setMsgBody(body);
		mail.setRecipient("druckvorstufe@new-eu.de");
		mail.setSubject("Visitenkarten Bestellung");
		
		emailConfiguration mailConfig = new emailConfiguration();
		
		Session session = Session.getInstance(mailConfig.getProperties(), mailConfig.getAuthenticator());
		
		emailUtil.getInstance().sendSimpleMail(session, mail, mailConfig.getFromMail());
		
		return "formulare/vcard/home";
	}

	@GetMapping({ "form", "form/" })
	public String renderCreateForm(Model model) {
		vcards.clear();

		model.addAttribute("page", "vcard");
		model.addAttribute("vcard", new VCard());

		// get form data lists from Jobrouter
		List<CostCentre> _CostCentres = kostenstelleJobrouterDAO.getInstance().getDataSets();
		List<FormData> _optaddresses = formDataJobrouterDAO.getInstance().getFormData("vc_addr", true);
		List<FormData> _optphones = formDataJobrouterDAO.getInstance().getFormData("vc_tel", true);
		model.addAttribute("CostCentres", _CostCentres);
		model.addAttribute("optphones", _optphones);
		model.addAttribute("optaddresses", _optaddresses);

		// tracking
		System.out.println("call a form vcard " + LocalDateTime.now());
		tLog.getInstance().log(null, "info", "call a vcard form");

		return "formulare/vcard/form";
	}

	@PostMapping("form")
	public String sendForm(@ModelAttribute VCard vcard, Model model) {
		model.addAttribute("vcard", vcard);
		vcards.add(vcard);
		_sended = true;
		return "redirect:";
	}
}
