package net.afterday.compass.engine.events;


import net.afterday.compass.logging.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by spaka on 5/23/2018.
 */

public class EmissionEventBus
{
    private Disposable waitingForEmission;
    public static final int WARN_BEFORE = 4 * 60;
    private static EmissionEventBus instance;
    private static final Subject<Boolean> emissionRunning = BehaviorSubject.createDefault(false);
    private static final Subject<Integer> emissionCountDown = BehaviorSubject.createDefault(-1);
    public static EmissionEventBus instance()
    {
        if(instance == null)
        {
            instance = new EmissionEventBus();
        }
        return instance;
    }

    public Observable<Boolean> getEmissionStateStream()
    {
        return emissionRunning;
    }

    public Observable<Integer> getEmissionWarninTimer()
    {
        return emissionCountDown;
    }

    public void startEmissionAfter(long seconds)
    {
        Observable.timer(seconds - WARN_BEFORE, TimeUnit.SECONDS).take(1).subscribe((t) -> startEmissionWarning());
    }

    private void startEmissionWarning()
    {
        Log.d("Emission is near");
    }
}
