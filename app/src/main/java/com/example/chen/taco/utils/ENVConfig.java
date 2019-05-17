package com.example.chen.taco.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.chen.taco.parser.SaxParseHandler;


public class ENVConfig {
	/** 挡板状态 */
	public static boolean mockState = false;
	
	public static SaxParseHandler xmlParser = null;
	
	public static Context appContext = null;

	public static SharedPreferences sp;
	
	private static String CONFIG_PREFERENCES = "config_preferences";
	
	/**同步网络时间允许的时间差**/
	public static long timeInteval = 10*1000;

	/**
	 * 判断当前请求是否为挡板数据类型
	 * 
	 * @param url 请求的地址
	 * @return
	 */
	public static boolean currentRequestTypeIsMock(String url) {
		if (url.startsWith("http://")||url.startsWith("https://")) {
			return false;
		}
		return true;
	}

	/**
	 * 环境配置
	 * @param context 应用上下文
	 */
	public static void configurationEnvironment(Context context) {
		sp = context.getSharedPreferences(CONFIG_PREFERENCES,
				Context.MODE_PRIVATE);
		appContext = context;
	}
	
	/**
	 * 用raw目录下的environment_config.xml对应的id配置环境
	 * @param rawResourceId
	 */
	public static void setEnvironmentConfig(int rawResourceId) {
		xmlParser = SaxParseHandler.shareInstance(appContext, rawResourceId);
	}
}
