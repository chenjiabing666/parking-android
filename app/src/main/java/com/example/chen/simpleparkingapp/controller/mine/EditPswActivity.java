package com.example.chen.simpleparkingapp.controller.mine;

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

public class EditPswActivity extends BaseActivity implements View.OnClickListener {

    private PersonInfoViewModel presentViewModel;
    private ClearWriteEditText clCurrentPsw, clNewPsw, clConfirmPsw;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_psw);
        user = UserCenter.getInstance().getUser();
        ActivityManager.addActivity(this);
        initView();
    }

    private void initView() {
        initTitle();
        clCurrentPsw = findViewById(R.id.et_current_psw);
        clNewPsw = findViewById(R.id.et_new_psw);
        clConfirmPsw = findViewById(R.id.et_confirm_psw);
        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    private void initTitle() {
        View titleView = findViewById(R.id.title_et_psw);
        TextView tvTitle = titleView.findViewById(R.id.tv_title_middle);
        tvTitle.setText("修改密码");
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
            case R.id.btn_save:
                editPsw();
                break;
        }
    }

    //修改密码
    private void editPsw() {
        if (user == null) {
            ToastUtils.show(this, "用户信息为空，请重新登录");
            Route.route().nextController(this, LoginActivity.class.getName(), Route.WITHOUT_RESULTCODE);
            ActivityManager.finishAllActivity();
        }
        String currentPsw = clCurrentPsw.getText().toString().trim();
        String newPsw = clNewPsw.getText().toString().trim();
        String confirmPsw = clConfirmPsw.getText().toString().trim();

        if (TextUtils.isEmpty(currentPsw) || TextUtils.isEmpty(newPsw) || TextUtils.isEmpty(confirmPsw)) {
            ToastUtils.show(this, "输入的密码不能为空");
            return;
        }

        if (!newPsw.equals(confirmPsw)) {
            ToastUtils.show(this, "两次密码不一致");
            return;
        }

        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", "" + user.getMobile());
        params.put("oldPwd", currentPsw);
        params.put("newPwd", newPsw);
        doTask(AppServiceMediator.SERVICE_EDIT_PSW, params);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        dismissProgress();
        if (token.method.equals(AppServiceMediator.SERVICE_EDIT_PSW)) {
            ToastUtils.show(this, "密码修改成功");
            finish();
        }
    }
}
