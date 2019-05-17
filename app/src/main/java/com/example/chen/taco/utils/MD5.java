package com.example.chen.taco.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5 {

	/**
	 * @param str
	 *            需要加密的字符串
	 * @return
	 */
	public static String encrypt32(String str) {
		return MD5.encrypt(str, 32);
	}

	public static String encrypt16(String str) {
		return MD5.encrypt(str, 16);
	}

	private static String encrypt(String str, int digit) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			if (digit == 32) {
				str = buf.toString();
			} else if (digit == 16) {
				str = buf.substring(8, 24);
			}

		} catch (NoSuchAlgorithmException e) {

		}
		return str;

	}

	public static void main(String[] args) {
		String X = MD5.encrypt("hello word", 32);
		System.out.println(X);
	}

}
