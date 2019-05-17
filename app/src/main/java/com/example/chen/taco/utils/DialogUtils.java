package com.example.chen.taco.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;


public class DialogUtils {

    public static Dialog createWeiBoDialog(Context context, String text) {

        View view = View.inflate(context, R.layout.dialog_weibo, null);
        ProgressBar weiboProgress = (ProgressBar) view.findViewById(R.id.weibo_progress);
        TextView tipTextView = (TextView) view.findViewById(R.id.tipTextView);
        tipTextView.setText(text);

        Dialog dialog = new Dialog(context,R.style.MyDialogStyle);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopupWindowAnimStyle);
        dialog.show();
        return dialog;
    }

    public static void closeDialog(Dialog dialog){
        if (dialog!=null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
