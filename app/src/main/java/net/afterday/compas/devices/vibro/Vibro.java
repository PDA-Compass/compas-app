package net.afterday.compas.devices.vibro;

import net.afterday.compas.core.player.PlayerProps;

/**
 * Created by spaka on 7/2/2018.
 */

public interface Vibro
{
    void vibrateDamage(PlayerProps playerProps);
    void vibrateHit();
    void vibrateW();
    void vibrateDeath();
    void vibrateMessage();
    void vibrateAlarm();
    void vibrateTouch();
}
