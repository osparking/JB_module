package com.jbpark.dabang.module;

public class CustomerAddress {
	private String 주소번호;
	private int 단지번호;
	private String 고객이름;
	private int 우편번호;
	private String 도로명주소;
	private String 상세주소;
	//@formatter:off
	public CustomerAddress(String 주소번호, int 단지번호, 
			String 고객이름, int 우편번호, String 도로명주소, String 상세주소) {
		super();
		this.주소번호 = 주소번호;
		this.단지번호 = 단지번호;
		this.고객이름 = 고객이름;
		this.우편번호 = 우편번호;
		this.도로명주소 = 도로명주소;
		this.상세주소 = 상세주소;
	}
	//@formatter:on
	
	public String get주소번호() {
		return 주소번호;
	}
	public int get우편번호() {
		return 우편번호;
	}

	public int get단지번호() {
		return 단지번호;
	}
	public String get고객이름() {
		return 고객이름;
	}
	public String get도로명주소() {
		return 도로명주소;
	}
	public String get상세주소() {
		return 상세주소;
	}
	public void set상세주소(String 상세주소) {
		this.상세주소 = 상세주소;
	}
	@Override
	public String toString() {
		return "(우)" + 우편번호 + ". " + 도로명주소 + ", " + 상세주소;
	}

}
