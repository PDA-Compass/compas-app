package net.afterday.compas.persistency.player;

import net.afterday.compas.core.player.Player;

/**
 * Created by spaka on 6/14/2018.
 */

public interface PlayerPersistency
{
    Player.FRACTION getFractionByCode(String code);
    Player.COMMAND getCommandByCode(String code);
}
