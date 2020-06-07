package net.afterday.compas.engine.engine.system.damage

import java.util.*

object DamageType {
    const val PHISICAL: Byte = 0
    const val FIRE: Byte = 1
    const val ELECTRIC: Byte = 2
    const val MENTAL: Byte = 3
    const val POISON: Byte = 4
    const val RADIATION: Byte = 5
    const val HEALTH: Byte = 6
    const val MONOLITH: Byte = 7
}

open class DamageEvent(
        val id: String,
        val type: Byte,
        val value: Float,
        val at: Long,
        val setting: Dictionary<String, String>?
)