package net.afterday.compass.engine;

import android.util.Log;

import net.afterday.compass.core.Controls;
import net.afterday.compass.core.CountDown;
import net.afterday.compass.core.Game;
import net.afterday.compass.core.GameImpl;
import net.afterday.compass.core.events.PlayerEventsListener;
import net.afterday.compass.core.gameState.Frame;
import net.afterday.compass.core.gameState.State;
import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.core.inventory.items.Events.ItemAdded;
import net.afterday.compass.core.player.Impacts;
import net.afterday.compass.core.player.Player;
import net.afterday.compass.core.userActions.UserActionsPack;
import net.afterday.compass.devices.DeviceProvider;
import net.afterday.compass.effects.Effects;
import net.afterday.compass.engine.counter.CounterEvent;
import net.afterday.compass.engine.events.ItemEventsBus;
import net.afterday.compass.engine.events.PlayerEventBus;
import net.afterday.compass.engine.influences.InfluenceProvider;
import net.afterday.compass.engine.influences.InfluenceProviderImpl;
import net.afterday.compass.engine.threading.Threads;
import net.afterday.compass.persistency.PersistencyProvider;
import net.afterday.compass.persistency.items.ItemsPersistency;
import net.afterday.compass.sensors.SensorsProvider;
import net.afterday.compass.serialization.SharedPrefsSerializer;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 1/28/2018.
 */

public class Engine
{
    public static final int TICK_MILLISECONDS = 1000;
    private static final String TAG = "Engine";
    private static final int NUMBER_OF_CPUS = 4; //TODO: nustatyti branduoliu skaiciu
    private static final int SEC30 = 3 * 10;
    private static final int MIN1 = 6 * 10;
    private static final int MIN5 = 30 * 10;
    private static final int MIN30 = 180 * 10;
    private static final int MIN60 = 360 * 10;

    private static final String START = "Start";

    private static Engine instance;

    private Observable<Long> ticks;
    private Observable<Long> secondsLeft = BehaviorSubject.createDefault((long)-1);
    private SensorsProvider sensorsProvider;
    private PersistencyProvider persistencyProvider;
    private AtomicBoolean isStarted = new AtomicBoolean(false);
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private Subject<Boolean> acceptsInfluences = BehaviorSubject.create();
    private Subject<Integer> playerLevel = BehaviorSubject.create();
    private Subject<ItemAdded> itemAdded = PublishSubject.create();
    private Subject<Player.STATE> currentPlayerState = BehaviorSubject.create();
    private Subject<Long> countdownStarted = BehaviorSubject.create();
    private Observable<UserActionsPack> userActionsStream;
    private Observable<Impacts.STATE> impactsStatesStream = PublishSubject.create();
    private Subject<CounterEvent> counterEventsStream = BehaviorSubject.create();
    private Subject<Frame> framesStream = BehaviorSubject.create();
    private Subject<String> commands = PublishSubject.create();
    private Observable<String> startCommands;
    private Game game;
    private Scheduler computation;
    private InfluenceProvider<InfluencesPack> influenceProvider;
    private Observable<InfluencesPack> influencesStream;
    private Observable<Boolean> running = BehaviorSubject.create();
    private Subject<State> gameStates = BehaviorSubject.<State>create();
    private Observable<Long> gameRunning;
    private ItemEventsBus itemEventsBus;
    private DeviceProvider deviceProvider;
    private Effects effects;
    private Controls controls;
    private CountDown countDown;

    private Engine()
    {
        controls = new ControlsImpl();
        countDown = new CountDownImpl();
        //game = new GameImpl(controls, countDown);
        itemEventsBus = ItemEventsBus.instance();
        gameStates.onNext(State.NOT_STARTED);
        computation = Threads.computation();
        ticks = Observable.interval(0, TICK_MILLISECONDS, TimeUnit.MILLISECONDS);
        gameRunning = gameStates.filter((gs) -> gs == State.STARTED).switchMap((st) -> st == State.NOT_STARTED ? Observable.empty() : ticks);//.subscribe((l) -> {//Log.d(TAG, "Wooooorks");});
        //gameRunning.subscribe((g) -> Log.d(TAG, "RRRRRR"));
        startCommands = this.commands.filter((cmd) -> cmd == START);

        startCommands
                .observeOn(computation)
                .take(1)
                .map((cmd) -> this.initializeGame())
                    .subscribe(this::startGame);



    }

    public void start()
    {
        //Log.d(TAG, "start " + Thread.currentThread().getName());
        String errorMsg = null;
        if(sensorsProvider == null)
        {
            errorMsg = "SensorsProvider not set.";
        }
        if(persistencyProvider == null)
        {
            errorMsg = "Persistency provider not set.";
        }
        if(userActionsStream == null)
        {
            errorMsg = "User actions stream not set.";
        }
        if(errorMsg != null)
        {
            throw new IllegalStateException(errorMsg);
        }
        commands.onNext(START);
    }

