package net.afterday.compas.engine.engine.influences.WifiInfluences;

import net.afterday.compas.engine.core.influences.InfluencesPack;
import net.afterday.compas.engine.engine.influences.InfluenceExtractionStrategy;
import net.afterday.compas.engine.sensors.WiFi.WifiScanResult;

import java.util.List;

public class ByMacExtractionStrategy extends AbstractWifiExtractor implements InfluenceExtractionStrategy<List<WifiScanResult>, InfluencesPack>
{
    private List<String> modules;

    public ByMacExtractionStrategy(List<String> modules)
    {
        this.modules = modules;
    }

    @Override
    public InfluencesPack makeInfluences(List<WifiScanResult> i)
    {
        return extract(i);
    }

    @Override
    boolean isValid(WifiScanResult scanResult)
    {
        return modules.contains(scanResult.getBssid());
    }
}
