package com.example.chen.simpleparkingapp.controller.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.base.UserCenter;
import com.example.chen.simpleparkingapp.controller.login.LoginActivity;
import com.example.chen.simpleparkingapp.model.Parking;
import com.example.chen.simpleparkingapp.model.ParkingImage;
import com.example.chen.simpleparkingapp.model.RsParking;
import com.example.chen.simpleparkingapp.model.User;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.utils.GlideImageLoader;
import com.example.chen.simpleparkingapp.view.dialog.CustomDialog;
import com.example.chen.simpleparkingapp.viewmodel.ParkingViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.DateUtil;
import com.example.chen.taco.utils.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.transformer.AccordionTransformer;

import java.util.*;

public class ParkingDetailsActivity extends BaseActivity implements View.OnClickListener, CustomDialog.OnConfirmClickListener {

    private ParkingViewModel presentViewModel;
    public static String PARKING_ID = "parkingId";
    private int parkingId;
    private List<String> banners = new ArrayList<>();
    //轮播图片使用Banner
    private Banner banner;
    private TextView tvAddress, tvCount, tvCall, tvPrice, tvPreview, tvYearPrice;
    private TimePickerView startTimePv, endTimePv;
    private String startTime, endTime;
    private CustomDialog openYearDiloag;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        parkingId = getIntent().getIntExtra(PARKING_ID, 0);
        user = UserCenter.getInstance().getUser();

