package net.afterday.compas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import net.afterday.compas.R;
import net.afterday.compas.util.Convert;
import net.afterday.compas.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Healthbar extends View
{
    private static final String TAG = "Healthbar";

    private static final int WIDGET_WIDTH = 749;
    private static final int WIDGET_HEIGHT = 224;

    // Input stuff
    private double mHealth = 0d;
    private long mControlled = 0L;
    private double mHealing = 0d;
    private boolean hasHealthInstant;

    // Dimension stuff
    private int mWidth;
    private int mHeight;

    private float mScaleFactorX;
    private float mScaleFactorY;

    // Paint stuff
    private Paint mPaint;
    private Paint mPaintGrey;
    private Matrix mMatrix;
    private RectF mRect;
    private Typeface mTypefaceSeg;

    // Bitmaps
    private Bitmap mBackImage;
    private Bitmap mTopImage;
    private Bitmap arrow;

    private boolean isPressed;

    private OnTouchListener listener;
    private PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP);
    private OnTouchListener clicker = (v, t) -> {
        if(listener != null)
        {
            listener.onTouch(v, t);
        }
        if(t.getAction() == MotionEvent.ACTION_DOWN)
        {
            isPressed = true;
            mPaint.setColorFilter(colorFilter);
            mPaintGrey.setColorFilter(colorFilter);
            invalidate();
        }else if(t.getAction() == MotionEvent.ACTION_UP)
        {
            isPressed = false;
            mPaint.setColorFilter(null);
            mPaintGrey.setColorFilter(null);
            invalidate();
        }
        return true;
    };

    public Healthbar(Context context) {
        super(context);

        init();
    }

    public Healthbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public Healthbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public Healthbar setHealth(double health) {
        mHealth = health;
        invalidate();
        return this;
    }

    public Healthbar setHealing(double healing) {
        mHealing = healing;
        invalidate();
        return this;
    }

    public Healthbar setControlled(long controlled) {
        mControlled = controlled;
        invalidate();
        return this;
    }

    public Healthbar setInfo(double health, double healing, long controlled, boolean hasHealthInstant) {
        if(mHealth == health && mHealing == healing && mControlled == controlled && this.hasHealthInstant == hasHealthInstant)
        {
            return this;
        }
        mHealth = health;
        mHealing = healing;
        mControlled = controlled;
        this.hasHealthInstant = hasHealthInstant;
        invalidate();
        return this;
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

        drawRect(185, 185, 44, 19, mRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        convertRect(mBackImage.getWidth(), mBackImage.getHeight(), 0, 0, mMatrix);

        // Draw the back layer
        canvas.drawBitmap(mBackImage, mMatrix, null);

        // Draw the colored circle
        drawColor(canvas);

        drawText(canvas);

        // Draw the front layer
        canvas.drawBitmap(mTopImage, mMatrix, null);
        if(this.hasHealthInstant)
        {
            convertRect(arrow.getWidth(), arrow.getHeight(), 4, 142, mMatrix);
            canvas.drawBitmap(arrow, mMatrix, null);
        }
    }

    protected void drawColor(Canvas canvas) {
        int[] color = getColor();
        mPaint.setARGB(255, color[1], color[2], color[3]);
        canvas.drawOval(mRect, mPaint);
    }

    protected void drawText(Canvas canvas) {

        // Set up paint for the display
        mPaint.setARGB(255, 255, 127, 0);
        mPaint.setTextSize(100 * mScaleFactorY);
        mPaintGrey.setTextSize(100 * mScaleFactorY);
        // Paint first segment
        String[] fullTxt = String.format(Locale.US, "%.2f", mHealth).split("\\.");
        String txt = "!!!" + fullTxt[0];
        txt = txt.substring(txt.length() - 3);

        canvas.drawText("188", 220 * mScaleFactorX, 160 * mScaleFactorY, mPaintGrey);
        canvas.drawText(txt, 220 * mScaleFactorX, 160 * mScaleFactorY, mPaint);

        float txtWidth = mPaint.measureText("188");

        // Paint second segment
        mPaint.setTextSize(75 * mScaleFactorY);
        mPaintGrey.setTextSize(75 * mScaleFactorY);

        txt = "." + fullTxt[1];

        canvas.drawText("88",  220 * mScaleFactorX + txtWidth, 160 * mScaleFactorY, mPaintGrey);
        canvas.drawText(txt, 220 * mScaleFactorX + txtWidth, 160 * mScaleFactorY, mPaint);

        txtWidth += mPaint.measureText("88");

        // Paint percent sign
        mPaint.setTextSize(50 * mScaleFactorY);
        canvas.drawText("%", 220 * mScaleFactorX + txtWidth, 160 * mScaleFactorY, mPaint);
    }

    protected void drawUpgrade(Canvas canvas) {
        //
    }

    protected void init() {
        super.setOnTouchListener(clicker);
        //mBackImage = BitmapFactory.decodeResource(getResources(), R.drawable.seg_back_health);
        mBackImage = BitmapFactory.decodeResource(getResources(), R.drawable.seg_back);
        mTopImage = BitmapFactory.decodeResource(getResources(), R.drawable.seg_health);
        arrow = BitmapFactory.decodeResource(getResources(), R.drawable.seg_upgrade);
        mMatrix = new Matrix();
        mRect = new RectF();
        mPaint = new Paint();
        mPaintGrey = new Paint();
        mPaintGrey.setARGB(255,35,35,35);
        //mPaintGrey.setARGB(255,255,255,255);

        try {
            mTypefaceSeg = Typeface.createFromAsset(getContext().getAssets(), "fonts/segment.ttf");
            mPaint.setTypeface(mTypefaceSeg);
            mPaintGrey.setTypeface(mTypefaceSeg);
        }
        catch (RuntimeException e) {
            //Log.e(TAG, "Cannot create typeface");
        }


    }

    private void convertRect(int bitmapWidth, int bitmapHeight, int left, int top, Matrix matrix)
    {
        matrix.reset();
        matrix.postScale(mScaleFactorX, mScaleFactorY);
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top);
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

    private int[] getColor()
    {
        if (mHealth <= 0d || mControlled > 0) {
            return Convert.numberToRGB(Convert.RGB_GREY);
        }
        if (mHealing > 0) {
            return Convert.numberToRGB(Convert.RGB_BLUE);
        }
        if (mHealth > 100d) {
            return new int[]{255,0,255,0};
        }

        return Convert.numberToRGB(mHealth);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l)
    {
        this.listener = l;
    }
}
