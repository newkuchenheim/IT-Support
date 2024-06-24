package de.newkuchenheim.ITSupport.bdo.response;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 20.02.2024
 * 
 */
public class ErrorJobrouter {
	private int errorCode;
	private String errorMessage;
	
	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
