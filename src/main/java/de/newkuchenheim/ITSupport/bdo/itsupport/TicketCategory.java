package de.newkuchenheim.ITSupport.bdo.itsupport;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 14.07.23
 * 
 */
public class TicketCategory {
	private String value;
	private String text;
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * 
	 * @param value the value to set
	 * @param text the text to set
	 */
	public TicketCategory(String value, String text) {
		this.value = value;
		this.text = text;
	}
}
