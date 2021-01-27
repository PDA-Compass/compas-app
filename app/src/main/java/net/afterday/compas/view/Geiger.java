package net.afterday.compas.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import net.afterday.compas.R;
import net.afterday.compas.util.Convert;

public class Geiger extends View
{
    private static final String TAG = "Geiger";

    private static final int WIDGET_WIDTH = 1010;
    private static final int WIDGET_HEIGHT = 1010;
    private float tSvh; //Target SVH

    // Input stuff
    private float mSvh;
    private float mMental;
    private float mMonolith;
    private float mAnomaly;
    private float mSpringboard;
    private float mFunnel;
    private float mCarousel;
    private float mElevator;
    private float mFrying;
    private float mElectra;
    private float mMeatgrinder;
    private float mKissel;
    private float mSoda;
    private float mAcidfog;
    private float mBurningfluff;
    private float mRustyhair;
    private float mSpatialbubble;

    // Dimension stuff
    private int mWidth;
    private int mHeight;
    private int level;

    private float mScaleFactorX;
    private float mScaleFactorY;

    // Paint stuff
    private Paint mPaint;
    private Matrix mMatrix;
    private RectF mRect;

    // Bitmaps
    private Bitmap mScale;
    private Bitmap scaleLvl5;
    private Bitmap mIndicator;
    private Bitmap mFrontSide;
    private Bitmap brokenGlass;
    private Bitmap fingerPrint;

    private Bitmap mPeakOff;
    private Bitmap mPeakOn;

    private Bitmap mBulbBack;
    private Bitmap mBulbAnomaly;
    private Bitmap mBulbSpringboard;
    private Bitmap mBulbFunnel;
    private Bitmap mBulbCarousel;
    private Bitmap mBulbElevator;
    private Bitmap mBulbFrying;
    private Bitmap mBulbElectra;
    private Bitmap mBulbMeatgrinder;
    private Bitmap mBulbKissel;
    private Bitmap mBulbSoda;
    private Bitmap mBulbAcidfog;
    private Bitmap mBulbBurningfluff;
    private Bitmap mBulbRustyhair;
    private Bitmap mBulbSpatialbubble;
    private Bitmap mBulbMental;

    private boolean isBroken;
    private boolean hasFingerPrint;

    // Animation stuff
    private ValueAnimator mSvhAnimator;

    public Geiger(Context context) {
        super(context);
        init();
    }

