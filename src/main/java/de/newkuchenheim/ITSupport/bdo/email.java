package de.newkuchenheim.ITSupport.bdo;

public class email {
	
	private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
    
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessageDetails() {
		return this.getMsgBody() + "<br><p>Bitte beachten Sie, dass dieses Schreiben maschinell erstellt wurde und keine Unterschrift benötigt.</p>";
	}
}
