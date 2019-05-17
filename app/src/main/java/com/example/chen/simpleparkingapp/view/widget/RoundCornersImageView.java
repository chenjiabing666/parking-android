package com.example.chen.simpleparkingapp.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.chen.simpleparkingapp.R;

/**
 * Created by Administrator on 2017/11/25 0025.
 */

public class RoundCornersImageView extends AppCompatImageView {

    private int corner;

    public RoundCornersImageView(Context context) {
        this(context, null);
    }

    public RoundCornersImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornersImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.roundCorner);
        corner = a.getDimensionPixelSize(R.styleable.roundCorner_corner, 0);
        a.recycle();
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(getWidth(),getHeight() , Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        bitmap = GetRoundedCornerBitmap(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    public Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, corner, corner, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    public void setCorner(int corner) {
        this.corner = corner;
        invalidate();
    }
}

