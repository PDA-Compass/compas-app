package net.afterday.compas.engine;

import android.util.Log;
import android.util.Pair;

import com.google.gson.JsonObject;

import net.afterday.compas.R;
import net.afterday.compas.core.Controls;
import net.afterday.compas.core.CountDown;
import net.afterday.compas.core.Game;
import net.afterday.compas.core.GameImpl;
import net.afterday.compas.core.events.PlayerEventsListener;
import net.afterday.compas.core.gameState.Frame;
import net.afterday.compas.core.gameState.State;
import net.afterday.compas.core.influences.Emission;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Impacts;
import net.afterday.compas.core.player.Player;
import net.afterday.compas.core.serialization.Jsonable;
import net.afterday.compas.core.serialization.Serializer;
import net.afterday.compas.core.userActions.UserActionsPack;
import net.afterday.compas.devices.DeviceProvider;
import net.afterday.compas.effects.Effects;
import net.afterday.compas.engine.actions.Action;
import net.afterday.compas.engine.actions.ActionsExecutor;
import net.afterday.compas.engine.events.CodeInputEventBus;
import net.afterday.compas.engine.events.EmissionEventBus;
import net.afterday.compas.engine.events.ItemEventsBus;
import net.afterday.compas.engine.events.PlayerEventBus;
import net.afterday.compas.engine.influences.InfluenceProvider;
import net.afterday.compas.engine.influences.InfluenceProviderImpl;
import net.afterday.compas.engine.influences.InfluencesController;
import net.afterday.compas.engine.threading.Threads;
import net.afterday.compas.persistency.PersistencyProvider;
import net.afterday.compas.persistency.items.ItemDescriptor;
import net.afterday.compas.sensors.SensorsProvider;
import net.afterday.compas.serialization.SharedPrefsSerializer;
import net.afterday.compas.logging.Logger;
import net.afterday.compas.util.Triple;

import java.util.Calendar;
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

public class Engine implements Jsonable
{
    public static final int TICK_MILLISECONDS = 1000;
    private static final String COUNTDOWN = "COUNTDOWN";
    private static final String TAG = "Engine";
    private static final int NUMBER_OF_CPUS = 4;
    private static final int SEC30 = 30;
    private static final int MIN1 = 60;
    private static final int MIN5 = 60 * 5;
    private static final int MIN30 = 60 * 30;
    private static final int MIN60 = 60 * 60;

    private static final String START = "Start";

    private static Engine instance;

    private Observable<Long> ticks;
    private Observable<Long> secondsLeft = BehaviorSubject.createDefault((long) -1);
    private SensorsProvider sensorsProvider;
    private PersistencyProvider persistencyProvider;

    private Subject<Boolean> acceptsInfluences = BehaviorSubject.create();
    private Subject<Integer> playerLevel = BehaviorSubject.create();
    private Subject<ItemAdded> itemAdded = PublishSubject.create();
    private Subject<Player.STATE> currentPlayerState = PlayerEventBus.instance().getPlayerStateStream();
    private Subject<Long> countdownStarted = BehaviorSubject.create();
    private Observable<Impacts.STATE> impactsStatesStream = PublishSubject.create();
    private Subject<Frame> framesStream = BehaviorSubject.create();
    private Subject<Player.FRACTION> fractionStream = PlayerEventBus.instance().getPlayerFractionStream();
    private Subject<String> commands = PublishSubject.create();
    private Observable<String> startCommands;
    private Game game;
    private Scheduler computation;
    private InfluencesController influenceProvider;
    private Observable<InfluencesPack> influencesStream;
    private Subject<State> gameStates = BehaviorSubject.<State>create();
    private Observable<Long> gameRunning;
    private ItemEventsBus itemEventsBus;
    private DeviceProvider deviceProvider;
    private Effects effects;
    private Controls controls;
    private Serializer serializer;
    private JsonObject o;
    private CompositeDisposable currentEmission = new CompositeDisposable();
    private ActionsExecutor executor;

    private Subject<Boolean> influencesRunning = BehaviorSubject.createDefault(false);

