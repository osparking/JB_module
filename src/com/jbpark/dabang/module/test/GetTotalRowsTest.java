package com.jbpark.dabang.module.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jbpark.dabang.module.AddrSearchKey;
import com.jbpark.dabang.module.AddressMan;
import com.jbpark.dabang.module.SearchResult;
import com.jbpark.dabang.module.StopSearchingException;

public class GetTotalRowsTest {
	AddressMan aMan = new AddressMan();

	@Test
	public void testGetTotalRows() {
		var key = new AddrSearchKey();

		key.set도로_건물("팔달");
		int rows = aMan.getTotalRows(key);
		assertTrue(rows == 2045);
	}
}
