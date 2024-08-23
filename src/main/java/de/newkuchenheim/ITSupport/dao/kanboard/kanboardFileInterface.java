package de.newkuchenheim.ITSupport.dao;

import java.io.UnsupportedEncodingException;

public interface kanboardFileInterface<T> {
	/**
	 * send a request to attach a new file to an Ticket in IT-Support Board
	 * 
	 * @param ticket
	 * 
	 * @return int fileid if request was sent success. Otherwise -1 when failure.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public int sendFile(T object)  throws UnsupportedEncodingException ;
}
