package net.afterday.compas.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import net.afterday.compas.R;
import net.afterday.compas.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.util.Fonts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by spaka on 5/20/2018.
 */

public class LevelProgress extends View
{
    private static final String TAG = "LevelProgress";
    private static final int WIDGET_WIDTH = 350;
    private static final int WIDGET_HEIGHT = 100;

    private int mWidth;
    private int mHeight;
    private int percents = 0;
    private int xpAdded = 0;
    private boolean showXp = false;
    private boolean showMax = false;

    private float mScaleFactorX;
    private float mScaleFactorY;

    private RectF rect;
    private Matrix matrix;
    private Paint paint;
    private ValueAnimator vAnimator;
    private Paint fPaint;
    private List<OnLevelChangedListener> levelChangedListeners = new ArrayList<>();

    public LevelProgress(Context context)
    {
        super(context);
        init();
    }

    public LevelProgress(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LevelProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setProgress(int progress)
    {
        this.percents = progress;
        invalidate();
    }

    public void showMax(boolean show)
    {
        showMax = show;
        invalidate();
    }

    public void setProgress(ItemAdded itemAdded)
    {
        xpAdded = itemAdded.getItem().getItemDescriptor().getXpPoints();
        if(xpAdded == 0)
        {
            return;
        }
        showXp = true;
        invalidate();
        Observable.timer(1, TimeUnit.SECONDS).take(1).observeOn(AndroidSchedulers.mainThread()).subscribe((t) -> {
            showXp = false;
            int progress = itemAdded.getLevelXpPercents();
            boolean levelChanged = itemAdded.levelChanged();

            if(progress == percents && !levelChanged)
            {
                postInvalidate();
                return;
            }
            //int progress = percents + item.getXpPoints();
            if(progress <= percents)
            {
                vAnimator.cancel();
                vAnimator = ValueAnimator.ofInt(percents, 100 + progress);
                vAnimator.setDuration(700);
                vAnimator.setInterpolator(new LinearInterpolator());
                vAnimator.addUpdateListener((v) -> {
                    int val = (int)v.getAnimatedValue();
                    Log.d(TAG, "Animator 1: " + val);
                    percents = val <= 100 ? val : val - 100;
                    postInvalidate();
                });
            }else
            {
                vAnimator.cancel();
                vAnimator = ValueAnimator.ofInt(percents, progress);
                vAnimator.setDuration(700);
                vAnimator.setInterpolator(new LinearInterpolator());
                vAnimator.addUpdateListener((v) -> {
                    percents = (int)v.getAnimatedValue();
                    Log.d(TAG, "Animator 1: " + percents);
                    postInvalidate();
                });
            }
            vAnimator.start();
            if(itemAdded.levelChanged())
            {
                vAnimator.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        for(OnLevelChangedListener l : levelChangedListeners)
                        {
                            l.levelChanged(itemAdded.getLevel());
                        }
                    }
                });
            }
        });
        if(itemAdded.getLevel() == 5)
        {
            Observable.timer(2, TimeUnit.SECONDS).take(1).observeOn(AndroidSchedulers.mainThread())
                    .subscribe((t) -> {
                        showXp = true;
                        showMax = true;
                        postInvalidate();
                    });
        }
    }

    public void addOnLevelChangedListener(OnLevelChangedListener listener)
    {
        this.levelChangedListeners.add(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Get sizes
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//        int finalMeasureSpecX = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
//        int finalMeasureSpecY = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = widthSize;
        mHeight = heightSize;

        mScaleFactorX = (float) mWidth / WIDGET_WIDTH;
        mScaleFactorY = (float) mHeight / WIDGET_HEIGHT;
        matrix.reset();
        matrix.postScale(mScaleFactorX, mScaleFactorY);
        matrix.postTranslate(0, 0);
        fPaint.setTextSize((int)(mHeight * 1.0));
        //fPaint.setTextSize(60);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Log.e(TAG, "HEIGHT: " + mHeight);

//        canvas.drawText( "MAX", mScaleFactorX * 60, (mHeight + 5) * mScaleFactorY, fPaint);
//        return;
        if(showMax)
        {
            canvas.drawText( "MAX", mScaleFactorX * 70, (mHeight + 15) * mScaleFactorY, fPaint);
            return;
        }
        if(showXp && xpAdded > 0)
        {
            canvas.drawText( "+" + Integer.toString(xpAdded), mScaleFactorX * 70, (mHeight + 15) * mScaleFactorY, fPaint);
            return;
        }
        if(percents == 100)
        {
            percents = 0;
        }
        drawRect(mWidth * percents / 100 , rect);
        canvas.drawRoundRect(rect, 7, 7, paint);

    }

    protected void init() {
        matrix = new Matrix();
        rect = new RectF();
        paint = new Paint();
        paint.setARGB(255,255,127,0);
        paint.setAlpha(180);
        vAnimator = ValueAnimator.ofInt(0, 0);
        fPaint = Fonts.instance().getDefaultFontPaint();
        //fPaint.setTextSize(110);
    }

    private void drawRect(int width, RectF rect)
    {
        rect.set(
                0,
                0,
                width,
                mHeight
        );
    }

    public interface OnLevelChangedListener
    {
        void levelChanged(int level);
    }
}
