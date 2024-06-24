package de.newkuchenheim.ITSupport.bdo;

public class FormChgLog implements Comparable<FormChgLog> {
	private long jrid;
	private long chglogid;
	private String type;
	private String writtenby;
	private String writtenon;
	private String title;
	private String description;
	/**
	 * @return the jrid
	 */
	public long getJrid() {
		return jrid;
	}
	/**
	 * @param jrid the jrid to set
	 */
	public void setJrid(long jrid) {
		this.jrid = jrid;
	}
	/**
	 * @return the chglogid
	 */
	public long getChglogID() {
		return chglogid;
	}
	/**
	 * @param chglogid the chglogid to set
	 */
	public void setChglogID(long chglogid) {
		this.chglogid = chglogid;
	}
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
	 * @return the writtenby
	 */
	public String getWrittenBy() {
		return writtenby;
	}
	/**
	 * @param writtenby the writtenby to set
	 */
	public void setWrittenBy(String writtenby) {
		this.writtenby = writtenby;
	}
	/**
	 * @return the writtenon
	 */
	public String getWrittenOn() {
		return writtenon;
	}
	/**
	 * @param writtenon the writtenon to set
	 */
	public void setWrittenOn(String writtenon) {
		this.writtenon = writtenon;
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
	@Override
	public int compareTo(FormChgLog fcl) {
		return Long.compare(chglogid, fcl.getChglogID());
	}
}
