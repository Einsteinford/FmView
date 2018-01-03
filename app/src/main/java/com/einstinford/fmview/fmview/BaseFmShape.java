package com.einstinford.fmview.fmview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;


/**
 * @author kk
 * @date 2017/12/26
 */

public abstract class BaseFmShape {
    /**
     * 用于组件自己根据自己的需求进行绘制
     *
     * @param canvas
     */
    abstract void draw(Canvas canvas);
}

class FmBitmap extends BaseFmShape {
    private Paint mBitmapPaint;
    private Bitmap mBitmap;

    public FmBitmap(Bitmap bitmap) {
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = bitmap;
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
    }
}

class FmScale extends BaseFmShape {
    private Paint mPaintScale;
    private Path mPathScale;

    public FmScale(int screenWidth, int strokeWidth) {
        float defaultDistance = screenWidth * 8 / 75;    //默认刻度两侧边距;
        float mLongScale = (screenWidth - (defaultDistance * 2)) / 9;
        float mDefaultTop = screenWidth * 0.067f;
        float shortScale = (screenWidth - (defaultDistance * 2)) / 36;

        mPaintScale = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintScale.setStrokeWidth(strokeWidth);
        mPaintScale.setStyle(Paint.Style.STROKE);
        mPaintScale.setColor(Color.parseColor("#DFD5CC"));
        mPathScale = new Path();

        for (int i = 0; i < 10; i++) {
            mPathScale.moveTo(defaultDistance + mLongScale * i, mDefaultTop);
            mPathScale.lineTo(defaultDistance + mLongScale * i, screenWidth * 0.18f);
            if (i != 9) {
                for (int j = 1; j < 4; j++) {
                    mPathScale.moveTo(defaultDistance + mLongScale * i + shortScale * j, mDefaultTop);
                    mPathScale.lineTo(defaultDistance + mLongScale * i + shortScale * j, screenWidth * 0.13f);
                }
            }
        }

    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawPath(mPathScale, mPaintScale);
    }
}

class FmText extends BaseFmShape {
    private float x, y;
    private int textSize, redTextSize;
    Typeface redTextType;
    private int status;
    private String text;

    /**
     * 默认色
     */
    public static final int STATUS_OFF = 0;
    private final int normal = Color.parseColor("#909aa9");
    private Paint textPaint;
    /**
     * 红色变大字体
     */
    public static final int STATUS_ON = 1;
    private final int red = Color.parseColor("#F27D73");

    public FmText(float x, float y, int textSize, int redTextSize, Typeface redTextType) {
        this.x = x;
        this.y = y;
        this.textSize = textSize;
        this.redTextSize = redTextSize;
        this.redTextType = redTextType;
        this.status = STATUS_OFF;

        this.textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    void draw(Canvas canvas) {
        if (text != null) {
            if (this.status == STATUS_OFF) {
                textPaint.setColor(normal);
                textPaint.setTextSize(textSize);
                textPaint.setTypeface(Typeface.DEFAULT);
            } else {
                textPaint.setColor(red);
                textPaint.setTextSize(redTextSize);
                textPaint.setTypeface(redTextType);
            }
            canvas.drawText(text, x, y, textPaint);
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}