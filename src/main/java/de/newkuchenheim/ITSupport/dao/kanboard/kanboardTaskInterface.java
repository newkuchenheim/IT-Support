package de.newkuchenheim.ITSupport.dao;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import de.newkuchenheim.ITSupport.bdo.Ticket;
import de.newkuchenheim.ITSupport.bdo.awNote;

public interface kanboardTaskInterface<T> {
	
	/**
	 * send a request to create a Ticket in IT-Support Board
	 * 
	 * @param ticket
	 * 
	 * @return int id if request was sent success. Otherwise -1 when failure.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public int sendTicket(T ticket) throws UnsupportedEncodingException;
	
	/**
	 * get Ticket by Keys ID & name
	 * 
	 * @param tickets
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public T getTicketByName(T ticket)  throws UnsupportedEncodingException;
}
