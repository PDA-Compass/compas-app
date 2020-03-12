package net.afterday.compas.engine.engine.influences.WifiInfluences;

import net.afterday.compas.engine.core.influences.InfluencesPack;
import net.afterday.compas.engine.engine.influences.InfluenceExtractionStrategy;
import net.afterday.compas.engine.sensors.WiFi.WifiScanResult;
import java.util.List;

public class MacIgnoringStrategy extends AbstractWifiExtractor implements InfluenceExtractionStrategy<List<WifiScanResult>, InfluencesPack>
{

    @Override
    boolean isValid(WifiScanResult scanResult)
    {
        return true;
    }

    @Override
    public InfluencesPack makeInfluences(List<WifiScanResult> i)
    {
        return extract(i);
    }
}
