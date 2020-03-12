package net.afterday.compas.engine.influences.WifiInfluences;

import android.net.wifi.ScanResult;
import android.util.Log;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.Influences;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Justas Spakauskas on 4/2/2018.
 */

public class WifiInfluenceImpl implements WifiInfluence
{
    private static final String TAG = "WiFi influence";
    private ScanResult scanResult;
    private String name = null;
    private int typeId = -1;
    private double multiplier = 1d;

    public WifiInfluenceImpl(ScanResult scanResult, String name, int type, double multiplier)
    {
        Log.d(TAG, "TypeId = " + type);
        this.scanResult = scanResult;
        this.typeId = type;
        this.name = name;
        this.multiplier = multiplier;
        ////Log.d(TAG, "Influence created from wifi signal " + scanResult.level);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getId()
    {
        return scanResult.BSSID;
    }

    @Override
    public boolean affects(int what)
    {
        return false;
    }

    @Override
    public boolean isDanger()
    {
        return typeId != RADIATION && typeId != BURER && typeId != CONTROLLER && typeId != ANOMALY && typeId != MONOLITH;
    }

    @Override
    public long getTimestamp()
    {
        return 0;
    }

    public double getStrength()
    {
        return WifiConverter.convert(getTypeId(), scanResult.level) * multiplier;
    }

    @Override
    public int getTypeId()
    {
        return typeId;
    }

    @Override
    public String toString()
    {
        return "WiFiInfluence - name: " + getName() + "; strength: " + getStrength();
    }

}
