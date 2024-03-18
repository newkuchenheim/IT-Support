package de.newkuchenheim.ITSupport.dao.implement;

import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.ArchiveJobrouterConfig;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.DataJobrouterConfig;
import de.newkuchenheim.ITSupport.bdo.jobrouterConfig.FileUploadJobrouterConfig;
import de.newkuchenheim.ITSupport.dao.jobrouterDAO;
import de.newkuchenheim.ITSupport.dao.jobrouterFileUploadInterface;

public class fileuploadJobRouterDAO extends jobrouterDAO implements jobrouterFileUploadInterface {
	private static fileuploadJobRouterDAO instance;
	
	public static fileuploadJobRouterDAO getInstance() {
		if (instance == null) {
			return new fileuploadJobRouterDAO();
		}
		return instance;
	}

	@Override
	public byte[] getFile(String fileid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> sendFile(MultipartFile uploadedFile) throws IOException {
		return null;
	}
}
