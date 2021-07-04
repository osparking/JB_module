package jbpark.utility;

import java.util.logging.Logger;

public class SuffixChecker {

	public static void main(String[] args) {
		String ch = "나";
		boolean 받침있음 = has받침(ch.codePointAt(0), ch);
		if (받침있음) {
			Logger.getGlobal().info("받침있음");
		} else {
			Logger.getGlobal().info("없음");
		}
	}

	/**
	 * 한글 글자가 종성이 있는지 여부를 판단한다.
	 * @param ch 판단할 한글 한 글자 문자열
	 * @return 종성이 있으면 참, 없으면 거짓
	 * 출처: https://bloodguy.tistory.com/entry/ 검색 "종성"
	 *      https://twitter.com/nicehide
	 */
	public static boolean has받침(int cp, String ch) {
		int code = cp - 44032;
		boolean hasBatChim = true;
		
		// 한글일 경우 (가=0, 힣=11171)
		if (code > -1 && code < 11172) {
			if (code % 28 == 0)
				hasBatChim = false;
		} else {
			// 숫자중 2(이),4(사),5(오),9(구)는 종성이 없음
			if ("2459".indexOf(ch) > -1) {
				hasBatChim = false;
			}
		}
		return hasBatChim;
	}
}