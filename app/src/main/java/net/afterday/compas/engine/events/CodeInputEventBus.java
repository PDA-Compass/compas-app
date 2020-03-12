package net.afterday.compas.engine.events;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by spaka on 6/12/2018.
 */

public class CodeInputEventBus
{
    private static final Subject<String> codeScans = PublishSubject.create();
    private static final Subject<Boolean> codeAccepts = PublishSubject.create();

    public static Observable<String> getCodeScans()
    {
        return codeScans;
    }

    public static void codeScanned(String code)
    {
        codeScans.onNext(code);
    }

    public static void codeAccepted(boolean accepted)
    {
        codeAccepts.onNext(accepted);
    }
}