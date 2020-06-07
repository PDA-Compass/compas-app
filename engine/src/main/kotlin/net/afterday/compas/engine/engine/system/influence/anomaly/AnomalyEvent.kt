package net.afterday.compas.engine.engine.system.influence.anomaly

import java.util.*

class AnomalyEvent (
    val id: String,
    val type: Int,
    val value: Int, //TODO: need use byte
    val setting: Dictionary<String, String>?,
    val at: Long
)