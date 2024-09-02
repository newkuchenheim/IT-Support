package de.newkuchenheim.ITSupport.dao.jobrouter;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 20.02.2024
 * 
 */
public interface jobrouterProcessInterface {
	/**
	 * send a request to download a file uploaded in a process step. If the step is a simulated one, you should set the simulation parameter to 1 (?simulation=1).
	 * 
	 * @param workflowid Workflow-ID of the step
	 * @param fileid id for a file in an already saved step
	 * 
	 * @returns File as binary data if request sent success. Otherwise null when failure.
	 */
	public byte[] getAttachment(String workflowid, String fileid);
}
