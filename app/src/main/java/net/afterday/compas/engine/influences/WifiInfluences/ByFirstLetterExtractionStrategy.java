package net.afterday.compas.engine.influences.WifiInfluences;

import android.net.wifi.ScanResult;
import android.util.Log;

import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.engine.influences.InflPack;
import net.afterday.compas.engine.influences.InfluenceExtractionStrategy;
import net.afterday.compas.sensors.SensorResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByFirstLetterExtractionStrategy extends AbstractWifiExtractor implements InfluenceExtractionStrategy<List<SensorResult>, InfluencesPack>
{
    @Override
    public InfluencesPack makeInfluences(List<SensorResult> i)
    {
        InfluencesPack ip = new InflPack();
        Pattern regex = Pattern.compile("(.*?)(R|A|M|B|C|H|F|Z|S|D|N)");
        for(SensorResult sr : i)
        {
            Matcher matcher = regex.matcher(sr.name);
            if(matcher.find())
            {
                String n = matcher.group(2);
                Log.e("sds","F0:"+n);
                if(types.containsKey(n))
                {
                    int tId = types.get(n);
                    Log.e("sds","F0:"+tId);
                    ip.addInfluence(tId, WifiConverter.convert(tId, sr.value) * 1);
                }
            }
        }
        return ip;
    }

    @Override
    boolean isValid(SensorResult scanResult)
    {
        return true;
    }
}
