package com.jbpark.dabang.module.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jbpark.dabang.module.Utility;

public class IsValidpassword_test {

	@Test
	public void test() {
		assertTrue(Utility.isValidPassword("aB98@"));
	}
	@Test
	public void test2() {
		assertTrue(Utility.isValidPassword("qQ1!"));
	}
}
