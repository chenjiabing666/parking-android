package com.example.chen.simpleparkingapp.controller.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.simpleparkingapp.R;
import com.example.chen.taco.mvvm.BaseActivity;

import java.util.ArrayList;

public class SeeImgActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TextView tvView;
    private ArrayList<String> mDatas;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_img);

        mDatas = getIntent().getStringArrayListExtra("urls");
        position = getIntent().getIntExtra("position", 0);
        viewPager = findViewById(R.id.viewPager);
        tvView = findViewById(R.id.tvView);
        findViewById(R.id.ivBack).setOnClickListener(this);
        viewPager.setAdapter(new MyPagerAdapter(mDatas));
        viewPager.addOnPageChangeListener(new MyPagerChanger());
        if (mDatas != null && mDatas.size() > 0) {
            tvView.setText((position + 1) +"/"+ mDatas.size()+"");
        }
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        private final ArrayList<String> mDatas;

        public MyPagerAdapter(ArrayList<String> mDatas) {
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(SeeImgActivity.this);
            Glide.with(SeeImgActivity.this).load(mDatas.get(position)).error(R.drawable.load_img_err).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private class MyPagerChanger implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if (mDatas != null && mDatas.size() > 0) {
                tvView.setText((i + 1)  +"/"+ mDatas.size()+"");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }
}
