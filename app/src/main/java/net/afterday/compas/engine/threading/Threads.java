package net.afterday.compas.engine.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by spaka on 4/21/2018.
 */

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
