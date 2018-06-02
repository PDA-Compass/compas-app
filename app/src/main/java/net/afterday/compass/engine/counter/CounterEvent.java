package net.afterday.compass.engine.counter;

/**
 * Created by spaka on 5/5/2018.
 */

public interface CounterEvent
{
    Counter.STATE getState();
    long getTimeLeft();
}
