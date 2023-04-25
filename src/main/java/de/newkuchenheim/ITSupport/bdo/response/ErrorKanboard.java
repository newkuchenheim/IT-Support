/**
 * 
 */
package de.newkuchenheim.ITSupport.bdo.response;

/**
 * @author Minh Tam Truong
 * 
 * @createOn 10.10.2022
 * 
 */
public class ErrorKanboard {
	
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
