package net.afterday.compas.engine.engine.influences.WifiInfluences;

import net.afterday.compas.engine.core.log.Log;
import net.afterday.compas.engine.sensors.WiFi.WifiScanResult;

public class WifiInfluenceImpl implements WifiInfluence
{
    private static final String TAG = "WiFi influence";
    private WifiScanResult scanResult;
    private String name = null;
    private int typeId = -1;
    private double multiplier = 1d;

    public WifiInfluenceImpl(WifiScanResult scanResult, String name, int type, double multiplier)
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
        return scanResult.getBssid();
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
        return WifiConverter.convert(getTypeId(), scanResult.getLevel()) * multiplier;
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
