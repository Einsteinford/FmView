package com.einstinford.fmview.fmview;

import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.animation.LinearInterpolator;

/**
 * @author kk
 * @date 2017/12/26
 */

public abstract class BaseFmAnim extends BaseFmShape {
    /**
     * 启动动画效果
     */
    abstract void startAnim();
}

class FmWheel extends BaseFmAnim {
    private Paint mPaintWheel;
    private Path mPathWheel;
    private Path mPathWheelAnim;
    private int wheelInt;
    private ObjectAnimator mWheelAnimator;

    public FmWheel(int strokeWidth, int ScreenWidth) {

        mPaintWheel = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintWheel.setColor(Color.parseColor("#6B5942"));
        mPaintWheel.setStrokeWidth(strokeWidth);
        mPaintWheel.setStyle(Paint.Style.STROKE);


        long wheelStart = ScreenWidth * 128 / 375;
        long wheelSpace = ScreenWidth * 8 / 575;
        long wheelYStart = ScreenWidth * 12 / 25;
        long wheelYEnd = ScreenWidth * 62 / 125;
        long wheelAnimStart = ScreenWidth * 131 / 375;

        Path pathWheel = new Path();
        for (int i = 0; i < 24; i++) {
            pathWheel.moveTo(wheelStart + wheelSpace * i, wheelYStart);
            pathWheel.lineTo(wheelStart + wheelSpace * i, wheelYEnd);
        }
        Path pathWheelAnim = new Path();
        for (int i = 0; i < 23; i++) {
            pathWheelAnim.moveTo(wheelAnimStart + wheelSpace * i, wheelYStart);
            pathWheelAnim.lineTo(wheelAnimStart + wheelSpace * i, wheelYEnd);
        }

        mPathWheel = pathWheel;
        mPathWheelAnim = pathWheelAnim;
        mWheelAnimator = ObjectAnimator.ofInt(this, "wheelInt", 0);
        mWheelAnimator.setDuration(350);
        mWheelAnimator.setInterpolator(new LinearInterpolator());
        mWheelAnimator.setIntValues(0, 7);
    }

    @Override
    void draw(Canvas canvas) {
        if (wheelInt % 2 == 0) {
            canvas.drawPath(mPathWheel, mPaintWheel);
        } else {
            canvas.drawPath(mPathWheelAnim, mPaintWheel);
        }
    }

    @Override
    public void startAnim() {
        mWheelAnimator.start();
    }

    public int getWheelInt() {
        return wheelInt;
    }

    public void setWheelInt(int wheelInt) {
        this.wheelInt = wheelInt;
    }
}
