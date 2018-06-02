package net.afterday.compass.persistency.influences;

import net.afterday.compass.core.influences.Influence;

import java.util.List;

/**
 * Created by Justas Spakauskas on 2/10/2018.
 */

public interface InfluencesPersistency
{
    List<Influence> getPossibleInfluences();
    List<String> getRegisteredWifiModules();
}
