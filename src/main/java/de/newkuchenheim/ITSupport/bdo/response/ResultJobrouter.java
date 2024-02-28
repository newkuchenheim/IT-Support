package de.newkuchenheim.ITSupport.bdo.response;

/**
 * @author Sebastian Hansen
 * @param <T>
 * 
 * @createOn 20.02.2024
 * 
 */
public class ResultJobrouter<T> {
	private T resultType;

	/**
	 * @return the resultType
	 */
	public T getResultType() {
		return resultType;
	}

	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(T resultType) {
		this.resultType = resultType;
	}
}
