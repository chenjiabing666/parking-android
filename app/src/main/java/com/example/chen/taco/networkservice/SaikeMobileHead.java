package com.example.chen.taco.networkservice;

/**
 * 
 * @author 张松松
 * Http请求头域信息类，发送Http请求时需要传递这些信息到服务器
 *
 */

public class SaikeMobileHead {
	
	public String deviceId;    	//设备编码
	public String userToken;   	//用户登陆成功后返回的token
	public String userAccount; 	//当前登录的用户账号
	public String appCode;   	//应用编码
	public String appVersion;  	//应用版本号
	public String sourceCode;  	//应用渠道号
	public String clientId;		//客户端编码
	public String deviceManufacturer;//设备制造商
	public String plateformType;//平台

	public String toString(){
		return "SaicMobileHead:{" + 
	"deviceId:" + deviceId +
	"; userToken:" + userToken +
	"; userAccount:" + userAccount +
	"; appCode:" + appCode +
	"; appVersion:" + appVersion +
	"; sourceCode:" + sourceCode +
	"; clientId:" + clientId +
	"; deviceManufacturer:" + deviceManufacturer +
	"; plateformType:" + plateformType +
	";}";	
	}
	
	public SaikeMobileHead(){
		deviceId = "unique device identify";
		userToken = "login user token";
		userAccount = "login user account";
		appCode = "AppName";
		appVersion = "1.0";
		sourceCode = "Debug";
		clientId = "client Id";
		deviceManufacturer = "deviceManufacturer";
		plateformType = "android";
	}
}