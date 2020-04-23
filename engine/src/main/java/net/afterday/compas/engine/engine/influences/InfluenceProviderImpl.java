package net.afterday.compas.engine.engine.influences;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.influences.InfluencesPack;

import net.afterday.compas.engine.engine.influences.BluetoothInfluences.BluetoothInfluenceProvider;
import net.afterday.compas.engine.engine.influences.BluetoothInfluences.BluetoothInfluenceProviderImpl;
import net.afterday.compas.engine.engine.influences.GpsInfluences.GpsInfluenceProvider;
import net.afterday.compas.engine.engine.influences.GpsInfluences.GpsInfluenceProviderImpl;
import net.afterday.compas.engine.engine.influences.WifiInfluences.WiFiInfluenceProvider;
import net.afterday.compas.engine.engine.influences.WifiInfluences.WifiInfluenceProviderImpl;
import net.afterday.compas.engine.persistency.influences.InfluencesPersistency;
import net.afterday.compas.engine.sensors.Sensor;
import net.afterday.compas.engine.sensors.SensorResult;
import net.afterday.compas.engine.sensors.SensorsProvider;
import java.util.List;

public class InfluenceProviderImpl implements InfluencesController
{
    private Observable<SensorResult> influences = PublishSubject.create();
    private static final String TAG = "InfluenceProviderImpl";
    private Subject<Boolean> emissionRunning = PublishSubject.create();
    private InfluencesPersistency ip;
    private SensorsProvider sp;
    private WiFiInfluenceProvider wip;
    private BluetoothInfluenceProvider bip;
    private GpsInfluenceProvider gip;

    private Observable<SensorResult> blInfls;
    private Observable<InfluencesPack> wifiInfls;
    private Observable<Integer> gpsInfls;
    private CompositeDisposable cd = new CompositeDisposable();
    private Subject<Integer> influencesState = BehaviorSubject.createDefault(0);
    private boolean emission = false;
    private Subject<SensorResult> sensorStream = BehaviorSubject.create();

    public InfluenceProviderImpl(final SensorsProvider sp, final InfluencesPersistency ip, Observable<Long> ticks)
    {
        this.sp = sp;
        this.ip = ip;
        wip = new WifiInfluenceProviderImpl(sp.getWifiSensor(), ip);
        bip = new BluetoothInfluenceProviderImpl(sp.getBluetoothSensor(), ip);

        gip = new GpsInfluenceProviderImpl(sp.getGpsSensor(), ip);

        blInfls = bip.getInfluenceStream();
        wifiInfls = wip.getInfluenceStream();
        gpsInfls = gip.getInfluenceStream();

//        Observable<Integer> state = influencesState.filter((is) -> is != 0);
//        Observable<Long> ticks = Observable.interval(Engine.TICK_MILLISECONDS, TimeUnit.MILLISECONDS);
//        Observable<Integer> running = Observable.combineLatest(state, ticks, (r, t) -> r);
//        running.
        //ticks.withLatestFrom(wifiInfls, blInfls, gpsInfls, (t, w, b, g) -> combineInfls(w, b, g)).subscribe((inflP) -> ((Subject<InfluencesPack>)influences).onNext(inflP));
//        cd.add(
//                Observable
//                .<Double, InfluencesPack, Integer, InfluencesPack>combineLatest(blInfls, wifiInfls, gpsInfls (b, w) -> combineInfls(b, w))
//                //.doOnNext((l) -> Log.d(TAG, "HIT"))
//                .subscribe((inflP) -> {((Subject<InfluencesPack>)influences).onNext(inflP);})
//        );
    }

    @Override
    public Observable<SensorResult> getInfluenceStream()
    {
        return this.influences;
    }

    @Override
    public void start()
    {
        bip.start();
        wip.start();
//        gip.start();
    }

    @Override
    public void stop()
    {
        bip.stop();
        wip.stop();
//        gip.stop();
    }

    @Override
    public void start(int level)
    {
        wip.start();
        bip.start();
        if(level >= 5)
        {
            bip.start();
        }
//        if((type & WIFI) == WIFI)
//        {
//            wip.start();
//        }
//        if((type & BLUETOOTH) == BLUETOOTH)
//        {
//            bip.start();
//        }
//        if((type & GPS) == GPS)
//        {
//            gip.start();
//        }

    }

    @Override
    public void stop(int level)
    {
        wip.stop();
        bip.stop();
//        if((type & WIFI) == WIFI)
//        {
//            wip.stop();
//        }
//        if((type & BLUETOOTH) == BLUETOOTH)
//        {
//            bip.stop();
//        }
//        if((type & GPS) == GPS)
//        {
//            gip.stop();
//        }
    }

    @Override
    public void startEmission()
    {
        emission = true;
        gip.start();
    }

    @Override
    public void stopEmission()
    {
        emission = false;
        gip.stop();
    }

    private InfluencesPack combineInfls(InfluencesPack infls, Double blStrength, Integer satelites)
    {
        if(blStrength != null)
        {
            infls.addInfluence(Influence.ARTEFACT, blStrength);
        }
        if(emission)
        {
            infls.setEmission(true);
            infls.addInfluence(Influence.EMISSION, satelites);
        }
        return infls;
    }
}
