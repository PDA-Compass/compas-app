package net.afterday.compas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import net.afterday.compas.R;
import net.afterday.compas.util.Convert;

import java.util.Locale;

public class Radbar extends View
{
    private static final String TAG = "Radbar";

    private static final int WIDGET_WIDTH = 749;
    private static final int WIDGET_HEIGHT = 224;

    // Input stuff
    private double mHealth = 100d;
    private double mRadiation = 0d;
    private long mControlled = 0L;
    private double mHealing = 0d;
    private boolean hasRadInstant;

    // Dimension stuff
    private int mWidth;
    private int mHeight;

    private float mScaleFactorX;
    private float mScaleFactorY;

    // Paint stuff
    private Paint mPaint;
    private Paint mPaintGrey;
    private Paint mPaintSymbol;
    private Paint mPaintMore;
    private Matrix mMatrix;
    private RectF mRect;
    private Typeface mTypefaceSeg;

    // Bitmaps
    private Bitmap mBackImage;
    private Bitmap mTopImage;
    private Bitmap arrow;

    private OnTouchListener listener;
    private boolean isPressed;
    private PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
    private OnTouchListener clicker = (v, t) -> {
        if(listener != null)
        {
            listener.onTouch(v, t);
        }
        if(t.getAction() == MotionEvent.ACTION_DOWN)
        {
            isPressed = true;
            mPaint.setColorFilter(colorFilter);
            mPaintSymbol.setColorFilter(colorFilter);
            invalidate();
        }else if(t.getAction() == MotionEvent.ACTION_UP)
        {
            isPressed = false;
            mPaint.setColorFilter(null);
            mPaintSymbol.setColorFilter(null);
            invalidate();
        }
        return true;
    };

    public Radbar(Context context) {
        super(context);

        init();
    }

    public Radbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public Radbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public Radbar setHealth(double health) {
        mHealth = health;
        invalidate();
        return this;
    }

    public Radbar setRadiation(double radiation) {
        mRadiation = radiation;
        invalidate();
        return this;
    }

    public Radbar setHealing(double healing) {
        mHealing = healing;
        invalidate();
        return this;
    }

    public Radbar setControlled(long controlled) {
        mControlled = controlled;
        invalidate();
        return this;
    }

    public Radbar setInfo(double health, double radiation, double healing, long controlled, boolean hasRadInstant) {
        if(mHealth == health && mRadiation == radiation && mHealing == healing && mControlled == controlled && this.hasRadInstant == hasRadInstant)
        {
            return this;
        }
        mHealth = health;
        mRadiation = radiation;
        mHealing = healing;
        mControlled = controlled;
        this.hasRadInstant = hasRadInstant;
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
        mPaintSymbol.setTextSize(50 * mScaleFactorY);
        mPaintMore.setTextSize(80 * mScaleFactorY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "RadBar - onDraw");
        super.onDraw(canvas);

        convertRect(mBackImage.getWidth(), mBackImage.getHeight(), 0, 0, mMatrix);

        // Draw the back layer
        canvas.drawBitmap(mBackImage, mMatrix, null);

        // Draw the colored circle
        drawColor(canvas);

        drawText(canvas);

        // Draw the front layer
        canvas.drawBitmap(mTopImage, mMatrix, null);
        if(hasRadInstant)
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
        mPaint.setTextSize(90 * mScaleFactorY);
        mPaintGrey.setTextSize(90 * mScaleFactorY);

        double rad = mRadiation;

        // Draw more symbol
        if (rad > 15d) {
            rad = 15d;
            mPaintMore.setARGB(255,255,127,0);
        }
        else {
            mPaintMore.setARGB(255,35,35,35);
        }

        canvas.drawText(">", 270 * mScaleFactorX, 135 * mScaleFactorY, mPaintMore);

        // Paint first segment
        String[] fullTxt = String.format(Locale.US, "%.3f", rad).split("\\.");
        String txt = "!!" + fullTxt[0];
        txt = txt.substring(txt.length() - 2);

        canvas.drawText("18", 270 * mScaleFactorX, 155 * mScaleFactorY, mPaintGrey);
        canvas.drawText(txt, 270 * mScaleFactorX, 155 * mScaleFactorY, mPaint);

        float txtWidth = mPaint.measureText("18");

        // Paint second segment
        mPaint.setTextSize(65 * mScaleFactorY);
        mPaintGrey.setTextSize(65 * mScaleFactorY);

        txt = "." + fullTxt[1];

        canvas.drawText("888",  270 * mScaleFactorX + txtWidth, 155 * mScaleFactorY, mPaintGrey);
        canvas.drawText(txt, 270 * mScaleFactorX + txtWidth, 155 * mScaleFactorY, mPaint);

        txtWidth += mPaint.measureText("888");

        // Paint percent sign
        mPaint.setTextSize(40 * mScaleFactorY);
        mPaintGrey.setTextSize(40 * mScaleFactorY);
//        canvas.drawText("88",  270 * mScaleFactorX + txtWidth, 155 * mScaleFactorY, mPaintGrey);
        canvas.drawText("Sv", 270 * mScaleFactorX + txtWidth, 155 * mScaleFactorY, mPaintSymbol);
    }

    protected void drawUpgrade(Canvas canvas) {
        //
    }

    protected void init() {
        super.setOnTouchListener(clicker);
        //mBackImage = BitmapFactory.decodeResource(getResources(), R.drawable.seg_back_rad);
        mBackImage = BitmapFactory.decodeResource(getResources(), R.drawable.seg_back);
        mTopImage = BitmapFactory.decodeResource(getResources(), R.drawable.seg_rad);
        arrow = BitmapFactory.decodeResource(getResources(), R.drawable.seg_upgrade);
        mMatrix = new Matrix();
        mRect = new RectF();
        mPaint = new Paint();
        mPaintGrey = new Paint();
        mPaintGrey.setARGB(255,35,35,35);
        mPaintSymbol = new Paint();
        mPaintSymbol.setARGB(255,255,127,0);
        mPaintMore = new Paint();
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

        if(mRadiation >= 15f) {
            return new int[]{255,255,0,0};
        }
        else if(mRadiation < 15f && mRadiation >= 7f) {
            return Convert.numberToRGB(Convert.map(mRadiation, 7, 15, 15, 0)); // red
        }
        else if(mRadiation < 7f && mRadiation >= 1f) {
            return Convert.numberToRGB(Convert.map(mRadiation, 1, 7, 47, 30)); // yellow
        }
        else if(mRadiation < 1f && mRadiation >= 0f) {
            return Convert.numberToRGB(Convert.map(mRadiation, 0, 1, 100, 60)); // green
        }

        return Convert.numberToRGB(Convert.map(mRadiation, 0, 15, 100, 0));
    }

    @Override
    public void setOnTouchListener(OnTouchListener l)
    {
        this.listener = l;
    }
}
