package net.afterday.compas.engine.core.inventory.items

import net.afterday.compas.engine.core.inventory.items.Item.CATEGORY

object ItemDescriptorConst {
    const val NULL_MODIFIER = -99999999.0
}

interface ItemDescriptor {
    val image: Int
    val name: String
    val nameId: Int
    val description: String
    val descriptionId: Int

    val isBooster: Boolean
    val isDevice: Boolean
    val isArmor: Boolean
    val isArtefact: Boolean

    val isSingleUse: Boolean
    val isUsable: Boolean
    val isDroppable: Boolean

    val isConsumable: Boolean
    val duration: Long

    val modifiers: DoubleArray
    val xpPoints: Int

    val category: CATEGORY?
}