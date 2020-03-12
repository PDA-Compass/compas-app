package net.afterday.compas;

import android.app.Application;
import android.content.Intent;

//import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import net.afterday.compas.core.Game;
import net.afterday.compas.core.gameState.Frame;
import net.afterday.compas.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.core.player.Player;
import net.afterday.compas.core.serialization.Serializer;
import net.afterday.compas.core.userActions.UserActionsPack;
import net.afterday.compas.engine.Engine;
import net.afterday.compas.engine.events.ItemEventsBus;
import net.afterday.compas.logging.Logger;
import net.afterday.compas.sensors.Battery.Battery;
import net.afterday.compas.sensors.WiFi.WifiImpl;
import net.afterday.compas.settings.Settings;
import net.afterday.compas.util.Fonts;
import net.afterday.compas.view.SmallLogListAdapter;

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
    private Settings settings;
    @Override
    public void onCreate()
    {
        super.onCreate();

        //android.util.Log.d(TAG, "onCreate " + Thread.currentThread().getName());
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);

        ////Log.d(TAG, "onCreate");
        instance = this;
//        serializer = SharedPrefsSerializer.instance(this);
//        engine = Engine.instance();
//        fonts = Fonts.instance(this.getAssets());
//        engine.setPersistencyProvider(new PersistencyProviderImpl());
//        engine.setSensorsProvider(SensorsProviderImpl.initialize(getApplicationContext()));
//        engine.setDeviceProvider(new DeviceProviderImpl(getApplicationContext()));
//        battery = SensorsProviderImpl.instance().getBatterySensor();
//        batteryLevelStream = battery.getSensorResultsStream();
//        battery.start();
//        framesStream = engine.getFramesStream();
//        engine.start();
        settings = Settings.instance(this);
        fonts = Fonts.instance(this.getAssets());
        startService(new Intent(StalkerApp.this, LocalMainService.class));
//        Fabric.with(this, new Crashlytics());
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
//                //Catch your exception
//                // Without System.exit() this will not work.
//                android.util.Log.e("EXCEPTION!", "" + paramThrowable);
//                System.exit(2);
//            }
//        });

    }

    public Game getGame()
    {
        return game;
    }

    public static StalkerApp getInstance()
    {
        return instance;
    }
}
