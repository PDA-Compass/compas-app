package net.afterday.compass.sensors.WiFi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import net.afterday.compass.StalkerApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class WifiImpl implements WiFi
{
    private String TAG = "WiFi sensor";
    private static long lastScan = 0;
    private static WiFi instance;
    private WifiManager mWifi;
    private Context appContext;
    private StalkerApp app;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Observable<List<ScanResult>> wifiScans = PublishSubject.create();

    public WifiImpl(Context context)
    {
        mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        context.registerReceiver(new WifiReceiver(), new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//        Observable.interval(500, TimeUnit.MILLISECONDS).subscribe((i) -> {
//            if(this.isRunning.get())
//            {
//                ((Subject)this.wifiScans).onNext(this.scanResults);
//            }
//        });
    }

    public void start()
    {
        Log.d(TAG, "WIFI Sensor started " + Thread.currentThread().getName());
        this.isRunning.set(true);
        mWifi.startScan();
        //Observable.timer(0, TimeUnit.MILLISECONDS).take(1).observeOn(AndroidSchedulers.mainThread()).subscribe((t) -> mWifi.startScan());
       // mWifi.startScan();
    }

    public void stop()
    {
        //Log.d(TAG, "Sensor stopped");
        this.isRunning.set(false);
    }

    @Override
    public Observable<List<ScanResult>> getSensorResultsStream()
    {
        return wifiScans;
    }

    private class WifiReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> results = mWifi.getScanResults();
                //Log.d(TAG, "WIFI Scanned." + Thread.currentThread().getName());
//                for (ScanResult sr : results)
//                {
//                    Log.e(TAG, "Result: " + sr.SSID);
//                }
//                Log.e(TAG, "--------------------------- " + Thread.currentThread().getName());
                if(isRunning.get())
                {
                    ((Subject)wifiScans).onNext(results);
                    mWifi.startScan();
                }

            }
        }
    }
}
