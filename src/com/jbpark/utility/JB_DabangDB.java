package com.jbpark.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

public class JB_DabangDB {
	public static Connection getConnection(Logger logger) {
		Connection conn = null;
		String userName = "myself";
		String password = "1234";
		String url = "jdbc:mariadb://localhost:3306/jb_dabang";
		String driver = "org.mariadb.jdbc.Driver";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			logger.info("Connection is successful");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
