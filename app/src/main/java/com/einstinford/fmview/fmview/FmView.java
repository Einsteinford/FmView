package com.einstinford.fmview.fmview;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.einstinford.fmview.BaseData;
import com.einstinford.fmview.EventManager;
import com.einstinford.fmview.R;
import com.einstinford.fmview.bean.FmInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;
import java.util.Vector;


/**
 * FmView class
 *
 * @author kk
 * @date 2017/09/26
 */
public class FmView extends SurfaceView {

    private static final String TAG = "kkTest";

    private List<FmInfo> mFmList;

    private Dialog mDialog;
    private String mOldDetail;
    private String mOldList;
//    private Button mBtnRadio;
//    private Button mBtnSwitch;
//    private TextView mTvTitle;
//    private TextView mTvDes;


    private Paint mPaintPointer;
//    private Paint mPaintScale;

//    private Path mPathScale;

    private Shader mShaderPointer;

    private Matrix mMatrixPointer;

    //    private ObjectAnimator mWheelAnimator;
    private ObjectAnimator mAnimator;
    private OvershootInterpolator mInterpolator;
    private AccelerateDecelerateInterpolator mTempInterpolator;

    private int ViewHeight = BaseData.ScreenWidth * 129 / 216;

    private float progress = 0;
    private float mTouchDownX;
    private float mTouchDownY;
    private float mPointStartX;
    private float mPointEndX;
    private float defaultDistance = BaseData.ScreenWidth * 8 / 75;    //默认刻度两侧边距;
    private float mLongScale = (BaseData.ScreenWidth - (defaultDistance * 2)) / 9;
    private float mDefaultTop = BaseData.ScreenWidth * 0.067f;

    private Context cxt;
    //TODO  修改为观察者模式，最好有before和after两种状态
    public static int sFmPosition;
    private Vector<BaseFmShape> mShapes;
    private BaseFmAnim mWheel;

    public FmView(Context context) {
        this(context, null);
    }

    public FmView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FmView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.cxt = context;
        init();
    }

    public void setFmList(@NonNull List<FmInfo> fmList) {
        mFmList = fmList;

//        交由外部处理点击
//        mBtnSwitch.setOnClickListener(this);

        if (mDialog == null) {
//            mDialog = new AbstractFmDialog(getContext(), audioNames) {
//                @Override
//                public void onItemClick(View convertView, int position) {
//                    EventBus.getDefault().post(
//                            new EventManager.FmAreaIdChangeEvent(fmAreaIds.get(position), mBtnRadio.getText().toString()));
//                    mBtnRadio.setText(audioNames.get(position));
//                    mDialog.dismiss();
//                }
//            };
//            Window dialogWindow = mDialog.getWindow();
//            if (dialogWindow != null) {
//                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                dialogWindow.setGravity(Gravity.LEFT);
//                lp.x = getResources().getDimensionPixelSize(R.dimen.base_20); // 新位置X坐标
////                lp.y = 100; // 新位置Y坐标
//                dialogWindow.setAttributes(lp);
//            }
        }
        if (sFmPosition < mFmList.size()) {
//            mTvTitle.setText(mFmList.get(sFmPosition).title);
//            mTvDes.setText(mFmList.get(sFmPosition).shortdesc);
        } else {
            pointToZero();
        }
//        invalidate();
    }

