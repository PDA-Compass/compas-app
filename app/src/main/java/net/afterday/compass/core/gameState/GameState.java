package net.afterday.compass.core.gameState;

import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.core.player.Player;

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
