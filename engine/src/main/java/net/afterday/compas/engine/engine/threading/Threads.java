package net.afterday.compas.engine.engine.threading;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

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
