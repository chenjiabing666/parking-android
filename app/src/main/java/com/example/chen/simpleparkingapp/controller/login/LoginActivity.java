package com.example.chen.simpleparkingapp.controller.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.controller.MainActivity;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.utils.CheckUtils;
import com.example.chen.simpleparkingapp.view.widget.ClearWriteEditText;
import com.example.chen.simpleparkingapp.viewmodel.LoginViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private LoginViewModel presentViewModel;
    private ClearWriteEditText etUserName, etPsw;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        initTitle();
        etUserName = findViewById(R.id.et_user_name);
        etPsw = findViewById(R.id.et_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.go_register).setOnClickListener(this);
        findViewById(R.id.go_find_psw).setOnClickListener(this);
    }

    private void initTitle() {
        View titleView = findViewById(R.id.title_login);
        titleView.findViewById(R.id.ivBack).setOnClickListener(this);
    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (LoginViewModel) baseViewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.go_register://注册
                Route.route().nextController(this, RegisterActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
            case R.id.go_find_psw://忘记密码
                Route.route().nextController(this, ForgetPswActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
        }
    }

    private void login() {
        String userName = etUserName.getText().toString().trim();
        password = etPsw.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show(this, "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }
        if (!CheckUtils.getInstances().isMobile(userName)) {
            ToastUtils.show(this, "请输入正确格式的手机号");
            return;
        }
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", userName);
        params.put("password", password);
        doTask(AppServiceMediator.SERVICE_DO_LOGIN, params);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_DO_LOGIN)) {
            dismissProgress();
            User user = presentViewModel.user;
            if (user != null) {
//                user.setPassword(password);
                UserCenter.getInstance().setUser(user);
                Route.route().nextController(this, MainActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                finish();
            }
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
    }
}
