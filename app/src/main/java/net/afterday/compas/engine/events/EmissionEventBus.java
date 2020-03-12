package net.afterday.compas.engine.events;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by spaka on 5/23/2018.
 */

public class EmissionEventBus
{
    private Disposable waitingForEmission;
    public static final int WARN_BEFORE = 4 * 60;
    private static EmissionEventBus instance;
    private static final Subject<Boolean> emissionActive = BehaviorSubject.createDefault(false);
    private static final Subject<Integer> emissionWarning = PublishSubject.create();
    private static final Subject<Integer> fakeEmissions = PublishSubject.create();
    public static EmissionEventBus instance()
    {
        if(instance == null)
        {
            instance = new EmissionEventBus();
        }
        return instance;
    }

    public void setEmissionActive(boolean emissionActive)
    {
        EmissionEventBus.emissionActive.onNext(emissionActive);
    }

    public void emissionWillStart(int startsAfter)
    {
        EmissionEventBus.emissionWarning.onNext(startsAfter);
    }

    public void fakeEmission()
    {
        EmissionEventBus.fakeEmissions.onNext(1);
    }

    public Observable<Boolean> getEmissionStateStream()
    {
        return EmissionEventBus.emissionActive;
    }

    public Observable<Integer> getEmissionWarnings()
    {
        return EmissionEventBus.emissionWarning;
    }

    public Observable<Integer> getFakeEmissions(){return EmissionEventBus.fakeEmissions;}
}
