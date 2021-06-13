package com.jbpark.dabang.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AddressMan {
	static Connection conn = getConnection();

	static Connection getConnection() {
		Connection conn = null;
		String userName = "myself";
		String password = "1234";
		String url = "jdbc:mariadb://localhost:3306/address_road_gg";
		String driver = "org.mariadb.jdbc.Driver";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			System.out.println("Connection is successful");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void main(String[] args) {
		String txtSmall = "resources\\도로명코드_utf_8_small.txt";
		File addrFile = new File(txtSmall);

		try (Scanner scanner = new Scanner(addrFile)) {
			int insertionCount = 0;
			while (scanner.hasNextLine()) {
				String[] addrItems;
				String line = scanner.nextLine();

				addrItems = line.split("\\|", -1);
				insertRoadCode(addrItems);
				insertionCount++;
			}
			System.out.println(insertionCount + " 행 생성됨.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void insertRoadCode(String[] addrItems) {
		long 도로명코드 = Long.parseLong(addrItems[0]);
		String 도로명 = addrItems[1];
		int 읍면동일련번호 = Integer.parseInt(addrItems[3]);
		String 시도명 = addrItems[4];
		String 시군구명 = addrItems[6];
		String 읍면동 = addrItems[8];
		int 읍면동구분 = Integer.parseInt(addrItems[10]);
		String 읍면동코드 = "null"; // 자료 없음
		
		if (addrItems[11].length() > 0) {
			읍면동코드 = addrItems[11];
		}
		
		if (시군구명.length() == 0) {
			시군구명 = "null";
		} else {
			시군구명 = "'" + 시군구명 + "'";
		}
		
		if (읍면동.length() == 0) {
			읍면동 = "null"; 
		} else {
			읍면동 = "'" + 읍면동 + "'"; 
		}
		
		try {
			Statement insStmt = conn.createStatement();
			// insert into 도로명코드(도로명코드,읍면동일련번호,
			//		시도명,시군구,읍면동구분,도로명,읍면동,읍면동코드) 
			// values (411152012001,0,
			//		'경기도','수원시 팔달구',2,'창룡대로',null,null)
			String sql = "insert into 도로명코드("
					+ "도로명코드,읍면동일련번호,시도명,"
					+ "시군구,읍면동구분,도로명,"
					+ "읍면동,읍면동코드) "
					+ "values (" 
					+ 도로명코드 +"," + 읍면동일련번호+ ",'" + 시도명 + "'," 
					+ 시군구명 + "," + 읍면동구분 + ",'" + 도로명 + "'," 
					+ 읍면동 + "," + 읍면동코드 + ")";
			insStmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