    private Engine()
    {
        controls = new ControlsImpl();
        itemEventsBus = ItemEventsBus.instance();
        gameStates.onNext(State.NOT_STARTED);
        computation = Threads.computation();
        ticks = Observable.interval(0, TICK_MILLISECONDS, TimeUnit.MILLISECONDS);
        gameRunning = gameStates.filter((gs) -> gs == State.STARTED).switchMap((st) -> st == State.NOT_STARTED ? Observable.empty() : ticks);//.subscribe((l) -> {//Log.d(TAG, "Wooooorks");});
        executor = ActionsExecutor.instance(gameRunning);
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
        if (sensorsProvider == null)
        {
            errorMsg = "SensorsProvider not set.";
        }
        if (persistencyProvider == null)
        {
            errorMsg = "Persistency provider not set.";
        }
        if (errorMsg != null)
        {
            throw new IllegalStateException(errorMsg);
        }
        commands.onNext(START);
    }

    public Observable<Frame> getFramesStream()
    {
        return framesStream;
    }

    public ItemEventsBus getItemEventsBus()
    {
        return itemEventsBus;
    }

    private Game initializeGame()
    {
        serializer = SharedPrefsSerializer.instance();
        game = new GameImpl(persistencyProvider, serializer);
        game.getPlayer().addPlayerEventsListener(new PlayerEventsListenerImpl());
        playerLevel.onNext(game.getPlayer().getPlayerProps().getLevel());
        fractionStream.onNext(game.getPlayer().getPlayerProps().getFraction());
        Player.STATE ps = game.getPlayer().getPlayerProps().getState();
        Jsonable jso = serializer.deserialize(COUNTDOWN);
        long left = -1;
        if (jso != null)
        {
            o = jso.toJson();
            if (o.has("left"))
            {
                left = o.get("left").getAsLong();
            }
        } else
        {
            o = new JsonObject();
            o.addProperty("left", left);
        }
        currentPlayerState.onNext(ps);
        if (left > 0)
        {
            countdownStarted.onNext(System.currentTimeMillis() - (ps.getWaitTime() - left) * 1000);
            ((Subject<Long>) secondsLeft).onNext(left);
        }
        secondsLeft.skip(1).subscribe((s) ->
        {
            if (s % 5 == 0 || s == -1)
            {
                o.addProperty("left", s);
                serializer.serialize(COUNTDOWN, this);
            }
        });

        influenceProvider = new InfluenceProviderImpl(sensorsProvider, this.persistencyProvider.getInfluencesPersistency(), gameRunning);
        influencesStream = influenceProvider.getInfluenceStream();


        Observable.combineLatest(influencesRunning, playerLevel, (r, l) -> new android.support.v4.util.Pair<Boolean, Integer>(r, l))
                .subscribe((p) ->
                {
                    if (p.first)
                    {
                        influenceProvider.start(p.second);
                    } else
                    {
                        influenceProvider.stop(p.second);
                    }
                });

        effects = new Effects(deviceProvider);
        effects.setPlayerStatesStream(currentPlayerState);
        effects.setPlayerLevelStream(playerLevel);
        effects.setImpactsStatesStream(impactsStatesStream);

        return game;
    }

