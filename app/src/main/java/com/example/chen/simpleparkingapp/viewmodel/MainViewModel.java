package com.example.chen.simpleparkingapp.viewmodel;

import com.example.chen.simpleparkingapp.base.AppViewModel;
import com.example.chen.simpleparkingapp.model.Home;
import com.example.chen.simpleparkingapp.model.Parking;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.tasktool.TaskToken;

import java.util.List;

public class MainViewModel extends AppViewModel {
    public Home home;
    public List<Parking> nearList;

    @Override
    public void paddingResult(TaskToken token) {
        super.paddingResult(token);
        ServiceResponse response = getResponse();
        if (token.method.equals(AppServiceMediator.SERVICE_GET_HOME)) {
            home = (Home) response.getResponse();
        }else if (token.method.equals(AppServiceMediator.SERVICE_GET_NEAR_BY_PARKING)) {
            nearList = (List<Parking>) response.getResponse();
        }
    }
}
