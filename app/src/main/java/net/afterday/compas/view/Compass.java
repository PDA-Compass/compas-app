package net.afterday.compas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import net.afterday.compas.R;
import net.afterday.compas.settings.Constants;
import net.afterday.compas.settings.Settings;

/**
 * Created by Justas Spakauskas on 3/10/2018.
 */

public class Compass extends View implements SensorListener, SensorEventListener
{
    private static final String TAG = "Compass";

    private static final int WIDGET_WIDTH = 1030;
    private static final int WIDGET_HEIGHT = 1030;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private Matrix matrix;

    private Bitmap compass;

    // Dimension stuff
    private int mWidth;
    private int mHeight;

    private float mScaleFactorX;
    private float mScaleFactorY;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private float degrees = 0;
    private boolean isOn = false;


    //////////////////////////////////////////////////
    private final float MAX_ROATE_DEGREE = 1.0f;
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor;
    private String mLocationProvider;
    private float mDirection;
    private float mTargetDirection;
    private AccelerateInterpolator mInterpolator;
    protected final Handler mHandler = new Handler();
    private Drawable compassD;
    private int offset = 0;

    public Compass(Context context)
    {
        super(context);
        init();
    }

    public Compass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Compass(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
            if (isOn) {
                if (mDirection != mTargetDirection) {

                    // calculate the short routine
                    float to = mTargetDirection + offset;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }

                    // limit the max speed to MAX_ROTATE_DEGREE
                    float distance = to - mDirection;
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROATE_DEGREE : (-1.0f * MAX_ROATE_DEGREE);
                    }

                    // need to slow down if the distance is short
                    mDirection = normalizeDegree(mDirection
                            + ((to - mDirection) * mInterpolator.getInterpolation(Math
                            .abs(distance) > MAX_ROATE_DEGREE ? 0.4f : 0.3f)));
                    postInvalidate();
                }
                mHandler.postDelayed(mCompassViewUpdater, 50);
            }
        }
    };

    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float direction = event.values[0] * -1.0f;
            mTargetDirection = normalizeDegree(direction);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private float normalizeDegree(float degree)
    {
        return (degree + 720) % 360;
    }

    public void compassOff()
    {
        if(!isOn)
        {
            return;
        }
        mHandler.removeCallbacks(mCompassViewUpdater);
        try
        {
            mSensorManager.unregisterListener(mOrientationSensorEventListener, mOrientationSensor);
        }catch (Exception e)
        {
        }
        mSensorManager = null;
        mOrientationSensor = null;
        mDirection = 0;
        postInvalidate();
        isOn = false;
    }

    public void compassOn()
    {
        if(isOn)
        {
            return;
        }
        initResources();
        initServices();
        if (mOrientationSensor != null) {
            mSensorManager.registerListener(mOrientationSensorEventListener, mOrientationSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        mHandler.postDelayed(mCompassViewUpdater, 50);
        isOn = true;
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
//        //Log.d(TAG, "widthSize: " + widthSize + " heightSize:" + heightSize
//                    + " finalMeasureSpecX:" + finalMeasureSpecX  + " finalMeasureSpecY:" + finalMeasureSpecY
//                    + " mWidth:" + mWidth  + " mHeight:" + mHeight
//                    + " mScaleFactorX:" + mScaleFactorX  + " finalMeasureSpecY:" + finalMeasureSpecY
//                    + " finalMeasureSpecX:" + finalMeasureSpecX  + " mScaleFactorY:" + mScaleFactorY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        convertRect(compass.getWidth(), compass.getHeight(), 0, 0, matrix);
        canvas.drawBitmap(compass, matrix, null);
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        compassOn();
    }

    private void initResources()
    {
        mDirection = 0.0f;
        mTargetDirection = 0.0f;
        mInterpolator = new AccelerateInterpolator();
    }

    private void initServices()
    {
        mSensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    private void convertRect(int bitmapWidth, int bitmapHeight, int left, int top, Matrix matrix)
    {
        matrix.reset();
        matrix.postRotate(mDirection, bitmapWidth / 2, bitmapHeight / 2);
        matrix.postScale(mScaleFactorX, mScaleFactorY);
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top);
    }

    protected void init() {
        offset = Settings.instance().getIntSetting(Constants.ORIENTATION) == Constants.ORIENTATION_PORTRAIT ? 0 : 90;
        compass = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
        matrix = new Matrix();
    }


    @Override
    public void onSensorChanged(int i, float[] floats)
    {

    }

    @Override
    public void onAccuracyChanged(int i, int i1)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
        SensorManager.getOrientation(mR, mOrientation);
        float azimuthInRadians = mOrientation[0];
        degrees = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
        invalidate();
    }

    @Override
    public void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        compassOff();
        if(!compass.isRecycled())
        {
            compass.recycle();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }
}
