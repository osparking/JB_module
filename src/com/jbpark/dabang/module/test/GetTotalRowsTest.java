package com.jbpark.dabang.module.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jbpark.dabang.module.AddrSearchKey;
import com.jbpark.dabang.module.AddressMan;

public class GetTotalRowsTest {
	@Test
	public void testGetTotalRows() {
		var key = new AddrSearchKey();

		key.set도로_건물("팔달");
		int rows = AddressMan.getTotalRows(key);
		assertTrue(rows == 2045);
	}
}
