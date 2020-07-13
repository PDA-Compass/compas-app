package net.afterday.compas.engine.engine;

import com.google.gson.JsonObject;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.afterday.compas.engine.core.Game;
import net.afterday.compas.engine.core.GameImpl;
import net.afterday.compas.engine.core.events.PlayerEventsListener;
import net.afterday.compas.engine.core.gameState.Frame;
import net.afterday.compas.engine.core.gameState.State;
import net.afterday.compas.engine.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.log.Audit;
import net.afterday.compas.engine.core.log.Log;
import net.afterday.compas.engine.core.player.Impacts;
import net.afterday.compas.engine.core.player.Player;
import net.afterday.compas.engine.core.serialization.Jsonable;
import net.afterday.compas.engine.core.serialization.Serializer;
import net.afterday.compas.engine.effects.Effects;
import net.afterday.compas.engine.engine.events.CodeInputEventBus;
import net.afterday.compas.engine.engine.events.EmissionEventBus;
import net.afterday.compas.engine.engine.events.ItemEventsBus;
import net.afterday.compas.engine.engine.events.PlayerEventBus;
import net.afterday.compas.engine.engine.system.DamageSystem;
import net.afterday.compas.engine.engine.system.InfluenceSystem;
import net.afterday.compas.engine.persistency.PersistencyProvider;
import net.afterday.compas.engine.sensors.SensorsProvider;
import net.afterday.compas.engine.util.Triple;

import java.util.concurrent.TimeUnit;


public class Engine implements Jsonable
{
    public static final int TICK_MILLISECONDS = 1000;
    private static final String COUNTDOWN = "COUNTDOWN";
    private static final String TAG = "Engine";
    private static final String START = "Start";

    private Observable<Long> ticks;
    private Observable<Long> secondsLeft = BehaviorSubject.createDefault((long) -1);
    private PersistencyProvider persistencyProvider;

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

    private Subject<State> gameStates = BehaviorSubject.<State>create();
    private Observable<Long> gameRunning;
    private ItemEventsBus itemEventsBus;
    private Effects effects;
    private InfluenceSystem influenceSystem;
    private DamageSystem damageSystem;
    private Serializer serializer;
    private JsonObject o;


    public Engine(Serializer serializer)
    {
        this.serializer = serializer; //SharedPrefsSerializer.instance();
        itemEventsBus = ItemEventsBus.instance();
        gameStates.onNext(State.NOT_STARTED);
        computation = Schedulers.computation();
        ticks = Observable.interval(0, TICK_MILLISECONDS, TimeUnit.MILLISECONDS);
        gameRunning = gameStates.filter((gs) -> gs == State.STARTED).switchMap((st) -> st == State.NOT_STARTED ? Observable.empty() : ticks);//.subscribe((l) -> {//Log.d(TAG, "Wooooorks");});
        startCommands = this.commands.filter((cmd) -> cmd == START);
        startCommands
                .observeOn(computation)
                .take(1)
                .map((cmd) -> this.initializeGame())
                .subscribe(this::startGame);

        influenceSystem = new InfluenceSystem(playerLevel, gameRunning);
        damageSystem = new DamageSystem();
    }

    public void setEffects(Effects effects){
        this.effects = effects;
    }

    public void start()
    {
        String errorMsg = null;
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
        effects.setPlayerStatesStream(currentPlayerState);
        effects.setPlayerLevelStream(playerLevel);
        effects.setImpactsStatesStream(impactsStatesStream);

        return game;
    }

