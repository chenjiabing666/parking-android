package com.example.chen.taco.mvvm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.tasktool.Taskpool;
import com.example.chen.taco.utils.CommonUtil;
import com.example.chen.taco.utils.DialogUtils;
import com.example.chen.taco.utils.LogUtil;
import com.example.chen.taco.utils.RomUtils;
import com.example.chen.taco.utils.ToastUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class BaseFragmentActivity extends FragmentActivity implements Controller {
    public ViewModel baseViewModel;
    private List<TaskToken> requestTokens;
    private String viewModelId;
    public Dialog progressDialog;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            Log.i("TAG", "onCreate fixOrientation when Oreo, result = " + result);
        }
        super.onCreate(savedInstanceState);
        RomUtils.setActionBar(this, true);
        requestTokens = new ArrayList<TaskToken>();
        Intent intent = getIntent();
        if (null != intent.getExtras()) {
            viewModelId = intent.getExtras()
                    .getString(Route.ACTIVITY_TOKEN_KEY);
        }
        if (null == baseViewModel) {
            baseViewModel = ViewModelManager.manager().viewModelForKey(
                    viewModelId);
            if (null != baseViewModel) {
                baseViewModel.setActivity(this);
                alreadyBindBaseViewModel();
            }
        } else {

            baseViewModel.setActivity(this);
            alreadyBindBaseViewModel();
        }
    }

    private boolean isTranslucentOrFloating(){
        boolean isTranslucentOrFloating = false;
        try {
            int [] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean)m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private boolean fixOrientation(){
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo)field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            Log.i("TAG", "avoid calling setRequestedOrientation when Oreo.");
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null == baseViewModel) {
            baseViewModel = ViewModelManager.manager().viewModelForKey(
                    viewModelId);
            if (null != baseViewModel) {
                baseViewModel.setActivity(this);
                alreadyBindBaseViewModel();
                if (baseViewModel.getResponse() != null) {
                    if (baseViewModel.isDataReady.get()
                            && !baseViewModel.isRefreshed.get()) {
                        refreshData(baseViewModel.getTaskToken());
                    } else if (baseViewModel.isFailed.get()) {
                        LogUtil.e("whx", "ON START");
                        baseViewModel
                                .requestFailed(baseViewModel.getResponse());
                    } else {
                        baseViewModel.requestFailed(baseViewModel.getResponse());
                    }
                } else {
                    LogUtil.e("whx", "baseViewModel.getResponse() == null");
                }
            } else {
                LogUtil.e("whx", "baseViewModel is null");
            }
        } else {
            LogUtil.e("whx", "baseViewModel is NOT null");
        }
    }

    @Override
    public void onBackPressed() {
        cancelToast();
        super.onBackPressed();
        destoryViewModel();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (baseViewModel != null) {
            baseViewModel.isRefreshed.set(false);
        }
    }

    /**
     * activity已经绑定了viewmodel，可以直接调用baseviewmodel获取viewmodel
     */
    public void alreadyBindBaseViewModel() {
        if (baseViewModel.getTaskToken() != null) {
            requestTokens.add(baseViewModel.getTaskToken());
        }
    }

    @Override
    public void refreshData(TaskToken token) {
        baseViewModel.isRefreshed.set(true);
    }


    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        ToastUtils.show(this, errorMsg);
        dismissProgress();
    }

    /**
     * 取消一个网络请求,需要传入dotask时候返回的唯一令牌
     */
    public void cancelTask(TaskToken token) {
        requestTokens.remove(token);
        Taskpool.sharedInstance().cancelTask(token);
    }


    /**
     * 关闭activity监听
     */
    protected View.OnClickListener defaultLeftClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CommonUtil.KeyBoardCancel(BaseFragmentActivity.this);
            onBackPressed();
        }
    };

    public void destoryViewModel() {
        for (int i = 0; i < requestTokens.size(); i++) {
            TaskToken token = requestTokens.get(i);
            Taskpool.sharedInstance().cancelTask(token);
        }
        ViewModelManager.manager().destoryViewModel(viewModelId);
    }

    @Override
    public TaskToken doTask(String method, HashMap<String, ?> parameters) {
        return doTask(method,parameters,null);
    }

    @Override
    public TaskToken doTask(String method, HashMap<String, ?> parameters, String flag) {
        if (baseViewModel == null) {
            try {
                throw new IllegalArgumentException("baseViewModel is null , when doTask（）!");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        TaskToken token = Taskpool.sharedInstance().doTask(baseViewModel, method, parameters, flag);
        if (null != token) {
            requestTokens.add(token);
        }
        return token;
    }

    public String getViewModelId() {
        return viewModelId;
    }

    @Override
    public ViewModel getViewModel() {
        return baseViewModel;
    }

    @Override
    public void setViewModel(ViewModel viewModel) {
        baseViewModel = viewModel;
    }

    //public abstract void startProgressDialog(boolean cancle);

    /**
     * 加载progressView
     */
    public void showProgress() {
        //startProgressDialog(false);
    }

    /**
     * 取消ProgressView
     *
     * @param
     */
    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     *
     */
    public void showLodingDialog() {
        if (progressDialog == null) {
            progressDialog = DialogUtils.createWeiBoDialog(this, getString(com.chad.library.R.string.loading));
        } else if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void showToast(String text) {
        this.showToast(text, Toast.LENGTH_SHORT);
    }

    public void showToast(String text, int duration) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, text, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
