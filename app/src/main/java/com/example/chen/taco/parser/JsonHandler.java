package com.example.chen.taco.parser;

import com.example.chen.taco.networkservice.ServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.utils.LogUtil;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * json处理类
 * 
 */
public class JsonHandler {
	
	/**
	 * 解析格式如 {"errorCode":"0","result":{"param1":22,"param2":"strValue"}}的Json字符串，讲结果存入response中
	 * @param json 被解析的json字符串
	 * @param cls  result 类型
	 * @param response  存放返回信息
	 */
	public static <T> void jsonToObject(String json, Class<T> cls, ServiceResponse<T> response) {
		T resultObject = null;

		//获取result的结果
		try{
			resultObject = JsonHandler.jsonToObject(json, cls);
		} catch (Exception e) {
			e.printStackTrace();
			response.setReturnDesc(ServiceMediator.Service_Return_JsonParseError_Desc);
			response.setReturnCode(ServiceMediator.Service_Return_JsonParseError);
			return;
		}
		response.setResponse(resultObject);
		response.setReturnCode(ServiceMediator.Service_Return_Success);
	}

	/**
	 * Josn List解析
	 * 解析格式如 {"errorCode":"0","result":[{"param1":22,"param2":"strValue"}]}的Json字符串，讲结果存入response中
	 * @param json 被解析的json字符串
	 * @param type result 数组中元素的 类型
	 * @param response  存放返回信息
	 */
	public static <T> void jsonToList(String json, Type type, ServiceResponse<List<T>> response) {
		List<T> resultObject = null;

		try {
			resultObject = JsonHandler.jsonToList(json, type);
		} catch (Exception e) {
			LogUtil.d("====================================================","====================================");
			e.printStackTrace();
			response.setReturnDesc(ServiceMediator.Service_Return_JsonParseError_Desc);
			response.setReturnCode(ServiceMediator.Service_Return_JsonParseError);
			return;
		}
		response.setResponse(resultObject);
		response.setReturnCode(ServiceMediator.Service_Return_Success);
		return;
	}
	
	/**
	 * 解析返回类型为boolean类型的jsonString
	 * 格式：{"errorCode":"0","result":"1"}
	 * 
	 * @param json 需要解析的json
	 * @param response 
	 */
	public static void jsonToBoolean(String json, ServiceResponse<Boolean> response) {
		if(json != null){
			if (json.equals("1")) {
				response.setResponse(true);
			} else {
				response.setResponse(false);
			}			
			response.setReturnCode(ServiceMediator.Service_Return_Success);
			return;
		}else{
			response.setReturnDesc(ServiceMediator.Service_Return_JsonParseError_Desc);
			response.setReturnCode(ServiceMediator.Service_Return_JsonParseError);
			LogUtil.e("zhangss", "json解析错误： result 获取失败");
		}
	}
	
	/**
	 * 解析返回类型为String类型的jsonString
	 * 格式：{"errorCode":"0","result":"resultStr"}
	 * 
	 * @param json 需要解析的json
	 * @param response 
	 */
	public static void jsonToString(String json, ServiceResponse<String> response) {
		if(json != null){
			response.setResponse(json);
			response.setReturnCode(ServiceMediator.Service_Return_Success);
			return;
		}else{
			response.setReturnDesc(ServiceMediator.Service_Return_JsonParseError_Desc);
			response.setReturnCode(ServiceMediator.Service_Return_JsonParseError);
			LogUtil.e("zhangss", "json解析错误： result 获取失败");
		}
	}
	

	/**
	 * 解析返回类型为boolean类型的jsonString
	 * 格式{"errorCode":"0","result":0}
	 * 
	 * @param json 需要解析的json
	 * @param response 
	 */
	public static void jsonToNumber(String json, ServiceResponse<Number> response) {
		int num = -1;
		num = Integer.parseInt(json);
		if(num != -1){
			response.setResponse(num);
			response.setReturnCode(ServiceMediator.Service_Return_Success);
			return;
		}else{
			response.setReturnDesc(ServiceMediator.Service_Return_JsonParseError_Desc);
			response.setReturnCode(ServiceMediator.Service_Return_JsonParseError);
			LogUtil.e("zhangss", "json解析错误： result 获取失败");
		}		
	}

	/**
	 * json转对象
	 * 
	 * @param json json字符串
	 * @param obj  obj对象
	 * @return
	 */
	public static <T> T jsonToObject(String json, Class<T> obj) {
		System.out.println("json=="+json);
		return new Gson().fromJson(json, obj);
	}

	/**
	 * 对象转json
	 * 
	 * @param obj 对象
	 * @return
	 */
	public static String objectToJson(Object obj) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);
		return jsonString;
	}

	/**
	 * json转List
	 * 
	 * @param json json字符串
	 * @return cls List 元素对象类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> jsonToList(String json, Class<T> cls) {
		Gson gson = new Gson();
		List<T> list = new ArrayList<T>();
		ArrayList<?> temp = gson.fromJson(json, ArrayList.class);
		for (int i = 0; i < temp.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) temp.get(i);
			list.add((T) jsonToObject(new Gson().toJson(map), cls));
		}
		return list;
	}
	
	/**
	 * json转List
	 * 
	 * @param json json字符串
	 * @param type List的数据类型
	 * @return cls List 元素对象类型
	 */
	public static <T> List<T> jsonToList(String json, Type type) {
		System.out.println("jsonlist=="+json);
		List<T> list = new Gson().fromJson(json, type);
		return list;
	}
	
	
	
}

