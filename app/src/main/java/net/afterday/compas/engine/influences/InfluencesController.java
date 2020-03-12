package net.afterday.compas.engine.influences;

import net.afterday.compas.core.influences.InfluencesPack;

/**
 * Created by spaka on 6/9/2018.
 */

public interface InfluencesController extends InfluenceProvider<InfluencesPack>
{
    void start(int type);
    void stop(int type);
    void startEmission();
    void stopEmission();
}