    public Observable<Frame> getFramesStream()
    {
        return framesStream;
    }

    public Observable<CounterEvent> getCounterEventsStream()
    {
        return null;
    }

    public ItemEventsBus getItemEventsBus()
    {
        return itemEventsBus;
    }

    //TODO: padaryti krovima is persistencies asinchronisku
    private Game initializeGame()
    {
        game = new GameImpl(controls, persistencyProvider, SharedPrefsSerializer.instance());
        game.getPlayer().addPlayerEventsListener(new PlayerEventsListenerImpl());
        playerLevel.onNext(game.getPlayer().getPlayerProps().getLevel());
        currentPlayerState.onNext(game.getPlayer().getPlayerProps().getState());
        influenceProvider = new InfluenceProviderImpl(sensorsProvider, this.persistencyProvider.getInfluencesPersistency());
        influencesStream = influenceProvider.getInfluenceStream();

        effects = new Effects(deviceProvider);
        effects.setPlayerStatesStream(currentPlayerState);
        effects.setPlayerLevelStream(playerLevel);
        effects.setImpactsStatesStream(impactsStatesStream);

        return game;
    }

    private void startGame(Game game)
    {
        gameRunning.withLatestFrom(acceptsInfluences, (n, a) -> a)
                   .filter(a -> a)
                   .withLatestFrom(influencesStream, (n, i) -> i)
                   .observeOn(computation)
                    //.doOnNext((p) -> this.soundPlayer.test())
                   //.map((inflPacks) -> scanner.makeInfluences(inflPacks))
                    //.subscribe((inf) -> {});
                   .subscribe((inf) -> this.framesStream.onNext(game.acceptInfluences(inf)));

        itemEventsBus.getAddItemEvents().observeOn(computation).subscribe((code) -> game.getInventory().addItem(code));//.map((c) -> itemsProvider.getItemByCode(c).orElse(new NonExistingItem()));
        itemEventsBus.getDropItemEvents().observeOn(computation).subscribe((item) -> game.getPlayer().dropItem(item));

        itemEventsBus.getUserItemsRequests().subscribe((r) -> {
            //Log.d(TAG, "user items requested!");
            itemEventsBus.userItemsLoaded(game.getInventory());
        });
        itemEventsBus.getUseItemRequests().subscribe((r) -> {


            framesStream.onNext(game.useItem(r));
        });
        setupSuicides();
        //Consumer<Long> updater = (i) -> ((Subject<Long>)secondsLeft).onNext(MIN5 - i);
        //Observable<Long> controlledCountDown = currentPlayerState.filter((ps) -> ps == Player.STATE.DEAD_CONTROLLER).switchMap((ps) -> gameRunning).withLatestFrom(countdownStarted, (g, s) -> (System.currentTimeMillis() - s) / 1000);//.filter((t) -> t < 10000).subscribe((r) -> Log.d(TAG, "WOOOOOOOORKS " + r / 1000));
        //Consumer<Long> updater5Min = makeUpdater(MIN5);
//        Observable<Long> deadControllerCountDown = makeCountDownFor(Player.STATE.W_CONTROLLED);
//
//        countUntil(deadControllerCountDown, MIN5, true).doOnNext((t) -> Log.d(TAG, "1. W_CONTROLLED: " + t)).subscribe((t) -> ((Subject<Long>)secondsLeft).onNext(MIN5 - t));
//        countUntil(deadControllerCountDown, MIN5, false).doOnNext((t) -> Log.d(TAG, "2. W_CONTROLLED: " + t)).subscribe((t) -> {game.getPlayer().setState(Player.STATE.CONTROLLED);});
//
//        Observable<Long> controlledCoundDown = makeCountDownFor(Player.STATE.CONTROLLED);
//        countUntil(controlledCoundDown, MINS60, true).doOnNext((t) -> Log.d(TAG, "1. ----CONTROLLED: " + t)).subscribe((t) -> ((Subject<Long>)secondsLeft).onNext(MINS60 - t));
//        countUntil(controlledCoundDown, MINS60, false).doOnNext((t) -> Log.d(TAG, "1. ----CONTROLLED: " + t)).subscribe((t) -> {game.getPlayer().setState(Player.STATE.DEAD_CONTROLLER);});
//
//        Observable<Long> wMentalled = makeCountDownFor(Player.STATE.W_MENTALLED);
//        countUntil(wMentalled, MIN5, true).subscribe((t) -> ((Subject<Long>)secondsLeft).onNext(MIN5 - t));
//        countUntil(wMentalled, MIN5, false).subscribe((t) -> {((Subject<Long>)secondsLeft).onNext((long)-1); game.getPlayer().setState(Player.STATE.MENTALLED);});
//
//        Observable<Long> mentalled = makeCountDownFor(Player.STATE.MENTALLED);
//        countUntil(mentalled, MINS60, true).subscribe((t) -> ((Subject<Long>)secondsLeft).onNext(MINS60 - t));
//        countUntil(mentalled, MINS60, false)
//                .subscribe((t) -> {
//                                    ((Subject<Long>)secondsLeft).onNext((long)-1);
//                                    game.getPlayer().setState(Player.STATE.DEAD_MENTAL);
//        });

        Disposable stateTimers = setupStateTimers();

        //countUntil(cou)
//                deadControllerCountDown.filter((t) -> t <= MIN5).subscribe((t) -> ((Subject<Long>)secondsLeft).onNext(MIN5 - t));
//        deadControllerCountDown.filter((t) -> t > MIN5).subscribe((t) -> {countdownStarted.onNext(System.currentTimeMillis()); currentPlayerState.onNext(Player.STATE.CONTROLLED);});
//        Observable<Long> controlledCountDown = makeCountDownFor(Player.STATE.CONTROLLED);

        Frame frame = game.start();
        acceptsInfluences.onNext(true);
        this.gameStates.onNext(State.STARTED);
    }

