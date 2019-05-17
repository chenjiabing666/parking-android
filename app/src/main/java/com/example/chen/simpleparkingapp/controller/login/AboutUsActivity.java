package com.example.chen.simpleparkingapp.controller.login;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.taco.mvvm.BaseActivity;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        initView();
    }

    private void initView() {
        findViewById(R.id.ll_title_left).setOnClickListener(this);

        TextView tvMiddle = findViewById(R.id.tv_title_middle);
        tvMiddle.setText("关于我们");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
        }
    }
}
