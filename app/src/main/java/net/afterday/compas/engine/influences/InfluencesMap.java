package net.afterday.compas.engine.influences;

import net.afterday.compas.persistency.Source;

import java.util.Map;

/**
 * Created by spaka on 2/3/2018.
 */

public class InfluencesMap implements Source
{
    private Map<String, InfluenceMapper> map;
    public InfluencesMap(Map<String, InfluenceMapper> map)
    {
        this.map = map;
    }

    public Map<String, InfluenceMapper> getMap()
    {
        return this.map;
    }
}
