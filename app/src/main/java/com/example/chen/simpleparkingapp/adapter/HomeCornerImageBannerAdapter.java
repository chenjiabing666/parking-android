package com.example.chen.simpleparkingapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.simpleparkingapp.controller.home.ParkingDetailsActivity;
import com.example.chen.simpleparkingapp.controller.home.SeeImgActivity;
import com.example.chen.simpleparkingapp.model.Banner;
import com.example.chen.simpleparkingapp.view.widget.RoundCornersImageView;
import com.example.chen.taco.mvvm.Route;

import java.util.ArrayList;
import java.util.List;

public class HomeCornerImageBannerAdapter extends PagerAdapter {

    private final Handler handler;
    private final int FANCY_WHAT;
    private List<Banner> banners;
    private ArrayList<String> mImgData = new ArrayList<>();
    private Activity context;
    private final LayoutInflater inflater;

    public HomeCornerImageBannerAdapter(List<Banner> banners, Activity context, Handler handler, int FANCY_WHAT) {
        this.banners = banners;
        this.context = context;
        this.handler = handler;
        this.FANCY_WHAT = FANCY_WHAT;
        inflater = LayoutInflater.from(context);

        mImgData.clear();
        for (Banner banner : banners) {
            mImgData.add(banner.getImageUrl());
        }
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final int realPosition = position % banners.size();
        View view = inflater.inflate(R.layout.item_round_corner_img, null);
        RoundCornersImageView rcImg = view.findViewById(R.id.rcImg);
        rcImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String icon = banners.get(realPosition).getImageUrl();
        if (!TextUtils.isEmpty(icon)) {
            Glide.with(context).load(icon).error(R.drawable.load_img_err).into(rcImg);
        }
        container.addView(view);

        rcImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下
                        handler.removeMessages(FANCY_WHAT);
                        break;
                    case MotionEvent.ACTION_UP://起来
                        handler.sendEmptyMessageDelayed(FANCY_WHAT, 2000);
                        break;
                    case MotionEvent.ACTION_MOVE://移动
                        break;
                }
                return false;
            }
        });

        rcImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SeeImgActivity.class);
                intent.putStringArrayListExtra("urls", mImgData);
                intent.putExtra("position", realPosition);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
