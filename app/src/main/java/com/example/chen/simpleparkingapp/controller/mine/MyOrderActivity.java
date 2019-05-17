package com.example.chen.simpleparkingapp.controller.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.adapter.TabFragmentAdapter;
import com.example.chen.simpleparkingapp.fragment.OrderFragment;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.viewmodel.OrderViewModel;
import com.example.chen.taco.mvvm.BaseFragmentActivity;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends BaseFragmentActivity implements View.OnClickListener {

    private OrderViewModel presentViewModel;
    private ViewPager viewPager;
    private OrderFragment completeFragment, noPayFragment, bookFragment, cancleFragment;
    private List<Fragment> baseFragments = new ArrayList<>();
    private int pos;
    private View sliding_thumb;
    private int beforeIndex = 0;
    private TextView tab1, tab2, tab3, tab4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        pos = SystemUtil.getScreenWidth(this) / 4;
        initView();
    }

    private void initView() {
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        tvTitle.setText("我的订单");
        sliding_thumb = findViewById(R.id.sliding_thumb);
        viewPager = findViewById(R.id.viewPager);
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        tab3 = findViewById(R.id.tab3);
        tab4 = findViewById(R.id.tab4);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);

        completeFragment = new OrderFragment();
        noPayFragment = new OrderFragment();
        bookFragment = new OrderFragment();
        cancleFragment = new OrderFragment();

        baseFragments.add(bookFragment);
        baseFragments.add(cancleFragment);
        baseFragments.add(noPayFragment);
        baseFragments.add(completeFragment);

        for (int i = 0; i < baseFragments.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", (i + 1));
            baseFragments.get(i).setArguments(bundle);
        }

        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(), baseFragments);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(onPageChangeListener);
        onPageChangeListener.onPageSelected(0);
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
            ((OrderFragment) baseFragments.get(i)).getOrderListForeground(MyOrderActivity.this, 0, (i + 1));
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void initTabSelector(int i) {
        tab1.setSelected(false);
        tab2.setSelected(false);
        tab3.setSelected(false);
        tab4.setSelected(false);

        if (i == 0) {
            tab1.setSelected(true);
        } else if (i == 1) {
            tab2.setSelected(true);
        } else if (i == 2) {
            tab3.setSelected(true);
        } else if (i == 3) {
            tab4.setSelected(true);
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
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_ORDERLIST_FOREGROUND)) {
            dismissProgress();
            if (token.flag.equals("1")) {
                ((OrderFragment) baseFragments.get(0)).refreshData(token, presentViewModel);
            } else if (token.flag.equals("2")) {
                ((OrderFragment) baseFragments.get(1)).refreshData(token, presentViewModel);
            } else if (token.flag.equals("3")) {
                ((OrderFragment) baseFragments.get(2)).refreshData(token, presentViewModel);
            } else if (token.flag.equals("4")) {
                ((OrderFragment) baseFragments.get(3)).refreshData(token, presentViewModel);
            }
        } else if (token.method.equals(AppServiceMediator.SERVICE_CANCLE_ORDER)) {
            dismissProgress();
            if (token.flag.equals("1")) {
                ((OrderFragment) baseFragments.get(0)).refreshData(token, presentViewModel);
            } else if (token.flag.equals("2")) {
                ((OrderFragment) baseFragments.get(1)).refreshData(token, presentViewModel);
            } else if (token.flag.equals("3")) {
                ((OrderFragment) baseFragments.get(2)).refreshData(token, presentViewModel);
            } else if (token.flag.equals("4")) {
                ((OrderFragment) baseFragments.get(3)).refreshData(token, presentViewModel);
            }
        } else if (token.method.equals(AppServiceMediator.SERVICE_REFRESH_CODE)) {
            dismissProgress();
            ((OrderFragment) baseFragments.get(Integer.parseInt(token.flag) - 1)).refreshData(token, presentViewModel);
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_ORDERLIST_FOREGROUND)) {
            if (token.flag.equals("1")) {
                ((OrderFragment) baseFragments.get(0)).requestFailedHandle(token, errorCode, errorMsg);
            } else if (token.flag.equals("2")) {
                ((OrderFragment) baseFragments.get(1)).requestFailedHandle(token, errorCode, errorMsg);
            } else if (token.flag.equals("3")) {
                ((OrderFragment) baseFragments.get(2)).requestFailedHandle(token, errorCode, errorMsg);
            } else if (token.flag.equals("4")) {
                ((OrderFragment) baseFragments.get(3)).requestFailedHandle(token, errorCode, errorMsg);
            }
        }
    }

    public interface RefreshListener {
        void refreshData(TaskToken token, OrderViewModel presentViewModel);

        void requestFailedHandle(TaskToken token, int errorCode, String errorMsg);

    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (OrderViewModel) baseViewModel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tab1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tab3:
                viewPager.setCurrentItem(2);
                break;
            case R.id.tab4:
                viewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OrderFragment.ORDER:
                    int type = data.getIntExtra("type", 1);
                    if (type >= 0)
                        baseFragments.get(type - 1).onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }
}
