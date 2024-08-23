/**
 * 
 */
package de.newkuchenheim.ITSupport.bdo;

import java.time.LocalDate;
import java.util.Date;
import java.time.format.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.thymeleaf.expression.Dates;

/**
 * @author Minh Tam Truong
 * 
 * @createOn 03.04.2023
 * 
 */
public class awNote {

	private String mname;
	
	private String kontakt1;
	private String telnr1;
	private String mail1;
	
	private String kontakt2;
	private String telnr2;
	private String mail2;
	
	private boolean isVertreter;
	//@DateTimeFormat(pattern = "dd.MM.yyyy")
	private LocalDate awvonDate;
	private LocalDate awtoDate;
	
	
	/**
	 * @return the kontakt1
	 */
	public String getKontakt1() {
		return kontakt1;
	}
	/**
	 * @param kontakt1 the kontakt1 to set
	 */
	public void setKontakt1(String kontakt1) {
		this.kontakt1 = kontakt1;
	}
	/**
	 * @return the telnr1
	 */
	public String getTelnr1() {
		return telnr1;
	}
	/**
	 * @param telnr1 the telnr1 to set
	 */
	public void setTelnr1(String telnr1) {
		this.telnr1 = telnr1;
	}
	/**
	 * @return the mail1
	 */
	public String getMail1() {
		return mail1;
	}
	/**
	 * @param mail1 the mail1 to set
	 */
	public void setMail1(String mail1) {
		this.mail1 = mail1;
	}
	/**
	 * @return the kontakt2
	 */
	public String getKontakt2() {
		return kontakt2;
	}
	/**
	 * @param kontakt2 the kontakt2 to set
	 */
	public void setKontakt2(String kontakt2) {
		this.kontakt2 = kontakt2;
	}
	/**
	 * @return the telnr2
	 */
	public String getTelnr2() {
		return telnr2;
	}
	/**
	 * @param telnr2 the telnr2 to set
	 */
	public void setTelnr2(String telnr2) {
		this.telnr2 = telnr2;
	}
	/**
	 * @return the mail2
	 */
	public String getMail2() {
		return mail2;
	}
	/**
	 * @param mail2 the mail2 to set
	 */
	public void setMail2(String mail2) {
		this.mail2 = mail2;
	}
	/**
	 * @return the isVertreter
	 */
	public boolean isVertreter() {
		return isVertreter;
	}
	/**
	 * @param isVertreter the isVertreter to set
	 */
	public void setVertreter(boolean isVertreter) {
		this.isVertreter = isVertreter;
	}
	/**
	 * @return the awvonDate
	 */
	public LocalDate getAwvonDate() {
		return awvonDate;
	}
	/**
	 * @param awvonDate the awvonDate to set
	 */
	public void setAwvonDate(String awvonDate) {
		if(awvonDate != null && !awvonDate.isBlank())
			this.awvonDate = LocalDate.parse(awvonDate);
		
	}
	/**
	 * @return the awtoDate
	 */
	public LocalDate getAwtoDate() {
		return awtoDate;
	}
	/**
	 * @param awtoDate the awtoDate to set
	 */
	public void setAwtoDate(String awtoDate) {
		if(awtoDate != null && !awtoDate.isBlank())
			this.awtoDate = LocalDate.parse(awtoDate);
	}
	/**
	 * @return the mname
	 */
	public String getMname() {
		return mname;
	}
	/**
	 * @param mname the mname to set
	 */
	public void setMname(String mname) {
		this.mname = mname;
	}

}
