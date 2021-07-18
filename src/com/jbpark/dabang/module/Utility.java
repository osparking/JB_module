package com.jbpark.dabang.module;

import java.util.Scanner;
import java.util.logging.Logger;

import com.jbpark.utility.JLogger;

public class Utility {
	private static Logger logger = JLogger.getLogger(true); 
	
	public static Integer geIntegerValue(Scanner scanner,
			String qLong, String qNoun) {
		return geIntegerValue(scanner, qLong, qNoun, false);
	}
	
	public static Integer geIntegerValue(Scanner scanner,
			String qLong, String qNoun, boolean returnNull) {
		int count = 1;
		System.out.println(qLong);
		while (true) {
			String line = null;
			System.out.print(qNoun + " : ");
			try {
				if (scanner.hasNextLine()) {
					line = scanner.nextLine();
					count = Integer.parseInt(line.trim());
					break;
				}
			} catch(NumberFormatException e) {
				if (returnNull && line.trim().length() == 0)
					return null;
				System.out.println("입력된 " + qNoun + " '" 
						+ line.trim() + "'은 부적절합니다.");
				System.out.println("다시 입력하십시오...");
			}
		}
		return count;
	}
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Integer page = geIntegerValue(scanner, 
				"페이지 번호를 입력하세요.", "페이지 번호(기본=1)",
				true);
		logger.config("페이지: " + (page == null ? 1 : page));
	}

}
