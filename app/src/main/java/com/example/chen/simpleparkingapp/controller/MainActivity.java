package com.example.chen.simpleparkingapp.controller;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.ActivityManager;
import com.example.chen.simpleparkingapp.fragment.HomeFragment;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.viewmodel.MainViewModel;
import com.example.chen.taco.mvvm.BaseFragmentActivity;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

public class MainActivity extends BaseFragmentActivity {

    private MainViewModel presentViewModel;
    private RadioGroup radioGroup;
    private RadioButton homePageRb, mineRb;
    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.addActivity(this);
        initView();
        initFragment();
        RxPermissions.getInstance(this).request(permissions).subscribe();

    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (MainViewModel) baseViewModel;
    }


    private void initView() {
        radioGroup = findViewById(R.id.main_page_rg);
        homePageRb = findViewById(R.id.tab_homepage_rb);
        mineRb = findViewById(R.id.tab_mine_rb);
    }

    private void initFragment() {
        mFragments = new Fragment[4];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_home);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_mine);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]);
                switch (checkedId) {
                    case R.id.tab_homepage_rb:
                        homePageRb.setChecked(true);
                        fragmentTransaction.show(mFragments[0]).commit();
                        break;
                    case R.id.tab_mine_rb:
                        mineRb.setChecked(true);
                        fragmentTransaction.show(mFragments[1]).commit();
                        break;
                }
            }
        });

        radioGroup.check(R.id.tab_homepage_rb);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_HOME)) {
            ((HomeFragment) mFragments[0]).refreshData(token, presentViewModel);
        } else if (token.method.equals(AppServiceMediator.SERVICE_GET_NEAR_BY_PARKING)) {
            ((HomeFragment) mFragments[0]).refreshData(token, presentViewModel);
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_HOME)) {
            dismissProgress();
            ((HomeFragment) mFragments[0]).requestFailedHandle(token, errorCode, errorMsg);
        } else if (token.method.equals(AppServiceMediator.SERVICE_GET_NEAR_BY_PARKING)) {
            ((HomeFragment) mFragments[0]).requestFailedHandle(token, errorCode, errorMsg);
        }
    }

    public interface RefreshDataListener {
        void refreshData(TaskToken token, MainViewModel presentModel);

        void requestFailedHandle(TaskToken token, int errorCode, String errorMsg);

    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtils.show(this, "再按一次退出");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
