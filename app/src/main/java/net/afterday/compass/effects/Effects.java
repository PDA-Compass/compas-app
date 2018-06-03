package net.afterday.compass.effects;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import net.afterday.compass.StalkerApp;
import net.afterday.compass.core.gameState.Frame;
import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.core.player.Impacts;
import net.afterday.compass.core.player.Player;
import net.afterday.compass.core.player.PlayerProps;
import net.afterday.compass.devices.DeviceProvider;
import net.afterday.compass.devices.sound.Sound;
import net.afterday.compass.engine.Engine;
import net.afterday.compass.engine.events.ItemEventsBus;

import org.reactivestreams.Subscription;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by spaka on 5/3/2018.
 */

public class Effects
{
    private static final String TAG = "Effects";
    private DeviceProvider deviceProvider;
    private Sound sound;
    private ValueAnimator soundAnimator;
    private Timer anomalyTimer = new Timer(true);
    private Disposable anomalySubsciption;
    private Disposable artefactSubsciption;
    private TimerTask playAnomalyClick = new TimerTask() {
        @Override
        public void run() {
            sound.playAnomalyClick();
        }
    };

   // protected final Handler handler;

    protected Runnable radPlayer;
    private Scheduler scheduler;
    private Disposable playerStatesSubsciption,
                       playerLevelSubscription,
                       impactsStatesSubscription;
    private Player.STATE currentState;
    //private boolean healingPlaying = false;

    public Effects(DeviceProvider deviceProvider)
    {
        this.deviceProvider = deviceProvider;
        this.sound = deviceProvider.getSoundPlayer();


        //Looper.prepare();
//        handler = new Handler();
//        radPlayer = new Runnable() {
//        @Override
//        public void run() {
//            double rand = Math.random();
//            double probability = Math.min(50 / 17d, 0.9d);
//            if (rand <= probability) {
//                sound.playRadClick();
//            }
//        }
//    };
        HandlerThread handlerThread = new HandlerThread("backgroundThread");
        handlerThread.start();
        scheduler = AndroidSchedulers.from(handlerThread.getLooper());
        StalkerApp.getInstance().getFramesStream().observeOn(scheduler).subscribe((f) -> processFrame(f));
        ItemEventsBus.instance().getItemAddedEvents().subscribe((ia) -> sound.playItemScanned());
    }

    public void setPlayerStatesStream(Observable<Player.STATE> statesStream)
    {
        if(playerStatesSubsciption != null && !playerStatesSubsciption.isDisposed())
        {
            playerStatesSubsciption.dispose();
        }
        playerStatesSubsciption = statesStream.observeOn(scheduler).subscribe((s) -> {
//            if(currentState == Player.STATE.HEALING && s != Player.STATE.HEALING)
//            {
//                sound.stopHealing();
//            }
            switch (s)
            {
                case ALIVE: break;
                case MENTALLED:
                    sound.playZombify();
                    break;
                case CONTROLLED:
                    sound.playControlled();
                    break;
                case ABDUCTED:
                    sound.playAbducted();
                    break;
                case DEAD_BURER:
                    sound.playDeath();
                    break;
                case DEAD_MENTAL:
                    sound.playDeath();
                    break;
                case W_ABDUCTED:
                    sound.playWTimer();
                    break;
                case W_MENTALLED:
                    sound.playTransmutating();
                    break;
                case W_DEAD_ANOMALY:
                    sound.playWTimer();
                    break;
                case DEAD_ANOMALY:
                    sound.playDeath();
                    break;
                case W_CONTROLLED:
                    sound.playTransmutating();
                    break;
                case W_DEAD_BURER:
                    sound.playWTimer();
                    break;
                case DEAD_CONTROLLER:
                    sound.playDeath();
                    break;
                case DEAD_RADIATION:
                    sound.playDeath();
                    break;
            }
        });
    }

    public void setImpactsStatesStream(Observable<Impacts.STATE> impactsStatesStream)
    {
        impactsStatesSubscription = impactsStatesStream.subscribe((is) -> {
            switch (is)
            {
                case HEALING: sound.playHealing();
                default: sound.stopHealing();
            }
        });
    }

