package com.jbpark.dabang.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
		AddressMan addressMan = new AddressMan();
		
		if (args.length > 0) {
			switch (args[0]) {
			case "road":
				addressMan.smallTxt2TableRoad();
				break;

			case "address":
			default:
				addressMan.largeAddressTxt2Table();
				break;
			}
		} else {
			addressMan.largeAddressTxt2Table();
		}
	}

	/**
	 * 큰 경기도 주소 텍스트 파일을 읽어서 수원 팔달구 해당 행들을 테이블로 삽입한다.
	 */
	private void largeAddressTxt2Table() {
		String txtWhole = "resources\\주소_경기도.txt";
		File addrFile = new File(txtWhole);
		int lines = 0;
		
		try (Scanner scanner = new Scanner(addrFile)) {
			int selectionCount = 0;
			while (scanner.hasNextLine()) {
				if (++lines % 1000 == 0)
					System.out.print(lines/1000 + " ");
				String[] items;
				String line = scanner.nextLine();

				items = line.split("\\|", -1);
				if (fKeyInSmall(items[1], items[2]))
					selectionCount++;
			}
			System.out.println(selectionCount + " 행 식별됨.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 두 외래키 값을 받아서 도로명코드 테이블에 존재하는 값인지 확인
	 * 
	 * @param 도로명코드
	 * @param 읍면동일련번호
	 * @return 키가 테이블에 존재하면 참, 아니면 거짓
	 */
	private boolean fKeyInSmall(String 도로명코드, String 읍면동일련번호) {
		boolean fkExists = false;
		try {
			Statement insStmt = conn.createStatement();
			String sql = "select count(*) row_count" + " from 도로명코드 도" + "	where 도.도로명코드 = " + 도로명코드
					+ " and 도.읍면동일련번호 = " + 읍면동일련번호;
			ResultSet rs = insStmt.executeQuery(sql);

			if (rs != null && rs.next()) {
				if (rs.getInt(1) > 0) {
					fkExists = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fkExists;
	}

	private void smallTxt2TableRoad() {
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
			// 시도명,시군구,읍면동구분,도로명,읍면동,읍면동코드)
			// values (411152012001,0,
			// '경기도','수원시 팔달구',2,'창룡대로',null,null)
			String sql = "insert into 도로명코드(" + "도로명코드,읍면동일련번호,시도명," + "시군구,읍면동구분,도로명," + "읍면동,읍면동코드) " + "values ("
					+ 도로명코드 + "," + 읍면동일련번호 + ",'" + 시도명 + "'," + 시군구명 + "," + 읍면동구분 + ",'" + 도로명 + "'," + 읍면동 + ","
					+ 읍면동코드 + ")";
			insStmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