        openYearDiloag = new CustomDialog(this);
        openYearDiloag.setListener(this);
        openYearDiloag.setContent("确定开通该停车场的年费？");
        initView();
        initStartTime();
        initEndTime();
        getParkingById();
    }

    @Override
    public void alreadyBindBaseViewModel() {
        super.alreadyBindBaseViewModel();
        presentViewModel = (ParkingViewModel) baseViewModel;
    }

    private void initView() {
        banner = findViewById(R.id.banner);
        TextView titleMiddle = findViewById(R.id.tv_title_middle);
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        titleMiddle.setText("详情");
        banner.setBannerAnimation(AccordionTransformer.class);
        tvAddress = findViewById(R.id.tvAddress);
        tvCount = findViewById(R.id.tvCount);
        tvCall = findViewById(R.id.tvCall);
        tvPrice = findViewById(R.id.tvPrice);
        tvYearPrice = findViewById(R.id.tvYearPrice);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnSubmit).setOnClickListener(this);
        tvPreview = findViewById(R.id.tvPreview);
        tvPreview.setOnClickListener(this);
    }

    private void getParkingById() {
        HashMap<String, String> params = new HashMap<>();
        params.put("parkingId", "" + parkingId);
        doTask(AppServiceMediator.SERVICE_GET_PARKING_BY_ID, params);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_PARKING_BY_ID)) {
            dismissProgress();
            RsParking rsParking = presentViewModel.parking;

            if (rsParking != null) {
                List<ParkingImage> images = rsParking.getImages();
                if (images != null && images.size() > 0) {
                    for (int i = 0; i < images.size(); i++) {
                        banners.add(images.get(i).getUrl());
                    }
                    //设置轮播的图片，并且设置图片加载器ImageLoader
                    banner.setImages(banners)
                            .setImageLoader(new GlideImageLoader())
                            .start();
                }

                Parking parking = rsParking.getParking();
                if (parking != null) {
                    if (!TextUtils.isEmpty(parking.getAddress())) {
                        tvAddress.setText(parking.getAddress());
                    }
                    if (parking.getLeaveCount() != null) {
                        tvCount.setText("车位：" + parking.getLeaveCount() + "个");
                    }
                    if (!TextUtils.isEmpty(parking.getMobile())) {
                        tvCall.setText("电话：" + parking.getMobile());
                    }
                    if (parking.getMoney() != null) {
                        tvPrice.setText("价格：" + parking.getMoney() + "元/分钟");
                    }
                    if (parking.getYearMoney() != null) {
                        tvYearPrice.setText("年费价格：￥" + parking.getYearMoney());
                    }
                }
            }
        } else if (token.method.equals(AppServiceMediator.SERVICE_ADD_ORDER)) {//预约
            dismissProgress();
            ToastUtils.show(this, "预约成功");
            finish();
        } else if (token.method.equals(AppServiceMediator.SERVICE_OPEN_YEAR)) {//开通年费
            dismissProgress();
            ToastUtils.show(this, "开通成功");
        }
    }

    @Override
    public void requestFailedHandle(TaskToken token, int errorCode, String errorMsg) {
        super.requestFailedHandle(token, errorCode, errorMsg);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_PARKING_BY_ID)) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.btnStart://开通年费
                if (!openYearDiloag.isShowing()) {
                    openYearDiloag.show();
                }
                break;
            case R.id.btnSubmit://提交预约
                submitBook();
                break;
            case R.id.tvPreview://选择预约时间
                if (!startTimePv.isShowing()) {
                    startTimePv.show();
                }
                break;
        }
    }

    //提交预约
    private void submitBook() {
        if (TextUtils.isEmpty(startTime)) {
            ToastUtils.show(this, "请选择预约时间");
            return;
        }
        if (user == null) {
            Route.route().nextController(this, LoginActivity.class.getName(), Route.WITHOUT_RESULTCODE);
            return;
        }
        showDialog();
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("userId", "" + user.getId());
        parmas.put("startTime", startTime);
        parmas.put("endTime", endTime);
        parmas.put("parkingId", parkingId + "");
        doTask(AppServiceMediator.SERVICE_ADD_ORDER, parmas);

    }

    //选择预约时间
    private void initStartTime() {
        Calendar startData = Calendar.getInstance();

        final Calendar endData = Calendar.getInstance();
        endData.set(Calendar.YEAR, startData.get(Calendar.YEAR) + 2);
        endData.set(Calendar.MONTH, 11);
        endData.set(Calendar.DAY_OF_MONTH, 31);

        startTimePv = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                startTime = DateUtil.stringFromTime(date, "yyyy-MM-dd HH:mm");
                if (!endTimePv.isShowing()) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    endTimePv.setDate(calendar);
                    endTimePv.show();
                }

            }
        })
                .setLayoutRes(R.layout.pickerview_time_start, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        Button btnSubmit = v.findViewById(R.id.btnSubmit);
                        Button btnCancle = v.findViewById(R.id.btnCancel);

                        btnCancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startTimePv.dismiss();
                            }
                        });

                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startTimePv.returnData();
                                startTimePv.dismiss();
                            }
                        });
                    }
                })
                .setRangDate(startData, endData)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setDividerColor(getResources().getColor(R.color.list_divider))
                .setContentSize(14)
                .setBgColor(getResources().getColor(R.color.colortransparent))
                .setDate(Calendar.getInstance())
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .build();
    }

    private void initEndTime() {
        Calendar startData = Calendar.getInstance();

        final Calendar endData = Calendar.getInstance();
        endData.set(Calendar.YEAR, startData.get(Calendar.YEAR) + 2);
        endData.set(Calendar.MONTH, 11);
        endData.set(Calendar.DAY_OF_MONTH, 31);

        endTimePv = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                endTime = DateUtil.stringFromTime(date, "yyyy-MM-dd HH:mm");
                if (endTime.compareTo(startTime) <= 0) {
                    ToastUtils.show(ParkingDetailsActivity.this, "结束时间应比开始时间晚");
                } else {
                    endTimePv.dismiss();
                    tvPreview.setText("预约：" + startTime + " 到 " + endTime);
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_time_start, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        Button btnSubmit = v.findViewById(R.id.btnSubmit);
                        Button btnCancle = v.findViewById(R.id.btnCancel);

                        btnCancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                endTimePv.dismiss();
                            }
                        });

                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                endTimePv.returnData();
                            }
                        });
                    }
                })
                .setRangDate(startData, endData)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setDividerColor(getResources().getColor(R.color.list_divider))
                .setContentSize(14)
                .setBgColor(getResources().getColor(R.color.colortransparent))
                .setDate(Calendar.getInstance())
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .build();
    }

    @Override
    public void onConfirmClick() {
        openYear();
    }

    private void openYear() {
        if (user == null) {
            Route.route().nextController(this, LoginActivity.class.getName(), Route.WITHOUT_RESULTCODE);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "" + user.getId());
        params.put("parkingId", "" + parkingId);
        doTask(AppServiceMediator.SERVICE_OPEN_YEAR, params);
    }
}
