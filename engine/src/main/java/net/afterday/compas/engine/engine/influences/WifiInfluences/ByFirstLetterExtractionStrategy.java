package net.afterday.compas.engine.engine.influences.WifiInfluences;

import net.afterday.compas.engine.core.influences.InfluencesPack;
import net.afterday.compas.engine.engine.influences.InflPack;
import net.afterday.compas.engine.engine.influences.InfluenceExtractionStrategy;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: implement extract
/*public class ByFirstLetterExtractionStrategy extends AbstractWifiExtractor implements InfluenceExtractionStrategy<List<WifiScanResult>, InfluencesPack>
{
    @Override
    public InfluencesPack makeInfluences(List<WifiScanResult> i)
    {
        InfluencesPack ip = new InflPack();
        Pattern regex = Pattern.compile("(.*?)(R|A|M|B|C|H|F|Z)");
//        Pattern regex = Pattern.compile("(?i)(.*?)(R|A|M|B|C|H|F|Z)");
        for(WifiScanResult sr : i)
        {
            Matcher matcher = regex.matcher(sr.getBssid());
            if(matcher.find())
            {
                String n = matcher.group(2);
                if(types.containsKey(n))
                {
                    int tId = types.get(n);
                    ip.addInfluence(tId, WifiConverter.convert(tId, sr.getLevel()) * 1);
                }
            }
        }
        return ip;
    }

    @Override
    boolean isValid(WifiScanResult scanResult)
    {
        return true;
    }
}*/
