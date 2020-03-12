package net.afterday.compas.devices;

import net.afterday.compas.devices.sound.Sound;
import net.afterday.compas.devices.vibro.Vibro;

/**
 * Created by spaka on 4/18/2018.
 */

public interface DeviceProvider
{
    Sound getSoundPlayer();
    Vibro getVibrator();
}
