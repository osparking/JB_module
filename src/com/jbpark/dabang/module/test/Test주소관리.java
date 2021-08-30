package com.jbpark.dabang.module.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.jbpark.dabang.module.AddrSearchKey;
import com.jbpark.dabang.module.AddressMan;

class Test주소관리 {
	@Test
	void testGetTotalRowsMap1() {
		AddrSearchKey searchKey = new AddrSearchKey();

		searchKey.set도로_건물("덕영대로895");

		var totalRowsMap = AddressMan.getTotalRowsMap(searchKey);

		System.out.println(new Gson().toJson(totalRowsMap));
		assertEquals(totalRowsMap.get("totalRows"), 42);
	}

	@Test
	void testGetTotalRowsMap2() {
		AddrSearchKey key건물 = new AddrSearchKey();
		
		key건물.set도로_건물("세진");
		
		var totalRowsMap = AddressMan.getTotalRowsMap(key건물);
		
		System.out.println(new Gson().toJson(totalRowsMap));
		assertEquals(totalRowsMap.get("totalRows"), 2);
	}

	@Test
	void testGetTotalRowsMap3() {
		AddrSearchKey key건물_본번 = new AddrSearchKey();
		
		key건물_본번.set도로_건물("덕영대로");
		key건물_본번.set건물본번("89");
		
		var totalRowsMap = AddressMan.getTotalRowsMap(key건물_본번);
		
		System.out.println(new Gson().toJson(totalRowsMap));
		assertEquals(totalRowsMap.get("totalRows"), 6);
	}
}
