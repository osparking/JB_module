package com.jbpark.dabang.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import jbpark.utility.JB_FileHandler;

public class AddressMan {
	static Connection conn = getConnection();
	private static Logger logger = Logger.getLogger("com.jbpark.dabang");
	{
		logger.setLevel(Level.CONFIG);
//		logger.setUseParentHandlers(false);
		int LOG_ROTATION_COUNT = 10;
		JB_FileHandler handler;
		try {
			String logFile = "D:/LOG/JB_module";
			System.out.println("로그파일: " + logFile + ".*.log.*");
			handler = new JB_FileHandler(logFile + ".%g.log", 0, LOG_ROTATION_COUNT);
			handler.setLevel(Level.CONFIG);
			logger.addHandler(handler);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

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
			case "search":
				Scanner scanner = new Scanner(System.in);
				while (true) {
					try {
						addressMan.search(scanner);
					} catch (StopSearchingException sse) {
						break;
					}
				}
				logger.info("종료");
				System.out.println("종료");
				break;

			case "code":
				addressMan.smallTxt2TableRoad();
				break;

			case "address":
				addressMan.largeAddressTxt2Table();
				break;

			case "additional":
				System.out.println(LocalDateTime.now());
				addressMan.largeAdditionalText();
				System.out.println(LocalDateTime.now());
				break;

			default:
				break;
			}
		}
	}

	private void search(Scanner scanner) throws StopSearchingException {
		try  {
			//@formatter:off
			String sql = "SELECT  A.기초구역번호 AS 새우편번호, " 
					+ "concat( " + "B.시도명, ' ', "
					+ "if (B.시군구 = '', '', concat(B.시군구,' ')), " 
					+ "case when B.읍면동구분 = 0 then concat(B.읍면동,' ') "
					+ "else ''  " 
					+ "end,  " 
					+ "concat(B.도로명,' '), " 
					+ "case when A.지하여부 = 0 then ''  "
					+ "	when A.지하여부 = 1 then '지하 '  " 
					+ "	when A.지하여부 = 2 then '공중 ' end, " + "A.건물본번, "
					+ "if (A.건물부번 = 0, '', concat('-',A.건물부번)), " 
					+ "CASE WHEN (B.읍면동구분 = 0 AND D.공동주택여부 = 0) THEN '' "
					+ "	WHEN (B.읍면동구분 = 0 AND D.공동주택여부 = 1) then " 
					+ "		case D.시군구건물명  "
					+ "			when (D.시군구건물명 = '') then ''  " 
					+ "			else concat('(',D.시군구건물명,')') end  "
					+ "	WHEN (B.읍면동구분 = 1 AND D.공동주택여부 = 0)  " 
					+ "		THEN concat('(',B.읍면동,')') "
					+ "	WHEN (B.읍면동구분 = 1 AND D.공동주택여부 = 1)  " 
					+ "		THEN concat('(', B.읍면동 "
					+ "			, case when (D.시군구건물명 = '') then ''  "
					+ "				   else concat(',', D.시군구건물명) end " 
					+ "			,')')  " 
					+ "   	END  "
					+ "   	) AS 도로명주소 " 
					+ "  FROM 도로명주소 A, 도로명코드 B, 부가정보 D  " 
					+ " WHERE A.도로명코드    = B.도로명코드 "
					+ "   AND A.읍면동일련번호 = B.읍면동일련번호 " 
					+ "   AND A.관리번호     = D.관리번호  "
					+ "   AND %s;";
			
			AddrSearchKey addrSearchKey = getAddrSearchKey(scanner);

			boolean cond1 = addrSearchKey.get도로명일부() != null;
			boolean cond2 = addrSearchKey.get도로명일부() != null
					&& addrSearchKey.get건물본번일부() != null;
			boolean cond3 = addrSearchKey.get건물명일부() != null;
			
			assert ( cond1 || cond2 || cond3)
				: "건물본번 있으나 도로명은 없음!";
			
			String sCond = null;
			if (addrSearchKey.get건물명일부() == null) {
				sCond = "B.도로명 LIKE ?";
				if (addrSearchKey.get건물본번일부() != null) {
					sCond += " and A.건물본번 like ?";
				}
			} else {
				sCond = "D.시군구건물명 LIKE ?";
			}

			String stmt = String.format(sql, sCond);
			var ps = conn.prepareStatement(stmt);

			if (addrSearchKey.get건물명일부() == null) {
				// 덕영대로895 => 42 행
				ps.setString(1, addrSearchKey.get도로명일부() + "%");
				if (addrSearchKey.get건물본번일부() != null) {
					// 덕영대로 89 => 5 행
					ps.setString(2, addrSearchKey.get건물본번일부() + "%");
				}
			} else {
				ps.setString(1, addrSearchKey.get건물명일부() + "%");
			}
			//@formatter:on
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
			String timeLabel = LocalTime.now().format(dtf);
			logger.config(addrSearchKey + ": " + timeLabel);
			System.out.println(addrSearchKey + ": " + timeLabel);

			String msg = "결과 행 수: " + getRoadAddrList(ps).size();
			logger.config(msg);
			System.out.println(msg);
			getRoadAddrList(ps).forEach(System.out::println);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private AddrSearchKey getAddrSearchKey(Scanner scanner) 
			throws StopSearchingException{
		var asKey = new AddrSearchKey();

		System.out.print("검색 옵션 입력(1:도로명, 2:도로명+건물번호, 3:건물명): ");
		try {
			String inputText = null;
			
			if (scanner.hasNextLine()) {
				inputText = scanner.nextLine().trim();
			}
			int searchOption = Integer.parseInt(inputText);

			switch (searchOption) {
			case 1:
				// 도로명(일부)만 입력
				System.out.print("도로명(일부): ");
				asKey.set도로명일부(scanner.nextLine().trim());
				break;
			case 2:
				// 도로명(일부)+건물본번(일부) 입력
				System.out.print("도로명(일부): ");
				asKey.set도로명일부(scanner.nextLine().trim());
				System.out.print("건물본번(일부): ");
				asKey.set건물본번일부(scanner.nextLine().trim());
				break;
			case 3:
				// 건물명(일부)만 입력
				System.out.print("건물명(일부): ");
				asKey.set건물명일부(scanner.nextLine().trim());
				break;
			default:
				break;
			}
		} catch (NoSuchElementException 
				| IllegalStateException 
				| NumberFormatException e) {
			System.out.println();
			throw new StopSearchingException();
		}

		return asKey;
	}

	/**
	 * 관리번호 값 도로명주소 테이블 존재여부 판단
	 * 
	 * @param ps
	 * @return 존재 때 true, 비 존재 때 false
	 */
	private List<RoadAddress> getRoadAddrList(PreparedStatement ps) {
		var roadAddrList = new ArrayList<RoadAddress>();

		try (ResultSet rs = ps.executeQuery()) {
			while (rs != null && rs.next()) {
				var roadAddress = new RoadAddress(rs.getString(1), rs.getString(2));
				roadAddrList.add(roadAddress);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roadAddrList;
	}

	private void largeAdditionalText() {
		String file = "resources\\부가정보_경기도_utf_8.txt";
		int lines = 0;
		//@formatter:off
		try (BufferedReader br = 
				Files.newBufferedReader(Path.of(file))) { // v2-c2
			int selectionCount = 0;
			int printUnit = 5000;
			String sql = "select count(*) from 도로명주소 도 "
					+ "where 도.관리번호= ?";
			var ps = conn.prepareStatement(sql);
			String line = null;
			String iSql = "insert into 부가정보 values (?,?,?)";
			var iPs = conn.prepareStatement(iSql);

			while ((line = br.readLine()) != null) {
				if (++lines % printUnit == 0) {
					System.out.println(lines / printUnit + " ");
					System.out.println(LocalDateTime.now());
				}
				String[] items;

				items = line.split("\\|", -1);
				ps.setString(1, items[0]);
				if (fKeyIn도로명주소(ps)) {
					selectionCount += 
							insert2TableAdditional(iPs, items);
				}
			}

			System.out.println(lines + " 행 읽힘.");
			System.out.println(selectionCount + " 행 삽입됨.");
		} catch (IOException e) {
			e.printStackTrace();
		}		
		//@formatter:on
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// @formatter:off
	private int insert2TableAdditional(
			PreparedStatement iPs, String[] items) {
		int insertionCount = 0;
		// [4111112900100230002049701, 4111156000, 파장동,
		// 16201, , , , , 0]
		String 관리번호 = items[0];
		String 시군구건물명 = items[7];
		String 공동주택여부 = items[8];

		try {
			iPs.setString(1, 관리번호);
			iPs.setString(2, 시군구건물명);
			iPs.setString(3, 공동주택여부);
			insertionCount = iPs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return insertionCount;
	}
	//@formatter:on

	/**
	 * 관리번호 값 도로명주소 테이블 존재여부 판단
	 * 
	 * @param ps
	 * @return 존재 때 true, 비 존재 때 false
	 */
	private boolean fKeyIn도로명주소(PreparedStatement ps) {
		// select count(*) from 도로명주소 도
		// where 도.관리번호 = 4117310400112330000008128;
		boolean fkExists = false;

		try (ResultSet rs = ps.executeQuery()) {
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
					System.out.print(lines / 1000 + " ");
				String[] items;
				String line = scanner.nextLine();

				items = line.split("\\|", -1);
				if (fKeyInSmall(items[1], items[2])) {
					selectionCount += insert2TableAddress(items);
				}
			}
			System.out.println(selectionCount + " 행 삽입됨.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private int insert2TableAddress(String[] items) {
		int insertionCount = 0;
		String 관리번호 = items[0];
		String 도로명코드 = items[1];
		String 읍면동일련번호 = items[2];
		String 지하여부 = items[3];
		String 건물본번 = items[4];
		String 건물부번 = items[5];
		String 기초구역번호 = items[6];

		try {
			Statement insStmt = conn.createStatement();
			//@formatter:off
			String sql = "insert into 도로명주소 "
					+ "values (" + 관리번호 +"," +  도로명코드 
					+ "," + 읍면동일련번호 + "," + 지하여부 
					+ "," + 건물본번 + "," + 건물부번 
					+ "," + 기초구역번호 + ")";
			//@formatter:on
			insertionCount = insStmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return insertionCount;
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
			// @formatter:off
			String sql = "select count(*) row_count" 
					+ " from 도로명코드 도" 
					+ "	where 도.도로명코드 = " + 도로명코드
					+ " and 도.읍면동일련번호 = " + 읍면동일련번호;
			// @formatter:on
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
			//@formatter:off
			String sql = "insert into 도로명코드(" 
					+ "도로명코드,읍면동일련번호,시도명," 
					+ "시군구,읍면동구분,도로명," 
					+ "읍면동,읍면동코드) " 
					+ "values ("
					+ 도로명코드 + "," + 읍면동일련번호 + ",'" + 시도명 
					+ "'," + 시군구명 + "," + 읍면동구분 + ",'" + 도로명 
					+ "'," + 읍면동 + "," + 읍면동코드 + ")";
			//@formatter:on
			insStmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
