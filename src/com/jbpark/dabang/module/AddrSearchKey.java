package com.jbpark.dabang.module;

public class AddrSearchKey {
	private String 도로명일부;
	private String 건물명일부;
	private String 건물본번일부;

	public String get도로명일부() {
		return 도로명일부;
	}

	public void set도로명일부(String 도로명일부) {
		this.도로명일부 = 도로명일부;
	}

	public String get건물명일부() {
		return 건물명일부;
	}

	public void set건물명일부(String 건물명일부) {
		this.건물명일부 = 건물명일부;
	}

	public String get건물본번일부() {
		return 건물본번일부;
	}

	public void set건물본번일부(String 건물본번일부) {
		this.건물본번일부 = 건물본번일부;
	}

	//@formatter:off
	@Override
	public String toString() {
		return "검색키: [도로명='" + 
				(도로명일부 == null ? "" : 도로명일부)
				+ "', 건물명='" + 
				(건물명일부 == null ? "" : 건물명일부)
				+ "', 건물본번='" +
				(건물본번일부 == null ? "" : 건물본번일부) 
				+ "']";
	}
	
}
