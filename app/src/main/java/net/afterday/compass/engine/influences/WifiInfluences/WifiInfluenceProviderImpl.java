package net.afterday.compass.engine.influences.WifiInfluences;


import android.net.wifi.ScanResult;

import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.engine.influences.InflPack;
import net.afterday.compass.engine.influences.InfluenceExtractionStrategy;
import net.afterday.compass.engine.threading.Threads;
import net.afterday.compass.persistency.influences.InfluencesPersistency;
import net.afterday.compass.sensors.WiFi.WiFi;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 4/2/2018.
 */

public class WifiInfluenceProviderImpl implements WiFiInfluenceProvider
{
    private static final String TAG = "WifiInflProvider";
    private WiFi wifi;
    private InfluencesPersistency ip;
    private Observable<List<ScanResult>> scanResults;
    private Observable<InfluencesPack> wifiInfluence = BehaviorSubject.createDefault(new InflPack());
    private InfluenceExtractionStrategy<List<ScanResult>, InfluencesPack> ies;
    private boolean wasHealing = false;
    private int noHealing = 0;
    private double lastHealingStrength;
    private long lastHealingTime = 0;

    public WifiInfluenceProviderImpl(WiFi wifi, InfluencesPersistency ip)
    {
        this.wifi = wifi;
        this.ip = ip;
        //ies = new ByMacExtractionStrategy(ip.getRegisteredWifiModules());
        ies = new MacIgnoringStrategy();

        wifi.getSensorResultsStream()
                .observeOn(Threads.computation())
                .map(this.ies::makeInfluences)
                .map((i) -> verifyHealing(i))
                .subscribe((i) -> ((Subject<InfluencesPack>)wifiInfluence).onNext(i));//.doOnNext((i) -> Log.d(TAG, "WifiEmitted: " + i.toString()));
    }

    @Override
    public Observable<InfluencesPack> getInfluenceStream()
    {
        return wifiInfluence;
    }

    public void start()
    {
        //((Subject<InfluencesPack>)wifiInfluence).onNext(new InflPack());
        wifi.start();
    }

    @Override
    public void stop()
    {
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
