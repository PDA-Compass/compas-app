package net.afterday.compas.app.fragment;

import net.afterday.compas.engine.core.inventory.items.Item;

public interface ItemInfoCallback {
    void onItemInfoClosed(Item item);
    void onItemUsed(Item item);
    void onItemDropped(Item item);
}
