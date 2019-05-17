package com.example.chen.simpleparkingapp.controller.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.utils.CheckUtils;
import com.example.chen.simpleparkingapp.viewmodel.LoginViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;

import java.util.HashMap;

//忘记密码
public class ForgetPswActivity extends BaseActivity implements View.OnClickListener {

    private LoginViewModel presentViewModel;
    private EditText etUserName, etCode, etNewPsw, etConfirmPsw;
    private TextView tvGetCode;

    private static final int GET_CODE = 100;

    private int secode = 60;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_CODE) {
                if (secode == 1) {
                    secode = 60;
                    tvGetCode.setText("获取");
                    handler.removeCallbacksAndMessages(null);
                    tvGetCode.setFocusable(true);
                    tvGetCode.setEnabled(true);
                    tvGetCode.setClickable(true);
                } else {
                    secode--;
                    tvGetCode.setText(secode + "s");
                    handler.sendEmptyMessageDelayed(GET_CODE, 1000);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
        initView();
    }

    private void initView() {
        initTitle();
        etUserName = findViewById(R.id.et_user_name);
        etCode = findViewById(R.id.et_code);
        tvGetCode = findViewById(R.id.tv_get_code);
        etNewPsw = findViewById(R.id.et_new_psw);
        etConfirmPsw = findViewById(R.id.et_confirm_psw);
        tvGetCode.setOnClickListener(this);
        findViewById(R.id.btn_confirm_edit).setOnClickListener(this);
    }

    private void initTitle() {
        View titleView = findViewById(R.id.title_forget_psw);
        titleView.findViewById(R.id.ivBack).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("忘记密码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.btn_confirm_edit:
                confirmEdit();
                break;

        }
    }

    //修改密码
    private void confirmEdit() {
        String userName = etUserName.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String psw = etNewPsw.getText().toString().trim();
        String pswAgain = etConfirmPsw.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show(this, "请输入手机号");
            return;
        }
        if (!CheckUtils.getInstances().isMobile(userName)) {
            ToastUtils.show(this, "请输入正确格式的手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(this, "请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(pswAgain)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }
        if (!psw.equals(pswAgain)) {
            ToastUtils.show(this, "两次密码不一致");
            return;
        }

        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile",userName);
        params.put("code",code);
        params.put("newPwd",psw);
        doTask(AppServiceMediator.SERVICE_FORGET_PASSWORD,params);
    }

    //获取验证码
    private void getCode() {
        String userName = etUserName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show(this, "请输入手机号");
            return;
        }
        if (!CheckUtils.getInstances().isMobile(userName)) {
            ToastUtils.show(this, "请输入正确格式的手机号");
            return;
        }
        tvGetCode.setEnabled(false);
        tvGetCode.setClickable(false);
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", userName);
        doTask(AppServiceMediator.SERVICE_GET_CODE, params);
    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (LoginViewModel) baseViewModel;
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        dismissProgress();
        if (token.method.equals(AppServiceMediator.SERVICE_GET_CODE)) {//验证码
            ToastUtils.show(this, "验证码发送成功");
            handler.sendEmptyMessage(GET_CODE);
        } else if (token.method.equals(AppServiceMediator.SERVICE_FORGET_PASSWORD)) {
            ToastUtils.show(this, "密码重置成功");
            finish();
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_CODE)) {
            tvGetCode.setEnabled(true);
            tvGetCode.setClickable(true);
        }
    }
}
