package net.afterday.compas.engine.engine.influences;

import io.reactivex.rxjava3.core.Observable;

public interface InfluenceProvider<T>
{
    int WIFI = 1;
    int BLUETOOTH = 2;
    int GPS = 4;
    Observable<T> getInfluenceStream();
    void start();
    void stop();
}
