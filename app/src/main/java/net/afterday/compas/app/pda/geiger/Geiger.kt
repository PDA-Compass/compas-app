package net.afterday.compas.app.pda.geiger

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import net.afterday.compas.app.R
import net.afterday.compas.engine.util.Convert
import kotlin.math.max

open class Geiger : View {
    private val TAG = "Geiger"
    private val WIDGET_WIDTH = 1010
    private val WIDGET_HEIGHT = 1010

    private var tSvh = 0.0f //Target SVH
    // Dimension stuff
    private var mWidth = 0
    private var mHeight = 0
    private val level = 3

    private var mScaleFactorX = 0f
    private var mScaleFactorY = 0f

    // Input stuff
    private var mSvh = 0f
    private var mMental = 0.0f
    private var mMonolith = 0f
    private var mAnomaly = 0f


    // Bitmaps
    private val mScale: Bitmap
    private val scaleLvl5: Bitmap? = null
    private val mIndicator: Bitmap
    private val mFrontSide: Bitmap
    private val brokenGlass: Bitmap
    private val fingerPrint: Bitmap

    private val mPeakOff: Bitmap
    private val mPeakOn: Bitmap

    private val mBulbBack: Bitmap
    private val mBulbAnomaly: Bitmap
    private val mBulbMental: Bitmap

    // Paint stuff
    private val mPaint: Paint
    private val mMatrix: Matrix
    private val mRect: RectF

    // Animation stuff
    private var mSvhAnimator: ValueAnimator

    private val isBroken = false
    private val hasFingerPrint = false

    private val geigerService = GeigerService(this, context)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mScale = BitmapFactory.decodeResource(resources, R.drawable.scale)
        //scaleLvl5 = BitmapFactory.decodeResource(getResources(), R.drawable.scale_lvl5);
        //scaleLvl5 = BitmapFactory.decodeResource(getResources(), R.drawable.scale_lvl5);
        mIndicator = BitmapFactory.decodeResource(resources, R.drawable.indicator)
        mFrontSide = BitmapFactory.decodeResource(resources, R.drawable.geiger_top)
        brokenGlass = BitmapFactory.decodeResource(resources, R.drawable.broken_glass)
        fingerPrint = BitmapFactory.decodeResource(resources, R.drawable.fingerprint)
        mPeakOff = BitmapFactory.decodeResource(resources, R.drawable.peak_off)
        mPeakOn = BitmapFactory.decodeResource(resources, R.drawable.peak_on)

        mBulbBack = BitmapFactory.decodeResource(resources, R.drawable.bulb_back)
        mBulbAnomaly = BitmapFactory.decodeResource(resources, R.drawable.bulb_anomaly)
        mBulbMental = BitmapFactory.decodeResource(resources, R.drawable.bulb_mental)

        mMatrix = Matrix()
        mRect = RectF()
        mPaint = Paint()

