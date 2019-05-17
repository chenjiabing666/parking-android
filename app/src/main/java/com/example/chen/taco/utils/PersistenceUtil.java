package com.example.chen.taco.utils;

import android.content.SharedPreferences;

import com.example.chen.taco.parser.JsonHandler;

import java.util.List;

/**
 * 持久化工具
 */
public class PersistenceUtil {
	public final static String TAG = "PersistenceUtil";

	/**
	 * 储存一个对象到share preferences
	 */
	public static <T> void saveObjectToSharePreferences(T object, String key) {
		if (ENVConfig.sp == null) {
			LogUtil.e(TAG,"please config environment first:ENVConfig.configurationEnvironment(context)");
		}else {
			if (object != null && key != null) {
				SharedPreferences.Editor editor = ENVConfig.sp.edit();
				String objStr = JsonHandler.objectToJson(object);
//				LogUtil.d(TAG,"add to sp json:"+objStr);
				editor.putString(key, objStr);
				editor.commit();
			}
		}
	}
	
	/**
	 *  从share preferences获取一个对象   cls:User.class
	 * 
	 * @param key  
	 * @param cls  该对象的类  例如：User.class
	 * @return
	 */
	public static <T> T getObjectFromSharePreferences(String key, Class<T> cls) {
		if (ENVConfig.sp == null) {
			LogUtil.e(TAG,"please config environment first:ENVConfig.configurationEnvironment(context)");
		}else {
			String jsonStr = ENVConfig.sp.getString(key, "");
			LogUtil.d(TAG,"json from sp:"+jsonStr);
			if (jsonStr.equals("") || jsonStr == null) {
				return null;
			}else {
				return JsonHandler.jsonToObject(jsonStr, cls);
			}
		}
		return null;
	}

	/**
	 *  从share preferences获取一个对象   cls:User.class
	 *
	 * @param key
	 * @param cls  该对象的类  例如：User.class
	 * @return
	 */
	public static List getListFromSharePreferences(String key, Class cls) {
		if (ENVConfig.sp == null) {
			LogUtil.e(TAG,"please config environment first:ENVConfig.configurationEnvironment(context)");
		}else {
			String jsonStr = ENVConfig.sp.getString(key, "");
			LogUtil.d(TAG,"json from sp:"+jsonStr);
			if (jsonStr.equals("") || jsonStr == null) {
				return null;
			}else {
				return JsonHandler.jsonToList(jsonStr, cls);
			}
		}
		return null;
	}

}