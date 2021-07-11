package com.jbpark.dabang.module;

public class AddrSearchKey {
	private String 도로_건물;
	private String 건물본번;

	public String get도로_건물() {
		return 도로_건물;
	}

	public void set도로_건물(String 도로_건물) {
		this.도로_건물 = 도로_건물;
	}

	public String get건물본번() {
		return 건물본번;
	}

	public void set건물본번(String 건물본번) {
		this.건물본번 = 건물본번;
	}

	//@formatter:off
	@Override
	public String toString() {
		return 
			"검색키: [도로명='" + (도로_건물 == null ? "" : 도로_건물)
			+ "', 건물본번='" + (건물본번 == null ? "" : 건물본번) 
			+ "']";
	}
	
}
