package com.example.chen.taco.networkservice;


/**
 * 
 * @author 张松松 服务返回结果类:对服务层处理结果进行包装,供UI层使用
 * @param <T>
 * 
 */
public class ServiceResponse<T> {

	// 返回码
	private int returnCode;
	// 返回码 描述
	private String returnDesc;
	// 操作码 与接口相关
	private String operatCode;
	// 返回结果
	private T response;
	// 返回结果部分1
	private Object response1;
	// 返回结果部分2
	private Object response2;
	// 返回结果部分3
	private Object response3;
	// 请求令牌
	private String requestToken;

	private String method;
	private String activityClass;
	private String activityToken;

	private T tokenObj;

	public T getTokenObj() {
		return tokenObj;
	}

	public void setTokenObj(T md) {
		tokenObj = md;
	}

	public String getActivityClass() {
		return activityClass;
	}

	public void setActivityClass(String aClass) {
		activityClass = aClass;
	}

	public String getActivityToken() {
		return activityToken;
	}

	public void setActivityToken(String token) {
		activityToken = token;
	}

	public String getRequestMethod() {
		return method;
	}

	public void setRequestMethod(String md) {
		method = md;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String token) {
		requestToken = token;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	public String getOperatCode() {
		return operatCode;
	}

	public void setOperatCode(String operatCode) {
		this.operatCode = operatCode;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public Object getResponse1() {
		return response1;
	}

	public void setResponse1(Object response1) {
		this.response1 = response1;
	}

	public Object getResponse2() {
		return response2;
	}

	public void setResponse2(Object response2) {
		this.response2 = response2;
	}

	public Object getResponse3() {
		return response3;
	}

	public void setResponse3(Object response3) {
		this.response3 = response3;
	}




	/**
	 * 默认值
	 */
	private final int Default_Service_Code = ServiceMediator.Service_Return_Error;
	private final String Default_Service_Message = ServiceMediator.Service_Return_Error_Desc;

	/**
	 * 构造函数
	 */
	public ServiceResponse() {
		this.returnCode = Default_Service_Code;
		this.returnDesc = Default_Service_Message;
	}
}

