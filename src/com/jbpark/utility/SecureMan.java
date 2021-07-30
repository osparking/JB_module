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
	 * @param salt
	 * @return 암호화된 비밀번호
	 * @see <a href="https://www.baeldung.com/java-password-hashing"> Hashing a
	 *      Password in Java</a>
	 */
	//@formatter:off
	public static byte[] encryptPassword(String password, byte[] salt) {
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

	/**
	 * 암호를 해슁하고, 이 때 소금도 보관해 둔다. 나중에 같은 암호라고 
	 * 주장되는 입력(entered)을 원래 암호와 같은지 비교할 때 소금이 
	 * 사용된다.
	 * @param args
	 */
	public static void main(String[] args) {
		String password = "1234567891";
		byte[] salt = getSalt(); 
		byte[] encedPwd = encryptPassword(password, salt);
		// 16 bytes
		System.out.println("암호화 전: " + password);
		System.out.println("암호화 후(1): " + Arrays.toString(encedPwd));
		printInHexFormat(encedPwd);  // 암호화 후(2)
		
		String entered = " " + password ;
		boolean passwordMatch = verifyPassword(
				entered, encedPwd, salt);
		
		System.out.print("검증 결과: 입력 '" + entered);
		if(passwordMatch) {
			System.out.println("'는 바르다.");
		} else {
			System.out.println("'는 그르다.");
		}		
		return; 
	}
	
	/**
	 * 비밀번호 암호화 때 호출되어 그를 위한 소금 생성, DB에 암호화된
	 * 비밀번호와 함께 저장되어, 입력된 비밀번호 검사 때 사용됨.
	 * @return 생성된 난수 소금
	 */
    private static byte[] getSalt() {
    	byte[] salt = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		return salt;
	}

    /**
     * 
     * @param entered
     * @param securedPassword
     * @param salt
     * @return
     * @see <a href="https://www.appsdeveloperblog.com/encrypt-user-password-example-java/">
     * Validate User Password Code Example in Java</a>
     */
	public static boolean verifyPassword(String entered,
            byte[] securedPassword, byte[] salt)
    {
        boolean returnValue = false;
        
        // Generate New secure password with the same salt
        byte[] securedEntered = encryptPassword(entered, salt);
        
        // Check if two passwords are equal
        returnValue = Arrays.equals(securedPassword, securedEntered);
        
        return returnValue;
    }

	/**
	 * @see <a href="https://stackoverflow.com/a/61146042/3901138">
	 * How to convert a byte array to a hex string in Java?
	 * (Marian 답변)</a>
	 */
	private static void printInHexFormat(byte[] encedPwd) {
		System.out.println("암호화 후(2): " + 
				new BigInteger(1, encedPwd).toString(16));
	}
}
