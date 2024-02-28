package de.newkuchenheim.ITSupport.dao;

import java.util.List;

import org.json.JSONArray;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 20.02.2024
 * 
 */
public interface jobrouterDataInterface<T> {
	/**
	 * send a request to get all datasets of the given table.
	 * 
	 * @param guid of table
	 * 
	 * @return JSONArray object with all datasets.
	 * 
	 * @throws
	 */
	public JSONArray getDataSets(String guid);
	/**
	 * send a request to get specific dataset of a table.
	 * 
	 * @param guid of table
	 * 
	 * @return JSONArray Object with  specific dataset.
	 * 
	 * @throws
	 */
	public JSONArray getDataSet(String guid, long jrid);
	/**
	 * send a request to create new dataset in given table.
	 * 
	 * @param guid of table
	 * 
	 * @return long - internal jrid of new dataset if request was sent success. Otherwise -1.
	 * 
	 * @throws
	 */
	public long sendDataSet(T object);
	/**
	 * send a request to create new dataset in given table.
	 * 
	 * @param jrids List of internal dataset ids to delete
	 * 
	 * @return boolean true if request was sent success. Otherwise false.
	 * 
	 * @throws
	 */
	public long deleteDataSets(List<String> jrids);
	/**
	 * send a request to create new dataset in given table
	 * 
	 * @param guid of table
	 * 
	 * @return int count of listoptions if request was sent success. Otherwise -1
	 * 
	 * @throws
	 */
	public int sendListOptions(T object);
}
