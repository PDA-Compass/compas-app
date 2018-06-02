package net.afterday.compass;

import android.app.Application;

import net.afterday.compass.core.Game;
import net.afterday.compass.core.gameState.Frame;
import net.afterday.compass.core.inventory.items.Events.ItemAdded;
import net.afterday.compass.core.player.Player;
import net.afterday.compass.core.serialization.Serializer;
import net.afterday.compass.core.userActions.UserActionsPack;
import net.afterday.compass.devices.DeviceProviderImpl;
import net.afterday.compass.engine.Engine;
import net.afterday.compass.engine.events.ItemEventsBus;
import net.afterday.compass.logging.Log;
import net.afterday.compass.persistency.PersistencyProviderImpl;
import net.afterday.compass.sensors.Battery.Battery;
import net.afterday.compass.sensors.SensorsProviderImpl;
import net.afterday.compass.serialization.SharedPrefsSerializer;
import net.afterday.compass.util.Fonts;
import net.afterday.compass.view.SmallLogListAdapter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;


/**
 * Created by Justas Spakauskas on 1/28/2018.
 */

public class StalkerApp extends Application
{
    private static final String TAG = "StalkerApp";
    private static StalkerApp instance;
    private Game game;
    private Engine engine;
    private Observable<UserActionsPack> userActionsStream = PublishSubject.create();
    private Observable<Frame> framesStream;
    private Observable<Integer> batteryLevelStream;
    private Battery battery;
    private Fonts fonts;
    private Serializer serializer;
    @Override
    public void onCreate()
    {
        super.onCreate();
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
//                //Catch your exception
//                // Without System.exit() this will not work.
//                android.util.Log.e("EXCEPTION!", "" + paramThrowable);
//                System.exit(2);
//            }
//        });
        //android.util.Log.d(TAG, "onCreate " + Thread.currentThread().getName());
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);

        ////Log.d(TAG, "onCreate");
        instance = this;
        serializer = SharedPrefsSerializer.instance(this);
        engine = Engine.instance();
        fonts = Fonts.instance(this.getAssets());
        engine.setPersistencyProvider(new PersistencyProviderImpl());
        engine.setSensorsProvider(SensorsProviderImpl.initialize(getApplicationContext()));
        engine.setDevideProvider(new DeviceProviderImpl(getApplicationContext()));
        battery = SensorsProviderImpl.instance().getBatterySensor();
        batteryLevelStream = battery.getSensorResultsStream();
        battery.start();
        engine.setUserActionsStream(userActionsStream);
        framesStream = engine.getFramesStream();
        engine.start();

    }

    public Game getGame()
    {
        return game;
    }


    public static StalkerApp getInstance()
    {
        return instance;
    }

    public Observable<Frame> getFramesStream()
    {
        return framesStream;
    }

    public Observable<Long> getCountDownStream()
    {
        return engine.getCountDownStream();
    }

    public Observable<Integer> getPlayerLevelStream()
    {
        return engine.getPlayerLevelStream();
    }

    public Observable<Player.STATE> getPlayerStateStream()
    {
        return engine.getPlayerStateStream();
    }

    public Observable<ItemAdded> getItemAddedStream()
    {
        return engine.getItemAddedStream();
    }

    public Disposable setUserActionsStream(Observable<UserActionsPack> userActionsStream)
    {
        return userActionsStream.subscribe((ua) -> {
            ////Log.d(TAG, "UserAction: " + ua);
        });
    }

    public void registerLogAdapter(SmallLogListAdapter logListAdapter)
    {
        Log.registerLogListAdapter(logListAdapter);
    }

    public void unregisterLogAdapter()
    {
        Log.unregisterLogListAdapter();
    }

    public Observable<Integer> getBatteryLevelStream()
    {
        return batteryLevelStream;
    }

    public ItemEventsBus getItemEventBus()
    {
        return engine.getItemEventsBus();
    }
}
