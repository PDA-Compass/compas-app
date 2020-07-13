package net.afterday.compas.app.pda.geiger

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.hardware.*
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import net.afterday.compas.app.R
import net.afterday.compas.app.settings.Constants
import net.afterday.compas.app.settings.Settings

open class Compass : View, SensorListener, SensorEventListener {
    private val TAG = "Compass"

    private val WIDGET_WIDTH = 1030
    private val mAccelerometer: Sensor? = null
    private val mMagnetometer: Sensor? = null
    private var mMatrix: Matrix
    private var compass: Bitmap

    // Dimension stuff
    private var mWidth = 0

    private var mScaleFactorX = 0f
    private var mScaleFactorY = 0f

    private val mLastAccelerometer = FloatArray(3)
    private val mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private val mR = FloatArray(9)
    private val mOrientation = FloatArray(3)
    private val mCurrentDegree = 0f
    private var degrees = 0f
    private var isOn = false


    //////////////////////////////////////////////////
    private val MAX_ROATE_DEGREE = 1.0f
    private var mSensorManager: SensorManager? = null
    private var mOrientationSensor: Sensor? = null
    private val mLocationProvider: String? = null
    private var mDirection = 0f
    private var mTargetDirection = 0f
    private var mInterpolator: AccelerateInterpolator? = null
    protected val mHandler = Handler()
    private val compassD: Drawable? = null
    private var offset = 0


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        offset = if (Settings.instance().getIntSetting(Constants.ORIENTATION) == Constants.ORIENTATION_PORTRAIT) 0 else 90
        compass = BitmapFactory.decodeResource(resources, R.drawable.compass)
        mMatrix = Matrix()
    }

    protected var mCompassViewUpdater: Runnable = object : Runnable {
        override fun run() {
            if (isOn) {
                if (mDirection != mTargetDirection) {

                    // calculate the short routine
                    var to = mTargetDirection + offset
                    if (to - mDirection > 180) {
                        to -= 360f
                    } else if (to - mDirection < -180) {
                        to += 360f
                    }

                    // limit the max speed to MAX_ROTATE_DEGREE
                    var distance = to - mDirection
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = if (distance > 0) MAX_ROATE_DEGREE else -1.0f * MAX_ROATE_DEGREE
                    }

                    // need to slow down if the distance is short
                    mDirection = normalizeDegree(mDirection
                            + (to - mDirection) * mInterpolator!!.getInterpolation(if (Math
                                    .abs(distance) > MAX_ROATE_DEGREE) 0.4f else 0.3f))
                    postInvalidate()
                }
                mHandler.postDelayed(this, 50)
            }
        }
    }

    private val mOrientationSensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val direction = event.values[0] * -1.0f
            mTargetDirection = normalizeDegree(direction)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private  fun normalizeDegree(degree: Float): Float {
        return (degree + 720) % 360
    }

    open fun compassOff() {
        if (!isOn) {
            return
        }
        mHandler.removeCallbacks(mCompassViewUpdater)
        try {
            mSensorManager!!.unregisterListener(mOrientationSensorEventListener, mOrientationSensor)
        } catch (e: Exception) {
        }
        mSensorManager = null
        mOrientationSensor = null
        mDirection = 0f
        postInvalidate()
        isOn = false
    }

    open fun compassOn() {
        if (isOn) {
            return
        }
        initResources()
        initServices()
        if (mOrientationSensor != null) {
            mSensorManager!!.registerListener(mOrientationSensorEventListener, mOrientationSensor,
                    SensorManager.SENSOR_DELAY_GAME)
        }
        mHandler.postDelayed(mCompassViewUpdater, 50)
        isOn = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Get sizes
        val widthSize = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
        val finalMeasureSpecX = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        val finalMeasureSpecY = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        super.onMeasure(finalMeasureSpecX, finalMeasureSpecY)
        mWidth = widthSize
        mScaleFactorX = mWidth.toFloat() / WIDGET_WIDTH
        mScaleFactorY = mScaleFactorX
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        convertRect(compass.width, compass.height, 0, 0, mMatrix)
        canvas.drawBitmap(compass, mMatrix, null)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        compassOn()
    }

    private fun initResources() {
        mDirection = 0.0f
        mTargetDirection = 0.0f
        mInterpolator = AccelerateInterpolator()
    }

    private fun initServices() {
        mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mOrientationSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION)
    }

    private fun convertRect(bitmapWidth: Int, bitmapHeight: Int, left: Int, top: Int, matrix: Matrix) {
        matrix.reset()
        matrix.postRotate(mDirection, bitmapWidth / 2.toFloat(), bitmapHeight / 2.toFloat())
        matrix.postScale(mScaleFactorX, mScaleFactorY)
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top)
    }

    override fun onSensorChanged(i: Int, floats: FloatArray?) {}

    override fun onAccuracyChanged(i: Int, i1: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.size)
            mLastAccelerometerSet = true
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.size)
            mLastMagnetometerSet = true
        }
        SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer)
        SensorManager.getOrientation(mR, mOrientation)
        val azimuthInRadians = mOrientation[0]
        degrees = (Math.toDegrees(azimuthInRadians.toDouble()) + 360).toFloat() % 360
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compassOff()
        if (!compass.isRecycled) {
            compass.recycle()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}
}