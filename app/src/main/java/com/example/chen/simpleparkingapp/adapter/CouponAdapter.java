package com.example.chen.simpleparkingapp.adapter;

import android.support.annotation.Nullable;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.model.Coupon;

import java.util.List;

public class CouponAdapter extends BaseQuickAdapter<Coupon, BaseViewHolder> {

    private final int type;//种类 1 使用优惠券

    public CouponAdapter(@Nullable List<Coupon> data, int type) {
        super(R.layout.item_coupon, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, Coupon item) {

        if (item.getMoney() != null) {
            TextView tvMoney = helper.getView(R.id.tvMoney);
            String money = "<font color='#00c188'><small>￥</small></font><font color='#00c188'><big><big>" + item.getMoney() + "</big></big></font>";
            tvMoney.setText(Html.fromHtml(money));
        }

        if (item.getType() != null && item.getType() == 2) {//指定停车场
            helper.setText(R.id.tvCoupon, "指定停车场使用");
        } else {
            helper.setText(R.id.tvCoupon, "通用");
        }

        helper.setText(R.id.tvCoupon,item.getContent());

        if (!TextUtils.isEmpty(item.getContent())) {
            helper.setText(R.id.tvCoupon, item.getContent());
        }

        helper.setVisible(R.id.tvUserNow, type == 1);

        helper.addOnClickListener(R.id.tvUserNow);

        helper.setText(R.id.tvTime,"到期时间："+item.getExpireDate());

    }
}
