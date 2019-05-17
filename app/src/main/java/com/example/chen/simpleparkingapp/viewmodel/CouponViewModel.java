package com.example.chen.simpleparkingapp.viewmodel;

import com.example.chen.simpleparkingapp.base.AppViewModel;
import com.example.chen.simpleparkingapp.model.Coupon;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.CommonUtil;

import java.util.List;

public class CouponViewModel extends AppViewModel {

    public List<Coupon> couponList;

    @Override
    public void paddingResult(TaskToken token) {
        super.paddingResult(token);
        ServiceResponse response = getResponse();
        if (token.method.equals(AppServiceMediator.SERVICE_GET_FORGROUNDCOUPON_LIST)) {
            couponList = (List<Coupon>) response.getResponse();
        } else if (token.method.equals(AppServiceMediator.SERVICE_GET_COUPON_BY_ORDERID)) {
            couponList = (List<Coupon>) response.getResponse();
        }
    }
}
