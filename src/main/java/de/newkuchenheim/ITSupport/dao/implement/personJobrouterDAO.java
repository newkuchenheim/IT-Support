package de.newkuchenheim.ITSupport.dao.implement;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.newkuchenheim.ITSupport.bdo.Person;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.DataJobrouterConfig;
import de.newkuchenheim.ITSupport.dao.jobrouterDAO;
import de.newkuchenheim.ITSupport.dao.jobrouterDataInterface;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 06.05.2026
 * 
 */

public class personJobrouterDAO extends jobrouterDAO implements jobrouterDataInterface<Person> {
	private static personJobrouterDAO instance;
	private String PersonGUID = "5F96E6FD-425B-1D35-CE2C-20033CA1065C";
	
	public static personJobrouterDAO getInstance() {
		if (instance == null) {
			instance = new personJobrouterDAO();
		}
		return instance;
	}
	
	@Override
	public List<Person> getDataSets(String guid) {
		// create config and send request
		List<Person> Persons = new ArrayList<>();
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
						Person PersonTmp = new Person();
						JSONObject currDataset = ((JSONObject) dataset);
						if (currDataset.get("persnr") != null && currDataset.get("persnr") instanceof Integer) {
							PersonTmp.setPersId(currDataset.getInt("persnr"));
						}
						if (currDataset.get("vorname") != null && currDataset.get("vorname") instanceof String) {
							PersonTmp.setFirstname(currDataset.getString("vorname"));
						}
						if (currDataset.get("nachname") != null && currDataset.get("nachname") instanceof String) {
							PersonTmp.setLastname(currDataset.getString("nachname"));
						}
						if (currDataset.get("email") != null && currDataset.get("email") instanceof String) {
							PersonTmp.setEmail(currDataset.getString("email"));
						}
						if (currDataset.get("is_active") != null && currDataset.get("is_active") instanceof Integer) {
							PersonTmp.setIs_active(currDataset.getInt("is_active"));
						}
						Persons.add(PersonTmp);
					}
					// remove inactive persons
					Persons.removeIf(pl -> pl.getIs_active() == 0);
				}
			}
		}
		return Persons;
	}

	@Override
	public Person getDataSet(String guid, long jrid) {
		// create config and send request
		Person Person = null;
		if (guid != null && !guid.isBlank()) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.GET_DATASETS;
			dataConf.resetRequestRoute();
			dataConf.setParameterValue(":guid", guid);
			dataConf.setParameterValue(":jrid", jrid);
			
			System.out.println(dataConf.getParams());
			System.out.println(dataConf.buildRequestRoute());
			JSONObject datasets = sendDataRequest(dataConf);
			if (datasets != null) {
				JSONArray dataArray = datasets.getJSONArray("datasets");
				if (dataArray != null && !dataArray.isEmpty()) {
					Person = new Person();
					JSONObject Dataset = dataArray.getJSONObject(0);
					if (Dataset.get("persnr") != null && Dataset.get("persnr") instanceof Integer) {
						Person.setPersId(Dataset.getInt("persnr"));
					}
					if (Dataset.get("vorname") != null && Dataset.get("vorname") instanceof String) {
						Person.setFirstname(Dataset.getString("vorname"));
					}
					if (Dataset.get("nachname") != null && Dataset.get("nachname") instanceof String) {
						Person.setLastname(Dataset.getString("nachname"));
					}
					if (Dataset.get("email") != null && Dataset.get("email") instanceof String) {
						Person.setEmail(Dataset.getString("email"));
					}
					if (Dataset.get("is_active") != null && Dataset.get("is_active") instanceof Integer) {
						Person.setIs_active(Dataset.getInt("is_active"));
					}
				}
				if(Person.getIs_active() == 0) {
					Person = null;
				}
			}
		}
		return Person;
	}

	@Override
	public long sendDataSet(String guid, Person object) {
		if (guid != null && !guid.isBlank() && object != null) {
			DataJobrouterConfig dataConf = DataJobrouterConfig.POST_DATASETS;
			dataConf.resetRequestRoute();
			dataConf.setParameterValue(":guid", guid);
			JSONObject dataset = new JSONObject();
			dataset.put("pernr", object.getPersId());
			dataset.put("vorname", object.getFirstname());
			dataset.put("nachname", object.getLastname());
			dataset.put("email", object.getEmail());
			JSONObject resultObj = sendDataRequest(dataConf);
			if (resultObj != null) {
				return Long.parseLong(resultObj.getJSONArray("datasets").getJSONObject(0).getString("jrid"));
			}
		}
		return -1;
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

	@Override
	public int sendListOptions(String guid, Person object) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public List<Person> getDataSets() {
		return this.getDataSets(this.PersonGUID);
	}
	
	public Person getDataSet(long jrid) {
		return this.getDataSet(this.PersonGUID, jrid);
	}
	
	public long sendDataSet(Person Person) {
		return this.sendDataSet(this.PersonGUID, Person);
	}
	
	public int sendListOptions(Person Person) {
		return this.sendListOptions(this.PersonGUID, Person);
	}
	
	public boolean deleteDataSets(List<String> jrids) {
		return this.deleteDataSets(this.PersonGUID, jrids);
	}

}
