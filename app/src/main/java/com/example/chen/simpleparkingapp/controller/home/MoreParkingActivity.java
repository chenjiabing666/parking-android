package com.example.chen.simpleparkingapp.controller.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.ParkingListAdapter;
import com.example.chen.simpleparkingapp.model.Parking;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.viewmodel.ParkingViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoreParkingActivity extends BaseActivity implements View.OnClickListener {


    private ParkingViewModel presentViewModel;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<Parking> mDatas = new ArrayList<>();
    private BaseQuickAdapter adapter;
    private int beforePager = 0;
    private View noDataView, errView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_parking);
        initView();
        getForegroundParkingList();
    }


    private void initView() {
        noDataView = LayoutInflater.from(this).inflate(R.layout.layout_empty_data, null);
        errView = LayoutInflater.from(this).inflate(R.layout.layout_empty_err, null);
        errView.findViewById(R.id.tvErrorView).setOnClickListener(this);
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        tvTitle.setText("推荐停车场");
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new ParkingListAdapter(mDatas);
        recyclerView.setAdapter(adapter);

        //上拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                beforePager = 0;
                getForegroundParkingList();
            }
        });

        //下拉加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getForegroundParkingList();
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MoreParkingActivity.this, ParkingDetailsActivity.class);
                intent.putExtra(ParkingDetailsActivity.PARKING_ID, mDatas.get(position).getId());
                Route.route().nextControllerWithIntent(MoreParkingActivity.this, ParkingDetailsActivity.class.getName(), Route.WITHOUT_RESULTCODE, intent);
            }
        });

    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (ParkingViewModel) baseViewModel;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tvErrorView:
                beforePager = 0;
                getForegroundParkingList();
                break;
        }
    }

    private void getForegroundParkingList() {
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNum", (beforePager + 1) + "");
        params.put("pageSize", "10");
        doTask(AppServiceMediator.SERVICE_GET_PARKING_LIST, params);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_PARKING_LIST)) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
            dismissProgress();
            List<Parking> tempList = presentViewModel.parkingList;
            if (tempList == null || tempList.size() == 0) {
                if (beforePager == 0) {

                }
            }
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
        if (token.method.equals(AppServiceMediator.SERVICE_GET_PARKING_LIST)) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
            if (beforePager == 0) {
                adapter.setEmptyView(errView);
            }
        }
    }
}
