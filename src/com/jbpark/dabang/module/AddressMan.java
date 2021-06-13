package com.jbpark.dabang.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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
		createSmallRoadCodeTable();
//		String txt = "resources\\도로명코드_utf_8.txt";
//		File addrFile = new File(txt);
//
//		try (Scanner scanner = new Scanner(addrFile)) {
//			int lines = 0;
//
//			while (scanner.hasNextLine()) {
//				String[] addrItems;
//				String line = scanner.nextLine();
//				
//				addrItems = line.split("\\|", -1);
//				if (addrItems[4].equals("경기도") 
//						&& addrItems[6].equals("수원시 팔달구") 
//						&& addrItems[12].equals("0")) /* 0: 사용 중 */
//				{  
//					insertRoadCode(addrItems);
//				}
//			}
//			System.out.println(lines);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}

	private static void createSmallRoadCodeTable() {
		String txt = "resources\\도로명코드_utf_8.txt";
		String txtSmall = "resources\\도로명코드_utf_8_small.txt";
		File addrFile = new File(txt);
//		File addrFileSmall = new File(txtSmall);
//		addrFileSmall.createNewFile(); // if file already exists will do nothing 

		try (Scanner scanner = new Scanner(addrFile);
				PrintWriter oFile = new PrintWriter(new FileOutputStream(txtSmall, false))) {				
			int lines = 0;

			while (scanner.hasNextLine()) {
				String[] addrItems;
				String line = scanner.nextLine();
				
				addrItems = line.split("\\|", -1);
				if (addrItems[4].equals("경기도") 
						&& addrItems[6].equals("수원시 팔달구") 
						&& addrItems[12].equals("0")) /* 0: 사용 중 */
				{  
					oFile.println(line);
				}
				lines++;
			}
			oFile.close();
			System.out.println(lines);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
		}
		
	}

	private static void insertRoadCode(String[] addrItems) {
		long 도로명코드 = Long.parseLong(addrItems[0]);
		String 도로명 = addrItems[1];
		int 읍면동일련번호 = Integer.parseInt(addrItems[3]);
		String 시도명 = addrItems[4];
		String 시군구명 = addrItems[6];
		String 읍면동명 = addrItems[8];
		int 읍면동구분 = Integer.parseInt(addrItems[10]);

		int 읍면동코드 = -1; // 자료 없음
		if (addrItems[11].length() > 0) {
			읍면동코드 = Integer.parseInt(addrItems[11]);
		}
		System.out.print("도로명코드: " + 도로명코드);
		System.out.print(", " + 시도명);
		if (시군구명.length() > 0 ^ 읍면동명.length() > 0) {
			if (시군구명.length() > 0)
				System.out.print(", " + 시군구명);
			else
				System.out.print(", " + 읍면동명);
		} else {
			System.out.println();
		}
		System.out.print(", " + 도로명);
		System.out.print(", 읍면동번호: " + 읍면동일련번호);
		System.out.print(", 읍면동구분: " + 읍면동구분);
		System.out.println(", 읍면동코드: " + 읍면동코드);
	}
}
