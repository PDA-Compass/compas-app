package net.afterday.compas.engine.engine.influences;

import net.afterday.compas.engine.sensors.SensorResult;

public interface InfluencesController extends InfluenceProvider<SensorResult>
{
    void start(int type);
    void stop(int type);
    void startEmission();
    void stopEmission();
}
