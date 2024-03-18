package de.newkuchenheim.ITSupport.dao.implement;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.newkuchenheim.ITSupport.bdo.CostCentre;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.DataJobrouterConfig;
import de.newkuchenheim.ITSupport.dao.jobrouterDAO;
import de.newkuchenheim.ITSupport.dao.jobrouterDataInterface;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 14.02.2024
 * 
 */
public class kostenstelleJobRouterDAO extends jobrouterDAO implements jobrouterDataInterface<CostCentre> {
	private static kostenstelleJobRouterDAO instance;
	
	public static kostenstelleJobRouterDAO getInstance() {
		if (instance == null) {
			return new kostenstelleJobRouterDAO();
		}
		return instance;
	}
	
	@Override
	public JSONArray getDataSets(String guid) {
		// create config and send request
		if (guid != null && !guid.isBlank()) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.GET_DATASETS;
			dataConf.setParameterValue(":guid", guid);
			
			System.out.println(dataConf.getParams());
			System.out.println(dataConf.buildRequestRoute());
			JSONObject datasets = sendDataRequest(dataConf);
			if (datasets != null) {
				return datasets.getJSONArray("datasets");
			}
			return null;
		}
		return null;
	}

	@Override
	public JSONArray getDataSet(String guid, long jrid) {
		// create config and send request
		if (guid != null && !guid.isBlank()) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.GET_DATASETS_JRID;
			dataConf.setParameterValue(":guid", guid);
			dataConf.setParameterValue(":jrid", jrid);
			
			System.out.println(dataConf.getParams());
			System.out.println(dataConf.buildRequestRoute());
			JSONObject datasets = sendDataRequest(dataConf);
			if (datasets != null) {
				return datasets.getJSONArray("datasets");
			}
			return null;
		}
		return null;
	}

	@Override
	public long sendDataSet(String guid, CostCentre CostCentre) {
		if (guid != null && !guid.isBlank() && CostCentre != null) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.POST_DATASETS;
			dataConf.setParameterValue(":guid", guid);
			JSONObject dataset = new JSONObject();
			dataset.put("ks", CostCentre.getNumber());
			dataset.put("bez", CostCentre.getLabel());
			dataset.put("standort", CostCentre.getLocation());
			dataset.put("bez1", CostCentre.getLabel1());
			dataConf.setPostParamsValue("dataset", dataset);
			JSONObject resultObj = sendDataRequest(dataConf);
			if (resultObj != null) {
				return Long.parseLong(resultObj.getJSONArray("datasets").getJSONObject(0).getString("jrid"));
			}
		}
		return -1;
	}

	@Override
	public int sendListOptions(String guid, CostCentre CostCentre) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteDataSets(String guid, List<String> jrids) {
		if (guid != null && !guid.isBlank() && jrids != null && !jrids.isEmpty()) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.DELETE_DATASETS;
			dataConf.setParameterValue(":guid", guid);
			JSONArray jrIDs = new JSONArray();
			JSONObject jrID;
			for (int i = 0; i < jrids.size(); i++) {
				jrID = new JSONObject();
				jrID.put("jrid", jrids.get(i));
				jrIDs.put(jrID);
			}
			dataConf.setPostParamsValue("datasets", jrIDs);
			JSONObject resultObj = sendDataRequest(dataConf);
			if (resultObj != null) {
				return resultObj.getBoolean("success");
			} else {
				return false;
			}
		}
		return false;
	}
}
