package net.afterday.compass.core;

import net.afterday.compass.core.player.Player;

/**
 * Created by spaka on 5/5/2018.
 */

public interface CountDown
{
    void startCountDown(Player.STATE playerState);
    void stopCountDown();
}
