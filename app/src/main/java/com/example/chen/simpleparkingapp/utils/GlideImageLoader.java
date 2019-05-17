package com.example.chen.simpleparkingapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.chen.simpleparkingapp.R;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .centerCrop()
                .error(R.drawable.load_img_err)
                .into(imageView);
    }
}
