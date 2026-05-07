package de.newkuchenheim.ITSupport.bdo;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 06.05.2026
 * 
 */

public class Person {
	private int persId;
	private String firstname;
	private String lastname;
	private String email;
	private int is_active;
	/**
	 * @return the persId
	 */
	public int getPersId() {
		return persId;
	}
	/**
	 * @param persId the persId to set
	 */
	public void setPersId(int persId) {
		this.persId = persId;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the is_active
	 */
	public int getIs_active() {
		return is_active;
	}
	/**
	 * @param is_active the is_active to set
	 */
	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}
}
