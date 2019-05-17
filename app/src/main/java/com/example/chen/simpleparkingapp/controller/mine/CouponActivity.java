package com.example.chen.simpleparkingapp.controller.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.CouponAdapter;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.model.Coupon;
import com.example.chen.simpleparkingapp.model.Order;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.viewmodel.CouponViewModel;
import com.example.chen.simpleparkingapp.viewmodel.MyCarViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.tasktool.TaskToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CouponActivity extends BaseActivity implements View.OnClickListener {

    private CouponViewModel presentViewModel;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<Coupon> mDatas = new ArrayList<>();
    private BaseQuickAdapter adapter;
    private int beforePager = 0;
    private View noDataView, errView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        user = UserCenter.getInstance().getUser();
        initView();
        getForgroundCouponList();
    }

    private void initView() {
        noDataView = LayoutInflater.from(this).inflate(R.layout.layout_empty_data, null);
        errView = LayoutInflater.from(this).inflate(R.layout.layout_empty_err, null);
        errView.findViewById(R.id.tvErrorView).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        tvTitle.setText("优惠券");
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CouponAdapter(mDatas, 0);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                beforePager = 0;
                getForgroundCouponList();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getForgroundCouponList();
            }
        });
    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (CouponViewModel) baseViewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tvErrorView:
                beforePager = 0;
                getForgroundCouponList();
                break;
        }
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_FORGROUNDCOUPON_LIST)) {
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh();
            dismissProgress();
            List<Coupon> tempList = presentViewModel.couponList;
            if (tempList == null || tempList.size() == 0) {
                if (beforePager == 0) {
                    adapter.setEmptyView(noDataView);
                }
                return;
            }

            Log.e("tag", tempList.size() + "");

            if (beforePager == 0) {
                mDatas.clear();
            }
            mDatas.addAll(tempList);
            adapter.notifyDataSetChanged();
            beforePager++;
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_FORGROUNDCOUPON_LIST)) {
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh();
            if (beforePager == 0) {
                adapter.setEmptyView(noDataView);
            }
        }
    }

    private void getForgroundCouponList() {
        showDialog();
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("pageNum", "" + (beforePager + 1));
        parmas.put("pageSize", "10");
        parmas.put("userId", "" + user.getId());
        doTask(AppServiceMediator.SERVICE_GET_FORGROUNDCOUPON_LIST, parmas);

    }
}
