package net.afterday.compas.engine.influences.BluetoothInfluences;

import android.support.v4.util.Pair;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.engine.influences.InflPack;
import net.afterday.compas.engine.influences.InfluenceExtractionStrategy;
import net.afterday.compas.persistency.influences.InfluencesPersistency;
import net.afterday.compas.sensors.Bluetooth.Bluetooth;
import net.afterday.compas.sensors.Bluetooth.BluetoothScanResult;
import net.afterday.compas.util.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 4/2/2018.
 */

public class BluetoothInfluenceProviderImpl implements BluetoothInfluenceProvider
{
    private static final int EMITTING_INTERVAL = 1000;
    private static final String TAG = "BluetoothInflProvider";
    private static final String RUNNING = "R";
    private static final String STOPPED = "S";
    private final Bluetooth bluetooth;
    private final Observable<Double> blScans = BehaviorSubject.createDefault(Influence.NULL);
    private static final InfluenceExtractionStrategy<List<BluetoothScanResult>, Double> extractionStrategy;
    private final Subject<String> providerState = BehaviorSubject.createDefault(STOPPED);
    private final Observable<Long> providerRunning;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    static {
        extractionStrategy = new BluetoothExtractionStrategy();
    }

    public BluetoothInfluenceProviderImpl(Bluetooth bluetooth, InfluencesPersistency ip)
    {
        //providerRunning = Observable.empty();
        providerRunning = providerState.switchMap((s) -> s == RUNNING ? Observable.interval(EMITTING_INTERVAL, TimeUnit.MILLISECONDS) : Observable.empty());
        providerState.filter((ps) -> ps == RUNNING)
                     .switchMap((ps) -> bluetooth.getSensorResultsStream())
                     //.buffer(providerRunning)
                     .observeOn(Schedulers.computation()).doOnNext((e) -> {
            android.util.Log.e(TAG, "AAAAAAAAAAAAA ---- " + e);
        })
                     //.map((sr) -> extractionStrategy.makeInfluences(sr))
                     .subscribe((i) -> ((Subject<Double>)blScans).onNext(i));
//        bluetooth.getSensorResultsStream().withLatestFrom(providerState, (res, ps) -> new Pair<Pair<String, Integer>, Boolean>(res, ps == RUNNING))
//                    .filter((o) -> o.second)
//                    .map()
//                    .observeOn(Schedulers.computation())
//                    .filter((p) -> p.first != null && p.second != null)
//                    .buffer(providerRunning)
//                   // .doOnNext((i) -> Log.d(TAG, "" + i))
//                    .map((i) -> {List<Pair<String, Integer>> lst = new ArrayList<>(); lst.add(i); return lst;})
//                    .map((sr) -> extractionStrategy.makeInfluences(sr))
//                    .subscribe((i) -> ((Subject<InfluencesPack>)blScans).onNext(i));
        this.bluetooth = bluetooth;

    }

    @Override
    public Observable<Double> getInfluenceStream()
    {
        return blScans;
    }

    public void start()
    {
        if(!isRunning.get())
        {
            isRunning.set(true);
            bluetooth.start();
            providerState.onNext(RUNNING);
        }
    }

    @Override
    public void stop()
    {
        isRunning.set(false);
        providerState.onNext(STOPPED);
        bluetooth.stop();
    }

    private static class BluetoothResultsScanner
    {
        //private List<Pair<String, Integer>> buffer;
        private Map<String, Pair<Integer, Long>> buffer;

        BluetoothResultsScanner()
        {

        }

        InfluencesPack scan(Pair<String, Integer> scanRes)
        {
            return null;
        }
    }
}
