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
import com.example.chen.simpleparkingapp.adapter.CouponAdapter;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.model.Coupon;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.viewmodel.CouponViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.tasktool.TaskToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetCouponByOrderIdActivity extends BaseActivity implements View.OnClickListener {

    public static final String COUPON_ID = "couponId";
    private CouponViewModel presentViewModel;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private List<Coupon> mDatas = new ArrayList<>();
    private User user;
    private int orderId;
    private View noDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_by_order_id);
        user = UserCenter.getInstance().getUser();
        orderId = getIntent().getIntExtra(OrderDetailActivity.ORDER_ID, 0);
        initView();
        getCouponByOrderId();
    }


    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (CouponViewModel) baseViewModel;
    }

    private void initView() {
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        tvTitle.setText("优惠券");
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadmore(false);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CouponAdapter(mDatas, 1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noDataView = LayoutInflater.from(this).inflate(R.layout.layout_empty_data, null);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tvUserNow) {
                    Intent intent = new Intent();
                    intent.putExtra(GetCouponByOrderIdActivity.COUPON_ID, mDatas.get(position).getId());
                    setResult(RESULT_OK, intent);
                    finish();
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
        }
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_COUPON_BY_ORDERID)) {
            List<Coupon> tempList = presentViewModel.couponList;
            refreshLayout.finishRefresh();
            dismissProgress();
            if (tempList == null || tempList.size() == 0) {
                adapter.setEmptyView(noDataView);
                return;
            }
            mDatas.addAll(tempList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);

    }

    private void getCouponByOrderId() {
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", "" + orderId);
        params.put("userId", "" + user.getId());
        doTask(AppServiceMediator.SERVICE_GET_COUPON_BY_ORDERID, params);
    }
}
