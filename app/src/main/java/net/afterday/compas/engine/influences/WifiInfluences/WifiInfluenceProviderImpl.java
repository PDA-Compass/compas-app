package net.afterday.compas.engine.influences.WifiInfluences;


import android.net.wifi.ScanResult;

import io.reactivex.annotations.NonNull;
import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.engine.influences.InflPack;
import net.afterday.compas.engine.influences.InfluenceExtractionStrategy;
import net.afterday.compas.engine.threading.Threads;
import net.afterday.compas.persistency.influences.InfluencesPersistency;
import net.afterday.compas.sensors.Bluetooth.Bluetooth;
import net.afterday.compas.sensors.Sensor;
import net.afterday.compas.sensors.SensorResult;
import net.afterday.compas.sensors.WiFi.WiFi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import io.reactivex.functions.BiFunction;

/**
 * Created by Justas Spakauskas on 4/2/2018.
 */

public class WifiInfluenceProviderImpl implements WiFiInfluenceProvider
{
    private static final String TAG = "WifiInflProvider";
    private WiFi wifi;
    private Bluetooth bluetooth;
    private InfluencesPersistency ip;
    private Observable<List<SensorResult>> scanResults;
    private Observable<InfluencesPack> wifiInfluence = BehaviorSubject.createDefault(new InflPack());
    private InfluenceExtractionStrategy<List<SensorResult>, InfluencesPack> ies;
    private boolean wasHealing = false;
    private int noHealing = 0;
    private double lastHealingStrength;
    private long lastHealingTime = 0;

    public WifiInfluenceProviderImpl(WiFi wifi, Bluetooth bluetooth, InfluencesPersistency ip)
    {
        this.wifi = wifi;
        this.bluetooth = bluetooth;

        this.ip = ip;
        //ies = new ByMacExtractionStrategy(ip.getRegisteredWifiModules());
        ies = new MacIgnoringStrategy();
        Observable.<List<SensorResult>, List<SensorResult>, List<SensorResult>>combineLatest(
                wifi.getSensorResultsStream(),
                bluetooth.getSensorResultsStream(),
                combine
        )
        .observeOn(Threads.computation())
        .map(this.ies::makeInfluences)
        .map((i) -> verifyHealing(i))
        .subscribe((i) -> ((Subject<InfluencesPack>)wifiInfluence).onNext(i));//.doOnNext((i) -> Log.d(TAG, "WifiEmitted: " + i.toString()));
    }

    BiFunction<List<SensorResult>, List<SensorResult>, List<SensorResult>> combine = new BiFunction<List<SensorResult>, List<SensorResult>, List<SensorResult>>() {
        @NonNull
        @Override
        public List<SensorResult> apply(@NonNull List<SensorResult> sensorResults, @NonNull List<SensorResult> sensorResults2) throws Exception {
            ArrayList<SensorResult> results = new ArrayList<>();
            results.addAll(sensorResults);
            results.addAll(sensorResults2);

            return results;
        }
    };

    @Override
    public Observable<InfluencesPack> getInfluenceStream()
    {
        return wifiInfluence;
    }

    public void start()
    {
        //((Subject<InfluencesPack>)wifiInfluence).onNext(new InflPack());
        bluetooth.start();
        wifi.start();
    }

    @Override
    public void stop()
    {
        bluetooth.stop();
        wifi.stop();
        ((Subject<InfluencesPack>)wifiInfluence).onNext(new InflPack());
    }

    public InfluencesPack verifyHealing(InfluencesPack ip)
    {
//        if(ip.influencedBy(Influence.BURER))
//        {
//            ip.addInfluence(Influence.RADIATION, 100000);
//        }
        if(ip.influencedBy(Influence.HEALTH))
        {
            //wasHealing = true;
            lastHealingStrength = ip.getInfluence(Influence.HEALTH);
            lastHealingTime = System.currentTimeMillis();
        }else if((System.currentTimeMillis() - lastHealingTime) < 5000)
        {
            //noHealing++;
            ip.addInfluence(Influence.HEALTH, lastHealingStrength);
            //wasHealing = false;
            lastHealingStrength = 0;
            lastHealingTime = 0;
        }
        return ip;
    }
}
