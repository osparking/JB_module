package com.jbpark.dabang.module.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import com.jbpark.dabang.module.AddrSearchKey;
import com.jbpark.dabang.module.AddressMan;
import com.jbpark.dabang.module.SearchResult;
import com.jbpark.dabang.module.StopSearchingException;

public class AddressTest {
	AddressMan aMan = new AddressMan();

	@Test
	public void test팔달_주소수() {
		var key = new AddrSearchKey();
		
		key.set도로_건물("팔달");
		try {
			SearchResult result = aMan.searchAddress(key, 1);
			int count = result.getTotalRow();
			assertTrue(count == 2045 && result.getAddrCount() == 20);			
		} catch (StopSearchingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Disabled("9가 고객SN 인 것이 확실하지 않으므로...")
	public void testDisplayCustomerAddress() {
		var addressList = aMan.displayCustomerAddresses(9, null);
		assertNotNull(addressList);
	}
}
