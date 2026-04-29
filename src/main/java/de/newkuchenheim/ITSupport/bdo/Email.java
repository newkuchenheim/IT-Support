package de.newkuchenheim.ITSupport.bdo;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 28.04.2026
 * 
 */

public class Email {
	
	private String recipient;
	private String recipient_cc;
    private String msgBody;
    private String subject;
    private String attachment;
    
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	/**
	 * @return the receipient_cc
	 */
	public String getRecipient_cc() {
		return recipient_cc;
	}
	/**
	 * @param receipient_cc the receipient_cc to set
	 */
	public void setRecipient_cc(String receipient_cc) {
		this.recipient_cc = receipient_cc;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
