package de.newkuchenheim.ITSupport.dao.implement;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import de.newkuchenheim.ITSupport.bdo.CostCentre;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.ArchiveJobrouterConfig;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.DataJobrouterConfig;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.FileUploadJobrouterConfig;
import de.newkuchenheim.ITSupport.dao.jobrouterDAO;
import de.newkuchenheim.ITSupport.dao.jobrouterDataInterface;
import de.newkuchenheim.ITSupport.dao.jobrouterFileUploadInterface;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 14.02.2024
 * 
 */
public class kostenstelleJobRouterDAO extends jobrouterDAO implements jobrouterDataInterface<CostCentre>, jobrouterFileUploadInterface {
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
	public long sendDataSet(CostCentre object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int sendListOptions(CostCentre object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long deleteDataSets(List<String> jrids) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getFile(String fileid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> sendFile(MultipartFile uploadedFile) throws IOException {
		FileUploadJobrouterConfig fileConfig = FileUploadJobrouterConfig.POST_FILE;
		JSONObject postParams = fileConfig.getPostParams();
		JSONArray fileData = new JSONArray();
		JSONObject file = new JSONObject();
		file.put("filename", uploadedFile.getOriginalFilename());
		file.put("content", uploadedFile.getBytes());
		postParams.put("files", fileData);
		Map<String, String> fileDataResult = sendFileRequest(fileConfig);
		return fileDataResult;
	}
	
	
}
