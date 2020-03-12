package net.afterday.compas.engine.core.inventory.items.Events;

import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.player.XpChanged;

public interface ItemAdded extends XpChanged
{
    Item getItem();
}
