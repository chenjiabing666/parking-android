package com.example.chen.simpleparkingapp.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.model.YearVipVos;

import java.util.List;

public class VIPManagerAdapter extends BaseQuickAdapter<YearVipVos, BaseViewHolder> {
    public VIPManagerAdapter(@Nullable List<YearVipVos> data) {
        super(R.layout.item_vip_manager, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YearVipVos item) {
        if (!TextUtils.isEmpty(item.getParkingName())) {
            helper.setText(R.id.tvParking, item.getParkingName());
        }

        helper.addOnClickListener(R.id.tvDo);

        helper.setText(R.id.tvStartTime, "开通时间：" + item.getCreatedDate())
                .setText(R.id.tvEndTime, "到期时间：" + item.getExpireDate());

        if (!TextUtils.isEmpty(item.getImageUrl())) {
            Glide.with(mContext).load(item.getImageUrl()).error(R.drawable.load_img_err).into((ImageView) helper.getView(R.id.ivIcon));
        }
    }
}
