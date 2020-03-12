package net.afterday.compas.engine.core.player

import net.afterday.compas.engine.core.influences.Influence
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class PlayerPropsImplTest {
    fun getPlayersProps():PlayerProps{
        val props = PlayerPropsImpl(Player.STATE.ALIVE)
        props.addHealth(100.0);
        props.radiation = 0.0;
        props.fraction = Player.FRACTION.STALKER
        return props
    }

    @Test
    fun `Player Init`(){
        val props = getPlayersProps();
        assertEquals(100.0, props.health)
        assertEquals(0.0, props.radiation)
        assertEquals(Player.FRACTION.STALKER,  props.fraction)
    }

    @Test
    fun `Health`(){
        val props = getPlayersProps();
        assertEquals(100.0, props.health)
        props.changeHealth(50.0, Influence.ANOMALY)
        assertEquals(50.0, props.health)
        props.changeHealth(50.0, Influence.ANOMALY)
        assertEquals(0.0, props.health)

        assertNotEquals(Player.STATE.ALIVE, props.state)
        assertEquals(Player.STATE.W_DEAD_ANOMALY, props.state)
    }
}