        mSvhAnimator = ValueAnimator.ofFloat(0f, 1f)
    }

    fun mental(mental:Float) {
        if (mental == mMental) {
            return;
        }

        mMental = mental;
        invalidate();
    }

    fun anomaly(anomaly:Float) {
        if (anomaly == mAnomaly) {
            return;
        }

        mAnomaly = anomaly;
        invalidate();
    }

    fun monolith(monolith:Float) {
        if (monolith == mMonolith) {
            return;
        }
        mMonolith = monolith;
        invalidate();
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Get sizes
        val widthSize =  Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))

        val finalMeasureSpecX = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        val finalMeasureSpecY = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        super.onMeasure(finalMeasureSpecX, finalMeasureSpecY)
        mWidth = widthSize
        mScaleFactorX = mWidth.toFloat() / WIDGET_WIDTH
        mScaleFactorY = mWidth.toFloat() / WIDGET_HEIGHT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //this.bringToFront();
        drawBack(canvas)
        drawPeak(canvas)
        drawIndicator(canvas)
        drawFront(canvas)
    }

    fun toSvh(svh: Float, duration: Long) {
        if (svh == tSvh) {
            return
        }
        tSvh = svh
        mSvhAnimator.cancel()
        mSvhAnimator = ValueAnimator.ofFloat(mSvh, svh.toFloat())
        mSvhAnimator.interpolator = AnticipateOvershootInterpolator()
        mSvhAnimator.duration = duration
        mSvhAnimator.addUpdateListener {
            mSvh = mSvhAnimator.animatedValue as Float
            postInvalidate()
        }
        mSvhAnimator.start()
    }

    protected open fun drawBack(canvas: Canvas) {
        convertRect(31, 31, mMatrix)
        canvas.drawBitmap(mScale, mMatrix, null)
        //convertRect(scaleLvl5.getWidth(), scaleLvl5.getHeight(), 0, 100, mMatrix);
        //canvas.drawBitmap(scaleLvl5, mMatrix, null);
        if (level >= 2) drawAnomaly(canvas)
        if (level >= 3) drawMental(canvas)
        if (hasFingerPrint) {
            convertRect(-35, 210, mMatrix)
            canvas.drawBitmap(fingerPrint, mMatrix, null)
        }
        if (isBroken) {
            convertRect(170, 100, mMatrix)
            canvas.drawBitmap(brokenGlass, mMatrix, null)
        }
    }

    protected open fun drawIndicator(canvas: Canvas) {
        convertRect(487, 350, mMatrix)
        mMatrix.postRotate(svhToRotation(mSvh), 505 * mScaleFactorX, 1010 * mScaleFactorY)
        canvas.drawBitmap(mIndicator, mMatrix, null)
    }

    protected open fun drawFront(canvas: Canvas) {
        convertRect(0, 0, mMatrix)
        canvas.drawBitmap(mFrontSide, mMatrix, null)
    }

    protected open fun drawAnomaly(canvas: Canvas) {
        convertRect(281, 115, mMatrix)
        canvas.drawBitmap(mBulbBack, mMatrix, null)
        drawRect(151, 151, 286, 120, mRect)
        val color = if (mAnomaly <= 0f) Convert.numberToRGB(Convert.RGB_GREY.toDouble()) else Convert.numberToRGB(Convert.map(mAnomaly, 0f, 15f, 100f, 0f).toDouble())
        if (mAnomaly >= 15f) {
            color[1] = 255
            color[2] = 0
            color[3] = 0
        }
        mPaint.setARGB(255, 255, 255, 255)
        mPaint.setARGB(255, color[1], color[2], color[3])
        canvas.drawOval(mRect, mPaint)
        convertRect(281, 115, mMatrix)
        canvas.drawBitmap(mBulbAnomaly, mMatrix, null)
    }

    protected open fun drawMental(canvas: Canvas) {
        convertRect(572, 115, mMatrix)
        canvas.drawBitmap(mBulbBack, mMatrix, null)
        drawRect(151, 151, 577, 120, mRect)
        val color = if (mMental <= 0f) Convert.numberToRGB(Convert.RGB_GREY.toDouble()) else Convert.numberToRGB(Convert.map(mMental, 0f, 15f, 100f, 0f).toDouble())
        if (mMental >= 15f) {
            color[1] = 255
            color[2] = 0
            color[3] = 0
        }
        mPaint.setARGB(255, color[1], color[2], color[3])
        canvas.drawOval(mRect, mPaint)
        convertRect(572, 115, mMatrix)
        canvas.drawBitmap(mBulbMental, mMatrix, null)
    }

    protected open fun drawPeak(canvas: Canvas) {
        convertRect(369, 60, mMatrix)
        if (mSvh >= 15) {
            canvas.drawBitmap(mPeakOn, mMatrix, null)
        } else {
            canvas.drawBitmap(mPeakOff, mMatrix, null)
        }
    }

    private fun convertRect(left: Int, top: Int, matrix: Matrix) {
        matrix.reset()
        matrix.postScale(mScaleFactorX, mScaleFactorY)
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top)
    }

    private fun svhToRotation(svh: Float): Float {
        when {
            svh >= 0f && svh < 1f -> {
                return Convert.map(svh, 0f, 1f, -37f, -20f)
            }
            svh >= 1f && svh < 7f -> {
                return Convert.map(svh, 1f, 7f, -20f, 10f)
            }
            svh >= 7f && svh < 9f -> {
                return Convert.map(svh, 7f, 9f, 10f, 21f)
            }
            svh >= 9f && svh < 15f -> {
                return Convert.map(svh, 9f, 15f, 21f, 37f)
            }
            svh >= 15f -> {
                return 37f
            }
            else -> return -37f
        }
    }

    private fun drawRect(width: Int, height: Int, left: Int, top: Int, rect: RectF) {
        rect[left * mScaleFactorX, top * mScaleFactorY, (left + width) * mScaleFactorX] = (top + height) * mScaleFactorY
    }
}