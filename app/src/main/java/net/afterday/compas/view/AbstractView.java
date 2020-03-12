package net.afterday.compas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public abstract class AbstractView extends View
{
    protected Matrix matrix;
    protected float scaleX, scaleY;
    protected int width, height;
    protected int backgroundWidth = 1, backgroundHeight = 1;


    public AbstractView(Context context)
    {
        this(context, null);
    }

    public AbstractView(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public AbstractView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        matrix = new Matrix();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        width = widthSize;
        height = heightSize;
        Bitmap background = getBackgroundDrawable();
        if(background != null)
        {
            backgroundWidth = background.getWidth();
            backgroundHeight = background.getHeight();
            scaleX = (float) width / backgroundWidth;
            scaleY = (float) height / backgroundHeight;
            matrix.postScale(scaleX, scaleY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    abstract protected Bitmap getBackgroundDrawable();
    abstract protected void init();
}
