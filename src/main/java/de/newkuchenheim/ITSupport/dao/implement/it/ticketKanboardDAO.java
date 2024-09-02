package de.newkuchenheim.ITSupport.dao.implement.it;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

import org.json.JSONObject;

import de.newkuchenheim.ITSupport.bdo.Email;
import de.newkuchenheim.ITSupport.bdo.itsupport.Ticket;
import de.newkuchenheim.ITSupport.bdo.kanboardConfig.TaskFileKanboardConfigutration;
import de.newkuchenheim.ITSupport.bdo.kanboardConfig.TaskKanboardConfiguration;
import de.newkuchenheim.ITSupport.bdo.mailConfig.emailConfiguration;
import de.newkuchenheim.ITSupport.bdo.mailConfig.emailUtil;
import de.newkuchenheim.ITSupport.dao.kanboard.kanboardDAO;
import de.newkuchenheim.ITSupport.dao.kanboard.kanboardFileInterface;
import de.newkuchenheim.ITSupport.dao.kanboard.kanboardTaskInterface;
import jakarta.mail.Session;

public class ticketKanboardDAO extends kanboardDAO implements kanboardTaskInterface<Ticket>, kanboardFileInterface<Ticket> {

	private static ticketKanboardDAO instance;
	//Mail Configuration
	private emailConfiguration emailConfig = new emailConfiguration();
	private emailUtil emailUtilitiy = emailUtil.getInstance();
	
	
	/**
	 * Get Instance kanboardDAO
	 * 
	 * @return kanboardDAO
	 */
	public static ticketKanboardDAO getInstance() {
		if (instance == null) {
			return new ticketKanboardDAO();
		}
		return instance;
	}

	/**
	 * send Ticket to Kanboard. If response is instance of Integer, then return result als ID of the new created Ticket. Otherwise -1.
	 * 
	 * @return ID int ID of the new ticket, otherwise -1.
	 */
	@Override
	public int sendTicket(Ticket ticket) throws UnsupportedEncodingException {
		if (ticket != null) {
			
			//Generate den Title
			String title = "[" + ticket.getBuilding() + "]-[" + ticket.getCategory();
			// add micos detail or user detail category if set
			if (ticket.getMicoscat() != null && !ticket.getMicoscat().isBlank()) { 
					title += " - " + ticket.getMicoscat();
			} else if (ticket.getUsercat() != null && !ticket.getUsercat().isBlank()) {
					title += " - " + ticket.getUsercat();
			}
			title += "]-[" + ticket.getLastname() + "]";
				
			String description = ticket.getDescription();
			String new_desc = "";
			if (description != null && !description.isBlank() && description.contains(System.lineSeparator())) {
				//String[] _str_split = description.split(System.lineSeparator());
				String[] _str_split = description.split("\\R");
				for (String s : _str_split) {
					new_desc = new_desc + s + _NEWLINE;
				}
				description = new_desc;
			}

			String descript = _HEADING_1_OPEN + ticket.getCategory() + _HEADING_1_CLOSE + _HEADING_3_OPEN + "Kontakt"
					+ _HEADING_3_CLOSE + _TAB_UL_1 + _BOLD_OPEN + "Name:" + _BOLD_CLOSE + ticket.getFirstname() + " "
					+ ticket.getLastname() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Tel: " + _BOLD_CLOSE
					+ ticket.getTelefon() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Email: " + _BOLD_CLOSE
					+ ticket.getEmail() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Werkstatt: " + _BOLD_CLOSE
					+ ticket.getBuilding() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Abteilung: " + _BOLD_CLOSE
					+ ticket.getBranch() + _NEWLINE + _NEWLINE + _HEADING_3_OPEN + "TICKET" + _HEADING_3_CLOSE
					+ _TAB_UL_1 + _BOLD_OPEN + "Grund: " + _BOLD_CLOSE + ticket.getCategory() + _NEWLINE + _TAB_UL_1
					+ _BOLD_OPEN + "Beschreibung: " + _BOLD_CLOSE + description + _NEWLINE
					+ _HEADING_3_OPEN + "IT-LOG" + _HEADING_3_CLOSE + _TAB_UL_1 
					+ "@system_user " + LocalDateTime.now().toString() + " Ticket eingegangen";
			
			TaskKanboardConfiguration task = TaskKanboardConfiguration.CREATE_TASK;
			task.setParameterValue("project_id", 1);
			task.setParameterValue("creator_id", 18);
			task.setParameterValue("swimmland_id", 57);
			task.setParameterValue("color_id", "cyan");
			task.setParameterValue("column_id", 1);
			task.setParameterValue("title", title);
			task.setParameterValue("description", descript);

			//System.out.println(task.getParams());
			//System.out.println(task.buildRequest());
			
			Object result = sendTaskRequest(task);
			if(result instanceof Integer) {
				if(ticket.getEmail() != null && !ticket.getEmail().isBlank()) {
					Session session = Session.getDefaultInstance(this.emailConfig.getProperties(), this.emailConfig.getAuthenticator());
					
					Email mail = new Email();
					mail.setSubject("Support-Ticket #" + result + " wurde gesendet");
					mail.setMsgBody("Vielen Dank für Ihre Nutzung unseres Ticketsystems! \n Wir werden schnellsmöglich Ihr Ticket betreuen. "
							+ "Sie können Bearbeitungszutand des Ticketd verfolgen, indem Sie den unteren Link anwenden. \n"
							+ "\t* Ticket-Tracking: 192.168.0.224:8080/itsupport/Ticket-Tracking \n"
							+ "\t* Ticket-ID: " + result + "\n"
							+ "\t* Gesendet am: " + LocalDateTime.now().toString());
					mail.setRecipient(ticket.getEmail());
					
					this.emailUtilitiy.sendSimpleMail(session, mail, this.emailConfig.getFromMail());
				}
				
				return (int) result;
			} 
			else 
				return -1;
		}
		return -1;
	}
	
