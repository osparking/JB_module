package com.jbpark.dabang.module.test;

import static org.junit.Assert.assertTrue;

import java.util.Scanner;

import org.junit.Test;

import com.jbpark.dabang.module.AddressMan;
import com.jbpark.dabang.module.SearchResult;
import com.jbpark.dabang.module.StopSearchingException;

public class AddressTest {

	@Test
	public void test() {
		AddressMan aMan = new AddressMan();
		Scanner scanner = new Scanner(System.in);
		System.out.println("'세진'을 입력할 것.");
		try {
			SearchResult result = aMan.search(scanner);
			int count = result.getTotalRow();
			System.out.println(count);
			assertTrue(count == 2);
		} catch (StopSearchingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
