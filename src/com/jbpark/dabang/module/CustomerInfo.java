package com.jbpark.dabang.module;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CustomerInfo {
	private int 고객SN;
	private String 고객ID;
	private String 고객이름;
	private byte[] salt = new byte[16];
	private byte[] password = new byte[16];
	private boolean deleted;

	public CustomerInfo(int 고객SN, String 고객이름) {
		this.고객SN = 고객SN;
		this.고객이름 = 고객이름;
		salt = new byte[16];
		password = new byte[16];
	}
}
