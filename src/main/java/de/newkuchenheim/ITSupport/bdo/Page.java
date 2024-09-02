package de.newkuchenheim.ITSupport.bdo;

/**
 * Indexing all pages (from root) of application
 */
public enum Page {
	
	//itsupport
	//home
	HOME_PAGE("itsupport", "IT-Support", "192.168.0.224:8080/itsupport/", "/itsupport}"),
	//ticket
	TICKET_PAGE("ticket", "Support-Ticket an IT senden", "192.168.0.224:8080/itsupport/ticket/form", "/itsupport/ticket/form"),
	TICKET_TRACKING_PAGE("ticket_tracking", "Ticket-Tracking", "192.168.0.224:8080/itsupport/ticket/ticket_tracking", "/itsupport/ticket/ticket_tracking"),
	//awnotiz
	AW_NOTIZ_PAGE("awnotiz", "Abwesenheitnotiz (AWS)", "192.168.0.224:8080/itsupport/awnotiz/form", "/itsupport/awnotiz/form"),
	WIKI_FEEDBACK_PAGE("wikifeedback", "WIKI-Feedback", "192.168.0.224:8080/itsupport/wikifeedback/form", "/itsupport/wikifeedback/form");
	
	//formulare
	// TODO: bitte param von DigiForm Page hinzufügen
	
	//digidaten
	// TODO: bitte param von DigiDaten Page hinzufügen
	
	private String name_page;
	private String display_name;
	private String absolutURL;
	private String relativURL;
	
	
	Page(String name_page, String title, String URL, String relativURL){
		this.setName_page(name_page);
		this.setDisplay_name(title);
		this.setAbsolutURL(URL);
		this.setRelativURL(relativURL);
	}


	public String getTitle() {
		return display_name;
	}


	public void setDisplay_name(String title) {
		this.display_name = title;
	}


	public String getRelativURL() {
		return relativURL;
	}


	public void setRelativURL(String relativURL) {
		this.relativURL = relativURL;
	}


	public String getName_page() {
		return name_page;
	}


	public void setName_page(String name_page) {
		this.name_page = name_page;
	}


	public String getAbsolutURL() {
		return absolutURL;
	}


	public void setAbsolutURL(String absolutURL) {
		this.absolutURL = absolutURL;
	}
}
