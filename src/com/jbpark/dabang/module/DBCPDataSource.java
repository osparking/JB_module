package com.jbpark.dabang.module;

import java.sql.SQLException;
import java.sql.Connection;
import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSource {
	private static BasicDataSource ds = new BasicDataSource();

	static {
		ds.setUrl("jdbc:mariadb://localhost:3306/jb_dabang");
		ds.setUsername("myself");
		ds.setPassword("1234");
		ds.setMinIdle(5); // pool 에 유지할 최소 유휴 connection 수
		ds.setMaxIdle(10); // pool 에 유지할 최대 유휴 connection 수
		ds.setMaxOpenPreparedStatements(100); // 최대 pstmt 수
	}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	private DBCPDataSource() {
	}

}