    private void startGame(Game game)
    {
        //TODO: Move out
        //TODO: uncomented
        /*this.influenceSystem.getInfluencesStream()
                .observeOn(computation)
                .subscribe((inf) -> this.framesStream.onNext(game.acceptInfluences(inf)));
        */
        CodeInputEventBus.getCodeScans().observeOn(computation).subscribe((code) -> CodeInputEventBus.codeAccepted(game.acceptCode(code)));
        //itemEventsBus.getAddItemEvents().observeOn(computation).subscribe((code) -> game.getPlayer().addItem(code));//.map((c) -> itemsProvider.getItemByCode(c).orElse(new NonExistingItem()));
        itemEventsBus.getDropItemEvents().observeOn(computation).subscribe((item) ->
                game.getPlayer().dropItem(item));

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

        //TODO: Move to InfluenceSystem
        Observable.combineLatest(EmissionEventBus.instance().getEmissionStateStream(), currentPlayerState, PlayerEventBus.instance().getPlayerFractionStream(), (e, p, f) -> new Triple<>(e, p, f)).subscribe((pr) ->
        {
            if (pr.first && pr.second.getCode() == Player.ALIVE && pr.third != Player.FRACTION.MONOLITH)
            {
                //TODO: uncommented
                //influenceSystem.getInfluenceProvider().startEmission();
            }
        });
        ItemEventsBus.instance().getItemUsedEvents()
                .filter((i) -> i.getItemDescriptor().getName() == "Anabiotic")
                .withLatestFrom(EmissionEventBus.instance().getEmissionStateStream(), (i, e) -> e)
                .filter((e) -> e)
                .subscribe((e) ->
                {
                    influenceSystem.emissionEnded();
                });

        Disposable stateTimers = setupStateTimers();

        framesStream.onNext(game.start());
        influenceSystem.isStarted(true);
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
            if (t > time1) {
                game.getPlayer().setState(finalState);
            }
            ((Subject<Long>) secondsLeft).onNext(time1 - t);
        }));
        return cd;
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

    public void setPersistencyProvider(PersistencyProvider provider)
    {
        this.persistencyProvider = provider;
    }

    public Observable<Player.STATE> getPlayerStateStream()
    {
        return currentPlayerState;
    }
    public Observable<ItemAdded> getItemAddedStream()
    {
        return itemAdded;
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

    public class PlayerEventsListenerImpl implements PlayerEventsListener
    {
        @Override
        public void onPlayerStateChanged(Player.STATE oldState, Player.STATE newState)
        {
            Log.d(TAG, "onPlayerStateChanged " + newState);
            if(newState == Player.STATE.W_DEAD_BURER)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                Audit.d("R.string.message_suicide");
                currentPlayerState.onNext(newState);
                influenceSystem.isStarted(false);
                return;
            }
            if(newState == Player.STATE.DEAD_BURER)
            {
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                Audit.e("R.string.message_dead");
                currentPlayerState.onNext(newState);
                influenceSystem.isStarted(true);
                return;
            }
            if(newState == Player.STATE.W_CONTROLLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                Audit.d("R.string.message_controller_trans");
                currentPlayerState.onNext(newState);
                influenceSystem.isStarted(false);
                return;
            }
            if(newState == Player.STATE.CONTROLLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                Audit.e("R.string.message_controller_undercontrol");
                currentPlayerState.onNext(newState);
                influenceSystem.isStarted(false);
                return;
            }
            if(newState == Player.STATE.DEAD_CONTROLLER)
            {
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                Audit.e("R.string.message_dead_controller");
                currentPlayerState.onNext(Player.STATE.DEAD_CONTROLLER);
                influenceSystem.isStarted(true);
                return;
            }
            if(newState == Player.STATE.W_MENTALLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                Audit.d("R.string.message_mental_trans");
                currentPlayerState.onNext(newState);
                influenceSystem.isStarted(false);
                return;
            }
            if(newState == Player.STATE.MENTALLED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                Audit.e("R.string.message_mental_zombified");
                currentPlayerState.onNext(newState);
                influenceSystem.isStarted(false);
                return;
            }
            if(newState == Player.STATE.DEAD_MENTAL)
            {
                Audit.e("R.string.message_dead_mental");
                currentPlayerState.onNext(Player.STATE.DEAD_MENTAL);
                influenceSystem.isStarted(true);
                return;
            }
            if(newState == Player.STATE.W_DEAD_ANOMALY)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                Audit.d("R.string.message_suicide");
                currentPlayerState.onNext(Player.STATE.W_DEAD_ANOMALY);
                influenceSystem.isStarted(false);
                return;
            }
            if(newState == Player.STATE.DEAD_ANOMALY)
            {
                Audit.e("R.string.message_dead_anomaly");
                currentPlayerState.onNext(Player.STATE.DEAD_ANOMALY);
                influenceSystem.isStarted(true);
                return;
            }
            if(newState == Player.STATE.ALIVE)
            {
                Audit.d("R.string.message_revive");
                currentPlayerState.onNext(Player.STATE.ALIVE);
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                influenceSystem.isStarted(true);
                return;
            }
            if(newState == Player.STATE.W_DEAD_RADIATION)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                Audit.d("R.string.message_suicide");
                currentPlayerState.onNext(Player.STATE.W_DEAD_RADIATION);
                influenceSystem.isStarted(false);
                return;
            }
            if(newState == Player.STATE.DEAD_RADIATION)
            {
                Audit.e("R.string.message_dead_radiation");
                currentPlayerState.onNext(Player.STATE.DEAD_RADIATION);
                influenceSystem.isStarted(true);
                return;
            }
            if(newState == Player.STATE.W_ABDUCTED)
            {
                countdownStarted.onNext(System.currentTimeMillis());
                Audit.d("R.string.message_suicide");
                currentPlayerState.onNext(Player.STATE.W_ABDUCTED);
                influenceSystem.isStarted(false);
                return;
            }
            if(newState == Player.STATE.ABDUCTED)
            {
                game.getPlayer().getPlayerProps().addHealth(1);
                countdownStarted.onNext(System.currentTimeMillis());
                Audit.e("R.string.message_abducted");
                currentPlayerState.onNext(Player.STATE.ABDUCTED);
                influenceSystem.isStarted(true);
                return;
            }
            if(newState == Player.STATE.DEAD_EMISSION)
            {
                ((Subject<Long>)secondsLeft).onNext((long)-1);
                Audit.e("R.string.message_dead_emission");
                //countdownStarted.onNext(System.currentTimeMillis());
                currentPlayerState.onNext(newState);
                influenceSystem.isStarted(true);
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
            Audit.logItemAdded(itemAdded);
        }

        @Override
        public void onItemUsed(Item item)
        {
            ItemEventsBus.instance().itemUsed(item);
            Audit.logItemUsed(item);
        }

        @Override
        public void onItemDropped(Item item)
        {
            ItemEventsBus.instance().itemDropped(item);
            Audit.logItemDropped(item);
        }

        @Override
        public void onFractionChanged(Player.FRACTION newFraction, Player.FRACTION oldFraction)
        {
            Audit.d("Fraction changed to " + newFraction.toString());
            fractionStream.onNext(newFraction);
        }

        @Override
        public void onPlayerLevelChanged(int level)
        {
            Engine.this.playerLevel.onNext(level);
        }
    }
}