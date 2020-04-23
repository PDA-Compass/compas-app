package net.afterday.compas.engine.engine.influences.GpsInfluences;

import io.reactivex.rxjava3.core.Observable;
import net.afterday.compas.engine.persistency.influences.InfluencesPersistency;
import net.afterday.compas.engine.sensors.Gps.Gps;

public class GpsInfluenceProviderImpl implements GpsInfluenceProvider
{
    private Gps gps;
    public GpsInfluenceProviderImpl(Gps gps,  InfluencesPersistency ip)
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
