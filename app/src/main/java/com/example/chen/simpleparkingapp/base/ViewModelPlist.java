package com.example.chen.simpleparkingapp.base;

import com.example.chen.simpleparkingapp.WelcomeActivity;
import com.example.chen.simpleparkingapp.controller.MainActivity;
import com.example.chen.simpleparkingapp.controller.home.MessageDetailActivity;
import com.example.chen.simpleparkingapp.controller.home.MessageListActivity;
import com.example.chen.simpleparkingapp.controller.home.MoreParkingActivity;
import com.example.chen.simpleparkingapp.controller.home.ParkingDetailsActivity;
import com.example.chen.simpleparkingapp.controller.home.SearchActivity;
import com.example.chen.simpleparkingapp.controller.login.ForgetPswActivity;
import com.example.chen.simpleparkingapp.controller.login.LoginActivity;
import com.example.chen.simpleparkingapp.controller.login.RegisterActivity;
import com.example.chen.simpleparkingapp.controller.login.SetPasswordActivity;
import com.example.chen.simpleparkingapp.controller.mine.*;
import com.example.chen.simpleparkingapp.viewmodel.CouponViewModel;
import com.example.chen.simpleparkingapp.viewmodel.MainViewModel;
import com.example.chen.simpleparkingapp.viewmodel.MessageViewModel;
import com.example.chen.simpleparkingapp.viewmodel.MyCarViewModel;
import com.example.chen.simpleparkingapp.viewmodel.OrderViewModel;
import com.example.chen.simpleparkingapp.viewmodel.ParkingViewModel;
import com.example.chen.simpleparkingapp.viewmodel.PersonInfoViewModel;
import com.example.chen.simpleparkingapp.viewmodel.VIPManagerViewModel;
import com.example.chen.simpleparkingapp.viewmodel.WelcomeViewModel;
import com.example.chen.simpleparkingapp.viewmodel.LoginViewModel;

import java.util.HashMap;

public class ViewModelPlist {

    public static HashMap<String, String> hashMap = new HashMap<String, String>();

    static {
        //欢迎页
        hashMap.put(WelcomeActivity.class.getName(), WelcomeViewModel.class.getName());
        //登录
        hashMap.put(LoginActivity.class.getName(), LoginViewModel.class.getName());
        //注册
        hashMap.put(RegisterActivity.class.getName(), LoginViewModel.class.getName());
        //设置密码
        hashMap.put(SetPasswordActivity.class.getName(), LoginViewModel.class.getName());
        //忘记密码
        hashMap.put(ForgetPswActivity.class.getName(), LoginViewModel.class.getName());
        //主页
        hashMap.put(MainActivity.class.getName(), MainViewModel.class.getName());
        //停车场推荐更多
        hashMap.put(MoreParkingActivity.class.getName(), ParkingViewModel.class.getName());
        //停车场详情
        hashMap.put(ParkingDetailsActivity.class.getName(), ParkingViewModel.class.getName());
        hashMap.put(SearchActivity.class.getName(), ParkingViewModel.class.getName());
        //我的订单
        hashMap.put(MyOrderActivity.class.getName(), OrderViewModel.class.getName());
        //付款
        hashMap.put(OrderDetailActivity.class.getName(),OrderViewModel.class.getName());
        //我的车辆
        hashMap.put(MyCarActivity.class.getName(), MyCarViewModel.class.getName());
        //优惠券
        hashMap.put(CouponActivity.class.getName(), CouponViewModel.class.getName());
        hashMap.put(GetCouponByOrderIdActivity.class.getName(), CouponViewModel.class.getName());
        //会员管理
        hashMap.put(VIPManagerActivity.class.getName(), VIPManagerViewModel.class.getName());
        //消息
        hashMap.put(MessageListActivity.class.getName(), MessageViewModel.class.getName());
        //消息列表
        hashMap.put(MessageDetailActivity.class.getName(), MessageViewModel.class.getName());
        //修改姓名
        hashMap.put(EditPersonalNameActivity.class.getName(), PersonInfoViewModel.class.getName());
        //修改密码
        hashMap.put(EditPswActivity.class.getName(), PersonInfoViewModel.class.getName());
        //个人信息
        hashMap.put(PersonInfoActivity.class.getName(),PersonInfoViewModel.class.getName());
    }
}
