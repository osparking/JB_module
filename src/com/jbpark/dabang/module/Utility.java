package com.jbpark.dabang.module;

import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jbpark.utility.JLogger;

public class Utility {
	/**
	 * 고객ID가 정당한 형식인지 검사
	 * 
	 * @param identifier
	 * @return 참 정당한 경우, 거짓 부당한 경우.
	 * @see <a href=
	 *      "https://www.geeksforgeeks.org/how-to-validate-identifier-using-regular-expression-in-java/">
	 *      참고 웹문서</a>
	 */
	public static boolean isValidID(String identifier) {
		String regex = "^([a-zA-Z_$][a-zA-Z\\d_$]*)$";

		// Compile the ReGex
		Pattern p = Pattern.compile(regex);

		if (identifier == null) {
			return false;
		}

		Matcher m = p.matcher(identifier);
		return m.matches();
	}

	public static String get고객ID(Scanner scanner) {
		while (true) {
			System.out.print("고객ID를 입력하세요 : ");
			if (scanner.hasNext()) {
				String id = scanner.nextLine().trim();
				if (isValidID(id))
					return id;
				else { 
					String msg = "'" + id + "'는 바른 형식이 아닙니다.";
					System.out.println(msg);
					System.out.print("고객ID를 입력하세요 : ");
				}
			}
		}
	}

	public static Integer getIntegerValue(Scanner scanner, String qLong, String qNoun) throws NoInputException {
		return getIntegerValue(scanner, qLong, qNoun, false);
	}

	/**
	 * 
	 * @param scanner
	 * @param qLong
	 * @param qNoun
	 * @param allowNoInput 참이면, 예외 발생
	 * @return
	 * @throws NoInputException 참일때 발생시킴
	 */
	public static Integer getIntegerValue(Scanner scanner, String qLong, String qNoun, boolean allowNoInput)
			throws NoInputException {
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
			} catch (NumberFormatException e) {
				if (allowNoInput) {
					if (line.trim().length() == 0)
						throw new NoInputException();
				}
				System.out.println("입력된 " + qNoun + " '" + line.trim() + "'은 부적절합니다.");
				System.out.println("다시 입력하십시오...");
			}
		}
		return count;
	}

	public static void main(String[] args) throws NoInputException {
		Logger logger = JLogger.getLogger(true);

		Scanner scanner = new Scanner(System.in);
		Integer page = getIntegerValue(scanner, "페이지 번호를 입력하세요.", "페이지 번호(기본=1)", true);
		logger.config("페이지: " + (page == null ? 1 : page));
	}

}
