package com.example.chen.taco.networkaccessor;

import com.example.chen.taco.networkservice.ServiceMediator;

import org.apache.http.cookie.Cookie;

import java.util.List;


@SuppressWarnings("unused")
public class NetworkResponse {
	private static int DEFAULT_STATUS_CODE = ServiceMediator.Service_Return_Error;
	private static String DEFAULT_CODE_DESC = ServiceMediator.Service_Return_Error_Desc;
	private static String DEFAULT_DATA = "null";
	private static List<Cookie> cookies = null;

	// 状态码
	private int mStatusCode;
	// 描述信息
	private String mCodeDesc;
	// 数据
	private String mData;
	// 缓存
	private List<Cookie> mCookies;

	public int getStatusCode() {
		return mStatusCode;
	}

	public void setStatusCode(int code) {
		this.mStatusCode = code;
	}

	public String getCodeDesc() {
		return mCodeDesc;
	}

	public void setData(String data) {
		this.mData = data;
	}

	public String getData() {
		return mData;
	}

	public void setCookies(List<Cookie> cookies) {
		this.mCookies = cookies;
	}

	public List<Cookie> getCookies() {
		return mCookies;
	}

	public NetworkResponse() {
		this(null, NetworkResponse.DEFAULT_STATUS_CODE,
				NetworkResponse.DEFAULT_DATA);
	}

	public NetworkResponse(String dataString, int code, String codeDeString) {
		this.setWithData(dataString, code, codeDeString);
	}

	/**
	 * 设置属性
	 * 
	 * @param dataString
	 * @param code
	 * @param codeDeString
	 */
	public void setWithData(String dataString, int code, String codeDeString) {
		this.mStatusCode = code;
		this.mCodeDesc = codeDeString;
		this.mData = dataString;
		this.mCookies = null;
	}

	/**
	 * 设置属性
	 * 
	 * @param dataString
	 * @param code
	 * @param codeDeString
	 * @param cookies
	 */
	public void setWithData(String dataString, int code, String codeDeString, List<Cookie> cookies) {
		this.mStatusCode = code;
		this.mCodeDesc = codeDeString;
		this.mData = dataString;
		this.mCookies = cookies;
	}

}
