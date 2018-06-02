package net.afterday.compass.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import net.afterday.compass.util.Fonts;

/**
 * Created by Justas Spakauskas on 3/20/2018.
 */

public class Battery extends View
{
    private static final String TAG = "Clock";

    private static final int WIDGET_WIDTH = 195;
    private static final int WIDGET_HEIGHT = 85;

    // Input stuff
    private String mText = "0%";

    // Dimension stuff
    private int mWidth;
    private int mHeight;

    private float mScaleFactorX;
    private float mScaleFactorY;

    // Paint stuff
    private Paint mPaint;
    private Typeface mTypeface;

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
        mText = level + "%";
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

        mPaint.setTextSize(70 * mScaleFactorY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(mText, 23 * mScaleFactorX, 60 * mScaleFactorY, mPaint);
    }

    protected void init() {
//        mPaint = new Paint();
//        mPaint.setARGB(255,255,127,0);
//
//        try {
//            mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/console.ttf");
//            mPaint.setTypeface(mTypeface);
//        }
//        catch (RuntimeException e) {
//            //Log.e(TAG, "Cannot create typeface");
//        }
        mPaint = Fonts.instance().getDefaultFontPaint();
    }
}
