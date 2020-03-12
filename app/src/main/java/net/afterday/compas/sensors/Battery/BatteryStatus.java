package net.afterday.compas.sensors.Battery;

/**
 * Created by spaka on 7/8/2018.
 */

public interface BatteryStatus
{
    int getEnergyLevel();
    boolean isCharging();
}
