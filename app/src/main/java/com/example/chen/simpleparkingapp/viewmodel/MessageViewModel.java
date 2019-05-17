package com.example.chen.simpleparkingapp.viewmodel;

import com.example.chen.simpleparkingapp.base.AppViewModel;
import com.example.chen.simpleparkingapp.model.Message;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.tasktool.TaskToken;

import java.util.List;

public class MessageViewModel extends AppViewModel {

    public List<Message> messageList;
    public Message message;

    @Override
    public void paddingResult(TaskToken token) {
        super.paddingResult(token);
        ServiceResponse response = getResponse();
        if (token.method.equals(AppServiceMediator.SERVICE_GET_MESSAGE_LIST)) {
            messageList = (List<Message>) response.getResponse();
        } else if (token.method.equals(AppServiceMediator.SERVICE_GET_MESSAGE_BY_ID)) {
            message = (Message) response.getResponse();
        }
    }
}
