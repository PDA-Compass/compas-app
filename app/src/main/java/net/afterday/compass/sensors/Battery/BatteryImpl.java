package net.afterday.compass.sensors.Battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 3/20/2018.
 */

public class BatteryImpl implements Battery
{
    private IntentFilter iFilter;
    private Intent intent;
    private BatteryManager bManager;
    private Context context;
    private Observable<Integer> batteryLevel = BehaviorSubject.create();
    private BatteryListener listener;
    public BatteryImpl(Context context)
    {
        this.context = context;
    }

    @Override
    public void start()
    {
        if(listener == null)
        {
            listener = new BatteryListener();
        }
        context.registerReceiver(listener, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void stop()
    {
        context.unregisterReceiver(listener);
    }

    @Override
    public Observable<Integer> getSensorResultsStream()
    {
        return batteryLevel;
    }

    private class BatteryListener extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            ((Subject)batteryLevel).onNext(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
        }
    }
}
