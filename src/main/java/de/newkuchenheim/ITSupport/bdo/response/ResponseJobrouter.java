package de.newkuchenheim.ITSupport.bdo.response;

/**
 * @author Sebastian Hansen
 * @param <T>
 * 
 * @createOn 20.02.2024
 * 
 */
public class ResponseJobrouter<T> {
	private ErrorJobrouter error;
	private String route;
	private ResultJobrouter<T> results;

	/**
	 * @return the error
	 */
	public ErrorJobrouter getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ErrorJobrouter error) {
		this.error = error;
	}

	/**
	 * @return the route
	 */
	public String getRoute() {
		return route;
	}

	/**
	 * @param route the route to set
	 */
	public void setRoute(String route) {
		this.route = route;
	}

	/**
	 * @return the results
	 */
	public ResultJobrouter<T> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(ResultJobrouter<T> results) {
		this.results = results;
	}
	
	public String getAnswerFromJobrouter() {
		if(route != null && !route.isBlank()) {
			if(results != null ) {
				return results.getResultType().toString();
			}
			else {
				return "Result on Failure";
			}
		}
		return "Result on Failure";
	}
}
