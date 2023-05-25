/**
 * 
 */
package de.newkuchenheim.ITSupport.bdo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

/**
 * @author Minh Tam Truong
 * 
 * @createOn 21.10.2022
 * 
 */
public class tLog {
	private static tLog instance;
    private FileHandler fh = null;
//	    private String LOG_PATH = "C:/S/log/uwe4log.log";

    private static final Logger logger = Logger.getLogger(tLog.class.getName());

    public static tLog getInstance(){
        if(instance == null)
            instance = new tLog();
        return instance;
    }
    
    public tLog(){
        Path log_path = Paths.get(System.getProperty("user.home"),"logs", "track.log");
        File logfile = new File(log_path.toString());

        System.out.println(log_path.toString());
        if(!Paths.get(System.getProperty("user.home"), "logs").toFile().isDirectory()){
            File log_directory = new File(Paths.get(System.getProperty("user.home"), "logs").toString());
            log_directory.mkdirs();
        }
        try {
        	fh = new FileHandler(log_path.toString(), true);

            fh.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    Calendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(record.getMillis());
                    return record.getLevel()
                            + "\t"
                            + logTime.format(cal.getTime())
                            + "\t"
                            + record.getMessage() + "\n";
                }
            });
            logger.addHandler(fh);

        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e.getMessage());
            e.printStackTrace();
        }

    }

    public void log(Exception ex, String level, String msg){
        switch (level){
            case "severe":
                logger.log(Level.SEVERE, msg, ex);
                break;
            case "warning":
                logger.log(Level.WARNING, msg, ex);
                break;
            case "info":
                logger.log(Level.INFO, msg, ex);
                break;
            case "config":
                logger.log(Level.CONFIG, msg, ex);
                break;
            case "fine":
                logger.log(Level.FINE, msg, ex);
                break;
            case "finer":
                logger.log(Level.FINER, msg, ex);
                break;
            case "finest":
                logger.log(Level.FINEST, msg, ex);
                break;
            default:
                logger.log(Level.CONFIG, msg, ex);
                break;
        }

	}

}
