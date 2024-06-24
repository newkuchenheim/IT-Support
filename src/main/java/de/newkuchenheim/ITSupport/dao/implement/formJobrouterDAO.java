package de.newkuchenheim.ITSupport.dao.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.newkuchenheim.ITSupport.bdo.FormChgLog;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.DataJobrouterConfig;
import de.newkuchenheim.ITSupport.dao.jobrouterDAO;

public class formJobrouterDAO extends jobrouterDAO  {
	private static formJobrouterDAO instance;
	public static formJobrouterDAO getInstance() {
		if (instance == null) {
			instance = new formJobrouterDAO();
		}
		return instance;
	}
	
	public List<FormChgLog> getChangeLogs() {
		// create config and send request
		List<FormChgLog> FormChgLogs = new ArrayList<>();
		DataJobrouterConfig dataConf = DataJobrouterConfig.GET_DATASETS;
		dataConf.resetRequestRoute();
		dataConf.setParameterValue(":guid", "102BBF00-8865-1D5A-7E92-17FF5E4358A8");
		
		System.out.println(dataConf.getParams());
		System.out.println(dataConf.buildRequestRoute());
		JSONObject datasets = sendDataRequest(dataConf);
		if (datasets != null) {
			JSONArray dataArray = datasets.getJSONArray("datasets");
			if (dataArray != null && !dataArray.isEmpty()) {
				for(Object dataset : dataArray) {
					FormChgLog FormChgLogTmp = new FormChgLog();
					FormChgLogTmp.setJrid(Long.parseLong(((JSONObject)dataset).getString("jrid")));
					FormChgLogTmp.setChglogID(Long.parseLong(((JSONObject)dataset).getString("chglogID")));
					FormChgLogTmp.setType(((JSONObject)dataset).getString("type"));
					FormChgLogTmp.setWrittenBy(((JSONObject)dataset).getString("writtenBy"));
					FormChgLogTmp.setWrittenOn(((JSONObject)dataset).getString("writtenOn"));
					FormChgLogTmp.setTitle(((JSONObject)dataset).getString("title"));
					FormChgLogTmp.setDescription(((JSONObject)dataset).getString("description"));
					FormChgLogs.add(FormChgLogTmp);
				}
				// Sort List by number
				Collections.sort(FormChgLogs, Collections.reverseOrder());
			}
		}
		return FormChgLogs;
	}
}
