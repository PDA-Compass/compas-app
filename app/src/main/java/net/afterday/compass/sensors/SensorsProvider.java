package net.afterday.compass.sensors;

import net.afterday.compass.sensors.Battery.Battery;
import net.afterday.compass.sensors.Bluetooth.Bluetooth;
import net.afterday.compass.sensors.WiFi.WiFi;

/**
 * Created by Justas Spakauskas on 2/10/2018.
 */

public interface SensorsProvider
{
    WiFi getWifiSensor();
    Battery getBatterySensor();
    Bluetooth getBluetoothSensor();
}
