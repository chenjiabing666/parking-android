package com.example.chen.simpleparkingapp.controller.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.model.Message;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.viewmodel.MessageViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.tasktool.TaskToken;

import java.util.HashMap;

public class MessageDetailActivity extends BaseActivity implements View.OnClickListener {

    private MessageViewModel presentViewModel;
    public static final String ID = "id";
    private int id;
    private TextView tvData, tvTitle, tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        id = getIntent().getIntExtra(ID, 0);

        initView();
        getMessagebyId();
    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (MessageViewModel) baseViewModel;
    }

    private void initView() {
        initTitle();
        tvData = findViewById(R.id.tvData);
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
    }

    private void initTitle() {
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        tvTitle.setText("消息详情");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
        }
    }

    //获取消息
    private void getMessagebyId() {
        showDialog();
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("messageId", id + "");
        doTask(AppServiceMediator.SERVICE_GET_MESSAGE_BY_ID, parmas);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_MESSAGE_BY_ID)) {
            dismissProgress();
            Message message = presentViewModel.message;
            if (message != null) {
                if (!TextUtils.isEmpty(message.getCreatedDate())) {
                    tvData.setText(message.getCreatedDate());
                }
                if (!TextUtils.isEmpty(message.getTitle())) {
                    tvTitle.setText(message.getTitle());
                }
                if (!TextUtils.isEmpty(message.getContent())) {
                    tvContent.setText(message.getContent());
                }
            }
        }
    }
}
