package net.afterday.compas.engine.engine.influences.GpsInfluences;

import net.afterday.compas.engine.sensors.Gps.Gps;

import io.reactivex.Observable;

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
