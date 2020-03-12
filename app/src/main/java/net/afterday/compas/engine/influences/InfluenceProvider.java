package net.afterday.compas.engine.influences;

import io.reactivex.Observable;

/**
 * Created by spaka on 4/2/2018.
 */

public interface InfluenceProvider<T>
{
    int WIFI = 1;
    int BLUETOOTH = 2;
    int GPS = 4;
    Observable<T> getInfluenceStream();
    void start();
    void stop();
}
