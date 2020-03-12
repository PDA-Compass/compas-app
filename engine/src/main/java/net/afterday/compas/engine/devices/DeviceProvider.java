package net.afterday.compas.engine.devices;

import net.afterday.compas.engine.devices.sound.Sound;
import net.afterday.compas.engine.devices.vibro.Vibro;

public interface DeviceProvider
{
    Sound getSoundPlayer();
    Vibro getVibrator();
}
