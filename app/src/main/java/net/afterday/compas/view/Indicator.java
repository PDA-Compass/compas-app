package net.afterday.compas.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import net.afterday.compas.R;
import net.afterday.compas.util.Convert;

/**
 * Created by Justas Spakauskas on 3/10/2018.
 */

public class Indicator extends View
{
    private static final String TAG = "Indicator";

    private static final int WIDGET_WIDTH = 600;
    private static final int WIDGET_HEIGHT = 300;
    private Matrix matrix;

    private Bitmap indicatorOn;
    private Bitmap indicatorBck;
    private ValueAnimator vAnimator;

    private int maxWidth;

    private int mWidth;
    private int mHeight;
    private int level;
    private int x = 1;

    private float mScaleFactorX;
    private float mScaleFactorY;

    private float mStrength;

    public Indicator(Context context)
    {
        super(context);
        init();
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setStrength(float strength)
    {
        if(this.level < 5)
        {
            return;
        }
        if(strength == mStrength)
        {
            return;
        }
        mStrength = strength;
        vAnimator.cancel();
        int nw = (int) Convert.map(strength > 100 ? 100 : strength, 0, 100, maxWidth, 0);
        vAnimator = ValueAnimator.ofInt(x, nw);
        vAnimator.setDuration(1000);
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                x = (int)animation.getAnimatedValue();
                postInvalidate();
            }
        });
        vAnimator.start();
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

        //minWidth = 0 * mScaleFactorX;
        maxWidth = indicatorOn.getWidth();
        x = maxWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "Indicator - onDraw");
        super.onDraw(canvas);
        Matrix m = new Matrix();
        convertRect(indicatorOn.getWidth(), indicatorOn.getHeight(), 0, 0, m);
        convertRect(indicatorBck.getWidth(), indicatorBck.getHeight(), 0, 0, matrix);
        int v = indicatorOn.getWidth() - x;
        canvas.drawBitmap(indicatorBck, matrix, null);
        if((maxWidth - x) > 0)
        {
            canvas.translate((x / 2) * mScaleFactorX, 0);

            canvas.drawBitmap(Bitmap.createBitmap(indicatorOn, x / 2, 0, maxWidth - x, indicatorOn.getHeight()),matrix, null);
        }
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
    }

    private void convertRect(int bitmapWidth, int bitmapHeight, int left, int top, Matrix matrix)
    {
        matrix.reset();
        matrix.postScale(mScaleFactorX, mScaleFactorY);
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top);
    }

    protected void init() {

        indicatorOn = BitmapFactory.decodeResource(getResources(), R.drawable.light_bars);
        indicatorBck = BitmapFactory.decodeResource(getResources(), R.drawable.light_bars_off);
 //       //Log.w(TAG, "IndicatorON width: " + indicatorOn.getWidth());
        //c2 = new Canvas(indicatorBck);
        vAnimator = ValueAnimator.ofFloat(0f, 1f);
        matrix = new Matrix();
    }

}
