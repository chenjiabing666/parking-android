package com.example.chen.simpleparkingapp.controller.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.MessageAdapter;
import com.example.chen.simpleparkingapp.base.ActivityManager;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.controller.login.LoginActivity;
import com.example.chen.simpleparkingapp.model.Message;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.view.dialog.MessagePopupWindow;
import com.example.chen.simpleparkingapp.viewmodel.MessageViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageListActivity extends BaseActivity implements View.OnClickListener, MessagePopupWindow.OnDeleteClickListener {

    private MessageViewModel presentViewModel;
    private List<Message> mDatas = new ArrayList<>();
    private User user;
    private SmartRefreshLayout refreshLayout;
    private int beforePage = 0;
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private View noDataView, errView;
    private MessagePopupWindow messagePopupWindow;
    private int deletePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        ActivityManager.addActivity(this);
        user = UserCenter.getInstance().getUser();
        if (user == null) {
            Route.route().nextController(this, LoginActivity.class.getName(), Route.WITHOUT_RESULTCODE);
        }
        initView();
        getMessageList();
    }

    private void initView() {
        initTitle();
        errView = LayoutInflater.from(this).inflate(R.layout.layout_empty_err, null);
        errView.findViewById(R.id.tvErrorView).setOnClickListener(this);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        noDataView = LayoutInflater.from(this).inflate(R.layout.layout_empty_data, null);
        TextView tvNoData = noDataView.findViewById(R.id.tvNoData);
        tvNoData.setText("当前没有消息哦··");
        messagePopupWindow = new MessagePopupWindow(this);
        messagePopupWindow.setOnDeleteClickListener(this);
        initAdapter();
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(mDatas);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                beforePage = 0;
                getMessageList();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getMessageList();
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tvLook) {
                    Intent intent = new Intent(MessageListActivity.this, MessageDetailActivity.class);
                    intent.putExtra(MessageDetailActivity.ID, mDatas.get(position).getId());
                    Route.route().nextControllerWithIntent(MessageListActivity.this, MessageDetailActivity.class.getName(), Route.WITHOUT_RESULTCODE, intent);
                }
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                deletePosition = position;
                if (!messagePopupWindow.isShowing()) {
                    messagePopupWindow.showAsDropDown(view, 0, -view.getHeight() - DensityUtil.dp2px(58));
                }
                return true;
            }
        });
    }

    private void initTitle() {
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        tvTitle.setText("消息");
    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (MessageViewModel) baseViewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tvErrorView:
                getMessageList();
                break;
        }
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_MESSAGE_LIST)) {//消息列表
            dismissProgress();
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh();
            List<Message> tempList = presentViewModel.messageList;
            if (tempList == null || tempList.size() == 0) {
                if (beforePage == 0) {
                    adapter.setEmptyView(noDataView);
                } else {
                    ToastUtils.show(this, "没有更多数据了");
                }
                return;
            }
            if (beforePage == 0) {
                mDatas.clear();
            }
            mDatas.addAll(tempList);
            adapter.notifyDataSetChanged();
            beforePage++;

        } else if (token.method.equals(AppServiceMediator.SERVICE_DELETE_MESSAGE_BY_ID)) {
            dismissProgress();
            ToastUtils.show(this, "删除成功");
            adapter.remove(deletePosition);
            if (mDatas.size() == 0) {
                adapter.setEmptyView(noDataView);
            }
        }
    }


    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_MESSAGE_LIST)) {
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh();
            if (beforePage == 0) {
                adapter.setEmptyView(errView);
            }
        }
    }

    @Override
    public void onDelete() {
        delecteMessage();
        messagePopupWindow.dismiss();
    }

    //删除消息
    private void delecteMessage() {
        showDialog();
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("messageId", "" + mDatas.get(deletePosition).getId());
        parmas.put("userId", "" + (user.getId()));
        doTask(AppServiceMediator.SERVICE_DELETE_MESSAGE_BY_ID, parmas);
    }


    //获取消息列表
    private void getMessageList() {
        showDialog();
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("pageNum", "" + (beforePage + 1));
        parmas.put("pageSize", "10");
        parmas.put("userId", "" + (user.getId()));
        doTask(AppServiceMediator.SERVICE_GET_MESSAGE_LIST, parmas);
    }
}