	/**
	 * send File to Kanboard Task. If response is instance of Integer, then return result as ID of the uploaded file. Otherwise -1.
	 * 
	 * @return ID int ID of the uploaded file, otherwise -1.
	 */
	@Override
	public int sendFile(Ticket ticket) throws UnsupportedEncodingException {
		if (ticket != null && ticket.getFile() != null) {
			TaskFileKanboardConfigutration taskFile = TaskFileKanboardConfigutration.Create_Task_File;
			taskFile.setParameterValue("project_id", 1);
			taskFile.setParameterValue("task_id", ticket.getId());
			taskFile.setParameterValue("filename", ticket.getFile().getOriginalFilename());
			// show only first 5 characters for file content
			taskFile.setParameterValue("blob", ticket.getFileContent().substring(0, 5) + "...");
			
			//System.out.println(taskFile.getParams());
			//System.out.println(taskFile.buildRequest());
			
			// add whole file content to upload
			taskFile.setParameterValue("blob", ticket.getFileContent());
			
			Object result = sendFileRequest(taskFile);
			if(result instanceof Integer) {
				return (int) result;
			} 
			else 
				return -1;
		}
		return -1;
	}

	@Override
	public Ticket getTicketByName(Ticket ticket) throws UnsupportedEncodingException {
		if(ticket.getId() > 0) {
			TaskKanboardConfiguration getTask = TaskKanboardConfiguration.GET_TASK;
			getTask.setParameterValue("task_id", ticket.getId());
			
			Object result = sendTaskRequest(getTask);
			
			if(result != null) {		
				JSONObject task_info = new JSONObject(result.toString());
				String title = task_info.getString("title").toLowerCase();
				int project_id = task_info.getInt("project_id");
				
				//only ticket with adjusted Name & project id can be read
				if(title.contains(ticket.getLastname().toLowerCase()) && project_id == 1) {
					//set assigned area of Ticket
					ticket.setBuilding(getSwimlandName(task_info.getInt("swimlane_id")));
					//set level of ticket
//					JSONObject color_obj = new JSONObject(task_info.getJSONObject("color").toString());
//					String color = color_obj.getString("name").toLowerCase();
//					ticket.setLevel(getStufeByColor(color));
//					ticket.setColor_id(color);
					ticket.setColor_id("cyan");
					
					ticket.setState(getStateByColumnID(task_info.getInt("column_id")));
					
					String infos = task_info.getString("title").replace("[", "").replace("]", "");
					ticket.setTitle(infos);
	
					//ticket.setDescription(task_info.getString("description"));
					
					ticket.setContactperson(getContactPersonByID(task_info.getInt("owner_id")));
					
					LocalDateTime beginne_at;
					if(!task_info.isNull("date_started")) {
						beginne_at = convertLongTimeToLocalDate(task_info.getLong("date_started"));
						ticket.setBeginn_at(beginne_at);
					}
					
					
					LocalDateTime endet_am;
					if(!task_info.isNull("date_due")) {
						endet_am = convertLongTimeToLocalDate(task_info.getLong("date_due"));
						ticket.setEnded_am(endet_am);
					}
					
					LocalDateTime changed_am;
					if(!task_info.isNull("date_modification")) {
						changed_am = convertLongTimeToLocalDate(task_info.getLong("date_modification"));
						ticket.setChanged_at(changed_am);
					}
					
					String description = task_info.getString("description");
					convertDescriptionToHTML(ticket, description);
					
					return ticket;
				} else {
					ticket.setTitle("Ticket nicht gefunden");
					ticket.setDescription("Keinen Ticket mit Ticketnummer " + ticket.getId() + " und Ihren Namen im System gefunden. Bitte überprüfen Sie nochmal Ihre Angabe");
					return ticket;
				}
			} else {
				ticket.setTitle("Ticket nicht gefunden");
				ticket.setDescription("Keinen Ticket mit Ticketnummer " + ticket.getId() + " und Ihren Namen im System gefunden. Bitte überprüfen Sie nochmal Ihre Angabe");
				return ticket;
			}
		}
		return null;
	}
	
