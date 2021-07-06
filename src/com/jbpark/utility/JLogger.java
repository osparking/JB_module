package com.jbpark.utility;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.logging.Level;
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
	private static Logger logger = Logger.getGlobal();
	
	public static String getFilePath() {
		return "로그파일: " + logFile;
	}
	private static String logFile = "D:/LOG/global.%g.log";
	public static Logger getLogger() {
		logger.setLevel(Level.CONFIG);
		logger.setUseParentHandlers(false);
		int LOG_ROTATION_COUNT = 30;
		JB_FileHandler handler;
		try {
			handler = new JB_FileHandler(logFile, 0, LOG_ROTATION_COUNT);
			handler.setLevel(Level.CONFIG);
			logger.addHandler(handler);
		} catch (NoSuchFileException e) {
			System.out.println("파일부재 오류: " + e.getFile());
			System.out.println("프로그램 종료!");
			System.exit(-1);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}		
		
		System.out.println(JLogger.getFilePath());
		return logger;
	}	
}
