package com.example.chen.simpleparkingapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.HomeCornerImageBannerAdapter;
import com.example.chen.simpleparkingapp.adapter.ParkingListAdapter;
import com.example.chen.simpleparkingapp.adapter.TabFragmentAdapter;
import com.example.chen.simpleparkingapp.controller.MainActivity;
import com.example.chen.simpleparkingapp.controller.home.MessageListActivity;
import com.example.chen.simpleparkingapp.controller.home.MoreParkingActivity;
import com.example.chen.simpleparkingapp.controller.home.ParkingDetailsActivity;
import com.example.chen.simpleparkingapp.controller.home.SearchActivity;
import com.example.chen.simpleparkingapp.controller.mine.MyOrderActivity;
import com.example.chen.simpleparkingapp.model.Banner;
import com.example.chen.simpleparkingapp.model.Home;
import com.example.chen.simpleparkingapp.model.Parking;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.widget.AlphaPageTransformer;
import com.example.chen.simpleparkingapp.view.widget.ScaleInTransformer;
import com.example.chen.simpleparkingapp.viewmodel.MainViewModel;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.SystemUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener, MainActivity.RefreshDataListener {

    private View rootView;
    private MainActivity activity;
    private ViewPager viewPager;
    private TextView tab1, tab2;
    private List<Fragment> mBaseFragment = new ArrayList<>();
    private int pos;
    private View sliding_thumb;
    private int beforeIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        pos = SystemUtil.getScreenWidth(activity) / 2;
        initView();
    }

    private void initView() {
        mBaseFragment.add(new RecommedFragment());
        mBaseFragment.add(new NearbyFragment());
        tab1 = rootView.findViewById(R.id.tab1);
        tab2 = rootView.findViewById(R.id.tab2);
        sliding_thumb = rootView.findViewById(R.id.sliding_thumb);
        viewPager = rootView.findViewById(R.id.viewPager);
        rootView.findViewById(R.id.iv_message).setOnClickListener(this);
        rootView.findViewById(R.id.tvSearch).setOnClickListener(this);
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(activity.getSupportFragmentManager(), mBaseFragment);
        viewPager.setAdapter(tabFragmentAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        onPageChangeListener.onPageSelected(0);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            translateAnimation(sliding_thumb, pos * beforeIndex, pos * i, 300);
            initTabSelector(i);
            beforeIndex = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void initTabSelector(int i) {
        tab1.setSelected(false);
        tab2.setSelected(false);

        if (i == 0) {
            tab1.setSelected(true);
        } else if (i == 1) {
            tab2.setSelected(true);
        }
    }

    //位移动画
    private void translateAnimation(View view, float fromX, float toX, int duration) {
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(duration);
        view.startAnimation(translateAnimation);
    }

    @Override
    public void refreshData(TaskToken token, MainViewModel presentModel) {
        if (token.method.equals(AppServiceMediator.SERVICE_GET_HOME)) {
            ((RecommedFragment) mBaseFragment.get(0)).refreshData(token, presentModel);
        }else  if (token.method.equals(AppServiceMediator.SERVICE_GET_NEAR_BY_PARKING)) {
            ((NearbyFragment) mBaseFragment.get(1)).refreshData(token, presentModel);
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        if (token.method.equals(AppServiceMediator.SERVICE_GET_HOME)) {
            ((RecommedFragment) mBaseFragment.get(0)).requestFailedHandle(token, errorCode, errorMsg);
        }else  if (token.method.equals(AppServiceMediator.SERVICE_GET_NEAR_BY_PARKING)) {
            ((NearbyFragment) mBaseFragment.get(1)).requestFailedHandle(token, errorCode, errorMsg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_message:
                Route.route().nextController(activity, MessageListActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
            case R.id.tvSearch:
                Route.route().nextController(activity, SearchActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
            case R.id.tab1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab2:
                viewPager.setCurrentItem(1);
                break;
        }
    }

}
