package com.example.chen.simpleparkingapp.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.model.Parking;

import java.util.List;

public class ParkingListAdapter extends BaseQuickAdapter<Parking, BaseViewHolder> {

    public ParkingListAdapter(@Nullable List<Parking> data) {
        super(R.layout.item_parking, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Parking item) {
        if (!TextUtils.isEmpty(item.getImageUrl())) {
            Glide.with(mContext).load(item.getImageUrl()).error(R.drawable.load_img_err).into((ImageView) helper.getView(R.id.ivIcon));
        }
        if (!TextUtils.isEmpty(item.getName())) {
            helper.setText(R.id.tvParking, item.getName());
        }
        if (!TextUtils.isEmpty(item.getAddress())) {
            helper.setText(R.id.tvAddress, "地址：" + item.getAddress());
        }
        if (item.getMoney() != null) {
            helper.setText(R.id.tvPrice, "价格：" + item.getMoney() + "元/分钟");
        }
        helper.setText(R.id.tvCount, "剩余停车位：" + item.getLeaveCount() + "个");
    }
}
