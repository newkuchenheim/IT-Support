package de.newkuchenheim.ITSupport.dao;

import java.io.UnsupportedEncodingException;

public interface kanboardFileInterface<T> {
	
	public int sendFile(T object)  throws UnsupportedEncodingException ;
}
