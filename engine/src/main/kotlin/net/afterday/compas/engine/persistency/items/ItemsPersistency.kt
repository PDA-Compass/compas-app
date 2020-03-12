package net.afterday.compas.engine.persistency.items

import net.afterday.compas.engine.core.inventory.items.ItemDescriptor

interface ItemsPersistency {
    fun getItemsByCode(): Map<String, ItemDescriptor>?
    fun getItemForCode(code: String): ItemDescriptor?
    fun getItemsAddeWithLevel(): Map<Int, List<ItemDescriptor>>?
}