package net.afterday.compas.engine.core.gameState;

import net.afterday.compas.engine.core.player.Equipment;
import net.afterday.compas.engine.core.player.PlayerProps;

public interface Frame
{
    PlayerProps getPlayerProps();
    Equipment getEquipment();
}
