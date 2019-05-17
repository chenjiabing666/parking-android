package com.example.chen.simpleparkingapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.controller.MainActivity;
import com.example.chen.simpleparkingapp.controller.login.SettingActivity;
import com.example.chen.simpleparkingapp.controller.mine.CouponActivity;
import com.example.chen.simpleparkingapp.controller.mine.MyCarActivity;
import com.example.chen.simpleparkingapp.controller.mine.MyOrderActivity;
import com.example.chen.simpleparkingapp.controller.mine.PersonInfoActivity;
import com.example.chen.simpleparkingapp.controller.mine.VIPManagerActivity;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.view.widget.RoundCornersImageView;
import com.example.chen.taco.mvvm.Route;
import com.google.gson.Gson;

public class MineFragment extends Fragment implements View.OnClickListener {

    public static boolean isEdit = true;
    private View rootView;
    private MainActivity activity;
    private RoundCornersImageView rvIcon;
    private TextView tvName, tvBalance;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mine, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        initView();
        user = UserCenter.getInstance().getUser();
        System.out.println("---------------------------------------------->"+user.getBalance());
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isEdit) {
            user = UserCenter.getInstance().getUser();
            initData();
//            isEdit = false;
        }
    }

    private void initData() {
        if (!TextUtils.isEmpty(user.getIcon())) {
            Glide.with(activity).load(user.getIcon()).error(R.drawable.icon_no_login).into(rvIcon);
        }
        String name = "匿名用户";
        if (!TextUtils.isEmpty(user.getMobile())) {
            name = user.getMobile();
        }
        if (!TextUtils.isEmpty(user.getNickName())) {
            name = user.getNickName();
        }
        tvName.setText(name);
        if (user.getBalance() != null) {
            tvBalance.setText("余额：" + user.getBalance());
            System.out.println("余额：" + user.getBalance());
        } else {
            tvBalance.setText("余额：0");
        }
    }

    private void initView() {
        rvIcon = rootView.findViewById(R.id.ivIcon);
        tvName = rootView.findViewById(R.id.tvName);
        tvBalance = rootView.findViewById(R.id.tvBalance);
        rootView.findViewById(R.id.ivInfoBg).setOnClickListener(this);
        rootView.findViewById(R.id.tvMyOrder).setOnClickListener(this);
        rootView.findViewById(R.id.tvMyCars).setOnClickListener(this);
        rootView.findViewById(R.id.tvCoupon).setOnClickListener(this);
        rootView.findViewById(R.id.tvManager).setOnClickListener(this);
        rootView.findViewById(R.id.tvSetting).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivInfoBg://个人信息
                Route.route().nextController(activity,PersonInfoActivity.class.getName(),Route.WITHOUT_RESULTCODE);
                break;
            case R.id.tvMyOrder:
                Route.route().nextController(activity, MyOrderActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
            case R.id.tvMyCars://我的车辆
                Route.route().nextController(activity, MyCarActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
            case R.id.tvCoupon://优惠券
                Route.route().nextController(activity, CouponActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
            case R.id.tvManager://会员管理
                Route.route().nextController(activity, VIPManagerActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
            case R.id.tvSetting://设置
                startActivity(new Intent(activity, SettingActivity.class));
                break;
        }
    }
}
