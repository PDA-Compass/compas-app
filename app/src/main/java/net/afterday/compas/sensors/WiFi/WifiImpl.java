package net.afterday.compas.sensors.WiFi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import net.afterday.compas.StalkerApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import net.afterday.compas.sensors.SensorResult;

public class WifiImpl implements WiFi
{
    private String TAG = "WiFi sensor";
    private static long lastScan = 0;
    private static WiFi instance;
    private WifiManager mWifi;
    private Context appContext;
    private StalkerApp app;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Observable<List<SensorResult>> wifiScans = PublishSubject.create();
    private long dangerousScans = 0;
    private long delayedScans = 0;
    private Subject<Boolean> isRunningSubj = BehaviorSubject.createDefault(false);
    public WifiImpl(Context context)
    {
        mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final long period = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)? 30 : 1;

        isRunningSubj.switchMap((isRunning) -> isRunning ? Observable.interval(period, TimeUnit.SECONDS) : Observable.empty()).observeOn(AndroidSchedulers.mainThread())
        .subscribe((t) -> {
            Boolean b = mWifi.startScan();
            if (!b) {
                Log.e(TAG, "Wifi not started (in)");
            }
            List<ScanResult> results = mWifi.getScanResults();
            ArrayList<SensorResult> ss = new ArrayList<>(results.size());
            if(wifiScans == null)
            {
                return;
            }
            //Log.d(TAG, "WIFI Scanned. (THROTTLING RECEIVER)" + Thread.currentThread().getName());
            for (ScanResult sr : results)
            {
                SensorResult srr = new SensorResult();
                srr.id = sr.BSSID;
                srr.name = sr.SSID;
                srr.value = sr.level;
                srr.time = sr.timestamp;
                ss.add(srr);
                Log.e(TAG, "Result: " + sr.SSID);
            }
            //Log.e(TAG, "--------------------------- " + Thread.currentThread().getName());
            ((Subject) wifiScans).onNext(ss);
        });
    }

    public void start()
    {
        Log.d(TAG, "WIFI Sensor started " + Thread.currentThread().getName());
        this.isRunning.set(true);
        this.isRunningSubj.onNext(true);
        Boolean b = mWifi.startScan();
        if (!b) {
            Log.e(TAG, "Wifi not started");
        }
    }

    public void stop()
    {
        //Log.d(TAG, "Sensor stopped");
        this.isRunning.set(false);
        this.isRunningSubj.onNext(false);
    }

    @Override
    public Observable<List<SensorResult>> getSensorResultsStream()
    {
        return wifiScans;
    }

    public class WifiReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) && intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false))
            {
                List<ScanResult> results = mWifi.getScanResults();
                ArrayList<SensorResult> ss = new ArrayList<>(results.size());
                Log.d(TAG, "WIFI Scanned." + Thread.currentThread().getName());
                for (ScanResult sr : results)
                {
                    SensorResult srr = new SensorResult();
                    srr.id = sr.BSSID;
                    srr.name = sr.SSID;
                    srr.value = sr.level;
                    srr.time = sr.timestamp;
                    ss.add(srr);
                    Log.e(TAG, "Result: " + sr.SSID);
                }
                Log.e(TAG, "--------------------------- " + Thread.currentThread().getName());
                if (isRunning.get())
                {
                    ((Subject) wifiScans).onNext(ss);
                    mWifi.startScan();
                }
            }
        }

    }

    public class ThrottlingReceiver extends BroadcastReceiver
    {
        //Kol kas palieku kaip buvo paprastam receiveri, ziuresim ar reikia throttlingo
        @Override
        public void onReceive(Context context, Intent intent)
        {
//            if(intent.hasExtra(WifiManager.EXTRA_RESULTS_UPDATED))
//            {
//                Log.w(TAG, "---------------------------");
//            }
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
//                if(intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false))
//                {
//                List<ScanResult> results = mWifi.getScanResults();
//                Log.d(TAG, "WIFI Scanned." + Thread.currentThread().getName());
//                for (ScanResult sr : results)
//                {
//                    Log.e(TAG, "Result: " + sr.SSID);
//                }
//                Log.e(TAG, "--------------------------- " + Thread.currentThread().getName());
//                if (isRunning.get())
//                {
//                    ((Subject) wifiScans).onNext(results);
//                    long now = System.currentTimeMillis();
//                    if (now - lastScan < 1000)
//                    {
//                        dangerousScans++;
//                    }
//                    if (dangerousScans > 5)
//                    {
//                        delayedScans++;
//                        Log.e(TAG, "DELAYED SCAN!!!");
//                        Observable.timer(2, TimeUnit.SECONDS).take(1).subscribe((i) -> mWifi.startScan());
//                        if (delayedScans > 5)
//                        {
//                            dangerousScans = 0;
//                            delayedScans = 0;
//                        }
//                    } else
//                    {
//                        mWifi.startScan();
//                    }
//                }

                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
                {
                    List<ScanResult> results = mWifi.getScanResults();
                    Log.d(TAG, "WIFI Scanned. (THROTTLING RECEIVER)" + Thread.currentThread().getName());
                    for (ScanResult sr : results)
                    {
                        Log.e(TAG, "Result: " + sr.SSID);
                    }
                    Log.e(TAG, "--------------------------- " + Thread.currentThread().getName());
                    if (isRunning.get())
                    {
                        ((Subject) wifiScans).onNext(results);
                        mWifi.startScan();
                    }
                }
            }
        }
    }
}