    public Geiger(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Geiger(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setAnomaly(float anomaly) {
        if (anomaly == mAnomaly) {
            return;
        }

        mAnomaly = anomaly;
        invalidate();
    }

    public void setSpringboard(float springboard) {
        if (springboard == mSpringboard) {
            return;
        }

        mSpringboard = springboard;
        invalidate();
    }
    public void setFunnel(float funnel) {
        if (funnel == mFunnel) {
            return;
        }

        mFunnel = funnel;
        invalidate();
    }
    public void setCarousel(float carousel) {
        if (carousel == mCarousel) {
            return;
        }

        mCarousel = carousel;
        invalidate();
    }
    public void setElevator(float elevator) {
        if (elevator == mElevator) {
            return;
        }

        mElevator = elevator;
        invalidate();
    }
    public void setFrying(float frying) {
        if (frying == mFrying) {
            return;
        }

        mFrying = frying;
        invalidate();
    }
    public void setElectra(float electra) {
        if (electra == mElectra) {
            return;
        }

        mElectra = electra;
        invalidate();
    }
    public void setMeatgrinder(float meatgrinder) {
        if (meatgrinder == mMeatgrinder) {
            return;
        }

        mMeatgrinder = meatgrinder;
        invalidate();
    }
    public void setKissel(float kissel) {
        if (kissel == mKissel) {
            return;
        }

        mKissel = kissel;
        invalidate();
    }
    public void setSoda(float soda) {
        if (soda == mSoda) {
            return;
        }

        mSoda = soda;
        invalidate();
    }
    public void setAcidfog(float acidfog) {
        if (acidfog == mAcidfog) {
            return;
        }

        mAcidfog = acidfog;
        invalidate();
    }
    public void setBurningfluff(float burningfluff) {
        if (burningfluff == mBurningfluff) {
            return;
        }

        mBurningfluff = burningfluff;
        invalidate();
    }
    public void setRustyhair(float rustyhair) {
        if (rustyhair == mRustyhair) {
            return;
        }

        mRustyhair = rustyhair;
        invalidate();
    }
    public void setSpatialbubble(float spatialbubble) {
        if (spatialbubble == mSpatialbubble) {
            return;
        }

        mSpatialbubble = spatialbubble;
        invalidate();
    }

    public void setMental(float mental) {
        if (mental == mMental) {
            return;
        }

        mMental = mental;
        invalidate();
    }

    public void setMonolith(float monolith) {
        if (monolith == mMonolith) {
            return;
        }

        mMonolith = monolith;
        invalidate();
    }

    public void setSvh(float svh) {
        if (svh == mSvh) {
            return;
        }

        mSvh = svh;
        invalidate();
    }

    public float getSvh() {
        return mSvh;
    }

    public void toSvh(final float svh, long duration) {
        if (svh == tSvh) {
            return;
        }
        tSvh = svh;
        mSvhAnimator.cancel();
        mSvhAnimator = ValueAnimator.ofFloat(mSvh, svh);
        mSvhAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        mSvhAnimator.setDuration(duration);
        mSvhAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSvh = (float) mSvhAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        mSvhAnimator.start();
    }

    public void setBrokenGlass(boolean broken)
    {
        if(isBroken == broken)
        {
            return;
        }
        isBroken = broken;
        invalidate();
    }

    public void setFingerPrint(boolean fPrint)
    {
        if(hasFingerPrint == fPrint)
        {
            return;
        }
        hasFingerPrint = fPrint;
        invalidate();
    }

    public void setLevel(int level)
    {
        this.level = level;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Get sizes
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int finalMeasureSpecX = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        int finalMeasureSpecY = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(finalMeasureSpecX, finalMeasureSpecY);

        mWidth = widthSize;
        mHeight = heightSize;

        mScaleFactorX = (float) mWidth / WIDGET_WIDTH;
        mScaleFactorY = (float) mHeight / WIDGET_HEIGHT;
        //Log.d(TAG, "widthSize: " + widthSize + " heightSize:" + heightSize
//                + " finalMeasureSpecX:" + finalMeasureSpecX  + " finalMeasureSpecY:" + finalMeasureSpecY
//                + " mWidth:" + mWidth  + " mHeight:" + mHeight
//                + " mScaleFactorX:" + mScaleFactorX  + " finalMeasureSpecY:" + finalMeasureSpecY
//                + " finalMeasureSpecX:" + finalMeasureSpecX  + " mScaleFactorY:" + mScaleFactorY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "Geiger - onDraw");
        super.onDraw(canvas);
        //this.bringToFront();
        drawBack(canvas);

        drawPeak(canvas);
        drawIndicator(canvas);
        drawFront(canvas);

    }

    protected void init() {

        mScale = BitmapFactory.decodeResource(getResources(), R.drawable.scale);
        //scaleLvl5 = BitmapFactory.decodeResource(getResources(), R.drawable.scale_lvl5);
        mIndicator = BitmapFactory.decodeResource(getResources(), R.drawable.indicator);
        mFrontSide = BitmapFactory.decodeResource(getResources(), R.drawable.geiger_top);
        brokenGlass = BitmapFactory.decodeResource(getResources(), R.drawable.broken_glass);
        fingerPrint = BitmapFactory.decodeResource(getResources(), R.drawable.fingerprint);
        mPeakOff = BitmapFactory.decodeResource(getResources(), R.drawable.peak_off);
        mPeakOn = BitmapFactory.decodeResource(getResources(), R.drawable.peak_on);

        mBulbBack = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_back);
        mBulbAnomaly = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbSpringboard = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbFunnel = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbCarousel = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbElevator = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbFrying = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbElectra = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbMeatgrinder = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbKissel = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbSoda = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbAcidfog = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbBurningfluff = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbRustyhair = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbSpatialbubble = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_anomaly);
        mBulbMental = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_mental);

        mMatrix = new Matrix();
        mRect = new RectF();
        mPaint = new Paint();

