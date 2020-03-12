package net.afterday.compas.engine.engine.influences;

import net.afterday.compas.engine.persistency.Source;
import java.util.Map;

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
