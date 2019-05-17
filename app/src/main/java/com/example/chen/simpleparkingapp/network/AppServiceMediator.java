package com.example.chen.simpleparkingapp.network;

import android.text.TextUtils;

import com.example.chen.simpleparkingapp.model.*;
import com.example.chen.taco.networkaccessor.NetworkAccess;
import com.example.chen.taco.networkaccessor.NetworkResponse;
import com.example.chen.taco.networkservice.Field_Method_Parameter_Annotation;
import com.example.chen.taco.networkservice.ServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.networkservice.ServiceUtils;
import com.example.chen.taco.parser.JsonHandler;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppServiceMediator extends ServiceMediator {

    public final static String TAG = "NetworkAccess";

    //用户
    public static final String SERVICE_DO_REGISTER = "register";//注册
    public static final String SERVICE_GET_CODE = "getCode";//获取验证码
    public static final String SERVICE_DO_LOGIN = "login";//登录
    public static final String SERVICE_FORGET_PASSWORD = "forgetPassword";//忘记密码
    public static final String SERVICE_GET_USER_INFO = "getUserById";//获取用户信息
    public static final String SERVICE_EDIT_PSW = "modifyPassword";//修改密码
    public static final String SERVICE_EDIT_UAER_INFO = "modifyUserInfo";//修改个人资料
    public static final String SERVICE_MODIFY_USER = "modifyUser";
    public static final String SERVICE_GET_YEAR_VIP_LIST = "getYearVipList";//年费会员
    public static final String GET_BLANCE = "getBlance";//余额
    //主页
    public static final String SERVICE_GET_HOME = "home";//主页

    // 消息
    public static final String SERVICE_GET_MESSAGE_LIST = "getMessageList";//消息列表
    public static final String SERVICE_GET_MESSAGE_BY_ID = "getMessageById";//获取消息详情
    public static final String SERVICE_DELETE_MESSAGE_BY_ID = "deleteMessageById";//删除消息

    //排行榜
    public static final String SERVICE_GET_RANKING_LIST = "getRankingList";//排行榜

    //停车场
    public static final String SERVICE_GET_PARKING_LIST = "getForegroundParkingList";//停车资源
    public static final String SERVICE_GET_NEAR_BY_PARKING = "getNearbyParking";//附近停车场
    public static final String SERVICE_GET_PARKING_BY_ID = "getParkingById";//获取停车场详情

    public static final String SERVICE_DO_SEARCH = "doSearch";//搜索

    //订单
    public static final String SERVICE_ADD_ORDER = "addOrder";//添加订单
    public static final String SERVICE_OPEN_YEAR = "openYear";//开通年费
    public static final String SERVICE_CANCLE_ORDER = "cancleOrder";//取消订单
    public static final String SERVICE_GET_ORDERLIST_FOREGROUND = "getOrderListForeground";//订单列表
    public static final String SERVICE_GET_ORDER_BY_ID = "getOrderById";//获取订单详情
    public static final String SERVICE_PAY_ORDER = "pay";//付款

    //优惠券
    public static final String SERVICE_GET_FORGROUNDCOUPON_LIST = "getForgroundCouponList";
    public static final String SERVICE_GET_COUPON_BY_ORDERID = "getCouponByOrderId";
    public static final String SERVICE_REFRESH_CODE = "refreshCode";//刷新code


    private static AppServiceMediator mediator;

    private AppServiceMediator() {
        super();
    }

    public static AppServiceMediator sharedInstance() {
        if (mediator == null) {
            synchronized (AppServiceMediator.class) {
                if (mediator == null) {
                    mediator = new AppServiceMediator();
                }
            }
        }
        return mediator;
    }

    public String getRequestStr(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        //sb.append("?");
        if (params != null & params.size() != 0) {

            for (Map.Entry<String, String> entry : params.entrySet()) {

                // 如果请求参数中有中文，需要进行URLEncoder编码
                sb.append(entry.getKey()).append("=").append(entry.getValue());

                sb.append("&");

            }
            sb.deleteCharAt(sb.length() - 1);
        }
        LogUtil.v(TAG, "入参==" + sb.toString());
        return sb.toString();
    }

    //注册
    @Field_Method_Parameter_Annotation(args = {"mobile", "password", "code"})
    public ServiceResponse<String> register(String mobile, String password, String code) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.DO_REGISTER);

        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("code", code);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.DO_REGISTER, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    //获取验证码
    @Field_Method_Parameter_Annotation(args = {"mobile"})
    public ServiceResponse<String> getCode(String mobile) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_CODE);

        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_CODE, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    // 登录
    @Field_Method_Parameter_Annotation(args = {"mobile", "password"})
    public ServiceResponse<User> login(String mobile, String password) {
        ServiceResponse<User> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.DO_LOGIN);

        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.DO_LOGIN, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToObject(responseStr, User.class, response);
        }
        return response;
    }

    //忘记密码
    @Field_Method_Parameter_Annotation(args = {"mobile", "code", "newPwd"})
    public ServiceResponse<String> forgetPassword(String mobile, String code, String newPwd) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.FORGET_PSW);

        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("newPwd", newPwd);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.FORGET_PSW, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    //获取用户信息
    @Field_Method_Parameter_Annotation(args = {"id"})
    public ServiceResponse<User> getUserById(String id) {
        ServiceResponse<User> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_USER_BY_ID);

        Map<String, String> params = new HashMap<>();
        params.put("userId", id);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_USER_BY_ID, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToObject(responseStr, User.class, response);
        }
        return response;
    }

    // 修改密码
    @Field_Method_Parameter_Annotation(args = {"mobile", "oldPwd", "newPwd"})
    public ServiceResponse<String> modifyPassword(String mobile, String oldPwd, String newPwd) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.EDIT_PASSWORD);

        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("oldPwd", oldPwd);
        params.put("newPwd", newPwd);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.EDIT_PASSWORD, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    //修改个人资料
    @Field_Method_Parameter_Annotation(args = {"userId", "nickName", "gender", "icon"})
    public ServiceResponse<String> modifyUserInfo(String userId, String nickName, String gender, String icon) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.EDIT_USER_INFO);

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("nickName", nickName);
        params.put("gender", gender);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse;

        boolean hasFile = false;
        if (!TextUtils.isEmpty(icon)) {
            File file = new File(icon);
            hasFile = file.exists();
        }
        if (hasFile) {
            File file = new File(icon);
            netResponse = NetworkAccess.httpRequestByForm(AppRequestUrl.EDIT_USER_INFO, params, file, false);
        } else {
            netResponse = NetworkAccess.httpRequest(AppRequestUrl.EDIT_USER_INFO, requestStr);
        }
        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    //
    @Field_Method_Parameter_Annotation(args = {"userId", "nickName", "carNumber", "icon"})
    public ServiceResponse<String> modifyUser(String userId, String nickName, String carNumber, String icon) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.EDIT_USER_INFO);

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("nickName", nickName);
        params.put("carNumber", carNumber);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse;

        boolean hasFile = false;
        if (!TextUtils.isEmpty(icon)) {
            File file = new File(icon);
            hasFile = file.exists();
        }
        if (hasFile) {
            File file = new File(icon);
            netResponse = NetworkAccess.httpRequestByForm(AppRequestUrl.EDIT_USER_INFO, params, file, false);
        } else {
            netResponse = NetworkAccess.httpRequest(AppRequestUrl.EDIT_USER_INFO, requestStr);
        }
        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    //消息列表
    @Field_Method_Parameter_Annotation(args = {"pageNum", "pageSize", "userId"})
    public ServiceResponse<List<Message>> getMessageList(String pageNum, String pageSize, String userId) {
        ServiceResponse<List<Message>> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_MESSAGE_LIST);

        HashMap<String, String> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("userId", userId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_MESSAGE_LIST, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            Type type = new TypeToken<List<Message>>() {
            }.getType();
            JsonHandler.jsonToList(responseStr, type, response);
        }
        return response;
    }

    //消息详情
    @Field_Method_Parameter_Annotation(args = {"messageId"})
    public ServiceResponse<Message> getMessageById(String messageId) {
        ServiceResponse<Message> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_MESSAGE_BY_ID);

        HashMap<String, String> params = new HashMap<>();
        params.put("messageId", messageId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_MESSAGE_BY_ID, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToObject(responseStr, Message.class, response);
        }
        return response;
    }

    //删除消息
    @Field_Method_Parameter_Annotation(args = {"messageId"})
    public ServiceResponse<String> deleteMessageById(String messageId) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.DELETE_MESSAGE_BY_ID);

        HashMap<String, String> params = new HashMap<>();
        params.put("messageId", messageId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.DELETE_MESSAGE_BY_ID, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    @Field_Method_Parameter_Annotation(args = {"userId"})
    public ServiceResponse<Home> home(String userId) {
        ServiceResponse<Home> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_HOME);

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        String requestData = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_HOME,requestData);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToObject(responseStr, Home.class, response);
        }
        return response;
    }

    @Field_Method_Parameter_Annotation(args = {"pageNum", "pageSize"})
    public ServiceResponse<List<Parking>> getForegroundParkingList(String pageNum, String pageSize) {
        ServiceResponse<List<Parking>> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_FOREGROUND_PARKING_LIST);

        HashMap<String, String> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        String requestData = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_FOREGROUND_PARKING_LIST, requestData);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            Type type = new TypeToken<List<Parking>>() {
            }.getType();
            JsonHandler.jsonToList(responseStr, type, response);
        }
        return response;
    }

    //
    @Field_Method_Parameter_Annotation(args = {"longitude", "latitude"})
    public ServiceResponse<List<Parking>> getNearbyParking(String longitude, String latitude) {
        ServiceResponse<List<Parking>> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_NEAR_BY_PARKING);

        HashMap<String, String> params = new HashMap<>();
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        String requestData = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_NEAR_BY_PARKING, requestData);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            Type type = new TypeToken<List<Parking>>() {
            }.getType();
            JsonHandler.jsonToList(responseStr, type, response);
        }
        return response;
    }

    //
    @Field_Method_Parameter_Annotation(args = {"name"})
    public ServiceResponse<List<Parking>> doSearch(String name) {
        ServiceResponse<List<Parking>> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_PARKING_BY_NAME);

        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        String requestData = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_PARKING_BY_NAME, requestData);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            Type type = new TypeToken<List<Parking>>() {
            }.getType();
            JsonHandler.jsonToList(responseStr, type, response);
        }
        return response;
    }

    //
    @Field_Method_Parameter_Annotation(args = {"parkingId"})
    public ServiceResponse<RsParking> getParkingById(String parkingId) {
        ServiceResponse<RsParking> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_PARKING_BY_ID);

        HashMap<String, String> params = new HashMap<>();
        params.put("parkingId", parkingId);
        String requestData = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_PARKING_BY_ID, requestData);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToObject(responseStr, RsParking.class, response);
        }
        return response;
    }

    @Field_Method_Parameter_Annotation(args = {"userId", "startTime", "endTime", "parkingId"})
    public ServiceResponse<String> addOrder(String userId, String startTime, String endTime, String parkingId) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.ADD_ORDER);

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("parkingId", parkingId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.ADD_ORDER, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    //开通年费
    @Field_Method_Parameter_Annotation(args = {"userId", "parkingId"})
    public ServiceResponse<String> openYear(String userId, String parkingId) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.OPEN_YEAR);

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("parkingId", parkingId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.OPEN_YEAR, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    @Field_Method_Parameter_Annotation(args = {"pageNum", "pageSize", "userId", "status"})
    public ServiceResponse<List<Order>> getOrderListForeground(String pageNum, String pageSize, String userId, String status) {
        ServiceResponse<List<Order>> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_ORDERLIST_FOREGROUND);

        HashMap<String, String> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("userId", userId);
        params.put("status", status);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_ORDERLIST_FOREGROUND, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            Type type = new TypeToken<List<Order>>() {
            }.getType();
            JsonHandler.jsonToList(responseStr, type, response);
        }
        return response;
    }

    // 获取订单详情
    @Field_Method_Parameter_Annotation(args = {"orderId"})
    public ServiceResponse<Order> getOrderById(String pageNum) {
        ServiceResponse<Order> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_ORDER_BY_ID);

        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", pageNum);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_ORDER_BY_ID, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToObject(responseStr, Order.class, response);
        }
        return response;
    }

    //  优惠券
    @Field_Method_Parameter_Annotation(args = {"pageNum", "pageSize", "userId"})
    public ServiceResponse<List<Coupon>> getForgroundCouponList(String pageNum, String pageSize, String userId) {
        ServiceResponse<List<Coupon>> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_FORGROUNDCOUPON_LIST);

        HashMap<String, String> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("userId", userId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_FORGROUNDCOUPON_LIST, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            Type type = new TypeToken<List<Coupon>>() {
            }.getType();
            JsonHandler.jsonToList(responseStr, type, response);
        }
        return response;
    }

    //
    @Field_Method_Parameter_Annotation(args = {"orderId", "userId"})
    public ServiceResponse<List<Coupon>> getCouponByOrderId(String orderId, String userId) {
        ServiceResponse<List<Coupon>> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_COUPON_BY_ORDER_ID);

        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("userId", userId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_COUPON_BY_ORDER_ID, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            Type type = new TypeToken<List<Coupon>>() {
            }.getType();
            JsonHandler.jsonToList(responseStr, type, response);
        }
        return response;
    }

    //
    @Field_Method_Parameter_Annotation(args = {"orderId", "couponId"})
    public ServiceResponse<String> pay(String orderId, String couponId) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.PAY_ORDER);

        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("couponId", couponId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.PAY_ORDER, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    //
    @Field_Method_Parameter_Annotation(args = {"orderId"})
    public ServiceResponse<String> cancleOrder(String orderId) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.CANCLE_ORDER);

        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.CANCLE_ORDER, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToString(responseStr, response);
        }
        return response;
    }

    //
    @Field_Method_Parameter_Annotation(args = {"userId"})
    public ServiceResponse<List<YearVipVos>> getYearVipList(String userId) {
        ServiceResponse<List<YearVipVos>> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.GET_YEAR_VIP_LIST);

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.GET_YEAR_VIP_LIST, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            Type type = new TypeToken<List<YearVipVos>>() {

            }.getType();
            JsonHandler.jsonToList(responseStr, type, response);
        }
        return response;
    }

    //
    @Field_Method_Parameter_Annotation(args = {"userId", "parkingId", "type", "orderId"})
    public ServiceResponse<Code> refreshCode(String userId, String parkingId, String type, String orderId) {
        ServiceResponse<Code> response = new ServiceResponse<>();
        response.setOperatCode(AppRequestUrl.REFRESH_CODE);

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("parkingId", parkingId);
        params.put("type", type);
        params.put("orderId", orderId);
        String requestStr = getRequestStr(params);

        NetworkResponse netResponse = NetworkAccess.httpRequest(AppRequestUrl.REFRESH_CODE, requestStr);

        String responseStr = ServiceUtils.getRequestResult(response, netResponse);
        if (response.getReturnCode() == ServiceMediator.Service_Return_Success) {
            JsonHandler.jsonToObject(responseStr, Code.class, response);
        }
        return response;
    }
}
