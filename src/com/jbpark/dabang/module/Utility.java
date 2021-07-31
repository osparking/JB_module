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
	//@formatter:off
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

	public static String get고객ID(Scanner scanner, String prompt) {
		while (true) {
			System.out.print(prompt);
			if (scanner.hasNext()) {
				String id = scanner.nextLine().trim();
				if (isValidID(id))
					return id;
				else { 
					String msg = "'" + id + "'는 바른 형식이 아닙니다.";
					System.out.println(msg);
					System.out.print(prompt);
				}
			}
		}
	}

	public static Integer getIntegerValue(Scanner scanner, 
			String qLong, String qNoun) throws NoInputException {
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
	public static Integer getIntegerValue(Scanner scanner, 
			String qLong, String qNoun, boolean allowNoInput)
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
				System.out.println("입력된 " + qNoun + " '" 
						+ line.trim() + "'은 부적절합니다.");
				System.out.println("다시 입력하십시오...");
			}
		}
		return count;
	}
	
	/**
	 * 
	 * @param password
	 * @return
	 * @see <a href="https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-expressions-in-java/">
	 * geeksforgeeks</a>
	 */
    public static boolean isValidPassword(String password){
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                       + "(?=.*[a-z])(?=.*[A-Z])"
                       + "(?=.*[!@#$%^&+=])"
                       + "(?=\\S+$).{4,20}$";
  
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
  
        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }
  
        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);
  
        // Return if the password
        // matched the ReGex
        return m.matches();
    }	
	
	public static String getPassword(Scanner scanner) {
		String password = null;
		
		while (true) {
			while (true) {
				showPasswordRequirements();
				if (scanner.hasNext()) {
					password = scanner.nextLine();
					if (isValidPassword(password)) {
						break;
					} else {
						System.out.println(":: 요구조건에 미달합니다.");
					}
				}
			}
			for (int i =0; i<3; i++) {
				System.out.print("반복 입력하세요: ");
				if (scanner.hasNext()) {
					String password2 = scanner.nextLine();
					if (password.equals(password2))
						return password;
				}
			}
		}
	}

	private static void showPasswordRequirements() {
		StringBuffer pwdReq = new StringBuffer("\t- 최소 4자리, 최대 20자리\n");
		pwdReq.append("\t- 숫자, 대문자, 소문자, 특수문자 각 1개 이상\n");
		pwdReq.append("\t- 특수문자: !@#$%&*()-+=^");
		System.out.println("다음 조건을 충족하는 비밀번호를 입력하세요 - ");
		System.out.println(pwdReq);
	}

	public static void main(String[] args) throws NoInputException {
		Logger logger = JLogger.getLogger(true);

		Scanner scanner = new Scanner(System.in);
		String password = getPassword(scanner);
		Integer page = getIntegerValue(scanner, 
				"페이지 번호를 입력하세요.", "페이지 번호(기본=1)", true);
		logger.config("페이지: " + (page == null ? 1 : page));
	}

}
