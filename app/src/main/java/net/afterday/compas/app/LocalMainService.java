package net.afterday.compas.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import net.afterday.compas.engine.core.Game;
import net.afterday.compas.engine.core.gameState.Frame;
import net.afterday.compas.engine.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.engine.core.player.Player;
import net.afterday.compas.engine.core.serialization.Serializer;
import net.afterday.compas.app.db.DataBase;
import net.afterday.compas.engine.devices.DeviceProvider;
import net.afterday.compas.app.devices.DeviceProviderImpl;
import net.afterday.compas.engine.engine.Engine;
import net.afterday.compas.engine.engine.events.ItemEventsBus;
import net.afterday.compas.app.logging.LogLine;
import net.afterday.compas.app.logging.Logger;
import net.afterday.compas.engine.sensors.Battery.Battery;
import net.afterday.compas.engine.sensors.Battery.BatteryStatus;
import net.afterday.compas.app.sensors.SensorsProviderImpl;
import net.afterday.compas.app.sensors.WiFi.WifiImpl;
import net.afterday.compas.app.serialization.SharedPrefsSerializer;
import net.afterday.compas.app.util.Fonts;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import net.afterday.compas.stalker.persistency.PersistencyProviderStalker;


public class LocalMainService extends Service
{
    private IBinder binder = new MainBinder();
    private boolean running = false;

    private static final String TAG = "LocalMainService";
    private static LocalMainService instance;
    private Game game;
    private Engine engine;
    private Observable<Frame> framesStream;
    private Observable<BatteryStatus> batteryStatusStream;
    private Battery battery;
    private Fonts fonts;
    private Serializer serializer;
    private Logger logger;
    private DataBase dataBase;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        if(!running)
        {
            Log.e(TAG, "----------------------------------------------------------------");
            startForeground();
            initGame();
        }
        running = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent)
    {
//        boolean unbinded = super.onUnbind(intent);
//        Intent i = new Intent(getApplicationContext(), HiddenActivity.class);
//        //i.setClassName("net.afterday.compas", "net.afterday.compas.HiddenActivity");
//        startActivity(i);
//        return unbinded;
        return super.onUnbind(intent);
    }

    private void startForeground()
    {
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews collapsed = new RemoteViews(getPackageName(), R.layout.notification);
        Intent intentAction = new Intent(this, ActionsReceiver.class);
        intentAction.putExtra("ServiceControlls","STOP");
        PendingIntent stopServiceIntent = PendingIntent.getBroadcast(this, 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);
        collapsed.setOnClickPendingIntent(R.id.open, pIntent);
        collapsed.setOnClickPendingIntent(R.id.stop, stopServiceIntent);


        Notification n = new NotificationCompat.Builder(this)
                            .setContent(collapsed)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            //.setStyle(new android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle())
                            .build();
//        Notification n = new NotificationCompat.Builder(this)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .addAction(R.drawable.leak_canary_icon, "OPEN", pIntent)
//                            .addAction(R.drawable.leak_canary_icon, "Stop PDA", stopServiceIntent)
//                            .build();

        startForeground(Constants.MAIN_SERVICE, n);
    }

    private void initGame()
    {
        DeviceProvider deviceProvider = new DeviceProviderImpl(this);
        serializer = SharedPrefsSerializer.instance(this);
        dataBase = DataBase.instance(this);
        logger = Logger.instance(this, deviceProvider.getVibrator());
        engine = new Engine(serializer, null);
        engine.setPersistencyProvider(new PersistencyProviderStalker());
        engine.setSensorsProvider(SensorsProviderImpl.initialize(this));
        //engine.setDeviceProvider(deviceProvider); //TODO: (Mikhail)
        battery = SensorsProviderImpl.instance().getBatterySensor();
        batteryStatusStream = battery.getSensorResultsStream();
        battery.start();
        framesStream = engine.getFramesStream();
        engine.start();
    }

    public class MainBinder extends Binder
    {
        public LocalMainService getService()
        {
            return LocalMainService.this;
        }
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

    public Observable<List<LogLine>> getLogStream()
    {
        return logger.getLogStream();
    }

    public Observable<BatteryStatus> getBatteryStatusStream()
    {
        return batteryStatusStream;
    }

    public ItemEventsBus getItemEventBus()
    {
        return engine.getItemEventsBus();
    }

    public static LocalMainService getInstance()
    {
        return instance;
    }
}
