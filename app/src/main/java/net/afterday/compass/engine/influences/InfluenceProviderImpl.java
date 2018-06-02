package net.afterday.compass.engine.influences;


import android.net.wifi.ScanResult;
import android.util.Log;

import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.engine.influences.BluetoothInfluences.BluetoothInfluenceProvider;
import net.afterday.compass.engine.influences.BluetoothInfluences.BluetoothInfluenceProviderImpl;
import net.afterday.compass.engine.influences.WifiInfluences.WiFiInfluenceProvider;
import net.afterday.compass.engine.influences.WifiInfluences.WifiInfluenceProviderImpl;
import net.afterday.compass.persistency.influences.InfluencesPersistency;
import net.afterday.compass.sensors.Sensor;
import net.afterday.compass.sensors.SensorsProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 2/3/2018.
 */

public class InfluenceProviderImpl implements InfluenceProvider<InfluencesPack>
{
    private Sensor<List<ScanResult>> wifiSensor;
    private Observable<InfluencesPack> influences = PublishSubject.create();
    private static final String TAG = "InfluenceProviderImpl";
//    public InfluenceProviderImpl(final WiFi wifi, final List<String> wifiInfls)
//    {
//        this.wifiSensor = wifi;
//        this.influences = wifiSensor.getSensorResultsStream().
//                <InfluencesPack>map((List<ScanResult> scans) ->{
//                    InflPack infls = new InflPack();
//                    for(ScanResult r : scans)
//                    {
//                        if(wifiInfls.contains(r.BSSID))
//                        {
//                            infls.addInfluence(new WifiInfluence(r));
//                        }
//                    }
//                    log(infls);
//                    return infls;
//                } );
//    }
    private InfluencesPersistency ip;
    private SensorsProvider sp;
    private WiFiInfluenceProvider wip;
    private BluetoothInfluenceProvider bip;
    private Observable<Double> blInfls;
    private Observable<InfluencesPack> wifiInfls;
    private CompositeDisposable cd = new CompositeDisposable();

    public InfluenceProviderImpl(final SensorsProvider sp, final InfluencesPersistency ip)
    {
        this.sp = sp;
        this.ip = ip;
        wip = new WifiInfluenceProviderImpl(sp.getWifiSensor(), ip);
        bip = new BluetoothInfluenceProviderImpl(sp.getBluetoothSensor(), ip);
        blInfls = bip.getInfluenceStream();
        wifiInfls = wip.getInfluenceStream();
        cd.add(
                Observable
                .<Double, InfluencesPack, InfluencesPack>combineLatest(blInfls, wifiInfls, (b, w) -> combineInfls(b, w))
                //.doOnNext((l) -> Log.d(TAG, "HIT"))
                .subscribe((inflP) -> {((Subject<InfluencesPack>)influences).onNext(inflP);})
        );
    }

    @Override
    public Observable<InfluencesPack> getInfluenceStream()
    {
        return this.influences;
    }

    @Override
    public void start()
    {
        bip.start();
        wip.start();
    }

    @Override
    public void stop()
    {
        bip.stop();
        wip.stop();
    }

//    private void log(InflPack inflPack)
//    {
//        List<Influence> infls = inflPack.getInfluences();
//        if(infls.size() < 1)
//        {
//            ////Log.e("NO INFLUENCES");
//        }else
//        {
//            String i = "";
//            for(Influence infl : infls)
//            {
//                i += infl.getName() + "\n";
//            }
//        }
//    }

    private InfluencesPack combineInfls(Double blStrength, InfluencesPack inflsb)
    {
        if(blStrength != null)
        {
            inflsb.addInfluence(Influence.ARTEFACT, blStrength);
        }
        return inflsb;
    }

}
