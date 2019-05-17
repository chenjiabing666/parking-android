package com.example.chen.simpleparkingapp.controller.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.ParkingListAdapter;
import com.example.chen.simpleparkingapp.model.Parking;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.widget.ClearWriteEditText;
import com.example.chen.simpleparkingapp.viewmodel.ParkingViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private ParkingViewModel presentViewModel;
    private ClearWriteEditText etSearch;
    private List<Parking> mDatas = new ArrayList<>();
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private View errView, noDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        TextView tvMiddle = findViewById(R.id.tv_title_middle);
        tvMiddle.setText("搜索");
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        etSearch = findViewById(R.id.etSearch);
        findViewById(R.id.tvSearch).setOnClickListener(this);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParkingListAdapter(mDatas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchActivity.this, ParkingDetailsActivity.class);
                intent.putExtra(ParkingDetailsActivity.PARKING_ID, mDatas.get(position).getId());
                Route.route().nextControllerWithIntent(SearchActivity.this, ParkingDetailsActivity.class.getName(), Route.WITHOUT_RESULTCODE, intent);
            }
        });

        errView = LayoutInflater.from(this).inflate(R.layout.layout_empty_err, null);
        errView.findViewById(R.id.tvErrorView).setOnClickListener(this);
        noDataView = LayoutInflater.from(this).inflate(R.layout.layout_empty_data, null);
    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (ParkingViewModel) baseViewModel;
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_DO_SEARCH)) {
            dismissProgress();
            List<Parking> tempList = presentViewModel.searchList;
            mDatas.clear();
            mDatas.addAll(tempList);
            adapter.notifyDataSetChanged();
            if (tempList == null || tempList.size() == 0) {
                adapter.setEmptyView(noDataView);
            }
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
        if (token.method.equals(AppServiceMediator.SERVICE_DO_SEARCH)) {
            adapter.setEmptyView(errView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tvSearch:
                search();
                break;
            case R.id.tvErrorView:
                search();
                break;
        }
    }

    private void search() {
        String content = etSearch.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.show(this, "请输入你要搜索的内容");
            return;
        }

        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("name", content);
        doTask(AppServiceMediator.SERVICE_DO_SEARCH, params);
    }
}
