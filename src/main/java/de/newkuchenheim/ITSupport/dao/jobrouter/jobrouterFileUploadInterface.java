package de.newkuchenheim.ITSupport.dao;

import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Sebastian Hansen
* 
* @createOn 20.02.2024
* 
*/
public interface jobrouterFileUploadInterface {
	/**
	 * send a request to get file from Jobrouter.
	 * 
	 * @param fileid
	 * 
	 * @return File data as HashMap if request sent success. Otherwise null when failure.
	 */
	public Map<String, Object> getFile(String fileid);
	/**
	 * send a request to upload a temporary file to Jobrouter
	 * 
	 * @param uploadedFile
	 * 
	 * @return file data of temporary file in jobrouter as Map if request sent success. Otherwise null when failure.
	 * 
	 */
	public Map<String, String> sendFile(MultipartFile uploadedFile) throws IOException;
}
