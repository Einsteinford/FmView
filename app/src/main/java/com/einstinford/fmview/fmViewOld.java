package com.einstinford.fmview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by kk on 17-9-26.
 */

public class fmViewOld extends View {

    private static final String TAG = "kkTest";
    private Paint mPaintPointer;
    //    private Paint mPaintShade;
    private Paint mPaintCAL;
    float progress = 0;
    float DEFAULT_DISTANCE = 50;
    //    private Shader mShaderShade;
//    private Matrix mMatrixShade;
    private Shader mShaderPointer;
    private Matrix mMatrixPointer;
    private Path mPathCAL;


    public fmViewOld(Context context) {
        this(context, null);
    }

    public fmViewOld(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public fmViewOld(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        mPaintPointer = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPointer.setShadowLayer(8,5,0,Color.parseColor("#9C000000"));
        mPaintPointer.setStrokeWidth(8);
        mShaderPointer = new LinearGradient(DEFAULT_DISTANCE - 2, 100, DEFAULT_DISTANCE + 3, 100, Color.parseColor("#ffa9a4"),
                Color.parseColor("#ca5351"), Shader.TileMode.CLAMP);
        mPaintPointer.setShader(mShaderPointer);


//        mPaintShade = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaintShade.setStrokeWidth(10);
//        mPaintShade.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
//        mShaderShade = new LinearGradient(DEFAULT_DISTANCE + 9, 100, DEFAULT_DISTANCE + 18, 100, Color.parseColor("#E6D2C9"),
//                Color.parseColor("#F4F5E7"), Shader.TileMode.CLAMP);
//        mPaintShade.setShader(mShaderShade);

        mPaintCAL = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCAL.setStrokeWidth(5);
        mPaintCAL.setStyle(Paint.Style.STROKE);
//        mPaintCAL.setColor(Color.parseColor("#FF4081"));
        mPaintCAL.setColor(Color.parseColor("#DFD5CC"));
        mPathCAL = new Path();
        int space = 50;
        int LongCAL = (TangramViewMetrics.screenWidth() - space / 2) / 10;
        int ShortCAL = (TangramViewMetrics.screenWidth() - space / 2) / 37;

        for (int i = 0; i < 10; i++) {
            mPathCAL.moveTo(space + LongCAL * i, 0);
            mPathCAL.lineTo(space + LongCAL * i, 100);
            if (i!=9){
                for (int j = 1; j < 4; j++) {
                    mPathCAL.moveTo(space + LongCAL * i + ShortCAL * j, 0);
                    mPathCAL.lineTo(space + LongCAL * i + ShortCAL * j, 60);
                }
            }
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPathCAL, mPaintCAL);

        if (mMatrixPointer != null) {
            mMatrixPointer.setTranslate(progress, 0);
            mShaderPointer.setLocalMatrix(mMatrixPointer);
        } else {
            mMatrixPointer = new Matrix();
        }
        canvas.drawLine(DEFAULT_DISTANCE + progress, 0, DEFAULT_DISTANCE + progress, 300, mPaintPointer);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i(TAG, "widthMeasureSpec: " + TangramViewMetrics.screenWidth());
        Log.i(TAG, "heightMeasureSpec: " + TangramViewMetrics.screenHeight());
        setMeasuredDimension(TangramViewMetrics.screenWidth(), 350); //填写屏幕像素，高度由宽度乘以系数
    }
}
