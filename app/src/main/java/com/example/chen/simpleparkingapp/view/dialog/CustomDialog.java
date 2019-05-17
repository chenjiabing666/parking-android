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


public class CustomDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private TextView tvTitle;
    private String title;

    public CustomDialog(Context context) {
        super(context, R.style.MyCustomDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);
        initView();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnSubmit).setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
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
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.btnSubmit:
                if (listener != null) {
                    listener.onConfirmClick();
                    dismiss();
                }
                break;
        }
    }

    public interface OnConfirmClickListener {
        void onConfirmClick();
    }

    private OnConfirmClickListener listener;

    public void setListener(OnConfirmClickListener listener) {
        this.listener = listener;
    }

    public void setContent(String content) {
        this.title = content;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }
}
