package com.jbpark.dabang.module.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jbpark.dabang.module.AddrSearchKey;
import com.jbpark.dabang.module.AddressMan;
import com.jbpark.dabang.module.SearchResult;
import com.jbpark.dabang.module.StopSearchingException;

public class AddressTest {

	@Test
	public void test() {
		AddressMan aMan = new AddressMan();
		var key = new AddrSearchKey();
		
		key.set도로_건물("세진");
		try {
			SearchResult result = aMan.search(key, 1);
			int count = result.getTotalRow();
			assertTrue(count == 2);
		} catch (StopSearchingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
