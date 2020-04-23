package net.afterday.compas.engine.engine

import net.afterday.compas.engine.core.serialization.Serializer
import org.mockito.Mockito
import org.testng.annotations.Test

open class EngineTest {

    @Test fun `simple engine test`() {
        val serializerMock = Mockito.mock(Serializer::class.java)

        val engine = Engine(serializerMock)
        engine.start()
    }
}