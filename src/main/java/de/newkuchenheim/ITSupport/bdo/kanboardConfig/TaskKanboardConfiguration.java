package de.newkuchenheim.ITSupport.bdo.kanboardConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TaskKanboardConfiguration {

	// private Map<String, Object> getTask = new HashMap();

	CREATE_TASK(1176509098, "createTask", new HashMap()),
	GET_TASK(700738119, "getTask", new HashMap()), GET_ALL_TASKS(133280317, "getAllTasks", new HashMap()),
	UPADTE_TASK(1406803059, "updateTask", new HashMap()), CLOSE_TASK(1654396960, "closeTask", new HashMap()),
	REMOVE_TASK(1423501287, "removeTask", new HashMap());

	private long id;
	private String method_name;
	private Map<String, Object> params = new HashMap();
	private String requestJson;

	TaskKanboardConfiguration(long method_id, String method_name, Map<String, Object> map) {
		this.id = method_id;
		this.method_name = method_name;
		this.params = map;
		configParamsMap(method_name);
	}
	
	/**
	 * Get ID of method
	 * @return id
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * Get Method-Name of Kanboard-RESTAPI
	 * @return method_name
	 */
	public String getMethodName() {
		return this.method_name;
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
		case "getTask":
			params.put("task_id", 0);
			break;
		case "getAllTasks":
			params.put("project_id", 0);
			params.put("status_id", 0);
			break;
		case "updateTask":
			params.put("id", 0);
			break;
		case "closeTask":
			params.put("task_id", 0);
			break;
		case "removeTask":
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
				+ "\"method\": \"" + this.method_name + "\"," 
				+ "\"id\": " + this.id + ","
				+ "\"params\": {";
		
		//build params-String
		if(!params.isEmpty()) {
			List<String> paramList = new ArrayList();
			
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
