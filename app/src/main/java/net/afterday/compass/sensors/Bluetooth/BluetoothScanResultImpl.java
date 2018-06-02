package net.afterday.compass.sensors.Bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by spaka on 4/3/2018.
 */

public class BluetoothScanResultImpl implements BluetoothScanResult
{
    private final String name;
    private final int strength;
    private final long time;

    public BluetoothScanResultImpl(BluetoothDevice bd, int strength, long time)
    {
        this.name = bd.getName();
        this.strength = strength;
        this.time = time;

    }
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public int getStrength()
    {
        return strength;
    }

    @Override
    public long getScanTime()
    {
        return time;
    }
}