    private void startGame(Game game)
    {
        influencesStream
                .observeOn(computation)
                .subscribe((inf) -> this.framesStream.onNext(game.acceptInfluences(inf)));

        CodeInputEventBus.getCodeScans().observeOn(computation).subscribe((code) -> CodeInputEventBus.codeAccepted(game.acceptCode(code)));
        //itemEventsBus.getAddItemEvents().observeOn(computation).subscribe((code) -> game.getPlayer().addItem(code));//.map((c) -> itemsProvider.getItemByCode(c).orElse(new NonExistingItem()));
        itemEventsBus.getDropItemEvents().observeOn(computation).subscribe((item) ->
        {
            game.getPlayer().dropItem(item);
        });

        itemEventsBus.getUserItemsRequests().subscribe((r) ->
        {
            //Log.d(TAG, "user items requested!");
            itemEventsBus.userItemsLoaded(game.getInventory());
        });
        itemEventsBus.getUseItemRequests().subscribe((r) ->
        {
            framesStream.onNext(game.useItem(r));
        });
        setupSuicides();
        setupEmissions();
        Observable.combineLatest(EmissionEventBus.instance().getEmissionStateStream(), currentPlayerState, PlayerEventBus.instance().getPlayerFractionStream(), (e, p, f) -> new Triple<Boolean, Player.STATE, Player.FRACTION>(e, p, f)).subscribe((pr) ->
        {
            if (pr.first && pr.second.getCode() == Player.ALIVE && pr.third != Player.FRACTION.MONOLITH)
            {
                influenceProvider.startEmission();
            }
        });
        ItemEventsBus.instance().getItemUsedEvents()
                .filter((i) -> i.getItemDescriptor().getName() == "Anabiotic")
                .withLatestFrom(EmissionEventBus.instance().getEmissionStateStream(), (i, e) -> e)
                .filter((e) -> e)
                .subscribe((e) ->
                {
                    emissionEnded();
                });
        Disposable stateTimers = setupStateTimers();

        framesStream.onNext(game.start());
        controls.startInfluences();
        acceptsInfluences.onNext(true);
        this.gameStates.onNext(State.STARTED);
    }

    private Disposable setupStateTimers()
    {
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(makeCountDownForStates(Player.STATE.W_CONTROLLED, Player.STATE.CONTROLLED, Player.STATE.DEAD_CONTROLLER));
        cd.add(makeCountDownForStates(Player.STATE.W_MENTALLED, Player.STATE.MENTALLED, Player.STATE.DEAD_MENTAL));
        cd.add(makeCountDownForStates(Player.STATE.W_DEAD_ANOMALY, Player.STATE.DEAD_ANOMALY));
        cd.add(makeCountDownForStates(Player.STATE.W_DEAD_BURER, Player.STATE.DEAD_BURER));
        cd.add(makeCountDownForStates(Player.STATE.W_DEAD_RADIATION, Player.STATE.DEAD_RADIATION));
        cd.add(makeCountDownForStates(Player.STATE.W_ABDUCTED, Player.STATE.DEAD_BURER));
        cd.add(makeCountDownForStates(Player.STATE.ABDUCTED, Player.STATE.ALIVE));
        return cd;
    }

    private Disposable makeCountDownForStates(Player.STATE s1, Player.STATE s2, Player.STATE s3)
    {
        return makeCountDownForStates(s1, s1.getWaitTime(), s2, s2.getWaitTime(), s3);
    }

    private Disposable makeCountDownForStates(Player.STATE s1, Player.STATE s2)
    {
        return makeCountDownForStates(s1, s1.getWaitTime(), s2);
    }

    private Disposable makeCountDownForStates(Player.STATE state1, long time1, Player.STATE finalState)
    {
        CompositeDisposable cd = new CompositeDisposable();
        Observable<Long> stateCountDown = makeCountDownFor(state1);
        cd.add(stateCountDown.subscribe((t) ->
        {
            if (t <= time1)
            {
                ((Subject<Long>) secondsLeft).onNext(time1 - t);
            } else
            {
                game.getPlayer().setState(finalState);
                ((Subject<Long>) secondsLeft).onNext(time1 - t);
            }
        }));
        return cd;
    }

