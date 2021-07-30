package com.jbpark.utility;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SecureMan {
	/**
	 * 비밀번호를 "PBKDF2"으로 암호화 한다.
	 * 
	 * @param password 비밀번호
	 * @return 암호화된 비밀번호
	 * @see <a href="https://www.baeldung.com/java-password-hashing">Hashing a
	 *      Password in Java</a>
	 */
	//@formatter:off
	public byte[] encryptPassword(String password) {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		
		KeySpec spec = new PBEKeySpec(
				password.toCharArray(), salt, 65536, 128);
		// 128 bit: key length
		byte[] hash = null;
		try {
			SecretKeyFactory factory = SecretKeyFactory
					.getInstance("PBKDF2WithHmacSHA1");
			hash = factory.generateSecret(spec).getEncoded();
			return hash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	public static void main(String[] args) {
		SecureMan secureMan = new SecureMan();
		String password = "1234567891";
		byte[] encedPwd 
			= secureMan.encryptPassword(password);
		// 16 bytes
		System.out.println("암호화 전: " + password);
		System.out.println("암호화 후: " + 
				Arrays.toString(encedPwd));
		printInHexFormat(encedPwd);
		return; 
	}

	/**
	 * @see <a href="https://stackoverflow.com/a/61146042/3901138">
	 * How to convert a byte array to a hex string in Java?
	 * (Marian 답변)</a>
	 */
	private static void printInHexFormat(byte[] encedPwd) {
		System.out.println("암호화 후: " + 
				new BigInteger(1, encedPwd).toString(16));
	}
}
