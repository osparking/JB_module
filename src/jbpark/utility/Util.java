package jbpark.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static Date getDate(String dateString) {
		Date aDate = null;
		
		try {
			aDate = sdf.parse(dateString);
			return aDate;
		} catch(ParseException pe) {
			pe.printStackTrace();
		}
		return aDate;
	}
	
	public static void main(String[] args) {
		Date oneDay = getDate("2021-8-22");
		System.out.println(oneDay);
	}
}
