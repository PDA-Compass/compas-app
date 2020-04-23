package net.afterday.compas.engine

import net.afterday.compas.engine.persistency.PersistencyProvider
import net.afterday.compas.engine.sensors.SensorsProvider

object Services {
    lateinit var sensorsProvider: SensorsProvider
    lateinit var persistencyProvider: PersistencyProvider
}