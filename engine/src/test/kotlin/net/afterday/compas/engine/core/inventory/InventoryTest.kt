package net.afterday.compas.engine.core.inventory

import net.afterday.compas.engine.core.inventory.items.Item
import net.afterday.compas.engine.core.serialization.Serializer
import net.afterday.compas.engine.core.inventory.items.ItemDescriptor
import net.afterday.compas.engine.core.inventory.items.ItemDescriptorImpl
import net.afterday.compas.engine.persistency.items.ItemsPersistency
import org.mockito.Mockito.*
import org.testng.annotations.Test
import kotlin.test.assertEquals

open class InventoryTest {
    private fun getInventiry(): Inventory {
        val itemsPersistencyMock = mock(ItemsPersistency::class.java)
        val serializerMock = mock(Serializer::class.java)
        return InventoryImpl(itemsPersistencyMock, serializerMock)
    }

    private fun getArtefact(): ItemDescriptor {
        return ItemDescriptorImpl(Item.CATEGORY.ARTIFACTS).apply {
            name = "test1"
            isArtefact = true
        }
    }

    private fun getArmor(): ItemDescriptor {
        return ItemDescriptorImpl(Item.CATEGORY.ARTIFACTS).apply {
            name = "test1"
            isArmor = true
        };
    }

    @Test fun `add items to inventory`() {
        val inventory = getInventiry()
        val maxArtefact = InventoryImpl.MAX_ARTIFACTS_COUNT;

        for (i in 1 .. maxArtefact ) {

            inventory.addItem(getArtefact(), "item1")
        }
        assertEquals(maxArtefact, inventory.items.size)

        // Add artefact most max
        inventory.addItem(getArtefact(), "item1")
        assertEquals(maxArtefact, inventory.items.size)

        // Add Armor
        inventory.addItem(getArmor(), "armor1")
        assertEquals(maxArtefact+1, inventory.items.size)
    }

    @Test fun `remove items from inventory`(){
        val inventory = getInventiry()
        val item = inventory.addItem(getArtefact(), "item1")
        inventory.addItem(getArtefact(), "item2")
        assert(inventory.items.size == 2)
        assert(inventory.items[0] == item)
        val result = inventory.dropItem(item)
        assertEquals(result, true)
        assert(inventory.items.size == 1)
        assert(!inventory.items.contains(item))

        assertEquals(inventory.dropItem(item), false)
    }

    @Test fun `Artefact effect`(){
        val inventory = getInventiry()
        val artefact = ItemDescriptorImpl(Item.CATEGORY.ARTIFACTS).apply {
            name = "artefact1"
            isArtefact = true
            modifiers[Item.HEALTH_MODIFIER] = 2.0
            modifiers[Item.RADIATION_EMMITER] = 1.5
            modifiers[Item.RADIATION_MODIFIER] = 1.2
        }
        val item = inventory.addItem(artefact, "item1")

        assertEquals(2.0, inventory.artifacts[Item.HEALTH_MODIFIER])
        assertEquals(1.5, inventory.artifacts[Item.RADIATION_EMMITER])
        assertEquals(1.2, inventory.artifacts[Item.RADIATION_MODIFIER])

        inventory.addItem(artefact, "item2")

        assertEquals(2.0*2.0, inventory.artifacts[Item.HEALTH_MODIFIER])
        assertEquals(1.5+1.5, inventory.artifacts[Item.RADIATION_EMMITER])
        assertEquals(1.2*1.2, inventory.artifacts[Item.RADIATION_MODIFIER])

        inventory.dropItem(item)

        assertEquals(2.0, inventory.artifacts[Item.HEALTH_MODIFIER])
        assertEquals(1.5, inventory.artifacts[Item.RADIATION_EMMITER])
        assertEquals(1.2, inventory.artifacts[Item.RADIATION_MODIFIER])
    }
}