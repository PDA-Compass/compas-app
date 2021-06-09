package net.afterday.compas.engine.influences.WifiInfluences;

import android.net.wifi.ScanResult;
import android.util.Log;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.engine.influences.InflPack;
import net.afterday.compas.engine.influences.InfluenceExtractionStrategy;
import net.afterday.compas.sensors.SensorResult;

import java.util.List;

/**
 * Created by spaka on 4/3/2018.
 */

public class ByMacExtractionStrategy extends AbstractWifiExtractor implements InfluenceExtractionStrategy<List<SensorResult>, InfluencesPack>
{
    private List<String> modules;

    public ByMacExtractionStrategy(List<String> modules)
    {
        this.modules = modules;
    }

    @Override
    public InfluencesPack makeInfluences(List<SensorResult> i)
    {
        return extract(i);
    }

    @Override
    boolean isValid(SensorResult scanResult)
    {
        return modules.contains(scanResult.id);
    }
}
