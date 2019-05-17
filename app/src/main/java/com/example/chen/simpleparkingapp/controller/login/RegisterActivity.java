package com.example.chen.simpleparkingapp.controller.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.AppApplication;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.widget.ClearWriteEditText;
import com.example.chen.simpleparkingapp.viewmodel.LoginViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private LoginViewModel presentViewModel;
    private ClearWriteEditText etUserName;
    private EditText etCode;
    private TextView tvCode;
    private int GET_CODE = 100;

    private int secode = 60;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_CODE) {
                if (secode == 1) {
                    secode = 60;
                    tvCode.setText("获取");
                    handler.removeCallbacksAndMessages(null);
                    tvCode.setFocusable(true);
                    tvCode.setEnabled(true);
                    tvCode.setClickable(true);
                } else {
                    secode--;
                    tvCode.setText(secode + "s");
                    handler.sendEmptyMessageDelayed(GET_CODE, 1000);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        AppApplication.getInstance().addActivity(this);
    }

    private void initView() {
        initTitle();
        etUserName = findViewById(R.id.et_user_name);
        etCode = findViewById(R.id.et_code);
        tvCode = findViewById(R.id.tv_get_code);
        tvCode.setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.tv_user_need_know).setOnClickListener(this);
    }

    private void initTitle() {
        View titleView = findViewById(R.id.register_title);
        titleView.findViewById(R.id.ivBack).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("注册");
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
            case R.id.tv_get_code://获取验证码
                getCode();
                break;
            case R.id.btn_next://下一步
                nextStep();
                break;
            case R.id.tv_user_need_know://用户须知
                startActivity(new Intent(this,UserNeedKonwActivity.class));
                break;
        }
    }

    //下一步
    private void nextStep() {
        String userName = etUserName.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show(this, "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(this, "请输入验证码");
            return;
        }
        Intent intent = new Intent(this,SetPasswordActivity.class);
        intent.putExtra(SetPasswordActivity.USER_NAME,userName);
        intent.putExtra(SetPasswordActivity.CODE,code);
        Route.route().nextControllerWithIntent(this, SetPasswordActivity.class.getName(), Route.WITHOUT_RESULTCODE,intent);
    }

    //获取验证码
    private void getCode() {
        String mobile = etUserName.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.show(this, "用户名不能为空");
            return;
        }
        tvCode.setEnabled(false);
        tvCode.setClickable(false);
        showDialog();
        HashMap<String,String> params = new HashMap<>();
        params.put("mobile",mobile);
        doTask(AppServiceMediator.SERVICE_GET_CODE,params);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_CODE)) {
            dismissProgress();
            handler.sendEmptyMessage(GET_CODE);
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_CODE)) {
            tvCode.setEnabled(true);
            tvCode.setClickable(true);
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }
}