    private Disposable setupStateTimers()
    {
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(makeCountDownForStates(Player.STATE.W_CONTROLLED, SEC30, Player.STATE.CONTROLLED, MIN1, Player.STATE.DEAD_CONTROLLER));
        cd.add(makeCountDownForStates(Player.STATE.W_MENTALLED, SEC30, Player.STATE.MENTALLED, MIN1, Player.STATE.DEAD_MENTAL));
        cd.add(makeCountDownForStates(Player.STATE.W_DEAD_ANOMALY, SEC30, Player.STATE.DEAD_ANOMALY));
        cd.add(makeCountDownForStates(Player.STATE.W_DEAD_BURER, SEC30, Player.STATE.DEAD_BURER));
        cd.add(makeCountDownForStates(Player.STATE.W_DEAD_RADIATION, SEC30, Player.STATE.DEAD_RADIATION));
        cd.add(makeCountDownForStates(Player.STATE.W_ABDUCTED, SEC30, Player.STATE.DEAD_BURER));
        cd.add(makeCountDownForStates(Player.STATE.ABDUCTED, MIN1, Player.STATE.ALIVE));
        return cd;
    }

    private Disposable makeCountDownForStates(Player.STATE state1, long time1, Player.STATE finalState)
    {
        CompositeDisposable cd = new CompositeDisposable();
        Observable<Long> stateCountDown = makeCountDownFor(state1);
        cd.add(stateCountDown.subscribe((t) -> {
            if(t <= time1)
            {
                ((Subject<Long>)secondsLeft).onNext(time1 - t);
            }else
            {
                game.getPlayer().setState(finalState);
                ((Subject<Long>)secondsLeft).onNext(time1 - t);
            }
        }));
        return cd;
    }

    private Disposable setupSuicides()
    {
        Observable<String> suicideRequests = PlayerEventBus.instance().getPlayerCommandsStream().filter((c) -> c == PlayerEventBus.SUICIDE);
        suicideRequests.withLatestFrom(currentPlayerState, (s, cs) -> cs)
                .observeOn(computation)
                .subscribe((cs) -> {
                    if(cs.getSuicideType() != Player.SUICIDE_NOT_ALLOWED)
                    {
                        switch (cs)
                        {
                            case ALIVE:
                                framesStream.onNext(game.getPlayer().setState(Player.STATE.W_ABDUCTED));
                                break;
                            case W_ABDUCTED:
                                game.getPlayer().setState(Player.STATE.ABDUCTED);
                                break;
                            case ABDUCTED:
                                game.getPlayer().setState(Player.STATE.DEAD_BURER);
                                break;
                            case CONTROLLED:
                            case MENTALLED:
                                game.getPlayer().setState(Player.STATE.DEAD_BURER);
                                break;
                        }
                    }
                });
        return null;
    }

    private Disposable makeCountDownForStates(Player.STATE state1, long time1, Player.STATE state2, long time2, Player.STATE finalState)
    {
        CompositeDisposable cd = new CompositeDisposable();
        Observable<Long> state1CountDown = makeCountDownFor(state1);
        cd.add(state1CountDown.subscribe((t) -> {
            if(t <= time1)
            {
                ((Subject<Long>)secondsLeft).onNext(time1 - t);
            }else
            {
                game.getPlayer().setState(state2);
                ((Subject<Long>)secondsLeft).onNext(time1 - t);
            }
        }));
        Observable<Long> state2CountDown = makeCountDownFor(state2);
        cd.add(state2CountDown.subscribe((t) -> {
            if(t <= time2)
            {
                ((Subject<Long>)secondsLeft).onNext(time2 - t);
            }else
            {
                game.getPlayer().setState(finalState);
                ((Subject<Long>)secondsLeft).onNext(time1 - t);
            }
        }));
        return cd;
    }

