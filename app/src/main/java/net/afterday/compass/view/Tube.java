package net.afterday.compass.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

//import net.afterday.compass.Player;

import net.afterday.compass.R;
import net.afterday.compass.core.player.Player;
import net.afterday.compass.core.player.Player.STATE;
//import net.afterday.compass.core.player.PlayerStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Tube extends View {
    private static final String TAG = "Tube";

    private static final int WIDGET_WIDTH = 766;
    private static final int WIDGET_HEIGHT = 335;

    // Input stuff
    private double mRadiation;
    private double mAnomaly;
    private double mMental;
    private double mController;
    private double mBurer;
    private double mHealing;
    //private PlayerStatus mPlayerStatus = PlayerStatus.ALIVE;
    private long mZombified;
    private long mControlled;
    private boolean mEmission;

    // Dimension stuff
    private int mWidth;
    private int mHeight;
    private int level;

    private float mScaleFactorX;
    private float mScaleFactorY;

    // Paint stuff
    private Paint mPaint;
    private Matrix mMatrix;
    private RectF mRect;

    // Bitmaps
    private Bitmap mTubeOff;
    private Bitmap mTubeDeadAno;
    private Bitmap mTubeDeadMental;
    private Bitmap mTubeDeadMisc;
    private Bitmap mTubeDeadRad;
    private Bitmap mTubeAnomaly;
    private Bitmap mTubeBomb;
    private Bitmap mTubeBurer;
    private Bitmap mTubeClear;
    private Bitmap mTubeControlled;
    private Bitmap mTubeController;
    private Bitmap mTubeEmission;
    private Bitmap mTubeHealing;
    private Bitmap mTubeMental;
    private Bitmap mTubeRad0;
    private Bitmap mTubeRad1;
    private Bitmap mTubeRad2;
    private Bitmap mTubeVodka;
    private Bitmap mTubeZombified;
    private Bitmap mTubeAbducted;
    private Bitmap mTubeDying;
    private Bitmap mTubeTransmutation;
    private Bitmap mTubeUnknown;
    private STATE currentState;

    private Map<STATE, Bitmap> bitmapsByState;

    public Tube(Context context) {
        super(context);
        init();
    }

    public Tube(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Tube(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setParameters(
            double radiation,
            double anomaly,
            double mental,
            double controller,
            double burer,
            double healing,
            STATE playerState,
            boolean emission
    ) {
        mRadiation = radiation;
        mAnomaly = anomaly;
        mMental = mental;
        mController = controller;
        mBurer = burer;
        mHealing = healing;
        mEmission = emission;
        currentState = playerState;
        //Log.d(TAG, "Radiation: " + mRadiation + " Healing: " + mHealing);
        invalidate();
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setState(STATE playerState)
    {
        currentState = playerState;
        invalidate();
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

        mPaint.setTextSize(50 * mScaleFactorY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw " + currentState);
        drawTube(canvas);
    }

    protected void init() {
        bindResources();
        bindStatesToImg();

        mZombified = System.currentTimeMillis();
        currentState = STATE.ALIVE;
        mMatrix = new Matrix();
        mRect = new RectF();
        mPaint = new Paint();
        mPaint.setARGB(255,100,255,100);
    }

    //Norint prideti nauja kolba, irasyti ja cia
    private void bindResources()
    {
        mTubeOff = BitmapFactory.decodeResource(getResources(), R.drawable.tube_off);
        mTubeDeadAno = BitmapFactory.decodeResource(getResources(), R.drawable.tube_dead_ano);
        mTubeDeadMental = BitmapFactory.decodeResource(getResources(), R.drawable.tube_dead_mental);
        mTubeDeadMisc = BitmapFactory.decodeResource(getResources(), R.drawable.tube_dead_misc);
        mTubeDeadRad = BitmapFactory.decodeResource(getResources(), R.drawable.tube_dead_rad);
        mTubeAnomaly = BitmapFactory.decodeResource(getResources(), R.drawable.tube_anomaly);
        mTubeBomb = BitmapFactory.decodeResource(getResources(), R.drawable.tube_bomb);
        mTubeBurer = BitmapFactory.decodeResource(getResources(), R.drawable.tube_burer);
        mTubeClear = BitmapFactory.decodeResource(getResources(), R.drawable.tube_clear);
        mTubeControlled = BitmapFactory.decodeResource(getResources(), R.drawable.tube_controlled);
        mTubeController = BitmapFactory.decodeResource(getResources(), R.drawable.tube_controller);
        mTubeEmission = BitmapFactory.decodeResource(getResources(), R.drawable.tube_emission);
        mTubeHealing = BitmapFactory.decodeResource(getResources(), R.drawable.tube_healing);
        mTubeMental = BitmapFactory.decodeResource(getResources(), R.drawable.tube_mental);
        mTubeRad0 = BitmapFactory.decodeResource(getResources(), R.drawable.tube_rad0);
        mTubeRad1 = BitmapFactory.decodeResource(getResources(), R.drawable.tube_rad1);
        mTubeRad2 = BitmapFactory.decodeResource(getResources(), R.drawable.tube_rad2);
        mTubeVodka = BitmapFactory.decodeResource(getResources(), R.drawable.tube_vodka);
        mTubeZombified = BitmapFactory.decodeResource(getResources(), R.drawable.tube_zombified);
        mTubeAbducted = BitmapFactory.decodeResource(getResources(), R.drawable.tube_abducted);
        mTubeDying = BitmapFactory.decodeResource(getResources(), R.drawable.tube_dying);
        mTubeTransmutation = BitmapFactory.decodeResource(getResources(), R.drawable.tube_transmutation);
        mTubeUnknown = BitmapFactory.decodeResource(getResources(), R.drawable.tube_unknown);
    }

    //Cia susieti kolba su atitinkama busena
    private void bindStatesToImg()
    {
        bitmapsByState = new HashMap<>();
        bitmapsByState.put(STATE.W_CONTROLLED, mTubeTransmutation);
        bitmapsByState.put(STATE.W_DEAD_ANOMALY, mTubeDying);
        bitmapsByState.put(STATE.W_DEAD_BURER, mTubeDying);
        bitmapsByState.put(STATE.W_MENTALLED, mTubeTransmutation);
        bitmapsByState.put(STATE.W_DEAD_RADIATION, mTubeDying);
        bitmapsByState.put(STATE.DEAD_RADIATION, mTubeDeadRad);
        bitmapsByState.put(STATE.CONTROLLED, mTubeControlled);
        bitmapsByState.put(STATE.MENTALLED, mTubeZombified);
        bitmapsByState.put(STATE.DEAD_CONTROLLER, mTubeDeadMental);
        bitmapsByState.put(STATE.DEAD_ANOMALY, mTubeDeadAno);
        bitmapsByState.put(STATE.DEAD_MENTAL, mTubeDeadMental);
        bitmapsByState.put(STATE.ALIVE, mTubeClear);
        bitmapsByState.put(STATE.DEAD_BURER, mTubeDeadMisc);
        bitmapsByState.put(STATE.ABDUCTED, mTubeAbducted);
        bitmapsByState.put(STATE.W_ABDUCTED, mTubeDying);
    }

    protected void drawTube(Canvas canvas) {

        convertRect(mTubeOff.getWidth(), mTubeOff.getHeight(), 0, 0, mMatrix);

        if(currentState.getCode() != Player.ALIVE)
        {
            if(bitmapsByState.containsKey(currentState))
            {
                canvas.drawBitmap(bitmapsByState.get(currentState), mMatrix, null);
            }else
            {
                canvas.drawBitmap(mTubeOff, mMatrix, null);
            }
            return;
        }else if(currentState == STATE.ABDUCTED)
        {
            canvas.drawBitmap(mTubeAbducted, mMatrix, null);
            return;
        }

        // Emission tube
        if (mEmission) {
            canvas.drawBitmap(mTubeEmission, mMatrix, null);
            return;
        }

        // Healing tube
        if (mHealing > 0) {
            canvas.drawBitmap(mTubeHealing, mMatrix, null);
            return;
        }

        // Safe tube
        if (
                mRadiation < 0.01d
                && mAnomaly < 0.01d
                && mMental < 0.01d
                && mController < 0.01d
                && mBurer < 0.01d
                && mHealing < 0.01d
        ) {
            canvas.drawBitmap(mTubeClear, mMatrix, null);
            return;
        }

        // Zone tubes
        int maxInf = maxInfluence();
        switch (maxInf) {
            case 0: // Radiation
                if (mRadiation >= 7d) {
                    canvas.drawBitmap(mTubeRad2, mMatrix, null);
                    return;
                }
                else if (mRadiation >= 1d) {
                    canvas.drawBitmap(mTubeRad1, mMatrix, null);
                    return;
                }
                else if (mRadiation >= 0) {
                    canvas.drawBitmap(mTubeRad0, mMatrix, null);
                    return;
                }
                break;
            case 1: // Anomaly
                //canvas.drawBitmap(mTubeAnomaly, mMatrix, null);
                canvas.drawBitmap(level >= 2 ? mTubeAnomaly : mTubeUnknown, mMatrix, null);
                return;
            case 2: // Mental
                //canvas.drawBitmap(mTubeMental, mMatrix, null);
                canvas.drawBitmap(level >= 3 ? mTubeMental : mTubeUnknown, mMatrix, null);
                return;
            case 3: // Controller
                //canvas.drawBitmap(mTubeController, mMatrix, null);
                canvas.drawBitmap(level >= 4 ? mTubeController : mTubeUnknown, mMatrix, null);
                return;
            case 4: // Burer
                //canvas.drawBitmap(mTubeBurer, mMatrix, null);
                canvas.drawBitmap(level >= 4 ? mTubeBurer : mTubeUnknown, mMatrix, null);
                return;
        }

        // Draw default tube
        canvas.drawBitmap(mTubeOff, mMatrix, null);
    }

    private int maxInfluence() {
        double max = 0d;
        int inf = -1;

        if (mRadiation > max) {
            inf = 0;
            max = mRadiation;
        }
        if (mAnomaly > max) {
            inf = 1;
            max = mAnomaly;
        }
        if (mMental > max) {
            inf = 2;
            max = mMental;
        }
        if (mController > max) {
            inf = 3;
            max = mController;
        }
        if (mBurer > max) {
            inf = 4;
            max = mBurer;
        }
        if (mHealing > max) {
            inf = 5;
            max = mHealing;
        }

        return inf;
    }

    private void drawTimer(Canvas canvas)
    {
        long timeLeft = 100000;
        //Log.d(TAG, "DRAW TIMER");
//        if (mZombified > 0L) {
//            timeLeft = mZombified + 10 - System.currentTimeMillis(); //GameImpl thread
//            mPaint.setARGB(255,100,255,100);
//        }
//        else if (mControlled > 0L) {
//            timeLeft = mControlled + 10 - System.currentTimeMillis(); //GameImpl thread
//            mPaint.setARGB(255,255,255,100);
//        }
        mPaint.setColor(Color.WHITE);
//        String text = String.format(
//                "%02d:%02d",
//                TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60,
//                TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60
//        );
        String text = "00:55";
        mPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(text, 383 * mScaleFactorX, 70 * mScaleFactorY, mPaint);
    }

    private void convertRect(int bitmapWidth, int bitmapHeight, int left, int top, Matrix matrix)
    {
        matrix.reset();
        matrix.postScale(mScaleFactorX, mScaleFactorY);
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top);
    }
}
