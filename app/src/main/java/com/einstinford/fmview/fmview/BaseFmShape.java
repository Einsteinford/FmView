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
    private float x,y;
    private int status;

    /**
     *  红色变大字体
     */
    public static final int STATUS_ON = 1;
    /**
     * 默认色
     */
    public static final int STATUS_OFF = 0;

    public FmText(float x, float y) {
        this.x = x;
        this.y = y;
        this.status = STATUS_OFF;
    }

    @Override
    void draw(Canvas canvas) {

    }
}

//class FmText extends BaseFmShape implements DrawText{
//    //TODO 职责太多
//
//    private Paint mTextPaint;
//    private Paint mRedTextPaint;
//    /**
//     * 默认刻度两侧边距
//     */
//    private float defaultDistance;
//    private float mLongScale;
//    private float mTextY;
//
//    public FmText(int textSize, int redTextSize, int screenWidth, Typeface redTextType) {
//        defaultDistance = screenWidth * 8 / 75;
//        mLongScale = (screenWidth - (defaultDistance * 2)) / 9;
//        mTextY = screenWidth * 0.26f;
//
//        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setColor(Color.parseColor("#909aa9"));
//        mTextPaint.setTextSize(textSize);
//        mTextPaint.setFakeBoldText(true);
//        mTextPaint.setTextAlign(Paint.Align.CENTER);
//
//        mRedTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mRedTextPaint.setColor(Color.parseColor("#F27D73"));
//        mRedTextPaint.setTextSize(redTextSize);
//        mRedTextPaint.setFakeBoldText(true);
//        //使用自定义字体
//        mRedTextPaint.setTypeface(redTextType);
//        mRedTextPaint.setTextAlign(Paint.Align.CENTER);
//
//    }
//
//    @Override
//    void draw(Canvas canvas) {
//            if (i == sFmPosition) {
//                canvas.drawText(mFmList.get(i).mhz,
//                        defaultDistance + mLongScale * i, mTextY,
//                        mRedTextPaint);
//            } else {
//                canvas.drawText(mFmList.get(i).mhz,
//                        defaultDistance + mLongScale * i, mTextY,
//                        mTextPaint);
//            }
//
//        }
//
//    @Override
//    public void drawText(String text, boolean isRed) {
//
//    }
//}
//}
//
//interface DrawText{
//  void drawText(String text,boolean isRed);
//}