package de.newkuchenheim.ITSupport.dao.implement;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.newkuchenheim.ITSupport.bdo.FormData;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.DataJobrouterConfig;
import de.newkuchenheim.ITSupport.dao.jobrouterDAO;
import de.newkuchenheim.ITSupport.dao.jobrouterDataInterface;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 27.04.2026
 * 
 */

public class formDataJobrouterDAO extends jobrouterDAO implements jobrouterDataInterface<FormData> {
	private static formDataJobrouterDAO instance;
	private String FormDataGUID = "BAADBC30-D148-408E-6D0C-60235DD96324";
	private List<FormData> _FormDataAll = new ArrayList<>();
	
	public static formDataJobrouterDAO getInstance() {
		if (instance == null) {
			instance = new formDataJobrouterDAO();
		}
		return instance;
	}
	
	
	@Override
	public List<FormData> getDataSets(String guid) {
		List<FormData> FormData = new ArrayList<>();
		if (guid != null && !guid.isBlank()) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.GET_DATASETS;
			dataConf.resetRequestRoute();
			dataConf.setParameterValue(":guid", guid);
			
			System.out.println(dataConf.getParams());
			System.out.println(dataConf.buildRequestRoute());
			JSONObject datasets = sendDataRequest(dataConf);
			if (datasets != null) {
				JSONArray dataArray = datasets.getJSONArray("datasets");
				if (!dataArray.isEmpty()) {
					for (Object item : dataArray) {
						FormData tempFormData = new FormData();
						tempFormData.setJrid(((JSONObject)item).getInt("jrid"));
						tempFormData.setId(((JSONObject)item).getInt("id"));
						tempFormData.setKeyword(((JSONObject)item).getString("keyword"));
						tempFormData.setFormName(((JSONObject)item).getString("form_name"));
						tempFormData.setModule(((JSONObject)item).getString("module"));
						tempFormData.setText(((JSONObject)item).getString("text"));
						tempFormData.setValue(((JSONObject)item).getString("value"));
						if (((JSONObject)item).get("description") != null && ((JSONObject)item).get("description") instanceof String) {
							tempFormData.setDescription(((JSONObject)item).getString("description"));
						}
						FormData.add(tempFormData);
					}
				}
			}
		}
		return FormData;
	}

	@Override
	public FormData getDataSet(String guid, long jrid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long sendDataSet(String guid, FormData object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteDataSets(String guid, List<String> jrids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int sendListOptions(String guid, FormData object) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public List<FormData> getDataSets() {
		return this.getDataSets(FormDataGUID);
	}
	
	/**
	 * 
	 * @param keyword for specific form data
	 * @param reset clear form data list if param true
	 * @return List of Form Data
	 */
	public List<FormData> getFormData(String keyword, boolean reset) {
		if (reset) {
			_FormDataAll = getDataSets();
		}
		// get copy data to new list
		List<FormData> FormData = new ArrayList<>(_FormDataAll);
		// get only form data for keyword
		if (FormData != null && !FormData.isEmpty()) {
			FormData.removeIf(fd -> !fd.getKeyword().equals(keyword));
		}
		return FormData;
	}
}
