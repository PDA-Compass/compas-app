package net.afterday.compas.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import net.afterday.compas.R;
public class Bar extends AbstractView
{
    private static final String TAG = "Bar";
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;

    private Paint paint;
    private RectF rectf;

    private Bitmap mTopImage;

    private float percentage = 0;
    private int bottom;

    private int imgResId = -1;
    private int offsetLeft = 0;
    private int offsetBottom = 0;
    private int scaleHeight = 0;
    private int scaleWidth = 0;
    private int measurement = 0;
    private int scaleColor = 0xFFFFFF;

    public Bar(Context context) {
        this(context, null);
    }

    public Bar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Bar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs, defStyleAttr);
    }

    private void getAttrs(Context context, AttributeSet attrs, int defStyleAttr)
    {
        if(attrs == null)
        {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Bar, 0, 0);
        imgResId = a.getResourceId(R.styleable.Bar_image, 0);
        offsetLeft = a.getInt(R.styleable.Bar_offset_left, 0);
        offsetBottom = a.getInt(R.styleable.Bar_offset_bottom, 0);
        scaleHeight = a.getInt(R.styleable.Bar_scale_height, 0);
        scaleWidth = a.getInt(R.styleable.Bar_scale_width, 0);
        measurement = a.getInt(R.styleable.Bar_measurement, VERTICAL);
        scaleColor = a.getInt(R.styleable.Bar_scale_color, 0xFFFFFF);
        a.recycle();
    }

    @Override
    protected Bitmap getBackgroundDrawable()
    {
        if(mTopImage == null)
        {
            mTopImage = BitmapFactory.decodeResource(getResources(), imgResId);
        }
        return mTopImage;
    }


    public void setPercents(double percents)
    {
        if(percentage == percents)
        {
            return;
        }
        if(percents > 100)
        {
            percents = 100;
        }else if(percents < 0)
        {
            percents = 0;
        }
        percentage = (float) percents;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mTopImage, matrix, null);
        if(percentage > 0)
        {
            if(measurement == VERTICAL)
            {
                drawRect(scaleWidth, (int)(scaleHeight / 100 * percentage), rectf);
            }
            else
            {
                drawRect((int)(scaleWidth / 100 * percentage), scaleHeight, rectf);
            }
        }
        canvas.drawRect(rectf, paint);
    }

    protected void init() {
        rectf = new RectF();
        paint = new Paint();
        paint.setColor(scaleColor);
        bottom = backgroundHeight - offsetBottom;
    }

    private void drawRect(int width, int height, RectF rect)
    {
        Log.e(TAG, "W: " + this.width + " H: " + this.height + " BW: " + getBackgroundDrawable().getWidth() + " BH: " + getBackgroundDrawable().getHeight() + " SX: " + scaleX + " SY: " + scaleY + " SH: " + scaleHeight);
        if(measurement == VERTICAL)
            {
            rect.set(
                    offsetLeft * scaleX,
                    (offsetBottom + scaleHeight - height) * scaleY,
                    (offsetLeft + scaleWidth) * scaleX,
                    scaleHeight * scaleY
            );
        }
        else {
            rect.set(
                offsetLeft * scaleX,
                (bottom - height) * scaleY,
                (offsetLeft + width) * scaleX,
                (bottom) * scaleY
            );
        }

    }

    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }
}

