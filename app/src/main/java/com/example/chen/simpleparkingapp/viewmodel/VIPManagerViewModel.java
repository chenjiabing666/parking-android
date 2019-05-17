package com.example.chen.simpleparkingapp.viewmodel;

import com.example.chen.simpleparkingapp.base.AppViewModel;
import com.example.chen.simpleparkingapp.model.Code;
import com.example.chen.simpleparkingapp.model.YearVipVos;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.tasktool.TaskToken;

import java.util.List;

public class VIPManagerViewModel extends AppViewModel {
    public List<YearVipVos> yearVipVosList;
    public Code code;

    @Override
    public void paddingResult(TaskToken token) {
        super.paddingResult(token);
        ServiceResponse response = getResponse();
        if (token.method.equals(AppServiceMediator.SERVICE_GET_YEAR_VIP_LIST)){
            yearVipVosList = (List<YearVipVos>) response.getResponse();
        } else if (token.method.equals(AppServiceMediator.SERVICE_REFRESH_CODE)) {
            code = (Code) response.getResponse();
        }
    }
}
