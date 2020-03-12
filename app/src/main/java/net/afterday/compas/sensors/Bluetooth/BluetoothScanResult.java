package net.afterday.compas.sensors.Bluetooth;

/**
 * Created by spaka on 4/3/2018.
 */

public interface BluetoothScanResult
{
    String getName();
    int getStrength();
    long getScanTime();
}
