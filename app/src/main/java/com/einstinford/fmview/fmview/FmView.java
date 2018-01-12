package com.einstinford.fmview.fmview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

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

    public static int ViewHeight = BaseData.ScreenWidth * 129 / 216;

    private float mTouchDownX;
    private float mTouchDownY;

    private float defaultDistance = BaseData.ScreenWidth * 8 / 75;    //默认刻度两侧边距;
    private float mLongScale = (BaseData.ScreenWidth - (defaultDistance * 2)) / 9;

    private Context cxt;
    public static int sFmPosition;
    private Vector<BaseFmShape> mShapes;
    private BaseFmAnim mWheel;
    private BaseFmAnim mPointer;
    private FmText[] mFmTexts;
    private int mTextMaxSize = 10;

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


    /**
     * 初始化Path，Paint，Bitmap等绘制数据
     */
    private void init() {
        //关闭硬件加速
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        setBackgroundResource(R.drawable.view_fm_bg);
//        setBackgroundColor(0x00000000);

        /*储存所需绘制的图形组件*/
        mShapes = new Vector<>();

        initScale();

        initWheel();

        initRadioText();

        initPointer();

        initFontBg();

    }

    private void initPointer() {

        Paint mPaintPointer = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPointer.setShadowLayer(cxt.getResources().getDimensionPixelSize(R.dimen.base_2),
                cxt.getResources().getDimensionPixelSize(R.dimen.base_3),
                0, Color.parseColor("#9C000000"));   //设置阴影
        mPaintPointer.setStrokeWidth(cxt.getResources().getDimensionPixelSize(R.dimen.base_3));   //粗细

        /*渐变上色*/
        Shader mShaderPointer = new LinearGradient(defaultDistance - cxt.getResources().getDimensionPixelSize(R.dimen.base_1),
                cxt.getResources().getDimensionPixelSize(R.dimen.base_50),
                defaultDistance + cxt.getResources().getDimensionPixelSize(R.dimen.base_1_5),
                cxt.getResources().getDimensionPixelSize(R.dimen.base_50)
                , Color.parseColor("#ffa9a4"),
                Color.parseColor("#ca5351"), Shader.TileMode.CLAMP);

        mPointer = new FmPointer(mPaintPointer, mShaderPointer, BaseData.ScreenWidth, sFmPosition);
        mShapes.add(mPointer);
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
     * 新建10个字，每一个字单独绘画
     */
    private void initRadioText() {
        float textY = BaseData.ScreenWidth * 0.26f;

        mFmTexts = new FmText[mTextMaxSize];
        for (int i = 0; i < mTextMaxSize; i++) {
            mFmTexts[i] = new FmText(defaultDistance + mLongScale * i, textY,
                    cxt.getResources().getDimensionPixelSize(R.dimen.base_text_size_11),
                    cxt.getResources().getDimensionPixelSize(R.dimen.base_text_size_18),
                    Typeface.createFromAsset(getContext().getAssets(), "Ubuntu-C.ttf"));
        }
        mFmTexts[sFmPosition].setStatus(FmText.Companion.getSTATUS_ON());
        Collections.addAll(mShapes, mFmTexts);
    }

    private void initFontBg() {
        BaseFmShape fontBitmap = new FmBitmap(
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.view_fm_front),
                        BaseData.ScreenWidth, BaseData.ScreenWidth * 37 / 108, true));
        mShapes.add(fontBitmap);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (BaseFmShape shape : mShapes) {
            shape.draw(canvas);
        }
    }

    public void setFmList(@NonNull List<FmInfo> fmList) {
        mFmList = fmList;
        if (mFmTexts != null) {
            for (int i = 0; i < (mFmList.size() > mTextMaxSize ? mTextMaxSize : mFmList.size()); i++) {
                mFmTexts[i].setText(mFmList.get(i).getMhz());
            }
        }
    }

    public void beforePosChange() {
        mFmTexts[sFmPosition].setStatus(FmText.Companion.getSTATUS_OFF());
    }

    private void positionChanged() {
        EventBus.getDefault().post(new EventManager.FmChannelChangeEvent());
        mFmTexts[sFmPosition].setStatus(FmText.Companion.getSTATUS_ON());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                if (mFmList != null) {
                    mWheel.startAnim(BaseFmAnim.Direction.LEFT, 0);
                    beforePosChange();
                    if (event.getX() - mTouchDownX > mLongScale / 2) {
                        mPointer.startAnim(BaseFmAnim.Direction.RIGHT, mFmList.size() - 1);
                    } else if (event.getX() - mTouchDownX < -mLongScale / 2) {
                        mPointer.startAnim(BaseFmAnim.Direction.LEFT, mFmList.size() - 1);
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

}
