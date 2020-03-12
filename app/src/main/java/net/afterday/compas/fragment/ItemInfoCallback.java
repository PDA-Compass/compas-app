package net.afterday.compas.fragment;

import net.afterday.compas.core.inventory.items.Item;

public interface ItemInfoCallback {
    void onItemInfoClosed(Item item);
    void onItemUsed(Item item);
    void onItemDropped(Item item);
}
