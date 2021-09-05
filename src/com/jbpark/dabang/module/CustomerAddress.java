package com.jbpark.dabang.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@formatter:off
@Getter @Setter @AllArgsConstructor
public class CustomerAddress {
	private int 주소번호;
	private int 단지번호;
	private int 우편번호;
	private String 도로명주소;
	private String 상세주소;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(우)");
		
		sb.append(우편번호);
		sb.append(". ");
		sb.append(도로명주소);
		sb.append(", ");
		sb.append(상세주소);
		
		return sb.toString();
	}
}
