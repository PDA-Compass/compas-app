package net.afterday.compas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import net.afterday.compas.util.Fonts;

/**
 * Created by spaka on 5/5/2018.
 */

public class CountDownTimer extends View
{
    private static final String TAG = "CountDownTimer";
    private Paint paint;
    private int mWidth,
                mHeight;

    private float mScaleFactorX,
                  mScaleFactorY;

    private static final int WIDGET_WIDTH = 200;
    private static final int WIDGET_HEIGHT = 120;
    private long secondsLeft = 0;

    public CountDownTimer(Context context)
    {
        super(context);
        init();
    }

    public CountDownTimer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownTimer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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

        paint.setTextSize(120 * mScaleFactorY);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        //canvas.drawText("00:00", 23 * mScaleFactorX, 60 * mScaleFactorY, paint);
        if(secondsLeft < 0)
        {
            return;
        }
        //bringToFront();

        canvas.drawText(secondsToString(secondsLeft), 23 * mScaleFactorX, 60 * mScaleFactorY, paint);
    }

    public void setSecondsLeft(Long timeLeft)
    {
        secondsLeft = timeLeft;
        invalidate();
    }

    private void init()
    {
        paint = Fonts.instance().getDefaultFontPaint();
    }

    private String secondsToString(long secondsLeft)
    {
        String mins = Long.toString(secondsLeft / 60);
        if(mins.length() < 2)
        {
            mins = "0" + mins;
        }
        String secs = Long.toString(secondsLeft % 60);
        if(secs.length() < 2)
        {
            secs = "0" + secs;
        }
        return mins + ":" + secs;
    }
}
