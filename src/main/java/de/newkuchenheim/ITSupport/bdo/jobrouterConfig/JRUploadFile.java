package de.newkuchenheim.ITSupport.bdo.jobrouterConfig;

import org.springframework.web.multipart.MultipartFile;

/**
* @author Sebastian Hansen
* 
* @createOn 20.02.2024
* 
*/
public class JRUploadFile {
	private String id;
	private String viewerURL;
	private String location;
	private byte[] content;
	private MultipartFile srcFile;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the viewerURL
	 */
	public String getViewerURL() {
		return viewerURL;
	}
	/**
	 * @param viewerURL the viewerURL to set
	 */
	public void setViewerURL(String viewerURL) {
		this.viewerURL = viewerURL;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	/**
	 * @return the srcFile
	 */
	public MultipartFile getSrcFile() {
		return srcFile;
	}
	/**
	 * @param srcFile the srcFile to set
	 */
	public void setSrcFile(MultipartFile srcFile) {
		this.srcFile = srcFile;
	}
	
	public JRUploadFile(String id, String viewerURL, String location, byte[] content) {
		this.id = id;
		this.viewerURL = viewerURL;
		this.location = location;
		this.content = content;
	}
	
	public JRUploadFile() {
		this(null, null, null, null);
	}
}
