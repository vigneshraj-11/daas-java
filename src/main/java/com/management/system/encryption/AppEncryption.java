package com.management.system.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AppEncryption {

	private static final String ALGORITHM = "AES";
	private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't',
			'K', 'e', 'y' };

	private static KeyGenerator keyGen;
	private static SecretKey secretKey;

	static {
		try {
			keyGen = KeyGenerator.getInstance(ALGORITHM);
			keyGen.init(128);
			secretKey = new SecretKeySpec(keyValue, ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String valueToEnc) throws Exception {
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encValue = c.doFinal(valueToEnc.getBytes());
		return Base64.getEncoder().encodeToString(encValue);
	}

	public static String decrypt(String encryptedValue) throws Exception {
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decordedValue = Base64.getDecoder().decode(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		return new String(decValue);
	}

	public static byte[] encryptBytes(byte[] valueToEnc) throws Exception {
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, secretKey);
		return c.doFinal(valueToEnc);
	}

	public static byte[] decryptBytes(byte[] encryptedValue) throws Exception {
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, secretKey);
		return c.doFinal(encryptedValue);
	}

}