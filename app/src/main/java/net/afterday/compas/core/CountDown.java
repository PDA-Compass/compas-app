package net.afterday.compas.core;

import net.afterday.compas.core.player.Player;

/**
 * Created by spaka on 5/5/2018.
 */

public interface CountDown
{
    void startCountDown(Player.STATE playerState);
    void stopCountDown();
}
