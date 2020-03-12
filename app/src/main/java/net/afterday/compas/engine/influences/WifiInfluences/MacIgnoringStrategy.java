package net.afterday.compas.engine.influences.WifiInfluences;

import android.net.wifi.ScanResult;
import android.util.Log;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.engine.influences.InflPack;
import net.afterday.compas.engine.influences.InfluenceExtractionStrategy;

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
