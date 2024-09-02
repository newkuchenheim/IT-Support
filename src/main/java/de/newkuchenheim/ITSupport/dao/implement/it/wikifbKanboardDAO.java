package de.newkuchenheim.ITSupport.dao.implement.it;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import de.newkuchenheim.ITSupport.bdo.itsupport.Ticket;
import de.newkuchenheim.ITSupport.bdo.kanboardConfig.TaskKanboardConfiguration;
import de.newkuchenheim.ITSupport.dao.kanboard.kanboardDAO;
import de.newkuchenheim.ITSupport.dao.kanboard.kanboardTaskInterface;

public class wikifbKanboardDAO extends kanboardDAO implements kanboardTaskInterface<Ticket> {

	private static wikifbKanboardDAO instance;

	/**
	 * Get Instance kanboardDAO
	 * 
	 * @return kanboardDAO
	 */
	public static wikifbKanboardDAO getInstance() {
		if (instance == null) {
			return new wikifbKanboardDAO();
		}
		return instance;
	}

	@Override
	public int sendTicket(Ticket ticket) throws UnsupportedEncodingException {
		if (ticket != null) {
			String title = "[" + ticket.getBuilding() + "]-[" + ticket.getCategory() + "]-[" + ticket.getLastname()
					+ "]";

			// CR or LN check
			// bearbeiten description if line Seper. contain
			String description = ticket.getDescription();
			String new_desc = "";
			if (description != null && !description.isBlank() && description.contains(System.lineSeparator())) {
				// String[] _str_split = description.split(System.lineSeparator());
				String[] _str_split = description.split("\\R");
				for (String s : _str_split) {
					new_desc = new_desc + s + _NEWLINE;
				}
				description = new_desc;
			}

			String descript = _HEADING_1_OPEN + ticket.getCategory() + _HEADING_1_CLOSE + _HEADING_3_OPEN + "Kontakt"
					+ _HEADING_3_CLOSE + _TAB_UL_1 + _BOLD_OPEN + "Name:" + _BOLD_CLOSE + ticket.getFirstname() + " "
					+ ticket.getLastname() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Werkstatt: " + _BOLD_CLOSE
					+ ticket.getBuilding() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Abteilung: " + _BOLD_CLOSE
					+ ticket.getBranch() + _NEWLINE + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Grund: " + _BOLD_CLOSE
					+ ticket.getCategory() + _NEWLINE + _TAB_UL_1 + _BOLD_OPEN + "Beschreibung: " + _NEWLINE
					+ _BOLD_CLOSE + description;

			// send request to Kanboard
			
			TaskKanboardConfiguration task = TaskKanboardConfiguration.CREATE_TASK;
			task.setParameterValue("project_id", 16);
			task.setParameterValue("creator_id", 18);
			task.setParameterValue("swimmland_id", 143);
			task.setParameterValue("color_id", "red");
			task.setParameterValue("column_id", 66);
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
		return -1;
	}

	@Override
	public Ticket getTicketByName(Ticket ticket) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

}
