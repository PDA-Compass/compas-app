package net.afterday.compas.engine.engine.threading;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.Executors;

public class Threads
{
    private static Scheduler computation;

    public static Scheduler computation()
    {
        if(computation == null)
        {
            computation = Schedulers.from(Executors.newSingleThreadExecutor());
        }
        return computation;
    }
}
