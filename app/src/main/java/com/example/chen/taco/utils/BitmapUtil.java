package com.example.chen.taco.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.util.HashMap;

public class BitmapUtil {

    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                        .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    public static Bitmap getActivityScreen(Activity activity){
        //获取window最底层的view
        View view=activity.getWindow().getDecorView();
        view.buildDrawingCache();

        //状态栏高度
        Rect rect=new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int stateBarHeight=rect.top;
        Display display=activity.getWindowManager().getDefaultDisplay();

        //获取屏幕宽高
        int widths=display.getWidth();
        int height=display.getHeight();

        //设置允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        //去掉状态栏高度
        Bitmap bitmap= Bitmap.createBitmap(view.getDrawingCache(),0,stateBarHeight,widths,height-stateBarHeight);

        view.destroyDrawingCache();
        return bitmap;
    }

    //获取视频缩略图
    public static Bitmap createVideoThumbnail(Activity activity, String url) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(url, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            UIUtils.showToast(activity,"获取缩略图失败");
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                Log.d("release", "释放MediaMetadataRetriever资源失败");
            }
        }
        return bitmap;
    }

}
