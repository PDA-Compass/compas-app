package net.afterday.compas.engine.sensors;

import net.afterday.compas.engine.sensors.Battery.Battery;
import net.afterday.compas.engine.sensors.Bluetooth.Bluetooth;
import net.afterday.compas.engine.sensors.Gps.Gps;
import net.afterday.compas.engine.sensors.WiFi.WiFi;

public interface SensorsProvider
{
    WiFi getWifiSensor();
    Battery getBatterySensor();
    Bluetooth getBluetoothSensor();
    Gps getGpsSensor();
}
