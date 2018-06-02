package net.afterday.compass.core;

import net.afterday.compass.core.gameState.Frame;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.core.inventory.Inventory;
import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.core.player.Player;

/**
 * Created by Justas Spakauskas on 2/3/2018.
 */

public interface Game
{
    Frame start();
    ////////////////////////////////////
    Frame acceptInfluences(InfluencesPack influences);
    Player getPlayer();
    Inventory getInventory();
    boolean addItem(String code);
    Frame useItem(Item item);
}
