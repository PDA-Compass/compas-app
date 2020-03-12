package net.afterday.compas.engine.sensors;

import io.reactivex.Observable;

public interface Sensor<T>
{
    void start();
    void stop();
    Observable<T> getSensorResultsStream();
}
