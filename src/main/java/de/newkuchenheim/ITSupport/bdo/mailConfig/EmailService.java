package de.newkuchenheim.ITSupport.bdo.mailConfig;

//Importing required classes
import de.newkuchenheim.ITSupport.bdo.email;

//Interface
public interface EmailService {

 // To send a simple email
 String sendSimpleMail(email details);

 // To send an email with attachment
 String sendMailWithAttachment(email details);
 
}