package com.einstinford.fmview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kk on 17-9-26.
 */

public class SportsView extends View {
    float progress = 0;
    private RectF arcRectF;
    private Paint paint;

    public SportsView(Context context) {
        this(context, null);
    }

    public SportsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SportsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.parseColor("#FF9800"));
        paint.setStrokeWidth(30);
        arcRectF = new RectF(30,30,300,300);
    }
//    ......

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
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        ......

        canvas.drawArc(arcRectF, 135, progress * 2.7f, false, paint);

//        ......
    }
}

