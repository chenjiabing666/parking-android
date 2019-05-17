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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.HomeCornerImageBannerAdapter;
import com.example.chen.simpleparkingapp.adapter.ParkingListAdapter;
import com.example.chen.simpleparkingapp.controller.MainActivity;
import com.example.chen.simpleparkingapp.controller.home.MessageListActivity;
import com.example.chen.simpleparkingapp.controller.home.MoreParkingActivity;
import com.example.chen.simpleparkingapp.controller.home.ParkingDetailsActivity;
import com.example.chen.simpleparkingapp.controller.home.SearchActivity;
import com.example.chen.simpleparkingapp.model.Banner;
import com.example.chen.simpleparkingapp.model.Home;
import com.example.chen.simpleparkingapp.model.Parking;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.widget.AlphaPageTransformer;
import com.example.chen.simpleparkingapp.view.widget.ScaleInTransformer;
import com.example.chen.simpleparkingapp.viewmodel.MainViewModel;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecommedFragment extends Fragment implements View.OnClickListener, MainActivity.RefreshDataListener {

    private static final int FANCY_WHAT = 1002;
    private View rootView;
    private MainActivity activity;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private List<Parking> mDatas = new ArrayList<>();
    private List<Banner> banners = new ArrayList<>();
    private ViewPager vpBanner;
    private LinearLayout llIndicator;
    private HomeCornerImageBannerAdapter bannerAdapter;
    private boolean isDragging = false;
    private View listView, errView;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FANCY_WHAT:
                    int item = vpBanner.getCurrentItem() + 1;
                    vpBanner.setCurrentItem(item);
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(FANCY_WHAT, 3000);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recommend, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        initView();
        addHeaderView();
        getHomeData();
    }

    private void addHeaderView() {
        View headerView = LayoutInflater.from(activity).inflate(R.layout.layout_home_header, null);
        vpBanner = headerView.findViewById(R.id.vpBanner);
        llIndicator = headerView.findViewById(R.id.ll_indicator);
        headerView.findViewById(R.id.tvMmore).setOnClickListener(this);
        adapter.addHeaderView(headerView);
    }

    private void initView() {
        listView =  rootView.findViewById(R.id.list);
        errView = rootView.findViewById(R.id.errorView);
        errView.setOnClickListener(this);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadmore(false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        adapter = new ParkingListAdapter(mDatas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getHomeData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(activity, ParkingDetailsActivity.class);
                intent.putExtra(ParkingDetailsActivity.PARKING_ID, mDatas.get(position).getId());
                Route.route().nextControllerWithIntent(activity, ParkingDetailsActivity.class.getName(), Route.WITHOUT_RESULTCODE,intent);
            }
        });

    }

    private void initBanner() {
        llIndicator.removeAllViews();
        bannerAdapter = new HomeCornerImageBannerAdapter(banners, activity, handler, FANCY_WHAT);
        for (int i = 0; i < banners.size(); i++) {
            ImageView imageView = new ImageView(activity);
            imageView.setImageResource(R.drawable.indicator_bg_selector);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dp2px(8), DensityUtil.dp2px(4)));
            llIndicator.addView(imageView);
        }
        vpBanner.setOffscreenPageLimit(3);
        vpBanner.setPageTransformer(true, new AlphaPageTransformer(new ScaleInTransformer()));
        vpBanner.setPageMargin(DensityUtil.dp2px(8));
        vpBanner.setAdapter(bannerAdapter);
        vpBanner.addOnPageChangeListener(new PageChangeListener());
        vpBanner.setCurrentItem(1);
        handler.sendEmptyMessageDelayed(FANCY_WHAT, 3000);
    }

    private void initDefaultIndicator() {
        for (int i = 0; i < llIndicator.getChildCount(); i++) {
            llIndicator.getChildAt(i).setSelected(false);
        }
    }

    @Override
    public void refreshData(TaskToken token, MainViewModel presentModel) {
        if (token.method.equals(AppServiceMediator.SERVICE_GET_HOME)) {
            refreshLayout.finishRefresh();
            Home home = presentModel.home;
            activity.dismissProgress();
            if (home != null) {
                errView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                List<Banner> tempBanner = home.getBanners();
                banners.clear();
                banners.addAll(tempBanner);
                initBanner();

                List<Parking> tempParking = home.getParkings();
                if (tempParking != null && tempParking.size() > 0) {
                    mDatas.clear();
                    mDatas.addAll(tempParking);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        if (token.method.equals(AppServiceMediator.SERVICE_GET_HOME)) {
            refreshLayout.finishRefresh();
            errView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    //banner的页面选择监听
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            initDefaultIndicator();
            if (banners.size() > 0) {
                llIndicator.getChildAt(position % banners.size()).setSelected(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == vpBanner.SCROLL_STATE_DRAGGING) {//拖拽状态
                isDragging = true;
            } else if (state == vpBanner.SCROLL_STATE_SETTLING) {//滑动状态

            } else if (state == vpBanner.SCROLL_STATE_IDLE && isDragging) {//空闲状态
                isDragging = false;
                handler.removeMessages(FANCY_WHAT);
                handler.sendEmptyMessageDelayed(FANCY_WHAT, 2000);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMmore:
                Route.route().nextController(activity, MoreParkingActivity.class.getName(), Route.WITHOUT_RESULTCODE);
                break;
            case R.id.errorView:
                getHomeData();
                break;
        }
    }

    private void getHomeData() {
        activity.showLodingDialog();
        HashMap<String, String> params = new HashMap<>();
        activity.doTask(AppServiceMediator.SERVICE_GET_HOME, params);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
