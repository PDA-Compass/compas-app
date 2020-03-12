package net.afterday.compas.sensors.Bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by spaka on 4/3/2018.
 */

public class BluetoothScanResultImpl implements BluetoothScanResult
{
    private final String address;
    private final int strength;
    private final long time;

    public BluetoothScanResultImpl(String address, int strength, long time)
    {
        this.address = address;
        this.strength = strength;
        this.time = time;

    }
    @Override
    public String getName()
    {
        return address;
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
