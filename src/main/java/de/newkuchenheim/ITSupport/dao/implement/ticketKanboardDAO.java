package de.newkuchenheim.ITSupport.dao.implement;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Base64;

import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.kanboardConfig.TaskFileKanboardConfigutration;
import de.newkuchenheim.ITSupport.bdo.kanboardConfig.TaskKanboardConfiguration;
import de.newkuchenheim.ITSupport.dao.kanboardDAO;
import de.newkuchenheim.ITSupport.dao.kanboardFileInterface;
import de.newkuchenheim.ITSupport.dao.kanboardTaskInterface;

public class ticketKanboardDAO extends kanboardDAO implements kanboardTaskInterface<Ticket>, kanboardFileInterface<Ticket> {

	private static ticketKanboardDAO instance;

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
					+ ticket.getTelefon() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Werkstatt: " + _BOLD_CLOSE
					+ ticket.getBuilding() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Abteilung: " + _BOLD_CLOSE
					+ ticket.getBranch() + _NEWLINE + _NEWLINE + _HEADING_3_OPEN + "TICKET" + _HEADING_3_CLOSE
					+ _TAB_UL_1 + _BOLD_OPEN + "Grund: " + _BOLD_CLOSE + ticket.getCategory() + _NEWLINE + _TAB_UL_1
					+ _BOLD_OPEN + "Beschreibung: " + _NEWLINE + _BOLD_CLOSE + description;
			
			TaskKanboardConfiguration task = TaskKanboardConfiguration.CREATE_TASK;
			task.setParameterValue("project_id", 1);
			task.setParameterValue("creator_id", 18);
			task.setParameterValue("swimmland_id", 57);
			task.setParameterValue("color_id", "cyan");
			task.setParameterValue("column_id", 1);
			task.setParameterValue("title", title);
			task.setParameterValue("description", descript);

			System.out.println(task.getParams());
			System.out.println(task.buildRequest());
			
			Object result = sendTaskRequest(task);
			if(result instanceof Integer) {
				return (int) result;
			} 
			else 
				return -1;
		}
		return-1;
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
			
			System.out.println(taskFile.getParams());
			System.out.println(taskFile.buildRequest());
			
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
	
}
