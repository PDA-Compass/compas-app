package net.afterday.compas.engine.engine.influences.WifiInfluences;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.influences.InfluencesPack;
import net.afterday.compas.engine.engine.influences.InflPack;
import net.afterday.compas.engine.engine.influences.InfluenceExtractionStrategy;
import net.afterday.compas.engine.persistency.influences.InfluencesPersistency;
import net.afterday.compas.engine.sensors.WiFi.WiFi;

import java.util.List;

public class WifiInfluenceProviderImpl implements WiFiInfluenceProvider
{
    private static final String TAG = "WifiInflProvider";
    private WiFi wifi;
    private InfluencesPersistency ip;
    //private Observable<List<WifiScanResult>> scanResults;
    private Observable<InfluencesPack> wifiInfluence = BehaviorSubject.createDefault(new InflPack());
    //private InfluenceExtractionStrategy<List<WifiScanResult>, InfluencesPack> ies;
    private boolean wasHealing = false;
    private int noHealing = 0;
    private double lastHealingStrength;
    private long lastHealingTime = 0;

    public WifiInfluenceProviderImpl(WiFi wifi, InfluencesPersistency ip)
    {
        this.wifi = wifi;
        this.ip = ip;
        //ies = new ByMacExtractionStrategy(ip.getRegisteredWifiModules());
        //ies = new MacIgnoringStrategy();

        //TODO: uncommented
        /*wifi.getSensorResultsStream()
                .observeOn(Schedulers.computation())
                .map(this.ies::makeInfluences)
                .map(this::verifyHealing)
                .subscribe((i) -> ((Subject<InfluencesPack>)wifiInfluence).onNext(i));
        */
        //.doOnNext((i) -> Log.d(TAG, "WifiEmitted: " + i.toString()));
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
