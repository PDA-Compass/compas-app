package net.afterday.compas.sensors;

import net.afterday.compas.sensors.Battery.Battery;
import net.afterday.compas.sensors.Bluetooth.Bluetooth;
import net.afterday.compas.sensors.Gps.Gps;
import net.afterday.compas.sensors.WiFi.WiFi;

/**
 * Created by Justas Spakauskas on 2/10/2018.
 */

public interface SensorsProvider
{
    WiFi getWifiSensor();
    Battery getBatterySensor();
    Bluetooth getBluetoothSensor();
    Gps getGpsSensor();
}
