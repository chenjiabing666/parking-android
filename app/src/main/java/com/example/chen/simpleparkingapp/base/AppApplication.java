package com.example.chen.simpleparkingapp.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


import com.example.chen.simpleparkingapp.R;
import com.example.chen.taco.mvvm.ViewModelManager;
import com.example.chen.taco.utils.ENVConfig;

import java.util.LinkedList;
import java.util.List;

public class AppApplication extends Application {

    private static AppApplication application;
    private static Context mContext;
    private ENVConfig envConfig;
    public List<Activity> activityList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();

        application=this;
        mContext = getApplicationContext();
        initConfig();
        initMVVM();
    }

    /**
     * 初始化本地配置类
     */
    public void initConfig() {
        envConfig = new ENVConfig();
        ENVConfig.configurationEnvironment(mContext);
        ENVConfig.setEnvironmentConfig(R.raw.environment_config);
        if (UserCenter.getInstance().getNotify() == null) {
            UserCenter.getInstance().setNotify(true);
        }
    }

    /**
     * 初始化框架
     */
    public void initMVVM() {
        ViewModelManager.manager().addViewModelPlist(ViewModelPlist.hashMap);
    }

    /**
     * 添加Activity到容器中
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void clearActivitys() {
        for (int i = 0; i < activityList.size(); i++) {
            if (!activityList.get(i).isDestroyed()) {
                activityList.get(i).finish();
            }
        }
        activityList.clear();
    }

    public static AppApplication getInstance (){
        return application;
    }
}
