package net.afterday.compas.engine.engine.system.influence.anomaly

import java.util.*

class AnomalyEvent (
    val id: String,
    val type: Int,
    val value: Long, //TODO: need use byte
    val setting: Dictionary<String, String>?
)