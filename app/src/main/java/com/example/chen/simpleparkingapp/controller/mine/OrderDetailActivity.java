package com.example.chen.simpleparkingapp.controller.mine;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.controller.home.ParkingDetailsActivity;
import com.example.chen.simpleparkingapp.model.Order;
import com.example.chen.simpleparkingapp.network.AppServiceMediator;
import com.example.chen.simpleparkingapp.viewmodel.OrderViewModel;
import com.example.chen.taco.mvvm.BaseActivity;
import com.example.chen.taco.mvvm.Route;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.ToastUtils;

import java.util.HashMap;

/**
 * 订单详情页面
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private final int SELECT_COUPON = 0x110;//选择优惠券
    public static String ORDER_ID = "orderId";
    public static String POSITION = "position";
    private OrderViewModel presentViewModel;
    private int type;//1付款,0订单详情
    public static String TITLE_TYPE = "titleType";
    private int orderId;
    private int couponId = 0;//优惠券id
    private int position;//订单列表的位置
    private Order order;
    private Button btnCancle, btnPay, btnSelectCoupon,btnMore;
    private ImageView ivIcon;
    private TextView tvNumber, tvPrice, tvStartTime, tvEndTime, tvParking;
    private int orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        //获取intent传递过来的数据
        type = getIntent().getIntExtra(TITLE_TYPE, 0);
        orderType = getIntent().getIntExtra("type", 0);
        orderId = getIntent().getIntExtra(ORDER_ID, 0);
        position = getIntent().getIntExtra(POSITION, 0);
        initView();
        getOrderDetail();
    }

    private void initView() {
        findViewById(R.id.ll_title_left).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title_middle);
        if (type == 1) {
            tvTitle.setText("付款");
        } else {
            tvTitle.setText("订单详情");
        }
        tvParking = findViewById(R.id.tvParking);
        ivIcon = findViewById(R.id.ivIcon);
        btnCancle = findViewById(R.id.btnCancle);
        btnPay = findViewById(R.id.btnPay);
        tvNumber = findViewById(R.id.tvNumber);
        tvPrice = findViewById(R.id.tvPrice);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        btnSelectCoupon = findViewById(R.id.btnSelectCoupon);
        btnCancle.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        btnSelectCoupon.setOnClickListener(this);
        btnCancle.setVisibility(View.GONE);
        btnPay.setVisibility(View.GONE);
        btnSelectCoupon.setVisibility(View.GONE);
        btnMore=findViewById(R.id.btn_more);
        btnMore.setOnClickListener(this);
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
            case R.id.btnCancle://取消订单
                cancleOrder();
                break;
            case R.id.btnPay://付款
                payOrder();
                break;
            case R.id.btnSelectCoupon://优惠券
                Intent intent = new Intent(this, GetCouponByOrderIdActivity.class);
                intent.putExtra(ORDER_ID, orderId);
                Route.route().nextControllerWithIntent(this, GetCouponByOrderIdActivity.class.getName(), SELECT_COUPON, intent);
                break;
            case R.id.btn_more://再次预约
                Intent intent1 = new Intent(this, ParkingDetailsActivity.class);
                intent1.putExtra(ParkingDetailsActivity.PARKING_ID, order.getParkingId());
                Route.route().nextControllerWithIntent(this, ParkingDetailsActivity.class.getName(), Route.WITHOUT_RESULTCODE,intent1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_COUPON) {
            couponId = data.getIntExtra(GetCouponByOrderIdActivity.COUPON_ID, 0);
            ToastUtils.show(this, "已选择优惠券，请付款");
        }
    }

    //付款
    private void payOrder() {
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", "" + orderId);
        params.put("couponId", couponId == 0 ? "" : (couponId + ""));
        doTask(AppServiceMediator.SERVICE_PAY_ORDER, params);
    }

    //取消订单
    private void cancleOrder() {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", "" + orderId);
        doTask(AppServiceMediator.SERVICE_CANCLE_ORDER, params);
    }

    //获取订单详情
    private void getOrderDetail() {
        showDialog();
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("orderId", "" + orderId);
        doTask(AppServiceMediator.SERVICE_GET_ORDER_BY_ID, parmas);
    }

    @Override
    public void refreshData(TaskToken token) {
        super.refreshData(token);
        if (token.method.equals(AppServiceMediator.SERVICE_GET_ORDER_BY_ID)) {
            order = presentViewModel.order;
            dismissProgress();
            initOrder();
        } else if (token.method.equals(AppServiceMediator.SERVICE_PAY_ORDER)) {//付款
            dismissProgress();
            ToastUtils.show(this, "付款成功");
            Intent intent = new Intent();
            intent.putExtra(OrderDetailActivity.POSITION, position);
            intent.putExtra("type", orderType);
            setResult(RESULT_OK, intent);
            finish();
        } else if (token.method.equals(AppServiceMediator.SERVICE_CANCLE_ORDER)) {//取消订单
            dismissProgress();
            ToastUtils.show(this, "取消成功");
            Intent intent = new Intent();
            intent.putExtra(OrderDetailActivity.POSITION, position);
            intent.putExtra("type", orderType);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    //初始化订单，设置状态
    private void initOrder() {
        if (order != null) {
            if (order.getStatus() != null) {
                if (order.getStatus() == 1) {//已预约可以取消
                    btnCancle.setVisibility(View.VISIBLE);
//                    btnMore.setVisibility(View.VISIBLE);
                } else if (order.getStatus() == 3) {//待付款
                    btnSelectCoupon.setVisibility(View.VISIBLE);
                    btnPay.setVisibility(View.VISIBLE);
                }else if (order.getStatus() == 4) {//已经完成的订单
                    btnMore.setVisibility(View.VISIBLE);
                }else if (order.getStatus() == 2) {//已取消的订单
                    btnMore.setVisibility(View.VISIBLE);
                }
            }

            if (!TextUtils.isEmpty(order.getImageUrl())) {
                Glide.with(this).load(order.getImageUrl()).error(R.drawable.load_img_err).into(ivIcon);
            }

            tvParking.setText(order.getParkingName() + "");

            if (!TextUtils.isEmpty(order.getNumber())) {
                tvNumber.setText("订单编号：" + order.getNumber());
            }
            if (order.getMoney() != null) {
                tvPrice.setText("总计：" + order.getMoney() + "元");
            }
            tvStartTime.setText("开始时间：" + order.getStartTime());
            tvEndTime.setText("结束时间：" + order.getEndTime());
        }
    }
}
