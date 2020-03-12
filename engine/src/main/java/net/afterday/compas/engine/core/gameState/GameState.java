package net.afterday.compas.engine.core.gameState;

import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.player.Player;

import java.util.List;

public interface GameState
{
    Player getPlayer();
    State getState();
    List<Item> getItems();
}
