package com.example.chen.taco.networkservice;



import com.example.chen.taco.utils.ENVConfig;

import java.lang.reflect.Method;
import java.util.HashMap;


/**
 * 服务层:
 * 1.所有UI与网络及数据层的交互需要调用服务层接口
 * 2.耗时操作需要配合TaskPool完成
 * 3.服务接口均返回ServiceResponse对象，对返回结果进行包装
 */

public class ServiceMediator {
	public final static String PLATEFORM = "android";//设备平台
	public final static String USER_TYPE = "0";//用户来自电商主站应用
	public final static String ACCOUNT_TYPE = "2";//手机注册
	public final static String EMAIL_AUTH_URL = "";//使用邮箱注册账号时的验证URL，手机注册传空
	public final static String CLIENT_ID = "clientId";//设备注册获得的id
	
	public static String pmfUrl = ENVConfig.xmlParser==null?"":ENVConfig.xmlParser.getPMFURL();
	
	public enum SECURITY_TYPE {

	    // 利用构造函数传参
		HIGH_SECURITY(1), LOW_SECURITY(3), MEDIUM_SECURITY(2);

	    // 定义私有变量
	    private int nCode;

	    // 构造函数，枚举类型只能为私有
	    private SECURITY_TYPE(int _nCode) {

	        this.nCode = _nCode;

	    }

	    @Override
	    public String toString() {
	        return String.valueOf(this.nCode);
	    }

	}

	public enum VALIDATE_CODE_TYPE {
		// 利用构造函数传参
		VALIDATE_CODE_REGISTER(1), VALIDATE_CODE_RESETPWD(2);
		// 定义私有变量
		private int nCode;

		// 构造函数，枚举类型只能为私有
		private VALIDATE_CODE_TYPE(int _nCode) {
			this.nCode = _nCode;
		}

		@Override
		public String toString() {
			return String.valueOf(this.nCode);
		}

	}

	/**
	 * 返回码定义
	 */
	public final static int Service_Return_Success = 200;
	public final static String Service_Return_Success_Desc = "请求成功";
	public final static int Service_Return_Error = 90001;
	public final static String Service_Return_Error_Desc = "请求失败";
	public final static int Service_Return_JsonParseError = 90002;
	public final static String Service_Return_JsonParseError_Desc = "Json解析异常";
	// 网络异常
	public final static int Service_Return_RequestTimeOut = 90003;
	public final static String Service_Return_RequestTimeOut_Desc = "请求超时";
	public final static int Service_Return_NetworkError = 90004;
	public final static String Service_Return_NetworkError_Desc = "网络异常";
	public final static int Service_Return_CancelRequest = 90005;
	public final static String Service_Return_CancelRequest_Desc = "取消请求";
	// 服务器异常
	public final static int Service_Return_ServerError = 90006;
	public final static String Service_Return_ServerError_Desc = "服务器异常";
	// OpenAPI错误码
	public final static int Service_Return_UserNotFound = 31101007;
	public final static String Service_Return_UserNotFound_Desc = "用户名无法找到！";
	public final static int Service_Return_UserLocked = 31101008;
	public final static String Service_Return_UserLocked_Desc = "密码输入错误此时过多，账户已锁定！";
	public final static int Service_Return_SMSCodeSendFailed = 31101010;
	public final static String Service_Return_SMSCodeSendFailed_Desc = "短信验证码发送失败！";
	public final static int Service_Return_ParameterError = 31101011;
	public final static String Service_Return_ParameterError_Desc = "参数错误！";
	public final static int Service_Return_SMSCodeError = 31101012;
	public final static String Service_Return_SMSCodeError_Desc = "短信验证码错误！";
	public final static int Service_Return_SMSCodeInvalidation = 31101013;
	public final static String Service_Return_SMSCodeInvalidation_Desc = "短信验证码失效！";
	public final static int Service_Return_UserNotActivated = 31101014;
	public final static String Service_Return_UserNotActivated_Desc = "用户没有激活！";
	public final static int Service_Return_UserNotExist = 31101015;
	public final static String Service_Return_UserNotExist_Desc = "用户不存在！";
	public final static int Service_Return_UserNotLogin = 31101016;
	public final static String Service_Return_UserNotLogin_Desc = "用户没有登录！";
	public final static int Service_Return_UserIsExist = 31101018;
	public final static String Service_Return_UserIsExist_Desc = "用户已经注册！";
	public final static int Service_Return_MobileNotExist = 31101022;
	public final static String Service_Return_MobileNotExist_Desc = "手机号码不存在！";
	public final static int Service_Return_LogoutFailed = 31101024;
	public final static String Service_Return_LogoutFailed_Desc = "登出失败！";
	public final static int Service_Return_PSWDSetFailed = 31101025;
	public final static String Service_Return_PSWDSetFailed_Desc = "密码设置失败！";
	public final static int Service_Return_MessageCollectionFailed = 31103002;
	public final static String Service_Return_MessageCollectionFailed_Desc = "信息收集失败！";

