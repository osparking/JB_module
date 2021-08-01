package com.jbpark.dabang.module.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jbpark.dabang.module.Utility;

public class UtilityTest {

	@Test
	public void testGoodId() {
		String goodId = "myself3";
		assertTrue(Utility.isValidID(goodId));
	}
	
	@Test
	public void testBadID() {
		String badId = "ab cd";
		assertFalse(Utility.isValidID(badId));
	}

}
