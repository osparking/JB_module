package com.jbpark.dabang.module.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.jbpark.dabang.module.고객계정;

class Test고객계정 {
	
	@Test
	void testUpdatePasswd_good() {
		String userId = "myself3";
		var reasons = new LinkedList<String>();
		assertTrue(고객계정.updatePasswd(
				userId, "qQ1!", "qQ1!", reasons));
	}
	
	@Test
	void testProcessLogin_passwd() {
		String userId = "myself";
		var reasons = new LinkedList<String>();
		고객계정.processLogin(userId, "1234", reasons);
		assertTrue(containsString(reasons, "비밀번호 오류"));
	}

	@Test
	void testProcessLogin_userId() {
		String userId = "nobody";
		var reasons = new LinkedList<String>();
		고객계정.processLogin(userId, "qQ1!", reasons);
		assertTrue(containsString(reasons, "ID 오류"));
	}
	
	@Test
	void testProcessLogin_good() {
		String userId = "myself";
		var reasons = new LinkedList<String>();
		assertTrue(고객계정.processLogin(userId, "qQ1!", reasons));
	}
	
	@Test
	void testRegisterUser_pw_syntax() {
		String userId = "myself99";
		var reasons = new LinkedList<String>();
		고객계정.registerUser(userId, "1234", reasons);
		assertTrue(containsString(reasons, "구문 오류"));
	}
	
	@Test
	void testRegisterUser_id_taken() {
		String userId = "myself";
		var reasons = new LinkedList<String>();
		고객계정.registerUser(userId, "qQ1!", reasons);
		assertTrue(containsString(reasons, "이미 사용 중"));
	}
	
	@Test
	void testRegisterUser_id_syntax() {
		String userId = "3myself";
		var reasons = new LinkedList<String>();
		고객계정.registerUser(userId, "qQ1!", reasons);
		assertTrue(containsString(reasons, "구문 오류"));
	}
	
	@Test
	void testRegisterUser_good() {
		String userId = "myself3";
		var reasons = new LinkedList<String>();
		assertTrue(고객계정.registerUser(userId, "qQ1!", reasons));
	}
	
	@Test
	void testIsGoodNewUserId_taken() {
		String userId = "myself";
		
		var reasons = new LinkedList<String>();
		고객계정.isGoodNewUserId(userId, reasons);
		assertTrue(containsString(reasons, "이미 사용 중"));
	}
	
	@Test
	void testIsGoodNewUserId_syntax() {
		String userId = "3myself";
		
		var reasons = new LinkedList<String>();
		고객계정.isGoodNewUserId(userId, reasons);
		assertTrue(containsString(reasons, "구문 오류"));
	}
	
	/**
	 * 문자열 목록 중 오류 메시지가 포함되었는지 검사
	 * @param reasons 문자열 목록
	 * @param errorMsg 오류 메시지
	 * @return 포함된 경우 참; 아니면 거짓
	 */
	private boolean containsString(
			List<String> reasons, String errorMsg) {
		boolean result = false;
		
		if (reasons != null) {
			for (String reason: reasons) {
				if (reason.contains(errorMsg)) {
					result = true;
					break;
				}
			};
		}
		return result;
	}

	@Test
	void testIsGoodNewUserId_good() {
		String userId = "myself4";
		
		var reasons = new LinkedList<String>();
		assertTrue(고객계정.isGoodNewUserId(userId, reasons));
	}
}