	/**
	 * get swimland name of project IT-Aufgabe by id
	 * 
	 * @param id swimland_id
	 * @return String name swimmland name
	 */
	private String getSwimlandName(int id) {
		switch (id) {
			case 1: 
				return "Allgemein";
			case 9: 
				return "Kuchenheim";
			case 10: 
				return "Ülpenich";
			case 11: 
				return "Kall";
			case 12: 
				return "Zingsheim";
			case 13: 
				return "Eulog";
			case 73: 
				return "BiAp";
			case 136: 
				return "BBB";
			case 142: 
				return "WIKI, Kanboard & Servern";
			default:
				return "Eingang";
		}
		
	}
	
	/**
	 * Get contact person of task by ID
	 * @param id int owner_id
	 * @return name String contact person
	 */
	private String getContactPersonByID(int id) {
		switch (id) {
			case 3: 
				return "Rene Blum";
			case 4: 
				return "Michael Hochgürtel";
			case 6: 
				return "Harald Schröder";
			case 7: 
				return "Minh Tam Truong";
			case 72: 
				return "Sebastian Hanse";
			case 79: 
				return "Christian Zacharias";
			default:
				return "System";
		}
		
	}
	
	/**
	 * get column name by columnID
	 * 
	 * @param id int column_id
	 * @return name String name of column
	 */
	private String getStateByColumnID(int id) {
		switch (id) {
			case 2: 
				return "Bereit";
			case 3: 
				return "In Bearbeitung";
			case 4: 
				return "Erledigen";
			default:
				return "Eingang";
		}
		
	}
	
	/**
	 * get level of ticket
	 * 4 level: schwer, mittel, einfach, kurzfristig Störung/Eingang  
	 * @param color String color of ticket
	 * @return level String level of ticket
	 */
	private String getStufeByColor(String color) {
		switch (color) {
			case "red": 
				return "Schwer";
			case "yellow": 
				return "Mittel";
			case "green": 
				return "einfach";
			default:
				return "Eingang/kurzfristige Problem";
		}
		
	}
	
	/**
	 * seperate description of ticket to 2 parts. Part "TICKET" as category of ticket, part "IT-Log" as IT-Tracking
	 * @param ticket
	 * @param desc
	 */
	private void convertDescriptionToHTML(Ticket ticket, String desc) {
		if(desc != null && !desc.isBlank()) {
			String[] caution_desc_array = desc.split("### TICKET ###");
			
			if(caution_desc_array.length == 2) {
				String caution_desc = caution_desc_array[1];
				
				caution_desc = caution_desc.replace("\r\n", "");
				caution_desc = caution_desc.replace("**", "");
				caution_desc = caution_desc.split("Beschreibung:")[1];
				
				String[] itlog_desc_array = desc.split("### IT-LOG ###");
				if(itlog_desc_array.length == 2) {
					String itlog_desc = itlog_desc_array[1];
					
					//itlog_desc = itlog_desc.replace("\r\n", System.lineSeparator());
					itlog_desc = itlog_desc.replace("**", "");
					ticket.setDescription(itlog_desc);
					System.out.println(ticket.getDescription());
					
					if(caution_desc.contains("### IT-LOG ###"))
						caution_desc = caution_desc.split("### IT-LOG ###")[0];
					else if(caution_desc.contains("###IT-LOG###"))
						caution_desc = caution_desc.split("###IT-LOG###")[0];
					else if(caution_desc.contains("### IT-Log ###"))
						caution_desc = caution_desc.split("### IT-Log ###")[0];
					else if(caution_desc.contains("###IT-Log###"))
						caution_desc = caution_desc.split("###IT-Log###")[0];
				}
				
				ticket.setCategory(caution_desc);
				//System.out.println(ticket.getCategory());
				
			}
			
				
		}
	}
	
	private LocalDateTime convertLongTimeToLocalDate(long value) {
		if(value > 0) {
			LocalDateTime date = Instant.ofEpochSecond(value).atZone(ZoneId.of("Europe/Berlin")).toLocalDateTime();
			return date;
		}
		return null;
	}
}
