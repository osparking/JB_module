package com.jbpark.dabang.module;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//@formatter:off
public class 주소관리 {

	/**
	 * 고객의 역대 주소 중 한 주소의 상세 주소 부분을 갱신한다.
	 * 
	 * @param addrNo 역대 주소 번호
	 * @param detailAddr 새 상세 주소
	 * @return 갱신된 레코드 수
	 */
	public static int updateCustAddress(int addrNo, 
			String detailAddr) {
		String sql = "update 고객주소 set 상세주소 = ? where 주소번호 = "
				+ addrNo;
		
		try (var pstmt = DBCPDataSource.getConnection().
				prepareStatement(sql)) {
			pstmt.setString(1, detailAddr);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 고객ID, 단지번호, 상세주소 입력받아 고객주소 행 저장 
	 * 
	 * @param 고객SN 고객일련번호(고객id와 다름)
	 * @param 단지번호 단지일련번호
	 * @param detailedAddr 상세주소
	 * @return
	 */
	public static int save고객주소(int 고객SN, int 단지번호, 
			String detailedAddr) {
		String insert = "insert into 고객주소(고객SN, 단지번호, 상세주소) "
				+ "values (%s, %s, '%s')";
		String iSql = String.format(insert, 고객SN, 단지번호, detailedAddr);

		try (var stmt = DBCPDataSource.getConnection().createStatement()) {
			return stmt.executeUpdate(iSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	static private int get단지주소번호(String mgmtNumber) {
		String sql = "select c.단지번호 from 단지주소 c where c.관리번호 = ?";
		try {
			var ps = DBCPDataSource.getConnection().prepareStatement(sql);
			ps.setString(1, mgmtNumber);
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	static private int save단지번호_주소(int 고객SN, String detailedAddr, 
			RoadAddress address) {
		// 관리번호 단지주소 등록 여부 판단
		int 단지번호 = get단지주소번호(address.getMgmtNumber());

		if (단지번호 < 1) {
			// 비등록이면, 단지주소 등록(삽입)
			단지번호 = save단지주소(address);
		}
		// 고객주소 행 삽입(단지주소자동번호 등 사용)
		save고객주소(고객SN, 단지번호, detailedAddr);
		return 단지번호;
	}

	static public int save단지주소Test(RoadAddress address) {
		return save단지주소(address);
	}

	static private int save단지주소(RoadAddress address) {
		String iSql = String.format("insert into 단지주소" + 
				" (관리번호, 우편번호, 도로명주소) values ('%s', %s, '%s');",
				address.getMgmtNumber(), address.getNewZipcode(), 
				address.getRoadName());
		ResultSet rs = null;

		try (var stmt = DBCPDataSource.getConnection().createStatement()) {
			stmt.executeUpdate(iSql, Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
