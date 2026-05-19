package de.newkuchenheim.ITSupport.mailConfig;


import java.io.InputStream;

import de.newkuchenheim.ITSupport.bdo.Email;
import jakarta.mail.*;
import jakarta.mail.internet.*;

/**
 * @author Sebastian Hansen
 * 
 * @createOn 28.04.2026
 * 
 */

public class emailUtil {
	
	private static emailUtil instance;
	
	public static emailUtil getInstance() {
		if(instance == null)
			return new emailUtil();
		else return instance;
	}

	public String sendSimpleMail(Session session, Email item, String fromMail) {
		try {  
			MimeMessage message = new MimeMessage(session);  
		    message.setFrom(new InternetAddress(fromMail));  
		    message.addRecipient(Message.RecipientType.TO,new InternetAddress(item.getRecipient()));
		    if (item.getRecipient_cc() != null && !item.getRecipient_cc().isEmpty() && !item.getRecipient_cc().isBlank()) {
		    	message.addRecipient(Message.RecipientType.CC,new InternetAddress(item.getRecipient_cc()));
		    }
		    message.setSubject(item.getSubject());  
		    message.setText(item.getMsgBody());  
		       
		    //send the message  
		    Transport.send(message);  
		  
		    return "This mail will be sent. \n";
		} catch (MessagingException e) {
			e.printStackTrace();
			return "This mail couldn't be sent. \n";
		}  
	}
	
	public String sendHTMLMail(Session session, Email item, String fromMail) {
		try {  
			MimeMessage message = new MimeMessage(session);  
		    message.setFrom(new InternetAddress(fromMail));  
		    message.addRecipient(Message.RecipientType.TO,new InternetAddress(item.getRecipient()));
		    if (item.getRecipient_cc() != null && !item.getRecipient_cc().isEmpty() && !item.getRecipient_cc().isBlank()) {
		    	message.addRecipient(Message.RecipientType.CC,new InternetAddress(item.getRecipient_cc()));
		    }
		    message.setSubject(item.getSubject());  
		    message.setContent(item.getMsgBody(), "text/html; charset=utf-8");  
		       
		    //send the message  
		    Transport.send(message);  
		  
		    return "This mail will be sent. \n";
		} catch (MessagingException e) {
			e.printStackTrace();
			return "This mail couldn't be sent. \n";
		}  
	}
	
	public String sendMailWithAttachment(Session session, Email item, String fromMail, boolean IsHTML, InputStream AttachmentStream) {
		try {  
			MimeMessage message = new MimeMessage(session);  
		    message.setFrom(new InternetAddress(fromMail));  
		    message.addRecipient(Message.RecipientType.TO,new InternetAddress(item.getRecipient()));
		    if (item.getRecipient_cc() != null && !item.getRecipient_cc().isEmpty() && !item.getRecipient_cc().isBlank()) {
		    	message.addRecipient(Message.RecipientType.CC,new InternetAddress(item.getRecipient_cc()));
		    }
		    message.setSubject(item.getSubject());
		    
		    // create BodyPart
		    BodyPart msgBodyPart = new MimeBodyPart();
		    if (IsHTML) {
		    	((MimeBodyPart) msgBodyPart).setText(item.getMsgBody(), "text/html; charset=utf-8");
		    } else {
		    	((MimeBodyPart) msgBodyPart).setText(item.getMsgBody(), "utf-8");
		    }
		    
		    // create Attachment Part
		    MimeBodyPart attachPart = new MimeBodyPart(AttachmentStream);
		    
		    // create multipart body with the parts above
		    Multipart multipart = new MimeMultipart();
		    multipart.addBodyPart(msgBodyPart);
		    multipart.addBodyPart(attachPart);
		    
		    message.setContent(multipart);
		    
		    //send the message  
		    Transport.send(message);  
		  
		    return "This mail will be sent. \n";
		} catch (MessagingException e) {
			e.printStackTrace();
			return "This mail couldn't be sent. \n";
		} 
	}
	
}