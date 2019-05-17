package com.example.chen.taco.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018-01-13.
 */

public class UIUtils {
    public static void showToast(Context context, String msg){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 指定位置和时间显示Toast
     * @param context
     * @param msg
     * @param position  //位置 例： Gravity.CENTER
     * @param dur       //时间
     */
    public static void showToastWithView(Context context, String msg, int position, int dur){
        Toast toast = Toast.makeText(context,msg,dur);
        toast.setGravity(position,0,0);
        toast.show();
    }


    public static ProgressDialog showLoadingWithOutOverTime(Context context, String message, boolean canBeCancel){
        ProgressDialog progressDialog = ProgressDialog.show(context, null, message, false, canBeCancel);
        progressDialog.setCancelable(canBeCancel);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条

        return progressDialog;
    }

    /**
     * 显示loading，20秒超时
     * @param context
     * @param message
     * @param
     * @return
     */
    public static ProgressDialog showLoading(Context context, String message, boolean candis) {
        final int OVER_TIME = 20;
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, message, false, candis);
        progressDialog.setCancelable(candis);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(OVER_TIME*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
        return progressDialog;
    }


    public static ProgressDialog showLoading(final Context context, String message) {
         return  showLoading(context,message,true);
    }

    public static void createLoading(ProgressDialog dialog, Context context, String message){
        if(dialog == null || !dialog.isShowing()){
            dialog= UIUtils.showLoading(context,message,false);
        }
    }

    public static void cancelLoading(ProgressDialog dialog){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public static void alert(Activity activity, String title, String msg, final alertCallBack callBack){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callBack.yes(dialogInterface,i);
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callBack.no(dialogInterface,i);
            }
        });
        builder.create().show();
    }

    public static void popuCenter(final Activity activity, int res, String method, Class cla){
        PopupWindow window = null;

        if(window == null || !window.isShowing()){
            View popupView = activity.getLayoutInflater().inflate(res, null);
            window = new PopupWindow(popupView);
            //通过反射来执行findViedwbyId
            Method method1= null;
            try {
                method1 = cla.getMethod(method,Activity.class,View.class,PopupWindow.class);
                method1.invoke(null,activity,popupView,window);
            } catch (Exception e) {
                e.printStackTrace();
            }

            window.setWidth((int) SystemUtil.DpTopixel(activity,250F));
            window.setHeight((int) SystemUtil.DpTopixel(activity,350F));
            window.setFocusable(true);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setOutsideTouchable(true);
            window.update();
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setBackgorund(1f,activity);
                }
            });
            //居中显示
            window.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER,0,0);
            setBackgorund(0.4f,activity);
        }
    }


    public static void setBackgorund(float f,Activity activity){
        WindowManager.LayoutParams lp =activity.getWindow().getAttributes();
        lp.alpha = f;
        activity.getWindow().setAttributes(lp);
    }

    public interface alertCallBack{
         void yes(DialogInterface dialogInterface, int i);
        void no(DialogInterface dialogInterface, int i);
    }
}