    private void setupEmissions()
    {
        Disposable d = new CompositeDisposable();
        List<Emission> emissions = persistencyProvider.getInfluencesPersistency().getEmissions();
        long now = System.currentTimeMillis();
        for (Emission e : emissions)
        {
            Calendar strtAt = e.getStartTime();
//            Log.e(TAG, calToStr(strtAt));
//            Log.e(TAG, calToStr(Calendar.getInstance()));
            if (strtAt.getTimeInMillis() + (e.duration() * 1000 * 60) < now || (e.isFake() && strtAt.getTimeInMillis() < now))
            {
                continue;
            }
            if (now < (strtAt.getTimeInMillis() - e.notifyBefore() * 1000 * 60))
            {
                executor.postAction(new Action()
                {
                    @Override
                    public long startTime()
                    {
                        //Log.e(TAG, "Now: " + System.currentTimeMillis() + " Start at: " + strtAt + " Strt at: " + (strtAt.getTimeInMillis() - e.notifyBefore() * 1000 * 60));
                        return strtAt.getTimeInMillis() - e.notifyBefore() * 1000 * 60;
                    }

                    @Override
                    public void execute()
                    {
                        notifyEmission(e.notifyBefore());
                    }

                    @Override
                    public String toString()
                    {
                        return "Notify emission action: " + calToStr(strtAt);
                    }
                });
            }

            executor.postAction(new Action()
            {
                @Override
                public long startTime()
                {
                    return e.getStartTime().getTimeInMillis();
                }

                @Override
                public void execute()
                {
                    if (e.isFake())
                    {
                        notifyFakeEmission();
                        return;
                    }
                    if (strtAt.getTimeInMillis() < now)
                    {
                        startEmission((int)(e.duration() - (now - strtAt.getTimeInMillis()) / 1000 / 60));
                    } else
                    {
                        startEmission(e.duration());
                    }
                }

                @Override
                public String toString()
                {
                    return "Start emission action: " + calToStr(strtAt);
                }
            });
        }
    }

    private String calToStr(Calendar c)
    {
        return "Year: " + c.get(Calendar.YEAR) + " Month: " + c.get(Calendar.MONTH) + " Day: " + c.get(Calendar.DAY_OF_MONTH) + " Hour: " + c.get(Calendar.HOUR_OF_DAY) + " Min: " + c.get(Calendar.MINUTE) + " Sec: " + c.get(Calendar.SECOND) + " Milis: " + c.getTimeInMillis();
    }

    private void notifyFakeEmission()
    {
        Log.e(TAG, "EMISSION FAKE");
        net.afterday.compas.logging.Logger.d(R.string.message_emission_fake);
        EmissionEventBus.instance().fakeEmission();
    }

    private void notifyEmission(int emissionStartAfter)
    {
        Log.e(TAG, "EMISSION WILL START");
        net.afterday.compas.logging.Logger.e(R.string.message_emission_approaching);
        EmissionEventBus.instance().emissionWillStart(emissionStartAfter);
    }

    private void startEmission(int endAfter)
    {
        Log.e(TAG, "EMISSION STARTED");
        net.afterday.compas.logging.Logger.e(R.string.message_emission_started);
        EmissionEventBus.instance().setEmissionActive(true);
        if(currentEmission == null)
        {
            currentEmission = new CompositeDisposable();
        }
        currentEmission.add(Observable.timer(endAfter, TimeUnit.MINUTES).take(1).subscribe((t) -> emissionEnded()));
    }

    private void emissionEnded()
    {
        if(currentEmission != null && !currentEmission.isDisposed())
        {
            currentEmission.dispose();
            currentEmission = null;
        }
        Logger.d(R.string.message_emission_ended);
        influenceProvider.stopEmission();
        EmissionEventBus.instance().setEmissionActive(false);
    }

