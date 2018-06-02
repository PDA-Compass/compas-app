package net.afterday.compass.engine.influences;

import net.afterday.compass.core.influences.InfluencesPack;

/**
 * Created by spaka on 4/2/2018.
 */

public interface InfluenceExtractionStrategy <T, I>
{
    I makeInfluences(T i);
}
