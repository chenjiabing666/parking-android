package com.example.chen.simpleparkingapp.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.utils.CheckUtils;
import com.example.chen.taco.utils.ToastUtils;


public class CustomEditDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private TextView tvTitle;
    private String title;
    private EditText etEdit;
    private String content;

    public CustomEditDialog(Context context) {
        super(context, R.style.MyCustomDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_custom);
        initView();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnSubmit).setOnClickListener(this);
        etEdit = findViewById(R.id.etEdit);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            etEdit.setText(content);
            etEdit.setSelection(content.length());
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
                    String content = etEdit.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        if (CheckUtils.getInstances().isCarnumberNO(content)) {
                            listener.onConfirmClick(content);
                            dismiss();
                        } else {
                            ToastUtils.show(context, "请输入正确的车牌号");
                        }
                    } else {
                        ToastUtils.show(context, "请输入内容");
                    }
                }
                break;
        }
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(String content);
    }

    private OnConfirmClickListener listener;

    public void setListener(OnConfirmClickListener listener) {
        this.listener = listener;
    }

    public void setContent(String title, String content) {
        this.title = title;
        this.content = content;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }
}
