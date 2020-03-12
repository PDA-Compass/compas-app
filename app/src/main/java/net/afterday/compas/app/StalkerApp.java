package net.afterday.compas.app;

import android.app.Application;
import android.content.Intent;

import android.os.Build;
import net.afterday.compas.app.LocalMainService;
import net.afterday.compas.engine.core.Game;
import net.afterday.compas.engine.core.gameState.Frame;
import net.afterday.compas.engine.core.serialization.Serializer;
import net.afterday.compas.engine.engine.Engine;
import net.afterday.compas.engine.sensors.Battery.Battery;
import net.afterday.compas.app.settings.Settings;
import net.afterday.compas.app.util.Fonts;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class StalkerApp extends Application
{
    private static final String TAG = "StalkerApp";
    private static StalkerApp instance;
    private Game game;
    private Engine engine;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(StalkerApp.this, LocalMainService.class));
        }
        else {
            startService(new Intent(StalkerApp.this, LocalMainService.class));
        }
//
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
