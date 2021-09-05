package com.jbpark.dabang.module.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.jbpark.dabang.module.AddrSearchKey;
import com.jbpark.dabang.module.AddressMan;
import com.jbpark.dabang.module.StopSearchingException;
import com.jbpark.utility.JLogger;

public class AddressTest {
	@Test
	public void testDisplayCustomerAddress() {
		Logger logger = JLogger.getLogger(true);
		var addressList = 
				AddressMan.getCustomerAddresses(4, 10, 1);
		AddressMan.showCustomerAddresses(logger, addressList);
		assertTrue(addressList.size() > 0);
	}
}
