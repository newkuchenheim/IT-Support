package de.newkuchenheim.ITSupport.bdo.mailConfig;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.*;



public class emailConfiguration {
	final String fromEmail = "it-support@new-eu.de"; 
	final String password = "jql6k2dz"; 
	
	private Properties props;
	private Authenticator auth;
	
	public emailConfiguration() {
		
		props = new Properties();
		props.put("mail.smtp.host", "smtp.ionos.de"); //SMTP Host
		props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
		props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
		props.put("mail.smtp.port", "465"); //SMTP Port
		
		auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		
		//Session.getDefaultInstance(props, auth);

	}
	
	public Properties getProperties() {
		return this.props;
	}
	
	public Authenticator getAuthenticator() {
		return this.auth;
	}

	public String getFromMail() {
		return this.fromEmail;
	}
}
