package com.example.chen.simpleparkingapp.controller.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.utils.AppUtils;
import com.example.chen.simpleparkingapp.utils.DataCleanManager;
import com.example.chen.simpleparkingapp.view.dialog.CustomDialog;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.utils.ToastUtils;

public class SettingActivity extends BaseActivity implements View.OnClickListener, CustomDialog.OnConfirmClickListener {


    private TextView tvCashSize, tvVersion;
    private CustomDialog customDialog;
    private String size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initVIew();
    }

    private void initVIew() {
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        findViewById(R.id.ll_about_us).setOnClickListener(this);
        tvTitle.setText("设置");
        tvCashSize = findViewById(R.id.tvCashSize);
        tvVersion = findViewById(R.id.tvVersion);
        findViewById(R.id.ll_clear_cash).setOnClickListener(this);
        findViewById(R.id.ll_update).setOnClickListener(this);
        customDialog = new CustomDialog(this);
        customDialog.setListener(this);
        try {
            size = DataCleanManager.getTotalCacheSize(this);
            tvCashSize.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String versionNum = AppUtils.getInstance().getVersionName(this, AppUtils.getInstance().getPackageName(this));
        if (!TextUtils.isEmpty(versionNum)) {
            tvVersion.setText(versionNum);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_clear_cash:
                if (!customDialog.isShowing()) {
                    customDialog.setContent("确定清除" + size + "缓存？");
                    customDialog.show();
                }
                break;
            case R.id.ll_update://版本更新
                checkVersion();
                break;
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.ll_about_us:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
        }
    }

    //版本更新
    private void checkVersion() {
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(SettingActivity.this, "当前已是最新版本");
                            dismissProgress();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onConfirmClick() {
        clearCash();
    }

    private void clearCash() {
        DataCleanManager.clearAllCache(this);
        size = "0K";
        tvCashSize.setText(size);
        ToastUtils.show(this, "清理缓存成功");
    }
}
