package net.afterday.compas.util;

import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by spaka on 5/21/2018.
 */

public class Fonts
{
    private static Fonts instance;
    private AssetManager assetManager;
    private Typeface defaultTypeFace;

    private Fonts(AssetManager assetManager)
    {
        this.assetManager = assetManager;
    }

    public static Fonts instance()
    {
        if(instance == null)
        {
            throw new IllegalStateException("AssetsManager not set. Must call instance(AssetsManager) first");
        }
        return instance;
    }

    public static Fonts instance(AssetManager assetManager)
    {
        instance = new Fonts(assetManager);
        return instance;
    }

    public Paint getDefaultFontPaint()
    {
        Typeface tf = getDefaultTypeFace();
        Paint p = new Paint();
        if(tf != null)
        {
            p.setTypeface(tf);
        }
        p.setARGB(255,255,127,0);
        return p;
    }

    public Paint setDefaultColor(Paint paint)
    {
        paint.setARGB(255,255,127,0);
        return paint;
    }

    public Typeface getDefaultTypeFace()
    {
        if(defaultTypeFace == null)
        {
            try{
                defaultTypeFace = Typeface.createFromAsset(assetManager, "fonts/console.ttf");
            }catch (Exception e)
            {

            }
        }
        return defaultTypeFace;
    }
}
