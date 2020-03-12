package net.afterday.compas.persistency.influences;

import net.afterday.compas.core.influences.Emission;
import net.afterday.compas.core.influences.Influence;

import java.util.List;

/**
 * Created by Justas Spakauskas on 2/10/2018.
 */

public interface InfluencesPersistency
{
    List<Influence> getPossibleInfluences();
    List<String> getRegisteredWifiModules();
    List<Emission> getEmissions();
}
