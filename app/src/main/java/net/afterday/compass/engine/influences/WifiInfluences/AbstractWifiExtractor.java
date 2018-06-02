package net.afterday.compass.engine.influences.WifiInfluences;

import android.net.wifi.ScanResult;
import android.support.v4.util.Pair;

import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.core.influences.Influences;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.engine.influences.InflPack;
import net.afterday.compass.logging.Log;

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
    private Pattern regex = Pattern.compile("^(R|A|M|B|C|H|F)(\\d+)");
    private static Map<String, String> names = new HashMap();
    private static Map<String, Integer> types = new HashMap();

    //TODO perkelti vardus i ifluence interface'a ir pasalinti influences!
    static {
        //android.util.Log.d(TAG, "Static block");
        names.put("R", Influences.RADIATION);
        names.put("H", Influences.HEALING);
        names.put("A", Influences.ANOMALY);
        names.put("M", Influences.MENTAL);
        names.put("B", Influences.BURER);
        names.put("C", Influences.CONTROLLER);
        names.put("F", Influences.ARTEFACT);

        types.put("R", Influence.RADIATION);
        types.put("H", Influence.HEALTH);
        types.put("A", Influence.ANOMALY);
        types.put("M", Influence.MENTAL);
        types.put("B", Influence.BURER);
        types.put("C", Influence.CONTROLLER);
        types.put("F", Influence.ARTEFACT);

    }

    abstract boolean isValid(ScanResult scanResult);

    protected InfluencesPack extract(List<ScanResult> scanResults)
    {
        //android.util.Log.d("Extract!!", scanResults + "");
        InflPack ip = new InflPack();
        for (ScanResult sr : scanResults)
        {
            if(isValid(sr))
            {
                //android.util.Log.d("Extract!! ---------", sr + "");
                Matcher matcher = regex.matcher(sr.SSID);
                double multiplier;
                while (matcher.find()) {
                    if(matcher.groupCount() < 2)
                    {
                        return null;
                    }
                    String n = matcher.group(1);

                    multiplier = Integer.parseInt(matcher.group(2));
                    multiplier = multiplier > 0 ? multiplier / 100 : 1;
                    if(types.containsKey(n))
                    {
                        int tId = types.get(n);
                        ip.addInfluence(tId, WifiConverter.convert(tId, sr.level) * multiplier);
                    }
                }
            }
        }
        return ip;
    }
}
