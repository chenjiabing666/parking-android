package com.example.chen.simpleparkingapp.viewmodel;

import com.example.chen.simpleparkingapp.base.AppViewModel;
import com.example.chen.simpleparkingapp.model.Code;
import com.example.chen.simpleparkingapp.model.Order;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.tasktool.TaskToken;

import java.util.List;

public class OrderViewModel extends AppViewModel {
    public List<Order> oderList;
    public Order order;
    public Code code;

    @Override
    public void paddingResult(TaskToken token) {
        super.paddingResult(token);
        ServiceResponse response = getResponse();
        if (token.method.equals(AppServiceMediator.SERVICE_GET_ORDERLIST_FOREGROUND)) {
            oderList = (List<Order>) response.getResponse();
        }else if (token.method.equals(AppServiceMediator.SERVICE_GET_ORDER_BY_ID)) {
            order = (Order) response.getResponse();
        } else if (token.method.equals(AppServiceMediator.SERVICE_REFRESH_CODE)) {
            code = (Code) response.getResponse();
        }
    }
}
