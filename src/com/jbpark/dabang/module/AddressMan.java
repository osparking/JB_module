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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

// @formatter:off
public class AddressMan {
	private static Logger logger = null; 

	private static Connection conn = getConnection();

	public static Connection getConnection() {
		Connection conn = null;
		String userName = "myself";
		String password = "1234";
		String url = "jdbc:mariadb://localhost:3306/jb_dabang";
		String driver = "org.mariadb.jdbc.Driver";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		AddressMan addressMan = new AddressMan();

		if (args.length > 0) {
			switch (args[0]) {
			case "search":
				Scanner scanner = new Scanner(System.in);
				while (true) {
					try {
						var key = addressMan.getAddrSearchKey(scanner);
						Integer pageNo;
						int rows = addressMan.getTotalRows(key);
						pageNo = addressMan.getWantedPage(scanner, rows);

						var result = addressMan.searchAddress(key, pageNo);
						System.out.println("결과 행 수: " + 
								result.getAddrCount());
						
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

	public static int getWantedPage(Scanner scanner, int rows) {
		String fmt = "주소 총 %d건, 총 %d페이지 채취가능."; 
		int pages = (int)Math.ceil(rows/20.0);
		int page = 1;
		String msg = String.format(fmt, rows, pages);
		System.out.println(msg);
		try {
			/**
			 * 가능한 페이지 범위 제시
			 */
			fmt = "\t(1~%d, 기본:1)";
			String pagePrompt = String.format(fmt, pages);
			return Utility.getIntegerValue(scanner, 
					"페이지 번호를 입력하세요.", pagePrompt, true);
		} catch (NoInputException e) {
		}
		return page;
	}

	public int getTotalRows(AddrSearchKey key) {
		String sqlCount = getAddressCountQuery();
		String sKey = getSearchCondString(key); 
		
		sqlCount = String.format(sqlCount, sKey);
		try (var stmt = conn.createStatement()){
			var rs = stmt.executeQuery(sqlCount);
			
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return -1;
	}
	
	public static int getCustAddrRows(int 고객sn) {
		StringBuilder sb = new StringBuilder(
				"select count(*) from 고객주소 주");
		sb.append(" where 주.고객SN = ");
		sb.append(고객sn);
		
		try (var stmt = conn.createStatement()){
			var rs = stmt.executeQuery(sb.toString());
			
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 고객에게 검색 키(들)을 입력받아 주소DB에서 유사한 주소 목록 채취하여 반환
	 * 
	 * @param scanner
	 * @return 검색 결과 (제한된 주소 목록과 총 주소 건수)
	 * @throws StopSearchingException
	 */
	public SearchResult searchAddress(AddrSearchKey key, int pageNo) 
			throws StopSearchingException {
		int maxRow = 20;
		int offset = maxRow * (pageNo - 1);
		var addresses = new RoadAddress[20]; 
		
		System.out.println(key + ", 한계: " 
				+ maxRow + "행" + ", 채취 페이지: " + pageNo);
		
		String sKey = getSearchCondString(key); 
		String sqlList = getAddressSelectQuery();
		sqlList = String.format(sqlList, sKey, maxRow, offset);
		
		SearchResult result = null;
		
		try (var stmt = conn.createStatement()){
			result = new SearchResult();
			var rs = stmt.executeQuery(sqlList);
			int idx = 0;
			while (rs != null && rs.next()) {
				var roadAddress = new RoadAddress(
						rs.getString(1), 
						rs.getString(2),
						rs.getString(3));
				addresses[idx++] = roadAddress;
			}
			result.setAddresses(addresses);
			result.setAddressCount(idx);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	private String getSearchCondString(AddrSearchKey key) {
		if (key.get건물본번() == null) {
			// 건물명 혹은 (건물 본번 없는)도로명
			return "(B.도로명 LIKE '%" + key.get도로_건물() + "%' or "
					+ "D.시군구건물명 LIKE '%" + key.get도로_건물() + "%')";
		} else {
			// 도로명 및 건물 본번으로 검색
			return "B.도로명 LIKE '" + key.get도로_건물() + "' " 
					+ "AND A.건물본번 LIKE '%" + key.get건물본번() + "%'";
		}
	}

	public AddrSearchKey getAddrSearchKey(Scanner scanner) 
			throws StopSearchingException {
		var asKey = new AddrSearchKey();

		System.out.println("검색 키 입력 형태 => ");
		System.out.println("\t-도로명(예, 덕영대로895)");
		System.out.println("\t-도로명 건물번호(예, 덕영대로 89)");
		System.out.println("\t-건물명(예, 세진)");
		System.out.print("(멈추려면 그냥 엔터 치세요 :-) : ");
		try {
			String inputText = null;

			if (scanner.hasNextLine()) {
				inputText = scanner.nextLine().trim();
			}
			String[] searchKeys = inputText.split("\\s+");

			assert (searchKeys.length == 1 || searchKeys.length == 2);
			if (searchKeys.length == 1) {
				// 도로명 혹은 건물명
				if (searchKeys[0].length() == 0)
					throw new StopSearchingException();
				asKey.set도로_건물(searchKeys[0]);
			} else {
				// 도로명 그리고 건물본번
				asKey.set도로_건물(searchKeys[0]);
				asKey.set건물본번(searchKeys[1]);
			}
		} catch (NoSuchElementException | IllegalStateException | NumberFormatException e) {
			System.out.println();
			throw new StopSearchingException();
		}

		return asKey;
	}

	/**
	 * 
	 * @return select 문 - 한 s 인자.
	 */
	private String getAddressCountQuery() {
		StringBuilder sb = new StringBuilder("SELECT count(*) ");
		
		sb.append("FROM 도로명주소 A, 도로명코드 B, 부가정보 D "); 
		sb.append("WHERE A.도로명코드 = B.도로명코드");
		sb.append(" AND A.읍면동일련번호 = B.읍면동일련번호"); 
		sb.append(" AND A.관리번호 = D.관리번호" + " AND %s");
		return sb.toString();
	}

	private String getAddressSelectQuery() {
		var sb = new StringBuilder();
		
		sb.append("SELECT A.관리번호, A.기초구역번호 AS 새우편번호, "); 
		sb.append("concat( " + "B.시도명, ' ', ");
		sb.append("if (B.시군구 = '', '', concat(B.시군구,' ')), ");
		sb.append("case when B.읍면동구분 = 0 then concat(B.읍면동,' ') ");
		sb.append("else ''  ");
		sb.append("end,  ");
		sb.append("concat(B.도로명,' '), ");
		sb.append("case when A.지하여부 = 0 then ''  ");
		sb.append("	when A.지하여부 = 1 then '지하 '  ");
		sb.append("	when A.지하여부 = 2 then '공중 ' end, " + "A.건물본번, ");
		sb.append("if (A.건물부번 = 0, '', concat('-',A.건물부번)), ");
		sb.append("CASE WHEN (B.읍면동구분 = 0 AND D.공동주택여부 = 0) THEN '' ");
		sb.append("	WHEN (B.읍면동구분 = 0 AND D.공동주택여부 = 1) then ");
		sb.append("		case D.시군구건물명  ");
		sb.append("			when (D.시군구건물명 = '') then ''  ");
		sb.append("			else concat('(',D.시군구건물명,')') end  ");
		sb.append("	WHEN (B.읍면동구분 = 1 AND D.공동주택여부 = 0)  ");
		sb.append("		THEN concat('(',B.읍면동,')') ");
		sb.append("	WHEN (B.읍면동구분 = 1 AND D.공동주택여부 = 1)  ");
		sb.append("		THEN concat('(', B.읍면동 ");
		sb.append("			, case when (D.시군구건물명 = '') then ''  ");
		sb.append("				   else concat(',', D.시군구건물명) end ");
		sb.append("			,')')  ");
		sb.append("   	END  ");
		sb.append("   	) AS 도로명주소 ");
		sb.append("  FROM 도로명주소 A, 도로명코드 B, 부가정보 D  ");
		sb.append(" WHERE A.도로명코드    = B.도로명코드 ");
		sb.append("   AND A.읍면동일련번호 = B.읍면동일련번호 ");
		sb.append("   AND A.관리번호     = D.관리번호  ");
		sb.append("   AND %s limit %d offset %d");
		
		return sb.toString();
	}

	private void largeAdditionalText() {
		String file = "resources\\부가정보_경기도_utf_8.txt";
		int lines = 0;

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
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

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
			String sql = "insert into 도로명주소 "
					+ "values (" + 관리번호 +"," +  도로명코드 
					+ "," + 읍면동일련번호 + "," + 지하여부 
					+ "," + 건물본번 + "," + 건물부번 
					+ "," + 기초구역번호 + ")";
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
			String sql = "select count(*) row_count" 
					+ " from 도로명코드 도" 
					+ "	where 도.도로명코드 = " + 도로명코드
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
			String sql = "insert into 도로명코드(" 
					+ "도로명코드,읍면동일련번호,시도명," 
					+ "시군구,읍면동구분,도로명," 
					+ "읍면동,읍면동코드) " 
					+ "values ("
					+ 도로명코드 + "," + 읍면동일련번호 + ",'" + 시도명 
					+ "'," + 시군구명 + "," + 읍면동구분 + ",'" + 도로명 
					+ "'," + 읍면동 + "," + 읍면동코드 + ")";
			insStmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getCustAddrSql(int 고객sn) {
		StringBuilder sb = new StringBuilder("select 주.주소번호, ");
		
		sb.append("단.단지번호, 단.우편번호, 단.도로명주소, 주.상세주소 ");
		sb.append("from 고객주소 주 ");
		sb.append("	join 단지주소 단 on 단.단지번호 = 주.단지번호 ");
		sb.append("where 주.고객SN = ");
		sb.append(고객sn);
		sb.append(" order by 주.주소번호 desc");
		
		return sb.toString();
	}

	public static List<CustomerAddress> getCustomerAddresses(
			int 고객sn, int page) {
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					AddressMan.getCustAddrSql(고객sn));
			var addresses = new ArrayList<CustomerAddress>();
			
			while (rs.next()) {
				addresses.add(new CustomerAddress(
						rs.getString(1), 
						Integer.parseInt(rs.getString(2)),
						rs.getInt(3), rs.getString(4), 
						rs.getString(5)));
			}
			return addresses;
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return null;
	}	

	public static void showCustomerAddresses(Logger logger,
			List<CustomerAddress> addresses) {
		AddressMan.logger = logger;
		//var addresses = getCustomerAddresses(고객sn, page);
	
		// 고객 역대 입력 주소 표시
		System.out.println("고객 주소 목록");
		if (addresses.size() == 0) {
			System.out.println(": 없습니다.");
		} else {
			for (int i=0; i<addresses.size(); i++) {
				System.out.println("\t" + (i+1) + "." 
						+ addresses.get(i));
			}
		}
	}
}
