package net.afterday.compas.core;

import net.afterday.compas.core.gameState.Frame;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.core.inventory.Inventory;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Player;

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
    boolean acceptCode(String code);
    Frame useItem(Item item);
}