    private Observable<Long> countUntil(Observable<Long> stream, long seconds, boolean runnning)
    {
        if(runnning)
        {
            return stream.filter((t) -> t <= seconds);
        }else
        {
            return stream.filter((t) -> t > seconds);//.take(1);
        }
    }

    private Observable<Long> makeCountDownFor(Player.STATE state)
    {
        return currentPlayerState.switchMap((ps) -> ps == state ? gameRunning : Observable.empty()).withLatestFrom(countdownStarted, (g, s) -> (System.currentTimeMillis() - s) / 1000);
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public void setSensorsProvider(SensorsProvider sensorsProvider)
    {
        this.sensorsProvider = sensorsProvider;
    }

    public void setPersistencyProvider(PersistencyProvider provider)
    {
        this.persistencyProvider = provider;
    }

    public void setUserActionsStream(Observable<UserActionsPack> userActionsObservable)
    {
        userActionsStream = userActionsObservable;
    }

    public void setDevideProvider(DeviceProvider devideProvider)
    {
        this.deviceProvider = devideProvider;
    }

    public Observable<Player.STATE> getPlayerStateStream()
    {
        return currentPlayerState;
    }

    public Observable<ItemAdded> getItemAddedStream()
    {
        return itemAdded;
    }

    public static Engine instance()
    {
        if(instance == null)
        {
            instance = new Engine();
        }
        return instance;
    }

    public Observable<Long> getCountDownStream()
    {
        return secondsLeft;
    }
    public Observable<Integer> getPlayerLevelStream()
    {
        return playerLevel;
    }

    private class ControlsImpl implements Controls
    {

        @Override
        public void stopInfluences()
        {
            influenceProvider.stop();
            acceptsInfluences.onNext(false);
        }

        @Override
        public void stopInfluencesFor(long miliseconds)
        {

        }

        @Override
        public void startInfluences()
        {
            influenceProvider.start();
            acceptsInfluences.onNext(true);
        }
    }

    private class CountDownImpl implements CountDown
    {
        @Override
        public void startCountDown(Player.STATE playerState)
        {

        }

        @Override
        public void stopCountDown()
        {

        }
    }

    private class PlayerEventsListenerImpl implements PlayerEventsListener
    {

        @Override
        public void onPlayerStateChanged(Player.STATE oldState, Player.STATE newState)
        {
            Log.d(TAG, "onPlayerStateChanged " + newState);
//            if(newState == Player.STATE.HEALING)
//            {
//                currentPlayerState.onNext(newState);
//            }
            if(newState == Player.STATE.W_DEAD_BURER)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_BURER)
            {
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                //net.afterday.compass.logging.Log.d(R.string.message_dead);
                //countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(newState);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_CONTROLLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.CONTROLLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_CONTROLLER)
            {
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                currentPlayerState.onNext(Player.STATE.DEAD_CONTROLLER);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_MENTALLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.MENTALLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_MENTAL)
            {
                currentPlayerState.onNext(Player.STATE.DEAD_MENTAL);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_DEAD_ANOMALY)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(Player.STATE.W_DEAD_ANOMALY);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_ANOMALY)
            {
                currentPlayerState.onNext(Player.STATE.DEAD_ANOMALY);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.ALIVE)
            {
                currentPlayerState.onNext(Player.STATE.ALIVE);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_DEAD_RADIATION)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(Player.STATE.W_DEAD_RADIATION);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_RADIATION)
            {
                currentPlayerState.onNext(Player.STATE.DEAD_RADIATION);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_ABDUCTED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(Player.STATE.W_ABDUCTED);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.ABDUCTED)
            {
                game.getPlayer().getPlayerProps().addHealth(100);
                countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(Player.STATE.ABDUCTED);
                controls.startInfluences();
                return;
            }
        }

        @Override
        public void onImpactsStateChanged(Impacts.STATE oldState, Impacts.STATE newState)
        {
            ((Subject)impactsStatesStream).onNext(newState);
        }

        @Override
        public void onItemAdded(ItemAdded itemAdded)
        {
            Engine.this.itemAdded.onNext(itemAdded);
        }

        @Override
        public void onPlayerLevelChanged(int level)
        {
            Engine.this.playerLevel.onNext(level);
        }

    }
}
