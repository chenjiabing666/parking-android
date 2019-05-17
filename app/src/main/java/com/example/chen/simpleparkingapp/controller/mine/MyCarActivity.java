package com.example.chen.simpleparkingapp.controller.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.dialog.CustomEditDialog;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;

import java.util.HashMap;

public class MyCarActivity extends BaseActivity implements View.OnClickListener, CustomEditDialog.OnConfirmClickListener {

    private TextView tvCarNumber;
    private User user;
    private CustomEditDialog customEditDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        user = UserCenter.getInstance().getUser();
        initView();
    }

    private void initView() {
        customEditDialog = new CustomEditDialog(this);
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        findViewById(R.id.tvEdit).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        tvTitle.setText("我的车辆");
        tvCarNumber = findViewById(R.id.tvCarNumber);
        tvCarNumber.setText(TextUtils.isEmpty(user.getCarNumber()) ? "车牌号：" : "车牌号：" + user.getCarNumber());
        customEditDialog.setListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tvEdit:
                if (!customEditDialog.isShowing()) {
                    customEditDialog.setContent("确定修改车牌号？", user.getCarNumber());
                    customEditDialog.show();
                }
                break;

        }
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_MODIFY_USER)) {
            dismissProgress();
            ToastUtils.show(this,"修改成功");
            user.setCarNumber(token.flag);
            UserCenter.getInstance().setUser(user);
            finish();
        }
    }

    @Override
    public void onConfirmClick(String content) {
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "" + user.getId());
        params.put("nickName", "");
        params.put("carNumber", content);
        params.put("icon", "");
        doTask(AppServiceMediator.SERVICE_MODIFY_USER, params,content);
    }


}
