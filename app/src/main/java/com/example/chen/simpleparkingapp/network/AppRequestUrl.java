package com.example.chen.simpleparkingapp.network;

import com.example.chen.taco.networkaccessor.RequestUrl;

public class AppRequestUrl extends RequestUrl {

    //用户
    public static final String GET_CODE = BASE_URL + "user/getCode.do";//获取验证码
    public static final String DO_REGISTER = BASE_URL + "user/registry.do";//注册
    public static final String DO_LOGIN = BASE_URL + "user/login.do";//登录
    public static final String FORGET_PSW = BASE_URL + "user/forgetPassword.do";//忘记密码
    public static final String GET_USER_BY_ID = BASE_URL + "user/getUserById.do";//获取用户信息
    public static final String EDIT_PASSWORD = BASE_URL + "user/modifyPassword.do";//修改密码
    public static final String EDIT_USER_INFO = BASE_URL + "user/modifyUser.do";//修改个人信息

    //消息
    public static final String GET_MESSAGE_LIST = BASE_URL + "message/getMessageList.do";//消息列表
    public static final String GET_MESSAGE_BY_ID = BASE_URL + "message/getMessageById.do";//消息详情
    public static final String DELETE_MESSAGE_BY_ID = BASE_URL + "message/deleteMessageById.do";//删除消息

    //主页
    public static final String GET_HOME = BASE_URL + "index/getIndex.do";//主页

    //停车场信息
    public static final String GET_FOREGROUND_PARKING_LIST = BASE_URL + "parking/getForegroundParkingList.do";//停车列表;
    public static final String GET_PARKING_BY_ID = BASE_URL + "parking/getParkingById.do";//获取停车场详情
    public static final String GET_PARKING_BY_NAME = BASE_URL + "parking/getParkingByName.do";//搜索

    //订单
    public static final String GET_ORDERLIST_FOREGROUND = BASE_URL + "order/getOrderListForeground.do";//订单列表
    public static final String ADD_ORDER = BASE_URL + "order/addOrder.do";//添加订单
    public static final String OPEN_YEAR = BASE_URL + "parking/openYear.do";//开通年费
    public static final String GET_ORDER_BY_ID = BASE_URL + "order/getOrderById.do";//获取订单详情
    public static final String PAY_ORDER = BASE_URL + "order/pay.do";//付款
    public static final String CANCLE_ORDER = BASE_URL + "order/modifyOrder.do";//取消订单

    public static final String GET_FORGROUNDCOUPON_LIST = BASE_URL + "coupon/getForgroundCouponList.do";//优惠券
    public static final String GET_COUPON_BY_ORDER_ID = BASE_URL + "coupon/getCouponByOrderId.do";
    public static final String GET_YEAR_VIP_LIST = BASE_URL + "user/getYearVipList.do";//年费会员
    public static final String REFRESH_CODE = BASE_URL + "user/refreshCode.do";//刷新二维码
    public static final String GET_NEAR_BY_PARKING = BASE_URL + "parking/getNearbyParking.do";//附近停车场
}
