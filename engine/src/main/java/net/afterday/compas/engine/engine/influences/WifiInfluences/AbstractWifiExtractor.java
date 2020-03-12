package net.afterday.compas.engine.engine.influences.WifiInfluences;

import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.influences.InfluencesPack;
import net.afterday.compas.engine.engine.influences.InflPack;
import net.afterday.compas.engine.sensors.WiFi.WifiScanResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractWifiExtractor
{
    private Pattern regex = Pattern.compile("(.*?([RAMBCHFZ])((\\d+)))");

    protected static Map<String, Integer> types = new HashMap<>();

    static {
        //android.util.Log.d(TAG, "Static block");

        types.put("R", Influence.RADIATION);
        types.put("H", Influence.HEALTH);
        types.put("A", Influence.ANOMALY);
        types.put("M", Influence.MENTAL);
        types.put("B", Influence.BURER);
        types.put("C", Influence.CONTROLLER);
        types.put("F", Influence.ARTEFACT);
        types.put("Z", Influence.MONOLITH);

    }

    abstract boolean isValid(WifiScanResult scanResult);

    protected InfluencesPack extract(List<WifiScanResult> scanResults)
    {
        //android.util.Log.d("Extract!!", scanResults + "");
        InflPack ip = new InflPack();
        for (WifiScanResult sr : scanResults)
        {
            if(isValid(sr))
            {
                //android.util.Log.d("Extract!! ---------", sr + "");
                Matcher matcher = regex.matcher(sr.getSsid());
                while (matcher.find()) {
                    if(matcher.groupCount() < 4)
                    {
                        break;
                    }
                    String n = matcher.group(2);
                    if(!types.containsKey(n))
                    {
                        continue;
                    }
                    int number;
                    try
                    {
                        number = Integer.parseInt(matcher.group(3));
                    }catch (Exception e)
                    {
                        continue;
                    }
                    int tId = types.get(n);
                    if(tId == Influence.HEALTH && Math.abs(sr.getLevel()) > number)
                    {
                        continue;
                    }
                    double multiplier = number > 0 ? number / 100d : 1d;
                    ip.addInfluence(tId, WifiConverter.convert(tId, sr.getLevel()) * multiplier);
                }
            }
        }
        return ip;
    }
}
