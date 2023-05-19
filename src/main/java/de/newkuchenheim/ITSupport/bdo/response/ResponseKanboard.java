/**
 * 
 */
package de.newkuchenheim.ITSupport.bdo.response;

import de.newkuchenheim.ITSupport.bdo.Ticket;

/**
 * @author Minh Tam Truong
 * @param <T>
 * 
 * @createOn 10.10.2022
 * 
 */
public class ResponseKanboard<T> {

	private ErrorKanboard error;
	private long methodID;
	private ResultKanboard<T> results;

	/**
	 * @return the error
	 */
	public ErrorKanboard getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ErrorKanboard error) {
		this.error = error;
	}

	/**
	 * @return the methodID
	 */
	public long getMethodID() {
		return methodID;
	}

	/**
	 * @param methodID the methodID to set
	 */
	public void setMethodID(long methodID) {
		this.methodID = methodID;
	}

	/**
	 * @return the results
	 */
	public ResultKanboard<T> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(ResultKanboard<T> results) {
		this.results = results;
	}
	
	public String getAnswerFromKanboard() {
		if(methodID > 0) {

			if(results != null ) {
				// if result type long in the future will be used for a lot of functions of kanboard
				// then a enum class for exact funtions will be written and used to falls decision
//				if(results.getResultType() instanceof Integer) { 
//					return "ProjectID is found";
//				} else if (results.getResultType() instanceof Ticket) {
//					return "Ticket is found";
//				} else if(results.getResultType() instanceof Boolean) {
//					return "Result on success";
//				} 
				
				return results.getResultType().toString();
			}
			else {
				return "Result on Failure";
			}
		}
		return "Result on Failure";
	}
}
