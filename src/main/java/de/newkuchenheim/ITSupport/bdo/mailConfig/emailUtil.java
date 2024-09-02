package de.newkuchenheim.ITSupport.bdo.mailConfig;


import de.newkuchenheim.ITSupport.bdo.Email;
import de.newkuchenheim.ITSupport.bdo.itsupport.Ticket;
import jakarta.mail.*;
import jakarta.mail.internet.*;

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
	
	
}
