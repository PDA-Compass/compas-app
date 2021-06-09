package net.afterday.compas.engine.influences.WifiInfluences;

import android.net.wifi.ScanResult;
import android.support.v4.util.Pair;
import android.util.Log;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.Influences;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.engine.influences.InflPack;
import net.afterday.compas.sensors.SensorResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sersec on 2018-04-04.
 */

public abstract class AbstractWifiExtractor
{
    private Pattern regex = Pattern.compile("(.*?([RAMBCHFZSDN])((\\d+)))");

    protected static Map<String, Integer> types = new HashMap();

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
        types.put("S", Influence.SHELTER);
        types.put("D", Influence.RADIOACTIVE);
        types.put("N", Influence.FORBIDDEN);

    }

    abstract boolean isValid(SensorResult scanResult);

    protected InfluencesPack extract(List<SensorResult> scanResults)
    {
        //android.util.Log.d("Extract!!", scanResults + "");
        InflPack ip = new InflPack();
        for (SensorResult sr : scanResults)
        {
            if(isValid(sr))
            {
                //android.util.Log.d("Extract!! ---------", sr + "");
                Matcher matcher = regex.matcher(sr.name);
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
                    if(tId == Influence.HEALTH && Math.abs(sr.value) > number)
                    {
                        continue;
                    }
                    double multiplier = number > 0 ? number / 100d : 1d;
                    ip.addInfluence(tId, WifiConverter.convert(tId, sr.value) * multiplier);
                }
            }
        }
        return ip;
    }
}
