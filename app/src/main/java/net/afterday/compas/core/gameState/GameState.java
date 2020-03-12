package net.afterday.compas.core.gameState;

import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Player;

import java.util.List;

/**
 * Created by Justas Spakauskas on 3/5/2018.
 */

public interface GameState
{
    Player getPlayer();
    State getState();
    List<Item> getItems();
}
