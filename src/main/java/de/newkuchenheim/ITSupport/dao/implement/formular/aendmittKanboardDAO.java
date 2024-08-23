package de.newkuchenheim.ITSupport.dao.implement;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.kanboardConfig.TaskKanboardConfiguration;
import de.newkuchenheim.ITSupport.dao.kanboardDAO;
import de.newkuchenheim.ITSupport.dao.kanboardTaskInterface;

public class aendmittKanboardDAO extends kanboardDAO {
	private static aendmittKanboardDAO instance;

	/**
	 * Get Instance kanboardDAO
	 * 
	 * @return kanboardDAO
	 */
	public static aendmittKanboardDAO getInstance() {
		if (instance == null) {
			instance = new aendmittKanboardDAO();
		}
		return instance;
	}
	
	public boolean updateFeedbackTicket(Ticket ticket) throws UnsupportedEncodingException {
		// get current task data
		TaskKanboardConfiguration getTask = TaskKanboardConfiguration.GET_TASK;
		getTask.setParameterValue("task_id", 4291);
		
		Object result = sendTaskRequest(getTask);
		
		if(result != null) {		
			JSONObject task_info = new JSONObject(result.toString());
			String oldDescr = task_info.getString("description");
			if (oldDescr.contains("\r")) {
				oldDescr = oldDescr.replace("\r\n", "\\r\\n");
			} else {
				oldDescr = oldDescr.replace("\n", "\\r\\n");
			}
			String newDescr = ticket.getDescription();
			if (newDescr.contains("\r")) {
				newDescr = newDescr.replace("\r\n", "\\r\\n");
			} else {
				newDescr = newDescr.replace("\n", "\\r\\n");
			}
			String newFeedback = String.format("%s\\r\\n* %s, %s %s:\\r\\n%s", oldDescr, ticket.getBuilding(), ticket.getFirstname(), ticket.getLastname(), newDescr);
			TaskKanboardConfiguration updateTask = TaskKanboardConfiguration.UPADTE_TASK;
			updateTask.setParameterValue("id", 4291);
			updateTask.setParameterValue("description", newFeedback);
			
			// send Update request
			result = sendTaskRequest(updateTask);
			
			if(result != null) {
				return (boolean)result;
			}
		}
		
		return false;
	}
	
	
}
