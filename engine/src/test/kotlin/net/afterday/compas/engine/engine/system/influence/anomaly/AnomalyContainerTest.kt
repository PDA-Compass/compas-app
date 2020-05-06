package net.afterday.compas.engine.engine.system.influence.anomaly

import org.testng.Assert
import org.testng.annotations.Test

open class AnomalyContainerTest {

    @Test fun `Container get letter map `(){
        val container = AnomalyContainer()

        class AnomalyTest : AnomalyHandler() {
            override val code = "01"
            override val letter = "b"
            override val name = "anom"

            override fun handle(anomaly: AnomalyEvent) {
                TODO("Not yet implemented")
            }
        }
        class AnomalyTest1 : AnomalyHandler() {
            override val code = "02"
            override val letter = "t"
            override val name = "anom1"

            override fun handle(anomaly: AnomalyEvent) {
                TODO("Not yet implemented")
            }
        }

        container.registerHandler(AnomalyTest())
        container.registerHandler(AnomalyTest1())

        val map = container.getLetterMap()
        Assert.assertEquals(map.size, 2)
        Assert.assertEquals(map["b"], 0)
        Assert.assertEquals(map["t"], 1)
    }

    @Test fun `Container`(){

    }

}