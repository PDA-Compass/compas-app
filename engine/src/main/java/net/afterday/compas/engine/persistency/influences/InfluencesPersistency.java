package net.afterday.compas.engine.persistency.influences;

import net.afterday.compas.engine.core.influences.Emission;
import net.afterday.compas.engine.core.influences.Influence;

import java.util.List;

public interface InfluencesPersistency
{
    List<Influence> getPossibleInfluences();
    List<String> getRegisteredWifiModules();
    List<Emission> getEmissions();
}
