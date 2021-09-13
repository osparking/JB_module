package com.jbpark.dabang.module.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.jbpark.dabang.module.고객계정;

class Test고객계정 {

	@Test
	void testIsGoodNewUserId_good() {
		String userId = "myself3";
		
		var reasons = new LinkedList<String>();
		assertTrue(고객계정.isGoodNewUserId(userId, reasons));
	}
}
