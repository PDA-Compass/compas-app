package net.afterday.compas.engine.sensors;

import io.reactivex.rxjava3.core.Observable;

public interface Sensor<T>
{
    void start();
    void stop();
    Observable<T> getSensorResultsStream();
}
