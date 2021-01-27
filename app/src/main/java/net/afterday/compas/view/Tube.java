package net.afterday.compas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

//import net.afterday.compas.Player;

import net.afterday.compas.R;
import net.afterday.compas.core.player.Player;
import net.afterday.compas.core.player.Player.STATE;
//import net.afterday.compas.core.player.PlayerStatus;

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
    private double mSpringboard;
    private double mFunnel;
    private double mCarousel;
    private double mElevator;
    private double mFrying;
    private double mElectra;
    private double mMeatgrinder;
    private double mKissel;
    private double mSoda;
    private double mAcidfog;
    private double mBurningfluff;
    private double mRustyhair;
    private double mSpatialbubble;
    private double mMental;
    private double mMonolith;
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
    private Bitmap mTubeSpringboard;
    private Bitmap mTubeFunnel;
    private Bitmap mTubeCarousel;
    private Bitmap mTubeElevator;
    private Bitmap mTubeFrying;
    private Bitmap mTubeElectra;
    private Bitmap mTubeMeatgrinder;
    private Bitmap mTubeKissel;
    private Bitmap mTubeSoda;
    private Bitmap mTubeAcidfog;
    private Bitmap mTubeBurningfluff;
    private Bitmap mTubeRustyhair;
    private Bitmap mTubeSpatialbubble;
    private Bitmap mTubeBomb;
    private Bitmap mTubeBurer;
    private Bitmap mTubeClear;
    private Bitmap mTubeControlled;
    private Bitmap mTubeController;
    private Bitmap mTubeEmission;
    private Bitmap mTubeDeadEmission;
    private Bitmap mTubeHealing;
    private Bitmap mTubeMental;
    private Bitmap mTubeMonolith;
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
    private Player.FRACTION fraction;
    private Bitmap currentTube;

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
            double springboard,
            double funnel,
            double carousel,
            double elevator,
            double frying,
            double electra,
            double meatgrinder,
            double kissel,
            double soda,
            double acidfog,
            double burningfluff,
            double rustyhair,
            double spatialbubble,
            double mental,
            double monolith,
            double controller,
            double burer,
            double healing,
            STATE playerState
    ) {
        mRadiation = radiation;
        mAnomaly = anomaly;
        mSpringboard = springboard;
        mFunnel = funnel;
        mCarousel = carousel;
        mElevator = elevator;
        mFrying = frying;
        mElectra = electra;
        mMeatgrinder = meatgrinder;
        mKissel = kissel;
        mSoda = soda;
        mAcidfog = acidfog;
        mBurningfluff = burningfluff;
        mRustyhair = rustyhair;
        mSpatialbubble = spatialbubble;
        mMental = mental;
        mMonolith = monolith;
        mController = controller;
        mBurer = burer;
        mHealing = healing;
        currentState = playerState;
        //Log.d(TAG, "Radiation: " + mRadiation + " Healing: " + mHealing);
        repaintIfNeed();
    }

    public void setEmission(boolean emission)
    {
        this.mEmission = emission;
        repaintIfNeed();
    }

    public void setFraction(Player.FRACTION fraction)
    {
        this.fraction = fraction;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setState(STATE playerState)
    {
        currentState = playerState;
        repaintIfNeed();
    }

    private void repaintIfNeed()
    {
        Bitmap t = getCurrentTube();
        Log.e(TAG, "t: " + t);
        if(currentTube == t)
        {
            return;
        }
        currentTube = t;
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
        mTubeSpringboard = BitmapFactory.decodeResource(getResources(), R.drawable.tube_springboard);
        mTubeFunnel = BitmapFactory.decodeResource(getResources(), R.drawable.tube_funnel);
        mTubeCarousel = BitmapFactory.decodeResource(getResources(), R.drawable.tube_carousel);
        mTubeElevator = BitmapFactory.decodeResource(getResources(), R.drawable.tube_elevator);
        mTubeFrying = BitmapFactory.decodeResource(getResources(), R.drawable.tube_frying);
        mTubeElectra = BitmapFactory.decodeResource(getResources(), R.drawable.tube_electra);
        mTubeMeatgrinder = BitmapFactory.decodeResource(getResources(), R.drawable.tube_meatgrinder);
        mTubeKissel = BitmapFactory.decodeResource(getResources(), R.drawable.tube_kissel);
        mTubeSoda = BitmapFactory.decodeResource(getResources(), R.drawable.tube_soda);
        mTubeAcidfog = BitmapFactory.decodeResource(getResources(), R.drawable.tube_acidfog);
        mTubeBurningfluff = BitmapFactory.decodeResource(getResources(), R.drawable.tube_burningfluff);
        mTubeRustyhair = BitmapFactory.decodeResource(getResources(), R.drawable.tube_rustyhair);
        mTubeSpatialbubble = BitmapFactory.decodeResource(getResources(), R.drawable.tube_spatialbubble);
        //mTubeBomb = BitmapFactory.decodeResource(getResources(), R.drawable.tube_bomb);
        mTubeBurer = BitmapFactory.decodeResource(getResources(), R.drawable.tube_burer);
        mTubeClear = BitmapFactory.decodeResource(getResources(), R.drawable.tube_clear);
        mTubeControlled = BitmapFactory.decodeResource(getResources(), R.drawable.tube_controlled);
        mTubeController = BitmapFactory.decodeResource(getResources(), R.drawable.tube_controller);
        mTubeEmission = BitmapFactory.decodeResource(getResources(), R.drawable.tube_emission);
        mTubeDeadEmission = BitmapFactory.decodeResource(getResources(), R.drawable.tube_dead_emi);
        mTubeHealing = BitmapFactory.decodeResource(getResources(), R.drawable.tube_healing);
        mTubeMental = BitmapFactory.decodeResource(getResources(), R.drawable.tube_mental);
        mTubeMonolith = BitmapFactory.decodeResource(getResources(), R.drawable.tube_monolith);
        mTubeRad0 = BitmapFactory.decodeResource(getResources(), R.drawable.tube_rad0);
        mTubeRad1 = BitmapFactory.decodeResource(getResources(), R.drawable.tube_rad1);
        mTubeRad2 = BitmapFactory.decodeResource(getResources(), R.drawable.tube_rad2);
        //mTubeVodka = BitmapFactory.decodeResource(getResources(), R.drawable.tube_vodka);
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
        bitmapsByState.put(STATE.W_DEAD_SPRINGBOARD, mTubeDying);
        bitmapsByState.put(STATE.DEAD_SPRINGBOARD, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_FUNNEL, mTubeDying);
        bitmapsByState.put(STATE.DEAD_FUNNEL, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_CAROUSEL, mTubeDying);
        bitmapsByState.put(STATE.DEAD_CAROUSEL, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_ELEVATOR, mTubeDying);
        bitmapsByState.put(STATE.DEAD_ELEVATOR, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_FRYING, mTubeDying);
        bitmapsByState.put(STATE.DEAD_FRYING, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_ELECTRA, mTubeDying);
        bitmapsByState.put(STATE.DEAD_ELECTRA, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_MEATGRINDER, mTubeDying);
        bitmapsByState.put(STATE.DEAD_MEATGRINDER, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_KISSEL, mTubeDying);
        bitmapsByState.put(STATE.DEAD_KISSEL, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_SODA, mTubeDying);
        bitmapsByState.put(STATE.DEAD_SODA, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_ACIDFOG, mTubeDying);
        bitmapsByState.put(STATE.DEAD_ACIDFOG, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_BURNINGFLUFF, mTubeDying);
        bitmapsByState.put(STATE.DEAD_BURNINGFLUFF, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_RUSTYHAIR, mTubeDying);
        bitmapsByState.put(STATE.DEAD_RUSTYHAIR, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_SPATIALBUBBLE, mTubeDying);
        bitmapsByState.put(STATE.DEAD_SPATIALBUBBLE, mTubeDeadAno);
        bitmapsByState.put(STATE.W_DEAD_BURER, mTubeDying);
        bitmapsByState.put(STATE.W_MENTALLED, mTubeTransmutation);
        bitmapsByState.put(STATE.W_DEAD_RADIATION, mTubeDying);
        bitmapsByState.put(STATE.DEAD_RADIATION, mTubeDeadRad);
        bitmapsByState.put(STATE.DEAD_EMISSION, mTubeDeadEmission);
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
        if(currentTube == null)
        {
            return;
        }
        convertRect(currentTube.getWidth(), currentTube.getHeight(), 0, 0, mMatrix);
        canvas.drawBitmap(currentTube, mMatrix, null);
    }

    private Bitmap getCurrentTube()
    {

        if(fraction == Player.FRACTION.MONOLITH && mMonolith > 0d)
        {
            return mTubeMonolith;
        }

        if(fraction == Player.FRACTION.DARKEN && mRadiation > 0d)
        {
            return mTubeRad0;
        }

        if (mEmission && currentState.getCode() == Player.ALIVE) {
            return mTubeEmission;
        }

        if(currentState.getCode() != Player.ALIVE)
        {
            if(bitmapsByState.containsKey(currentState))
            {
                return bitmapsByState.get(currentState);
            }else
            {
                return mTubeOff;
            }
        }else if(currentState == STATE.ABDUCTED)
        {
            return mTubeAbducted;
        }

        // Healing tube
        if (mHealing > 0 && fraction != Player.FRACTION.MONOLITH) {
            return mTubeHealing;
        }

//        if (mHealing > 0 && fraction != Player.FRACTION.DARKEN) {
//            return mTubeHealing;
//        }

        // Safe tube
        if (
                mRadiation < 0.01d
                        && mAnomaly < 0.01d
                        && mMental < 0.01d
                        && mSpringboard < 0.01d
                        && mFunnel < 0.01d
                        && mCarousel < 0.01d
                        && mElevator < 0.01d
                        && mFrying < 0.01d
                        && mElectra < 0.01d
                        && mMeatgrinder < 0.01d
                        && mKissel < 0.01d
                        && mSoda < 0.01d
                        && mAcidfog < 0.01d
                        && mBurningfluff < 0.01d
                        && mRustyhair < 0.01d
                        && mSpatialbubble < 0.01d
                        && mMonolith < 0.01d
                        && mController < 0.01d
                        && mBurer < 0.01d
                ) {
            return mTubeClear;
        }

        // Zone tubes
        int maxInf = maxInfluence();
        switch (maxInf) {
            case 0: // Radiation
                if (mRadiation >= 7d) {
                    return mTubeRad2;
                }
                else if (mRadiation >= 1d) {
                    return mTubeRad1;
                }
                else if (mRadiation >= 0) {
                    return mTubeRad0;
                }
            case 1: // Anomaly
                //canvas.drawBitmap(mTubeAnomaly, mMatrix, null);
                return level >= 2 ? mTubeAnomaly : mTubeUnknown;
            case 2: // Springboard
                //canvas.drawBitmap(mTubeSpringboard, mMatrix, null);
                return level >= 2 ? mTubeSpringboard : mTubeUnknown;
            case 3: // Funnel
                //canvas.drawBitmap(mFunnel, mMatrix, null);
                return level >= 2 ? mTubeFunnel : mTubeUnknown;
            case 4: // Carousel
                //canvas.drawBitmap(mTubeCarousel, mMatrix, null);
                return level >= 2 ? mTubeCarousel : mTubeUnknown;
            case 5: // Elevator
                //canvas.drawBitmap(mTubeElevator, mMatrix, null);
                return level >= 2 ? mTubeElevator : mTubeUnknown;
            case 6: // Frying
                //canvas.drawBitmap(mTubeFrying, mMatrix, null);
                return level >= 2 ? mTubeFrying : mTubeUnknown;
            case 7: // Electra
                //canvas.drawBitmap(mTubeElectra, mMatrix, null);
                return level >= 2 ? mTubeElectra : mTubeUnknown;
            case 8: // Meatgrinder
                //canvas.drawBitmap(mTubeMeatgrinder, mMatrix, null);
                return level >= 2 ? mTubeMeatgrinder : mTubeUnknown;
            case 9: // Kissel
                //canvas.drawBitmap(mTubeKissel, mMatrix, null);
                return level >= 2 ? mTubeKissel : mTubeUnknown;
            case 10: // Soda
                //canvas.drawBitmap(mTubeSoda, mMatrix, null);
                return level >= 2 ? mTubeSoda : mTubeUnknown;
            case 11: // Acidfog
                //canvas.drawBitmap(mTubeAcidfog, mMatrix, null);
                return level >= 2 ? mTubeAcidfog : mTubeUnknown;
            case 12: // Burningfluff
                //canvas.drawBitmap(mTubeBurningfluff, mMatrix, null);
                return level >= 2 ? mTubeBurningfluff : mTubeUnknown;
            case 13: // Rustyhair
                //canvas.drawBitmap(mTubeRustyhair, mMatrix, null);
                return level >= 2 ? mTubeRustyhair : mTubeUnknown;
            case 14: // Spatialbubble
                //canvas.drawBitmap(mTubeSpatialbubble, mMatrix, null);
                return level >= 2 ? mTubeSpatialbubble : mTubeUnknown;
            case 15: // Mental
                return level >= 3 ? mTubeMental : mTubeUnknown;
            case 16: // Controller
                //canvas.drawBitmap(mTubeController, mMatrix, null);
                return level >= 4 ? mTubeController : mTubeUnknown;
            case 17: // Burer
                //canvas.drawBitmap(mTubeBurer, mMatrix, null);
                return level >= 4 ? mTubeBurer : mTubeUnknown;
            case 18: // Monolith
                return mTubeMonolith;
        }
        return mTubeOff;
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
        if (mSpringboard > max) {
            inf = 1;
            max = mSpringboard;
        }
        if (mFunnel > max) {
            inf = 1;
            max = mFunnel;
        }
        if (mCarousel > max) {
            inf = 1;
            max = mCarousel;
        }
        if (mElevator > max) {
            inf = 1;
            max = mElevator;
        }
        if (mFrying > max) {
            inf = 1;
            max = mFrying;
        }
        if (mElectra > max) {
            inf = 1;
            max = mElectra;
        }
        if (mMeatgrinder > max) {
            inf = 1;
            max = mMeatgrinder;
        }
        if (mKissel > max) {
            inf = 1;
            max = mKissel;
        }
        if (mSoda > max) {
            inf = 1;
            max = mSoda;
        }
        if (mAcidfog > max) {
            inf = 1;
            max = mAcidfog;
        }
        if (mBurningfluff > max) {
            inf = 1;
            max = mBurningfluff;
        }
        if (mRustyhair > max) {
            inf = 1;
            max = mRustyhair;
        }
        if (mSpatialbubble > max) {
            inf = 1;
            max = mSpatialbubble;
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
        if (mMonolith > max) {
            inf = 5;
            max = mMonolith;
        }
        return inf;
    }

    private void convertRect(int bitmapWidth, int bitmapHeight, int left, int top, Matrix matrix)
    {
        matrix.reset();
        matrix.postScale(mScaleFactorX, mScaleFactorY);
        matrix.postTranslate(mScaleFactorX * left, mScaleFactorY * top);
    }
}
