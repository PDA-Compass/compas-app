package net.afterday.compas.app.pda.geiger

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import net.afterday.compas.app.R
import net.afterday.compas.engine.core.player.Player
import java.util.*

open class Tube : View {
    private val TAG = "Tube"

    private val WIDGET_WIDTH = 766
    private val WIDGET_HEIGHT = 335

    // Input stuff
    private var mRadiation = 0.0
    private var mAnomaly = 0.0
    private var mMental = 0.0
    private var mMonolith = 0.0
    private var mController = 0.0
    private var mBurer = 0.0
    private var mHealing = 0.0

    //private PlayerStatus mPlayerStatus = PlayerStatus.ALIVE;
    private var mZombified: Long = 0
    private val mControlled: Long = 0
    private var mEmission = false

    // Dimension stuff
    private var mWidth = 0
    private var mHeight = 0
    private var level = 0

    private var mScaleFactorX = 0f
    private var mScaleFactorY = 0f

    // Paint stuff
    private val mPaint: Paint
    private val mMatrix: Matrix
    private val mRect: RectF

    // Bitmaps
    private var mTubeOff: Bitmap
    private var mTubeDeadAno: Bitmap
    private var mTubeDeadMental: Bitmap
    private var mTubeDeadMisc: Bitmap
    private var mTubeDeadRad: Bitmap
    private var mTubeAnomaly: Bitmap
    private val mTubeBomb: Bitmap? = null
    private var mTubeBurer: Bitmap
    private var mTubeClear: Bitmap
    private var mTubeControlled: Bitmap
    private var mTubeController: Bitmap
    private var mTubeEmission: Bitmap
    private var mTubeDeadEmission: Bitmap
    private var mTubeHealing: Bitmap
    private var mTubeMental: Bitmap
    private var mTubeMonolith: Bitmap
    private var mTubeRad0: Bitmap
    private var mTubeRad1: Bitmap
    private var mTubeRad2: Bitmap
    private val mTubeVodka: Bitmap? = null
    private var mTubeZombified: Bitmap
    private var mTubeAbducted: Bitmap
    private var mTubeDying: Bitmap
    private var mTubeTransmutation: Bitmap
    private var mTubeUnknown: Bitmap
    private var currentState: Player.STATE
    private var fraction: Player.FRACTION? = null
    private var currentTube: Bitmap? = null

    private var bitmapsByState: MutableMap<Player.STATE, Bitmap> = HashMap()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        bindResources()
        bindStatesToImg()
        mZombified = System.currentTimeMillis()
        currentState = Player.STATE.ALIVE
        mMatrix = Matrix()
        mRect = RectF()
        mPaint = Paint()
        mPaint.setARGB(255, 100, 255, 100)

