package net.afterday.compas.engine.influences.WifiInfluences;

import android.net.wifi.ScanResult;
import android.util.Log;

import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.engine.influences.InflPack;
import net.afterday.compas.engine.influences.InfluenceExtractionStrategy;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByFirstLetterExtractionStrategy extends AbstractWifiExtractor implements InfluenceExtractionStrategy<List<ScanResult>, InfluencesPack>
{
    @Override
    public InfluencesPack makeInfluences(List<ScanResult> i)
    {
        InfluencesPack ip = new InflPack();
        Pattern regex = Pattern.compile("(.*?)(R|A|M|B|C|H|F|Z)");
//        Pattern regex = Pattern.compile("(?i)(.*?)(R|A|M|B|C|H|F|Z)");
        for(ScanResult sr : i)
        {
            Matcher matcher = regex.matcher(sr.SSID);
            if(matcher.find())
            {
                String n = matcher.group(2);
                if(types.containsKey(n))
                {
                    int tId = types.get(n);
                    ip.addInfluence(tId, WifiConverter.convert(tId, sr.level) * 1);
                }
            }
        }
        return ip;
    }

    @Override
    boolean isValid(ScanResult scanResult)
    {
        return true;
    }
}
