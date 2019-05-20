package com.example.chen.simpleparkingapp.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.NearbyAdapter;
import com.example.chen.simpleparkingapp.controller.MainActivity;
import com.example.chen.simpleparkingapp.controller.home.ParkingDetailsActivity;
import com.example.chen.simpleparkingapp.model.Parking;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.utils.LocationUtils;
import com.example.chen.simpleparkingapp.viewmodel.MainViewModel;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;

public class NearbyFragment extends Fragment implements MainActivity.RefreshDataListener, View.OnClickListener {

    private MainActivity activity;
    private View rootView;
    private double longitude;//经度
    private double latitude;//纬度
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private List<Parking> mDatas = new ArrayList<>();
    private View noDataView, errView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.common_recycler_view, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        initView();
        getNearbyParkingData();
    }

    private void initView() {
        noDataView = LayoutInflater.from(activity).inflate(R.layout.layout_empty_data, null);
        errView = LayoutInflater.from(activity).inflate(R.layout.layout_empty_err, null);
        errView.findViewById(R.id.tvErrorView).setOnClickListener(this);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        adapter = new NearbyAdapter(mDatas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getNearbyParkingData();
            }
        });
        refreshLayout.setEnableLoadmore(false);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(activity, ParkingDetailsActivity.class);
                intent.putExtra(ParkingDetailsActivity.PARKING_ID, mDatas.get(position).getId());
                Route.route().nextControllerWithIntent(activity, ParkingDetailsActivity.class.getName(), Route.WITHOUT_RESULTCODE, intent);
            }
        });
    }

    private void getNearbyParkingData() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                RxPermissions.getInstance(activity).request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            getLocation();

                        } else {
                            ToastUtils.show(activity, "请在权限中心打开【您的位置】的权限");
                        }
                    }
                });
            } else {
                getLocation();
            }
        } else {
            getLocation();
        }
    }

    //获取经纬度
    private void getLocation() {
        Location location = LocationUtils.getInstance(activity).showLocation();
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            getNearbyParking();
        }
    }

    //获取附近停车场
    private void getNearbyParking() {
        HashMap<String, String> params = new HashMap<>();
        params.put("longitude", "" + longitude);
        params.put("latitude", "" + latitude);
        activity.doTask(AppServiceMediator.SERVICE_GET_NEAR_BY_PARKING, params);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void refreshData(TaskToken token, MainViewModel presentModel) {
        if (token.method.equals(AppServiceMediator.SERVICE_GET_NEAR_BY_PARKING)) {
            List<Parking> tempList = presentModel.nearList;
            refreshLayout.finishRefresh();
            activity.dismissProgress();
            if (tempList == null || tempList.size() == 0) {
                adapter.setEmptyView(noDataView);
                return;
            }
            mDatas.clear();
            mDatas.addAll(tempList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        if (token.method.equals(AppServiceMediator.SERVICE_GET_NEAR_BY_PARKING)) {
            refreshLayout.finishRefresh();
            adapter.setEmptyView(noDataView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvErrorView:
                getNearbyParkingData();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        LocationUtils.getInstance(activity).removeLocationUpdatesListener();
        super.onDestroyView();
    }
}
