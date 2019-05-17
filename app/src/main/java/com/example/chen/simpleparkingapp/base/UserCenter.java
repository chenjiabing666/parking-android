package com.example.chen.simpleparkingapp.base;

import android.content.SharedPreferences;

import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.taco.utils.ENVConfig;
import com.example.chen.taco.utils.LogUtil;
import com.example.chen.taco.utils.PersistenceUtil;

public class UserCenter {

    private static UserCenter userCenter;
    private static final String PUSH_NOTIFICATION = "PUSH_NOTIFICATION";
    private Boolean receiveNotify = true;
    private User user = null;
    private static final String APP_USER = "APP_USER";

    public static UserCenter getInstance() {
        if (userCenter == null) {
            synchronized (UserCenter.class) {
                if (userCenter == null) {
                    userCenter = new UserCenter();
                }
            }
        }
        return userCenter;
    }

    public Boolean getNotify() {
        receiveNotify = PersistenceUtil.getObjectFromSharePreferences(PUSH_NOTIFICATION,
                Boolean.class);
        return receiveNotify;
    }

    public void setNotify(boolean flag) {
        this.receiveNotify = flag;
        PersistenceUtil.saveObjectToSharePreferences(receiveNotify, PUSH_NOTIFICATION);

    }

    public User getUser() {
        if (user == null) {
            Object object = PersistenceUtil.getObjectFromSharePreferences(APP_USER,
                    User.class);
            if (object instanceof String) {
                String objString = (String) object;
                if (objString.equals("")) {
                    LogUtil.d("userCenter", "获取用户信息为空！");
                } else {
                    LogUtil.e("userCenter", "获取的用户信息为空！且有错误！空信息为：" + objString);
                }
                user = null;
            } else if (object instanceof User) {
                user = (User) object;
            }
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (null != user) {
            PersistenceUtil.saveObjectToSharePreferences(user, APP_USER);
        } else { // 删除user信息
            SharedPreferences.Editor editor = ENVConfig.sp.edit();
            editor.remove(APP_USER);
            editor.commit();
        }
    }

}
