package com.jbpark.dabang.module.test;

import java.util.logging.Logger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.jbpark.utility.JLogger;

public class TestMain {
	static Logger logger = JLogger.getLogger(true);
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(Test_02_tests.class);
		
		logger.info("실행: " + result.getRunCount());
		logger.info("실패: " + result.getFailureCount());
	}
}
