package com.example.chen.simpleparkingapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.OrdersAdapter;
import com.example.chen.simpleparkingapp.base.CommonConstant;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.controller.mine.MyOrderActivity;
import com.example.chen.simpleparkingapp.controller.mine.OrderDetailActivity;
import com.example.chen.simpleparkingapp.model.Code;
import com.example.chen.simpleparkingapp.model.Order;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.dialog.CodeDialog;
import com.example.chen.simpleparkingapp.viewmodel.OrderViewModel;
import com.example.chen.taco.mvvm.BaseFragmentActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的订单的页面
 */
public class OrderFragment extends Fragment implements MyOrderActivity.RefreshListener, View.OnClickListener, CodeDialog.OnRefreshClickListener {

    public static final int ORDER = 0x122;//订单
    private View rootView;
    private int type;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private List<Order> mDatas = new ArrayList<>();
    private MyOrderActivity activity;
    private int beforePager = 0;

    private View noDataView;
    private User user;
    private View errView;
    private CodeDialog codeDialog;
    private int codePosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, null);
        Bundle bundle = getArguments();
        user = UserCenter.getInstance().getUser();
        if (bundle != null) {
            type = bundle.getInt("type");
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MyOrderActivity) getActivity();
        codeDialog = new CodeDialog(activity);
        codeDialog.setListener(this);
        initView();

    }

    private void initView() {
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        adapter = new OrdersAdapter(mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
        noDataView = LayoutInflater.from(activity).inflate(R.layout.layout_empty_data, null);
        errView = LayoutInflater.from(activity).inflate(R.layout.layout_empty_err, null);
        errView.findViewById(R.id.tvErrorView).setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getOrderListForeground(activity, 0, type);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getOrderListForeground(activity, beforePager, type);
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

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Integer status = mDatas.get(position).getStatus();
                Intent intent = new Intent(activity, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_ID, mDatas.get(position).getId());
                intent.putExtra(OrderDetailActivity.TITLE_TYPE, type);
                intent.putExtra(OrderDetailActivity.POSITION, position);
                intent.putExtra("type", status);
                Route.route().nextControllerWithIntent(activity, OrderDetailActivity.class.getName(), ORDER, intent);
            }
        });
    }

    //获取订单列表
    public void getOrderListForeground(BaseFragmentActivity activity, int beforePager, int state) {
        activity.showLodingDialog();
        if (user == null) {
            user = UserCenter.getInstance().getUser();
        }
        this.beforePager = beforePager;
        HashMap<String, String> parms = new HashMap<>();
        parms.put("pageNum", "" + (beforePager + 1));
        parms.put("pageSize", "10");
        parms.put("userId", "" + user.getId());
        parms.put("status", "" + state);
        activity.doTask(AppServiceMediator.SERVICE_GET_ORDERLIST_FOREGROUND, parms, state + "");
    }

    @Override
    public void refreshData(TaskToken token, OrderViewModel presentViewModel) {
        if (token.method.equals(AppServiceMediator.SERVICE_GET_ORDERLIST_FOREGROUND)) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
            List<Order> tempList = presentViewModel.oderList;
            if (beforePager == 0) {
                mDatas.clear();
            }
            if (tempList == null || tempList.size() == 0) {
                if (beforePager == 0) {
                    adapter.setEmptyView(noDataView);
                    adapter.notifyDataSetChanged();
                }
                return;
            }

            if (beforePager == 0) {
                mDatas.clear();
            }
            beforePager++;
            mDatas.addAll(tempList);
            adapter.notifyDataSetChanged();
        } else if (token.method.equals(AppServiceMediator.SERVICE_REFRESH_CODE)) {
            Code code = presentViewModel.code;
            if (code != null) {
                ToastUtils.show(activity, "刷新成功");
                codeDialog.upCodeUrl(code.getCodeUrl());
                mDatas.get(codePosition).setCodeUrl(code.getCodeUrl());
            }
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        if (token.method.equals(AppServiceMediator.SERVICE_GET_ORDERLIST_FOREGROUND)) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
            if (beforePager == 0) {
                adapter.setEmptyView(errView);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ORDER:
                    int position = data.getIntExtra(OrderDetailActivity.POSITION, -1);
                    if (position >= 0 && position < mDatas.size()) {
                        adapter.remove(position);
                        if (mDatas.size() == 0) {
                            adapter.setEmptyView(noDataView);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvErrorView:
                getOrderListForeground(activity, 0, type);
                break;
        }
    }

    @Override
    public void onRefreshCode() {
        refreshCode();
    }

    private void refreshCode() {
        activity.showLodingDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "" + user.getId());
        params.put("parkingId", "" + mDatas.get(codePosition).getParkingId());
        params.put("type", CommonConstant.CODE_TYPE_ORDER);
        params.put("orderId", "" + mDatas.get(codePosition).getId());
        activity.doTask(AppServiceMediator.SERVICE_REFRESH_CODE, params, type + "");
    }
}
