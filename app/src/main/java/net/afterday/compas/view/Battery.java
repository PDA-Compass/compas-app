package net.afterday.compas.view;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import net.afterday.compas.sensors.Battery.BatteryStatus;
import net.afterday.compas.util.Fonts;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Justas Spakauskas on 3/20/2018.
 */

public class Battery extends View
{
    private static final String TAG = "Clock";

    private static final int WIDGET_WIDTH = 195;
    private static final int WIDGET_HEIGHT = 85;
    private static final int GREEN = 0xff22ff00;
    private static final int RED = 0xffff0019;
    // Input stuff
    private String mText = "0%";
    private int color = GREEN;

    // Dimension stuff
    private int mWidth;
    private int mHeight;
    private int energy;

    private float mScaleFactorX;
    private float mScaleFactorY;
    private boolean isVisible = true;

    // Paint stuff
    private Paint mPaint;
    private Typeface mTypeface;
    private Disposable subscription;

    public Battery(Context context) {
        super(context);

        init();
    }

    public Battery(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public Battery(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public Battery setText(String text) {
        mText = text;
        invalidate();
        return this;
    }

    public void setLevel(int level)
    {
        energy = level;
        mText = level + "%";
        color = this.getColor();
        //mPaint.setColor(color);
        invalidate();
    }

    public void setStatus(BatteryStatus batteryStatus)
    {
        energy = batteryStatus.getEnergyLevel();
//        energy = 14;
        mText = energy + "%";
        if(energy > 15)
        {
            mPaint = Fonts.instance().setDefaultColor(mPaint);
        }else
        {
            mPaint.setColor(RED);
        }
        
        if(batteryStatus.isCharging())
        {
            if(subscription != null && !subscription.isDisposed())
            {
                subscription.dispose();
            }
            subscription = Observable.interval(1, TimeUnit.SECONDS).subscribe((s) -> {
                isVisible = !isVisible;
                postInvalidate();
            });
        }else
        {
            if(subscription != null && !subscription.isDisposed())
            {
                subscription.dispose();
            }
            subscription = null;
            isVisible = true;
            invalidate();
        }
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

        mPaint.setTextSize(60 * mScaleFactorY);
        //mPaint.setTextSize((int)(mHeight * 1.0));
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if(subscription != null && !subscription.isDisposed())
        {
            subscription.dispose();
            subscription = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isVisible)
        {
            canvas.drawText(mText, 23 * mScaleFactorX, 60 * mScaleFactorY, mPaint);
        }
    }

    protected void init() {
        mTypeface = Fonts.instance().getDefaultTypeFace();
        mPaint = new Paint();
        mPaint.setTypeface(mTypeface);
    }

    private int getColor()
    {
        return (Integer) (new ArgbEvaluator()).evaluate(energy / 100, RED, GREEN);
        //return new Argb
    }

    private float interpolate(float a, float b, float proportion) {
        return (a + ((b - a) * proportion));
    }

    private Paint setPaintColor(int a, int b, float proportion, Paint p) {
        float[] hsva = new float[3];
        float[] hsvb = new float[3];
        Color.colorToHSV(a, hsva);
        Color.colorToHSV(b, hsvb);
        for (int i = 0; i < 3; i++)
        {
            hsvb[i] = interpolate(hsva[i], hsvb[i], proportion);
        }
        p.setColor(Color.HSVToColor(hsvb));
        return p;
    }
}
