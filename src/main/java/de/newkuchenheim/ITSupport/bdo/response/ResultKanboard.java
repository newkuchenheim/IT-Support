/**
 * 
 */
package de.newkuchenheim.ITSupport.bdo.response;

/**
 * @author Minh Tam Truong
 * @param <T>
 * 
 * @createOn 10.10.2022
 * 
 */
public class ResultKanboard<T> {

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
