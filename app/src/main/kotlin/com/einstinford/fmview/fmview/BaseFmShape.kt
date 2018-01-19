package com.einstinford.fmview.fmview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface


/**
 * @author kk
 * @date 2017/12/26
 */

abstract class BaseFmShape {
    /**
     * 用于组件自己根据自己的需求进行绘制
     *
     * @param canvas
     */
    abstract fun draw(canvas: Canvas)
}

class FmBitmap(private val mBitmap: Bitmap) : BaseFmShape() {
    private val mBitmapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
    }
}

class FmScale(screenWidth: Int, strokeWidth: Int) : BaseFmShape() {
    private val mPaintScale: Paint
    private val mPathScale: Path

    init {
        val defaultDistance = (screenWidth * 8 / 75).toFloat()    //默认刻度两侧边距;
        val mLongScale = (screenWidth - defaultDistance * 2) / 9
        val mDefaultTop = screenWidth * 0.067f
        val shortScale = (screenWidth - defaultDistance * 2) / 36

        mPaintScale = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintScale.strokeWidth = strokeWidth.toFloat()
        mPaintScale.style = Paint.Style.STROKE
        mPaintScale.color = Color.parseColor("#DFD5CC")
        mPathScale = Path()

        for (i in 0..9) {
            mPathScale.moveTo(defaultDistance + mLongScale * i, mDefaultTop)
            mPathScale.lineTo(defaultDistance + mLongScale * i, screenWidth * 0.18f)
            if (i != 9) {
                for (j in 1..3) {
                    mPathScale.moveTo(defaultDistance + mLongScale * i + shortScale * j, mDefaultTop)
                    mPathScale.lineTo(defaultDistance + mLongScale * i + shortScale * j, screenWidth * 0.13f)
                }
            }
        }

    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(mPathScale, mPaintScale)
    }
}

class FmText(private val x: Float, private val y: Float, private val textSize: Int, private val redTextSize: Int, var redTextType: Typeface) : BaseFmShape() {
    var status: Int = 0
    var text: String? = null
    private val normal = Color.parseColor("#909aa9")
    private val textPaint: Paint
    private val red = Color.parseColor("#F27D73")

    init {
        this.status = STATUS_OFF

        this.textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.isFakeBoldText = true
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {
        if (text != null) {
            if (this.status == STATUS_OFF) {
                textPaint.color = normal
                textPaint.textSize = textSize.toFloat()
                textPaint.typeface = Typeface.DEFAULT
            } else {
                textPaint.color = red
                textPaint.textSize = redTextSize.toFloat()
                textPaint.typeface = redTextType
            }
            canvas.drawText(text!!, x, y, textPaint)
        }
    }

    companion object {

        /**
         * 默认色
         */
        val STATUS_OFF = 0
        /**
         * 红色变大字体
         */
        val STATUS_ON = 1
    }
}