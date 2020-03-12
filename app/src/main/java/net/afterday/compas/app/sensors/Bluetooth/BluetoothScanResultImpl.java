package net.afterday.compas.app.sensors.Bluetooth;

import net.afterday.compas.engine.sensors.Bluetooth.BluetoothScanResult;

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
