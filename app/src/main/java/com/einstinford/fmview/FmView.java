package com.einstinford.fmview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.List;


/**
 * Created by kk on 17-9-26.
 */

public class FmView extends View {

    private static final String TAG = "kkTest";
    private Paint mPaintPointer;
    private Paint mPaintCAL;
    float progress = 0;
    int wheelInt;
    private float DEFAULT_DISTANCE = 70;
    private float mDefault_top = TangramViewMetrics.screenWidth() * 0.067f;
    private Shader mShaderPointer;
    private Matrix mMatrixPointer;
    private Path mPathCAL;

    private float mTouchDownX;
    private float mTouchDownY;
    private float mPointStartX;
    private float mPointEndX;
    private float mLongCAL;
    private ObjectAnimator mAnimator;
    private OvershootInterpolator mInterpolator;
    private AccelerateDecelerateInterpolator mTempInterpolator;
    private Paint mTextPaint;
    private List<String> mFmList;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private Paint mPaintWheel;
    private Path mPathWheel;
    private Path mPathWheelAnim;
    private ObjectAnimator mWheelAnimator;

    public FmView(Context context) {
        this(context, null);
    }

    public FmView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FmView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getWheelInt() {
        return wheelInt;
    }

    public void setWheelInt(int wheelInt) {
        this.wheelInt = wheelInt;
    }

    public void init(List<String> fms) {

        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);  //关闭硬件加速
        setBackgroundResource(R.drawable.view_fm_bg);
        mFmList = fms;
        mPaintPointer = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPointer.setShadowLayer(8, 5, 0, Color.parseColor("#9C000000"));
        mPaintPointer.setStrokeWidth(8);
        mShaderPointer = new LinearGradient(DEFAULT_DISTANCE - 2, 100, DEFAULT_DISTANCE + 3, 100, Color.parseColor("#ffa9a4"),
                Color.parseColor("#ca5351"), Shader.TileMode.CLAMP);
        mPaintPointer.setShader(mShaderPointer);

        mPaintCAL = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCAL.setStrokeWidth(5);
        mPaintCAL.setStyle(Paint.Style.STROKE);
        mPaintCAL.setColor(Color.parseColor("#DFD5CC"));
        mPathCAL = new Path();

        mLongCAL = (TangramViewMetrics.screenWidth() - (DEFAULT_DISTANCE * 2)) / 9;
        float ShortCAL = (TangramViewMetrics.screenWidth() - (DEFAULT_DISTANCE * 2)) / 36;

