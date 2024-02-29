/**
 * 
 */
package de.newkuchenheim.ITSupport.bdo;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Minh Tam Truong
 * 
 * @createOn 15.09.2022
 * 
 */
public class Ticket {
	private int id;
	private String firstname;
	private String lastname;
	private String category;
	private String email;
	private String telefon;
	private String branch; 
	private String title;
	private String description;
	private String building;
	private String micoscat;
	private String usercat;
	private MultipartFile file;
	private String fileContent;
	private String color_id;
	private String level;
	private String state;
	private LocalDateTime beginn_at;
	private LocalDateTime ended_am;
	private boolean is_active;
	private String contactperson;
	
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
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
	 * @return the telefon
	 */
	public String getTelefon() {
		return telefon;
	}
	/**
	 * @param telefon the telefon to set
	 */
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	/**
	 * @return the branch
	 */
	public String getBranch() {
		return branch;
	}
	/**
	 * @param branch the branch to set
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
			this.description = description;
	}
	
	/**
	 * @return the building
	 */
	public String getBuilding() {
		return building;
	}
	/**
	 * @param building the building to set
	 */
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getMicoscat() {
		return micoscat;
	}
	public void setMicoscat(String micoscat) {
		this.micoscat = micoscat;
	}
	public String getUsercat() {
		return usercat;
	}
	public void setUsercat(String usercat) {
		this.usercat = usercat;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public String getColor_id() {
		return color_id;
	}
	public void setColor_id(String color_id) {
		this.color_id = color_id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public LocalDateTime getBeginn_at() {
		return beginn_at;
	}
	public void setBeginn_at(LocalDateTime beginn_at) {
		this.beginn_at = beginn_at;
	}
	public LocalDateTime getEnded_am() {
		return ended_am;
	}
	public void setEnded_am(LocalDateTime ended_am) {
		this.ended_am = ended_am;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public String getContactperson() {
		return contactperson;
	}
	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}	
	
}
