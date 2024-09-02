package de.newkuchenheim.ITSupport.dao.implement.formular;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.newkuchenheim.ITSupport.bdo.daten.FormData;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.DataJobrouterConfig;
import de.newkuchenheim.ITSupport.dao.jobrouter.jobrouterDAO;
import de.newkuchenheim.ITSupport.dao.jobrouter.jobrouterDataInterface;

public class aendmittJobrouterDAO extends jobrouterDAO implements jobrouterDataInterface<FormData> {
	private static aendmittJobrouterDAO instance;
	private List<FormData> _FormDataAll = new ArrayList<>();
	public static aendmittJobrouterDAO getInstance() {
		if (instance == null) {
			instance = new aendmittJobrouterDAO();
		}
		return instance;
	}

	@Override
	public List<FormData> getDataSets(String guid) {
		List<FormData> FormDates = new ArrayList<>();
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
						tempFormData.setJrid(Long.parseLong(((JSONObject)item).getString("jrid")));
						tempFormData.setId(((JSONObject)item).getInt("id"));
						tempFormData.setKeyword(((JSONObject)item).getString("keyword"));
						tempFormData.setFormName(((JSONObject)item).getString("form_name"));
						tempFormData.setModule(((JSONObject)item).getString("module"));
						tempFormData.setText(((JSONObject)item).getString("text"));
						tempFormData.setValue(((JSONObject)item).getString("value"));
						if (((JSONObject)item).get("description") != null && ((JSONObject)item).get("description") instanceof String) {
							tempFormData.setDescription(((JSONObject)item).getString("description"));
						}
						FormDates.add(tempFormData);
					}
				}
			}
		}
		return FormDates;
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
	/**
	 * 
	 * @param keyword for specific form data
	 * @param reset clear form data list if param true
	 * @return List of Form Dates
	 */
	public List<FormData> getFormData(String keyword, boolean reset) {
		if (reset) {
			_FormDataAll = getDataSets("BAADBC30-D148-408E-6D0C-60235DD96324");
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
