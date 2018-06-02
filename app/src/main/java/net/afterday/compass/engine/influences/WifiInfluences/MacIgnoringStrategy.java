package net.afterday.compass.engine.influences.WifiInfluences;

import android.net.wifi.ScanResult;
import android.util.Log;

import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.engine.influences.InflPack;
import net.afterday.compass.engine.influences.InfluenceExtractionStrategy;

import java.util.List;

/**
 * Created by spaka on 4/2/2018.
 */

public class MacIgnoringStrategy extends AbstractWifiExtractor implements InfluenceExtractionStrategy<List<ScanResult>, InfluencesPack>
{

    @Override
    boolean isValid(ScanResult scanResult)
    {
        return true;
    }

    @Override
    public InfluencesPack makeInfluences(List<ScanResult> i)
    {
        return extract(i);
    }
}
