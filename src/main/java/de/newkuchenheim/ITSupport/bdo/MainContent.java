/**
 * 
 */
package de.newkuchenheim.ITSupport.bdo;

import java.time.LocalDateTime;


/**
 * @author Minh Tam Truong
 * 
 * @createOn 10.10.2022
 * 
 */
public class MainContent {
	private String type;
	private String title;
	private String description;
	private String writtenBy;
	private LocalDateTime writtenOn;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the writtenOn
	 */
	public LocalDateTime getWrittenOn() {
		return writtenOn;
	}

	/**
	 * @param writtenOn the writtenOn to set
	 */
	public void setWrittenOn(LocalDateTime writtenOn) {
		this.writtenOn = writtenOn;
	}

	/**
	 * @return the writtenBy
	 */
	public String getWrittenBy() {
		return writtenBy;
	}

	/**
	 * @param writtenBy the writtenBy to set
	 */
	public void setWrittenBy(String writtenBy) {
		this.writtenBy = writtenBy;
	}
}
