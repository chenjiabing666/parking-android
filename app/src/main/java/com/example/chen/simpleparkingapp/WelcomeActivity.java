package com.example.chen.simpleparkingapp;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.controller.MainActivity;
import com.example.chen.simpleparkingapp.controller.login.LoginActivity;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.utils.CheckUtils;
import com.example.chen.simpleparkingapp.viewmodel.WelcomeViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.mvvm.ViewModelManager;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;

import java.util.HashMap;

public class WelcomeActivity extends BaseActivity {

    private WelcomeViewModel presentViewModel;
    private CountDownTimer timer;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        baseViewModel = ViewModelManager.manager().newViewModel(WelcomeActivity.class.getName());
        baseViewModel.setActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                user = UserCenter.getInstance().getUser();

                if (user == null) {
                    Route.route().nextController(WelcomeActivity.this, LoginActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                    finish();
                } else {
                    login();
                }
            }
        };

        timer.start();
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_DO_LOGIN)) {
            dismissProgress();
            User cuUser = presentViewModel.user;
            cuUser.setPassword(user.getPassword());
            Route.route().nextController(WelcomeActivity.this, MainActivity.class.getName(), Route.WITHOUT_RESULTCODE);
            finish();
        }
    }

    private void login() {
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", user.getMobile());
        params.put("password", user.getPassword());
        doTask(AppServiceMediator.SERVICE_DO_LOGIN, params);
    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (WelcomeViewModel) baseViewModel;
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}
