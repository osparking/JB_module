package com.jbpark.utility;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * J(JongBum Park) Global Logger log file location
 * Directory: user/home/G_LOG
 * FIle name: java(n).log.(i) where n: 0-99.
 * See properties in JDK/logging.properties.
 * 
 * @author jbpar
 *
 */
public class JLogger {

	private static String filePath = null;
	private static Logger logger = Logger.getGlobal();
	
	public static String getFilePath() {
		StringBuilder lfPath = new StringBuilder();
		lfPath.append(System.getProperty("user.home"));
		lfPath.append("\\G_LOG\\java(n).log");
		filePath = lfPath.toString(); 		
		return "로그파일: " + filePath;
	}
	public static Logger getLogger() {
		try {
			FileHandler fHandler = new FileHandler();
			logger.addHandler(fHandler);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		System.out.println(JLogger.getFilePath());
		return logger;
	}	
}