	public final static int kOpenAPIURLError = 60101001;   //OpenApi 调用 URL 不符合规范
	public final static int kOpenAPIParameterNil = 60101002;   //OpenApi 接口参数为空
	public final static int kOpenAPIParameterError = 60101003; //OpenApi 接口参数错误
	public final static int kOpenAPIAppKeyNotExist = 60101004; //AppKey 不存在
	public final static int kOpenAPISignatureError = 60101005; //签名校验不通过
	public final static int kOpenAPIPermissionDenied = 60101006;   //没有请求资源的权限
	public final static int kOpenAPIIPDenied = 60101007;   //IP 禁止
	public final static int kOpenAPITimeout = 60101008;    //时间戳超时

	public final static String SERVICE_REGISTER 					= "doRegister"; // 用户注册
	//public final static String SERVICE_LOGIN 						= "doLogin"; // 用户登录
	public final static String SERVICE_LOGOUT 						= "doLogout"; // 用户注销
	public final static String SERVICE_RESET_PSWD 					= "resetPswd"; // 重置密码(短信)
	public final static String SERVICE_CHANGE_PSWD		 			= "changePswd"; // 修改密码
	public final static String SERVICE_GET_SMS_CODE		 			= "getSmsCode"; // 获取短信验证码
	public final static String SERVICE_REGISTER_DEVICE		 		= "registerDevice"; // 注册设备
	public final static String SERVICE_UNREGISTER_DEVICE		 	= "unregisterDevice"; // 反注册设备
	public final static String SERVICE_PAY		 					= "pay"; // 支付

	private static HashMap<String, Method> methodMap;

	/**
	 * Http头域信息,在Application中配置并设置相关值
	 */
	public static SaikeMobileHead httpHeader = new SaikeMobileHead();

	/**
	 * 方法映射 用于TaskPool
	 * 
	 * @return
	 */
	public HashMap<String, Method> getMethodMap() {
		return methodMap;
	}

	public Method getMethodByName(String strName) {
		return methodMap.get(strName);
	}

	public ServiceMediator() {
		Method[] methods = this.getClass().getMethods();
		methodMap = new HashMap<String, Method>(128);
		for (int i = 0; i < methods.length; i++) {
			methodMap.put(methods[i].getName(), methods[i]);
		}
	}
	
	/**
	 *  OpenAPI 登陆接口
	 *
	 *  @param account       账号
	 *  @param password      密码
	 *  
	 *  @return
	 */
	/*
	@Field_Method_Parameter_Annotation(args = {"account","password"})	
	public ServiceResponse<BaseUser> doLogin(String account,String password) {
		// 默认初始化为失败信息
		ServiceResponse<BaseUser> response = new ServiceResponse<BaseUser>();
		response.setOperatCode(RequestDataType.LOGIN);

		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("account", account);
		jsonObj.addProperty("password", CommonUtil.MD5Encrypt(password));
		jsonObj.addProperty("clientId", httpHeader.clientId);
		String requestStr = jsonObj.toString();
		// 参数转换为Json
		NetworkResponse netResponse = NetworkAccess.httpRequestAuth(RequestDataType.LOGIN, requestStr);
		
		// HTTP结果返回
		// 1.错误码转义
		String responseStr = ServiceUtils.getRequestResult(response, netResponse);
		if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
			// 返回成功 解析结果
			JsonHandler.jsonToObject(responseStr, BaseUser.class, response);
			httpHeader.userToken = response.getResponse().token;
		}
		return response;
	}
*/
	
	



}