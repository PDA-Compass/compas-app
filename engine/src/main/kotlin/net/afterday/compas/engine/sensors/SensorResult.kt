package net.afterday.compas.engine.sensors

data class SensorResult(
        val id: String,
        val name: String?,
        val value: Long,
        val time: Long
)