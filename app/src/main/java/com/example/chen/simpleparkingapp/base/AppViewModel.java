package com.example.chen.simpleparkingapp.base;


import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.taco.mvvm.ViewModel;
import com.example.chen.taco.networkservice.ServiceMediator;
import com.example.chen.taco.tasktool.TaskToken;

public class AppViewModel extends ViewModel {

    @Override
    public void paddingResult(TaskToken token) {

    }

    @Override
    public ServiceMediator getServiceMediator() {
        AppServiceMediator s = AppServiceMediator.sharedInstance();
        return s;
    }
}
