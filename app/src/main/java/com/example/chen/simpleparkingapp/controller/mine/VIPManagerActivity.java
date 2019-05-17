package com.example.chen.simpleparkingapp.controller.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.VIPManagerAdapter;
import com.example.chen.simpleparkingapp.base.CommonConstant;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.controller.home.ParkingDetailsActivity;
import com.example.chen.simpleparkingapp.model.Code;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.model.YearVipVos;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.dialog.CodeDialog;
import com.example.chen.simpleparkingapp.viewmodel.VIPManagerViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VIPManagerActivity extends BaseActivity implements View.OnClickListener, CodeDialog.OnRefreshClickListener {

    private VIPManagerViewModel presentViewModel;
    private BaseQuickAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private View noDataView, errView;
    private List<YearVipVos> mDatas = new ArrayList<>();
    private User user;
    private CodeDialog codeDialog;
    private int codePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_manager);
        user = UserCenter.getInstance().getUser();
        codeDialog = new CodeDialog(this);
        codeDialog.setListener(this);
        initView();
        getYearVipList();
    }


    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (VIPManagerViewModel) baseViewModel;

    }

    private void initView() {
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        tvTitle.setText("会员管理");
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VIPManagerAdapter(mDatas);
        recyclerView.setAdapter(adapter);
        noDataView = LayoutInflater.from(this).inflate(R.layout.layout_empty_data, null);
        errView = LayoutInflater.from(this).inflate(R.layout.layout_empty_err, null);
        errView.findViewById(R.id.tvErrorView).setOnClickListener(this);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(VIPManagerActivity.this, ParkingDetailsActivity.class);
                intent.putExtra(ParkingDetailsActivity.PARKING_ID, mDatas.get(position).getParkingId());
                Route.route().nextControllerWithIntent(VIPManagerActivity.this, ParkingDetailsActivity.class.getName(), Route.WITHOUT_RESULTCODE, intent);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tvDo) {
                    if (!codeDialog.isShowing()) {
                        codePosition = position;
                        codeDialog.upCodeUrl(mDatas.get(position).getCodeUrl());
                        codeDialog.show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tvErrorView:
                getYearVipList();
                break;
        }
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_YEAR_VIP_LIST)) {//年费会员
            dismissProgress();
            List<YearVipVos> tempList = presentViewModel.yearVipVosList;
            if (tempList == null || tempList.size() == 0) {
                adapter.setEmptyView(noDataView);
                return;
            }
            mDatas.clear();
            mDatas.addAll(tempList);
            adapter.notifyDataSetChanged();
        } else if (token.method.equals(AppServiceMediator.SERVICE_REFRESH_CODE)) {
            Code code = presentViewModel.code;
            dismissProgress();
            if (code != null) {
                ToastUtils.show(this, "刷新成功");
                codeDialog.upCodeUrl(code.getCodeUrl());
                mDatas.get(codePosition).setCodeUrl(code.getCodeUrl());
            }
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_YEAR_VIP_LIST)) {//年费会员
            adapter.setEmptyView(errView);
        }
    }

    private void getYearVipList() {
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "" + user.getId());
        doTask(AppServiceMediator.SERVICE_GET_YEAR_VIP_LIST, params);
    }

    @Override
    public void onRefreshCode() {
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "" + user.getId());
        params.put("parkingId", "" + mDatas.get(codePosition).getParkingId());
        params.put("type", CommonConstant.CODE_TYPE_VIP);
        params.put("orderId", ""+mDatas.get(codePosition).getId());
        doTask(AppServiceMediator.SERVICE_REFRESH_CODE, params);
    }
}
