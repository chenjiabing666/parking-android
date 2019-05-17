package com.example.chen.simpleparkingapp.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.model.Message;

import java.util.List;

public class MessageAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {

    public MessageAdapter(@Nullable List<Message> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        if (!TextUtils.isEmpty(item.getCreatedDate())) {
            helper.setText(R.id.tvData, item.getCreatedDate());
        }
        if (!TextUtils.isEmpty(item.getContent())) {
            helper.setText(R.id.tvContent, item.getContent());
        }
        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setText(R.id.tvTitle, item.getTitle());
        }
        helper.addOnClickListener(R.id.tvLook);
    }
}
