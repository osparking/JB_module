package com.jbpark.dabang.module;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.jbpark.utility.SecureMan;

public class 고객계정 {
	
	/**
	 * 주어진 고객 정보를 사용하여 고객을 등록한다. 
	 * @param userId 고객 사용자 ID
	 * @param passwd 비밀번호
	 * @param reasons 등록에 실패한 이유
	 * @return 등록 성공의 경우 참, 아니면 거짓
	 */
	public static boolean registerUser(String userId,
			String passwd, List<String> reasons) {
		
		boolean result = false;
		
		if (isGoodNewUserId(userId, reasons)) {
			if (Utility.isValidPassword(passwd)) {
				byte[] salt = SecureMan.getSalt();
				byte[] pwdEncd = SecureMan.encryptPassword(passwd, 
						salt);		
				result = (1 == save전통고객(userId, salt, pwdEncd));
			} else {
				reasons.add("고객 비밀번호 구문 오류입니다.");
			}
		} 
		return result;
	}
	
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
			reasons.add("고객 아이디 구문 오류입니다.");
		}
		return usable;
	}

	public static int save전통고객(String 고객Id, byte[] salt, 
			byte[] pwdEncd) {
		String iSql = "insert into 전통고객" + "(고객ID, "
				+ "고객이름, salt, password) " + "values (?, ?, ?, ?);";
		try {
			var iPs = DBCPDataSource.getConnection().
					prepareStatement(iSql);

			iPs.setString(1, 고객Id);
			iPs.setString(2, "아무개");
			iPs.setBytes(3, salt);
			iPs.setBytes(4, pwdEncd);

			return iPs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
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
