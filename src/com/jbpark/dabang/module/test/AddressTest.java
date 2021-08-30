package com.jbpark.dabang.module.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.jbpark.dabang.module.AddrSearchKey;
import com.jbpark.dabang.module.AddressMan;
import com.jbpark.dabang.module.SearchResult;
import com.jbpark.dabang.module.StopSearchingException;
import com.jbpark.utility.JLogger;

public class AddressTest {
	AddressMan aMan = new AddressMan();

	@Test
	public void test팔달_주소수() {
		var key = new AddrSearchKey();
		
		key.set도로_건물("팔달");
		try {
			SearchResult result = AddressMan.searchAddress(key, 1);
			int count = AddressMan.getTotalRows(key);
			assertTrue(count == 2045 && result.getAddrCount() == 20);			
		} catch (StopSearchingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Disabled("9가 고객SN 인 것이 확실하지 않으므로...")
	public void testDisplayCustomerAddress() {
		Logger logger = JLogger.getLogger(true);
		var addressList = AddressMan.getCustomerAddresses(4, 1);
		AddressMan.showCustomerAddresses(logger, addressList);
		assertTrue(addressList.size() > 0);
	}
}
