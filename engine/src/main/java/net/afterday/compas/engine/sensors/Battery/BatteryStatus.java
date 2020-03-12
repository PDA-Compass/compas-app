package net.afterday.compas.engine.sensors.Battery;

public interface BatteryStatus
{
    int getEnergyLevel();
    boolean isCharging();
}