        mSvhAnimator = ValueAnimator.ofFloat(0f, 1f);
    }

    protected void drawBack(Canvas canvas) {
        convertRect(mScale.getWidth(), mScale.getHeight(), 31, 31, mMatrix);
        canvas.drawBitmap(mScale, mMatrix, null);
        //convertRect(scaleLvl5.getWidth(), scaleLvl5.getHeight(), 0, 100, mMatrix);
        //canvas.drawBitmap(scaleLvl5, mMatrix, null);
        if(level >= 2)
            drawAnomaly(canvas);
        drawSpringboard(canvas);
        drawFunnel(canvas);
        drawCarousel(canvas);
        drawElevator(canvas);
        drawFrying(canvas);
        drawElectra(canvas);
        drawMeatgrinder(canvas);
        drawKissel(canvas);
        drawSoda(canvas);
        drawAcidfog(canvas);
        drawBurningfluff(canvas);
        drawRustyhair(canvas);
        drawSpatialbubble(canvas);
        if(level >= 3)
            drawMental(canvas);
        if(hasFingerPrint)
        {
            convertRect(fingerPrint.getWidth(), fingerPrint.getHeight(), -35, 210, mMatrix);
            canvas.drawBitmap(fingerPrint, mMatrix, null);
        }
        if(isBroken)
        {
            convertRect(brokenGlass.getWidth(), brokenGlass.getHeight(), 170, 100, mMatrix);
            canvas.drawBitmap(brokenGlass, mMatrix, null);
        }
    }

    protected void drawIndicator(Canvas canvas) {

        convertRect(mIndicator.getWidth(), mIndicator.getHeight(), 487, 350, mMatrix);
        mMatrix.postRotate(svhToRotation(mSvh), 505 * mScaleFactorX, 1010 * mScaleFactorY);
        canvas.drawBitmap(mIndicator, mMatrix, null);
    }

    protected void drawFront(Canvas canvas) {
        convertRect(mFrontSide.getWidth(), mFrontSide.getHeight(), 0, 0, mMatrix);
        canvas.drawBitmap(mFrontSide, mMatrix, null);
    }

    protected void drawAnomaly(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mAnomaly <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mAnomaly, 0f, 15f, 100f, 0f));
        if(mAnomaly >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbAnomaly, mMatrix, null);
    }

    protected void drawSpringboard(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mSpringboard <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mSpringboard, 0f, 15f, 100f, 0f));
        if(mSpringboard >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbSpringboard, mMatrix, null);
    }
    protected void drawFunnel(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mFunnel <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mFunnel, 0f, 15f, 100f, 0f));
        if(mFunnel >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbFunnel, mMatrix, null);
    }
    protected void drawCarousel(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mCarousel <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mCarousel, 0f, 15f, 100f, 0f));
        if(mCarousel >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbCarousel, mMatrix, null);
    }
    protected void drawElevator(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mElevator <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mElevator, 0f, 15f, 100f, 0f));
        if(mElevator >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbElevator, mMatrix, null);
    }
    protected void drawFrying(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mFrying <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mFrying, 0f, 15f, 100f, 0f));
        if(mFrying >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbFrying, mMatrix, null);
    }
    protected void drawElectra(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mElectra <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mElectra, 0f, 15f, 100f, 0f));
        if(mElectra >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbElectra, mMatrix, null);
    }
    protected void drawMeatgrinder(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mMeatgrinder <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mMeatgrinder, 0f, 15f, 100f, 0f));
        if(mMeatgrinder >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbMeatgrinder, mMatrix, null);
    }
    protected void drawKissel(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mKissel <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mKissel, 0f, 15f, 100f, 0f));
        if(mKissel >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbKissel, mMatrix, null);
    }
    protected void drawSoda(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mSoda <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mSoda, 0f, 15f, 100f, 0f));
        if(mSoda >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbSoda, mMatrix, null);
    }
    protected void drawAcidfog(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mAcidfog <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mAcidfog, 0f, 15f, 100f, 0f));
        if(mAcidfog >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbAcidfog, mMatrix, null);
    }
    protected void drawBurningfluff(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mBurningfluff <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mBurningfluff, 0f, 15f, 100f, 0f));
        if(mBurningfluff >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBurningfluff, mMatrix, null);
    }
    protected void drawRustyhair(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mRustyhair <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mRustyhair, 0f, 15f, 100f, 0f));
        if(mRustyhair >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbRustyhair, mMatrix, null);
    }
    protected void drawSpatialbubble(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 281, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 286, 120, mRect);
        int color[] = mSpatialbubble <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mSpatialbubble, 0f, 15f, 100f, 0f));
        if(mSpatialbubble >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 281, 115, mMatrix);
        canvas.drawBitmap(mBulbSpatialbubble, mMatrix, null);
    }

    protected void drawMental(Canvas canvas) {
        convertRect(mBulbBack.getWidth(), mBulbBack.getHeight(), 572, 115, mMatrix);
        canvas.drawBitmap(mBulbBack, mMatrix, null);

        drawRect(151, 151, 577, 120, mRect);
        int color[] = mMental <= 0f ? Convert.numberToRGB(Convert.RGB_GREY) : Convert.numberToRGB(Convert.map(mMental, 0f, 15f, 100f, 0f));
        if(mMental >= 15f) {
            color[1] = 255;
            color[2] = 0;
            color[3] = 0;
        }
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);

        convertRect(161, 161, 572, 115, mMatrix);
        canvas.drawBitmap(mBulbMental, mMatrix, null);
    }

    protected void drawPeak(Canvas canvas) {
        convertRect(mPeakOff.getWidth(), mPeakOff.getHeight(), 369, 60, mMatrix);

        if (mSvh >= 15) {
            canvas.drawBitmap(mPeakOn, mMatrix, null);
        }
        else {
            canvas.drawBitmap(mPeakOff, mMatrix, null);
        }
    }

    private void convertRect(int bitmapWidth, int bitmapHeight, int left, int top, Matrix matrix)
    {
        matrix.reset();
        matrix.postScale(mScaleFactorX, mScaleFactorY);
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top);
    }

    private float svhToRotation(float svh) {
        if(svh >= 0f && svh < 1f) {
            return Convert.map(svh, 0f, 1f, -37f, -20f);
        }
        else if(svh >= 1f && svh < 7f) {
            return Convert.map(svh, 1f, 7f, -20f, 10f);
        }
        else if(svh >= 7f && svh < 9f) {
            return Convert.map(svh, 7f, 9f, 10f, 21f);
        }
        else if(svh >= 9f && svh < 15f) {
            return Convert.map(svh, 9f, 15f, 21f, 37f);
        }
        else if(svh >= 15f) {
            return 37f;
        }

        return -37f;
    }

    private void drawRect(int width, int height, int left, int top, RectF rect)
    {
        rect.set(
                left * mScaleFactorX,
                top * mScaleFactorY,
                (left + width) * mScaleFactorX,
                (top + height) * mScaleFactorY
        );
    }
}
