package de.newkuchenheim.ITSupport.controller.it;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import de.newkuchenheim.ITSupport.bdo.Page;
import de.newkuchenheim.ITSupport.bdo.template.htmlElement;

@Controller
public abstract class itsupportController {
	
	//Element for Navigation

	protected List<htmlElement> navi_items = new ArrayList<>();
	//protected EmailServiceImpl mailInstance = EmailServiceImpl.getInstance();
	
	protected itsupportController() {
		
		htmlElement _TICKET_L1 = new htmlElement("itsupport", "a", "Support-Ticket", null, null, true, false, new ArrayList<htmlElement>(), null);
		navi_items.add(_TICKET_L1);
		
		Map<String, String> _TICKET_FORM_ATT = new HashMap<>();
		_TICKET_FORM_ATT.put("href", Page.TICKET_PAGE.getRelativURL());
		htmlElement _TICKET_FORM_L1_1 = new htmlElement(Page.TICKET_PAGE.getName_page(), "a", Page.TICKET_PAGE.getTitle(), null, _TICKET_L1.text()+_TICKET_L1.hashCode(), false, true, null,_TICKET_FORM_ATT);
		navi_items.add(_TICKET_FORM_L1_1);
		
		Map<String, String> _TICKET_TRACKING_ATT = new HashMap<>();
		_TICKET_TRACKING_ATT.put("href", Page.TICKET_TRACKING_PAGE.getRelativURL());
		htmlElement _TICKET_TRACKING_L1_2 = new htmlElement(Page.TICKET_TRACKING_PAGE.getName_page(),"a", Page.TICKET_TRACKING_PAGE.getTitle(), null, _TICKET_L1.text()+_TICKET_L1.hashCode(), false, true, null, _TICKET_TRACKING_ATT);
		navi_items.add(_TICKET_TRACKING_L1_2);
		
		//add childnode into URL_LIST
		List<htmlElement> list = new ArrayList<>();
		list.add(_TICKET_FORM_L1_1);
		list.add(_TICKET_TRACKING_L1_2);
		_TICKET_L1.setChildren(list);		
		
		Map<String, String> _AWS_FORM_ATT = new HashMap<>();
		_AWS_FORM_ATT.put("href", Page.AW_NOTIZ_PAGE.getRelativURL());
		htmlElement _AWS_L1 = new htmlElement(Page.AW_NOTIZ_PAGE.getName_page(),"a", Page.AW_NOTIZ_PAGE.getTitle(), null, null, false, false, null, _AWS_FORM_ATT);
		navi_items.add(_AWS_L1);
		
		Map<String, String> _WIKIFB_FORM_ATT = new HashMap<>();
		_WIKIFB_FORM_ATT.put("href", Page.WIKI_FEEDBACK_PAGE.getRelativURL());
		htmlElement _WIKI_FEEDBACK_L1 = new htmlElement(Page.WIKI_FEEDBACK_PAGE.getName_page(), "a", Page.WIKI_FEEDBACK_PAGE.getTitle(), null, null, false, false, null, _WIKIFB_FORM_ATT);
		navi_items.add(_WIKI_FEEDBACK_L1);
		
	}
	
	protected void initNavigation(Model model, String page) {
		if(navi_items != null && !navi_items.isEmpty()) {
			
			model.addAttribute("nav_elems", navi_items);
			model.addAttribute("page", page);
			
			navi_items.forEach(elem -> {
				if(elem.page_name().equals(page)) {
					if(elem.isChildren()) {
						model.addAttribute("show_collapse", true);
					}
				}
			});
		}
	}
}
