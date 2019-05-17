package com.example.chen.simpleparkingapp.adapter;

import android.support.annotation.Nullable;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.model.Order;
import com.example.chen.simpleparkingapp.utils.GlideImageLoader;

import java.util.List;

public class OrdersAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {

    public OrdersAdapter(@Nullable List<Order> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {

        helper.setVisible(R.id.tvDo, false);
        if (item.getStatus() != null) {
            if (item.getStatus() == 1) {
                helper.setVisible(R.id.tvDo, true);
                helper.addOnClickListener(R.id.tvDo);
            }
        }

        if (!TextUtils.isEmpty(item.getImageUrl())) {
            Glide.with(mContext).load(item.getImageUrl()).error(R.drawable.load_img_err).into((ImageView) helper.getView(R.id.ivIcon));
        }

        if (!TextUtils.isEmpty(item.getParkingName())) {
            helper.setText(R.id.tvParking,item.getParkingName());
        }

        if (!TextUtils.isEmpty(item.getNumber())) {
            helper.setText(R.id.tvNumber, "订单编号：" + item.getNumber());
        }
        helper.setText(R.id.tvTime, "时间：" + item.getStartTime());

        if (item.getMoney() != null) {
            helper.setText(R.id.tvPrice, "总计：" + item.getMoney() + "元");
        }


    }
}
