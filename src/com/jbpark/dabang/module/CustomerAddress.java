package com.jbpark.dabang.module;

import lombok.Getter;
import lombok.Setter;

// @formatter:off
@Getter @Setter
public class CustomerAddress {
	private String 주소번호;
	private int 단지번호;
	private int 우편번호;
	private String 도로명주소;
	private String 상세주소;
	public CustomerAddress(String 주소번호, int 단지번호, 
			int 우편번호, String 도로명주소, String 상세주소) {
		super();
		this.주소번호 = 주소번호;
		this.단지번호 = 단지번호;
		this.우편번호 = 우편번호;
		this.도로명주소 = 도로명주소;
		this.상세주소 = 상세주소;
	}

	@Override
	public String toString() {
		return "(우)" + 우편번호 + ". " + 도로명주소 + ", " + 상세주소;
	}
}
