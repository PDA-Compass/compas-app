package net.afterday.compas.engine.core.player

import net.afterday.compas.engine.core.serialization.Serializer
import org.mockito.Mockito
import org.testng.annotations.Test

open class PlayerImplTest {
    fun getImpactsImpl():ImpactsImpl {
        val props = PlayerPropsImpl(Player.STATE.ALIVE)
        val serializerMock = Mockito.mock(Serializer::class.java)
        return ImpactsImpl(props, serializerMock)
    }


    @Test fun `Test`(){
        /*var impacts = getImpactsImpl()

        val itemDescriptor = ItemDescriptorImpl(Item.CATEGORY.ARTIFACTS)
        itemDescriptor.apply {
            isArtefact = true
            modifiers[Item.ANOMALY_MODIFIER] = 5.0;
        }
        val item = ItemImpl(itemDescriptor)*/
    }
}