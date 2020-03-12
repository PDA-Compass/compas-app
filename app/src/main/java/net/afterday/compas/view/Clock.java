package net.afterday.compas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


public class Clock extends View
{
    private static final String TAG = "Clock";

    private static final int WIDGET_WIDTH = 195;
    private static final int WIDGET_HEIGHT = 85;

    // Input stuff
    private String mText = "23:59";

    // Dimension stuff
    private int mWidth;
    private int mHeight;

    private float mScaleFactorX;
    private float mScaleFactorY;

    // Paint stuff
    private Paint mPaint;
    private Typeface mTypeface;
    private Observable<Long> ticks;
    private Disposable subscription;
    public Clock(Context context) {
        super(context);

        init();
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public Clock setText(String text) {
        mText = text;
        invalidate();
        return this;
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        subscription = ticks.subscribe((i) -> {
            postInvalidate();
        });
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        subscription.dispose();
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int mins = c.get(Calendar.MINUTE);
        mText = (hours < 10 ? "0" : "") + hours + ":" + (mins < 10 ? "0" : "") + mins;
        canvas.drawText(mText, 23 * mScaleFactorX, 60 * mScaleFactorY, mPaint);
    }

    protected void init() {
        ticks = Observable.interval(0,1, TimeUnit.SECONDS);
        mPaint = new Paint();
        mPaint.setARGB(255,255,127,0);

        try {
            mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/console.ttf");
            mPaint.setTypeface(mTypeface);
        }
        catch (RuntimeException e) {
            Log.e(TAG, "Cannot create typeface");
        }
    }
}
