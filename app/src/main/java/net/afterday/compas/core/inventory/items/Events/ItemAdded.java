package net.afterday.compas.core.inventory.items.Events;

import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.XpChanged;

/**
 * Created by spaka on 5/22/2018.
 */

public interface ItemAdded extends XpChanged
{
    Item getItem();
}
