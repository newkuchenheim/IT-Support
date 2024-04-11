package de.newkuchenheim.ITSupport.dao.implement;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.DataJobrouterConfig;
import de.newkuchenheim.ITSupport.dao.jobrouterDAO;
import de.newkuchenheim.ITSupport.dao.jobrouterDataInterface;

public class aendmitteilungJobrouterDAO extends jobrouterDAO implements jobrouterDataInterface<String> {
	private static aendmitteilungJobrouterDAO instance;
	
	public static aendmitteilungJobrouterDAO getInstance() {
		if (instance == null) {
			return new aendmitteilungJobrouterDAO();
		}
		return instance;
	}

	@Override
	public List<String> getDataSets(String guid) {
		// create config and send request
		List<String> dataList = new ArrayList<>();
		if (guid != null && !guid.isBlank()) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.GET_DATASETS;
			dataConf.setParameterValue(":guid", guid);
			
			System.out.println(dataConf.getParams());
			System.out.println(dataConf.buildRequestRoute());
			JSONObject datasets = sendDataRequest(dataConf);
			if (datasets != null) {
				JSONArray dataArray = datasets.getJSONArray("datasets");
				if (!dataArray.isEmpty()) {
					for (Object item : dataArray) {
						dataList.add(((JSONObject)item).getString("Name"));
					}
				}
			}
		}
		return dataList;
	}

	@Override
	public String getDataSet(String guid, long jrid) {
		// create config and send request
		String name = "";
		if (guid != null && !guid.isBlank()) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.GET_DATASETS_JRID;
			dataConf.setParameterValue(":guid", guid);
			dataConf.setParameterValue(":jrid", jrid);
			
			System.out.println(dataConf.getParams());
			System.out.println(dataConf.buildRequestRoute());
			JSONObject datasets = sendDataRequest(dataConf);
			if (datasets != null) {
				name =  datasets.getJSONArray("datasets").getJSONObject(0).getString("Name");
			}
		}
		return name;
	}

	@Override
	public long sendDataSet(String guid, String object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteDataSets(String guid, List<String> jrids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int sendListOptions(String guid, String object) {
		// TODO Auto-generated method stub
		return 0;
	}
}
