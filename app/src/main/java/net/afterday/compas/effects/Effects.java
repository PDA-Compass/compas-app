package net.afterday.compas.effects;

import android.animation.ValueAnimator;
import android.os.HandlerThread;
import android.support.v4.util.Pair;
import android.view.animation.LinearInterpolator;

import net.afterday.compas.R;
import net.afterday.compas.core.gameState.Frame;
import net.afterday.compas.core.player.Impacts;
import net.afterday.compas.core.player.Player;
import net.afterday.compas.core.player.PlayerProps;
import net.afterday.compas.devices.DeviceProvider;
import net.afterday.compas.devices.sound.Sound;
import net.afterday.compas.devices.vibro.Vibro;
import net.afterday.compas.devices.vibro.VibroImpl;
import net.afterday.compas.engine.Engine;
import net.afterday.compas.engine.events.EmissionEventBus;
import net.afterday.compas.engine.events.ItemEventsBus;
import net.afterday.compas.LocalMainService;
import net.afterday.compas.engine.events.PlayerEventBus;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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
    private Timer springboardTimer = new Timer(true);
    private Timer funnelTimer = new Timer(true);
    private Timer carouselTimer = new Timer(true);
    private Timer elevatorTimer = new Timer(true);
    private Timer fryingTimer = new Timer(true);
    private Timer electraTimer = new Timer(true);
    private Timer meatgrinderTimer = new Timer(true);
    private Timer kisselTimer = new Timer(true);
    private Timer sodaTimer = new Timer(true);
    private Timer acidfogTimer = new Timer(true);
    private Timer burningfluffTimer = new Timer(true);
    private Timer rustyhairTimer = new Timer(true);
    private Timer spatialbubbleTimer = new Timer(true);
    private Disposable anomalySubsciption;
    private Disposable springboardSubsciption;
    private Disposable funnelSubsciption;
    private Disposable carouselSubsciption;
    private Disposable elevatorSubsciption;
    private Disposable fryingSubsciption;
    private Disposable electraSubsciption;
    private Disposable meatgrinderSubsciption;
    private Disposable kisselSubsciption;
    private Disposable sodaSubsciption;
    private Disposable acidfogSubsciption;
    private Disposable burningfluffSubsciption;
    private Disposable rustyhairSubsciption;
    private Disposable spatialbubbleSubsciption;
    private Disposable artefactSubsciption;
    private TimerTask playAnomalyClick = new TimerTask() {
        @Override
        public void run() {
            sound.playAnomalyClick();
        }
    };
    private TimerTask playSpringboardClick = new TimerTask() {
        @Override
        public void run() {
            sound.playSpringboardClick();
        }
    };
    private TimerTask playFunnelClick = new TimerTask() {
        @Override
        public void run() {
            sound.playFunnelClick();
        }
    };
    private TimerTask playCarouselClick = new TimerTask() {
        @Override
        public void run() {
            sound.playCarouselClick();
        }
    };
    private TimerTask playElevatorClick = new TimerTask() {
        @Override
        public void run() {
            sound.playElevatorClick();
        }
    };
    private TimerTask playFryingClick = new TimerTask() {
        @Override
        public void run() {
            sound.playFryingClick();
        }
    };
    private TimerTask playElectraClick = new TimerTask() {
        @Override
        public void run() {
            sound.playElectraClick();
        }
    };
    private TimerTask playMeatgrinderClick = new TimerTask() {
        @Override
        public void run() {
            sound.playMeatgrinderClick();
        }
    };
    private TimerTask playKisselClick = new TimerTask() {
        @Override
        public void run() {
            sound.playKisselClick();
        }
    };
    private TimerTask playSodaClick = new TimerTask() {
        @Override
        public void run() {
            sound.playSodaClick();
        }
    };
    private TimerTask playAcidfogClick = new TimerTask() {
        @Override
        public void run() {
            sound.playAcidfogClick();
        }
    };
    private TimerTask playBurningfluffClick = new TimerTask() {
        @Override
        public void run() {
            sound.playBurningfluffClick();
        }
    };
    private TimerTask playRustyhairClick = new TimerTask() {
        @Override
        public void run() {
            sound.playRustyhairClick();
        }
    };
    private TimerTask playSpatialbubbleClick = new TimerTask() {
        @Override
        public void run() {
            sound.playSpatialbubbleClick();
        }
    };

    // protected final Handler handler;

    protected Runnable radPlayer;
    private Scheduler scheduler;
    private Disposable playerStatesSubsciption,
                       playerLevelSubscription,
                       impactsStatesSubscription;
    private Vibro vibro;
    private Player.STATE currentState;
    //private boolean healingPlaying = false;

    public Effects(DeviceProvider deviceProvider)
    {
        this.deviceProvider = deviceProvider;
        this.sound = deviceProvider.getSoundPlayer();
        this.vibro = deviceProvider.getVibrator();
        HandlerThread handlerThread = new HandlerThread("backgroundThread");
        handlerThread.start();
        scheduler = AndroidSchedulers.from(handlerThread.getLooper());
        LocalMainService.getInstance().getFramesStream().observeOn(scheduler).subscribe((f) -> processFrame(f));
        ItemEventsBus.instance().getItemAddedEvents().subscribe((ia) ->{
                sound.playItemScanned (); vibro.vibrateTouch();});
        //Inventoriaus garsai ir vibro
        ItemEventsBus.instance().getItemUsedEvents().subscribe((ia) ->{
                sound.playInventoryUse (); vibro.vibrateTouch ();});
        ItemEventsBus.instance().getItemDroppedEvents().subscribe((ia) ->{
                sound.playInventoryDrop(); vibro.vibrateTouch ();});

        EmissionEventBus.instance().getEmissionStateStream().skip(1).withLatestFrom(PlayerEventBus.instance().getPlayerStateStream(), (e, ps) -> new Pair<Boolean, Player.STATE>(e, ps)).subscribe((s) -> {
            if(s.second.getCode() == Player.ALIVE)
            {
                if(s.first)
                {
                    sound.playEmissionStarts();
                    vibro.vibrateAlarm();
                }else
                {
                    sound.playEmissionEnds();
                    vibro.vibrateMessage();
                }
            }
        });
        EmissionEventBus.instance().getEmissionWarnings().withLatestFrom(PlayerEventBus.instance().getPlayerStateStream(), (e, ps) -> new Pair<Integer, Player.STATE>(e, ps)).subscribe((s) -> {
            if(s.second.getCode() == Player.ALIVE)
            {
                sound.playEmissionWarning();
                vibro.vibrateMessage();
            }
        });
        EmissionEventBus.instance().getFakeEmissions().withLatestFrom(PlayerEventBus.instance().getPlayerStateStream(), (e, ps) -> new Pair<Integer, Player.STATE>(e, ps)).subscribe((s) -> {
            if(s.second.getCode() == Player.ALIVE)
            {
                sound.playEmissionEnds();
                vibro.vibrateMessage();
            }
        });
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
                    vibro.vibrateHit();
                    break;
                case CONTROLLED:
                    sound.playControlled();
                    vibro.vibrateHit();
                    break;
                case ABDUCTED:
                    sound.playAbducted();
                    vibro.vibrateHit();
                    break;
                case DEAD_BURER:
					sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case DEAD_MENTAL:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_ABDUCTED:
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case W_MENTALLED:
                    sound.playMentalHit();
                    sound.playTransmutating();
                    vibro.vibrateW();
                    break;
                case W_DEAD_ANOMALY:
                    sound.playAnomalyDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_ANOMALY:
					sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_SPRINGBOARD:
                    sound.playSpringboardDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_SPRINGBOARD:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_FUNNEL:
                    sound.playFunnelDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_FUNNEL:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_CAROUSEL:
                    sound.playCarouselDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_CAROUSEL:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_ELEVATOR:
                    sound.playElevatorDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_ELEVATOR:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_FRYING:
                    sound.playFryingDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_FRYING:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_ELECTRA:
                    sound.playElectraDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_ELECTRA:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_MEATGRINDER:
                    sound.playMeatgrinderDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_MEATGRINDER:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_KISSEL:
                    sound.playKisselDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_KISSEL:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_SODA:
                    sound.playSodaDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_SODA:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_ACIDFOG:
                    sound.playAcidfogDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_ACIDFOG:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_BURNINGFLUFF:
                    sound.playBurningfluffDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_BURNINGFLUFF:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_RUSTYHAIR:
                    sound.playRustyhairDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_RUSTYHAIR:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_SPATIALBUBBLE:
                    sound.playSpatialbubbleDeath();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_SPATIALBUBBLE:
                    sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_CONTROLLED:
                    sound.playController();
                    sound.playTransmutating();
                    vibro.vibrateW();
                    break;
                case W_DEAD_BURER:
                    sound.playBurer();
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_CONTROLLER:
					sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case W_DEAD_RADIATION:
                    sound.playWTimer();
                    vibro.vibrateW();
                    break;
                case DEAD_RADIATION:
					sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
                case DEAD_EMISSION:
                    sound.playEmissionHit();
					sound.playBulbBreak();
                    sound.playDeath();
                    vibro.vibrateDeath();
                    break;
            }
        });
    }

    public void setImpactsStatesStream(Observable<Impacts.STATE> impactsStatesStream)
    {
//        impactsStatesSubscription = impactsStatesStream.subscribe((is) -> {
//            switch (is)
//            {
//                case HEALING: sound.playHealing();
//                default: sound.stopHealing();
//            }
//        });
    }


    public void setPlayerLevelStream(Observable<Integer> playerLevelStream)
    {
        if(playerLevelSubscription != null && !playerLevelSubscription.isDisposed())
        {
            playerLevelSubscription.dispose();
        }
        playerLevelSubscription = playerLevelStream.subscribe((l) -> {

            if (l == 1)
            {
                sound.playLevelUp();
                net.afterday.compas.logging.Logger.d(R.string.message_level_1);
            }
            if (l == 2)
            {
                sound.playLevelUp();
                net.afterday.compas.logging.Logger.d(R.string.message_level_2);
            }
            if (l == 3)
            {
                sound.playLevelUp();
                net.afterday.compas.logging.Logger.d(R.string.message_level_3);
            }
            if (l == 4)
            {
                sound.playLevelUp();
                net.afterday.compas.logging.Logger.d(R.string.message_level_4);
            }
            if (l == 5)
            {
                sound.playGlassBreak();
                net.afterday.compas.logging.Logger.d(R.string.message_level_max);
            }
//            else
//            {
//                sound.playLevelUp();
//            }
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
        vibro.vibrateDamage(frame.getPlayerProps());
        playHits(playerProps);
        playRad(playerProps);
        playController(playerProps);
        playAnomaly(playerProps);
        playSpringboard(playerProps);
        playFunnel(playerProps);
        playCarousel(playerProps);
        playElevator(playerProps);
        playFrying(playerProps);
        playElectra(playerProps);
        playMeatgrinder(playerProps);
        playKissel(playerProps);
        playSoda(playerProps);
        playAcidfog(playerProps);
        playBurningfluff(playerProps);
        playRustyhair(playerProps);
        playSpatialbubble(playerProps);
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
//           case Influence.SPRINGBOARD:
//                playSpringboard(playerProps);
//                break;
//           case Influence.FUNNEL:
//                playFunnel(playerProps);
//                break;
//           case Influence.CAROUSEL:
//                playCarousel(playerProps);
//                break;
//           case Influence.ELEVATOR:
//                playElevator(playerProps);
//                break;
//           case Influence.FRYING:
//                playFrying(playerProps);
//                break;
//           case Influence.ELECTRA:
//                playElectra(playerProps);
//                break;
//           case Influence.MEATGRINDER:
//                playMeatgrinder(playerProps);
//                break;
//           case Influence.KISSEL:
//                playKissel(playerProps);
//                break;
//           case Influence.SODA:
//                playSoda(playerProps);
//                break;
//           case Influence.ACIDFOG:
//                playAcidfog(playerProps);
//                break;
//           case Influence.BURNINGFLUFF:
//                playBurningfluff(playerProps);
//                break;
//           case Influence.RUSTYHAIR:
//                playRustyhair(playerProps);
//                break;
//           case Influence.SPATIALBUBBLE:
//                playSpatialbubble(playerProps);
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
        boolean hasHit = false;
        if(playerProps.anomalyHit())
        {
            hasHit = true;
            sound.playAnomalyDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_anomaly_hit);
        }
        if(playerProps.springboardHit())
        {
            hasHit = true;
            sound.playSpringboardDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_springboard_hit);
        }
        if(playerProps.funnelHit())
        {
            hasHit = true;
            sound.playFunnelDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_funnel_hit);
        }
        if(playerProps.carouselHit())
        {
            hasHit = true;
            sound.playCarouselDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_carousel_hit);
        }
        if(playerProps.elevatorHit())
        {
            hasHit = true;
            sound.playElevatorDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_elevator_hit);
        }
        if(playerProps.fryingHit())
        {
            hasHit = true;
            sound.playFryingDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_frying_hit);
        }
        if(playerProps.electraHit())
        {
            hasHit = true;
            sound.playElectraDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_electra_hit);
        }
        if(playerProps.meatgrinderHit())
        {
            hasHit = true;
            sound.playMeatgrinderDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_meatgrinder_hit);
        }
        if(playerProps.kisselHit())
        {
            hasHit = true;
            sound.playKisselDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_kissel_hit);
        }
        if(playerProps.sodaHit())
        {
            hasHit = true;
            sound.playSodaDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_soda_hit);
        }
        if(playerProps.acidfogHit())
        {
            hasHit = true;
            sound.playAcidfogDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_acidfog_hit);
        }
        if(playerProps.burningfluffHit())
        {
            hasHit = true;
            sound.playBurningfluffDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_burningfluff_hit);
        }
        if(playerProps.rustyhairHit())
        {
            hasHit = true;
            sound.playRustyhairDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_rustyhair_hit);
        }
        if(playerProps.spatialbubbleHit())
        {
            hasHit = true;
            sound.playSpatialbubbleDeath();
            net.afterday.compas.logging.Logger.e(R.string.message_spatialbubble_hit);
        }
        if(playerProps.burerHit())
        {
            hasHit = true;
            sound.playBurer();
            net.afterday.compas.logging.Logger.e(R.string.message_burer_hit);
        }
        if(playerProps.controllerHit())
        {
            hasHit = true;
            sound.playController();
            net.afterday.compas.logging.Logger.e(R.string.message_controller_hit);
        }
        if(playerProps.mentalHit())
        {
            hasHit = true;
            sound.playMentalHit();
            net.afterday.compas.logging.Logger.e(R.string.message_mental_hit);
        }
        if(playerProps.monolithHit())
        {
            hasHit = true;
            sound.playMonolithHit();
            net.afterday.compas.logging.Logger.d(R.string.message_monolit_call);
        }
        if(playerProps.emissionHit())
        {
            hasHit = true;
            sound.playEmissionHit();
            net.afterday.compas.logging.Logger.e(R.string.message_emission_hit);
        }
        if(hasHit && !(playerProps.mentalHit() && playerProps.getFraction() == Player.FRACTION.MONOLITH))
        {
            vibro.vibrateHit();
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
        if(playerProps.getMentalImpact() <= 0 || playerProps.getFraction() == Player.FRACTION.MONOLITH)
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

    private void playSpringboard(PlayerProps playerProps)
    {
        if(playerProps.getSpringboardImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getSpringboardImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(springboardSubsciption != null && !springboardSubsciption.isDisposed())
            {
                springboardSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            springboardSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playSpringboardClick());
            //springboardTimer.scheduleAtFixedRate(playSpringboardClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playFunnel(PlayerProps playerProps)
    {
        if(playerProps.getFunnelImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getFunnelImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(funnelSubsciption != null && !funnelSubsciption.isDisposed())
            {
                funnelSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            funnelSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playFunnelClick());
            //funnelTimer.scheduleAtFixedRate(playFunnelClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playCarousel(PlayerProps playerProps)
    {
        if(playerProps.getCarouselImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getCarouselImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(carouselSubsciption != null && !carouselSubsciption.isDisposed())
            {
                carouselSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            carouselSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playCarouselClick());
            //carouselTimer.scheduleAtFixedRate(playCarouselClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playElevator(PlayerProps playerProps)
    {
        if(playerProps.getElevatorImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getElevatorImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(elevatorSubsciption != null && !elevatorSubsciption.isDisposed())
            {
                elevatorSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            elevatorSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playElevatorClick());
            //elevatorTimer.scheduleAtFixedRate(playElevatorClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playFrying(PlayerProps playerProps)
    {
        if(playerProps.getFryingImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getFryingImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(fryingSubsciption != null && !fryingSubsciption.isDisposed())
            {
                fryingSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            fryingSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playFryingClick());
            //fryingTimer.scheduleAtFixedRate(playFryingClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playElectra(PlayerProps playerProps)
    {
        if(playerProps.getElectraImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getElectraImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(electraSubsciption != null && !electraSubsciption.isDisposed())
            {
                electraSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            electraSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playElectraClick());
            //electraTimer.scheduleAtFixedRate(playElectraClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playMeatgrinder(PlayerProps playerProps)
    {
        if(playerProps.getMeatgrinderImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getMeatgrinderImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(meatgrinderSubsciption != null && !meatgrinderSubsciption.isDisposed())
            {
                meatgrinderSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            meatgrinderSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playMeatgrinderClick());
            //meatgrinderTimer.scheduleAtFixedRate(playMeatgrinderClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playKissel(PlayerProps playerProps)
    {
        if(playerProps.getKisselImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getKisselImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(kisselSubsciption != null && !kisselSubsciption.isDisposed())
            {
                kisselSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            kisselSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playKisselClick());
            //kisselTimer.scheduleAtFixedRate(playKisselClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playSoda(PlayerProps playerProps)
    {
        if(playerProps.getSodaImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getSodaImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(sodaSubsciption != null && !sodaSubsciption.isDisposed())
            {
                sodaSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            sodaSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playSodaClick());
            //sodaTimer.scheduleAtFixedRate(playSodaClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playAcidfog(PlayerProps playerProps)
    {
        if(playerProps.getAcidfogImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getAcidfogImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(acidfogSubsciption != null && !acidfogSubsciption.isDisposed())
            {
                acidfogSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            acidfogSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playAcidfogClick());
            //acidfogTimer.scheduleAtFixedRate(playAcidfogClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playBurningfluff(PlayerProps playerProps)
    {
        if(playerProps.getBurningfluffImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getBurningfluffImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(burningfluffSubsciption != null && !burningfluffSubsciption.isDisposed())
            {
                burningfluffSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            burningfluffSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playBurningfluffClick());
            //burningfluffTimer.scheduleAtFixedRate(playBurningfluffClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playRustyhair(PlayerProps playerProps)
    {
        if(playerProps.getRustyhairImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getRustyhairImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(rustyhairSubsciption != null && !rustyhairSubsciption.isDisposed())
            {
                rustyhairSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            rustyhairSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playRustyhairClick());
            //rustyhairTimer.scheduleAtFixedRate(playRustyhairClick, to, to);
        }else
        {
            playUnknown(playerProps);
        }
    }
    private void playSpatialbubble(PlayerProps playerProps)
    {
        if(playerProps.getSpatialbubbleImpact() <= 0)
        {
            return;
        }

        int level = playerProps.getLevel();
        double strength = playerProps.getSpatialbubbleImpact();
        if(level >= 2 && playerProps.getState().getCode() == Player.ALIVE)
        {
            if(spatialbubbleSubsciption != null && !spatialbubbleSubsciption.isDisposed())
            {
                spatialbubbleSubsciption.dispose();
            }
            int to = Math.max(100, Engine.TICK_MILLISECONDS / (int) Math.ceil(strength));
            spatialbubbleSubsciption = Observable.interval(to, TimeUnit.MILLISECONDS).take(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS).subscribe((t) -> sound.playSpatialbubbleClick());
            //spatialbubbleTimer.scheduleAtFixedRate(playSpatialbubbleClick, to, to);
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
