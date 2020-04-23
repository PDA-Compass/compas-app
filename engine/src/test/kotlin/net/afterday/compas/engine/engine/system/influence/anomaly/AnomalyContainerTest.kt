package net.afterday.compas.engine.engine.system.influence.anomaly

import org.testng.Assert
import org.testng.annotations.Test

open class AnomalyContainerTest {

    @Test fun `Container get letter map `(){
        val container = AnomalyContainer()

        class AnomalyTest : AnomalyHandler() {
            override fun getLetter(): String {
                return "b"
            }

            override fun handle(anomaly: AnomalyEvent) {
                TODO("Not yet implemented")
            }
        }
        class AnomalyTest1 : AnomalyHandler() {
            override fun getLetter(): String {
                return "t"
            }

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