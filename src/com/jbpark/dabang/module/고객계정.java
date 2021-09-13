package com.jbpark.dabang.module;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class 고객계정 {
	/**
	 * 새 고객 아이디가 구문이 바른지, 이미 사용 중인 것은 아닌지 검사
	 * 
	 * @param userId 새 고객 아이디
	 * @param reasons 아이디가 부적절한 이유 목록
	 * @return 아이디 사용 가능 여부(참: 사용 가능)
	 */
	public static boolean isGoodNewUserId(String userId,
			List<String> reasons) {
		boolean usable = false;
		
		if (Utility.isValidID(userId)) {
			try {
				get고객SN(userId);
				reasons.add("이미 사용 중인 아이디입니다.");
			} catch (NoSuch고객Exception e) {
				usable = true;
			}
		} else { 
			reasons.add("아이디가 바른 형식이 아닙니다.");
		}
		return usable;
	}

	public static int get고객SN(String 고객Id) 
			throws NoSuch고객Exception {
		
		String getSNsql = "select 고객SN from 전통고객 " 
				+ "where 고객ID = '" + 고객Id + "'";
		try {
			Statement getStmt = DBCPDataSource.getConnection().
					createStatement();
			ResultSet rs = getStmt.executeQuery(getSNsql);

			if (rs.next()) {
				return rs.getInt(1);
			} else {
				String msg = "아이디 '" + 고객Id + "'인 고객은 없습니다.";
				throw new NoSuch고객Exception(msg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}	
}
