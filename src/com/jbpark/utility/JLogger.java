package com.jbpark.utility;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
	private static Logger logger = Logger.getGlobal();
	
	public static String getFilePath() {
		return "로그: " + logFile;
	}
	private static String logFile = "";
	
	/**
	 * 로그파일 이름 'corejava.%g.log' 사용됨.
	 * @return 맞춤 변형된 Global logger 
	 */
	public static Logger getLogger() {
		return getLogger("corejava");
	}
	/**
	 * 로그파일 이름 '<filename>.%g.log' 사용됨.
	 * @param filename
	 * @return 맞춤 변형된 Global logger 
	 */
	public static Logger getLogger(String filename) {
		setLogFile(filename);
		
		logger.setLevel(Level.CONFIG);
		logger.setUseParentHandlers(false);
		int LOG_ROTATION_COUNT = 3;
		JB_FileHandler handler;
		//@formatter:off
		try {
			handler = new JB_FileHandler(logFile, 0,
					LOG_ROTATION_COUNT);
			handler.setLevel(Level.CONFIG);
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
		} catch (NoSuchFileException e) {
			System.out.println("사용자 폴더: " 
					+ System.getProperty("user.dir"));
			System.out.println("파일부재 오류: " + e.getFile());
			System.out.println("프로그램 종료!");
			System.exit(-1);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}		
		
		System.out.println(JLogger.getFilePath());
		return logger;
	}
	private static void setLogFile(String filename) {
		logFile = "LOG/" + filename + ".%g.log";
	}	
}
