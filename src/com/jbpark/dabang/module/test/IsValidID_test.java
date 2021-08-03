package com.jbpark.dabang.module.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.jbpark.dabang.module.Utility;

public class IsValidID_test {

	@Test
	public void test() {
		assertTrue(Utility.isValidID("_$kdhong"));
	}
	@Test
	public void test2() {
		assertTrue(Utility.isValidID("_12a"));
	}
}
