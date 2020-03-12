package net.afterday.compas.engine.engine.influences;

import net.afterday.compas.engine.core.influences.InfluencesPack;

public interface InfluencesController extends InfluenceProvider<InfluencesPack>
{
    void start(int type);
    void stop(int type);
    void startEmission();
    void stopEmission();
}
