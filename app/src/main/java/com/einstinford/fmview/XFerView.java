package com.einstinford.fmview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kk on 17-9-28.
 */

public class XFerView extends View {
    private Paint mPaintShade;
    private Paint mPaintCAL;
    private Shader mCALShade;
    private Shader mShaderShade;
    private Matrix mMatrixShade;
    private Path mPathCAL;
    float progress = 0;
    float DEFAULT_DISTANCE = 50;

    public XFerView(Context context) {
        this(context, null);
    }

    public XFerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XFerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mPaintShade = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintShade.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        mPaintShade.setStyle(Paint.Style.STROKE);


        mShaderShade = new LinearGradient(DEFAULT_DISTANCE + 9, 100, DEFAULT_DISTANCE + 18, 100, Color.parseColor("#E6D2C9"),
                Color.parseColor("#F4F5E7"), Shader.TileMode.CLAMP);

        mCALShade = new LinearGradient(0, 60, TangramViewMetrics.screenWidth(), 60, Color.parseColor("#DFD5CC"),
                Color.parseColor("#DFD5CC"), Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(mShaderShade, mCALShade, PorterDuff.Mode.DST);
        mPaintShade.setShader(composeShader);

//        mPaintCAL = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaintCAL.setStrokeWidth(6);
//        mPaintCAL.setStyle(Paint.Style.STROKE);
//        mPaintCAL.setColor(Color.parseColor("#FF4081"));
////        mPaintCAL.setColor(Color.parseColor("#DFD5CC"));
//        mPathCAL = new Path();
//        int space = 50;
//        int LongCAL = (TangramViewMetrics.screenWidth() - space / 2) / 10;
//        int ShortCAL = (TangramViewMetrics.screenWidth() - space / 2) / 37;
//
//        for (int i = 0; i < 10; i++) {
//            mPathCAL.moveTo(space + LongCAL * i, 0);
//            mPathCAL.lineTo(space + LongCAL * i, 100);
//            if (i != 9) {
//                for (int j = 1; j < 4; j++) {
//                    mPathCAL.moveTo(space + LongCAL * i + ShortCAL * j, 0);
//                    mPathCAL.lineTo(space + LongCAL * i + ShortCAL * j, 60);
//                }
//            }
//        }
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
        int saved = canvas.saveLayer(null, mPaintShade, Canvas.ALL_SAVE_FLAG);
        mPaintShade.setStrokeWidth(6);
//        mPaintShade.setColor(Color.parseColor("#FF4081"));
        canvas.drawRect(0, 0, 200, 200, mPaintShade);
//        canvas.drawPath(mPathCAL, mPaintShade);
//        if (mMatrixShade != null) {
//            mMatrixShade.setTranslate(progress, 0);
//            mShaderShade.setLocalMatrix(mMatrixShade);
//        } else {
//            mMatrixShade = new Matrix();
//        }
//        canvas.drawLine(DEFAULT_DISTANCE + progress + 9, 0, DEFAULT_DISTANCE + progress + 9, 300, mPaintShade);
        mPaintShade.setStrokeWidth(20);
//        mPaintShade.setColor(Color.parseColor("#303F9F"));
        canvas.drawCircle(100, 100, 100, mPaintShade);
        canvas.restoreToCount(saved);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(TangramViewMetrics.screenWidth(), 350); //填写屏幕像素，高度由宽度乘以系数
    }
}
