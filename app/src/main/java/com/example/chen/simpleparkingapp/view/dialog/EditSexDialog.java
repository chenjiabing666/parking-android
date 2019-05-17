package com.example.chen.simpleparkingapp.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.CommonConstant;


//修改性别的对话框
public class EditSexDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private int mSex = 0;
    private int changeSex;
    private RadioGroup rgGender;

    public EditSexDialog(Context context) {
        super(context, R.style.MyCustomDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_sex);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        findViewById(R.id.dialog_cancel_btn).setOnClickListener(this);
        findViewById(R.id.dialog_confirm_btn).setOnClickListener(this);
        rgGender = findViewById(R.id.rg_gender);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male://男
                        changeSex = CommonConstant.MAN;
                        break;
                    case R.id.rb_female://女
                        changeSex = CommonConstant.FEMALE;
                        break;
                }
            }
        });
        if (mSex == CommonConstant.MAN) {
            rgGender.check(R.id.rb_male);
        } else {
            rgGender.check(R.id.rb_female);
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
            case R.id.dialog_cancel_btn:
                dismiss();
                break;
            case R.id.dialog_confirm_btn:
                if (mListener != null) {
                    mListener.onSexChange(changeSex != mSex, changeSex);
                    dismiss();
                }
                break;
        }
    }

    public void setSex(int sex) {
        mSex = sex;
        if (rgGender != null) {
            if (mSex == CommonConstant.MAN) {
                rgGender.check(R.id.rb_male);
            } else {
                rgGender.check(R.id.rb_female);
            }
        }
    }

    public interface OnSexChangedListner {
        void onSexChange(boolean isChange, int sex);
    }

    private OnSexChangedListner mListener;

    public void setOnSexChangedListener(OnSexChangedListner mListener) {
        this.mListener = mListener;
    }
}