    public void setPlayerLevelStream(Observable<Integer> playerLevelStream)
    {
        if(playerLevelSubscription != null && !playerStatesSubsciption.isDisposed())
        {
            playerStatesSubsciption.dispose();
        }
        playerStatesSubsciption = playerLevelStream.subscribe((l) -> {
            if(l == 5)
            {
                sound.playGlassBreak();
            }else
            {
                sound.playLevelUp();
            }
        });
    }

    private void processFrame(Frame frame)
    {
        PlayerProps playerProps = frame.getPlayerProps();

        if(playerProps.getState().getCode() != Player.ALIVE || playerProps.getHealthImpact() > 0)
        {
            return;
        }
        soundAnimator = ValueAnimator.ofInt(0,60);
        playHits(playerProps);
        playRad(playerProps);
        playController(playerProps);
        playAnomaly(playerProps);
        playArtefact(playerProps);
        playMental(playerProps);
        playBurer(playerProps);
//        switch (maxImpactCode)
//        {
//            case Influence.RADIATION:
//                playRad(playerProps.getRadiationImpact());
//                break;
//            case Influence.ANOMALY:
//                playAnomaly(playerProps);
//                break;
//            case Influence.MENTAL:
//                playMental(playerProps);
//                break;
////            case Influence.BURER:
////                playBurer(playerProps);
////                break;
//            case Influence.CONTROLLER:
//                playController(playerProps);
//        }
        return;
    }

    private void playHits(PlayerProps playerProps)
    {

        if(playerProps.anomalyHit())
        {
            sound.playAnomalyDeath();
        }
        if(playerProps.burerHit())
        {
            sound.playBurer();
        }
        if(playerProps.controllerHit())
        {
            sound.playController();
        }
        if(playerProps.mentalHit())
        {
            sound.playMentalHit();
        }
    }

    private void playRad(PlayerProps playerProps)
    {
        double strength = playerProps.getRadiationImpact();
        if(strength == 0)
        {
            return;
        }
        soundAnimator.cancel();
        //Log.d("Sound", "Play!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + Thread.currentThread().getName());
        soundAnimator.setDuration(Engine.TICK_MILLISECONDS);
        soundAnimator.setInterpolator(new LinearInterpolator());
        soundAnimator.addUpdateListener((v) -> {
            double rand = Math.random();
            double probability = Math.min(strength / 17d, 0.9d);
            if (rand <= probability) {
                sound.playRadClick();
            }
        });
        soundAnimator.start();
    }

    private void playMental(PlayerProps playerProps)
    {
        if(playerProps.getMentalImpact() <= 0)
        {
            return;
        }
        if(playerProps.getLevel() >= 3)
        {
            //soundAnimator.cancel();
            sound.playMental();

        }
    }

    private void playBurer(PlayerProps playerProps)
    {
        if(playerProps.getBurerImpact() <= 0)
        {
            return;
        }
        //soundAnimator.cancel();
        //sound.playBurer();
        double strength = playerProps.getBurerImpact();
        int level = playerProps.getLevel();
        if(level >= 4)
        {
            //soundAnimator.cancel();
            sound.playBurerPresence();
        }else
        {
            playUnknown(playerProps);
        }
    }

    private void playController(PlayerProps playerProps)
    {
        if(playerProps.getControllerImpact() <= 0)
        {
            return;
        }
        double strength = playerProps.getControllerImpact();
        int level = playerProps.getLevel();
        if(level >= 4)
        {
            //soundAnimator.cancel();
            sound.playControllerPresence();
        }else
        {
            playUnknown(playerProps);
        }
    }

    private void playAnomaly(PlayerProps playerProps)
    {
        if(playerProps.getAnomalyImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getAnomalyImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(anomalySubsciption != null && !anomalySubsciption.isDisposed())
            {
                anomalySubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            anomalySubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playAnomalyClick());
            //anomalyTimer.scheduleAtFixedRate(playAnomalyClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }

    private void playArtefact(PlayerProps playerProps)
    {
        if(playerProps.getArtefactImpact() <= 0)
        {
            return;
        }
        int level = playerProps.getLevel();
        double strength = playerProps.getArtefactImpact();
        if(level >= 5 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(artefactSubsciption != null && !artefactSubsciption.isDisposed())
            {
                artefactSubsciption.dispose();
            }
            int to = Math.max(1000, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            artefactSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playArtefact());
        }
    }

    private void playUnknown(PlayerProps playerProps)
    {
//        soundAnimator.cancel();
//        sound.playAnomalyDeath();
    }


}
