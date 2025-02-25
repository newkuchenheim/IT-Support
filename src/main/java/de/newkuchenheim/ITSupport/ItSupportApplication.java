package de.newkuchenheim.ITSupport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import de.newkuchenheim.ITSupport.bdo.tLog;

@SpringBootApplication
public class ItSupportApplication {

	private static final String _PATH_CONFIG_WINDOWS  = System.getenv("USERPROFILE") + "\\IT-SupportContent";//"%USERPROFILE%/it-supportcontent
	private static final String _PATH_CONFIG_LINUX  = System.getProperty("user.home") + "/IT-SupportContent";//"/home/itsupport/itsupport/it-supportcontent
	private static final Path _PATH_CONFIG_SRC = Paths.get("src", "main", "resources", "static", "IT-SupportContent");
	
	public static void main(String[] args) {
		//System.setProperty("server.port","8090");
		SpringApplication.run(ItSupportApplication.class, args);
		
//		SpringApplicationBuilder parentBuilder =
//	            new SpringApplicationBuilder(ParentApplication.class)
//	                    .web(WebApplicationType.NONE);
//	    parentBuilder.run(args);
//	    parentBuilder.child(ServiceOneConfiguration.class)
//	            .properties("spring.config.name=child1").run(args);
//	    parentBuilder.child(ServiceTwoConfiguration.class)
//	            .properties("spring.config.name=child2").run(args);
		
		// set log File
		tLog.getInstance().log(null, "info", "Webapp wird gestartet");
		tLog.getInstance().log(null, "info", "OS: " + System.getProperty("os.name").equals("Linux"));			
		
		//create Data-Config
		String config_path = "";
		if(System.getProperty("os.name").equals("Linux")) {
			config_path = _PATH_CONFIG_LINUX;
		}else if (System.getProperty("os.name").equals("Windows 10")) {
			config_path = _PATH_CONFIG_WINDOWS;
		}
		try {
			if(config_path != null && !config_path.isBlank()) {
				Path _checkPath = Paths.get(config_path);
				if(!Files.isDirectory(_checkPath) || Files.notExists(_checkPath)) {
					//create directory with highest permission
//					Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx"); // permission 777
//					FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
					Files.createDirectories(_checkPath);
					
					//recursive copy
//					Stream<Path> streamPath = Files.walk(_PATH_CONFIG_SRC);
//					streamPath.forEach(p -> {
//						try {
//							Files.copy(p, _checkPath.resolve(_PATH_CONFIG_SRC.relativize(p)));
//						} catch (IOException e) {
//							//e.printStackTrace();
//							//System.out.println(String.format("Programm Start - File check - %s: FileAlreadyExistsException", p.getFileName()));
//						}
//					});
//					streamPath.close();
//				} else if(Files.isDirectory(_checkPath)) {
//					// if directory is exist but empty

					//recusive copy
//					Stream<Path> streamPath = Files.walk(_PATH_CONFIG_SRC);
//					streamPath.forEach(p -> {
//						try {
//							Files.copy(p, _checkPath.resolve(_PATH_CONFIG_SRC.relativize(p)));
//						} catch (IOException e) {
//							//e.printStackTrace();
//							System.out.println(String.format("Programm Start - File check - %s: FileAlreadyExistsException", p.getFileName()));
//						}
//					});
//					streamPath.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
