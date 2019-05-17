package com.example.chen.taco.tasktool;


public class TaskToken {
	/** <br> 请求的方法名*/
	public String method;
	
	/** <br> 方法请求的对应viewModelId,  可以通过该token去viewmodelmanager中获取已存在的对应viewmodel*/
	public String viewModelId;

	public String flag;

//	/** <br> 一个全局唯一的标示符,一个增长数字 */
//	public String identifier;
//
//	/** <br> method + # + identifier 组成该方法的唯一令牌 */
//	public String requestToken;
	
//	/** <br> activity类名*/
//	public String activityClass;
}