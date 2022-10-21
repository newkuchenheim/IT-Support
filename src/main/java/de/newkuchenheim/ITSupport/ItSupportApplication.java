package de.newkuchenheim.ITSupport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.newkuchenheim.ITSupport.bdo.tLog;

@SpringBootApplication
public class ItSupportApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItSupportApplication.class, args);
		tLog.getInstance().log(null, "info", "Webapp wird gestartet");
		tLog.getInstance().log(null, "infor", "OS: " + System.getProperty("os.name").equals("Linux"));			
		
	}

}
