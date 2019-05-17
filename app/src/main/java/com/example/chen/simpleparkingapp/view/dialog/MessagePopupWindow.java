package com.example.chen.simpleparkingapp.view.dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.chen.simpleparkingapp.R;


public class MessagePopupWindow extends PopupWindow {

    private Activity context;
    private final View rootView;

    public MessagePopupWindow(Activity context) {
        super(context);
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.popup_window_message, null);
        setContentView(rootView);

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.SelectPicPopupAnimation);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colortransparent)));

        rootView.findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDelete();
                }
            }
        });
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        WindowManager.LayoutParams lp2 = context.getWindow().getAttributes();
//        lp2.alpha = 0.5f; //0.0-1.0
        context.getWindow().setAttributes(lp2);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1.0f; // 0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    public interface OnDeleteClickListener {
        void onDelete();
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }
}
