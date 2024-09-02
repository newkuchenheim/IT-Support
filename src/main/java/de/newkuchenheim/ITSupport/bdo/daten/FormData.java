package de.newkuchenheim.ITSupport.bdo.daten;

public class FormData implements Comparable<FormData> {
	private long jrid;
	private int id;
	private String keyword;
	private String form_name;
	private String module;
	private String text;
	private String value;
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
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return the form_name
	 */
	public String getFormName() {
		return form_name;
	}
	/**
	 * @param form_name the form_name to set
	 */
	public void setFormName(String form_name) {
		this.form_name = form_name;
	}
	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}
	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
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
	public FormData(long jrid, int id, String keyword, String form_name, String module, String text, String value, String description) {
		this.jrid = jrid;
		this.id = id;
		this.keyword = keyword;
		this.form_name = form_name;
		this.module = module;
		this.text = text;
		this.value = value;
		this.description = description;
	}
	
	public FormData() {
		this(0, 0, "", "", "", "", "", "");
	}
	@Override
	public int compareTo(FormData fd) {
		return Integer.compare(id, fd.getId());
	}
}
