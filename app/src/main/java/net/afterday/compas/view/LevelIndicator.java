package net.afterday.compas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

import net.afterday.compas.R;

/**
 * Created by spaka on 5/9/2018.
 */

public class LevelIndicator extends AppCompatImageButton
{
    private static final String TAG = "QrBtnLevelIndicator";
//    private static final int WIDGET_WIDTH = 155;
//    private static final int WIDGET_HEIGHT = 155;
    private int mWidth,
                mHeight,
                backgroundWidth,
                backgroundHeight;
    private float mScaleFactorX,
                  mScaleFactorY;
    private Paint paint;
    private Paint imgPaint;
    private int level = 1;
    private Bitmap qrImage;
    private Matrix matrix;

    public LevelIndicator(Context context)
    {
        super(context);
        init();
    }

    public LevelIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LevelIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setLevel(int level)
    {
        if(level == this.level)
        {
            return;
        }
        this.level = level;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Get sizes
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//        int finalMeasureSpecX = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
//        int finalMeasureSpecY = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);


        mWidth = widthSize;
        mHeight = heightSize;

        mScaleFactorX = (float) mWidth / backgroundWidth;
        mScaleFactorY = (float) mHeight / backgroundHeight;

        paint.setTextSize(100 * mScaleFactorY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        this.setAlpha(0);
        super.onDraw(canvas);
        //

        Log.d("LevelIndicator", "draw   ---- " + level);
        //this.setAlpha(100);

        convertRect(-1,-2, matrix);
        canvas.drawBitmap(qrImage, matrix, imgPaint);
        if(level > 0)
        {
            canvas.drawText(Integer.toString(level), mScaleFactorX * (level > 1 ? 35 : 6), mScaleFactorY * 125, paint);
        }
//        if(level == 1)
//        {
//            canvas.drawText(Integer.toString(level), mScaleFactorX * 6, mScaleFactorY * 126, paint);
//        }else if(level > 1)
//        {
//            canvas.drawText(Integer.toString(level), mScaleFactorX * 35, mScaleFactorY * 126, paint);
//        }
//        if(level > 0)
//        {
//            Log.d(TAG, "ScaleFactor X: " + mScaleFactorX + " ScaleFactor Y: " + mScaleFactorY);
//            //canvas.drawText(Integer.toString(level), 6, 90, paint);
//            canvas.drawText(Integer.toString(5), mScaleFactorX * 6, mScaleFactorY * 126, paint);
//        }
        //bringToFront();

    }

    private void init()
    {
        imgPaint = new Paint();
        //imgPaint.setAlpha(180);
        paint = new Paint();
        paint.setARGB(255,255,127,0);
        paint.setAlpha(180);
        matrix = new Matrix();
        qrImage = BitmapFactory.decodeResource(getResources(), R.drawable.qr_button);
        backgroundWidth = qrImage.getWidth();
        backgroundHeight = qrImage.getHeight();
        try {
            paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/segment.ttf"));
        }
        catch (RuntimeException e) {
            //Log.e(TAG, "Cannot create typeface");
        }
        //paint.setColor(Color.WHITE);
    }

    private void convertRect(int left, int top, Matrix matrix)
    {
        matrix.reset();
        matrix.postScale(mScaleFactorX, mScaleFactorY);
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top);
    }
}
