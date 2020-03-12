package net.afterday.compas.engine.influences;

import net.afterday.compas.core.influences.InfluencesPack;

/**
 * Created by spaka on 4/2/2018.
 */

public interface InfluenceExtractionStrategy <T, I>
{
    I makeInfluences(T i);
}
