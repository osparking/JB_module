package com.jbpark.dabang.module;

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

	/**
	 * 한 고객의 역대 주소중 주소번호와 일치하는 주소 레코드 삭제
	 * 
	 * @param 주소번호 삭제될 주소번호
	 * @return 삭제된 주소 레코드 건수
	 */
	public static int deleteCustAddress(int 주소번호) {
		StringBuilder sb = new StringBuilder(
				"delete from 고객주소 where 주소번호 = " + 주소번호);
		
		try (Statement stmt = DBCPDataSource.getConnection().
				createStatement()) {
			return stmt.executeUpdate(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 고객주소 테이블에 특정 주소번호 일치 행이 존재 여부 검사
	 * 
	 * @param 주소번호 검색하려는 주소 레코드 키 값
	 * @return 참 - 존재할 때, 거짓 - 존재하지 않을 때
	 */
	public static boolean isGoodCustAddress(int 주소번호) {
		StringBuilder sb = new StringBuilder(
				"select count(*) from 고객주소 주");
		sb.append(" where 주.주소번호 = ");
		sb.append(주소번호);
		
		try (var stmt = DBCPDataSource.getConnection().
				createStatement()){
			var rs = stmt.executeQuery(sb.toString());
			
			if (rs.next()) {
				return rs.getInt(1) == 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
