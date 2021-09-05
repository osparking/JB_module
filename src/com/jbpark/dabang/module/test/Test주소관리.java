package com.jbpark.dabang.module.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.jbpark.dabang.module.AddrSearchKey;
import com.jbpark.dabang.module.AddressMan;
import com.jbpark.dabang.module.StopSearchingException;
import com.jbpark.dabang.module.주소관리;

//@formatter:off
class Test주소관리 {

	/** 한 고객의 특정 주소 레코드가 존재하는지 검사
	 * 
	 */
	@Test
	@Disabled
	void testIsGoodCustAddress() {
		boolean result = 주소관리.isGoodCustAddress(14);
		assertTrue(result);
	}
	/**
	 * 한 고객의 역대 주소 목록 중 특정 주소 레코드를 키 값을 받아 
	 * 삭제한다.
	 */
	@Test
	void testDeleteCustAddress() {
		int 주소번호 = 20;
		boolean exists = 주소관리.isGoodCustAddress(주소번호);
		assertTrue(exists);
		if (exists) {
			int rows = 주소관리.deleteCustAddress(주소번호);
			assertEquals(rows, 1);
		}
	}
	/**
	 * 한 고객의 역대 주소 중 특정 주소 번호 해당 상세주소를 
	 * 새 상세주소로 갱신한다.
	 */
	@Test
	@Disabled
	void testUpdateCustAddress() {
		int 주소번호 = 26; // 특정 주소 번호
		String new상세주소 = "304-4호"; // 새 상세주소
		
		int rows = 주소관리.updateCustAddress(주소번호, new상세주소);
		assertEquals(rows, 1);
	}
	
	/**
	 * 특정 고갱의 역대 입력 주소 중에서 지정된 페이지 크기로 요구된
	 * 페이지에 속하는 주소 목록을 반환받는지 시험하는 사례임.
	 */
	@Test
	@Disabled
	void testGetCustomerAddresses() {
		int 고객SN = 6;
		int pageNo = 1;
		var custAddrs = AddressMan.getCustomerAddresses(
				고객SN, 10, pageNo);
		
		custAddrs.stream().forEach(System.out::println);
		assertNotNull(custAddrs);
	}
	
	/**
	 * save고객주소의 기능을 시험한다. 
	 */
	@Test
	@Disabled
	void testSave고객주소() {
		int myselfID = 6;
		int 단지번호 = 6; // 덕영대로 899
		String detail = "304호";
		
		int rc = 주소관리.save고객주소(myselfID, 단지번호, detail);
		assertEquals(rc, 1);
	}
	
	/** 
	 * 도로명 주소 건수 검색(총 2 사례 중 1) - 검색키: 도로명/건물명
	 * 
	 * @throws StopSearchingException
	 */
	@Test
	@Disabled
	void testGetTotalRowsMap1() {
		AddrSearchKey searchKey = new AddrSearchKey();

		searchKey.set도로_건물("덕영대로895");

		var totalRowsMap = AddressMan.getTotalRowsMap(searchKey);

		System.out.println(new Gson().toJson(totalRowsMap));
		assertEquals(totalRowsMap.get("totalRows"), 42);
	}	

	/** 도로명 주소 건수 검색(총 2 사례 중 1) - 검색키: 도로명&건물번호(=건물본번)
	 * 
	 * @throws StopSearchingException
	 */
	@Test
	@Disabled
	void testGetTotalRowsMap2() {
		AddrSearchKey key건물_본번 = new AddrSearchKey();
		
		key건물_본번.set도로_건물("덕영대로");
		key건물_본번.set건물본번("89");
		
		var totalRowsMap = AddressMan.getTotalRowsMap(key건물_본번);
		
		System.out.println(new Gson().toJson(totalRowsMap));
		assertEquals(totalRowsMap.get("totalRows"), 6);
	}
	
	/** 도로명 주소 목록 검색(총 2 사례 중 1) - 검색키: 도로명/건물명
	 *  도로명 주소 건수 검색(총 2 사례 중 1) 검색 결과, pageSize 사용하여
	 *  주소검색결과 주소 목록 크기(=2) 계산 가능	 
	 * @throws StopSearchingException
	 */
	@Test
	@Disabled
	void testGetAddressListMap1() throws StopSearchingException {
		AddrSearchKey searchKey = new AddrSearchKey();
	
		searchKey.set도로_건물("덕영대로895");
			 
		var searchResultMap = AddressMan.getAddressListMap(
				searchKey, 20, 3); // 20: pageSize
	
		System.out.println(new Gson().toJson(searchResultMap));
		assertEquals(searchResultMap.get("주소검색결과").getAddresses().
				size(), 2); // 2: 주소검색결과 주소 목록 크기		
	}
	
	/** 도로명 주소 목록 검색(총 2 사례 중 2) - 검색키: 도로명&건물번호(=건물본번)
	 * 
	 * @throws StopSearchingException
	 */	
	@Test
	@Disabled
	void testGetAddressListMap2() throws StopSearchingException {
		AddrSearchKey searchKey = new AddrSearchKey();
		
		searchKey.set도로_건물("덕영대로");
		searchKey.set건물본번("89");
		
		var searchResultMap = AddressMan.getAddressListMap(
				searchKey, 20, 1);
		
		System.out.println(new Gson().toJson(searchResultMap));
		assertEquals(searchResultMap.get("주소검색결과").getAddresses().
				size(), 6);		
	}
	//@formatter:on

}