    private Disposable setupSuicides()
    {
        Observable<String> suicideRequests = PlayerEventBus.instance().getPlayerCommandsStream().filter((c) -> c == PlayerEventBus.SUICIDE);
        return suicideRequests.withLatestFrom(currentPlayerState, (s, cs) -> cs)
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
                            case CONTROLLED:
                            case MENTALLED:
                                game.getPlayer().setState(Player.STATE.DEAD_BURER);
                                break;
                        }
                    }
                });
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

    public void setDeviceProvider(DeviceProvider devideProvider)
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

    @Override
    public JsonObject toJson()
    {
        return o;
    }

    private class ControlsImpl implements Controls
    {

        @Override
        public void stopInfluences()
        {
            acceptsInfluences.onNext(false);
            influencesRunning.onNext(false);
        }

        @Override
        public void startInfluences()
        {
            acceptsInfluences.onNext(true);
            influencesRunning.onNext(true);
        }
    }

    private class PlayerEventsListenerImpl implements PlayerEventsListener
    {

        @Override
        public void onPlayerStateChanged(Player.STATE oldState, Player.STATE newState)
        {
            Log.d(TAG, "onPlayerStateChanged " + newState);
            if(newState == Player.STATE.W_DEAD_BURER)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                net.afterday.compas.logging.Logger.d(R.string.message_suicide);
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_BURER)
            {
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                net.afterday.compas.logging.Logger.e(R.string.message_dead);
                //countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(newState);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_CONTROLLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                net.afterday.compas.logging.Logger.d(R.string.message_controller_trans);
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.CONTROLLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                net.afterday.compas.logging.Logger.e(R.string.message_controller_undercontrol);
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_CONTROLLER)
            {
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                net.afterday.compas.logging.Logger.e(R.string.message_dead_controller);
                currentPlayerState.onNext(Player.STATE.DEAD_CONTROLLER);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_MENTALLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                net.afterday.compas.logging.Logger.d(R.string.message_mental_trans);
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.MENTALLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                net.afterday.compas.logging.Logger.e(R.string.message_mental_zombified);
                currentPlayerState.onNext(newState);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_MENTAL)
            {
                net.afterday.compas.logging.Logger.e(R.string.message_dead_mental);
                currentPlayerState.onNext(Player.STATE.DEAD_MENTAL);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_DEAD_ANOMALY)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                net.afterday.compas.logging.Logger.d(R.string.message_suicide);
                currentPlayerState.onNext(Player.STATE.W_DEAD_ANOMALY);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_ANOMALY)
            {
                net.afterday.compas.logging.Logger.e(R.string.message_dead_anomaly);
                currentPlayerState.onNext(Player.STATE.DEAD_ANOMALY);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.ALIVE)
            {
                net.afterday.compas.logging.Logger.d(R.string.message_revive);
                currentPlayerState.onNext(Player.STATE.ALIVE);
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_DEAD_RADIATION)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                net.afterday.compas.logging.Logger.d(R.string.message_suicide);
                currentPlayerState.onNext(Player.STATE.W_DEAD_RADIATION);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_RADIATION)
            {
                net.afterday.compas.logging.Logger.e(R.string.message_dead_radiation);
                currentPlayerState.onNext(Player.STATE.DEAD_RADIATION);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.W_ABDUCTED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                net.afterday.compas.logging.Logger.d(R.string.message_suicide);
                currentPlayerState.onNext(Player.STATE.W_ABDUCTED);
                controls.stopInfluences();
                return;
            }
            if(newState == Player.STATE.ABDUCTED)
            {
                game.getPlayer().getPlayerProps().addHealth(1);
                countdownStarted.onNext(System.currentTimeMillis());
                net.afterday.compas.logging.Logger.e(R.string.message_abducted);
                currentPlayerState.onNext(Player.STATE.ABDUCTED);
                controls.startInfluences();
                return;
            }
            if(newState == Player.STATE.DEAD_EMISSION)
            {
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                net.afterday.compas.logging.Logger.e(R.string.message_dead_emission);
                //countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(newState);
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
            ItemEventsBus.instance().itemAdded(itemAdded.getItem());
            Engine.this.itemAdded.onNext(itemAdded);
            Logger.logItemAdded(itemAdded);
        }

        @Override
        public void onItemUsed(Item item)
        {
            ItemEventsBus.instance().itemUsed(item);
            Logger.logItemUsed(item);
        }

        @Override
        public void onItemDropped(Item item)
        {
            ItemEventsBus.instance().itemDropped(item);
            Logger.logItemDropped(item);
        }

        @Override
        public void onFractionChanged(Player.FRACTION newFraction, Player.FRACTION oldFraction)
        {
            Logger.d("Fraction changed to " + newFraction.toString());
            fractionStream.onNext(newFraction);
        }

        @Override
        public void onPlayerLevelChanged(int level)
        {
            Engine.this.playerLevel.onNext(level);
        }
    }
}