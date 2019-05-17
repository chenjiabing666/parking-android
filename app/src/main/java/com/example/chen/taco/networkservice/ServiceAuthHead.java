package com.example.chen.taco.networkservice;


import com.example.chen.taco.utils.CommonUtil;
import com.example.chen.taco.utils.DateUtil;

import java.util.Date;

public class ServiceAuthHead {
	public final static String kAppKey = "appKey";
	public final static String kVersion = "version";
	public final static String kFormat = "format";
	public final static String kTimestamp = "timestamp";
	public final static String kSignatureMethod = "signatureMethod";
	public final static String kSignature = "signature";
	
	public final static String OPENAPI_APP_KEY = "app_rigida_open_api";
	public final static String OPENAPI_USER_TARGET_SERVICE = "RigidaUserService20";
	public final static String OPENAPI_DEVICE_TARGET_SERVICE = "RigidaDeviceService20";
	public final static String OPENAPI_VERSION = "1";
	public final static String OPENAPI_DATE_FORMATE = "yyyyMMddHHmmss";
	public final static String OPENAPI_DEFAULT_FORMAT = "json";
	public final static String OPENAPI_DEFAULT_SIGN_METHOD = "md5";

	public String appKey;   //设备认证的Key
	public String version;  //版本号
	public String format;   //参数格式
	public String timestamp;    //时间标签
	public String signatureMethod;  //签名加密方式
	public String signature;    //签名内容

	public String toString() {
		return "ServiceAuthHead:{" + 
	"appKey:" + appKey +
	"; appPermission:" + version +
	"; format:" + format +
	"; timestamp:" + timestamp +
	"; signatureMethod:" + signatureMethod +
	"; signature:" + signature +
	";}";	
	}
	
	public ServiceAuthHead() {
		appKey = OPENAPI_APP_KEY;
		version = OPENAPI_VERSION;
		format = OPENAPI_DEFAULT_FORMAT;
		timestamp = DateUtil.stringFromTime(new Date(), OPENAPI_DATE_FORMATE);
		signatureMethod = OPENAPI_DEFAULT_SIGN_METHOD;
	}


	public void createSignature(String method, String requestDataString)
	{
	    //create original signature
		StringBuffer tempSignature = new StringBuffer();
	    tempSignature.append(method.replace("/", ""));
	    tempSignature.append(requestDataString);
	    tempSignature.append(kAppKey+"="+appKey);
	    tempSignature.append(kFormat+"="+format);
	    tempSignature.append(kTimestamp+"="+timestamp);
	    tempSignature.append(kSignatureMethod+"="+signatureMethod);
	    tempSignature.append(kVersion+"="+version);
	    //MD5
	    this.signature = CommonUtil.MD5Encrypt(tempSignature.toString()).toUpperCase();
	}
}
