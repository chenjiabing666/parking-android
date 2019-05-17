package com.example.chen.simpleparkingapp.viewmodel;

import com.example.chen.simpleparkingapp.base.AppViewModel;
import com.example.chen.simpleparkingapp.model.Parking;
import com.example.chen.simpleparkingapp.model.RsParking;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.tasktool.TaskToken;

import java.util.List;

public class ParkingViewModel extends AppViewModel {

    public List<Parking> parkingList;
    public RsParking parking;
    public List<Parking> searchList;

    @Override
    public void paddingResult(TaskToken token) {
        super.paddingResult(token);
        ServiceResponse response = getResponse();
        if (token.method.equals(AppServiceMediator.SERVICE_GET_PARKING_LIST)) {
            parkingList = (List<Parking>) response.getResponse();
        } else if (token.method.equals(AppServiceMediator.SERVICE_GET_PARKING_BY_ID)) {
            parking = (RsParking) response.getResponse();
        } else if (token.method.equals(AppServiceMediator.SERVICE_DO_SEARCH)) {
            searchList = (List<Parking>) response.getResponse();
        }
    }
}
