package net.afterday.compas.engine.devices.vibro;

import net.afterday.compas.engine.core.player.PlayerProps;

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
