package net.afterday.compas.engine.influences.GpsInfluences;

import net.afterday.compas.sensors.Gps.Gps;

import io.reactivex.Observable;

/**
 * Created by spaka on 6/7/2018.
 */

public class GpsInfluenceProviderImpl implements GpsInfluenceProvider
{
    private Gps gps;
    public GpsInfluenceProviderImpl(Gps gps)
    {
        this.gps = gps;
    }

    @Override
    public Observable<Integer> getInfluenceStream()
    {
        return gps.getSensorResultsStream();
    }

    @Override
    public void start()
    {
        gps.start();
    }

    @Override
    public void stop()
    {
        gps.stop();
    }
}
