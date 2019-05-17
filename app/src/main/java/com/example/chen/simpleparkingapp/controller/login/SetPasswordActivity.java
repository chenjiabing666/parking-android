package com.example.chen.simpleparkingapp.controller.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.AppApplication;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.widget.ClearWriteEditText;
import com.example.chen.simpleparkingapp.viewmodel.LoginViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;

import java.util.HashMap;

public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {

    public static final String USER_NAME = "userName";
    public static final String CODE = "code";
    private LoginViewModel presentViewModel;
    private ClearWriteEditText etPsw, etConfirmPsw;
    private String userName, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_psw);
        AppApplication.getInstance().addActivity(this);

        initView();
        userName = getIntent().getStringExtra(USER_NAME);
        code = getIntent().getStringExtra(CODE);

    }

    private void initView() {
        initTitle();
        etPsw = findViewById(R.id.et_set_password);
        etConfirmPsw = findViewById(R.id.et_confirm_password);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    private void initTitle() {
        View titleView = findViewById(R.id.title_set_psw);
        titleView.findViewById(R.id.ivBack).setOnClickListener(this);
        TextView tvTitle = titleView.findViewById(R.id.tvTitle);
        tvTitle.setText("设置密码");
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
            case R.id.btn_register:
                goRegister();
                break;
        }
    }

    private void goRegister() {
        String psw = etPsw.getText().toString().trim();
        String pswAgain = etConfirmPsw.getText().toString().trim();
        if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(pswAgain)) {
            ToastUtils.show(this, "密码不能为空");
            return;
        }
        if (!psw.equals(pswAgain)) {
            ToastUtils.show(this, "两次密码不一致");
            return;
        }

        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", userName);
        params.put("password", psw);
        params.put("code", code);
        doTask(AppServiceMediator.SERVICE_DO_REGISTER, params);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_DO_REGISTER)) {
            ToastUtils.show(this, "注册成功,请登录");
            AppApplication.getInstance().clearActivitys();
        }
    }
}
