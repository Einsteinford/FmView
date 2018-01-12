package com.einstinford.fmview.fmview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.einstinford.fmview.BaseData;
import com.einstinford.fmview.R;

import java.util.Observable;

/**
 * @author kk
 * @date 2017/12/26
 */

public abstract class BaseFmAnim extends BaseFmShape {
    enum Direction {
        LEFT, RIGHT
    }

    /**
     * 启动动画效果
     *
     * @param direction 动画的方向
     * @param size      格子数目
     */
    abstract void startAnim(Direction direction, int size);
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
    public void draw(Canvas canvas) {
        if (wheelInt % 2 == 0) {
            canvas.drawPath(mPathWheel, mPaintWheel);
        } else {
            canvas.drawPath(mPathWheelAnim, mPaintWheel);
        }
    }

    @Override
    public void startAnim(Direction direction ,int size) {
        mWheelAnimator.start();
    }

    public int getWheelInt() {
        return wheelInt;
    }

    public void setWheelInt(int wheelInt) {
        this.wheelInt = wheelInt;
    }
}

class FmPointer extends BaseFmAnim {
    private float progress = 0;
    private ObjectAnimator mAnimator;
    private OvershootInterpolator mInterpolator;
    private AccelerateDecelerateInterpolator mTempInterpolator;
    private Paint mPaintPointer;
    private Matrix mMatrixPointer;
    private Shader mShaderPointer;

    private float mStopY;
    private float mDefaultDistance;
    private float mDefaultTop;


    private float mPointStartX;
    private float mPointEndX;

    private float mLongScale;
    private int position;

    public FmPointer(Paint paintPointer, Shader pointerShader, int screenWidth, int position) {
        this.mPaintPointer = paintPointer;
        this.mShaderPointer = pointerShader;
        this.mStopY = screenWidth * 0.33f;
        this.mDefaultTop = screenWidth * 0.067f;
        this.mDefaultDistance = screenWidth * 8 / 75;
        this.mLongScale = (screenWidth - (mDefaultDistance * 2)) / 9;
        this.progress = position * mLongScale;
        this.position = position;

        /*指针的左侧坐标*/
        mPointStartX = this.position * mLongScale;
        /*指针的右侧坐标*/
        mPointEndX = mPointStartX + mLongScale;

        /*作为指针动画的变量*/

        mAnimator = ObjectAnimator.ofFloat(this, "progress", 0);
        mAnimator.setDuration(450);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i("kkTest", "onAnimationStart: " + progress);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i("kkTest", "onAnimationEnd: " + progress);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        //普通动画效果
        mInterpolator = new OvershootInterpolator();
        //超过刻度的动画
        mTempInterpolator = new AccelerateDecelerateInterpolator();

        mPaintPointer.setShader(mShaderPointer);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mMatrixPointer != null) {
            mMatrixPointer.setTranslate(progress, 0);
            mShaderPointer.setLocalMatrix(mMatrixPointer);
        } else {
            mMatrixPointer = new Matrix();
        }

        canvas.drawLine(mDefaultDistance + progress, mDefaultTop,
                mDefaultDistance + progress, mStopY, mPaintPointer);
    }

    @Override
    public void startAnim(Direction direction ,int size) {
        switch (direction) {
            case LEFT:
                if (position == 0) {
                    pointToMax(size);
                } else {
                    pointLeft(size);
                }
                break;
            case RIGHT:
                if (position == size) {
                    pointToZero();
                } else {
                    pointRight(size);
                }
                break;
            default:
                break;
        }
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    private void pointLeft(int maxPos) {
        if (position == maxPos) {
            mPointStartX -= mLongScale;
            mPointEndX = maxPos * mLongScale;
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointEndX, mPointStartX);
            mAnimator.start();
            position = maxPos - 1;
        } else if (position > 0) {
            mPointStartX -= mLongScale;
            mPointEndX -= mLongScale;
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointEndX, mPointStartX);
            mAnimator.start();
            position--;
        }
//        positionChanged();
    }

    private void pointRight(int maxPos) {
        if (position == 0) {
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointStartX, mPointEndX);
            mAnimator.start();
            mPointStartX += mLongScale;
            mPointEndX += mLongScale;
            position = 1;
        } else if (position < maxPos) {
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointStartX, mPointEndX);
            mAnimator.start();
            mPointStartX += mLongScale;
            mPointEndX += mLongScale;
            position++;
        }
//        positionChanged();
    }

    private void pointToZero() {
        mPointEndX = 0;
        mAnimator.setInterpolator(mTempInterpolator);
        mAnimator.setFloatValues(mPointStartX, mPointEndX);
        mAnimator.start();
        mPointEndX = mLongScale;
        mPointStartX = 0;
        position = 0;
//        positionChanged();
    }

    private void pointToMax(int maxPos) {
        mPointEndX = 0;
        mPointStartX = maxPos * mLongScale;
        mAnimator.setInterpolator(mTempInterpolator);
        mAnimator.setFloatValues(mPointEndX, mPointStartX);
        mAnimator.start();
        position = maxPos;
//        positionChanged();
    }
}
