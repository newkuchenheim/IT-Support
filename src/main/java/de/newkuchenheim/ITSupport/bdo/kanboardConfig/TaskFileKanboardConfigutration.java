package de.newkuchenheim.ITSupport.bdo.kanboardConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Truong
 *
 */
public enum TaskFileKanboardConfigutration {

	Create_Task_File("createTaskFile", 94500810, new HashMap()),
	Get_All_Task_Files("getAllTaskFiles", 1880662820, new HashMap()),
	Get_Task_File("getTaskFile", 318676852, new HashMap()),
	Download_TaskFile("downloadTaskFile", 235943344, new HashMap()),
	Remove_Task_File("removeTaskFile", 447036524, new HashMap()),
	Remove_All_Task_Files("removeAllTaskFiles", 593312993, new HashMap());
	
	private String method;
	private int id;
	private int projectID;
	private Map<String, Object> params;
	private String requestJson;
	
	TaskFileKanboardConfigutration(String method, int id, Map<String, Object> map){
		this.id = id;
		this.method = method;
		this.params = map;
		configParamsMap(method);
	}
	
	/**
	 * Configure a default params-map Value of with the defined Keys can be computed
	 * later
	 * 
	 * @param methodname
	 * @return
	 */
	private void configParamsMap(String methodname) {
		params.clear();
		switch (methodname) {
		case "createTaskFile":
			//params.put("createTaskFile", 0);
			params.put("project_id", 0);
			params.put("task_id", 0);
			params.put("filename", "");
			params.put("blob", "");
			break;
		case "getAllTaskFiles":
			params.put("task_id", 0);
			break;
		case "getTaskFile":
			params.put("file_id", 0);
			break;
		case "downloadTaskFile":
			params.put("file_id", 0);
			break;
		case "removeTaskFile":
			params.put("file_id", 0);
			break;
		case "removeAllTaskFile":
			params.put("task_id", 0);
		}
	}
	
	public Map<String, Object> getParams() {
		return params;
	}

	public boolean setParameterValue(String param, Object value) {
		if (param != null && !param.isBlank()) {
			params.put(param, value);
			return true;
		} 
		return false;
	}
	
	public String buildRequest() {
		String quote = "\"";
		
		requestJson = "{\"jsonrpc\": \"2.0\"," 
				+ "\"method\": \"" + this.method + "\"," 
				+ "\"id\": " + this.id + ","
				+ "\"params\": {";
		
		//build params-String
		if(!params.isEmpty()) {
			/*int params_size = params.size();
			
			params.entrySet().forEach(e -> {
				requestJson += quote + e.getKey() + quote + ": ";
				
				//check type of value. fall value is instance if string, then add quote sign
				if(e.getValue() instanceof Integer) {
					requestJson += e.getValue();
				} else if(e.getValue() instanceof String) {
					requestJson += quote + e.getValue() + quote;
				}
				
				//add a comma for next key set
				if(params.size()>1){
					requestJson += ",";
				}
				params.remove(e.getKey());
			});
			requestJson += "}}";
			return requestJson;*/
			List<String> paramList = new ArrayList<String>();
			
			params.entrySet().forEach(e -> {
				String paramRequest = quote + e.getKey() + quote + ": ";
				
				//check type of value. fall value is instance if string, then add quote sign
				if(e.getValue() instanceof Integer) {
					paramRequest += e.getValue();
				} else if(e.getValue() instanceof String) {
					paramRequest += quote + e.getValue() + quote;
				}
				paramList.add(paramRequest);
			});
			if(paramList != null && !paramList.isEmpty()) {
				for(int i=0; i<paramList.size(); i++) {
					requestJson = requestJson + paramList.get(i);
					if(i < (paramList.size() - 1) ) {
						requestJson = requestJson + ",";
					}
				}
			}
			requestJson += "}}";
			return requestJson;
		}
		
		return null;
	}
}
