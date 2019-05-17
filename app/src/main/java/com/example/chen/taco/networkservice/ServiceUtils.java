package com.example.chen.taco.networkservice;

import com.example.chen.taco.networkaccessor.NetworkResponse;
import com.example.chen.taco.utils.LogUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.net.HttpURLConnection;


public class ServiceUtils {
	private final static String TAG = "ServiceUtils";
	
	public static boolean isJsonString(String jsonString) {
		//判断返回结果是否正确的json字符串格式
		boolean result = false;
    	try {
        	JsonParser jp = new JsonParser();
        	JsonElement je = jp.parse(jsonString);
        	result = je.isJsonObject();
    	}catch (JsonSyntaxException e) {
    		e.printStackTrace();
    		LogUtil.e("ServiceUtils","parse Json Error");
    	}
    	return result;
	}
	
	public static String getOpenAPIResult(ServiceResponse<?> serResponse, NetworkResponse netResponse) {
		String responseStr = null;
    	if (netResponse.getStatusCode() == HttpURLConnection.HTTP_OK)
    	{
        	String resultData = netResponse.getData();
        	if (!isJsonString(resultData)) {
    			//解析异常
        		serResponse.setReturnCode(ServiceMediator.Service_Return_JsonParseError);
        		serResponse.setReturnDesc(ServiceMediator.Service_Return_JsonParseError_Desc);
        	}else {
	        	int errorCodeIndex;
	        	String errorCodeString;	//获取"errorCode":"0"
	        	String tempString;	//获取"result":""或者"errorMsg":""
	    		if (!resultData.contains(",\"errorCode\"")) {
	    			//errorCode在前
	        		errorCodeIndex = resultData.indexOf(",");
	    			errorCodeString = resultData.substring(1, errorCodeIndex);
	    			tempString = resultData.substring(errorCodeIndex+1, resultData.length()-1);
	    		}else {
	    			//errorCode在后
	        		errorCodeIndex = resultData.indexOf("errorCode");
	        		errorCodeString = resultData.substring(errorCodeIndex-1, resultData.length()-1);
	    			tempString = resultData.substring(1, errorCodeIndex-2);
	    		}
	    		String errorCode = errorCodeString.split(":")[1];	//获取errorCode的String值
	    		if (errorCode.equals("\"0\"")||errorCode.equals("0")) {
	    			//返回成功
	    			String resultString = tempString;
	    			int resultIndex = resultString.indexOf(":");
	    			String result = resultString.substring(resultIndex+1, resultString.length());	//获取result对应的JsonString或String
	    			String subString = result.substring(0,1);
	    			if (subString.equals("\""))
	    			{
	    				//处理返回结果String
	    				responseStr = result.substring(1, result.length() - 1);
	    			}
	    			else
	    			{
	    				//处理返回结果url
	        			responseStr = result.replace("/", "\\/");
	    			}
//	    			serResponse.setReturnCode(ServiceMediator.Service_Return_Success);
	    			//直接返回成功，不做进一步解析
	    			NetworkResponse networkRes = new NetworkResponse();
	    			networkRes.setStatusCode(HttpURLConnection.HTTP_OK);
//	    			responseStr = responseStr.replace("\"", "\\\"");
	    			networkRes.setData(responseStr);
	    			responseStr = getRequestResult(serResponse, networkRes);
	    		}else {
	    			//返回失败 其他错误码处理
	    			int code = Integer.parseInt(errorCode.trim().replace("\"", ""));
	
	    			//TO DO
	    			String errorMessageString = tempString;
	    			int resultIndex = errorMessageString.indexOf(":");
	    			String errorMessage = errorMessageString.substring(resultIndex+1,errorMessageString.length());	//获取errorMsg对应的String
	    			String message = errorMessage.substring(1, errorMessage.length() - 1);
	    			serResponse.setReturnCode(code);
	    			serResponse.setReturnDesc(message);
	    		}
        	}
    	}
    	else 
    	{
    		//网络错误分类处理
    		if (netResponse.getStatusCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT ||
    				netResponse.getStatusCode() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
    		{
    			//请求超时
        		serResponse.setReturnCode(ServiceMediator.Service_Return_RequestTimeOut);
        		serResponse.setReturnDesc(ServiceMediator.Service_Return_RequestTimeOut_Desc);
        		LogUtil.d(TAG, "OpenAPI请求超时");
    		}
    		else
    		{
    			//网络错误
        		serResponse.setReturnCode(ServiceMediator.Service_Return_NetworkError);
        		serResponse.setReturnDesc(ServiceMediator.Service_Return_NetworkError_Desc);
    		}
    	}
		return responseStr;
	}
	
    /**
     * 1.将服务器返回的json字符串截取出result及errorCode内容
     * 2.错误码转义 供UI使用
     * //     * @param resultData
     *
     * @return result对应的字符串，若errorCode不为0，则返回为空
     */ 
	//{"errorCode":"0","errorMessage":"注册成功！","result":1}
    public static String getRequestResult(ServiceResponse<?> serResponse, NetworkResponse netResponse) {
    	String responseStr = null;
    	if (netResponse.getStatusCode() == HttpURLConnection.HTTP_OK)
    	{
        	String resultData = netResponse.getData();	//{"errorCode":"0","errorMessage":"注册成功！","result":1}

        	if (!isJsonString(resultData)) {
    			//解析异常
        		serResponse.setReturnCode(ServiceMediator.Service_Return_JsonParseError);
        		serResponse.setReturnDesc(ServiceMediator.Service_Return_JsonParseError_Desc);
        	}else {
	        	int errorIndex;
	        	String errorString;	//获取status
	        	String tempString;	//获取result
	        	
	        	String errorNumberStr;
	        	String errorMessageStr;
	        	String errorNumber;
	        	String errorMessage;

	        	//获取status
	    		if (resultData.contains(",\"result\"")) {
	    			//error在前,有result
	    			errorIndex = resultData.indexOf("result");
	    			errorString = resultData.substring(1, errorIndex-2);	//"errorCode":"0","errorMessage":"注册成功！"
	    			tempString = resultData.substring(errorIndex-1, resultData.length()-1);	//"result":1
	    		}else {
	    			//只有error，没有result
	    			errorString = resultData.substring(1,resultData.length()-1);	//"errorCode":"10003","errorMessage":"用户未注册！"
	    			tempString="";
	    		}
	    		int errorNumberIndex = errorString.indexOf(",");
	        	errorNumberStr = errorString.substring(0,errorNumberIndex); //"result":1
	        	errorMessageStr = errorString.substring(errorNumberIndex+1, errorString.length());	//"errorMessage":"注册成功！"
	        	errorNumber = errorNumberStr.split(":")[1].replace("\"", ""); 	//1
	    		errorMessage = errorMessageStr.split(":")[1].split(",")[0].replace("\"", "");	// ""
	    		if (errorNumber.equals("\"0\"")||errorNumber.equals("0")) {
	    			//返回成功
	    			String resultString = tempString;	//"result":1
	    			int resultIndex = resultString.indexOf(":");
	    			String result = resultString.substring(resultIndex+1, resultString.length());	//获取result对应的JsonString或String//"1003"
	    			String subString = result.substring(0,1); //"
	    			if (subString.equals("\""))
	    			{
	    				//处理返回结果String
	    				responseStr = result.substring(1, result.length() - 1);	//1003
	    			}
	    			else
	    			{
	    				//处理返回结果url
	        			responseStr = result.replace("/", "\\/");
	    			}
	    			serResponse.setReturnCode(ServiceMediator.Service_Return_Success);
	    		}else {
	    			//返回失败 其他错误码处理
	    			int code = Integer.parseInt(errorNumber.trim().replace("\"", ""));
//	    			if (code >= 60101001 && code <= 60101008) {
//	    				//openAPI调用错误(openAPI错误码为60101001到60101008)
//		    			serResponse.setReturnCode(ServiceMediator.Service_Return_Error);
//		    			serResponse.setReturnDesc(ServiceMediator.Service_Return_Error_Desc);
//	    			}else {
		    			serResponse.setReturnCode(Integer.valueOf(errorNumber));
		    			serResponse.setReturnDesc(errorMessage);
//	    			}
	    		}
        	}
    	}
    	else 
    	{
    		//网络错误分类处理
    		if (netResponse.getStatusCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT ||
    				netResponse.getStatusCode() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
    		{
    			//请求超时
        		serResponse.setReturnCode(ServiceMediator.Service_Return_RequestTimeOut);
        		serResponse.setReturnDesc(ServiceMediator.Service_Return_RequestTimeOut_Desc);
        		LogUtil.d(TAG, "请求超时");
    		}
    		else
    		{
    			//网络错误
        		serResponse.setReturnCode(ServiceMediator.Service_Return_NetworkError);
        		serResponse.setReturnDesc(ServiceMediator.Service_Return_NetworkError_Desc);
    		}
    	}
		return responseStr;
	}
}
