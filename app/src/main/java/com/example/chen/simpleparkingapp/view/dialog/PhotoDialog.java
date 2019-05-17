package com.example.chen.simpleparkingapp.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;

public class PhotoDialog extends Dialog implements View.OnClickListener {

    private TextView tvButton1;
    private TextView tvButton2;
    private String title1;
    private String title2;

    public PhotoDialog(Context context) {
        super(context, R.style.MyCustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_phone);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tvButton1 = findViewById(R.id.tvCamera);
        tvButton1.setOnClickListener(this);
        tvButton2 = findViewById(R.id.tvTakePhoto);
        tvButton2.setOnClickListener(this);
        findViewById(R.id.tvCancle).setOnClickListener(this);

        if (!TextUtils.isEmpty(title1) ) {
            tvButton1.setText(title1);
        }
        if (!TextUtils.isEmpty(title2)) {
            tvButton2.setText(title2);
        }
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCamera://拍照
                if (photoSelectorListener != null) {
                    photoSelectorListener.takePhotoFromCamera();
                    dismiss();
                }
                break;
            case R.id.tvTakePhoto://相册
                if (photoSelectorListener != null) {
                    photoSelectorListener.takePhotoFromPic();
                    dismiss();
                }
                break;
            case R.id.tvCancle:
                dismiss();
                break;
        }
    }

    public interface PhotoSelectorListener {
        void takePhotoFromCamera();

        void takePhotoFromPic();
    }

    private PhotoSelectorListener photoSelectorListener;

    public void setPhotoSelectorListener(PhotoSelectorListener photoSelectorListener) {
        this.photoSelectorListener = photoSelectorListener;
    }

    public void setTvButton1(String title) {
        this.title1 = title;
    }

    public void setTvButton2(String title) {
       this.title2 = title;
    }
}
