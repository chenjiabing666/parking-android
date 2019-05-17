package com.example.chen.simpleparkingapp.controller.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.ActivityManager;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.controller.login.LoginActivity;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.widget.ClearWriteEditText;
import com.example.chen.simpleparkingapp.viewmodel.PersonInfoViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;

import java.util.HashMap;

public class EditPersonalNameActivity extends BaseActivity implements View.OnClickListener {

    private PersonInfoViewModel presentViewModel;
    private ClearWriteEditText etName;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        ActivityManager.addActivity(this);
        user = UserCenter.getInstance().getUser();
        initView();
    }

    private void initView() {
        initTitle();
        etName = (ClearWriteEditText) findViewById(R.id.et_edit_name);
        findViewById(R.id.btnSave).setOnClickListener(this);
    }

    private void initTitle() {
        View titleView = findViewById(R.id.edit_person_name_title);
        TextView titleMiddle = (TextView) titleView.findViewById(R.id.tv_title_middle);
        titleMiddle.setText("修改昵称");
        titleView.findViewById(R.id.ll_title_left).setOnClickListener(this);
    }


    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (PersonInfoViewModel) baseViewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.btnSave:
                save();
                break;
        }
    }

    private void save() {
        if (user == null) {
            ToastUtils.show(this, "当前用户信息为空，请先登录");
            Route.route().nextController(this, LoginActivity.class.getName(), Route.WITHOUT_RESULTCODE);
            ActivityManager.finishAllActivity();
            return;
        }
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.show(this, "请输入昵称");
            return;
        }
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "" + user.getId());
        params.put("nickName", name);
        params.put("gender", "");
        doTask(AppServiceMediator.SERVICE_EDIT_UAER_INFO, params, name);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_EDIT_UAER_INFO)) {
            dismissProgress();
            ToastUtils.show(this, "修改成功");
            Intent intent = new Intent();
            intent.putExtra("nickName", token.flag);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
