package com.jbpark.dabang.module.test;

import java.util.logging.Logger;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.jbpark.utility.JLogger;

public class X_main_1 {
	static Logger logger = JLogger.getLogger(true);
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(X_suite_1.class);
		System.out.println("실행: " + result.getRunCount());
		logger.info("실행: " + result.getRunCount());
		System.out.println("실패: " + result.getFailureCount());
		logger.info("실패: " + result.getFailureCount());
		System.out.println("전체 성공!" + result.wasSuccessful());
	}
}
