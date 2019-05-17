package com.example.chen.simpleparkingapp.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.simpleparkingapp.R;


public class CodeDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private TextView tvRefresh;
    private String codeUrl;
    private ImageView ivErWeima;

    public CodeDialog(Context context) {
        super(context, R.style.MyCustomDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_code);
        initView();
    }

    private void initView() {
        tvRefresh = findViewById(R.id.tvRefresh);
        tvRefresh.setOnClickListener(this);
        ivErWeima = findViewById(R.id.iv_er_weima);

        if (!TextUtils.isEmpty(codeUrl)) {
            Glide.with(context).load(codeUrl).error(R.drawable.load_img_err).into(ivErWeima);
        }
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRefresh:
                if (listener != null) {
                    listener.onRefreshCode();
                }
                break;
        }
    }

    public interface OnRefreshClickListener {
        void onRefreshCode();
    }

    private OnRefreshClickListener listener;

    public void setListener(OnRefreshClickListener listener) {
        this.listener = listener;
    }

    public void upCodeUrl(String codeUrl){
        this.codeUrl = codeUrl;
        if (ivErWeima!=null) {
            if (!TextUtils.isEmpty(codeUrl)) {
                Glide.with(context).load(codeUrl).error(R.drawable.load_img_err).into(ivErWeima);
            }
        }
    }
}