        for (int i = 0; i < 10; i++) {
            mPathCAL.moveTo(DEFAULT_DISTANCE + mLongCAL * i, mDefault_top);
            mPathCAL.lineTo(DEFAULT_DISTANCE + mLongCAL * i, TangramViewMetrics.screenWidth() * 0.18f);
            if (i != 9) {
                for (int j = 1; j < 4; j++) {
                    mPathCAL.moveTo(DEFAULT_DISTANCE + mLongCAL * i + ShortCAL * j, mDefault_top);
                    mPathCAL.lineTo(DEFAULT_DISTANCE + mLongCAL * i + ShortCAL * j, TangramViewMetrics.screenWidth() * 0.13f);
                }
            }
        }
        mPointStartX = 0;
        mPointEndX = mLongCAL;
        mAnimator = ObjectAnimator.ofFloat(this, "progress", 0);
        mAnimator.setDuration(450);
        mInterpolator = new OvershootInterpolator();    //普通动画效果
        mTempInterpolator = new AccelerateDecelerateInterpolator();     //超过刻度的动画

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#DFD5CC"));
        mTextPaint.setTextSize(20);
        mTextPaint.setFakeBoldText(true);

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.view_fm_front);
        mBitmap = Bitmap.createScaledBitmap(temp, TangramViewMetrics.screenWidth(),
                TangramViewMetrics.screenWidth() * 37 / 108, true);


        mWheelAnimator = ObjectAnimator.ofInt(this, "wheelInt", 0);
        mWheelAnimator.setDuration(450);
        mWheelAnimator.setInterpolator(new LinearInterpolator());
        mWheelAnimator.setIntValues(0, 8);
        mPaintWheel = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintWheel.setColor(Color.parseColor("#6B5942"));
        mPaintWheel.setStrokeWidth(2);
        mPaintWheel.setStyle(Paint.Style.STROKE);
        long wheelStart = TangramViewMetrics.screenWidth() * 128 / 375;
        long wheelSpace = TangramViewMetrics.screenWidth() * 8 / 575;
        long wheelYStart = TangramViewMetrics.screenWidth() * 12 / 25;
        long wheelYEnd = TangramViewMetrics.screenWidth() * 62 / 125;
        mPathWheel = new Path();
        for (int i = 0; i < 24; i++) {
            mPathWheel.moveTo(wheelStart + wheelSpace * i, wheelYStart);
            mPathWheel.lineTo(wheelStart + wheelSpace * i, wheelYEnd);
        }
        mPathWheelAnim = new Path();
        long wheelAnim = TangramViewMetrics.screenWidth() * 131 / 375;
        for (int i = 0; i < 23; i++) {
            mPathWheelAnim.moveTo(wheelAnim + wheelSpace * i, wheelYStart);
            mPathWheelAnim.lineTo(wheelAnim + wheelSpace * i, wheelYEnd);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaintPointer != null && mPaintCAL != null && mTextPaint != null) {
            canvas.drawPath(mPathCAL, mPaintCAL);
            Log.i(TAG, "onDraw: " + progress);
            if (wheelInt % 2 == 0) {
                canvas.drawPath(mPathWheel, mPaintWheel);
            } else {
                canvas.drawPath(mPathWheelAnim, mPaintWheel);
            }


            for (int i = 0; i < 10; i++) {
                canvas.drawText(mFmList.get(i),
                        DEFAULT_DISTANCE * 0.63f + mLongCAL * i, TangramViewMetrics.screenWidth() * 0.24f,
                        mTextPaint);
            }
            if (mMatrixPointer != null) {
                mMatrixPointer.setTranslate(progress, 0);
                mShaderPointer.setLocalMatrix(mMatrixPointer);
            } else {
                mMatrixPointer = new Matrix();
            }

            canvas.drawLine(DEFAULT_DISTANCE + progress, mDefault_top,
                    DEFAULT_DISTANCE + progress, TangramViewMetrics.screenWidth() * 0.28f, mPaintPointer);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        }
    }

    // 创建 getter 方法
    public float getProgress() {
        return progress;
    }

    // 创建 setter 方法
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    private void pointLeft() {
        if (mPointStartX == (9 * mLongCAL)) {
            mPointStartX -= mLongCAL;
            mPointEndX = 9 * mLongCAL;
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointEndX, mPointStartX);
            mAnimator.start();

        } else if (mPointStartX > 0) {
            mPointStartX -= mLongCAL;
            mPointEndX -= mLongCAL;
            mAnimator.setFloatValues(mPointEndX, mPointStartX);
            mAnimator.start();
        } else {
            mPointEndX = 0;
            mPointStartX = 9 * mLongCAL;
            mAnimator.setInterpolator(mTempInterpolator);
            mAnimator.setFloatValues(mPointEndX, mPointStartX);
            mAnimator.start();
        }
        //TODO start位置的字体需要进行动画
        String s = mFmList.get((int) (mPointStartX / mLongCAL));
    }

    private void pointRight() {
        if (mPointStartX == 0) {
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointStartX, mPointEndX);
            mAnimator.start();
            mPointStartX += mLongCAL;
            mPointEndX += mLongCAL;
        } else if (mPointStartX < (9 * mLongCAL)) {
            mAnimator.setFloatValues(mPointStartX, mPointEndX);
            mAnimator.start();
            mPointStartX += mLongCAL;
            mPointEndX += mLongCAL;
        } else {
            mPointEndX = 0;
            mAnimator.setInterpolator(mTempInterpolator);
            mAnimator.setFloatValues(mPointStartX, mPointEndX);
            mAnimator.start();
            mPointEndX = mLongCAL;
            mPointStartX = 0;
        }
        //TODO end位置的字体需要进行动画
        String s = mFmList.get((int) (mPointEndX / mLongCAL - 1));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        Log.i(TAG, "widthMeasureSpec: " + TangramViewMetrics.screenWidth());
//        Log.i(TAG, "heightMeasureSpec: " + TangramViewMetrics.screenHeight());
        setMeasuredDimension(TangramViewMetrics.screenWidth(), TangramViewMetrics.screenWidth() * 129 / 216); //填写屏幕像素，高度由宽度乘以系数
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        Log.i(TAG, "dispatchTouchEvent: " + event.getAction());
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                return true;
//            default:
//                return super.dispatchTouchEvent(event);
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i(TAG, "onTouchEvent: " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.i(TAG, "ACTION_DOWN: x = " + event.getX());
//                Log.i(TAG, "ACTION_DOWN: y = " + event.getY());
                mTouchDownY = event.getY();
                mTouchDownX = event.getX();
//                attemptClaimDrag();
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, "ACTION_MOVE: x = " + event.getX());
//                Log.i(TAG, "ACTION_MOVE: y = " + event.getY());
                if (Math.abs(mTouchDownX - event.getX()) * 1.3 > Math.abs(mTouchDownY - event.getY())) {
                    attemptClaimDrag();
                } else {
                    attemptDisclaimDrag();
                }
                break;
            case MotionEvent.ACTION_UP:
//                Log.i(TAG, "ACTION_UP: x = " + event.getX());
//                Log.i(TAG, "ACTION_UP: y = " + event.getY());
                if (mAnimator != null) {
                    mWheelAnimator.start();
                    if (event.getX() - mTouchDownX > mLongCAL) {
                        pointLeft();
                    } else if (event.getX() - mTouchDownX < -mLongCAL) {
                        pointRight();
                    }
                }
                attemptClaimDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
//                Log.i(TAG, "ACTION_UP: x = " + event.getX());
//                Log.i(TAG, "ACTION_UP: y = " + event.getY());
                break;
        }
        return true;
    }

    private void attemptDisclaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

}
