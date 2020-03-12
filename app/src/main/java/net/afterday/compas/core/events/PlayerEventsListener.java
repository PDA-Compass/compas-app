package net.afterday.compas.core.events;

import net.afterday.compas.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Impacts;
import net.afterday.compas.core.player.Player;

/**
 * Created by spaka on 5/8/2018.
 */

public interface PlayerEventsListener
{
    void onPlayerStateChanged(Player.STATE oldState, Player.STATE newState);
    void onImpactsStateChanged(Impacts.STATE oldState, Impacts.STATE newState);
    void onItemAdded(ItemAdded itemAdded);
    void onPlayerLevelChanged(int level);
    void onItemUsed(Item item);
    void onItemDropped(Item item);
    void onFractionChanged(Player.FRACTION newFraction, Player.FRACTION oldFraction);
}
