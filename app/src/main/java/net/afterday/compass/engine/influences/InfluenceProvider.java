package net.afterday.compass.engine.influences;

import io.reactivex.Observable;

/**
 * Created by spaka on 4/2/2018.
 */

public interface InfluenceProvider<T>
{
    Observable<T> getInfluenceStream();
    void start();
    void stop();
}
