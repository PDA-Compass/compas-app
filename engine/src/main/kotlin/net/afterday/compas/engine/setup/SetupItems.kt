package net.afterday.compas.engine.setup

import net.afterday.compas.engine.core.inventory.items.Item
import net.afterday.compas.engine.core.inventory.items.ItemDescriptor
import net.afterday.compas.engine.core.inventory.items.ItemDescriptorImpl

abstract class SetupItems() {
    val items: HashMap<String, ItemDescriptor> = HashMap()
    init {
        this.setup()
    }

    abstract fun setup(): Unit

    protected infix fun String.`is`(item: ItemDescriptorImpl) {
        items[this] = item
    }

    fun Item.CATEGORY.item(block: ItemDescriptorImpl.()->Unit): ItemDescriptorImpl{
        return ItemDescriptorImpl(this).apply(block)
    }
}