//    public void setAreaName(String AreaName) {
//        mBtnRadio.setText(AreaName);
//    }

    private void positionChanged() {
        EventBus.getDefault().post(new EventManager.FmChannelChangeEvent());
        if (!mFmList.isEmpty()) {
//            mTvTitle.setText(mFmList.get(sFmPosition).title);
//            mTvDes.setText(mFmList.get(sFmPosition).shortdesc);
        }
    }

    /**
     * 初始化Path，Paint，Bitmap等绘制数据
     */
    private void init() {
        //关闭硬件加速
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

//        getHolder().addCallback(callback);

        setBackgroundResource(R.drawable.view_fm_bg);
//        setBackgroundColor(getResources().getColor(android.R.color.transparent));

        /*作为指针动画的变量*/
        progress = sFmPosition * mLongScale;
        /*指针的左侧坐标*/
        mPointStartX = sFmPosition * mLongScale;
        /*指针的右侧坐标*/
        mPointEndX = mPointStartX + mLongScale;
        /*储存所需绘制的图形组件*/
        mShapes = new Vector<>();

        initPointer();

        initScale();

        initWheel();

        initRadioText();

        initFontBg();

        initBtnSwitch();

        initBtnRadio();

        initTvTitle();

        initTvDes();

    }

    private void initPointer() {
        mAnimator = ObjectAnimator.ofFloat(this, "progress", 0);
        mAnimator.setDuration(450);
        //普通动画效果
        mInterpolator = new OvershootInterpolator();
        //超过刻度的动画
        mTempInterpolator = new AccelerateDecelerateInterpolator();

        mPaintPointer = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPointer.setShadowLayer(cxt.getResources().getDimensionPixelSize(R.dimen.base_2),
                cxt.getResources().getDimensionPixelSize(R.dimen.base_3),
                0, Color.parseColor("#9C000000"));   //设置阴影
        mPaintPointer.setStrokeWidth(cxt.getResources().getDimensionPixelSize(R.dimen.base_3));   //粗细
        mShaderPointer = new LinearGradient(defaultDistance - cxt.getResources().getDimensionPixelSize(R.dimen.base_1),
                cxt.getResources().getDimensionPixelSize(R.dimen.base_50),
                defaultDistance + cxt.getResources().getDimensionPixelSize(R.dimen.base_1_5),
                cxt.getResources().getDimensionPixelSize(R.dimen.base_50)
                , Color.parseColor("#ffa9a4"),
                Color.parseColor("#ca5351"), Shader.TileMode.CLAMP);    //渐变上色
        mPaintPointer.setShader(mShaderPointer);
    }

    private void initScale() {
        BaseFmShape fmScale = new FmScale(BaseData.ScreenWidth, cxt.getResources().getDimensionPixelSize(R.dimen.base_2_5));
        mShapes.add(fmScale);
    }

    private void initWheel() {
        mWheel = new FmWheel(cxt.getResources().getDimensionPixelSize(R.dimen.base_1), BaseData.ScreenWidth);
        mShapes.add(mWheel);
    }

    /**
     * 新建10个刻度，每一个刻度单独绘画
     */
    private void initRadioText() {
        float textY = BaseData.ScreenWidth * 0.26f;
        int textSize = 10;
        FmText[] fmTexts = new FmText[textSize];
        for (int i = 0; i < textSize; i++) {
            fmTexts[i] = new FmText(defaultDistance + mLongScale * i, textY,
                    cxt.getResources().getDimensionPixelSize(R.dimen.base_text_size_11),
                    cxt.getResources().getDimensionPixelSize(R.dimen.base_text_size_18),
                    Typeface.createFromAsset(getContext().getAssets(), "Ubuntu-C.ttf"));
        }
        Collections.addAll(mShapes, fmTexts);
    }

    private void initFontBg() {
        BaseFmShape fontBitmap = new FmBitmap(
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.view_fm_front),
                        BaseData.ScreenWidth, BaseData.ScreenWidth * 37 / 108, true));
        mShapes.add(fontBitmap);
    }

    private void initBtnSwitch() {
//        mBtnSwitch = new Button(cxt);
//        mBtnSwitch.setBackgroundResource(R.drawable.view_fm_power_on_bg);
//        LayoutParams switchLp = new LayoutParams(cxt.getResources().getDimensionPixelSize(R.dimen.base_42),
//                cxt.getResources().getDimensionPixelSize(R.dimen.base_53), Gravity.BOTTOM | Gravity.END);
//        switchLp.bottomMargin = cxt.getResources().getDimensionPixelSize(R.dimen.base_28);
//        switchLp.rightMargin = cxt.getResources().getDimensionPixelSize(R.dimen.base_20);
//        addView(mBtnSwitch, switchLp);
    }

    private void initBtnRadio() {
//        mBtnRadio = new Button(cxt);
//        mBtnRadio.setBackgroundResource(R.drawable.view_fm_radio_bg);
//        Log.i(TAG, "initBtnRadio: Dimension" + cxt.getResources().getDimension(R.dimen.base_text_size_6));
//        Log.i(TAG, "initBtnRadio: DimensionPixelSize" + cxt.getResources().getDimensionPixelSize(R.dimen.base_text_size_6));
//        mBtnRadio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//        mBtnRadio.setText("省台");
//        mBtnRadio.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDialog != null) {
//                    mDialog.show();
//                }
//            }
//        });
//        Drawable drawableR = getResources().getDrawable(R.drawable.icon_arrow_down);
//        drawableR.setBounds(-cxt.getResources().getDimensionPixelSize(R.dimen.base_9), 0, 0,
//                cxt.getResources().getDimensionPixelSize(R.dimen.base_6));
//        mBtnRadio.setCompoundDrawables(null, null, drawableR, null);
//        LayoutParams radioLp =
//                new LayoutParams(cxt.getResources().getDimensionPixelSize(R.dimen.base_70),
//                        cxt.getResources().getDimensionPixelSize(R.dimen.base_42), Gravity.BOTTOM | Gravity.START);
//        radioLp.bottomMargin = cxt.getResources().getDimensionPixelSize(R.dimen.base_35);
//        radioLp.leftMargin = cxt.getResources().getDimensionPixelSize(R.dimen.base_10);
//        addView(mBtnRadio, radioLp);
    }

    private void initTvTitle() {
//        mTvTitle = new TextView(cxt);
//        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//        mTvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//        mTvTitle.setTextColor(Color.parseColor("#FFFFFF"));
//        LayoutParams titleLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
//        titleLp.topMargin = cxt.getResources().getDimensionPixelSize(R.dimen.base_28);
//        addView(mTvTitle, titleLp);
    }

    private void initTvDes() {
//        mTvDes = new TextView(cxt);
//        mTvDes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//        mTvDes.setTextColor(Color.parseColor("#FFFFFF"));
//        LayoutParams desLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
//        desLp.topMargin = cxt.getResources().getDimensionPixelSize(R.dimen.base_47);
//        addView(mTvDes, desLp);
    }

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            redraw();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//            WIDTH = i1 / (COL + 2);   //根据实际设备屏幕宽度，重新修改圆圈大小
            redraw();       //刷新页面
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    public void redraw() {
        //锁定画图
//        Canvas c = getHolder().lockCanvas();
        //结束锁定画图，并提交改变。
//        getHolder().unlockCanvasAndPost(c);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaintPointer != null) {
//            if (mFmList != null) {
//                for (int i = 0; i < mFmList.size(); i++) {
//                    if (i == sFmPosition) {
//                        canvas.drawText(mFmList.get(i).mhz,
//                                defaultDistance + mLongScale * i, BaseData.ScreenWidth * 0.26f,
//                                mRedTextPaint);
//                    } else {
//                        canvas.drawText(mFmList.get(i).mhz,
//                                defaultDistance + mLongScale * i, BaseData.ScreenWidth * 0.26f,
//                                mTextPaint);
//                    }
//
//                }
//            }
            for (BaseFmShape shape : mShapes) {
                shape.draw(canvas);
            }
            if (mMatrixPointer != null) {
                mMatrixPointer.setTranslate(progress, 0);
                mShaderPointer.setLocalMatrix(mMatrixPointer);
            } else {
                mMatrixPointer = new Matrix();
            }

            canvas.drawLine(defaultDistance + progress, mDefaultTop,
                    defaultDistance + progress, BaseData.ScreenWidth * 0.33f, mPaintPointer);


        }
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }


    private void pointLeft(int maxPos) {
        if (sFmPosition == maxPos) {
            mPointStartX -= mLongScale;
            mPointEndX = maxPos * mLongScale;
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointEndX, mPointStartX);
            mAnimator.start();
            sFmPosition = maxPos - 1;
        } else if (sFmPosition > 0) {
            mPointStartX -= mLongScale;
            mPointEndX -= mLongScale;
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointEndX, mPointStartX);
            mAnimator.start();
            sFmPosition--;
        }
        positionChanged();
    }

    private void pointRight(int maxPos) {
        if (sFmPosition == 0) {
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointStartX, mPointEndX);
            mAnimator.start();
            mPointStartX += mLongScale;
            mPointEndX += mLongScale;
            sFmPosition = 1;
        } else if (sFmPosition < maxPos) {
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.setFloatValues(mPointStartX, mPointEndX);
            mAnimator.start();
            mPointStartX += mLongScale;
            mPointEndX += mLongScale;
            sFmPosition++;
        }
        positionChanged();
    }

    private void pointToZero() {
        mPointEndX = 0;
        mAnimator.setInterpolator(mTempInterpolator);
        mAnimator.setFloatValues(mPointStartX, mPointEndX);
        mAnimator.start();
        mPointEndX = mLongScale;
        mPointStartX = 0;
        sFmPosition = 0;
        positionChanged();
    }

    private void pointToMax(int maxPos) {
        mPointEndX = 0;
        mPointStartX = maxPos * mLongScale;
        mAnimator.setInterpolator(mTempInterpolator);
        mAnimator.setFloatValues(mPointEndX, mPointStartX);
        mAnimator.start();
        sFmPosition = maxPos;
        positionChanged();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {    //TODO 由于处理了此事件，导致TKrefresh控件速度缓慢，待处理
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownY = event.getY();
                mTouchDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mTouchDownX - event.getX()) * 1.3 > Math.abs(mTouchDownY - event.getY())) {
                    attemptClaimDrag();
                } else {
                    attemptDisclaimDrag();
                }
                break;
            case MotionEvent.ACTION_UP:
                redraw();       //图像刷新
                if (mAnimator != null && mFmList != null) {
                    mWheel.startAnim();
                    if (event.getX() - mTouchDownX > mLongScale / 2) {
                        if (sFmPosition == mFmList.size() - 1) {
                            pointToZero();
                        } else {
                            pointRight(mFmList.size() - 1);
                        }
                    } else if (event.getX() - mTouchDownX < -mLongScale / 2) {
                        if (sFmPosition == 0) {
                            pointToMax(mFmList.size() - 1);
                        } else {
                            pointLeft(mFmList.size() - 1);
                        }
                    }
                }
                attemptClaimDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(BaseData.ScreenWidth, ViewHeight); //填写屏幕像素，高度由宽度乘以系数
    }
}
