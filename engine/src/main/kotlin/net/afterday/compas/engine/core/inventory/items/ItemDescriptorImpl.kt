package net.afterday.compas.engine.core.inventory.items
import net.afterday.compas.engine.core.influences.Influence

class ItemDescriptorImpl(override val category: Item.CATEGORY) : ItemDescriptor {
    override var image: Int = 0
    override var name: String = ""
    override var nameId: Int = 0
    override var description: String = ""
    override var descriptionId: Int = 0
    override var isBooster: Boolean = false
    override var isDevice: Boolean = false
    override var isArmor: Boolean = false
    override var isArtefact: Boolean = false
    override var isSingleUse: Boolean = false
    override var isUsable: Boolean = false
    override var isDroppable: Boolean = false
    override var isConsumable: Boolean = false
    override var duration: Long = 0
    override var modifiers: DoubleArray = DoubleArray(Influence.INFLUENCE_COUNT)
    override var xpPoints: Int = 0

    init {
        for (i in 0 until Influence.INFLUENCE_COUNT) {
            this.modifiers[i] = ItemDescriptorConst.NULL_MODIFIER
        }
    }
}