package com.example.chen.taco.networkaccessor;

import java.net.HttpURLConnection;
import java.net.ProtocolException;


public class ProtocolHandler {
	public void handleResponse() {
		
	}
	
public static void handleRequest(HttpURLConnection connection) {
		
		connection.setRequestProperty("User-Agent","Mozilla/5.0 (Dalvik/1.6.0 (Linux; U; Android 4.4; sdk Build/KRT16L) ");
		
		connection.setRequestProperty("Accept-Language", "zh-cn,zh");

		connection.setRequestProperty("Accept-Charset", "utf-8");

		connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

		connection.setRequestProperty("Connection", "close");
		/*
		connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
                */
		connection.setInstanceFollowRedirects(true);
		
		try {
			connection.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void handleRequest(HttpURLConnection connection , boolean isForm) {
		
		connection.setRequestProperty("User-Agent","Mozilla/5.0 (Dalvik/1.6.0 (Linux; U; Android 4.4; sdk Build/KRT16L) ");
		
		connection.setRequestProperty("Accept-Language", "zh-cn,zh");

		connection.setRequestProperty("Accept-Charset", "utf-8");

		if (isForm) {
			connection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + "worktool");
			
		}else{
			connection.setRequestProperty("Content-type", "application/json");
		}

		connection.setRequestProperty("Connection", "close");
		/*
		connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
                */
		connection.setInstanceFollowRedirects(true);
		
		try {
			connection.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void handleRequestForm(HttpURLConnection connection) {

		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Dalvik/1.6.0 (Linux; U; Android 4.4; sdk Build/KRT16L)");
		
		connection.setRequestProperty("Charset", "UTF-8");

		connection.setRequestProperty("Content-Type",
                "multipart/form-data;boundary="+HttpURLConnectionHelper.BOUNDARY);
		
		connection.setRequestProperty("Connection", "Keep-Alive");
		
		try {
			connection.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}