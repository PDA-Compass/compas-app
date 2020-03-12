package net.afterday.compas.stalker.setup

import net.afterday.compas.engine.core.inventory.items.Item.CATEGORY.*
import net.afterday.compas.engine.core.inventory.items.Item
import net.afterday.compas.engine.setup.SetupItems

open class SetupItemsStalker : SetupItems() {
    override fun setup() {
        "Bange" `is` MEDKITS.item {
            description = "Bange"
            isArmor = false
            isArtefact = false
            modifiers[Item.HEALTH_MODIFIER] = 10.0
        }

        "Medusa" `is` ARTIFACTS.item {
            description = "Medusa"
            isArmor = false
            isArtefact = true
        }
    }
}