        mTubeOff = BitmapFactory.decodeResource(resources, R.drawable.tube_off)
        mTubeDeadAno = BitmapFactory.decodeResource(resources, R.drawable.tube_dead_ano)
        mTubeDeadMental = BitmapFactory.decodeResource(resources, R.drawable.tube_dead_mental)
        mTubeDeadMisc = BitmapFactory.decodeResource(resources, R.drawable.tube_dead_misc)
        mTubeDeadRad = BitmapFactory.decodeResource(resources, R.drawable.tube_dead_rad)
        mTubeAnomaly = BitmapFactory.decodeResource(resources, R.drawable.tube_anomaly)
        //mTubeBomb = BitmapFactory.decodeResource(getResources(), R.drawable.tube_bomb);
        mTubeBurer = BitmapFactory.decodeResource(resources, R.drawable.tube_burer)
        mTubeClear = BitmapFactory.decodeResource(resources, R.drawable.tube_clear)
        mTubeControlled = BitmapFactory.decodeResource(resources, R.drawable.tube_controlled)
        mTubeController = BitmapFactory.decodeResource(resources, R.drawable.tube_controller)
        mTubeEmission = BitmapFactory.decodeResource(resources, R.drawable.tube_emission)
        mTubeDeadEmission = BitmapFactory.decodeResource(resources, R.drawable.tube_dead_emi)
        mTubeHealing = BitmapFactory.decodeResource(resources, R.drawable.tube_healing)
        mTubeMental = BitmapFactory.decodeResource(resources, R.drawable.tube_mental)
        mTubeMonolith = BitmapFactory.decodeResource(resources, R.drawable.tube_monolith)
        mTubeRad0 = BitmapFactory.decodeResource(resources, R.drawable.tube_rad0)
        mTubeRad1 = BitmapFactory.decodeResource(resources, R.drawable.tube_rad1)
        mTubeRad2 = BitmapFactory.decodeResource(resources, R.drawable.tube_rad2)
        //mTubeVodka = BitmapFactory.decodeResource(getResources(), R.drawable.tube_vodka);
        mTubeZombified = BitmapFactory.decodeResource(resources, R.drawable.tube_zombified)
        mTubeAbducted = BitmapFactory.decodeResource(resources, R.drawable.tube_abducted)
        mTubeDying = BitmapFactory.decodeResource(resources, R.drawable.tube_dying)
        mTubeTransmutation = BitmapFactory.decodeResource(resources, R.drawable.tube_transmutation)
        mTubeUnknown = BitmapFactory.decodeResource(resources, R.drawable.tube_unknown)
    }

    open fun setParameters(
            radiation: Double,
            anomaly: Double,
            mental: Double,
            monolith: Double,
            controller: Double,
            burer: Double,
            healing: Double,
            playerState: Player.STATE
    ) {
        mRadiation = radiation
        mAnomaly = anomaly
        mMental = mental
        mMonolith = monolith
        mController = controller
        mBurer = burer
        mHealing = healing
        currentState = playerState
        //Log.d(TAG, "Radiation: " + mRadiation + " Healing: " + mHealing);
        repaintIfNeed()
    }

    open fun setEmission(emission: Boolean) {
        mEmission = emission
        repaintIfNeed()
    }

    open fun setFraction(fraction: Player.FRACTION?) {
        this.fraction = fraction
    }

    open fun setLevel(level: Int) {
        this.level = level
    }

    open fun setState(playerState: Player.STATE) {
        currentState = playerState
        repaintIfNeed()
    }

    private fun repaintIfNeed() {
        val t = getCurrentTube()
        Log.e(TAG, "t: $t")
        if (currentTube == t) {
            return
        }
        currentTube = t
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Get sizes
        val widthSize = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec)) - 300
        val finalMeasureSpecX = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        val finalMeasureSpecY = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        super.onMeasure(finalMeasureSpecX, finalMeasureSpecY)
        mWidth = widthSize
        mHeight = widthSize
        mScaleFactorX = mWidth.toFloat() / WIDGET_WIDTH
        mScaleFactorY = mScaleFactorX //(float) mHeight / WIDGET_HEIGHT;
        mPaint.textSize = 50 * mScaleFactorY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw $currentState")
        drawTube(canvas)
    }

    //Norint prideti nauja kolba, irasyti ja cia
    private fun bindResources() {

    }

    //Cia susieti kolba su atitinkama busena
    private fun bindStatesToImg() {
        bitmapsByState[Player.STATE.W_CONTROLLED] = mTubeTransmutation
        bitmapsByState[Player.STATE.W_DEAD_ANOMALY] = mTubeDying
        bitmapsByState[Player.STATE.W_DEAD_BURER] = mTubeDying
        bitmapsByState[Player.STATE.W_MENTALLED] = mTubeTransmutation
        bitmapsByState[Player.STATE.W_DEAD_RADIATION] = mTubeDying
        bitmapsByState[Player.STATE.DEAD_RADIATION] = mTubeDeadRad
        bitmapsByState[Player.STATE.DEAD_EMISSION] = mTubeDeadEmission
        bitmapsByState[Player.STATE.CONTROLLED] = mTubeControlled
        bitmapsByState[Player.STATE.MENTALLED] = mTubeZombified
        bitmapsByState[Player.STATE.DEAD_CONTROLLER] = mTubeDeadMental
        bitmapsByState[Player.STATE.DEAD_ANOMALY] = mTubeDeadAno
        bitmapsByState[Player.STATE.DEAD_MENTAL] = mTubeDeadMental
        bitmapsByState[Player.STATE.ALIVE] = mTubeClear
        bitmapsByState[Player.STATE.DEAD_BURER] = mTubeDeadMisc
        bitmapsByState[Player.STATE.ABDUCTED] = mTubeAbducted
        bitmapsByState[Player.STATE.W_ABDUCTED] = mTubeDying
    }

    protected open fun drawTube(canvas: Canvas) {
        if (currentTube != null) {
            convertRect(currentTube!!.width, currentTube!!.height, 0, 450, mMatrix)
            canvas.drawBitmap(currentTube!!, mMatrix, null)
        }
    }

    private fun getCurrentTube(): Bitmap? {
        if (fraction == Player.FRACTION.MONOLITH && mMonolith > 0.0) {
            return mTubeMonolith
        }
        if (fraction == Player.FRACTION.DARKEN && mRadiation > 0.0) {
            return mTubeRad0
        }
        if (mEmission && currentState.code == Player.ALIVE) {
            return mTubeEmission
        }
        if (currentState.code != Player.ALIVE) {
            return if (bitmapsByState.containsKey(currentState)) {
                bitmapsByState[currentState]
            } else {
                mTubeOff
            }
        } else if (currentState == Player.STATE.ABDUCTED) {
            return mTubeAbducted
        }

        // Healing tube
        if (mHealing > 0 && fraction != Player.FRACTION.MONOLITH) {
            return mTubeHealing
        }

//        if (mHealing > 0 && fraction != Player.FRACTION.DARKEN) {
//            return mTubeHealing;
//        }

        // Safe tube
        if (mRadiation < 0.01 && mAnomaly < 0.01 && mMental < 0.01 && mMonolith < 0.01 && mController < 0.01 && mBurer < 0.01) {
            return mTubeClear
        }

        // Zone tubes
        when (maxInfluence()) {
            0 -> {
                if (mRadiation >= 7.0) {
                    return mTubeRad2
                } else if (mRadiation >= 1.0) {
                    return mTubeRad1
                } else if (mRadiation >= 0) {
                    return mTubeRad0
                }
                return if (level >= 2) mTubeAnomaly else mTubeUnknown
            }
            1 -> return if (level >= 2) mTubeAnomaly else mTubeUnknown
            2 -> return if (level >= 3) mTubeMental else mTubeUnknown
            3 -> return if (level >= 4) mTubeController else mTubeUnknown
            4 -> return if (level >= 4) mTubeBurer else mTubeUnknown
            5 -> return mTubeMonolith
        }
        return mTubeOff
    }

    private fun maxInfluence(): Int {
        var max = 0.0
        var inf = -1
        if (mRadiation > max) {
            inf = 0
            max = mRadiation
        }
        if (mAnomaly > max) {
            inf = 1
            max = mAnomaly
        }
        if (mMental > max) {
            inf = 2
            max = mMental
        }
        if (mController > max) {
            inf = 3
            max = mController
        }
        if (mBurer > max) {
            inf = 4
            max = mBurer
        }
        if (mMonolith > max) {
            inf = 5
            max = mMonolith
        }
        return inf
    }

    private fun convertRect(bitmapWidth: Int, bitmapHeight: Int, left: Int, top: Int, matrix: Matrix?) {
        matrix!!.reset()
        matrix.postScale(mScaleFactorX, mScaleFactorY)
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top)
    }
}