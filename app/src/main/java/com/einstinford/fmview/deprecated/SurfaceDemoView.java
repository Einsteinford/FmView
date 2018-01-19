package com.einstinford.fmview.deprecated;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by kk on 2018/1/9.
 */

public class SurfaceDemoView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    public SurfaceDemoView(Context context) {
        this(context, null);
    }

    public SurfaceDemoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {

    }

    private void init() {

    }
}
