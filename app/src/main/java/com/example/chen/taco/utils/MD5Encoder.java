package com.example.chen.taco.utils;


import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密,
 * 
 */
public class MD5Encoder {
	// 加密字符串
	public static String encoding(String fileName) {
		if (TextUtils.isEmpty(fileName)) {
			System.out.println("字符串为空，编码失败");
			return null;
		}
		String resultString = MD5Encoder.getMD5Str(fileName);
		return resultString;
	}

	// 加密url
	public static String encoding(URL fileUrl) {
		String urlString = fileUrl.toString();
		return MD5Encoder.encoding(urlString);
	}

	/**
	 * 取得md5的32位字符串（取得的字符串均为小写）
	 * 
	 * @param str
	 *            要被加密的字符串
	 * @return
	 */
	private static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}
}
