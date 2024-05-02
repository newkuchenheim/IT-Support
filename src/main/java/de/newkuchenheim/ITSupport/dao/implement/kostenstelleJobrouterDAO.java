package de.newkuchenheim.ITSupport.dao.implement;

import java.util.ArrayList;
import java.util.Collections;
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
public class kostenstelleJobrouterDAO extends jobrouterDAO implements jobrouterDataInterface<CostCentre> {
	private static kostenstelleJobrouterDAO instance;
	
	public static kostenstelleJobrouterDAO getInstance() {
		if (instance == null) {
			return new kostenstelleJobrouterDAO();
		}
		return instance;
	}
	
	@Override
	public List<CostCentre> getDataSets(String guid) {
		// create config and send request
		List<CostCentre> CostCentres = new ArrayList<>();
		if (guid != null && !guid.isBlank()) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.GET_DATASETS;
			dataConf.resetRequestRoute();
			dataConf.setParameterValue(":guid", guid);
			
			System.out.println(dataConf.getParams());
			System.out.println(dataConf.buildRequestRoute());
			JSONObject datasets = sendDataRequest(dataConf);
			if (datasets != null) {
				JSONArray dataArray = datasets.getJSONArray("datasets");
				if (dataArray != null && !dataArray.isEmpty()) {
					for(Object dataset : dataArray) {
						CostCentre CostCentreTmp = new CostCentre();
						JSONObject currDataset = ((JSONObject) dataset);
						if (currDataset.get("ks") != null && currDataset.get("ks") instanceof String) {
							CostCentreTmp.setNumber(currDataset.getString("ks"));
						}
						if (currDataset.get("bez") != null && currDataset.get("bez") instanceof String) {
							CostCentreTmp.setLabel(currDataset.getString("bez"));
						}
						if (currDataset.get("bez1") != null && currDataset.get("bez1") instanceof String) {
							CostCentreTmp.setLabel1(currDataset.getString("bez1"));
						}
						if (currDataset.get("standort") != null && currDataset.get("standort") instanceof String) {
							CostCentreTmp.setLocation(currDataset.getString("standort"));
						}
						CostCentres.add(CostCentreTmp);
						// Sort List by number
						Collections.sort(CostCentres);
					}
				}
			}
		}
		return CostCentres;
	}

	@Override
	public CostCentre getDataSet(String guid, long jrid) {
		// create config and send request
		CostCentre CostCentre = null;
		if (guid != null && !guid.isBlank()) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.GET_DATASETS_JRID;
			dataConf.resetRequestRoute();
			dataConf.setParameterValue(":guid", guid);
			dataConf.setParameterValue(":jrid", jrid);
			
			System.out.println(dataConf.getParams());
			System.out.println(dataConf.buildRequestRoute());
			JSONObject datasets = sendDataRequest(dataConf);
			if (datasets != null) {
				JSONArray dataArray = datasets.getJSONArray("datasets");
				if (dataArray != null && !dataArray.isEmpty()) {
					CostCentre = new CostCentre();
					JSONObject dataset = dataArray.getJSONObject(0);
					if (dataset.get("ks") != null && dataset.get("ks") instanceof String) {
						CostCentre.setNumber(dataset.getString("ks"));
					}
					if (dataset.get("bez") != null && dataset.get("bez") instanceof String) {
						CostCentre.setLabel(dataset.getString("bez"));
					}
					if (dataset.get("bez1") != null && dataset.get("bez1") instanceof String) {
						CostCentre.setLabel1(dataset.getString("bez1"));
					}
					if (dataset.get("standort") != null && dataset.get("standort") instanceof String) {
						CostCentre.setLocation(dataset.getString("standort"));
					}
				}
			}
		}
		return CostCentre;
	}

	@Override
	public long sendDataSet(String guid, CostCentre CostCentre) {
		if (guid != null && !guid.isBlank() && CostCentre != null) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.POST_DATASETS;
			dataConf.resetRequestRoute();
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
			dataConf.resetRequestRoute();
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
